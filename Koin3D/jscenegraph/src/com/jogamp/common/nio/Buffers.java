/**
 * 
 */
package com.jogamp.common.nio;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

/**
 * @author Yves Boyadjian
 *
 */
public class Buffers {

	public static ByteBuffer newDirectByteBuffer(byte[] array) {
		ByteBuffer buffer = BufferUtils.createByteBuffer(array.length);
		buffer.put(array);
		buffer.flip();
		return buffer;
	}

	public static ByteBuffer newDirectByteBuffer(int capacity) {
		ByteBuffer buffer = BufferUtils.createByteBuffer(capacity);
		return buffer;
	}
	//TODO

	public static FloatBuffer copyFloatBuffer(FloatBuffer wrap) {
		int capacity = wrap.capacity();
		ByteBuffer buffer = BufferUtils.createByteBuffer(capacity*Float.BYTES);
		for(int i=0;i<capacity;i++) {
			buffer.putFloat(wrap.get());
		}
		buffer.flip();
		return buffer.asFloatBuffer();
	}

	public static ByteBuffer copyIntBuffer(IntBuffer wrap) {
		int capacity = wrap.capacity();
		ByteBuffer buffer = BufferUtils.createByteBuffer(capacity*Integer.BYTES);
		for(int i=0;i<capacity;i++) {
			buffer.putInt(wrap.get());
		}
		buffer.flip();
		return buffer;
	}

}
