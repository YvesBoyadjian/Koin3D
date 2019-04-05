/**
 * 
 */
package application.scenegraph;

import jscenegraph.coin3d.inventor.nodes.SoLOD;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.nodes.SoIndexedFaceSet;

/**
 * @author Yves Boyadjian
 *
 */
public class SoTouchLOD extends SoLOD {

	int previousChild = -1;
	
	public void
	GLRender(SoGLRenderAction action)
	{
		super.GLRender(action);
		int newChild = whichToTraverse(action);
		if(previousChild >= 0) {
			if(newChild != previousChild) {
				SoIndexedFaceSet SoIndexedFaceSet = (SoIndexedFaceSet)getChild(previousChild);
				SoIndexedFaceSet.coordIndex.touch();
			}
		}
		previousChild = newChild;
	}	

	public void
	GLRenderBelowPath(SoGLRenderAction action)
	{
		super.GLRenderBelowPath(action);
		int newChild = whichToTraverse(action);
		if(previousChild >= 0) {
			if(newChild != previousChild) {
				SoIndexedFaceSet SoIndexedFaceSet = (SoIndexedFaceSet)getChild(previousChild);
				SoIndexedFaceSet.coordIndex.touch();
			}
		}
		previousChild = newChild;
	}	
	public void
	GLRenderOffPath(SoGLRenderAction action)
	{
		super.GLRenderOffPath(action);
		int newChild = whichToTraverse(action);
		if(previousChild >= 0) {
			if(newChild != previousChild) {
				SoIndexedFaceSet SoIndexedFaceSet = (SoIndexedFaceSet)getChild(previousChild);
				SoIndexedFaceSet.coordIndex.touch();
			}
		}
		previousChild = newChild;
	}	
}
