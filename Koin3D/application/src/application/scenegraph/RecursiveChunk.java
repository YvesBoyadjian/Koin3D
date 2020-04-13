/**
 * 
 */
package application.scenegraph;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
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
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.misc.SoNotList;
import jscenegraph.database.inventor.nodes.SoCamera;
import jscenegraph.database.inventor.nodes.SoGroup;
import jscenegraph.database.inventor.nodes.SoIndexedFaceSet;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoPerspectiveCamera;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.port.memorybuffer.FloatMemoryBuffer;

/**
 * @author Yves Boyadjian
 *
 */
public class RecursiveChunk {
	
	final private boolean KEEP_IN_MEMORY = false;
	
	final int MIN_CHUNK_SIZE = 80;
	
	RecursiveChunk parent;
	int rank;
	int i0;
	int j0;
	int ni;
	int nj;
	ChunkArray ca;
	
	float x_min, x_max, y_min, y_max;
	
	SbBox3f sceneBox = new SbBox3f();
	SbVec3f sceneCenter = new SbVec3f();
	
	List<RecursiveChunk> childs = new ArrayList<>();

	public RecursiveChunk(ChunkArray ca, RecursiveChunk parent,int rank, int i0, int j0, int ni, int nj) {
		this.ca = ca;
		this.parent = parent;
		this.rank = rank;
		this.i0 = i0;
		this.j0 = j0;
		this.ni = ni;
		this.nj = nj;
		
		if (ni > MIN_CHUNK_SIZE && nj > MIN_CHUNK_SIZE) {
			int mi = ni/2;
			int mj = nj/2;
			RecursiveChunk c1 = new RecursiveChunk(ca,this,1,i0,j0,mi+1,mj+1);
			RecursiveChunk c2 = new RecursiveChunk(ca,this,2,i0,j0+mj,mi+1,nj - mj);
			RecursiveChunk c3 = new RecursiveChunk(ca,this,3,i0+mi,j0,ni - mi,mj+1);
			RecursiveChunk c4 = new RecursiveChunk(ca,this,4,i0+mi,j0+mj,ni - mi,nj - mj);
			childs.add(c1);
			childs.add(c2);
			childs.add(c3);
			childs.add(c4);
		}
	}
	
	public int getLevel() {
		if( parent == null ) {
			return 1;
		}
		else {
			return parent.getLevel() + 1;
		}
	}
	
	public String getRankString() {
		
		String rankString = "r"+rank;
		
		if (parent != null) {
			rankString = parent.getRankString() + rankString;
		}
		return rankString;
	}
	
	public String getFilename(String variable) {
		return "recursive_chunk_"+variable+"_mcs"+MIN_CHUNK_SIZE+"_l"+getLevel()+"_"+getRankString()+".mri";
	}
	
	public int getDecimatedChunkWidth() {
		return childs.isEmpty() ? ni : Math.min(ni, MIN_CHUNK_SIZE);
	}

	public int getDecimatedChunkHeight() {
		return childs.isEmpty() ? nj : Math.min(nj, MIN_CHUNK_SIZE);
	}
	
	public float getDeltaX() {
		return Math.abs(x_max - x_min) / getDecimatedChunkWidth(); 
	}

	public SoNode getGroup(SoTouchLODMaster master, float lodFactor, boolean culling) {
		
		if(childs.isEmpty()) {
			return getIndexedFaceSet();
		}
		else {
			SoLOD lod = new SoTouchLOD2(master);
			lod.center.setValue(getCenter());
			float deltaX = getDeltaX();
			lod.range.setValue(ChunkArray.DEFINITION * deltaX * 85.0f / /*250.0f*/lodFactor);
			SoGroup subChunkGroup = new SoGroup() {
//				public void notify(SoNotList list) {
//					super.notify(list);
//					if( getLevel() > 4) {
//						int ii = 0;
//					}
//				}
			};
			if(culling) {
				//subChunkGroup.renderCulling.setValue(SoSeparator.CacheEnabled.ON);
			}
			
			subChunkGroup.addChild(childs.get(0).getGroup(master, lodFactor,culling));
			subChunkGroup.addChild(childs.get(1).getGroup(master, lodFactor,culling));
			subChunkGroup.addChild(childs.get(2).getGroup(master, lodFactor,culling));
			subChunkGroup.addChild(childs.get(3).getGroup(master, lodFactor,culling));
			lod.addChild(subChunkGroup);
			lod.addChild(getIndexedFaceSet());
			return lod;
		}
	}

	public SoNode getShadowGroup(SoTouchLODMaster master, float lodFactor, boolean culling) {
		
		if(childs.isEmpty()) {
			return getIndexedFaceSet();
		}
		else {
			SoCameraLOD lod = new SoTouchLOD3(master);
			lod.center.setValue(getCenter());
			float deltaX = getDeltaX();
			lod.range.setValue(200 + ChunkArray.DEFINITION * deltaX * 85.0f / /*250.0f*/lodFactor);
			SoGroup subChunkGroup = new SoGroup();
			if(culling) {
				//subChunkGroup.renderCulling.setValue(SoSeparator.CacheEnabled.ON);
			}
			
			subChunkGroup.addChild(childs.get(0).getShadowGroup(master,lodFactor,culling));
			subChunkGroup.addChild(childs.get(1).getShadowGroup(master,lodFactor,culling));
			subChunkGroup.addChild(childs.get(2).getShadowGroup(master,lodFactor,culling));
			subChunkGroup.addChild(childs.get(3).getShadowGroup(master,lodFactor,culling));
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
//		if( node instanceof SoTouchLOD2) {
//			SoTouchLOD2 cnode = (SoTouchLOD2) node;			
//			cnode.setCamera(camera);
//		}
		if( node instanceof SoGroup) {
			SoGroup cnode = (SoGroup) node;			
			for(int i= 0; i<cnode.getNumChildren();i++) {
				setCamera(cnode.getChild(i),camera);
			}
		}
	}

	private SoNode getIndexedFaceSet() {
		
		SoIndexedFaceSet indexedFaceSet = new SoRecursiveIndexedFaceSet(this);
		
//		SoVertexProperty vertexProperty = new SoVertexProperty();
//		vertexProperty.vertex.setValuesPointer(getDecimatedVertices(),getDecimatedVerticesBuffer());
//	    vertexProperty.normalBinding.setValue(SoVertexProperty.Binding.PER_VERTEX_INDEXED);
//	    vertexProperty.normal.setValuesPointer(/*0,*/ getDecimatedNormals(),getDecimatedNormalsBuffer());
//	    vertexProperty.texCoord.setValuesPointer(/*0,*/ getDecimatedTexCoords(),getDecimatedTexCoordsBuffer());
//		
//		indexedFaceSet.vertexProperty.setValue(vertexProperty);
//
//	    indexedFaceSet.coordIndex.setValuesPointer(/*0,*/ getDecimatedCoordIndices());
		
		return indexedFaceSet;
	}
	
	FloatMemoryBuffer getDecimatedTexCoords() {
		
		if(decimatedTextCoords == null) {
			
		int decimatedChunkWidth = getDecimatedChunkWidth();
		int decimatedChunkHeight = getDecimatedChunkHeight();
		FloatMemoryBuffer array = FloatMemoryBuffer.allocateFloatsMalloc(decimatedChunkWidth*decimatedChunkHeight*2);
		for(int i=0;i<decimatedChunkWidth;i++) {
			for(int j=0; j<decimatedChunkHeight; j++) {
				int index = i*decimatedChunkHeight+j;
				float alpha = (i/*+0.5f*/)/(decimatedChunkWidth-1.0f);
				float imin = (float)(i0)/(ca.getW()-1.0f);
				float imax = (float)(i0+ni-1)/(ca.getW()-1.0f);
				float beta = (j/*+0.5f*/)/(decimatedChunkHeight-1.0f);
				float jmin = (float)(j0)/(ca.getH()-1.0f);
				float jmax = (float)(j0+nj-1)/(ca.getH()-1.0f);
				array.setFloat(index*2, imin + alpha*(imax -imin));
				array.setFloat(index*2+1, jmin + beta*(jmax-jmin));
			}
		}
		this.decimatedTextCoords = /*FloatMemoryBuffer.allocateFromFloatArray(*/array/*)*/;
		}
		return decimatedTextCoords;
	}
	
	int decimatedVerticesCount;

	FloatMemoryBuffer getDecimatedVertices() {
		if( decimatedVertices == null) {
			if( true /*|| (decimatedVertices = loadDecimatedVertices()) == null*/) { // load does not accelerate loading
				int decimatedChunkWidth = getDecimatedChunkWidth();
				int decimatedChunkHeight = getDecimatedChunkHeight();
				int nbVertices = decimatedChunkWidth * decimatedChunkHeight;
				decimatedVertices = FloatMemoryBuffer.allocateFloatsMalloc(nbVertices*3);
				FloatBuffer fb = decimatedVertices.toByteBuffer().asFloatBuffer();
				
				float[] xyz = new float[3];
				
				x_min = Float.MAX_VALUE;
				x_max = - Float.MAX_VALUE;
				y_min = Float.MAX_VALUE;
				y_max = - Float.MAX_VALUE;
				
				SbVec3f dummy = new SbVec3f();
				
				for(int i =0 ; i< decimatedChunkWidth; i++) {
					for(int j =0 ; j<decimatedChunkHeight ; j++) {
						int i0 = fromSonToSourceI(i);
						int j0 = fromSonToSourceJ(j);//chunkWidth -1 - ((decimatedChunkWidth -1 -j) << l);
						int indice0 = i0*ca.getH()+j0;
						int indice = i*decimatedChunkHeight+j;
						ca.verticesGet(indice0, xyz);
						fb.put(xyz[0]); //decimatedVertices.setFloat(indice*3, xyz[0]);
						fb.put(xyz[1]); //decimatedVertices.setFloat(indice*3+1, xyz[1]);
						fb.put(xyz[2]); //decimatedVertices.setFloat(indice*3+2, xyz[2]);
						
						x_min = Math.min(xyz[0],x_min);
						x_max = Math.max(xyz[0],x_max);
						y_min = Math.min(xyz[1],y_min);
						y_max = Math.max(xyz[1],y_max);
						
						dummy.setValue(xyz[0],xyz[1],xyz[2]);
						
						sceneBox.extendBy(dummy);
					}
				}
				sceneCenter.setValue(sceneBox.getCenter());
				//saveDecimatedVertices(); // load does not accelerate loading
				
				//System.out.println("loadRC");
			}
		}
		decimatedVerticesCount++;
		return decimatedVertices;
	}
	
	private void saveDecimatedVertices() {
		File file = new File(getFilename("decimatedVertices"));
		try {
			FileOutputStream fos = new FileOutputStream(file);
			byte[] b = new byte[decimatedVertices.numFloats()*Float.BYTES];
			ByteBuffer bb = ByteBuffer.wrap(b);
			bb.asFloatBuffer().put(decimatedVertices.toFloatArray());
			fos.write(b);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private float[] loadDecimatedVertices() {
		
		float[] decimatedVertices = null;
		
		File file = new File(getFilename("decimatedVertices"));
		
		if( !file.exists()) {
			return null;
		}
		if( !file.isFile()) {
			return null;
		}
		if ( file.length() != getDecimatedVerticesFileLength()) {
			return null;
		}
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			
			int nbb = (int)file.length();									
			byte[] buffer = new byte[nbb];
			ByteBuffer bb = ByteBuffer.wrap(buffer);
			FloatBuffer fb = bb.asFloatBuffer();
			fileInputStream.read(buffer);
			decimatedVertices = new float[nbb / Float.BYTES];					
			fb.get(decimatedVertices);
			
			fileInputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}		
		
		x_min = Float.MAX_VALUE;
		x_max = - Float.MAX_VALUE;
		y_min = Float.MAX_VALUE;
		y_max = - Float.MAX_VALUE;
		
		SbVec3f dummy = new SbVec3f();
		
		int decimatedChunkWidth = getDecimatedChunkWidth();
		int decimatedChunkHeight = getDecimatedChunkHeight();
		
		for(int i =0 ; i< decimatedChunkWidth; i++) {
			for(int j =0 ; j<decimatedChunkHeight ; j++) {
				int indice = i*decimatedChunkHeight+j;
				
				x_min = Math.min(decimatedVertices[indice*3],x_min);
				x_max = Math.max(decimatedVertices[indice*3],x_max);
				y_min = Math.min(decimatedVertices[indice*3+1],y_min);
				y_max = Math.max(decimatedVertices[indice*3+1],y_max);
				
				dummy.setValue(decimatedVertices[indice*3],decimatedVertices[indice*3+1],decimatedVertices[indice*3+2]);
				
				sceneBox.extendBy(dummy);
			}
		}
		sceneCenter.setValue(sceneBox.getCenter());
		
		return decimatedVertices;
	}

	private long getDecimatedVerticesFileLength() {
		int decimatedChunkWidth = getDecimatedChunkWidth();
		int decimatedChunkHeight = getDecimatedChunkHeight();
		int nbVertices = decimatedChunkWidth *decimatedChunkHeight;
		int nbFloat = nbVertices*3;
		return nbFloat * Float.BYTES;
	}

//	FloatBuffer getDecimatedVerticesBuffer() {
//		if(decimatedVerticesBuffer == null) {
//			
//			int length = getDecimatedVertices().length;
//			decimatedVerticesBuffer = BufferUtils.createFloatBuffer(length);
//			decimatedVerticesBuffer.clear();
//			decimatedVerticesBuffer.put(getDecimatedVertices(), 0, length);
//			decimatedVerticesBuffer.flip();
//		}
//		return decimatedVerticesBuffer;
//	}
	
//	FloatBuffer getDecimatedNormalsBuffer() {
//		if(decimatedNormalsBuffer == null) {
//			
//			int length = getDecimatedNormals().length;
//			decimatedNormalsBuffer = BufferUtils.createFloatBuffer(length);
//			decimatedNormalsBuffer.clear();
//			decimatedNormalsBuffer.put(getDecimatedNormals(), 0, length);
//			decimatedNormalsBuffer.flip();
//		}
//		return decimatedNormalsBuffer;
//	}
	
//	FloatBuffer getDecimatedTexCoordsBuffer() {
//		if(decimatedTexCoordsBuffer == null) {
//			
//			int length = getDecimatedTexCoords().length;
//			decimatedTexCoordsBuffer = BufferUtils.createFloatBuffer(length);
//			decimatedTexCoordsBuffer.clear();
//			decimatedTexCoordsBuffer.put(getDecimatedTexCoords(), 0, length);
//			decimatedTexCoordsBuffer.flip();
//		}
//		return decimatedTexCoordsBuffer;
//	}
	
	FloatMemoryBuffer getDecimatedNormals() {
		
		if( decimatedNormals == null) {
		if( true /*|| (decimatedNormals = loadDecimatedNormals()) == null*/) { // load does not accelerate loading					
			int sourceChunkWidth = ni;
			int sourceChunkHeight = nj;
			int decimatedChunkWidth = getDecimatedChunkWidth();
			int decimatedChunkHeight = getDecimatedChunkHeight();
			int nbVertices = decimatedChunkWidth *decimatedChunkHeight;
	
			FloatMemoryBuffer decimatedNormals = FloatMemoryBuffer.allocateFloatsMalloc(nbVertices*3);
			
			int nb = 1;
//			for(int i=0;i<l;i++) {
//				nb *= 3;
//			}
			int delta = (nb - 1)/2;
			
			int indice = 0; 
			
			float normalX, normalY, normalZ;
			for(int i =0 ; i< decimatedChunkWidth; i++) {
				for(int j =0 ; j< decimatedChunkHeight; j++) {
					int i0 = fromSonToSourceI(i);
					int j0 = fromSonToSourceJ(j);
					int nbc = 0;
					normalX = 0; normalY = 0; normalZ = 0;
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
							normalX += ca.normalsGet(indice0*3);
							normalY += ca.normalsGet(indice0*3+1);
							normalZ += ca.normalsGet(indice0*3+2);
							nbc++;
						}
					}
					decimatedNormals.setFloat(indice*3, normalX / nbc);
					decimatedNormals.setFloat(indice*3+1, normalY / nbc);
					decimatedNormals.setFloat(indice*3+2, normalZ / nbc);
					indice++;
				}
			}
			this.decimatedNormals = /*FloatMemoryBuffer.allocateFromFloatArray(*/decimatedNormals/*)*/;
			//saveDecimatedNormals(); // load does not accelerate loading
		}
		}
		return decimatedNormals;
	}	
	
	private void saveDecimatedNormals() {
		File file = new File(getFilename("decimatedNormals"));
		try {
			FileOutputStream fos = new FileOutputStream(file);
			byte[] b = new byte[decimatedNormals.numFloats()*Float.BYTES];
			ByteBuffer bb = ByteBuffer.wrap(b);
			bb.asFloatBuffer().put(decimatedNormals.toFloatArray());
			fos.write(b);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private float[] loadDecimatedNormals() {
		
		float[] decimatedNormals = null;
		
		File file = new File(getFilename("decimatedNormals"));
		
		if( !file.exists()) {
			return null;
		}
		if( !file.isFile()) {
			return null;
		}
		if ( file.length() != getDecimatedNormalsFileLength()) {
			return null;
		}
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			
			int nbb = (int)file.length();									
			byte[] buffer = new byte[nbb];
			ByteBuffer bb = ByteBuffer.wrap(buffer);
			FloatBuffer fb = bb.asFloatBuffer();
			fileInputStream.read(buffer);
			decimatedNormals = new float[nbb / Float.BYTES];					
			fb.get(decimatedNormals);
			
			fileInputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
		return decimatedNormals;
	}

	private long getDecimatedNormalsFileLength() {
		int decimatedChunkWidth = getDecimatedChunkWidth();
		int decimatedChunkHeight = getDecimatedChunkHeight();
		int nbVertices = decimatedChunkWidth *decimatedChunkHeight;
		int nbFloat = nbVertices*3;
		return nbFloat * Float.BYTES;
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

	private FloatMemoryBuffer decimatedVertices;
	
	//private FloatBuffer decimatedVerticesBuffer;
	
	private FloatMemoryBuffer decimatedNormals;
	
	//private FloatBuffer decimatedNormalsBuffer;
	
	private FloatMemoryBuffer decimatedTextCoords;
	
	//private FloatBuffer decimatedTexCoordsBuffer;
	
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

//	public SoNode getVertexProperty() {
//		SoVertexProperty vertexProperty = new SoVertexProperty();
//		vertexProperty.vertex.setValuesPointer(getDecimatedVertices()/*,getDecimatedVerticesBuffer()*/);
//	    vertexProperty.normalBinding.setValue(SoVertexProperty.Binding.PER_VERTEX_INDEXED);
//	    vertexProperty.normal.setValuesPointer(/*0,*/ getDecimatedNormals()/*,getDecimatedNormalsBuffer()*/);
//	    vertexProperty.texCoord.setValuesPointer(/*0,*/ getDecimatedTexCoords()/*,getDecimatedTexCoordsBuffer()*/);
//		
//		return vertexProperty;
//	}

	public void prepare() {
		getDecimatedVertices();
		
		if(KEEP_IN_MEMORY) {		
			getDecimatedNormals();//getDecimatedNormalsBuffer();
			getDecimatedTexCoords();//getDecimatedTexCoordsBuffer();
			getDecimatedCoordIndices();
		}
		else {
			decimatedVertices.free();
			decimatedVertices = null;//getDecimatedVerticesBuffer();
		}
		getCenter();
		
		childs.parallelStream().forEach((c)-> c.prepare());
		decimatedVerticesCount = 0;
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
	
	private final SbVec3f dummy = new SbVec3f();
	private final SbVec3f point = new SbVec3f();
	
	public float distance(float x, float y) {
		point.setValue(x,y,sceneCenter.getZ());
		
		if( sceneBox.intersect(point)) {
			return 0;
		}
		
		SbVec3f closestPoint = sceneBox.getClosestExternalPoint(point);
		
		float distance = point.operator_minus(closestPoint,dummy).length();
		
		return distance;
	}

	public void clear() {
		decimatedVerticesCount--;
		if(KEEP_IN_MEMORY) {
			return;
		}
		if(decimatedVerticesCount == 0) {
		FloatMemoryBuffer.free(decimatedVertices); decimatedVertices = null; //decimatedVerticesBuffer = null;		
		FloatMemoryBuffer.free(decimatedNormals); decimatedNormals = null; //decimatedNormalsBuffer = null;
		FloatMemoryBuffer.free(decimatedTextCoords); decimatedTextCoords = null; //decimatedTexCoordsBuffer = null;
		decimatedCoordIndices = null;
		}
	}
}
