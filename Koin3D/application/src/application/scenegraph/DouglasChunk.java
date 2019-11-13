package application.scenegraph;

import java.util.ArrayList;
import java.util.List;

import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbColor;

public class DouglasChunk {
	
	private static final SbColor TRUNK_COLOR = new SbColor(60.0f/255,42.0f/255,0.0f/255);
	
	private static final SbColor TREE_FOLIAGE = new SbColor(22.0f/255,42.0f/255,0.0f/255);
	
	private static final SbColor GREEN = new SbColor(5.0f/255,52.0f/255,0.0f/255);
	
	DouglasForest df;

	SbBox3f boundingBox = new SbBox3f();
	
	List<Integer> insideTrees = new ArrayList<>();
	
	public DouglasChunk(DouglasForest df, SbBox3f bb) {
		this.df = df;
		boundingBox.copyFrom(bb);
	}

	public void addTree(int i) {
		insideTrees.add(i);
	}
	
	public int getNbTrees() {
		return insideTrees.size();
	}
	
	public float getX(int i) {
		return df.xArray[insideTrees.get(i)];
	}
	
	public float getY(int i) {
		return df.yArray[insideTrees.get(i)];
	}
	
	public float getZ(int i) {
		return df.zArray[insideTrees.get(i)];
	}
	
	public float getHeight(int i) {
		return df.heightArray[insideTrees.get(i)];
	}
	
	public float getAngleDegree1(int i) {
		return df.angleDegree1[insideTrees.get(i)];
	}
	
	public float getRandomTopTree(int i) {
		return df.randomTopTree[insideTrees.get(i)];
	}
	
	public float getRandomBottomTree(int i) {
		return df.randomBottomTree[insideTrees.get(i)];
	}

	int[] douglasIndicesF;
	float[] douglasVerticesF;
	float[] douglasNormalsF;
	int[] douglasColorsF;
	float[] douglasTexCoordsF;
	
	int[] douglasIndicesT;
	float[] douglasVerticesT;
	float[] douglasNormalsT;
	int[] douglasColorsT;
	
	/**
	 * Compute for foliage
	 */
	public void computeDouglasF() {
		
		int nbDouglas = getNbTrees();
		
		int NB_INDICES_PER_TRIANGLE = 4;
		
		int NB_INDICES_PER_QUAD = 5;
		
		int NB_TRIANGLES_PER_TREE_FOLIAGE = 2;//5;
		
		int NB_QUAD_PER_TREE_FOLIAGE = 3;
		
		int NB_VERTEX_PER_TREE_FOLIAGE = 6;//10;
		
		int NB_INDICES_PER_TREE = NB_INDICES_PER_TRIANGLE * NB_TRIANGLES_PER_TREE_FOLIAGE + NB_INDICES_PER_QUAD * NB_QUAD_PER_TREE_FOLIAGE;
		
		int nbIndices = NB_INDICES_PER_TREE * nbDouglas;
		
		douglasIndicesF = new int[nbIndices];
		
		int nbVertices = NB_VERTEX_PER_TREE_FOLIAGE * nbDouglas;
		
		douglasVerticesF = new float[nbVertices * 3];
		
		douglasNormalsF = new float[nbVertices * 3];
		
		douglasColorsF = new int[nbVertices];
		
		douglasTexCoordsF = new float[nbVertices * 2];
		
		for( int tree = 0; tree< nbDouglas; tree++) {
			
			float height = getHeight(tree);
			
			int vertex = tree * NB_VERTEX_PER_TREE_FOLIAGE;
			
//			douglasColors[vertex] = TRUNK_COLOR.getPackedValue();//TOP
//			douglasColors[vertex+1] = TRUNK_COLOR.getPackedValue();//TR1
//			douglasColors[vertex+2] = TRUNK_COLOR.getPackedValue();//TR2
//			douglasColors[vertex+3] = TRUNK_COLOR.getPackedValue();//TR3
			douglasColorsF[vertex] = TREE_FOLIAGE.getPackedValue();//TFOL1
			douglasColorsF[vertex+1] = TREE_FOLIAGE.getPackedValue();//TFOL2
			douglasColorsF[vertex+2] = TREE_FOLIAGE.getPackedValue();//TFOL3
			douglasColorsF[vertex+3] = TREE_FOLIAGE.getPackedValue();//BFOL1
			douglasColorsF[vertex+1+3] = TREE_FOLIAGE.getPackedValue();//BFOL2
			douglasColorsF[vertex+2+3] = TREE_FOLIAGE.getPackedValue();//BFOL3
			
			int vertexCoordIndice = vertex * 3;
			int texCoordIndice = vertex * 2;
			
			// top of tree
//			douglasVertices[vertexCoordIndice] = getX(tree);
//			douglasVertices[vertexCoordIndice+1] = getY(tree);
//			douglasVertices[vertexCoordIndice+2] = getZ(tree) + height;
//			
//			douglasNormals[vertexCoordIndice] = 0;
//			douglasNormals[vertexCoordIndice+1] = 0;
//			douglasNormals[vertexCoordIndice+2] = 1;
			
			float width = height * 0.707f / 50.0f;
			
			float angleDegree1 = getAngleDegree1(tree);
			float angleDegree2 = angleDegree1 + 120.0f;
			float angleDegree3 = angleDegree2 + 120.0f;
			float angleRadian1 = angleDegree1 * (float)Math.PI / 180.0f;
			float angleRadian2 = angleDegree2 * (float)Math.PI / 180.0f;
			float angleRadian3 = angleDegree3 * (float)Math.PI / 180.0f;
			
			//trunk
//			vertexCoordIndice += 3;
			
//			douglasVertices[vertexCoordIndice] = getX(tree) + width * (float)Math.cos(angleRadian1);
//			douglasVertices[vertexCoordIndice+1] = getY(tree) + width * (float)Math.sin(angleRadian1);
//			douglasVertices[vertexCoordIndice+2] = getZ(tree) - 1;
//			
//			douglasNormals[vertexCoordIndice] = (float)Math.cos(angleRadian1);
//			douglasNormals[vertexCoordIndice+1] = (float)Math.sin(angleRadian1);
//			douglasNormals[vertexCoordIndice+2] = 0;
//			
//			vertexCoordIndice += 3;
			
//			douglasVertices[vertexCoordIndice] = getX(tree) + width * (float)Math.cos(angleRadian2);
//			douglasVertices[vertexCoordIndice+1] = getY(tree) + width * (float)Math.sin(angleRadian2);
//			douglasVertices[vertexCoordIndice+2] = getZ(tree) - 1;
//			
//			douglasNormals[vertexCoordIndice] =  (float)Math.cos(angleRadian2);
//			douglasNormals[vertexCoordIndice+1] = (float)Math.sin(angleRadian2);
//			douglasNormals[vertexCoordIndice+2] = 0;
//			
//			vertexCoordIndice += 3;
//			
//			douglasVertices[vertexCoordIndice] = getX(tree) + width * (float)Math.cos(angleRadian3);
//			douglasVertices[vertexCoordIndice+1] = getY(tree) + width * (float)Math.sin(angleRadian3);
//			douglasVertices[vertexCoordIndice+2] = getZ(tree) - 1;
//			
//			douglasNormals[vertexCoordIndice] = (float)Math.cos(angleRadian3);
//			douglasNormals[vertexCoordIndice+1] = (float)Math.sin(angleRadian3);
//			douglasNormals[vertexCoordIndice+2] = 0;
			// end of trunk
			
//			vertexCoordIndice += 3;
			
			float widthTop = getRandomTopTree(tree);//width *2.5f * forest.randomTopTrees.nextFloat();
			
			// top of tree foliage
			douglasVerticesF[vertexCoordIndice] = getX(tree)+ widthTop * (float)Math.cos(angleRadian1);
			douglasVerticesF[vertexCoordIndice+1] = getY(tree)+ widthTop * (float)Math.sin(angleRadian1);
			douglasVerticesF[vertexCoordIndice+2] = getZ(tree) + height;
			
			douglasNormalsF[vertexCoordIndice] = (float)Math.cos(angleRadian1);
			douglasNormalsF[vertexCoordIndice+1] = (float)Math.sin(angleRadian1);
			douglasNormalsF[vertexCoordIndice+2] = 0.2f;
			
			douglasTexCoordsF[texCoordIndice] = 0.0f;
			douglasTexCoordsF[texCoordIndice+1] = 70.0f;
			
			vertexCoordIndice += 3;
			texCoordIndice += 2;
			
			douglasVerticesF[vertexCoordIndice] = getX(tree)+ widthTop * (float)Math.cos(angleRadian2);
			douglasVerticesF[vertexCoordIndice+1] = getY(tree)+ widthTop * (float)Math.sin(angleRadian2);
			douglasVerticesF[vertexCoordIndice+2] = getZ(tree) + height;
			
			douglasNormalsF[vertexCoordIndice] = (float)Math.cos(angleRadian2);
			douglasNormalsF[vertexCoordIndice+1] = (float)Math.sin(angleRadian2);
			douglasNormalsF[vertexCoordIndice+2] = 0.2f;
			
			douglasTexCoordsF[texCoordIndice] = 1.0f;
			douglasTexCoordsF[texCoordIndice+1] = 70.0f;
			
			vertexCoordIndice += 3;
			texCoordIndice += 2;
			
			douglasVerticesF[vertexCoordIndice] = getX(tree)+ widthTop * (float)Math.cos(angleRadian3);
			douglasVerticesF[vertexCoordIndice+1] = getY(tree)+ widthTop * (float)Math.sin(angleRadian3);
			douglasVerticesF[vertexCoordIndice+2] = getZ(tree) + height;
			
			douglasNormalsF[vertexCoordIndice] = (float)Math.cos(angleRadian3);
			douglasNormalsF[vertexCoordIndice+1] = (float)Math.sin(angleRadian3);
			douglasNormalsF[vertexCoordIndice+2] = 0.2f;
			
			douglasTexCoordsF[texCoordIndice] = 2.0f;
			douglasTexCoordsF[texCoordIndice+1] = 70.0f;
			
			//foliage
			vertexCoordIndice += 3;
			texCoordIndice += 2;

			
			
			float foliageWidth = getRandomBottomTree(tree);//(height+ forest.randomBottomTrees.nextFloat()*12.0f) * 0.1f;
			
			
			
			douglasVerticesF[vertexCoordIndice] = getX(tree) + foliageWidth * (float)Math.cos(angleRadian1);
			douglasVerticesF[vertexCoordIndice+1] = getY(tree) + foliageWidth * (float)Math.sin(angleRadian1);

			float z = df.sg.getInternalZ(douglasVerticesF[vertexCoordIndice],douglasVerticesF[vertexCoordIndice+1],0.0f) + df.sg.getzTranslation();
			
			douglasVerticesF[vertexCoordIndice+2] = Math.max(getZ(tree) + 2.5f,z+0.2f);
			
			douglasNormalsF[vertexCoordIndice] =  (float)Math.cos(angleRadian1);
			douglasNormalsF[vertexCoordIndice+1] =  (float)Math.sin(angleRadian1);
			douglasNormalsF[vertexCoordIndice+2] = 0;
			
			douglasTexCoordsF[texCoordIndice] = 0.0f;
			douglasTexCoordsF[texCoordIndice+1] = 0.0f;
			
			vertexCoordIndice += 3;
			texCoordIndice += 2;
			
			douglasVerticesF[vertexCoordIndice] = getX(tree) + foliageWidth * (float)Math.cos(angleRadian2);
			douglasVerticesF[vertexCoordIndice+1] = getY(tree) + foliageWidth * (float)Math.sin(angleRadian2);
			
			z = df.sg.getInternalZ(douglasVerticesF[vertexCoordIndice],douglasVerticesF[vertexCoordIndice+1],0.0f) + df.sg.getzTranslation();
			
			douglasVerticesF[vertexCoordIndice+2] = Math.max(getZ(tree) + 2.5f,z+0.2f);
			
			douglasNormalsF[vertexCoordIndice] =   (float)Math.cos(angleRadian2);
			douglasNormalsF[vertexCoordIndice+1] =  (float)Math.sin(angleRadian2);
			douglasNormalsF[vertexCoordIndice+2] = 0;
			
			douglasTexCoordsF[texCoordIndice] = 10.0f;
			douglasTexCoordsF[texCoordIndice+1] = 0.0f;
			
			vertexCoordIndice += 3;
			texCoordIndice += 2;
			
			douglasVerticesF[vertexCoordIndice] = getX(tree) + foliageWidth * (float)Math.cos(angleRadian3);
			douglasVerticesF[vertexCoordIndice+1] = getY(tree) + foliageWidth * (float)Math.sin(angleRadian3);
			
			z = df.sg.getInternalZ(douglasVerticesF[vertexCoordIndice],douglasVerticesF[vertexCoordIndice+1],0.0f) + df.sg.getzTranslation();
			
			douglasVerticesF[vertexCoordIndice+2] = Math.max(getZ(tree) + 2.5f,z+0.2f);
			
			douglasNormalsF[vertexCoordIndice] = (float)Math.cos(angleRadian3);
			douglasNormalsF[vertexCoordIndice+1] = (float)Math.sin(angleRadian3);
			douglasNormalsF[vertexCoordIndice+2] = 0;
			
			douglasTexCoordsF[texCoordIndice] = 20.0f;
			douglasTexCoordsF[texCoordIndice+1] = 0.0f;
			
			// end of foliage
			
			int i = tree * NB_INDICES_PER_TREE;
			
//			// trunk side
//			douglasIndices[i] = vertex;
//			douglasIndices[i+1] = vertex+1;
//			douglasIndices[i+2] = vertex+2;
//			douglasIndices[i+3] = -1;
//			
//			i+= NB_INDICES_PER_TRIANGLE;
//
////			indices[i] = vertex+1;
////			indices[i+1] = vertex+2;
////			indices[i+2] = vertex+3;
////			indices[i+3] = -1;
////
////			i+= NB_INDICES_PER_TRIANGLE;
//
//			// trunk side
//			douglasIndices[i] = vertex+0;
//			douglasIndices[i+1] = vertex+2;
//			douglasIndices[i+2] = vertex+3;
//			douglasIndices[i+3] = -1;
//
//			i+= NB_INDICES_PER_TRIANGLE;
//
//			// trunk side
//			douglasIndices[i] = vertex+0;
//			douglasIndices[i+1] = vertex+3;
//			douglasIndices[i+2] = vertex+1;
//			douglasIndices[i+3] = -1;
//			
//			i+= NB_INDICES_PER_TRIANGLE;

			//foliage side
			douglasIndicesF[i] = vertex+1+3-4;
			douglasIndicesF[i+1] = vertex+1+3+3-4;
			douglasIndicesF[i+2] = vertex+2+3+3-4;
			douglasIndicesF[i+3] = vertex+2+3-4;
			douglasIndicesF[i+4] = -1;
			
			i+= /*NB_INDICES_PER_TRIANGLE*/NB_INDICES_PER_QUAD;

			// foliage side 
			douglasIndicesF[i] = vertex+2+3-4;
			douglasIndicesF[i+1] = vertex+2+3+3-4;
			douglasIndicesF[i+2] = vertex+3+3+3-4;
			douglasIndicesF[i+3] = vertex+3+3-4;
			douglasIndicesF[i+4] = -1;

			i+= /*NB_INDICES_PER_TRIANGLE*/NB_INDICES_PER_QUAD;

			// foliage side
			douglasIndicesF[i] = vertex+3+3-4;
			douglasIndicesF[i+1] = vertex+3+3+3-4;
			douglasIndicesF[i+2] = vertex+1+3+3-4;
			douglasIndicesF[i+3] = vertex+1+3-4;
			douglasIndicesF[i+4] = -1;
			
			i+= /*NB_INDICES_PER_TRIANGLE*/NB_INDICES_PER_QUAD;

			// foliage bottom
			douglasIndicesF[i] = vertex+3+3+3-4;
			douglasIndicesF[i+1] = vertex+2+3+3-4;
			douglasIndicesF[i+2] = vertex+1+3+3-4;
			douglasIndicesF[i+3] = -1;
			
			i+= /*NB_INDICES_PER_TRIANGLE*/NB_INDICES_PER_TRIANGLE;

			// foliage top
			douglasIndicesF[i] = vertex+1+3-4;
			douglasIndicesF[i+1] = vertex+2+3-4;
			douglasIndicesF[i+2] = vertex+3+3-4;
			douglasIndicesF[i+3] = -1;
			
		}
	}
	public void computeDouglasT() {
		
		int nbDouglas = getNbTrees();
		
		int NB_INDICES_PER_TRIANGLE = 4;
		
		int NB_INDICES_PER_QUAD = 5;
		
		int NB_TRIANGLES_PER_TREE_TRUNK = 3;//5;
		
		int NB_QUAD_PER_TREE_TRUNK = 0;//3;
		
		int NB_VERTEX_PER_TREE_TRUNK = 4;//10;
		
		int NB_INDICES_PER_TREE = NB_INDICES_PER_TRIANGLE * NB_TRIANGLES_PER_TREE_TRUNK + NB_INDICES_PER_QUAD * NB_QUAD_PER_TREE_TRUNK;
		
		int nbIndices = NB_INDICES_PER_TREE * nbDouglas;
		
		douglasIndicesT = new int[nbIndices];
		
		int nbVertices = NB_VERTEX_PER_TREE_TRUNK * nbDouglas;
		
		douglasVerticesT = new float[nbVertices * 3];
		
		douglasNormalsT = new float[nbVertices * 3];
		
		douglasColorsT = new int[nbVertices];
		
		for( int tree = 0; tree< nbDouglas; tree++) {
			
			float height = getHeight(tree);
			
			int vertex = tree * NB_VERTEX_PER_TREE_TRUNK;
			
			douglasColorsT[vertex] = TRUNK_COLOR.getPackedValue();//TOP
			douglasColorsT[vertex+1] = TRUNK_COLOR.getPackedValue();//TR1
			douglasColorsT[vertex+2] = TRUNK_COLOR.getPackedValue();//TR2
			douglasColorsT[vertex+3] = TRUNK_COLOR.getPackedValue();//TR3
//			douglasColors[vertex+1+3] = TREE_FOLIAGE.getPackedValue();//TFOL1
//			douglasColors[vertex+2+3] = TREE_FOLIAGE.getPackedValue();//TFOL2
//			douglasColors[vertex+3+3] = TREE_FOLIAGE.getPackedValue();//TFOL3
//			douglasColors[vertex+1+3+3] = TREE_FOLIAGE.getPackedValue();//BFOL1
//			douglasColors[vertex+2+3+3] = TREE_FOLIAGE.getPackedValue();//BFOL2
//			douglasColors[vertex+3+3+3] = TREE_FOLIAGE.getPackedValue();//BFOL3
			
			int vertexCoordIndice = vertex * 3;
			
			// top of tree
			douglasVerticesT[vertexCoordIndice] = getX(tree);
			douglasVerticesT[vertexCoordIndice+1] = getY(tree);
			douglasVerticesT[vertexCoordIndice+2] = getZ(tree) + height;
			
			douglasNormalsT[vertexCoordIndice] = 0;
			douglasNormalsT[vertexCoordIndice+1] = 0;
			douglasNormalsT[vertexCoordIndice+2] = 1;
			
			float width = height * 0.707f / 50.0f;
			
			float angleDegree1 = getAngleDegree1(tree);
			float angleDegree2 = angleDegree1 + 120.0f;
			float angleDegree3 = angleDegree2 + 120.0f;
			float angleRadian1 = angleDegree1 * (float)Math.PI / 180.0f;
			float angleRadian2 = angleDegree2 * (float)Math.PI / 180.0f;
			float angleRadian3 = angleDegree3 * (float)Math.PI / 180.0f;
			
			//trunk
			vertexCoordIndice += 3;
			
			douglasVerticesT[vertexCoordIndice] = getX(tree) + width * (float)Math.cos(angleRadian1);
			douglasVerticesT[vertexCoordIndice+1] = getY(tree) + width * (float)Math.sin(angleRadian1);
			douglasVerticesT[vertexCoordIndice+2] = getZ(tree) - 1;
			
			douglasNormalsT[vertexCoordIndice] = (float)Math.cos(angleRadian1);
			douglasNormalsT[vertexCoordIndice+1] = (float)Math.sin(angleRadian1);
			douglasNormalsT[vertexCoordIndice+2] = 0;
			
			vertexCoordIndice += 3;
			
			douglasVerticesT[vertexCoordIndice] = getX(tree) + width * (float)Math.cos(angleRadian2);
			douglasVerticesT[vertexCoordIndice+1] = getY(tree) + width * (float)Math.sin(angleRadian2);
			douglasVerticesT[vertexCoordIndice+2] = getZ(tree) - 1;
			
			douglasNormalsT[vertexCoordIndice] =  (float)Math.cos(angleRadian2);
			douglasNormalsT[vertexCoordIndice+1] = (float)Math.sin(angleRadian2);
			douglasNormalsT[vertexCoordIndice+2] = 0;
			
			vertexCoordIndice += 3;
			
			douglasVerticesT[vertexCoordIndice] = getX(tree) + width * (float)Math.cos(angleRadian3);
			douglasVerticesT[vertexCoordIndice+1] = getY(tree) + width * (float)Math.sin(angleRadian3);
			douglasVerticesT[vertexCoordIndice+2] = getZ(tree) - 1;
			
			douglasNormalsT[vertexCoordIndice] = (float)Math.cos(angleRadian3);
			douglasNormalsT[vertexCoordIndice+1] = (float)Math.sin(angleRadian3);
			douglasNormalsT[vertexCoordIndice+2] = 0;
			// end of trunk
			
			vertexCoordIndice += 3;
			
			float widthTop = getRandomTopTree(tree);//width *2.5f * forest.randomTopTrees.nextFloat();
			
//			// top of tree foliage
//			douglasVertices[vertexCoordIndice] = getX(tree)+ widthTop * (float)Math.cos(angleRadian1);
//			douglasVertices[vertexCoordIndice+1] = getY(tree)+ widthTop * (float)Math.sin(angleRadian1);
//			douglasVertices[vertexCoordIndice+2] = getZ(tree) + height;
//			
//			douglasNormals[vertexCoordIndice] = (float)Math.cos(angleRadian1);
//			douglasNormals[vertexCoordIndice+1] = (float)Math.sin(angleRadian1);
//			douglasNormals[vertexCoordIndice+2] = 0.2f;
//			
//			vertexCoordIndice += 3;
//			
//			douglasVertices[vertexCoordIndice] = getX(tree)+ widthTop * (float)Math.cos(angleRadian2);
//			douglasVertices[vertexCoordIndice+1] = getY(tree)+ widthTop * (float)Math.sin(angleRadian2);
//			douglasVertices[vertexCoordIndice+2] = getZ(tree) + height;
//			
//			douglasNormals[vertexCoordIndice] = (float)Math.cos(angleRadian2);
//			douglasNormals[vertexCoordIndice+1] = (float)Math.sin(angleRadian2);
//			douglasNormals[vertexCoordIndice+2] = 0.2f;
//			
//			vertexCoordIndice += 3;
//			
//			douglasVertices[vertexCoordIndice] = getX(tree)+ widthTop * (float)Math.cos(angleRadian3);
//			douglasVertices[vertexCoordIndice+1] = getY(tree)+ widthTop * (float)Math.sin(angleRadian3);
//			douglasVertices[vertexCoordIndice+2] = getZ(tree) + height;
//			
//			douglasNormals[vertexCoordIndice] = (float)Math.cos(angleRadian3);
//			douglasNormals[vertexCoordIndice+1] = (float)Math.sin(angleRadian3);
//			douglasNormals[vertexCoordIndice+2] = 0.2f;
//			
//			//foliage
//			vertexCoordIndice += 3;
//
//			
//			
//			float foliageWidth = getRandomBottomTree(tree);//(height+ forest.randomBottomTrees.nextFloat()*12.0f) * 0.1f;
//			
//			
//			
//			douglasVertices[vertexCoordIndice] = getX(tree) + foliageWidth * (float)Math.cos(angleRadian1);
//			douglasVertices[vertexCoordIndice+1] = getY(tree) + foliageWidth * (float)Math.sin(angleRadian1);
//
//			float z = df.sg.getInternalZ(douglasVertices[vertexCoordIndice],douglasVertices[vertexCoordIndice+1],0.0f) + df.sg.getzTranslation();
//			
//			douglasVertices[vertexCoordIndice+2] = Math.max(getZ(tree) + 2.5f,z+0.2f);
//			
//			douglasNormals[vertexCoordIndice] =  (float)Math.cos(angleRadian1);
//			douglasNormals[vertexCoordIndice+1] =  (float)Math.sin(angleRadian1);
//			douglasNormals[vertexCoordIndice+2] = 0;
//			
//			vertexCoordIndice += 3;
//			
//			douglasVertices[vertexCoordIndice] = getX(tree) + foliageWidth * (float)Math.cos(angleRadian2);
//			douglasVertices[vertexCoordIndice+1] = getY(tree) + foliageWidth * (float)Math.sin(angleRadian2);
//			
//			z = df.sg.getInternalZ(douglasVertices[vertexCoordIndice],douglasVertices[vertexCoordIndice+1],0.0f) + df.sg.getzTranslation();
//			
//			douglasVertices[vertexCoordIndice+2] = Math.max(getZ(tree) + 2.5f,z+0.2f);
//			
//			douglasNormals[vertexCoordIndice] =   (float)Math.cos(angleRadian2);
//			douglasNormals[vertexCoordIndice+1] =  (float)Math.sin(angleRadian2);
//			douglasNormals[vertexCoordIndice+2] = 0;
//			
//			vertexCoordIndice += 3;
//			
//			douglasVertices[vertexCoordIndice] = getX(tree) + foliageWidth * (float)Math.cos(angleRadian3);
//			douglasVertices[vertexCoordIndice+1] = getY(tree) + foliageWidth * (float)Math.sin(angleRadian3);
//			
//			z = df.sg.getInternalZ(douglasVertices[vertexCoordIndice],douglasVertices[vertexCoordIndice+1],0.0f) + df.sg.getzTranslation();
//			
//			douglasVertices[vertexCoordIndice+2] = Math.max(getZ(tree) + 2.5f,z+0.2f);
//			
//			douglasNormals[vertexCoordIndice] = (float)Math.cos(angleRadian3);
//			douglasNormals[vertexCoordIndice+1] = (float)Math.sin(angleRadian3);
//			douglasNormals[vertexCoordIndice+2] = 0;
//			// end of foliage
			
			int i = tree * NB_INDICES_PER_TREE;
			
			// trunk side
			douglasIndicesT[i] = vertex;
			douglasIndicesT[i+1] = vertex+1;
			douglasIndicesT[i+2] = vertex+2;
			douglasIndicesT[i+3] = -1;
			
			i+= NB_INDICES_PER_TRIANGLE;

//			indices[i] = vertex+1;
//			indices[i+1] = vertex+2;
//			indices[i+2] = vertex+3;
//			indices[i+3] = -1;
//
//			i+= NB_INDICES_PER_TRIANGLE;

			// trunk side
			douglasIndicesT[i] = vertex+0;
			douglasIndicesT[i+1] = vertex+2;
			douglasIndicesT[i+2] = vertex+3;
			douglasIndicesT[i+3] = -1;

			i+= NB_INDICES_PER_TRIANGLE;

			// trunk side
			douglasIndicesT[i] = vertex+0;
			douglasIndicesT[i+1] = vertex+3;
			douglasIndicesT[i+2] = vertex+1;
			douglasIndicesT[i+3] = -1;
			
			i+= NB_INDICES_PER_TRIANGLE;

//			//foliage side
//			douglasIndices[i] = vertex+1+3;
//			douglasIndices[i+1] = vertex+1+3+3;
//			douglasIndices[i+2] = vertex+2+3+3;
//			douglasIndices[i+3] = vertex+2+3;
//			douglasIndices[i+4] = -1;
//			
//			i+= /*NB_INDICES_PER_TRIANGLE*/NB_INDICES_PER_QUAD;
//
//			// foliage side 
//			douglasIndices[i] = vertex+2+3;
//			douglasIndices[i+1] = vertex+2+3+3;
//			douglasIndices[i+2] = vertex+3+3+3;
//			douglasIndices[i+3] = vertex+3+3;
//			douglasIndices[i+4] = -1;
//
//			i+= /*NB_INDICES_PER_TRIANGLE*/NB_INDICES_PER_QUAD;
//
//			// foliage side
//			douglasIndices[i] = vertex+3+3;
//			douglasIndices[i+1] = vertex+3+3+3;
//			douglasIndices[i+2] = vertex+1+3+3;
//			douglasIndices[i+3] = vertex+1+3;
//			douglasIndices[i+4] = -1;
//			
//			i+= /*NB_INDICES_PER_TRIANGLE*/NB_INDICES_PER_QUAD;
//
//			// foliage bottom
//			douglasIndices[i] = vertex+3+3+3;
//			douglasIndices[i+1] = vertex+2+3+3;
//			douglasIndices[i+2] = vertex+1+3+3;
//			douglasIndices[i+3] = -1;
//			
//			i+= /*NB_INDICES_PER_TRIANGLE*/NB_INDICES_PER_TRIANGLE;
//
//			// foliage top
//			douglasIndices[i] = vertex+1+3;
//			douglasIndices[i+1] = vertex+2+3;
//			douglasIndices[i+2] = vertex+3+3;
//			douglasIndices[i+3] = -1;
			
		}
	}

	public void computeDouglas() {
		computeDouglasT();
		computeDouglasF();
	}
}
