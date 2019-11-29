/**
 * 
 */
package jscenegraph.port;

import java.nio.FloatBuffer;

import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec3fSingle;
import jscenegraph.port.memorybuffer.FloatMemoryBuffer;

/**
 * @author Yves Boyadjian
 *
 */
public class MutableSbVec3fArray implements FloatBufferAble {
	
	private FloatMemoryBuffer valuesArray;

	private int delta;
	
	public MutableSbVec3fArray(MutableSbVec3fArray other, int delta) {
		valuesArray = other.valuesArray;
		this.delta = other.delta + delta;
	}

	public MutableSbVec3fArray(FloatMemoryBuffer valuesArray) {
		this.valuesArray = valuesArray;
	}

	public MutableSbVec3fArray(SbVec3fSingle singleSbVec3f) {
		valuesArray = singleSbVec3f.getValueBuffer();
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
		return new SbVec3fArray(FloatMemoryBuffer.allocateFloats(maxPoints*3));
	}
	
	public FloatArray toFloatArray() {
		return new FloatArray(delta*3,valuesArray);
	}

	@Override
	public FloatBuffer toFloatBuffer() {
		FloatBuffer fb = valuesArray.toByteBuffer().asFloatBuffer();
		fb.position(delta*3);
		return fb;
		//return FloatBuffer.wrap(valuesArray,delta*3,valuesArray.numFloats() - delta*3);
	}
	
	public void plusPlus() {
		delta++;
	}

	FloatMemoryBuffer getValuesArray() {
		return valuesArray;
	}

	int getDelta() {
		return delta;
	}
}
