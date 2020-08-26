/**
 * 
 */
package jscenegraph.port;

import jscenegraph.database.inventor.SbRotation;
import jscenegraph.database.inventor.SbRotationd;

/**
 * @author Yves Boyadjian
 *
 */
public class SbRotationdArray extends Indexable<SbRotationd> {
	
	private int start;
	private SbRotationd[] values;

	public SbRotationdArray(int length) {
		values = new SbRotationd[length];
		for(int i=0;i<length;i++) {
			values[i] = new SbRotationd();
		}
	}

	public SbRotationdArray(int start2, SbRotationdArray sbRotationArray) {
		this.start = start2 + sbRotationArray.start;
		this.values = sbRotationArray.values;
	}

	@Override
	public SbRotationd getO(int index) {
		return values[index + start];
	}

	@Override
	public int length() {
		return values.length - start;
	}

	@Override
	public void setO(int i, SbRotationd object) {
		values[i + start] = object;
		
	}

	public SbRotationdArray plus(int start2) {
		return new SbRotationdArray(start2,this);
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
