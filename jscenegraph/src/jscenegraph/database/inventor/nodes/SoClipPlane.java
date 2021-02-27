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
 |      This file defines the SoClipPlane node class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import jscenegraph.database.inventor.SbPlane;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoPickAction;
import jscenegraph.database.inventor.elements.SoClipPlaneElement;
import jscenegraph.database.inventor.elements.SoGLClipPlaneElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFBool;
import jscenegraph.database.inventor.fields.SoSFPlane;

/**
 * @author Yves Boyadjian
 *
 */

////////////////////////////////////////////////////////////////////////////////
//! Clipping plane node.
/*!
\class SoClipPlane
\ingroup Nodes
This node clips all subsequent shapes in the scene graph to the
half-space defined by the plane field. The half-space is the side
of the plane in the direction of the plane normal. 
For example, if the plane is positioned at the origin 
and the normal is pointing down the positive X axis,
everything in the negative X space will be clipped away.


Any number of
clipping planes may be active simultaneously, although the graphics
library may place a limit on this number during rendering.

\par File Format/Default
\par
\code
ClipPlane {
  plane 1 0 0 0
  on TRUE
}
\endcode

\par Action Behavior
\par
SoGLRenderAction, SoCallbackAction, SoRayPickAction
<BR> Adds the plane to the current list of clipping planes in the state. 

\par See Also
\par
SoCamera, SoShapeHints
*/
////////////////////////////////////////////////////////////////////////////////

public class SoClipPlane extends SoNode {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoClipPlane.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoClipPlane.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoClipPlane.class); }    	  	
	
    //! \name Fields
    //@{

    //! Plane defining half-space.
    public final SoSFPlane           plane = new SoSFPlane();          

    //! Whether clipping plane is active.
    public final SoSFBool            on = new SoSFBool();             

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoClipPlane()
//
////////////////////////////////////////////////////////////////////////
{
	nodeHeader.SO_NODE_CONSTRUCTOR(/*SoClipPlane*/);

    // Default clipping plane defines the half-space with non-negative x
	nodeHeader.SO_NODE_ADD_SFIELD(plane,"plane", (new SbPlane(new SbVec3f(1.0f, 0.0f, 0.0f), 0.0f)));
	nodeHeader.SO_NODE_ADD_SFIELD(on,"on",    (true));

    isBuiltIn = true;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Destructor
//
// Use: private

public void destructor()
//
////////////////////////////////////////////////////////////////////////
{
	super.destructor();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Typical action method.
//
// Use: extender

public void
SoClipPlane_doAction(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
    // Add clip plane only if it is active
    if (! on.isIgnored() && on.getValue() == false)
        return;

    if (! plane.isIgnored()) {
        SoClipPlaneElement.add(action.getState(), this, plane.getValue());
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Performs callback action
//
// Use: extender

public void callback(SoCallbackAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoClipPlane_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Performs GL rendering on a clipPlane node.
//
// Use: extender

public void GLRender(SoGLRenderAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoClipPlane_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Performs picking on a clipPlane node.
//
// Use: extender

public void pick(SoPickAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoClipPlane_doAction(action);
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoClipPlane class.
//
// Use: internal

public static void initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SoSubNode.SO__NODE_INIT_CLASS(SoClipPlane.class, "ClipPlane", SoNode.class);

    // Enable clip plane element
    SO_ENABLE(SoCallbackAction.class, SoClipPlaneElement.class);
    SO_ENABLE(SoPickAction.class,     SoClipPlaneElement.class);
    SO_ENABLE(SoGLRenderAction.class, SoGLClipPlaneElement.class);
}

}
