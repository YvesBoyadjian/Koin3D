/**
 * 
 */
package jscenegraph.port;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

/**
 * @author Yves Boyadjian
 *
 */
public class FloatPtr implements Destroyable {
	
	private ByteBuffer buffer;
	private int floatOffset;
	private FloatBuffer floatBuffer;

	public FloatPtr(CharPtr other) {
		buffer = other.getBuffer();
		floatOffset = other.getFloatOffset();
		if( buffer != null) {
			floatBuffer = buffer.asFloatBuffer();
		}
	}
	
	public FloatPtr(FloatPtr other, int floatOffset) {
		buffer = other.getBuffer();
		this.floatOffset = other.getFloatOffset() + floatOffset;
		if( buffer != null) {
			floatBuffer = buffer.asFloatBuffer();
		}
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
		floatBuffer.put(floatOffset, value);
	}

	public void destructor() {
		buffer = null;
		floatBuffer = null;
	}
}
