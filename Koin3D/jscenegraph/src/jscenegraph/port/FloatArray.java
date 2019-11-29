/**
 * 
 */
package jscenegraph.port;

import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.port.memorybuffer.FloatMemoryBuffer;

/**
 * @author Yves Boyadjian
 *
 */
public class FloatArray {

	private int start;
	private FloatMemoryBuffer values;
	
	public FloatArray(int start, FloatMemoryBuffer values) {
		this.start = start;
		this.values = values;
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
}
