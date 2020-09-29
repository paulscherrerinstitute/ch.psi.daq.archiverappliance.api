package ch.psi.daq.archiverappliance.api.api.v1.query.data;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class DataPointSerializer extends JsonSerializer {

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        if(value instanceof DataPointRawValue) {
            gen.writeNumber(((DataPointRawValue) value).getValue());
        }
        else if(value instanceof DataPointMinMaxMeanValue){
            gen.writeStartObject();
            gen.writeNumberField("min", ((DataPointMinMaxMeanValue) value).getMin());
            gen.writeNumberField("max", ((DataPointMinMaxMeanValue) value).getMax());
            gen.writeNumberField("mean", ((DataPointMinMaxMeanValue) value).getMean());
            gen.writeEndObject();
        }
    }
}
