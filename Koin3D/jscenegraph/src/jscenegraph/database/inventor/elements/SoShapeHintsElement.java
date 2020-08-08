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
 |      This file defines the SoShapeHintsElement class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.elements;

import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;


///////////////////////////////////////////////////////////////////////////////
///
///  \class SoShapeHintsElement
///  \ingroup Elements
///
///  Element that stores current shape hints.
///  All three hints must be set at the same time; to
///  leave any hint as is, use the "AS_IS" enum value.
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoShapeHintsElement extends SoElement {
	
	    //! Hints about ordering of face vertices:
    public enum VertexOrdering {
        UNKNOWN_ORDERING(0),       //!<      No ordering info is known
        CLOCKWISE(1),              //!<      Vertices are ordered CW around faces
        COUNTERCLOCKWISE(2),       //!<      Vertices are ordered CCW around faces
        ORDERING_AS_IS(3);          //!<      Indicates to set() to leave as is
        
        private int value;
        
        VertexOrdering(int value) {
        	this.value = value;
        }
        
        public int getValue() {
        	return value;
        }
        
        public static VertexOrdering fromValue(int value) {
        	switch(value) {
        	case 0: return UNKNOWN_ORDERING;
        	case 1: return CLOCKWISE;
        	case 2: return COUNTERCLOCKWISE;
        	case 3: return ORDERING_AS_IS;
        	default: return null;
        	}
        }
    };

    //! Hints about entire shape:
    public enum ShapeType {
        UNKNOWN_SHAPE_TYPE(0),     //!<      Nothing is known about shape
        SOLID(1),                  //!<      Shape is known to be solid
        SHAPE_TYPE_AS_IS(2);        //!<      Indicates to set() to leave as is
        
        private int value;
        
        ShapeType(int value) {
        	this.value = value;
        }
        
        public int getValue() {
        	return value;
        }
        
        public static ShapeType fromValue(int value) {
        	switch(value) {
        	case 0: return UNKNOWN_SHAPE_TYPE;
        	case 1: return SOLID;
        	case 2: return SHAPE_TYPE_AS_IS;
        	default: return null;
        	}
        }
    };

    //! Hints about faces of shape:
    public enum FaceType {
        UNKNOWN_FACE_TYPE(0),      //!<      Nothing is known about faces
        CONVEX(1),                 //!<      Faces are all convex
        FACE_TYPE_AS_IS(2);         //!<      Indicates to set() to leave as is
        
        private int value;
        
        FaceType(int value) {
        	this.value = value;
        }
        
        public int getValue() {
        	return value;
        }
        
        public static FaceType fromValue(int value) {
        	switch(value) {
        	case 0: return UNKNOWN_FACE_TYPE;
        	case 1: return CONVEX;
        	case 2: return FACE_TYPE_AS_IS;
        	default: return null;
        	}
        }
    };

	VertexOrdering      vertexOrdering;
    ShapeType           shapeType;
    FaceType            faceType;
    boolean              useVBO;

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Initializes element.
//
// Use: public

public void
init(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    vertexOrdering      = getDefaultVertexOrdering();
    shapeType           = getDefaultShapeType();
    faceType            = getDefaultFaceType();
    useVBO              = getDefaultVBOUsage();
}

	    //! Returns each default hint
    public static VertexOrdering getDefaultVertexOrdering() {return VertexOrdering.UNKNOWN_ORDERING;}
    public static ShapeType      getDefaultShapeType()  { return ShapeType.UNKNOWN_SHAPE_TYPE; }
    public static FaceType       getDefaultFaceType()   { return FaceType.CONVEX; }
    public static boolean           getDefaultVBOUsage()   { return true; }

	

	@Override
	public boolean matches(SoElement elt) {
	    SoShapeHintsElement shElt = ( SoShapeHintsElement ) elt;

	    return (vertexOrdering == shElt.vertexOrdering     &&
	            shapeType      == shElt.shapeType          &&
	            faceType       == shElt.faceType           &&
	            useVBO         == shElt.useVBO);
	}

	@Override
	public SoElement copyMatchInfo() {
	    SoShapeHintsElement result =
	            (SoShapeHintsElement )getTypeId().createInstance();

	        result.vertexOrdering      = vertexOrdering;
	        result.shapeType           = shapeType;
	        result.faceType            = faceType;
	        result.useVBO              = useVBO;
	        
	        return result;
	}
public static boolean isVBOUsed( SoState state )
{
  SoShapeHintsElement elt = (SoShapeHintsElement ) getConstElement(state, classStackIndexMap.get(SoShapeHintsElement.class));
  return elt.useVBO;  
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets shape hints in element accessed from state.
//
// Use: public, static

public static void
set(SoState state, VertexOrdering _vertexOrdering,
                         ShapeType _shapeType, FaceType _faceType)
//
////////////////////////////////////////////////////////////////////////
{
    SoShapeHintsElement elt;

    // Get an instance we can change (pushing if necessary)
    elt = (SoShapeHintsElement ) getElement(state, classStackIndexMap.get(SoShapeHintsElement.class));

    if (elt != null) {
        elt.setElt(_vertexOrdering, _shapeType, _faceType);
    	elt.updateLazyElement(state);
    }
}


//! FIXME: write doc.

public static void set(SoState state,
                         SoNode node,
                         VertexOrdering vertexOrdering,
                         ShapeType shapeType,
                         FaceType faceType)
{
  SoShapeHintsElement elem = (SoShapeHintsElement)
    (
     SoElement.getElement(state, classStackIndexMap.get(SoShapeHintsElement.class)) // java port
     );
  if (elem != null) {
    elem.setElt(vertexOrdering, shapeType, faceType);
    elem.updateLazyElement(state);
  }
}



////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets VBO usage in element accessed from state.
//
// Use: public, static

public static void
set(SoState state, boolean _useVBO)
                         //
                         ////////////////////////////////////////////////////////////////////////
{
  SoShapeHintsElement elt;

  // Get an instance we can change (pushing if necessary)
  elt = (SoShapeHintsElement ) getElement(state, classStackIndexMap.get(SoShapeHintsElement.class));

  if (elt != null)
    elt.useVBO = _useVBO;
}
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Pushes the element, copying values from previous element in
//    state:
//
// Use: public, virtual

public void
push(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    SoShapeHintsElement   prevElt =
        ( SoShapeHintsElement )getNextInStack();

    vertexOrdering      = prevElt.vertexOrdering;
    faceType            = prevElt.faceType;
    shapeType           = prevElt.shapeType;
    useVBO              = prevElt.useVBO;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Virtual set method; the GL version of this element overrides
//    this to send stuff to GL, too...
//
// Use: protected

public void
setElt(VertexOrdering _vertexOrdering,
                            ShapeType _shapeType, FaceType _faceType)
//
////////////////////////////////////////////////////////////////////////
{
    // Set the hints in the elements. If any of the hints is
    // AS_IS, leave the values alone.

    if (_vertexOrdering != VertexOrdering.ORDERING_AS_IS)
        vertexOrdering = _vertexOrdering;
    if (_shapeType != ShapeType.SHAPE_TYPE_AS_IS)
        shapeType = _shapeType;
    if (_faceType != FaceType.FACE_TYPE_AS_IS)
        faceType = _faceType;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns shape hints from state.
//
// Use: public, static

public static void
get(SoState state,
                           final VertexOrdering[] vertexOrdering,
                           final ShapeType[] shapeType, final FaceType[] faceType)
//
////////////////////////////////////////////////////////////////////////
{
    SoShapeHintsElement elt;

    elt = ( SoShapeHintsElement )
        getConstElement(state, classStackIndexMap.get(SoShapeHintsElement.class));

    vertexOrdering[0]      = elt.vertexOrdering;
    shapeType[0]           = elt.shapeType;
    faceType[0]            = elt.faceType;
}

//! FIXME: write doc.

public static VertexOrdering
getVertexOrdering(SoState state)
{
final SoShapeHintsElement elem = (SoShapeHintsElement)
  (
   SoElement.getConstElement(state, classStackIndexMap.get(SoShapeHintsElement.class))
   );
return elem.vertexOrdering;
}



//! FIXME: write doc.

public static FaceType
getFaceType(SoState state)
{
final SoShapeHintsElement elem = (SoShapeHintsElement)
  (
   SoElement.getConstElement(state, classStackIndexMap.get(SoShapeHintsElement.class))
   );
return elem.faceType;
}


public static SoShapeHintsElement.ShapeType
getShapeType(SoState state)
{
  SoShapeHintsElement elem = (SoShapeHintsElement)
    (
     SoElement.getConstElement(state, classStackIndexMap.get(SoShapeHintsElement.class))
     );
  return elem.shapeType;
}

public void
updateLazyElement(SoState state)
{
  if (state.isElementEnabled(SoLazyElement.getClassStackIndex(SoLazyElement.class))) {
    SoLazyElement.setVertexOrdering(state, this.vertexOrdering == VertexOrdering.CLOCKWISE ?
                                     SoLazyElement.VertexOrdering.CW : SoLazyElement.VertexOrdering.CCW);
    SoLazyElement.setTwosideLighting(state, this.vertexOrdering != VertexOrdering.UNKNOWN_ORDERING &&
                                      this.shapeType == ShapeType.UNKNOWN_SHAPE_TYPE);
    SoLazyElement.setBackfaceCulling(state, this.vertexOrdering != VertexOrdering.UNKNOWN_ORDERING &&
                                      this.shapeType == ShapeType.SOLID);
  }
}
}
