/**
 * 
 */
package application.scenegraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import jscenegraph.coin3d.inventor.nodes.SoLOD;
import jscenegraph.coin3d.inventor.nodes.SoTexture2;
import jscenegraph.coin3d.inventor.nodes.SoVertexProperty;
import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.nodes.SoCallback;
import jscenegraph.database.inventor.nodes.SoGroup;
import jscenegraph.database.inventor.nodes.SoIndexedFaceSet;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.database.inventor.nodes.SoTextureCoordinate2;

/**
 * @author Yves Boyadjian
 *
 */
public class ChunkArray {
	
	public static final int DEFINITION = 2560;

	private Chunk[][] chunks;
	
	private Map<Chunk,Integer> chunkI = new IdentityHashMap<>();
	
	private Map<Chunk,Integer> chunkJ = new IdentityHashMap<>();
	
	private List<Chunk> chunkList = new ArrayList<Chunk>();
	
	private int w;
	private int h;
	
	private int nbChunkWidth;
	private int nbChunkHeight;
	
	public ChunkArray(int w, int h) {
		
		this.w = w;
		this.h = h;
		
		nbChunkHeight = Math.max(1,lowChunkFromIndice(h-1));
		nbChunkWidth = Math.max(1,lowChunkFromIndice(w-1));
		
		chunks = new Chunk[nbChunkWidth][nbChunkHeight];
		for(int i=0;i<nbChunkWidth;i++) {
			for(int j=0;j<nbChunkHeight;j++) {
				String chunkId = "nbw"+nbChunkWidth+"nbh"+nbChunkHeight+"w"+i+"h"+j+"cw"+Chunk.CHUNK_WIDTH;
								
				Chunk chunk = new Chunk(chunkId, Chunk.CHUNK_WIDTH);
				chunks[i][j] = chunk;
				chunkI.put(chunk, i);
				chunkJ.put(chunk, j);
				chunkList.add(chunk);
			}
		}
	}
	
	private int lowChunkFromIndice(int indice) {
		return (indice+Chunk.CHUNK_WIDTH-2)/(Chunk.CHUNK_WIDTH-1);		
	}

	private int highChunkFromIndice(int indice) {
		return (indice+Chunk.CHUNK_WIDTH-1)/(Chunk.CHUNK_WIDTH-1);		
	}

	public void initArrays() {
		for(int i=0;i<nbChunkWidth;i++) {
			for(int j=0;j<nbChunkHeight;j++) {
				chunks[i][j].initArrays();
			}
		}
	}

	/**
	 * true if success
	 * @return
	 */
	public boolean loadZAndColors() {
		for(int i=0;i<nbChunkWidth;i++) {
			for(int j=0;j<nbChunkHeight;j++) {
				if(! chunks[i][j].loadZAndColors()) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean loadNormalsAndStones() {
		for(int i=0;i<nbChunkWidth;i++) {
			for(int j=0;j<nbChunkHeight;j++) {
				if(! chunks[i][j].loadNormalsAndStones()) {
					return false;
				}
			}
		}
		return true;
	}
	
	public void saveZAndColors() {
		for(int i=0;i<nbChunkWidth;i++) {
			for(int j=0;j<nbChunkHeight;j++) {
				chunks[i][j].saveZAndColors();
			}
		}
	}
	
	public void saveZ() {
		for(int i=0;i<nbChunkWidth;i++) {
			for(int j=0;j<nbChunkHeight;j++) {
				chunks[i][j].saveZ();
			}
		}
	}
	
	public void saveColors() {
		for(int i=0;i<nbChunkWidth;i++) {
			for(int j=0;j<nbChunkHeight;j++) {
				chunks[i][j].saveColors();
			}
		}
	}

	public void saveNormalsAndStones() {
		for(int i=0;i<nbChunkWidth;i++) {
			for(int j=0;j<nbChunkHeight;j++) {
				chunks[i][j].saveNormalsAndStones();
			}
		}
	}
	
//	public void initIndices() {
//		for(int i=0;i<nbChunkWidth;i++) {
//			for(int j=0;j<nbChunkHeight;j++) {
//				chunks[i][j].initIndices();
//			}
//		}
//	}
	
	public void initIndexedFaceSets() {
		
		chunkList.parallelStream().forEach((chunk)->chunk.initIndexedFaceSet());
		
//		for(int i=0;i<nbChunkWidth;i++) {
//			for(int j=0;j<nbChunkHeight;j++) {
//				chunks[i][j].initIndexedFaceSet();
//			}
//		}
	}
	
	public void verticesPut(int index, float value) {
		int vertexIndex = index/3;
		int i = vertexIndex/h;
		int j = vertexIndex - i*h;
		int coord = index - vertexIndex * 3;
				
//		Collection<Chunk> relevantChunks = getChunks(i,j);
//		
//		relevantChunks.forEach((c)-> {
//			int chunkIndice = getInChunkIndice(c, i,j);
//			c.vertices[chunkIndice*3+coord] = value;
//		});
		
		chunksForEach(i,j,(c)-> {
			int chunkIndice = getInChunkIndice(c, i,j);
			c.vertices[chunkIndice*3+coord] = value;
		});
	}
	
	private int getInChunkIndice(Chunk c, int i, int j) {
		
		
		int iStartChunk = chunkI.get(c)*(Chunk.CHUNK_WIDTH-1);
		int jStartChunk = chunkJ.get(c)*(Chunk.CHUNK_WIDTH-1);
		
		int iInChunk = i-iStartChunk; 
		int jInChunk = j-jStartChunk; 
		return c.getChunkIndice(iInChunk,jInChunk);
	}
	
	Collection<Chunk> relevantChunks = new ArrayList<>();
	
	private Collection<Chunk> getChunks(int i, int j) {
		
		relevantChunks.clear();
		
		int ic = lowChunkFromIndice(i); int jc = lowChunkFromIndice(j);
		if(isInside(ic,jc)) {
			relevantChunks.add(chunks[ic-1][jc-1]);
		}
		
		ic = lowChunkFromIndice(i); jc = highChunkFromIndice(j);
		if(isInside(ic,jc)) {
			relevantChunks.add(chunks[ic-1][jc-1]);
		}
		
		ic = highChunkFromIndice(i); jc = lowChunkFromIndice(j);
		if(isInside(ic,jc)) {
			relevantChunks.add(chunks[ic-1][jc-1]);
		}
		
		ic = highChunkFromIndice(i); jc = highChunkFromIndice(j);
		if(isInside(ic,jc)) {
			relevantChunks.add(chunks[ic-1][jc-1]);
		}
		
		return relevantChunks;
	}

	private void chunksForEach(int i, int j, Consumer<Chunk> consumer) {
		
		int ic = lowChunkFromIndice(i); int jc = lowChunkFromIndice(j);
		if(isInside(ic,jc)) {
			consumer.accept(chunks[ic-1][jc-1]);
		}
		
		ic = lowChunkFromIndice(i); jc = highChunkFromIndice(j);
		if(isInside(ic,jc)) {
			consumer.accept(chunks[ic-1][jc-1]);
		}
		
		ic = highChunkFromIndice(i); jc = lowChunkFromIndice(j);
		if(isInside(ic,jc)) {
			consumer.accept(chunks[ic-1][jc-1]);
		}
		
		ic = highChunkFromIndice(i); jc = highChunkFromIndice(j);
		if(isInside(ic,jc)) {
			consumer.accept(chunks[ic-1][jc-1]);
		}
	}

	private boolean isInside(int i0, int j0) {
		return i0>0 && i0<=nbChunkWidth && j0 >0 && j0 <=nbChunkHeight;
	}
	
	
	public boolean isStone(int index) {
		
		int vertexIndex = index;
		int i = vertexIndex/h;
		int j = vertexIndex - i*h;
		//int coord = index - vertexIndex * 3;
		
		Chunk c = getOneChunk(i,j);
		int chunkIndice = getInChunkIndice(c, i,j);
		
		boolean stone = c.stone.get(chunkIndice);
		return stone;
	}
	

	public float[] verticesGet(int index, float[] ret) {
		
		if(ret == null) {
			ret = new float[3];
		}
		
		int vertexIndex = index;
		int i = vertexIndex/h;
		int j = vertexIndex - i*h;
		//int coord = index - vertexIndex * 3;
		
		Chunk c = getOneChunk(i,j);
		int chunkIndice = getInChunkIndice(c, i,j);
		
		ret[0] = c.vertices[chunkIndice*3+0];
		ret[1] = c.vertices[chunkIndice*3+1];
		ret[2] = c.vertices[chunkIndice*3+2];
		return ret;
	}
	
	public float verticesGetZ(int index) {
		
		int vertexIndex = index;
		int i = vertexIndex/h;
		int j = vertexIndex - i*h;
		//int coord = index - vertexIndex * 3;
		
		Chunk c = getOneChunk(i,j);
		int chunkIndice = getInChunkIndice(c, i,j);
		
		//ret[0] = c.vertices[chunkIndice*3+0];
		//ret[1] = c.vertices[chunkIndice*3+1];
		float ret_2 = c.vertices[chunkIndice*3+2];
		return ret_2;
	}
	
	public float normalsGet(int index) {
		int vertexIndex = index/3;
		int i = vertexIndex/h;
		int j = vertexIndex - i*h;
		int coord = index - vertexIndex * 3;
		
		Chunk c = getOneChunk(i,j);
		int chunkIndice = getInChunkIndice(c, i,j);
		
		return c.normals[chunkIndice*3+coord];
	}
	
	public byte colorsGet(int index) {
		int vertexIndex = index/4;
		int i = vertexIndex/h;
		int j = vertexIndex - i*h;
		int coord = index - vertexIndex * 4;
		
		Chunk c = getOneChunk(i,j);
		int chunkIndice = getInChunkIndice(c, i,j);
		
		return c.colors[chunkIndice*4+coord];
	}
	
	public int colorsGetRGBA(int vertexIndex) {
		int i = vertexIndex/h;
		int j = vertexIndex - i*h;
		
		Chunk c = getOneChunk(i,j);
		int chunkIndice = getInChunkIndice(c, i,j);
		
		byte red = c.colors[chunkIndice*4];
		byte green = c.colors[chunkIndice*4+1];
		byte blue = c.colors[chunkIndice*4+2];
		byte alpha = c.colors[chunkIndice*4+3];
		
		int RGBA = (Chunk.byteToInt(red) << 24) | (Chunk.byteToInt(green) << 16) | (Chunk.byteToInt(blue) << 8) | Chunk.byteToInt(alpha);
		
		return RGBA;
	}
	
	private Chunk getOneChunk(int i, int j) {
		
		int ic = lowChunkFromIndice(i); int jc = lowChunkFromIndice(j);
		if(isInside(ic,jc)) {
			return chunks[ic-1][jc-1];
		}
		
		ic = lowChunkFromIndice(i); jc = highChunkFromIndice(j);
		if(isInside(ic,jc)) {
			return chunks[ic-1][jc-1];
		}
		
		ic = highChunkFromIndice(i); jc = lowChunkFromIndice(j);
		if(isInside(ic,jc)) {
			return chunks[ic-1][jc-1];
		}
		
		ic = highChunkFromIndice(i); jc = highChunkFromIndice(j);
		if(isInside(ic,jc)) {
			return chunks[ic-1][jc-1];
		}
		
		return null;
	}	
	
	public void colorsPut(int index, int r, int g, int b, int a) {
		int vertexIndex = index;
		int i = vertexIndex/h;
		int j = vertexIndex - i*h;
		
		chunksForEach(i,j,(c)-> {
			int chunkIndice = getInChunkIndice(c, i,j);
			c.colors[chunkIndice*4] = (byte)r;
			c.colors[chunkIndice*4+1] = (byte)g;
			c.colors[chunkIndice*4+2] = (byte)b;
			c.colors[chunkIndice*4+3] = (byte)a;
		});
	}

	public void stonePut(int index) {
		int vertexIndex = index;
		int i = vertexIndex/h;
		int j = vertexIndex - i*h;
		
		chunksForEach(i,j,(c)-> {
			int chunkIndice = getInChunkIndice(c, i,j);
			c.stone.set(chunkIndice);
		});
	}
	
	public void normalsPut(int index, float valueX, float valueY, float valueZ) {
		int vertexIndex = index;
		int i = vertexIndex/h;
		int j = vertexIndex - i*h;
		//int coord = index - vertexIndex * 3;
		
//		Collection<Chunk> relevantChunks = getChunks(i,j);
//		
//		relevantChunks.forEach((c)-> {
//			int chunkIndice = getInChunkIndice(c, i,j);
//			c.normals[chunkIndice*3+0] = valueX;
//			c.normals[chunkIndice*3+1] = valueY;
//			c.normals[chunkIndice*3+2] = valueZ;
//		});
		
		chunksForEach(i,j,(c)-> {
			int chunkIndice = getInChunkIndice(c, i,j);
			c.normals[chunkIndice*3+0] = valueX;
			c.normals[chunkIndice*3+1] = valueY;
			c.normals[chunkIndice*3+2] = valueZ;
		});
	}

//	public SoNode getGroup() {
//		
//		float[] distances = new float[Chunk.NB_LOD-1];
//		for(int l=0;l<Chunk.NB_LOD-1;l++) {
//			int decimatedChunkWidth =  Chunk.getDecimatedChunkWidth(l);//((Chunk.CHUNK_WIDTH -1) >> l) + 1;
//			distances[l] = 0.1f;//Chunk.CHUNK_WIDTH *2.0f + DEFINITION * Chunk.CHUNK_WIDTH * 2.0f / decimatedChunkWidth;
//		}
//		SoGroup group = new SoGroup();
//		//SoTextureCoordinate2 coords = new SoTextureCoordinate2();
//		//group.addChild(coords);
//		
////		SoTexture2 texture = new SoTexture2();
////		texture.filename.setValue("D:/screen.tif");
//		
//		SoTouchLODMaster master = new SoTouchLODMaster();
//		
//		SoCallback cbn = new SoCallback();
//		
//		cbn.setCallback((action)->master.reset());
//		
//		group.addChild(cbn);
//		
//		for(int i=0;i<nbChunkWidth;i++) {
//			for(int j=0;j<nbChunkHeight;j++) {
//				//SoSeparator sep = new SoSeparator();
//				SoGroup sep = new SoGroup();
//				
//				SoLOD lod = new SoTouchLOD(chunks[i][j],master);
//				lod.center.setValue(chunks[i][j].getCenter());
//				lod.range.setValues(0, distances);
//				for(int l=0; l< Chunk.NB_LOD;l++) {
//					lod.addChild(chunks[i][j].getIndexedFaceSet(l));
//				}
//				SoTexture2 texture = new SoTexture2();
//				texture.image.setValue(new SbVec2s((short)Chunk.getDecimatedChunkWidth(0),(short)Chunk.getDecimatedChunkWidth(0)),3, chunks[i][j].getImage(), true);
//				
//				sep.addChild(texture);
//				sep.addChild(lod);
//				group.addChild(sep);
//			}
//		}
//		
//		return group;
//	}

	public SoNode getShadowGroup() {
		
//		SoGroup group = new SoGroup();
//		for(int i=0;i<nbChunkWidth;i++) {
//			for(int j=0;j<nbChunkHeight;j++) {
//				group.addChild(chunks[i][j].getShadowIndexedFaceSet());
//			}
//		}
//		
//		return group;
		
		return getShadowIndexedFaceSet();
	}

	public void initXY(float delta_x, float delta_y) {
		for(int i=0;i<nbChunkWidth;i++) {
			for(int j=0;j<nbChunkHeight;j++) {
				float x0 = i * delta_x * (Chunk.CHUNK_WIDTH-1);
				float y0 = (nbChunkHeight - j -1) * delta_y * (Chunk.CHUNK_WIDTH-1);
				chunks[i][j].initXY(delta_x,delta_y,x0,y0);
			}
		}
		
	}
	
	SbBox3f sceneBox = new SbBox3f();
	SbVec3f sceneCenter = new SbVec3f();
	
	private void computeBBox() {
		
		if(sceneBox.isEmpty()) {
		
			for(int i=0;i<nbChunkWidth;i++) {
				for(int j=0;j<nbChunkHeight;j++) {
					SbBox3f csb = chunks[i][j].sceneBox;
					sceneBox.extendBy(csb);
				}
			}
			sceneCenter.setValue(sceneBox.getCenter());
		
		}
	}

	public SoNode getShadowIndexedFaceSet() {
		SoIndexedFaceSet shadowIndexedFaceSet;
		
		computeBBox();

		shadowIndexedFaceSet = new SoIndexedFaceSet() {
	    	public void computeBBox(SoAction action, SbBox3f box, SbVec3f center) {
	    		box.copyFrom(sceneBox);
	    		center.copyFrom(sceneCenter);
	    	}
	    };
	    
	    final int SHADOW_LOD = 4;//27;
	
	    int shadowWidth = w / SHADOW_LOD;
	    int shadowHeight = h / SHADOW_LOD;
	    int nbCoordIndices = (shadowWidth-1)*(shadowHeight-1)*5;
	    int[] decimatedCoordIndices = new int[nbCoordIndices];
		int indice=0;
		for(int i=1;i<shadowWidth;i++) {
		for(int j=1; j<shadowHeight;j++) {
			decimatedCoordIndices[indice++] = (i-1)*shadowHeight+(j-1); //1
			decimatedCoordIndices[indice++] = (i)*shadowHeight+(j-1); //2
			decimatedCoordIndices[indice++] = (i)*shadowHeight+(j); //3
			decimatedCoordIndices[indice++] = (i-1)*shadowHeight+(j); //4
			decimatedCoordIndices[indice++] = -1; 
		}
		}
	    
	    
	    shadowIndexedFaceSet.coordIndex.setValuesPointer(/*0,*/ decimatedCoordIndices);
	    
		SoVertexProperty vertexProperty = new SoVertexProperty();
		
		int nbVertices = shadowWidth *shadowHeight;
		float[] decimatedVertices = new float[nbVertices*3];

		float[] xyz = new float[3];
		
		indice = 0;
		for(int i =0 ; i< shadowWidth; i++) {
			for(int j =0 ; j<shadowHeight ; j++) {
				int i0 = i*SHADOW_LOD;
				int j0 = j*SHADOW_LOD;
				int index = i0 * h + j0;
				verticesGet(index,xyz);
				decimatedVertices[indice*3] = xyz[0];
				decimatedVertices[indice*3+1] = xyz[1];
				decimatedVertices[indice*3+2] = xyz[2];
				indice++;
			}
		}
		vertexProperty.vertex.setValuesPointer(decimatedVertices);
		
		shadowIndexedFaceSet.vertexProperty.setValue(vertexProperty);
		    	    
	    return shadowIndexedFaceSet;
		
	}
	
	public RecursiveChunk getRecursiveChunk() {
		RecursiveChunk rc = new RecursiveChunk(this,null,1,0,0,w,h);
		
		rc.prepare();
		
		return rc;
	}

	public int getW() {
		return w;
	}

	public int getH() {
		return h;
	}

	public OverallTexture getOverallTexture() {
		OverallTexture ot  = new OverallTexture(this);
		return ot;
	}

	public SbBox3f getSceneBox() {
		
		computeBBox();

		return sceneBox;
	}
}
