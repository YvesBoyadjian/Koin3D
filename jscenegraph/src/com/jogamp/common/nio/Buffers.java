/**
 * 
 */
package com.jogamp.common.nio;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.BufferUtils;

import jscenegraph.port.IntArrayPtr;

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

	public static ByteBuffer newDirectByteBuffer(byte[] array, int offset) {
		ByteBuffer buffer = BufferUtils.createByteBuffer(array.length);
		buffer.put(array);
		buffer.flip();
		buffer.position(offset);
		return buffer;
	}

	public static ShortBuffer newDirectShortBuffer(short[] array) {
		ShortBuffer buffer = BufferUtils.createShortBuffer(array.length);
		buffer.put(array);
		buffer.flip();
		return buffer;
	}

	public static ByteBuffer newDirectByteBuffer(IntArrayPtr array) {
		int size = array.size();
		ByteBuffer buffer = BufferUtils.createByteBuffer(size*Integer.BYTES);
		buffer.asIntBuffer().put(array.getValues(),array.getStart(),size);
		buffer.clear();
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
