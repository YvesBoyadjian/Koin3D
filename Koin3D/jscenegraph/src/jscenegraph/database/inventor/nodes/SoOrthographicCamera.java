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
 |      This file defines the SoOrthographicCamera node class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbSphere;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbViewVolume;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFFloat;


////////////////////////////////////////////////////////////////////////////////
//! Orthographic camera node.
/*!
\class SoOrthographicCamera
\ingroup Nodes
An orthographic camera defines a parallel projection from a viewpoint.
This camera does not diminish objects with distance, as an
SoPerspectiveCamera does. The viewing volume for an orthographic
camera is a rectangular parallelepiped (a box).


By default, the camera is located at (0,0,1) and looks along the
negative z-axis; the \b position  and \b orientation  fields can be
used to change these values. The \b height  field defines the total
height of the viewing volume; this and the aspectRatio field
determine its width.

\par File Format/Default
\par
\code
OrthographicCamera {
  viewportMapping ADJUST_CAMERA
  position 0 0 1
  orientation 0 0 1 0
  aspectRatio 1
  nearDistance 1
  farDistance 10
  focalDistance 5
  height 2
}
\endcode

\par Action Behavior
\par
SoGLRenderAction, SoCallbackAction, SoGetBoundingBoxAction, SoHandleEventAction, SoRayPickAction
<BR> Sets the viewport and camera information in the state. 

\par See Also
\par
SbViewVolume, SoPerspectiveCamera
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoOrthographicCamera extends SoCamera {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoOrthographicCamera.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoOrthographicCamera.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoOrthographicCamera.class); }    	  	
	

	public final SoSFFloat height = new SoSFFloat();

	

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoOrthographicCamera()
//
////////////////////////////////////////////////////////////////////////
{
	super();
	
    nodeHeader.SO_NODE_CONSTRUCTOR(/*SoOrthographicCamera*/);
    nodeHeader.SO_NODE_ADD_SFIELD(height,"height",    (2.0f));
    isBuiltIn = true;
}

	
	
	 ////////////////////////////////////////////////////////////////////////
	    //
	    // Description:
	    //    Scales the height of the camera, in this case the 'height' field
	    //
	    // Use: public
	    
	  public  void
	    scaleHeight(float scaleFactor)
	    //
	    ////////////////////////////////////////////////////////////////////////
	    {
	        if (scaleFactor == 0.0)
	           return;
	   
	       height.setValue(scaleFactor * height.getValue());
	   }
	   	
	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Fills in a view volume structure, based on the camera. If the
	   //    useAspectRatio field is not 0.0 (the default), the camera uses
	   //    that ratio instead of the one it has.
	   //
	   // Use: public
	   
	@Override
	  public SbViewVolume
	   getViewVolume( float useAspectRatio)
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       final SbViewVolume view = new SbViewVolume();
	   
	       float       camAspect = (useAspectRatio != 0.0 ? useAspectRatio :
	                                aspectRatio.getValue());
	   
	       float xRadius = 0.5f * height.getValue() * camAspect;
	       float yRadius = 0.5f * height.getValue();
	   
	       // Set up the orthographic camera.
	       view.ortho(- xRadius, xRadius, - yRadius, yRadius,
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
	   //    positions the camera without changing its height so that the
	   //    entire sphere is visible. The aspect ratio to use for the camera
	   //    is passed in.
	   // 
	   // Use: protected
	   
	@Override
	public void
	   viewBoundingBox(final SbBox3f box,
	                                        float aspect, float slack)
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       final SbSphere    bSphere = new SbSphere();
	       final SbMatrix    rotation = new SbMatrix();
	       final SbVec3f     pos = new SbVec3f();
	   
	       // if the bounding box is not empty, create the bounding sphere
	       if (! box.isEmpty())
	           bSphere.circumscribe(box);
	       else {
	           pos.setValue(0.f, 0.f, 0.f);
	           bSphere.setValue(pos, 1.0f);
	       }
	   
	       // The position will be such that the "camera" touches the
	       // bounding sphere.
	       rotation.operator_assign( orientation.getValue());
	       rotation.multVecMatrix(new SbVec3f(0.0f, 0.0f, bSphere.getRadius()), pos);
	       position.operator_assign(pos.operator_add(bSphere.getCenter()));
	   
	       // Set the clipping planes to the bounds of the sphere and the
	       // focal distance to the center of the sphere.
	       nearDistance.operator_assign( - (slack -1) * bSphere.getRadius());
	       farDistance.operator_assign( (slack + 1) * bSphere.getRadius());
	       focalDistance.operator_assign(bSphere.getRadius());
	   
	       // Find the height necessary to fit the object completely in the
	       // window.  We don't need any slack, because the bounding sphere
	       // is already bigger than the bounding box.
	       if (viewportMapping.getValue()!=ViewportMapping.ADJUST_CAMERA.getValue() &&
	         viewportMapping.getValue()!=ViewportMapping.LEAVE_ALONE.getValue() &&
	         aspect < 1.0) {
	           height.operator_assign( 2.0f * bSphere.getRadius() / aspect);
	       } else {
	           height.operator_assign( 2.0f * bSphere.getRadius());
	       }
	   }

	 ////////////////////////////////////////////////////////////////////////
	  //
	  // Description:
	  //    This initializes the SoOrthographicCamera class.
	  //
	  // Use: internal
	  
	 public static void
	  initClass()
	  //
	  ////////////////////////////////////////////////////////////////////////
	  {
	      SoSubNode.SO__NODE_INIT_CLASS(SoOrthographicCamera.class, "OrthographicCamera", SoCamera.class);
	  }
	  }
