/**
 * 
 */
package jscenegraph.port;

import jscenegraph.database.inventor.engines.SoCalculator.Expression;

/**
 * @author Yves Boyadjian
 *
 */
public class ObjectArray<T extends Object> extends Indexable<T> {
	
	int start;
	T[] values;

	public ObjectArray(int start, T[] values) {
		this.start = start;
		this.values = values;
	}

	public ObjectArray(int start2, ObjectArray<T> values2) {
		this.start = start2 + values2.start;
		this.values = values2.values;
	}

	@Override
	public T getO(int index) {
		return values[start+index];
	}

	@Override
	public int length() {
		return values.length - start;
	}

	@Override
	public void setO(int i, T object) {
		values[i+start] = object;
	}

	@Override
	public Indexable<T> plus(int delta) {
		return new ObjectArray(delta,this);
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
