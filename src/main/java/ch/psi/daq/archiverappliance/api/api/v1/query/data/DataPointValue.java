package ch.psi.daq.archiverappliance.api.api.v1.query.data;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = DataPointValueSerializer.class)
public abstract class DataPointValue {
}
