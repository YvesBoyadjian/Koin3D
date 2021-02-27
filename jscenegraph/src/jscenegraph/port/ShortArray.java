/**
 * 
 */
package jscenegraph.port;

import jscenegraph.database.inventor.SbRotation;

/**
 * @author Yves Boyadjian
 *
 */
public class ShortArray extends Indexable<Short> {
	
	private int delta;
	private short[] values;

	public ShortArray(int length) {
		values = new short[length];
	}

	public ShortArray(int start, ShortArray shortArray) {
		delta = start+shortArray.delta;
		values = shortArray.values;
	}

	@Override
	public Short getO(int index) {		
		return values[index + delta];
	}

	@Override
	public int length() {
		return values.length - delta;
	}

	@Override
	public void setO(int i, Short object) {
		values[i + delta] = object;
	}

	public ShortArray plus(int start) {
		return new ShortArray(start,this);
	}

	@Override
	public int delta() {
		return delta;
	}

	@Override
	public Object values() {
		return values;
	}

}
