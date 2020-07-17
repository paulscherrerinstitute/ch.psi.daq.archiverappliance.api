package ch.psi.daq.archiverappliance.api.api.v1.query.data;

import java.util.List;

public class ChannelResult {
    private ChannelDescription channel;
    private List<DataPoint> data;

    public ChannelDescription getChannel() {
        return channel;
    }

    public void setChannel(ChannelDescription channel) {
        this.channel = channel;
    }

    public List<DataPoint> getData() {
        return data;
    }

    public void setData(List<DataPoint> data) {
        this.data = data;
    }
}
