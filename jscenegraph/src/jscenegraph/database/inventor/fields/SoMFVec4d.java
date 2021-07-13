package jscenegraph.database.inventor.fields;

import jscenegraph.database.inventor.SbVec4d;
import jscenegraph.database.inventor.SbVec4f;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.port.*;
import jscenegraph.port.memorybuffer.DoubleMemoryBuffer;
import jscenegraph.port.memorybuffer.FloatMemoryBuffer;

import java.util.function.DoubleConsumer;

public class SoMFVec4d extends SoMField<SbVec4d, SbVec4dArray> {

    @Override
    protected SbVec4d constructor() {
        return new SbVec4d();
    }

    @Override
    protected SbVec4dArray arrayConstructor(int length) {
        return new SbVec4dArray(DoubleMemoryBuffer.allocateDoubles(length*4));
    }

    /* Get pointer into array of values
     *
     * Faster method
     *
     * */

    /**
     * Java port
     * @return
     */
    /* Get pointer into array of values
     *
     * Faster method
     *
     * */
    public DoubleArray getValuesArray(int start) {
        evaluate();

        return values.toDoubleArray().plus(start*4);
    }

    public void setValuesPointer(DoubleMemoryBuffer userdata) {
        makeRoom(0);
        if (userdata != null) {
            values = new SbVec4dArray(userdata);
            // userDataIsUsed = true; COIN3D
            num = maxNum = userdata.numDoubles()/4;
            valueChanged();
        }

    }

    ////////////////////////////////////////////////////////////////////////
    //
    // Description:
    // Reads one (indexed) value from file. Returns FALSE on error.
    //
    // Use: private

    public boolean read1Value(SoInput in, int index)
    //
    ////////////////////////////////////////////////////////////////////////
    {
        DoubleConsumer[] ref = getValuesSbVec4dArray().get(index).getRef();
        return (in.read(ref[0]) && in.read(ref[1]) && in.read(ref[2]) && in.read(ref[3]));
    }

    //! Set the \p index'th value to the given floating point values.
////////////////////////////////////////////////////////////////////////
//
//Description:
//Sets one vector value from 3 separate floats. (Convenience function)
//
//Use: public

    public void set1Value(int index, double x, double y, double z, double w) {
        set1Value(index, new SbVec4d(x, y, z, w));
    }

    /* Set field to have one value */
    public void setValue(SbVec4d newValue) {
        makeRoom(1);
        Mutable dest = values.getO(0);
        Mutable src = (Mutable) newValue;
        dest.copyFrom(src);
        valueChanged();
    }

    /* Set 1 value at given index */
    public void set1Value(int index, SbVec4d newValue) {
        if (index >= getNum())
            makeRoom(index + 1);
        values.setO(index,newValue);
        valueChanged();
    }

    public SbVec4dArray getValuesSbVec4dArray() {
        evaluate();

        return values;
    }
}
