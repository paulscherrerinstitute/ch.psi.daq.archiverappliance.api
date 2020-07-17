package ch.psi.daq.archiverappliance.api.api.v1.config;

import java.util.List;

/**
 * [
 *    {
 *       "backend":"sf-databuffer",
 *       "channels":[
 *          "Channel_01_AMPLT",
 *          "Channel_02_AMPLT",
 *          "Channel_03_PHASE"
 *       ]
 *    }
 * ]
 */
public class ResponseChannels {
    private String backend;
    private List<String> channels;

    public String getBackend() {
        return backend;
    }

    public void setBackend(String backend) {
        this.backend = backend;
    }

    public List<String> getChannels() {
        return channels;
    }

    public void setChannels(List<String> channels) {
        this.channels = channels;
    }
}