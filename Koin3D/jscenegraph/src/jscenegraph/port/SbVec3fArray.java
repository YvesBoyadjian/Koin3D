/**
 * 
 */
package jscenegraph.port;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec3fSingle;

/**
 * @author Yves Boyadjian
 *
 */
public class SbVec3fArray implements FloatBufferAble {
	
	private float[] valuesArray;

	private int delta;
	
	public SbVec3fArray(SbVec3fArray other, int delta) {
		valuesArray = other.valuesArray;
		this.delta = other.delta + delta;
	}

	public SbVec3fArray(float[] valuesArray) {
		this.valuesArray = valuesArray;
	}

	public SbVec3fArray(SbVec3fSingle singleSbVec3f) {
		valuesArray = singleSbVec3f.getValue();
	}

	public SbVec3fArray(MutableSbVec3fArray other) {
		valuesArray = other.getValuesArray();
		this.delta = other.getDelta();
	}

	public SbVec3fArray(MutableSbVec3fArray other, int delta) {
		valuesArray = other.getValuesArray();
		this.delta = other.getDelta()+ delta;
	}

	public static SbVec3fArray copyOf(SbVec3fArray other) {
		if(other == null) {
			return null;
		}
		SbVec3fArray copy = new SbVec3fArray(other,0);
		return copy;
	}

	public SbVec3f get(int index) {
		return new SbVec3f(valuesArray, (index+delta)*3);
	}

	public SbVec3fArray plus(int delta) {
		return new SbVec3fArray(this,delta);
	}

	public static SbVec3fArray allocate(int maxPoints) {
		return new SbVec3fArray(new float[maxPoints*3]);
	}
	
	public FloatArray toFloatArray() {
		return new FloatArray(delta*3,valuesArray);
	}

	@Override
	public FloatBuffer toFloatBuffer() { //FIXME poor performance
		int offset = delta*3;
		int length = valuesArray.length - offset;
		FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(length);
		floatBuffer.put(valuesArray, offset, length);
		floatBuffer.flip();
		return floatBuffer;//FloatBuffer.wrap(valuesArray,offset, length);
	}
	
	float[] getValuesArray() {
		return valuesArray;
	}

	int getDelta() {
		return delta;
	}

	public static SbVec3fArray fromArray(SbVec3f[] arrayPtr) {
		int length = arrayPtr.length;
		float[] valuesArray = new float[length*3];
		int indice=0;
		for(int i=0; i< length; i++) {
			valuesArray[indice++] = arrayPtr[i].getX();
			valuesArray[indice++] = arrayPtr[i].getY();
			valuesArray[indice++] = arrayPtr[i].getZ();
		}
		SbVec3fArray retVal = new SbVec3fArray(valuesArray);
		return retVal;
	}
}
