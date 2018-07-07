/**
 * 
 */
package jscenegraph.database.inventor;

/**
 * @author Yves Boyadjian
 *
 */
public class SbVec3fSingle extends SbVec3f {

    public SbVec3fSingle(float f, float g, float h) {
    	super(f,g,h);
	}

	public SbVec3fSingle() {
		super();
	}

	public SbVec3fSingle(SbVec3f other) {
		super(other);
	}

	public final float[] getValue() {
    	return getValueRef(); 
    }
    
}
