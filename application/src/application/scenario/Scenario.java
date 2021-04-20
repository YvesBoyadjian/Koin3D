package application.scenario;

import application.scenegraph.SceneGraphIndexedFaceSetShader;
import application.viewer.glfw.SoQtWalkViewer;

import java.util.ArrayList;
import java.util.List;

public class Scenario {

    SceneGraphIndexedFaceSetShader sceneGraph;

    final List<Quest> quests = new ArrayList<>();

    Quest currentQuest;

    boolean over;

    public Scenario(SceneGraphIndexedFaceSetShader sceneGraph){
        this.sceneGraph = sceneGraph;
    }

    public void addQuest(Quest quest) {
        quest.setSceneGraph(sceneGraph);
        quests.add(quest);
    }

    public void start() {
        if(!quests.isEmpty()) {
            currentQuest = quests.get(0);
        }
        else {
            over = true;
        }
    }

    public boolean idle(SoQtWalkViewer viewer) {

        Quest thisQuest = currentQuest;

        if( !over && currentQuest.isAchieved(viewer) ) {
            int currentQuestIndex = quests.indexOf(currentQuest);
            if(currentQuestIndex >= 0 && currentQuestIndex < quests.size()-1) {
                currentQuest = quests.get(currentQuestIndex+1);
            }
            else {
                over = true;
            }
            if(!idle(viewer)) {
                thisQuest.actionIfNextNotAchieved();
            }
            return true;
        }
        return false;
    }
}
