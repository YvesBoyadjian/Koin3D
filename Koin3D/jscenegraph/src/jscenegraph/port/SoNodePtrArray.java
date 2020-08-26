/**
 * 
 */
package jscenegraph.port;

import jscenegraph.database.inventor.SbMatrixd;

/**
 * @author Yves Boyadjian
 *
 */
public class SoNodePtrArray extends Indexable<SoNodePtr> {
	
	private SoNodePtr[] values;
	private int start;

	public SoNodePtrArray(int length) {
		values = new SoNodePtr[length];
		for (int i=0; i<length; i++) {
			values[i] = new SoNodePtr();
		}		
	}

	public SoNodePtrArray(int start2, SoNodePtrArray soNodePtrArray) {
		this.start = start2 + soNodePtrArray.start;
		this.values = soNodePtrArray.values;
	}

	@Override
	public SoNodePtr getO(int index) {
		return values[index+start];
	}

	@Override
	public int length() {
		return values.length - start;
	}

	@Override
	public void setO(int i, SoNodePtr object) {
		values[i+start].copyFrom(object);
		
	}

	public SoNodePtrArray plus(int start2) {
		return new SoNodePtrArray(start2,this);
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
