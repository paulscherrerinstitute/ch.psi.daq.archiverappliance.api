package ch.psi.daq.archiverappliance.api;

import ch.psi.daq.archiverappliance.api.api.v1.query.DateRange;
import ch.psi.daq.archiverappliance.api.api.v1.query.data.DataPoint;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Predicate;

/**
 * Boundary trigger to be used with the flux windowUntil function:
 * https://projectreactor.io/docs/core/release/api/reactor/core/publisher/Flux.html#windowUntil-java.util.function.Predicate-boolean-
 *
 * This boundary trigger can result in empty windows that need to be filtered out afterwards
 * with:
 * .filter(x -> !x.isEmpty())
 */
public class DateRangeBinBoundaryTrigger implements Predicate<DataPoint> {

    private Instant currentBoundary;
    private final Duration durationBin;

    public DateRangeBinBoundaryTrigger(DateRange range, int numberOfBins){

        Duration duration = Duration.between(range.getStartDate(), range.getEndDate());
        durationBin = duration.dividedBy(numberOfBins);

        if(range.getStartDate().isBefore(range.getEndDate())){
            currentBoundary = range.getStartDate().plus(durationBin);
        }
        else{
            currentBoundary = range.getEndDate().plus(durationBin);
        }
    }

    @Override
    public boolean test(DataPoint o) {
        if(o.getTimestamp().isBefore(currentBoundary) || o.getTimestamp().equals(currentBoundary)){
            return false;
        }
        else {
            currentBoundary = currentBoundary.plus(durationBin);
            return true;
        }
    }
}
