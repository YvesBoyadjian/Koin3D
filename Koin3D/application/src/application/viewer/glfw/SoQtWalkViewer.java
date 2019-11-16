/**
 * 
 */
package application.viewer.glfw;


import java.awt.AWTException;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import javax.imageio.ImageIO;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
import java.time.Instant;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;


//import org.eclipse.swt.events.MouseEvent;
//import org.eclipse.swt.events.MouseMoveListener;
//import org.eclipse.swt.graphics.Cursor;
//import org.eclipse.swt.graphics.Point;
//import org.eclipse.swt.widgets.Composite;
//import org.eclipse.swt.widgets.Display;
//import org.lwjgl.opengl.swt.GLCanvas;
import com.jogamp.opengl.GL2;

import application.scenegraph.HeightProvider;
import jscenegraph.database.inventor.SbRotation;
import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.events.SoEvent;
import jscenegraph.database.inventor.events.SoKeyboardEvent;
import jscenegraph.database.inventor.events.SoLocation2Event;
import jscenegraph.database.inventor.nodes.SoCamera;
import jscenegraph.database.inventor.sensors.SoIdleSensor;
import jscenegraph.database.inventor.sensors.SoSensor;
import jsceneviewerglfw.Composite;
import jsceneviewerglfw.Display;
import jsceneviewerglfw.MouseEvent;
import jsceneviewerglfw.MouseMoveListener;
import jsceneviewerglfw.inventor.qt.SoQtCameraController;
import jsceneviewerglfw.inventor.qt.SoQtCameraController.Type;
import jsceneviewerglfw.inventor.qt.viewers.SoQtConstrainedViewer;
import jsceneviewerglfw.inventor.qt.viewers.SoQtFullViewer;
import org.lwjgl.util.Point;

/**
 * @author Yves Boyadjian
 *
 */
public class SoQtWalkViewer extends SoQtConstrainedViewer {
	
	private final static float GOD = 1000;
	
	private final static float JET = 260;
	
	private final static float AIRPLANE = 100;
	
	private final static float USAIN_BOLT_RUN = 10;
	
	private final static float SPEED = /*GOD;*/USAIN_BOLT_RUN;
	
	private final static float EYES_HEIGHT = 1.7f;

	final SbVec2f old_position = new SbVec2f(Float.NaN, Float.NaN);

	protected float sensitivity = 1;
	protected boolean invert = false;
	
	private double lastTimeSec = System.nanoTime()/1.0e9;
	
	private Set<SoKeyboardEvent.Key> keysDown = new HashSet<>();
	
	private List<Consumer<SoQtWalkViewer>> idleListeners = new ArrayList<>();
	
	private SoIdleSensor idleSensor = new SoIdleSensor(SoQtWalkViewer::idleCB,this);
	
	private HeightProvider heightProvider = new HeightProvider() {

		@Override
		public float getZ(float x, float y, float z) {
			return z;
		}
		
	};
	
	public SoQtWalkViewer(SoQtFullViewer.BuildFlag flag, SoQtCameraController.Type type, Composite parent, int f) {
		super(flag, type, parent, f);
	}

	public void buildWidget(int style) {
		super.buildWidget(style);
		
//		getDeviceWidget().
//		addMouseMoveListener(new MouseMoveListener() {
//
//			@Override
//			public void mouseMove(MouseEvent e) {
//				if(focus)
//					processMouseMoveEvent(/*e*/);
//			}
//			
//		});
//		old_position.copyFrom(getCursorPosition());

		
	}
	
	final SbVec2f diff = new SbVec2f();
	
	protected void processMouseMoveEvent(/*MouseEvent e*/) {
		
		  /* Zjisteni zmeny pozice kurzoru. */
		  SbVec2f position = getCursorPosition();
		  
		  if( ! Float.isNaN(old_position.getX())) {
		  
			  diff.operator_add_equal(old_position.operator_minus(position));
		  
		  //old_position.copyFrom( getPosition().operator_add( getCenter()));
		  
		  //setCursorPosition(old_position); //YB

		  //position = getCursorPosition();
		  }
		  old_position.setValue(position.getX(), position.getY());
		  
		  //idle();
	}
	
	protected void moveCamera() {

		  SoCamera  camera = getCameraController().getCamera();
		  
		  if(camera == null) {
			  return;
		  }

			if(focus)
				processMouseMoveEvent(/*e*/);
			
		  float rotation_x = sensitivity * 0.001f * diff.getValueRead()[1];
		  rotation_x = invert ? -rotation_x : rotation_x;
		  float rotation_z = sensitivity * 0.001f * diff.getValueRead()[0];
		  
		  diff.setValue((short)0, (short)0);

		  /* Rotace v X ose. */
		  camera.orientation.setValue( new SbRotation(new SbVec3f(1.0f, 0.0f, 0.0f), rotation_x).operator_mul(
		    camera.orientation.getValue()));

		  /* Rotace v Z ose. */
		  camera.orientation.setValue( camera.orientation.getValue().operator_mul(
		    new SbRotation(new SbVec3f(0.0f, 0.0f, 1.0f), rotation_z)));
	}
	
	private SbVec2f getCenter()
	{
	  /* Ziskani stredu okna relativne. */
	  Composite widget = getGLWidget();
	  return new SbVec2f((float)(widget.getSize().getX() / 2), (float)(widget.getSize().getY() / 2));
	}

	private SbVec2f getPosition()
	{
	  /* Ziskani pocatku okna vuci obrazovce. */
	  Composite widget = getParentWidget();
	  
	  long window = getGLWidget().getWindow();
	  
	  
	  Point position = new Point(0,0);//widget.toDisplay(0, 0);
	  return new SbVec2f((float)position.getX(), (float)position.getY());
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

	 private SbVec2f getCursorPosition()
	 {
	   /* Ziskani absolutni pozice kurzoru. */
	   //QPoint position = QCursor.pos();
		 DoubleBuffer bufx = BufferUtils.createDoubleBuffer(1);
		 DoubleBuffer bufy = BufferUtils.createDoubleBuffer(1);
		 glfwGetCursorPos(getGLWidget().getWindow(),bufx,bufy);
	   //Point position = Display.getCurrent().getCursorLocation();		  
	   return new SbVec2f((float)bufx.get(), (float)bufy.get());
	 }
	 	
	protected boolean processSoKeyboardEvent( SoKeyboardEvent event)
	{
		//double currentTimeSec = System.nanoTime()/1.0e9;
		
		//float deltaT = (float)(currentTimeSec - lastTimeSec);
		//if(deltaT > 1.0f) {
			//deltaT = 1.0f;
		//}
		//System.out.println("processSoKeyboardEvent");
		
		  if (SoKeyboardEvent.SO_KEY_PRESS_EVENT(event, SoKeyboardEvent.Key.PRINT))
		  {
			  GL11.glReadBuffer(GL11.GL_FRONT);
			  int[] width = new int[1];
			  int [] height = new int[1];
			  glfwGetWindowSize(getGLWidget().getWindow(),width,height);
			  //int width = Display.getDisplayMode().getWidth();
			  //int height= Display.getDisplayMode().getHeight();
			  int bpp = 4; // Assuming a 32-bit display with a byte each for red, green, blue, and alpha.
			  ByteBuffer buffer = BufferUtils.createByteBuffer(width[0] * height[0] * bpp);
			  GL11.glReadPixels(0, 0, width[0], height[0], GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer );
			  BufferedImage image = new BufferedImage(width[0], height[0], BufferedImage.TYPE_INT_RGB);
			   
			  for(int x = 0; x < width[0]; x++) 
			  {
			      for(int y = 0; y < height[0]; y++)
			      {
			          int i = (x + (width[0] * y)) * bpp;
			          int r = buffer.get(i) & 0xFF;
			          int g = buffer.get(i + 1) & 0xFF;
			          int b = buffer.get(i + 2) & 0xFF;
			          image.setRGB(x, height[0] - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
			      }
			  }
			     
			  try {
				  String format = "JPG";
				  
				  File file = new File("MountRainierIslandScreenShot.jpg");
				  
			      ImageIO.write(image, format, file);
			  } catch (IOException e) { e.printStackTrace(); }
		    return true;
		  }
		  if (SoKeyboardEvent.SO_KEY_RELEASE_EVENT(event, SoKeyboardEvent.Key.ESCAPE))
		  {
			  onClose();
			  glfwSetWindowShouldClose(getGLWidget().getWindow(), true);
		    return true;
		  }
		  else if (SoKeyboardEvent.SO_KEY_PRESS_EVENT(event, SoKeyboardEvent.Key.W))
		  {
			  if(!keysDown.contains(SoKeyboardEvent.Key.W)) {
				  lastTimeSec = System.nanoTime()/1.0e9;
				  keysDown.add(SoKeyboardEvent.Key.W);
			  }
			  return true;
		  }
		  else if (SoKeyboardEvent.SO_KEY_PRESS_EVENT(event, SoKeyboardEvent.Key.S))
		  {
			  //lastTimeSec = System.nanoTime()/1.0e9;
			  if(!keysDown.contains(SoKeyboardEvent.Key.S)) {
				  lastTimeSec = System.nanoTime()/1.0e9;
				  keysDown.add(SoKeyboardEvent.Key.S);
			  }
			  return true;
		  }
		  else if (SoKeyboardEvent.SO_KEY_PRESS_EVENT(event, SoKeyboardEvent.Key.A))
		  {
			  //lastTimeSec = System.nanoTime()/1.0e9;
			  if(!keysDown.contains(SoKeyboardEvent.Key.A)) {
				  lastTimeSec = System.nanoTime()/1.0e9;
				  keysDown.add(SoKeyboardEvent.Key.A);
			  }
			  return true;
		  }
		  else if (SoKeyboardEvent.SO_KEY_PRESS_EVENT(event, SoKeyboardEvent.Key.D))
		  {
			  //lastTimeSec = System.nanoTime()/1.0e9;
			  if(!keysDown.contains(SoKeyboardEvent.Key.D)) {
				  lastTimeSec = System.nanoTime()/1.0e9;
				  keysDown.add(SoKeyboardEvent.Key.D);
			  }
			  return true;
		  }
		  else if (SoKeyboardEvent.SO_KEY_RELEASE_EVENT(event, SoKeyboardEvent.Key.W)) {
			  keysDown.remove(SoKeyboardEvent.Key.W);
			  return true;
			  
		  }
		  else if(
				  SoKeyboardEvent.SO_KEY_RELEASE_EVENT(event, SoKeyboardEvent.Key.S) ) {
			  keysDown.remove(SoKeyboardEvent.Key.S);			  					  
			  return true;
				  }
		  else if(
				  SoKeyboardEvent.SO_KEY_RELEASE_EVENT(event, SoKeyboardEvent.Key.A) ) {
			  keysDown.remove(SoKeyboardEvent.Key.A);			  
			  return true;
		  }
		  else if(
				  SoKeyboardEvent.SO_KEY_RELEASE_EVENT(event, SoKeyboardEvent.Key.D)) {
			  keysDown.remove(SoKeyboardEvent.Key.D);			 			  
			  return true;
		  }
		return false;
	}
	
	public void updateLocation(SbVec3f diff_position) {
		
		double currentTimeSec = System.nanoTime()/1.0e9;
		
		  SoCamera camera = getCameraController().getCamera();
		  SbVec3f old_position = camera.position.getValue();
		  SbRotation orientation = camera.orientation.getValue();
		  orientation.multVec(diff_position, diff_position);
		  SbVec3f new_position = old_position.operator_add( diff_position);
		  new_position.setZ(EYES_HEIGHT + heightProvider.getZ(new_position.getX(), new_position.getY(), new_position.getZ() - EYES_HEIGHT));
		  camera.position.setValue( new_position );
		  getCameraController().changeCameraValues(camera);

		  lastTimeSec = currentTimeSec;

//		  System.out.println("x = "+ camera.position.getValue().getX());
//		  System.out.println("y = "+ camera.position.getValue().getY());
//		  System.out.println("z = "+ camera.position.getValue().getZ());
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
	
	private boolean idleActive = true;
	
	public static void idleCB(Object data, SoSensor sensor) {
		
		SoQtWalkViewer v = (SoQtWalkViewer) data;
		if(!v.idleActive) {
			return;
		}
		SoQtWalkViewer viewer = (SoQtWalkViewer)data;
		viewer.idle();
		//viewer.getSceneHandler().getSceneGraph().touch();
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
			  keysDown.contains(SoKeyboardEvent.Key.W)) {
			  
			  //lastTimeSec = System.nanoTime()/1.0e9;
			  
			  
			  updateLocation(new SbVec3f(0.0f, 0.0f, -SPEED* deltaT));
		  }
		  if (
		  
			  keysDown.contains(SoKeyboardEvent.Key.S)) {
			  
				//  lastTimeSec = System.nanoTime()/1.0e9;
			  
			  updateLocation(new SbVec3f(0.0f, 0.0f, SPEED* deltaT));

		  }
		  if (  keysDown.contains(SoKeyboardEvent.Key.A)) {
			  
				  //lastTimeSec = System.nanoTime()/1.0e9;
			  
			  updateLocation(new SbVec3f(- SPEED* deltaT, 0.0f, 0.0f));
			  
		  }
		  if (  keysDown.contains(SoKeyboardEvent.Key.D)) {
			  
				  //lastTimeSec = System.nanoTime()/1.0e9;

			  updateLocation( new SbVec3f(SPEED* deltaT, 0.0f, 0.0f));

		  }
		  
		  idleListeners.forEach((item)->item.accept(this));
		  
		  if(keysDown.isEmpty()) {
			  //System.gc();
		  }
	}
	
	boolean focus;
	
	public boolean setFocus() {
		super.setFocus();
		return getGLWidget().setFocus();
	}
	
	int nbFrames = 0;
	
	long lastFrameTime = -100;
	
	final static int NB_FRAMES = 100;

    protected void paintGL(GL2 gl2) {
    	
    	//idle();
    	super.paintGL(gl2);
    	
    	if(lastFrameTime < 0) {
    		lastFrameTime++;
    	}
    	if(lastFrameTime == 0) {
    		lastFrameTime = System.nanoTime();
    	}
    	if(lastFrameTime > 0) {
    		nbFrames++;
    	}
    	if(nbFrames == NB_FRAMES) {
    		long newFrameTime = System.nanoTime();
    		float fps = 1.0e9f/(newFrameTime - lastFrameTime)*NB_FRAMES;
    		System.out.println("fps = "+fps);
    		nbFrames = 0;
    		lastFrameTime = newFrameTime;
    	}
    }
    
//    public void actualRedraw()
//    {
//    	idle();
//    	super.actualRedraw();
//    }
    
    public void addIdleListener(Consumer<SoQtWalkViewer> listener) {
    	idleListeners.add(listener);
    }

	public void start() {
		idle();
		idleSensor.schedule();
	}

	public void destructor() {
		idleActive = false;
		super.destructor();
	}
	
	public static final float MAX_FPS = 250.0f;
	
//	private long lastTime = Instant.now().toEpochMilli();

//    public void swapBuffers() {
//    	
//    	long present = Instant.now().toEpochMilli();
//    	long deltaMilli = present - lastTime;
//    	long deltaMinMilli = 1000 / (int)MAX_FPS;
//    	if(deltaMinMilli > deltaMilli) {
//    		try {
//				Thread.sleep(deltaMinMilli - deltaMilli);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//    	}
//    	
//    	lastTime = Instant.now().toEpochMilli();
//    	super.swapBuffers();
//    }
    
    public void setHeightProvider(HeightProvider hp) {
    	this.heightProvider = hp;
    }
    
    // to be redefined
    public void onClose() {
    	
    }
}
