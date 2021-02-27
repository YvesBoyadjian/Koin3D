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
 |      This file defines the SoNormalBinding node class.
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
import jscenegraph.database.inventor.elements.SoNormalBindingElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFEnum;

/**
 * @author Yves Boyadjian
 *
 */

////////////////////////////////////////////////////////////////////////////////
//! Node that specifies how multiple surface normals are bound to shapes.
/*!
\class SoNormalBinding
\ingroup Nodes
This node specifies how the current normals are bound to shapes that
follow in the scene graph. Each shape node may interpret bindings
differently.


The bindings for faces and vertices are meaningful only for shapes
that are made from faces and vertices. Similarly, the indexed bindings
are only used by the shapes that allow indexing. For bindings that
require multiple normals, be sure to have at least as many normals
defined as are necessary; otherwise, errors will occur.

\par File Format/Default
\par
\code
NormalBinding {
  value PER_VERTEX_INDEXED
}
\endcode

\par Action Behavior
\par
SoGLRenderAction, SoCallbackAction, SoRayPickAction
<BR> Sets the current normal binding type. 

\par See Also
\par
SoMaterialBinding, SoNormal, SoTextureCoordinateBinding, SoVertexShape
*/
////////////////////////////////////////////////////////////////////////////////

public class SoNormalBinding extends SoNode {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoNormalBinding.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoNormalBinding.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoNormalBinding.class); }    
	  
	    public
    enum Binding {
        //! Whole object has same normal
        OVERALL           ( SoNormalBindingElement.Binding.OVERALL),
        //! One normal for each part of object
        PER_PART          ( SoNormalBindingElement.Binding.PER_PART),
        //! One normal for each part of object, indexed
        PER_PART_INDEXED  ( SoNormalBindingElement.Binding.PER_PART_INDEXED),
        //! One normal for each face of object
        PER_FACE          ( SoNormalBindingElement.Binding.PER_FACE),
        //! One normal for each face, indexed
        PER_FACE_INDEXED  ( SoNormalBindingElement.Binding.PER_FACE_INDEXED),
        //! One normal for each vertex of object
        PER_VERTEX        ( SoNormalBindingElement.Binding.PER_VERTEX),
        //! One normal for each vertex, indexed
        PER_VERTEX_INDEXED( SoNormalBindingElement.Binding.PER_VERTEX_INDEXED);

    	private int value;
    	Binding(SoNormalBindingElement.Binding value) {
    		this.value = value.getValue();
    	}
    	public int getValue() {
    		return value;
    	}
	    };
	    
	    //! Specifies how to bind normals to shapes.
	    public final SoSFEnum            value = new SoSFEnum();          


	    
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoNormalBinding()
//
////////////////////////////////////////////////////////////////////////
{
    nodeHeader.SO_NODE_CONSTRUCTOR(/*SoNormalBinding.class*/);
    nodeHeader.SO_NODE_ADD_FIELD(value,"value", (SoNormalBindingElement.getDefault().getValue()));

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
//    Handles most actions.
//
// Use: extender

public void
SoNormalBinding_doAction(SoAction action)

//
////////////////////////////////////////////////////////////////////////
{
    if (! value.isIgnored()) {
        SoNormalBindingElement.set(action.getState(),
                SoNormalBindingElement.Binding.fromValue(value.getValue()));
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
    SoNormalBinding_doAction(action);
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
    SoNormalBinding_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does pick action...
//
// Use: extender

public void pick(SoPickAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoNormalBinding_doAction(action);
}
	    
	    
////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoNormalBinding class.
//
// Use: internal

public static final void initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SO__NODE_INIT_CLASS(SoNormalBinding.class, "NormalBinding", SoNode.class);

    // Enable elements for appropriate actions:
    SO_ENABLE(SoGLRenderAction.class, SoNormalBindingElement.class);
    SO_ENABLE(SoCallbackAction.class, SoNormalBindingElement.class);
    SO_ENABLE(SoPickAction.class, SoNormalBindingElement.class);
}


}
