/**
 * 
 */
package application.scenegraph;

import java.util.Random;

import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.fields.SoMFVec3f;

/**
 * @author Yves Boyadjian
 *
 */
public class Seals {

	int NB_SEAL_BIRTHS = 4000000;
	
	final static int SEED_SEAL_PLACEMENT = 48;
	
	SceneGraphIndexedFaceSetShader sg; 
	 
	int nbSeals = 0;
	
	SoMFVec3f sealCoords = new SoMFVec3f();
	
	public Seals( SceneGraphIndexedFaceSetShader sg ) {
		this.sg = sg;
	}

	public int getNbSeals() {
		
		if(nbSeals == 0) {
			compute();
		}
		
		return nbSeals;
	}

	public float[] getSeal(int sealIndex, float[] vector) {
		
		if(nbSeals == 0) {
			compute();
		}
		
		SbVec3f oneSealCoords = sealCoords.getValueAt(sealIndex);
		vector[0] = oneSealCoords.getX();
		vector[1] = oneSealCoords.getY();
		vector[2] = oneSealCoords.getZ();
		return vector;
	}
	
	private void compute() {
		
		Random randomPlacementSeals = new Random(SEED_SEAL_PLACEMENT);		
		
		int[] indices = new int[4];
		
		float zWater =  - 150 + sg.getzTranslation() - sg.CUBE_DEPTH /2;
		
		float[] xyz = new float[3];
		int start;
		
		for( int i = 0; i < NB_SEAL_BIRTHS; i++) {
			float x = getRandomX(randomPlacementSeals);
			float y = getRandomY(randomPlacementSeals);
			float z = sg.getInternalZ(x,y,0.0f,indices) + sg.getzTranslation();
			
			boolean isNearWater = Math.abs(z - zWater) < 5;
			boolean isAboveWater = z > zWater;
			
			if( isNearWater && isAboveWater) {
				xyz[0] = x;
				xyz[1] = y;
				xyz[2] = z + 0.3f;
				start = sealCoords.getNum();
				sealCoords.setValues(start, xyz);
				
				nbSeals++;
			}
		}
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

}
