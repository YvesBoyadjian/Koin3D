/**
 * 
 */
package jscenegraph.port;

import java.nio.ByteBuffer;

import com.jogamp.common.nio.Buffers;

/**
 * @author Yves Boyadjian
 *
 */
public class CharPtr {
	
	int floatOffset;
	ByteBuffer buffer;

	/**
	 * Construct a char ptr with numBytes
	 * 
	 * @param numBytes
	 */
	public CharPtr(int numBytes) {
		buffer = Buffers.newDirectByteBuffer(numBytes);
		floatOffset = 0;
	}
	
	public CharPtr(FloatPtr floatPtr) {
		buffer = floatPtr.getBuffer();
		floatOffset = floatPtr.getFloatOffset();
	}

	public CharPtr(IntPtr intPtr) {
		buffer = intPtr.getBuffer();
		floatOffset = intPtr.getIntOffset();
	}

	public ByteBuffer getBuffer() {
		return buffer;
	}
	
	public int getByteOffset() {
		return (int)(floatOffset*(long)Float.SIZE/Byte.SIZE);
	}
	
	public int getFloatOffset() {
		return floatOffset;
	}

	public int minus(CharPtr other) {
		if(buffer != other.getBuffer()) {
			throw new IllegalStateException();
		}
		return getByteOffset() - other.getByteOffset();
	}

	public void destructor() {
		buffer = null;
	}
}
