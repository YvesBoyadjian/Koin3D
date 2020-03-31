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
/**************************************************************************\
 * Copyright (c) Kongsberg Oil & Gas Technologies AS
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 
 * Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
\**************************************************************************/

/*!
  \class SoGLVBOElement Inventor/elements/SoGLVBOElement.h
  \brief The SoGLVBOElement class is used to store VBO state.
  \ingroup elements

  FIXME: write doc.

  \COIN_CLASS_EXTENSION

  \since Coin 2.5
*/


package jscenegraph.mevis.inventor.elements;

import java.nio.ByteBuffer;

import com.jogamp.opengl.GL2;

import jscenegraph.coin3d.glue.cc_glglue;
import jscenegraph.coin3d.inventor.lists.SbList;
import jscenegraph.coin3d.inventor.misc.SoGLDriverDatabase;
import jscenegraph.coin3d.misc.SoGL;
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
	
	private static class SoGLVBOElementP {
		
		  public SoVBO vertexvbo;
		  public SoVBO normalvbo;
		  public SoVBO colorvbo;
		  public final SbList <SoVBO> texcoordvbo = new SbList<>();
	}
	
	private SoGLVBOElementP pimpl;
	
public
  enum VBOType { // mevislab
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

//private
//  SoVBO[] _vbo = new SoVBO[VBOType.MAX_VBO_TYPES.getValue()];
	
	public SoGLVBOElement() {
		pimpl = new SoGLVBOElementP();
	}
	

//public static void unsetVBO( SoState state, VBOType type )
//{
//  SoGLVBOElement element = (SoGLVBOElement) state.getElement(classStackIndexMap.get(SoGLVBOElement.class));
//  element._vbo[type.getValue()] = null;
//}

	
//public static void unsetVBOIfEnabled( SoState state, VBOType type )
//{
//  if (state.isElementEnabled(classStackIndexMap.get(SoGLVBOElement.class))) {
//    SoGLVBOElement element = (SoGLVBOElement) state.getElement(classStackIndexMap.get(SoGLVBOElement.class));
//    element._vbo[type.getValue()] = null;
//  }
//}

  //! creates or clear a VBO of given type. Passing the data in is optional and may be done later on
  //! the allocated vbo. The ownership of the VBO is passed to the called.
public  static void updateVBO(SoState state, VBOType type, final SoVBO[] vbo) {
	updateVBO(state,type,vbo, 0, null, 0);
}


public static void updateVBO( SoState state, VBOType type, final SoVBO[] vbo, int numBytes /*= 0*/, /*ByteBuffer*/VoidPtr data /*= null*/, long nodeId /*= 0*/ )
{
  // we always create/store a VBO object, since we may even use it for vertex array rendering
  // and we do not know if the user desires VBO usage or not in here...
  SoGLVBOElement element = (SoGLVBOElement) state.getElement(classStackIndexMap.get(SoGLVBOElement.class));
  if ((vbo[0]== null)) {
    vbo[0] = new SoVBO(GL2.GL_ARRAY_BUFFER);
  }
  switch(type) {
case COLOR_VBO:
	  element.pimpl.colorvbo = vbo[0];
	break;
case MAX_VBO_TYPES:
	break;
case NORMAL_VBO:
	  element.pimpl.normalvbo = vbo[0];
	break;
case TEXCOORD_VBO:
	  element.pimpl.texcoordvbo.operator_square_bracket(0, vbo[0]);
	break;
case VERTEX_VBO:
	  element.pimpl.vertexvbo = vbo[0];
	break;
default:
	break;
  
  }
  if (numBytes!=0) {
    // store data and node id
    (vbo[0]).setData(numBytes, data, nodeId, state);
  }
}

//public SoVBO getVBO( VBOType type )
//{
//  return _vbo[type.getValue()];
//}


// COIN 3D

/*!
  Sets the vertex VBO.
*/
public static void setVertexVBO(SoState state, SoVBO vbo)
{
  SoGLVBOElement elem = getElement(state);
  elem.pimpl.vertexvbo = vbo;
}

/*!
  Sets the normal VBO.
*/
public static void
setNormalVBO(SoState state, SoVBO vbo)
{
  SoGLVBOElement elem = getElement(state);
  elem.pimpl.normalvbo = vbo;
}

/*!
  Sets the color VBO.
*/
public static void
setColorVBO(SoState state, SoVBO vbo)
{
  SoGLVBOElement elem = getElement(state);
  elem.pimpl.colorvbo = vbo;
}

/*!
  Sets the texture coordinate VBO.
*/
public static void
setTexCoordVBO(SoState state, int unit, SoVBO vbo)
{
  SoGLVBOElement elem = getElement(state);
  int n = elem.pimpl.texcoordvbo.getLength();
  for (int i = n; i <= unit; i++) {
    elem.pimpl.texcoordvbo.append(null);
  }
  elem.pimpl.texcoordvbo.operator_square_bracket(unit, vbo);
}

// doc in parent
public void
init(SoState state)
{
  this.pimpl.vertexvbo = null;
  this.pimpl.normalvbo = null;
  this.pimpl.colorvbo = null;
  this.pimpl.texcoordvbo.truncate(0);
}

// doc in parent
public void
push(SoState state)
{
  SoGLVBOElement prev = (SoGLVBOElement )
    this.getNextInStack();

  this.pimpl.vertexvbo = prev.pimpl.vertexvbo;
  this.pimpl.normalvbo = prev.pimpl.normalvbo;
  this.pimpl.colorvbo = prev.pimpl.colorvbo;
  this.pimpl.texcoordvbo.truncate(0);

  for (int i = 0; i < prev.pimpl.texcoordvbo.getLength(); i++) {
    this.pimpl.texcoordvbo.append(prev.pimpl.texcoordvbo.operator_square_bracket(i));
  }
}

// doc in parent
public void
pop(SoState state, SoElement prevtopelement)
{
  // nothing to do
}

// doc in parent
public boolean
matches(SoElement elt)
{
  assert(false);// && "should never get here");
  return true;
}

// doc in parent
public SoElement 
copyMatchInfo()
{
  assert(false);// && "should never get here");
  return null;
}

/*!
  Returns a writable element instance.
*/
public static SoGLVBOElement
getElement(SoState state)
{
  return (SoGLVBOElement) state.getElement(classStackIndexMap.get(SoGLVBOElement.class));
}

/*!
  Returns a read-only element instance.
*/
public static SoGLVBOElement getInstance(SoState state)
{
  return (SoGLVBOElement) state.getConstElement(classStackIndexMap.get(SoGLVBOElement.class));
}

public SoVBO 
getVertexVBO() 
{
  return this.pimpl.vertexvbo;
}

public SoVBO 
getNormalVBO() 
{
  return this.pimpl.normalvbo;
}

public SoVBO 
getColorVBO() 
{
  return this.pimpl.colorvbo;
}

public int
getNumTexCoordVBO()
{
  return this.pimpl.texcoordvbo.getLength();
}

public SoVBO
getTexCoordVBO( int idx)
{
  if (idx < this.pimpl.texcoordvbo.getLength()) {
    return this.pimpl.texcoordvbo.operator_square_bracket(idx);
  }
  return null;
}

/*!
  Returns \a TRUE if VBO is supported for the current context,
  and if numdata is between the limits set for VBO rendering.

*/
public static boolean
shouldCreateVBO(SoState state, int numdata)
{
  cc_glglue glue = SoGL.sogl_glue_instance(state);
  // don't use SoGLCacheContextElement to find the current cache
  // context since we don't want this call to create a cache dependecy
  // on SoGLCacheContextElement.
  return
    SoGLDriverDatabase.isSupported(glue, SoGLDriverDatabase.SO_GL_FRAMEBUFFER_OBJECT) &&
    SoVBO.shouldCreateVBO(state, glue.contextid, numdata);
}

//#undef PRIVATE

}
