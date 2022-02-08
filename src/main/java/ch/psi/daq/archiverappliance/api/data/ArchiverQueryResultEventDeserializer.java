package ch.psi.daq.archiverappliance.api.data;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;

enum Type {
    DOUBLE, STRING, INTEGER
}

public class ArchiverQueryResultEventDeserializer extends JsonDeserializer {
    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        JsonNode node = p.getCodec().readTree(p);

        ArchiverQueryResultEvent event = new ArchiverQueryResultEvent();

        if(node.get("val") != null) {
            JsonNode value = node.get("val");
            if(value.isArray()){
                if(!value.isEmpty()) {
                    // Determine type
                    Type type = Type.DOUBLE;
                    ArrayList<?> values = new ArrayList<Double>();
                    if(value.get(0).isTextual()){
                        type = Type.STRING;
                        values = new ArrayList<String>();
                    }
                    else if (value.get(0).isInt()){
                        type = Type.INTEGER;
                        values = new ArrayList<Integer>();
                    }

                    for (JsonNode n : value) {
                        switch (type){
                            case DOUBLE:
                                ((ArrayList<Double>) values).add(n.doubleValue());
                                break;
                            case STRING:
                                ((ArrayList<String>) values).add(n.textValue());
                                break;
                            case INTEGER:
                                ((ArrayList<Integer>) values).add(n.intValue());
                                break;
                        }
                    }
                    event.setValue(values);
                }
                else{
                    // add empty list
                    event.setValue(new ArrayList());
                }
            }
            else {
                if (value.isTextual()) {
                    event.setValue(value.textValue());
                } else {
                    Double v = value.doubleValue();
                    // Ignore infinite and NaN values
                    if (!v.isInfinite() && !v.isNaN()) {
                        event.setValue(value.doubleValue());
                    }
                }
            }
        }

        if(node.get("secs") != null) {
            event.setSeconds(node.get("secs").asLong());
        }

        if(node.get("nanos") != null) {
            event.setNanoseconds(node.get("nanos").asLong());
        }

        if(node.get("status") != null) {
            event.setStatus(node.get("status").asInt());
        }

        if(node.get("severity") != null) {
            event.setSeverity(node.get("severity").asInt());
        }

        return event;
    }
}
