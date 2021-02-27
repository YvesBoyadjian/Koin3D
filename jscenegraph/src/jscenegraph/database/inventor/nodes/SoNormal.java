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
 |      This file defines the SoNormal node class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import com.jogamp.opengl.GL2;

import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoGetPrimitiveCountAction;
import jscenegraph.database.inventor.actions.SoPickAction;
import jscenegraph.database.inventor.elements.SoGLNormalElement;
import jscenegraph.database.inventor.elements.SoNormalElement;
import jscenegraph.database.inventor.elements.SoOverrideElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFVec3f;
import jscenegraph.database.inventor.misc.SoBase;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.mevis.inventor.elements.SoGLVBOElement;
import jscenegraph.mevis.inventor.misc.SoVBO;
import jscenegraph.port.VoidPtr;

/**
 * @author Yves Boyadjian
 *
 */

////////////////////////////////////////////////////////////////////////////////
//! Node that defines surface normals for shapes.
/*!
\class SoNormal
\ingroup Nodes
This node defines a set of 3D surface normal vectors to be used by
vertex-based shape nodes that follow it in the scene graph. This node
does not produce a visible result during rendering; it simply replaces
the current normals in the rendering state for subsequent nodes to
use.  This node contains one multiple-valued field that contains the
normal vectors.


Surface normals are needed to compute lighting when the Phong lighting
model is used. Most vertex-based shapes that use normals can compute
default normals if none are specified, depending on the current normal
binding.

\par File Format/Default
\par
\code
Normal {
  vector [  ]
}
\endcode

\par Action Behavior
\par
SoGLRenderAction, SoCallbackAction, SoRayPickAction
<BR> Sets the current normals in the traversal state. 

\par See Also
\par
SoCoordinate3, SoLightModel, SoNormalBinding, SoVertexShape
*/
////////////////////////////////////////////////////////////////////////////////

public class SoNormal extends SoNode {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoNormal.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoNormal.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoNormal.class); }    	  	
	
  public
    //! \name Fields
    //@{

    //! Surface normal vectors.
    final SoMFVec3f           vector = new SoMFVec3f();         

  private SoNormalP pimpl; //ptr
  
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoNormal()
//
////////////////////////////////////////////////////////////////////////
{
	pimpl = new SoNormalP();
	
    nodeHeader.SO_NODE_CONSTRUCTOR(/*SoNormal.class*/);
    nodeHeader.SO_NODE_ADD_MFIELD(vector,"vector", (new SbVec3f(0,0,0)));
    vector.deleteValues(0); // Nuke bogus normal
    isBuiltIn = true;
}

  
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Destructor
//
// Use: private

public void destructor()
//
////////////////////////////////////////////////////////////////////////
{
	pimpl.destructor();
  super.destructor();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Handles most actions,
//
// Use: extender

//public void SoNormal_doAction(SoAction action)
////
//////////////////////////////////////////////////////////////////////////
//{
//  if (! vector.isIgnored()) {
//    SoState state = action.getState();
//    SoNormalElement.set(action.getState(), this,
//      vector.getNum(), vector.getValuesSbVec3fArray(/*0*/));
//    if (state.isElementEnabled(SoGLVBOElement.getClassStackIndex(SoGLVBOElement.class))) {
//      SoGLVBOElement.updateVBO(state, SoGLVBOElement.VBOType.NORMAL_VBO, _vbo,
//        vector.getNum()*(SbVec3f.sizeof()), VoidPtr.create(vector.getValuesArray(0)), getNodeId());
//    }
//  }
//}

public void doAction(SoAction action) {
	SoNormal_doAction(action);
}

// Doc in superclass.
public void SoNormal_doAction(SoAction action)
{
  SoState state = action.getState();
  if (!this.vector.isIgnored() &&
      !SoOverrideElement.getNormalVectorOverride(state)) {
    SoNormalElement.set(state, this,
                         this.vector.getNum(), this.vector.getValuesSbVec3fArray(/*0*/));
    if (this.isOverride()) {
      SoOverrideElement.setNormalVectorOverride(state, this, true);
    }
  }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does callback action thing.
//
// Use: extender

public void callback(SoCallbackAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoNormal_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does GL render action.
//
// Use: extender

//public void GLRender(SoGLRenderAction action)
////
//////////////////////////////////////////////////////////////////////////
//{
//    SoNormal_doAction(action);
//}


// Doc in superclass.
public void GLRender(SoGLRenderAction action)
{
  //
  // FIXME: code to test if all normals are unit length, and store
  // this in some cached variable.  should be passed on to
  // SoGLNormalizeElement to optimize rendering (pederb)
  //
  SoNormal_doAction(action);
  SoState state = action.getState();
  
  SoBase.staticDataLock();
  boolean setvbo = false;
  final int num = this.vector.getNum();
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
      pimpl.vbo.setBufferData(VoidPtr.create(this.vector.getValuesArray(0)),
                                        num*(SbVec3f.sizeof()),
                                        this.getNodeId(),state); // YB 
    }
  }
  else if (pimpl.vbo != null && pimpl.vbo.getBufferDataId() != 0) {
    // clear buffers to deallocate VBO memory
    pimpl.vbo.setBufferData(null, 0, 0);
  }
  SoBase.staticDataUnlock();
  SoGLVBOElement.setNormalVBO(state, setvbo? pimpl.vbo : null);
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does pick action...
//
// Use: extender

public void pick(SoPickAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoNormal_doAction(action);
}
  
  
////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoNormal class.
//
// Use: internal

public static void initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SO__NODE_INIT_CLASS(SoNormal.class, "Normal", SoNode.class);

    // Enable elements for appropriate actions:
    //SO_ENABLE(SoGLRenderAction.class, SoGLNormalElement.class);
    SO_ENABLE(SoGLRenderAction.class, SoNormalElement.class);
    //SO_ENABLE(SoGLRenderAction.class, SoGLVBOElement.class);
    SO_ENABLE(SoCallbackAction.class, SoNormalElement.class);
    SO_ENABLE(SoGetPrimitiveCountAction.class, SoNormalElement.class);
    SO_ENABLE(SoPickAction.class,     SoNormalElement.class);
}


}
