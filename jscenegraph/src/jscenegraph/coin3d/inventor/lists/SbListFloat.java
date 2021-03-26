package jscenegraph.coin3d.inventor.lists;

import jscenegraph.port.*;

import java.util.Arrays;

public class SbListFloat implements Destroyable, Mutable {

    private static final int DEFAULTSIZE = 4;

    private int itembuffersize;
    private int numitems;

    private float[] itembuffer;
    private final float[] builtinbuffer = new float[DEFAULTSIZE];

//    private FloatArrayPtr internalFloatArrayPtr;

    /**
     *
     */
    public SbListFloat() {
        super();
        itembuffersize = DEFAULTSIZE;
        itembuffer = builtinbuffer;
    }

    public SbListFloat(int sizehint) {
        super();
        itembuffersize = DEFAULTSIZE;
        numitems = 0;
        itembuffer = builtinbuffer;
        if(sizehint > DEFAULTSIZE) {
            this.grow(sizehint);
        }
    }

    public int getLength() {
        return this.numitems;
    }

    public int size() {
        return getLength();
    }

//    public FloatArrayPtr getArrayPtr() {
//        return getArrayPtr(0);
//    }
//    public FloatArrayPtr getArrayPtr(int start) {
//        if(internalFloatArrayPtr == null) {
//            internalFloatArrayPtr = new FloatArrayPtr(0,itembuffer);
//        }
//        return new FloatArrayPtr(start,internalFloatArrayPtr);
//    }

    // java port
    public void truncate(int length) {
        truncate(length,0);
    }
    public void truncate( int length, int dofit) {
        //#ifdef COIN_EXTRA_DEBUG
        assert(length <= this.numitems);
        //#endif // COIN_EXTRA_DEBUG
        this.numitems = length;
        if (dofit != 0) this.fit();
    }

    public void fit() {
        final int items = this.numitems;

        if (items < this.itembuffersize) {
            float[] newitembuffer = this.builtinbuffer;
            boolean copyDone = false;
            if (items > DEFAULTSIZE) {
                newitembuffer = Arrays.copyOf(this.itembuffer, items);
                copyDone = true;
            }

            if (newitembuffer != this.itembuffer && !copyDone) {
                for (int i = 0; i < items; i++) newitembuffer[i] = this.itembuffer[i];
            }

            this.itembuffer = newitembuffer;
            this.itembuffersize = items > DEFAULTSIZE ? items : DEFAULTSIZE;
//            internalFloatArrayPtr = null; // invalidate internal IntArrayPtr
        }
    }

    public void append(float item) {
        if (this.numitems == this.itembuffersize) this.grow();
        this.itembuffer[this.numitems++] = item;
//        internalFloatArrayPtr = null; // invalidate internal IntArrayPtr
    }

    public void add(float item) {
        append(item);
    }

    private void grow() {
        grow(-1);
    }
    private void grow(int size) {
        // Default behavior is to double array size.
        if (size == -1) this.itembuffersize <<= 1;
        else if (size <= this.itembuffersize) return;
        else { this.itembuffersize = size; }

        float[] newbuffer = Arrays.copyOf(this.itembuffer,this.itembuffersize);
        this.itembuffer = newbuffer;
//        internalFloatArrayPtr = null; // invalidate internal IntArrayPtr
    }

    public void truncate(int length, boolean b) {
        truncate(length, b ? 1 :0);
    }

    public float operator_square_bracket(int i) {
        if(i<numitems)
            return itembuffer[i];
        throw new IllegalArgumentException();
    }

    public float get(int i) {
        return operator_square_bracket(i);
    }

    @Override
    public void destructor() {
        numitems = -1;
        itembuffersize = -1;
        itembuffer = null;
//        Destroyable.delete(internalFloatArrayPtr);
//        internalFloatArrayPtr = null;
    }

    @Override
    public void copyFrom(Object other) {
        SbListFloat otherList = (SbListFloat)other;
        copy(otherList);
    }

    /*!
      Make this list a copy of \a l.
    */
    public void copy(final SbListFloat l)
    {
        if (this == l) return;
        int n = l.numitems;
        this.expand(n);
        for (int i = 0; i < n; i++) this.itembuffer[i] = l.itembuffer[i];
    }

    // java port
    public void operator_square_bracket(int i, float object) {
//        if (i >= size()) grow(i);
        itembuffer[i] = object;
    }

    protected void expand(int size) {
        this.grow(size);
        this.numitems = size;
    }

    public  void set(int i, float j) { itembuffer[i] = j; }

}
