/**
 * 
 */
package jscenegraph.port;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.Objects;

/**
 * @author Yves Boyadjian
 *
 */
public class UShortPtr {
	
	ByteBuffer data;
	ShortBuffer sb;

	public UShortPtr(ByteBuffer object) {
		Objects.requireNonNull(object);
		data = object;
		sb = data.asShortBuffer();
	}

	public void set(int value, int index) {
		sb.put(index,(short)value);
	}

}
