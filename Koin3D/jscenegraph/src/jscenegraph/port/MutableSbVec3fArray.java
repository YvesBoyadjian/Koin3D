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

	SbVec3f dummy;

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

	private MutableSbVec3fArray(SbVec3fArray other) {
		valuesArray = other.getValuesArray();
		this.delta = other.getDelta();
	}

	public static MutableSbVec3fArray from(SbVec3fArray other) {
		if ( null == other ) {
			return null;
		}
		return new MutableSbVec3fArray(other);
	}

	public MutableSbVec3fArray(MutableSbVec3fArray other) {
		valuesArray = other.getValuesArray();
		this.delta = other.getDelta();
	}

	public SbVec3f get(int index) {
		if( null == dummy ) {
			dummy = new SbVec3f(valuesArray, (index+delta)*3);
		}
		else {
			dummy.setIndice((index+delta)*3);
		}
		return dummy;
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
		FloatBuffer fb = valuesArray.toFloatBuffer();
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

	public void assign(MutableSbVec3fArray other) {
		if( valuesArray != other.getValuesArray()) {
			throw new IllegalArgumentException();
		}
		delta = other.getDelta();
	}

	public void assign(MutableSbVec3fArray other, int delta) {
		if( valuesArray != other.valuesArray ) {
			throw new IllegalArgumentException();			
		}
		this.delta = other.delta + delta;
	}

	public void assign(SbVec3fArray other, int delta) {
		if( valuesArray != other.valuesArray ) {
			throw new IllegalArgumentException();			
		}
		this.delta = other.delta + delta;
	}
}
