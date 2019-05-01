/**
 * 
 */
package application.scenegraph;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import jscenegraph.coin3d.inventor.nodes.SoVertexProperty;
import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.nodes.SoIndexedFaceSet;
import jscenegraph.database.inventor.nodes.SoNode;

/**
 * @author Yves Boyadjian
 *
 */
public class Chunk {
	
	public static final int CHUNK_WIDTH = /*2 * 3 **/4 * 4 * 3 * 3 * 3 * 3 + 1; //325
	
	public static final int NB_LOD = 5;
	
	private int chunkWidth;
	
	private float delta_x;
	private float delta_y;
	private float x0;
	private float y0;
	
	float[] vertices;
	float[] normals;
	byte[] colors;
	//int[] coordIndices;	

	SoIndexedFaceSet[] LODindexedFaceSets;
	SoIndexedFaceSet shadowIndexedFaceSet;
	
	SbBox3f sceneBox = new SbBox3f();
	SbVec3f sceneCenter = new SbVec3f();
	
	public Chunk(int chunkWidth) {
		this.chunkWidth = chunkWidth;
	}

	public void initArrays() {
		
		int nbVertices = chunkWidth *chunkWidth;
		vertices = new float[nbVertices*3];
		normals = new float[nbVertices*3];
		colors = new byte[nbVertices*4];
		
//		int nbCoordIndices = (chunkWidth-1)*(chunkWidth-1)*5;
//		coordIndices = new int[nbCoordIndices];
	}

//	public void initIndices() {
//		int indice=0;
//		for(int i=1;i<chunkWidth;i++) {
//		for(int j=1; j<chunkWidth;j++) {
//			coordIndices[indice++] = (i-1)*chunkWidth+(j-1); //1
//			coordIndices[indice++] = (i)*chunkWidth+(j-1); //2
//			coordIndices[indice++] = (i)*chunkWidth+(j); //3
//			coordIndices[indice++] = (i-1)*chunkWidth+(j); //4
//			coordIndices[indice++] = -1; 
//		}
//		}
//	}

	public void initIndexedFaceSet() {
		
//	    SoVertexProperty vertexProperty = new SoVertexProperty();
//	    
//	    vertexProperty.vertex.setValuesPointer(/*0,*/ vertices);
//	    vertexProperty.normalBinding.setValue(SoVertexProperty.Binding.PER_VERTEX_INDEXED);
//	    vertexProperty.normal.setValuesPointer(/*0,*/ normals);
//	    vertexProperty.materialBinding.setValue(SoVertexProperty.Binding.PER_VERTEX_INDEXED);
//	    vertexProperty.orderedRGBA.setValues(0, colors);
	    
	    int nbVertices = vertices.length /3;
	    final SbVec3f ptV = new SbVec3f();
	    for(int i=0;i<nbVertices;i++) {
	    	ptV.setValue(vertices[i*3], vertices[i*3+1], vertices[i*3+2]);
	    	sceneBox.extendBy(ptV);
	    }
	    sceneCenter.setValue(sceneBox.getCenter());
	    
	    LODindexedFaceSets = new SoIndexedFaceSet[NB_LOD];
	    
//	    LODindexedFaceSets[0] = new SoIndexedFaceSet() {
//	    	public void computeBBox(SoAction action, SbBox3f box, SbVec3f center) {
//	    		box.copyFrom(sceneBox);
//	    		center.copyFrom(sceneCenter);
//	    	}
//	    };
//	    
//	    LODindexedFaceSets[0].vertexProperty.setValue(vertexProperty);
//	    LODindexedFaceSets[0].coordIndex.setValues(0, coordIndices);
	    
	    for(int l=0;l<NB_LOD;l++) {
	    	//LODindexedFaceSets[l] = buildIndexedFaceSet(l);
	    }
	    //shadowIndexedFaceSet = buildIndexedFaceSet(Math.min(3,NB_LOD -1));
	}
	
	private SoIndexedFaceSet buildIndexedFaceSet(int l) {
	    	
    	SoVertexProperty vertexProperty = getVertexProperty(l);
	    
	    SoIndexedFaceSet LODindexedFaceSets;
	    if( l==0) {
		    LODindexedFaceSets = new SoIndexedFaceSet() {
		    	public void computeBBox(SoAction action, SbBox3f box, SbVec3f center) {
		    		box.copyFrom(sceneBox);
		    		center.copyFrom(sceneCenter);
		    	}
		    	public void GLRender(SoGLRenderAction action)
		    	{
		    		super.GLRender(action);
		    	}
		    };
	    	
	    }
	    else {
	    LODindexedFaceSets = new SoIndexedFaceSet() {
	    	public void computeBBox(SoAction action, SbBox3f box, SbVec3f center) {
	    		box.copyFrom(sceneBox);
	    		center.copyFrom(sceneCenter);
	    	}
	    };
	    }
	    LODindexedFaceSets.vertexProperty.setValue(vertexProperty);
	    LODindexedFaceSets.coordIndex.setValuesPointer(/*0,*/ getDecimatedCoordIndices(l));
	    return LODindexedFaceSets;
	}
	
	public SoVertexProperty getVertexProperty(int l) {
		
    	SoVertexProperty vertexProperty = new SoVertexProperty();
	    
	    vertexProperty.vertex.setValuesPointer(/*0,*/ getDecimatedVertices(l),getDecimatedVerticesBuffer(l));
	    vertexProperty.normalBinding.setValue(SoVertexProperty.Binding.PER_VERTEX_INDEXED);
	    vertexProperty.normal.setValuesPointer(/*0,*/ getDecimatedNormals(l),getDecimatedNormalsBuffer(l));
	    //vertexProperty.materialBinding.setValue(SoVertexProperty.Binding.PER_VERTEX_INDEXED);
	    //vertexProperty.orderedRGBA.setValues(0, getDecimatedColors(l));
	    vertexProperty.texCoord.setValuesPointer(/*0,*/ getDecimatedTexCoords(l), getDecimatedTexCoordsBuffer(l));

	    return vertexProperty;
	}
	
	private float[][] decimatedTextCoords = new float[NB_LOD][];
	
	private float[] getDecimatedTexCoords(int l) {
		
		if(decimatedTextCoords[l] == null) {
		
		int decimatedChunkWidth = getDecimatedChunkWidth(l);
		float[] array = new float[decimatedChunkWidth*decimatedChunkWidth*2];
		for(int i=0;i<decimatedChunkWidth;i++) {
			for(int j=0; j<decimatedChunkWidth; j++) {
				int index = i*decimatedChunkWidth+j;
				array[index*2+1] = (i+0.5f)/decimatedChunkWidth;
				array[index*2] = (j+0.5f)/decimatedChunkWidth;
			}
		}
		this.decimatedTextCoords[l] = array;
		
		}
		return decimatedTextCoords[l];
	}
	
	private FloatBuffer[] decimatedTextCoordsBuffer = new FloatBuffer[NB_LOD];
	
	private FloatBuffer getDecimatedTexCoordsBuffer(int l) {
		if(decimatedTextCoordsBuffer[l] == null) {
			decimatedTextCoordsBuffer[l] = BufferUtils.createFloatBuffer(getDecimatedTexCoords(l).length);
		}
		return decimatedTextCoordsBuffer[l];
	}

	public static int getDecimatedChunkWidth(int i) {
		
		int mul = CHUNK_WIDTH - 1;
		for( int ii=0;ii<i;ii++) {
			mul /= 3;
		}
		int decimatedChunkWidth = mul + 1;
		return decimatedChunkWidth;
	}
	
	private int[][] decimatedCoordIndices = new int [NB_LOD][];

	public int[] getDecimatedCoordIndices(int l) {
		
		if(decimatedCoordIndices[l] == null) {
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
			this.decimatedCoordIndices[l] = decimatedCoordIndices;
		}
		return decimatedCoordIndices[l];
	}
	
	private int fromSonToSource(int indice, int l) {
		for(int i=0;i<l;i++) {
			indice *= 3;
		}
		return indice;
	}
	
	int byteToInt(byte value) {
		int val = value;
		if(val < 0) {
			val += 256;
		}
		return val;
	}

//	private int[] getDecimatedColors(int l) {
//		int sourceChunkWidth = getDecimatedChunkWidth(0);
//		int decimatedChunkWidth = getDecimatedChunkWidth(l);
//		int nbVertices = decimatedChunkWidth *decimatedChunkWidth;
//
//		int[] decimatedColors = new int[nbVertices];
//				
//		int delta = ((l==0) ? 0 : 1);
//		
//		int indice = 0; 
//		for(int i =0 ; i< decimatedChunkWidth; i++) {
//			for(int j =0 ; j< decimatedChunkWidth; j++) {
//				int i0 = fromSonToSource(i,l);
//				int j0 = fromSonToSource(j,l);
//
//				float red = 0;
//				float green = 0;
//				float blue = 0;
//				float alpha = 0;
//				int nb = 0;
//				for(int di = -delta; di<=delta; di++) {
//					for( int dj = -delta; dj<= delta; dj++) {
//						
//						int i1 = i0 + di;
//						int j1 = j0 + dj;
//						
//						if(i1 < 0) {
//							i1 = 0;
//						}
//						if(j1 < 0) {
//							j1 = 0;
//						}
//						if(i1 >= sourceChunkWidth) {
//							i1 = sourceChunkWidth - 1;
//						}
//						if(j1 >= sourceChunkWidth) {
//							j1 = sourceChunkWidth - 1;
//						}
//						int indice0 = i1*chunkWidth+j1;
//						red += byteToInt(colors[indice0*4]);
//						green += byteToInt(colors[indice0*4+1]);
//						blue += byteToInt(colors[indice0*4+2]);
//						alpha += byteToInt(colors[indice0*4+3]);
//						nb++;
//					}
//				}
//				float rf = red/(float)nb;
//				float rg = green/(float)nb;
//				float rb = blue/(float)nb;
//				float ra = alpha/(float)nb;
//				int redi = (int)( rf );
//				int greeni = (int)( rg);
//				int bluei = (int)( rb);
//				int alphai = (int)( ra);
//				int rgba = (alphai << 0) | (redi << 24)| (greeni << 16)|(bluei<<8);
//				decimatedColors[indice] = rgba;//colors[indice0];
//				indice++;
//			}
//		}
//		return decimatedColors;
//	}

	private FloatBuffer[] decimatedNormalsBuffers = new FloatBuffer[NB_LOD];

	private FloatBuffer getDecimatedNormalsBuffer(int l) {
		if(decimatedNormalsBuffers[l] == null) {
			decimatedNormalsBuffers[l] = BufferUtils.createFloatBuffer(getDecimatedNormals(l).length);
		}
		return decimatedNormalsBuffers[l];
	}	
	
	private float[][] decimatedNormals = new float[NB_LOD][];

	private float[] getDecimatedNormals(int l) {
		
		if(decimatedNormals[l] == null) {
			
			if(l==0) {
				decimatedNormals[l] = normals;
			}
			else {
		
		int sourceChunkWidth = getDecimatedChunkWidth(0);
		int decimatedChunkWidth = getDecimatedChunkWidth(l);
		int nbVertices = decimatedChunkWidth *decimatedChunkWidth;

		float[] decimatedNormals = new float[nbVertices*3];
		
		int nb = 1;
		for(int i=0;i<l;i++) {
			nb *= 3;
		}
		int delta = (nb - 1)/2;
		
		int indice = 0; 
		for(int i =0 ; i< decimatedChunkWidth; i++) {
			for(int j =0 ; j< decimatedChunkWidth; j++) {
				int i0 = fromSonToSource(i,l);
				int j0 = fromSonToSource(j,l);
				int nbc = 0;
				for(int di = -delta; di<=delta; di++) {
					for( int dj = -delta; dj<= delta; dj++) {
						
						int i1 = i0 + di;
						int j1 = j0 + dj;
						
						if(i1 < 0) {
							i1 = 0;
						}
						if(j1 < 0) {
							j1 = 0;
						}
						if(i1 >= sourceChunkWidth) {
							i1 = sourceChunkWidth - 1;
						}
						if(j1 >= sourceChunkWidth) {
							j1 = sourceChunkWidth - 1;
						}
						//int indice0 = i0*chunkWidth+j0;
						int indice0 = i1*chunkWidth+j1;
						decimatedNormals[indice*3] += normals[indice0*3];
						decimatedNormals[indice*3+1] += normals[indice0*3+1];
						decimatedNormals[indice*3+2] += normals[indice0*3+2];
						nbc++;
					}
				}
				decimatedNormals[indice*3] /= nbc;
				decimatedNormals[indice*3+1] /= nbc;
				decimatedNormals[indice*3+2] /= nbc;
				indice++;
			}
		}
		this.decimatedNormals[l] = decimatedNormals;
			}
		}
		return decimatedNormals[l];
	}
	
	private FloatBuffer[] decimatedVerticesBuffers = new FloatBuffer[NB_LOD];

	private FloatBuffer getDecimatedVerticesBuffer(int l) {
		if(decimatedVerticesBuffers[l] == null) {
			decimatedVerticesBuffers[l] = BufferUtils.createFloatBuffer(getDecimatedVertices(l).length);
		}
		return decimatedVerticesBuffers[l];
	}	
	
	private float[][] decimatedVertices = new float[NB_LOD][];

	private float[] getDecimatedVertices(int l) {
		
		if(decimatedVertices[l] == null) {
			
			if(l==0) {
				decimatedVertices[l] = vertices;
			}
			else {
		
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
					int i0 = fromSonToSource(i,l);
					int j0 = fromSonToSource(j,l);//chunkWidth -1 - ((decimatedChunkWidth -1 -j) << l);
					int indice0 = i0*chunkWidth+j0;
					int indice = i*decimatedChunkWidth+j;
					decimatedVertices[indice*3] = vertices[indice0*3];
					decimatedVertices[indice*3+1] = vertices[indice0*3+1];
					decimatedVertices[indice*3+2] = vertices[indice0*3+2];
				}
			}
			this.decimatedVertices[l] = decimatedVertices;
			}
		}
		return decimatedVertices[l];
	}

	public SoNode getIndexedFaceSet(int i) {
		return LODindexedFaceSets[i];
	}
	
	public SoNode getShadowIndexedFaceSet() {
		return shadowIndexedFaceSet;
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
	
	public short getImageSize() {
		int size = Chunk.getDecimatedChunkWidth(0);
		return (short)size;
	}

	public byte[] getImage() {
		int size = Chunk.getDecimatedChunkWidth(0);
		byte[] image = new byte[size*size*3];
		for(int i=0;i<size;i++) {
			for(int j=0;j<size;j++) {
				int index = i*size+j;
				image[index*3] = colors[index*4];
				image[index*3+1] = colors[index*4+1];
				image[index*3+2] = colors[index*4+2];
			}
		}
		return image;
	}

}
