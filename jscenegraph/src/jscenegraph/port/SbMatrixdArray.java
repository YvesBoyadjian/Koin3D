/**
 * 
 */
package jscenegraph.port;

import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbMatrixd;

/**
 * @author Yves Boyadjian
 *
 */
public class SbMatrixdArray extends Indexable<SbMatrixd> {
	
	private SbMatrixd[] values;
	private int start;

	public SbMatrixdArray(int length) {
		values = new SbMatrixd[length];
		for(int i=0; i<length;i++) {
			values[i] = new SbMatrixd();
		}
	}

	public SbMatrixdArray(int start, SbMatrixd[] values2) {
		this.values = values2;
		this.start = start;
	}

	@Override
	public SbMatrixd getO(int index) {
		return values[index+start];
	}

	@Override
	public int length() {
		return values.length - start;
	}

	@Override
	public void setO(int index, SbMatrixd object) {
		values[index+start].copyFrom(object);
	}

	public SbMatrixdArray plus(int start) {
		return new SbMatrixdArray(start,values);
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
