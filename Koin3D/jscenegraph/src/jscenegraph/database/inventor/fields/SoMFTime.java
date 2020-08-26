/**
 * 
 */
package jscenegraph.database.inventor.fields;

import jscenegraph.database.inventor.SbTime;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.port.SbTimeArray;

/**
 * @author Yves Boyadjian
 *
 */
public class SoMFTime extends SoMField<SbTime,SbTimeArray> { //TODO

	@Override
	protected SbTime constructor() {
		return new SbTime();
	}

	@Override
	protected SbTimeArray arrayConstructor(int length) {		
		return new SbTimeArray(length);
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

    ((SbTime)values.getO(index)).setValue(seconds[0]);

    return true;
}

@Override
public SbTimeArray doGetValues(int start) {
	return values.plus(start);
}

}
