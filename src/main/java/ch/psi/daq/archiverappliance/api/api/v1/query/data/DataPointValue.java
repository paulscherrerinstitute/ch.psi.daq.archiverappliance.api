package ch.psi.daq.archiverappliance.api.api.v1.query.data;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = DataPointSerializer.class)
public abstract class DataPointValue {
}
