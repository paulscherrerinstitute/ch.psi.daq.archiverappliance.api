package ch.psi.daq.archiverappliance.api;

import ch.psi.daq.archiverappliance.api.data.ArchiverQueryResult;
import ch.psi.daq.archiverappliance.api.data.ArchiverQueryResultEvent;
import ch.psi.daq.archiverappliance.api.data.ArchiverQueryResultList;
import ch.psi.daq.archiverappliance.api.data.ArchiverQueryResultMeta;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ArchiverQueryManager {

    private static final Logger logger = LoggerFactory.getLogger(ArchiverQueryManager.class);

    private String queryUrl;
    private ObjectMapper objectMapper;

    public ArchiverQueryManager(@Value("server.name") String serverName, ObjectMapper objectMapper){
        this.queryUrl = "http://"+serverName+":17668/retrieval/data/getData.json?pv={pv}&from={start}&to={end}";

        this.objectMapper = objectMapper;
    }

    public ArchiverQueryResult query(String channelName, Instant start, Instant end){
        return WebClient.builder()
                .build()
                .get()
                .uri(queryUrl, channelName, start, end)
                .retrieve()
                .bodyToMono(ArchiverQueryResultList.class)
                .block()
                .get(0); // Return the first result of the list (as we only query for one channel)
    }

    /**
     * Note The archiver usually returns one entry before the actual query range!
     * TODO need to be filtered
     *
     * @param channelName
     * @param start
     * @param end
     * @return
     */
    public Flux<ArchiverQueryResultEvent> queryStream(String channelName, Instant start, Instant end) {

        // TODO this needs to be re-worked - just copy paste from the old project
        final AtomicReference<JsonParser> jParserRef = new AtomicReference<>();
        final AtomicReference<ArchiverQueryResultMeta> metaRef = new AtomicReference<>();
        final AtomicLong errorCounter = new AtomicLong();

        logger.info("Issue query: {}", new DefaultUriBuilderFactory().expand(queryUrl, channelName, start, end));

        return WebClient.builder()
                .build()
                .get()
                .uri(queryUrl, channelName, start, end)
                .exchange()
                .filter(r -> {
                    if(r.statusCode().is2xxSuccessful()){
                        return true;
                    }
                    else{
                        logger.warn("Unsuccessful data retrieval from archiver");
                        logger.warn(r.statusCode().getReasonPhrase());
                        return false;
                    }
                })
//                .filter(r -> r.statusCode().is2xxSuccessful()) // Only continue if the status code was successful
                .flatMapMany(clientResp -> Jackson2Tokenizer.tokenize(
                        clientResp.body(BodyExtractors.toDataBuffers()), objectMapper.getFactory(), false))
                .flatMapIterable(tokenBuffer -> {
                    try {
                        final com.fasterxml.jackson.core.JsonParser jParser = tokenBuffer.asParser(objectMapper);
                        jParserRef.set(jParser);

                        // Strangely, the json root element is an array with length 1
                        if (jParser.nextToken() == JsonToken.START_ARRAY
                                && jParser.nextToken() == JsonToken.START_OBJECT
                                && jParser.nextToken() == JsonToken.FIELD_NAME
                                && "meta".equals(jParser.getCurrentName())) {

                            // get to meta content
                            jParser.nextToken();
                            final ArchiverQueryResultMeta meta = objectMapper.readValue(jParser, ArchiverQueryResultMeta.class);
                            metaRef.set(meta);

                            if (jParser.nextToken() == JsonToken.FIELD_NAME
                                    && "data".equals(jParser.getCurrentName())) {
                                // get to data content
                                jParser.nextToken();
                                final Iterator<JsonToken> iter = new JsonTokenIterator(jParser, JsonToken.END_ARRAY);
                                return () -> iter;
                            } else {
                                logger.error(
                                        "ArchiverAppliance's did not response with the expected data JSON format");
                            }
                        } else {
                            logger.error(
                                    "ArchiverAppliance's did not response with the expected meta JSON format");
                        }
                    } catch (Exception e) {
                        logger.error("Quering ArchiverAppliance failed due to '{}'", e.getMessage());
                    }

                    return Collections.emptyList();
                })
                .map(token -> {
                    try {
                        final ArchiverQueryResultEvent event =
                                objectMapper.readValue(jParserRef.get(), ArchiverQueryResultEvent.class);

                        return event;
                    } catch (Exception e) {
                        // try to prevent fill-up of error logs if things go really wrong
                        final long errorCount = errorCounter.getAndIncrement();
                        if (errorCount % 50000 == 0) {
                            logger.warn("Could not parse ArchiverApplianceEvent. Error count '{}' for query '{}'.",
                                    errorCount,
                                    new DefaultUriBuilderFactory().expand(queryUrl, channelName, start, end));
                        } else if (errorCount % 1000 == 0) {
                            logger.warn("Could not parse ArchiverApplianceEvent due to '{}'. Error count '{}' for query '{}'.",
                                    e.getMessage(),
                                    errorCount,
                                    new DefaultUriBuilderFactory().expand(queryUrl, channelName, start, end));
                        }
                    }

                    return null;
                })
                .filter(event -> event !=null);

    }
}
