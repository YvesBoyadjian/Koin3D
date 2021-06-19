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
  \class SoTextureCoordinate2 SoTextureCoordinate2.h Inventor/nodes/SoTextureCoordinate2.h
  \brief The SoTextureCoordinate2 class contains a set of coordinates for the mapping of 2D textures.

  \ingroup coin_nodes

  When encountering a node of this type during traversal, the
  coordinates it contains will be put on the state stack. Some shape
  nodes (for instance SoIndexedFaceSet, among many others) can then
  use these coordinates for explicit, detailed control of how textures
  are mapped to its surfaces.

  (If texture mapping is used without any SoTextureCoordinate2 nodes in
  the scene graph leading up to a shape node, all shape types have
  default fallbacks. So SoTextureCoordinate2 nodes are only necessary
  to use if you are not satisfied with the default mapping.)

  Note that an SoTextureCoordinate2 node will \e replace the
  coordinates already present in the state (if any).

  Here's a very simple example (in Inventor scene graph file format --
  mapping it to source code is straightforward) that shows how to set
  up two quadratic polygons, one mapped 1:1 to the texture, the other
  using only the upper left quarter of the texture:

\code

Separator {
   Texture2 {
      image 6 8 3
      0x00ff0000 0x00ff0000 0x000000ff 0x000000ff 0x00ff00ff 0x00ff00ff
      0x00ff0000 0x00ff0000 0x000000ff 0x000000ff 0x00ff00ff 0x00ff00ff
      0x00ff0000 0x00ff0000 0x000000ff 0x000000ff 0x00ff00ff 0x00ff00ff
      0x0000ff00 0x0000ff00 0x0000ffff 0x0000ffff 0x0000ff00 0x0000ff00
      0x0000ff00 0x0000ff00 0x0000ffff 0x0000ffff 0x0000ff00 0x0000ff00
      0x00ffff00 0x00ffff00 0x000000ff 0x000000ff 0x00ffffff 0x00ffffff
      0x00ffff00 0x00ffff00 0x000000ff 0x000000ff 0x00ffffff 0x00ffffff
      0x00ffff00 0x00ffff00 0x000000ff 0x000000ff 0x00ffffff 0x00ffffff
   }

   Coordinate3 { point [ -1 -1 0, 1 -1 0, 1 1 0, -1 1 0 ] }

   # "1:1 mapping" to actual texture appearance. (Note that Y goes
   # from bottom to top, versus the common way of specifying bitmap
   # data from top to bottom.)
   TextureCoordinate2 { point [ 0 1, 1 1, 1 0, 0 0 ] }

   IndexedFaceSet {
      coordIndex [ 0, 1, 2, 3, -1 ]
      textureCoordIndex [ 0, 1, 2, 3, -1 ]
   }

   Translation { translation +4 0 0 }

   # Top left corner.
   TextureCoordinate2 { point [ 0 0.5, 0.5 0.5, 0.5 0, 0 0 ] }

   IndexedFaceSet {
      coordIndex [ 0, 1, 2, 3, -1 ]
      textureCoordIndex [ 0, 1, 2, 3, -1 ]
   }
}

\endcode

  <b>FILE FORMAT/DEFAULTS:</b>
  \code
    TextureCoordinate2 {
        point [  ]
    }
  \endcode

  \sa SoTextureCoordinateFunction, SoTextureCoordinateBinding
*/

// *************************************************************************

package jscenegraph.database.inventor.nodes;

import jscenegraph.coin3d.glue.cc_glglue;
import jscenegraph.coin3d.inventor.elements.SoGLMultiTextureCoordinateElement;
import jscenegraph.coin3d.inventor.elements.SoMultiTextureCoordinateElement;
import jscenegraph.coin3d.inventor.elements.SoTextureUnitElement;
import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoPickAction;
import jscenegraph.database.inventor.elements.SoGLCacheContextElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFVec2f;
import jscenegraph.database.inventor.misc.SoBase;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.mevis.inventor.elements.SoGLVBOElement;
import jscenegraph.mevis.inventor.misc.SoVBO;
import jscenegraph.port.VoidPtr;

import static com.jogamp.opengl.GL.GL_ARRAY_BUFFER;
import static com.jogamp.opengl.GL.GL_STATIC_DRAW;
import static jscenegraph.coin3d.misc.SoGL.cc_glglue_instance;
import static jscenegraph.coin3d.misc.SoGL.cc_glglue_max_texture_units;

/**
 * @author Yves Boyadjian
 *
 */

////////////////////////////////////////////////////////////////////////////////
//! 2D texture coordinate node.
/*!
\class SoTextureCoordinate2
\ingroup Nodes
This node defines a set of 2D coordinates to be used to map textures
to subsequent vertex-based shapes (those derived from
SoVertexShape). It replaces the current texture coordinates in the
rendering state for the shapes to use.


The current texture coordinate binding (see
SoTextureCoordinateBinding) determines how texture coordinates are
mapped to vertices of shapes. An alternative to using explicit texture
coordinates is to generate them using a function; see
SoTextureCoordinateFunction.


Texture coordinates range from 0 to 1 across the texture. The
horizontal coordinate, called <tt>S</tt>, is specified first, followed by
the vertical coordinate, <tt>T</tt>.

\par File Format/Default
\par
\code
TextureCoordinate2 {
  point [  ]
}
\endcode

\par Action Behavior
\par
SoGLRenderAction, SoCallbackAction
<BR> Sets the current texture coordinates in the state. 

\par See Also
\par
SoTexture2, SoTextureCoordinateBinding, SoTextureCoordinateFunction, SoVertexShape
*/
////////////////////////////////////////////////////////////////////////////////

// *************************************************************************

/*!
  \var SoMFVec2f SoTextureCoordinate2::point

  The set of 2D texture coordinates. Default value of field is an
  empty set.

  Texture coordinates are usually specified in normalized coordinates,
  i.e. in the range [0, 1]. (0, 0) is the lower left corner, while
  (1, 1) is the upper right corner of the texture image. Coordinates
  outside the [0, 1] range can be used to repeat the texture across a
  surface.

  \sa SoTexure2::wrapS, SoTexture2::wrapT
*/

// *************************************************************************

public class SoTextureCoordinate2 extends SoNode {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoTextureCoordinate2.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoTextureCoordinate2.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoTextureCoordinate2.class); }    
	

	    public
    //! \name Fields
    //@{

    //! Texture coordinate points.
    final SoMFVec2f           point = new SoMFVec2f();          

	    protected final SoVBO[] _vbo = new SoVBO[1];
	  
	    
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoTextureCoordinate2()
//
////////////////////////////////////////////////////////////////////////
{
    nodeHeader.SO_NODE_CONSTRUCTOR(/*SoTextureCoordinate2.class*/);
    nodeHeader.SO_NODE_ADD_MFIELD(point,"point", (new SbVec2f(0,0)));
    point.deleteValues(0);
    isBuiltIn = true;

    _vbo[0] = null;
}


////////////////////////////////////////////////////////////////////////
//
//Description:
//Destructor
//
//Use: private

public void destructor()
//
////////////////////////////////////////////////////////////////////////
{
	if(_vbo[0] != null)
		_vbo[0].destructor();
	super.destructor();
}

	    
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Handles actions.
//
// Use: extender

public void SoTextureCoordinate2_doAction(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoState state = action.getState();
    int unit = SoTextureUnitElement.get(state);

    if (! point.isIgnored() && point.getNum() > 0) {
        SoMultiTextureCoordinateElement.set2(state, this, unit,
                                         point.getNum(), point.getValuesSbVec2fArray(/*0*/));
        if (state.isElementEnabled(SoGLVBOElement.getClassStackIndex(SoGLVBOElement.class))) {
          SoGLVBOElement.updateVBO(state, SoGLVBOElement.VBOType.TEXCOORD_VBO, _vbo,
            point.getNum()*SbVec2f.sizeof(), VoidPtr.create(point.getValuesArray(0)), getNodeId());
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
    SoTextureCoordinate2_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does GL render action.
//
// Use: extender

public void GLRender(SoGLRenderAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoState state = action.getState();
    int unit = SoTextureUnitElement.get(state);


  cc_glglue glue = cc_glglue_instance(SoGLCacheContextElement.get(state));
    int maxunits = cc_glglue_max_texture_units(glue);

    if (unit < maxunits) {
        if (!point.isIgnored() && point.getNum() > 0) {
            SoGLMultiTextureCoordinateElement.setTexGen(state, this, unit, null);
            SoMultiTextureCoordinateElement.set2(state, this, unit,
                    point.getNum(), point.getValuesSbVec2fArray(/*0*/));
        }
    }

    SoBase.staticDataLock();
  final int num = this.point.getNum();
    boolean setvbo = false;
    if (SoGLVBOElement.shouldCreateVBO(state, num)) {
    setvbo = true;
    boolean dirty = false;
    if (_vbo[0] == null) {
        _vbo[0] = new SoVBO(GL_ARRAY_BUFFER, GL_STATIC_DRAW);
        dirty =  true;
    }
    else if (_vbo[0].getBufferDataId() != this.getNodeId()) {
        dirty = true;
    }
    if (dirty) {
        _vbo[0].setBufferData(VoidPtr.create(this.point.getValues(0)),
                num*(SbVec2f.sizeof()),
                this.getNodeId());
    }
}
  else if (_vbo[0] != null && _vbo[0].getBufferDataId() != 0) {
    // clear buffers to deallocate VBO memory
    _vbo[0].setBufferData(null, 0, 0);
}
    SoBase.staticDataUnlock();
    SoGLVBOElement.setTexCoordVBO(state, 0, setvbo ? _vbo[0] : null);
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
    SoTextureCoordinate2_doAction(action);
}

	    
	    
////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoTextureCoordinate2 class.
//
// Use: internal

public static void initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SO__NODE_INIT_CLASS(SoTextureCoordinate2.class, "TextureCoordinate2", SoNode.class);

    SO_ENABLE(SoCallbackAction.class, SoMultiTextureCoordinateElement.class);
    SO_ENABLE(SoGLRenderAction.class, SoGLMultiTextureCoordinateElement.class);
    SO_ENABLE(SoGLRenderAction.class, SoGLVBOElement.class);
    SO_ENABLE(SoPickAction.class,     SoMultiTextureCoordinateElement.class);
}

}
