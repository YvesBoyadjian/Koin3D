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
 |      This file defines the SoMaterialBinding node class.
 |
 |   Author(s)          : Paul S. Strauss
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
import jscenegraph.database.inventor.elements.SoMaterialBindingElement;
import jscenegraph.database.inventor.elements.SoOverrideElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFEnum;


////////////////////////////////////////////////////////////////////////////////
//! Node that specifies how multiple materials are bound to shapes.
/*!
\class SoMaterialBinding
\ingroup Nodes
This node specifies how the current materials are bound to shapes that
follow in the scene graph. Each shape node may interpret bindings
differently. The current material always has a base value, which is
defined by the first value of all material fields. Since material
fields may have multiple values, the binding determines how these
values are distributed over a shape.


The bindings for faces and vertices are meaningful only for shapes
that are made from faces and vertices. Similarly, the indexed bindings
are only used by the shapes that allow indexing.


The bindings apply only to diffuse colors and transparency.  Other
materials (emissive, specular, ambient, shininess) will have the first
value applied to the
entire shape, regardless of the material binding or the number provided.


If the number of transparencies is less than the number of diffuse
colors, only the first transparency value will be used, regardless of
the material binding.
If the number of diffuse colors in the state is less than the number required
for the given binding, a debug warning will be printed and unpredictable colors
will result.

\par File Format/Default
\par
\code
MaterialBinding {
  value OVERALL
}
\endcode

\par Action Behavior
\par
SoGLRenderAction, SoCallbackAction
<BR> Sets the current material binding type. 

\par See Also
\par
SoMaterial, SoNormalBinding, SoShape, SoTextureCoordinateBinding
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoMaterialBinding extends SoNode {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoMaterialBinding.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoMaterialBinding.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoMaterialBinding.class); }    	  	
	
    
	   public enum Binding {
		            OVERALL(SoMaterialBindingElement.Binding.OVERALL),
		            PER_PART(SoMaterialBindingElement.Binding.PER_PART),
		            PER_PART_INDEXED(SoMaterialBindingElement.Binding.PER_PART_INDEXED),
		            PER_FACE(SoMaterialBindingElement.Binding.PER_FACE),
		            PER_FACE_INDEXED(SoMaterialBindingElement.Binding.PER_FACE_INDEXED),
		            PER_VERTEX(SoMaterialBindingElement.Binding.PER_VERTEX),
		            PER_VERTEX_INDEXED(SoMaterialBindingElement.Binding.PER_VERTEX_INDEXED);
		    
//		    #ifndef IV_STRICT
//		            DEFAULT           = OVERALL,
//		            NONE              = OVERALL
//		    #endif
		            private final int value;
		            Binding(SoMaterialBindingElement.Binding binding) {
		            	this.value = binding.getValue();
		            }
		            public int getValue() { return value; }
		        };
		   
		        //! Specifies how to bind materials to shapes.
		        public final SoSFEnum            value = new SoSFEnum();
		        

	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    This initializes the SoMaterialBinding class.
	   //
	   // Use: internal
	   
	  public static void
	   initClass()
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       SoSubNode.SO__NODE_INIT_CLASS(SoMaterialBinding.class, "MaterialBinding", SoNode.class);
	   
	       // Enable elements for appropriate actions:
	       //SO_ENABLE(SoGLRenderAction, SoMaterialBindingElement);
	       SoGLRenderAction.enableElement(SoMaterialBindingElement.class);
	       //SO_ENABLE(SoCallbackAction, SoMaterialBindingElement);
	       SoCallbackAction.enableElement(SoMaterialBindingElement.class);
	       //SO_ENABLE(SoPickAction,     SoMaterialBindingElement);
	       SoPickAction.enableElement(SoMaterialBindingElement.class);
	   }
	  

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoMaterialBinding()
//
////////////////////////////////////////////////////////////////////////
{
    nodeHeader.SO_NODE_CONSTRUCTOR(/*SoMaterialBinding*/);

    nodeHeader.SO_NODE_ADD_SFIELD(value,"value",
                         (SoMaterialBindingElement.getDefault().getValue()));

    // Set up static info for enumerated type field
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Binding.OVERALL);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Binding.PER_PART);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Binding.PER_PART_INDEXED);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Binding.PER_FACE);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Binding.PER_FACE_INDEXED);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Binding.PER_VERTEX);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Binding.PER_VERTEX_INDEXED);

    // And obsolete bindings:
    if (nodeHeader.firstInstance()) {
    	nodeHeader.getFieldData().addEnumValue("Binding", "DEFAULT", 0);
    	nodeHeader.getFieldData().addEnumValue("Binding", "NONE", 1);
    }


    // Set up info in enumerated type field
    nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(value,"value", "Binding");

    isBuiltIn = true;
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
    if (b == 0 || b == 1) value.setValue( Binding.OVERALL);

    return result;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Handles most actions.
//
// Use: extender

private void
SoMaterialBinding_doAction(SoAction action)

//
////////////////////////////////////////////////////////////////////////
{
    if (! value.isIgnored() && 
        !SoOverrideElement.getMaterialBindingOverride(action.getState())) {
        if (isOverride()) {
            SoOverrideElement.
                setMaterialBindingOverride(action.getState(), this, true);
        }
        SoMaterialBindingElement.set(action.getState(),
                SoMaterialBindingElement.Binding.fromValue(value.getValue()));
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
    SoMaterialBinding_doAction(action);
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
    SoMaterialBinding_doAction(action);
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
    SoMaterialBinding_doAction(action);
}
	  
}
