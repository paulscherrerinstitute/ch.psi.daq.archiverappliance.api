package ch.psi.daq.archiverappliance.api.api.v1.query;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChannelListDeserializer extends JsonDeserializer {
    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);

        // We expect a list of strings or a list of  {"name": "", "backend":""}

        List<String> channels = new ArrayList<>();
            for(JsonNode n: node){
                String channelName;
                if( n.isTextual()){
                    channelName = n.textValue();
                }
                else{
                    channelName = n.get("name").textValue();
                }
                channels.add(channelName);
            }

        return channels;
    }
}