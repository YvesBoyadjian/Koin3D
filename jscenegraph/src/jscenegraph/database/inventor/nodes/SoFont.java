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
 |      This file defines the SoFont node class.
 |
 |   Author(s)          : Thad Beier, Gavin Bell
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
import jscenegraph.database.inventor.elements.SoFontNameElement;
import jscenegraph.database.inventor.elements.SoFontSizeElement;
import jscenegraph.database.inventor.elements.SoOverrideElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFFloat;
import jscenegraph.database.inventor.fields.SoSFName;
import jscenegraph.database.inventor.misc.SoState;


////////////////////////////////////////////////////////////////////////////////
//! Node that defines font type and size for text.
/*!
\class SoFont
\ingroup Nodes
This node defines the current font type and point size for all
subsequent text shapes in the scene graph.  Fonts are specified with
PostScript names, except for the default font. The default font is
called <tt>"defaultFont"</tt> and is the standard SGI graphics font for 2D
text. <tt>"Utopia"</tt> is the standard Inventor font for 3D text.

\par File Format/Default
\par
\code
Font {
  name "defaultFont"
  size 10
}
\endcode

\par Action Behavior
\par
SoGLRenderAction, SoCallbackAction, SoGetBoundingBoxAction, SoRayPickAction
<BR> Sets the font name and size in the current traversal state. 

\par See Also
\par
SoText2, SoText3
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoFont extends SoNode {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoFont.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoFont.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoFont.class); }    	  	
	

	public final SoSFName name = new SoSFName();
	
	public final SoSFFloat size = new SoSFFloat();

	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    This initializes the SoFont class.
	   //
	   // Use: internal
	   
	  public static void
	   initClass()
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       SoSubNode.SO__NODE_INIT_CLASS(SoFont.class, "Font", SoNode.class);
	   
	       // Enable elements for appropriate actions:
//	       SO_ENABLE(SoGLRenderAction,         SoFontNameElement);
	       SoGLRenderAction.enableElement(SoFontNameElement.class);
//	       SO_ENABLE(SoGLRenderAction,         SoFontSizeElement);
	       SoGLRenderAction.enableElement(SoFontSizeElement.class);
//	       SO_ENABLE(SoPickAction,             SoFontNameElement);
	       SoPickAction.enableElement(SoFontNameElement.class);
//	       SO_ENABLE(SoPickAction,             SoFontSizeElement);
	       SoPickAction.enableElement(SoFontSizeElement.class);
//	       SO_ENABLE(SoGetBoundingBoxAction,   SoFontNameElement);
	       SoGetBoundingBoxAction.enableElement(SoFontNameElement.class);
//	       SO_ENABLE(SoGetBoundingBoxAction,   SoFontSizeElement);
	       SoGetBoundingBoxAction.enableElement(SoFontSizeElement.class);
//	       SO_ENABLE(SoCallbackAction,         SoFontNameElement);
	       SoCallbackAction.enableElement(SoFontNameElement.class);
//	       SO_ENABLE(SoCallbackAction,         SoFontSizeElement);
	       SoCallbackAction.enableElement(SoFontSizeElement.class);
	   }
	  

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoFont()
//
////////////////////////////////////////////////////////////////////////
{
    nodeHeader.SO_NODE_CONSTRUCTOR(/*SoFont*/);
    nodeHeader.SO_NODE_ADD_SFIELD(name,"name", (SoFontNameElement.getDefault()));
    nodeHeader.SO_NODE_ADD_SFIELD(size,"size", (SoFontSizeElement.getDefault()));
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
//    Handles actions
//
// Use: extender

private void
SoFont_doAction(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoState state = action.getState();

    if (! name.isIgnored()
        && ! SoOverrideElement.getFontNameOverride(state)) {
        if (isOverride()) {
            SoOverrideElement.setFontNameOverride(state, this, true);
        }
        SoFontNameElement.set(state, this, name.getValue());
    }

    if (! size.isIgnored()
        && ! SoOverrideElement.getFontSizeOverride(state)) {
        if (isOverride()) {
            SoOverrideElement.setFontSizeOverride(state, this, true);
        }
        SoFontSizeElement.set(state, size.getValue());
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
    SoFont_doAction(action);
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
    SoFont_doAction(action);
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
    SoFont_doAction(action);
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
    SoFont_doAction(action);
}
	  
}
