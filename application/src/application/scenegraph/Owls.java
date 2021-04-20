package application.scenegraph;

import application.objects.Target;
import jscenegraph.coin3d.inventor.lists.SbListFloat;
import jscenegraph.database.inventor.SbBox3f;

import java.util.Random;

public abstract class Owls implements Target {

    public static final String SPOTTED_OWL_NAME = "Spotted Owl";

    public static final String BARRED_OWL_NAME = "Barred Owl";

    @Override
    public abstract String getTexturePath();

    @Override
    public int getNbTargets() {

        if( nbOwls == 0 ) {
            compute();
        }
        return nbOwls;
    }

    @Override
    public float[] getTarget(int owlIndex, float[] vector) {

        if (nbOwls == 0) {
            compute();
        }

        vector[0] = owlCoords.get(owlIndex*3);
        vector[1] = owlCoords.get(owlIndex*3+1);
        vector[2] = owlCoords.get(owlIndex*3+2);

        return vector;
    }

    @Override
    public float getSize() {
        return 0.7f;
    }

    @Override
    public abstract float getRatio();

    @Override
    public float getViewDistance() {
        return 500;
    }

    SceneGraphIndexedFaceSetShader sg;

    int nbOwls = 0;

    SbListFloat owlCoords = new SbListFloat();

    final int THIRTY_THOUSAND = 30000;

    int NB_OWL_BIRTHS = THIRTY_THOUSAND;

    final int SEED_OWL_PLACEMENT;

    public Owls( SceneGraphIndexedFaceSetShader sg, int seed ) {
        this.sg = sg;
        SEED_OWL_PLACEMENT = seed;
    }

    private void compute() {

        Random randomPlacementOwls = new Random(SEED_OWL_PLACEMENT);

        int[] indices = new int[4];

        float zWater = - 150 + sg.getzTranslation() - sg.CUBE_DEPTH/2;

        for( int i = 0; i < NB_OWL_BIRTHS; i++) {
            float x = getRandomX(randomPlacementOwls);
            float y = getRandomY(randomPlacementOwls);
            float z = sg.getInternalZ(x,y,indices) + sg.getzTranslation();

            //boolean isNearWater = Math.abs(z - zWater) < 150;
            boolean isAboveWater = z > zWater;
            boolean isNotInSnow = z - zWater < 2000;
/*
            float z1 = sg.getInternalZ(x+0.5f,y,indices) + sg.getzTranslation();
            float z2 = sg.getInternalZ(x-0.5f,y,indices) + sg.getzTranslation();
            float z3 = sg.getInternalZ(x,y+0.5f,indices) + sg.getzTranslation();
            float z4 = sg.getInternalZ(x,y-0.5f,indices) + sg.getzTranslation();

 */
            /*
            float d1 = Math.abs(z-z1);
            float d2 = Math.abs(z-z2);
            float d3 = Math.abs(z-z3);
            float d4 = Math.abs(z-z4);
            float dzMax = 0.35f;
             */
            //boolean isNotTooSteep = (d1<dzMax) && (d2<dzMax) && (d3<dzMax) && (d4<dzMax);
            float altitude = 2.0f + 20.0f*(float)Math.pow(randomPlacementOwls.nextFloat(),1.5f);

            if( /*!isNearWater &&*/ isAboveWater /*&& isNotTooSteep */&& isNotInSnow ) {
                owlCoords.append(x);
                owlCoords.append(y);
                owlCoords.append(z+altitude);

                nbOwls++;
            }
        }
    }

    float getRandomX(Random randomPlacementOwl) {
        SbBox3f sceneBox = sg.getChunks().getSceneBoxFullIsland();
        float xMin = sceneBox.getBounds()[0];
        float xMax = sceneBox.getBounds()[3];
        return xMin + (xMax - xMin) * randomPlacementOwl.nextFloat();
    }

    float getRandomY(Random randomPlacementOwl) {
        SbBox3f sceneBox = sg.getChunks().getSceneBoxFullIsland();
        float yMin = sceneBox.getBounds()[1];
        float yMax = sceneBox.getBounds()[4];
        return yMin + (yMax - yMin) * randomPlacementOwl.nextFloat();
    }
}
