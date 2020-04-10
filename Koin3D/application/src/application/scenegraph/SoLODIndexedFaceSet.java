/**
 * 
 */
package application.scenegraph;

import jscenegraph.coin3d.inventor.nodes.SoVertexProperty;
import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.elements.SoGLCacheContextElement;
import jscenegraph.database.inventor.nodes.SoIndexedFaceSet;

/**
 * @author Yves Boyadjian
 *
 */
public class SoLODIndexedFaceSet extends SoIndexedFaceSet {
	
	public static enum Type {
		TRUNK,
		FOLIAGE
	}
	
	private final Type type;

	private final SbVec3f referencePoint;
	
	public float maxDistance;
	
	private final SbBox3f box = new SbBox3f();
	
	private final SbVec3f center = new SbVec3f();
	
	private final SbVec3f dummy = new SbVec3f(); //SINGLE_THREAD
	
	private final DouglasChunk chunk;
	
	private boolean loaded = false;
	
	private final int[] counting;
	
	public SoLODIndexedFaceSet(SbVec3f referencePoint, DouglasChunk chunk, Type type, final int[] counting) {
		this.referencePoint = referencePoint;
		this.chunk = chunk;
		this.type = type;
		this.counting = counting;
	}
	
	public void GLRender(SoGLRenderAction action)
	{		
		getBBox(action, box, center);
		
		if( box.intersect(referencePoint)) {
			load();
			super.GLRender(action);
		}
		else {
			SbVec3f closestPoint = box.getClosestExternalPoint(referencePoint);
			
			if( closestPoint.operator_minus(referencePoint,dummy).length() <= maxDistance ) {
				load();
				super.GLRender(action);				
			}
			else {
				clear();
			}
		}
		  // don't auto cache LOD nodes.
		  SoGLCacheContextElement.shouldAutoCache(action.getState(),
		                                           SoGLCacheContextElement.AutoCache.DONT_AUTO_CACHE.getValue());
	}		
	
	private void load() {
		switch(type) {
		case FOLIAGE:
			loadFoliage();
			break;
		case TRUNK:
			loadTrunk();
			break;
		default:
			break;
		
		}
	}
	
	public void loadTrunk() {
		if(!loaded && counting[0] < 2 /*&& counting[1] < 50*/) {
			counting[0]++;
			counting[1]++;
			loaded = true;
		SoLODIndexedFaceSet indexedFaceSetT = this;
		
		indexedFaceSetT.coordIndex.setValuesPointer(chunk.douglasIndicesT);
		
		SoVertexProperty vertexProperty = new SoVertexProperty();
		
		vertexProperty.vertex.setValuesPointer(chunk.douglasVerticesT);
		
		vertexProperty.normalBinding.setValue(SoVertexProperty.Binding.PER_VERTEX_INDEXED);
		
		vertexProperty.normal.setValuesPointer(chunk.douglasNormalsT);
		
		vertexProperty.materialBinding.setValue(SoVertexProperty.Binding.PER_VERTEX_INDEXED);
		
		vertexProperty.orderedRGBA.setValues(0, chunk.douglasColorsT);
		
		indexedFaceSetT.vertexProperty.setValue(vertexProperty);
		}
	}
	
	public void loadFoliage() {
		if(!loaded && counting[0] < 2 /*&& counting[1] < 50*/) {
			counting[0]++;
			counting[1]++;
			loaded = true;
		SoLODIndexedFaceSet indexedFaceSetF = this;
		
		indexedFaceSetF.coordIndex.setValuesPointer(chunk.douglasIndicesF);
		
		SoVertexProperty vertexProperty = new SoVertexProperty();
		
		vertexProperty.vertex.setValuesPointer(chunk.douglasVerticesF);
		
		vertexProperty.normalBinding.setValue(SoVertexProperty.Binding.PER_VERTEX_INDEXED);
		
		vertexProperty.normal.setValuesPointer(chunk.douglasNormalsF);
		
		boolean withColors = true;
		if(withColors) {
			vertexProperty.texCoord.setValuesPointer(chunk.douglasTexCoordsF);
			vertexProperty.materialBinding.setValue(SoVertexProperty.Binding.PER_VERTEX_INDEXED);
			vertexProperty.orderedRGBA.setValues(0, chunk.douglasColorsF);
		}
		else {
			vertexProperty.orderedRGBA.setValue(DouglasChunk.TREE_FOLIAGE_AVERAGE_MULTIPLIER/*SbColor(1,0.0f,0.0f)*/.getPackedValue());
		}
		
		indexedFaceSetF.vertexProperty.setValue(vertexProperty);
		}		
	}
	public void clear() {
		if(loaded) {
			counting[1]--;
			loaded = false;
		    boolean wasEnabled = this.vertexProperty.enableNotify(false);
			vertexProperty.setValue(null/*recursiveChunk.getVertexProperty()*/);
			this.vertexProperty.enableNotify(wasEnabled);
			
			//coordIndex.setValuesPointer(recursiveChunk.getDecimatedCoordIndices());
			//boolean wasEnabled = coordIndex.enableNotify(false);
			coordIndex.setNum(0);
		    //coordIndex.enableNotify(wasEnabled);
		}
	}
}
