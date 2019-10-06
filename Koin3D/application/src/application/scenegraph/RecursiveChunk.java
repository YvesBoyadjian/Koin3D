/**
 * 
 */
package application.scenegraph;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.lwjgl.BufferUtils;

import jscenegraph.coin3d.inventor.nodes.SoLOD;
import jscenegraph.coin3d.inventor.nodes.SoVertexProperty;
import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.nodes.SoCamera;
import jscenegraph.database.inventor.nodes.SoGroup;
import jscenegraph.database.inventor.nodes.SoIndexedFaceSet;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoPerspectiveCamera;
import jscenegraph.database.inventor.nodes.SoSeparator;

/**
 * @author Yves Boyadjian
 *
 */
public class RecursiveChunk {
	
	final int MIN_CHUNK_SIZE = 100;
	
	RecursiveChunk parent;
	int i0;
	int j0;
	int ni;
	int nj;
	ChunkArray ca;
	
	float x_min, x_max, y_min, y_max;
	
	SbBox3f sceneBox = new SbBox3f();
	SbVec3f sceneCenter = new SbVec3f();
	
	List<RecursiveChunk> childs = new ArrayList<>();

	public RecursiveChunk(ChunkArray ca, RecursiveChunk parent, int i0, int j0, int ni, int nj) {
		this.ca = ca;
		this.parent = parent;
		this.i0 = i0;
		this.j0 = j0;
		this.ni = ni;
		this.nj = nj;
		
		if (ni > MIN_CHUNK_SIZE && nj > MIN_CHUNK_SIZE) {
			int mi = ni/2;
			int mj = nj/2;
			RecursiveChunk c1 = new RecursiveChunk(ca,this,i0,j0,mi+1,mj+1);
			RecursiveChunk c2 = new RecursiveChunk(ca,this,i0,j0+mj,mi+1,nj - mj);
			RecursiveChunk c3 = new RecursiveChunk(ca,this,i0+mi,j0,ni - mi,mj+1);
			RecursiveChunk c4 = new RecursiveChunk(ca,this,i0+mi,j0+mj,ni - mi,nj - mj);
			childs.add(c1);
			childs.add(c2);
			childs.add(c3);
			childs.add(c4);
		}
	}
	
	public int getDecimatedChunkWidth() {
		return childs.isEmpty() ? ni : Math.min(ni, MIN_CHUNK_SIZE);
	}

	public int getDecimatedChunkHeight() {
		return childs.isEmpty() ? nj : Math.min(nj, MIN_CHUNK_SIZE);
	}

	public SoNode getGroup(float lodFactor, boolean culling) {
		
		if(childs.isEmpty()) {
			return getIndexedFaceSet();
		}
		else {
			SoLOD lod = new SoTouchLOD2();
			lod.center.setValue(getCenter());
			lod.range.setValue(ChunkArray.DEFINITION * ni / /*250.0f*/lodFactor);
			SoSeparator subChunkGroup = new SoSeparator();
			if(culling) {
				subChunkGroup.renderCulling.setValue(SoSeparator.CacheEnabled.ON);
			}
			
			subChunkGroup.addChild(childs.get(0).getGroup(lodFactor,culling));
			subChunkGroup.addChild(childs.get(1).getGroup(lodFactor,culling));
			subChunkGroup.addChild(childs.get(2).getGroup(lodFactor,culling));
			subChunkGroup.addChild(childs.get(3).getGroup(lodFactor,culling));
			lod.addChild(subChunkGroup);
			lod.addChild(getIndexedFaceSet());
			return lod;
		}
	}

	public SoNode getShadowGroup(float lodFactor, boolean culling) {
		
		if(childs.isEmpty()) {
			return getIndexedFaceSet();
		}
		else {
			SoCameraLOD lod = new SoTouchLOD3();
			lod.center.setValue(getCenter());
			lod.range.setValue(500 + ChunkArray.DEFINITION * ni / /*250.0f*/lodFactor);
			SoSeparator subChunkGroup = new SoSeparator();
			if(culling) {
				subChunkGroup.renderCulling.setValue(SoSeparator.CacheEnabled.ON);
			}
			
			subChunkGroup.addChild(childs.get(0).getShadowGroup(lodFactor,culling));
			subChunkGroup.addChild(childs.get(1).getShadowGroup(lodFactor,culling));
			subChunkGroup.addChild(childs.get(2).getShadowGroup(lodFactor,culling));
			subChunkGroup.addChild(childs.get(3).getShadowGroup(lodFactor,culling));
			lod.addChild(subChunkGroup);
			lod.addChild(getIndexedFaceSet());
			return lod;
		}
	}
	
	public static void setCamera(SoNode node, SoCamera camera) {
		if( node instanceof SoCameraLOD) {
			SoCameraLOD cnode = (SoCameraLOD) node;			
			cnode.setCamera(camera);
		}
		if( node instanceof SoTouchLOD2) {
			SoTouchLOD2 cnode = (SoTouchLOD2) node;			
			cnode.setCamera(camera);
		}
		if( node instanceof SoGroup) {
			SoGroup cnode = (SoGroup) node;			
			for(int i= 0; i<cnode.getNumChildren();i++) {
				setCamera(cnode.getChild(i),camera);
			}
		}
	}

	private SoNode getIndexedFaceSet() {
		SoIndexedFaceSet indexedFaceSet = new SoRecursiveIndexedFaceSet(this);
		
		SoVertexProperty vertexProperty = new SoVertexProperty();
		vertexProperty.vertex.setValuesPointer(getDecimatedVertices());
	    vertexProperty.normalBinding.setValue(SoVertexProperty.Binding.PER_VERTEX_INDEXED);
	    vertexProperty.normal.setValuesPointer(/*0,*/ getDecimatedNormals());
	    vertexProperty.texCoord.setValuesPointer(/*0,*/ getDecimatedTexCoords());
		
		indexedFaceSet.vertexProperty.setValue(vertexProperty);

	    indexedFaceSet.coordIndex.setValuesPointer(/*0,*/ getDecimatedCoordIndices());
		
		return indexedFaceSet;
	}

	private float[] getDecimatedTexCoords() {
		
		if(decimatedTextCoords == null) {
			
		int decimatedChunkWidth = getDecimatedChunkWidth();
		int decimatedChunkHeight = getDecimatedChunkHeight();
		float[] array = new float[decimatedChunkWidth*decimatedChunkHeight*2];
		for(int i=0;i<decimatedChunkWidth;i++) {
			for(int j=0; j<decimatedChunkHeight; j++) {
				int index = i*decimatedChunkHeight+j;
				float alpha = (i/*+0.5f*/)/(decimatedChunkWidth-1.0f);
				float imin = (float)(i0)/(ca.getW()-1.0f);
				float imax = (float)(i0+ni-1)/(ca.getW()-1.0f);
				float beta = (j/*+0.5f*/)/(decimatedChunkHeight-1.0f);
				float jmin = (float)(j0)/(ca.getH()-1.0f);
				float jmax = (float)(j0+nj-1)/(ca.getH()-1.0f);
				array[index*2] = imin + alpha*(imax -imin);
				array[index*2+1] = jmin + beta*(jmax-jmin);
			}
		}
		this.decimatedTextCoords = array;
		
		}
		return decimatedTextCoords;
	}

	private float[] getDecimatedVertices() {
		if(decimatedVertices == null) {
			int decimatedChunkWidth = getDecimatedChunkWidth();
			int decimatedChunkHeight = getDecimatedChunkHeight();
			int nbVertices = decimatedChunkWidth * decimatedChunkHeight;
			decimatedVertices = new float[nbVertices*3];
			
			float[] xyz = new float[3];
			
			x_min = Float.MAX_VALUE;
			x_max = - Float.MAX_VALUE;
			y_min = Float.MAX_VALUE;
			y_max = - Float.MAX_VALUE;
			
			for(int i =0 ; i< decimatedChunkWidth; i++) {
				for(int j =0 ; j<decimatedChunkHeight ; j++) {
					int i0 = fromSonToSourceI(i);
					int j0 = fromSonToSourceJ(j);//chunkWidth -1 - ((decimatedChunkWidth -1 -j) << l);
					int indice0 = i0*ca.getH()+j0;
					int indice = i*decimatedChunkHeight+j;
					ca.verticesGet(indice0, xyz);
					decimatedVertices[indice*3] = xyz[0];
					decimatedVertices[indice*3+1] = xyz[1];
					decimatedVertices[indice*3+2] = xyz[2];
					
					x_min = Math.min(decimatedVertices[indice*3],x_min);
					x_max = Math.max(decimatedVertices[indice*3],x_max);
					y_min = Math.min(decimatedVertices[indice*3+1],y_min);
					y_max = Math.max(decimatedVertices[indice*3+1],y_max);
					
					sceneBox.extendBy(new SbVec3f(decimatedVertices[indice*3],decimatedVertices[indice*3+1],decimatedVertices[indice*3+2]));
				}
			}
			sceneCenter.setValue(sceneBox.getCenter());
		}
		return decimatedVertices;
	}
	
	private FloatBuffer getDecimatedVerticesBuffer() {
		if(decimatedVerticesBuffer == null) {
			
			int length = getDecimatedVertices().length;
			decimatedVerticesBuffer = BufferUtils.createFloatBuffer(length);
			decimatedVerticesBuffer.clear();
			decimatedVerticesBuffer.put(getDecimatedVertices(), 0, length);
			decimatedVerticesBuffer.flip();
		}
		return decimatedVerticesBuffer;
	}
	
	private FloatBuffer getDecimatedNormalsBuffer() {
		if(decimatedNormalsBuffer == null) {
			
			int length = getDecimatedNormals().length;
			decimatedNormalsBuffer = BufferUtils.createFloatBuffer(length);
			decimatedNormalsBuffer.clear();
			decimatedNormalsBuffer.put(getDecimatedNormals(), 0, length);
			decimatedNormalsBuffer.flip();
		}
		return decimatedNormalsBuffer;
	}
	
	private FloatBuffer getDecimatedTexCoordsBuffer() {
		if(decimatedTexCoordsBuffer == null) {
			
			int length = getDecimatedTexCoords().length;
			decimatedTexCoordsBuffer = BufferUtils.createFloatBuffer(length);
			decimatedTexCoordsBuffer.clear();
			decimatedTexCoordsBuffer.put(getDecimatedTexCoords(), 0, length);
			decimatedTexCoordsBuffer.flip();
		}
		return decimatedTexCoordsBuffer;
	}
	
	private float[] getDecimatedNormals() {
		
		if(decimatedNormals == null) {					
			int sourceChunkWidth = ni;
			int sourceChunkHeight = nj;
			int decimatedChunkWidth = getDecimatedChunkWidth();
			int decimatedChunkHeight = getDecimatedChunkHeight();
			int nbVertices = decimatedChunkWidth *decimatedChunkHeight;
	
			float[] decimatedNormals = new float[nbVertices*3];
			
			int nb = 1;
//			for(int i=0;i<l;i++) {
//				nb *= 3;
//			}
			int delta = (nb - 1)/2;
			
			int indice = 0; 
			for(int i =0 ; i< decimatedChunkWidth; i++) {
				for(int j =0 ; j< decimatedChunkHeight; j++) {
					int i0 = fromSonToSourceI(i);
					int j0 = fromSonToSourceJ(j);
					int nbc = 0;
					for(int di = -delta; di<=delta; di++) {
						for( int dj = -delta; dj<= delta; dj++) {
							
							int i1 = i0 + di;
							int j1 = j0 + dj;
							
							if(i1 < this.i0) {
								i1 = this.i0;
							}
							if(j1 < this.j0) {
								j1 = this.j0;
							}
							if(i1 >= sourceChunkWidth + this.i0) {
								i1 = sourceChunkWidth - 1 + this.i0;
							}
							if(j1 >= sourceChunkHeight + this.j0) {
								j1 = sourceChunkHeight - 1 + this.j0;
							}
							//int indice0 = i0*chunkWidth+j0;
							int indice0 = i1*ca.getH()+j1;
							decimatedNormals[indice*3] += ca.normalsGet(indice0*3);
							decimatedNormals[indice*3+1] += ca.normalsGet(indice0*3+1);
							decimatedNormals[indice*3+2] += ca.normalsGet(indice0*3+2);
							nbc++;
						}
					}
					decimatedNormals[indice*3] /= nbc;
					decimatedNormals[indice*3+1] /= nbc;
					decimatedNormals[indice*3+2] /= nbc;
					indice++;
				}
			}
			this.decimatedNormals = decimatedNormals;
		}
		return decimatedNormals;
	}	
	
	public int[] getDecimatedCoordIndices() {
		
		if(decimatedCoordIndices == null) {
			int decimatedChunkWidth = getDecimatedChunkWidth();
			int decimatedChunkHeight = getDecimatedChunkHeight();
			int nbCoordIndices = (decimatedChunkWidth-1)*(decimatedChunkHeight-1)*5;
			int[] decimatedCoordIndices = new int[nbCoordIndices];
			int indice=0;
			for(int i=1;i<decimatedChunkWidth;i++) {
			for(int j=1; j<decimatedChunkHeight;j++) {
				decimatedCoordIndices[indice++] = (i-1)*decimatedChunkHeight+(j-1); //1
				decimatedCoordIndices[indice++] = (i)*decimatedChunkHeight+(j-1); //2
				decimatedCoordIndices[indice++] = (i)*decimatedChunkHeight+(j); //3
				decimatedCoordIndices[indice++] = (i-1)*decimatedChunkHeight+(j); //4
				decimatedCoordIndices[indice++] = -1; 
			}
			}
			this.decimatedCoordIndices = decimatedCoordIndices;
		}
		return decimatedCoordIndices;
	}
	
	private int fromSonToSourceI(int i) {
		return i*(ni-1)/(getDecimatedChunkWidth()-1)+i0;
	}

	private int fromSonToSourceJ(int j) {
		return j*(nj-1)/(getDecimatedChunkHeight()-1)+j0;
	}

	private float[] decimatedVertices;
	
	private FloatBuffer decimatedVerticesBuffer;
	
	private float[] decimatedNormals;
	
	private FloatBuffer decimatedNormalsBuffer;
	
	private float[] decimatedTextCoords;
	
	private FloatBuffer decimatedTexCoordsBuffer;
	
	private int[] decimatedCoordIndices;
	
	private float[] center;

	private float[] getCenter() {
		
		if(center == null) {
		
			float xc = 0,yc = 0,zc = 0;
			int count = 0;
			
			float[] xyz = new float[3];
			
			for(int i=i0;i<i0+ni;i+=(ni-1)) {
				for(int j=j0;j<j0+nj;j+=(nj-1)) {
					int index = i*ca.getH()+j;
					ca.verticesGet(index,xyz);
					xc += xyz[0];
					yc += xyz[1];
					zc += xyz[2];
					count++;
				}
			}
			xc /= count;
			yc /= count;
			zc /= count;
			//float[] xyz = new float[3];
			xyz[0] = xc;
			xyz[1] = yc;
			xyz[2] = zc;
			center = xyz;
		}
		return center;
	}

	public SoNode getVertexProperty() {
		SoVertexProperty vertexProperty = new SoVertexProperty();
		vertexProperty.vertex.setValuesPointer(getDecimatedVertices(),getDecimatedVerticesBuffer());
	    vertexProperty.normalBinding.setValue(SoVertexProperty.Binding.PER_VERTEX_INDEXED);
	    vertexProperty.normal.setValuesPointer(/*0,*/ getDecimatedNormals(),getDecimatedNormalsBuffer());
	    vertexProperty.texCoord.setValuesPointer(/*0,*/ getDecimatedTexCoords(),getDecimatedTexCoordsBuffer());
		
		return vertexProperty;
	}

	public void prepare() {
		getDecimatedVertices();
		getDecimatedNormals();
		getDecimatedTexCoords();
		getDecimatedCoordIndices();
		getCenter();
		
		childs.parallelStream().forEach((c)-> c.prepare());
	}
	
	public boolean isInside(float x, float y) {
		if( x < x_min) {
			return false;
		}
		if( x > x_max) {
			return false;
		}
		if( y < y_min) {
			return false;
		}
		if( y > y_max) {
			return false;
		}
		return true;
	}
	
	public float distance(float x, float y) {
		SbVec3f point = new SbVec3f(x,y,sceneCenter.getZ());
		
		SbVec3f closestPoint = sceneBox.getClosestPoint(point);
		
		float distance = point.operator_minus(closestPoint).length();
		
		return distance;
	}
}
