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
 |      This file defines the SoProfileCoordinate3 node class.
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
import jscenegraph.database.inventor.actions.SoPickAction;
import jscenegraph.database.inventor.elements.SoProfileCoordinateElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFVec3f;

/**
 * @author Yves Boyadjian
 *
 */


////////////////////////////////////////////////////////////////////////////////
//! Rational profile coordinate node.
/*!
\class SoProfileCoordinate3
\ingroup Nodes
This node defines a set of rational 3D coordinates to be used by
subsequent SoProfile nodes. (These coordinates may be used for any
type of profile; they may be useful in some cases for specifying
control points for SoNurbsProfile nodes.) This node does not
produce a visible result during rendering; it simply replaces the
current profile coordinates in the traversal state for subsequent
nodes to use.

\par File Format/Default
\par
\code
ProfileCoordinate3 {
  point 0 0 1
}
\endcode

\par Action Behavior
\par
SoGLRenderAction, SoCallbackAction, SoGetBoundingBoxAction, SoRayPickAction
<BR> Sets profile coordinates in current traversal state. 

\par See Also
\par
SoProfile, SoProfileCoordinate2
*/
////////////////////////////////////////////////////////////////////////////////

public class SoProfileCoordinate3 extends SoNode {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoProfileCoordinate3.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoProfileCoordinate3.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoProfileCoordinate3.class); }    	  	
	
    //! \name Fields
    //@{

    //! Rational 3D profile coordinate points.
    public final SoMFVec3f           point = new SoMFVec3f();          

    //@}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoProfileCoordinate3()
//
////////////////////////////////////////////////////////////////////////
{
    nodeHeader.SO_NODE_CONSTRUCTOR(/*SoProfileCoordinate3.class*/);
    nodeHeader.SO_NODE_ADD_MFIELD(point,"point", (SoProfileCoordinateElement.getDefault3()));
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
//    Handles actions (just changes the state)
//
// Use: extender

public void
SoProfileCoordinate3_doAction(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
    if (! point.isIgnored() && point.getNum() > 0)
        SoProfileCoordinateElement.set3(action.getState(), this,
                                         point.getNum(), point.getValues(0));
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does GL render action.
//
// Use: extender

public void
GLRender(SoGLRenderAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoProfileCoordinate3_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does callback action thing.
//
// Use: extender

public void
callback(SoCallbackAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoProfileCoordinate3_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does pick action...
//
// Use: extender

public void
pick(SoPickAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoProfileCoordinate3_doAction(action);
}
    

////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoProfileCoordinate3 class.
//
// Use: internal

public static void
initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SO__NODE_INIT_CLASS(SoProfileCoordinate3.class, "ProfileCoordinate3", SoNode.class);
}

}
