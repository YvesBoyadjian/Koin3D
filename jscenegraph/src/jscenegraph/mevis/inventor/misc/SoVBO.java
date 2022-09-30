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
 *  Visualization GmbH, Universit�tsallee 29, D-28359 Bremen, GERMANY, or:
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
  \class SoVBO
  \brief The SoVBO class is used to handle OpenGL vertex buffer objects.

  It wraps the buffer handling, taking care of multi-context handling
  and allocation/deallocation of buffers. FIXME: more doc.

*/

package jscenegraph.mevis.inventor.misc;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import jscenegraph.opengl.GL;
import jscenegraph.opengl.GL2;

import jscenegraph.coin3d.glue.cc_glglue;
import jscenegraph.coin3d.inventor.misc.SoContextHandler;
import jscenegraph.coin3d.misc.SoGL;
import jscenegraph.database.inventor.elements.SoGLCacheContextElement;
import jscenegraph.database.inventor.elements.SoGLDisplayList;
import jscenegraph.database.inventor.elements.SoShapeHintsElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.port.CharPtr;
import jscenegraph.port.Ctx;
import jscenegraph.port.Destroyable;
import jscenegraph.port.VoidPtr;
import jscenegraph.port.memorybuffer.MemoryBuffer;


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
private		  long _nodeId;

private		  SoGLDisplayList _glBuffer;

private		  boolean _ownsData;
private		  boolean _hasSwappedRGBAData;
private		  boolean _hadGLError;

private		  static int _vboMinimumSizeLimit = 20;
private		  static int _vboMaximumSizeLimit = 0x10000000;

// COIN3D
private /*GLenum*/int target;
private /*GLenum*/int usage;
//private /*const GLvoid **/VoidPtr data;
private /*intptr_t*/long datasize;
private /*SbUniqueId*/long dataid;
private boolean didalloc;

private final /*SbHash<uint32_t, GLuint>*/Map<Integer,Integer> vbohash = new HashMap<>();


//private static int vbo_vertex_count_min_limit = -1;
//private static int vbo_vertex_count_max_limit = -1;
//private static int vbo_render_as_vertex_arrays = -1;
//private static int vbo_enabled = -1;
private static int vbo_debug = 0;

// VBO rendering seems to be faster than other rendering, even for
// large VBOs. Just set the default limit very high
private static final int DEFAULT_MAX_LIMIT = 100000000;
private static final int DEFAULT_MIN_LIMIT = 20;

	
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
	this(type, GL2.GL_STATIC_DRAW);
}

public SoVBO( int target_, int usage_ )
{
	usage = usage_;
  _type = target_;
  target = target_; // COIN 3D
  _ownsData = false;
  _numBytes = 0;
  _data = null;
  _nodeId = 0;
  _glBuffer = null;
  _hasSwappedRGBAData = false;
  _hadGLError = false;

  SoContextHandler.addContextDestructionCallback(SoVBO::context_destruction_cb, this);
}


//
// Callback from SoGLCacheContextElement
//
public static void
vbo_delete(Object closure, int contextid)
{
  cc_glglue glue = SoGL.cc_glglue_instance((int) contextid);
  int id = (int) ((int) closure);
  final int[] ids = new int[1];
  ids[0] = id;
  SoGL.cc_glglue_glDeleteBuffers(glue, 1, ids);
}


// java port
public void destructor()
{
	  SoContextHandler.removeContextDestructionCallback(SoVBO::context_destruction_cb, this);
	  // schedule delete for all allocated GL resources
	  for(
	      Iterator<Entry<Integer, Integer>> iter =
	       this.vbohash.entrySet().iterator();
	      iter.hasNext();
	      )
	    {
		  Entry<Integer, Integer> entry = iter.next();
	      Object ptr = ((int) entry.getValue());
	      SoGLCacheContextElement.scheduleDeleteCallback(entry.getKey(), SoVBO::vbo_delete, ptr);
	  }

	
  clearData();
  freeGL();
}


//
// Callback from SoContextHandler
//
public static void
context_destruction_cb(int context, Object userdata)
{
  Integer buffer;
  SoVBO thisp = (SoVBO) userdata;

  if ((buffer = thisp.vbohash.get(context))!=null) {
    cc_glglue glue = SoGL.cc_glglue_instance((int) context);
    final int[] buffers = new int [1]; buffers[0] = buffer;
    SoGL.cc_glglue_glDeleteBuffers(glue, 1, buffers);
    thisp.vbohash.remove(context);
  }
}




public void setData(int numBytes, final /*Buffer*/VoidPtr data, long nodeId, SoState state)
{
  // free previous data if it was owned
  if (_ownsData && (_data != null)) {
	  Destroyable.delete(_data);
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
public void
setBufferData(VoidPtr data, int size)
{
	setBufferData(data, size, 0);
}
public void
setBufferData(VoidPtr data, int size, long dataid)
{
	setBufferData(data,size,dataid,null); // TODO YB 
}

/*!
Sets the buffer data. \a dataid is a unique id used to identify
the buffer data. In Coin it's possible to use the node id
(SoNode::getNodeId()) to test if a buffer is valid for a node.
*/
public void setBufferData(VoidPtr data, int size, long dataid, SoState state) // COIN 3D
{
	  // schedule delete for all allocated GL resources
	  for(
		      Iterator<Entry<Integer, Integer>> iter =
		       this.vbohash.entrySet().iterator();
		      iter.hasNext();
		      )
		    {
			  Entry<Integer, Integer> entry = iter.next();
		      Object ptr = ((int) entry.getValue());
	    SoGLCacheContextElement.scheduleDeleteCallback(entry.getKey(), SoVBO::vbo_delete, ptr);
	  }


	  // clear hash table
	  this.vbohash.clear();

	  // free previous data if it was owned
	  if (_ownsData && (_data != null)) {
		  Destroyable.delete(_data);		  
	    _data = null;//free((void*)_data);
	  }
	  if (dataid != _nodeId) {
	    freeGL(state);
	  }

	  _numBytes = size;
	  _data = data;
	  datasize = size;
	  _nodeId = dataid;
	  _ownsData = false;
	  _hasSwappedRGBAData = false;
}

public void allocateData( int numBytes, long nodeId , SoState state)
{
  if (_ownsData && (_data!= null)) {
	  Destroyable.delete(_data);
    _data = null;//free((void*)_data);
  }
  freeGL(state);

  _numBytes = numBytes;
  _data = VoidPtr.create(/*Buffers.newDirectByteBuffer(numBytes)*/MemoryBuffer.allocateBytesMalloc(numBytes));
  datasize = numBytes;
  _nodeId = nodeId;
  _ownsData = true;
  _hasSwappedRGBAData = false;
}

// java port
public VoidPtr allocBufferData(int size) {
	return allocBufferData(size,0);
}

public VoidPtr allocBufferData(int size, long dataid) {
	return allocBufferData(size,dataid,null); // TODO YB
}

/**
 * COIN 3D YB
 * @param size
 * @param dataid
 * @param state
 * @return
 */
public VoidPtr allocBufferData(int size, long dataid, SoState state) {
	
	  // schedule delete for all allocated GL resources
	  for(
		      Iterator<Entry<Integer, Integer>> iter =
		       this.vbohash.entrySet().iterator();
		      iter.hasNext();
		      )
		    {
			  Entry<Integer, Integer> entry = iter.next();
		      Object ptr = ((int) entry.getValue());
	    SoGLCacheContextElement.scheduleDeleteCallback(entry.getKey(), SoVBO::vbo_delete, ptr);
	  }

	  // clear hash table
	  this.vbohash.clear();

	
	
	allocateData(size, dataid, state);
	return _data;
}

public void clearData()
{
  if (_ownsData && (_data!= null)) {
    Destroyable.delete(_data);//free((void*)_data);
  }
  _data = null;
  _numBytes = 0;
  _ownsData = false;
  _hasSwappedRGBAData = false;
}

public void copyAndSwapPackedRGBA( int numValues, final IntBuffer values, long nodeId, SoState state )
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
    	gl2.glBufferData(_type, (long)_numBytes, _data.toFloatArray().toByteBuffer(), gl2.GL_STATIC_DRAW);    	
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
public long getDataId() { return _nodeId; }

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
      SoVBO.isVBOFast(contextid)
      //&& (SoShapeStyleElement.get(state).getFlags() & SoShapeStyleElement.Flags.SHADOWMAP.getValue()) == 0 // YB : Why disabling VBO for shadowmap mode ? It works perfectly
;
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
public long getBufferDataId() // COIN 3D
{
  //return this.dataid;
	return this._nodeId; // YB
}

/*!
  Binds the buffer for the context \a contextid.
*/
public void
bindBuffer(int contextid)
{
  if ((this._data == null) ||
      (this.datasize == 0)) {
    throw new IllegalStateException(/*assert(0 &&*/ "no data in buffer");
    //return; //java port
  }

  final cc_glglue glue = SoGL.cc_glglue_instance((int) contextid);

  final Integer[] buffer = new Integer[1];
  if ( (buffer[0] = this.vbohash.get(contextid)) == null) {
    // need to create a new buffer for this context
    SoGL.cc_glglue_glGenBuffers(glue, 1, buffer);
    SoGL.cc_glglue_glBindBuffer(glue, this.target, buffer[0]);
    SoGL.cc_glglue_glBufferData(glue, this.target,
                           this.datasize,
                           this._data,
                           this.usage);
    this.vbohash.put(contextid, buffer[0]);
  }
  else {
    // buffer already exists, bind it
    SoGL.cc_glglue_glBindBuffer(glue, this.target, buffer[0]);
  }

//#if COIN_DEBUG
  if (vbo_debug != 0) {
    if (this.target == GL.GL_ELEMENT_ARRAY_BUFFER) {
      SoDebugError.postInfo("SoVBO::bindBuffer",
                             "Rendering using VBO. Index array size: "+
                             (this.datasize / Integer.BYTES)/*sizeof(int32_t)*/);
    }
    else {
      SoDebugError.postInfo("SoVBO::bindBuffer",
                             "Setting up buffer for rendering. Datasize: "+
                             this.datasize);
    }
  }
//#endif // COIN_DEBUG
}


public static boolean
shouldRenderAsVertexArrays(SoState state,
                                  int contextid,
                                  int numdata)
{
  // FIXME: consider also using results from the performance tests

  // don't render as vertex arrays if there are very few elements to
  // be rendered. The VA setup overhead would make it slower than just
  // doing plain immediate mode rendering.
  return (numdata >= vbo_vertex_count_min_limit) && vbo_render_as_vertex_arrays != 0;
}


}