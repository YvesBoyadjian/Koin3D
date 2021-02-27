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
  \class SoVRMLVertexShape SoVRMLVertexShape.h Inventor/VRMLnodes/SoVRMLVertexShape.h
  \brief The SoVRMLVertexShape class is a superclass for vertex based shapes.
*/

/*!
  \var SoSFNode SoVRMLVertexShape::coord
  Should contain an SoVRMLCoordinate node.
*/

/*!
  \var SoSFNode SoVRMLVertexShape::texCoord
  Can contain an SoVRMLTextureCoordinate node.
*/

/*!
  \var SoSFNode SoVRMLVertexShape::normal
  Can contain an SoVRMLNormal node.
*/

/*!
  \var SoSFNode SoVRMLVertexShape::color
  Can contain an SoVRMLColor node.
*/

/*!
  \var SoSFBool SoVRMLVertexShape::colorPerVertex
  When TRUE, colors are applied per vertex. Default value is TRUE.
*/

/*!
  \var SoSFBool SoVRMLVertexShape::normalPerVertex
  When TRUE, normals are applied per vertex. Default value is TRUE.
*/

package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoGetBoundingBoxAction;
import jscenegraph.database.inventor.actions.SoPickAction;
import jscenegraph.database.inventor.bundles.SoNormalBundle;
import jscenegraph.database.inventor.caches.SoNormalCache;
import jscenegraph.database.inventor.elements.SoCacheElement;
import jscenegraph.database.inventor.elements.SoCoordinateElement;
import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFBool;
import jscenegraph.database.inventor.fields.SoSFNode;
import jscenegraph.database.inventor.misc.SoNotList;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoGroup;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.port.Destroyable;
import jscenegraph.port.SbVec3fArray;

/**
 * @author BOYADJIAN
 *
 */
public abstract class SoVRMLVertexShape extends SoVRMLGeometry {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_ABSTRACT_HEADER(SoVRMLVertexShape.class,this);
   	
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLVertexShape.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLVertexShape.class); } 
	  

	  public final SoSFNode coord = new SoSFNode();
	  public final SoSFNode texCoord = new SoSFNode();
	  public final SoSFNode normal = new SoSFNode();
	  public final SoSFNode color = new SoSFNode();
	  public final SoSFBool colorPerVertex = new SoSFBool();
	  public final SoSFBool normalPerVertex = new SoSFBool();
	  
	  private SoVRMLVertexShapeP pimpl;

	  /*!
	  Constructor.
	*/
	public SoVRMLVertexShape()
	{
	  pimpl = new SoVRMLVertexShapeP();
	  pimpl.normalcache = null;

	  nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLVertexShape.class);

	  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(coord,"coord", (null));
	  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(texCoord,"texCoord", (null));
	  nodeHeader.SO_VRMLNODE_ADD_EXPOSED_FIELD(normal,"normal", (null));
	  nodeHeader.SO_VRMLNODE_ADD_FIELD(color,"color", (null));
	  nodeHeader.SO_VRMLNODE_ADD_FIELD(colorPerVertex,"colorPerVertex", (true));
	  nodeHeader.SO_VRMLNODE_ADD_FIELD(normalPerVertex,"normalPerVertex", (true));
	}

/*!
  Destructor.
*/
public void destructor()
{
  if (pimpl.normalcache != null) pimpl.normalcache.unref();
  Destroyable.delete(pimpl);
  super.destructor();
}

// This documentation block has a copy in shapenodes/VertexShape.cpp.
/*!
  \COININTERNAL

  Subclasses should override this method to generate default normals
  using the SoNormalBundle class. \c TRUE should be returned if
  normals were generated, \c FALSE otherwise.

  Default method returns \c FALSE.

  \COIN_FUNCTION_EXTENSION
*/
public boolean generateDefaultNormals(SoState state ,
                                          SoNormalBundle nb)
{
  return false;
}


// This documentation block has a copy in shapenodes/VertexShape.cpp.
/*!
  \COININTERNAL

  Subclasses should override this method to generate default normals
  using the SoNormalCache class. This is more effective than using
  SoNormalGenerator. Return \c TRUE if normals were generated, \c
  FALSE otherwise.

  Default method just returns \c FALSE.

  \COIN_FUNCTION_EXTENSION
*/
public boolean generateDefaultNormals(SoState state,
                                          SoNormalCache nc)
{
  return false;
}

public void
SoVRMLVertexShape_doAction(SoAction action)
{
  SoNode node;

  node = this.coord.getValue();
  if (node != null) node.doAction(action);

  node = this.texCoord.getValue();
  if (node != null) node.doAction(action);

  node = this.normal.getValue();
  if (node != null) node.doAction(action);

  node = this.color.getValue();
  if (node != null) node.doAction(action);
}

public void GLRender(SoGLRenderAction action) {
	SoVRMLVertexShape_GLRender(action);
}

public void SoVRMLVertexShape_GLRender(SoGLRenderAction action) {
  SoNode node;

  node = this.coord.getValue();
  if (node != null) node.GLRender(action);

  node = this.texCoord.getValue();
  if (node != null) node.GLRender(action);

  node = this.normal.getValue();
  if (node != null) node.GLRender(action);

  node = this.color.getValue();
  if (node != null) node.GLRender(action);
}

public void getBoundingBox(SoGetBoundingBoxAction action)
{
  super.getBoundingBox(action);
}

public void callback(SoCallbackAction action)
{
  super.callback(action);
}

public void pick(SoPickAction action)
{
  super.pick(action);
}

public void notify(SoNotList list)
{
  SoField f = list.getLastField();
  
  if (f == this.coord) {
    this.readLockNormalCache();
    if (pimpl.normalcache != null) {
      pimpl.normalcache.invalidate();
    }
    this.readUnlockNormalCache();
  }
  super.notify(list);
}

public boolean shouldGLRender(SoGLRenderAction action)
{
  return SoShape_shouldGLRender(action);
}

public void setNormalCache(SoState state,
                                  int num,
                                  SbVec3fArray normals)
{
  this.writeLockNormalCache();
  if (pimpl.normalcache != null) pimpl.normalcache.unref();
  // create new normal cache with no dependencies
  state.push();
  pimpl.normalcache = new SoNormalCache(state);
  pimpl.normalcache.ref();
  pimpl.normalcache.set(num, normals);
  // force element dependencies
  SoCoordinateElement.getInstance(state);
  state.pop();
  this.writeUnlockNormalCache();
}

/*!  

  Convenience method that can be used by subclasses to return or
  create a normal cache. If the current cache is not valid, it takes
  care of unrefing the old cache and pushing and popping the state to
  create element dependencies when creating the new cache.

  When returning from this method, the normal cache will be
  read locked, and the caller should call readUnlockNormalCache()
  when the normals in the cache is no longer needed.

  \COIN_FUNCTION_EXTENSION

  \since Coin 2.0
*/
public SoNormalCache generateAndReadLockNormalCache(SoState state)
{
  this.readLockNormalCache();
  if (pimpl.normalcache != null && pimpl.normalcache.isValid(state)) {
    return pimpl.normalcache;
  }
  this.readUnlockNormalCache();
  this.writeLockNormalCache();
  
  boolean storeinvalid = SoCacheElement.setInvalid(false);
  
  if (pimpl.normalcache != null) pimpl.normalcache.unref();
  state.push(); // need to push for cache dependencies
  pimpl.normalcache = new SoNormalCache(state);
  pimpl.normalcache.ref();
  SoCacheElement.set(state, pimpl.normalcache);
  //
  // See if the node supports the Coin-way of generating normals
  //
  if (!generateDefaultNormals(state, pimpl.normalcache)) {
    // FIXME: implement SoNormalBundle
    if (generateDefaultNormals(state, (SoNormalBundle)null)) {
      // FIXME: set generator in normal cache
    }
  }
  state.pop(); // don't forget this pop
  
  SoCacheElement.setInvalid(storeinvalid);
  this.writeUnlockNormalCache();
  this.readLockNormalCache();
  return pimpl.normalcache;
}

public SoNormalCache getNormalCache()
{
  return pimpl.normalcache;
}

/*!
  Convenience method that returns the current coordinate and normal
  element. This method is not part of the OIV API.
*/
public void getVertexData(SoState state,
                                 final SoCoordinateElement[] coords,
                                 final SbVec3fArray[] normals,
                                 boolean neednormals)
{
  coords[0] = SoCoordinateElement.getInstance(state);
  assert(coords[0] != null);

  normals[0] = null;
  if (neednormals) {
    SoVRMLNormal node = (SoVRMLNormal) this.normal.getValue();
    normals[0] = (node != null && node.vector.getNum() != 0) ? node.vector.getValuesSbVec3fArray(/*0*/) : null;
  }
}

/*!

  Read lock the normal cache. This method should be called before
  fetching the normal cache (using getNormalCache()). When the cached
  normals are no longer needed, readUnlockNormalCache() must be called.
  
  It is also possible to use generateAndReadLockNormalCache().

  \COIN_FUNCTION_EXTENSION

  \sa readUnlockNormalCache()
  \since Coin 2.0
*/
public void readLockNormalCache()
{
//#ifdef COIN_THREADSAFE
  pimpl.normalcachemutex.readLock();
//#endif // COIN_THREADSAFE
}

/*!
  Read unlock the normal cache. Should be called when the read-locked
  cached normals are no longer needed.

  \COIN_FUNCTION_EXTENSION

  \sa readLockNormalCache()
  \since Coin 2.0
*/
public void readUnlockNormalCache()
{
//#ifdef COIN_THREADSAFE
  pimpl.normalcachemutex.readUnlock();
//#endif // COIN_THREADSAFE
}

// write lock normal cache
public void writeLockNormalCache()
{
//#ifdef COIN_THREADSAFE
  pimpl.normalcachemutex.writeLock();
//#endif // COIN_THREADSAFE
}

// write unlock normal cache
public void writeUnlockNormalCache()
{
//#ifdef COIN_THREADSAFE
  pimpl.normalcachemutex.writeUnlock();
//#endif // COIN_THREADSAFE
}

	/*!
	  \copydetails SoNode::initClass(void)
	*/
	public static void initClass()
	{
	  //SO_NODE_INTERNAL_INIT_ABSTRACT_CLASS(SoVRMLVertexShape, SO_VRML97_NODE_TYPE);
		  SoSubNode.SO__NODE_INIT_ABSTRACT_CLASS(SoVRMLVertexShape.class, "VRMLVertexShape", SoVRMLGeometry.class);
	}

}
