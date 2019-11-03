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

	int[] douglasIndices;
	float[] douglasVertices;
	float[] douglasNormals;
	int[] douglasColors;
	
	public void computeDouglas() {
		
		int nbDouglas = getNbTrees();
		
		int NB_INDICES_PER_TRIANGLE = 4;
		
		int NB_INDICES_PER_QUAD = 5;
		
		int NB_TRIANGLES_PER_TREE = 5;
		
		int NB_QUAD_PER_TREE = 3;
		
		int NB_VERTEX_PER_TREE = 10;
		
		int NB_INDICES_PER_TREE = NB_INDICES_PER_TRIANGLE * NB_TRIANGLES_PER_TREE + NB_INDICES_PER_QUAD * NB_QUAD_PER_TREE;
		
		int nbIndices = NB_INDICES_PER_TREE * nbDouglas;
		
		douglasIndices = new int[nbIndices];
		
		int nbVertices = NB_VERTEX_PER_TREE * nbDouglas;
		
		douglasVertices = new float[nbVertices * 3];
		
		douglasNormals = new float[nbVertices * 3];
		
		douglasColors = new int[nbVertices];
		
		for( int tree = 0; tree< nbDouglas; tree++) {
			
			float height = getHeight(tree);
			
			int vertex = tree * NB_VERTEX_PER_TREE;
			
			douglasColors[vertex] = TRUNK_COLOR.getPackedValue();//TOP
			douglasColors[vertex+1] = TRUNK_COLOR.getPackedValue();//TR1
			douglasColors[vertex+2] = TRUNK_COLOR.getPackedValue();//TR2
			douglasColors[vertex+3] = TRUNK_COLOR.getPackedValue();//TR3
			douglasColors[vertex+1+3] = TREE_FOLIAGE.getPackedValue();//TFOL1
			douglasColors[vertex+2+3] = TREE_FOLIAGE.getPackedValue();//TFOL2
			douglasColors[vertex+3+3] = TREE_FOLIAGE.getPackedValue();//TFOL3
			douglasColors[vertex+1+3+3] = TREE_FOLIAGE.getPackedValue();//BFOL1
			douglasColors[vertex+2+3+3] = TREE_FOLIAGE.getPackedValue();//BFOL2
			douglasColors[vertex+3+3+3] = TREE_FOLIAGE.getPackedValue();//BFOL3
			
			int vertexCoordIndice = vertex * 3;
			
			// top of tree
			douglasVertices[vertexCoordIndice] = getX(tree);
			douglasVertices[vertexCoordIndice+1] = getY(tree);
			douglasVertices[vertexCoordIndice+2] = getZ(tree) + height;
			
			douglasNormals[vertexCoordIndice] = 0;
			douglasNormals[vertexCoordIndice+1] = 0;
			douglasNormals[vertexCoordIndice+2] = 1;
			
			float width = height * 0.707f / 50.0f;
			
			float angleDegree1 = getAngleDegree1(tree);
			float angleDegree2 = angleDegree1 + 120.0f;
			float angleDegree3 = angleDegree2 + 120.0f;
			float angleRadian1 = angleDegree1 * (float)Math.PI / 180.0f;
			float angleRadian2 = angleDegree2 * (float)Math.PI / 180.0f;
			float angleRadian3 = angleDegree3 * (float)Math.PI / 180.0f;
			
			//trunk
			vertexCoordIndice += 3;
			
			douglasVertices[vertexCoordIndice] = getX(tree) + width * (float)Math.cos(angleRadian1);
			douglasVertices[vertexCoordIndice+1] = getY(tree) + width * (float)Math.sin(angleRadian1);
			douglasVertices[vertexCoordIndice+2] = getZ(tree) - 1;
			
			douglasNormals[vertexCoordIndice] = (float)Math.cos(angleRadian1);
			douglasNormals[vertexCoordIndice+1] = (float)Math.sin(angleRadian1);
			douglasNormals[vertexCoordIndice+2] = 0;
			
			vertexCoordIndice += 3;
			
			douglasVertices[vertexCoordIndice] = getX(tree) + width * (float)Math.cos(angleRadian2);
			douglasVertices[vertexCoordIndice+1] = getY(tree) + width * (float)Math.sin(angleRadian2);
			douglasVertices[vertexCoordIndice+2] = getZ(tree) - 1;
			
			douglasNormals[vertexCoordIndice] =  (float)Math.cos(angleRadian2);
			douglasNormals[vertexCoordIndice+1] = (float)Math.sin(angleRadian2);
			douglasNormals[vertexCoordIndice+2] = 0;
			
			vertexCoordIndice += 3;
			
			douglasVertices[vertexCoordIndice] = getX(tree) + width * (float)Math.cos(angleRadian3);
			douglasVertices[vertexCoordIndice+1] = getY(tree) + width * (float)Math.sin(angleRadian3);
			douglasVertices[vertexCoordIndice+2] = getZ(tree) - 1;
			
			douglasNormals[vertexCoordIndice] = (float)Math.cos(angleRadian3);
			douglasNormals[vertexCoordIndice+1] = (float)Math.sin(angleRadian3);
			douglasNormals[vertexCoordIndice+2] = 0;
			// end of trunk
			
			vertexCoordIndice += 3;
			
			float widthTop = getRandomTopTree(tree);//width *2.5f * forest.randomTopTrees.nextFloat();
			
			// top of tree foliage
			douglasVertices[vertexCoordIndice] = getX(tree)+ widthTop * (float)Math.cos(angleRadian1);
			douglasVertices[vertexCoordIndice+1] = getY(tree)+ widthTop * (float)Math.sin(angleRadian1);
			douglasVertices[vertexCoordIndice+2] = getZ(tree) + height;
			
			douglasNormals[vertexCoordIndice] = (float)Math.cos(angleRadian1);
			douglasNormals[vertexCoordIndice+1] = (float)Math.sin(angleRadian1);
			douglasNormals[vertexCoordIndice+2] = 0.2f;
			
			vertexCoordIndice += 3;
			
			douglasVertices[vertexCoordIndice] = getX(tree)+ widthTop * (float)Math.cos(angleRadian2);
			douglasVertices[vertexCoordIndice+1] = getY(tree)+ widthTop * (float)Math.sin(angleRadian2);
			douglasVertices[vertexCoordIndice+2] = getZ(tree) + height;
			
			douglasNormals[vertexCoordIndice] = (float)Math.cos(angleRadian2);
			douglasNormals[vertexCoordIndice+1] = (float)Math.sin(angleRadian2);
			douglasNormals[vertexCoordIndice+2] = 0.2f;
			
			vertexCoordIndice += 3;
			
			douglasVertices[vertexCoordIndice] = getX(tree)+ widthTop * (float)Math.cos(angleRadian3);
			douglasVertices[vertexCoordIndice+1] = getY(tree)+ widthTop * (float)Math.sin(angleRadian3);
			douglasVertices[vertexCoordIndice+2] = getZ(tree) + height;
			
			douglasNormals[vertexCoordIndice] = (float)Math.cos(angleRadian3);
			douglasNormals[vertexCoordIndice+1] = (float)Math.sin(angleRadian3);
			douglasNormals[vertexCoordIndice+2] = 0.2f;
			
			//foliage
			vertexCoordIndice += 3;

			
			
			float foliageWidth = getRandomBottomTree(tree);//(height+ forest.randomBottomTrees.nextFloat()*12.0f) * 0.1f;
			
			
			
			douglasVertices[vertexCoordIndice] = getX(tree) + foliageWidth * (float)Math.cos(angleRadian1);
			douglasVertices[vertexCoordIndice+1] = getY(tree) + foliageWidth * (float)Math.sin(angleRadian1);

			float z = df.sg.getInternalZ(douglasVertices[vertexCoordIndice],douglasVertices[vertexCoordIndice+1],0.0f) + df.sg.getzTranslation();
			
			douglasVertices[vertexCoordIndice+2] = Math.max(getZ(tree) + 2.5f,z+0.2f);
			
			douglasNormals[vertexCoordIndice] =  (float)Math.cos(angleRadian1);
			douglasNormals[vertexCoordIndice+1] =  (float)Math.sin(angleRadian1);
			douglasNormals[vertexCoordIndice+2] = 0;
			
			vertexCoordIndice += 3;
			
			douglasVertices[vertexCoordIndice] = getX(tree) + foliageWidth * (float)Math.cos(angleRadian2);
			douglasVertices[vertexCoordIndice+1] = getY(tree) + foliageWidth * (float)Math.sin(angleRadian2);
			
			z = df.sg.getInternalZ(douglasVertices[vertexCoordIndice],douglasVertices[vertexCoordIndice+1],0.0f) + df.sg.getzTranslation();
			
			douglasVertices[vertexCoordIndice+2] = Math.max(getZ(tree) + 2.5f,z+0.2f);
			
			douglasNormals[vertexCoordIndice] =   (float)Math.cos(angleRadian2);
			douglasNormals[vertexCoordIndice+1] =  (float)Math.sin(angleRadian2);
			douglasNormals[vertexCoordIndice+2] = 0;
			
			vertexCoordIndice += 3;
			
			douglasVertices[vertexCoordIndice] = getX(tree) + foliageWidth * (float)Math.cos(angleRadian3);
			douglasVertices[vertexCoordIndice+1] = getY(tree) + foliageWidth * (float)Math.sin(angleRadian3);
			
			z = df.sg.getInternalZ(douglasVertices[vertexCoordIndice],douglasVertices[vertexCoordIndice+1],0.0f) + df.sg.getzTranslation();
			
			douglasVertices[vertexCoordIndice+2] = Math.max(getZ(tree) + 2.5f,z+0.2f);
			
			douglasNormals[vertexCoordIndice] = (float)Math.cos(angleRadian3);
			douglasNormals[vertexCoordIndice+1] = (float)Math.sin(angleRadian3);
			douglasNormals[vertexCoordIndice+2] = 0;
			// end of foliage
			
			int i = tree * NB_INDICES_PER_TREE;
			
			// trunk side
			douglasIndices[i] = vertex;
			douglasIndices[i+1] = vertex+1;
			douglasIndices[i+2] = vertex+2;
			douglasIndices[i+3] = -1;
			
			i+= NB_INDICES_PER_TRIANGLE;

//			indices[i] = vertex+1;
//			indices[i+1] = vertex+2;
//			indices[i+2] = vertex+3;
//			indices[i+3] = -1;
//
//			i+= NB_INDICES_PER_TRIANGLE;

			// trunk side
			douglasIndices[i] = vertex+0;
			douglasIndices[i+1] = vertex+2;
			douglasIndices[i+2] = vertex+3;
			douglasIndices[i+3] = -1;

			i+= NB_INDICES_PER_TRIANGLE;

			// trunk side
			douglasIndices[i] = vertex+0;
			douglasIndices[i+1] = vertex+3;
			douglasIndices[i+2] = vertex+1;
			douglasIndices[i+3] = -1;
			
			i+= NB_INDICES_PER_TRIANGLE;

			//foliage side
			douglasIndices[i] = vertex+1+3;
			douglasIndices[i+1] = vertex+1+3+3;
			douglasIndices[i+2] = vertex+2+3+3;
			douglasIndices[i+3] = vertex+2+3;
			douglasIndices[i+4] = -1;
			
			i+= /*NB_INDICES_PER_TRIANGLE*/NB_INDICES_PER_QUAD;

			// foliage side 
			douglasIndices[i] = vertex+2+3;
			douglasIndices[i+1] = vertex+2+3+3;
			douglasIndices[i+2] = vertex+3+3+3;
			douglasIndices[i+3] = vertex+3+3;
			douglasIndices[i+4] = -1;

			i+= /*NB_INDICES_PER_TRIANGLE*/NB_INDICES_PER_QUAD;

			// foliage side
			douglasIndices[i] = vertex+3+3;
			douglasIndices[i+1] = vertex+3+3+3;
			douglasIndices[i+2] = vertex+1+3+3;
			douglasIndices[i+3] = vertex+1+3;
			douglasIndices[i+4] = -1;
			
			i+= /*NB_INDICES_PER_TRIANGLE*/NB_INDICES_PER_QUAD;

			// foliage bottom
			douglasIndices[i] = vertex+3+3+3;
			douglasIndices[i+1] = vertex+2+3+3;
			douglasIndices[i+2] = vertex+1+3+3;
			douglasIndices[i+3] = -1;
			
			i+= /*NB_INDICES_PER_TRIANGLE*/NB_INDICES_PER_TRIANGLE;

			// foliage top
			douglasIndices[i] = vertex+1+3;
			douglasIndices[i+1] = vertex+2+3;
			douglasIndices[i+2] = vertex+3+3;
			douglasIndices[i+3] = -1;
			
		}
	}
}
