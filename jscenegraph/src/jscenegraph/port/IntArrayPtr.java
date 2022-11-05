/**
 * 
 */
package jscenegraph.port;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Objects;

import org.lwjgl.BufferUtils;

/**
 * @author Yves Boyadjian
 *
 */
public class IntArrayPtr implements Destroyable, IntBufferAble {
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Objects.hash(array);
		result = prime * result + Objects.hash(intOffset);
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof IntArrayPtr)) {
			return false;
		}
		IntArrayPtr other = (IntArrayPtr) obj;
		return array == other.array && intOffset == other.intOffset;
	}

	private int [] array;	
	private int intOffset;

	private IntBuffer[] intBuffer = new IntBuffer[1];
		
	public IntArrayPtr(int start, int[] values) {
		this.array = values;
		intOffset = start;
	}

	public IntArrayPtr(int[] array) {
		this.array = array;
		intOffset = 0;
	}

	public IntArrayPtr(IntArrayPtr intArrayPtr) {
		this.array = intArrayPtr.array;
		this.intOffset = intArrayPtr.intOffset;
		this.intBuffer = intArrayPtr.intBuffer;
	}

	public IntArrayPtr(int offset, IntArrayPtr intArrayPtr) {
		this.array = intArrayPtr.array;
		this.intOffset = intArrayPtr.intOffset + offset;
		this.intBuffer = intArrayPtr.intBuffer;
	}

	public IntArrayPtr(int start, IntArray values) {
		this.array = values.values;
		intOffset = start + values.start;		
	}

	public IntArrayPtr(IntArray values) {
		this.array = values.values;
		this.intOffset = values.start;
	}

	public void plusPlus() {
		intOffset++;
	}

	public int get() {
		return array[intOffset];
	}

	public int getPlusPlus() {
		int retVal = array[intOffset];
		plusPlus();
		return retVal;
	}

	public int[] getValues() {
		return array;
	}
	
	public int getStart() {
		return intOffset;
	}
	
	public int size() {
		return array.length-intOffset;
	}
	
	public int get(int i) {
		return array[i+intOffset];
	}

	public static IntArrayPtr plus( IntArrayPtr other, int num) {
		if( other == null) {
			if (num != 0) {
				throw new IllegalArgumentException("adding value to null pointer");
			}
			return null;
		}
		else {
			return other.plus(num);
		}
	}
	
	public static boolean lessThan(IntArrayPtr first, IntArrayPtr last) {
		if(first == null && last == null) {
			return false;
		}
		return first.lessThan(last);
	}
	
	private IntArrayPtr plus(int num) {
		return new IntArrayPtr(intOffset+num,array);
	}

	public int minus(IntArrayPtr other) {
		if(array != other.array) {
			throw new IllegalArgumentException();
		}
		return intOffset - other.intOffset;
	}

	private boolean lessThan(IntArrayPtr other) {
		if(array != other.array) {
			throw new IllegalArgumentException();
		}
		return intOffset < other.intOffset;
	}

	public void set(int index, int value) {
		array[intOffset+index] = value;
	}

	public boolean greaterOrEqual(IntArrayPtr other) {
		if(array != other.array) {
			throw new IllegalArgumentException();
		}
		return intOffset >= other.intOffset;
	}

	public int star() {
		int retVal = get();
		return retVal;
	}

	public int starPlusPlus() {
		int retVal = get();
		plusPlus();
		return retVal;
	}

	public static IntArrayPtr copyOf(IntArrayPtr other) {
		if(other == null)
			return null;
		return new IntArrayPtr(other);
	}
	
	public IntBuffer toIntBuffer() {
		int offset = intOffset;
		int length = array.length - offset;
		if(intBuffer[0] == null || intBuffer[0].capacity() != length) {
			intBuffer[0] = BufferUtils.createIntBuffer(length);
		//}
		intBuffer[0].clear();
		intBuffer[0].put(array, offset, length);
		intBuffer[0].flip();
		}
		return intBuffer[0];		
	}

	@Override
	public void destructor() {
		array = null;
		intBuffer[0] = null;
	}

	public boolean plusLessThan(int delta, IntArrayPtr other) {
		if(array != other.array) {
			throw new IllegalArgumentException();
		}
		return (intOffset + delta) < other.intOffset;
	}

	public static int minus(IntArrayPtr first, IntArrayPtr second) {
		if (first == null && second == null) {
			return 0;
		}
		return first.minus(second);
	}
}
