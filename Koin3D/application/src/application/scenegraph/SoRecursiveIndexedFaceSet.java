/**
 * 
 */
package application.scenegraph;

import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.nodes.SoIndexedFaceSet;

/**
 * @author Yves Boyadjian
 *
 */
public class SoRecursiveIndexedFaceSet extends SoIndexedFaceSet {
	
	RecursiveChunk recursiveChunk;
	
	//boolean cleared;

	public SoRecursiveIndexedFaceSet(RecursiveChunk recursiveChunk) {
		this.recursiveChunk = recursiveChunk;
	}

	public void clear() {
		coordIndex.setValuesPointer(recursiveChunk.getDecimatedCoordIndices());
		vertexProperty.setValue(recursiveChunk.getVertexProperty());
		//cleared = true;
	}
	
	public void computeBBox(SoAction action, SbBox3f box, SbVec3f center) {
		box.copyFrom(recursiveChunk.sceneBox);
		center.copyFrom(recursiveChunk.sceneCenter);
	}

//	public void GLRender(SoGLRenderAction action)
//	{
//		long start = System.nanoTime();
//		super.GLRender(action);
//		long stop = System.nanoTime();
//		if(cleared) {
//		cleared = false;
////		long delta = stop - start;
////		
////		if(delta > 10e6) {
////			System.out.println("SoTouchLOD2 " + delta/1e6 +" ms");
////		}
////		int i=0;
//	}
//	}
}
