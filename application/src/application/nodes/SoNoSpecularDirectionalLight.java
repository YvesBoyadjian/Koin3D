/**
 * 
 */
package application.nodes;

import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_LIGHT0;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SPECULAR;

import com.jogamp.opengl.GL2;

import jscenegraph.database.inventor.SbVec4fSingle;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.elements.SoGLLightIdElement;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoDirectionalLight;
import jscenegraph.port.Ctx;

/**
 * @author Yves Boyadjian
 *
 */
public class SoNoSpecularDirectionalLight extends SoDirectionalLight {

	public void
	GLRender(SoGLRenderAction action) {
		super.GLRender(action);

	    int     id;
	    
	    // Don't turn light on if it's off
	    if (! on.getValue())
	        return;

	    SoState state = action.getState();

	    // Get an instance we can change (pushing if necessary)
	    id = SoGLLightIdElement.get(state);

	    // Element is being overridden or we have too many sources for GL
	    // to handle? Skip the whole deal.
	    if (id < 0)
	        return;
	
	    GL2 gl2 = Ctx.get(action.getCacheContext());

	    id = GL_LIGHT0 + id;

	    final SbVec4fSingle     v4 = new SbVec4fSingle(0.0f, 0.0f, 0.0f, 1.0f);
	    
	    gl2.glLightfv( id, GL_SPECULAR, v4.getValue(),0);
	}
}
