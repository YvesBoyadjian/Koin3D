/**
 * 
 */
package application.scenegraph;

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
}
