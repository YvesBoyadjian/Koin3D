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

package jscenegraph.mevis.inventor.misc;

import jscenegraph.opengl.GL2;

import jscenegraph.database.inventor.elements.SoGLLazyElement;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.port.Destroyable;
import jscenegraph.port.IntArray;
import jscenegraph.port.IntArrayPtr;
import jscenegraph.port.IntPtr;
import jscenegraph.port.VoidPtr;


//! SoVertexArrayIndexer is a class that manages rendering of indexed primitives using
//! Vertex Array rendering. The indices can optionally be cached in a VBO, so that they
//! do not need to be send on each render call.
//! This class is mainly needed because the OIV index structures typically contain
//! -1 as marker for the end of each primitive, which is undesired for sending indices to
//! OpenGL. Currently a single indexer only manages the kind of primitives that are passed
//! in the constructor.
//! (MeVis ONLY)
/**
 * @author Yves Boyadjian
 *
 */
public class SoVertexArrayIndexer implements Destroyable {
	
private
  int _type;

  private long _nodeId;

  private boolean     _ownsIndices;
  private int      _numIndices;
  private IntPtr  _indices;
  
  private SoVBO _indexVbo;
	
  public SoVertexArrayIndexer( int type )
  {
    _type = type;
    _nodeId = 0;
    _indexVbo = null;
    _indices = null;
    _numIndices = 0;
    _ownsIndices = false;
  }


	public void destructor() {
  clear();
  if (_indexVbo != null) {
    _indexVbo.destructor();
  }
	}

  //! set the type
  public void setType(int type) { _type = type; }

  //! get the type
  public int getType() { return _type; }

	

public void clear()
{
  if (_indices != null && _ownsIndices) {
    //delete[] _indices; java port
  }
  _indices = null;
  _numIndices = 0;
  _nodeId = 0;
  _ownsIndices = false;
}

public void freeGL(SoState state)
{
  if (_indexVbo != null) {
    _indexVbo.freeGL(state);
  }
}

public void render(SoState state, boolean useVbo)
{
  Object dataPtr = _indices;
  if (useVbo) {
    if (_indexVbo == null) {
      _indexVbo = new SoVBO(GL2.GL_ELEMENT_ARRAY_BUFFER);
    }
    _indexVbo.setData(_numIndices* 4/*sizeof(uint32_t)*/, VoidPtr.create(_indices./*getBuffer*/toByteBuffer()), _nodeId, state);
    if (_indexVbo.bind(state)) {
      // if the VBO could be bound, use it.
      dataPtr = null;
    }
  }
  
  SoGLLazyElement.drawElements(state, _type, _numIndices, GL2.GL_UNSIGNED_INT, dataPtr);

  if (useVbo) {
	  
	  GL2 gl2 = state.getGL2();
    // unbind the VBO
    _indexVbo.unbind(gl2);
  }
}

  //! get the node id of the data
  public long getDataId() { return _nodeId; }



public void setInventorTriangles( int numTriangles, final int[] inventorIndices, long nodeId )
{
  clear();
  _ownsIndices = true;
  _nodeId = nodeId;
  _numIndices = numTriangles*3;
  _indices = new IntPtr(_numIndices);
  IntPtr ptr = new IntPtr(_indices);
  IntArrayPtr oivIndices = new IntArrayPtr(inventorIndices);
  for (int i=0; i < numTriangles; i++) {
    ptr.asterisk(oivIndices.get()); ptr.plusPlus(); oivIndices.plusPlus();
    ptr.asterisk(oivIndices.get()); ptr.plusPlus(); oivIndices.plusPlus();
    ptr.asterisk(oivIndices.get()); ptr.plusPlus(); oivIndices.plusPlus();
    // skip the -1
    oivIndices.plusPlus();
  }
}

public void setInventorQuads( int numQuads, final int[] inventorIndices, long nodeId )
{
  clear();
  _ownsIndices = true;
  _nodeId = nodeId;
  _numIndices = numQuads*4;
  _indices = new IntPtr(_numIndices);
  IntPtr ptr = new IntPtr(_indices);
  IntArrayPtr oivIndices = new IntArrayPtr(inventorIndices);
  for (int i=0; i < numQuads; i++) {
	  ptr.asterisk(oivIndices.get()); ptr.plusPlus(); oivIndices.plusPlus();
	  ptr.asterisk(oivIndices.get()); ptr.plusPlus(); oivIndices.plusPlus();
	  ptr.asterisk(oivIndices.get()); ptr.plusPlus(); oivIndices.plusPlus();
	  ptr.asterisk(oivIndices.get()); ptr.plusPlus(); oivIndices.plusPlus();
    // skip the -1
    oivIndices.plusPlus();
  }
}

public void setInventorLines( int numLines, IntArray inventorIndices, long nodeId )
{
  clear();
  _ownsIndices = true;
  _nodeId = nodeId;
  _numIndices = numLines*2;
  _indices = new IntPtr(_numIndices);
  IntPtr ptr = new IntPtr(_indices);
  IntArrayPtr oivIndices = new IntArrayPtr(inventorIndices);
  for (int i=0; i < numLines; i++) {
	  ptr.asterisk(oivIndices.get()); ptr.plusPlus(); oivIndices.plusPlus();
	  ptr.asterisk(oivIndices.get()); ptr.plusPlus(); oivIndices.plusPlus();
    // skip the -1
    oivIndices.plusPlus();
  }
}

public void setIndices(int numIndices, final int[] indices, long nodeId )
{
  clear();
  _ownsIndices = false;
  _nodeId = nodeId;
  _numIndices = numIndices;
  _indices = new IntPtr(indices);
}
	
}
