/**
 * 
 */
package application.scenegraph;

import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.nodes.SoNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoTouchLODMaster extends SoNode {
	
	private int counter;

	public void reset() {
		counter = 0;
	}
	
	public void increment() {
		counter++;
	}
	
	public int getCount() {
		return counter;
	}
	
	public void GLRender(SoGLRenderAction action) {
		
		reset();
		super.GLRender(action);
	}
}
