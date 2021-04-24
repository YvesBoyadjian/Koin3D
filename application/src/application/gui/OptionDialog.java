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
    private JButton buttonNew;
    private JButton lowButton;
    private JButton mediumButton;
    private JButton highButton;
    private JButton ultraButton;
    private JButton lowestButton;
    private JButton extremeButton;
    SoQtWalkViewer viewer;
    SceneGraphIndexedFaceSetShader sg;

    public OptionDialog(SoQtWalkViewer viewer, SceneGraphIndexedFaceSetShader sg) {
        setTitle("Game options");
        setContentPane(contentPane);
        //setModal(true);
        getRootPane().setDefaultButton(buttonCancel);

        this.viewer = viewer;
        this.sg = sg;

        buttonNew.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onLeaveToNew();
            }
        });

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

        lowestButton.addActionListener((e)->onLowest());
        lowButton.addActionListener((e)->onLow());
        mediumButton.addActionListener((e)->onMedium());
        highButton.addActionListener((e)->onHigh());
        ultraButton.addActionListener((e)->onUltra());
        extremeButton.addActionListener((e)->onExtreme());

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
        viewer.onClose(false);
        glfwSetWindowShouldClose(viewer.getGLWidget().getWindow(), true);
    }

    private void onLeaveToNew() {
        // add your code here
        //dispose();
        setVisible(false);
        apply();
        if(viewer.isTimeStop()) {
            viewer.toggleTimeStop();
        }
        viewer.onClose(true);
        glfwSetWindowShouldClose(viewer.getGLWidget().getWindow(), true);
    }

    private void onLowest() {
        setShadowPrecision(0.05);
        setLODFactor(0.05);
        setLODFactorShadow(0.05);
        setTreeDistance(500);
        setTreeShadowDistance(500);
        setIslandDepth(7000);
        setVolumetricSky(false);
    }

    private void onLow() {
        setShadowPrecision(0.25);
        setLODFactor(0.2);
        setLODFactorShadow(0.2);
        setTreeDistance(1500);
        setTreeShadowDistance(500);
        setIslandDepth(7000);
        setVolumetricSky(false);
    }

    private void onMedium() {
        setShadowPrecision(0.25);
        setLODFactor(0.5);
        setLODFactorShadow(0.5);
        setTreeDistance(3000);
        setTreeShadowDistance(1500);
        setIslandDepth(7000);
        setVolumetricSky(false);
    }

    private void onHigh() {
        setShadowPrecision(0.3);
        setLODFactor(1.0);
        setLODFactorShadow(1.0);
        setTreeDistance(7000);
        setTreeShadowDistance(3000);
        setIslandDepth(14000);
        setVolumetricSky(false);
    }

    private void onUltra() {
        setShadowPrecision(0.3);
        setLODFactor(1.0);
        setLODFactorShadow(1.0);
        setTreeDistance(7000);
        setTreeShadowDistance(3000);
        setIslandDepth(14000);
        setVolumetricSky(true);
    }

    private void onExtreme() {
        setShadowPrecision(0.4);
        setLODFactor(2.0);
        setLODFactorShadow(2.0);
        setTreeDistance(30000);
        setTreeShadowDistance(30000);
        setIslandDepth(14000);
        setVolumetricSky(true);
    }

    private void apply() {
        //sg.enableNotifySun();
        sg.getShadowGroup().precision.setValue((float)((double)((Double)((SpinnerNumberModel) spinnerShadowgroup.getModel()).getNumber())));
        sg.setLevelOfDetail((float)((double)((Double)((SpinnerNumberModel)spinnerLODFactor.getModel()).getNumber())));
        sg.setLevelOfDetailShadow((float)((double)((Double)((SpinnerNumberModel)spinnerLODFactorShadow.getModel()).getNumber())));
        sg.setTreeDistance((float)((double)((Double)((SpinnerNumberModel)spinnerTreeDistance.getModel()).getNumber())));
        sg.setTreeShadowDistance((float)((double)((Double)((SpinnerNumberModel)spinnerTreeShadowDistance.getModel()).getNumber())));
        sg.setMaxI(((int)((Integer)((SpinnerNumberModel)spinnerMaxI.getModel()).getNumber())));
        boolean volumetric = volumetricSkyCheckBox.getModel().isSelected();
        sg.getShadowGroup().isVolumetricActive.setValue(volumetric);
        sg.getEnvironment().fogColor.setValue(volumetric ? sg.SKY_COLOR.darker().darker().darker().darker().darker().darker() : sg.SKY_COLOR.darker());
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

    public void setShadowPrecision(double precision) {
        spinnerShadowgroup.getModel().setValue(precision);
    }

    public void setLODFactor(double lodFactor) {
        spinnerLODFactor.getModel().setValue(lodFactor);
    }

    public void setLODFactorShadow(double lodFactorShadow) {
        spinnerLODFactorShadow.getModel().setValue(lodFactorShadow);
    }

    public void setTreeDistance(double treeDistance) {
        spinnerTreeDistance.getModel().setValue(treeDistance);
    }

    public void setTreeShadowDistance(double treeShadowDistance) {
        spinnerTreeShadowDistance.getModel().setValue(treeShadowDistance);
    }

    public void setIslandDepth(int islandDepth) {
        spinnerMaxI.getModel().setValue(islandDepth);
    }

    public void setVolumetricSky(boolean volumetricSky) {
        volumetricSkyCheckBox.getModel().setSelected(volumetricSky);
    }

    public static void main(String[] args) {
        OptionDialog dialog = new OptionDialog(null,null);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
