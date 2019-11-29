/**
 * 
 */
package jscenegraph.port;

import java.nio.FloatBuffer;

import jscenegraph.database.inventor.SbColor;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec3fSingle;
import jscenegraph.port.memorybuffer.FloatMemoryBuffer;

/**
 * @author Yves Boyadjian
 *
 */
public class SbColorArray implements FloatBufferAble {

	private FloatMemoryBuffer valuesArray;

	private int delta;
	
	public SbColorArray(SbColorArray other, int delta) {
		valuesArray = other.valuesArray;
		this.delta = other.delta + delta;
	}

	public SbColorArray(FloatMemoryBuffer valuesArray) {
		this.valuesArray = valuesArray;
	}

	public SbColorArray(SbVec3fSingle singleSbVec3f) {
		valuesArray = singleSbVec3f.getValueBuffer();
	}

	public SbColorArray(int length) {
		this(FloatMemoryBuffer.allocateFloats(3*length));
	}

	public SbColor get(int index) {
		return new SbColor(valuesArray, (index+delta)*3);
	}

	public SbColorArray plus(int delta) {
		return new SbColorArray(this,delta);
	}

	public static SbColorArray allocate(int maxColors) {
		return new SbColorArray(FloatMemoryBuffer.allocateFloats(maxColors*3));
	}
	
	public FloatArray toFloatArray() {
		return new FloatArray(delta*3,valuesArray);
	}

	public FloatBuffer toFloatBuffer() {
		FloatBuffer fb = valuesArray.toByteBuffer().asFloatBuffer();
		fb.position(delta*3);
		return fb;
		//return FloatBuffer.wrap(valuesArray, delta*3, valuesArray.numFloats() - delta*3);
	}

	public static SbColorArray copyOf(SbColorArray other) {
		if(other == null)
			return null;
		return new SbColorArray(other,0);
	}
}
