package jscenegraph.port.memorybuffer;

import jscenegraph.mevis.inventor.misc.SoVBO;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class IntMemoryBuffer extends MemoryBuffer {

    public final static int MINIMUM_INTS_FOR_BUFFER = SoVBO.getVertexCountMinLimit() * 3;

    private int[] intArray;

    private boolean byteBufferDirty;

    private IntBuffer dummyIntBuffer;

    IntMemoryBuffer() {
        super();
    }

    public int numBytes() {
        if(byteBuffer != null) {
            return byteBuffer.capacity();
        }
        else {
            return intArray.length * Integer.BYTES;
        }
    }

    public int numInts() {
        return numBytes()/Integer.BYTES;
    }

    public static final IntMemoryBuffer allocateInts(int numInts) {

        IntMemoryBuffer memoryBuffer = new IntMemoryBuffer();

        if( numInts >= MINIMUM_INTS_FOR_BUFFER) {

            int numBytes = numInts*Integer.BYTES;

            memoryBuffer.byteBuffer = BufferUtils.createByteBuffer(numBytes);
        }
        else {
            memoryBuffer.intArray = new int[numInts];
        }

        return memoryBuffer;
    }

    public static final IntMemoryBuffer allocateIntsMalloc(int numInts) {

        IntMemoryBuffer memoryBuffer = new IntMemoryBuffer();

        if( numInts >= MINIMUM_INTS_FOR_BUFFER) {

            int numBytes = numInts*Integer.BYTES;

            memoryBuffer.byteBuffer = MemoryUtil.memAlloc(numBytes);
            memoryBuffer.malloc = true;
        }
        else {
            memoryBuffer.intArray = new int[numInts];
        }

        return memoryBuffer;
    }

    /**
     * Note : array data is copied
     * @param array
     * @return
     */
    public static IntMemoryBuffer allocateFromIntArray(int[] array) {

        int numInts = array.length;
        //int numBytes = numFloats * Float.BYTES;

        IntMemoryBuffer memoryBuffer = allocateInts(numInts);
        memoryBuffer.setInts(array, numInts);

        return memoryBuffer;
    }

    public static final void arraycopy(
            IntMemoryBuffer src,
            int srcPos,
            IntMemoryBuffer dest,
            int destPos,
            int length
    ) {
        IntBuffer destSlice = dest.toIntBuffer().position(destPos).slice();
        IntBuffer srcSlice = src.toIntBuffer().position(srcPos).slice().limit(length);
        destSlice.put(srcSlice);

        dest.toIntBuffer().position(0);
        src.toIntBuffer().position(0);

//		for(int i=0; i<length; i++) {
//			dest.setFloat(i+destPos,src.getFloat(i+srcPos));
//		}
    }

    public void setInts(int[] srcInts, int numInts) {

        if( byteBuffer != null ) {
            updateByteBuffer();
            for( int index = 0; index < numInts; index++) {
                byteBuffer.putInt(index * Integer.BYTES,srcInts[index]);
            }
        }
        else {
            for( int index = 0; index < numInts; index++) {
                intArray[index] = srcInts[index];
            }
        }
    }

    public void setInt(int intIndex, int value) {

        if( byteBuffer != null ) {
            updateByteBuffer();
            byteBuffer.putInt(intIndex * Integer.BYTES, value);
        }
        else {
            intArray[intIndex] = value;
        }
    }

    public int getInt(int intIndex) {

        if( byteBuffer != null ) {
            updateByteBuffer();
            return byteBuffer.getInt(intIndex * Integer.BYTES);
        }
        else {
            return intArray[intIndex];
        }
    }

    public int[] toIntArray() {
        //updateByteBuffer();

        if( intArray == null ) {
            intArray = new int[numInts()];
        }

        if(byteBuffer != null) {
            if(!byteBufferDirty) {
                // copy from buffer to array
                byteBuffer.asIntBuffer().get(intArray);
                byteBufferDirty = true;
            }
        }
        return intArray;
    }

    protected void updateByteBuffer() {
        if(byteBuffer == null) {
            int numBytes = intArray.length * Integer.BYTES;
            byteBuffer = BufferUtils.createByteBuffer(numBytes);
            byteBuffer.asIntBuffer().put(intArray);
        }
        if(byteBufferDirty) {
            byteBufferDirty = false;
            // copy from array to buffer
            byteBuffer.asIntBuffer().put(intArray);
        }
    }

    /**
     * Position of int buffer is not guaranteed
     * @return
     */
    public IntBuffer toIntBuffer() {
        if(dummyIntBuffer == null) {
            ByteBuffer dummyByteBuffer = toByteBuffer();
            dummyByteBuffer.position(0);
            dummyIntBuffer = dummyByteBuffer.asIntBuffer();
        }
        return dummyIntBuffer;
    }
}
