/**
 * 
 */
package jscenegraph.port;

import jscenegraph.database.inventor.SbMatrix;

/**
 * @author Yves Boyadjian
 *
 */
public class SbMatrixArray extends Indexable<SbMatrix> {
	
	private SbMatrix[] values;
	private int start;

	public SbMatrixArray(int length) {
		values = new SbMatrix[length];
		for(int i=0; i<length;i++) {
			values[i] = new SbMatrix();
		}
	}

	public SbMatrixArray(int start, SbMatrix[] values2) {
		this.values = values2;
		this.start = start;
	}

	@Override
	public SbMatrix getO(int index) {
		return values[index+start];
	}

	@Override
	public int length() {
		return values.length - start;
	}

	@Override
	public void setO(int index, SbMatrix object) {
		values[index+start].copyFrom(object);
	}

	public SbMatrixArray plus(int start) {
		return new SbMatrixArray(start,values);
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
