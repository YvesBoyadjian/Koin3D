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
 * Copyright (C) 1990,91,92  Silicon Graphics, Inc.
 *
 _______________________________________________________________________
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 |
 |   $Revision: 1.1.1.1 $
 |
 |   Description:
 |      This file defines the SoProfileCoordinate2 node class.
 |
 |   Author(s)          : Thad Beier, Dave Immel, Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoGetBoundingBoxAction;
import jscenegraph.database.inventor.actions.SoPickAction;
import jscenegraph.database.inventor.elements.SoProfileCoordinateElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFVec2f;

/**
 * @author Yves Boyadjian
 *
 */

////////////////////////////////////////////////////////////////////////////////
//! Profile coordinate node.
/*!
\class SoProfileCoordinate2
\ingroup Nodes
This node defines a set of 2D coordinates to be used by subsequent
SoProfile nodes. This node does not produce a visible result
during rendering; it simply replaces the current profile coordinates
in the traversal state for subsequent nodes to use.

\par File Format/Default
\par
\code
ProfileCoordinate2 {
  point 0 0
}
\endcode

\par Action Behavior
\par
SoGLRenderAction, SoCallbackAction, SoGetBoundingBoxAction, SoRayPickAction
<BR> Sets profile coordinates in current traversal state. 

\par See Also
\par
SoProfile, SoProfileCoordinate3
*/
////////////////////////////////////////////////////////////////////////////////

public class SoProfileCoordinate2 extends SoNode {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoProfileCoordinate2.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoProfileCoordinate2.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoProfileCoordinate2.class); }    	  	
	
  public
    //! \name Fields
    //@{

    //! 2D profile coordinate points.
    final SoMFVec2f           point = new SoMFVec2f();          


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoProfileCoordinate2()
//
////////////////////////////////////////////////////////////////////////
{
    nodeHeader.SO_NODE_CONSTRUCTOR(/*SoProfileCoordinate2.class*/);
    nodeHeader.SO_NODE_ADD_MFIELD(point,"point", (SoProfileCoordinateElement.getDefault2()));
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
//    Handles most actions.
//
// Use: extender

private void
SoProfileCoordinate2_doAction(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
    if (! point.isIgnored() && point.getNum() > 0)
        SoProfileCoordinateElement.set2(action.getState(), this,
                                         point.getNum(), point.getValues(0));
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does GL render action.
//
// Use: extender

public void GLRender(SoGLRenderAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoProfileCoordinate2_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does callback action thing.
//
// Use: extender

public void callback(SoCallbackAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoProfileCoordinate2_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does pick action...
//
// Use: extender

public void pick(SoPickAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoProfileCoordinate2_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does get bounding box action...
//
// Use: extender

public void getBoundingBox(SoGetBoundingBoxAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoProfileCoordinate2_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoProfileCoordinate2 class.
//
// Use: internal

public static void initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SO__NODE_INIT_CLASS(SoProfileCoordinate2.class, "ProfileCoordinate2", SoNode.class);

    // Enable elements for appropriate actions:
    SO_ENABLE(SoGLRenderAction.class, SoProfileCoordinateElement.class);
    SO_ENABLE(SoCallbackAction.class, SoProfileCoordinateElement.class);
    SO_ENABLE(SoPickAction.class,     SoProfileCoordinateElement.class);
    SO_ENABLE(SoGetBoundingBoxAction.class,   SoProfileCoordinateElement.class);
}

}
