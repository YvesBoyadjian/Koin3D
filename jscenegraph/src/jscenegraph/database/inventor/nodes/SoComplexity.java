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
 |      This file defines the SoComplexity node class.
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
import jscenegraph.database.inventor.actions.SoGetBoundingBoxAction;
import jscenegraph.database.inventor.actions.SoPickAction;
import jscenegraph.database.inventor.elements.SoComplexityElement;
import jscenegraph.database.inventor.elements.SoComplexityTypeElement;
import jscenegraph.database.inventor.elements.SoOverrideElement;
import jscenegraph.database.inventor.elements.SoShapeStyleElement;
import jscenegraph.database.inventor.elements.SoTextureOverrideElement;
import jscenegraph.database.inventor.elements.SoTextureQualityElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFEnum;
import jscenegraph.database.inventor.fields.SoSFFloat;
import jscenegraph.database.inventor.misc.SoState;


////////////////////////////////////////////////////////////////////////////////
//! Shape complexity node.
/*!
\class SoComplexity
\ingroup Nodes
This node sets the current shape complexity value. This is a heuristic
value which provides a hint at what geometric complexity to render
shape nodes.  Values range from 0 to 1, where 0 means minimum
complexity and 1 means maximum complexity. Each shape node interprets
complexity in its own way.


Shape complexity always affects rendering and primitive generation for
the SoCallbackAction. For some shapes, it also affects picking.


There are three ways to interpret shape complexity, depending on the
\b type  field. <tt>BOUNDING_BOX</tt> complexity ignores the \b value 
field and renders all shapes as bounding boxes, using the current
material, drawing style, etc. The other two types use the \b value 
field to determine the tessellation of shapes into polygons.
<tt>OBJECT_SPACE</tt> complexity uses \b value  directly to determine the
tessellation.  <tt>SCREEN_SPACE</tt> complexity depends on \b value  and the
projected size of the shape on the screen; a \b value  of 0 produces
the minimum tessellation for a shape, and a \b value  of 1 produces a
tessellation that is fine enough that each edge of a polygon is about 1
or two pixels in length. Since the projected size depends on the
camera position, objects may be tessellated differently every frame if
the camera is moving; note that this may have adverse effects on
render caching in SoSeparator nodes.


The SoComplexity node also sets a hint for the quality of textures
applied to shapes, based on the value of the \b textureQuality  field.
The texture quality will take effect at the next Texture2 node;
Texture2 nodes previously traversed will not be affected.

\par File Format/Default
\par
\code
Complexity {
  type OBJECT_SPACE
  value 0.5
  textureQuality 0.5
}
\endcode

\par Action Behavior
\par
SoGLRenderAction, SoCallbackAction, SoGetBoundingBoxAction, SoRayPickAction
<BR> Sets the current complexity in the state. 

\par See Also
\par
SoShape, SoShapeHints, SoTexture2
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoComplexity extends SoNode {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoComplexity.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoComplexity.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoComplexity.class); }    
	  
	
	
	   public
		        enum Type {
		            OBJECT_SPACE(SoComplexityTypeElement.Type.OBJECT_SPACE.getValue()),
		            SCREEN_SPACE(SoComplexityTypeElement.Type.SCREEN_SPACE.getValue()),
		            BOUNDING_BOX(SoComplexityTypeElement.Type.BOUNDING_BOX.getValue());
		            
		            private int value;
		            
		            Type(int value) {
		            	this.value = value;
		            }
		            public int getValue() {
		            	return value;
		            }
		        };
		    	

    //! \name Fields
      //@{
  
      //! How shape complexity is interpreted.
      public final SoSFEnum            type = new SoSFEnum();
  
      //! Complexity value.
      public final SoSFFloat           value = new SoSFFloat();
  
      //! Hint about texture quality. A value of 0 indicates that the fastest
      //! texturing should be used, while a value of 1 indicates that the best
      //! quality texturing should be used.
      public final SoSFFloat           textureQuality = new SoSFFloat();
      
      
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoComplexity()
//
////////////////////////////////////////////////////////////////////////
{
	nodeHeader.SO_NODE_CONSTRUCTOR();

	nodeHeader.SO_NODE_ADD_SFIELD(type,"type",  (SoComplexityTypeElement.getDefault().getValue()));
	nodeHeader.SO_NODE_ADD_SFIELD(value,"value", (SoComplexityElement.getDefault()));
	nodeHeader.SO_NODE_ADD_SFIELD(textureQuality,"textureQuality",
                      (SoTextureQualityElement.getDefault()));

    // Set up static info for enumerated type field
	nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Type.OBJECT_SPACE);
	nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Type.SCREEN_SPACE);
	nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Type.BOUNDING_BOX);

    // Set up info in enumerated type field
	nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(type, "type", "Type");

    isBuiltIn = true;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Handles any action.
//
// Use: extender

public void
SoComplexity_doAction(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoState state = action.getState();

    if (! type.isIgnored()
        && ! SoOverrideElement.getComplexityTypeOverride(state)) {
        if (isOverride()) {
            SoOverrideElement.setComplexityTypeOverride(state, this, true);
        }
        SoComplexityTypeElement.set(state, SoComplexityTypeElement.Type.fromValue(
                                     type.getValue()));
    }

    if (! value.isIgnored()
        && ! SoOverrideElement.getComplexityOverride(state)) {
        if (isOverride()) {
            SoOverrideElement.setComplexityOverride(state, this, true);
        }
        SoComplexityElement.set(state, value.getValue());
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
    SoComplexity_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does the get bounding box thing
//
// Use: extender

public void
getBoundingBox(SoGetBoundingBoxAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoComplexity_doAction(action);
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
    SoState state = action.getState();

    if (! textureQuality.isIgnored()
        && ! SoTextureOverrideElement.getQualityOverride(state)) {
        if (isOverride()) {
            SoTextureOverrideElement.setQualityOverride(state, true);
        }
        SoTextureQualityElement.set(state, textureQuality.getValue());
    }

    SoComplexity_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does pick action thing.
//
// Use: extender

public void
pick(SoPickAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoComplexity_doAction(action);
}
      

////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoComplexity class.
//
// Use: internal

public static void
initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SoSubNode.SO__NODE_INIT_CLASS(SoComplexity.class, "Complexity", SoNode.class);

    // Enable elements for appropriate actions:
    SO_ENABLE(SoCallbackAction.class, SoComplexityTypeElement.class);
    SO_ENABLE(SoCallbackAction.class, SoShapeStyleElement.class);
    SO_ENABLE(SoCallbackAction.class, SoComplexityElement.class);
    SO_ENABLE(SoGetBoundingBoxAction.class, SoComplexityTypeElement.class);
    SO_ENABLE(SoGetBoundingBoxAction.class, SoShapeStyleElement.class);
    SO_ENABLE(SoGetBoundingBoxAction.class, SoComplexityElement.class);
    SO_ENABLE(SoGLRenderAction.class, SoComplexityTypeElement.class);
    SO_ENABLE(SoGLRenderAction.class, SoShapeStyleElement.class);
    SO_ENABLE(SoGLRenderAction.class, SoComplexityElement.class);
    SO_ENABLE(SoGLRenderAction.class, SoTextureQualityElement.class);
    SO_ENABLE(SoGLRenderAction.class, SoTextureOverrideElement.class);
    SO_ENABLE(SoPickAction.class,     SoComplexityTypeElement.class);
    SO_ENABLE(SoPickAction.class,     SoShapeStyleElement.class);
    SO_ENABLE(SoPickAction.class,     SoComplexityElement.class);
}


  }
