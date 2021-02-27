package application.swt;


import java.awt.AWTException;
import java.awt.Robot;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.lwjgl.opengl.swt.GLCanvas;
import com.jogamp.opengl.GL2;

import jscenegraph.coin3d.inventor.SbVec2i32;
import jscenegraph.database.inventor.SbRotation;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.events.SoEvent;
import jscenegraph.database.inventor.events.SoKeyboardEvent;
import jscenegraph.database.inventor.events.SoLocation2Event;
import jscenegraph.database.inventor.nodes.SoCamera;
import jscenegraph.database.inventor.sensors.SoIdleSensor;
import jscenegraph.database.inventor.sensors.SoSensor;
import jsceneviewer.inventor.qt.SoQtCameraController;
import jsceneviewer.inventor.qt.SoQtCameraController.Type;
import jsceneviewer.inventor.qt.viewers.SoQtConstrainedViewer;
import jsceneviewer.inventor.qt.viewers.SoQtFullViewer;

/**
 * @author Yves Boyadjian
 *
 */
public class SoQtWalkViewer extends SoQtConstrainedViewer {

    private final static float GOD = 1000;

    private final static float JET = 260;

    private final static float AIRPLANE = 100;

    private final static float USAIN_BOLT_RUN = 10;

    private final static float SPEED = GOD;

    final SbVec2i32 old_position = new SbVec2i32();

    protected float sensitivity = 1;
    protected boolean invert = false;

    private double lastTimeSec = System.nanoTime()/1.0e9;

    private Set<SoKeyboardEvent.Key> keysDown = new HashSet<>();

    private List<Consumer<application.swt.SoQtWalkViewer>> idleListeners = new ArrayList<>();

    private SoIdleSensor idleSensor = new SoIdleSensor(application.swt.SoQtWalkViewer::idleCB,this);

    public SoQtWalkViewer(SoQtFullViewer.BuildFlag flag, SoQtCameraController.Type type, Composite parent, int f) {
        super(flag, type, parent, f);
        setKeyCommandsEnabled(false);
    }

    public void buildWidget(int style) {
        super.buildWidget(style);

        getDeviceWidget().
                addMouseMoveListener(new MouseMoveListener() {

                    @Override
                    public void mouseMove(MouseEvent e) {
                        if(focus)
                            processMouseMoveEvent(/*e*/);
                    }

                });
        old_position.copyFrom(getCursorPosition());


    }

    final SbVec2i32 diff = new SbVec2i32();

    protected void processMouseMoveEvent(/*MouseEvent e*/) {

        /* Zjisteni zmeny pozice kurzoru. */
        final SbVec2i32 position = getCursorPosition();

        diff.operator_add_equal(old_position.operator_minus(position));

        //old_position.copyFrom( getPosition().operator_add( getCenter()));
        old_position.copyFrom( position );
        //setCursorPosition(old_position); //YB
        //idle();
    }

    protected void moveCamera() {

        SoCamera  camera = getCameraController().getCamera();

        float rotation_x = sensitivity * 0.001f * diff.getValue()[1];
        rotation_x = invert ? -rotation_x : rotation_x;
        float rotation_z = sensitivity * 0.001f * diff.getValue()[0];

        diff.setValue((int)0, (int)0);

        /* Rotace v X ose. */
        camera.orientation.setValue( new SbRotation(new SbVec3f(1.0f, 0.0f, 0.0f), rotation_x).operator_mul(
                camera.orientation.getValue()));

        /* Rotace v Z ose. */
        camera.orientation.setValue( camera.orientation.getValue().operator_mul(
                new SbRotation(new SbVec3f(0.0f, 0.0f, 1.0f), rotation_z)));


    }

    private SbVec2i32 getCenter()
    {
        /* Ziskani stredu okna relativne. */
        Composite widget = getParentWidget();
        return new SbVec2i32((int)(widget.getSize().x / 2), (int)(widget.getSize().y / 2));
    }

    private SbVec2i32 getPosition()
    {
        /* Ziskani pocatku okna vuci obrazovce. */
        Composite widget = getParentWidget();
        Point position = widget.toDisplay(0, 0);
        return new SbVec2i32((int)position.x, (int)position.y);
    }

    private void setCursorPosition(final SbVec2i32 position)
    {
        /* Nastaveni absolutni pozice kurzoru. */
        try {
            new Robot().mouseMove(position.getValue()[0], position.getValue()[1]);
        } catch (AWTException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private SbVec2i32 getCursorPosition()
    {
        /* Ziskani absolutni pozice kurzoru. */
        //QPoint position = QCursor.pos();
        Point position = Display.getCurrent().getCursorLocation();
        return new SbVec2i32(position.x, position.y);
    }

    protected boolean processSoKeyboardEvent( SoKeyboardEvent event)
    {
        //double currentTimeSec = System.nanoTime()/1.0e9;

        //float deltaT = (float)(currentTimeSec - lastTimeSec);
        //if(deltaT > 1.0f) {
        //deltaT = 1.0f;
        //}
        //System.out.println("processSoKeyboardEvent");

        if (SoKeyboardEvent.SO_KEY_RELEASE_EVENT(event, SoKeyboardEvent.Key.ESCAPE))
        {
            System.exit(0);
            return true;
        }
        else if (SoKeyboardEvent.SO_KEY_PRESS_EVENT(event, SoKeyboardEvent.Key.Z) ||
                SoKeyboardEvent.SO_KEY_PRESS_EVENT(event, SoKeyboardEvent.Key.UP_ARROW))
        {
            if(!keysDown.contains(SoKeyboardEvent.Key.Z)) {
                lastTimeSec = System.nanoTime()/1.0e9;
                keysDown.add(SoKeyboardEvent.Key.Z);
            }
            return true;
        }
        else if (SoKeyboardEvent.SO_KEY_PRESS_EVENT(event, SoKeyboardEvent.Key.S) ||
                SoKeyboardEvent.SO_KEY_PRESS_EVENT(event, SoKeyboardEvent.Key.DOWN_ARROW))
        {
            //lastTimeSec = System.nanoTime()/1.0e9;
            if(!keysDown.contains(SoKeyboardEvent.Key.S)) {
                lastTimeSec = System.nanoTime()/1.0e9;
                keysDown.add(SoKeyboardEvent.Key.S);
            }
            return true;
        }
        else if (SoKeyboardEvent.SO_KEY_PRESS_EVENT(event, SoKeyboardEvent.Key.Q) ||
                SoKeyboardEvent.SO_KEY_PRESS_EVENT(event, SoKeyboardEvent.Key.LEFT_ARROW))
        {
            //lastTimeSec = System.nanoTime()/1.0e9;
            if(!keysDown.contains(SoKeyboardEvent.Key.Q)) {
                lastTimeSec = System.nanoTime()/1.0e9;
                keysDown.add(SoKeyboardEvent.Key.Q);
            }
            return true;
        }
        else if (SoKeyboardEvent.SO_KEY_PRESS_EVENT(event, SoKeyboardEvent.Key.D) ||
                SoKeyboardEvent.SO_KEY_PRESS_EVENT(event, SoKeyboardEvent.Key.RIGHT_ARROW))
        {
            //lastTimeSec = System.nanoTime()/1.0e9;
            if(!keysDown.contains(SoKeyboardEvent.Key.D)) {
                lastTimeSec = System.nanoTime()/1.0e9;
                keysDown.add(SoKeyboardEvent.Key.D);
            }
            return true;
        }
        else if (SoKeyboardEvent.SO_KEY_RELEASE_EVENT(event, SoKeyboardEvent.Key.Z) ||
                SoKeyboardEvent.SO_KEY_RELEASE_EVENT(event, SoKeyboardEvent.Key.UP_ARROW)) {
            keysDown.remove(SoKeyboardEvent.Key.Z);
            return true;

        }
        else if(
                SoKeyboardEvent.SO_KEY_RELEASE_EVENT(event, SoKeyboardEvent.Key.S) ||
                        SoKeyboardEvent.SO_KEY_RELEASE_EVENT(event, SoKeyboardEvent.Key.DOWN_ARROW)) {
            keysDown.remove(SoKeyboardEvent.Key.S);
            return true;
        }
        else if(
                SoKeyboardEvent.SO_KEY_RELEASE_EVENT(event, SoKeyboardEvent.Key.Q) ||
                        SoKeyboardEvent.SO_KEY_RELEASE_EVENT(event, SoKeyboardEvent.Key.LEFT_ARROW)) {
            keysDown.remove(SoKeyboardEvent.Key.Q);
            return true;
        }
        else if(
                SoKeyboardEvent.SO_KEY_RELEASE_EVENT(event, SoKeyboardEvent.Key.D) ||
                        SoKeyboardEvent.SO_KEY_RELEASE_EVENT(event, SoKeyboardEvent.Key.RIGHT_ARROW)) {
            keysDown.remove(SoKeyboardEvent.Key.D);
            return true;
        }
        return false;
    }

    private void updateLocation(SbVec3f diff_position) {

        double currentTimeSec = System.nanoTime()/1.0e9;

        SoCamera camera = getCameraController().getCamera();
        SbVec3f old_position = camera.position.getValue();
        SbRotation orientation = camera.orientation.getValue();
        orientation.multVec(diff_position, diff_position);
        camera.position.setValue( old_position.operator_add( diff_position));
        getCameraController().changeCameraValues(camera);

        lastTimeSec = currentTimeSec;

    }

    protected boolean processSoLocation2Event( SoLocation2Event _event)
    {
        focus = true;
        //processMouseMoveEvent(/*e*/);
        //  SoCamera  camera = getCameraController().getCamera();

//	  System.out.println("locationmouse");
        //getDeviceWidget().setFocus();

        return true;
    }

    protected boolean processSoEvent( SoEvent event)
    {
        boolean result = false;

        if (event.isOfType(SoLocation2Event.getClassTypeId()))
        {
            result = processSoLocation2Event((SoLocation2Event)
                    (event));
        }
        else if (event.isOfType(SoKeyboardEvent.getClassTypeId()))
        {
            result = processSoKeyboardEvent((SoKeyboardEvent)
                    (event));
        }

        if (!result)
        {
            setViewing(false);
            result = super.processSoEvent(event);
            setViewing(true);
        }

        return result;
    }

    public static void idleCB(Object data, SoSensor sensor) {
        application.swt.SoQtWalkViewer viewer = (application.swt.SoQtWalkViewer)data;
        viewer.idle();
        //System.out.println("idle");
        //viewer.getSceneHandler().getSceneGraph().touch();
        //Display.getCurrent().asyncExec(()->sensor.schedule());
        sensor.schedule();
    }

    public void idle() {

        moveCamera();

        double currentTimeSec = System.nanoTime()/1.0e9;

        float deltaT = (float)(currentTimeSec - lastTimeSec);
        if(deltaT > 1.0f) {
            deltaT = 1.0f;
        }

        if (
                keysDown.contains(SoKeyboardEvent.Key.Z)) {

            //lastTimeSec = System.nanoTime()/1.0e9;


            updateLocation(new SbVec3f(0.0f, 0.0f, -SPEED* deltaT));
        }
        if (

                keysDown.contains(SoKeyboardEvent.Key.S)) {

            //  lastTimeSec = System.nanoTime()/1.0e9;

            updateLocation(new SbVec3f(0.0f, 0.0f, SPEED* deltaT));

        }
        if (  keysDown.contains(SoKeyboardEvent.Key.Q)) {

            //lastTimeSec = System.nanoTime()/1.0e9;

            updateLocation(new SbVec3f(- SPEED* deltaT, 0.0f, 0.0f));

        }
        if (  keysDown.contains(SoKeyboardEvent.Key.D)) {

            //lastTimeSec = System.nanoTime()/1.0e9;

            updateLocation( new SbVec3f(SPEED* deltaT, 0.0f, 0.0f));

        }

        idleListeners.forEach((item)->item.accept(this));
    }

    boolean focus;

    public boolean setFocus() {
        super.setFocus();
        return getGLWidget().setFocus();
    }

    protected void paintGL(GL2 gl2) {

        //idle();
        super.paintGL(gl2);
    }

//    public void actualRedraw()
//    {
//    	idle();
//    	super.actualRedraw();
//    }

    public void addIdleListener(Consumer listener) {
        idleListeners.add(listener);
    }

    public void start() {
        idleSensor.schedule();
    }
}
