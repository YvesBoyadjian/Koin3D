/**
 * 
 */
package jscenegraph.database.inventor.fields;

import jscenegraph.database.inventor.SoInput;

/**
 * @author Yves Boyadjian
 *
 */
public class SoMFDouble extends SoMField<Double> { //TODO

	/* (non-Javadoc)
	 * @see jscenegraph.database.inventor.fields.SoMField#constructor()
	 */
	@Override
	protected Double constructor() {
		return new Double(0);
	}

	/* (non-Javadoc)
	 * @see jscenegraph.database.inventor.fields.SoMField#arrayConstructor(int)
	 */
	@Override
	protected Double[] arrayConstructor(int length) {
		return new Double[length];
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
		values[index] = ret[0];
		return true;
	}
	return false;
}

}
