/**
 * 
 */
package jscenegraph.database.inventor;

/**
 * @author Yves Boyadjian
 *
 */
public class SbVec4fSingle extends SbVec4f {

    public SbVec4fSingle(float x, float y, float z, float w) {
    	super(x,y,z,w);
	}

	public SbVec4fSingle() {
		super();
	}

	public SbVec4fSingle(SbVec4f other) {
		super(other);
	}

	public final float[] getValue() {
    	return getValueRef(); 
    }
    
}
