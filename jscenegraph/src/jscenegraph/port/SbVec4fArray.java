/**
 * 
 */
package jscenegraph.port;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec3fSingle;
import jscenegraph.database.inventor.SbVec4f;
import jscenegraph.database.inventor.SbVec4fSingle;
import jscenegraph.port.memorybuffer.FloatMemoryBuffer;

/**
 * @author Yves Boyadjian
 *
 */
public class SbVec4fArray extends Indexable<SbVec4f> implements FloatBufferAble, ByteBufferAble {

	FloatMemoryBuffer valuesArray;

	int delta;
	
	public SbVec4fArray(SbVec4fArray other, int delta) {
		valuesArray = other.valuesArray;
		this.delta = other.delta + delta;
	}

	public SbVec4fArray(FloatMemoryBuffer valuesArray) {
		this.valuesArray = valuesArray;
	}

	public SbVec4fArray(SbVec4fSingle singleSbVec4f) {
		valuesArray = singleSbVec4f.getValueBuffer();
	}

	public SbVec4f get(int index) {
		return new SbVec4f(valuesArray, (index+delta)*4);
	}

	public SbVec4fArray plus(int delta) {
		return new SbVec4fArray(this,delta);
	}

	public static SbVec4fArray allocate(int maxPoints) {
		return new SbVec4fArray(FloatMemoryBuffer.allocateFloats(maxPoints*4));
	}
	
	public FloatArray toFloatArray() {
		return new FloatArray(delta*4,valuesArray);
	}
	
	public FloatMemoryBuffer toFloat() {
		
		if(delta != 0) {
			throw new IllegalStateException();
		}
		
		return valuesArray;
	}

	@Override
	public FloatBuffer toFloatBuffer() {
		FloatBuffer fb = valuesArray.toByteBuffer().asFloatBuffer();
		fb.position(delta*4);
		
		return fb;
		
		//return FloatBuffer.wrap(valuesArray,delta*4,valuesArray.length - delta*4);
	}

	public static SbVec4fArray copyOf(SbVec4fArray coords4) {
		if(coords4 == null)
			return null;
		return new SbVec4fArray(coords4,0);
	}

	@Override
	public SbVec4f getO(int index) {		
		return get(index);
	}

	@Override
	public int length() {
		return valuesArray.numFloats()/4 - delta;
	}

	@Override
	public void setO(int i, SbVec4f object) {
		getO(i).copyFrom(object);
		
	}

	@Override
	public int delta() {
		return delta;
	}

	@Override
	public Object values() {
		return valuesArray;
	}

	@Override
	public ByteBuffer toByteBuffer() {
		ByteBuffer bb = valuesArray.toByteBuffer();
		
		int offset = delta*4*Float.BYTES;
		
		bb.position(offset);
		
		return bb;
	}
}
