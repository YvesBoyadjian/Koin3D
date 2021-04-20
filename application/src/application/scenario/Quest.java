package application.scenario;

import application.scenegraph.SceneGraphIndexedFaceSetShader;
import application.viewer.glfw.SoQtWalkViewer;

public interface Quest {
    void setSceneGraph(SceneGraphIndexedFaceSetShader sceneGraph);
    boolean isAchieved(SoQtWalkViewer viewer);
    void actionIfNextNotAchieved();
}
