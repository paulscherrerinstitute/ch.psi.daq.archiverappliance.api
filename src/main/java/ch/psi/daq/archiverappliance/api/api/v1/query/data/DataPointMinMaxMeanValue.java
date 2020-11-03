package ch.psi.daq.archiverappliance.api.api.v1.query.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

// TODO This way of calculating the mean might cause some overflows - need to be revised
public class DataPointMinMaxMeanValue extends DataPointValue {
    private double min;
    private double max;

    @JsonIgnore
    private double sum;
    @JsonIgnore
    private int count;

    public DataPointMinMaxMeanValue() {
        min = Double.MIN_VALUE;
        max = Double.MAX_VALUE;
        count = 0;
        sum = 0;
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
        return sum/count;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void sumUp(double value){
        count += 1;
        sum += value;
    }

    @Override
    public String toString() {
        return "[" + min + " " + getMean() + " " + max + "]";
    }
}
