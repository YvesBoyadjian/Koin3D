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
 |      This file defines the SoPickStyle node class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoPickAction;
import jscenegraph.database.inventor.elements.SoOverrideElement;
import jscenegraph.database.inventor.elements.SoPickStyleElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFEnum;
import jscenegraph.database.inventor.misc.SoState;


////////////////////////////////////////////////////////////////////////////////
//! Picking style node.
/*!
\class SoPickStyle
\ingroup Nodes
This node determines how subsequent geometry nodes in the scene graph
are to be picked, as indicated by the \b style  field.


Note that this is the only way to change the pick behavior of shapes;
drawing style, complexity, and other rendering-related properties have
no effect on picking.

\par File Format/Default
\par
\code
PickStyle {
  style SHAPE
}
\endcode

\par Action Behavior
\par
SoRayPickAction, SoCallbackAction
<BR> Sets the current pick style in the state. 

\par See Also
\par
SoComplexity, SoDrawStyle, SoRayPickAction
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoPickStyle extends SoNode {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoPickStyle.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoPickStyle.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoPickStyle.class); }    	  	
	
	
	  public enum Style {
		    SHAPE(SoPickStyleElement.Style.SHAPE.getValue()),
		    BOUNDING_BOX(SoPickStyleElement.Style.BOUNDING_BOX.getValue()),
		    UNPICKABLE(SoPickStyleElement.Style.UNPICKABLE.getValue());
		    
		    int value;
		    Style(int value) {
		    	this.value = value;
		    }
		    public int getValue() {
		    	return value;
		    }
		    };
		   
		   
		   
	public final SoSFEnum style = new SoSFEnum();
		   
		  
	 ////////////////////////////////////////////////////////////////////////
	  //
	  // Description:
	  //    This initializes the SoPickStyle class.
	  //
	  // Use: internal
	  
	 public static void
	  initClass()
	  //
	  ////////////////////////////////////////////////////////////////////////
	  {
	      SoSubNode.SO__NODE_INIT_CLASS(SoPickStyle.class, "PickStyle", SoNode.class);
	  
	      // Enable elements for picking actions:
	      //SO_ENABLE(SoCallbackAction, SoPickStyleElement);
	      SoCallbackAction.enableElement(SoPickStyleElement.class);
	      //SO_ENABLE(SoPickAction,     SoPickStyleElement);
	      SoPickAction.enableElement(SoPickStyleElement.class);
	  }


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoPickStyle()
//
////////////////////////////////////////////////////////////////////////
{
    nodeHeader.SO_NODE_CONSTRUCTOR(/*SoPickStyle*/);
    nodeHeader.SO_NODE_ADD_SFIELD(style,"style", (SoPickStyleElement.getDefault().getValue()));

    // Set up static info for enumerated type field
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Style.SHAPE);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Style.BOUNDING_BOX);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Style.UNPICKABLE);

    // Set up info in enumerated type field
    nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(style,"style", "Style");

    isBuiltIn = true;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Handles most actions.
//
// Use: extender

public void SoPickStyle_doAction(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoState state = action.getState();

    if (! style.isIgnored()
        && ! SoOverrideElement.getPickStyleOverride(state)) {
        if (isOverride()) {
            SoOverrideElement.setPickStyleOverride(state, this, true);
        }
        SoPickStyleElement.set(state,
                                SoPickStyleElement.Style.fromValue( style.getValue()));
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Handles callback action.
//
// Use: extender

public void
callback(SoCallbackAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoPickStyle_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Handles pick action.
//
// Use: extender

public void
pick(SoPickAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoPickStyle_doAction(action);
}
	 
}
