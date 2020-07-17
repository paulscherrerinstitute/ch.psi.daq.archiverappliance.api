package ch.psi.daq.archiverappliance.api.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ArchiverQueryResultMeta {

    private String name;
    @JsonProperty("EGU")
    private String egu;
    @JsonProperty("PREC")
    private String precision;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEgu() {
        return egu;
    }

    public void setEgu(String egu) {
        this.egu = egu;
    }

    public String getPrecision() {
        return precision;
    }

    public void setPrecision(String precision) {
        this.precision = precision;
    }
}
