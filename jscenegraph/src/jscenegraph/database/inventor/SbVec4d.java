package jscenegraph.database.inventor;

import jscenegraph.port.Mutable;
import jscenegraph.port.memorybuffer.DoubleMemoryBuffer;
import jscenegraph.port.memorybuffer.FloatMemoryBuffer;

import java.util.function.DoubleConsumer;

public class SbVec4d implements Mutable {

    protected DoubleMemoryBuffer vec4;         //!< Storage for vector components
    protected int indice;

    public static int sizeof() {
        return 4*4;
    }

    //! Default constructor.
    public SbVec4d()                                           {
        vec4 = DoubleMemoryBuffer.allocateDoubles(4);
        indice = 0;
    }

    /**
     * Internal contructor
     * @param array
     * @param indice
     */
    public SbVec4d(DoubleMemoryBuffer array, int indice) {
        vec4 = array;
        this.indice = indice;
    }

    //! Constructor given vector components.
    public SbVec4d(	final double[] v)                           {
        vec4 = DoubleMemoryBuffer.allocateDoubles(4);
        indice = 0;
        setValue(v);
    }

    //! Constructor given vector components.
    public SbVec4d(double x, double y, double z, double w)       {
        vec4 = DoubleMemoryBuffer.allocateDoubles(4);
        indice = 0;
        setValue(x, y, z, w);
    }




    public SbVec4d(SbVec4d value) {
        vec4 = DoubleMemoryBuffer.allocateDoubles(4);
        indice = 0;
        copyFrom(value);
    }

    /**
     * Internal method
     * @param i
     * @return
     */
    protected double g(int i) {
        return vec4.getDouble(indice+i);
    }

    /**
     * Internal method
     * @param i
     * @param v
     */
    protected void s(int i, double v) {
        vec4.setDouble(indice+i, v);
    }

    //! Accesses indexed component of vector
    public double operator_square_bracket(int i)            {
        return g(i);
    }

    public void operator_square_bracket(int i, double value) {
        s(i, value);
    }

//
// Component-wise binary scalar multiplication operator
//

    public SbVec4d
    operator_mul( double d)
    {
        SbVec4d v = this;
        return new SbVec4d(v.g(0) * d, v.g(1) * d, v.g(2) * d, v.g(3) * d);
    }

//
// Component-wise binary vector addition operator
//

    public SbVec4d
    operator_add(SbVec4d v2)
    {
        SbVec4d v1 = this;
        return new SbVec4d(v1.g(0) + v2.g(0),
                v1.g(1) + v2.g(1),
                v1.g(2) + v2.g(2),
                v1.g(3) + v2.g(3));
    }



    public void copyFrom(SbVec4d t) {
        s(0, t.g(0));
        s(1, t.g(1));
        s(2, t.g(2));
        s(3, t.g(3));
    }

    // Returns vector components for reading only.
    public final double[] getValueRead() {
        double[] vecRead = new double[4];
        vecRead[0] = g(0);
        vecRead[1] = g(1);
        vecRead[2] = g(2);
        vecRead[3] = g(3);
        return vecRead;
    }


    protected final double[] getValueRef() {
        if( indice != 0) {
            throw new IllegalStateException();
        }
        return vec4.toDoubleArray();
    }


//
// Sets value of vector from array of 4 components
//

    public SbVec4d
    setValue(final double[] v)
    {
        s(0, v[0]);
        s(1, v[1]);
        s(2, v[2]);
        s(3, v[3]);

        return (this);
    }

//
// Sets value of vector from 4 individual components
//

    public SbVec4d
    setValue(double x, double y, double z, double w)
    {
        s(0, x);
        s(1, y);
        s(2, z);
        s(3, w);

        return (this);
    }


//
// Component-wise binary vector subtraction operator
//

    public SbVec4d
    operator_minus(final SbVec4d v2)
    {
        final SbVec4d v1 = this;
        return new SbVec4d(v1.g(0) - v2.g(0),
                v1.g(1) - v2.g(1),
                v1.g(2) - v2.g(2),
                v1.g(3) - v2.g(3));
    }


//
// Equality comparison operator within a tolerance.
//

    public boolean
    equals(final SbVec4d v, double tolerance)
    {
        final SbVec4d     diff = this.operator_minus(v);

        return diff.dot(diff) <= tolerance;
    }


//
// Returns dot (inner) product of vector and another vector
//

    public double
    dot(final SbVec4d v)
    {
        return g(0) * v.g(0) + g(1) * v.g(1) +
                g(2) * v.g(2) + g(3) * v.g(3) ;
    }

    @Override
    public void copyFrom(Object other) {
        copyFrom((SbVec4d)other);
    }

    /**
     * java port
     * @return
     */
    public DoubleConsumer[] getRef() {
        DoubleConsumer[] ref = new DoubleConsumer[4];
        ref[0] = value -> s(0, value);
        ref[1] = value -> s(1, value);
        ref[2] = value -> s(2, value);
        ref[3] = value -> s(3, value);
        return ref;
    }

    /*!
      Returns the vector as a Cartesian 3D vector in \a v. This means that
      the 3 first components x, y and z will be divided by the fourth, w.
    */
    public void getReal(SbVec3d v) {
        v.setValue(g(0)/g(3), g(1)/g(3), g(2)/g(3));
    }

    public void setValue(int i, double f) {
        s(i,f);
    }

    public double getX() {
        return g(0);
    }

    public double getY() {
        return g(1);
    }

    public double getZ() {
        return g(2);
    }

    public double getW() {
        return g(3);
    }

    public double[] toDoubleGL() {
        return getValueRead();
    }
}
