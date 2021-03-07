/**
 * 
 */
package application.nodes;

import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.elements.SoCacheElement;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoSeparator;

/**
 * @author Yves Boyadjian
 *
 */
public class SoTargets extends SoSeparator {
	
	public SoTargets() {
		super();
		renderCaching.setValue(SoSeparator.CacheEnabled.OFF);
	}

	public void
	GLRenderBelowPath(SoGLRenderAction action)

	////////////////////////////////////////////////////////////////////////
	{
		  SoState state = action.getState();

		  // never cache this node
		  SoCacheElement.invalidate(state);

		super.GLRenderBelowPath(action);
	}
	
}
