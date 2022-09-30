/**
 * 
 */
package jscenegraph.interaction.inventor;

import static jscenegraph.opengl.GL.GL_COLOR_BUFFER_BIT;
import static jscenegraph.opengl.GL.GL_DEPTH_BITS;
import static jscenegraph.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static jscenegraph.opengl.GL.GL_LEQUAL;

import jscenegraph.opengl.GL2;

import jscenegraph.database.inventor.SbColor;
import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SbTime;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SbViewportRegion;
import jscenegraph.database.inventor.SoDB;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoHandleEventAction;
import jscenegraph.database.inventor.events.SoEvent;
import jscenegraph.database.inventor.fields.SoSFTime;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.sensors.SoNodeSensor;
import jscenegraph.database.inventor.sensors.SoOneShotSensor;
import jscenegraph.database.inventor.sensors.SoSensor;
import jscenegraph.database.inventor.sensors.SoSensorCB;
import jscenegraph.port.Ctx;

/**
 * @author Yves Boyadjian
 *
 */
public class SoSceneManager {
	
	public interface SoSceneManagerRenderCB {
		public void apply(Object userData, SoSceneManager mgr);
	}
	
	protected SoSceneManagerRenderCB renderCB;
    protected Object renderCBData;
 
    private boolean raCreatedHere;
    private boolean heaCreatedHere;
  	private SoGLRenderAction renderAction;
    private SoHandleEventAction handleEventAction;
 	private SoNode scene;
	private SoNodeSensor sceneSensor;
	private boolean active;
    private boolean needZbuffer;
    private boolean needToSendVP; //!< true when glViewport() needs to be called
  	
	private final SbColor bkgColor = new SbColor();
    private int bkgIndex;
    private boolean graphicsInitNeeded;
 	private boolean rgbMode;
	
	private static SoSensorCB sceneSensorCallback = new SoSensorCB() {

		@Override
		public void run(Object data, SoSensor sensor) {
			SoSceneManager sceneManager = (SoSceneManager)data;
			sceneManager.sceneSensorCallback(sceneManager, sensor);
		}
		
	};
	
    //! realTime field update vars and callbacks
    private static boolean updateRealTime = true;
    private static SoSFTime     realTime;
    private static SoOneShotSensor realTimeSensor = null; //!< touches realTime global field
    
    private GL2 getGL() {
    	return Ctx.get(renderAction.getCacheContext());
    }
     
    ////////////////////////////////////////////////////////////////////////
     //
     // Constructor 
     //
     public SoSceneManager()
     //
     ////////////////////////////////////////////////////////////////////////
     {
         bkgColor.setValue(0,0,0);
         bkgIndex = 0;
         rgbMode = true;
         graphicsInitNeeded = true;
     
         // inventor specific variables
         scene           = null;
         raCreatedHere   = true;
         renderAction    = new SoGLRenderAction(new SbViewportRegion(new SbVec2s((short)1,(short)1)));
         heaCreatedHere  = true;
         handleEventAction = new SoHandleEventAction(new SbViewportRegion(new SbVec2s((short)1,(short)1)));
         renderCB        = null;
         renderCBData    = null;
        active          = false;
        needToSendVP    = true;
    
        // create the scene sensor (used for automatic rendering)
        // do not attach the sensor here. That's done when we
        // become active, and the user supplies a redraw callback.
        sceneSensor = new SoNodeSensor();
        sceneSensor.setData((Object) this);
//    #ifdef DEBUG
//        if (SoDebug::GetEnv("IV_DEBUG_SENSORS")) {
//            SoDebug::NamePtr("sceneChangeSensor", sceneSensor);
//        }
//    #endif
    
        // Assume default priority now. We'll set the sensor priority
        // before we attach, just in case the user changes the priority
        // before that occurs.
        setRedrawPriority(getDefaultRedrawPriority());
    
        // setup the sensor to touch real time after a redraw
        if ( realTimeSensor == null) {
            realTime = (SoSFTime ) SoDB.getGlobalField(new SbName("realTime"));
            realTimeSensor = new SoOneShotSensor();
            realTimeSensor.setFunction((SoSensorCB )
                                     SoSceneManager.realTimeSensorCB);
//    #ifdef DEBUG
//            if (SoDebug::GetEnv("IV_DEBUG_SENSORS")) {
//                SoDebug::NamePtr("realTimeSensor", realTimeSensor);
//            }
//    #endif
        }
    }
     
     
////////////////////////////////////////////////////////////////////////
//
// Description:
//      Gets antialiasing on GL rendering action.
//
// use: public
//
public void getAntialiasing(final boolean[] smoothing, final int[] numPasses)
//
////////////////////////////////////////////////////////////////////////
{
    smoothing[0] = renderAction.isSmoothing();
    numPasses[0] = renderAction.getNumPasses();
}

     
    
 	/**
	 * Apply an SoGLRenderAction to the scene graph managed here. 
	 * The caller is responsible for setting up a window to render into. 
	 * If clearWindow is true, this clears the graphics window before rendering. 
	 * If clearZbuffer is true, the z buffer will be cleared before rendering. 
	 * 
	 * @param clearWindow
	 * @param clearZbuffer
	 */
	 ////////////////////////////////////////////////////////////////////////
	   //
	   // This routine is called to render the scene graph.
	   // A window MUST be set before this is called.
	   //
	   // use: public
	   //
	   public void // java port
	   render(boolean clearWindow) {
		   render(clearWindow,true);
	   }

     
     public void
	   render(boolean clearWindow, boolean clearZbuffer)
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
    	 GL2 gl = getGL();
	       // reinitialize if necessary
	       if (graphicsInitNeeded) {
	           final int[] numBits = new int[1];
	           gl.glGetIntegerv(GL_DEPTH_BITS, numBits,0);
	           needZbuffer = (numBits[0] != 0); // FALSE for overlay windows !
	           if (needZbuffer)
	               gl.glDepthFunc(GL_LEQUAL); // needed for hidden line rendering
	           graphicsInitNeeded = false;
	       }
	   
	       //
	       // when the window changes size, we need to call glViewport() before
	       // we can do a color clear.
	       //
	       if (needToSendVP) {
	           final SbViewportRegion theRegion = renderAction.getViewportRegion();
	           SbVec2s size   = theRegion.getViewportSizePixels();
	           SbVec2s origin = theRegion.getViewportOriginPixels();
	           gl.glViewport(origin.operator_square_bracket(0), origin.operator_square_bracket(1), size.operator_square_bracket(0), size.operator_square_bracket(1));
	           needToSendVP = false;
	       }
	   
	       //
	       // clear to the background color and clear the zbuffer
	       //
	       if (clearWindow) {
	           if (rgbMode)
	                gl.glClearColor(bkgColor.operator_square_bracket(0), bkgColor.operator_square_bracket(1), bkgColor.operator_square_bracket(2), 0);
	           else gl.glClearIndex(bkgIndex);
	   
	           // clear the color+zbuffer at the same time if we can
	           if (needZbuffer && clearZbuffer)
	               gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	           else
	               gl.glClear(GL_COLOR_BUFFER_BIT);
	       }
	       // check to see if only the zbuffer is needed...
	       else if (needZbuffer && clearZbuffer)
	           gl.glClear(GL_DEPTH_BUFFER_BIT);
	   
	       // render the scene graph!
	       if (scene != null)
	           renderAction.apply(scene);
	   
	       // sensor doesn't need to fire again if it's still scheduled
	       sceneSensor.unschedule();
	   
	       // schedule the realTime One shot sensor to update the real time
	       // as soon as we can now that we have rendered the scene. This will
	       // enable us to render things that are animating with a consistent
	       // time across mutiple renderAreas, while providing the maximum
	       // optainable frame rate (much better than a hard coded 30 or 60 
	       // times/sec timer sensor).
	       if (updateRealTime)
	           realTimeSensor.schedule();
	   }
	   
	  	/**
	  	 * Process the passed event by applying an SoHandleEventAction 
	  	 * to the scene graph managed here. 
	  	 * Returns true if the event was handled by a node. 
	  	 * 
	  	 * @param event
	  	 * @return
	  	 */
	   ////////////////////////////////////////////////////////////////////////
	    //
	    // Description:
	    //      Process the passed X event.
	    //
	    // use: virtual protected
	    //
	   public boolean
	    processEvent(final SoEvent event)
	    //
	    ////////////////////////////////////////////////////////////////////////
	    {
	        if ((scene != null) && (handleEventAction != null)) {
	            handleEventAction.setEvent(event);
	            handleEventAction.apply(scene);
	    
	            return handleEventAction.isHandled();
	        }
	        else
	            return false;
	    }
	    
	   /**
	    * Reinitialize graphics. 
	    * This should be called, for instance, when there is a new window. 
	    */
	   ////////////////////////////////////////////////////////////////////////
	    //
	    // Initialize the window for rendering.
	    // A window MUST be set before this is called.
	    //
	    // use: public
	    //
	   public void
	    reinitialize()
	    //
	    ////////////////////////////////////////////////////////////////////////
	    {
	        graphicsInitNeeded = true;
	    
	        // we have a new window, so re-init the render action
	        renderAction.invalidateState();
	    }
	    	   	   

public static void
sceneSensorCallback(SoSceneManager mgr, SoSensor sensor)
{
//#ifdef DEBUG
//    if (mgr->renderCB == NULL) {
//        SoDebugError::post("SoSceneManager::sceneSensorCallback",
//        "Callback was fired,  but auto-redraw is turned off.");
//        return;
//    }
//    if (! mgr->isActive()) {
//        SoDebugError::post("SoSceneManager::sceneSensorCallback",
//        "Callback was fired,  but scene manager not active.");
//        return;
//    }
//#endif

    // redraw that scene graph!
    mgr.redraw(); 
}

	   
	public void
	redraw()
	{
	    if (renderCB != null)
	        (renderCB).apply(renderCBData, this);
	}

	
	public void scheduleRedraw() {
	     // The sceneSensor will only schedule itself if a CB func is set.
	       // We only set the sensor CB func if autoRedraw is on and we are active.
	       // Thus, there are no flags to check here. Just call schedule, and
	       // the sensor will do the right thing.
	       sceneSensor.schedule();		  		
	}
	
	/**
	 * Set the scene graph which is managed here. 
	 * This is the Inventor scene which will be traversed for rendering and 
	 * event processing. 
	 * 
	 * @param newScene
	 */
	//
	   // Description:
	   //      Make the new user supplied scene graph the rendering root.
	   //
	   // use: virtual public
	//
	   public void setSceneGraph(SoNode newScene) {
		     boolean currentlyActive = isActive();
		          deactivate();
		          
		          // ref the new scene
		          if (newScene != null)
		              newScene.ref();
		      
		          // check if there already is a scene graph
		          if (scene != null)
		              scene.unref();
		      
		          // now set the new scene graph
		          scene = newScene;
		          
		          if (currentlyActive)
		              activate();
		     		
	}
	
	   /**
	    * Get the scene graph which is managed here. 
	    * 
	    * @return
	    */
	   //
	    // Description:
	    //      Return the user supplied scene graph.
	    //
	    // use: virtual public
	    //
	public SoNode getSceneGraph() {
		 return scene; 
	}
	
	/**
	 * Set the size of the window in which the scene manager 
	 * should render. 
	 * This size must be set before render() and processEvent() 
	 * are called. 
	 * 
	 * @param newSize
	 */
	public void setWindowSize(final SbVec2s newSize) {
	     final SbViewportRegion rgn = new SbViewportRegion(renderAction.getViewportRegion());
	          rgn.setWindowSize(newSize);
	      
	          if (renderAction != null)
	              renderAction.setViewportRegion(rgn);
	      
	          if (handleEventAction != null)
	              handleEventAction.setViewportRegion(rgn);
	      
	          // make sure to call glViewport() with the new size
	          needToSendVP = true;	     		
	}
	
	/**
	 * Set the window background color when in RGB mode. 
	 * This is the color the scene manager viewport is cleared to when render() 
	 * is called with clearWindow set to true. Default is black (0,0,0). 
	 * 
	 * @param c
	 */
	public void setBackgroundColor(SbColor c) {
		
	     bkgColor.copyFrom(c);
	          if (isRGBMode())
	              scheduleRedraw();
	     	}
	
    //! Get the window background color when in RGB mode.
    public SbColor getBackgroundColor() { return bkgColor; }
 	
    //! Set the window background color when in color index mode.
      //! This is the color the scene manager viewport is cleared to when
      //! render()
      //! is called with \p clearWindow set to <em>TRUE</em>.
      //! Default is black (index 0).
    public  void                setBackgroundIndex(int index) {
        bkgIndex = index;
             if (! isRGBMode())
                 scheduleRedraw();
            	
    }
      //! Get the window background color when in color index mode.
    public  int                 getBackgroundIndex() { return bkgIndex; }
      
	public boolean isRGBMode() { return rgbMode; }

	/**
	 * Activate the scene manager. 
	 * The scene manager will only employ sensors for automatic redraw 
	 * while it is active. 
	 * Typically, the scene manager should be activated whenever its window 
	 * is visible on the screen, and deactivated when its window is closed or 
	 * iconified. 
	 */
	public void activate() {
		
	     // attach sceneSensor to top node for redrawing purpose
		       // only if the user has specified a redraw callback (i.e.
		       // auto-redraw is enabled)
		       if (renderCB != null) {
		           if (scene != null && sceneSensor.getAttachedNode() == null) {
		               sceneSensor.setFunction((SoSensorCB )
		                            SoSceneManager.sceneSensorCallback);
		               sceneSensor.attach(scene);
		           }
		       }
		       active = true;
		  	}
	
	// Deactivate the scene manager. 
	public void deactivate() {
		
	     sceneSensor.detach();
	          sceneSensor.setFunction(null);
	          active = false;
	 }
	
	/**
	 * The render callback provides a mechanism for automatically redrawing the scene in response to changes in the scene graph. 
	 * The scene manager employs a sensor to monitor scene graph changes. 
	 * When the sensor is triggered, the render callback registered here is invoked. 
	 * The callback should set up its graphics window, then call the scene manager render() method. 
	 * If the callback is set to NULL (the default), auto-redraw is turned off. 
	 * 
	 */
	 ////////////////////////////////////////////////////////////////////////
	   //
	   //  Set the rendering callback. The app can supply this to enable
	   //  automatic redrawing of the scene. This callback should perform
	   //  window specific graphics initialization, then call the scene manager
	   //  render() method.
	   //
	   //  use: public
	   //
	   public void	   setRenderCallback(
	       SoSceneManagerRenderCB f,
	       Object userData)
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       renderCB = f;
	       renderCBData = userData;
	   
	       if (f != null) {
	           // if we are active, attach the scene sensor
	           if (active) {
	               if (scene != null && sceneSensor.getAttachedNode() == null) {
	                   sceneSensor.setFunction((SoSensorCB )
	                                SoSceneManager.sceneSensorCallback);
	                   sceneSensor.attach(scene);
	               }
	           }
	       }
	       else {
	           // detach the scene sensor (whether active or not)
	           sceneSensor.detach();
	           sceneSensor.setFunction(null);
	       }
	   }
	   	
	
	public SoGLRenderAction getGLRenderAction() {
		 return renderAction; 
	}
	
	protected boolean isActive() {
		return active;
	}
	
	/**
	 * Set the priority of the redraw sensor. 
	 * Sensors are processed based on priority, with priority 
	 * values of 0 processed immediately. 
	 * The default priority for the scene manager redraw sensor 
	 * is 10000. 
	 * 
	 * @param priority
	 */
	public void setRedrawPriority(int priority)  {
		  sceneSensor.setPriority(priority);		
	}
	
////////////////////////////////////////////////////////////////////////
//
// Destructor
//
public void destructor()
//
////////////////////////////////////////////////////////////////////////
{
    // delete actions
    if (raCreatedHere) {
        renderAction.destructor(); renderAction = null;
    }
    if (heaCreatedHere) {
        handleEventAction.destructor(); handleEventAction = null;
    }
    // detach the scene
    setSceneGraph(null);
    sceneSensor.destructor(); sceneSensor = null;
}	
	
	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Set a new render action, for instance for highlighting.
	   //
	   // public
	   //
	  public void
	   setGLRenderAction(SoGLRenderAction ra)
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
//	   #ifdef DEBUG
//	       if (ra == NULL)
//	           SoDebugError::post("SoSceneManager::setGLRenderAction",
//	                           "a NULL render action was passed. This is bad!");
//	   #endif
	   
	       // Make sure the viewport region is set
	       // (be paranoid and check for NULL)
	       if (renderAction != null) {
	           final SbViewportRegion rgn = new SbViewportRegion(renderAction.getViewportRegion());
	           ra.setViewportRegion(rgn);
	       }
	   
	       // Get rid of the old render action if it was created here.
	       if (raCreatedHere) {
	    	   renderAction.destructor();
	           raCreatedHere = false;
	       }
	   
	       // Set to the new render action.
	       renderAction = ra;
	   }
	   	
	
	public SoHandleEventAction getHandleEventAction() {
		 return handleEventAction; 
	}
	
	// Get the default priority of the redraw sensor.
	public static int getDefaultRedrawPriority() {
		 return 10000; 
	}
	
	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //      Callback to update the realTime global field.
	   //
	   // Use: static private
	   
	  	public static final SoSensorCB realTimeSensorCB =
		 new SoSensorCB() {

			@Override
			public void run(Object data, SoSensor sensor) {
				realTime.setValue(SbTime.getTimeOfDay());				
			}
			
		};	

		 ////////////////////////////////////////////////////////////////////////
		   //
		   //  Set to RGB mode or color map mode.
		   //
		   //  use: public
		   //
		  public void
		   setRGBMode(boolean onOrOff)
		   //
		   ////////////////////////////////////////////////////////////////////////
		   {
		       rgbMode = onOrOff;
		   
		       // the render action determines color map/rgb at initialization
		   //???pauli    renderAction->reinitialize();
		   }
		  
		  /**
		   * Set the antialiasing for rendering. 
		   * There are two kinds of antialiasing available: smoothing and multipass antialiasing. 
		   * If smoothing is set to TRUE, smoothing is enabled. 
		   * Smoothing uses OpenGL's line- and point-smoothing features to provide cheap antialiasing of lines and points. 
		   * The value of numPasses controls multipass antialiasing. 
		   * Each time a render action is applied, Inventor renders the scene numPasses times from slightly different camera positions, averaging the results. 
		   * numPasses can be from one to 255, inclusive. 
		   * Setting numPasses to one disables multipass antialiasing. 
		   * You can use either, both, or neither of these antialiasing techniques. 
		   * By default, both smoothing and multipass antialiasing are disabled. 
		   * 
		   * @param smoothing
		   * @param numPasses
		   */
		  ////////////////////////////////////////////////////////////////////////
		   //
		   // Description:
		   //      Sets antialiasing on GL rendering action.
		   //
		   // use: public
		   //
		  public void
		   setAntialiasing(boolean smoothing, int numPasses)
		   //
		   ////////////////////////////////////////////////////////////////////////
		   {
		       renderAction.setSmoothing( smoothing );
		       renderAction.setNumPasses( numPasses );
		   
		       // Set render action callback for multiple pass rendering.  This
		       // makes sure to clear background to correct color between frames.
		       if ( numPasses > 1 )
		           renderAction.setPassCallback(new SoGLRenderAction.SoGLRenderPassCB() {

					@Override
					public void run(GL2 gl, Object data) {
						antialiasingCallback(data, gl);						
					}
		        	   
		           }, this);
		       else
		           renderAction.setPassCallback(null, null);
		   }

		  //
		   ////////////////////////////////////////////////////////////////////////
		   // static callbacks stubs
		   ////////////////////////////////////////////////////////////////////////
		   //
		   
		  public void
		   antialiasingCallback( Object r , GL2 gl)
		   {
		       SoSceneManager mgr = (SoSceneManager ) r;
		   
		       if (mgr.rgbMode)
		            gl.glClearColor(mgr.bkgColor.getValueRead()[0], mgr.bkgColor.getValueRead()[1], mgr.bkgColor.getValueRead()[2], 0);
		       else gl.glClearIndex( mgr.bkgIndex );
		   
		       gl.glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );
		   }

		  ////////////////////////////////////////////////////////////////////////
		   //
		   // Priority of the redraw sensor
		   //
		   // public 
		   //
		  public int
		   getRedrawPriority() 
		   //
		   ////////////////////////////////////////////////////////////////////////
		   {
		       return sceneSensor.getPriority();
		   }
}
