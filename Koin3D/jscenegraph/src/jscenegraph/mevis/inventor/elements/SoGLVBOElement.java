/*
*
*  Copyright (C) 2011 MeVis Medical Solutions AG  All Rights Reserved.
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
*  Contact information: MeVis, Center for Medical Diagnostic Systems and
*  Visualization GmbH, Universitätsallee 29, D-28359 Bremen, GERMANY, or:
*
*  http://www.mevis.de
*
*/

/*
 Author: Florian Link
 Date:   09-2011
*/

package jscenegraph.mevis.inventor.elements;

import java.nio.ByteBuffer;

import com.jogamp.opengl.GL2;

import jscenegraph.database.inventor.elements.SoElement;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.mevis.inventor.misc.SoVBO;
import jscenegraph.port.VoidPtr;

//! Manage the currently registered VBOs in the state (for coordinates, normals, colors and tex coords)
//! (MeVis ONLY)
/**
 * @author Yves Boyadjian
 *
 */
public class SoGLVBOElement extends SoElement {
	
public
  enum VBOType {
    VERTEX_VBO( 0),
    NORMAL_VBO( 1),
    COLOR_VBO( 2),
    TEXCOORD_VBO( 3),
    MAX_VBO_TYPES( 4);
    private int value;
    VBOType(int value) {
    	this.value = value;
    }
    public int getValue() {
    	return value;
    }
  };

private
  SoVBO[] _vbo = new SoVBO[VBOType.MAX_VBO_TYPES.getValue()];
	
public void
init(SoState state)
{
   super.init(state);
   
   for (int i = 0;i<VBOType.MAX_VBO_TYPES.getValue(); i++) {
     _vbo[i] = null;
   }
}

public void
push(SoState state)
{
   SoGLVBOElement prevElt = (SoGLVBOElement)getNextInStack();

   for (int i = 0;i<VBOType.MAX_VBO_TYPES.getValue(); i++) {
     _vbo[i] = prevElt._vbo[i];
   }
}

public void
pop(SoState state, SoElement childElt)
{
  // no need to pop anything
}


public boolean matches( SoElement elt )
{
  // should not be called
  return true;
}

	public SoElement copyMatchInfo()
	    {
	      // should not be called
	      return null;
	    }

public static void unsetVBO( SoState state, VBOType type )
{
  SoGLVBOElement element = (SoGLVBOElement) state.getElement(classStackIndexMap.get(SoGLVBOElement.class));
  element._vbo[type.getValue()] = null;
}

	
public static void unsetVBOIfEnabled( SoState state, VBOType type )
{
  if (state.isElementEnabled(classStackIndexMap.get(SoGLVBOElement.class))) {
    SoGLVBOElement element = (SoGLVBOElement) state.getElement(classStackIndexMap.get(SoGLVBOElement.class));
    element._vbo[type.getValue()] = null;
  }
}

  //! creates or clear a VBO of given type. Passing the data in is optional and may be done later on
  //! the allocated vbo. The ownership of the VBO is passed to the called.
public  static void updateVBO(SoState state, VBOType type, final SoVBO[] vbo) {
	updateVBO(state,type,vbo, 0, null, 0);
}


public static void updateVBO( SoState state, VBOType type, final SoVBO[] vbo, int numBytes /*= 0*/, /*ByteBuffer*/VoidPtr data /*= NULL*/, int nodeId /*= 0*/ )
{
  // we always create/store a VBO object, since we may even use it for vertex array rendering
  // and we do not know if the user desires VBO usage or not in here...
  SoGLVBOElement element = (SoGLVBOElement) state.getElement(classStackIndexMap.get(SoGLVBOElement.class));
  if ((vbo[0]== null)) {
    vbo[0] = new SoVBO(GL2.GL_ARRAY_BUFFER);
  }
  element._vbo[type.getValue()] = vbo[0];
  if (numBytes!=0) {
    // store data and node id
    (vbo[0]).setData(numBytes, data, nodeId, state);
  }
}

public static SoGLVBOElement getInstance( SoState state )
{
  return (SoGLVBOElement) state.getConstElement(classStackIndexMap.get(SoGLVBOElement.class));
}

public SoVBO getVBO( VBOType type )
{
  return _vbo[type.getValue()];
}
}
