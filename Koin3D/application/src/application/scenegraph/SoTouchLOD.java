/**
 * 
 */
package application.scenegraph;

import jscenegraph.coin3d.inventor.nodes.SoLOD;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.fields.SoSFNode;
import jscenegraph.database.inventor.nodes.SoIndexedFaceSet;
import jscenegraph.database.inventor.nodes.SoNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoTouchLOD extends SoLOD {
	
	private Chunk chunk;
	
	private int previousChild = -1;
	
	public SoTouchLOD(Chunk chunk) {
		this.chunk = chunk;
	}

//	public void
//	GLRender(SoGLRenderAction action)
//	{
//		super.GLRender(action);
//		int newChild = whichToTraverse(action);
//		if(previousChild >= 0) {
//			if(newChild != previousChild) {
//				SoIndexedFaceSet SoIndexedFaceSet = (SoIndexedFaceSet)getChild(previousChild);
//				SoIndexedFaceSet.coordIndex.touch();
//				//SoIndexedFaceSet.vertexProperty.touch();
//				SoNode node = SoIndexedFaceSet.vertexProperty.getValue();
//				node.touch();
//			}
//		}
//		previousChild = newChild;
//	}	

	public void
	GLRenderBelowPath(SoGLRenderAction action)
	{
		super.GLRenderBelowPath(action);
		int newChild = whichToTraverse(action);
		if(previousChild >= 0) {
			if(newChild != previousChild) {
				SoIndexedFaceSet SoIndexedFaceSet = (SoIndexedFaceSet)getChild(previousChild);
				if(previousChild == 0) {
					SoIndexedFaceSet.coordIndex.touch();
					SoIndexedFaceSet.vertexProperty.setValue(chunk.getVertexProperty(previousChild));
				}
//				SoNode node = SoIndexedFaceSet.vertexProperty.getValue();
//				node.touch();
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
				if(previousChild == 0) {
					SoIndexedFaceSet.coordIndex.touch();
					SoIndexedFaceSet.vertexProperty.setValue(chunk.getVertexProperty(previousChild));
				}
//				SoNode node = SoIndexedFaceSet.vertexProperty.getValue();
//				node.touch();
			}
		}
		previousChild = newChild;
	}	
}
