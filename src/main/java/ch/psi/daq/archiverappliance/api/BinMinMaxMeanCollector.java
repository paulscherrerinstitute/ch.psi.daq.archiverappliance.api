package ch.psi.daq.archiverappliance.api;

import ch.psi.daq.archiverappliance.api.api.v1.query.data.DataPoint;
import ch.psi.daq.archiverappliance.api.api.v1.query.data.DataPointMinMaxMeanValue;
import ch.psi.daq.archiverappliance.api.api.v1.query.data.DataPointRawValue;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * More details on how to implement a Collector see: https://blog.indrek.io/articles/creating-a-collector-in-java-8/
 */
public class BinMinMaxMeanCollector implements Collector<DataPoint, DataPoint, DataPoint> {

    @Override
    public Supplier<DataPoint> supplier() {
        return ()->{
            DataPoint point = new DataPoint();
            DataPointMinMaxMeanValue value = new DataPointMinMaxMeanValue();
            // this way we ensure that min/max get updated
            value.setMin(Double.MAX_VALUE);
            value.setMax(Double.MIN_VALUE);

            point.setValue(value);
            return point;
        };
    }

    @Override
    public BiConsumer<DataPoint, DataPoint> accumulator() {
        return (buffer, value) ->{
            double v = ((DataPointRawValue)value.getValue()).getValue();
            DataPointMinMaxMeanValue bufferValue = (DataPointMinMaxMeanValue) buffer.getValue();

            if(buffer.getTimestamp() == null || value.getTimestamp().isBefore(buffer.getTimestamp())){
                buffer.setTimestamp(value.getTimestamp());
            }

            if( v<bufferValue.getMin()){
                bufferValue.setMin(v);
            }

            if (v > bufferValue.getMax()){
                bufferValue.setMax(v);
            }

            // for calculating mean
            bufferValue.sumUp(v);
        };
    }

    @Override
    public BinaryOperator<DataPoint> combiner() {
        return (bufferOne, bufferTwo) ->{
            if(bufferTwo.getTimestamp().isBefore(bufferOne.getTimestamp())){
                bufferOne.setTimestamp(bufferTwo.getTimestamp());
            }

            DataPointMinMaxMeanValue bufferOneValue = (DataPointMinMaxMeanValue) bufferOne.getValue();
            DataPointMinMaxMeanValue bufferTwoValue = (DataPointMinMaxMeanValue) bufferTwo.getValue();

            if(bufferTwoValue.getMin() < bufferOneValue.getMin()){
                bufferOneValue.setMin(bufferTwoValue.getMin());
            }

            if(bufferTwoValue.getMax() > bufferOneValue.getMax()){
                bufferOneValue.setMax(bufferTwoValue.getMax());
            }

            bufferOneValue.setSum(bufferOneValue.getSum() + bufferTwoValue.getSum());
            bufferOneValue.setCount(bufferOneValue.getCount()+bufferTwoValue.getCount());

            return bufferOne;
        };
    }

    @Override
    public Function<DataPoint, DataPoint> finisher() {
        return buffer -> buffer;    // or Function.identity();
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Set.of(Characteristics.UNORDERED);
    }
}
