/**
 * 
 */
package jscenegraph.port;

import java.nio.FloatBuffer;

import jscenegraph.database.inventor.SbVec2f;

/**
 * @author Yves Boyadjian
 *
 */
public class SbVec2fArray implements FloatBufferAble {

	private float[] valuesArray;
	
	private int delta;
	
	public SbVec2fArray(SbVec2fArray other, int delta) {
		valuesArray = other.valuesArray;
		this.delta = other.delta + delta;
	}

	public SbVec2fArray(float[] valuesArray) {
		this.valuesArray = valuesArray;
	}

	public SbVec2f get(int index) {
		return new SbVec2f(valuesArray, (index + delta)*2);
	}

	public SbVec2fArray plus(int delta) {
		return new SbVec2fArray(this,delta);
	}
	
	public FloatArray toFloatArray() {
		return new FloatArray(delta*2,valuesArray);
	}
	
	public float[] toFloat() {
		
		if(delta != 0) {
			throw new IllegalStateException();
		}
		
		return valuesArray;
	}

	@Override
	public FloatBuffer toFloatBuffer() {
		return FloatBuffer.wrap(valuesArray,delta*2,valuesArray.length - delta*2);
	}
}
