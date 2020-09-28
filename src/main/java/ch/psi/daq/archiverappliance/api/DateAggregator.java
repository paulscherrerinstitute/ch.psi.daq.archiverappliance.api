package ch.psi.daq.archiverappliance.api;

import ch.psi.daq.archiverappliance.api.api.v1.query.data.DataPointMinMaxMeanValue;
import ch.psi.daq.archiverappliance.api.data.ArchiverQueryResultEvent;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.Deque;
import java.util.LinkedList;

public class DateAggregator {

    private DataPointMinMaxMeanValue currentBinValue = null;

    Instant start = null;
    Instant end = null;
    private Deque<Instant> bins;

    public DateAggregator(Instant start, Instant end, int numberOfBins){

        // This calculation needs to be done properly ...
        this.start = start;
        this.end = end;

        bins = new LinkedList<>();
        long binPeriod = (end.toEpochMilli()-start.toEpochMilli())/numberOfBins;
        Instant currentStart = start;
        while(true) {
            Instant nextBin = currentStart.plusMillis(binPeriod);
            // If we are after the end, we stop
            if(nextBin.isAfter(end)) {
                break;
            }

            bins.add(nextBin);
            currentStart = nextBin;
        }
        // as the above calculation is not as accurate as it should (ignoring nanoseconds) be we replace the
        // last "bin" with end
        bins.removeLast();
        bins.add(end);
    }

    public Flux<DataPointMinMaxMeanValue> handleIntegerSequence(Flux<ArchiverQueryResultEvent> sequence) {
        return sequence.handle((number, sink) -> {
            Instant time = Instant.ofEpochSecond(number.getSeconds(), number.getNanoseconds());
            if(time.isBefore(start)){
                return;
            }

            if(time.isAfter(end)){

                // TODO this last sendout needs to be on flux end !!!!
                // send out last element
                if(currentBinValue != null) {
                    // send out the previous bin - if exists
                    // how do we send out the last bin????
                    sink.next(currentBinValue);
                }
                return;
            }

            // skip all values that are before or after the range
            if(! (time.isBefore(start) || time.isAfter(end))){
                // we should never hit the case where the deque is empty
                if(time.isAfter(bins.peekFirst())){

                    // skip all bins that are before the current values time
                    while(time.isAfter(bins.peekFirst())) {
                        bins.removeFirst();
                    }

                    if(currentBinValue != null) {
                        // send out the previous bin - if exists
                        // how do we send out the last bin????
                        sink.next(currentBinValue);
                    }

                    currentBinValue = new DataPointMinMaxMeanValue();
                    currentBinValue.setMax(number.getValue());
                    currentBinValue.setMean(number.getValue());
                    currentBinValue.setMin(number.getValue());
                }
                else{

                    if(currentBinValue != null) {
                        // Update currentBinValue
                        if (number.getValue() > currentBinValue.getMax()) {
                            currentBinValue.setMax(number.getValue());
                        } else if (number.getValue() < currentBinValue.getMin()) {
                            currentBinValue.setMin(number.getValue());
                        }

                        // this is not a weighted average calculation
                        currentBinValue.setMean((currentBinValue.getMean() + number.getValue()) / 2);
                    }
                    else{
                        // This is the first value in the bin
                        currentBinValue = new DataPointMinMaxMeanValue();
                        currentBinValue.setMax(number.getValue());
                        currentBinValue.setMean(number.getValue());
                        currentBinValue.setMin(number.getValue());
                    }
                }

            }
        });
    }
}
