package ch.psi.daq.archiverappliance.api.api.v1.query;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@JsonIgnoreProperties
public class Query {

    @JsonDeserialize(using = ChannelListDeserializer.class)
    private List<String> channels;
    private Range range;
    private List<String> eventFields;
    private Aggregation aggregation;

    public List<String> getChannels() {
        return channels;
    }

    public void setChannels(List<String> channels) {
        this.channels = channels;
    }

    public Range getRange() {
        return range;
    }

    public void setRange(Range range) {
        this.range = range;
    }

    public List<String> getEventFields() {
        return eventFields;
    }

    public void setEventFields(List<String> eventFields) {
        this.eventFields = eventFields;
    }

    public Aggregation getAggregation() {
        return aggregation;
    }

    public void setAggregation(Aggregation aggregation) {
        this.aggregation = aggregation;
    }
}
