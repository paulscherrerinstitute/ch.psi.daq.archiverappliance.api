package ch.psi.daq.archiverappliance.api.api.v1.query.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.Instant;

public class  DataPoint {
    private DataPointValue value;

    @JsonIgnore
    private Instant timestamp;
    @JsonIgnore
    private boolean initialized = false;

//    private long pulseId;
//    private long globalMillis;
//    private String globalSeconds;
//    private String globalDate;

    // TODO: Float64 UInt16
    private String type = "Float64";
    private int[] shape = new int[]{1};

    private int eventCount = 1;

    public DataPoint(){
    }

    public DataPoint(Instant timestamp){
        this.timestamp = timestamp;
    }

    public DataPointValue getValue() {
        return value;
    }

    public void setValue(DataPointValue value) {
        this.value = value;
    }

    public int getEventCount() {
        return eventCount;
    }

    public void setEventCount(int eventCount) {
        this.eventCount = eventCount;
    }

    // TODO THIS IS JUST A HACK
    public long getPulseId() {
        return -1;
    }

    public String getGlobalSeconds() {
        return String.format("%d.%09d", timestamp.getEpochSecond(), timestamp.getNano());
    }

    public long getGlobalMillis() {
        return timestamp.toEpochMilli();
    }

    public String getGlobalDate() {
        return timestamp.toString();
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int[] getShape() {
        return shape;
    }

    public void setShape(int[] shape) {
        this.shape = shape;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    @Override
    public String toString() {
        return timestamp.toString() + " - " + value.toString();
    }
}
