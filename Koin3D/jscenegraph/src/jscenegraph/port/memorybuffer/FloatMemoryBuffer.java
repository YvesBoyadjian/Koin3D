/**
 * A class representing a zone of memory containing floats
 */
package jscenegraph.port.memorybuffer;

import org.lwjgl.BufferUtils;

import jscenegraph.mevis.inventor.misc.SoVBO;

/**
 * @author Yves Boyadjian
 *
 */
public class FloatMemoryBuffer extends MemoryBuffer {
	
	public final static int MINIMUM_FLOATS_FOR_BUFFER = SoVBO.getVertexCountMinLimit() * 3;
	
	private float[] floatArray;
	
	private boolean byteBufferDirty;

	FloatMemoryBuffer() {
		super();
	}

	public int numBytes() {
		if(byteBuffer != null) {
			return byteBuffer.capacity();
		}
		else {
			return floatArray.length * Float.BYTES;
		}
	}
	
	public int numFloats() {
		return numBytes()/Float.BYTES;
	}

	public static final FloatMemoryBuffer allocateFloats(int numFloats) {
		
		FloatMemoryBuffer memoryBuffer = new FloatMemoryBuffer();
		
		if( numFloats >= MINIMUM_FLOATS_FOR_BUFFER) {
		
			int numBytes = numFloats*Float.BYTES;
		
			memoryBuffer.byteBuffer = BufferUtils.createByteBuffer(numBytes);
		}
		else {
			memoryBuffer.floatArray = new float[numFloats];
		}

		return memoryBuffer;
	}
	
	/**
	 * Note : array data is copied
	 * @param array
	 * @return
	 */
	public static FloatMemoryBuffer allocateFromFloatArray(float[] array) {
		
		int numFloats = array.length;
		//int numBytes = numFloats * Float.BYTES;
		
		FloatMemoryBuffer memoryBuffer = allocateFloats(numFloats);
		memoryBuffer.setFloats(array, numFloats);
		
		return memoryBuffer;
	}

	public void setFloats(float[] srcFloats, int numFloats) {

		if( byteBuffer != null ) {
			updateByteBuffer();
			for( int index = 0; index < numFloats; index++) {
				byteBuffer.putFloat(index * Float.BYTES,srcFloats[index]);
			}
		}
		else {
			for( int index = 0; index < numFloats; index++) {
				floatArray[index] = srcFloats[index];
			}
		}
	}

	public void setFloat(int floatIndex, float value) {
		
		if( byteBuffer != null ) {
			updateByteBuffer();
			byteBuffer.putFloat(floatIndex * Float.BYTES, value);
		}
		else {
			floatArray[floatIndex] = value;
		}
	}

	public float getFloat(int floatIndex) {
		
		if( byteBuffer != null ) {
			updateByteBuffer();
			return byteBuffer.getFloat(floatIndex * Float.BYTES);
		}
		else {
			return floatArray[floatIndex];
		}
	}

	public float[] toFloatArray() {
		//updateByteBuffer();
		
		if( floatArray == null ) {
			floatArray = new float[numFloats()];
		}
		
		if(byteBuffer != null) {
			if(!byteBufferDirty) {
				// copy from buffer to array
				byteBuffer.asFloatBuffer().get(floatArray);
				byteBufferDirty = true;
			}
		}
		return floatArray;
	}
	
	protected void updateByteBuffer() {
		if(byteBuffer == null) {
			int numBytes = floatArray.length * Float.BYTES;
			byteBuffer = BufferUtils.createByteBuffer(numBytes);
			byteBuffer.asFloatBuffer().put(floatArray);
		}
		if(byteBufferDirty) {
			byteBufferDirty = false;
			// copy from array to buffer
			byteBuffer.asFloatBuffer().put(floatArray);
		}
	}
}
