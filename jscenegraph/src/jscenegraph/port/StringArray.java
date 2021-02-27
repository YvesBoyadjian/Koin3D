/**
 * 
 */
package jscenegraph.port;

import jscenegraph.database.inventor.SbRotation;

/**
 * @author Yves Boyadjian
 *
 */
public class StringArray extends Indexable<String> {
	
	private int delta;
	private String[] values;

	public StringArray(int length) {
		values = new String[length];
		for(int i=0;i<length;i++) {
			values[i] = "";
		}
	}

	public StringArray(int start, StringArray stringArray) {
		delta = start + stringArray.delta;
		values = stringArray.values;
	}

	@Override
	public String getO(int index) {
		return values[delta + index];
	}

	@Override
	public int length() {
		return values.length - delta;
	}

	@Override
	public void setO(int i, String object) {
		values[i+delta] = object;
	}

	public StringArray plus(int start) {
		return new StringArray(start,this);
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
