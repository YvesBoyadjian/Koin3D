package jscenegraph.database.inventor.fields;

import jscenegraph.database.inventor.SbVec4d;
import jscenegraph.database.inventor.SbVec4f;
import jscenegraph.database.inventor.SoInput;

public class SoSFVec4d extends SoSField<SbVec4d> {

    // Sets the field to the given value.
    //
    // Description:
    //    Sets value from 3 separate floats. (Convenience function)
    //
    // Use: public

    public void setValue(double x, double y, double z, double w) {
        setValue(new SbVec4d(x, y, z, w));
    }

    @Override
    protected SbVec4d constructor() {
        return new SbVec4d();
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
        return (in.read(value.getRef()[0]) &&
                in.read(value.getRef()[1]) &&
                in.read(value.getRef()[2]) &&
                in.read(value.getRef()[3]));
    }

    public void setValue(double[] vector) { // java port
        setValue(new SbVec4d(vector));
    }
}
