package ch.psi.daq.archiverappliance.api.api.v1.query.data;

public class DataPointMinMaxMeanValue extends DataPointValue {
    private double min;
    private double max;
    private double mean;

    public DataPointMinMaxMeanValue() {
    }

    public DataPointMinMaxMeanValue(double min, double max, double mean) {
        this.min = min;
        this.max = max;
        this.mean = mean;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }
}
