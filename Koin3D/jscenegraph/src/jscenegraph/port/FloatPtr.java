/**
 * 
 */
package jscenegraph.port;

import java.nio.ByteBuffer;

/**
 * @author Yves Boyadjian
 *
 */
public class FloatPtr {
	
	private ByteBuffer buffer;
	private int floatOffset;

	public FloatPtr(CharPtr other) {
		buffer = other.getBuffer();
		floatOffset = other.getFloatOffset();
	}
	
	public FloatPtr(FloatPtr other, int floatOffset) {
		buffer = other.getBuffer();
		this.floatOffset = other.getFloatOffset() + floatOffset;
	}

	public FloatPtr operator_add(int floatOffset) {
		return new FloatPtr(this, floatOffset);
	}
	
	public ByteBuffer getBuffer() {
		return buffer;
	}
	
	public int getFloatOffset() {
		return floatOffset;
	}

	public void plusPlus() {
		floatOffset++;
	}

	public void asterisk(float value) {
		buffer.asFloatBuffer().put(floatOffset, value);
	}

	public void destructor() {
		buffer = null;
	}
}
