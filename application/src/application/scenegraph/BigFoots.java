package application.scenegraph;

import application.objects.Target;
import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.fields.SoMFVec3f;

import java.util.Random;

public class BigFoots implements Target {
    @Override
    public String getTexturePath() {
        return "ressource/bf.jpg";
    }

    @Override
    public int getNbTargets() {

        if( nbBigFoots == 0 ) {
            compute();
        }
        return nbBigFoots;
    }

    @Override
    public float[] getTarget(int bigFootIndex, float[] vector) {

        if (nbBigFoots == 0) {
            compute();
        }

        SbVec3f oneBigFootCoords = bigFootCoords.getValueAt(bigFootIndex);
        vector[0] = oneBigFootCoords.getX();
        vector[1] = oneBigFootCoords.getY();
        vector[2] = oneBigFootCoords.getZ();

        return vector;
    }

    @Override
    public float getSize() {
        return 2.3f;
    }

    @Override
    public float getRatio() {
        return 1;
    }

    SceneGraphIndexedFaceSetShader sg;

    int nbBigFoots = 0;

    SoMFVec3f bigFootCoords = new SoMFVec3f();

    final int TEN_THOUSAND = 10000;

    int NB_BIGFOOT_BIRTHS = TEN_THOUSAND;

    final static int SEED_BIGFOOT_PLACEMENT = 49;

    public BigFoots( SceneGraphIndexedFaceSetShader sg ) {
        this.sg = sg;
    }

    private void compute() {

        Random randomPlacementBigFoots = new Random(SEED_BIGFOOT_PLACEMENT);

        int[] indices = new int[4];

        float zWater = - 150 + sg.getzTranslation() - sg.CUBE_DEPTH/2;

        float[] xyz = new float[3];
        int start;

        for( int i = 0; i < NB_BIGFOOT_BIRTHS; i++) {
            float x = getRandomX(randomPlacementBigFoots);
            float y = getRandomY(randomPlacementBigFoots);
            float z = sg.getInternalZ(x,y,indices) + sg.getzTranslation();

            boolean isNearWater = Math.abs(z - zWater) < 15;
            boolean isAboveWater = z > zWater;

            float z1 = sg.getInternalZ(x+0.5f,y,indices) + sg.getzTranslation();
            float z2 = sg.getInternalZ(x-0.5f,y,indices) + sg.getzTranslation();
            float z3 = sg.getInternalZ(x,y+0.5f,indices) + sg.getzTranslation();
            float z4 = sg.getInternalZ(x,y-0.5f,indices) + sg.getzTranslation();
            float d1 = Math.abs(z-z1);
            float d2 = Math.abs(z-z2);
            float d3 = Math.abs(z-z3);
            float d4 = Math.abs(z-z4);
            float dzMax = 0.3f;
            boolean isNotTooSteep = (d1<dzMax) && (d2<dzMax) && (d3<dzMax) && (d4<dzMax);

            if( !isNearWater && isAboveWater && isNotTooSteep ) {
                xyz[0] = x;
                xyz[1] = y;
                xyz[2] = z + 0.65f;
                start = bigFootCoords.getNum();
                bigFootCoords.setValues(start, xyz);

                nbBigFoots++;
            }
        }
    }

    float getRandomX(Random randomPlacementTrees) {
        SbBox3f sceneBox = sg.getChunks().getSceneBoxFullIsland();
        float xMin = sceneBox.getBounds()[0];
        float xMax = sceneBox.getBounds()[3];
        return xMin + (xMax - xMin) * randomPlacementTrees.nextFloat();
    }

    float getRandomY(Random randomPlacementTrees) {
        SbBox3f sceneBox = sg.getChunks().getSceneBoxFullIsland();
        float yMin = sceneBox.getBounds()[1];
        float yMax = sceneBox.getBounds()[4];
        return yMin + (yMax - yMin) * randomPlacementTrees.nextFloat();
    }
}
