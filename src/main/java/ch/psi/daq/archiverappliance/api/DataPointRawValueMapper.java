package ch.psi.daq.archiverappliance.api;

import ch.psi.daq.archiverappliance.api.api.v1.query.data.DataPoint;
import ch.psi.daq.archiverappliance.api.api.v1.query.data.DataPointRawValue;
import ch.psi.daq.archiverappliance.api.data.ArchiverQueryResultEvent;

import java.time.Instant;
import java.util.function.Function;

public class DataPointRawValueMapper implements Function<ArchiverQueryResultEvent, DataPoint> {

    @Override
    public DataPoint apply(ArchiverQueryResultEvent event) {
        DataPoint dataPoint = new DataPoint();
        dataPoint.setTimestamp(Instant.ofEpochSecond(event.getSeconds(), event.getNanoseconds()));
        DataPointRawValue value = new DataPointRawValue();
        value.setValue(event.getValue());

        dataPoint.setValue(value);
        return dataPoint;
    }
}
