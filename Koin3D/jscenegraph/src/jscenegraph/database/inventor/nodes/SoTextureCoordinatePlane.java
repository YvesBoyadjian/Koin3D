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
 |      The TextureCoordinatePlane.  This texture coordinate function
 |      maps object space coordinates (xyz) to texture space (st)
 |      coordinates by projecting the object space coordinates onto
 |      a plane.
 |
 |   Author(s)          : Thad Beier, Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import com.jogamp.opengl.GL2;

import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec4f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoPickAction;
import jscenegraph.database.inventor.elements.SoGLTextureCoordinateElement;
import jscenegraph.database.inventor.elements.SoTextureCoordinateElement;
import jscenegraph.database.inventor.elements.SoTextureOverrideElement;
import jscenegraph.database.inventor.elements.SoTextureQualityElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFVec3f;
import jscenegraph.database.inventor.misc.SoState;

/**
 * @author Yves Boyadjian
 *
 */


////////////////////////////////////////////////////////////////////////////////
//! Node that specifies texture coordinates by projection from a plane.
/*!
\class SoTextureCoordinatePlane
\ingroup Nodes
This node creates texture coordinates for points on an object's
surface by projecting them onto a plane.
The \b directionS  and \b directionT  fields define the plane.
The S coordinate is computed as the distance from the object-space
origin along the vector specified in the \b directionS  field. 
The T coordinate is computed similarly,
using the \b directionT  field.


The length of the direction vector is also taken into account.
For example, assume \b directionS  is (0.5, 0, 0) and \b directionT  is
(0, 1, 0).  The square defined by the (x, y, z) vertices:
\code
(-1, -1, 0) (1, -1, 0) (1, 1, 0) (-1, 1, 0) 
\endcode
will be assigned the (s, t) texture coordinates:
\code
(-2, -1) (2, -1) (2, 1) (-2, 1)
\endcode

\par File Format/Default
\par
\code
TextureCoordinatePlane {
  directionS 1 0 0
  directionT 0 1 0
}
\endcode

\par Action Behavior
\par
SoGLRenderAction, SoCallbackAction, SoRayPickAction
<BR> Sets the current texture function in the state. 

\par See Also
\par
SoTexture2, SoTexture2Transform, SoTextureCoordinateDefault, SoTextureCoordinateEnvironment
*/
////////////////////////////////////////////////////////////////////////////////

public class SoTextureCoordinatePlane extends SoTextureCoordinateFunction {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoTextureCoordinatePlane.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoTextureCoordinatePlane.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoTextureCoordinatePlane.class); }    	  	
	

		    //! \name Fields
		    //@{
		    //! S coordinates projection direction
	  public	final	    SoSFVec3f           directionS = new SoSFVec3f(); 
		    //! T coordinates projection direction
	  public	final	    SoSFVec3f           directionT = new SoSFVec3f(); 

	  

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoTextureCoordinatePlane()
//
////////////////////////////////////////////////////////////////////////
{
    nodeHeader.SO_NODE_CONSTRUCTOR(/*SoTextureCoordinatePlane.class*/);
    nodeHeader.SO_NODE_ADD_FIELD(directionS,"directionS",   (new SbVec3f(1, 0, 0)));
    nodeHeader.SO_NODE_ADD_FIELD(directionT,"directionT",   (new SbVec3f(0, 1, 0)));
    isBuiltIn = true;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Destructor
//
// Use: public

public void destructor()
//
////////////////////////////////////////////////////////////////////////
{
	super.destructor();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Calculates texture coordinate
//
// Use: public

private static final SbVec4f result = new SbVec4f();

public SbVec4f  valueCallback( Object instance,
    final SbVec3f position,
    final SbVec3f normal /* not used */)
//
////////////////////////////////////////////////////////////////////////
{
    SoTextureCoordinatePlane tc = 
        (SoTextureCoordinatePlane )instance;

    final SbVec3f ds = tc.directionS.getValue();
    result.getValue()[0] = ds.dot(position);
    final SbVec3f dt = tc.directionT.getValue();
    result.getValue()[1] = dt.dot(position);
    result.getValue()[2] = 0.0f;
    result.getValue()[3] = 1.0f;

    return result;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does GL render action.
//
// Use: protected

public void GLRender(SoGLRenderAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoState state = action.getState();

    // Special case to workaround OpenGL on Indigo/IndigoII bug:
    if (SoTextureOverrideElement.getQualityOverride(state) &&
        SoTextureQualityElement.get(state) == 0.0) return;

    SoGLTextureCoordinateElement.setTexGen(state, this, 
                                            (me)->doTexgen(state,me), this,
                                            (instance, position, normal)->valueCallback( instance, position, normal), this);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Callback registered with the TextureCoordinateElement that keep
//    the GL up-to-date as the state is pushed/popped.
//
// Use: private, static

public void doTexgen(SoState state, Object me)
//
////////////////////////////////////////////////////////////////////////
{
    final SoTextureCoordinatePlane p =
        (SoTextureCoordinatePlane )me;
    
    GL2 gl2 = state.getGL2();

    final SbVec4f t = new SbVec4f();

    final SbVec3f ds = p.directionS.getValue();
    t.setValue(ds.getValue()[0], ds.getValue()[1], ds.getValue()[2], 0.0f);
    gl2.glTexGenf(GL2.GL_S, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_OBJECT_LINEAR);
    gl2.glTexGenfv(GL2.GL_S, GL2.GL_OBJECT_PLANE, t.getValue(),0);
    
    final SbVec3f dt = p.directionT.getValue();
    t.setValue(dt.getValue()[0], dt.getValue()[1], dt.getValue()[2], 0.0f);
    gl2.glTexGenf(GL2.GL_T, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_OBJECT_LINEAR);
    gl2.glTexGenfv(GL2.GL_T, GL2.GL_OBJECT_PLANE, t.getValue(),0);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does callback action thing.
//
// Use: protected

public void callback(SoCallbackAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoTextureCoordinatePlane_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does pick action...
//
// Use: protected

public void pick(SoPickAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoTextureCoordinatePlane_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    doAction.  Add this node to the state
//
// Use: Extender public

public void
SoTextureCoordinatePlane_doAction(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoState state = action.getState();

    SoTextureCoordinateElement.setFunction(state, this,
                                            (instance,position,normal)-> valueCallback(instance,position,normal), this);
}
	  
////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoTextureCoordinatePlane class.
//
// Use: internal

public static void initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SO__NODE_INIT_CLASS(SoTextureCoordinatePlane.class,
                      "TextureCoordinatePlane", SoTextureCoordinateFunction.class);

    // Elements are enabled by SoTextureCoordinate nodes.
}

}
