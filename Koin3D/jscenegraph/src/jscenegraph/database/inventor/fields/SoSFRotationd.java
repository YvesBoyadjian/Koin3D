/**
 * 
 */
package jscenegraph.database.inventor.fields;

import jscenegraph.database.inventor.SbRotationd;
import jscenegraph.database.inventor.SbVec3d;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoInput;

/**
 * @author Yves
 *
 */
public class SoSFRotationd extends SoSField<SbRotationd> { //TODO

	@Override
	protected SbRotationd constructor() {
		return new SbRotationd();
	}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets value from axis and angle. (Convenience function)
//
// Use: public

public void setValue(final SbVec3d axis,     // The axis
                       double angle)             // The angle (in radians)
{
    setValue(new SbRotationd(axis, angle));
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Reads value from file. Returns FALSE on error.
//
// Use: private

public boolean readValue(SoInput in)
//
////////////////////////////////////////////////////////////////////////
{
    final SbVec3d     axis = new SbVec3d();
    final double[]       angle = new double[1];

    if (! (in.read(axis.getRef()[0]) &&
           in.read(axis.getRef()[1]) &&
           in.read(axis.getRef()[2]) &&
           in.read(angle)))
        return false;

    setValue(axis, angle[0]);

    return true;
}

}
