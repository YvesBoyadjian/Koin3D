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
 |      TextureCoordinateEnvironment.  This texture coordinate function
 |      maps object normals to texture space (st).
 |
 |   Author(s)          : Thad Beier, Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import com.jogamp.opengl.GL2;

import jscenegraph.coin3d.inventor.elements.SoGLMultiTextureCoordinateElement;
import jscenegraph.coin3d.inventor.elements.SoMultiTextureCoordinateElement;
import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec3fSingle;
import jscenegraph.database.inventor.SbVec4f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoPickAction;
import jscenegraph.database.inventor.elements.SoModelMatrixElement;
import jscenegraph.database.inventor.elements.SoTextureOverrideElement;
import jscenegraph.database.inventor.elements.SoTextureQualityElement;
import jscenegraph.database.inventor.elements.SoViewingMatrixElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.misc.SoState;

/**
 * @author Yves Boyadjian
 *
 */


////////////////////////////////////////////////////////////////////////////////
//! Node that specifies texture coordinates by projection from a environment.
/*!
\class SoTextureCoordinateEnvironment
\ingroup Nodes
This node creates texture coordinates by projecting points on an
object's surface to the interior of a surrounding sphere, along the
reflection across the surface normal of the vector from the camera
point to the surface. If the current texture image represents a
spherical reflection map of the current surrounding environment,
subsequent shapes will appear to reflect that environment.

\par File Format/Default
\par
\code
TextureCoordinateEnvironment {
}
\endcode

\par Action Behavior
\par
SoGLRenderAction, SoCallbackAction, SoRayPickAction
<BR> Sets the current texture function in the state. 

\par See Also
\par
SoTexture2, SoTexture2Transform, SoTextureCoordinateDefault, SoTextureCoordinatePlane
*/
////////////////////////////////////////////////////////////////////////////////

public class SoTextureCoordinateEnvironment extends SoTextureCoordinateFunction {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoTextureCoordinateEnvironment.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoTextureCoordinateEnvironment.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoTextureCoordinateEnvironment.class); }    	  	
	


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoTextureCoordinateEnvironment()
//
////////////////////////////////////////////////////////////////////////
{
    nodeHeader.SO_NODE_CONSTRUCTOR(/*SoTextureCoordinateEnvironment.class*/);
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

public SbVec4f  valueCallback(Object action,
    final SbVec3f point,
    final SbVec3f normal)
//
////////////////////////////////////////////////////////////////////////
{
    SoAction a = (SoAction )action;

    //
    // See the glTexGen() man page for the math here.
    //

    // First, map normal and point into eye space:
    final SbMatrix mm = SoModelMatrixElement.get(a.getState());
    final SbMatrix vm = SoViewingMatrixElement.get(a.getState());

    // Compute the matrix that transforms normals from object-space to
    // eye-space; use the inverse transpose to scale correctly
    final SbVec3f normalE = new SbVec3f();
    final SbMatrix nm = (vm.operator_mul(mm)).inverse().transpose();
    nm.multDirMatrix(normal, normalE);

    final SbVec3f pointE = new SbVec3f();
    mm.multVecMatrix(point, pointE);  // Gives world-space point
    vm.multVecMatrix(pointE, pointE); // ... to eye-space.

    // Get the normalized vector from the eye (which is conveniently
    // at 0,0,0 in eye space) to the point.
    pointE.normalize();

    // Now, figure out reflection vector, from formula:
    // R = P - 2 (N . N) pointE
    SbVec3fSingle reflection = new SbVec3fSingle(pointE.operator_minus( pointE.operator_mul(2.0f * normalE.dot(normalE))));

    // Finally, compute s/t coordinates...
    reflection.getValue()[2] += 1.0;
    float magnitude = reflection.length();

    // This is static so we can return a reference to it
    result.setValue(reflection.getValueRead()[0] / magnitude + 0.5f,
                    reflection.getValueRead()[1] / magnitude + 0.5f,
                    0.0f, 1.0f);

    return result;
}
private final static SbVec4f result = new SbVec4f();

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

    // Let the state know that the GL is generating texture
    // coordinates.
    SoGLMultiTextureCoordinateElement.setTexGen(state, this,
                                            (obj)-> doTexgen(state,obj), this,
                                            (userdata, point, normal)->valueCallback(userdata,point,normal), action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Callback registered with the TextureCoordinateElement that keep
//    the GL up-to-date as the state is pushed/popped.
//
// Use: private, static

private void doTexgen(SoState state, Object obj)
//
////////////////////////////////////////////////////////////////////////
{
	GL2 gl2 = state.getGL2();
	
    gl2.glTexGenf(GL2.GL_S, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_SPHERE_MAP);
    gl2.glTexGenf(GL2.GL_T, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_SPHERE_MAP);
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
    SoTextureCoordinateEnvironment_doAction(action);
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
    SoTextureCoordinateEnvironment_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    doAction.  Add this node to the state
//
// Use: Extender public

public void
SoTextureCoordinateEnvironment_doAction(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoState state = action.getState();

    SoMultiTextureCoordinateElement.setFunction(state, this,
    		(userdata, point, normal)->valueCallback(userdata,point,normal), action);
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoTextureCoordinateEnvironment class.
//
// Use: internal

public static void initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SO__NODE_INIT_CLASS(SoTextureCoordinateEnvironment.class,
                  "TextureCoordinateEnvironment", SoTextureCoordinateFunction.class);

    // Elements are enabled by SoTextureCoordinate nodes.
}

}
