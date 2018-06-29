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
 |   $Revision $
 |
 |   Description:
 |      This file defines the SoProfile node class.
 |
 |   Author(s)          : Thad Beier, Dave Immel, Paul Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import java.util.function.Consumer;
import java.util.function.IntConsumer;

import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoGetBoundingBoxAction;
import jscenegraph.database.inventor.actions.SoPickAction;
import jscenegraph.database.inventor.elements.SoProfileCoordinateElement;
import jscenegraph.database.inventor.elements.SoProfileElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFInt32;
import jscenegraph.database.inventor.fields.SoSFEnum;
import jscenegraph.database.inventor.misc.SoState;

/**
 * @author Yves Boyadjian
 *
 */

////////////////////////////////////////////////////////////////////////////////
//! Abstract base class for all profile nodes.
/*!
\class SoProfile
\ingroup Nodes
This node is the abstract base class for all profile nodes, which
define 2D curves. A profile is not itself geometry, but is used to
change or delimit the geometry of something else. For an SoText3
node, the profile determines the cross-section of the side of each
text character. For an SoNurbsSurface node, the profile is used to
specify trim curves for the surface.


The current profile state can consist of one or more profiles, each of
which can be made up of one or more instances of SoProfile
subclass nodes. Each profile node specifies (in the \b index  field) a
set of indices that refer to the current set of profile coordinates,
specified using either an SoProfileCoordinate2 or an
SoProfileCoordinate3 node. No profile curve should intersect
itself or another profile curve.


Profiles are part of the state, just like all other properties. The
state contains a current list of profiles.  Depending on the
\b linkage  field, a profile can clear the list and begin a new
profile, begin a new profile at the end of those already in the list,
or append to the last profile in the current list. Note that when
appending profile B to the end of profile A, B must begin at the same
2D point at which A ends.

\par Action Behavior
\par
SoGLRenderAction, SoCallbackAction, SoGetBoundingBoxAction, SoRayPickAction
<BR> Adds profile to current traversal state. 

\par See Also
\par
SoLinearProfile, SoNurbsProfile, SoNurbsSurface, SoProfileCoordinate2, SoProfileCoordinate3, SoText3
*/
////////////////////////////////////////////////////////////////////////////////

public abstract class SoProfile extends SoNode {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_ABSTRACT_HEADER(SoProfile.class,this);
	   
	public                                                                     
    static SoType       getClassTypeId()        /* Returns class type id */   
                                    { return SoSubNode.getClassTypeId(SoProfile.class); }                   
    public SoType      getTypeId()       /* Returns type id      */
    {
		return nodeHeader.getClassTypeId();		    	
    }
  public                                                                  
    SoFieldData   getFieldData() {
	  return nodeHeader.getFieldData(); 
  }
  public  static SoFieldData[] getFieldDataPtr()                              
        { return SoSubNode.getFieldDataPtr(SoProfile.class); }              

	
  public
    enum Profile {
        START_FIRST    ( SoProfileElement.Profile.START_FIRST.ordinal()),
        START_NEW      ( SoProfileElement.Profile.START_NEW.ordinal()),
        ADD_TO_CURRENT ( SoProfileElement.Profile.ADD_TO_CURRENT.ordinal());
    	
    	private int value;
    	
    	Profile(int value) {
    		this.value = value;
    	}
    	
    	public int getValue() {
    		return value;
    	}
    };

    //! \name Fields
    //@{

    //! Indices into profile coordinates.
    public final SoMFInt32           index = new SoMFInt32();        

    //! Specifies connectivity of profile curve with respect to profiles in
    //! current list in state.
    public final SoSFEnum            linkage = new SoSFEnum();      

    //! Returns vertices approximating the profile
    abstract void        getVertices(final SoState state, final IntConsumer nVertices,
                                    final Consumer<SbVec2f[]> vertices);


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: protected

public SoProfile()
//
////////////////////////////////////////////////////////////////////////
{
    nodeHeader.SO_NODE_CONSTRUCTOR(/*SoProfile.class*/);
    nodeHeader.SO_NODE_ADD_MFIELD(index,"index",   (0));
    nodeHeader.SO_NODE_ADD_FIELD(linkage,"linkage", (Profile.START_FIRST.getValue()));

    // Set up static info for enumerated type field
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Profile.START_FIRST);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Profile.START_NEW);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Profile.ADD_TO_CURRENT);

    nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(linkage,"linkage", "Profile");
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
//    Implements all actions.
//
// Use: extender

private void
SoProfile_doAction(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoProfileElement.add(action.getState(), this);
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
    SoProfile_doAction(action);
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
    SoProfile_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does getBoundingBox action.
//
// Use: extender

public void getBoundingBox(SoGetBoundingBoxAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoProfile_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does pick action.
//
// Use: extender

public void pick(SoPickAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoProfile_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoProfile class.
//
// Use: internal

public static void initClass()
//
////////////////////////////////////////////////////////////////////////
{
	SO__NODE_INIT_ABSTRACT_CLASS(SoProfile.class, "Profile", SoNode.class);

    SO_ENABLE(SoCallbackAction.class,         SoProfileElement.class);
    SO_ENABLE(SoCallbackAction.class,         SoProfileCoordinateElement.class);
    SO_ENABLE(SoGLRenderAction.class,         SoProfileElement.class);
    SO_ENABLE(SoGLRenderAction.class,         SoProfileCoordinateElement.class);
    SO_ENABLE(SoGetBoundingBoxAction.class,   SoProfileElement.class);
    SO_ENABLE(SoGetBoundingBoxAction.class,   SoProfileCoordinateElement.class);
    SO_ENABLE(SoPickAction.class,             SoProfileElement.class);
    SO_ENABLE(SoPickAction.class,             SoProfileCoordinateElement.class);
}

}
