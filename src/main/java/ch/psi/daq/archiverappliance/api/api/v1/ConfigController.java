package ch.psi.daq.archiverappliance.api.api.v1;

import ch.psi.daq.archiverappliance.api.ArchiverConfigurationManager;
import ch.psi.daq.archiverappliance.api.api.v1.config.RequestChannels;

import ch.psi.daq.archiverappliance.api.api.v1.config.ResponseChannelConfigurations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@CrossOrigin
@RestController
public class ConfigController {
    private static final Logger logger = LoggerFactory.getLogger(ConfigController.class);

    private ArchiverConfigurationManager archiverManager;
    private String backendName;
    private int backendId;

    public ConfigController(ArchiverConfigurationManager archiverManager,
                            @Value("${backend.name}") String backendName,
                            @Value("${backend.id}") int backendId){
        this.archiverManager = archiverManager;
        this.backendName = backendName;
        this.backendId = backendId;

    }


    @RequestMapping(value="/channels", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<String>> getChannels(@RequestBody RequestChannels request){

        // backend and ordering are not supported by this request - parameters are simply ignored

        Flux<String> flux = archiverManager.getChannels(request.isReload());
        if(request.getRegex() != ".*") {
            final Pattern pattern = Pattern.compile(request.getRegex());
            flux = flux.filter(channel -> pattern.matcher(channel).find());
        }
        return flux.collectList();
    }

    @RequestMapping(value="/channels/config", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
    public Flux<ResponseChannelConfigurations> getChannelConfigurations(@RequestBody RequestChannels request){


        return archiverManager.getChannelConfigurations(request.getRegex())
                .collectList()
                .map(l -> {
                    // Update backend for each channel (duplicate information but needed because of the ui right now)
                    l.stream().forEach(c -> c.setBackend(backendName));

                    ResponseChannelConfigurations response = new ResponseChannelConfigurations();
                    response.setChannels(l);
                    response.setBackend(backendName);
                    return response;
                })
                .flux();
    }


    // Backward compatibility functions only

    @RequestMapping(value="/params/backends", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
    public List<String> getBackendName(){
        return Arrays.asList(backendName);
    }

    @RequestMapping(value="/params/backends/byid", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> getBackendId(){
        Map<String, String> map = new HashMap<>();
        map.put(backendId+"", backendName);
        return map;
    }

    @RequestMapping(value="/params/aggregations", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
    public List<String> getAggregations() {
        return Arrays.asList("mean", "min", "max", "sum", "count", "variance", "stddev", "skewness", "kurtosis", "typed");
    }

    @RequestMapping(value="/params/configfields", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
    public List<String> getConfigFields(){
        return Arrays.asList("name", "backend", "pulseId", "globalSeconds", "globalDate", "globalMillis", "type", "shape", "source", "precision", "unit", "description");
    }

    @RequestMapping(value="/params/eventfields", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
    public List<String> getEventFields(){
        return Arrays.asList("channel", "backend", "pulseId", "globalTime", "globalSeconds", "globalDate", "globalMillis", "iocTime", "iocSeconds", "iocDate", "iocMillis", "shape", "type", "eventCount", "value");
    }

    @RequestMapping(value="/params/ordering", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
    public List<String> getOrdering() {
        return Arrays.asList("asc", "desc", "none");
    }

    @RequestMapping(value="/params/responseformat", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
    public List<String> getResponseFormat(){
        return Arrays.asList(  "json");
    }

    @RequestMapping(value="/params/compression", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
    public List<String> getCompression(){
        return Arrays.asList("none");
    }
}
