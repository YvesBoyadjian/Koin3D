package vrmlviewer;

import jsceneviewerglfw.Display;
import jsceneviewerglfw.inventor.qt.SoQt;
import jsceneviewerglfw.inventor.qt.viewers.SoQtExaminerViewer;
import jsceneviewerglfw.inventor.qt.viewers.SoQtFullViewer;

public class VRMLViewer {

public static void main(String[] args) {

    Display display = new Display();

    SoQt.init("VRMLViewer");

    SoQtExaminerViewer examinerViewer = new SoQtExaminerViewer(
            SoQtFullViewer.BuildFlag.BUILD_ALL
    );

    examinerViewer.buildWidget(0);

    display.loop();

    display.dispose();
}
}
