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
 |      This file defines the SoBaseColor node class.
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
import jscenegraph.database.inventor.elements.SoColorPacker;
import jscenegraph.database.inventor.elements.SoGLLazyElement;
import jscenegraph.database.inventor.elements.SoLazyElement;
import jscenegraph.database.inventor.elements.SoOverrideElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFColor;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.mevis.inventor.elements.SoGLVBOElement;
import jscenegraph.mevis.inventor.misc.SoVBO;
import jscenegraph.port.Destroyable;


////////////////////////////////////////////////////////////////////////////////
//! Node that defines an object's base color.
/*!
\class SoBaseColor
\ingroup Nodes
This node defines the base color (or colors) of subsequent shape nodes
in the scene graph. SoBaseColor sets only the diffuse color(s) of
the current material and has no effect on the material's other
attributes.

\par File Format/Default
\par
\code
BaseColor {
  rgb 0.8 0.8 0.8
}
\endcode

\par Action Behavior
\par
SoGLRenderAction, SoCallbackAction
<BR> Sets the current base color in the state. 

\par See Also
\par
SoMaterial, SoPackedColor
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoBaseColor extends SoNode implements Destroyable {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoBaseColor.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoBaseColor.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoBaseColor.class); }    	  	
	

	public final SoMFColor rgb = new SoMFColor();
	
    protected SoColorPacker colorPacker;

    protected final SoVBO[] _vbo = new SoVBO[1];
	

	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    This initializes the SoBaseColor class.
	   //
	   // Use: internal
	   
	  public static void
	   initClass()
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       SoSubNode.SO__NODE_INIT_CLASS(SoBaseColor.class, "BaseColor", SoNode.class);
	       //SO_ENABLE(SoGLRenderAction, SoGLVBOElement);
	       SoGLRenderAction.enableElement(SoGLVBOElement.class);
	   }
	  

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoBaseColor()
//
////////////////////////////////////////////////////////////////////////
{
    nodeHeader.SO_NODE_CONSTRUCTOR(/*SoBaseColor*/);
    nodeHeader.SO_NODE_ADD_MFIELD(rgb,"rgb", (SoLazyElement.getDefaultDiffuse()));
    isBuiltIn = true;
    colorPacker = new SoColorPacker();
    _vbo[0] = null;
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
    colorPacker.destructor();
    if(_vbo[0] != null) {
    	_vbo[0].destructor();
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Typical action method.
//
// Use: extender

public void
SoBaseColor_doAction(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoState state = action.getState();

    if (! rgb.isIgnored() && rgb.getNum() > 0
        && ! SoOverrideElement.getDiffuseColorOverride(state)) {
        if (isOverride()) {
            SoOverrideElement.setDiffuseColorOverride(state, this, true);
        }
        SoGLLazyElement.setDiffuse(state, this, rgb.getNum(), 
            rgb.getValues(0), colorPacker);

        if (state.isElementEnabled(SoGLVBOElement.getClassStackIndex(SoGLVBOElement.class))) {
          // update the VBO, no data is passed since that is done by SoColorPacker later on
          SoGLVBOElement.updateVBO(state, SoGLVBOElement.VBOType.COLOR_VBO, _vbo);
        }
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Performs GL rendering on a baseColor node.
//
// Use: extender

public void
GLRender(SoGLRenderAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoBaseColor_doAction(action);

    // If there's only one color, we might as well send it now.  This
    // prevents cache dependencies in some cases that were
    // specifically optimized for Inventor 2.0.
    if (rgb.getNum() == 1)
        SoGLLazyElement.sendAllMaterial(action.getState());
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Performs callback action
//
// Use: extender

public void
callback(SoCallbackAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoBaseColor_doAction(action);
}
	  
}
