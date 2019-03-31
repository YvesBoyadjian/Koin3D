/**
 * 
 */
package application.scenegraph;

import jscenegraph.coin3d.inventor.nodes.SoVertexProperty;
import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.nodes.SoIndexedFaceSet;
import jscenegraph.database.inventor.nodes.SoNode;

/**
 * @author Yves Boyadjian
 *
 */
public class Chunk {
	
	public static final int CHUNK_WIDTH = (1 << 8) + 1;
	
	public static final int NB_LOD = 5;
	
	private int chunkWidth;
	
	private float delta_x;
	private float delta_y;
	private float x0;
	private float y0;
	
	float[] vertices;
	float[] normals;
	int[] colors;
	int[] coordIndices;	

	SoIndexedFaceSet[] LODindexedFaceSets;
	
	SbBox3f sceneBox = new SbBox3f();
	SbVec3f sceneCenter = new SbVec3f();
	
	public Chunk(int chunkWidth) {
		this.chunkWidth = chunkWidth;
	}

	public void initArrays() {
		
		int nbVertices = chunkWidth *chunkWidth;
		vertices = new float[nbVertices*3];
		normals = new float[nbVertices*3];
		colors = new int[nbVertices];
		
		int nbCoordIndices = (chunkWidth-1)*(chunkWidth-1)*5;
		coordIndices = new int[nbCoordIndices];
	}

	public void initIndices() {
		int indice=0;
		for(int i=1;i<chunkWidth;i++) {
		for(int j=1; j<chunkWidth;j++) {
			coordIndices[indice++] = (i-1)*chunkWidth+(j-1); //1
			coordIndices[indice++] = (i)*chunkWidth+(j-1); //2
			coordIndices[indice++] = (i)*chunkWidth+(j); //3
			coordIndices[indice++] = (i-1)*chunkWidth+(j); //4
			coordIndices[indice++] = -1; 
		}
		}
	}

	public void initIndexedFaceSet() {
		
	    SoVertexProperty vertexProperty = new SoVertexProperty();
	    
	    vertexProperty.vertex.setValuesPointer(/*0,*/ vertices);
	    vertexProperty.normalBinding.setValue(SoVertexProperty.Binding.PER_VERTEX_INDEXED);
	    vertexProperty.normal.setValuesPointer(/*0,*/ normals);
	    vertexProperty.materialBinding.setValue(SoVertexProperty.Binding.PER_VERTEX_INDEXED);
	    vertexProperty.orderedRGBA.setValues(0, colors);
	    
	    int nbVertices = vertices.length /3;
	    final SbVec3f ptV = new SbVec3f();
	    for(int i=0;i<nbVertices;i++) {
	    	ptV.setValue(vertices[i*3], vertices[i*3+1], vertices[i*3+2]);
	    	sceneBox.extendBy(ptV);
	    }
	    sceneCenter.setValue(sceneBox.getCenter());
	    
	    LODindexedFaceSets = new SoIndexedFaceSet[NB_LOD];
	    
	    LODindexedFaceSets[0] = new SoIndexedFaceSet() {
	    	public void computeBBox(SoAction action, SbBox3f box, SbVec3f center) {
	    		box.copyFrom(sceneBox);
	    		center.copyFrom(sceneCenter);
	    	}
	    };
	    
	    LODindexedFaceSets[0].vertexProperty.setValue(vertexProperty);
	    LODindexedFaceSets[0].coordIndex.setValues(0, coordIndices);
	    
	    for(int l=1;l<NB_LOD;l++) {
	    	
		    vertexProperty = new SoVertexProperty();
		    
		    vertexProperty.vertex.setValuesPointer(/*0,*/ getDecimatedVertices(l));
		    vertexProperty.normalBinding.setValue(SoVertexProperty.Binding.PER_VERTEX_INDEXED);
		    vertexProperty.normal.setValuesPointer(/*0,*/ getDecimatedNormals(l));
		    vertexProperty.materialBinding.setValue(SoVertexProperty.Binding.PER_VERTEX_INDEXED);
		    vertexProperty.orderedRGBA.setValues(0, getDecimatedColors(l));
		    
		    LODindexedFaceSets[l] = new SoIndexedFaceSet() {
		    	public void computeBBox(SoAction action, SbBox3f box, SbVec3f center) {
		    		box.copyFrom(sceneBox);
		    		center.copyFrom(sceneCenter);
		    	}
		    };
		    
		    LODindexedFaceSets[l].vertexProperty.setValue(vertexProperty);
		    LODindexedFaceSets[l].coordIndex.setValues(0, getDecimatedCoordIndices(l));
	    }
	}
	
	private int getDecimatedChunkWidth(int i) {
		int decimatedChunkWidth = ((chunkWidth -1) >> i) + 1;
		return decimatedChunkWidth;
	}

	private int[] getDecimatedCoordIndices(int l) {
		int decimatedChunkWidth = getDecimatedChunkWidth(l);
		int nbCoordIndices = (decimatedChunkWidth-1)*(decimatedChunkWidth-1)*5;
		int[] decimatedCoordIndices = new int[nbCoordIndices];
		int indice=0;
		for(int i=1;i<decimatedChunkWidth;i++) {
		for(int j=1; j<decimatedChunkWidth;j++) {
			decimatedCoordIndices[indice++] = (i-1)*decimatedChunkWidth+(j-1); //1
			decimatedCoordIndices[indice++] = (i)*decimatedChunkWidth+(j-1); //2
			decimatedCoordIndices[indice++] = (i)*decimatedChunkWidth+(j); //3
			decimatedCoordIndices[indice++] = (i-1)*decimatedChunkWidth+(j); //4
			decimatedCoordIndices[indice++] = -1; 
		}
		}
		return decimatedCoordIndices;
	}

	private int[] getDecimatedColors(int l) {
		int decimatedChunkWidth = getDecimatedChunkWidth(l);
		int nbVertices = decimatedChunkWidth *decimatedChunkWidth;

		int[] decimatedColors = new int[nbVertices];
				
		int indice = 0; 
		for(int i =0 ; i< decimatedChunkWidth; i++) {
			for(int j =0 ; j< decimatedChunkWidth; j++) {
				int i0 = i << l;
				int j0 = j << l;
				int indice0 = i0*chunkWidth+j0;
				decimatedColors[indice] = colors[indice0];
				indice++;
			}
		}
		return decimatedColors;
	}

	private float[] getDecimatedNormals(int l) {
		int decimatedChunkWidth = getDecimatedChunkWidth(l);
		int nbVertices = decimatedChunkWidth *decimatedChunkWidth;

		float[] decimatedNormals = new float[nbVertices*3];
		
		int indice = 0; 
		for(int i =0 ; i< decimatedChunkWidth; i++) {
			for(int j =0 ; j< decimatedChunkWidth; j++) {
				int i0 = i << l;
				int j0 = j << l;
				int indice0 = i0*chunkWidth+j0;
				decimatedNormals[indice*3] = normals[indice0*3];
				decimatedNormals[indice*3+1] = normals[indice0*3+1];
				decimatedNormals[indice*3+2] = normals[indice0*3+2];
				indice++;
			}
		}
		return decimatedNormals;
	}

	private float[] getDecimatedVertices(int l) {
		int decimatedChunkWidth = getDecimatedChunkWidth(l);
		int nbVertices = decimatedChunkWidth *decimatedChunkWidth;
		float[] decimatedVertices = new float[nbVertices*3];
		
//		float delta_x_decimated = delta_x * (chunkWidth-1)/(decimatedChunkWidth-1);
//		float delta_y_decimated = delta_y * (chunkWidth-1)/(decimatedChunkWidth-1);
//		
//		for(int i=0;i<decimatedChunkWidth;i++) {
//		for(int j=0; j<decimatedChunkWidth;j++) {
//				int index = i*decimatedChunkWidth+j;
//				decimatedVertices[index*3+0] = i * delta_x_decimated +x0;
//				decimatedVertices[index*3+1] = (decimatedChunkWidth - j -1) * delta_y_decimated + y0;
//		}
//		}
		
		for(int i =0 ; i< decimatedChunkWidth; i++) {
			for(int j =0 ; j<decimatedChunkWidth ; j++) {
				int i0 = i << l;
				int j0 = j << l;//chunkWidth -1 - ((decimatedChunkWidth -1 -j) << l);
				int indice0 = i0*chunkWidth+j0;
				int indice = i*decimatedChunkWidth+j;
				decimatedVertices[indice*3] = vertices[indice0*3];
				decimatedVertices[indice*3+1] = vertices[indice0*3+1];
				decimatedVertices[indice*3+2] = vertices[indice0*3+2];
			}
		}
		return decimatedVertices;
	}

	public SoNode getIndexedFaceSet(int i) {
		return LODindexedFaceSets[i];
	}

	public int getChunkIndice(int iInChunk, int jInChunk) {
		return iInChunk*chunkWidth+jInChunk;
	}

	public void initXY(float delta_x, float delta_y, float x0, float y0) {
		this.delta_x = delta_x;
		this.delta_y = delta_y;
		this.x0 = x0;
		this.y0 = y0;
		for(int i=0;i<chunkWidth;i++) {
		for(int j=0; j<chunkWidth;j++) {
				int index = i*chunkWidth+j;
				vertices[index*3+0] = i * delta_x +x0;
				vertices[index*3+1] = (chunkWidth - j -1) * delta_y + y0;
		}
		}
	}

	public SbVec3f getCenter() {
		return sceneCenter;
	}

}
