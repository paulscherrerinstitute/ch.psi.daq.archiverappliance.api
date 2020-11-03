package ch.psi.daq.archiverappliance.api.api.v1.query;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * "aggregation":{
 *       "aggregationType":"value",
 *       "aggregations":[
 *          "min",
 *          "mean",
 *          "max"
 *       ],
 *       "nrOfBins":2
 *    }
 */
@JsonIgnoreProperties
public class Aggregation {
    private String aggregationType = "value"; // value (default) or index
    private List<String> aggregations;
    private int nrOfBins;

    public String getAggregationType() {
        return aggregationType;
    }

    public void setAggregationType(String aggregationType) {
        this.aggregationType = aggregationType;
    }

    public List<String> getAggregations() {
        return aggregations;
    }

    public void setAggregations(List<String> aggregations) {
        this.aggregations = aggregations;
    }

    public int getNrOfBins() {
        return nrOfBins;
    }

    public void setNrOfBins(int nrOfBins) {
        this.nrOfBins = nrOfBins;
    }
}
