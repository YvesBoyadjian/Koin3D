package application.scenegraph;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import application.objects.DouglasFir;
import jscenegraph.coin3d.inventor.lists.SbListInt;
import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbColor;
import jscenegraph.database.inventor.SbRotation;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.port.memorybuffer.FloatMemoryBuffer;

public class DouglasChunk {
	
	private static final SbColor TRUNK_COLOR = new SbColor(60.0f/255,42.0f/255,0.0f/255);
	
	private static final SbColor TREE_FOLIAGE = new SbColor(22.0f/255,42.0f/255,0.0f/255);
	
	public static final SbColor TREE_FOLIAGE_AVERAGE_MULTIPLIER = new SbColor(0.8f,0.85f,0.4f);
	
	private static final SbColor GREEN = new SbColor(5.0f/255,52.0f/255,0.0f/255);
	
	DouglasForest df;

	SbBox3f boundingBox = new SbBox3f();
	
	float xMin = Float.MAX_VALUE;
	float yMin = Float.MAX_VALUE;
	float zMin = Float.MAX_VALUE;
	
	float xMax =  - Float.MAX_VALUE;
	float yMax = - Float.MAX_VALUE;
	float zMax = - Float.MAX_VALUE;
	
	SbListInt insideTrees = new SbListInt();
	
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
	
	public int getRandomColorMultiplierTree(int i) {
		return df.randomColorMultiplierTree[insideTrees.get(i)];
	}
	
	public float getRandomLeanAngleTree(int i) {
		return df.randomLeanAngleTree[insideTrees.get(i)];		
	}
	
	public float getRandomLeanDirectionAngleTree(int i) {
		return df.randomLeanDirectionAngleTree[insideTrees.get(i)];		
	}

	int[] douglasIndicesF;
	FloatMemoryBuffer douglasVerticesF;
	FloatMemoryBuffer douglasNormalsF;
	int[] douglasColorsF;
	FloatMemoryBuffer douglasTexCoordsF;
	
	int[] douglasIndicesT;
	FloatMemoryBuffer douglasVerticesT;
	FloatMemoryBuffer douglasNormalsT;
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
		
		douglasVerticesF = FloatMemoryBuffer.allocateFloats(nbVertices * 3);
		
		///*FloatBuffer*/ByteBuffer douglasVerticesFb = douglasVerticesF.toByteBuffer();/*.asFloatBuffer()*/;
		
		douglasNormalsF = FloatMemoryBuffer.allocateFloats(nbVertices * 3);
		
		///*FloatBuffer*/ByteBuffer douglasNormalsFb = douglasNormalsF.toByteBuffer()/*.asFloatBuffer()*/;
		
		douglasColorsF = new int[nbVertices];
		
		douglasTexCoordsF = FloatMemoryBuffer.allocateFloats(nbVertices * 2);
		
		//ByteBuffer douglasTexCoordsFb = douglasTexCoordsF.toByteBuffer();/*.asFloatBuffer()*/;
		
		int[] indices = new int[4];
		
		for( int tree = 0; tree< nbDouglas; tree++) {
			
			float height = getHeight(tree);
			
			int vertex = tree * NB_VERTEX_PER_TREE_FOLIAGE;
			
			int colorMultiplierTree = getRandomColorMultiplierTree(tree);
			
			douglasColorsF[vertex] = colorMultiplierTree;//TREE_FOLIAGE.getPackedValue();//TFOL1
			douglasColorsF[vertex+1] = colorMultiplierTree;//TREE_FOLIAGE.getPackedValue();//TFOL2
			douglasColorsF[vertex+2] = colorMultiplierTree;//TREE_FOLIAGE.getPackedValue();//TFOL3
			douglasColorsF[vertex+3] = colorMultiplierTree;//TREE_FOLIAGE.getPackedValue();//BFOL1
			douglasColorsF[vertex+1+3] = colorMultiplierTree;//TREE_FOLIAGE.getPackedValue();//BFOL2
			douglasColorsF[vertex+2+3] = colorMultiplierTree;//TREE_FOLIAGE.getPackedValue();//BFOL3
			
			int vertexCoordIndice = vertex * 3 ;
			int texCoordIndice = vertex * 2;
						
			float angleDegree1 = getAngleDegree1(tree);
			float angleDegree2 = angleDegree1 + 120.0f;
			float angleDegree3 = angleDegree2 + 120.0f;
			float angleRadian1 = angleDegree1 * (float)Math.PI / 180.0f;
			float angleRadian2 = angleDegree2 * (float)Math.PI / 180.0f;
			float angleRadian3 = angleDegree3 * (float)Math.PI / 180.0f;
			
			SbVec3f rotAxis = new SbVec3f((float)Math.sin(getRandomLeanDirectionAngleTree(tree)),(float)Math.cos(getRandomLeanDirectionAngleTree(tree)),0.0f);
			
			SbRotation rot = new SbRotation(rotAxis,getRandomLeanAngleTree(tree));
						
			SbVec3f xyzTree = new SbVec3f(getX(tree), getY(tree),getZ(tree));
			
			SbVec3f xyzTop = new SbVec3f(0,0,height);
			
			SbVec3f xyzTopLean = rot.multVec(xyzTop);
			
			float widthTop = getRandomTopTree(tree);//width *2.5f * forest.randomTopTrees.nextFloat();
			
			// top of tree foliage
			douglasVerticesF.setFloat(vertexCoordIndice, getX(tree)+ widthTop * (float)Math.cos(angleRadian1) + xyzTopLean.getX() );
			douglasVerticesF.setFloat(vertexCoordIndice+1, getY(tree)+ widthTop * (float)Math.sin(angleRadian1) + xyzTopLean.getY() );
			douglasVerticesF.setFloat(vertexCoordIndice+2, getZ(tree) /*+ height*/ + xyzTopLean.getZ());
			
			douglasNormalsF.setFloat(vertexCoordIndice, (float)Math.cos(angleRadian1));
			douglasNormalsF.setFloat(vertexCoordIndice+1, (float)Math.sin(angleRadian1));
			douglasNormalsF.setFloat(vertexCoordIndice+2, 0.2f);
			
			douglasTexCoordsF.setFloat(texCoordIndice, 0.0f);
			douglasTexCoordsF.setFloat(texCoordIndice+1, /*70.0f*/height);
			
			vertexCoordIndice += 3;
			texCoordIndice += 2;
			
			douglasVerticesF.setFloat(vertexCoordIndice, getX(tree)+ widthTop * (float)Math.cos(angleRadian2) + xyzTopLean.getX() );
			douglasVerticesF.setFloat(vertexCoordIndice+1, getY(tree)+ widthTop * (float)Math.sin(angleRadian2) + xyzTopLean.getY() );
			douglasVerticesF.setFloat(vertexCoordIndice+2, getZ(tree) +  + xyzTopLean.getZ() );
			
			douglasNormalsF.setFloat(vertexCoordIndice, (float)Math.cos(angleRadian2));
			douglasNormalsF.setFloat(vertexCoordIndice+1, (float)Math.sin(angleRadian2));
			douglasNormalsF.setFloat(vertexCoordIndice+2, 0.2f);
			
			douglasTexCoordsF.setFloat(texCoordIndice, 1.0f);
			douglasTexCoordsF.setFloat(texCoordIndice+1, /*70.0f*/height);
			
			vertexCoordIndice += 3;
			texCoordIndice += 2;
			
			douglasVerticesF.setFloat(vertexCoordIndice, getX(tree)+ widthTop * (float)Math.cos(angleRadian3) + xyzTopLean.getX() );
			douglasVerticesF.setFloat(vertexCoordIndice+1, getY(tree)+ widthTop * (float)Math.sin(angleRadian3) + xyzTopLean.getY() );
			douglasVerticesF.setFloat(vertexCoordIndice+2, getZ(tree) +  + xyzTopLean.getZ() );
			
			douglasNormalsF.setFloat(vertexCoordIndice, (float)Math.cos(angleRadian3));
			douglasNormalsF.setFloat(vertexCoordIndice+1, (float)Math.sin(angleRadian3));
			douglasNormalsF.setFloat(vertexCoordIndice+2, 0.2f);
			
			douglasTexCoordsF.setFloat(texCoordIndice, 2.0f);
			douglasTexCoordsF.setFloat(texCoordIndice+1, /*70.0f*/height);
			
			//foliage
			vertexCoordIndice += 3;
			texCoordIndice += 2;

			
			
			float foliageWidth = getRandomBottomTree(tree);//(height+ forest.randomBottomTrees.nextFloat()*12.0f) * 0.1f;
			
			
			
			douglasVerticesF.setFloat(vertexCoordIndice, getX(tree) + foliageWidth * (float)Math.cos(angleRadian1));
			douglasVerticesF.setFloat(vertexCoordIndice+1, getY(tree) + foliageWidth * (float)Math.sin(angleRadian1));

			float z = df.sg.getInternalZ(douglasVerticesF.getFloat(vertexCoordIndice),douglasVerticesF.getFloat(vertexCoordIndice+1),indices) + df.sg.getzTranslation();
			
			douglasVerticesF.setFloat(vertexCoordIndice+2, Math.max(getZ(tree) + 2.5f,z+0.2f));
			
			douglasNormalsF.setFloat(vertexCoordIndice,  (float)Math.cos(angleRadian1));
			douglasNormalsF.setFloat(vertexCoordIndice+1,  (float)Math.sin(angleRadian1));
			douglasNormalsF.setFloat(vertexCoordIndice+2, 0);
			
			douglasTexCoordsF.setFloat(texCoordIndice, 0.0f);
			douglasTexCoordsF.setFloat(texCoordIndice+1, 0.0f);
			
			vertexCoordIndice += 3;
			texCoordIndice += 2;
			
			douglasVerticesF.setFloat(vertexCoordIndice, getX(tree) + foliageWidth * (float)Math.cos(angleRadian2));
			douglasVerticesF.setFloat(vertexCoordIndice+1, getY(tree) + foliageWidth * (float)Math.sin(angleRadian2));
			
			z = df.sg.getInternalZ(douglasVerticesF.getFloat(vertexCoordIndice),douglasVerticesF.getFloat(vertexCoordIndice+1),indices) + df.sg.getzTranslation();
			
			douglasVerticesF.setFloat(vertexCoordIndice+2, Math.max(getZ(tree) + 2.5f,z+0.2f));
			
			douglasNormalsF.setFloat(vertexCoordIndice,   (float)Math.cos(angleRadian2));
			douglasNormalsF.setFloat(vertexCoordIndice+1,  (float)Math.sin(angleRadian2));
			douglasNormalsF.setFloat(vertexCoordIndice+2, 0);
			
			douglasTexCoordsF.setFloat(texCoordIndice, /*10.0f*/foliageWidth);
			douglasTexCoordsF.setFloat(texCoordIndice+1, 0.0f);
			
			vertexCoordIndice += 3;
			texCoordIndice += 2;
			
			douglasVerticesF.setFloat(vertexCoordIndice, getX(tree) + foliageWidth * (float)Math.cos(angleRadian3));
			douglasVerticesF.setFloat(vertexCoordIndice+1, getY(tree) + foliageWidth * (float)Math.sin(angleRadian3));
			
			z = df.sg.getInternalZ(douglasVerticesF.getFloat(vertexCoordIndice),douglasVerticesF.getFloat(vertexCoordIndice+1),indices) + df.sg.getzTranslation();
			
			douglasVerticesF.setFloat(vertexCoordIndice+2, Math.max(getZ(tree) + 2.5f,z+0.2f));
			
			douglasNormalsF.setFloat(vertexCoordIndice, (float)Math.cos(angleRadian3));
			douglasNormalsF.setFloat(vertexCoordIndice+1, (float)Math.sin(angleRadian3));
			douglasNormalsF.setFloat(vertexCoordIndice+2, 0);
			
			douglasTexCoordsF.setFloat(texCoordIndice, /*20.0f*/foliageWidth*2);
			douglasTexCoordsF.setFloat(texCoordIndice+1, 0.0f);
			
			// end of foliage
			
			int i = tree * NB_INDICES_PER_TREE;
			
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
		//douglasVerticesFb.flip();
		//douglasNormalsFb.flip();
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
		
		douglasVerticesT = FloatMemoryBuffer.allocateFloats(nbVertices * 3);
		
		douglasNormalsT = FloatMemoryBuffer.allocateFloats(nbVertices * 3);
		
		douglasColorsT = new int[nbVertices];
		
		float trunk_width_coef = DouglasFir.trunk_diameter_angle_degree*(float)Math.PI/180.0f;
		
		for( int tree = 0; tree< nbDouglas; tree++) {
			
			float height = getHeight(tree);
			
			int vertex = tree * NB_VERTEX_PER_TREE_TRUNK;
			
			douglasColorsT[vertex] = TRUNK_COLOR.getPackedValue();//TOP
			douglasColorsT[vertex+1] = TRUNK_COLOR.getPackedValue();//TR1
			douglasColorsT[vertex+2] = TRUNK_COLOR.getPackedValue();//TR2
			douglasColorsT[vertex+3] = TRUNK_COLOR.getPackedValue();//TR3
			
			int vertexCoordIndice = vertex * 3;
			
			SbVec3f rotAxis = new SbVec3f((float)Math.sin(getRandomLeanDirectionAngleTree(tree)),(float)Math.cos(getRandomLeanDirectionAngleTree(tree)),0.0f);
			
			SbRotation rot = new SbRotation(rotAxis,getRandomLeanAngleTree(tree));
						
			SbVec3f xyzTree = new SbVec3f(getX(tree), getY(tree),getZ(tree));
			
			SbVec3f xyzTop = new SbVec3f(0,0,height);
			
			SbVec3f xyzTopLean = rot.multVec(xyzTop);
			
			// top of tree
			douglasVerticesT.setFloat(vertexCoordIndice, getX(tree) + xyzTopLean.getX() );
			douglasVerticesT.setFloat(vertexCoordIndice+1, getY(tree) + xyzTopLean.getY() );
			douglasVerticesT.setFloat(vertexCoordIndice+2, getZ(tree) + xyzTopLean.getZ() );
			
			xMin = Math.min(xMin,getX(tree));
			yMin = Math.min(yMin,getY(tree));
			zMin = Math.min(zMin,getZ(tree));
			
			xMax = Math.max(xMax,getX(tree));
			yMax = Math.max(yMax,getY(tree));
			zMax = Math.max(zMax,getZ(tree) + height);
			
			douglasNormalsT.setFloat(vertexCoordIndice, 0);
			douglasNormalsT.setFloat(vertexCoordIndice+1, 0);
			douglasNormalsT.setFloat(vertexCoordIndice+2, 1);
			
			float width = height * trunk_width_coef;// 0.707f / 50.0f;
			
			float angleDegree1 = getAngleDegree1(tree);
			float angleDegree2 = angleDegree1 + 120.0f;
			float angleDegree3 = angleDegree2 + 120.0f;
			float angleRadian1 = angleDegree1 * (float)Math.PI / 180.0f;
			float angleRadian2 = angleDegree2 * (float)Math.PI / 180.0f;
			float angleRadian3 = angleDegree3 * (float)Math.PI / 180.0f;
			
			//trunk
			vertexCoordIndice += 3;
			
			douglasVerticesT.setFloat(vertexCoordIndice, getX(tree) + width * (float)Math.cos(angleRadian1));
			douglasVerticesT.setFloat(vertexCoordIndice+1, getY(tree) + width * (float)Math.sin(angleRadian1));
			douglasVerticesT.setFloat(vertexCoordIndice+2, getZ(tree) - 1);
			
			douglasNormalsT.setFloat(vertexCoordIndice, (float)Math.cos(angleRadian1));
			douglasNormalsT.setFloat(vertexCoordIndice+1, (float)Math.sin(angleRadian1));
			douglasNormalsT.setFloat(vertexCoordIndice+2, 0);
			
			vertexCoordIndice += 3;
			
			douglasVerticesT.setFloat(vertexCoordIndice, getX(tree) + width * (float)Math.cos(angleRadian2));
			douglasVerticesT.setFloat(vertexCoordIndice+1, getY(tree) + width * (float)Math.sin(angleRadian2));
			douglasVerticesT.setFloat(vertexCoordIndice+2, getZ(tree) - 1);
			
			douglasNormalsT.setFloat(vertexCoordIndice,  (float)Math.cos(angleRadian2));
			douglasNormalsT.setFloat(vertexCoordIndice+1, (float)Math.sin(angleRadian2));
			douglasNormalsT.setFloat(vertexCoordIndice+2, 0);
			
			vertexCoordIndice += 3;
			
			douglasVerticesT.setFloat(vertexCoordIndice, getX(tree) + width * (float)Math.cos(angleRadian3));
			douglasVerticesT.setFloat(vertexCoordIndice+1, getY(tree) + width * (float)Math.sin(angleRadian3));
			douglasVerticesT.setFloat(vertexCoordIndice+2, getZ(tree) - 1);
			
			douglasNormalsT.setFloat(vertexCoordIndice, (float)Math.cos(angleRadian3));
			douglasNormalsT.setFloat(vertexCoordIndice+1, (float)Math.sin(angleRadian3));
			douglasNormalsT.setFloat(vertexCoordIndice+2, 0);
			// end of trunk
			
			vertexCoordIndice += 3;
			
			int i = tree * NB_INDICES_PER_TREE;
			
			// trunk side
			douglasIndicesT[i] = vertex;
			douglasIndicesT[i+1] = vertex+1;
			douglasIndicesT[i+2] = vertex+2;
			douglasIndicesT[i+3] = -1;
			
			i+= NB_INDICES_PER_TRIANGLE;

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

		}
	}

	public void computeDouglas() {
		computeDouglasT();
		computeDouglasF();
	}
}
