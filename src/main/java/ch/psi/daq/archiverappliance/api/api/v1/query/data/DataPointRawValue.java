package ch.psi.daq.archiverappliance.api.api.v1.query.data;

public class DataPointRawValue<T> extends DataPointValue {

    private T value;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    // For testing purpose only
    @Override
    public String toString() {
        return ""+value;
    }
}
