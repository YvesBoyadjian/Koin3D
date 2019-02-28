/**
 * 
 */
package jscenegraph.port;

import java.nio.FloatBuffer;

import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec3fSingle;

/**
 * @author Yves Boyadjian
 *
 */
public class MutableSbVec3fArray implements FloatBufferAble {
	
	private float[] valuesArray;

	private int delta;
	
	public MutableSbVec3fArray(MutableSbVec3fArray other, int delta) {
		valuesArray = other.valuesArray;
		this.delta = other.delta + delta;
	}

	public MutableSbVec3fArray(float[] valuesArray) {
		this.valuesArray = valuesArray;
	}

	public MutableSbVec3fArray(SbVec3fSingle singleSbVec3f) {
		valuesArray = singleSbVec3f.getValue();
	}

	public MutableSbVec3fArray(SbVec3fArray other) {
		valuesArray = other.getValuesArray();
		this.delta = other.getDelta();
	}

	public SbVec3f get(int index) {
		return new SbVec3f(valuesArray, (index+delta)*3);
	}

	public MutableSbVec3fArray plus(int delta) {
		return new MutableSbVec3fArray(this,delta);
	}

	public static SbVec3fArray allocate(int maxPoints) {
		return new SbVec3fArray(new float[maxPoints*3]);
	}
	
	public FloatArray toFloatArray() {
		return new FloatArray(delta*3,valuesArray);
	}

	@Override
	public FloatBuffer toFloatBuffer() {
		return FloatBuffer.wrap(valuesArray,delta*3,valuesArray.length - delta*3);
	}
	
	public void plusPlus() {
		delta++;
	}

	float[] getValuesArray() {
		return valuesArray;
	}

	int getDelta() {
		return delta;
	}
}
