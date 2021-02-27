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
 |      This file defines the SoShapeHints node class.
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
import jscenegraph.database.inventor.elements.SoCreaseAngleElement;
import jscenegraph.database.inventor.elements.SoGLShapeHintsElement;
import jscenegraph.database.inventor.elements.SoOverrideElement;
import jscenegraph.database.inventor.elements.SoShapeHintsElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFBool;
import jscenegraph.database.inventor.fields.SoSFEnum;
import jscenegraph.database.inventor.fields.SoSFFloat;
import jscenegraph.database.inventor.misc.SoState;


////////////////////////////////////////////////////////////////////////////////
//! Node that provides hints about shapes.
/*!
\class SoShapeHints
\ingroup Nodes
By default, Inventor assumes very little about the shapes it renders.
You can use the SoShapeHints node to indicate that vertex-based
shapes (those derived from SoVertexShape) are solid, contain
ordered vertices, or contain convex faces.  For fastest rendering,
specify SOLID, COUNTERCLOCKWISE, CONVEX shapes.


These hints allow Inventor to optimize certain rendering features.
Optimizations that may be performed include enabling back-face culling
and disabling two-sided lighting. For example, if an object is solid
and has ordered vertices, Inventor turns on backface culling and turns
off two-sided lighting. If the object is not solid but has ordered
vertices, it turns off backface culling and turns on two-sided
lighting. In all other cases, both backface culling and two-sided
lighting are off.


The SoShapeHints node also affects how default normals are
generated.  When a node derived from SoVertexShape has to generate
default normals, it uses the \b creaseAngle  field to determine which
edges should be smooth-shaded and which ones should have a sharp
crease.  The crease angle is the angle between surface normals on
adjacent polygons. For example, a crease angle of .5 radians
means that an edge between two adjacent polygonal faces
will be smooth shaded if the normals to the two faces form an angle
that is less than .5 radians (about 30 degrees). Otherwise, it will
be faceted.  Normal generation is fastest when the creaseAngle is 0
(the default), producing one normal per facet.  A creaseAngle of pi
produces one averaged normal per vertex.

\par File Format/Default
\par
\code
ShapeHints {
  vertexOrdering UNKNOWN_ORDERING
  shapeType UNKNOWN_SHAPE_TYPE
  faceType CONVEX
  creaseAngle 0
  useVBO TRUE
}
\endcode

\par Action Behavior
\par
SoGLRenderAction, SoCallbackAction, SoRayPickAction, SoGetBoundingBoxAction
<BR> Sets the state to contain the hints; sets up optimizations based on the hints. 

\par See Also
\par
SoVertexShape
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoShapeHints extends SoNode {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoShapeHints.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoShapeHints.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoShapeHints.class); }    	  	
	
  public
    //! Hints about ordering of face vertices: if ordering of all
    //! vertices of all faces is known to be consistent when viewed
    //! from "outside" shape or not.
    enum VertexOrdering {
        UNKNOWN_ORDERING ( SoShapeHintsElement.VertexOrdering.UNKNOWN_ORDERING.getValue()),
        CLOCKWISE        ( SoShapeHintsElement.VertexOrdering.CLOCKWISE.getValue()),
        COUNTERCLOCKWISE ( SoShapeHintsElement.VertexOrdering.COUNTERCLOCKWISE.getValue());
        
        private int value;
        
        VertexOrdering(int value) {
        	this.value = value;
        }
        public int getValue() {
        	return value;
        }
    };

    //! Hints about entire shape: if shape is known to be a solid
    //! object, as opposed to a surface.
    public enum ShapeType {
        UNKNOWN_SHAPE_TYPE ( SoShapeHintsElement.ShapeType.UNKNOWN_SHAPE_TYPE.getValue()),
        SOLID              ( SoShapeHintsElement.ShapeType.SOLID.getValue());
        
        private int value;
        
        ShapeType(int value) {
        	this.value = value;
        }
        public int getValue() {
        	return value;
        }
    };

    //! Hints about faces of shape: if all faces are known to be convex
    //! or not.
    public enum FaceType {
        UNKNOWN_FACE_TYPE(SoShapeHintsElement.FaceType.UNKNOWN_FACE_TYPE.getValue()),
        CONVEX            ( SoShapeHintsElement.FaceType.CONVEX.getValue());
        
        private int value;
        
        FaceType(int value) {
        	this.value = value;
        }
        public int getValue() {
        	return value;
        }
    };

    //! \name Fields
    //@{

    //! Indicates how the vertices of faces are ordered. <tt>CLOCKWISE</tt>
    //! ordering means that the vertices of each face form a clockwise loop
    //! around the face, when viewed from the outside (the side toward which
    //! the normal points).
    public final SoSFEnum            vertexOrdering = new SoSFEnum(); 

    //! Indicates whether the shape is known to enclose a volume (<tt>SOLID</tt>)
    //! or not. If the inside (the side away from the surface normal) of any
    //! part of the shape is visible, the shape is not solid.
    public final SoSFEnum            shapeType = new SoSFEnum();      

    //! Indicates whether each face is convex. Because the penalty for
    //! non-convex faces is very steep (faces must be triangulated
    //! expensively), the default assumes all faces are convex. Therefore,
    //! shapes with concave faces may not be displayed correctly unless this
    //! hint is set to <tt>UNKNOWN_FACE_TYPE</tt>.
    public final SoSFEnum            faceType = new SoSFEnum();       

    //! Indicates the minimum angle (in radians) between two adjacent face
    //! normals required to form a sharp crease at the edge when default
    //! normals are computed and used.
    public final SoSFFloat           creaseAngle = new SoSFFloat();    
    //! Allows shapes to make use of vertex buffer objects if available
    public final SoSFBool            useVBO = new SoSFBool();         


////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoShapeHints class.
//
// Use: internal

public static void initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SoSubNode.SO__NODE_INIT_CLASS(SoShapeHints.class, "ShapeHints", SoNode.class);

    SO_ENABLE(SoGLRenderAction.class, SoGLShapeHintsElement.class);
    SO_ENABLE(SoGLRenderAction.class, SoCreaseAngleElement.class);
    SO_ENABLE(SoCallbackAction.class, SoShapeHintsElement.class);
    SO_ENABLE(SoCallbackAction.class, SoCreaseAngleElement.class);
    SO_ENABLE(SoPickAction.class, SoShapeHintsElement.class);
    SO_ENABLE(SoPickAction.class, SoCreaseAngleElement.class);
    SO_ENABLE(SoGetBoundingBoxAction.class, SoShapeHintsElement.class);
    SO_ENABLE(SoGetBoundingBoxAction.class, SoCreaseAngleElement.class);
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoShapeHints()
//
////////////////////////////////////////////////////////////////////////
{
    nodeHeader.SO_NODE_CONSTRUCTOR(/*SoShapeHints*/);

    nodeHeader.SO_NODE_ADD_SFIELD(vertexOrdering,"vertexOrdering",
                      (SoShapeHintsElement.getDefaultVertexOrdering().getValue()));
    nodeHeader.SO_NODE_ADD_SFIELD(shapeType,"shapeType",
                      (SoShapeHintsElement.getDefaultShapeType().getValue()));
    nodeHeader.SO_NODE_ADD_SFIELD(faceType,"faceType",
                      (SoShapeHintsElement.getDefaultFaceType().getValue()));
    nodeHeader.SO_NODE_ADD_SFIELD(creaseAngle,"creaseAngle",
                      (SoCreaseAngleElement.getDefault()));

    nodeHeader.SO_NODE_ADD_SFIELD(useVBO,"useVBO", (SoShapeHintsElement.getDefaultVBOUsage()));

    //
    // Set up static info for enumerated type fields
    //

    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(VertexOrdering.   UNKNOWN_ORDERING);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(VertexOrdering.   CLOCKWISE);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(VertexOrdering.   COUNTERCLOCKWISE);

    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(ShapeType.        UNKNOWN_SHAPE_TYPE);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(ShapeType.        SOLID);

    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(FaceType.         UNKNOWN_FACE_TYPE);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(FaceType.         CONVEX);

    nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(vertexOrdering,"vertexOrdering",    "VertexOrdering");
    nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(shapeType,"shapeType",         "ShapeType");
    nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(faceType,"faceType",          "FaceType");

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
//    Updates state for actions
//
// Use: extender

private void
SoShapeHints_doAction(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoState state = action.getState();

    if (! creaseAngle.isIgnored()
        && ! SoOverrideElement.getCreaseAngleOverride(state)) {
        if (isOverride()) {
            SoOverrideElement.setCreaseAngleOverride(state, this, true);
        }
        SoCreaseAngleElement.set(state, creaseAngle.getValue());
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Updates state for actions that also set shape hints element.
//
// Use: private

private void SoShapeHints_doAction2(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoShapeHints_doAction(action);
    SoState state = action.getState();

    if (SoOverrideElement.getShapeHintsOverride(state)) {
        return;
    }

    if (isOverride()) {
        SoOverrideElement.setShapeHintsOverride(state, this, true);
    }

    //
    // Determine which hints to set. If any are ignored, leave them AS_IS
    //

    SoShapeHintsElement.VertexOrdering order;
    SoShapeHintsElement.ShapeType              shape;
    SoShapeHintsElement.FaceType               face;

    order = (vertexOrdering.isIgnored() ?
             SoShapeHintsElement.VertexOrdering.ORDERING_AS_IS :
             SoShapeHintsElement.VertexOrdering.fromValue(vertexOrdering.getValue()));

    shape  = (shapeType.isIgnored() ?
             SoShapeHintsElement.ShapeType.SHAPE_TYPE_AS_IS :
             SoShapeHintsElement.ShapeType.fromValue(shapeType.getValue()));

    face  = (faceType.isIgnored() ?
             SoShapeHintsElement.FaceType.FACE_TYPE_AS_IS :
             SoShapeHintsElement.FaceType.fromValue( faceType.getValue()));

    SoShapeHintsElement.set(state, order, shape, face);
    SoShapeHintsElement.set(state, useVBO.getValue());
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Handles callback action
//
// Use: extender

public void callback(SoCallbackAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoShapeHints_doAction2(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Handles GL render action
//
// Use: extender

public void GLRender(SoGLRenderAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoShapeHints_doAction2(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Handles pick action
//
// Use: extender

public void pick(SoPickAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoShapeHints_doAction2(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Handles get bounding box action
//
// Use: extender

public void getBoundingBox(SoGetBoundingBoxAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoShapeHints_doAction(action);
}

}
