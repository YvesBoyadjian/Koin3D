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

}
