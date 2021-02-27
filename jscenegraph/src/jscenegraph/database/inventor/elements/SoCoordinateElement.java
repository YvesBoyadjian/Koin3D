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
 |      This file defines the SoCoordinateElement class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.elements;

import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec3fSingle;
import jscenegraph.database.inventor.SbVec4f;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.mevis.inventor.elements.SoGLVBOElement;
import jscenegraph.port.FloatArray;
import jscenegraph.port.SbVec3fArray;
import jscenegraph.port.SbVec4fArray;


///////////////////////////////////////////////////////////////////////////////
///
///  \class SoCoordinateElement
///  \ingroup Elements
///
///  Element that stores the current coordinates. Coordinates may be
///  specified as 3-D or 4-D vectors. This element remembers the last
///  type of value stored.
///
///  This class allows read-only access to the top element in the state
///  to make accessing several values in it more efficient. Individual
///  values must be accessed through this instance.
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoCoordinateElement extends SoReplacedElement {

	   protected
		        int             numCoords;
	   protected        SbVec3fArray       coords3;
	   protected        SbVec4fArray       coords4;
	   protected        boolean              coordsAre3D;
		    	
	   private
		    //! This stores a pointer to the default coordinates so that we can
		    //! set "coords3" to point to them if no other values have been set.
		    static SbVec3fSingle      defaultCoord3;

	   
	   
	private final   	SbVec3f             convert3 = new SbVec3f();       //!< To convert from 4-D to 3-D
	private final	SbVec4f             convert4 = new SbVec4f();       //!< To convert from 3-D to 4-D
	   
	
    //! Returns the default 3-D or 4-D coordinate
    public static SbVec3f getDefault3()                { return new SbVec3f(0, 0, 0); }
    public static SbVec4f getDefault4()                { return new SbVec4f(0, 0, 0, 1); }


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
    super.init(state);

    // Initialize default coordinate storage if not already done
    if (defaultCoord3 == null) {
        defaultCoord3  = new SbVec3fSingle();
        defaultCoord3.copyFrom(getDefault3());
    }

    // Assume 3D until told otherwise
    coordsAre3D = true;
    coords3     = new SbVec3fArray(/*)[1]; coords3[0] = */defaultCoord3);
    numCoords   = 1;
}

	
	
    //! Returns the top (current) instance of the element in the state
  public static SoCoordinateElement getInstance(SoState state)
           {return ( SoCoordinateElement )
               getConstElement(state, classStackIndexMap.get(SoCoordinateElement.class));}
   
  //! Returns the number of coordinate points in an instance
  public int             getNum() { return numCoords; }
 
  //! Returns TRUE if the coordinates were specified as 3-vectors,
     //! FALSE if 4-vectors
  public boolean              is3D() { return coordsAre3D; }
 
  //! Returns the indexed coordinate from an element as a 3- or
     //! 4-vector, converting if necessary. A returned reference may be
     //! invalid after the next call to either of these methods.
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns the indexed coordinate from an element as an SbVec3f.
//
// Use: public

public SbVec3f 
get3(int index)
//
////////////////////////////////////////////////////////////////////////
{
//#ifdef DEBUG
    if (index < 0 || index >= numCoords)
        SoDebugError.post("SoCoordinateElement::get3",
                           "Index ("+index+") is out of range 0 - "+(numCoords - 1));
//#endif /* DEBUG */

    if (coordsAre3D)
        return coords3.get(index);

    // Convert from 4-D if necessary
    else {
        // Cast the const away...
        SoCoordinateElement     elt = (SoCoordinateElement ) this;
        SbVec4f           c4  = coords4.get(index);

        // If we can't do the projection, or we don't have to
        if (c4.operator_square_bracket(3) == 0.0 || c4.operator_square_bracket(3) == 1.0) {
            elt.convert3.operator_square_bracket(0, c4.operator_square_bracket(0));
            elt.convert3.operator_square_bracket(1, c4.operator_square_bracket(1));
            elt.convert3.operator_square_bracket(2, c4.operator_square_bracket(2));
        }

        else {
            elt.convert3.operator_square_bracket(0, c4.operator_square_bracket(0) / c4.operator_square_bracket(3));
            elt.convert3.operator_square_bracket(1, c4.operator_square_bracket(1) / c4.operator_square_bracket(3));
            elt.convert3.operator_square_bracket(2, c4.operator_square_bracket(2) / c4.operator_square_bracket(3));
        }

        return convert3;
    }
}

/**
 * Java port
 * 
 * @return
 */
public float[] get3Ptr() {
    if (coordsAre3D) {
    	int length = numCoords;
    	float[] vertexArray = new float[length*3];
    	int index = 0;
    	for(int i=0;i<length;i++) {
    		vertexArray[index] = coords3.getFast(i).getX();
    		index++;
    		vertexArray[index] = coords3.getFast(i).getY();
    		index++;
    		vertexArray[index] = coords3.getFast(i).getZ();
    		index++;
    	}
        return vertexArray;
    }
    else {
    	throw new IllegalStateException();
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns the indexed coordinate from an element as an SbVec4f.
//
// Use: public

public SbVec4f 
get4(int index)
//
////////////////////////////////////////////////////////////////////////
{
//#ifdef DEBUG
//    if (index < 0 || index >= numCoords)
//        SoDebugError::post("SoCoordinateElement::get4",
//                           "Index (%d) is out of range 0 - %d",
//                           index, numCoords - 1);
//#endif /* DEBUG */

    // Convert from 3-D if necessary
    if (coordsAre3D) {
        // Cast the const away...
        SoCoordinateElement     elt = (SoCoordinateElement ) this;
        SbVec3f           c3  = coords3.get(index);

        elt.convert4.operator_square_bracket(0, c3.operator_square_bracket(0));
        elt.convert4.operator_square_bracket(1, c3.operator_square_bracket(1));
        elt.convert4.operator_square_bracket(2, c3.operator_square_bracket(2));
        elt.convert4.operator_square_bracket(3, 1.0f);

        return convert4;
    }

    else
        return coords4.get(index);
}

/**
 * Java port
 * 
 * @return
 */
public float[] get4Ptr() {
    if (coordsAre3D) {
    	throw new IllegalStateException();
    }
    else {
    	int length = numCoords;
    	float[] vertexArray = new float[length*4];
    	int index = 0;
    	for(int i=0;i<length;i++) {
    		vertexArray[index] = coords4.get(i).getValueRead()[0];
    		index++;
    		vertexArray[index] = coords4.get(i).getValueRead()[1];
    		index++;
    		vertexArray[index] = coords4.get(i).getValueRead()[2];
    		index++;
    		vertexArray[index] = coords4.get(i).getValueRead()[3];
    		index++;
    	}
        return vertexArray;
    }
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets the 3-D coordinates in element accessed from state.
//
// Use: public

public static void
set3(SoState state, SoNode node,
                          int numCoords, final SbVec3fArray coords)
//
////////////////////////////////////////////////////////////////////////
{
    // if someone sets this directly, remove any color VBO
    //SoGLVBOElement.unsetVBOIfEnabled(state, SoGLVBOElement.VBOType.VERTEX_VBO);

    if (state.isElementEnabled(SoGLVBOElement.getClassStackIndex(SoGLVBOElement.class))) {
        SoGLVBOElement.setVertexVBO(state, null); // COIN 3D
      }
    
    SoCoordinateElement elt;

    // Get an instance we can change (pushing if necessary)
    elt = (SoCoordinateElement ) getElement(state, classStackIndexMap.get(SoCoordinateElement.class), node);

    if (elt != null) {
        elt.numCoords   = numCoords;
        elt.coords3     = SbVec3fArray.copyOf(coords);
        elt.coordsAre3D = true;
    }
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets the 4-D coordinates in element accessed from state.
//
// Use: public

public static void
set4(SoState state, SoNode node,
                          int numCoords, final SbVec4fArray coords)
//
////////////////////////////////////////////////////////////////////////
{
    // if someone sets this directly, remove any color VBO
    //SoGLVBOElement.unsetVBOIfEnabled(state, SoGLVBOElement.VBOType.VERTEX_VBO);
    if (state.isElementEnabled(SoGLVBOElement.getClassStackIndex(SoGLVBOElement.class))) {
        SoGLVBOElement.setVertexVBO(state, null);
      }

    SoCoordinateElement elt;

    // Get an instance we can change (pushing if necessary)
    elt = (SoCoordinateElement ) getElement(state, getClassStackIndex(SoCoordinateElement.class), node);

    if (elt != null) {
        elt.numCoords   = numCoords;
        elt.coords4     = coords;
        elt.coordsAre3D = false;
    }
}




/*! COIN3D
  Returns a pointer to the 3D coordinate array. Don't use this method
  unless SoCoordinateElement::is3D() returns \c TRUE.

  This method is not part of the original SGI Open Inventor v2.1 API.

  \since Coin 1.0
*/
public SbVec3fArray
getArrayPtr3()
{
//#if COIN_DEBUG
  if (!this.is3D()) {
    SoDebugError.postWarning("SoDiffuseColorElement::getArrayPtr3",
                              "coordinates are *not* 3D -- use "+
                              "getArrayPtr4() instead");
  }
//#endif // COIN_DEBUG

  return SbVec3fArray.copyOf(this.coords3)/*D*/;
}

public FloatArray getArrayPtr3f() {
	//#if COIN_DEBUG
	  if (!this.is3D()) {
	    SoDebugError.postWarning("SoDiffuseColorElement::getArrayPtr3",
	                              "coordinates are *not* 3D -- use "+
	                              "getArrayPtr4() instead");
	  }
	//#endif // COIN_DEBUG

	return FloatArray.copyOf(this.coords3);
}

/*!
  Returns a pointer to the 4D coordinate array. Don't use this method
  unless SoCoordinateElement::is3D() returns \c FALSE.

  This method is not part of the original SGI Open Inventor v2.1 API.

  \since Coin 1.0
*/
public SbVec4fArray
getArrayPtr4()
{
//#if COIN_DEBUG
  if (this.is3D()) {
    SoDebugError.postWarning("SoDiffuseColorElement::getArrayPtr4",
                              "coordinates are *not* 4D -- use "+
                              "getArrayPtr3() instead");
  }
//#endif // COIN_DEBUG

  return SbVec4fArray.copyOf(this.coords4)/*D*/;
}

public FloatArray getArrayPtr4f() {
	//#if COIN_DEBUG
	  if (this.is3D()) {
	    SoDebugError.postWarning("SoDiffuseColorElement::getArrayPtr4",
	                              "coordinates are *not* 4D -- use "+
	                              "getArrayPtr3() instead");
	  }
	//#endif // COIN_DEBUG

	  return FloatArray.copyOf(this.coords4)/*D*/;
}

  }
