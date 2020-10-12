package vrmlviewer;

import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.SoInputFile;
import jscenegraph.database.inventor.nodes.*;
import jsceneviewerawt.inventor.qt.SoQt;
import jsceneviewerawt.inventor.qt.SoQtCameraController;
import jsceneviewerawt.inventor.qt.viewers.SoQtExaminerViewer;
import jsceneviewerawt.inventor.qt.viewers.SoQtFullViewer;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.List;

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

    //SoCone cube = new SoCone();

    //String path = "C:/Users/Yves Boyadjian/Downloads/83_honda_atc.wrl";
    String path = "C:/Users/Yves Boyadjian/Downloads/doom-combat-scene_wrl/doom combat scene.wrl";

    SoSeparator cache = new SoSeparator();

    cache.ref();

    cache.renderCaching.setValue(SoSeparator.CacheEnabled.ON);

    SoText3 text = new SoText3();
    text.string.setValue("Drag an Drop your WRL file here");

    cache.addChild(text);

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

        examinerViewer.setDropTarget(new DropTarget() {
            public synchronized void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    List<File> droppedFiles = (List<File>)
                            evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    cache.removeAllChildren();
                    for (File file : droppedFiles) {
                        SoFile input = new SoFile();
                        input.name.setValue(file.toString());

                        cache.addChild(input);
                        examinerViewer.viewAll();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        });
    });
}
}
