/**
 * 
 */
package jscenegraph.port;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Yves Boyadjian
 *
 */
public class IntArrayPtr {
	
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
	}

	public IntArrayPtr(int offset, IntArrayPtr intArrayPtr) {
		this.array = intArrayPtr.array;
		this.intOffset = intArrayPtr.intOffset + offset;
	}

	public void plusPlus() {
		intOffset++;
	}

	public int get() {
		return array[intOffset];
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

	public IntArrayPtr plus(int num) {
		return new IntArrayPtr(intOffset+num,array);
	}

	public int minus(IntArrayPtr other) {
		if(array != other.array) {
			throw new IllegalArgumentException();
		}
		return other.intOffset - intOffset;
	}

	public boolean lessThan(IntArrayPtr other) {
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
}
