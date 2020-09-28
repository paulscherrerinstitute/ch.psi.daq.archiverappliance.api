package ch.psi.daq.archiverappliance.api;

import ch.psi.daq.archiverappliance.api.data.ArchiverQueryResultEvent;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class DateAggregatorTest {

    @Test
    public void testBinning() throws InterruptedException {

        DateAggregator aggregator = new DateAggregator(Instant.ofEpochSecond(1000), Instant.ofEpochSecond(2000), 10);

        List<ArchiverQueryResultEvent> list = new ArrayList<>();

        // event before range
        ArchiverQueryResultEvent event = new ArchiverQueryResultEvent();
        event.setSeconds(999);
        event.setValue(1);
        list.add(event);

        event = new ArchiverQueryResultEvent();
        event.setSeconds(1001);
        event.setValue(1);
        list.add(event);

        // event after range
        event = new ArchiverQueryResultEvent();
        event.setSeconds(2001);
        event.setValue(1);
        list.add(event);

        Flux<ArchiverQueryResultEvent> flux = Flux.fromIterable(list);
        aggregator.handleIntegerSequence(flux)
                .subscribe(x -> System.out.println(x));


    }
}
