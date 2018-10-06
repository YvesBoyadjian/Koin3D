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

package jscenegraph.mevis.inventor.misc;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2;

import jscenegraph.database.inventor.elements.SoGLCacheContextElement;
import jscenegraph.database.inventor.elements.SoGLDisplayList;
import jscenegraph.database.inventor.elements.SoShapeHintsElement;
import jscenegraph.database.inventor.elements.SoShapeStyleElement;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.port.CharPtr;
import jscenegraph.port.Ctx;
import jscenegraph.port.Destroyable;
import jscenegraph.port.VoidPtr;


//! SoVBO is used to manage OpenGL vertex buffer objects.
//! Note that the creation of a real OpenGL VBO is deferred until bind() is
//! called, so that the VBO is created lazily.
//! (MeVis ONLY)
/**
 * @author Yves Boyadjian
 *
 */
public class SoVBO implements Destroyable {


private	  int _type;

private		  /*Buffer*/VoidPtr _data;
private		  int      _numBytes;
private		  int _nodeId;

private		  SoGLDisplayList _glBuffer;

private		  boolean _ownsData;
private		  boolean _hasSwappedRGBAData;
private		  boolean _hadGLError;

private		  static int _vboMinimumSizeLimit = 20;
private		  static int _vboMaximumSizeLimit = 0x10000000;
	
private final static boolean isVARenderingAllowed;
private final static boolean isVBORenderingAllowed;

static {
	  isVARenderingAllowed = (System.getenv("IV_NO_VERTEX_ARRAY")==null);	
	  isVBORenderingAllowed = (System.getenv("IV_NO_VBO")==null);
}
public static boolean isVertexArrayRenderingAllowed()
{
  return isVARenderingAllowed;
}

public static boolean shouldUseVBO( SoState state , int numData )
{
	GL2 gl2 = Ctx.get(SoGLCacheContextElement.get(state)); // java port

  boolean result = isVBORenderingAllowed && gl2.isExtensionAvailable("GL_ARB_vertex_buffer_object") && (numData > _vboMinimumSizeLimit) && (numData < _vboMaximumSizeLimit);
  if (state != null) {
    result = result && SoShapeHintsElement.isVBOUsed(state);
  }
  return result;
}

public SoVBO( int type )
{
  _type = type;
  _ownsData = false;
  _numBytes = 0;
  _data = null;
  _nodeId = 0;
  _glBuffer = null;
  _hasSwappedRGBAData = false;
  _hadGLError = false;
}

// java port
public void destructor()
{
  clearData();
  freeGL();
}

public void setData(int numBytes, final /*Buffer*/VoidPtr data, int nodeId, SoState state)
{
  // free previous data if it was owned
  if (_ownsData && (_data != null)) {
    _data = null;//free((void*)_data);
  }
  if (nodeId != _nodeId) {
    freeGL(state);
  }

  _numBytes = numBytes;
  _data = data;
  _nodeId = nodeId;
  _ownsData = false;
  _hasSwappedRGBAData = false;
}

/*!
Sets the buffer data. \a dataid is a unique id used to identify
the buffer data. In Coin it's possible to use the node id
(SoNode::getNodeId()) to test if a buffer is valid for a node.
*/
public void setBufferData(VoidPtr data, int size, int dataid, SoState state) // COIN 3D
{
	  // free previous data if it was owned
	  if (_ownsData && (_data != null)) {
	    _data = null;//free((void*)_data);
	  }
	  if (dataid != _nodeId) {
	    freeGL(state);
	  }

	  _numBytes = size;
	  _data = data;
	  _nodeId = dataid;
	  _ownsData = false;
	  _hasSwappedRGBAData = false;
}

public void allocateData( int numBytes, int nodeId , SoState state)
{
  if (_ownsData && (_data!= null)) {
    _data = null;//free((void*)_data);
  }
  freeGL(state);

  _numBytes = numBytes;
  _data = VoidPtr.create(Buffers.newDirectByteBuffer(numBytes/*/Integer.BYTES*/));//ByteBuffer.allocate(numBytes);//new int[numBytes/Integer.SIZE];//malloc(numBytes); TODO JOGL
  _nodeId = nodeId;
  _ownsData = true;
  _hasSwappedRGBAData = false;
}

/**
 * COIN 3D YB
 * @param size
 * @param dataid
 * @param state
 * @return
 */
public VoidPtr allocBufferData(int size, int dataid, SoState state) {
	allocateData(size, dataid, state);
	return _data;
}

public void clearData()
{
  if (_ownsData && (_data!= null)) {
    _data = null;//free((void*)_data);
  }
  _data = null;
  _numBytes = 0;
  _ownsData = false;
  _hasSwappedRGBAData = false;
}

public void copyAndSwapPackedRGBA( int numValues, final IntBuffer values, int nodeId, SoState state )
{
  allocateData((int)(numValues * (long)Integer.SIZE/Byte.SIZE), nodeId, state);

  _hasSwappedRGBAData = true;

  IntBuffer dest = (IntBuffer)_data.toIntBuffer()/*.asIntBuffer()*/;
  dest.clear();
  for (int i = 0; i < numValues; i++) {
    int value = values.get(i);
    
    dest.put( 
      (value << 24) |
      ((value & 0xff00) << 8) |
      ((value & 0xff0000) >>> 8) |
      (value >>> 24));
  }
  dest.rewind();
}

public void copyAndSwapPackedRGBA( SoState state )
{
  copyAndSwapPackedRGBA(_numBytes/4, (IntBuffer)_data.toIntBuffer()/*.asIntBuffer()*/, _nodeId, state);
}

public void freeGL() {
	freeGL(null);
}
public void freeGL(SoState state)
{
  if (_glBuffer != null) {
    _glBuffer.unref(state);
    _glBuffer = null;
  }
}

public boolean bind(SoState state)
{
	GL2 gl2 = Ctx.get(SoGLCacheContextElement.get(state));
	
  if ((_glBuffer==null) || SoGLCacheContextElement.get(state)!=_glBuffer.getContext()) {
    if (_glBuffer != null) {
      _glBuffer.unref(state);
    }
    // clear GL error state to avoid getting errors from someone else
    gl2.glGetError();

    _glBuffer = new SoGLDisplayList(state, SoGLDisplayList.Type.VERTEX_BUFFER_OBJECT);
    _glBuffer.ref();
    gl2.glBindBuffer(_type, _glBuffer.getFirstIndex());
    if(_data != null && _data.isFloatArray() && _data.toFloatArray().getStart()==0) {
    	gl2.glBufferData(_type, (long)_numBytes, _data.toFloatArray().getValues(), gl2.GL_STATIC_DRAW);    	
    }
    else if(_data != null && _data.isIntArray() && _data.toIntArray().getStart()==0) {
    	gl2.glBufferData(_type, (long)_numBytes, _data.toIntArray().getValues(), gl2.GL_STATIC_DRAW);    	
    }
    else {
    	gl2.glBufferData(_type, (long)_numBytes, _data == null ? null : _data.toBuffer(), gl2.GL_STATIC_DRAW);
    }
    _hadGLError = (gl2.glGetError() != gl2.GL_NO_ERROR);
    if (_hadGLError) {
      // unbind after error
      gl2.glBindBuffer(_type, 0);
    }
  } else {
    if (!_hadGLError) {
      // data is still there, just bind it
      gl2.glBindBuffer(_type, _glBuffer.getFirstIndex());
    } else {
      // unbind previous vbo
      gl2.glBindBuffer(_type, 0);
    }
  }
  return !_hadGLError;
}

public void unbind(GL2 gl2)
{
  gl2.glBindBuffer(_type, 0);
}


public void updateData( GL2 gl2, Object data )
{
	ByteBuffer buffer;
	if(data instanceof ByteBuffer) {
		buffer = (ByteBuffer)data;
	}
	else if(data instanceof CharPtr) {
		CharPtr charPtr = (CharPtr)data;
		if(charPtr.getByteOffset() == 0) {
			buffer = charPtr.getBuffer();
		}
		else {
			throw new IllegalArgumentException("CharPtr has non null offset");
		}
	}
	else {
		throw new IllegalArgumentException("Unknown data object");		
	}
  gl2.glBufferSubData(_type, 0l, _numBytes, buffer);
}

public boolean isValid(SoState state)
{
  return (_glBuffer != null && SoGLCacheContextElement.get(state)==_glBuffer.getContext());
}


  //! get the node id of the data
public int getDataId() { return _nodeId; }

  //! get the data pointer
  public /*Buffer*/VoidPtr getData() { return _data; }

  //! returns if the stored data has been swapped to be compatible to OpenGL on little endian machines
  public boolean hasSwappedRGBAData() { return _hasSwappedRGBAData; }

  //! get the lower limit for VBO usage
  public static int getVboMinimumSizeLimit() { return _vboMinimumSizeLimit; }

  static int vbo_render_as_vertex_arrays = -1;
  static int vbo_enabled = -1;
  
  public static boolean
  shouldCreateVBO(SoState state, int contextid, int numdata)
  {
    if (vbo_enabled == 0 || vbo_render_as_vertex_arrays == 0) return false;
    int minv = SoVBO.getVertexCountMinLimit();
    int maxv = SoVBO.getVertexCountMaxLimit();
    return
      (numdata >= minv) &&
      (numdata <= maxv) &&
      SoVBO.isVBOFast(contextid) &&
      (SoShapeStyleElement.get(state).getFlags() & SoShapeStyleElement.Flags.SHADOWMAP.getValue()) == 0;

  }

  static int vbo_vertex_count_min_limit = /*-1*/_vboMinimumSizeLimit;
  static int vbo_vertex_count_max_limit = /*-1*/_vboMaximumSizeLimit;
  
  /*!
  Returns the vertex VBO minimum limit.

  \sa setVertexCountLimits()
 */
public static int
getVertexCountMinLimit()
{
  return vbo_vertex_count_min_limit;
}

/*!
  Returns the vertex VBO maximum limit.

  \sa setVertexCountLimits()
 */
public static int
getVertexCountMaxLimit()
{
  return vbo_vertex_count_max_limit;
}

public static boolean
isVBOFast(int contextid)
{
  boolean result = true;
  //assert(vbo_isfast_hash != NULL);
  //vbo_isfast_hash.get(contextid, result); TODO
  return result;
}

/*!
  Returns the buffer data id.

  \sa setBufferData()
*/
public int getBufferDataId() // COIN 3D
{
  //return this.dataid;
	return this._nodeId; // YB
}


}