package ch.psi.daq.archiverappliance.api;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Instant;

public class AggregatorTest {

    @Test
    public void testBinning() throws InterruptedException {

        Aggregator aggregator = new Aggregator();
        Flux<Integer> flux = Flux.range(0, 10);
        aggregator.handleIntegerSequence(flux)
                .subscribe(x -> System.out.println(x));

    }
}
