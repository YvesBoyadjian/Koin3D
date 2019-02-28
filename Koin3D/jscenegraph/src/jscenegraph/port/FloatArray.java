/**
 * 
 */
package jscenegraph.port;

/**
 * @author Yves Boyadjian
 *
 */
public class FloatArray {

	private int start;
	private float[] values;
	
	public FloatArray(int start, float[] values) {
		this.start = start;
		this.values = values;
	}
	
	public float[] getValues() {
		return values;
	}
	
	public int getStart() {
		return start;
	}
	
	public int size() {
		return values.length-start;
	}
	
	public float get(int i) {
		return values[i+start];
	}
	
	public float[] get3() {
		float[] three = new float[3];
		three[0] = values[start];
		three[1] = values[start+1];
		three[2] = values[start+2];
		return three;
	}
}
