package ch.psi.daq.archiverappliance.api;

import ch.psi.daq.archiverappliance.api.api.v1.config.ChannelConfiguration;
import ch.psi.daq.archiverappliance.api.data.ArchiverChannelConfiguration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@Service
public class ArchiverConfigurationManager {

    private static final Logger logger = LoggerFactory.getLogger(ArchiverConfigurationManager.class);

    private String serverName;
    private String fileNameChannelsCache;
    private String fileNameChannelsConfigCache;

    private ObjectMapper mapper;
    private WebClient webClient;

    public ArchiverConfigurationManager(@Value("${server.name}") String serverName,
                                        @Value("${filename.channels.cache}") String fileNameChannelsCache,
                                        @Value("${filename.channels.config.cache}") String fileNameChannelsConfigCache,
                                        ObjectMapper mapper) {
        this.fileNameChannelsCache = fileNameChannelsCache;
        this.fileNameChannelsConfigCache = fileNameChannelsConfigCache;

        this.serverName = serverName;
        this.mapper = mapper;

        // This documentation might be useful:
        // https://projectreactor.io/docs/netty/release/reference/index.html#_connection_pool
//        ConnectionProvider provider = ConnectionProvider.builder("fixed")
//                .maxConnections(1000)
//                .maxLifeTime(Duration.ofSeconds(10))
//                .build();

        TcpClient tcpClient = TcpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 120000)
                .doOnConnected(connection -> connection.addHandlerLast(new ReadTimeoutHandler(120000)))
                .doOnConnected(connection -> connection.addHandlerLast(new WriteTimeoutHandler(120000)));

        ReactorClientHttpConnector connector = new ReactorClientHttpConnector(HttpClient.from(tcpClient));

        this.webClient = WebClient.builder()
                .clientConnector(connector)
                .build();

    }

    /**
     * Get all channels from a local cache file of from the archiverappliance
     * @param reload    Reload channel list from the archiverappliance
     * @return  Flux of channel names
     */
    public Flux<String> getChannels(boolean reload) {
        if (reload) {
            fetchChannels().block();
        }
        Flux<String> channelsFlux;
        try {
            List<String> channels = mapper.readValue(Paths.get(fileNameChannelsCache).toFile(), new TypeReference<>() {
            });
            channelsFlux = Flux.fromStream(channels.stream());
        } catch (IOException e) {
            logger.error("Unable to read channel file");
            channelsFlux = Flux.fromStream(new ArrayList<String>().stream());
        }

        return channelsFlux;
    }

    /**
     * Get the channel list from the archiverappliance
     * @return Flux of channel names
     */
    public Flux<String> getChannelsFromArchiver() {
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1))
                .build();

        return WebClient.builder()
                .exchangeStrategies(exchangeStrategies) // we need to increase the buffer size for the archiver request
                .build()
                .get()
                .uri("http://" + serverName + ":17665/mgmt/bpl/getAllPVs?pv=*&limit=-1")
//                    .accept(MediaType.APPLICATION_JSON)
//                    .acceptCharset(Charset.forName("ISO-8859-1"))
                .retrieve()
//                    .bodyToMono(ChannelList.class)  // TODO This does not work for some reasons (Content type 'application/json;charset=ISO-8859-1' not supported)
                .bodyToMono(String.class)
                .map(v -> {
                    try {
                        List<String> list = mapper.readValue(v, new TypeReference<>() {
                        });
                        return list;
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .flux()
                .flatMap(l -> Flux.fromIterable(l))
                .cache();
    }

    /**
     * Fetch the channels from the archiverappliance and save them into a local cache file
     * @return  Mono indicating whether the fetch was successful
     */
    public Mono<Boolean> fetchChannels() {
        return getChannelsFromArchiver().collectList().map(channelList -> {
            try {
                mapper.writeValue(Paths.get(fileNameChannelsCache).toFile(), channelList);
            } catch (IOException e) {
                logger.error("Unable to write channels file", e);
                return false;
            }
            return true;
        });
    }

    /**
     * Get the configuration of all channels matching the specified regex pattern
     * from the archiverappliance
     *
     * @param regexPattern channel pattern
     * @return Flux of configurations
     */
    public Flux<ChannelConfiguration> getChannelConfigurations(String regexPattern) {
        Predicate<String> channelFilter = x -> true;
        if (regexPattern != ".*") {
            final Pattern pattern  = Pattern.compile(regexPattern, Pattern.CASE_INSENSITIVE);
            channelFilter = x -> pattern.matcher(x).find();
        }

        return getChannels(false)
                .filter(channelFilter)
//                .delayElements(Duration.ofMillis(1))
                .flatMap(channel -> getChannelConfigurationFromArchiver(channel))
//                .limitRequest(20) // For testing purposes limit the number of channel
                .filter(m -> m != null)
                .map(config -> mapArchiverChannelConfigurationToChannelConfiguration(config));
    }

    /**
     * Get the configuration of a single channel from the archiverappliance
     *
     * @param channelName
     * @return Configuration of the channel, null if configuration cannot be retrieved from the server
     */
    public Mono<ArchiverChannelConfiguration> getChannelConfigurationFromArchiver(String channelName) {
        Mono<ArchiverChannelConfiguration> configuration;

        configuration = webClient
                .get()
                .uri("http://" + serverName + ":17665/mgmt/bpl/getPVTypeInfo?pv={pv}", channelName)
                .retrieve()
//                .onStatus(httpStatus -> HttpStatus.NOT_FOUND.equals(httpStatus),
//                        clientResponse -> Mono.empty())
                .bodyToMono(String.class)
//                .onErrorResume(e -> Mono.empty())
                .onErrorResume(WebClientResponseException.class, ex -> ex.getRawStatusCode() == 404 ? Mono.empty() : Mono.error(ex))
                .map(v -> {
                    try {
                        return mapper.readValue(v, ArchiverChannelConfiguration.class);
                    } catch (JsonProcessingException e) {
                        logger.warn("Unable to parse return value vor channel: {} Error: {}", channelName, e.getMessage());
                    }
                    return ArchiverChannelConfiguration.EMPTY;
                })
                .filter(c -> c != ArchiverChannelConfiguration.EMPTY);
        return configuration;
    }

    /**
     * Map the channel configuration object returned by the archiverappliance to the
     * data API configuration object.
     * @param config
     * @return
     */
    private ChannelConfiguration mapArchiverChannelConfigurationToChannelConfiguration(ArchiverChannelConfiguration config) {
        ChannelConfiguration configuration = new ChannelConfiguration();
        configuration.setName(config.getName());
        configuration.setType(config.getType());
        configuration.setShape(config.getShape());
        configuration.setUnit(config.getUnit());
        configuration.setSource(config.getSource());
        return configuration;
    }

    /**
     * Fetch channel configurations from the archiverappliance and write them into a
     * local cache file
     *
     * @return Mono indicating whether the fetch was successful
     */
    public Mono<Boolean> fetchChannelConfigurations() {
        return getChannelConfigurations(".*").collectList().map(channelList -> {
            try {
                mapper.writeValue(Paths.get(fileNameChannelsConfigCache).toFile(), channelList);
            } catch (IOException e) {
                logger.error("Unable to write channels configuration file", e);
                return false;
            }
            return true;
        });
    }
}
