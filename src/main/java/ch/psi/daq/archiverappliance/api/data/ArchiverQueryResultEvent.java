package ch.psi.daq.archiverappliance.api.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties()
public class ArchiverQueryResultEvent {
   @JsonProperty("secs")
   private long seconds;
   @JsonProperty("nanos")
   private long nanoseconds;
   @JsonProperty("val")
   private double value;
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

   public double getValue() {
      return value;
   }

   public void setValue(double value) {
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
