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
 |      Subclass of TextureCoordinateElement that also support GL
 |   rendering-- it has send() methods, and has a getType() that can
 |   return NONE, meaning shapes don't have to do anything to get
 |   texture coordinates generated for them (the GL's TexGen will do
 |   it for them).
 |
 |   Author(s)          : Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.elements;

import com.jogamp.opengl.GL2;

import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;


///////////////////////////////////////////////////////////////////////////////
///
///  \class SoGLTextureCoordinateElement
///  \ingroup Elements
///
///  Element storing the current gltexture coordinates
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoGLTextureCoordinateElement extends SoTextureCoordinateElement {

  private
    SoTexCoordTexgenCB  texgenCB;
    private Object                texgenCBData;
    private SoState             copiedFromParent;
	
	
//!
//! The TextureCoordinateFunction nodes that use the GL texgen function
//! to generate texture coordinates must register a callback that makes
//! the appropriate GL calls with the element so pop() can reset the GL
//! state back to what it should be.  The TextureCoordinateFunction
//! nodes that can't use the GL texgen function must register a NULL
//! callback, in which case the element will disable the GL's automatic
//! texture coordinate generation.
//!
public interface SoTexCoordTexgenCB {
	void run(Object userdata);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    TexGen is being used
//
// Use: public

// java port
public static void
setTexGen(SoState state, SoNode node,
        SoTexCoordTexgenCB texGenFunc) {
	setTexGen( state, node,
	        texGenFunc, null,
	        null, null);
	
}

public static void
setTexGen(SoState state, SoNode node,
        SoTexCoordTexgenCB texGenFunc, Object texGenData,
        SoTextureCoordinateFunctionCB func, Object funcData)
//
////////////////////////////////////////////////////////////////////////
{
    // Do base-class stuff
    SoTextureCoordinateElement.setFunction(state, node, func,
                                            funcData);
    
    // Get an element we can modify:
    SoGLTextureCoordinateElement        elt;
    elt = (SoGLTextureCoordinateElement )
        getElement(state, classStackIndexMap.get(SoGLTextureCoordinateElement.class), node);

    if (elt != null) {

        elt.setElt(state, texGenFunc, texGenData);

    }
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Initializes element.
//
// Use: public

public void
init(SoState s)
//
////////////////////////////////////////////////////////////////////////
{
    super.init(s);

    texgenCB = null;
    texgenCBData = null;
    copiedFromParent = null;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    TexGen is being used
//
// Use: protected

protected void
setElt(SoState state, SoTexCoordTexgenCB func,
                                     Object userData)
//
////////////////////////////////////////////////////////////////////////
{
	GL2 gl2 = SoGLCacheContextElement.get(state); // java port
	
    // Enable or disable texgen as appropriate
    if (func != null) {
        // Only call Enable if not already enabled:
        if (texgenCB == null) {
            gl2.glEnable(GL2.GL_TEXTURE_GEN_S);
            gl2.glEnable(GL2.GL_TEXTURE_GEN_T);
            copiedFromParent = null;
        }
        else if (copiedFromParent != null) {
            SoGLTextureCoordinateElement parent = 
                (SoGLTextureCoordinateElement ) getNextInStack();
            parent.capture(copiedFromParent);
        }

        // Call function to set up texgen parameters
        (func).run(userData);

        whatKind     = CoordType.FUNCTION;
    }
    else {
        // Only call Disable if parent element was enabled:
        if (texgenCB != null) {
            gl2.glDisable(GL2.GL_TEXTURE_GEN_S);
            gl2.glDisable(GL2.GL_TEXTURE_GEN_T);
            copiedFromParent = null;
        }
        else if (copiedFromParent != null) {
            SoGLTextureCoordinateElement parent = 
                (SoGLTextureCoordinateElement ) getNextInStack();
            parent.capture(copiedFromParent);
        }
    }

    texgenCB     = func;
    texgenCBData = userData;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Given an index, send the appropriate stuff to GL.
//
// Use: public

public void
send(int index, GL2 gl2)
//
////////////////////////////////////////////////////////////////////////
{
//#ifdef DEBUG
//    if (whatKind != EXPLICIT)
//        SoDebugError::post("SoGLTextureCoordinateElement::send",
//                           "explicit texture coordinates were not set!");
//
//    if (index < 0 || index >= numCoords)
//        SoDebugError::post("SoGLTextureCoordinateElement::send",
//                           "Index (%d) out of range 0 - %d",
//                           index, numCoords - 1);
//#endif /* DEBUG */

    if (coordsAre2D)
        gl2.glTexCoord2fv(coords2[index].getValue(),0);
    else
        gl2.glTexCoord4fv(coords4[index].getValue(),0);
}


}
