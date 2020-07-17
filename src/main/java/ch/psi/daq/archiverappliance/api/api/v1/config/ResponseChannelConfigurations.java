package ch.psi.daq.archiverappliance.api.api.v1.config;

import java.util.List;

/**
 * [
 *    {
 *       "backend":"sf-imagebuffer",
 *       "channels":[
 *          {
 *             "name":"CAM_CHANNEL",
 *             "type":"UInt16",
 *             "shape":[1024,2048],
 *             "modulo":1,
 *             "offset":0,
 *             "backend":"sf-imagebuffer",
 *             "source":"tcp://CAM_SERVER:9999"
 *          }
 *       ]
 *    }
 * ]
 */
public class ResponseChannelConfigurations {
    private String backend;
    private List<ChannelConfiguration> channels;

    public String getBackend() {
        return backend;
    }

    public void setBackend(String backend) {
        this.backend = backend;
    }

    public List<ChannelConfiguration> getChannels() {
        return channels;
    }

    public void setChannels(List<ChannelConfiguration> channels) {
        this.channels = channels;
    }
}
