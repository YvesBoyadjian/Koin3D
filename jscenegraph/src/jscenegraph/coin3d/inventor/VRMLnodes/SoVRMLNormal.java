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
  \class SoVRMLNormal SoVRMLNormal.h Inventor/VRMLnodes/SoVRMLNormal.h
  \brief The SoVRMLNormal class is used to bind normals to geometry.

  \ingroup VRMLnodes

  \WEB3DCOPYRIGHT

  \verbatim
  Normal {
    exposedField MFVec3f vector  []   # (-,)
  }
  \endverbatim

  This node defines a set of 3D surface normal vectors to be used in
  the vector field of some geometry nodes (e.g., SoVRMLIndexedFaceSet
  and SoVRMLElevationGrid). This node contains one multiple-valued
  field that contains the normal vectors. Normals shall be of unit
  length.
*/

/*!
  \var SoMFVec3f SoVRMLNormal::vector
  The normal vectors. Empty by default.
*/

package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.opengl.GL2;

import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoGetPrimitiveCountAction;
import jscenegraph.database.inventor.actions.SoPickAction;
import jscenegraph.database.inventor.elements.SoNormalElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFVec3f;
import jscenegraph.database.inventor.misc.SoBase;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.mevis.inventor.elements.SoGLVBOElement;
import jscenegraph.mevis.inventor.misc.SoVBO;
import jscenegraph.port.Destroyable;
import jscenegraph.port.VoidPtr;

/**
 * @author Yves Boyadjian
 *
 */
public class SoVRMLNormal extends SoNode {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVRMLNormal.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLNormal.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLNormal.class); }
	  


	public final SoMFVec3f vector = new SoMFVec3f();
	
	 private
		  SoVRMLNormalP pimpl;
	

/*!
  Constructor.
*/
public SoVRMLNormal()
{
  pimpl = new SoVRMLNormalP();
  nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLNormal.class);

  nodeHeader.SO_VRMLNODE_ADD_EMPTY_EXPOSED_MFIELD(vector,"vector");
}

/*!
  Destructor.
*/
public void destructor()
{
  Destroyable.delete(pimpl);
}

// Doc in parent
public void
SoVRMLNormal_doAction(SoAction action)
{
  SoNormalElement.set(action.getState(), this,
                       this.vector.getNum(), this.vector.getValuesSbVec3fArray(/*0*/));
}

// Doc in parent
public void GLRender(SoGLRenderAction action)
{
  //
  // FIXME: code to test if all normals are unit length, and store
  // this in some cached variable.  should be passed on to
  // SoGLNormalizeElement to optimize rendering (pederb)
  //
  SoVRMLNormal_doAction((SoAction) action);
  SoState state = action.getState();
  
  SoBase.staticDataLock();
  boolean setvbo = false;
  int num = this.vector.getNum();
  if (SoGLVBOElement.shouldCreateVBO(state, num)) {
    setvbo = true;
    boolean dirty = false;
    if (pimpl.vbo == null) {
      pimpl.vbo = new SoVBO(GL2.GL_ARRAY_BUFFER, GL2.GL_STATIC_DRAW); 
      dirty =  true;
    }
    else if (pimpl.vbo.getBufferDataId() != this.getNodeId()) {
      dirty = true;
    }
    if (dirty) {
      pimpl.vbo.setBufferData(VoidPtr.create(this.vector.getValuesSbVec3fArray(/*0*/)),
                                        num*SbVec3f.sizeof(),
                                        this.getNodeId(),state); // YB java port
    }
  }
  else if (pimpl.vbo != null && pimpl.vbo.getBufferDataId() != 0) {
    // clear buffers to deallocate VBO memory
	  pimpl.vbo.setBufferData(null, 0, 0);
  }
  SoBase.staticDataUnlock();
  if (setvbo) {
    SoGLVBOElement.setNormalVBO(state, pimpl.vbo);
  }

}

// Doc in parent
public void callback(SoCallbackAction action)
{
  SoVRMLNormal_doAction((SoAction) action);
}

// Doc in parent
public void pick(SoPickAction action)
{
  SoVRMLNormal_doAction((SoAction) action);
}

// Doc in parent
public void getPrimitiveCount(SoGetPrimitiveCountAction action)
{
  SoVRMLNormal_doAction((SoAction) action);
}
	 
	 
	/*!
	  \copydetails SoNode::initClass(void)
	*/
public static void initClass()
	{
	  //SO_NODE_INTERNAL_INIT_CLASS(SoVRMLNormal, SO_VRML97_NODE_TYPE);
    SoSubNode.SO__NODE_INIT_CLASS(SoVRMLNormal.class, "VRMLNormal", SoNode.class);	 
	}

}
