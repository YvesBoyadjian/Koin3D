package application.scenario;

import application.MainGLFW;
import application.scenegraph.*;
import application.viewer.glfw.SoQtWalkViewer;
import jscenegraph.database.inventor.nodes.SoCamera;

public class TargetsKillingQuest implements Quest {

    SceneGraphIndexedFaceSetShader sceneGraph;

    @Override
    public void setSceneGraph(SceneGraphIndexedFaceSetShader sceneGraph) {
        this.sceneGraph = sceneGraph;
    }

    @Override
    public boolean isAchieved(SoQtWalkViewer viewer) {
        boolean achieved = getDistanceFromOracle(viewer) <= 4.5;
        achieved &= sceneGraph.haveShot(GroundSquirrels.GROUND_SQUIRREL_NAME);
        achieved &= sceneGraph.haveShot(HoaryMarmots.HOARY_MARMOT_NAME);
        achieved &= sceneGraph.haveShot(Seals.SEAL_NAME);
        achieved &= sceneGraph.haveShot(MountainGoats.MOUNTAIN_GOAT_NAME);
        achieved &= sceneGraph.haveShot(Owls.SPOTTED_OWL_NAME);
        achieved &= sceneGraph.haveShot(Owls.BARRED_OWL_NAME);
        achieved &= sceneGraph.haveShot(BigFoots.BIGFOOT_NAME);
        return achieved;
    }

    @Override
    public void actionIfNextNotAchieved(SoQtWalkViewer viewer) {
        viewer.setAllowToggleFly(true);
        String[] speech = {"Hooray, I now have enough to eat.","To show my gratitude, I'm allowing you to fly", "by pressing the 'F' key."};
        sceneGraph.talk(speech);
    }

    double getDistanceFromOracle(SoQtWalkViewer viewer) {
        SoCamera camera = viewer.getCameraController().getCamera();
        float x = camera.position.getValue().x();
        float y = camera.position.getValue().y();
        float z = camera.position.getValue().z() + MainGLFW.Z_TRANSLATION;

        float xOracle = SceneGraphIndexedFaceSetShader.ORACLE_X;
        float yOracle = SceneGraphIndexedFaceSetShader.ORACLE_Y;
        float zOracle = SceneGraphIndexedFaceSetShader.ORACLE_Z;

        return Math.sqrt(Math.pow(x-xOracle,2)+Math.pow(y-yOracle,2)+Math.pow(z-zOracle,2));
    }
}
