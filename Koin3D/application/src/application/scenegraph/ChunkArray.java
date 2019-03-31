/**
 * 
 */
package application.scenegraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jscenegraph.coin3d.inventor.nodes.SoLOD;
import jscenegraph.database.inventor.nodes.SoGroup;
import jscenegraph.database.inventor.nodes.SoNode;

/**
 * @author Yves Boyadjian
 *
 */
public class ChunkArray {
	
	public static final int DEFINITION = 2560;

	private Chunk[][] chunks;
	
	private Map<Chunk,Integer> chunkI = new HashMap<>();
	
	private Map<Chunk,Integer> chunkJ = new HashMap<>();
	
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
				Chunk chunk = new Chunk(Chunk.CHUNK_WIDTH);
				chunks[i][j] = chunk;
				chunkI.put(chunk, i);
				chunkJ.put(chunk, j);
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
	
	public void initIndices() {
		for(int i=0;i<nbChunkWidth;i++) {
			for(int j=0;j<nbChunkHeight;j++) {
				chunks[i][j].initIndices();
			}
		}
	}
	
	public void initIndexedFaceSets() {
		for(int i=0;i<nbChunkWidth;i++) {
			for(int j=0;j<nbChunkHeight;j++) {
				chunks[i][j].initIndexedFaceSet();
			}
		}
	}
	
	public void verticesPut(int index, float value) {
		int vertexIndex = index/3;
		int i = vertexIndex/h;
		int j = vertexIndex - i*h;
		int coord = index - vertexIndex * 3;
				
		Set<Chunk> relevantChunks = getChunks(i,j);
		
		relevantChunks.forEach((c)-> {
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
	
	private Set<Chunk> getChunks(int i, int j) {
		
		Set<Chunk> relevantChunks = new HashSet<>();
		
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

	private boolean isInside(int i0, int j0) {
		return i0>0 && i0<=nbChunkWidth && j0 >0 && j0 <=nbChunkHeight;
	}

	public float verticesGet(int index) {
		int vertexIndex = index/3;
		int i = vertexIndex/h;
		int j = vertexIndex - i*h;
		int coord = index - vertexIndex * 3;
		
		Chunk c = getOneChunk(i,j);
		int chunkIndice = getInChunkIndice(c, i,j);
		
		return c.vertices[chunkIndice*3+coord];
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

	public void colorsPut(int index, int rgba) {
		int vertexIndex = index;
		int i = vertexIndex/h;
		int j = vertexIndex - i*h;
		
		Set<Chunk> relevantChunks = getChunks(i,j);
		
		relevantChunks.forEach((c)-> {
			int chunkIndice = getInChunkIndice(c, i,j);
			c.colors[chunkIndice] = rgba;
		});
	}
	
	public void normalsPut(int index, float value) {
		int vertexIndex = index/3;
		int i = vertexIndex/h;
		int j = vertexIndex - i*h;
		int coord = index - vertexIndex * 3;
		
		Set<Chunk> relevantChunks = getChunks(i,j);
		
		relevantChunks.forEach((c)-> {
			int chunkIndice = getInChunkIndice(c, i,j);
			c.normals[chunkIndice*3+coord] = value;
		});
	}

	public SoNode getGroup() {
		
		float[] distances = new float[Chunk.NB_LOD-1];
		for(int l=0;l<Chunk.NB_LOD-1;l++) {
			int decimatedChunkWidth = ((Chunk.CHUNK_WIDTH -1) >> l) + 1;
			distances[l] = /*Chunk.CHUNK_WIDTH * 1.5f +*/ DEFINITION * Chunk.CHUNK_WIDTH *0.5f/** 1.5f*/ / decimatedChunkWidth;
		}
		SoGroup group = new SoGroup();
		for(int i=0;i<nbChunkWidth;i++) {
			for(int j=0;j<nbChunkHeight;j++) {
				SoLOD lod = new SoLOD();
				lod.center.setValue(chunks[i][j].getCenter());
				lod.range.setValues(0, distances);
				for(int l=0; l< Chunk.NB_LOD;l++)
					lod.addChild(chunks[i][j].getIndexedFaceSet(l));
				group.addChild(lod);
			}
		}
		
		return group;
	}

	public SoNode getShadowGroup() {
		
		SoGroup group = new SoGroup();
		for(int i=0;i<nbChunkWidth;i++) {
			for(int j=0;j<nbChunkHeight;j++) {
				group.addChild(chunks[i][j].getIndexedFaceSet(Math.min(3,Chunk.NB_LOD-1)));
			}
		}
		
		return group;
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
}
