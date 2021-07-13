/**
 * 
 */
package jscenegraph.port;

import jscenegraph.port.memorybuffer.DoubleMemoryBuffer;
import jscenegraph.port.memorybuffer.FloatMemoryBuffer;

/**
 * @author Yves Boyadjian
 *
 */
public class DoubleArray extends Indexable<Double> {
	
	int start;
	DoubleMemoryBuffer values;

	public DoubleArray(int start, DoubleMemoryBuffer values) {
		this.start = start;
		this.values = values;
	}

	public DoubleArray(int length) {
		values = DoubleMemoryBuffer.allocateDoubles(length);
	}

	public DoubleArray(int start2, DoubleArray values2) {
		this.start = start2 + values2.start;
		this.values = values2.values;
	}

	public DoubleMemoryBuffer getValuesArray() {
		return values;
	}

	public DoubleMemoryBuffer getValues() {
		return values;
	}

	public int getStart() {
		return start;
	}

	@Override
	public Double getO(int index) {
		return values.getDouble(index+start);
	}

	@Override
	public int length() {
		return values.numDoubles() - start;
	}

	@Override
	public void setO(int i, Double object) {
		set(i,(double) object);
	}

	public void set(int index, double value) {
		values.setDouble(index+start, value);
	}

	@Override
	public DoubleArray plus(int delta) {
		return new DoubleArray(delta,this);
	}

	@Override
	public int delta() {
		return start;
	}

	@Override
	public Object values() {
		return values;
	}

}
