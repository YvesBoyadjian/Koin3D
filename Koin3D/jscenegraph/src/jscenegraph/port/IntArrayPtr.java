/**
 * 
 */
package jscenegraph.port;

/**
 * @author Yves Boyadjian
 *
 */
public class IntArrayPtr {
	
	int [] array;	
	int intOffset;

	public IntArrayPtr(int start, int[] values) {
		this.array = values;
		intOffset = start;
	}

	public IntArrayPtr(int[] array) {
		this.array = array;
		intOffset = 0;
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
}
