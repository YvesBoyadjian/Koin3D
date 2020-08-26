/**
 * 
 */
package jscenegraph.port;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.port.memorybuffer.FloatMemoryBuffer;

/**
 * @author Yves Boyadjian
 *
 */
public class FloatArray extends Indexable<Float> implements ByteBufferAble {

	private int start;
	private FloatMemoryBuffer values;
	
	public FloatArray(int start, FloatMemoryBuffer values) {
		this.start = start;
		this.values = values;
	}
	
	public FloatArray(SbVec3fArray other, int delta) {
		values = other.valuesArray;
		this.start = other.delta*3 + delta;
	}

	public FloatArray(SbVec4fArray other, int delta) {
		values = other.valuesArray;
		this.start = other.delta*4 + delta;
	}

	public FloatArray(int start2, FloatArray values2) {
		this.start = start2 + values2.start;
		this.values = values2.values;
	}

	public FloatArray(int length) {
		values = FloatMemoryBuffer.allocateFloats(length);
	}

	public FloatMemoryBuffer getValues() {
		return values;
	}
	
	public int getStart() {
		return start;
	}
	
	public int size() {
		return values.numFloats()-start;
	}
	
	public float get(int i) {
		return values.getFloat(i+start);
	}
	
	public float[] get3() {
		float[] three = new float[3];
		three[0] = values.getFloat(start);
		three[1] = values.getFloat(start+1);
		three[2] = values.getFloat(start+2);
		return three;
	}

	public static FloatArray copyOf(FloatArray other) {
		if(other == null)
			return null;
		return new FloatArray(other.start,other.values);
	}

	public void set(int index, float value) {
		values.setFloat(index+start, value);
	}

	public void getSbVec3f(int sbVec3fIndex, SbVec3f v) {
		int floatIndex = sbVec3fIndex * 3;
		v.setX(get(floatIndex++));
		v.setY(get(floatIndex++));
		v.setZ(get(floatIndex));
	}

	public static FloatArray wrap(float value) {
		FloatArray fa = new FloatArray(0, FloatMemoryBuffer.allocateFloats(1));
		fa.set(0, value);
		return fa;
	}

	public FloatMemoryBuffer getValuesArray() {
		return values;
	}
	
	public static FloatArray copyOf(SbVec3fArray other) {
		if(other == null) {
			return null;
		}
		FloatArray copy = new FloatArray(other,0);
		return copy;
	}
	
	public static FloatArray copyOf(SbVec4fArray other) {
		if(other == null) {
			return null;
		}
		FloatArray copy = new FloatArray(other,0);
		return copy;
	}

	@Override
	public ByteBuffer toByteBuffer() {
		ByteBuffer bb = values.toByteBuffer();
		bb.position(start*Float.BYTES);
		return bb.slice();
	}

	@Override
	public Float getO(int index) {
		return values.getFloat(index+start);
	}

	@Override
	public int length() {
		return size();
	}

	@Override
	public void setO(int i, Float object) {
		set(i,(float)object);
	}

	public FloatArray plus(int i) {
		return new FloatArray(this.start + i, values);
	}

	@Override
	public int delta() {
		return start;
	}

	@Override
	public Object values() {
		return values;
	}
	
}
