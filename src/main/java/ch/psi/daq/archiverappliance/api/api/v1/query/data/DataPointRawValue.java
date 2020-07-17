package ch.psi.daq.archiverappliance.api.api.v1.query.data;

public class DataPointRawValue extends DataPointValue {
    private double value;

    public DataPointRawValue(){
    }

    public DataPointRawValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
