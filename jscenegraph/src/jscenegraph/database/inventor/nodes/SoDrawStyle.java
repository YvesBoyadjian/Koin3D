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
 |      This file defines the SoDrawStyle node class.
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
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.elements.SoDrawStyleElement;
import jscenegraph.database.inventor.elements.SoGLDrawStyleElement;
import jscenegraph.database.inventor.elements.SoGLLinePatternElement;
import jscenegraph.database.inventor.elements.SoGLLineWidthElement;
import jscenegraph.database.inventor.elements.SoGLPointSizeElement;
import jscenegraph.database.inventor.elements.SoLinePatternElement;
import jscenegraph.database.inventor.elements.SoLineWidthElement;
import jscenegraph.database.inventor.elements.SoOverrideElement;
import jscenegraph.database.inventor.elements.SoPointSizeElement;
import jscenegraph.database.inventor.elements.SoShapeStyleElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFEnum;
import jscenegraph.database.inventor.fields.SoSFFloat;
import jscenegraph.database.inventor.fields.SoSFUShort;
import jscenegraph.database.inventor.misc.SoState;


////////////////////////////////////////////////////////////////////////////////
//! Node that defines the style to use when rendering.
/*!
\class SoDrawStyle
\ingroup Nodes
This node defines the current drawing style for all subsequent shape
nodes in a scene graph. SoDrawStyle specifies how primitives
should be rendered. The drawing style has no effect on picking or
callback primitive generation.

\par File Format/Default
\par
\code
DrawStyle {
  style FILLED
  pointSize 0
  lineWidth 0
  linePattern 65535
}
\endcode

\par Action Behavior
\par
SoGLRenderAction, SoCallbackAction
<BR> Sets the current drawing style. 

\par See Also
\par
SoLightModel, SoPickStyle, SoShapeHints
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoDrawStyle extends SoNode {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoDrawStyle.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoDrawStyle.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoDrawStyle.class); }    	  	
	
		  		
	public enum Style {
		  FILLED(SoDrawStyleElement.Style.FILLED.getValue()) ,         //!< Filled regions
		  LINES(SoDrawStyleElement.Style.LINES.getValue()) ,          //!< Outlined regions
		  POINTS(SoDrawStyleElement.Style.POINTS.getValue()) ,         //!< Points
		  INVISIBLE(SoDrawStyleElement.Style.INVISIBLE.getValue());       //!< Nothing!
		  
		  private int value;
		  
		  Style(int value) {
			  this.value = value;
		  }
		  
		  public int getValue() {
			  return value;
		  }
		  
		  public static Style fromValue(int value) {
			  if(value == FILLED.getValue()) return FILLED;
			  if(value == LINES.getValue()) return LINES;
			  if(value == POINTS.getValue()) return POINTS;
			  if(value == INVISIBLE.getValue()) return INVISIBLE;
			  return null;
		  }
	};
		 

		  public final SoSFEnum style = new SoSFEnum();
		   
		  public final SoSFFloat pointSize = new SoSFFloat();
		   
		  public final SoSFFloat lineWidth = new SoSFFloat();
		   
		  public final SoSFUShort linePattern = new SoSFUShort();
		   		  
		  
	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    This initializes the SoDrawStyle class.
	   //
	   // Use: internal
	   
	  public static void
	   initClass()
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       SoSubNode.SO__NODE_INIT_CLASS(SoDrawStyle.class, "DrawStyle", SoNode.class);
	   
	       // Enable elements for appropriate actions:
	   
	       //SO_ENABLE(SoCallbackAction, SoDrawStyleElement);
	       SoCallbackAction.enableElement(SoDrawStyleElement.class);	       
	       //SO_ENABLE(SoCallbackAction, SoLinePatternElement);
	       SoCallbackAction.enableElement(SoLinePatternElement.class);	       
	       //SO_ENABLE(SoCallbackAction, SoLineWidthElement);
	       SoCallbackAction.enableElement(SoLineWidthElement.class);	       
	       //SO_ENABLE(SoCallbackAction, SoPointSizeElement);
	       SoCallbackAction.enableElement(SoPointSizeElement.class);	       
	       //SO_ENABLE(SoCallbackAction, SoShapeStyleElement);
	       SoCallbackAction.enableElement(SoShapeStyleElement.class);	       
	   
	       //SO_ENABLE(SoGLRenderAction, SoGLDrawStyleElement);
	       SoGLRenderAction.enableElement(SoGLDrawStyleElement.class);	       
	       //SO_ENABLE(SoGLRenderAction, SoGLLinePatternElement);
	       SoGLRenderAction.enableElement(SoGLLinePatternElement.class);	       
	       //SO_ENABLE(SoGLRenderAction, SoGLLineWidthElement);
	       SoGLRenderAction.enableElement(SoGLLineWidthElement.class);	       
	       //SO_ENABLE(SoGLRenderAction, SoGLPointSizeElement);
	       SoGLRenderAction.enableElement(SoGLPointSizeElement.class);	       
	       //SO_ENABLE(SoGLRenderAction, SoShapeStyleElement);
	       SoGLRenderAction.enableElement(SoShapeStyleElement.class);     
	   }


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoDrawStyle()
//
////////////////////////////////////////////////////////////////////////
{
    nodeHeader.SO_NODE_CONSTRUCTOR(/*SoDrawStyle*/);

    nodeHeader.SO_NODE_ADD_SFIELD(style,"style",            (SoDrawStyleElement.getDefault().getValue()));
    nodeHeader.SO_NODE_ADD_SFIELD(pointSize,"pointSize",        (SoPointSizeElement.getDefault()));
    nodeHeader.SO_NODE_ADD_SFIELD(lineWidth,"lineWidth",        (SoLineWidthElement.getDefault()));
    nodeHeader.SO_NODE_ADD_SFIELD(linePattern,"linePattern",      (SoLinePatternElement.getDefault()));

    // Set up static info for enumerated type field
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Style.FILLED);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Style.LINES);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Style.POINTS);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Style.INVISIBLE);

    // Set up info in enumerated type field
    nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(style,"style", "Style");

    isBuiltIn = true;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Handles actions.
//
// Use: extender

public void SoDrawStyle_doAction(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoState     state = action.getState();
    boolean        isFilled;

    if (! style.isIgnored()
        && ! SoOverrideElement.getDrawStyleOverride(state)) {
        if (isOverride()) {
            SoOverrideElement.setDrawStyleOverride(state, this, true);
        }
        SoDrawStyleElement.set(state,
                                SoDrawStyleElement.Style.fromValue( style.getValue()));
        isFilled = style.getValue() == Style.FILLED.getValue();
    }
    else
        isFilled = true;

    if (! pointSize.isIgnored()
        && ! SoOverrideElement.getPointSizeOverride(state)) {
        if (isOverride()) {
            SoOverrideElement.setPointSizeOverride(state, this, true);
        }
        SoPointSizeElement.set(state, pointSize.getValue());
    }

    if (! lineWidth.isIgnored()
        && ! SoOverrideElement.getLineWidthOverride(state)) {
        if (isOverride()) {
            SoOverrideElement.setLineWidthOverride(state, this, true);
        }
        SoLineWidthElement.set(state, lineWidth.getValue());
    }

    if (! linePattern.isIgnored()
        && ! SoOverrideElement.getLinePatternOverride(state)) {
        if (isOverride()) {
            SoOverrideElement.setLinePatternOverride(state, this, true);
        }
        SoLinePatternElement.set(state, linePattern.getValue());
    }
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
    SoDrawStyle_doAction(action);
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
    SoDrawStyle_doAction(action);
}



}
