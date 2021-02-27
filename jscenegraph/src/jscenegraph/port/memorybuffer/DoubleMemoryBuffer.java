package jscenegraph.port.memorybuffer;

import jscenegraph.mevis.inventor.misc.SoVBO;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

public class DoubleMemoryBuffer extends MemoryBuffer {

    public final static int MINIMUM_DOUBLES_FOR_BUFFER = SoVBO.getVertexCountMinLimit() * 3;

    private double[] doubleArray;

    private boolean byteBufferDirty;

    private DoubleBuffer dummyDoubleBuffer;

    DoubleMemoryBuffer() {
        super();
    }

    public int numBytes() {
        if(byteBuffer != null) {
            return byteBuffer.capacity();
        }
        else {
            return doubleArray.length * Double.BYTES;
        }
    }

    public int numDoubles() {
        return numBytes()/Double.BYTES;
    }

    public static final DoubleMemoryBuffer allocateDoubles(int numDoubles) {

        DoubleMemoryBuffer memoryBuffer = new DoubleMemoryBuffer();

        if( numDoubles >= MINIMUM_DOUBLES_FOR_BUFFER) {

            int numBytes = numDoubles*Double.BYTES;

            memoryBuffer.byteBuffer = BufferUtils.createByteBuffer(numBytes);
        }
        else {
            memoryBuffer.doubleArray = new double[numDoubles];
        }

        return memoryBuffer;
    }

    public static final DoubleMemoryBuffer allocateDoublesMalloc(int numDoubles) {

        DoubleMemoryBuffer memoryBuffer = new DoubleMemoryBuffer();

        if( numDoubles >= MINIMUM_DOUBLES_FOR_BUFFER) {

            int numBytes = numDoubles*Double.BYTES;

            memoryBuffer.byteBuffer = MemoryUtil.memAlloc(numBytes);
            memoryBuffer.malloc = true;
        }
        else {
            memoryBuffer.doubleArray = new double[numDoubles];
        }

        return memoryBuffer;
    }

    /**
     * Note : array data is copied
     * @param array
     * @return
     */
    public static DoubleMemoryBuffer allocateFromDoubleArray(double[] array) {

        int numDoubles = array.length;
        //int numBytes = numFloats * Float.BYTES;

        DoubleMemoryBuffer memoryBuffer = allocateDoubles(numDoubles);
        memoryBuffer.setDoubles(array, numDoubles);

        return memoryBuffer;
    }

    public static final void arraycopy(
            DoubleMemoryBuffer src,
            int srcPos,
            DoubleMemoryBuffer dest,
            int destPos,
            int length
    ) {
        for(int i=0; i<length; i++) {
            dest.setDouble(i+destPos,src.getDouble(i+srcPos));
        }
    }

    public void setDoubles(double[] srcFloats, int numFloats) {

        if( byteBuffer != null ) {
            updateByteBuffer();
            for( int index = 0; index < numFloats; index++) {
                byteBuffer.putDouble(index * Double.BYTES,srcFloats[index]);
            }
        }
        else {
            for( int index = 0; index < numFloats; index++) {
                doubleArray[index] = srcFloats[index];
            }
        }
    }

    public void setDouble(int floatIndex, double value) {

        if( byteBuffer != null ) {
            updateByteBuffer();
            byteBuffer.putDouble(floatIndex * Double.BYTES, value);
        }
        else {
            doubleArray[floatIndex] = value;
        }
    }

    public double getDouble(int floatIndex) {

        if( byteBuffer != null ) {
            updateByteBuffer();
            return byteBuffer.getDouble(floatIndex * Double.BYTES);
        }
        else {
            return doubleArray[floatIndex];
        }
    }

    public double[] toDoubleArray() {
        //updateByteBuffer();

        if( doubleArray == null ) {
            doubleArray = new double[numDoubles()];
        }

        if(byteBuffer != null) {
            if(!byteBufferDirty) {
                // copy from buffer to array
                byteBuffer.asDoubleBuffer().get(doubleArray);
                byteBufferDirty = true;
            }
        }
        return doubleArray;
    }

    protected void updateByteBuffer() {
        if(byteBuffer == null) {
            int numBytes = doubleArray.length * Double.BYTES;
            byteBuffer = BufferUtils.createByteBuffer(numBytes);
            byteBuffer.asDoubleBuffer().put(doubleArray);
        }
        if(byteBufferDirty) {
            byteBufferDirty = false;
            // copy from array to buffer
            byteBuffer.asDoubleBuffer().put(doubleArray);
        }
    }

    /**
     * Position of float buffer is not guaranteed
     * @return
     */
    public DoubleBuffer toDoubleBuffer() {
        if(dummyDoubleBuffer == null) {
            ByteBuffer dummyByteBuffer = toByteBuffer();
            dummyByteBuffer.position(0);
            dummyDoubleBuffer = dummyByteBuffer.asDoubleBuffer();
        }
        return dummyDoubleBuffer;
    }
}
