package ch.psi.daq.archiverappliance.api.api.v1.query;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


@JsonDeserialize(using = RangeDeserializer.class)
public abstract class Range {
    private boolean startInclusive = true;
    private boolean startExpansion = false;
    private boolean endInclusive = true;
    private boolean endExpansion = false;

    public boolean isStartInclusive() {
        return startInclusive;
    }

    public void setStartInclusive(boolean startInclusive) {
        this.startInclusive = startInclusive;
    }

    public boolean isStartExpansion() {
        return startExpansion;
    }

    public void setStartExpansion(boolean startExpansion) {
        this.startExpansion = startExpansion;
    }

    public boolean isEndInclusive() {
        return endInclusive;
    }

    public void setEndInclusive(boolean endInclusive) {
        this.endInclusive = endInclusive;
    }

    public boolean isEndExpansion() {
        return endExpansion;
    }

    public void setEndExpansion(boolean endExpansion) {
        this.endExpansion = endExpansion;
    }
}
