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
 *
 _______________________________________________________________________
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 |
 |   $Revision: 1.1.1.1 $
 |
 |   Description:
 |      Defines the SoFontStyle class
 |
 |   Author(s): Chris Marrin
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoGetBoundingBoxAction;
import jscenegraph.database.inventor.actions.SoPickAction;
import jscenegraph.database.inventor.elements.SoFontNameElement;
import jscenegraph.database.inventor.elements.SoFontSizeElement;
import jscenegraph.database.inventor.elements.SoOverrideElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFBitMask;
import jscenegraph.database.inventor.fields.SoSFEnum;
import jscenegraph.database.inventor.misc.SoState;


////////////////////////////////////////////////////////////////////////////////
//! Simple 3D text shape node.
/*!
\class SoFontStyle
\ingroup Nodes
This node defines the current font family and style for all
subsequent text shapes in the scene graph.

\par File Format/Default
\par
\code
FontStyle {
  name "defaultFont"
  size 10
  family SERIF
  style NONE
}
\endcode

\par Action Behavior
\par
SoGLRenderAction, SoCallbackAction, SoGetBoundingBoxAction, SoRayPickAction
<BR> Sets the font family and style in the current traversal state. 

\par See Also
\par
SoAsciiText, SoFont, SoText2, SoText3
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoFontStyle extends SoFont {
	
	public enum Family {
		SERIF(0),
		SANS(1),
		TYPEWRITER(2);
		
		private int value;
		
		Family(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
	}
	
	public enum Style {
		NONE(0),
		BOLD(1),
		ITALIC(2);
		
		private int value;
		
		Style(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
	}
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoFontStyle.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoFontStyle.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoFontStyle.class); }    	  	
	
	
	
	public final SoSFEnum family = new SoSFEnum();
	public final SoSFBitMask style = new SoSFBitMask();
	
	private
    static String[][] fontList =
    {
        { "Times", "Timesbd", 
          "Timesi", "Timesbi" }, 
        { "Arial", "Arialbd", 
          "Ariali", "Arialbi" }, 
        { "Cour", "Courbd", 
          "Couri", "Courbi" }, 
    };	
	

////////////////////////////////////////////////////////////////////////
//
//  Constructor
//  
public SoFontStyle()
//
////////////////////////////////////////////////////////////////////////
{
    nodeHeader.SO_NODE_CONSTRUCTOR(/*SoFontStyle*/);
    nodeHeader.SO_NODE_ADD_SFIELD(family,"family", (Family.SERIF.getValue()));
    nodeHeader.SO_NODE_ADD_SFIELD(style,"style", (Style.NONE.getValue()));

    // Set up static info for enumerated type field
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Family.SERIF);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Family.SANS);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Family.TYPEWRITER);

    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Style.NONE);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Style.BOLD);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Style.ITALIC);

    // Set up info in enumerated type field
    nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(family,"family", "Family");
    nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(style,"style", "Style");

    isBuiltIn = true;
}

////////////////////////////////////////////////////////////////////////
//
//  Destructor
//  
public void destructor()
//
////////////////////////////////////////////////////////////////////////
{
	super.destructor();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Gets the font name based on the family and style
//
// Use: protected

protected String
getFontName()
//
////////////////////////////////////////////////////////////////////////
{   
    int f = family.getValue();
    int s = style.getValue();
    
    if (family.isIgnored()) f = Family.SERIF.getValue();
    if (style.isIgnored()) s = Style.NONE.getValue();
    
    if (f < 0 || f > 2) f = 0;
    if (s < 0 || s > 3) s = 0;
    
    return fontList[f][s];
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Handles actions
//
// Use: extender

private void
SoFontStyle_doAction(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{   
    SoState state = action.getState();

    // set font to the given font style
    if ((!family.isIgnored() || !style.isIgnored()) 
        && !SoOverrideElement.getFontNameOverride(state)
                                                        ) {
        
        String font = getFontName();
        
        if (isOverride()) {
            SoOverrideElement.setFontNameOverride(state, this, true);
        }
        SoFontNameElement.set(state, this, new SbName(font));
    }
    
    if (! size.isIgnored()
        && ! SoOverrideElement.getFontSizeOverride(state)
                                                        ) {
        if (isOverride()) {
            SoOverrideElement.setFontSizeOverride(state, this, true);
        }
        SoFontSizeElement.set(state, /*this,*/ size.getValue());
    }
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
    SoFontStyle_doAction(action);
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
    SoFontStyle_doAction(action);
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
    SoFontStyle_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does get bounding box action...
//
// Use: extender

public void
getBoundingBox(SoGetBoundingBoxAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoFontStyle_doAction(action);
}
	

////////////////////////////////////////////////////////////////////////
//
//  This initializes the SoFontStyle class.
//  
public static void initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SoSubNode.SO__NODE_INIT_CLASS(SoFontStyle.class, "FontStyle", SoFont.class);
}


}
