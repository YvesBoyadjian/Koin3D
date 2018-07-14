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
 |      Abstract base camera node class
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import static com.jogamp.opengl.GL.GL_FRONT;
import static com.jogamp.opengl.GL2.GL_LINE_STIPPLE;
import static com.jogamp.opengl.GL2.GL_POLYGON_STIPPLE;
import static com.jogamp.opengl.GL2GL3.GL_FILL;
import static com.jogamp.opengl.GL2GL3.GL_LINE;

import com.jogamp.opengl.GL2;

import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbRotation;
import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SbVec2fSingle;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbViewVolume;
import jscenegraph.database.inventor.SbViewportRegion;
import jscenegraph.database.inventor.SoPath;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoGetBoundingBoxAction;
import jscenegraph.database.inventor.actions.SoHandleEventAction;
import jscenegraph.database.inventor.actions.SoRayPickAction;
import jscenegraph.database.inventor.elements.SoFocalDistanceElement;
import jscenegraph.database.inventor.elements.SoGLCacheContextElement;
import jscenegraph.database.inventor.elements.SoGLLazyElement;
import jscenegraph.database.inventor.elements.SoGLProjectionMatrixElement;
import jscenegraph.database.inventor.elements.SoGLRenderPassElement;
import jscenegraph.database.inventor.elements.SoGLUpdateAreaElement;
import jscenegraph.database.inventor.elements.SoGLViewingMatrixElement;
import jscenegraph.database.inventor.elements.SoGLViewportRegionElement;
import jscenegraph.database.inventor.elements.SoModelMatrixElement;
import jscenegraph.database.inventor.elements.SoProjectionMatrixElement;
import jscenegraph.database.inventor.elements.SoViewVolumeElement;
import jscenegraph.database.inventor.elements.SoViewingMatrixElement;
import jscenegraph.database.inventor.elements.SoViewportRegionElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFEnum;
import jscenegraph.database.inventor.fields.SoSFFloat;
import jscenegraph.database.inventor.fields.SoSFRotation;
import jscenegraph.database.inventor.fields.SoSFVec3f;
import jscenegraph.database.inventor.misc.SoState;


////////////////////////////////////////////////////////////////////////////////
//! Abstract base class for camera nodes.
/*!
\class SoCamera
\ingroup Nodes
This is the abstract base class for all camera nodes. It defines the
common methods and fields that all cameras have. Cameras are used to
view a scene. When a camera is encountered during rendering, it sets
the projection and viewing matrices and viewport appropriately; it
does not draw geometry. Cameras should be placed before any shape
nodes or light nodes in a scene graph; otherwise, those shapes or
lights cannot be rendered properly. Cameras are affected by the
current transformation, so you can position a camera by placing a
transformation node before it in the scene graph . The default
position and orientation of a camera is at (0,0,1) looking along the
negative z-axis.


You can also use a node kit to create a camera; see the reference page for
SoCameraKit.

\par See Also
\par
SoOrthographicCamera, SoPerspectiveCamera, SoCameraKit
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public abstract class SoCamera extends SoNode {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoCamera.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoCamera.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoCamera.class); }    	  	
	
///////////////////////////////////////////////////////////////////////////////
///
/// Commonly used camera aspect ratios
///

public static final float SO_ASPECT_SQUARE       = 1.00f;         /* Square (1.000)               */
public static final float SO_ASPECT_VIDEO        = 1.333333333f;  /* Video (0.75)                 */
public static final float SO_ASPECT_35mm_ACADEMY = 1.371f;        /* 35mm, Academy ap (.72939460) */
public static final float SO_ASPECT_16mm         = 1.369f;        /* 16mm cinema (.730460189)     */
public static final float SO_ASPECT_35mm_FULL    = 1.33333f;      /* 35mm cinema, full ap (0.75)  */
public static final float SO_ASPECT_70mm         = 2.287f;        /* 70 mm unsqueezed (.43725404) */
public static final float SO_ASPECT_CINEMASCOPE  = 2.35f;         /* Cinemascope (.425531914)     */
public static final float SO_ASPECT_HDTV         = 1.777777777f;  /* HDTV (16:9)                  */
public static final float SO_ASPECT_PANAVISION   = 2.361f;        /* Panavision (.423549343)      */
public static final float SO_ASPECT_35mm         = 1.5f;          /* 35mm still camera (.666666)  */
public static final float SO_ASPECT_VISTAVISION  = 2.301f;        /* Vistavision (.434593654)     */

	  
	
    //! What will happen if the camera and
    public enum ViewportMapping {
                                  		  //! viewport aspect ratios differ?
  
                                          //! The first 3 adjust the viewport
                                          //! to fit the camera.
          CROP_VIEWPORT_FILL_FRAME	(0),  //!< Draw filled frame around vp.
          CROP_VIEWPORT_LINE_FRAME	(1),  //!< Draw frame in lines.
          CROP_VIEWPORT_NO_FRAME	(2),  //!< Draw no frame.
  
          ADJUST_CAMERA				(3),  //!< Adjust camera to fit viewport.
          LEAVE_ALONE				(4);  //!< Do nothing. Camera image may
                                          //! become stretched out of proportion
          
          private int value;
          
          ViewportMapping(int value) {
        	this.value = value;  
          }
          public final int getValue() {
        	  return value;
          }
          public static ViewportMapping fromValue(int value) {
        	  switch(value) {
        	  case 0: return CROP_VIEWPORT_FILL_FRAME;
        	  case 1: return CROP_VIEWPORT_LINE_FRAME;
        	  case 2: return CROP_VIEWPORT_NO_FRAME;
        	  case 3: return ADJUST_CAMERA;
        	  case 4: return LEAVE_ALONE;
        	  default: return null;        		  
        	  }
          }
      };
  
	public final SoSFEnum viewportMapping = new SoSFEnum();
	   
	public final SoSFVec3f position = new SoSFVec3f();
	
	public final SoSFRotation orientation = new SoSFRotation();

	public final  SoSFFloat aspectRatio = new SoSFFloat();
	public final  SoSFFloat nearDistance = new SoSFFloat();
	public final  SoSFFloat farDistance = new SoSFFloat();
	   
	public final  SoSFFloat focalDistance = new SoSFFloat();
	  
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: protected

public SoCamera()
//
////////////////////////////////////////////////////////////////////////
{
	nodeHeader.SO_NODE_CONSTRUCTOR(/*SoCamera.class*/);

	nodeHeader.SO_NODE_ADD_SFIELD(viewportMapping,"viewportMapping",  (ViewportMapping.ADJUST_CAMERA.getValue()));
	nodeHeader.SO_NODE_ADD_SFIELD(position, "position",        new SbVec3f(0.0f, 0.0f, 1.0f));
	nodeHeader.SO_NODE_ADD_SFIELD(orientation,"orientation",      new SbRotation(0.0f, 0.0f, 0.0f, 1.0f));
	nodeHeader.SO_NODE_ADD_SFIELD(aspectRatio,"aspectRatio",      (SO_ASPECT_SQUARE));
	nodeHeader.SO_NODE_ADD_SFIELD(nearDistance,"nearDistance",     (1.0f));
	nodeHeader.SO_NODE_ADD_SFIELD(farDistance, "farDistance",     (10.0f));
	nodeHeader.SO_NODE_ADD_SFIELD(focalDistance,"focalDistance",  (SoFocalDistanceElement.getDefault()));

    // Set up static info for enumerated type field
	nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(ViewportMapping.CROP_VIEWPORT_FILL_FRAME);
	nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(ViewportMapping.CROP_VIEWPORT_LINE_FRAME);
	nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(ViewportMapping.CROP_VIEWPORT_NO_FRAME);
	nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(ViewportMapping.ADJUST_CAMERA);
	nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(ViewportMapping.LEAVE_ALONE);

    // Set up info in enumerated type field
	nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(viewportMapping,"viewportMapping", "ViewportMapping");

    isBuiltIn = true;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets the orientation of the camera so that it points toward the
//    given target point while keeping the "up" direction of the
//    camera parallel to the positive y-axis. If this is not possible,
//    it uses the positive z-axis as "up".
//
// Use: public

public void
pointAt(final SbVec3f targetPoint)
//
////////////////////////////////////////////////////////////////////////
{
    final SbVec3f     d = new SbVec3f(targetPoint.operator_minus(position.getValue()));

    final SbRotation  yRot = new SbRotation(new SbVec3f(0,1,0), (float)Math.atan2(-d.getValueRead()[0], -d.getValueRead()[2]));
    final SbRotation  xRot = new SbRotation(new SbVec3f(1,0,0), (float)Math.atan2(d.getValueRead()[1], Math.sqrt(d.getValueRead()[0]*d.getValueRead()[0] + d.getValueRead()[2]*d.getValueRead()[2])));

    orientation.setValue(xRot.operator_mul(yRot));
}


	
	
	/**
	 * Scales the height of the camera. 
	 * Perspective cameras scale their heightAngle fields, 
	 * and orthographic cameras scale their height fields. 
	 * 
	 * @param scaleFactor
	 */
	public abstract void scaleHeight(float scaleFactor);
	
	public SbViewVolume getViewVolume() {
		return getViewVolume(0);
	}
	
	public abstract SbViewVolume getViewVolume(float useAspectRatio);
	
	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Sets the camera up to view the scene under the given node. The
	   //    near and far clipping planes will be positioned 'slack' bounding
	   //    sphere radii away from the bounding box's center. A value of 1.0
	   //    will make the clipping planes the tightest around the bounding
	   //    sphere.
	   //
	   // Use: public
	   
	/**
	 * Sets the camera up to view the scene under the given node or 
	 * defined by the given path. 
	 * The near and far clipping planes will be positioned 'slack' 
	 * bounding sphere radii away from the bounding box's center. 
	 * A value of 1.0 will make the clipping planes the tightest 
	 * around the bounding sphere. 
	 * 
	 * @param root
	 * @param vpRegion
	 * @param slack
	 */
	 public void
	   viewAll(final SoNode root, final SbViewportRegion vpRegion) {
		 viewAll(root,vpRegion,1.0f);
	 }
	  
	
	  public void
	   viewAll(final SoNode root, final SbViewportRegion vpRegion, float slack)
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       final SoGetBoundingBoxAction      action = new SoGetBoundingBoxAction(vpRegion);
	   
	       // Crop the viewport region if necessary
	       final SbViewportRegion    croppedReg = getViewportBounds(vpRegion);
	   
	       // Find the bounding box of the scene, then adjust camera to view it
	       action.apply(root);
	       viewBoundingBox(action.getBoundingBox(),
	                       croppedReg.getViewportAspectRatio(), slack);
	       
	       action.destructor(); // TODO : implement automatic destruction
	   }
	  

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets the camera up to view the scene defined by the given path.
//    The near and far clipping planes will be positioned 'slack'
//    bounding sphere radii away from the bounding box's center. A
//    value of 1.0 will make the clipping planes the tightest around
//    the bounding sphere.
//
// Use: public

public void
viewAll(SoPath path, final SbViewportRegion vpRegion, float slack)
//
////////////////////////////////////////////////////////////////////////
{
    final SoGetBoundingBoxAction      action = new SoGetBoundingBoxAction(vpRegion);

    // Crop the viewport region if necessary
    SbViewportRegion    croppedReg = getViewportBounds(vpRegion);

    // Find the bounding box of the scene, then adjust camera to view it
    action.apply(path);
    viewBoundingBox(action.getBoundingBox(),
                    croppedReg.getViewportAspectRatio(), slack);
    
    action.destructor(); // TODO : implement automatic destruction
}
	  	 
	  ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Returns the viewport region this camera would use to render into
	   //    a given viewport region, accounting for cropping.
	   //
	   // Use: public
	   
	  public SbViewportRegion
	   getViewportBounds(final SbViewportRegion region)
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       float       vpAspect, camAspect;
	   
	       // Start with the same region as we are given
	       SbViewportRegion    croppedRegion = region;
	   
	       
	       switch (ViewportMapping.fromValue(viewportMapping.getValue())) {
	   
	         case CROP_VIEWPORT_FILL_FRAME:
	         case CROP_VIEWPORT_LINE_FRAME:
	         case CROP_VIEWPORT_NO_FRAME:
	   
	           vpAspect  = region.getViewportAspectRatio();
	           camAspect = aspectRatio.getValue();
	   
	           // Make a smaller viewport that
	           //     [a] fits within the old viewport and
	           //     [b] uses the camera's aspect ratio
	   
	           if (camAspect > vpAspect)
	               croppedRegion.scaleHeight(vpAspect / camAspect);
	   
	           else if (camAspect < vpAspect)
	               croppedRegion.scaleWidth(camAspect / vpAspect);
	   
	           break;
	   
	         case ADJUST_CAMERA:
	         case LEAVE_ALONE:
	           // Neither of these adjusts the viewport size.
	           break;
	       }
	   
	       return croppedRegion;
	   }
	  
	  
	   	  
	     //! Subclasses must define this method, which changes the position
	       //! and clipping planes of a camera to view the passed bounding box
	       //! using the given aspect ratio, without changing the height
	       //! scaling.
	  protected abstract void viewBoundingBox(final SbBox3f box,
	                                           float aspect, float slack);
	   	  	   	
     ////////////////////////////////////////////////////////////////////////
     //
     // Description:
     //    This initializes the SoCamera class. Since this is an abstract
     //    class, this doesn't need to deal with field data. Subclasses,
     //    do, however.
     //
     // Use: internal
     
    public static void
     initClass()
     //
     ////////////////////////////////////////////////////////////////////////
     {
    	SoSubNode.SO__NODE_INIT_ABSTRACT_CLASS(SoCamera.class, "Camera", SoNode.class);
     
         // Enable elements used:
     
         //SO_ENABLE(SoCallbackAction, SoFocalDistanceElement);
         SoCallbackAction.enableElement(SoFocalDistanceElement.class);
         //SO_ENABLE(SoCallbackAction, SoProjectionMatrixElement);
         SoCallbackAction.enableElement(SoProjectionMatrixElement.class);
         //SO_ENABLE(SoCallbackAction, SoViewVolumeElement);
         SoCallbackAction.enableElement(SoViewVolumeElement.class);
         //SO_ENABLE(SoCallbackAction, SoViewingMatrixElement);
         SoCallbackAction.enableElement(SoViewingMatrixElement.class);
         //SO_ENABLE(SoCallbackAction, SoViewportRegionElement);
         SoCallbackAction.enableElement(SoViewportRegionElement.class);
     
         //SO_ENABLE(SoGetBoundingBoxAction, SoFocalDistanceElement);
         SoGetBoundingBoxAction.enableElement(SoFocalDistanceElement.class);
         //SO_ENABLE(SoGetBoundingBoxAction, SoProjectionMatrixElement);
         SoGetBoundingBoxAction.enableElement(SoProjectionMatrixElement.class);
         //SO_ENABLE(SoGetBoundingBoxAction, SoViewVolumeElement);
         SoGetBoundingBoxAction.enableElement(SoViewVolumeElement.class);
         //SO_ENABLE(SoGetBoundingBoxAction, SoViewingMatrixElement);
         SoGetBoundingBoxAction.enableElement(SoViewingMatrixElement.class);
         //SO_ENABLE(SoGetBoundingBoxAction, SoViewportRegionElement);
         SoGetBoundingBoxAction.enableElement(SoViewportRegionElement.class);
     
         //SO_ENABLE(SoGLRenderAction, SoFocalDistanceElement);
         SoGLRenderAction.enableElement(SoFocalDistanceElement.class);
         //SO_ENABLE(SoGLRenderAction, SoGLProjectionMatrixElement);
         SoGLRenderAction.enableElement(SoGLProjectionMatrixElement.class);
         //SO_ENABLE(SoGLRenderAction, SoViewVolumeElement);
         SoGLRenderAction.enableElement(SoViewVolumeElement.class);
         //SO_ENABLE(SoGLRenderAction, SoGLViewingMatrixElement);
         SoGLRenderAction.enableElement(SoGLViewingMatrixElement.class);
         //SO_ENABLE(SoGLRenderAction, SoGLViewportRegionElement);
         SoGLRenderAction.enableElement(SoGLViewportRegionElement.class);
         //SO_ENABLE(SoGLRenderAction, SoGLUpdateAreaElement);
         SoGLRenderAction.enableElement(SoGLUpdateAreaElement.class);
     
         //SO_ENABLE(SoHandleEventAction, SoViewVolumeElement);
         SoHandleEventAction.enableElement(SoViewVolumeElement.class);
         //SO_ENABLE(SoHandleEventAction, SoViewportRegionElement);
         SoHandleEventAction.enableElement(SoViewportRegionElement.class);
     
         //SO_ENABLE(SoRayPickAction, SoFocalDistanceElement);
         SoRayPickAction.enableElement(SoFocalDistanceElement.class);
         //SO_ENABLE(SoRayPickAction, SoProjectionMatrixElement);
         SoRayPickAction.enableElement(SoProjectionMatrixElement.class);
         //SO_ENABLE(SoRayPickAction, SoViewVolumeElement);
         SoRayPickAction.enableElement(SoViewVolumeElement.class);
         //SO_ENABLE(SoRayPickAction, SoViewingMatrixElement);
         SoRayPickAction.enableElement(SoViewingMatrixElement.class);
         //SO_ENABLE(SoRayPickAction, SoViewportRegionElement);
         SoRayPickAction.enableElement(SoViewportRegionElement.class);
     }

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Performs most actions that SoCamera supports.
//
// Use: extender

public void
SoCamera_doAction(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
    final SbViewVolume        viewVol = new SbViewVolume();
    final boolean[]              changeRegion = new boolean[1];

    // Get the current viewport region
    SbViewportRegion vpReg =
        SoViewportRegionElement.get(action.getState());

    // Compute the view volume
    SoCamera_computeView(vpReg, viewVol, changeRegion);

    // Set the state
    final SbVec3f dummyJitter = new SbVec3f();
    setElements(action, viewVol, changeRegion[0],
                changeRegion[0] ? getViewportBounds(vpReg) : vpReg,
                false, dummyJitter);
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does the callback action.
//
// Use: extender

public void
callback(SoCallbackAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoCamera_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Performs GL rendering on a camera node.
//
// Use: extender

public void
GLRender(SoGLRenderAction action)
//
////////////////////////////////////////////////////////////////////////
{
    final SbViewportRegion    croppedReg = new SbViewportRegion();
    final SbViewVolume        viewVol = new SbViewVolume();
    final SbMatrix            viewMat = new SbMatrix(), projMat = new SbMatrix();
    final SbVec2fSingle             uaOrigin = new SbVec2fSingle(), uaSize = new SbVec2fSingle();
    final SbVec3f             jitterAmount = new SbVec3f();
    final boolean[]              changeRegion = new boolean[1];
    final SoState             state = action.getState();

    // Get the current viewport region
    final SbViewportRegion vpReg = SoGLViewportRegionElement.get(state);

    // Compute the view volume
    SoCamera_computeView(vpReg, viewVol, changeRegion);

    // Draw frame, if necessary, using current (full) viewport
    if (changeRegion[0]) {
        croppedReg.copyFrom(getViewportBounds(vpReg));
        drawFrame(action, vpReg, croppedReg);
    }

    // Jitter the camera for anti-aliasing if doing multiple passes
    if (action.getNumPasses() > 1)
        jitter(action.getNumPasses(), SoGLRenderPassElement.get(state),
               changeRegion[0] ? croppedReg : vpReg, jitterAmount);

    // Set the state
    setElements(action, viewVol, changeRegion[0], croppedReg,
                action.getNumPasses() > 1, jitterAmount);

    // Compute and set culling volume if different from view volume
    if (! SoGLUpdateAreaElement.get(state, uaOrigin, uaSize)) {
        SbViewVolume cvv = viewVol.narrow(uaOrigin.getValue()[0], uaOrigin.getValue()[1],
                                          uaSize.getValue()[0],   uaSize.getValue()[1]);
        SoModelMatrixElement.setCullMatrix(state, this, cvv.getMatrix());
    }
    // Otherwise, just set culling volume to be same as view volume
    else
        SoModelMatrixElement.setCullMatrix(state, this, viewVol.getMatrix());

    // Don't auto-cache above cameras:
    SoGLCacheContextElement.shouldAutoCache(state,
                SoGLCacheContextElement.AutoCache.DONT_AUTO_CACHE.getValue());
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns amount to jitter camera in normalized device coordinates
//    (after projection) for anti-aliasing during multi-pass rendering.
//
// Use: protected, virtual

protected void
jitter(int numPasses, int curPass,
                 final SbViewportRegion vpReg, final SbVec3f jitterAmount)
//
////////////////////////////////////////////////////////////////////////
{
    // Get the current sample point within the pixel
    final SbVec2fSingle samplePoint = new SbVec2fSingle();
    getJitterSample(numPasses, curPass, samplePoint);

    // Compute the jitter amount for the projection matrix. This
    // jitter will be in post-perspective space, which goes from -1 to
    // +1 in x and y. In this space, the size of a pixel is 2/width by
    // 2/height.
    final SbVec2s vpSize = vpReg.getViewportSizePixels();
    jitterAmount.setValue(0, samplePoint.getValue()[0] * 2.0f / (float) vpSize.getValue()[0]);
    jitterAmount.setValue(1, samplePoint.getValue()[1] * 2.0f / (float) vpSize.getValue()[1]);
    jitterAmount.setValue(2, 0.0f);
}

// These arrays define filter kernels to be used when the total
// number of passes is small. For N passes, use the smallest
// kernel that is >= N.
static final float  kernel2[][]   = {{ 0.246490f,  0.249999f },
                                       {-0.246490f, -0.249999f }},
                    kernel3[][]   = {{-0.373411f, -0.250550f },
                                       { 0.256263f,  0.368119f },
                                       { 0.117148f, -0.117570f }},
                    kernel4[][]   = {{-0.208147f,  0.353730f },
                                       { 0.203849f, -0.353780f },
                                       {-0.292626f, -0.149945f },
                                       { 0.296924f,  0.149994f }},
                    kernel8[][]   = {{-0.334818f,  0.435331f },
                                       { 0.286438f, -0.393495f },
                                       { 0.459462f,  0.141540f },
                                       {-0.414498f, -0.192829f },
                                       {-0.183790f,  0.082102f },
                                       {-0.079263f, -0.317383f },
                                       { 0.102254f,  0.299133f },
                                       { 0.164216f, -0.054399f }},
                    kernel15[][] = {{ 0.285561f,  0.188437f },
                                       { 0.360176f, -0.065688f },
                                       {-0.111751f,  0.275019f },
                                       {-0.055918f, -0.215197f },
                                       {-0.080231f, -0.470965f },
                                       { 0.138721f,  0.409168f },
                                       { 0.384120f,  0.458500f },
                                       {-0.454968f,  0.134088f },
                                       { 0.179271f, -0.331196f },
                                       {-0.307049f, -0.364927f },
                                       { 0.105354f, -0.010099f },
                                       {-0.154180f,  0.021794f },
                                       {-0.370135f, -0.116425f },
                                       { 0.451636f, -0.300013f },
                                       {-0.370610f,  0.387504f }},
                    kernel24[][] = {{ 0.030245f,  0.136384f },
                                       { 0.018865f, -0.348867f },
                                       {-0.350114f, -0.472309f },
                                       { 0.222181f,  0.149524f },
                                       {-0.393670f, -0.266873f },
                                       { 0.404568f,  0.230436f },
                                       { 0.098381f,  0.465337f },
                                       { 0.462671f,  0.442116f },
                                       { 0.400373f, -0.212720f },
                                       {-0.409988f,  0.263345f },
                                       {-0.115878f, -0.001981f },
                                       { 0.348425f, -0.009237f },
                                       {-0.464016f,  0.066467f },
                                       {-0.138674f, -0.468006f },
                                       { 0.144932f, -0.022780f },
                                       {-0.250195f,  0.150161f },
                                       {-0.181400f, -0.264219f },
                                       { 0.196097f, -0.234139f },
                                       {-0.311082f, -0.078815f },
                                       { 0.268379f,  0.366778f },
                                       {-0.040601f,  0.327109f },
                                       {-0.234392f,  0.354659f },
                                       {-0.003102f, -0.154402f },
                                       { 0.297997f, -0.417965f }},
                    kernel66[][] = {{ 0.266377f, -0.218171f },
                                       {-0.170919f, -0.429368f },
                                       { 0.047356f, -0.387135f },
                                       {-0.430063f,  0.363413f },
                                       {-0.221638f, -0.313768f },
                                       { 0.124758f, -0.197109f },
                                       {-0.400021f,  0.482195f },
                                       { 0.247882f,  0.152010f },
                                       {-0.286709f, -0.470214f },
                                       {-0.426790f,  0.004977f },
                                       {-0.361249f, -0.104549f },
                                       {-0.040643f,  0.123453f },
                                       {-0.189296f,  0.438963f },
                                       {-0.453521f, -0.299889f },
                                       { 0.408216f, -0.457699f },
                                       { 0.328973f, -0.101914f },
                                       {-0.055540f, -0.477952f },
                                       { 0.194421f,  0.453510f },
                                       { 0.404051f,  0.224974f },
                                       { 0.310136f,  0.419700f },
                                       {-0.021743f,  0.403898f },
                                       {-0.466210f,  0.248839f },
                                       { 0.341369f,  0.081490f },
                                       { 0.124156f, -0.016859f },
                                       {-0.461321f, -0.176661f },
                                       { 0.013210f,  0.234401f },
                                       { 0.174258f, -0.311854f },
                                       { 0.294061f,  0.263364f },
                                       {-0.114836f,  0.328189f },
                                       { 0.041206f, -0.106205f },
                                       { 0.079227f,  0.345021f },
                                       {-0.109319f, -0.242380f },
                                       { 0.425005f, -0.332397f },
                                       { 0.009146f,  0.015098f },
                                       {-0.339084f, -0.355707f },
                                       {-0.224596f, -0.189548f },
                                       { 0.083475f,  0.117028f },
                                       { 0.295962f, -0.334699f },
                                       { 0.452998f,  0.025397f },
                                       { 0.206511f, -0.104668f },
                                       { 0.447544f, -0.096004f },
                                       {-0.108006f, -0.002471f },
                                       {-0.380810f,  0.130036f },
                                       {-0.242440f,  0.186934f },
                                       {-0.200363f,  0.070863f },
                                       {-0.344844f, -0.230814f },
                                       { 0.408660f,  0.345826f },
                                       {-0.233016f,  0.305203f },
                                       { 0.158475f, -0.430762f },
                                       { 0.486972f,  0.139163f },
                                       {-0.301610f,  0.009319f },
                                       { 0.282245f, -0.458671f },
                                       { 0.482046f,  0.443890f },
                                       {-0.121527f,  0.210223f },
                                       {-0.477606f, -0.424878f },
                                       {-0.083941f, -0.121440f },
                                       {-0.345773f,  0.253779f },
                                       { 0.234646f,  0.034549f },
                                       { 0.394102f, -0.210901f },
                                       {-0.312571f,  0.397656f },
                                       { 0.200906f,  0.333293f },
                                       { 0.018703f, -0.261792f },
                                       {-0.209349f, -0.065383f },
                                       { 0.076248f,  0.478538f },
                                       {-0.073036f, -0.355064f },
                                       { 0.145087f,  0.221726f }};

static float[] extraSamples = null;
static int      numExtraSamples = 0;

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns a 2D sample point within a pixel.
//
// Use: private, static

private static void
getJitterSample(int numPasses, int curPass, final SbVec2f samplePoint)
//
////////////////////////////////////////////////////////////////////////
{

    // Determine which kernel to use and access the correct jitter amount
    if (numPasses == 2)
        samplePoint.setValue(kernel2[curPass]);
    else if (numPasses == 3)
        samplePoint.setValue(kernel3[curPass]);
    else if (numPasses == 4)
        samplePoint.setValue(kernel4[curPass]);
    else if (numPasses <= 8)
        samplePoint.setValue(kernel8[curPass]);
    else if (numPasses <= 15)
        samplePoint.setValue(kernel15[curPass]);
    else if (numPasses <= 24)
        samplePoint.setValue(kernel24[curPass]);
    else if (numPasses <= 66)
        samplePoint.setValue(kernel66[curPass]);

    // If we don't have enough samples in the largest kernel, we'll
    // allocate an array and fill it with random samples to cover the
    // ones not in the kernel
    else {

        // Do the allocation (if necessary) only on the first pass, to
        // avoid unnecessary tests
        if (curPass == 1) {

            int numExtraNeeded = numPasses - 66;

            if (numExtraSamples < numExtraNeeded) {
//                if (extraSamples != null)
//                    delete extraSamples; java port
                extraSamples = new float [numExtraNeeded * 2];

                // Set 2 coords of sample to random number between -1 and +1.
                for (int i = 0; i < 2 * numExtraNeeded; i++)
                    extraSamples[i] = 2.0f * (float)drand48() - 1.0f;
            }
        }

        if (curPass < 66)
            samplePoint.setValue(kernel66[curPass]);
        else
            samplePoint.setValue(extraSamples[(curPass - 66) * 2 + 0],
                                 extraSamples[(curPass - 66) * 2 + 1]);

    }
}

/* Maximum value that can be returned by the rand function. */

public static final int RAND_MAX = 0x7fff;

private final static double drand48() {
	return Math.random();
}



////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does the getBoundingBox action. If the isCameraSpace() flag is
//    set in the action, this also transforms the model matrix to set
//    up camera space.
//
// Use: extender

public void
getBoundingBox(SoGetBoundingBoxAction action)
//
////////////////////////////////////////////////////////////////////////
{
    // Do the usual stuff first
    SoCamera_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets up viewing info for event handling.
//
// Use: extender

public void
handleEvent(SoHandleEventAction action)
//
////////////////////////////////////////////////////////////////////////
{
    // Set only a subset of the elements for this action

    final SbViewVolume        viewVol = new SbViewVolume();
    final boolean[]              changeRegion = new boolean[1];
    SoState             state = action.getState();

    // Get the current viewport region
    final SbViewportRegion vpReg = SoViewportRegionElement.get(state);

    // Compute the view volume
    SoCamera_computeView(vpReg, viewVol, changeRegion);

    // Set necessary elements in state
    SoViewVolumeElement.set(state, this, viewVol);
    if (changeRegion[0])
        SoViewportRegionElement.set(state, getViewportBounds(vpReg));
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets up viewing info for ray picking.
//
// Use: extender

public void
rayPick(SoRayPickAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoCamera_doAction(action);

    // Tell the action how to set up world space view
    action.computeWorldSpaceRay();
}


    
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Computes the view volume the camera represents, based on the
//    given viewport region. Returns TRUE in changeRegion if the
//    viewport region needs to be adjusted based on the current
//    viewport mapping, meaning that it has to be changed in the
//    state.
//
// Use: private

private void
SoCamera_computeView(final SbViewportRegion vpReg,
                      final SbViewVolume viewVol, final boolean[] changeRegion)
//
////////////////////////////////////////////////////////////////////////
{
    float       camAspect, vpAspect;

    changeRegion[0] = false;

    camAspect = aspectRatio.getValue();

    switch (ViewportMapping.fromValue(viewportMapping.getValue())) {
      case CROP_VIEWPORT_FILL_FRAME:
      case CROP_VIEWPORT_LINE_FRAME:
      case CROP_VIEWPORT_NO_FRAME:
        changeRegion[0] = true;
        viewVol.copyFrom(getViewVolume(camAspect));
        break;

      case ADJUST_CAMERA:
        vpAspect = vpReg.getViewportAspectRatio();
        viewVol.copyFrom(getViewVolume(vpAspect));
        // Adjust size of view volume if it will fill viewport better
        if (vpAspect < 1.0)
            viewVol.scale(1.0f / vpAspect);
        break;

      case LEAVE_ALONE:
        // Do nothing. The window specified by the camera will be
        // stretched to fit into the current viewport.
        viewVol.copyFrom(getViewVolume(camAspect));
        break;
    }
}

    
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets relevant info in elements in state.
//
// Use: private

private void
setElements(final SoAction action, final SbViewVolume viewVol,
                      boolean setRegion, final SbViewportRegion vpReg,
                      boolean doJitter, final SbVec3f jitterAmount)
//
////////////////////////////////////////////////////////////////////////
{
    SoState     state = action.getState();
    final SbMatrix    viewMat = new SbMatrix(), projMat = new SbMatrix();

    // Compute viewing and projection matrices
    viewVol.getMatrices(viewMat, projMat);

    // Jitter if necessary
    if (doJitter) {
        final SbMatrix m = new SbMatrix();
        m.setTranslate(jitterAmount);
        projMat.multRight(m);
    }

    // Set necessary elements in state
    SoProjectionMatrixElement.set(state, this, projMat);

    // Set the viewport region only if it has changed
    if (setRegion)
        SoViewportRegionElement.set(state, vpReg);

    // If the current model matrix is not identity, the camera has to
    // be transformed by it. This is equivalent to transforming the
    // world by the inverse of the model matrix. So we set the viewing
    // matrix to be the product of the inverse of the model matrix and
    // the viewMat.
    final boolean[] modelIsIdent = new boolean[1];
    SbMatrix modelMat = SoModelMatrixElement.get(state, modelIsIdent);
    if (! modelIsIdent[0]) {
        viewMat.multRight(modelMat.inverse());

        // Also, transform the view volume by the model matrix
        viewVol.transform(modelMat);
    }

    SoViewVolumeElement.set(state, this, viewVol);
    SoViewingMatrixElement.set(state, this, viewMat);
    
    // 2004-05-25 Felix: Set focal distance element
    SoFocalDistanceElement.set(state, focalDistance.getValue());
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Draws cropping frame for use when rendering.
//
// Use: private

private void
drawFrame(SoGLRenderAction action,
                    final SbViewportRegion vpReg,
                    final SbViewportRegion croppedReg)
//
////////////////////////////////////////////////////////////////////////
{
// Frame color as RGB. In color index mode, just use the background
// color, since we have no idea what's in the color map.
//#define GREY1           .38, .38, .38
final int GREY1_INDEX    = 0;

    // Get origin and size of viewports in pixels
    final SbVec2s      vpOrig =      vpReg.getViewportOriginPixels();
    final SbVec2s      vpSize =      vpReg.getViewportSizePixels();
    final SbVec2s      cropLL = croppedReg.getViewportOriginPixels();
    final SbVec2s      cropUR = croppedReg.getViewportSizePixels().operator_add(cropLL);
    
    GL2 gl2 = action.getCacheContext();

    // Save stuff we're going to change explicitly
    gl2.glPushAttrib(GL2.GL_ENABLE_BIT | GL2.GL_CURRENT_BIT |
    		GL2.GL_POLYGON_BIT | GL2.GL_LINE_BIT);

    gl2.glDisable(GL2.GL_LIGHTING);
    if (SoGLLazyElement.isColorIndex(action.getState()))
        gl2.glIndexi(GREY1_INDEX);
    else
        gl2.glColor3f(0.38f,0.38f,0.38f);

    // OK to blow away the old projection matrix, because it will be 
    // changed anyway by the camera's render action right after this...
    gl2.glMatrixMode(GL2.GL_PROJECTION);
    gl2.glLoadIdentity();
    gl2.glOrtho(0.0f, vpSize.getValue()[0] - 1.0f, 0.0f, vpSize.getValue()[1] - 1.0f, -1.0f, 1.0f);

    // Ditto for the viewing matrix
    gl2.glMatrixMode(GL2.GL_MODELVIEW);
    gl2.glPushMatrix();
    gl2.glLoadIdentity();

    if (viewportMapping.getValue() == ViewportMapping.CROP_VIEWPORT_LINE_FRAME.getValue())  {
        // Draw a rectangle one pixel bigger than the camera's viewport
        gl2.glPolygonMode(GL2.GL_FRONT, GL_LINE);
        gl2.glLineWidth(1.0f);
        gl2.glDisable(GL_LINE_STIPPLE);
        gl2.glRects((short)(cropLL.getValue()[0] - 1 - vpOrig.getValue()[0]), (short)(cropLL.getValue()[1] - 1 - vpOrig.getValue()[1]),
        		(short)(cropUR.getValue()[0] + 1 - vpOrig.getValue()[0]), (short)(cropUR.getValue()[1] + 1 - vpOrig.getValue()[1]));
    }
    else if (viewportMapping.getValue() == ViewportMapping.CROP_VIEWPORT_FILL_FRAME.getValue()) {
    	gl2.glPolygonMode(GL_FRONT, GL_FILL);
    	gl2.glDisable(GL_POLYGON_STIPPLE);

        if (croppedReg.getViewportAspectRatio() >
            vpReg.getViewportAspectRatio()) {

            // Draw filled rectangles for a border
        	gl2.glRects((short)(0), (short)(0),
        			(short)(vpSize.getValue()[0] - 1), (short)(cropLL.getValue()[1] - vpOrig.getValue()[1] - 1));
        	gl2.glRects((short)(0), (short)(cropUR.getValue()[1] - vpOrig.getValue()[1] + 1 ),
        			(short)(vpSize.getValue()[0] - 1), (short)(vpSize.getValue()[1] - 1));
        }
        else {
        	gl2.glRects((short)(0), (short)(0),
        			(short)(cropLL.getValue()[0] - vpOrig.getValue()[0] - 1), (short)(vpSize.getValue()[1] - 1));
        	gl2.glRects((short)(cropUR.getValue()[0] - vpOrig.getValue()[0] + 1), (short)(0),
                    (short)(vpSize.getValue()[0] - 1), (short)(vpSize.getValue()[1] - 1));
        }
    }

    gl2.glPopMatrix();
    gl2.glPopAttrib();

//#undef GREY1
//#undef GREY1_INDEX
}
    

}

