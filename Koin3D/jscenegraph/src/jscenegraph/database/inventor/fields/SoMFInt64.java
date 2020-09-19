/**
 * 
 */
package jscenegraph.database.inventor.fields;

import jscenegraph.database.inventor.SoInput;
import jscenegraph.port.LongArray;

/**
 * @author Yves Boyadjian
 *
 */
public class SoMFInt64 extends SoMField<Long,LongArray> {

	/* (non-Javadoc)
	 * @see jscenegraph.database.inventor.fields.SoMField#read1Value(jscenegraph.database.inventor.SoInput, int)
	 */
	@Override
	public boolean read1Value(SoInput in, int index) {
		final long[] ret = new long[1];
	    if (in.read(ret)) {
	    	values.set(index, ret[0]);
	    	return true;
	    }
		return false;
	}

	/* (non-Javadoc)
	 * @see jscenegraph.database.inventor.fields.SoMField#constructor()
	 */
	@Override
	protected Long constructor() {
		return 0l;
	}

	/* (non-Javadoc)
	 * @see jscenegraph.database.inventor.fields.SoMField#arrayConstructor(int)
	 */
	@Override
	protected LongArray arrayConstructor(int length) {
		return new LongArray(length);
	}

}
