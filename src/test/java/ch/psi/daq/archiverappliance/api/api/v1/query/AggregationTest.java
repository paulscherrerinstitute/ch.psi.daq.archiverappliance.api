package ch.psi.daq.archiverappliance.api.api.v1.query;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class AggregationTest {

    @Test
    void parsing() throws JsonProcessingException {
        String json = "{\"aggregationType\":\"value\", \"aggregations\":[\"min\",\"mean\",\"max\"], \"durationPerBin\" : \"PT1S\"}";
        Aggregation aggregation = new ObjectMapper().readValue(json, Aggregation.class);

        assertEquals(aggregation.getDurationPerBin().getSeconds(), 1);
        assertEquals(aggregation.getNrOfBins(), 0);
        assertEquals(aggregation.getAggregationType(), "value");
        assertIterableEquals(aggregation.getAggregations(),  Arrays.asList("min", "mean", "max" ));
    }
}