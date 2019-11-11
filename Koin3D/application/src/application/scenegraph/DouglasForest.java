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
import java.util.Collection;
import java.util.List;
import java.util.Random;

import application.objects.DouglasFir;
import jscenegraph.coin3d.inventor.nodes.SoVertexProperty;
import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.nodes.SoIndexedFaceSet;
import jscenegraph.database.inventor.nodes.SoSeparator;

/**
 * @author Yves Boyadjian
 *
 */
public class DouglasForest {
	
	int NB_DOUGLAS_SEEDS = 4000000;
	
	final int SEED_PLACEMENT_TREES = 42;
	final int SEED_HEIGHT_TREES = 43;
	final int SEED_ANGLE_TREES = 44;
	final int SEED_WITH_TOP_TREES = 45;
	final int SEED_WITH_BOTTOM_TREES = 46;
	
	float[] xArray = new float[NB_DOUGLAS_SEEDS]; 
	float[] yArray = new float[NB_DOUGLAS_SEEDS]; 
	float[] zArray = new float[NB_DOUGLAS_SEEDS]; 
	float[] heightArray = new float[NB_DOUGLAS_SEEDS]; 
	float[] angleDegree1 = new float[NB_DOUGLAS_SEEDS];
	float[] randomTopTree = new float[NB_DOUGLAS_SEEDS];
	float[] randomBottomTree = new float[NB_DOUGLAS_SEEDS];
	
	SceneGraphIndexedFaceSetShader sg;
	
	private List<DouglasChunk> douglasChunks = new ArrayList<>(); 
	
	int nbDouglas = 0;
	
	public DouglasForest( SceneGraphIndexedFaceSetShader sg ) {
		this.sg = sg;
	}

	public int compute() {
		
		if( ! loadDouglas()) {
		
			Random randomPlacementTrees = new Random(SEED_PLACEMENT_TREES);		
			Random randomHeightTrees = new Random(SEED_HEIGHT_TREES);		
			Random randomAngleTrees = new Random(SEED_ANGLE_TREES);
			Random randomTopTrees  = new Random(SEED_WITH_TOP_TREES);
			Random randomBottomTrees  = new Random(SEED_WITH_BOTTOM_TREES);
			
			for( int i = 0; i < NB_DOUGLAS_SEEDS; i++) {
				float x = getRandomX(randomPlacementTrees);
				float y = getRandomY(randomPlacementTrees);
				float z = sg.getInternalZ(x,y,0.0f) + sg.getzTranslation();
				
				boolean isAboveWater = z > - 150 + sg.getzTranslation() - sg.CUBE_DEPTH /2;
				boolean isUnderSnowLevel = z < sg.ALPINE_HEIGHT;
				boolean isStone = sg.isStone(x,y);
				if( isAboveWater && isUnderSnowLevel && !isStone) {
					
					float height = DouglasFir.getHeight(randomHeightTrees);
					
					xArray[i] = x;
					yArray[i] = y;
					zArray[i] = z;
					heightArray[i] = height;
					float angleDegree = 120.0f * randomAngleTrees.nextFloat();
					angleDegree1[i] = angleDegree;
					float width = height * 0.707f / 50.0f;
					float widthTop = width *2.5f * randomTopTrees.nextFloat();
					randomTopTree[i] = widthTop;
					float foliageWidth = (height+ randomBottomTrees.nextFloat()*12.0f) * 0.1f;
					randomBottomTree[i] = foliageWidth;
					nbDouglas++;
				}
			}
			saveDouglas();
		}
		
		return nbDouglas;
	}

	private void saveDouglas() {
		File file = new File("douglas_forest.mri");
		
		try {
			FileOutputStream fos = new FileOutputStream(file);
			
			byte[] nbda = new byte[Integer.BYTES];
			ByteBuffer bbnbda = ByteBuffer.wrap(nbda);
			bbnbda.asIntBuffer().put(nbDouglas);
			fos.write(nbda);
			
			byte[] ba = new byte[xArray.length*Float.BYTES];
			ByteBuffer bb = ByteBuffer.wrap(ba);
			
			bb.asFloatBuffer().put(xArray);
			fos.write(ba);
			
			bb.asFloatBuffer().put(yArray);
			fos.write(ba);
			
			bb.asFloatBuffer().put(zArray);
			fos.write(ba);
			
			bb.asFloatBuffer().put(heightArray);
			fos.write(ba);
			
			bb.asFloatBuffer().put(angleDegree1);
			fos.write(ba);
			
			bb.asFloatBuffer().put(randomTopTree);
			fos.write(ba);
			
			bb.asFloatBuffer().put(randomBottomTree);
			fos.write(ba);
			
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean loadDouglas() {
		File file = new File( "douglas_forest.mri" );
		
		if( !file.exists()) {
			return false;
		}
		if( !file.isFile()) {
			return false;
		}
		if ( file.length() != getDouglasForestFileLength()) {
			return false;
		}
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			
			int nbb = (int)file.length();									
			byte[] buffer = new byte[nbb];
			fileInputStream.read(buffer);
			
			ByteBuffer bbnbda = ByteBuffer.wrap(buffer,0,Integer.BYTES);
			nbDouglas = bbnbda.asIntBuffer().get();
			
			ByteBuffer bb = ByteBuffer.wrap(buffer,Integer.BYTES,(int)file.length()-Integer.BYTES);
			
			FloatBuffer fb = bb.asFloatBuffer();			
			
			fb.get(xArray);
			fb.get(yArray);
			fb.get(zArray);
			fb.get(heightArray);
			fb.get(angleDegree1);
			fb.get(randomTopTree);
			fb.get(randomBottomTree);
			
			fileInputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
		return true;
	}

	private long getDouglasForestFileLength() {
		return NB_DOUGLAS_SEEDS * Float.BYTES * 7 + Integer.BYTES;
	}

	float getRandomX(Random randomPlacementTrees) {
		SbBox3f sceneBox = sg.getChunks().getSceneBox();
		float xMin = sceneBox.getBounds()[0];
		float xMax = sceneBox.getBounds()[3];
		return xMin + (xMax - xMin) * randomPlacementTrees.nextFloat();
	}
	
	float getRandomY(Random randomPlacementTrees) {
		SbBox3f sceneBox = sg.getChunks().getSceneBox();
		float yMin = sceneBox.getBounds()[1];
		float yMax = sceneBox.getBounds()[4];
		return yMin + (yMax - yMin) * randomPlacementTrees.nextFloat();
	}

	public void buildDouglasChunks() {
		SbBox3f sceneBox = sg.getChunks().getSceneBox();
		float xMin = sceneBox.getBounds()[0];
		float xMax = sceneBox.getBounds()[3];		
		float yMin = sceneBox.getBounds()[1];
		float yMax = sceneBox.getBounds()[4];
		
		float delta_x = xMax - xMin;
		float delta_y = yMax - yMin;
		
		float width_x = delta_x / 10;		
		float width_y = delta_y / 10;
		
		float min_width = Math.min(width_x,width_y);
		
		int nb_x = (int)Math.round(Math.ceil(delta_x / min_width));
		int nb_y = (int)Math.round(Math.ceil(delta_y / min_width));
		
		for(int i=0;i<nb_x;i++) {
			for(int j=0; j< nb_y;j++) {
				float chunkMinX = xMin + i*min_width;
				float chunkMaxX = xMin + (i+1)*min_width;
				float chunkMinY = yMin + j*min_width;
				float chunkMaxY = yMin + (j+1)*min_width;
				SbBox3f chunkBB = new SbBox3f();
				chunkBB.setBounds(chunkMinX, chunkMinY, 0, chunkMaxX, chunkMaxY, 0);
				douglasChunks.add(new DouglasChunk(this,chunkBB));
			}
		}
	}

	public void fillDouglasChunks() {
		
		final SbVec3f xy = new SbVec3f();
		
		for(int tree=0; tree<nbDouglas;tree++) {
			float x = xArray[tree];
			float y = yArray[tree];
			
			xy.setValue(x, y, 0.0f);
			
			int nbChunks = douglasChunks.size(); 
			for(int i=0;i<nbChunks;i++) {
				DouglasChunk chunk = douglasChunks.get(i);
				if( chunk.boundingBox.intersect(xy)) {
					chunk.addTree(/*x,y,zArray[tree],heightArray[tree],angleDegree1[tree],randomTopTree[tree],randomBottomTree[tree]*/tree);
				}
			}
			
		}
	}

	public void computeDouglas() {
		douglasChunks.parallelStream().forEach((dc)->dc.computeDouglas());
//		for( DouglasChunk chunk : douglasChunks ) {
//			chunk.computeDouglas();
//		}
	}

	public SoSeparator getDouglasTrees(float distance) {
		SoSeparator separator = new SoSeparator();
		
		for( DouglasChunk chunk : douglasChunks ) {

			SoLODIndexedFaceSet indexedFaceSet = new SoLODIndexedFaceSet() {
				public void GLRender(SoGLRenderAction action)
				{
					super.GLRender(action);
				}			
			};
			
			indexedFaceSet.coordIndex.setValuesPointer(chunk.douglasIndices);
			
			SoVertexProperty vertexProperty = new SoVertexProperty();
			
			vertexProperty.vertex.setValuesPointer(chunk.douglasVertices);
			
			vertexProperty.normalBinding.setValue(SoVertexProperty.Binding.PER_VERTEX_INDEXED);
			
			vertexProperty.normal.setValuesPointer(chunk.douglasNormals);
			
			vertexProperty.materialBinding.setValue(SoVertexProperty.Binding.PER_VERTEX_INDEXED);
			
			vertexProperty.orderedRGBA.setValues(0, chunk.douglasColors);
			
			indexedFaceSet.vertexProperty.setValue(vertexProperty);
			
			separator.addChild(indexedFaceSet);
			
			indexedFaceSet.maxDistance = distance;
		}		
		
		return separator;
	}

}