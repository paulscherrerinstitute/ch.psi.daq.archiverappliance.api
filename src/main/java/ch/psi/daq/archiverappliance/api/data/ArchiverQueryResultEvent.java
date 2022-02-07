package ch.psi.daq.archiverappliance.api.data;

import ch.psi.daq.archiverappliance.api.api.v1.query.RangeDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = ArchiverQueryResultEventDeserializer.class)
public class ArchiverQueryResultEvent<T> {
   private long seconds;
   private long nanoseconds;
   private T value;
   private int severity;
   private int status;

   public long getSeconds() {
      return seconds;
   }

   public void setSeconds(long seconds) {
      this.seconds = seconds;
   }

   public long getNanoseconds() {
      return nanoseconds;
   }

   public void setNanoseconds(long nanoseconds) {
      this.nanoseconds = nanoseconds;
   }

   public T getValue() {
      return value;
   }

   public void setValue(T value) {
      this.value = value;
   }

   public int getSeverity() {
      return severity;
   }

   public void setSeverity(int severity) {
      this.severity = severity;
   }

   public int getStatus() {
      return status;
   }

   public void setStatus(int status) {
      this.status = status;
   }
}
