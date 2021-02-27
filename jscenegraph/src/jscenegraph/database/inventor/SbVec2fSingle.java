/**
 * 
 */
package jscenegraph.database.inventor;

/**
 * @author Yves Boyadjian
 *
 */
public class SbVec2fSingle extends SbVec2f {

    public SbVec2fSingle(float f, float g) {
    	super(f,g);
	}

	public SbVec2fSingle() {
		super();
	}

	public SbVec2fSingle(SbVec2f other) {
		super(other);
	}

	public final float[] getValue() {
    	return getValueRef(); 
    }
    
}
