package ch.psi.daq.archiverappliance.api.api.v1.query;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class AggregationDeserializer extends JsonDeserializer {
    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);

        // We expect a list of strings or a list of  {"name": "", "backend":""}

        Aggregation aggregation = new Aggregation();
        if(node.get("aggregationType") != null) {
            String aggregationType = node.get("aggregationType").asText();
            if(! aggregationType.equals("value")){
                throw new JsonParseException(p, aggregationType + " - Unsupported aggregation type");
            }
            aggregation.setAggregationType(aggregationType);
        }

        if(node.get("aggregations") != null) {
            List<String> aggregationList = new ArrayList<>();
            for (JsonNode n : node.get("aggregations")) {
                aggregationList.add(n.textValue());
            }
            aggregation.setAggregations(aggregationList);
        }

        if(node.get("nrOfBins") != null) {
            int bins = node.get("nrOfBins").asInt(0);
            aggregation.setNrOfBins(bins);
        }

        if(node.get("durationPerBin") != null) {
            aggregation.setDurationPerBin(Duration.parse(node.get("durationPerBin").asText()));
        }

        return aggregation;
    }
}