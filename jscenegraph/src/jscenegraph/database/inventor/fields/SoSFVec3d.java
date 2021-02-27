package jscenegraph.database.inventor.fields;

import jscenegraph.database.inventor.SbVec3d;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoInput;

public class SoSFVec3d  extends SoSField<SbVec3d> {

    // Sets the field to the given value.
    //
    // Description:
    //    Sets value from 3 separate floats. (Convenience function)
    //
    // Use: public

    public void setValue(float x, float y, float z) {
        setValue(new SbVec3d(x, y, z));
    }

    @Override
    protected SbVec3d constructor() {
        return new SbVec3d();
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
                in.read(value.getRef()[2]));
    }

    public void setValue(double[] vector) { // java port
        setValue(new SbVec3d(vector));
    }

    /**
     * Java port
     * @param vector
     */
    public void setValue(SbVec3f vector) {
        float[] value = vector.getValueRead();
        double[] valuef = new double[3];
        valuef[0] = (float)value[0];
        valuef[1] = (float)value[1];
        valuef[2] = (float)value[2];
        setValue(valuef);
    }

}
