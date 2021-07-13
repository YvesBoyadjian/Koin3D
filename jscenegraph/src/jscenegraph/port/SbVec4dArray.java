package jscenegraph.port;

import jscenegraph.database.inventor.SbVec4d;
import jscenegraph.database.inventor.SbVec4f;
import jscenegraph.database.inventor.SbVec4fSingle;
import jscenegraph.port.memorybuffer.DoubleMemoryBuffer;
import jscenegraph.port.memorybuffer.FloatMemoryBuffer;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

public class SbVec4dArray extends Indexable<SbVec4d> implements DoubleBufferAble, ByteBufferAble {

    DoubleMemoryBuffer valuesArray;

    int delta;

    public SbVec4dArray(SbVec4dArray other, int delta) {
        valuesArray = other.valuesArray;
        this.delta = other.delta + delta;
    }

    public SbVec4dArray(DoubleMemoryBuffer valuesArray) {
        this.valuesArray = valuesArray;
    }
/*
    public SbVec4dArray(SbVec4dSingle singleSbVec4d) {
        valuesArray = singleSbVec4d.getValueBuffer();
    }
*/
    public SbVec4d get(int index) {
        return new SbVec4d(valuesArray, (index+delta)*4);
    }

    public SbVec4dArray plus(int delta) {
        return new SbVec4dArray(this,delta);
    }

    public static SbVec4dArray allocate(int maxPoints) {
        return new SbVec4dArray(DoubleMemoryBuffer.allocateDoubles(maxPoints*4));
    }

    public DoubleArray toDoubleArray() {
        return new DoubleArray(delta*4,valuesArray);
    }

    public DoubleMemoryBuffer toDouble() {

        if(delta != 0) {
            throw new IllegalStateException();
        }

        return valuesArray;
    }

    @Override
    public DoubleBuffer toDoubleBuffer() {
        DoubleBuffer fb = valuesArray.toByteBuffer().asDoubleBuffer();
        fb.position(delta*4);

        return fb;

        //return FloatBuffer.wrap(valuesArray,delta*4,valuesArray.length - delta*4);
    }

    public static SbVec4dArray copyOf(SbVec4dArray coords4) {
        if(coords4 == null)
            return null;
        return new SbVec4dArray(coords4,0);
    }

    @Override
    public SbVec4d getO(int index) {
        return get(index);
    }

    @Override
    public int length() {
        return valuesArray.numDoubles()/4 - delta;
    }

    @Override
    public void setO(int i, SbVec4d object) {
        getO(i).copyFrom(object);

    }

    @Override
    public int delta() {
        return delta;
    }

    @Override
    public Object values() {
        return valuesArray;
    }

    @Override
    public ByteBuffer toByteBuffer() {
        ByteBuffer bb = valuesArray.toByteBuffer();

        int offset = delta*4*Double.BYTES;

        bb.position(offset);

        return bb;
    }
}
