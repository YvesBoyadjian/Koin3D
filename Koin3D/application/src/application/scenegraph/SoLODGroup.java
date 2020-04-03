/**
 * 
 */
package application.scenegraph;

import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.nodes.SoGroup;

/**
 * @author Yves Boyadjian
 *
 */
public class SoLODGroup extends SoGroup {

	public final SbVec3f referencePoint = new SbVec3f();
	
	public float maxDistance;
	
	public final SbBox3f box = new SbBox3f();
	
	private final SbVec3f dummy = new SbVec3f(); //SINGLE_THREAD
	
	public void GLRender(SoGLRenderAction action)
	{		
		if( box.intersect(referencePoint)) {		
			super.GLRender(action);
		}
		else {
			SbVec3f closestPoint = box.getClosestExternalPoint(referencePoint);
			
			if( closestPoint.operator_minus(referencePoint,dummy).length() <= maxDistance ) {
				super.GLRender(action);				
			}
//			else {
//				closestPoint = box.getClosestPoint(referencePoint);
//				closestPoint.operator_minus(referencePoint,dummy);
//				int i=0;
//			}
		}
	}
					
}
