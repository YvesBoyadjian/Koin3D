package application.scenegraph;

import application.objects.Target;
import jscenegraph.coin3d.inventor.lists.SbListFloat;
import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.fields.SoMFVec3f;

import java.util.Random;

public class MountainGoats implements Target {
    @Override
    public String getTexturePath() {
        return "ressource/Mountain_Goat_USFWS.jpg";
    }

    @Override
    public int getNbTargets() {

        if( nbGoats == 0 ) {
            compute();
        }
        return nbGoats;
    }

    @Override
    public float[] getTarget(int goatIndex, float[] vector) {

        if (nbGoats == 0) {
            compute();
        }

        vector[0] = goatCoords.get(goatIndex*3);
        vector[1] = goatCoords.get(goatIndex*3+1);
        vector[2] = goatCoords.get(goatIndex*3+2);

        return vector;
    }

    @Override
    public float getSize() {
        return 1.6f;
    }

    @Override
    public float getRatio() {
        return 2663.0f/1804.0f;
    }

    @Override
    public float getViewDistance() {
        return 1000;
    }

    SceneGraphIndexedFaceSetShader sg;

    int nbGoats = 0;

    SbListFloat goatCoords = new SbListFloat();

    final int HUNDRED_THOUSAND = 100000;

    int NB_GOAT_BIRTHS = HUNDRED_THOUSAND;

    final static int SEED_GOAT_PLACEMENT = 50;

    public MountainGoats( SceneGraphIndexedFaceSetShader sg ) {
        this.sg = sg;
    }

    private void compute() {

        Random randomPlacementBigFoots = new Random(SEED_GOAT_PLACEMENT);

        int[] indices = new int[4];

        float zWater = - 150 + sg.getzTranslation() - sg.CUBE_DEPTH/2;

        float[] xyz = new float[3];

        for( int i = 0; i < NB_GOAT_BIRTHS; i++) {
            float x = getRandomX(randomPlacementBigFoots);
            float y = getRandomY(randomPlacementBigFoots);
            float z = sg.getInternalZ(x,y,indices) + sg.getzTranslation();

            boolean isNearWater = Math.abs(z - zWater) < 200;
            boolean isAboveWater = z > zWater;
            boolean isNotInSnow = z - zWater < 2000;

            float z1 = sg.getInternalZ(x+0.5f,y,indices) + sg.getzTranslation();
            float z2 = sg.getInternalZ(x-0.5f,y,indices) + sg.getzTranslation();
            float z3 = sg.getInternalZ(x,y+0.5f,indices) + sg.getzTranslation();
            float z4 = sg.getInternalZ(x,y-0.5f,indices) + sg.getzTranslation();
            float d1 = Math.abs(z-z1);
            float d2 = Math.abs(z-z2);
            float d3 = Math.abs(z-z3);
            float d4 = Math.abs(z-z4);
            float dzMax = 0.35f;
            boolean isNotTooSteep = (d1<dzMax) && (d2<dzMax) && (d3<dzMax) && (d4<dzMax);

            if( !isNearWater && isAboveWater && isNotTooSteep && isNotInSnow ) {
                xyz[0] = x;
                xyz[1] = y;
                xyz[2] = z + 0.45f;
                goatCoords.append(xyz[0]);
                goatCoords.append(xyz[1]);
                goatCoords.append(xyz[2]);

                nbGoats++;
            }
        }
    }

    float getRandomX(Random randomPlacementGoat) {
        SbBox3f sceneBox = sg.getChunks().getSceneBoxFullIsland();
        float xMin = sceneBox.getBounds()[0];
        float xMax = sceneBox.getBounds()[3];
        return xMin + (xMax - xMin) * randomPlacementGoat.nextFloat();
    }

    float getRandomY(Random randomPlacementGoat) {
        SbBox3f sceneBox = sg.getChunks().getSceneBoxFullIsland();
        float yMin = sceneBox.getBounds()[1];
        float yMax = sceneBox.getBounds()[4];
        return yMin + (yMax - yMin) * randomPlacementGoat.nextFloat();
    }
}
