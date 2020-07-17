package ch.psi.daq.archiverappliance.api.api.v1.config;

import java.util.List;

/**
 *  Request to get all channels
 *     {
 *         "regex":"TRFCA|TRFCB",
 *             "backends":[
 *         "sf-databuffer"
 *    ],
 *         "ordering":"asc",
 *             "reload":true
 *     }
 */
public class RequestChannels {

    private String regex = ".*";
    private List<String> backends;
    private String ordering = "asc";
    private boolean reload = false;
    private String sourceRegex = null; // This attribute is just to support the config query - not actually used

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public List<String> getBackends() {
        return backends;
    }

    public void setBackends(List<String> backends) {
        this.backends = backends;
    }

    public String getOrdering() {
        return ordering;
    }

    public void setOrdering(String ordering) {
        this.ordering = ordering;
    }

    public boolean isReload() {
        return reload;
    }

    public void setReload(boolean reload) {
        this.reload = reload;
    }

    public String getSourceRegex() {
        return sourceRegex;
    }

    public void setSourceRegex(String sourceRegex) {
        this.sourceRegex = sourceRegex;
    }
}
