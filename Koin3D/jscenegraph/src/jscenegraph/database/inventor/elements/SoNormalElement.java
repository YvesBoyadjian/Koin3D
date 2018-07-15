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
 |      This file defines the SoNormalElement class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.elements;

import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.mevis.inventor.elements.SoGLVBOElement;
import jscenegraph.port.SbVec3fArray;


///////////////////////////////////////////////////////////////////////////////
///
///  \class SoNormalElement
///  \ingroup Elements
///
///  Element that stores the current surface normals.
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
public class SoNormalElement extends SoReplacedElement {

	   protected
		        int             numNormals;
	   protected SbVec3fArray       normals;
		    

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

    normals     = null;
    numNormals  = 0;
}

	   
		   	
    //! Returns the top (current) instance of the element in the state
  public static SoNormalElement getInstance(SoState state)
           {return ( SoNormalElement )
               getConstElement(state, classStackIndexMap.get(SoNormalElement.class));}
  
  //! Returns the number of normal vectors in an instance
  public int             getNum()           { return numNormals; }
 
     //! Returns the indexed normal from an element
  public SbVec3f get(int index)
         {
// #ifdef DEBUG
     if (index < 0 || index >= numNormals)
         SoDebugError.post("SoNormalElement::get",
                            "Index ("+index+") is out of range 0 - "+(numNormals - 1));
// #endif /* DEBUG */
             return normals.get(index);
         }
   
  // java port
  public float[] get0() {
	  float[] normalArray = new float[numNormals*3];
	  int index=0;
	  for(int i = 0; i< numNormals;i++) {
		  normalArray[index] = normals.get(i).getValueRead()[0];
		  index++;
		  normalArray[index] = normals.get(i).getValueRead()[1];
		  index++;
		  normalArray[index] = normals.get(i).getValueRead()[2];
		  index++;
	  }
	  return normalArray;
  }
  

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets the normals in element accessed from state.
//
// Use: public

public static void
set(SoState state, SoNode node,
                     int numNormals, final SbVec3fArray normals)
//
////////////////////////////////////////////////////////////////////////
{
    // if someone sets this directly, remove any normal VBO
    SoGLVBOElement.unsetVBOIfEnabled(state, SoGLVBOElement.VBOType.NORMAL_VBO);

    // Get an instance we can change (pushing if necessary)
    SoNormalElement elt = (SoNormalElement ) getElement(state, classStackIndexMap.get(SoNormalElement.class), node);
    if (elt != null) {
        elt.numNormals = numNormals;
        elt.normals    = normals;
    }
}

  /*!
  Returns a pointer to the normal array. This method is not part of the OIV API.
*/
public SbVec3fArray
getArrayPtr()
{
  return this.normals;
}

  
   }
