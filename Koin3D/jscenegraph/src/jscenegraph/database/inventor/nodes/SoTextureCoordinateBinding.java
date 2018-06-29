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
 |      This file defines the SoTextureCoordinateBinding node class.
 |
 |   Author(s)          : John Rohlf
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoPickAction;
import jscenegraph.database.inventor.elements.SoTextureCoordinateBindingElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFEnum;

/**
 * @author Yves Boyadjian
 *
 */

////////////////////////////////////////////////////////////////////////////////
//! Node that specifies how texture coordinates are bound to shapes.
/*!
\class SoTextureCoordinateBinding
\ingroup Nodes
This node specifies how the current texture coordinates are bound to
vertex-based shapes that follow in the scene graph. The <tt>DEFAULT</tt>
binding causes each shape to define its own default coordinates.
These default coordinates typically cause a texture to be mapped
across the whole surface of a shape.

\par File Format/Default
\par
\code
TextureCoordinateBinding {
  value PER_VERTEX_INDEXED
}
\endcode

\par Action Behavior
\par
SoGLRenderAction, SoCallbackAction
<BR> Sets the current texture coordinate binding type. 

\par See Also
\par
SoMaterialBinding, SoNormalBinding, SoTexture2, SoTexture2Transform, SoTextureCoordinate2, SoTextureCoordinateFunction, SoVertexShape
*/
////////////////////////////////////////////////////////////////////////////////

public class SoTextureCoordinateBinding extends SoNode {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoTextureCoordinateBinding.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoTextureCoordinateBinding.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoTextureCoordinateBinding.class); }    
	
	  public
		    //! Binding value
		    enum Binding {
		        PER_VERTEX( SoTextureCoordinateBindingElement.Binding.PER_VERTEX.getValue()),
		        PER_VERTEX_INDEXED(
		            SoTextureCoordinateBindingElement.Binding.PER_VERTEX_INDEXED.getValue());
		    	
		    	private int value; 
		    	Binding(int value) {
		    		this.value = value;
		    	}
		    	public int getValue() {
		    		return value;
		    	}
		    };

		    //! \name Fields
		    //@{

		    //! Specifies how to bind texture coordinates to shapes.
		    public final SoSFEnum            value = new SoSFEnum();          

		    
    //! Creates a texture coordinate binding node with default settings.
    public SoTextureCoordinateBinding() {
    nodeHeader.SO_NODE_CONSTRUCTOR(/*SoTextureCoordinateBinding.class*/);
    nodeHeader.SO_NODE_ADD_FIELD(value,"value",
                         (SoTextureCoordinateBindingElement.getDefault().getValue()));

    // Set up static info for enumerated type field
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Binding.PER_VERTEX);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Binding.PER_VERTEX_INDEXED);

    // And obsolete bindings:
    if (nodeHeader.firstInstance()) {
    	getFieldData().addEnumValue("Binding", "DEFAULT", 0);
    }

    // Set up info in enumerated type field
    nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(value,"value", "Binding");

    isBuiltIn = true;
		    	
    }

    
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Handles actions
//
// Use: extender

void
SoTextureCoordinateBinding_doAction(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
    if (! value.isIgnored()) {
        SoTextureCoordinateBindingElement.set(action.getState(),
             SoTextureCoordinateBindingElement.Binding.fromValue(value.getValue()));
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Override read to translate obsolete bindings
//
// Use: extender

public boolean readInstance(SoInput in, short flags)
//
////////////////////////////////////////////////////////////////////////
{
    boolean result = super.readInstance(in, flags);

    // Deal with obsolete bindings:
    int b = value.getValue();
    if (b == 0 || b == 1) value.setValue( Binding.PER_VERTEX_INDEXED);

    return result;
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
    SoTextureCoordinateBinding_doAction(action);
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
    SoTextureCoordinateBinding_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does pick action...
//
// Use: protected

public void pick(SoPickAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoTextureCoordinateBinding_doAction(action);
}

    
		    
////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoTextureCoordinateBinding class.
//
// Use: internal

public static void initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SO__NODE_INIT_CLASS(SoTextureCoordinateBinding.class, "TextureCoordinateBinding", 
                       SoNode.class);

    // Enable elements for appropriate actions:
    SO_ENABLE(SoCallbackAction.class, SoTextureCoordinateBindingElement.class);
    SO_ENABLE(SoGLRenderAction.class, SoTextureCoordinateBindingElement.class);
    SO_ENABLE(SoPickAction.class, SoTextureCoordinateBindingElement.class);
}

}
