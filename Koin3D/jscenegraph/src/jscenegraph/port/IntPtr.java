/**
 * 
 */
package jscenegraph.port;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import com.jogamp.common.nio.Buffers;

/**
 * @author Yves Boyadjian
 *
 */
public class IntPtr implements ByteBufferAble {
	
	private ByteBuffer buffer;
	private IntBuffer asIntBuffer;
	private int intOffset;
	
	public IntPtr(int size) {
		int capacity = (int)(size*(long)Integer.SIZE/Byte.SIZE);
		buffer = Buffers.newDirectByteBuffer(capacity);
		asIntBuffer = buffer.asIntBuffer();
	}

	public IntPtr(FloatPtr other) {
		buffer = other.getBuffer();
		asIntBuffer = buffer.asIntBuffer();
		intOffset = other.getFloatOffset();
	}

	public IntPtr(IntPtr other) {
		buffer = other.getBuffer();
		asIntBuffer = buffer.asIntBuffer();
		intOffset = other.intOffset;
	}

	public IntPtr(int[] indices) {
		int capacity = (int)(indices.length*(long)Integer.SIZE/Byte.SIZE);
		buffer = Buffers.newDirectByteBuffer(capacity);
		asIntBuffer = buffer.asIntBuffer();
		asIntBuffer.put(indices);
	}

	public ByteBuffer getBuffer() {
		return buffer;
	}

	public void asterisk(int value) {
		asIntBuffer.put(intOffset, value);
	}

	public int getIntOffset() {
		return intOffset;
	}

	public void plusPlus() {
		intOffset++;
	}

	public int get() {
		return asIntBuffer.get(intOffset);
	}

	@Override
	public ByteBuffer toByteBuffer() {
		ByteBuffer retVal = buffer.duplicate();
		int byteOffset = intOffset * Integer.BYTES;
		retVal.position(byteOffset);
		return retVal;
	}
}
