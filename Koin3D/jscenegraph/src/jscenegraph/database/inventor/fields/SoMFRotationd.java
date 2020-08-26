/**
 * 
 */
package jscenegraph.database.inventor.fields;

import jscenegraph.database.inventor.SbRotationd;
import jscenegraph.database.inventor.SbVec3d;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.port.SbRotationdArray;

/**
 * @author Yves Boyadjian
 *
 */
public class SoMFRotationd extends SoMField<SbRotationd,SbRotationdArray> { //TODO

	@Override
	protected SbRotationd constructor() {
		return new SbRotationd();
	}

	@Override
	protected SbRotationdArray arrayConstructor(int length) {
		return new SbRotationdArray(length);
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
    final SbVec3d     axis = new SbVec3d();
    final double[]      angle = new double[1];

    if (! (in.read(axis.getRef()[0]) &&
           in.read(axis.getRef()[1]) &&
           in.read(axis.getRef()[2]) &&
           in.read(angle)))
        return false;

    set1Value(index, axis, angle[0]);

    return true;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets one rotation value from an axis and angle. (Convenience function)
//
// Use: public

public void set1Value(int index, final SbVec3d axis, double angle)
//
////////////////////////////////////////////////////////////////////////
{
    set1Value(index, new SbRotationd(axis, angle));
}

@Override
public SbRotationdArray doGetValues(int start) {
	return values.plus(start);
}

}
