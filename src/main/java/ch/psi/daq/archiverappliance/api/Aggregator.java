package ch.psi.daq.archiverappliance.api;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.Instant;
import java.time.Period;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Aggregator {

    // start
    // end
    // number of bins
    // next bin-boundary
        // whenever the next bin boundary is hit - calculate boundary for next

    private int sum = 0;

    public Flux<Integer> handleIntegerSequence(Flux<Integer> sequence) {
        return sequence.handle((number, sink) -> {
            sum += number;
            if (number % 2 == 0) {
//                sink.next(number / 2);
                sink.next(sum);
                sum=0;
            }
        });
    }
}
