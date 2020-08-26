/**
 * 
 */
package jscenegraph.port;

/**
 * @author Yves Boyadjian
 *
 */
public class DoubleArray extends Indexable<Double> {
	
	int start;
	Double[] values;

	public DoubleArray(int start, Double[] values) {
		this.start = start;
		this.values = values;
	}

	public DoubleArray(int length) {
		values = new Double[length];
		for(int i=0; i<length;i++) {
			values[i] = 0.;
		}
	}

	public DoubleArray(int start2, DoubleArray values2) {
		this.start = start2 + values2.start;
		this.values = values2.values;
	}

	@Override
	public Double getO(int index) {
		return values[index+start];
	}

	@Override
	public int length() {
		return values.length - start;
	}

	@Override
	public void setO(int i, Double object) {
		values[i+start] = object;
	}

	@Override
	public Indexable<Double> plus(int delta) {
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
