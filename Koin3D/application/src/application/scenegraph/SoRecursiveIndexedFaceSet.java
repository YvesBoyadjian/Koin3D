/**
 * 
 */
package application.scenegraph;

import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.nodes.SoIndexedFaceSet;

/**
 * @author Yves Boyadjian
 *
 */
public class SoRecursiveIndexedFaceSet extends SoIndexedFaceSet {
	
	RecursiveChunk recursiveChunk;

	public SoRecursiveIndexedFaceSet(RecursiveChunk recursiveChunk) {
		this.recursiveChunk = recursiveChunk;
	}

	public void clear() {
		coordIndex.setValuesPointer(recursiveChunk.getDecimatedCoordIndices());
		vertexProperty.setValue(recursiveChunk.getVertexProperty());		
	}
	
	public void computeBBox(SoAction action, SbBox3f box, SbVec3f center) {
		box.copyFrom(recursiveChunk.sceneBox);
		center.copyFrom(recursiveChunk.sceneCenter);
	}
}
