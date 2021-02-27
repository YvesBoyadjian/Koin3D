package application.nodes;

import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction.AbortCode;
import jscenegraph.database.inventor.actions.SoGLRenderAction.SoGLRenderAbortCB;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;

public class SoAbort extends SoNode {
	
	private int init = 200;
	
	public void GLRender(SoGLRenderAction action) {
		
		final long time = System.nanoTime();
		
		final long MAX_TIME = (long)1e9;
		
		if( init-- == 0)
		action.setAbortCallback(new SoGLRenderAbortCB() {

			@Override
			public AbortCode abort(Object userData) {
				if(System.nanoTime() - time > MAX_TIME) {
					return AbortCode.ABORT;
				}
				return AbortCode.CONTINUE;
			}
			
		}, null);
		super.GLRender(action);
	}
}
