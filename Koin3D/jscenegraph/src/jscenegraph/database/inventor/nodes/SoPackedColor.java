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
 |   $Revision: 1.2 $
 |
 |   Description:
 |      This file defines the SoPackedColor node class.
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
import jscenegraph.database.inventor.elements.SoGLLazyElement;
import jscenegraph.database.inventor.elements.SoLazyElement;
import jscenegraph.database.inventor.elements.SoOverrideElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFUInt32;
import jscenegraph.database.inventor.misc.SoNotList;
import jscenegraph.database.inventor.misc.SoNotRec;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.mevis.inventor.elements.SoGLVBOElement;
import jscenegraph.mevis.inventor.misc.SoVBO;


////////////////////////////////////////////////////////////////////////////////
//! Node that defines base colors using packed representation.
/*!
\class SoPackedColor
\ingroup Nodes
SoPackedColor is similar to SoBaseColor in that it sets the
diffuse color component of the current material. However, it also
changes the transparency component. The color and transparency
information is packed into unsigned 32-bit integers: <tt>0xrrggbbaa</tt>,
where <tt>aa</tt> represents the alpha (<tt>0x00</tt> = fully transparent,
<tt>0xff</tt> = opaque), and <tt>rr</tt>, <tt>gg</tt>, and <tt>bb</tt> represent the
red, blue, and green components of the color, respectively.
Note that the order (r,g,b,a) of these components is reversed from
the ordering in releases of Inventor prior to 2.1.


If the
transparency type is SoGLRenderAction::SCREEN_DOOR, only the
first transparency value will be used.  With other transparency types,
multiple transparencies will be used.  


SoPackedColor uses less memory than SoBaseColor or
SoMaterial  to store multiple color and transparency values. It
can be used in cases where space is critical.

\par File Format/Default
\par
\code
PackedColor {
orderedRGBA 0xccccccff
}
\endcode

\par Action Behavior
\par
SoGLRenderAction, SoCallbackAction
<BR> Sets the current base (diffuse) color(s) in the state. 

\par See Also
\par
SoBaseColor, SoMaterial
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoPackedColor extends SoNode {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoPackedColor.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoPackedColor.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoPackedColor.class); }    	  	
	
	
	  public
		       //! \name Fields
		       //@{
		   
		       //! Defines the packed colors.
		    final   SoMFUInt32          orderedRGBA = new SoMFUInt32();

	      //! store whether transparent or not
    private boolean transparent;
    
    private final SoVBO[] _vbo = new SoVBO[1];
	  
	  
	  ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    This initializes the SoPackedColor class.
	   //
	   // Use: internal
	   
	  public static void
	   initClass()
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       SoSubNode.SO__NODE_INIT_CLASS(SoPackedColor.class, "PackedColor", SoNode.class);
	   }
	   
	  public    boolean              isTransparent()
        {return transparent;}
	  

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoPackedColor()
//
////////////////////////////////////////////////////////////////////////
{
    nodeHeader.SO_NODE_CONSTRUCTOR(/*SoPackedColor*/);
    nodeHeader.SO_NODE_ADD_MFIELD(orderedRGBA,"orderedRGBA", (SoLazyElement.getDefaultPacked()));
    isBuiltIn = true;
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
	if(_vbo[0] != null)
		_vbo[0].destructor();
	super.destructor();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Typical action method.
//
// Use: extender

private void
SoPackedColor_doAction(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoState state = action.getState();

    if (! orderedRGBA.isIgnored() && orderedRGBA.getNum() > 0
        && ! SoOverrideElement.getDiffuseColorOverride(state)) {
        if (isOverride()) {
            SoOverrideElement.setDiffuseColorOverride(state, this, true);
        }
        SoLazyElement.setPacked(state, this,
                        orderedRGBA.getNum(), orderedRGBA.getValuesI(0));

        if (state.isElementEnabled(SoGLVBOElement.getClassStackIndex(SoGLVBOElement.class))) {
          // update the VBO, no data is passed since that is done by SoColorPacker later on
          SoGLVBOElement.updateVBO(state, SoGLVBOElement.VBOType.COLOR_VBO, _vbo);
        }
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Performs GL rendering on a packedColor node.
//
// Use: extender

public void GLRender(SoGLRenderAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoPackedColor_doAction(action);

    // If there's only one color, we might as well send it now.  This
    // prevents cache dependencies in some cases that were
    // specifically optimized for Inventor 2.0.
    if (orderedRGBA.getNum() == 1 && !orderedRGBA.isIgnored()) {
        SoGLLazyElement.sendAllMaterial(action.getState());
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
    SoPackedColor_doAction(action);
}

///////////////////////////////////////////////////////////////////////////
//
// Description:
// When notified of change in orderedRGBA field, reevaluate transparent flag
//
// use: SoExtender public
//
public void notify(SoNotList list)
//
////////////////////////////////////////////////////////////////////////
{
    if ((list.getLastRec().getType() == SoNotRec.Type.CONTAINER) &&
        (list.getLastField() == orderedRGBA) ) {
        transparent = false;
        for(int i = 0; i < orderedRGBA.getNum(); i++){
            if((orderedRGBA.operator_square_bracketI(i) & 0xff) != 0xff){
                transparent = true;
                break;
            }   
        }
    }

    super.notify(list);
}
	  
}
