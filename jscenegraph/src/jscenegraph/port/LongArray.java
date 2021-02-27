/**
 * 
 */
package jscenegraph.port;

/**
 * @author Yves Boyadjian
 *
 */
public class LongArray extends Indexable<Long> {
	
	int start;
	long[] values;

	public LongArray(int start, long[] values) {
		this.start = start;
		this.values = values;
	}

	public LongArray(int length) {
		values = new long[length];
		for(int i=0;i<length;i++) {
			values[i] = 0;
		}
	}

	public LongArray(int start2, LongArray values2) {
		this.start = start2 + values2.start;
		this.values = values2.values;
	}

	@Override
	public Long getO(int index) {
		return values[index+start];
	}

	@Override
	public int length() {
		return values.length - start;
	}

	@Override
	public void setO(int i, Long object) {
		values[i+start] = object;
	}

	public void set(int i, long value) {
		values[i+start] = value;
	}

	public long get(int index) {
		return values[index+start];
	}

	public LongArray plus(int start2) {
		return new LongArray(start2,this);
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
