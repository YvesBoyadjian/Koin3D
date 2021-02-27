/**
 * 
 */
package jscenegraph.port;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.port.memorybuffer.FloatMemoryBuffer;

/**
 * @author Yves Boyadjian
 *
 */
public class SbVec2fArray extends Indexable<SbVec2f> implements FloatBufferAble, ByteBufferAble, Destroyable {

	private FloatMemoryBuffer valuesArray;
	
	private int delta;

	SbVec2f dummy;
	
	//private FloatBuffer[] floatBuffer = new FloatBuffer[1];
	
	public SbVec2fArray(SbVec2fArray other, int delta) {
		valuesArray = other.valuesArray;
		this.delta = other.delta + delta;
		//this.floatBuffer = other.floatBuffer;
	}

	public SbVec2fArray(FloatMemoryBuffer valuesArray) {
		this.valuesArray = valuesArray;
	}

	public SbVec2f get(int index) {
		return new SbVec2f(valuesArray, (index + delta) * 2);
	}

	public SbVec2f getFast(int index) {
		if(null == dummy) {
			dummy = new SbVec2f(valuesArray, (index + delta) * 2);
		}
		else {
			dummy.setIndice((index + delta) * 2);
		}
		return dummy;
	}

	public SbVec2fArray plus(int delta) {
		return new SbVec2fArray(this,delta);
	}
	
	public FloatArray toFloatArray() {
		return new FloatArray(delta*2,valuesArray);
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
		
		int offset = delta*2;
		
		fb.position(offset);
		
		return fb;
		
//		int length = valuesArray.length - offset;
//		if(floatBuffer[0] == null || floatBuffer[0].capacity() != length) {
//			floatBuffer[0] = BufferUtils.createFloatBuffer(length);
//			floatBuffer[0].clear();
//			floatBuffer[0].put(valuesArray, offset, length);
//			floatBuffer[0].flip();
//		}
//		return floatBuffer[0];//FloatBuffer.wrap(valuesArray,delta*2,valuesArray.length - delta*2);
	}

	public FloatMemoryBuffer getValuesArray() {
		return valuesArray;
	}
	
	public int getStart() {
		return delta;
	}

	@Override
	public SbVec2f getO(int index) {		
		return get(index);
	}

	@Override
	public int length() {
		return valuesArray.numFloats()/2 - delta;
	}

	@Override
	public void setO(int i, SbVec2f object) {
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
		
		int offset = delta*2*Float.BYTES;
		
		bb.position(offset);
		
		return bb;
	}

	@Override
	public void destructor() {
		if (delta != 0) {
			throw new IllegalArgumentException();
		}
		Destroyable.delete(valuesArray);
		valuesArray = null;
	}
	
}
