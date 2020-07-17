package ch.psi.daq.archiverappliance.api.api.v1;

import ch.psi.daq.archiverappliance.api.ArchiverQueryManager;
import ch.psi.daq.archiverappliance.api.api.v1.query.DateRange;
import ch.psi.daq.archiverappliance.api.api.v1.query.Query;
import ch.psi.daq.archiverappliance.api.api.v1.query.Range;
import ch.psi.daq.archiverappliance.api.api.v1.query.data.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

@RestController
public class QueryController {
    private static final Logger logger = LoggerFactory.getLogger(QueryController.class);

    private ArchiverQueryManager archiverManager;

    public QueryController(ArchiverQueryManager archiverManager) {
        this.archiverManager = archiverManager;
    }


    @CrossOrigin
    @RequestMapping(value = "/query", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<ChannelResult> query(@RequestBody Query request) {

        return Flux.fromStream(request.getChannels().stream())  // Iterate through channels
                .flatMap(channelName -> {
                    Range range = request.getRange();
                    if (range instanceof DateRange) {

                        Mono<ChannelResult> mono = archiverManager.queryStream(channelName, ((DateRange) range).getStartDate(), ((DateRange) range).getEndDate())
                                .skip(1) // The archiver appliance does always return one data point before the actual range
                                .map(p -> {  // map the datapoints to the different format
                                    DataPoint dataPoint = new DataPoint();
                                    dataPoint.setTimestamp(Instant.ofEpochSecond(p.getSeconds(), p.getNanoseconds()));

                                    DataPointMinMaxMeanValue value = new DataPointMinMaxMeanValue();
                                    value.setMax(p.getValue());
                                    value.setMin(p.getValue());
                                    value.setMean(p.getValue());
                                    dataPoint.setValue(value);
                                    return dataPoint;
                                }).collectList()  // collect all results into a list
                                .map(l -> {  // construct the channel result
                                    ChannelDescription description = new ChannelDescription();
                                    description.setName(channelName);
                                    ChannelResult result = new ChannelResult();
                                    result.setChannel(description);
                                    result.setData(l);
                                    return result;
                                });
                        return mono;
                    }
                    return Mono.empty();
                });
    }
}
