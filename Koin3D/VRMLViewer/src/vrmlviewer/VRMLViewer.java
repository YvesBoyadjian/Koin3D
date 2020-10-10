package vrmlviewer;

import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.SoInputFile;
import jscenegraph.database.inventor.nodes.SoCone;
import jscenegraph.database.inventor.nodes.SoCube;
import jscenegraph.database.inventor.nodes.SoFile;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jsceneviewerawt.inventor.qt.SoQt;
import jsceneviewerawt.inventor.qt.SoQtCameraController;
import jsceneviewerawt.inventor.qt.viewers.SoQtExaminerViewer;
import jsceneviewerawt.inventor.qt.viewers.SoQtFullViewer;

import javax.swing.*;
import java.awt.*;

public class VRMLViewer {

public static void main(String[] args) {

    SoQt.init("VRMLViewer");

    //JPanel panel = new JPanel();
    JFrame frame = new JFrame("Frame");
    frame.getContentPane().setBackground(new Color(0,true));
    //frame.getContentPane().add(panel);
    frame.getContentPane().setLayout(new BorderLayout());
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    frame.pack();
    frame.setLocationRelativeTo(null);
//    frame.setVisible(true);

    SoCone cube = new SoCone();

    //String path = "C:/Users/Yves Boyadjian/Downloads/83_honda_atc.wrl";
    String path = "C:/Users/Yves Boyadjian/Downloads/doom-combat-scene_wrl/doom combat scene.wrl";

    SoSeparator cache = new SoSeparator();
    cache.renderCaching.setValue(SoSeparator.CacheEnabled.ON);

    SoFile input = new SoFile();
    input.name.setValue(path);

    cache.addChild(input);

    SwingUtilities.invokeLater(() -> {
        SoQtExaminerViewer examinerViewer = new SoQtExaminerViewer(
                SoQtFullViewer.BuildFlag.BUILD_ALL,
                SoQtCameraController.Type.BROWSER,
                /*panel*/frame.getContentPane()
        );

        examinerViewer.buildWidget(0);

        frame.pack();
        frame.setSize(800,600);
        frame.setVisible(true);

        examinerViewer.setSceneGraph(cache);

    });
}
}
