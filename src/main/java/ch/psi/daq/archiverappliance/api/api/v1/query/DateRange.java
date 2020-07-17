package ch.psi.daq.archiverappliance.api.api.v1.query;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Instant;

/**
 * "range":{
 *    "startDate":"2015-08-06T18:00:00.000",
 *    "startInclusive":true,
 *    "startExpansion":false,
 *    "endDate":"2015-08-06T18:59:59.999",
 *    "endInclusive":true,
 *    "endExpansion":false
 * }
 */
@JsonIgnoreProperties
public class DateRange extends Range{
    private Instant startDate;
    private Instant endDate;

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

}
