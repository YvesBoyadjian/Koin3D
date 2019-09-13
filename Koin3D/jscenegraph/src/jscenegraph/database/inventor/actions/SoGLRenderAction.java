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

import jscenegraph.coin3d.glue.cc_glglue;
import jscenegraph.coin3d.inventor.elements.SoMultiTextureEnabledElement;
import jscenegraph.coin3d.inventor.elements.SoMultiTextureImageElement;
import jscenegraph.coin3d.inventor.nodes.SoTransparencyType;
import jscenegraph.coin3d.misc.SoGL;
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
import jscenegraph.database.inventor.elements.SoProjectionMatrixElement;
import jscenegraph.database.inventor.elements.SoShapeHintsElement;
import jscenegraph.database.inventor.elements.SoShapeStyleElement;
import jscenegraph.database.inventor.elements.SoTextureOverrideElement;
import jscenegraph.database.inventor.elements.SoViewportRegionElement;
import jscenegraph.database.inventor.elements.SoWindowElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.misc.SoCallbackListCB;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoGroup;
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

    static int COIN_GLBBOX = 0;

    //! Various levels of transparency rendering quality
       public enum TransparencyType {
           SCREEN_DOOR,            //!< Use GL patterns for screen-door transparency
           ADD,                    //!< Use additive GL alpha blending
           DELAYED_ADD,            //!< Use additive blending, do transp objs last
           SORTED_OBJECT_ADD,      //!< Use additive blending, sort objects by bbox
           BLEND,                  //!< Use GL alpha blending
           DELAYED_BLEND,          //!< Use GL alpha blending, do transp objs last
          SORTED_OBJECT_BLEND,     //!< Use GL alpha blending, sort objects by bbox
          // The remaining are Coin extensions to the common Inventor API
          SORTED_OBJECT_SORTED_TRIANGLE_ADD,
          SORTED_OBJECT_SORTED_TRIANGLE_BLEND,
          NONE, // COIN 3D
          SORTED_LAYERS_BLEND; // COIN 3D

           public int getValue() {
        	   return ordinal();
           }

		public static TransparencyType fromValue(int value) {
			return values()[value];
		}
      };

      enum TransparentDelayedObjectRenderType {
    	    ONE_PASS,
    	    NONSOLID_SEPARATE_BACKFACE_PASS
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

	       //! Variables for transparency, smoothing, and multi-pass rendering:

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

         pimpl.transparencytype          = TransparencyType./*SCREEN_DOOR*/BLEND;

        whatChanged         = flags.ALL.getValue();

        // These three bits keep track of which view-volume planes we need
        // to test against; by default, all bits are 1.
        cullBits            = 7;

        pimpl.action = this;
        
        // Can't just push this on the SoViewportRegionElement stack, as the
        // state hasn't been made yet.
        pimpl.viewport.copyFrom(viewportRegion);

        pimpl.passcallback = null;
        pimpl.passcallbackdata = null;
        pimpl.smoothing = false;
        pimpl.currentpass = 0;
        pimpl.numpasses = 1;
        pimpl.internal_multipass = false;
        pimpl.transparencytype = SoGLRenderAction.TransparencyType.BLEND;
        pimpl.delayedpathrender = false;
        pimpl.transparencyrender = false;
        pimpl.isrendering = false;
        pimpl.isrenderingoverlay = false;
        pimpl.passupdate = false;
        pimpl.bboxaction = new SoGetBoundingBoxAction(viewportRegion);
        pimpl.updateorigin.setValue(0.0f, 0.0f);
        pimpl.updatesize.setValue(1.0f, 1.0f);
        pimpl.rendering = SoGLRenderActionP.Rendering.RENDERING_UNSET;
        pimpl.abortcallback = null;
        pimpl.cachecontext = 0;
        pimpl.needglinit = true;
        pimpl.sortedlayersblendpasses = 4;
        pimpl.viewportheight = 0;
        pimpl.viewportwidth = 0;
        pimpl.sortedlayersblendinitialized = false;
        pimpl.sortedlayersblendcounter = 0;
        pimpl.usenvidiaregistercombiners = false;
        pimpl.cachedprofilingsg = null;
        pimpl.transpobjdepthwrite = false;
        pimpl.transpdelayedrendertype = TransparentDelayedObjectRenderType.ONE_PASS;
        pimpl.renderingtranspbackfaces = false;

//        pimpl.sortedobjectstrategy = BBOX_CENTER; TODO
//        pimpl.sortedobjectcb = null;
//        pimpl.sortedobjectclosure = null;
        
        
        pimpl.smoothing = false;
        pimpl.needglinit = true;
     }

     @Override
	public void destructor() {
    	    super.destructor();
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
    	  pimpl.viewport.copyFrom(newRegion);
    	  pimpl.bboxaction.setViewportRegion(newRegion);
     }


    /**
     * Returns viewport region to use for rendering.
     */
    //! Returns viewport region to use for rendering.
      public SbViewportRegion getViewportRegion() { return pimpl.viewport; }

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
           pimpl.updateorigin.copyFrom(origin);
           pimpl.updatesize.copyFrom(size);
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
           origin.copyFrom(pimpl.updateorigin);
           size.copyFrom(pimpl.updatesize);
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
           if (pimpl.cachecontext != context) {
               pimpl.cachecontext = context;
               invalidateState();
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
       setRenderingIsRemote(boolean isremote)
       //
       ////////////////////////////////////////////////////////////////////////
       {
    	  pimpl.rendering = isremote ?
    			    SoGLRenderActionP.Rendering.RENDERING_SET_INDIRECT :
    			    SoGLRenderActionP.Rendering.RENDERING_SET_DIRECT;
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
    	  boolean isdirect;
    	  if (pimpl.rendering == SoGLRenderActionP.Rendering.RENDERING_UNSET) {
    	    isdirect = true;
    	  }
    	  else {
    	    isdirect = pimpl.rendering == SoGLRenderActionP.Rendering.RENDERING_SET_DIRECT;
    	  }
    	  return !isdirect;
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
           if (pimpl.transparencytype != type) {
        	   pimpl.transparencytype = type;
               pimpl.needglinit = true;
               whatChanged |= flags.TRANSPARENCY_TYPE.getValue();
           }
       }

      //! Sets/returns transparency quality level to use when rendering. The
           //! default is SCREEN_DOOR. (Note that SCREEN_DOOR transparency does not work
           //! in the case where transparency values are specified for each vertex
           //! of a shape. If this is the case, use one of the other transparency types.)
      public TransparencyType    getTransparencyType() { return pimpl.transparencytype; }


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
           if (pimpl.smoothing != smooth) {
               pimpl.smoothing = smooth;
               pimpl.needglinit = true;
               pimpl.smoothing = smooth;
               whatChanged |= flags.SMOOTHING.getValue();
           }
       }

      //! Sets/returns smoothing flag. When on, smoothing uses OpenGL's line-
           //! and point-smoothing features to provide cheap antialiasing of lines
           //! and points. The default is false.
      public     boolean              isSmoothing() { return pimpl.smoothing; }

      //! \see getNumPasses
      public     void                setNumPasses(int num)           {
    	  pimpl.numpasses = num;
    	  pimpl.internal_multipass = num > 1;
    	  }

      //! Sets/returns number of rendering passes for multipass rendering.
           //! Specifying more than one pass will result in antialiasing of the
           //! rendered scene, using OpenGL's accumulation buffer. (Camera nodes
           //! typically move their viewpoints a little bit for each pass to achieve
           //! the antialiasing.)  Each additional pass provides better
           //! antialiasing, but requires more rendering time The default is 1 pass.
      public int                 getNumPasses() { return pimpl.numpasses; }

      //! \see isPassUpdate
      public     void                setPassUpdate(boolean flag)      { pimpl.passupdate = flag; }
           //! Sets/returns a flag indicating whether intermediate results are
           //! displayed after each antialiasing pass for progressive improvement
           //! (default is false).
           public boolean              isPassUpdate()             { return pimpl.passupdate; }


      //! Sets a callback function to invoke between passes when antialiasing.
           //! Passing null (which is the default state) will cause a clear of the color
           //! and depth buffers to be performed.
      public     void                setPassCallback(SoGLRenderPassCB funcArg, Object userData)
               { pimpl.passcallback = funcArg; pimpl.passcallbackdata = userData; }

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
           return pimpl.cachecontext;
       }

    //! Returns current rendering pass number
    public int                 getCurPass()  { return pimpl.currentpass; }

      //! Returns true if render action should abort - checks user callback
      public     boolean              abortNow()
               {
    	  if (this.hasTerminated()) return true;

//    	  #if COIN_DEBUG && 0 // for dumping the scene graph during GLRender traversals
//    	    static int debug = -1;
//    	    if (debug == -1) {
//    	      const char * env = coin_getenv("COIN_DEBUG_GLRENDER_TRAVERSAL");
//    	      debug = env && (atoi(env) > 0);
//    	    }
//    	    if (debug) {
//    	      const SoFullPath * p = (const SoFullPath *)this->getCurPath();
//    	      assert(p);
//    	      const int len = p->getLength();
//    	      for (int i=1; i < len; i++) { printf("  "); }
//    	      const SoNode * n = p->getTail();
//    	      assert(n);
//    	      printf("%p %s (\"%s\")\n",
//    	             n, n->getTypeId().getName().getString(),
//    	             n->getName().getString());
//    	    }
//    	  #endif // debug

    	    boolean abort = false;
    	    if (pimpl.abortcallback != null) {
    	      switch (pimpl.abortcallback.abort(pimpl.abortcallbackdata)) {
    	      case CONTINUE:
    	        break;
    	      case ABORT:
    	        this.setTerminated(true);
    	        abort = true;
    	        break;
    	      case PRUNE:
    	        // abort this node, but do not abort rendering
    	        abort = true;
    	        break;
    	      case DELAY:
    	        this.addDelayedPath(this.getCurPath().copy());
    	        // prune this node
    	        abort = true;
    	        break;
    	      }
    	    }
    	    return abort;
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
handleTransparency(boolean istransparent)
//
////////////////////////////////////////////////////////////////////////
{
	  SoState thestate = this.getState();
	  cc_glglue glue = SoGL.sogl_glue_instance(thestate);

	  SoGLRenderAction.TransparencyType transptype =
	    SoGLRenderAction.TransparencyType.fromValue(
	    SoShapeStyleElement.getTransparencyType(thestate)
	    );


	  if (pimpl.transparencytype == TransparencyType.SORTED_LAYERS_BLEND) {

	    // Do not cache anything. We must have full control!
	    SoCacheElement.invalidate(thestate);

	    pimpl.sortedlayersblendprojectionmatrix.copyFrom(
	      SoProjectionMatrixElement.get(thestate));

	    if (!SoMultiTextureEnabledElement.get(thestate, 0)) {
	      if (glue.has_arb_fragment_program && !pimpl.usenvidiaregistercombiners) {
	        pimpl.setupFragmentProgram();
	      }
	      else {
	        pimpl.setupRegisterCombinersNV();
	      }
	    }

	    // Must always return false as everything must be rendered to the
	    // RGBA layers (which are blended together at the end of each
	    // frame).
	    return false;
	  }


	  // check common cases first
	  if (!istransparent || transptype == SoGLRenderAction.TransparencyType.NONE || transptype == SoGLRenderAction.TransparencyType.SCREEN_DOOR) {
	    if (pimpl.smoothing) {
	      SoLazyElement.enableBlending(thestate, GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	    }
	    else SoLazyElement.disableBlending(thestate);
	    return false;
	  }

	  // below this point, shape is transparent, and we know that
	  // transparency type is not SCREEN_DOOR or NONE.

	  // for the transparency render pass(es) we should always render when
	  // we get here.
	  if (pimpl.transparencyrender) {
	    if (pimpl.transpdelayedrendertype == TransparentDelayedObjectRenderType.NONSOLID_SEPARATE_BACKFACE_PASS) {
	      if (this.isRenderingTranspBackfaces()) {
	        if (SoShapeHintsElement.getShapeType(this.state) == SoShapeHintsElement.ShapeType.SOLID) {
	          // just delay this until the next pass
	          return true;
	        }
	        else {
	          SoLazyElement.setBackfaceCulling(this.state, true);
	        }
	      }
	      else {
	        if (SoShapeHintsElement.getShapeType(this.state) != SoShapeHintsElement.ShapeType.SOLID) {
	          SoLazyElement.setBackfaceCulling(this.state, true);
	        }
	      }
	    }
	    pimpl.setupBlending(thestate, transptype);
	    return false;
	  }
	  // check for special case when rendering delayed paths.  we don't
	  // want to add these objects to the list of transparent objects, but
	  // render right away.
	  if (pimpl.delayedpathrender) {
	    pimpl.setupBlending(thestate, transptype);
	    return false;
	  }
	  switch (transptype) {
	  case /*SoGLRenderAction.*/ADD:
	    SoLazyElement.enableBlending(thestate, GL_SRC_ALPHA, GL_ONE);
	    return false;
	  case /*SoGLRenderAction.*/BLEND:
	    SoLazyElement.enableBlending(thestate, GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	    return false;
	  case /*SoGLRenderAction.*/DELAYED_ADD:
	  case /*SoGLRenderAction.*/DELAYED_BLEND:
	    pimpl.addTransPath(this.getCurPath().copy());
	    SoCacheElement.setInvalid(true);
	    if (thestate.isCacheOpen()) {
	      SoCacheElement.invalidate(thestate);
	    }
	    return true; // delay render
	  case /*SoGLRenderAction.*/SORTED_OBJECT_ADD:
	  case /*SoGLRenderAction.*/SORTED_OBJECT_BLEND:
	  case /*SoGLRenderAction.*/SORTED_OBJECT_SORTED_TRIANGLE_ADD:
	  case /*SoGLRenderAction.*/SORTED_OBJECT_SORTED_TRIANGLE_BLEND:
	    pimpl.addSortTransPath(this.getCurPath().copy());
	    SoCacheElement.setInvalid(true);
	    if (thestate.isCacheOpen()) {
	      SoCacheElement.invalidate(thestate);
	    }
	    return true; // delay render
	  default:
	    throw new IllegalStateException( "should not get here");
	    //break;
	  }
	  //return false;
	}


//{
//    boolean      ret;
//
//    // Nothing special to do for screen-door blending
//    if (transpType == TransparencyType.SCREEN_DOOR) {
//		return false;
//	}
//
//    // Determine if the object is likely to be transparent. This is
//    // true if: there are several transparency values in the state or
//    // the first one is non-zero; there is a texture map that affects
//    // transparency; or the diffuse colors are specified as packed
//    // values (which contain alpha).
//    if (isTransparent ||
//        (SoLazyElement.getInstance(getState()).isTransparent()) ||
//        (SoMultiTextureImageElement.containsTransparency(getState()))) {
//
//        // If transparency is delayed, add a path to this object to
//        // the list of transparent objects, and tell the shape not to
//        // render
//        if (delayObjs) {
//            SoPath        curPath = getCurPath();
//
//            // For some group nodes (such as Array and MultipleCopy),
//            // the children are traversed more than once. In this
//            // case, don't add the path if it is the same as any of
//            // the previous ones.
//            boolean      isCopy = false;
//            int         i;
//
//            for (i = 0; i < transpPaths.getLength(); i++) {
//                if (curPath.operator_equals(transpPaths.operator_square_bracket(i))) {
//                    isCopy = true;
//                    break;
//                }
//            }
//
//            // Add path if not already there
//            if (! isCopy)
//			 {
//				transpPaths.append(curPath.copy());    // Also refs the path
//			}
//
//            // We also need to make sure that any open caches are
//            // invalidated; if they aren't, they will skip this
//            // object and (since the cache replaces traversal),
//            // this object will not be rendered delayed at all.
//
//            if (getState().isCacheOpen()) {
//				SoCacheElement.invalidate(getState());
//			}
//
//            ret = true;
//        }
//
//        // If transparency is not delayed, enable blending
//        else {
//            enableBlending(true);
//            ret = false;
//        }
//    }
//
//    // Disable blending, otherwise
//    else {
//        enableBlending(false);
//        ret = false;
//    }
//
//    return ret;
//
//    }


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
	  SoState thestate = this.getState();
	  SoCacheElement.invalidate(thestate);
	  assert(!pimpl.delayedpathrender);
	  pimpl.delayedpaths.append(path);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Initiates action on a graph.
//
// Use: protected

//@Override
//public void
//beginTraversal(SoNode node)
////
//////////////////////////////////////////////////////////////////////////
//{
//    // Init OpenGL if needed (this is only done once)
//    // NOTE: The SoGLRenderAction.apply() is the only scope in
//    //       which OIV has a valid current OpenGL context (e.g. from SoQt or another window binding),
//    //       so this is the right place to initialize OpenGL/GLEW:
//    SbOpenGL.init();
//    
//    if (pimpl.needglinit) {
//        pimpl.needglinit = false;
//        
//        GL2 gl2 = new GL2() {};
//
//        // we are always using GL_COLOR_MATERIAL in Coin
//        gl2.glColorMaterial(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE);
//        gl2.glEnable(GL2.GL_COLOR_MATERIAL);
//        gl2.glEnable(GL2.GL_NORMALIZE);
//
//        // initialize the depth function to the default Coin/Inventor
//        // value.  SoGLDepthBufferElement doesn't check for this, it just
//        // assumes that the function is initialized to GL_LEQUAL, which is
//        // not correct (the OpenGL specification says the initial value is
//        // GL_LESS, but I've seen drivers that defaults to GL_LEQUAL as
//        // well).
//        gl2.glDepthFunc(GL2.GL_LEQUAL);
//
//        if (pimpl.smoothing) {
//          gl2.glEnable(GL_POINT_SMOOTH);
//          gl2.glEnable(GL_LINE_SMOOTH);
//        }
//        else {
//          gl2.glDisable(GL_POINT_SMOOTH);
//          gl2.glDisable(GL_LINE_SMOOTH);
//        }
//      }
//
//    
//
//    // This is called either from the main call to apply() that is
//    // used to render a graph OR from the apply() call made while
//    // rendering transparent objects or delayed objects. In the first
//    // case, we want to render all passes. In the second and third
//    // cases, we want to render only the current pass. We can tell
//    // these cases apart by examining the flags.
//
//    if (renderingTranspObjs || renderingDelPaths) {
//		traverse(node);
//	} else {
//		renderAllPasses(node);
//	}
//}



// Documented in superclass. Overridden from parent class to
// initialize the OpenGL state.
public void
beginTraversal(SoNode node)
{
  if (pimpl.cachedprofilingsg == null) {
    if (node.isOfType(SoGroup.getClassTypeId()) &&
        ((SoGroup)(node)).getNumChildren() > 0) {
      pimpl.cachedprofilingsg = node;

//#ifdef HAVE_NODEKITS TODO
//      SoNode * kit = SoActionP::getProfilerOverlay();
//      if (kit) {
//        SoSearchAction sa;
//        sa.setType(SoProfilerVisualizeKit::getClassTypeId());
//        sa.setSearchingAll(TRUE);
//        sa.setInterest(SoSearchAction::ALL);
//        SbBool oldchildsearch = SoBaseKit::isSearchingChildren();
//        SoBaseKit::setSearchingChildren(TRUE);
//        sa.apply(kit);
//        SoBaseKit::setSearchingChildren(oldchildsearch);
//        SoPathList plist = sa.getPaths();
//        for (int i = 0, n = plist.getLength(); i < n; ++i) {
//          SoFullPath * path = reclassify_cast<SoFullPath *>(plist[i]);
//          SoNode * tail = path->getTail();
//          if ((tail != null) &&
//              (tail->isOfType(SoProfilerVisualizeKit::getClassTypeId()))) {
//            SoProfilerVisualizeKit * viskit = coin_assert_cast<SoProfilerVisualizeKit *>(tail);
//            viskit->root.setValue(node);
//          }
//        }
//      }
//#endif // HAVE_NODEKITS
    }
  }

  if (pimpl.isrendering) {
    if (pimpl.isrenderingoverlay)
      this.traverse(node);
    else
      super.beginTraversal(node);
    return;
  }

  // If the environment variable COIN_GLBBOX is set to 1, apply a bbox
  // action before rendering.  This will make sure bounding box caches
  // are updated (needed for view frustum culling). The default
  // SoQt/SoWin/SoXt viewers will also apply a SoGetBoundingBoxAction
  // so we don't do this by default yet.
  if (COIN_GLBBOX != 0) {
    pimpl.bboxaction.apply(node);
  }
  int err_before_init = GL2.GL_NO_ERROR;

  SoState thestate = this.getState(); // java port
  GL2 gl2 = thestate.getGL2();
  
  if (SoGL.sogl_glerror_debugging()) {
    err_before_init = gl2.glGetError();
  }
  if (pimpl.needglinit) {
    pimpl.needglinit = false;

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

  int err_after_init = GL2.GL_NO_ERROR;

  if (SoGL.sogl_glerror_debugging()) {
    err_after_init = gl2.glGetError();
  }

  if (/*COIN_DEBUG &&*/ ((err_before_init != GL2.GL_NO_ERROR) || (err_after_init != GL2.GL_NO_ERROR))) {
    int err = (err_before_init != GL2.GL_NO_ERROR) ? err_before_init : err_after_init;
    SoDebugError.postWarning("SoGLRenderAction::beginTraversal",
                              "GL error "+((err_before_init != GL2.GL_NO_ERROR) ? "before" : "after")+" initialization: "+                              
                              cc_glglue.coin_glerror_string(err));
  }

  pimpl.render(node);
  // GL errors after rendering will be caught in SoNode::GLRenderS().
}

// Documented in superclass. Overridden from parent class to clean up
// the lists of objects which were included in the delayed rendering.
public void
endTraversal(SoNode node)
{
  super.endTraversal(node);
//  if (SoProfilerP.shouldContinuousRender()) { TODO
//    float delay = SoProfilerP.getContinuousRenderDelay();
//    if (delay == 0.0f) {
//      node.touch();
//    } else {
//      if (pimpl.redrawSensor.get() == null) {
//        pimpl.redrawSensor.reset(new SoAlarmSensor());
//      }
//      if (pimpl.redrawSensor.isScheduled()) {
//        pimpl.redrawSensor.unschedule();
//      }
//      pimpl.redrawSensor.setFunction(SoGLRenderActionP::redrawSensorCB);
//      pimpl.redrawSensor.setData(node);
//      pimpl.redrawSensor.setTimeFromNow(new SbTime((double)(delay)));
//      pimpl.redrawSensor.schedule();
//      if (pimpl.deleteSensor.get() == null) {
//        pimpl.deleteSensor.reset(new SoNodeSensor());
//      }
//      pimpl.deleteSensor.setDeleteCallback(SoGLRenderActionP::deleteNodeCB, pimpl));
//      pimpl.deleteSensor.attach(node);
//
//    }
//  }
}

    //! Returns true if currently rendering delayed paths
    public boolean              isRenderingDelayedPaths()
        {
    	  return pimpl.delayedpathrender;
    	}

    public int                 getCullTestResults() { return cullBits; }
    public void                setCullTestResults(int b) { cullBits = b; }

	/*
	 * !
	 *
	 * Returns true if the action is currently rendering delayed or sorted
	 * transparent objects.
	 *
	 * \since Coin 3.0
	 */
	public boolean isRenderingTranspPaths() {
		return pimpl.transparencyrender;
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
  both arguments false, or, if you're using one of our GUI toolkits
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


/*!
  Returns TRUE if the action is currently rendering backfacing polygons
  in NONSOLID_SEPARATE_BACKFACE_PASS mode.

  \since Coin 3.0
*/
public boolean
isRenderingTranspBackfaces()
{
  return pimpl.renderingtranspbackfaces;
}


}
