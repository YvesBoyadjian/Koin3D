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
  \class SoVRMLIndexedShape SoVRMLIndexedShape.h Inventor/VRMLnodes/SoVRMLIndexedShape.h
  \brief The SoVRMLIndexedShape class is a superclass for geometry that use indexes.
*/

/*!
  \var SoMFInt32 SoVRMLIndexedShape::coordIndex
  The coordinate index array.
*/

/*!
  \var SoMFInt32 SoVRMLIndexedShape::colorIndex
  The color index array.
*/

/*!
  \var SoMFInt32 SoVRMLIndexedShape::normalIndex
  The normal index array.
*/

/*!
  \var SoMFInt32 SoVRMLIndexedShape::texCoordIndex
  The texture coordinate index array.
*/

package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.caches.SoNormalCache;
import jscenegraph.database.inventor.elements.SoCoordinateElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFInt32;
import jscenegraph.database.inventor.misc.SoNotList;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.port.IntArrayPtr;
import jscenegraph.port.SbVec3fArray;
import jscenegraph.port.Util;

/**
 * @author BOYADJIAN
 *
 */
public abstract class SoVRMLIndexedShape extends SoVRMLVertexShape {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_ABSTRACT_HEADER(SoVRMLIndexedShape.class,this);
   	
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLIndexedShape.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLIndexedShape.class); } 
	  
	
	  public final SoMFInt32 coordIndex = new SoMFInt32();
	  public final SoMFInt32 colorIndex = new SoMFInt32();
	  public final SoMFInt32 normalIndex = new SoMFInt32();
	  public final SoMFInt32 texCoordIndex = new SoMFInt32();

	  /*!
	  Constructor.
	*/
	public SoVRMLIndexedShape()
	{
		nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLIndexedShape.class);

		nodeHeader.SO_VRMLNODE_ADD_EMPTY_MFIELD(coordIndex,"coordIndex");
	  nodeHeader.SO_VRMLNODE_ADD_EMPTY_MFIELD(colorIndex,"colorIndex");
	  nodeHeader.SO_VRMLNODE_ADD_EMPTY_MFIELD(normalIndex,"normalIndex");
	  nodeHeader.SO_VRMLNODE_ADD_EMPTY_MFIELD(texCoordIndex,"texCoordIndex");
	}


/*!
  Convenience method that will fetch data needed for rendering or
  generating primitives. Takes care of normal cache.
*/
public boolean getVertexData(SoState state,
                                  SoCoordinateElement[] coords,
                                  SbVec3fArray[] normals,
                                  IntArrayPtr[] cindices,
                                  IntArrayPtr[] nindices,
                                  IntArrayPtr[] tindices,
                                  IntArrayPtr[] mindices,
                                  final int[]  numcindices,
                                  boolean neednormals,
                                  final boolean[] normalcacheused)
{
  super.getVertexData(state, coords, normals, neednormals);

  cindices[0] = this.coordIndex.getValuesIntArrayPtr(0);
  numcindices[0] = this.coordIndex.getNum();

  mindices[0] = this.colorIndex.getValuesIntArrayPtr(0);
  if (this.colorIndex.getNum() <= 0 || mindices[0].get() < 0) mindices[0] = null;

  tindices[0] = this.texCoordIndex.getValuesIntArrayPtr(0);
  if (this.texCoordIndex.getNum() <= 0 || tindices[0].get() < 0) tindices[0] = null;

  normalcacheused[0] = false;
  nindices[0] = null;

  if (neednormals) {
    nindices[0] = this.normalIndex.getValuesIntArrayPtr(0);
    if (this.normalIndex.getNum() <= 0 || nindices[0].get() < 0) nindices[0] = null;
    
    if (normals[0] == null) {
      SoNormalCache nc = this.generateAndReadLockNormalCache(state);
      normals[0] = nc.getNormals();
      nindices[0] = nc.getIndices();
      normalcacheused[0] = true;
      // if no normals were generated, unlock normal cache before
      // returning
      if (normals[0] == null) {
        this.readUnlockNormalCache();
        normalcacheused[0] = false;
      }
    }
  }
  return true;
}

// Doc in parent
public void notify(SoNotList list)
{
  SoField f = list.getLastField();
  if (f == this.coordIndex) {
    SoNormalCache nc = this.getNormalCache();
    if (nc != null) nc.invalidate();
  }
  super.notify(list);
}

	

	// Doc in parent
	public void computeBBox(SoAction action, final SbBox3f box,
	                                final SbVec3f center)
	{
  SoVRMLCoordinate node = (SoVRMLCoordinate) this.coord.getValue();
  if (node == null) return;

  int numCoords = node.point.getNum();
  SbVec3f[] coords = node.point.getValues(0);

  box.makeEmpty();
  IntArrayPtr ptr = coordIndex.getValuesIntArrayPtr(0); // java port
  IntArrayPtr endptr = ptr.plus(coordIndex.getNum());
  while (ptr.lessThan(endptr)) {
    int idx = ptr.starPlusPlus();

    if (idx >= numCoords) {
      SoDebugError.post("SoVRMLIndexedShape::computeBBox",
                         "index @ "+(ptr.minus(coordIndex.getValuesIntArrayPtr(0))) / Util.sizeof(ptr)+": "+idx+" is out of bounds ["+(numCoords!=0 ? 0 : -1)+", "+(numCoords - 1)+"]");
      continue;
    }

    if (idx >= 0) box.extendBy(coords[idx]);
  }
  if (!box.isEmpty()) center.copyFrom(box.getCenter());
	}

	/*!
	  \copydetails SoNode::initClass(void)
	*/
	public static void initClass()
	{
	  //SO_NODE_INTERNAL_INIT_ABSTRACT_CLASS(SoVRMLIndexedShape, SO_VRML97_NODE_TYPE);
		  SoSubNode.SO__NODE_INIT_ABSTRACT_CLASS(SoVRMLIndexedShape.class, "VRMLIndexedShape", SoVRMLVertexShape.class);
	}

}
