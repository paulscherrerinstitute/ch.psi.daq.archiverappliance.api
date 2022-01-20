package ch.psi.daq.archiverappliance.api.api.v1;

import ch.psi.daq.archiverappliance.api.ArchiverQueryManager;
import ch.psi.daq.archiverappliance.api.BinMinMaxMeanCollector;
import ch.psi.daq.archiverappliance.api.DataPointRawValueMapper;
import ch.psi.daq.archiverappliance.api.DateRangeBinBoundaryTrigger;
import ch.psi.daq.archiverappliance.api.api.v1.query.Aggregation;
import ch.psi.daq.archiverappliance.api.api.v1.query.DateRange;
import ch.psi.daq.archiverappliance.api.api.v1.query.Query;
import ch.psi.daq.archiverappliance.api.api.v1.query.Range;
import ch.psi.daq.archiverappliance.api.api.v1.query.data.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@CrossOrigin
@RestController
public class QueryController {
    private static final Logger logger = LoggerFactory.getLogger(QueryController.class);

    private ArchiverQueryManager archiverManager;
    private String backendName;

    public QueryController(ArchiverQueryManager archiverManager, @Value("${backend.name}") String backendName) {
        this.archiverManager = archiverManager;
        this.backendName = backendName;
    }

    @RequestMapping(value = "/query", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<ChannelResult> query(@RequestBody Query request) {

        // Check whether the requested channels are actually all from this backend

        return Flux.fromStream(request.getChannels().stream())  // Iterate through channels
                .flatMap(channelName -> {
                    Range range = request.getRange();
                    if (range instanceof DateRange) {

                        // sanity checks for range
                        Instant start = ((DateRange) range).getStartDate();
                        Instant end = ((DateRange) range).getEndDate();
                        if(!end.isAfter(start)){ // end needs to be after start
                            logger.error("Invalid time range - end is not after start - channel: " + channelName);
//                            return Mono.empty(); // workaround
                            return Mono.error(new RuntimeException("Invalid time range - end is not after start"));
                        }

                        // Query the Archiver and map data to api data format
                        Flux<DataPoint> flux = archiverManager.queryStream(channelName, ((DateRange) range).getStartDate(), ((DateRange) range).getEndDate())
//                                .skip(1) // The archiver appliance does always return one data point before the actual range
                                .filter(e -> !Double.isInfinite(e.getValue()))  // filter out infinite values
                                .filter(e ->!Double.isNaN(e.getValue()))  // filter out NAN
                                .map(new DataPointRawValueMapper())
                                // ensure that datapoints are actually in the requested timerange
                                .filter(e -> e.getTimestamp().isAfter(start)) // instead of skip(1)
                                .filter(e -> e.getTimestamp().isBefore(end));



                        // Aggregate values if requested
                        Aggregation aggregation = request.getAggregation();
                        if(aggregation != null){
                            if(aggregation.getDurationPerBin() != null){
                                int bins = (int) Duration.between(start, end).dividedBy(aggregation.getDurationPerBin());
                                flux = flux.windowUntil(new DateRangeBinBoundaryTrigger(((DateRange) range), bins), true)
                                        .flatMap(x -> x.collect(new BinMinMaxMeanCollector()));
                            }
                            else if(aggregation.getNrOfBins() > 0) {
                                flux = flux.windowUntil(new DateRangeBinBoundaryTrigger((DateRange) request.getRange(), request.getAggregation().getNrOfBins()), true)
                                        .flatMap(x -> x.collect(new BinMinMaxMeanCollector()));
                            }
                        }

                        // TODO Workaround - somehow the first bin might be "empty" - i.e. timestamp 0
                        // Ensure again that the bins are within arrange
                        flux = flux.filter(e-> e.getTimestamp().isAfter(start))
                                .filter(e-> e.getTimestamp().isBefore(end));

                        // Construct return datastructure
                        Mono<ChannelResult> mono = flux
                                .collectList()  // collect all results into a list
                                .map(l -> {  // construct the channel result
                                    ChannelDescription description = new ChannelDescription();
                                    description.setName(channelName);
                                    description.setBackend(backendName);
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
