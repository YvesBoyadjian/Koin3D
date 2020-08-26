/**
 * 
 */
package jscenegraph.database.inventor.fields;

import jscenegraph.database.inventor.SoInput;
import jscenegraph.port.DoubleArray;

/**
 * @author Yves Boyadjian
 *
 */
public class SoMFDouble extends SoMField<Double,DoubleArray> { //TODO

	/* (non-Javadoc)
	 * @see jscenegraph.database.inventor.fields.SoMField#constructor()
	 */
	@Override
	protected Double constructor() {
		return 0.;
	}

	/* (non-Javadoc)
	 * @see jscenegraph.database.inventor.fields.SoMField#arrayConstructor(int)
	 */
	@Override
	protected DoubleArray arrayConstructor(int length) {
		return new DoubleArray(length);
	}


////////////////////////////////////////////////////////////////////////
//
//Description:
//Reads one (indexed) value from file. Returns FALSE on error.
//
//Use: private

public boolean read1Value(SoInput in, int index)
//
////////////////////////////////////////////////////////////////////////
{
	final double[] ret = new double[1]; 
	if( in.read(ret)) {
		values.setO(index, ret[0]);
		return true;
	}
	return false;
}

@Override
public DoubleArray doGetValues(int start) {
	return new DoubleArray(start,values);
}

}
