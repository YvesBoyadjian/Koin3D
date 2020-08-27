/**
 * 
 */
package jscenegraph.port;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

/**
 * @author Yves Boyadjian
 *
 */
public class IntArray extends Indexable<Integer> implements IntBufferAble {
	
	int start;
	int[] values;

	private IntBuffer intBuffer;
	
	public IntArray(int start, int[] values) {
		this.start = start;
		this.values = values;
	}

	public IntArray(int length) {
		values = new int[length];
		for(int i=0;i<length;i++) {
			values[i] = 0;
		}
	}

	public IntArray(int start2, IntArray values2) {
		this.start = start2 + values2.start;
		this.values = values2.values;
	}

	@Override
	public Integer getO(int index) {
		return values[index+start];
	}

	@Override
	public int length() {
		return values.length - start;
	}

	@Override
	public void setO(int i, Integer object) {
		values[i+start] = object;
	}

	public void set(int i, int value) {
		values[i+start] = value;
	}

	public int get(int index) {
		return values[index+start];
	}

	public IntArray plus(int start2) {
		return new IntArray(start2,this);
	}

	@Override
	public int delta() {
		return start;
	}

	public int[] values() {
		return values;
	}

	@Override
		public IntBuffer toIntBuffer() {
		int offset = start;
		int length = values.length - offset;
		if(intBuffer == null || intBuffer.capacity() != length) {
			intBuffer = BufferUtils.createIntBuffer(length);
		//}
		intBuffer.clear();
		intBuffer.put(values, offset, length);
		intBuffer.flip();
		}
		return intBuffer;		
	}
}
