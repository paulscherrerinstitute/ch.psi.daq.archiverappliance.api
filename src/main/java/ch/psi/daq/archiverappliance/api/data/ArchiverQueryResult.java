package ch.psi.daq.archiverappliance.api.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ArchiverQueryResult {

    private ArchiverQueryResultMeta meta;
    private List<ArchiverQueryResultEvent> data;

    public ArchiverQueryResultMeta getMeta() {
        return meta;
    }

    public void setMeta(ArchiverQueryResultMeta meta) {
        this.meta = meta;
    }

    public List<ArchiverQueryResultEvent> getData() {
        return data;
    }

    public void setData(List<ArchiverQueryResultEvent> data) {
        this.data = data;
    }
}
