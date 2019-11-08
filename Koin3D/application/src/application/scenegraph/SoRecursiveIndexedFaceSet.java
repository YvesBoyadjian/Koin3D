/**
 * 
 */
package application.scenegraph;

import jscenegraph.coin3d.inventor.nodes.SoVertexProperty;
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
	
	boolean cleared = true;

	public SoRecursiveIndexedFaceSet(RecursiveChunk recursiveChunk) {
		this.recursiveChunk = recursiveChunk;
	}
	
	private void doLoad() {
				
		SoVertexProperty vertexProperty = new SoVertexProperty();
		vertexProperty.vertex.setValuesPointer(recursiveChunk.getDecimatedVertices(),recursiveChunk.getDecimatedVerticesBuffer());
	    vertexProperty.normalBinding.setValue(SoVertexProperty.Binding.PER_VERTEX_INDEXED);
	    vertexProperty.normal.setValuesPointer(/*0,*/ recursiveChunk.getDecimatedNormals(),recursiveChunk.getDecimatedNormalsBuffer());
	    vertexProperty.texCoord.setValuesPointer(/*0,*/ recursiveChunk.getDecimatedTexCoords(),recursiveChunk.getDecimatedTexCoordsBuffer());
		
		this.vertexProperty.setValue(vertexProperty);

	    coordIndex.setValuesPointer(/*0,*/ recursiveChunk.getDecimatedCoordIndices());
	}

	public void clear() {
		//coordIndex.setValuesPointer(recursiveChunk.getDecimatedCoordIndices());
		coordIndex.setNum(0);
		vertexProperty.setValue(null/*recursiveChunk.getVertexProperty()*/);
		recursiveChunk.clear();
		cleared = true;
	}
	
	public void computeBBox(SoAction action, SbBox3f box, SbVec3f center) {
		box.copyFrom(recursiveChunk.sceneBox);
		center.copyFrom(recursiveChunk.sceneCenter);
	}

	public void GLRender(SoGLRenderAction action)
	{
		if(cleared) {
			doLoad();
		cleared = false;
//		long delta = stop - start;
//		
//		if(delta > 10e6) {
//			System.out.println("SoTouchLOD2 " + delta/1e6 +" ms");
//		}
//		int i=0;
	}
		//long start = System.nanoTime();
		super.GLRender(action);
		//long stop = System.nanoTime();
	}
}
