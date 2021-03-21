package application.gui;

import application.scenegraph.SceneGraphIndexedFaceSetShader;
import application.viewer.glfw.SoQtWalkViewer;

import javax.swing.*;
import java.awt.event.*;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class OptionDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JSpinner spinnerShadowgroup;
    private JSpinner spinnerLODFactor;
    private JSpinner spinnerLODFactorShadow;
    private JSpinner spinnerTreeDistance;
    private JSpinner spinnerTreeShadowDistance;
    private JCheckBox volumetricSkyCheckBox;
    private JCheckBox displayFPSCheckBox;
    private JSpinner spinnerMaxI;
    SoQtWalkViewer viewer;
    SceneGraphIndexedFaceSetShader sg;

    public OptionDialog(SoQtWalkViewer viewer, SceneGraphIndexedFaceSetShader sg) {
        setTitle("Game options");
        setContentPane(contentPane);
        //setModal(true);
        getRootPane().setDefaultButton(buttonCancel);

        this.viewer = viewer;
        this.sg = sg;

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here
        //dispose();
        setVisible(false);
        apply();
        if(viewer.isTimeStop()) {
            viewer.toggleTimeStop();
        }
        viewer.setVisible(true);
        viewer.setFocus();
    }

    private void onCancel() {
        // add your code here if necessary
        setVisible(false);//dispose(); //dispose() triggers a crash on linux, at least on Ubuntu
        apply();
        if(viewer.isTimeStop()) {
            viewer.toggleTimeStop();
        }
        viewer.onClose();
        glfwSetWindowShouldClose(viewer.getGLWidget().getWindow(), true);
    }

    private void apply() {
        //sg.enableNotifySun();
        sg.getShadowGroup().precision.setValue((float)((double)((Double)((SpinnerNumberModel) spinnerShadowgroup.getModel()).getNumber())));
        sg.setLevelOfDetail((float)((double)((Double)((SpinnerNumberModel)spinnerLODFactor.getModel()).getNumber())));
        sg.setLevelOfDetailShadow((float)((double)((Double)((SpinnerNumberModel)spinnerLODFactorShadow.getModel()).getNumber())));
        sg.setTreeDistance((float)((double)((Double)((SpinnerNumberModel)spinnerTreeDistance.getModel()).getNumber())));
        sg.setTreeShadowDistance((float)((double)((Double)((SpinnerNumberModel)spinnerTreeShadowDistance.getModel()).getNumber())));
        sg.setMaxI(((int)((Integer)((SpinnerNumberModel)spinnerMaxI.getModel()).getNumber())));
        sg.getShadowGroup().isVolumetricActive.setValue(volumetricSkyCheckBox.getModel().isSelected());
        sg.enableFPS(displayFPSCheckBox.getModel().isSelected());
        //sg.disableNotifySun();
    }

    public void setVisible(boolean b) {
        if(b) {
            // SHADOW_PRECISION
            spinnerShadowgroup.setModel(new SpinnerNumberModel((double)sg.getShadowGroup().precision.getValue(),0.05,1.0,0.05));
            // LOD_FACTOR
            spinnerLODFactor.setModel(new SpinnerNumberModel((double)sg.getLevelOfDetail(),0.05,2.0,0.05));
            // LOD_FACTOR_SHADOW
            spinnerLODFactorShadow.setModel(new SpinnerNumberModel((double)sg.getLevelOfDetailShadow(),0.05,2.0,0.05));
            // TREE_DISTANCE
            spinnerTreeDistance.setModel(new SpinnerNumberModel((double)sg.getTreeDistance(),500,30000,500));
            // TREE_SHADOW_DISTANCE
            spinnerTreeShadowDistance.setModel(new SpinnerNumberModel((double)sg.getTreeShadowDistance(),500,30000,500));
            // MAX_I
            spinnerMaxI.setModel(new SpinnerNumberModel(sg.getMaxI(),7000,14000,500));
            // VOLUMETRIC_SKY
            volumetricSkyCheckBox.getModel().setSelected(sg.getShadowGroup().isVolumetricActive.getValue());
            // DISPLAY_FPS
            displayFPSCheckBox.getModel().setSelected(sg.isFPSEnabled());
        }
        super.setVisible(b);
    }

    public static void main(String[] args) {
        OptionDialog dialog = new OptionDialog(null,null);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}