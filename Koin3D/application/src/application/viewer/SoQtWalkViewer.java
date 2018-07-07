/**
 * 
 */
package application.viewer;


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

import com.jogamp.opengl.GL2;

import jscenegraph.database.inventor.SbRotation;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.events.SoEvent;
import jscenegraph.database.inventor.events.SoKeyboardEvent;
import jscenegraph.database.inventor.events.SoLocation2Event;
import jscenegraph.database.inventor.nodes.SoCamera;
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

	final SbVec2s old_position = new SbVec2s();

	protected float sensitivity = 1;
	protected boolean invert = false;
	
	private double lastTimeSec = System.nanoTime()/1.0e9;
	
	private Set<SoKeyboardEvent.Key> keysDown = new HashSet<>();
	
	private List<Consumer<SoQtWalkViewer>> idleListeners = new ArrayList<>();
	
	public SoQtWalkViewer(SoQtFullViewer.BuildFlag flag, SoQtCameraController.Type type, Composite parent, int f) {
		super(flag, type, parent, f);
	}

	public void buildWidget(int style) {
		super.buildWidget(style);
		
		getDeviceWidget().
		addMouseMoveListener(new MouseMoveListener() {

			@Override
			public void mouseMove(MouseEvent e) {
				processMouseMoveEvent(e);
			}
			
		});
		old_position.copyFrom(getCursorPosition());

	}
	
	protected void processMouseMoveEvent(MouseEvent e) {
		
		  SoCamera  camera = getCameraController().getCamera();

		  /* Zjisteni zmeny pozice kurzoru. */
		  final SbVec2s position = getCursorPosition();

		  final SbVec2s diff = old_position.operator_minus(position);

		  float rotation_x = sensitivity * 0.001f * diff.getValue()[1];
		  rotation_x = invert ? -rotation_x : rotation_x;
		  float rotation_z = sensitivity * 0.001f * diff.getValue()[0];

		  /* Rotace v X ose. */
		  camera.orientation.setValue( new SbRotation(new SbVec3f(1.0f, 0.0f, 0.0f), rotation_x).operator_mul(
		    camera.orientation.getValue()));

		  /* Rotace v Z ose. */
		  camera.orientation.setValue( camera.orientation.getValue().operator_mul(
		    new SbRotation(new SbVec3f(0.0f, 0.0f, 1.0f), rotation_z)));

		  old_position.copyFrom( getPosition().operator_add( getCenter()));
		  setCursorPosition(old_position); //YB
		  //idle();
	}
	
	private SbVec2s getCenter()
	{
	  /* Ziskani stredu okna relativne. */
	  Composite widget = getParentWidget();
	  return new SbVec2s((short)(widget.getSize().x / 2), (short)(widget.getSize().y / 2));
	}

	private SbVec2s getPosition()
	{
	  /* Ziskani pocatku okna vuci obrazovce. */
	  Composite widget = getParentWidget();
	  Point position = widget.toDisplay(0, 0);
	  return new SbVec2s((short)position.x, (short)position.y);
	}

	private void setCursorPosition(final SbVec2s position)
	{
	  /* Nastaveni absolutni pozice kurzoru. */
		try {
			new Robot().mouseMove(position.getValue()[0], position.getValue()[1]);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  
	}

	 private SbVec2s getCursorPosition()
	 {
	   /* Ziskani absolutni pozice kurzoru. */
	   //QPoint position = QCursor.pos();
	   Point position = Display.getCurrent().getCursorLocation();		  
	   return new SbVec2s((short)position.x, (short)position.y);
	 }
	 	
	protected boolean processSoKeyboardEvent( SoKeyboardEvent event)
	{
		//double currentTimeSec = System.nanoTime()/1.0e9;
		
		//float deltaT = (float)(currentTimeSec - lastTimeSec);
		//if(deltaT > 1.0f) {
			//deltaT = 1.0f;
		//}
		
		  if (SoKeyboardEvent.SO_KEY_RELEASE_EVENT(event, SoKeyboardEvent.Key.ESCAPE))
		  {
		    System.exit(0);
		    return true;
		  }
		  else if (SoKeyboardEvent.SO_KEY_PRESS_EVENT(event, SoKeyboardEvent.Key.Z))
		  {
			  if(!keysDown.contains(SoKeyboardEvent.Key.Z)) {
				  lastTimeSec = System.nanoTime()/1.0e9;
				  keysDown.add(SoKeyboardEvent.Key.Z);
			  }
			  return true;
		  }
		  else if (SoKeyboardEvent.SO_KEY_PRESS_EVENT(event, SoKeyboardEvent.Key.S))
		  {
			  //lastTimeSec = System.nanoTime()/1.0e9;
			  keysDown.add(SoKeyboardEvent.Key.S);
			  return true;
		  }
		  else if (SoKeyboardEvent.SO_KEY_PRESS_EVENT(event, SoKeyboardEvent.Key.Q))
		  {
			  //lastTimeSec = System.nanoTime()/1.0e9;
			  keysDown.add(SoKeyboardEvent.Key.Q);
			  return true;
		  }
		  else if (SoKeyboardEvent.SO_KEY_PRESS_EVENT(event, SoKeyboardEvent.Key.D))
		  {
			  //lastTimeSec = System.nanoTime()/1.0e9;
			  keysDown.add(SoKeyboardEvent.Key.D);
			  return true;
		  }
		  else if (SoKeyboardEvent.SO_KEY_RELEASE_EVENT(event, SoKeyboardEvent.Key.Z)) {
			  keysDown.remove(SoKeyboardEvent.Key.Z);
			  return true;
			  
		  }
		  else if(
				  SoKeyboardEvent.SO_KEY_RELEASE_EVENT(event, SoKeyboardEvent.Key.S) ) {
			  keysDown.remove(SoKeyboardEvent.Key.S);			  					  
			  return true;
				  }
		  else if(
				  SoKeyboardEvent.SO_KEY_RELEASE_EVENT(event, SoKeyboardEvent.Key.Q) ) {
			  keysDown.remove(SoKeyboardEvent.Key.Q);			  
			  return true;
		  }
		  else if(
				  SoKeyboardEvent.SO_KEY_RELEASE_EVENT(event, SoKeyboardEvent.Key.D)) {
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
	//  SoCamera  camera = getCameraController().getCamera();
	  
//	  System.out.println("locationmouse");
		//getDeviceWidget().setFocus();

	  return false;
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

	public void idle() {

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
	
	public boolean setFocus() {
		return getGLWidget().setFocus();
	}

    protected void paintGL(GL2 gl2) {
    	idle();
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
}
