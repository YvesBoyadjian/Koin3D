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
 * Copyright (C) 1995-96   Silicon Graphics, Inc.
 *
 _______________________________________________________________________
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 |
 |   $Revision: 1.1.1.1 $
 |
 |   Description:
 |      This file defines the SoVertexProperty node class, and the
 |       SoVertexPropertyCache class.
 |
 |   Author(s)          : Alan Norton, Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoGetBoundingBoxAction;
import jscenegraph.database.inventor.actions.SoPickAction;
import jscenegraph.database.inventor.elements.SoCoordinateElement;
import jscenegraph.database.inventor.elements.SoGLCoordinateElement;
import jscenegraph.database.inventor.elements.SoGLLazyElement;
import jscenegraph.database.inventor.elements.SoGLNormalElement;
import jscenegraph.database.inventor.elements.SoGLTextureCoordinateElement;
import jscenegraph.database.inventor.elements.SoLazyElement;
import jscenegraph.database.inventor.elements.SoMaterialBindingElement;
import jscenegraph.database.inventor.elements.SoNormalBindingElement;
import jscenegraph.database.inventor.elements.SoNormalElement;
import jscenegraph.database.inventor.elements.SoOverrideElement;
import jscenegraph.database.inventor.elements.SoTextureCoordinateElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFUInt32;
import jscenegraph.database.inventor.fields.SoMFVec2f;
import jscenegraph.database.inventor.fields.SoMFVec3f;
import jscenegraph.database.inventor.fields.SoSFEnum;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.mevis.inventor.elements.SoGLVBOElement;
import jscenegraph.mevis.inventor.misc.SoVBO;
import jscenegraph.port.VoidPtr;


////////////////////////////////////////////////////////////////////////////////
//! Vertex property node.
/*!
\class SoVertexProperty
\ingroup Nodes
This property node may be used to efficiently specify coordinates, normals, 
texture coordinates, colors, transparency values, material 
binding and normal binding for vertex-based shapes, i.e., shapes of 
class SoVertexShape.  An SoVertexProperty node can be used 
as a child of a group node in a scene graph, in which case the
properties it specifies are inherited by subsequent shape nodes in the
graph.  It can also be directly referenced as the VertexProperty
SoSFField of a vertex-based shape, bypassing scene graph inheritance.  


When directly referenced by a \b VertexProperty  SoSFField of a vertex-based
shape, the SoVertexProperty node is the most efficient way of
specifying vertex-based shapes.  Use of the directly referenced 
SoVertexProperty node results in significantly faster scene
rendering than scene graph inheritance of vertex properties, provided all
required vertex properties are specified in the SoVertexProperty
node.


Because the class SoVertexProperty is derived from SoNode, a
vertex property node can be inserted as a child node in a scene graph.
When inserted as a node in a scene graph, the SoVertexProperty node is 
traversed as any other property node and the properties it specifies are
inherited by subsequent shape nodes in the scene graph.  It specifies
the current material and normal bindings, and can be used to specify
the current 3D 
coordinates, the current normals, the current texture
coordinates, the current diffuse colors, and the current transparencies.


All multiple-valued fields in the SoVertexProperty node are optional.  
If a field is not present (i.e. if it has 0 values), then shapes that require
the missing information are required to obtain it from the current traversal
state.   However, users are
cautioned that, for optimal performance, the vertex property node should
be referenced as the \b VertexProperty  field of an SoVertexShape,
and should specify in its fields all values required to render that shape.


The various fields in a vertex property node can be used in place of 
corresponding fields in other property nodes, as follows:  The \b vertex  field
contains 3D coordinates, as in the \b point  field of an
SoCoordinate3
node.  The \b normal  field contains normal vectors, as in the
\b vector 
field of the \b SoNormal  node.  The \b orderedRGBA  field contains
packed colors in the hexadecimal format 0xrrggbbaa, where 
	rr is the red value (between 00 and 0xFF hex)
	gg is the green value (between 00 and 0xFF hex)
	bb is the blue value (between 00 and 0xFF hex)
	aa is the alpha value (between 00 = transparent 
		and 0xFF = opaque).
The packed colors are equivalent to an SoPackedColor node, and
provide values for both diffuse color and transparency.
The \b texCoord  field replaces 
the \b point  field of the SoTextureCoordinate2 node. 


If the
transparency type is SoGLRenderAction::SCREEN_DOOR, only the
first transparency value will be used.  With other transparency types,
multiple transparencies will be used.

  
The \b materialBinding  field replaces the \b value  field of the
SoMaterialBinding node.  The \b materialBinding  field in a directly
referenced SoVertexProperty node has no effect unless there is a nonempty
\b orderedRGBA  field, in which case the material binding specifies the
assignment of diffuse colors and alpha values to the shape.
The \b materialBinding  field can take as value any of the material
bindings supported by Inventor.    
 


The \b normalBinding  field replaces the \b value  field of the
SoNormalBinding node.   The \b normalBinding  field of
a directly referenced SoVertexProperty node has no effect 
unless there is a nonempty
\b normal  field, in which case the normal binding specifies the assignment
of normal vectors to the shape.  The value of the \b normalBinding 
field can be any of the normal bindings supported by Inventor.

\par File Format/Default
\par
\code
VertexProperty {
  vertex [  ]
  normal [  ]
  texCoord [  ]
  orderedRGBA [  ]
  materialBinding OVERALL
  normalBinding PER_VERTEX_INDEXED
}
\endcode

\par Action Behavior
\par
SoGetBoundingBoxAction
<BR> When traversed in a scene graph, sets coordinates in current traversal state.  If not traversed, has no effect on current traversal state associated with action. 

\par See Also
\par
SoIndexedTriangleSet
SoIndexedTriangleStripSet
SoIndexedFaceSet
SoIndexedLineSet
SoTriangleStripSet
SoLineSet
SoFaceSet
SoPointSet
SoQuadMesh
SoVertexShape
SoIndexedShape
SoNonindexedShape
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoVertexProperty extends SoNode {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVertexProperty.class,this);	   
	
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVertexProperty.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVertexProperty.class); }    
	  
	
	public final SoMFVec3f vertex = new SoMFVec3f();		//!< Coordinate point(s)
    public final SoMFVec2f texCoord = new SoMFVec2f();       //!< Texture coordinate(s)
    public final SoMFVec3f texCoord3 = new SoMFVec3f(); // COIN 3D
    public final SoMFVec3f normal = new SoMFVec3f();        //!< Normal vector(s)
    public final SoSFEnum normalBinding = new SoSFEnum();  //!< Ignored unless normal field set
    public final SoMFUInt32 orderedRGBA = new SoMFUInt32();    //!< Diffuse+transparency
    public final SoSFEnum materialBinding = new SoSFEnum();//!< Ignored unless orderedRGBA field set
	
    protected final SoVBO[] _vertexVBO = new SoVBO[1];
    protected final SoVBO[] _colorVBO = new SoVBO[1];
    protected final SoVBO[] _normalVBO = new SoVBO[1];
    protected final SoVBO[] _texCoordVBO = new SoVBO[1];
    
    
    
    public enum Binding {
          OVERALL(SoMaterialBindingElement.Binding.OVERALL),
          PER_PART(SoMaterialBindingElement.Binding.PER_PART),
          PER_PART_INDEXED(SoMaterialBindingElement.Binding.PER_PART_INDEXED),
          PER_FACE(SoMaterialBindingElement.Binding.PER_FACE),
          PER_FACE_INDEXED(SoMaterialBindingElement.Binding.PER_FACE_INDEXED),
          PER_VERTEX(SoMaterialBindingElement.Binding.PER_VERTEX),
          PER_VERTEX_INDEXED(SoMaterialBindingElement.Binding.PER_VERTEX_INDEXED);
          
          private final int id;
          Binding(SoMaterialBindingElement.Binding binding) {
        	  this.id = binding.getValue();
          }
          public int getValue() { return id; }
      };
  
 	

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoVertexProperty()
//
////////////////////////////////////////////////////////////////////////
{

	nodeHeader.SO_NODE_CONSTRUCTOR(/*SoVertexProperty*/);

    // Initialize with dummy values using standard macro, then delete
    // all values:
	nodeHeader.SO_NODE_ADD_MFIELD(vertex,"vertex", (new SbVec3f(0,0,0)));
    vertex.deleteValues(0);
    vertex.setDefault(true);
    nodeHeader.SO_NODE_ADD_MFIELD(normal,"normal", (new SbVec3f(0,0,0)));
    normal.deleteValues(0);
    normal.setDefault(true);
    nodeHeader.SO_NODE_ADD_MFIELD(texCoord,"texCoord", (new SbVec2f(0,0)));
    texCoord.deleteValues(0);
    texCoord.setDefault(true);
    nodeHeader.SO_NODE_ADD_MFIELD(orderedRGBA,"orderedRGBA", (0));    
    orderedRGBA.deleteValues(0);
    orderedRGBA.setDefault(true);

    // FIXME: this field was added in TGS Inventor 2.6 and Coin
    // 2.0. This should have repercussions for file format
    // compatibility. 20030227 mortene.
    nodeHeader.SO_NODE_ADD_MFIELD(texCoord3,"texCoord3", new SbVec3f(0,0,0)); // COIN 3D
    // Make multivalue fields empty.
    this.texCoord3.setNum(0); // COIN 3D
    // So they are not written in their default state on SoWriteAction
    // traversal.
    this.texCoord3.setDefault(true);

    
    // Initialize these with default values.  They'll be ignored if
    // the corresponding fields have no values:
    nodeHeader.SO_NODE_ADD_SFIELD(materialBinding,"materialBinding", (SoVertexProperty.Binding.OVERALL.getValue()));
    nodeHeader.SO_NODE_ADD_SFIELD(normalBinding,"normalBinding", (SoVertexProperty.Binding.PER_VERTEX_INDEXED.getValue()));

    // Set up static info for enum type fields:

    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Binding.OVERALL);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Binding.PER_VERTEX);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Binding.PER_VERTEX_INDEXED);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Binding.PER_FACE);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Binding.PER_FACE_INDEXED);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Binding.PER_PART);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Binding.PER_PART_INDEXED);

    nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(materialBinding,"materialBinding", "Binding");
    nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(normalBinding,"normalBinding", "Binding");

    isBuiltIn = true;

    _vertexVBO[0] = null;
    _colorVBO[0] = null;
    _normalVBO[0] = null;
    _texCoordVBO[0] = null;
}
	
	
////////////////////////////////////////////////////////////////////////
//
// Description:
//    initialize class  
//
public static void
initClass()
{
	SoSubNode.SO__NODE_INIT_CLASS(SoVertexProperty.class, "VertexProperty", SoNode.class);

    // enable elements for vertex:
    SO_ENABLE(SoCallbackAction.class,         SoCoordinateElement.class);
    SO_ENABLE(SoGLRenderAction.class,         SoGLCoordinateElement.class);
    SO_ENABLE(SoGLRenderAction.class,         SoGLVBOElement.class);
    SO_ENABLE(SoGetBoundingBoxAction.class,   SoCoordinateElement.class);
    SO_ENABLE(SoPickAction.class,             SoCoordinateElement.class);

    // Enable elements for normal:
    SO_ENABLE(SoGLRenderAction.class, SoGLNormalElement.class);
    SO_ENABLE(SoCallbackAction.class, SoNormalElement.class);
    SO_ENABLE(SoPickAction.class,     SoNormalElement.class);

    // Enable elements for normal binding:
    SO_ENABLE(SoGLRenderAction.class, SoNormalBindingElement.class);
    SO_ENABLE(SoCallbackAction.class, SoNormalBindingElement.class);
    SO_ENABLE(SoPickAction.class, SoNormalBindingElement.class);

    // Enable elements for material:
    SO_ENABLE(SoCallbackAction.class, SoLazyElement.class);
    SO_ENABLE(SoPickAction.class, SoLazyElement.class);
    SO_ENABLE(SoGLRenderAction.class, SoGLLazyElement.class);

    // Enable elements for MaterialBinding:    
    SO_ENABLE(SoGLRenderAction.class, SoMaterialBindingElement.class);
    SO_ENABLE(SoCallbackAction.class, SoMaterialBindingElement.class);
    SO_ENABLE(SoPickAction.class,     SoMaterialBindingElement.class);

    // Enable elements for textureCoordinates:
    SO_ENABLE(SoCallbackAction.class, SoTextureCoordinateElement.class);
    SO_ENABLE(SoGLRenderAction.class, SoGLTextureCoordinateElement.class);
    SO_ENABLE(SoPickAction.class,     SoTextureCoordinateElement.class);
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
  _vertexVBO[0].destructor();
  _colorVBO[0].destructor();
  _normalVBO[0].destructor();
  _texCoordVBO[0].destructor();
}


	
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Handles any action derived from SoAction.
//
// Use: extender

public void
doAction(SoAction action) {
	SoVertexProperty_doAction(action);
}

public void
SoVertexProperty_doAction(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoState             state = action.getState();
    //  Note, we are not supporting isIgnored flags:
    if (vertex.getNum() > 0)
        SoCoordinateElement.set3(state, this,
                                  vertex.getNum(), vertex.getValuesSbVec3fArray(/*0*/));

    if (normal.getNum() > 0){
        SoNormalElement.set(state, this,
                             normal.getNum(), normal.getValuesSbVec3fArray(/*0*/));
    }
    SoNormalBindingElement.set(state, 
        SoNormalBindingElement.Binding.fromValue(normalBinding.getValue()));

    // set diffuse color
    if (orderedRGBA.getNum() > 0
        && ! SoOverrideElement.getDiffuseColorOverride(state)) {
        if (isOverride()) {
            SoOverrideElement.setDiffuseColorOverride(state, this, true);
        }                       
        SoLazyElement.setPacked(state, this,
                orderedRGBA.getNum(), orderedRGBA.getValuesI(0));
    }
    // set material binding 
    SoMaterialBindingElement.set(state, 
        SoMaterialBindingElement.Binding.fromValue(materialBinding.getValue()));

    if ( texCoord3.getNum() > 0) { // COIN 3D
        SoTextureCoordinateElement.set3(state, this,
                         texCoord3.getNum(), texCoord3.getValues(0));
    }
    else if ( texCoord.getNum() > 0) {
        SoTextureCoordinateElement.set2(state, this,
                         texCoord.getNum(), texCoord.getValuesSbVec2fArray(/*0*/));
    }
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
  if ( texCoord.getNum() > 0) {
    SoGLTextureCoordinateElement.setTexGen(state, this, null);
  }

  SoVertexProperty_doAction(action);

  putVBOsIntoState(state);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Handles get bounding box action.
//
// Use: extender

public void
getBoundingBox(SoGetBoundingBoxAction action)
//
////////////////////////////////////////////////////////////////////////
{
    // Note that Bounding box only needs to look at coordinates:
    if (vertex.getNum() > 0)
        SoCoordinateElement.set3(action.getState(), this,
                                  vertex.getNum(), vertex.getValuesSbVec3fArray(/*0*/));
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
    SoVertexProperty_doAction(action);
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
    SoVertexProperty_doAction(action);
}

public void putVBOsIntoState( SoState state )
{
  if (state.isElementEnabled(SoGLVBOElement.getClassStackIndex(SoGLVBOElement.class))) {
    if (vertex.getNum() > 0) {
      SoGLVBOElement.updateVBO(state, SoGLVBOElement.VBOType.VERTEX_VBO, _vertexVBO,
        vertex.getNum() * SbVec3f.sizeof(), VoidPtr.create(vertex.getValuesArray(0)), getNodeId());
    }
    if (normal.getNum() > 0) {
      SoGLVBOElement.updateVBO(state, SoGLVBOElement.VBOType.NORMAL_VBO, _normalVBO,
        normal.getNum() * SbVec3f.sizeof(), VoidPtr.create(normal.getValuesArray(0)), getNodeId());
    }
    if (orderedRGBA.getNum() > 0
      && ! SoOverrideElement.getDiffuseColorOverride(state)) {
        // update the VBO, no data is passed since that is done by SoColorPacker later on
        SoGLVBOElement.updateVBO(state, SoGLVBOElement.VBOType.COLOR_VBO, _colorVBO);
    }
    if ( texCoord.getNum() > 0) {
      SoGLVBOElement.updateVBO(state, SoGLVBOElement.VBOType.TEXCOORD_VBO, _texCoordVBO,
        texCoord.getNum() * SbVec2f.sizeof(), VoidPtr.create(texCoord.getValuesBytes(0)), getNodeId());
    }
  }
}
	
}
