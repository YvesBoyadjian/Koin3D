/**
 * 
 */
package jscenegraph.port;

/**
 * @author Yves Boyadjian
 *
 */
public class BoolArray extends Indexable<Boolean> {
	
	Boolean[] values;
	int start;

	public BoolArray(int start, Boolean[] values2) {
		this.start = start;
		this.values = values2;
	}

	public BoolArray(int start2, BoolArray values2) {
		this.start = start2 + values2.start;
		this.values = values2.values;
	}

	public BoolArray(int length) {
		values = new Boolean[length];
		for(int i=0;i<length;i++) {
			values[i] = false;
		}
	}

	@Override
	public Boolean getO(int index) {
		return values[index+start];
	}

	@Override
	public int length() {
		return values.length - start;
	}

	public boolean get(int index) {
		return getO(index);
	}

	@Override
	public void setO(int i, Boolean object) {
		values[i+start] = object;
	}

	@Override
	public Indexable<Boolean> plus(int delta) {
		return new BoolArray(delta,this);
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
