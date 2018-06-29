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
 |      This file defines the SoTextureCoordinateElement class.
 |   TextureCoordinates can be specified in several different ways,
 |   from several different nodes:  they can be explicitly given by
 |   TextureCoordinate2/4 nodes, they can be automatically generated
 |   (the TextureCoordinateFunction nodes), or they can be DEFAULT,
 |   meaning that each shape must generate its own texture
 |   coordinates (can be set by TextureCoordinateFunctionDefault
 |   node).
 |
 |   Shapes will have to call TextureCoordinateElement::getType() and
 |   then call either generate default texture coordinates if it
 |   returns DEFAULT, call get2/get4 if it returns EXPLICIT, or call
 |   get(point, normal) if it returns FUNCTION.
 |
 |   Author(s)          : Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */
/**************************************************************************\
 *
 *  This file is part of the Coin 3D visualization library.
 *  Copyright (C) by Kongsberg Oil & Gas Technologies.
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  ("GPL") version 2 as published by the Free Software Foundation.
 *  See the file LICENSE.GPL at the root directory of this source
 *  distribution for additional information about the GNU GPL.
 *
 *  For using Coin with software that can not be combined with the GNU
 *  GPL, and for taking advantage of the additional benefits of our
 *  support services, please contact Kongsberg Oil & Gas Technologies
 *  about acquiring a Coin Professional Edition License.
 *
 *  See http://www.coin3d.org/ for more information.
 *
 *  Kongsberg Oil & Gas Technologies, Bygdoy Alle 5, 0257 Oslo, NORWAY.
 *  http://www.sim.no/  sales@sim.no  coin-support@coin3d.org
 *
\**************************************************************************/

/*!
  \class SoTextureCoordinateElement Inventor/elements/SoTextureCoordinateElement.h
  \brief The SoTextureCoordinateElement class is yet to be documented.
  \ingroup elements

  FIXME: write doc.
*/


package jscenegraph.database.inventor.elements;

import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec4f;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.mevis.inventor.elements.SoGLVBOElement;


///////////////////////////////////////////////////////////////////////////////
///
///  \class SoTextureCoordinateElement
///  \ingroup Elements
///
///  Element storing the current texture coordinates
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoTextureCoordinateElement extends SoReplacedElement {
	
  private
    final SbVec2f             convert2 = new SbVec2f();       //!< To convert from 4-D to 2-D
    private final SbVec4f             convert4 = new SbVec4f();       //!< To convert from 2-D to 4-D
	
	
	  public
    //! The (seemingly random) choice of values is for compatibility
    //! with Inventor 2.0 binary format files:
    enum CoordType {
		  NONE(0), // COIN3D TODO
        EXPLICIT(1),   //!< Coordinates stored in state
        FUNCTION(2);    //!< Coordinates generated by software function
        private int value;
        CoordType(int value) {
        	this.value = value;
        }
        public int getValue() {
        	return value;
        }
    };
    
//! Function that TextureCoordinateFunction nodes register to compute
//! texture coordinates.  Given the point and normal, compute a texture
//! coordinate and return it:
    public interface SoTextureCoordinateFunctionCB {
    	SbVec4f run(Object userdata, final SbVec3f point,
                final SbVec3f normal);
    };

	  protected
    //! What kind of coordinates will be done:
    CoordType           whatKind;
	
    //! Storage for FUNCTION:
    SoTextureCoordinateFunctionCB       funcCB;
    Object                              funcCBData;

	
    //! Storage for EXPLICIT:
    public  int             numCoords;
    public SbVec2f[]       coords2;
    public SbVec3f[] 		coords3; // COIN 3D
    public SbVec4f[]       coords4;
    public  boolean              coordsAre2D;
    protected int coordsDimension; // COIN 3D
  	

	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //    Returns the top (current) instance of the element in the state.
	   //
	   // Use: public
	   
	  public static SoTextureCoordinateElement
	   getInstance(SoState state)
	   //
	   ////////////////////////////////////////////////////////////////////////
	   {
	       return ( SoTextureCoordinateElement )
	           getConstElement(state, classStackIndexMap.get(SoTextureCoordinateElement.class));
	   }

	     //! Returns the number of coordinate points in an instance.
	  public     int             getNum()           { return numCoords; }
	   
	  public boolean              is2D()             { return coordsAre2D; }

	  // java port
	public float[] get2Ptr() {
	     if (coordsAre2D) {
	    	 float[] textureCoordArray = new float[numCoords*2];
	    	 int index = 0;
	    	 for (int i=0; i< numCoords;i++) {
	    		 textureCoordArray[index] = coords2[i].getValue()[0];
	    		 index++;
	    		 textureCoordArray[index] = coords2[i].getValue()[1];
	    		 index++;
	    	 }
	    	           return textureCoordArray;
	     }
	    	       else {
	    	    	   throw new IllegalStateException();
	    	       }
	    	  	}

	// java port
	public float[] get4Ptr() {
	     // Convert from 2-D if necessary
		       if (coordsAre2D) {
		    	   throw new IllegalStateException();
		       }
		   
		       else {
			    	 float[] textureCoordArray = new float[numCoords*4];
			    	 int index = 0;
			    	 for (int i=0; i< numCoords;i++) {
			    		 textureCoordArray[index] = coords4[i].getValue()[0];
			    		 index++;
			    		 textureCoordArray[index] = coords4[i].getValue()[1];
			    		 index++;
			    		 textureCoordArray[index] = coords4[i].getValue()[2];
			    		 index++;
			    		 textureCoordArray[index] = coords4[i].getValue()[3];
			    		 index++;
			    	 }
		           return textureCoordArray;
		       }
		   	}
	   
	
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns code depending on which set routine was called.
//
// Use: public, static

public static SoTextureCoordinateElement.CoordType
getType(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    return getInstance(state).getType();
}

	////////////////////////////////////////////////////////////////////////
//
// Description:
//    Non-static (virtual) version of above method.
//
// Use: public

public SoTextureCoordinateElement.CoordType
getType()
//
////////////////////////////////////////////////////////////////////////
{
    return whatKind;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Given a point and a normal, call the registered callback to get
//    corresponding texture coordinates:
//
// Use: public

public SbVec4f 
get(final SbVec3f point,
                                final SbVec3f normal)
//
////////////////////////////////////////////////////////////////////////
{
//#ifdef DEBUG
    if (whatKind != CoordType.FUNCTION)
        SoDebugError.post("SoTextureCoordinateElement::get",
                           "Functional texture coordinates were not set!");
//#endif /* DEBUG */
    return funcCB.run(funcCBData, point, normal);
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets the current coords, given a function to compute them.
//
// Use: public

public static void
setFunction(SoState state, SoNode node,
                                        SoTextureCoordinateFunctionCB func,
                                        Object userData)
//
////////////////////////////////////////////////////////////////////////
{
    SoTextureCoordinateElement  elt;

    elt = (SoTextureCoordinateElement ) getElement(state,
                                    classStackIndexMap.get(SoTextureCoordinateElement.class), node);
    if (elt != null) {
        elt.whatKind = CoordType.FUNCTION;
        elt.funcCB = func;
        elt.funcCBData = userData;
    }
    //The shapeStyle element will track this value:
    SoShapeStyleElement.setTextureFunction(state, true);
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
//    if (whatKind != EXPLICIT)
//        SoDebugError::post("SoTextureCoordinateElement::get4",
//                           "Explicit texture coordinates were not set!");
//
//    if (index < 0 || index >= numCoords)
//        SoDebugError::post("SoTextureCoordinateElement::get4",
//                           "Index (%d) is out of range 0 - %d",
//                           index, numCoords - 1);
//#endif /* DEBUG */

    // Convert from 2-D if necessary
    if (coordsAre2D) {
        // Cast the const away...
        SoTextureCoordinateElement elt = (SoTextureCoordinateElement ) this;
        final SbVec2f              c2  = coords2[index];

        elt.convert4.getValue()[0] = c2.getValue()[0];
        elt.convert4.getValue()[1] = c2.getValue()[1];
        elt.convert4.getValue()[2] = 0.0f;
        elt.convert4.getValue()[3] = 1.0f;

        return convert4;
    }

    else
        return coords4[index];
}

	
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets the current coords, given a set of 2D texture coordinates.
//
// Use: public

public static void
set2(SoState state, SoNode node,
                                 int numCoords, final SbVec2f[] coords)
//
////////////////////////////////////////////////////////////////////////
{
    // if someone sets this directly, remove any texcoord VBO
    SoGLVBOElement.unsetVBOIfEnabled(state, SoGLVBOElement.VBOType.TEXCOORD_VBO);

    SoTextureCoordinateElement  elt = (SoTextureCoordinateElement )getElement(state, classStackIndexMap.get(SoTextureCoordinateElement.class), node);

    if (elt != null) {
        elt.coordsDimension = 2; // COIN 3D
        elt.whatKind    = CoordType.EXPLICIT;
        elt.numCoords   = numCoords;
        elt.coords2     = coords;
        elt.coordsAre2D = true;
    }
    SoShapeStyleElement.setTextureFunction(state, false);
}


/*!
  FIXME: write doc.

  \COIN_FUNCTION_EXTENSION

  \since Coin 2.0
*/
public static void // COIN 3D
set3(final SoState state,
                                 final SoNode node,
                                 final int numCoords,
                                 final SbVec3f[] coords)
{
//  if (state.isElementEnabled(SoGLVBOElement.getClassStackIndex(SoGLVBOElement.class))) { COIN 3D
//    SoGLVBOElement.setTexCoordVBO(state, 0, null);
//  }

    // if someone sets this directly, remove any texcoord VBO
    SoGLVBOElement.unsetVBOIfEnabled(state, SoGLVBOElement.VBOType.TEXCOORD_VBO); // MEVISLAB

	SoTextureCoordinateElement  element =
    (SoTextureCoordinateElement)
    (
     SoReplacedElement.getElement(state, classStackIndexMap.get(SoTextureCoordinateElement.class), node)
     );
  if (element != null) {
    element.coordsDimension = 3;
    element.numCoords = numCoords;
    element.coords2 = null;
    element.coords3 = coords;
    element.coords4 = null;
    element.whatKind = CoordType.EXPLICIT;
  }
}

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

    whatKind  = CoordType.EXPLICIT;
    funcCB    = null;
    numCoords = 0;
    coords2   = null;
    coords4   = null;
    coordsDimension = 2; //Initialize to 2D as before COIN 3D
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Tell shapes to generate their own texture coordinates.  This is
//    the default.
//
// Use: public

public static void
setDefault(SoState state, SoNode node)
//
////////////////////////////////////////////////////////////////////////
{
    SoTextureCoordinateElement  elt;

    elt = (SoTextureCoordinateElement )
        getElement(state, classStackIndexMap.get(SoTextureCoordinateElement.class), node);

    if (elt != null) {
        elt.whatKind = CoordType.EXPLICIT;
        elt.numCoords = 0;
        elt.coords2 = null;
        elt.coords4 = null;
    }
    //The shapeStyle element will track this value:
    SoShapeStyleElement.setTextureFunction(state, false);
}



/*! FIXME: write doc.

  \COIN_FUNCTION_EXTENSION

  \since Coin 2.0
*/
//$ EXPORT INLINE
public int
getDimension()
{
  return this.coordsDimension;
}


/*! COIN3D
  Returns a pointer to the 2D texture coordinate array. This method is not
  part of the OIV API.
*/
public SbVec2f[] getArrayPtr2()
{
  return this.coords2;
}


}
