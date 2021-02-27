package jsceneviewerawt;

import com.jogamp.opengl.GL2;
import jsceneviewerawt.inventor.qt.SoQtGLWidget;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.awt.AWTGLCanvas;
import org.lwjgl.opengl.awt.GLData;

import javax.swing.*;
import java.awt.*;

public class QGLWidget extends AWTGLCanvas {

    SoQtGLWidget _w;

    GL2 gl2 = new GL2(){};

    public QGLWidget(SoQtGLWidget parent, GLData data) {
        super(data);
        _w = parent;
    }

    @Override
    public void initGL() {
        GL.createCapabilities();
        _w.initializeGL(gl2);
    }

    @Override
    public void paintGL() {
        _w.paintGL(gl2);
        if(_w.autoBufferSwap()) {
            swapBuffers();
        }
    }

    @Override
    public void repaint() {
        if (SwingUtilities.isEventDispatchThread()) {
            render();
        } else {
            SwingUtilities.invokeLater(() -> render());
        }
    }


    /* override and return false on components that DO NOT require
       a clearRect() before painting (i.e. native components) */
    public boolean shouldClearRectBeforePaint() {
        return false;
    }

    public void paint(Graphics g) {
        render();
    }

    public GLData format() {
        return effective;
    }
}
