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
  \class SoTextureCoordinatePlane SoTextureCoordinatePlane.h Inventor/nodes/SoTextureCoordinatePlane.h
  \brief The SoTextureCoordinatePlane class generates texture coordinates by projecting onto a plane.

  \ingroup coin_nodes

  SoTextureCoordinatePlane is used for generating texture coordinates
  by projecting the object onto a texture plane.  The s, t and r
  texture coordinates are computed as the distance from the origin to
  the projected point, in the respective directions. The texture plane
  is specified using two direction vectors, given as
  SoTextureCoordinatePlane::directionS and
  SoTextureCoordinatePlane::directionT in object space coordinates.
  SoTextureCoordinatePlane::directionR is used for generating the
  third coordinate, and can be used for generating texture coordinate
  for 3D textures. For 2D textures you can just leave this field alone.

  The length of the vector determines the repeat interval of the
  texture per unit length.

  A simple usage example:

  \code
  SoSeparator *root = new SoSeparator;
  root->ref();

  // the texture image
  SoTexture2 *tex = new SoTexture2;
  tex->filename.setValue("foo.png");
  root->addChild(tex);

  // the texture plane
  SoTextureCoordinatePlane *texPlane = new SoTextureCoordinatePlane;
  texPlane->directionS.setValue(SbVec3f(1,0,0));
  texPlane->directionT.setValue(SbVec3f(0,1,0));
  root->addChild(texPlane);

  // add a simple cube
  SoCube * c = new SoCube;
  c->width.setValue(1.0);
  c->height.setValue(1.0)
  c->depth.setValue(1.0);
  root->addChild(new SoCube);
  \endcode

  Here, we are projecting a texture onto a cube. The texture
  coordinate plane is specified by directionS = (1,0,0) and directionT
  = (0,1,0), meaning that it is parallel to the front face of the
  cube. Setting e.g. directionS = (0,1,0) and directionT = (-1,0,0)
  would rotate the texture counterclockwise by 90 degrees. Setting
  them to ((2,0,0), (0,2,0)) results to the texture being repeated twice
  per unit, so the texture appears four times on the 1x1 face.

  Note that when you transform the cube, the transformation will also
  affect the texture - it will be transformed vs. the rest of the
  world, but appear "fixed" on the object. If you want to change the
  placement of the texture on the object, you have to insert a
  SoTexture2Transform node before the texture coordinate plane. For
  instance in the example above, since the cube is centered in its
  coordinate system, the lower left corner of the texture appears to
  be in the middle of the face. To move the texture's origin to
  coincide with the lower left corner of the face, insert

  \code
  SoTexture2Transform * tf = new SoTexture2Transform;
  tf->translation.setValue(-0.5,-0.5);
  root->addChild(tf);
  \endcode

  before adding the texture coordinate plane.

  <b>FILE FORMAT/DEFAULTS:</b>
  \code
    TextureCoordinatePlane {
        directionS 1 0 0
        directionT 0 1 0
    }
  \endcode
*/

// *************************************************************************

package jscenegraph.database.inventor.nodes;

import jscenegraph.opengl.GL2;

import jscenegraph.coin3d.glue.cc_glglue;
import jscenegraph.coin3d.inventor.elements.SoGLMultiTextureCoordinateElement;
import jscenegraph.coin3d.inventor.elements.SoMultiTextureCoordinateElement;
import jscenegraph.coin3d.inventor.elements.SoTextureUnitElement;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec4f;
import jscenegraph.database.inventor.SbVec4fSingle;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoPickAction;
import jscenegraph.database.inventor.elements.SoGLCacheContextElement;
import jscenegraph.database.inventor.elements.SoTextureOverrideElement;
import jscenegraph.database.inventor.elements.SoTextureQualityElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFVec3f;
import jscenegraph.database.inventor.misc.SoState;

import static jscenegraph.coin3d.misc.SoGL.cc_glglue_instance;
import static jscenegraph.coin3d.misc.SoGL.cc_glglue_max_texture_units;

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

/*!
  \var SoSFVec3f SoTextureCoordinatePlane::directionS
  The S texture coordinate plane direction.
  The length of the vector determines the repeat interval of the
  texture per unit length.

*/
/*!
  \var SoSFVec3f SoTextureCoordinatePlane::directionT
  The T texture coordinate plane direction.
  The length of the vector determines the repeat interval of the
  texture per unit length.
*/

/*!
  \var SoSFVec3f SoTextureCoordinatePlane::directionR
  The R texture coordinate plane direction.
  The length of the vector determines the repeat interval of the
  texture per unit length.
*/


// *************************************************************************

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

	  public final SoSFVec3f directionR = new SoSFVec3f();
	  

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
    nodeHeader.SO_NODE_ADD_FIELD(directionR,"directionR", new SbVec3f(0.0f, 0.0f, 1.0f));
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

private static final SbVec4fSingle result = new SbVec4fSingle();

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
    final SbVec3f dr = tc.directionR.getValue();
    result.getValue()[2] = dr.dot(position);
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
    int unit = SoTextureUnitElement.get(state);

    // Special case to workaround OpenGL on Indigo/IndigoII bug:
    if (SoTextureOverrideElement.getQualityOverride(state) &&
        SoTextureQualityElement.get(state) == 0.0) return;


  final cc_glglue glue = cc_glglue_instance(SoGLCacheContextElement.get(state));
    int maxunits = cc_glglue_max_texture_units(glue);
    if (unit < maxunits) {
        SoGLMultiTextureCoordinateElement.setTexGen(state, this, unit,
                (me) -> doTexgen(state, me), this,
                (instance, position, normal) -> valueCallback(instance, position, normal), this);
    }
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

    final SbVec4fSingle t = new SbVec4fSingle();

    final SbVec3f ds = p.directionS.getValue();
    t.setValue(ds.getValueRead()[0], ds.getValueRead()[1], ds.getValueRead()[2], 0.0f);
    gl2.glTexGenf(GL2.GL_S, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_OBJECT_LINEAR);
    gl2.glTexGenfv(GL2.GL_S, GL2.GL_OBJECT_PLANE, t.getValue(),0);
    
    final SbVec3f dt = p.directionT.getValue();
    t.setValue(dt.getValueRead()[0], dt.getValueRead()[1], dt.getValueRead()[2], 0.0f);
    gl2.glTexGenf(GL2.GL_T, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_OBJECT_LINEAR);
    gl2.glTexGenfv(GL2.GL_T, GL2.GL_OBJECT_PLANE, t.getValue(),0);
    
    final SbVec3f dr = p.directionR.getValue();
    t.setValue(dr.getValueRead()[0], dr.getValueRead()[1], dr.getValueRead()[2], 0.0f);
    gl2.glTexGenf(GL2.GL_R, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_OBJECT_LINEAR);
    gl2.glTexGenfv(GL2.GL_R, GL2.GL_OBJECT_PLANE, t.getValue(),0);
    
    t.setValue(0,0,0,1);
    gl2.glTexGeni(GL2.GL_Q, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_OBJECT_LINEAR);
    gl2.glTexGenfv(GL2.GL_Q, GL2.GL_OBJECT_PLANE, t.getValue(),0);    
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
    int unit = SoTextureUnitElement.get(state);

    SoMultiTextureCoordinateElement.setFunction(state, this, unit,
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
