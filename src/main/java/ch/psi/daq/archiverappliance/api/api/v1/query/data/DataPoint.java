package ch.psi.daq.archiverappliance.api.api.v1.query.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.Instant;

public class DataPoint {
    private DataPointValue value;

    @JsonIgnore
    private Instant timestamp;

//    private long pulseId;
//    private long globalMillis;
//    private String globalSeconds;
//    private String globalDate;

    private int eventCount = 1;

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

    @Override
    public String toString() {
        return timestamp.toString() + " - " + value.toString();
    }
}
