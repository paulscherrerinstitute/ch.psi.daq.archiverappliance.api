package ch.psi.daq.archiverappliance.api.data;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class to hold all channel configurations returned by the archiver appliance for one particular
 * channel (retrieved via: http://sf-archapp-05.psi.ch:17665/mgmt/bpl/getPVTypeInfo?pv={pv})
 *
 * At the time of initial writing the return value for the call was something like this:
 *
 * {
 *   "paused": "false",
 *   "creationTime": "2016-04-01T13:05:01.495Z",
 *   "lowerAlarmLimit": "0.0",
 *   "precision": "0.0",
 *   "lowerCtrlLimit": "0.0",
 *   "computedBytesPerEvent": "10",
 *   "computedEventRate": "0.016666668",
 *   "usePVAccess": "false",
 *   "computedStorageRate": "0.16666667",
 *   "modificationTime": "2019-12-20T12:19:09.223Z",
 *   "upperDisplayLimit": "0.0",
 *   "upperWarningLimit": "0.0",
 *   "DBRType": "DBR_SCALAR_ENUM",
 *   "dataStores": [
 *     "pb://localhost?name=LTS&rootFolder=${ARCHAPPL_LONG_TERM_FOLDER}&partitionGranularity=PARTITION_MONTH"
 *   ],
 *   "upperAlarmLimit": "0.0",
 *   "userSpecifiedEventRate": "0.0",
 *   "policyName": "None_None_Monitor",
 *   "useDBEProperties": "false",
 *   "hasReducedDataSet": "false",
 *   "lowerWarningLimit": "0.0",
 *   "chunkKey": "S10/CPPS/DALA01/CH2READ:",
 *   "applianceIdentity": "sfArchApp1",
 *   "scalar": "true",
 *   "pvName": "S10-CPPS-DALA01:CH2READ",
 *   "upperCtrlLimit": "0.0",
 *   "lowerDisplayLimit": "0.0",
 *   "samplingPeriod": "0.001",
 *   "elementCount": "1",
 *   "samplingMethod": "MONITOR",
 *   "archiveFields": [],
 *   "extraFields": {
 *     "SCAN": "Passive",
 *     "RTYP": "bi"
 *   }
 * }
 *
 * An other configuration returned looks like this:
 * {
 *   "hostName": "sf-cagw-arch.psi.ch",
 *   "paused": "true",
 *   "creationTime": "2019-07-01T14:05:04.208Z",
 *   "lowerAlarmLimit": "NaN",
 *   "precision": "2.0",
 *   "lowerCtrlLimit": "0.0",
 *   "units": "A",
 *   "computedBytesPerEvent": "25",
 *   "computedEventRate": "0.033333335",
 *   "usePVAccess": "false",
 *   "computedStorageRate": "0.8333333",
 *   "modificationTime": "2020-02-18T15:27:12.299Z",
 *   "upperDisplayLimit": "0.0",
 *   "upperWarningLimit": "40.0",
 *   "DBRType": "DBR_SCALAR_DOUBLE",
 *   "dataStores": [
 *     "pb://localhost?name=STS&rootFolder=${ARCHAPPL_SHORT_TERM_FOLDER}&partitionGranularity=PARTITION_DAY&hold=15&gather=1",
 *     "pb://localhost?name=MTS&rootFolder=${ARCHAPPL_MEDIUM_TERM_FOLDER}&partitionGranularity=PARTITION_DAY&hold=121&gather=1&reducedata=firstSample_60",
 *     "pb://localhost?name=LTS&rootFolder=${ARCHAPPL_LONG_TERM_FOLDER}&partitionGranularity=PARTITION_MONTH&reducedata=firstSample_3600"
 *   ],
 *   "upperAlarmLimit": "80.0",
 *   "userSpecifiedEventRate": "0.0",
 *   "policyName": "Monitor_60_3600",
 *   "useDBEProperties": "false",
 *   "hasReducedDataSet": "false",
 *   "lowerWarningLimit": "NaN",
 *   "chunkKey": "CR0808/CURRENT/3/3:",
 *   "applianceIdentity": "sfArchApp5",
 *   "scalar": "true",
 *   "pvName": "CR0808:CURRENT-3-3",
 *   "upperCtrlLimit": "0.0",
 *   "lowerDisplayLimit": "0.0",
 *   "samplingPeriod": "0.001",
 *   "elementCount": "1",
 *   "samplingMethod": "MONITOR",
 *   "archiveFields": [
 *     "HIHI",
 *     "HIGH",
 *     "LOW",
 *     "LOLO",
 *     "LOPR",
 *     "HOPR"
 *   ],
 *   "extraFields": {
 *     "ADEL": "0.0",
 *     "MDEL": "0.0",
 *     "SCAN": "Passive",
 *     "NAME": "CR0808:CURRENT-3-3",
 *     "RTYP": "ai"
 *   }
 * }
 *
 * This is a waveform channel:
 * < HTTP/1.1 200
 * < Content-Type: application/json;charset=ISO-8859-1
 * < Content-Length: 1321
 * < Date: Mon, 16 Mar 2020 12:20:21 GMT
 * {
 *   "hostName": "SF-SIOC-CS-11.psi.ch",
 *   "paused": "false",
 *   "creationTime": "2016-07-19T06:17:27.072Z",
 *   "lowerAlarmLimit": "-2.147483648E9",
 *   "precision": "0.0",
 *   "lowerCtrlLimit": "0.0",
 *   "units": "",
 *   "computedBytesPerEvent": "24",
 *   "computedEventRate": "10.0",
 *   "usePVAccess": "false",
 *   "computedStorageRate": "248.0",
 *   "modificationTime": "2019-11-21T18:34:10.282Z",
 *   "upperDisplayLimit": "0.0",
 *   "upperWarningLimit": "-2.147483648E9",
 *   "DBRType": "DBR_WAVEFORM_INT",
 *   "dataStores": [
 *     "pb://localhost?name=STS&rootFolder=${ARCHAPPL_SHORT_TERM_FOLDER}&partitionGranularity=PARTITION_DAY&hold=15&gather=1",
 *     "pb://localhost?name=MTS&rootFolder=${ARCHAPPL_MEDIUM_TERM_FOLDER}&partitionGranularity=PARTITION_DAY&hold=121&gather=1&reducedata=firstSample_30",
 *     "pb://localhost?name=LTS&rootFolder=${ARCHAPPL_LONG_TERM_FOLDER}&partitionGranularity=PARTITION_MONTH&reducedata=firstSample_60"
 *   ],
 *   "upperAlarmLimit": "-2.147483648E9",
 *   "userSpecifiedEventRate": "0.0",
 *   "policyName": "10_30_60",
 *   "useDBEProperties": "false",
 *   "hasReducedDataSet": "false",
 *   "lowerWarningLimit": "-2.147483648E9",
 *   "chunkKey": "SLG/LCAM/C103/CAMCALC.VALJ:",
 *   "applianceIdentity": "sfArchApp3",
 *   "scalar": "false",
 *   "pvName": "SLG-LCAM-C103:CAMCALC.VALJ",
 *   "upperCtrlLimit": "0.0",
 *   "lowerDisplayLimit": "0.0",
 *   "samplingPeriod": "10.0",
 *   "elementCount": "3",
 *   "samplingMethod": "SCAN",
 *   "archiveFields": [],
 *   "extraFields": {
 *     "NAME": "??"
 *   }
 * }
 *
 * TODO: Currently multi dimensional arrays are not supported/implemented
 */
public class ArchiverChannelConfiguration {

    @JsonAlias("pvName")
    private String name;
    @JsonAlias("hostName")
    private String source;
    @JsonAlias("units")
    private String unit;

    // Attributes needed to calculate type and shape
    @JsonAlias("DBRType")
    private String dbrType;
    private String scalar;
    private String elementCount;

    // Calculated attributes
    // "type":"UInt16"
    // "shape":[1024,2048]

    private static Map<String, String> TYPE_MAPPING = new HashMap<>();
    static {
        TYPE_MAPPING.put("BYTE", "int8");
        TYPE_MAPPING.put("SHORT", "int16");
        TYPE_MAPPING.put("INT", "int32");
        TYPE_MAPPING.put("FLOAT", "float32");
        TYPE_MAPPING.put("DOUBLE", "float64");
        TYPE_MAPPING.put("STRING", "string");
        TYPE_MAPPING.put("ENUM", "int32");
    }

    /**
     * Calculated value out of scalar and elementCount property
     * @return  Shape of channel
     */
    public int[] getShape(){
        int[] shape = new int[] {1}; // scalar
        if(scalar.equalsIgnoreCase("false")){
            shape = new int[] {Integer.parseInt(elementCount)};
        }
        return shape;
    }

    /**
     * Converted value out of dbrType property
     * @return  Type of channel
     */
    public String getType(){
        // The type can be determined based on the ending of the dbr type
        // Take the last part of the dbr type
        final String ending = dbrType.substring(dbrType.lastIndexOf("_") + 1);
        return TYPE_MAPPING.get(ending);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDbrType() {
        return dbrType;
    }

    public void setDbrType(String dbrType) {
        this.dbrType = dbrType;
    }

    public String getScalar() {
        return scalar;
    }

    public void setScalar(String scalar) {
        this.scalar = scalar;
    }

    public String getElementCount() {
        return elementCount;
    }

    public void setElementCount(String elementCount) {
        this.elementCount = elementCount;
    }
}
