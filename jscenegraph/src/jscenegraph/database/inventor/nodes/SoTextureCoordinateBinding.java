/**************************************************************************\
 * Copyright (c) Kongsberg Oil & Gas Technologies AS
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 \**************************************************************************/

/*!
  \class SoTextureCoordinateBinding SoTextureCoordinateBinding.h Inventor/nodes/SoTextureCoordinateBinding.h
  \brief The SoTextureCoordinateBinding class says how texture coordinates should be bound to shapes.

  \ingroup coin_nodes

  SoTextureCoordinateBinding binds current coordinates to subsequent
  shapes by using either per vertex or per indexed vertex binding.

  <b>FILE FORMAT/DEFAULTS:</b>
  \code
    TextureCoordinateBinding {
        value PER_VERTEX_INDEXED
    }
  \endcode
*/

// *************************************************************************

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

// *************************************************************************

/*!
  \enum SoTextureCoordinateBinding::Binding

  The binding types available for the
  SoTextureCoordinateBinding::value field.
*/
/*!
  \var SoTextureCoordinateBinding::Binding SoTextureCoordinateBinding::PER_VERTEX
  Get a new texture coordinate from the pool of texture coordinates for
  each vertex of the shape.

  Texture Coordinates are fetched from index 0 and onwards, incrementing
  the index into the texture coordinates pool by 1 for each new vertex
  of the shape node.
*/
/*!
  \var SoTextureCoordinateBinding::Binding SoTextureCoordinateBinding::PER_VERTEX_INDEXED
  Get a new texture coordinate from the pool of texture coordinates for
  each vertex of the shape.

  Texture coordinates are fetched by the index value settings of the shape.
*/
/*!
  \var SoTextureCoordinateBinding::Binding SoTextureCoordinateBinding::DEFAULT

  Obsolete value, please don't use.
*/


/*!
  \var SoSFEnum SoTextureCoordinateBinding::value

  Type of texture map binding for subsequent shape nodes in the
  scene graph. Default field value is
  SoTextureCoordinateBinding::PER_VERTEX_INDEXED.
*/


// *************************************************************************


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
