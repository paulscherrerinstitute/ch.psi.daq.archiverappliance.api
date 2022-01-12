package ch.psi.daq.archiverappliance.api.api.v1.query;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeParseException;

public class RangeDeserializer extends JsonDeserializer {
    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);

        Range range;
        if(node.get("startDate") != null){
            // Date range
            DateRange dateRange = new DateRange();

            try {
                dateRange.setStartDate(Instant.parse(node.get("startDate").textValue()));
                dateRange.setEndDate(Instant.parse(node.get("endDate").textValue()));
            }
            catch (DateTimeParseException e){
                try {
                    // Try to parse the string in a different way
                    // https://howtodoinjava.com/java/date-time/parse-string-to-date-time-utc-gmt/
                    dateRange.setStartDate(OffsetDateTime.parse(node.get("startDate").textValue()).toInstant());
                    dateRange.setEndDate(OffsetDateTime.parse(node.get("endDate").textValue()).toInstant());
                }
                catch(Exception ex){
                    dateRange.setStartDate(LocalDateTime.parse(node.get("startDate").textValue()).atZone(ZoneId.systemDefault()).toInstant());
                    dateRange.setEndDate(LocalDateTime.parse(node.get("endDate").textValue()).atZone(ZoneId.systemDefault()).toInstant());
                }
            }

            range = dateRange;
        }
        else if(node.get("startSeconds") != null){
            // Seconds from epoch range
            DateRange dateRange = new DateRange();

            dateRange.setStartDate(Instant.ofEpochMilli((long)(Double.parseDouble(node.get("startSeconds").asText())*1000)));
            dateRange.setEndDate(Instant.ofEpochMilli((long)(Double.parseDouble(node.get("endSeconds").asText())*1000)));

            range = dateRange;
        }
        else {
            // Pulse Id Range
            range = null;
        }

        if(node.get("startInclusive") != null) {
            range.setStartInclusive(node.get("startInclusive").booleanValue());
        }

        if(node.get("startExpansion") != null) {
            range.setStartExpansion(node.get("startExpansion").booleanValue());
        }

        if(node.get("endInclusive") != null) {
            range.setEndInclusive(node.get("endInclusive").booleanValue());
        }

        if(node.get("endExpansion") != null) {
            range.setEndExpansion(node.get("endExpansion").booleanValue());
        }

        return range;
    }
}