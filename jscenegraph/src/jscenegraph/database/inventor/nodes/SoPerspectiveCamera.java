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
 |      This file defines the SoPerspectiveCamera node class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import jscenegraph.database.inventor.SbBasic;
import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbSphere;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbViewVolume;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFFloat;


////////////////////////////////////////////////////////////////////////////////
//! Perspective camera node.
/*!
\class SoPerspectiveCamera
\ingroup Nodes
A perspective camera defines a perspective projection from a
viewpoint. The viewing volume for a perspective camera is a truncated
right pyramid.


By default, the camera is located at (0,0,1) and looks along the
negative z-axis; the \b position  and \b orientation  fields can be
used to change these values. The \b heightAngle  field defines the
total vertical angle of the viewing volume; this and the
aspectRatio field determine the horizontal angle.

\par File Format/Default
\par
\code
PerspectiveCamera {
  viewportMapping ADJUST_CAMERA
  position 0 0 1
  orientation 0 0 1 0
  aspectRatio 1
  nearDistance 1
  farDistance 10
  focalDistance 5
  heightAngle 0.78539819
}
\endcode

\par Action Behavior
\par
SoGLRenderAction, SoCallbackAction, SoGetBoundingBoxAction, SoHandleEventAction, SoRayPickAction
<BR> Sets the viewport and camera information in the state. 

\par See Also
\par
SbViewVolume, SoOrthographicCamera
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoPerspectiveCamera extends SoCamera {
	
	public static final float MINIMUM_NEAR_PLANE = 0.01f;     /* minimum near clipping distance */
    /* (from center) */
	

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoPerspectiveCamera.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoPerspectiveCamera.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoPerspectiveCamera.class); }    	  	
	
 
  public final SoSFFloat heightAngle = new SoSFFloat();


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoPerspectiveCamera()
//
////////////////////////////////////////////////////////////////////////
{
    nodeHeader.SO_NODE_CONSTRUCTOR(/*SoPerspectiveCamera*/);
    nodeHeader.SO_NODE_ADD_SFIELD(heightAngle,"heightAngle",    ((float)SbBasic.M_PI_4));        // 45 degrees
    isBuiltIn = true;
}


 ////////////////////////////////////////////////////////////////////////
 //
 // Description:
 //    Scales the height of the camera, in this case the 'heightAngle' field
 //
 // Use: public  
  @Override
  public void scaleHeight(float scaleFactor) {
	     if (scaleFactor == 0.0)
	    	           return;
	    	   
	    	       heightAngle.setValue(scaleFactor * heightAngle.getValue());
	    	     }
   
  private final SbViewVolume view = new SbViewVolume(); // SINGLE_THREAD
  ////////////////////////////////////////////////////////////////////////
   //
   // Description:
   //    Fills in a view volume structure, based on the camera. If the
   //    useAspectRatio field is not 0.0 (the default), the camera uses
   //    that ratio instead of the one it has.
   //
   // Use: public
     
	@Override
	public SbViewVolume getViewVolume(float useAspectRatio) {
	     view.constructor();
	      
	          float       camAspect = (useAspectRatio != 0.0 ? useAspectRatio :
	                                   aspectRatio.getValue());
	      
	          // Set up the perspective camera.
	          view.perspective(heightAngle.getValue(), camAspect,
	                           nearDistance.getValue(), farDistance.getValue());
	      
	          // Note that these move the camera rather than moving objects
	          // relative to the camera.
	          view.rotateCamera(orientation.getValue());
	          view.translateCamera(position.getValue());
	          return view;
	     	}

	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Finds a bounding sphere around the passed bounding box, then
	   //    positions the camera without changing its height angle so that
	   //    the entire sphere is visible. The aspect ratio to use for the
	   //    camera is passed in.
	   // 
	   // Use: protected
	   	
//	@Override
//	protected void viewBoundingBox(SbBox3f box, float aspect, float slack) {
//	     final SbSphere    bSphere = new SbSphere();
//	          float       distToCenter;
//	          final SbMatrix    rotation = new SbMatrix();
//	          final SbVec3f     pos = new SbVec3f();
//	      
//	          // if the bounding box is not empty, create the bounding sphere
//	          if (! box.isEmpty())
//	              bSphere.circumscribe(box);
//	          else {
//	              pos.setValue(0.f, 0.f, 0.f);
//	              bSphere.setValue(pos, 1.0f);
//	          }
//	      
//	          // Find the angle necessary to fit the object completely in the
//	          // window.  We don't need any slack, because the bounding sphere
//	          // is already bigger than the bounding box.
//	          //      h = height (radius of sphere)
//	          //      d = distance to eye
//	          //      tan(alpha) gives us h/d
//	          float hOverD = (float)Math.tan(heightAngle.getValue()  / 2.0f);
//	          if (viewportMapping.getValue()!=ViewportMapping.ADJUST_CAMERA.getValue() &&
//	            viewportMapping.getValue()!=ViewportMapping.LEAVE_ALONE.getValue() &&
//	            aspect < 1.0) {
//	              hOverD *= aspect;
//	          }
//	      
//	          distToCenter = bSphere.getRadius() / hOverD;  //   h / (h/d) gives us d
//	      
//	          rotation .operator_assign( orientation.getValue());
//	          rotation.multVecMatrix(new SbVec3f(0.0f, 0.0f, distToCenter), pos);
//	      
//	          position     .operator_assign( pos.operator_add(bSphere.getCenter()));
//	          nearDistance .operator_assign( distToCenter - slack * bSphere.getRadius());
//	          if (nearDistance.getValue() < 0.0 ||
//	              nearDistance.getValue() < (MINIMUM_NEAR_PLANE * distToCenter))
//	              nearDistance .operator_assign( MINIMUM_NEAR_PLANE * distToCenter);
//	          farDistance   .operator_assign(distToCenter + slack * bSphere.getRadius());
//	          focalDistance.operator_assign(distToCenter);
//	     }

	// Doc in superclass.
	protected void
	viewBoundingBox(final SbBox3f box, float aspect,
	                                     float slack)
	{
	//#if COIN_DEBUG
	  // Only check for "flagged" emptiness, and don't use
	  // SbBox3f::hasVolume(), as we *can* handle flat boxes.
	  if (box.isEmpty()) {
	    SoDebugError.postWarning("SoPerspectiveCamera::viewBoundingBox",
	                              "bounding box is empty");
	    return;
	  }
	//#endif // COIN_DEBUG

	  // First, we want to move the camera in such a way that it is
	  // pointing straight at the center of the scene bounding box -- but
	  // without modifiying the rotation value (so we can't use
	  // SoCamera::pointAt()).
	  final SbVec3f cameradirection = new SbVec3f();
	  this.orientation.getValue().multVec(new SbVec3f(0, 0, -1), cameradirection);
	  this.position.setValue(box.getCenter().operator_add(cameradirection.operator_minus()));


	  // Get the radius of the bounding sphere.
	  final SbSphere bs = new SbSphere();
	  bs.circumscribe(box);
	  float radius = bs.getRadius();

	  // Make sure that everything will still be inside the viewing volume
	  // even if the aspect ratio "favorizes" width over height.
	  float aspectradius = radius / (aspect < 1.0f ? aspect : 1.0f);

	  // Move the camera to the edge of the bounding sphere, while still
	  // pointing at the scene.
	  final SbVec3f direction = this.position.getValue().operator_minus(box.getCenter());
	  direction.normalize(); // we know this is not a null vector

	  // There's a small chance that the frustum will intersect the
	  // bounding box when we calculate the movelength like this, but for
	  // all normal heightAngles it will yield a much better fit than
	  // the 100% safe version which also adds radius to movelength
	  float movelength = aspectradius/(float)(Math.tan(this.heightAngle.getValue() / 2.0));
	  this.position.setValue(box.getCenter().operator_add(direction.operator_mul(movelength)));

	  // Set up the clipping planes according to the slack value (a value
	  // of 1.0 will yield clipping planes that are tangent to the
	  // bounding sphere of the scene).
	  float distance_to_midpoint =
	    (this.position.getValue().operator_minus(box.getCenter())).length();
	  // make sure nearDistance isn't 0.0 (or too close to 0.0)
	  final float EPS = 0.001f;
	  this.nearDistance.setValue(SbBasic.SbMax(distance_to_midpoint*EPS, distance_to_midpoint - radius * slack));
	  this.farDistance.setValue( distance_to_midpoint + radius * slack);

	  // The focal distance is simply the distance from the camera to the
	  // scene midpoint. This field is not used in rendering, its just
	  // provided to make it easier for the user to do calculations based
	  // on the distance between the camera and the scene.
	  this.focalDistance.setValue( distance_to_midpoint);
	}
	
	 ////////////////////////////////////////////////////////////////////////
	  //
	  // Description:
	  //    This initializes the SoPerspectiveCamera class.
	  //
	  // Use: internal
	  
	 public static void
	  initClass()
	  //
	  ////////////////////////////////////////////////////////////////////////
	  {
	      SoSubNode.SO__NODE_INIT_CLASS(SoPerspectiveCamera.class, "PerspectiveCamera", SoCamera.class);
	  }
	   
}
