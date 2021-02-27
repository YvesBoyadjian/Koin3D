/**
 * 
 */
package jscenegraph.port;

/**
 * @author Yves Boyadjian
 *
 */
public class FLpt2 {
    public float        x;
    public float        y;
    
    public FLpt2(){};
    
    public FLpt2(float x, float y) {
    	this.x = x;
    	this.y = y;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FLpt2 other = (FLpt2) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		return true;
	}
}
