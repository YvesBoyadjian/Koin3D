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
  \class SoTextureCoordinateEnvironment SoTextureCoordinateEnvironment.h Inventor/nodes/SoTextureCoordinateEnvironment.h
  \brief The SoTextureCoordinateEnvironment class generates texture coordinates by projecting onto a surrounding texture.

  \ingroup coin_nodes

  The texture specifying the environment will be mapped around the
  scene graph below this node using a sphere. The texture will be mapped
  onto the scene graph taking camera position into account. This will
  lead to an object reflecting its environment.

  Here is a scene graph example showing how environment mapping can be
  applied to an object:

  \code
  #Inventor V2.1 ascii

  Separator {

    Texture2 {
      filename "ocean.jpg" # the environment, in this case ocean
    }
    TextureCoordinateEnvironment {}

    Cube {} # the environmentally mapped object
  }
  \endcode

  <b>FILE FORMAT/DEFAULTS:</b>
  \code
    TextureCoordinateEnvironment {
    }
  \endcode
*/

// *************************************************************************

// FIXME: Can this somehow relate to 3D textures? (kintel 20020203)


package jscenegraph.database.inventor.nodes;

import jscenegraph.opengl.GL2;

import jscenegraph.coin3d.glue.cc_glglue;
import jscenegraph.coin3d.inventor.elements.SoGLMultiTextureCoordinateElement;
import jscenegraph.coin3d.inventor.elements.SoMultiTextureCoordinateElement;
import jscenegraph.coin3d.inventor.elements.SoTextureUnitElement;
import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec3fSingle;
import jscenegraph.database.inventor.SbVec4f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoPickAction;
import jscenegraph.database.inventor.elements.*;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.misc.SoState;

import static jscenegraph.coin3d.misc.SoGL.cc_glglue_instance;
import static jscenegraph.coin3d.misc.SoGL.cc_glglue_max_texture_units;

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
    int unit = SoTextureUnitElement.get(state);

    // Special case to workaround OpenGL on Indigo/IndigoII bug:
    if (SoTextureOverrideElement.getQualityOverride(state) &&
        SoTextureQualityElement.get(state) == 0.0) return;

  final cc_glglue glue = cc_glglue_instance(SoGLCacheContextElement.get(state));
    int maxunits = cc_glglue_max_texture_units(glue);
    if (unit < maxunits) {
        // Let the state know that the GL is generating texture
        // coordinates.
        SoGLMultiTextureCoordinateElement.setTexGen(state, this,
                unit,
                (obj) -> doTexgen(state, obj), this,
                (userdata, point, normal) -> valueCallback(userdata, point, normal), action);
    }
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
    int unit = SoTextureUnitElement.get(state);

    SoMultiTextureCoordinateElement.setFunction(state, this, unit,
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
