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
	
	public static boolean lessThan(IntArray first, IntArray second) {
		if ( null == first && null != second ) {
			return true;
		}
		if ( first.values != second.values ) {
			throw new IllegalArgumentException();
		}		
		return first.start < second.start;
	}
	
	public IntArray(int start, int[] values) {
		this.start = start;
		this.values = values;
	}

	public IntArray(int length) {
		values = new int[length];
//		for(int i=0;i<length;i++) {
//			values[i] = 0;
//		}
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

	public void copy(int num, Indexable<Integer> source) {
//		for (int i = 0; i < num; i++) {
//			set(i, source.get(i));
//		}
		if(source instanceof IntArray) {
			IntArray source_ = (IntArray)source;
			System.arraycopy(source_.values, source_.start, values, start, num);
		}
		else {
			super.copy(num,source);
		}
	}
}
