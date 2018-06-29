/**
 * 
 */
package jscenegraph.database.inventor.fields;

import jscenegraph.database.inventor.SbTime;
import jscenegraph.database.inventor.SoInput;

/**
 * @author Yves Boyadjian
 *
 */
public class SoMFTime extends SoMField<SbTime> { //TODO

	@Override
	protected SbTime constructor() {
		return new SbTime();
	}

	@Override
	protected SbTime[] arrayConstructor(int length) {		
		return new SbTime[length];
	}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Reads one (indexed) value from file. Returns FALSE on error.
//
// Use: private

public boolean read1Value(SoInput in, int index)
//
////////////////////////////////////////////////////////////////////////
{
    final double[]      seconds = new double[1];

    if (! in.read(seconds))
        return false;

    ((SbTime)values[index]).setValue(seconds[0]);

    return true;
}

}
