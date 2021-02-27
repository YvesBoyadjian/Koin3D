/**
 * 
 */
package jscenegraph.port;

import jscenegraph.database.inventor.SbRotation;
import jscenegraph.database.inventor.SbTime;

/**
 * @author Yves Boyadjian
 *
 */
public class SbTimeArray extends Indexable<SbTime> {
	
	private int delta;
	private SbTime[] values;

	public SbTimeArray(int length) {
		values = new SbTime[length];
		for(int i=0; i<length; i++) {
			values[i] = new SbTime();
		}
	}

	public SbTimeArray(int start, SbTimeArray sbTimeArray) {
		delta = start + sbTimeArray.delta;
		values = sbTimeArray.values;
	}

	@Override
	public SbTime getO(int index) {
		return values[index + delta];
	}

	@Override
	public int length() {
		return values.length - delta;
	}

	@Override
	public void setO(int i, SbTime object) {
		getO(i).copyFrom(object);
	}

	public SbTimeArray plus(int start) {
		return new SbTimeArray(start,this);
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
