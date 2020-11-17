package jscenegraph.database.inventor.fields;

import jscenegraph.database.inventor.SbVec3d;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.port.DoubleArray;
import jscenegraph.port.Mutable;
import jscenegraph.port.SbVec3dArray;
import jscenegraph.port.SbVec3fArray;
import jscenegraph.port.memorybuffer.DoubleMemoryBuffer;

import java.util.function.DoubleConsumer;

public class SoMFVec3d extends SoMField<SbVec3d, SbVec3dArray> {

    private DoubleArray floatArray;

    private DoubleMemoryBuffer valuesArray;

    public SoMFVec3d() {
        super();
        //allocValues(0);
    }

    /**
     * Sets the field to contain the given value and only the given value (if
     * the array had multiple values before, they are deleted).
     *
     * @param xyz
     */
    ////////////////////////////////////////////////////////////////////////
    //
    // Description:
    // Sets to one vector value from array of 3 floats. (Convenience function)
    //
    // Use: public

    public void setValue(final double[] xyz) {
        setValue(new SbVec3d(xyz));
    }

    /* Get pointer into array of values
     *
     * Faster method
     *
     * */
    public DoubleArray getValuesArray(int start) {
        evaluate();

        DoubleArray shiftedValues = new DoubleArray( start*3, valuesArray);
        return shiftedValues;
    }

    // Set values from array of arrays of 3 floats.

    //
    // Description:
    // Sets values from array of arrays of 3 floats. This can be useful
    // in some applications that have vectors stored in this manner and
    // want to keep them that way for efficiency.
    //
    // Use: public

    public void setValues(int start, // Starting index
                          double xyz[][]) // Array of vector values
    //
    {
        int num = xyz.length; // Number of values to set
        int newNum = start + num;
        int i;

        if (newNum > getNum())
            makeRoom(newNum);

        for (i = 0; i < num; i++) {
            valuesArray.setDouble((start + i)*3, xyz[i][0]);
            valuesArray.setDouble((start + i)*3+1, xyz[i][1]);
            valuesArray.setDouble((start + i)*3+2, xyz[i][2]);
        }
        valueChanged();
    }

    /**
     * Java port
     * @param start
     * @param xyz
     */
    public void setValues(int start, double[] xyz) {
        int num = xyz.length/3; // Number of values to set
        int newNum = start + num;
        int i;

        if (newNum > getNum())
            makeRoom(newNum);

        for (i = 0; i < num; i++) {
            valuesArray.setDouble((start + i)*3, xyz[3*i]);
            valuesArray.setDouble((start + i)*3+1, xyz[3*i+1]);
            valuesArray.setDouble((start + i)*3+2, xyz[3*i+2]);
        }
        valueChanged();
    }

    public void setValuesPointer(double[] userdata) {
        setValuesPointer(DoubleMemoryBuffer.allocateFromDoubleArray(userdata),false);
    }

    /**
     * Values in buffer must be already initialized
     * @param userdata
     */
    public void setValuesPointer(DoubleMemoryBuffer userdata) {
        setValuesPointer(userdata,true);
    }

    /**
     *
     * @param userdata
     * @param keepOwnership : specify false, if you want Koin3D to delete himself the userdata
     */
    public void setValuesPointer(DoubleMemoryBuffer userdata, boolean keepOwnership) {
        makeRoom(0);
        if (userdata != null) {
            valuesArray = userdata;
            values = new SbVec3dArray(valuesArray);
            if(keepOwnership) {
                userDataIsUsed = true;
            }
            num = maxNum = userdata.numDoubles()/3;
            valueChanged();
        }

    }

    ////////////////////////////////////////////////////////////////////////
    //
    // Description:
    // Sets values from array of arrays of 3 floats. This can be useful
    // in some applications that have vectors stored in this manner and
    // want to keep them that way for efficiency.
    //
    // Use: public

    public void setValues(int start, // Starting index
                          int num, // Number of values to set
                          final double xyz[][/* 3 */]) // Array of vector values
    //
    ////////////////////////////////////////////////////////////////////////
    {
        int newNum = start + num;
        int i;

        if (newNum > getNum())
            makeRoom(newNum);

        for (i = 0; i < num; i++) {
            valuesArray.setDouble((start + i)*3, xyz[i][0]);
            valuesArray.setDouble((start + i)*3+1, xyz[i][1]);
            valuesArray.setDouble((start + i)*3+2, xyz[i][2]);
        }
        valueChanged();
    }

    /**
     * java port
     *
     * @param start
     * @param num
     * @param xyz3d
     */
    public void setValues(int start, int num, double[][][] xyz3d) {
        int xyzLength = 0;
        int xyz3dLength = xyz3d.length;
        for (int i = 0; i < xyz3dLength; i++) {
            if (xyz3d[i] != null) {
                xyzLength += xyz3d[i].length;
            }
        }
        double[][] xyz = new double[xyzLength][];
        int j = 0;
        for (int i = 0; i < xyz3dLength; i++) {
            if (xyz3d[i] != null) {
                double[][] iArray = xyz3d[i];
                int iLength = iArray.length;
                for (int k = 0; k < iLength; k++) {
                    xyz[j] = xyz3d[i][k];
                    j++;
                }
            }
        }
        setValues(start, num, xyz);
    }

    @Override
    protected SbVec3d constructor() {
        return new SbVec3d();
    }

    @Override
    protected SbVec3dArray arrayConstructor(int length) {
        return new SbVec3dArray(DoubleMemoryBuffer.allocateDoubles(length*3));
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
        DoubleConsumer[] ref = getValuesSbVec3dArray().getFast(index).getRef();
        return (in.read(ref[0]) && in.read(ref[1]) && in.read(ref[2]));
    }

    //! Set the \p index'th value to the given floating point values.
////////////////////////////////////////////////////////////////////////
//
//Description:
//Sets one vector value from 3 separate floats. (Convenience function)
//
//Use: public

    public void set1Value(int index, double x, double y, double z) {
        set1Value(index, new SbVec3d(x, y, z));
    }

    protected void allocValues(int newnum) {
        super.allocValues(newnum);
        valuesArray = (values != null ? values.getValuesArray() : null);
    }


    /* Set field to have one value */
    public void setValue(SbVec3d newValue) {
        makeRoom(1);
        Mutable dest = new SbVec3d(valuesArray,0);
        Mutable src = (Mutable) newValue;
        dest.copyFrom(src);
        valueChanged();
    }


    /* Set 1 value at given index */
    public void set1Value(int index, SbVec3d newValue) {
        if (index >= getNum())
            makeRoom(index + 1);
        valuesArray.setDouble(index*3, newValue.getX());
        valuesArray.setDouble(index*3+1, newValue.getY());
        valuesArray.setDouble(index*3+2, newValue.getZ());
        valueChanged();
    }

    public SbVec3d operator_square_bracket(int i) {
        evaluate();
        return new SbVec3d(valuesArray,i*3);
    }

    public SbVec3dArray getValuesSbVec3dArray() {
        evaluate();

        return values;
    }


    public DoubleArray getValuesDoubleArray() {
        evaluate();

        if( floatArray == null || floatArray.getValuesArray() != valuesArray ) {
            floatArray = new DoubleArray(0,valuesArray);
        }
        return floatArray;
    }
}
