/**
 * 
 */
package application.scenegraph;

import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.nodes.SoIndexedFaceSet;

/**
 * @author Yves Boyadjian
 *
 */
public class SoLODIndexedFaceSet extends SoIndexedFaceSet {

	public final SbVec3f referencePoint = new SbVec3f();
	
	public float maxDistance;
	
	private final SbBox3f box = new SbBox3f();
	
	private final SbVec3f center = new SbVec3f();
	
	public void GLRender(SoGLRenderAction action)
	{		
		getBBox(action, box, center);
		
		if( box.intersect(referencePoint)) {		
			super.GLRender(action);
		}
		else {
			SbVec3f closestPoint = box.getClosestPoint(referencePoint);
			
			if( closestPoint.operator_minus(referencePoint).length() <= maxDistance ) {
				super.GLRender(action);				
			}
		}
	}			
}
