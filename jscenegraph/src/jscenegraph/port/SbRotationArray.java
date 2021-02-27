/**
 * 
 */
package jscenegraph.port;

import jscenegraph.database.inventor.SbMatrixd;
import jscenegraph.database.inventor.SbRotation;

/**
 * @author Yves Boyadjian
 *
 */
public class SbRotationArray extends Indexable<SbRotation> {
	
	private int start;
	private SbRotation[] values;

	public SbRotationArray(int length) {
		values = new SbRotation[length];
		for(int i=0;i<length;i++) {
			values[i] = new SbRotation();
		}
	}

	public SbRotationArray(int start2, SbRotationArray sbRotationArray) {
		this.start = start2 + sbRotationArray.start;
		this.values = sbRotationArray.values;
	}

	@Override
	public SbRotation getO(int index) {
		return values[index + start];
	}

	@Override
	public int length() {
		return values.length - start;
	}

	@Override
	public void setO(int i, SbRotation object) {
		values[i + start] = object;
		
	}

	public SbRotationArray plus(int start2) {
		return new SbRotationArray(start2,this);
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
