package ch.psi.daq.archiverappliance.api.api.v1.query.data;

public class ChannelDescription {
    private String name;
    private String backend;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBackend() {
        return backend;
    }

    public void setBackend(String backend) {
        this.backend = backend;
    }
}
