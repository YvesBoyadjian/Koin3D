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
 |      This file defines the SoVertexShape node class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import static com.jogamp.opengl.GL.GL_ARRAY_BUFFER;
import static com.jogamp.opengl.GL.GL_FLOAT;
import static com.jogamp.opengl.GL.GL_UNSIGNED_BYTE;
import static com.jogamp.opengl.fixedfunc.GLPointerFunc.GL_COLOR_ARRAY;
import static com.jogamp.opengl.fixedfunc.GLPointerFunc.GL_NORMAL_ARRAY;
import static com.jogamp.opengl.fixedfunc.GLPointerFunc.GL_TEXTURE_COORD_ARRAY;
import static com.jogamp.opengl.fixedfunc.GLPointerFunc.GL_VERTEX_ARRAY;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.jogamp.opengl.GL2;

import jscenegraph.database.inventor.SbColor;
import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoDebug;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.bundles.SoNormalBundle;
import jscenegraph.database.inventor.caches.SoNormalCache;
import jscenegraph.database.inventor.elements.SoComplexityTypeElement;
import jscenegraph.database.inventor.elements.SoCoordinateElement;
import jscenegraph.database.inventor.elements.SoCreaseAngleElement;
import jscenegraph.database.inventor.elements.SoDrawStyleElement;
import jscenegraph.database.inventor.elements.SoGLLazyElement;
import jscenegraph.database.inventor.elements.SoLazyElement;
import jscenegraph.database.inventor.elements.SoMaterialBindingElement;
import jscenegraph.database.inventor.elements.SoNormalBindingElement;
import jscenegraph.database.inventor.elements.SoNormalElement;
import jscenegraph.database.inventor.elements.SoShapeHintsElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.errors.SoError;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFNode;
import jscenegraph.database.inventor.misc.SoNotList;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.mevis.inventor.elements.SoGLVBOElement;
import jscenegraph.mevis.inventor.misc.SoVBO;
import jscenegraph.port.SbVec3fArray;


////////////////////////////////////////////////////////////////////////////////
//! Abstract base class for all vertex-based shape nodes.
/*!
\class SoVertexShape
\ingroup Nodes
This node is the abstract base class for all vertex-based shape
(geometry) nodes. It is used as a
repository for convenience functions for subclasses and to provide a
type identifier to make it easy to determine whether a shape is
vertex-based.
It contains one public field, the SoVertexProperty
field. 


All subclasses of this node draw objects constructed from
vertices.  If the vertexProperty field is non-null and there are
coordinates in the associated vertex property node, then those
coordinates are used.  Otherwise the objects are drawn using the 
current coordinates in the state. The coordinates of the shape
are transformed by the current transformation matrix and are drawn
with the current light model and drawing style.


Subclasses that construct polygons from vertices may not render or
pick correctly if any of their polygons are self-intersecting or
non-planar.


All vertex shape subclasses use the bounding box of the shape to
determine default texture coordinates. The longest dimension of the
bounding box defines the S coordinates, and the next longest defines
the T coordinates. The value of the S coordinate ranges from 0 to 1,
from one end of the bounding box to the other. The T coordinate ranges
between 0 and the ratio of the second greatest dimension of the
bounding box to the greatest dimension.


When a vertex-based shape is picked with an SoRayPickAction, a
detail is always returned. If the shape is composed of faces (such as
SoFaceSet or SoTriangleStripSet), an SoFaceDetail is
returned. If the shape is composed of line segments (such as
SoLineSet), an SoLineDetail is returned. If the shape is
composed of points (such as SoPointSet), an SoPointDetail is
returned. Note that the type of detail returned is not affected by the
current drawing style.


Similarly, each class of vertex-based shape invokes appropriate
callbacks if those callbacks are registered with the
SoCallbackAction. Shapes made of faces invoke triangle callbacks
for each generated triangle. (Faces may be triangulated to create
these triangles.) Shapes made of line segments invoke line segment
callbacks for each segment, and shapes made of points invoke point
callbacks.


The subclass SoIndexedShape is a base class for vertex-based
shapes that index into the current set of coordinates.
The subclass SoNonIndexedShape is a base class for vertex-based
shapes that use the current coordinates in order.

\par See Also
\par
SoIndexedShape, SoNonIndexedShape, SoVertexProperty
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public abstract class SoVertexShape extends SoShape {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_ABSTRACT_HEADER(SoVertexShape.class,this);
   	
	public                                                                     
    static SoType       getClassTypeId()        /* Returns class type id */   
                                    { return SoSubNode.getClassTypeId(SoVertexShape.class); }                   
    public SoType      getTypeId()       /* Returns type id      */
    {
		return nodeHeader.getClassTypeId();		    	
    }
  public                                                                  
    SoFieldData   getFieldData() {
	  return nodeHeader.getFieldData(); 
  }
  public  static SoFieldData[] getFieldDataPtr()                              
        { return SoSubNode.getFieldDataPtr(SoVertexShape.class); }              
	
	

	 public final SoSFNode vertexProperty = new SoSFNode();
	 
    //! vpCache stores information from one render to the next about
    //! what information needs to be grabbed from the state, etc.
    protected final SoVertexPropertyCache       vpCache = new SoVertexPropertyCache();

	 
	 
  private
    //! This allows instances to cache normals that have been generated
    SoNormalCache       normalCache;

  public
    //! callback used for pre/post vertex array rendering on SoVertexShapes (MeVis ONLY)
    interface VertexArrayRenderingCB {
	  void run(SoVertexShape shape, SoGLRenderAction action, boolean useVbo, int numVertices);
  }
  
  
    private static VertexArrayRenderingCB _preVertexArrayRenderingCB;
    private static VertexArrayRenderingCB _postVertexArrayRenderingCB;
  

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: protected

public SoVertexShape()
//
////////////////////////////////////////////////////////////////////////
{
    nodeHeader.SO_NODE_CONSTRUCTOR(/*SoVertexShape*/);
    nodeHeader.SO_NODE_ADD_SFIELD(vertexProperty,"vertexProperty", (null));

    normalCache = null;
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
    if (normalCache != null)
        normalCache.destructor();
    
    super.destructor();
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Subclasses may define this method to generate normals to use
//    when the normal binding is DEFAULT and there aren't enough
//    normals in the state. This should use the given SoNormalBundle
//    to generate the normals. Returns TRUE if normals were generated.
//
//    The default method returns TRUE.
//
// Use: extender, virtual

public boolean
generateDefaultNormals(SoState state, SoNormalBundle normalBundle)
//
////////////////////////////////////////////////////////////////////////
{
    return false;
}

	
    

    //! Returns the current normal cache, or NULL if there is none
    public SoNormalCache       getNormalCache() { return normalCache; }


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets up a cache to hold normals. This assumes the cache depends
//    on only the current coordinates, shape hints, and crease angle.
//
// Use: protected

protected void
setNormalCache(SoState state,
                              int numNormals, final SbVec3fArray normals)
//
////////////////////////////////////////////////////////////////////////
{
    if (normalCache != null)
        normalCache.unref();

    normalCache = new SoNormalCache(state);
    normalCache.ref();

    normalCache.set(numNormals, normals);

    // Set up the dependencies
//#define ADD_DEPENDENCY(elt)                                                   \
//    normalCache.addElement(state.getConstElement(elt.getClassStackIndex()))
//
//    ADD_DEPENDENCY(SoCoordinateElement);
//    ADD_DEPENDENCY(SoCreaseAngleElement);
//    ADD_DEPENDENCY(SoShapeHintsElement);

    normalCache.addElement(state.getConstElement(SoCoordinateElement.getClassStackIndex(SoCoordinateElement.class)));
    normalCache.addElement(state.getConstElement(SoCreaseAngleElement.getClassStackIndex(SoCreaseAngleElement.class)));
    normalCache.addElement(state.getConstElement(SoShapeHintsElement.getClassStackIndex(SoShapeHintsElement.class)));
    
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Passes on notification after invalidating any caches.
//
// Use: internal

public void
notify(SoNotList list)
//
////////////////////////////////////////////////////////////////////////
{
    // Destroy cache, if present
    if (normalCache != null) {
        normalCache.unref();
        normalCache = null;
    }
    vpCache.invalidate();

    // Then do the usual stuff
    super.notify(list);
}

// java port
public void SoShape_notify(SoNotList list) {
	super.notify(list);
}



////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns TRUE if the shape should be rendered now.  Does more then
//    the method in SoShape, because of possible transparency in vertex
//    property node.
//
// Use: protected
//
//////////////////////////////////////////////////////////////////////////
protected boolean
shouldGLRender(SoGLRenderAction action)
//
////////////////////////////////////////////////////////////////////////
{

    // Check if the shape is invisible
    if (SoDrawStyleElement.get(action.getState()) ==
        SoDrawStyleElement.Style.INVISIBLE)
        return false;

    // If the shape is transparent and transparent objects are being
    // delayed, don't render now
    // if there is transparency in the vertex property node, the object is
    // transparent.  If there are no colors in the vertexProperty node,
    // we have to check the state.
    if (!vpCache.colorIsInVtxProp()){
        if (action.handleTransparency())
                return false;
    }
    else if (vpCache.transpIsInVtxProp()){
        if (action.handleTransparency(true))
                return false;
    }
    else
            SoLazyElement.setBlending(action.getState(), false);        

    // If the current complexity is BOUNDING_BOX, just render the
    // cuboid surrounding the shape and tell the shape to stop
    if (SoComplexityTypeElement.get(action.getState()) ==
        SoComplexityTypeElement.Type.BOUNDING_BOX) {
        GLRenderBoundingBox(action);
        return false;
    }

    // Otherwise, go ahead and render the object
    return true;
}

protected boolean beginVertexArrayRendering( SoGLRenderAction action )
{
  SoState state = action.getState();
  boolean useVertexAttributes = SoLazyElement.shouldUseVertexAttributes(state);
  
  GL2 gl2 = state.getGL2(); // java port
  
  SoGLVBOElement vboElement = SoGLVBOElement.getInstance(action.getState());

  // check if VBOs should be used
  boolean shouldUseVBO = SoVBO.shouldUseVBO(state, vpCache.getNumVertices());

  boolean vertexVBOUsed = false;
  boolean colorVBOUsed = false;
  boolean normalVBOUsed = false;
  boolean texCoordVBOUsed = false;

  boolean vboBound = false;
  { // Vertices:
    FloatBuffer dataPtr;// = (FloatBuffer)vpCache.getVertices(0); java port
    SoVBO vbo = vboElement.getVBO(SoGLVBOElement.VBOType.VERTEX_VBO);
    if (vbo != null && shouldUseVBO) {
      if (vbo.bind(state)) {
        // if the VBO could be bound, we use it
        vertexVBOUsed = true;
        vboBound = true;
        dataPtr = null;
      }
      else {
      	dataPtr = (FloatBuffer)vpCache.getVertices(0);    	  
      }
    }
    else {
    	dataPtr = (FloatBuffer)vpCache.getVertices(0);
    }
    if (useVertexAttributes) {
        gl2.glVertexAttribPointer(SoLazyElement.VertexAttribs.ATTRIB_VERTEX.getValue(), (vpCache.getVertexStride() == (SbVec3f.sizeof()))?3:4, GL_FLOAT,false, 0, dataPtr);
        gl2.glEnableVertexAttribArray(SoLazyElement.VertexAttribs.ATTRIB_VERTEX.getValue());
      } else {
	    if(dataPtr != null) {
	    	gl2.glVertexPointer((vpCache.getVertexStride() == SbVec3f.sizeof())?3:4, GL_FLOAT, 0, dataPtr);
	    }
	    else {
	    	gl2.glVertexPointer((vpCache.getVertexStride() == SbVec3f.sizeof())?3:4, GL_FLOAT, 0, 0);
	    }
	    gl2.glEnableClientState(GL2.GL_VERTEX_ARRAY);
    }
  }

  // Normals:
  boolean perVertexNormals = vpCache.getNumNormals()>0 && (vpCache.getNormalBinding() == SoNormalBindingElement.Binding.PER_VERTEX_INDEXED || vpCache.getNormalBinding() == SoNormalBindingElement.Binding.PER_VERTEX);
  if (perVertexNormals) {
    FloatBuffer dataPtr;//java port = (FloatBuffer)vpCache.getNormals(0);
    SoVBO vbo = vboElement.getVBO(SoGLVBOElement.VBOType.NORMAL_VBO);
    if (vbo != null && shouldUseVBO) {
      if (vbo.bind(state)) {
        // if the VBO could be bound, we use it
        normalVBOUsed = true;
        vboBound = true;
        dataPtr = null;
      }
      else {
    	  dataPtr = (FloatBuffer)vpCache.getNormals(0);
      }
    } else {
    	dataPtr = (FloatBuffer)vpCache.getNormals(0);
      if (vboBound) {
        gl2.glBindBuffer/*ARB*/(GL_ARRAY_BUFFER, 0);
        vboBound = false;
      }
    }    
    if (useVertexAttributes) {
        gl2.glVertexAttribPointer(SoLazyElement.VertexAttribs.ATTRIB_NORMAL.getValue(), 3, GL_FLOAT,false, 0, dataPtr);
        gl2.glEnableVertexAttribArray(SoLazyElement.VertexAttribs.ATTRIB_NORMAL.getValue());
      } else {
    	  if(dataPtr == null) {
    		  gl2.glNormalPointer(GL_FLOAT, 0, 0); // java port
    	  }
    	  else {
    		  gl2.glNormalPointer(GL_FLOAT, 0, dataPtr);
    	  }
    	gl2.glEnableClientState(GL_NORMAL_ARRAY);
    }
  }

  // Colors:
  boolean perVertexColors = vpCache.getNumColors()>0 && (vpCache.getMaterialBinding() == SoMaterialBindingElement.Binding.PER_VERTEX_INDEXED || vpCache.getMaterialBinding() == SoMaterialBindingElement.Binding.PER_VERTEX);
  if (perVertexColors) {
    IntBuffer dataPtr = null;
    SoVBO vbo = vboElement.getVBO(SoGLVBOElement.VBOType.COLOR_VBO);
    if (vbo != null) {
      // fill vbo with data even if no VBO rendering is desired, since we want
      // to use/cache the flipped colors on little endian machines
      if (vpCache.colorIsInVtxProp()) {
        if (vertexProperty.getValue().getNodeId() != vbo.getDataId()) {
          // take colors from vertex property
          vbo.setData((int)(vpCache.getNumColors() * (long)Integer.SIZE /Byte.SIZE), vpCache.getColors(0), vertexProperty.getValue().getNodeId(), state);
        }
      } else {
        // take colors from state      
        SoGLLazyElement lazyElement = (SoGLLazyElement) SoLazyElement.getInstance(state);
        lazyElement.updateColorVBO(state, vbo);
      }
//#if MACHINE_WORD_FORMAT == DGL_LITTLE_ENDIAN
      if (!vbo.hasSwappedRGBAData()) {
        vbo.copyAndSwapPackedRGBA(state);
      }
//#endif
      if (shouldUseVBO && vbo.bind(state)) {
        // if the VBO should and could be bound, we use it
        colorVBOUsed = true;
        vboBound = true;
        dataPtr = null;
      } else {
        // use data pointer from VBO, since it is swapped on little endian machines
        dataPtr = (IntBuffer)vbo.getData().toIntBuffer();
      }
    } else {
      // we do not have a VBO, but we need swapped color data
      SoError.post("SoVertexShape: Vertex Array rendering was started but not color VBO was set.");
      dataPtr = (IntBuffer)vpCache.getColors(0).toIntBuffer();
    }
    if (!shouldUseVBO) {
      if (vboBound) {
        gl2.glBindBuffer/*ARB*/(GL_ARRAY_BUFFER, 0); // java port
        vboBound = false;
      }
    }
    if (useVertexAttributes) {
        gl2.glVertexAttribPointer(SoLazyElement.VertexAttribs.ATTRIB_COLOR.getValue(), 4, GL_UNSIGNED_BYTE,true, 0, dataPtr);
        gl2.glEnableVertexAttribArray(SoLazyElement.VertexAttribs.ATTRIB_COLOR.getValue());
      } else {
    	  if(dataPtr == null) { // java port
    		  gl2.glColorPointer(4, GL_UNSIGNED_BYTE, 0, 0);    		  
    	  }
    	  else {
    		  gl2.glColorPointer(4, GL_UNSIGNED_BYTE, 0, dataPtr);
    	  }
    	gl2.glEnableClientState(GL_COLOR_ARRAY);
    }
  } else {
	    if (useVertexAttributes) {
	        // we need to send the single color on the correct vertex attribute...
	        SbColor color = SoGLLazyElement.getDiffuse(state, 0);
	        gl2.glVertexAttrib4fv(SoLazyElement.VertexAttribs.ATTRIB_COLOR.getValue(), color.getValueRead(),0); //FIXME
	      }
	    }
  boolean perVertexTexCoords = vpCache.getNumTexCoords()>0;
  if (perVertexTexCoords) {
    FloatBuffer dataPtr = (FloatBuffer)vpCache.getTexCoords(0);
    SoVBO vbo = vboElement.getVBO(SoGLVBOElement.VBOType.TEXCOORD_VBO);
    if (vbo != null && shouldUseVBO) {
      if (vbo.bind(state)) {
        // if the VBO could be bound, we use it
        texCoordVBOUsed = true;
        vboBound = true;
        dataPtr = null;
      }
    } else {
      if (vboBound) {
        gl2.glBindBuffer/*ARB*/(GL_ARRAY_BUFFER, 0); // java port
        vboBound = false;
      }
    }
    if (useVertexAttributes) {
        gl2.glVertexAttribPointer(SoLazyElement.VertexAttribs.ATTRIB_TEXCOORD.getValue(), (vpCache.getTexCoordStride() == SbVec2f.sizeof())?2:4, GL_FLOAT,false, 0, dataPtr);
        gl2.glEnableVertexAttribArray(SoLazyElement.VertexAttribs.ATTRIB_TEXCOORD.getValue());
      } else {
    gl2.glTexCoordPointer((vpCache.getTexCoordStride() == SbVec2f.sizeof())?2:4, GL_FLOAT, 0, dataPtr);
    gl2.glEnableClientState(GL_TEXTURE_COORD_ARRAY);
    }
  }

//#ifdef DEBUG
  if (SoDebug.GetEnv("IV_DEBUG_VBO_RENDERING") != null) {
    SoDebugError.postInfo("beginVertexArrayRendering", getTypeId().getName().getString()+" Vertex Array used: Vertices: "+vpCache.getNumVertices()+
    		" "+(vertexVBOUsed?"VBO":"VA")+" Color: "+(perVertexColors?"PerVertex":"Overall")+" "+(perVertexColors?(colorVBOUsed?"VBO":"VA"):"")+
    		" Normals: "+(perVertexNormals?"PerVertex":"Overall")+" "+(perVertexNormals?(normalVBOUsed?"VBO":"VA"):"")+
    		" TexCoord: "+(perVertexTexCoords?"PerVertex":"NONE")+" "+(perVertexTexCoords?(texCoordVBOUsed?"VBO":"VA"):""));
  }
//#endif

  if (_preVertexArrayRenderingCB != null) {
    (_preVertexArrayRenderingCB).run(this, action, shouldUseVBO, vpCache.getNumVertices());
  }

  return shouldUseVBO;
}

protected void endVertexArrayRendering( SoGLRenderAction action, boolean vboWasUsed )
{
	GL2 gl2 = action.getCacheContext();
	
  if (vboWasUsed) {
    gl2.glBindBuffer/*ARB*/(GL_ARRAY_BUFFER, 0); // java port
  }
  boolean useVertexAttributes = SoLazyElement.shouldUseVertexAttributes(action.getState());
  if (useVertexAttributes) {
    gl2.glDisableVertexAttribArray(SoLazyElement.VertexAttribs.ATTRIB_VERTEX.getValue());
    gl2.glDisableVertexAttribArray(SoLazyElement.VertexAttribs.ATTRIB_NORMAL.getValue());
    gl2.glDisableVertexAttribArray(SoLazyElement.VertexAttribs.ATTRIB_COLOR.getValue());
    gl2.glDisableVertexAttribArray(SoLazyElement.VertexAttribs.ATTRIB_TEXCOORD.getValue());
  } else {
	  gl2.glDisableClientState(GL_VERTEX_ARRAY);
	  gl2.glDisableClientState(GL_NORMAL_ARRAY);
	  gl2.glDisableClientState(GL_COLOR_ARRAY);
	  gl2.glDisableClientState(GL_TEXTURE_COORD_ARRAY);
  }

  if (_postVertexArrayRenderingCB != null) {
    (_postVertexArrayRenderingCB).run(this, action, vboWasUsed, vpCache.getNumVertices());
  }
}


public SoVertexProperty getVertexProperty()
{
  SoNode node = vertexProperty.getValue();
  if (node != null) {
    if (node.isOfType(SoVertexProperty.getClassTypeId())) {
      return (SoVertexProperty)(node);
    } else {
      SoError.post("Expected a SoVertexProperty as vertexPropery field value, got "+node.getTypeId().getName().getString()+" instead." );
      return null;
    }
  } else {
    return null;
  }
}

/*!
  Convenience method that returns the current coordinate and normal
  element. This method is not part of the OIV API.
*/
public static void
getVertexData(SoState state,
                             final SoCoordinateElement [] coords,
                             final SbVec3fArray [] normals,
                             boolean neednormals)
{
  coords[0] = SoCoordinateElement.getInstance(state);
  assert(coords[0]!=null);

  normals[0] = null;
  if (neednormals) {
    normals[0] = SoNormalElement.getInstance(state).getArrayPtr();
  }
}


 
////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoVertexShape class.
//
// Use: internal

public static void initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SoSubNode.SO__NODE_INIT_ABSTRACT_CLASS(SoVertexShape.class, "VertexShape", SoShape.class);
}


}
