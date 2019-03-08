/*
 *
 *  Copyright (C) 2000 Silicon Graphics, Inc.  All Rights Reserved.
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  Further, this software is distributed without any warranty that it is
 *  free of the rightful claim of any third person regarding infringement
 *  or the like.  Any license provided herein, whether implied or
 *  otherwise, applies only to this software file.  Patent licenses, if
 *  any, provided herein do not apply to combinations of this program with
 *  other software, or any other product whatsoever.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Contact information: Silicon Graphics, Inc., 1600 Amphitheatre Pkwy,
 *  Mountain View, CA  94043, or:
 *
 *  http://www.sgi.com
 *
 *  For further information regarding this notice, see:
 *
 *  http://oss.sgi.com/projects/GenInfo/NoticeExplan/
 *
 */


/*
 * Copyright (C) 1990,91   Silicon Graphics, Inc.
 *
 _______________________________________________________________________
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 |
 |   $Revision: 1.1.1.1 $
 |
 |   Description:
 |      Defines the SoGLRenderAction class
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.actions;

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_LINE_SMOOTH;
import static com.jogamp.opengl.GL.GL_ONE;
import static com.jogamp.opengl.GL.GL_ONE_MINUS_SRC_ALPHA;
import static com.jogamp.opengl.GL.GL_SRC_ALPHA;
import static com.jogamp.opengl.GL2.GL_ACCUM;
import static com.jogamp.opengl.GL2.GL_LOAD;
import static com.jogamp.opengl.GL2.GL_RETURN;
import static com.jogamp.opengl.GL2ES1.GL_POINT_SMOOTH;

import com.jogamp.opengl.GL2;

import jscenegraph.coin3d.inventor.elements.SoMultiTextureImageElement;
import jscenegraph.coin3d.inventor.nodes.SoTransparencyType;
import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SbViewportRegion;
import jscenegraph.database.inventor.SoPath;
import jscenegraph.database.inventor.SoPathList;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoActionMethodList.SoActionMethod;
import jscenegraph.database.inventor.elements.SoCacheElement;
import jscenegraph.database.inventor.elements.SoElement;
import jscenegraph.database.inventor.elements.SoGLCacheContextElement;
import jscenegraph.database.inventor.elements.SoGLLazyElement;
import jscenegraph.database.inventor.elements.SoGLLightIdElement;
import jscenegraph.database.inventor.elements.SoGLRenderPassElement;
import jscenegraph.database.inventor.elements.SoGLUpdateAreaElement;
import jscenegraph.database.inventor.elements.SoGLViewportRegionElement;
import jscenegraph.database.inventor.elements.SoLazyElement;
import jscenegraph.database.inventor.elements.SoOverrideElement;
import jscenegraph.database.inventor.elements.SoShapeStyleElement;
import jscenegraph.database.inventor.elements.SoTextureOverrideElement;
import jscenegraph.database.inventor.elements.SoViewportRegionElement;
import jscenegraph.database.inventor.elements.SoWindowElement;
import jscenegraph.database.inventor.misc.SoCallbackListCB;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.mevis.inventor.system.SbOpenGL;
import jscenegraph.port.Ctx;
import jscenegraph.port.Destroyable;
import jscenegraph.port.IGLCallback;


////////////////////////////////////////////////////////////////////////////////
//! Renders a scene graph using OpenGL.
/*!
\class SoGLRenderAction
\ingroup Actions
This class traverses a scene graph and renders it using the OpenGL
graphics library. It assumes that a valid window has been created
and initialized for proper OpenGL rendering. The SoQtRenderArea
class or any of its subclasses may be used to create such a window.

\par See Also
\par
SoSeparator, SoQtRenderArea
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoGLRenderAction extends SoAction implements Destroyable {

	@Override
	public SoType getTypeId() {
		return new SoType(classTypeId);
	}
    public static SoType getClassTypeId()
                                    { return classTypeId; }
    public static void addMethod(SoType t, SoActionMethod method)
                                    { methods.addMethod(t, method); }
    // java port
    public  static void                 enableElement(Class<?> klass)
    { enabledElements.enable(SoElement.getClassTypeId(klass), SoElement.getClassStackIndex(klass));}

    public static void enableElement(SoType t, int stkIndex)
                                    { enabledElements.enable(t, stkIndex);}
    @Override
	protected SoEnabledElementsList getEnabledElements() {
	  return enabledElements;
    }
    protected  static SoEnabledElementsList enabledElements;
    protected  static SoActionMethodList   methods;
    private static SoType               classTypeId	;

    //! Various levels of transparency rendering quality
       public enum TransparencyType {
           SCREEN_DOOR,            //!< Use GL patterns for screen-door transparency
           ADD,                    //!< Use additive GL alpha blending
           DELAYED_ADD,            //!< Use additive blending, do transp objs last
           SORTED_OBJECT_ADD,      //!< Use additive blending, sort objects by bbox
           BLEND,                  //!< Use GL alpha blending
           DELAYED_BLEND,          //!< Use GL alpha blending, do transp objs last
          SORTED_OBJECT_BLEND,     //!< Use GL alpha blending, sort objects by bbox
          NONE; // COIN 3D

           public int getValue() {
        	   return ordinal();
           }

		public static TransparencyType fromValue(int value) {
			return values()[value];
		}
      };

    //! Possible return codes from a render abort callback
      enum AbortCode {
          CONTINUE,               //!< Continue as usual
          ABORT,                  //!< Stop traversing the rest of the graph
          PRUNE,                  //!< Do not traverse this node or its children
          DELAY                   //!< Delay rendering of this node
      };      

    //! Callback functions used between rendering passes should be of this type.
      public interface SoGLRenderPassCB extends IGLCallback {

      }

      //! Callback functions for render abort should be of this type.
           //! This typedef is defined within the class, since it needs to
           //! refer to the AbortCode enumerated type.
           public interface SoGLRenderAbortCB {

        	   AbortCode abort(Object userData);

           }
           
           public interface SoGLPreRenderCB {
        	   void apply(Object userdata, SoGLRenderAction action);
           }
           
           
           private SoGLRenderActionP pimpl = new SoGLRenderActionP();

	     private final	       SbViewportRegion    vpRegion = new SbViewportRegion();       //!< Current viewport region
	     private final	       SbVec2f             updateOrigin = new SbVec2f();   //!< Origin of update area
	     private final	       SbVec2f             updateSize = new SbVec2f();     //!< Size of update area


	       //! Variables for render abort:
	     private	       SoGLRenderAbortCB   abortCB;       //!< Callback to test abort
	     private	       Object                abortData;     //!< User data for abort callback

	       //! Variables for transparency, smoothing, and multi-pass rendering:
	     private	       TransparencyType    transpType;     //!< Transparency quality type
	     private	       boolean              doSmooth;       //!< Doing smoothing ?
	     private	       int                 numPasses;      //!< Number of rendering passes
	     private	       int                 curPass;        //!< Current pass
	     private	       boolean              passUpdate;     //!< Whether to update after each pass
	     private	       SoGLRenderPassCB    passCB;        //!< Callback between passes
	     private	       Object                passData;      //!< User data for pass callback

	       //! For SORTED_OBJECT_ADD or SORTED_OBJECT_BLEND transparency:
	     private	       boolean              renderingTranspObjs; //!< true when rendering transp objs
	     private	       boolean              delayObjs;      //!< true if transp objects are to be
	                                           //! delayed until after opaque ones
	     private	       boolean              sortObjs;       //!< true if objects are to be sorted
	     private final	       SoPathList          transpPaths = new SoPathList();    //!< Paths to transparent objects
	     private	       SoGetBoundingBoxAction ba;         //!< For computing bounding boxes
	     private	       SbBox3f[]             bboxes;        //!< Bounding boxes of objects
	     private	       int                 numBBoxes;      //!< Number of bboxes allocated

	     private	       /*GL2*/int            cacheContext;   //!< GL cache context
	     private	       boolean              remoteRendering;//!< Remote rendering?

	       //! Stuff needed to implement rendering of delayed paths
	     private final	       SoPathList          delayedPaths = new SoPathList();   //!< List of paths to render
	     private	       boolean              renderingDelPaths; //!< true when rendering them

	     //! These flags determine which things have to be sent to GL when
	          //! the action is applied. They indicate what's changed since the
	          //! last time the action was applied.
          enum flags {
              TRANSPARENCY_TYPE(0x01),
              SMOOTHING(0x02),
              ALL(0x03);  //!< Initial value

              private int value;

              flags(int value) {
            	  this.value = value;
              }

              public int getValue() {
            	  return value;
              }
          };
     	  int whatChanged;

       //! Keep track of which planes we need to view-volume cull test
       //! against:
       int                 cullBits;

     ////////////////////////////////////////////////////////////////////////
     //
     // Description:
     //    Constructor. The first parameter defines the viewport region
     //    into which rendering will take place.
     //
     // Use: public

     public SoGLRenderAction(final SbViewportRegion viewportRegion)

     //
     ////////////////////////////////////////////////////////////////////////
     {
         //SO_ACTION_CONSTRUCTOR(SoGLRenderAction);
         traversalMethods = methods;


         vpRegion.copyFrom(viewportRegion);
         updateOrigin.setValue(0.0f, 0.0f);
         updateSize.setValue(1.0f, 1.0f);

         abortCB             = null;

         transpType          = TransparencyType.SCREEN_DOOR;
         doSmooth            = false;
         numPasses           = 1;
         passUpdate          = false;
         passCB              = null;

         renderingTranspObjs = false;
         delayObjs           = false;
         sortObjs            = false;
         ba                  = null;
        bboxes              = null;
        cacheContext        = /*null*/0;
        remoteRendering     = false;

        renderingDelPaths   = false;

        whatChanged         = flags.ALL.getValue();

        // These three bits keep track of which view-volume planes we need
        // to test against; by default, all bits are 1.
        cullBits            = 7;

        pimpl.smoothing = false;
        pimpl.needglinit = true;
     }

     @Override
	public void destructor() {
    	    if (ba != null) {
				ba.destructor();
			}

    	    if (bboxes != null) {
				/*delete []*/ bboxes = null;
			}
     }

   ////////////////////////////////////////////////////////////////////////
     //
     // Description:
     //    Sets the viewport region.  This should be called when the window
     //    changes size.
     //
     // Use: public
    public void
     setViewportRegion(final SbViewportRegion newRegion)
     //
     ////////////////////////////////////////////////////////////////////////
     {
         vpRegion.copyFrom(newRegion);
     }


    /**
     * Returns viewport region to use for rendering.
     */
    //! Returns viewport region to use for rendering.
      public SbViewportRegion getViewportRegion() { return vpRegion; }

       ////////////////////////////////////////////////////////////////////////
       //
       // Description:
       //    Sets the rendering update area.
       //
       // Use: public
      public void
       setUpdateArea(final SbVec2f origin, final SbVec2f size)
       //
       ////////////////////////////////////////////////////////////////////////
       {
           updateOrigin.copyFrom(origin);
           updateSize.copyFrom(size);
       }

       ////////////////////////////////////////////////////////////////////////
       //
       // Description:
       //    Returns the rendering update area.
       //
       // Use: public
       public void
       getUpdateArea(final SbVec2f origin, final SbVec2f size)
       //
       ////////////////////////////////////////////////////////////////////////
       {
           origin.copyFrom(updateOrigin);
           size.copyFrom(updateSize);
       }
       
       /*!
       Sets the abort callback.  The abort callback is called by the action
       for each node during traversal to check for abort conditions.

       The callback method should return one of the
       SoGLRenderAction::AbortCode enum values to indicate how the action
       should proceed further.

       Since the client SoGLRenderAbortCB callback function only has a
       single void* argument for the userdata, one has to do some
       additional work to find out which node the callback was made
       for. One can do this by for instance passing along the action
       pointer as userdata, and then call the
       SoGLRenderAction::getCurPath() method. The tail of the path will
       then be the last traversed node. Like this:

       \code
       // set up so we can abort or otherwise intervene with the render
       // traversal:
       myRenderAction->setAbortCallback(MyRenderCallback, myRenderAction);

       // [...]

       SoGLRenderAction::AbortCode
       MyRenderCallback(void * userdata)
       {
         SoGLRenderAction * action = (SoGLRenderAction *)userdata;
         SoNode * lastnode = action->getCurPath()->getTail();

         // [...]
         return SoGLRenderAction::CONTINUE;
       }
       \endcode

       \sa SoGLRenderAction::AbortCode
     */
     public void
     setAbortCallback(SoGLRenderAbortCB func,
                                        Object userdata)
     {
       this.pimpl.abortcallback = func;
       this.pimpl.abortcallbackdata = userdata;
     }

     /*!
       Returns the abort callback settings.

       \sa setAbortCallback
       \since Coin 3.0
     */
     public void
     getAbortCallback(SoGLRenderAbortCB[] func_out,
                                        Object[] userdata_out)
     {
       func_out[0] = this.pimpl.abortcallback;
       userdata_out[0] = this.pimpl.abortcallbackdata;
     }

       

      ////////////////////////////////////////////////////////////////////////
       //
       // Invalidate the state so that it will be created again
       // next time the action is applied.
       //
       // Use: public, virtual

      @Override
	public void
       invalidateState()
       //
       ////////////////////////////////////////////////////////////////////////
       {
           // Invalidate the state in the usual way
           super.invalidateState();
           pimpl.needglinit = true;

           // Also invalidate what we think we know...
           whatChanged = flags.ALL.getValue();
       }

      ////////////////////////////////////////////////////////////////////////
       //
       // Description:
       //    Sets cache context to given value
       //
       // Use: public

      public void
       setCacheContext( int context)
       //
       ////////////////////////////////////////////////////////////////////////
       {
           // If the cache context changes, we've changed OpenGL contexts,
           // and we should also invalidate the state:
           if (cacheContext != context) {
               invalidateState();
               cacheContext = context;
           }
       }

      /**
       * Sets/gets whether or not "remote" rendering is happening.
       * Inventor's auto-render-caching algorithm will choose to cache more often
       * when rendering is remote (the assumption being that performance will be
       * better with display lists stored on the remote machine).
       * By default, it is assumed rendering is NOT remote.
       *
       * @param flag
       */
      ////////////////////////////////////////////////////////////////////////
       //
       // Description:
       //    Sets whether rendering is remote or local.
       //
       // Use: public

      public void
       setRenderingIsRemote(boolean flag)
       //
       ////////////////////////////////////////////////////////////////////////
       {
           remoteRendering = flag;
       }

      ////////////////////////////////////////////////////////////////////////
       //
       // Description:
       //    Gets whether renering is remote or local.
       //
       // Use: public

      public boolean
       getRenderingIsRemote()
       //
       ////////////////////////////////////////////////////////////////////////
       {
           return remoteRendering;
       }


      ////////////////////////////////////////////////////////////////////////
       //
       // Description:
       //    Sets transparency quality level to use when rendering.
       //
       // Use: public

      public void
       setTransparencyType(TransparencyType type)
       //
       ////////////////////////////////////////////////////////////////////////
       {
           if (transpType != type) {
               transpType = type;
               pimpl.needglinit = true;
               whatChanged |= flags.TRANSPARENCY_TYPE.getValue();
           }
       }

      //! Sets/returns transparency quality level to use when rendering. The
           //! default is SCREEN_DOOR. (Note that SCREEN_DOOR transparency does not work
           //! in the case where transparency values are specified for each vertex
           //! of a shape. If this is the case, use one of the other transparency types.)
      public TransparencyType    getTransparencyType() { return transpType; }


      ////////////////////////////////////////////////////////////////////////
       //
       // Description:
       //    Turns on/off smoothing (cheap anti-aliasing). This may change
       //    the state of alpha blending to make the smoothing work.
       //
       // Use: public

      public void
       setSmoothing(boolean smooth)
       //
       ////////////////////////////////////////////////////////////////////////
       {
           if (doSmooth != smooth) {
               doSmooth = smooth;
               pimpl.needglinit = true;
               pimpl.smoothing = smooth;
               whatChanged |= flags.SMOOTHING.getValue();
           }
       }

      //! Sets/returns smoothing flag. When on, smoothing uses OpenGL's line-
           //! and point-smoothing features to provide cheap antialiasing of lines
           //! and points. The default is false.
      public     boolean              isSmoothing() { return doSmooth; }

      //! \see getNumPasses
      public     void                setNumPasses(int num)           { numPasses = num;  }

      //! Sets/returns number of rendering passes for multipass rendering.
           //! Specifying more than one pass will result in antialiasing of the
           //! rendered scene, using OpenGL's accumulation buffer. (Camera nodes
           //! typically move their viewpoints a little bit for each pass to achieve
           //! the antialiasing.)  Each additional pass provides better
           //! antialiasing, but requires more rendering time The default is 1 pass.
      public int                 getNumPasses() { return numPasses; }

      //! \see isPassUpdate
      public     void                setPassUpdate(boolean flag)      { passUpdate = flag; }
           //! Sets/returns a flag indicating whether intermediate results are
           //! displayed after each antialiasing pass for progressive improvement
           //! (default is false).
           public boolean              isPassUpdate()             { return passUpdate; }


      //! Sets a callback function to invoke between passes when antialiasing.
           //! Passing NULL (which is the default state) will cause a clear of the color
           //! and depth buffers to be performed.
      public     void                setPassCallback(SoGLRenderPassCB funcArg, Object userData)
               { passCB = funcArg; passData = userData; }

      /**
       * Sets/returns the OpenGL cache context.
       * A cache context is just an integer identifying when OpenGL display lists
       * (which are used for render caching) can be shared between render actions.
       *
       * @return
       */
      ////////////////////////////////////////////////////////////////////////
       //
       // Description:
       //    Gets cache context
       //
       // Use: public

      public int
       getCacheContext()
       //
       ////////////////////////////////////////////////////////////////////////
       {
           return cacheContext;
       }

    //! Returns current rendering pass number
    public int                 getCurPass()  { return curPass; }

      //! Returns true if render action should abort - checks user callback
      public     boolean              abortNow()
               { return (hasTerminated() || (abortCB != null && checkAbort())); }

      ////////////////////////////////////////////////////////////////////////
       //
       // Description:
       //    Returns true if render action should abort based on calling
       //    callback. This assumes the callback is not NULL.
       //
       // Use: private

      private boolean
       checkAbort()
       //
       ////////////////////////////////////////////////////////////////////////
       {
           boolean doAbort;

           switch (abortCB.abort(abortData)) {

             case CONTINUE:
               doAbort = false;
               break;

             case ABORT:
               // Mark the action has having terminated
               setTerminated(true);
               doAbort = true;
               break;

             case PRUNE:
               // Don't mark anything, but return true. This will tell the
               // node not to render itself.
               doAbort = true;
               break;

             case DELAY:
               // Add the current path to the list of delayed paths
               delayedPaths.append(getCurPath().copy());      // Also refs the path
               doAbort = true;
               break;

               default:
            	   throw new IllegalStateException("checkAbort : Illegal abort code");
           }

           return doAbort;
       }
      ////////////////////////////////////////////////////////////////////////
       //
       // Description:
       //    Initializes the SoGLRenderAction class.
       //
       // Use: internal

      public static void
       initClass()
       //
       ////////////////////////////////////////////////////////////////////////
       {
           //SO_ACTION_INIT_CLASS(SoGLRenderAction, SoAction);
	       enabledElements = new SoEnabledElementsList(SoAction.enabledElements);
	       methods = new SoActionMethodList(SoAction.methods);
	       classTypeId    = SoType.createType(SoAction.getClassTypeId(),
	                                           new SbName("SoGLRenderAction"), null);

           //SO_ENABLE(SoGLRenderAction, SoGLLazyElement);
	       SoGLRenderAction.enableElement(SoGLLazyElement.class);

           //SO_ENABLE(SoGLRenderAction, SoGLRenderPassElement);
	       SoGLRenderAction.enableElement(SoGLRenderPassElement.class);

           //SO_ENABLE(SoGLRenderAction, SoViewportRegionElement);
	       SoGLRenderAction.enableElement(SoViewportRegionElement.class);

           //SO_ENABLE(SoGLRenderAction, SoWindowElement);
	       SoGLRenderAction.enableElement(SoWindowElement.class);

	       // COIN 3D
	       //SO_ENABLE(SoGLRenderAction.class, SoDecimationPercentageElement.class);
	       //SO_ENABLE(SoGLRenderAction.class, SoDecimationTypeElement.class);
	       SO_ENABLE(SoGLRenderAction.class, SoGLLightIdElement.class);
	       SO_ENABLE(SoGLRenderAction.class, SoGLRenderPassElement.class);
	       SO_ENABLE(SoGLRenderAction.class, SoGLUpdateAreaElement.class);
	       SO_ENABLE(SoGLRenderAction.class, SoLazyElement.class);
	       SO_ENABLE(SoGLRenderAction.class, SoOverrideElement.class);
	       SO_ENABLE(SoGLRenderAction.class, SoTextureOverrideElement.class);
	       SO_ENABLE(SoGLRenderAction.class, SoWindowElement.class);
	       SO_ENABLE(SoGLRenderAction.class, SoGLViewportRegionElement.class);
	       SO_ENABLE(SoGLRenderAction.class, SoGLCacheContextElement.class);

       }

    //! Returns true if render action handles processing of a
    //! transparent object (if it is to be sorted and rendered later).
    //! If this returns false, the object should just go ahead and
    //! render itself.
    //! The optional argument isTransparent ensures that the object being
    //! rendered will be taken as transparent, regardless of transparency
    //! in the state.  If it is false, the state is checked for transparency.
    public boolean              handleTransparency() {
    	return handleTransparency(false);
    }

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns true if render action handles processing of a
//    transparent object (if it is to be sorted and rendered later).
//    If this returns false, the object should just go ahead and
//    render itself.
//    If the argument isTransparent is true, the object is assumed to
//    be transparent, regardless of the state.
//
// Use: extender

public boolean
handleTransparency(boolean isTransparent)
//
////////////////////////////////////////////////////////////////////////
{
    boolean      ret;

    // Nothing special to do for screen-door blending
    if (transpType == TransparencyType.SCREEN_DOOR) {
		return false;
	}

    // Determine if the object is likely to be transparent. This is
    // true if: there are several transparency values in the state or
    // the first one is non-zero; there is a texture map that affects
    // transparency; or the diffuse colors are specified as packed
    // values (which contain alpha).
    if (isTransparent ||
        (SoLazyElement.getInstance(getState()).isTransparent()) ||
        (SoMultiTextureImageElement.containsTransparency(getState()))) {

        // If transparency is delayed, add a path to this object to
        // the list of transparent objects, and tell the shape not to
        // render
        if (delayObjs) {
            SoPath        curPath = getCurPath();

            // For some group nodes (such as Array and MultipleCopy),
            // the children are traversed more than once. In this
            // case, don't add the path if it is the same as any of
            // the previous ones.
            boolean      isCopy = false;
            int         i;

            for (i = 0; i < transpPaths.getLength(); i++) {
                if (curPath.operator_equals(transpPaths.operator_square_bracket(i))) {
                    isCopy = true;
                    break;
                }
            }

            // Add path if not already there
            if (! isCopy)
			 {
				transpPaths.append(curPath.copy());    // Also refs the path
			}

            // We also need to make sure that any open caches are
            // invalidated; if they aren't, they will skip this
            // object and (since the cache replaces traversal),
            // this object will not be rendered delayed at all.

            if (getState().isCacheOpen()) {
				SoCacheElement.invalidate(getState());
			}

            ret = true;
        }

        // If transparency is not delayed, enable blending
        else {
            //enableBlending(true);
            ret = false;
        }
    }

    // Disable blending, otherwise
    else {
        //enableBlending(false);
        ret = false;
    }

    return ret;

    }


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Adds to the list of paths to render after all other stuff
//    (including delayed/sorted transparent objects) have been
//    rendered. (Used for annotation nodes.)
//
// Use: internal

public void
addDelayedPath(SoPath path)
//
////////////////////////////////////////////////////////////////////////
{
    delayedPaths.append(path);
}



////////////////////////////////////////////////////////////////////////
//
// Description:
//    Enables or disables GL blending. Remembers previous setting to
//    avoid sending commands unnecessarily.
//
// Use: private

//private void
//enableBlending(boolean enable)
////
//////////////////////////////////////////////////////////////////////////
//{
//    SoLazyElement.setBlending(state, enable);
//}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Initiates action on a graph.
//
// Use: protected

@Override
public void
beginTraversal(SoNode node)
//
////////////////////////////////////////////////////////////////////////
{
    // Init OpenGL if needed (this is only done once)
    // NOTE: The SoGLRenderAction.apply() is the only scope in
    //       which OIV has a valid current OpenGL context (e.g. from SoQt or another window binding),
    //       so this is the right place to initialize OpenGL/GLEW:
    SbOpenGL.init();
    
    if (pimpl.needglinit) {
        pimpl.needglinit = false;
        
        GL2 gl2 = new GL2() {};

        // we are always using GL_COLOR_MATERIAL in Coin
        gl2.glColorMaterial(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE);
        gl2.glEnable(GL2.GL_COLOR_MATERIAL);
        gl2.glEnable(GL2.GL_NORMALIZE);

        // initialize the depth function to the default Coin/Inventor
        // value.  SoGLDepthBufferElement doesn't check for this, it just
        // assumes that the function is initialized to GL_LEQUAL, which is
        // not correct (the OpenGL specification says the initial value is
        // GL_LESS, but I've seen drivers that defaults to GL_LEQUAL as
        // well).
        gl2.glDepthFunc(GL2.GL_LEQUAL);

        if (pimpl.smoothing) {
          gl2.glEnable(GL_POINT_SMOOTH);
          gl2.glEnable(GL_LINE_SMOOTH);
        }
        else {
          gl2.glDisable(GL_POINT_SMOOTH);
          gl2.glDisable(GL_LINE_SMOOTH);
        }
      }

    

    // This is called either from the main call to apply() that is
    // used to render a graph OR from the apply() call made while
    // rendering transparent objects or delayed objects. In the first
    // case, we want to render all passes. In the second and third
    // cases, we want to render only the current pass. We can tell
    // these cases apart by examining the flags.

    if (renderingTranspObjs || renderingDelPaths) {
		traverse(node);
	} else {
		renderAllPasses(node);
	}
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does actual rendering of all passes starting at a node.
//
// Use: private

private void
renderAllPasses(SoNode node)
//
////////////////////////////////////////////////////////////////////////
{
	GL2 gl2 = Ctx.get(getCacheContext());

    // If anything has changed since the last time this action was
    // applied, make sure it is set up correctly in GL.
    if (whatChanged != 0) {

        switch (transpType) {
          case SCREEN_DOOR:
            if (doSmooth) {
                // Blending has to be enabled for line smoothing to
                // work properly
                gl2.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                //enableBlending(true);
            } else {
				//enableBlending(false);
			}
            break;

          case ADD:
          case DELAYED_ADD:
          case SORTED_OBJECT_ADD:
            gl2.glBlendFunc(GL_SRC_ALPHA, GL_ONE);
            break;

          case BLEND:
          case DELAYED_BLEND:
          case SORTED_OBJECT_BLEND:
            gl2.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            break;
        }

        sortObjs = (transpType == TransparencyType.SORTED_OBJECT_ADD ||
                    transpType == TransparencyType.SORTED_OBJECT_BLEND);
        delayObjs = (sortObjs ||
                     transpType == TransparencyType.DELAYED_ADD ||
                     transpType == TransparencyType.DELAYED_BLEND);

        if (doSmooth) {
            gl2.glEnable(GL_POINT_SMOOTH);
            gl2.glEnable(GL_LINE_SMOOTH);
        }
        else {
        	gl2.glDisable(GL_POINT_SMOOTH);
        	gl2.glDisable(GL_LINE_SMOOTH);
        }

        // Reset flags to indicate that everything is up to date
        whatChanged = 0;
    }

    // Set the GL cache context:
    SoGLCacheContextElement.set(state, cacheContext, delayObjs,
                                 remoteRendering);

    // Set the transparency bit in the ShapeStyle element
    // and the lazy element.
    SoShapeStyleElement.setTransparencyType(state,transpType.getValue());
    SoLazyElement.setTransparencyType(state, transpType.getValue());

    // Simple case of one pass
    if (getNumPasses() == 1) {
        renderPass(node, 0);
        return;
    }

    int         pass;
    float       passFrac = 1.0f / getNumPasses();

    for (pass = 0; pass < getNumPasses(); pass++) {

        // Stuff to do between passes:
        if (pass > 0) {
            // Update the buffer after each pass if requested
            if (passUpdate) {
				gl2.glAccum(GL_RETURN, (float) getNumPasses() / (float) pass);
			}

            // If user-defined callback exists, call it. Otherwise,
            // clear to current clear color and depth buffer clear value
            if (passCB != null) {
				(passCB).run(gl2,passData);
			} else {
				gl2.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			}
        }

        renderPass(node, pass);

        // Stop if rendering was aborted
        if (hasTerminated()) {
			return;
		}

        if (pass > 0) {
			gl2.glAccum(GL_ACCUM, passFrac);
		} else {
			gl2.glAccum(GL_LOAD,  passFrac);
		}
    }

    gl2.glAccum(GL_RETURN, 1.0f);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does one pass of rendering starting at a node.
//
// Use: private

private void
renderPass(SoNode node, int pass)
//
////////////////////////////////////////////////////////////////////////
{
    // Set the current pass in the instance and in the state
    curPass = pass;
    SoGLRenderPassElement.set(getState(), pass);

    // Set the viewport region
    SoViewportRegionElement.set(getState(), vpRegion);
    SoGLUpdateAreaElement.set(getState(), updateOrigin, updateSize);

    // Do the actual rendering
    traverse(node);

    // For delayed (or sorted) transparency, see if any transparent
    // objects were added
    if (delayObjs && transpPaths.getLength() > 0 && ! hasTerminated()) {

        // Make sure blending is enabled if necessary
        if (transpType != TransparencyType.SCREEN_DOOR) {
			//enableBlending(true);
		}

        renderTransparentObjs();

        // Disable blending for next pass
        if (transpType != TransparencyType.SCREEN_DOOR) {
			//enableBlending(false);
		}
    }

    // Delayed paths
    if (delayedPaths.getLength() > 0 && ! hasTerminated()) {
        renderingDelPaths = true;

        // Render paths to delayed objects. We know these paths obey
        // the rules for compact path lists, so let the action know
        apply(delayedPaths, true);

        // Clear out the list
        delayedPaths.truncate(0);

        renderingDelPaths = false;
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Renders delayed objects that have been marked as transparent.
//    This sorts them if necessary. This should be called only if we
//    are delaying transparent objects and there is at least one
//    transparent object.
//
// Use: private

private void
renderTransparentObjs()
//
////////////////////////////////////////////////////////////////////////
{
    int i, numObjs = transpPaths.getLength(), numToDo;

    GL2 gl2 = Ctx.get(getCacheContext());

    // Indicate that we are doing transparent objects so we know not
    // to render all passes
    renderingTranspObjs = true;

    // Indicate that objects are not to be delayed. (So they will render.)
    delayObjs = false;

    // Don't write into z buffer so that ALL transparent objects will
    // be drawn. This makes things look better, even if sorting is not
    // on or if sorting gives the incorrect order.
    gl2.glDepthMask(false);

    // If not sorting, just render them in order
    if (! sortObjs) {
		// Render paths to transparent objects. We know these paths
        // obey the rules for compact path lists, so let the action know.
        apply(transpPaths, true);
	} else {
        if (ba == null) {
            ba = new SoGetBoundingBoxAction(vpRegion);

            // Make sure bounding boxes are in camera space. This
            // means the z coordinates of the bounding boxes indicate
            // distance from the camera.
            ba.setInCameraSpace(true);
        }

        // Make sure there is room for the bounding boxes
        if (bboxes == null) {
            bboxes = new SbBox3f[numObjs];
            numBBoxes = numObjs;
        }
        else if (numBBoxes < numObjs) {
            //delete [] bboxes; java port
            bboxes = new SbBox3f[numObjs];
            numBBoxes = numObjs;
        }

        for (i = 0; i < numObjs; i++) {
            ba.apply(transpPaths.operator_square_bracket(i));
            bboxes[i] = ba.getBoundingBox();
        }

        // Render them in sorted order
        for (numToDo = numObjs; numToDo > 0; --numToDo) {
            int         farthest = -1;
            float       zFar;

            // Use selection sort, since number of objects is usually small

            // Look for bbox with smallest zmax (farthest from camera!)
            zFar = Float.MAX_VALUE;
            for (i = 0; i < numObjs; i++) {
                if (bboxes[i].getMax().getValueRead()[2] < zFar) {
                    zFar = bboxes[i].getMax().getValueRead()[2];
                    farthest = i;
                }
            }

            // Render farthest one
            apply(transpPaths.operator_square_bracket(farthest));

            // Mark it as being far
            bboxes[farthest].getMax().setValue(2, Float.MAX_VALUE);
        }
    }

    // Restore zwritemask to what we assume it was before...
    gl2.glDepthMask(true);

    // Get ready for next time
    delayObjs = true;
    transpPaths.truncate(0);
    renderingTranspObjs = false;
}

    //! Returns TRUE if currently rendering delayed paths
    public boolean              isRenderingDelayedPaths()
        { return renderingDelPaths; }

    public int                 getCullTestResults() { return cullBits; }
    public void                setCullTestResults(int b) { cullBits = b; }

	private boolean transparencyrender; // COIN 3D

	/*
	 * !
	 *
	 * Returns TRUE if the action is currently rendering delayed or sorted
	 * transparent objects.
	 *
	 * \since Coin 3.0
	 */
	public boolean isRenderingTranspPaths() {
		return this.transparencyrender;
	}


/*!
  Adds a callback which is invoked right before the scene graph traversal
  starts. All necessary GL initialization is then done (e.g. the viewport
  is correctly set), and this callback can be useful to, for instance,
  clear the viewport before rendering, or draw a bitmap in the background
  before rendering etc.

  The callback is only invoked once (before the first rendering pass)
  when multi pass rendering is enabled.

  Please note that SoSceneManager usually adds a callback to clear the
  GL buffers in SoSceneManager::render(). So, if you plan to for
  instance draw an image in the color buffer using this callback, you
  should make sure that the scene manager doesn't clear the buffer.
  This can be done either by calling SoSceneManager::render() with
  both arguments FALSE, or, if you're using one of our GUI toolkits
  (SoXt/SoQt/SoGtk/SoWin), call setClearBeforeRender() on the viewer.

  This method is an extension versus the Open Inventor API.

  \sa removePreRenderCallback().
*/
public void
addPreRenderCallback(SoGLPreRenderCB func, Object userdata)
{
  this.pimpl.precblist.addCallback((new SoCallbackListCB() {

	@Override
	public void invoke(Object callbackData) {
		func.apply(userdata, (SoGLRenderAction)callbackData);
	}
	  
  }), userdata);
}

/*!
  Removed a callback added with the addPreRenderCallback() method.

  This method is an extension versus the Open Inventor API.

  \sa addPreRenderCallback()
*/
public void
removePreRenderCallback(SoGLPreRenderCB func, Object userdata)
{
  this.pimpl.precblist.removeCallback((SoCallbackListCB)(func), userdata);
}


}
