package ch.psi.daq.archiverappliance.api.api.v1.config;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * {
 *      "name":"CAM_CHANNEL",
 *      "type":"UInt16",
 *      "unit":"A",
 *      "shape":[1024,2048],
 *      "modulo":1,
 *      "offset":0,
 *      "backend":"sf-imagebuffer",
 *      "source":"tcp://CAM_SERVER:9999"
 * }
 */
@JsonInclude(JsonInclude.Include.NON_NULL)  // only include non null values
public class ChannelConfiguration {
    private String name;
    private String type;
    private String unit;
    private int[] shape;
    private int modulo;
    private int offset;
    private String backend;
    private String source;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
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

    public int getModulo() {
        return modulo;
    }

    public void setModulo(int modulo) {
        this.modulo = modulo;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getBackend() {
        return backend;
    }

    public void setBackend(String backend) {
        this.backend = backend;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
