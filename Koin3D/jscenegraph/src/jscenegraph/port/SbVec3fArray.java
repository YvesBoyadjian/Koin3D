/**
 * 
 */
package jscenegraph.port;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec3fSingle;
import jscenegraph.port.memorybuffer.FloatMemoryBuffer;

/**
 * @author Yves Boyadjian
 *
 */
public class SbVec3fArray implements FloatBufferAble {
	
	private FloatMemoryBuffer valuesArray;

	private int delta;
	
	//private FloatBuffer[] floatBuffer = new FloatBuffer[1];
	
	public SbVec3fArray(SbVec3fArray other, int delta) {
		valuesArray = other.valuesArray;
		this.delta = other.delta + delta;
		//this.floatBuffer = other.floatBuffer;
	}

	public SbVec3fArray(FloatMemoryBuffer valuesArray) {
		this.valuesArray = valuesArray;
	}

	public SbVec3fArray(SbVec3fSingle singleSbVec3f) {
		valuesArray = singleSbVec3f.getValueBuffer();
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
	
	public int getSizeFloat() {
		return valuesArray.numFloats() - delta*3;
	}

	public SbVec3f get(int index) {
		return new SbVec3f(valuesArray, (index+delta)*3);
	}

	public SbVec3fArray plus(int delta) {
		return new SbVec3fArray(this,delta);
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
		
		int offset = delta*3;
		
		fb.position(offset);
		
		return fb;
		
//		int length = valuesArray.length - offset;
//		if(floatBuffer[0] == null || floatBuffer[0].capacity() != length) {
//			floatBuffer[0] = BufferUtils.createFloatBuffer(length);
//		//}
//		floatBuffer[0].clear();
//		floatBuffer[0].put(valuesArray, offset, length);
//		floatBuffer[0].flip();
//		}
//		return floatBuffer[0];//FloatBuffer.wrap(valuesArray,offset, length);
	}
	
	public FloatMemoryBuffer getValuesArray() {
		return valuesArray;
	}
	
//	public FloatBuffer[] getValuesBuffer() {
//		return floatBuffer;
//	}

	int getDelta() {
		return delta;
	}

	public static SbVec3fArray fromArray(SbVec3f[] arrayPtr) {
		int length = arrayPtr.length;
		FloatMemoryBuffer valuesArray = FloatMemoryBuffer.allocateFloats(length*3);
		int indice=0;
		for(int i=0; i< length; i++) {
			valuesArray.setFloat(indice++, arrayPtr[i].getX());
			valuesArray.setFloat(indice++, arrayPtr[i].getY());
			valuesArray.setFloat(indice++, arrayPtr[i].getZ());
		}
		SbVec3fArray retVal = new SbVec3fArray(valuesArray);
		return retVal;
	}

	public void copyIn(FloatBuffer floatBuffer) {
		int offset = delta*3;
		int length = valuesArray.numFloats() - offset;
		floatBuffer.put(valuesArray.toFloatArray(), offset, length);
		floatBuffer.flip();
	}
}
