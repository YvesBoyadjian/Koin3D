package vrmlviewer;

import com.jogamp.opengl.GL2;
import jscenegraph.coin3d.inventor.VRMLnodes.SoVRMLImageTexture;
import jscenegraph.database.inventor.SbColor;
import jscenegraph.database.inventor.SbViewportRegion;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.SoInputFile;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.nodes.*;
import jscenegraph.port.Ctx;
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
    JFrame frame = new JFrame("VRMLViewer");
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

    SbViewportRegion.setDefaultPixelsPerInch(7.20f);

    SoVRMLImageTexture.setDelayFetchURL(false); // Don't wait to load textures

    SoSeparator cache = new SoSeparator();

    cache.ref();


    SoText3 text = new SoText3();
    text.string.setValue("Drag an drop your iv/wrl/zip file here");

    cache.addChild(text);

    SwingUtilities.invokeLater(() -> {
        SoQtExaminerViewer examinerViewer = new SoQtExaminerViewer(
                SoQtFullViewer.BuildFlag.BUILD_ALL,
                SoQtCameraController.Type.BROWSER,
                /*panel*/frame.getContentPane()
        );

        //examinerViewer.setAntialiasing(true, 15);
        examinerViewer.getSceneHandler().setTransparencyType(SoGLRenderAction.TransparencyType.DELAYED_BLEND);
        //examinerViewer.getSceneHandler().setTransparencyType(SoGLRenderAction.TransparencyType.SORTED_LAYERS_BLEND); still bugs with villa Savoye

        examinerViewer.getSceneHandler().setBackgroundColor(new SbColor(0.6f,0.535f,0.28f));

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

//                    SoCallback callback = new SoCallback();
//
//                    callback.setCallback(action -> {
//                        if(action instanceof SoGLRenderAction) {
//                            SoGLRenderAction glRenderAction = (SoGLRenderAction)action;
//                            GL2 gl2 = Ctx.get(glRenderAction.getCacheContext());
//                            gl2.glEnable(GL2.GL_FRAMEBUFFER_SRGB);
//                            gl2.glLightModeli(GL2.GL_LIGHT_MODEL_LOCAL_VIEWER, GL2.GL_TRUE);
//                        }
//                    });
//                    cache.addChild(callback);

                    String title = "";

                    for (File file : droppedFiles) {
                        SoFile input = new SoFile();
                        input.name.setValue(file.toString());

                        if(file.toString().endsWith(".iv")) {
                            cache.renderCaching.setValue(SoSeparator.CacheEnabled.AUTO);
                        }
                        else {
                            cache.renderCaching.setValue(SoSeparator.CacheEnabled.ON);
                        }

                        cache.addChild(input);
                        examinerViewer.viewAll();

                        title += file.getName()+" ";
                    }
                    frame.setTitle(title);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        });
    });
}
}
