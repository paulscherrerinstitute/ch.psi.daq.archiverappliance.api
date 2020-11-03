package ch.psi.daq.archiverappliance.api;

import ch.psi.daq.archiverappliance.api.api.v1.query.DateRange;
import ch.psi.daq.archiverappliance.api.api.v1.query.data.DataPoint;
import ch.psi.daq.archiverappliance.api.api.v1.query.data.DataPointRawValue;
import ch.psi.daq.archiverappliance.api.api.v1.query.data.DataPointValue;
import com.sun.jdi.request.DuplicateRequestException;
import org.assertj.core.api.IntArrayAssert;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.SQLOutput;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class DateRangeBinBoundaryTriggerTest {

    @Test
    void test1() {

        AtomicInteger boundary = new AtomicInteger(0);
        Flux<Integer> testflux = Flux.fromArray(new Integer[]{1,2,4,8,10});

//        List<Integer> l = testflux
        List<List<Integer>> l = testflux
                .windowUntil(x -> {
                    if(x <= boundary.get()){
                        return false;
                    }
                    else {
                        boundary.addAndGet(2);
                        return true;
                    }
                }, true)
                .flatMap(x -> x.collectList())
                .filter(x -> !x.isEmpty())

        .collectList().block();

        System.out.println(l);


    }

    @Test
    void test2() {

        DateRange range = new DateRange();
        Instant start = Instant.now();
        range.setStartDate(start);
        range.setEndDate(start.plus(Duration.ofMinutes(10)));

        List<DataPoint> list = new ArrayList<>();
//        for(int offset: new Integer[]{4,8}){
        for(int offset: new Integer[]{0,1,2,4,8,10}){
            DataPoint point = new DataPoint();
            point.setTimestamp(start.plus(Duration.ofMinutes(offset)));

            DataPointRawValue value = new DataPointRawValue();
            value.setValue(offset);
            point.setValue(value);

            list.add(point);
        }

        List<List<DataPoint>> l = Flux.fromIterable(list)
                .windowUntil(new DateRangeBinBoundaryTrigger(range, 5), true)
                .flatMap(x -> x.collectList())
                .collectList().block();

        System.out.println(l);


        // To test:
        // start exact on range start
        // end exact on range end
        // empty window(s) at the beginning
        // empty window(s) at the end
        // empty window(s) in the middle


        List<DataPoint> l2 = Flux.fromIterable(list)
                .windowUntil(new DateRangeBinBoundaryTrigger(range, 5), true)
                .flatMap( x -> x.collect(new BinMinMaxMeanCollector()))
                .collectList().block();

        System.out.println(l2);

    }
}