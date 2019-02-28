/**
 * 
 */
package jscenegraph.port;

import java.nio.FloatBuffer;

import jscenegraph.database.inventor.SbColor;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec3fSingle;

/**
 * @author Yves Boyadjian
 *
 */
public class SbColorArray implements FloatBufferAble {

	private float[] valuesArray;

	private int delta;
	
	public SbColorArray(SbColorArray other, int delta) {
		valuesArray = other.valuesArray;
		this.delta = other.delta + delta;
	}

	public SbColorArray(float[] valuesArray) {
		this.valuesArray = valuesArray;
	}

	public SbColorArray(SbVec3fSingle singleSbVec3f) {
		valuesArray = singleSbVec3f.getValue();
	}

	public SbColorArray(int length) {
		this(new float[3*length]);
	}

	public SbColor get(int index) {
		return new SbColor(valuesArray, (index+delta)*3);
	}

	public SbColorArray plus(int delta) {
		return new SbColorArray(this,delta);
	}

	public static SbColorArray allocate(int maxColors) {
		return new SbColorArray(new float[maxColors*3]);
	}
	
	public FloatArray toFloatArray() {
		return new FloatArray(delta*3,valuesArray);
	}

	public FloatBuffer toFloatBuffer() {
		return FloatBuffer.wrap(valuesArray, delta*3, valuesArray.length - delta*3);
	}
}
