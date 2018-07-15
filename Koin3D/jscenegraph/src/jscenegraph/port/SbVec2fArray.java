/**
 * 
 */
package jscenegraph.port;

import jscenegraph.database.inventor.SbVec2f;

/**
 * @author Yves Boyadjian
 *
 */
public class SbVec2fArray {

	private float[] valuesArray;

	public SbVec2fArray(float[] valuesArray) {
		this.valuesArray = valuesArray;
	}

	public SbVec2f get(int index) {
		return new SbVec2f(valuesArray, index*2);
	}
}
