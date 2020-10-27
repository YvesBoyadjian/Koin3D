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
 |      This file defines the SoIndexedFaceSet node class.
 |
 |   Author(s)          : Paul S. Strauss, Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

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
  \class SoIndexedFaceSet SoIndexedFaceSet.h Inventor/nodes/SoIndexedFaceSet.h
  \brief The SoIndexedFaceSet class is used to handle generic indexed facesets.
  \ingroup nodes

  Faces are specified using the coordIndex field. Each face must be
  terminated by a negative (-1) index. Coordinates, normals, materials
  and texture coordinates from the current state (or from the
  vertexProperty node if set), can be indexed to create triangles,
  quads or polygons.

  Here's a usage example of just about the simplest possible use of
  this node, to show a single polygon face:

  \verbatim
  #Inventor V2.1 ascii
  
  Separator {
     Coordinate3 {
        point [ 0 0 0, 1 0 0, 1 1 0 ]
     }
     IndexedFaceSet {
        coordIndex [ 0, 1, 2, -1 ]
     }
  }
  \endverbatim

  Binding PER_VERTEX_INDEXED, PER_VERTEX, PER_FACE_INDEXED, PER_FACE
  or OVERALL can be set for material, and normals. The default
  material binding is OVERALL. The default normal binding is
  PER_VERTEX_INDEXED. When PER_VERTEX_INDEXED binding is used and the
  corresponding materialIndex, normalIndex, texCoordIndex field is
  empty, the coordIndex field will be used to index material, normal
  or texture coordinate. If you do specify indices for material,
  normals or texture coordinates for PER_VERTEX_INDEXED binding, make
  sure your index array matches the coordIndex array: there should be
  a -1 wherever there is a -1 in the coordIndex field. This is done to
  make this node more easily readable for humans.


  A fairly common request when rendering facesets is how to display a
  set of faces with different colors on the backside versus the
  frontside. There is not \e direct support for this in the API, but
  it can easily be implemented by duplicating all faces in both the
  SoShapeHints::COUNTERCLOCKWISE and the SoShapeHints::CLOCKWISE
  order. Here is a simple usage example, showing the technique for a
  single polygon, using two SoFaceSet nodes for rendering the same
  polygon from both sides:

  \verbatim
  #Inventor V2.1 ascii
  
  Separator {
     Coordinate3 { point [ 0 0 0, 1 0 0, 1 1 0 ] }
  
     Separator {
        Material { diffuseColor [ 1 1 0 ] }
        ShapeHints {
           vertexOrdering COUNTERCLOCKWISE
           shapeType SOLID
        }
        FaceSet { numVertices [ 3 ] }
     }
  
     Separator {
        Material { diffuseColor [ 1 0 0 ] }
        ShapeHints {
           vertexOrdering CLOCKWISE
           shapeType SOLID
        }
        FaceSet { numVertices [ 3 ] }
     }
  }
  \endverbatim

  The same effect can also be done in the following manner, using an
  SoIndexedFaceSet to explicitly render the polygon coordinates in
  both directions (clockwise and counterclockwise):

  \verbatim
  #Inventor V2.1 ascii
  
  Separator {
     Coordinate3 { point [ 0 0 0, 1 0 0, 1 1 0 ] }
  
     Material { diffuseColor [ 1 0 0, 1 1 0 ] }
     MaterialBinding { value PER_FACE_INDEXED }
  
     ShapeHints {
        vertexOrdering COUNTERCLOCKWISE
        shapeType SOLID
     }
  
     IndexedFaceSet {
        coordIndex [ 0, 1, 2, -1, 2, 1, 0, -1 ]
        materialIndex [ 0, 1 ]
     }
  }
  \endverbatim


  Another rather rare issue that might be interesting to know about is
  that to render polygons with concave borders, you should set up an
  SoShapeHints node with SoShapeHints::faceType set to
  SoShapeHints::UNKNOWN_FACE_TYPE in the scene graph before the
  SoIndexedFaceSet (or SoFaceSet) node. This needs to be done to force
  the rendering code to tessellate the polygons properly to triangles
  before sending it off to OpenGL. Without it, the polygons will be
  sent as-is to OpenGL, and the OpenGL implementation's tessellator is
  often not able to tessellate properly. Here is an example which
  usually fails without the SoShapeHints node (try commenting it out,
  and see what happens):

  \verbatim
  #Inventor V2.1 ascii
  
  ShapeHints { faceType UNKNOWN_FACE_TYPE }
  
  Coordinate3
  {
          point [ 2 0 0,
                  1 0 0,
                  1 1 0,
                  0 1 0,
                  0 2 0,
                  1 2 0,
                  2 2 0,
                  2 1 0 ]
  }
  
  FaceSet {}
  \endverbatim

  <b>FILE FORMAT/DEFAULTS:</b>
  \code
    IndexedFaceSet {
        vertexProperty NULL
        coordIndex 0
        materialIndex -1
        normalIndex -1
        textureCoordIndex -1
    }
  \endcode

  \sa SoFaceSet, SoIndexedTriangleStripSet
*/

// *************************************************************************

package jscenegraph.database.inventor.nodes;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Objects;

import com.jogamp.opengl.GL2;

import jscenegraph.coin3d.inventor.bundles.SoVertexAttributeBundle;
import jscenegraph.coin3d.inventor.elements.SoGLMultiTextureCoordinateElement;
import jscenegraph.coin3d.inventor.elements.SoVertexAttributeBindingElement;
import jscenegraph.coin3d.inventor.misc.SoGLDriverDatabase;
import jscenegraph.coin3d.inventor.nodes.SoVertexProperty;
import jscenegraph.coin3d.misc.SoGL;
import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec3fSingle;
import jscenegraph.database.inventor.SbVec4f;
import jscenegraph.database.inventor.SoDebug;
import jscenegraph.database.inventor.SoPickedPoint;
import jscenegraph.database.inventor.SoPrimitiveVertex;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoGetPrimitiveCountAction;
import jscenegraph.database.inventor.actions.SoRayPickAction;
import jscenegraph.database.inventor.bundles.SoMaterialBundle;
import jscenegraph.database.inventor.bundles.SoNormalBundle;
import jscenegraph.database.inventor.bundles.SoTextureCoordinateBundle;
import jscenegraph.database.inventor.caches.SoConvexDataCache;
import jscenegraph.database.inventor.caches.SoNormalCache;
import jscenegraph.database.inventor.details.SoDetail;
import jscenegraph.database.inventor.details.SoFaceDetail;
import jscenegraph.database.inventor.details.SoPointDetail;
import jscenegraph.database.inventor.elements.SoCacheElement;
import jscenegraph.database.inventor.elements.SoCoordinateElement;
import jscenegraph.database.inventor.elements.SoCreaseAngleElement;
import jscenegraph.database.inventor.elements.SoGLCacheContextElement;
import jscenegraph.database.inventor.elements.SoGLCoordinateElement;
import jscenegraph.database.inventor.elements.SoGLLazyElement;
import jscenegraph.database.inventor.elements.SoLazyElement;
import jscenegraph.database.inventor.elements.SoMaterialBindingElement;
import jscenegraph.database.inventor.elements.SoModelMatrixElement;
import jscenegraph.database.inventor.elements.SoNormalBindingElement;
import jscenegraph.database.inventor.elements.SoNormalElement;
import jscenegraph.database.inventor.elements.SoShapeHintsElement;
import jscenegraph.database.inventor.elements.SoShapeStyleElement;
import jscenegraph.database.inventor.elements.SoTextureCoordinateBindingElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.errors.SoError;
import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFInt32;
import jscenegraph.database.inventor.misc.SoBase;
import jscenegraph.database.inventor.misc.SoNotList;
import jscenegraph.database.inventor.misc.SoNotRec;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoVertexPropertyCache.SoVPCacheFunc;
import jscenegraph.mevis.inventor.elements.SoGLVBOElement;
import jscenegraph.mevis.inventor.misc.SoVBO;
import jscenegraph.mevis.inventor.misc.SoVertexArrayIndexer;
import jscenegraph.port.Ctx;
import jscenegraph.port.Destroyable;
import jscenegraph.port.IntArray;
import jscenegraph.port.IntArrayPtr;
import jscenegraph.port.MutableSbVec3fArray;
import jscenegraph.port.SbVec3fArray;

/**
 * @author Yves Boyadjian
 *
 */

////////////////////////////////////////////////////////////////////////////////
//! Indexed polygonal face shape node.
/*!
\class SoIndexedFaceSet
\ingroup Nodes
This node represents a 3D shape formed by constructing faces
(polygons) from vertices located at the coordinates
specified in the \b vertexProperty  field (from SoVertexShape), 
or the current inherited state.
For optimal performance, the \b vertexProperty  field is recommended.


SoIndexedFaceSet uses the indices in the \b coordIndex  field
(from SoIndexedShape) to specify the polygonal faces. An index of
<tt>SO_END_FACE_INDEX</tt> (-1) indicates that the current face has ended
and the next one begins.
For improved performance, arrange all the faces with only 3 
vertices at beginning of the list, then all faces with 4 vertices,
and finally all other faces.


The vertices of the faces are transformed by the current
transformation matrix. The faces are drawn with the current light
model and drawing style.


Treatment of the current material and normal binding is as follows:
The <tt>PER_PART</tt> and <tt>PER_FACE</tt> bindings specify a material or
normal for each face. <tt>PER_VERTEX</tt> specifies a material or normal
for each vertex.  The corresponding <tt>_INDEXED</tt> bindings are the
same, but use the \b materialIndex  or \b normalIndex  indices (see
SoIndexedShape). The default material binding is 
<tt>OVERALL</tt>. The default normal binding is 
<tt>PER_VERTEX_INDEXED</tt>.


If any normals (or materials) are specified, Inventor assumes 
you provide the correct number of them, as indicated by the binding.
You will see unexpected results
if you specify fewer normals (or materials) than the shape requires.
If no normals are specified, they will be generated automatically.


Textures are applied
as described for the SoIndexedShape class.

\par File Format/Default
\par
\code
IndexedFaceSet {
  coordIndex 0
  materialIndex -1
  normalIndex -1
  textureCoordIndex -1
}
\endcode

\par Action Behavior
\par
SoGLRenderAction
<BR> Draws faces based on the current coordinates, normals, materials, drawing style, and so on. 
\par
SoRayPickAction
<BR> Picks faces based on the current coordinates and transformation. Details about the intersection are returned in an SoFaceDetail. 
\par
SoGetBoundingBoxAction
<BR> Computes the bounding box that encloses all vertices of the face set with the current transformation applied to them.  Sets the center to the average of the coordinates of all vertices. 
\par
SoCallbackAction
<BR> If any triangle callbacks are registered with the action, they will be invoked for each successive triangle generated from each face in the set. 

\par See Also
\par
SoIndexedTriangleSet, SoCoordinate3, SoDrawStyle, SoFaceDetail, SoFaceSet, SoVertexProperty
*/
////////////////////////////////////////////////////////////////////////////////

public class SoIndexedFaceSet extends SoIndexedShape {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoIndexedFaceSet.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoIndexedFaceSet.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoIndexedFaceSet.class); }    	  	
	

	// for concavestatus
	public static final int STATUS_UNKNOWN =0;
	public static final int STATUS_CONVEX  =1;
	public static final int STATUS_CONCAVE =2;
	
	  private SoIndexedFaceSetP pimpl;

//! This coordinate index indicates that the current face ends and the
//! next face begins
	public static final int SO_END_FACE_INDEX       =(-1);

    //! This enum is used to indicate the current material or normal binding
    enum Binding {
        OVERALL, PER_FACE, PER_FACE_INDEXED, PER_VERTEX,
            PER_VERTEX_INDEXED;
    	
    	public int getValue() {
    		return ordinal();
    	}
    };
    
    //! Typedef of pointer to method on IndexedFaceSet;
    //! This will be used to simplify declaration and initialization.
    interface PMFS {
    	void run(SoIndexedFaceSet set, SoGLRenderAction action);
    }
                                                  
    //! Saves normal binding when generating primitives for picking
    private Binding             savedNormalBinding;
    
    //! Number of triangles, quads, (n>4)-vertex faces
    private int     numTris, numQuads, numFaces;

    private SoVertexArrayIndexer _triangleIndexer;
    private SoVertexArrayIndexer _quadIndexer;


    //! Array of function pointers to render functions:
    private static PMFS[] TriRenderFunc = new PMFS[32];
    private static PMFS[] QuadRenderFunc = new PMFS[32];
    private static PMFS[] GenRenderFunc = new PMFS[32];
    
 // 32 different rendering loops; the 5 bits used to determine the
 // rendering case are:
 // 43210  BITS            Routine suffix
 // -----  ----            --------------
 // 00...  Overall mtl     (Om)
 // 01...  Part mtl        (Pm)  NOT GENERATED, Fm is same!
 // 10...  Face mtl        (Fm)
 // 11...  Vtx mtl         (Vm)
 // ..00.  Overall/No norm (On)
 // ..01.  Part norm       (Pn)  NOT GENERATED, Fn is same!
 // ..10.  Face norm       (Fn)
 // ..11.  Vtx norm        (Vn)
 // ....0  No texcoord     -none-
 // ....1  Vtx texcoord    (T)
 //
    static {
    	for(int i = 0 ; i<32;i++) {
    		final int ii = i;
    		TriRenderFunc[i] = (ifs,a) -> SoError.post("SoIndexedFaceSet TriRenderFunc number "+ii+" not yet implemented");
    		QuadRenderFunc[i] = (ifs,a) -> SoError.post("SoIndexedFaceSet QuadRenderFunc number "+ii+" not yet implemented");
    		GenRenderFunc[i] = (ifs,a) -> SoError.post("SoIndexedFaceSet GenRenderFunc number "+ii+" not yet implemented");
    	}
    	
    	
    	TriRenderFunc[6] = (soIndexedFaceSet, action) -> soIndexedFaceSet.TriOmVn(action);
    	TriRenderFunc[14] = (soIndexedFaceSet, action) -> soIndexedFaceSet.TriFmVn(action);
    	TriRenderFunc[22] = (soIndexedFaceSet, action) -> soIndexedFaceSet.TriFmVn(action);
    	TriRenderFunc[30] = (soIndexedFaceSet, action) -> soIndexedFaceSet.TriVmVn(action);
    	QuadRenderFunc[6] = (soIndexedFaceSet, action) -> soIndexedFaceSet.QuadOmVn(action);
    	QuadRenderFunc[14] = (soIndexedFaceSet, action) -> soIndexedFaceSet.QuadFmVn(action);
    	QuadRenderFunc[22] = (soIndexedFaceSet, action) -> soIndexedFaceSet.QuadFmVn(action);
    	GenRenderFunc[2] =  (soIndexedFaceSet, action) -> soIndexedFaceSet.GenOmFn(action);
    	GenRenderFunc[4] =  (soIndexedFaceSet, action) -> soIndexedFaceSet.GenOmFn(action);
    	GenRenderFunc[6] =  (soIndexedFaceSet, action) -> soIndexedFaceSet.GenOmVn(action);
    	GenRenderFunc[14] =  (soIndexedFaceSet, action) -> soIndexedFaceSet.GenFmVn(action);
    	GenRenderFunc[22] =  (soIndexedFaceSet, action) -> soIndexedFaceSet.GenFmVn(action);
    	GenRenderFunc[24] =  (soIndexedFaceSet, action) -> soIndexedFaceSet.GenVmOn(action);
    }

 // Constants for influencing auto-caching algorithm:

    private static final int AUTO_CACHE_IFS_MIN_WITHOUT_VP = 20;

 // And the number above which we'll say caches definitely SHOULDN'T be
 // built (because they'll use too much memory):
    private static final int AUTO_CACHE_IFS_MAX = SoGLCacheContextElement.OIV_AUTO_CACHE_DEFAULT_MAX;

	
    private static void LOCK_VAINDEXER(Object obj) {
    	SoBase.staticDataLock();
    }
    
    private static void UNLOCK_VAINDEXER(Object obj) {
    	SoBase.staticDataUnlock();
    }


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoIndexedFaceSet()
//
////////////////////////////////////////////////////////////////////////
{
	pimpl = new SoIndexedFaceSetP();
	  pimpl.convexCache = null;
	  pimpl.vaindexer = null;
	  pimpl.concavestatus = STATUS_UNKNOWN;
	
  nodeHeader.SO_NODE_CONSTRUCTOR(/*SoIndexedFaceSet.class*/);

  numTris = numQuads = numFaces = -1;

  isBuiltIn = true;

  savedNormalBinding = Binding.OVERALL;

  _triangleIndexer = null;
  _quadIndexer = null;
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
  Destroyable.delete(_triangleIndexer);
  Destroyable.delete(_quadIndexer);

  Destroyable.delete(pimpl.vaindexer);
  if (pimpl.convexCache != null) pimpl.convexCache.unref();
  Destroyable.delete(pimpl);
  
  super.destructor();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Performs GL rendering of a indexedFaceSet.
//
// Use: protected

public void GLRender(SoGLRenderAction action)
{
	GL2 gl2 = action.getState().getGL2();
	
	  if (this.coordIndex.getNum() < 3) return;
	  SoState state = action.getState();

	  boolean hasvp = false;
	  
	  if (this.vertexProperty.getValue() != null) {
	    hasvp = true;
	    state.push();
	    this.vertexProperty.getValue().GLRender(action);
	  }

	  if (!this.shouldGLRender(action)) {
	    if (hasvp)
	      state.pop();
	    return;
	  }

	  Binding mbind = this.findMaterialBinding(state);
	  Binding nbind = this.findNormalBinding(state);

	  final SoCoordinateElement[] coords = new SoCoordinateElement[1]; //ptr
	  final SbVec3fArray[] normals = new SbVec3fArray[1]; //ptr
	  final IntArrayPtr[] cindices = new IntArrayPtr[1]; //ptr
	  final int[] numindices = new int[1];
	  final IntArrayPtr[] nindices = new IntArrayPtr[1];
	  final IntArrayPtr[] tindices = new IntArrayPtr[1];
	  final IntArrayPtr[] mindices = new IntArrayPtr[1];
	  boolean doTextures;
	  final boolean[] normalCacheUsed = new boolean[1];

	  final SoMaterialBundle mb = new SoMaterialBundle(action);

	  final SoTextureCoordinateBundle tb = new SoTextureCoordinateBundle(action, true, false);
	  doTextures = tb.needCoordinates();
	  boolean sendNormals = !mb.isColorOnly() || tb.isFunction();

	  this.getVertexData(state, coords, normals, cindices,
	                      nindices, tindices, mindices, numindices,
	                      sendNormals, normalCacheUsed);

	  if (!sendNormals) nbind = Binding.OVERALL;
	  else if (nbind == Binding.OVERALL) {
	    if (normals[0]!=null) gl2.glNormal3fv(normals[0].getFast(0).getValueRead());
	    else gl2.glNormal3f(0.0f, 0.0f, 1.0f);
	  }
	  else if (normalCacheUsed[0] && nbind == Binding.PER_VERTEX) {
	    nbind = Binding.PER_VERTEX_INDEXED;
	  }
	  else if (normalCacheUsed[0] && nbind == Binding.PER_FACE_INDEXED) {
	    nbind = Binding.PER_FACE;
	  }
	  if (this.getNodeType() == SoNode.NodeType.VRML1) {
	    // For VRML1, PER_VERTEX means per vertex in shape, not PER_VERTEX
	    // on the state.
	    if (mbind == Binding.PER_VERTEX) {
	      mbind = Binding.PER_VERTEX_INDEXED;
	      mindices[0] = cindices[0];
	    }
	    if (nbind == Binding.PER_VERTEX) {
	      nbind = Binding.PER_VERTEX_INDEXED;
	      nindices[0] = cindices[0];
	    }
	  }

	  Binding tbind = Binding./*NONE*/OVERALL;
	  if (doTextures) {
	    if (tb.isFunction() && !tb.needIndices()) {
	      tbind = Binding./*NONE*/OVERALL;
	      tindices[0] = null;
	    }
	    // FIXME: just call inherited::areTexCoordsIndexed() instead of
	    // the if-check? 20020110 mortene.
	    else if (SoTextureCoordinateBindingElement.get(state) ==
	             SoTextureCoordinateBindingElement.Binding.PER_VERTEX) {
	      tbind = Binding.PER_VERTEX;
	      tindices[0] = null;
	    }
	    else {
	      tbind = Binding.PER_VERTEX_INDEXED;
	      if (tindices[0] == null) tindices[0] = cindices[0];
	    }
	  }

	  boolean convexcacheused = false;
	  if (this.useConvexCache(action, normals[0], nindices[0], normalCacheUsed[0])) {
	    cindices[0] = pimpl.convexCache.getCoordIndices();
	    numindices[0] = pimpl.convexCache.getNumCoordIndices();
	    mindices[0] = pimpl.convexCache.getMaterialIndices();
	    nindices[0] = pimpl.convexCache.getNormalIndices();
	    tindices[0] = pimpl.convexCache.getTexIndices();

	    if (mbind == Binding.PER_VERTEX) mbind = Binding.PER_VERTEX_INDEXED;
	    else if (mbind == Binding.PER_FACE) mbind = Binding.PER_FACE_INDEXED;
	    if (nbind == Binding.PER_VERTEX) nbind = Binding.PER_VERTEX_INDEXED;
	    else if (nbind == Binding.PER_FACE) nbind = Binding.PER_FACE_INDEXED;

	    if (tbind != Binding./*NONE*/OVERALL) tbind = Binding.PER_VERTEX_INDEXED;
	    convexcacheused = true;
	  }

	  mb.sendFirst(); // make sure we have the correct material

//	#if 0
//	  fprintf(stderr,"numindices: %d, convex: %d, ncache: %d, nbind: %d, mbind: %d, tbind: %d, va: %d\n",
//	          numindices, convexcacheused, normalCacheUsed, nbind, mbind, tbind,
//	          cc_glglue_has_vertex_array(sogl_glue_instance(state)));
//	#endif

	  int contextid = action.getCacheContext();
	  SoGLLazyElement lelem = null;
	  boolean dova =
	    SoVBO.shouldRenderAsVertexArrays(state, contextid, numindices[0]) &&
	    !convexcacheused && !normalCacheUsed[0] &&
	    ((nbind == Binding.OVERALL) || ((nbind == Binding.PER_VERTEX_INDEXED) && ((Objects.equals(nindices[0], cindices[0])) || (nindices[0] == null)))) &&
	    ((tbind == Binding./*NONE*/OVERALL && !tb.needCoordinates()) || // no 
	     ((tbind == Binding.PER_VERTEX_INDEXED) && ((Objects.equals(tindices[0], cindices[0])) || (tindices[0] == null)))) &&
	    ((mbind == Binding./*NONE*/OVERALL) || ((mbind == Binding.PER_VERTEX_INDEXED) && ((Objects.equals(mindices[0], cindices[0])) || (mindices[0] == null)))) &&
	    SoGLDriverDatabase.isSupported(SoGL.sogl_glue_instance(state), SoGLDriverDatabase.SO_GL_VERTEX_ARRAY);

	  SoGLVBOElement vboelem = SoGLVBOElement.getInstance(state);
	  SoVBO colorvbo = null;

	  boolean didrenderasvbo = false;
	  if (dova && (mbind != Binding.OVERALL)) {
	    dova = false;
	    if ((mbind == Binding.PER_VERTEX_INDEXED) && ((Objects.equals(mindices[0], cindices[0])) || (mindices[0] == null))) {
	      lelem = (SoGLLazyElement) SoLazyElement.getInstance(state);
	      colorvbo = vboelem.getColorVBO();
	      if (colorvbo != null) dova = true;
	      else {
	        // we might be able to do VA-rendering, but need to check the
	        // diffuse color type first.
	        if (!lelem.isPacked() && lelem.getNumTransparencies() <= 1) {
	          dova = true;
	        }
	      }
	    }
	  }
	  if (dova) {
	    boolean dovbo = this.startVertexArray(action,
	                                          coords[0],
	                                          (nbind != Binding.OVERALL) ? normals[0] : null,
	                                          doTextures,
	                                          mbind != Binding.OVERALL);
	    didrenderasvbo = dovbo;

	    LOCK_VAINDEXER(this);
	    if (pimpl.vaindexer == null) {
			//long start = System.nanoTime();
			
		    final int numindices_0 = numindices[0]; // java port
		      
		    final IntArrayPtr cindices_0 = cindices[0]; // java port
		      
		    jscenegraph.coin3d.inventor.rendering.SoVertexArrayIndexer indexer;
	        int cnt = 0;
	        int i = 0;
	        while (i + cnt < numindices_0 && cindices_0.get(i+cnt) >= 0) cnt++;
	        
	        if(cnt == 3) {
	        	indexer = new jscenegraph.coin3d.inventor.rendering.SoVertexArrayIndexer(numindices_0/4*3); //ptr
	        }
	        else if(cnt == 4) {
	        	indexer = new jscenegraph.coin3d.inventor.rendering.SoVertexArrayIndexer(numindices_0/5*4); //ptr	        	
	        }
	        else {
	        	indexer = new jscenegraph.coin3d.inventor.rendering.SoVertexArrayIndexer(); //ptr	        	
	        }
	        
	      i = 0;
	      
	      while (i < numindices_0) {
	        cnt = 0;
	        while (i + cnt < numindices_0 && cindices_0.get(i+cnt) >= 0) cnt++;
	        
	        switch (cnt) {
	        case 3:
	          indexer.addTriangle(cindices_0.get(i),cindices_0.get(i+1), cindices_0.get(i+2));
	          break;
	        case 4:
	          indexer.addQuad(cindices_0.get(i),cindices_0.get(i+1),cindices_0.get(i+2),cindices_0.get(i+3));
	          break;
	        default:
	          if (cnt > 4) {
	            indexer.beginTarget(GL2.GL_POLYGON);
	            for (int j = 0; j < cnt; j++) {
	              indexer.targetVertex(GL2.GL_POLYGON, cindices_0.get(i+j));
	            }
	            indexer.endTarget(GL2.GL_POLYGON);
	          }
	        }
	        i += cnt + 1;
	      }
			//long inter = System.nanoTime();
	      indexer.close();
			//long stop = System.nanoTime();
			//long delta1 = inter - start;
			//long delta2 = stop - inter;
			
//			if((delta1 + delta2) > 5e6) {
//				System.out.println("SoTouchLOD2 " + delta1/1e6 +"-"+delta2/1e6+" ms");
//			}
			
			
	      if (indexer.getNumVertices() != 0) {
	        pimpl.vaindexer = indexer;
	      }
	      else {
	        Destroyable.delete(indexer);
	      }
//	#if 0
//	      fprintf(stderr,"XXX: create VertexArrayIndexer: %d\n", indexer.getNumVertices());
//	#endif
	    }

	    if (pimpl.vaindexer != null) {
	      pimpl.vaindexer.render(SoGL.sogl_glue_instance(state), dovbo, contextid);
	    }
	    UNLOCK_VAINDEXER(this);
	    this.finishVertexArray(action,
	                            dovbo,
	                            (nbind != Binding.OVERALL),
	                            doTextures,
	                            mbind != Binding.OVERALL);
	  }
	  else {
	    final SoVertexAttributeBundle vab = new SoVertexAttributeBundle(action, true);
	    boolean doattribs = vab.doAttributes();

	    SoVertexAttributeBindingElement.Binding attribbind = 
	      SoVertexAttributeBindingElement.get(state);

	    if (!doattribs) { 
	      // for overall attribute binding we check for doattribs before
	      // sending anything in SoGL::FaceSet::GLRender
	      attribbind = SoVertexAttributeBindingElement.Binding.OVERALL;
	    }

	    SoGL.sogl_render_faceset((SoGLCoordinateElement)coords[0],
	                        cindices[0],
	                        numindices[0],
	                        normals[0],
	                        nindices[0],
	                        /*&*/mb,
	                        mindices[0],
	                        /*&*/tb,
	                        tindices[0],
	                        /*&*/vab,
	                        (int)nbind.getValue(),
	                        (int)mbind.getValue(),
	                        (int)attribbind.getValue(),
	                        doTextures ? 1 : 0,
	                        doattribs ? 1 : 0);
	    
	    Destroyable.delete(vab); // java port
	  }
	  if (normalCacheUsed[0]) {
	    this.readUnlockNormalCache();
	  }

	  if (convexcacheused) {
	    pimpl.readUnlockConvexCache();
	  }

	  if (hasvp) {
	    state.pop();
	  }
	  // send approx number of triangles for autocache handling
	  SoGL.sogl_autocache_update(state, this.coordIndex.getNum() / 4, didrenderasvbo);
	  
	  tb.destructor(); // java port
	  mb.destructor(); // java port	  
	}

//
////////////////////////////////////////////////////////////////////////
//{
//  SoState state = action.getState();
//
//  // Get ShapeStyleElement
//  final SoShapeStyleElement shapeStyle = SoShapeStyleElement.get(state);
//
//  SoVertexProperty vp = getVertexProperty();
//
//  // First see if the object is visible and should be rendered now:
//  if (shapeStyle.mightNotRender()) {
//    if (vpCache.mightNeedSomethingFromState(shapeStyle)) {
//      vpCache.fillInColorAndTranspAvail(vp, state);
//    }
//    if (! shouldGLRender(action))
//      return;
//  }
//
//  if (vp != null) {
//    vp.putVBOsIntoState(state);
//  }
//
//  if (vpCache.mightNeedSomethingFromState(shapeStyle)) {
//    // Set up numTris/Quads/Faces
//    if (numTris < 0)
//      setupNumTrisQuadsFaces();
//
//    // If faces might be concave, we have to send them through GLU:
//    final SoShapeHintsElement.VertexOrdering[] vo = new SoShapeHintsElement.VertexOrdering[1];
//    final SoShapeHintsElement.ShapeType[] st = new SoShapeHintsElement.ShapeType[1];
//    final SoShapeHintsElement.FaceType[] ft = new SoShapeHintsElement.FaceType[1];
//    SoShapeHintsElement.get(state, vo, st, ft);
//
//    if ((numQuads > 0 || numFaces > 0)
//      && ft[0] != SoShapeHintsElement.FaceType.CONVEX) {
//
//        // Use generatePrimitives for now...
//        SoShape_GLRender(action);
//        return;
//    }
//    int numPolys = numTris+numQuads+numFaces;
//    if (numPolys == 0 ) return;
//
//    vpCache.fillInCache(vp, state);
//
//    if (vpCache.shouldGenerateNormals(shapeStyle)) {
//      // See if there is a valid normal cache. If so, use it:
//      SoNormalCache normCache = getNormalCache();
//      if (normCache == null || !normCache.isValid(state)) {
//
//        // Compute the number of vertices. This is just an
//        // approximation, so using 3 verts per unknown polygon
//        // is close enough. 
//        int numVerts = 3 * numTris + 4 * numQuads + 3 * numFaces;
//
//        final SoNormalBundle nb = new SoNormalBundle(action, false);
//        nb.initGenerator(numVerts);
//        generateDefaultNormals(state, nb);
//        normCache = getNormalCache();
//        nb.destructor();
//      }
//      vpCache.numNorms = normCache.getNum();
//      vpCache.normalPtr = normCache.getNormalsFloat();
//
//    }
//
//    SoTextureCoordinateBundle tcb = null;
//    int useTexCoordsAnyway = 0;
//    if (vpCache.shouldGenerateTexCoords(shapeStyle)) {
//      state.push();
//      tcb = new SoTextureCoordinateBundle(action, true, true);
//    }
//    else if (shapeStyle.isTextureFunction() && vpCache.haveTexCoordsInVP()){
//      state.push();
//      useTexCoordsAnyway = SoVertexPropertyCache.Bits.TEXCOORD_BIT.getValue();
//      SoGLMultiTextureCoordinateElement.setTexGen(state, this, 0, null);
//    }
//
//    // Now that normals have been generated, can set up pointers
//    // (this is a method on SoIndexedShape):
//
//    setupIndices(numPolys, numPolys, shapeStyle.needNormals(), 
//      (useTexCoordsAnyway!=0 || shapeStyle.needTexCoords()));
//
//    //If lighting or texturing is off, this vpCache and other things
//    //need to be reconstructed when lighting or texturing is turned
//    //on, so we set the bits in the VP cache:
//    if(! shapeStyle.needNormals()) vpCache.needFromState |= 
//      SoVertexPropertyCache.Bits.NORMAL_BITS.getValue();
//    if(! shapeStyle.needTexCoords()) vpCache.needFromState |= 
//      SoVertexPropertyCache.Bits.TEXCOORD_BIT.getValue();
//
//    GLRenderInternal(action, useTexCoordsAnyway, shapeStyle);
//
//    if (coordIndex.getNum() < AUTO_CACHE_IFS_MIN_WITHOUT_VP &&
//      vpCache.mightNeedSomethingFromState(shapeStyle)) {
//        SoGLCacheContextElement.shouldAutoCache(state,
//          SoGLCacheContextElement.AutoCache.DO_AUTO_CACHE.getValue());
//    } else if (coordIndex.getNum() > AUTO_CACHE_IFS_MAX) {
//        SoGLCacheContextElement.shouldAutoCache(state,
//          SoGLCacheContextElement.AutoCache.DONT_AUTO_CACHE.getValue());
//    }           
//
//    if (tcb != null) {
//      tcb.destructor();
//      state.pop();
//    } else if (useTexCoordsAnyway != 0)
//      state.pop();
//  }
//  else {
//
//    GLRenderInternal(action, 0, shapeStyle);
//
//    // Influence auto-caching algorithm:
//    if (coordIndex.getNum() > AUTO_CACHE_IFS_MAX) {
//
//        SoGLCacheContextElement.shouldAutoCache(state,
//          SoGLCacheContextElement.AutoCache.DONT_AUTO_CACHE.getValue());
//    }           
//  }
//  return;
//}

//
// internal method which checks if convex cache needs to be
// used or (re)created. Returns TRUE if convex cache must be
// used. this->convexCache is then guaranteed to be != NULL.
//
protected boolean
useConvexCache(SoAction action,
                                 final SbVec3fArray normals,
                                 /*final*/ IntArrayPtr nindices,
                                 final boolean normalsfromcache)
{
  // we only want to use the convex data cache when rendering. All
  // other actions should work on the original data to ensure correct
  // part and face indices.
  if (!action.isOfType(SoGLRenderAction.getClassTypeId())) return false;
  
  SoState state = action.getState();
  if (SoShapeHintsElement.getFaceType(state) == SoShapeHintsElement.FaceType.CONVEX)
    return false;
  
  if (pimpl.concavestatus == STATUS_UNKNOWN) {
    IntArrayPtr ptr = this.coordIndex.getValuesIntArrayPtr(0);
    IntArrayPtr endptr = IntArrayPtr.plus(ptr,this.coordIndex.getNum());
    int cnt = 0;
    pimpl.concavestatus = STATUS_CONVEX;
    while (IntArrayPtr.lessThan(ptr,endptr)) {
      if (ptr.get() >= 0) {
    	  ptr.plusPlus();
    	  cnt++;
      }
      else {
    	  ptr.plusPlus();
        if (cnt > 3) { pimpl.concavestatus = STATUS_CONCAVE; break; }
        cnt = 0;
      }
    }
  }
  if (pimpl.concavestatus == STATUS_CONVEX) return false;

  pimpl.readLockConvexCache();

  if (pimpl.convexCache != null && pimpl.convexCache.isValid(state))
    return true;

  pimpl.readUnlockConvexCache();
  pimpl.writeLockConvexCache();

  if (pimpl.convexCache != null) pimpl.convexCache.unref();
  boolean storedinvalid = SoCacheElement.setInvalid(false);

  // need to send matrix if we have some weird transformation
  final SbMatrix modelmatrix = new SbMatrix(SoModelMatrixElement.get(state));
  if (modelmatrix.getValue()[3][0] == 0.0f &&
      modelmatrix.getValue()[3][1] == 0.0f &&
      modelmatrix.getValue()[3][2] == 0.0f &&
      modelmatrix.getValue()[3][3] == 1.0f) modelmatrix.copyFrom(SbMatrix.identity());

  // push to create cache dependencies
  state.push();
  pimpl.convexCache = new SoConvexDataCache(state);
  pimpl.convexCache.ref();
  SoCacheElement.set(state, pimpl.convexCache);
  if (this.vertexProperty.getValue() != null) this.vertexProperty.getValue().doAction(action);
  final SoCoordinateElement[] coords = new SoCoordinateElement[1]; //ptr
  final SbVec3fArray[] dummynormals = new SbVec3fArray[1]; // ptr
  final IntArrayPtr[] cindices = new IntArrayPtr[1];
  final int[] numindices = new int[1];
  final IntArrayPtr[] dummynindices = new IntArrayPtr[1];
  final IntArrayPtr[] tindices = new IntArrayPtr[1];
  final IntArrayPtr[] mindices = new IntArrayPtr[1];
  final boolean[] dummy = new boolean[1];

  // normals was included as parameters to this function (to avoid
  // a double readLock on the normal cache), so tell getVertexData()
  // not to return normals.
  this.getVertexData(state, coords, dummynormals, cindices,
                      dummynindices, tindices, mindices, numindices,
                      false, dummy);
  
  // force a cache-dependency on SoNormalElement
  SoNormalElement.getInstance(state);

  Binding mbind = this.findMaterialBinding(state);
  Binding nbind = this.findNormalBinding(state);

  if (this.getNodeType() == SoNode.NodeType.VRML1) {
    // For VRML1, PER_VERTEX means per vertex in shape, not PER_VERTEX
    // on the state.
    if (mbind == Binding.PER_VERTEX) {
      mbind = Binding.PER_VERTEX_INDEXED;
      mindices[0] = cindices[0];
    }
    if (nbind == Binding.PER_VERTEX) {
      nbind = Binding.PER_VERTEX_INDEXED;
      nindices = cindices[0];
    }
  }

  if (normalsfromcache && nbind == Binding.PER_VERTEX) {
    nbind = Binding.PER_VERTEX_INDEXED;
  }

  Binding tbind = Binding./*NONE*/OVERALL;
  // FIXME: just call inherited::areTexCoordsIndexed() instead of
  // the if-check? 20020110 mortene.
  if (SoTextureCoordinateBindingElement.get(state) ==
      SoTextureCoordinateBindingElement.Binding.PER_VERTEX) {
    tbind = Binding.PER_VERTEX;
  }
  else {
    tbind = Binding.PER_VERTEX_INDEXED;
    if (tindices[0] == null) tindices[0] = cindices[0];
  }

  if (nbind == Binding.PER_VERTEX_INDEXED && nindices == null) {
    nindices = cindices[0];
  }
  if (mbind == Binding.PER_VERTEX_INDEXED && mindices[0] == null) {
    mindices[0] = cindices[0];
  }
  pimpl.convexCache.generate(coords[0], modelmatrix,
                              cindices[0], numindices[0],
                              mindices[0], nindices, tindices[0],
                              SoConvexDataCache.Binding.fromValue(mbind.getValue()),
                              SoConvexDataCache.Binding.fromValue(nbind.getValue()),
                              SoConvexDataCache.Binding.fromValue(tbind.getValue()));

  pimpl.writeUnlockConvexCache();

  state.pop();
  SoCacheElement.setInvalid(storedinvalid);

  pimpl.readLockConvexCache();

  return true;
}



////////////////////////////////////////////////////////////////////////
//
// Description:
//    Generates polys representing a face set.
//
// Use: protected

//public void generatePrimitives(SoAction action)
////
//////////////////////////////////////////////////////////////////////////
//{
//  SoState state = action.getState();
//  // Put vertexProperty stuff into state:
//  SoVertexProperty vp = getVertexProperty();
//  state.push();
//  if (vp != null) {
//    vp.doAction(action);
//  }
//
//  // When generating primitives for picking, there is no need to
//  // create details now, since they will be created in
//  // createTriangleDetail() when an intersection is found (but we
//  // need to use the face detail to figure out the rest of it).
//  // Otherwise, we create a face detail containing the 3 points of
//  // the generated triangle, using the stuff in SoShape.
//  // We also delay computing default texture coordinates.
//  boolean forPicking = action.isOfType(SoRayPickAction.getClassTypeId());
//
//  final SoPrimitiveVertex           pv = new SoPrimitiveVertex();
//  final SoFaceDetail                fd = new SoFaceDetail();
//  final SoPointDetail               pd = new SoPointDetail();
//  final SoNormalBundle              nb = new SoNormalBundle(action, false);
//  final SoTextureCoordinateBundle   tcb = new SoTextureCoordinateBundle(action, false, ! forPicking);
//  final SoCoordinateElement   ce;
//  int                         curVert, curIndex, curFace, curCoord;
//  int                         numIndices;
//  final int[]                       coordIndices; int[] matlIndices;
//  int[]                       normIndices, texCoordIndices;
//  Binding                     materialBinding, normalBinding;
//  boolean                        texCoordsIndexed;
//
//  ce = SoCoordinateElement.getInstance(action.getState());
//
//  materialBinding  = getMaterialBinding(action);
//  normalBinding    = getNormalBinding(action, nb);
//  texCoordsIndexed = areTexCoordsIndexed(action);
//
//  numIndices      = coordIndex.getNum();
//  coordIndices    = coordIndex.getValuesI(0);
//  matlIndices     = materialIndex.getValuesI(0);
//  normIndices     = normalIndex.getValuesI(0);
//  texCoordIndices = textureCoordIndex.getValuesI(0);
//
//  // Check for special case of 1 index of SO_END_FACE_INDEX. This
//  // means that coord indices are to be used for materials, normals,
//  // or texture coords as well
//  if (materialIndex.getNum() == 1 && matlIndices[0] == SO_END_FACE_INDEX)
//    matlIndices = coordIndices;
//  if (normalIndex.getNum() == 1 && normIndices[0] == SO_END_FACE_INDEX) 
//    normIndices = coordIndices;
//  if (textureCoordIndex.getNum() == 1 &&
//    texCoordIndices[0] == SO_END_FACE_INDEX)
//    texCoordIndices = coordIndices;
//
//  if (forPicking) {
//    pv.setTextureCoords(new SbVec4f(0.0f, 0.0f, 0.0f, 0.0f));
//    pv.setDetail(fd);
//  }
//  else
//    pv.setDetail(pd);
//
//  // Also, save normal binding in instance so createTriangleDetail()
//  // will know what it is without having to reconstruct a normal bundle
//  savedNormalBinding = normalBinding;
//
//  // Step through all the coordinate indices, building faces out
//  // of them, until we run out of coordinate indices.
//  curFace = curVert = curIndex = 0;
//
//  while (curIndex < numIndices) {
//
//    int vertsInFace = getNumVerts(curIndex);
//
//    // Forget about faces with fewer than three vertices...
//    if (vertsInFace < 3) {
//      curIndex += vertsInFace+1;
//      curFace++;
//      continue;
//    }
//
//    if (forPicking) {
//      fd.setFaceIndex(curFace);
//      fd.setPartIndex(curFace);
//    }
//
//    beginShape(action, SoShape.TriangleShape.POLYGON, forPicking ? null : fd);
//
//    // Loop through all vertices of current face
//    while (curIndex < numIndices &&
//      (curCoord = (int)coordIndices[curIndex]) != SO_END_FACE_INDEX) {
//
//        // Check for duplicate vertices; GLU doesn't handle them
//        // very well. Discard current index if it's the same as
//        // the previous one.
//        if (curIndex == 0 || (curCoord != coordIndices[curIndex - 1])) {
//
//          int matlIndex = -1, normIndex = -1, tcIndex;
//
//          switch (materialBinding) {
//      case OVERALL:
//        matlIndex = 0;
//        break;
//      case PER_FACE:
//        matlIndex = curFace;
//        break;
//      case PER_FACE_INDEXED:
//        matlIndex = (int) matlIndices[curFace];
//        break;
//      case PER_VERTEX:
//        matlIndex = curVert;
//        break;
//      case PER_VERTEX_INDEXED:
//        matlIndex = (int) matlIndices[curIndex];
//        break;
//          }
//          switch (normalBinding) {
//      case OVERALL:
//        normIndex = 0;
//        break;
//      case PER_FACE:
//        normIndex = curFace;
//        break;
//      case PER_FACE_INDEXED:
//        normIndex = (int) normIndices[curFace];
//        break;
//      case PER_VERTEX:
//        normIndex = curVert;
//        break;
//      case PER_VERTEX_INDEXED:
//        normIndex = (int) normIndices[curIndex];
//        break;
//          }
//          tcIndex = (texCoordsIndexed ?
//            (int) texCoordIndices[curIndex] : curVert);
//
//          pv.setPoint(ce.get3(curCoord));
//          pv.setNormal(nb.get(normIndex));
//          pv.setMaterialIndex(matlIndex);
//
//          if (! tcb.isFunction())
//            pv.setTextureCoords(tcb.get(tcIndex));
//
//          if (! forPicking) {
//            if (tcb.isFunction())
//              pv.setTextureCoords(tcb.get(pv.getPoint(),
//              pv.getNormal()));
//            pd.setCoordinateIndex(curCoord);
//            pd.setMaterialIndex(matlIndex);
//            pd.setNormalIndex(normIndex);
//            pd.setTextureCoordIndex(tcIndex);
//          }
//
//          shapeVertex(pv);
//        }
//
//        curVert++;
//        curIndex++;
//    }
//
//    endShape();
//
//    curIndex++;     // Skip over the END_FACE_INDEX
//    curFace++;
//  }
//  state.pop();
//  
//  // java port
//  pv.destructor();
//  fd.destructor();
//  pd.destructor();
//  nb.destructor();
//  tcb.destructor();
//}


// this macro actually makes the code below more readable  :-)
private void DO_VERTEX(
		final SoPointDetail pointDetail, 
		final SoPrimitiveVertex vertex, 
		final IntArrayPtr mindices,
		final IntArrayPtr nindices,
		final IntArrayPtr tindices,
		Binding mbind,
		Binding nbind, 
		Binding tbind,
		final int[] matnr,
		final int[] normnr,
		final MutableSbVec3fArray currnormal,
		final SbVec3fArray normals,
		final SoTextureCoordinateBundle tb,
		final SoCoordinateElement coords,
		final int[] texidx,
		int idx) {
	
if (mbind == Binding.PER_VERTEX) {                  
  pointDetail.setMaterialIndex(matnr[0]);      
  vertex.setMaterialIndex(matnr[0]++);         
}                                           
else if (mbind == Binding.PER_VERTEX_INDEXED) {     
  pointDetail.setMaterialIndex(mindices.get()); 
  vertex.setMaterialIndex(mindices.get());
  mindices.plusPlus();
}                                         
if (nbind == Binding.PER_VERTEX) {                
  pointDetail.setNormalIndex(normnr[0]);     
  currnormal.assign( normals,normnr[0]++);        
  vertex.setNormal(currnormal.get(0));          
}                                         
else if (nbind == Binding.PER_VERTEX_INDEXED) {   
  pointDetail.setNormalIndex(nindices.get());  
  currnormal.assign(normals,nindices.get()); nindices.plusPlus();     
  vertex.setNormal(currnormal.get(0));          
}                                        
if (tb.isFunction()) {                 
  vertex.setTextureCoords(tb.get(coords.get3(idx), currnormal.get(0))); 
  if (tb.needIndices()) { 
	  pointDetail.setTextureCoordIndex(tindices!=null ? tindices.get() : texidx[0]); 
      if( tindices != null) {
    	  tindices.plusPlus();
      }
      else {
    	  texidx[0]++;
      }
  }
}                                         
else if (tbind != Binding.OVERALL) {                      
  pointDetail.setTextureCoordIndex(tindices!=null ? tindices.get() : texidx[0]); 
  vertex.setTextureCoords(tb.get(tindices!=null ? tindices.get() : texidx[0])); 
  if( tindices != null) {
	  tindices.plusPlus();
  }
  else {
	  texidx[0]++;
  }
}                                         
vertex.setPoint(coords.get3(idx));        
pointDetail.setCoordinateIndex(idx);      
this.shapeVertex(vertex);
}

// doc from parent
public void
generatePrimitives(SoAction action)
{
  if (this.coordIndex.getNum() < 3) return;

  SoState state = action.getState();

  if (this.vertexProperty.getValue() != null) {
    state.push();
    this.vertexProperty.getValue().doAction(action);
  }

  Binding mbind = this.findMaterialBinding(state);
  Binding nbind = this.findNormalBinding(state);

  final SoCoordinateElement[] coords = new SoCoordinateElement[1]; //ptr
  final SbVec3fArray[] normals = new SbVec3fArray[1]; //ptr
  final IntArrayPtr[] cindices = new IntArrayPtr[1]; //ptr
  final int[] numindices = new int[1];
  final IntArrayPtr[] nindices = new IntArrayPtr[1]; //ptr
  final IntArrayPtr[] tindices = new IntArrayPtr[1]; //ptr
  final IntArrayPtr[] mindices = new IntArrayPtr[1]; //ptr
  boolean doTextures;
  boolean sendNormals;
  final boolean[] normalCacheUsed = new boolean[1];

  sendNormals = true; // always generate normals

  getVertexData(state, coords, normals, cindices,
                nindices, tindices, mindices, numindices,
                sendNormals, normalCacheUsed);

  SoTextureCoordinateBundle tb = new SoTextureCoordinateBundle(action, false, false);
  doTextures = tb.needCoordinates();

  if (!sendNormals) nbind = Binding.OVERALL;
  else if (normalCacheUsed[0] && nbind == Binding.PER_VERTEX) {
    nbind = Binding.PER_VERTEX_INDEXED;
  }
  else if (normalCacheUsed[0] && nbind == Binding.PER_FACE_INDEXED) {
    nbind = Binding.PER_FACE;
  }

  if (this.getNodeType() == SoNode.NodeType.VRML1) {
    // For VRML1, PER_VERTEX means per vertex in shape, not PER_VERTEX
    // on the state.
    if (mbind == Binding.PER_VERTEX) {
      mbind = Binding.PER_VERTEX_INDEXED;
      mindices[0] = IntArrayPtr.copyOf(cindices[0]);
    }
    if (nbind == Binding.PER_VERTEX) {
      nbind = Binding.PER_VERTEX_INDEXED;
      nindices[0] = IntArrayPtr.copyOf(cindices[0]);
    }
  }

  Binding tbind = Binding.OVERALL;
  if (doTextures) {
    if (tb.isFunction() && !tb.needIndices()) {
      tbind = Binding.OVERALL;
      tindices[0] = null;
    }
    // FIXME: just call inherited::areTexCoordsIndexed() instead of
    // the if-check? 20020110 mortene.
    else if (SoTextureCoordinateBindingElement.get(state) ==
             SoTextureCoordinateBindingElement.Binding.PER_VERTEX) {
      tbind = Binding.PER_VERTEX;
      tindices[0] = null;
    }
    else {
      tbind = Binding.PER_VERTEX_INDEXED;
      if (tindices[0] == null) tindices[0] = IntArrayPtr.copyOf(cindices[0]);
    }
  }

  if (nbind == Binding.PER_VERTEX_INDEXED && nindices[0] == null) {
    nindices[0] = IntArrayPtr.copyOf(cindices[0]);
  }
  if (mbind == Binding.PER_VERTEX_INDEXED && mindices[0] == null) {
    mindices[0] = IntArrayPtr.copyOf(cindices[0]);
  }

  boolean convexcacheused = false;
  if (this.useConvexCache(action, normals[0], nindices[0], normalCacheUsed[0])) {
    cindices[0] = this.pimpl.convexCache.getCoordIndices();
    numindices[0] = this.pimpl.convexCache.getNumCoordIndices();
    mindices[0] = this.pimpl.convexCache.getMaterialIndices();
    nindices[0] = this.pimpl.convexCache.getNormalIndices();
    tindices[0] = this.pimpl.convexCache.getTexIndices();

    if (mbind == Binding.PER_VERTEX) mbind = Binding.PER_VERTEX_INDEXED;
    else if (mbind == Binding.PER_FACE) mbind = Binding.PER_FACE_INDEXED;
    if (nbind == Binding.PER_VERTEX) nbind = Binding.PER_VERTEX_INDEXED;
    else if (nbind == Binding.PER_FACE) nbind = Binding.PER_FACE_INDEXED;

    if (tbind != Binding.OVERALL) tbind = Binding.PER_VERTEX_INDEXED;
    convexcacheused = true;
  }

  final int[] texidx = new int[1];
  TriangleShape mode = TriangleShape.POLYGON;
  TriangleShape newmode;
  IntArrayPtr viptr = IntArrayPtr.copyOf(cindices[0]);
  IntArrayPtr viendptr = IntArrayPtr.plus(viptr,numindices[0]);
  int v1, v2, v3, v4, v5 = 0; // v5 init unnecessary, but kills a compiler warning.

  final SoPrimitiveVertex vertex = new SoPrimitiveVertex();
  final SoPointDetail pointDetail = new SoPointDetail();
  final SoFaceDetail faceDetail = new SoFaceDetail();

  vertex.setDetail(pointDetail);

  final SbVec3fSingle dummynormal = new SbVec3fSingle(0,0,1);
  MutableSbVec3fArray currnormal = new MutableSbVec3fArray(dummynormal);
  if (normals[0] != null) currnormal = MutableSbVec3fArray.from( normals[0]);
  vertex.setNormal(currnormal.get(0));

  final int[] matnr = new int[0];
  final int[] normnr = new int[0];

  while (viptr.plusLessThan(2, viendptr)) {
    v1 = viptr.get(); viptr.plusPlus();
    v2 = viptr.get(); viptr.plusPlus();
    v3 = viptr.get(); viptr.plusPlus();
    if (v1 < 0 || v2 < 0 || v3 < 0) {
//#if COIN_DEBUG
      SoDebugError.postInfo("SoIndexedFaceSet::generatePrimitives",
                             "Polygon with less than three vertices detected. "+
                             "Aborting current shape.");
//#endif // COIN_DEBUG
      
      break;
    } 
    if(IntArrayPtr.lessThan(viptr,viendptr)) {
    	v4 = viptr.get();
    	viptr.plusPlus();
    }
    else {
    	v4 = -1;
    }
    //v4 = viptr < viendptr ? *viptr++ : -1;
    if (v4  < 0) newmode = TriangleShape.TRIANGLES;
    else {
    	if(IntArrayPtr.lessThan(viptr,viendptr)) {
    		v5 = viptr.get();
    		viptr.plusPlus();
    	}
    	else {
    		v5 = -1;
    	}
      //v5 = viptr < viendptr ? *viptr++ : -1;
      if (v5 < 0) newmode = TriangleShape.QUADS;
      else newmode = TriangleShape.POLYGON;
    }
    if (newmode != mode) {
      if (mode != TriangleShape.POLYGON) this.endShape();
      mode = newmode;
      this.beginShape(action, mode, faceDetail);
    }
    else if (mode == TriangleShape.POLYGON) this.beginShape(action, TriangleShape.POLYGON, faceDetail);

    // vertex 1 can't use DO_VERTEX
    if (mbind == Binding.PER_VERTEX || mbind == Binding.PER_FACE) {
      pointDetail.setMaterialIndex(matnr[0]);
      vertex.setMaterialIndex(matnr[0]++);
    }
    else if (mbind == Binding.PER_VERTEX_INDEXED || mbind == Binding.PER_FACE_INDEXED) {
      pointDetail.setMaterialIndex(mindices[0].get());
      vertex.setMaterialIndex(mindices[0].get());mindices[0].plusPlus();
    }
    if (nbind == Binding.PER_VERTEX || nbind == Binding.PER_FACE) {
      pointDetail.setNormalIndex(normnr[0]);
      currnormal.assign(normals[0],normnr[0]);normnr[0]++;
      vertex.setNormal(currnormal.get(0));
    }
    else if (nbind == Binding.PER_FACE_INDEXED || nbind == Binding.PER_VERTEX_INDEXED) {
      pointDetail.setNormalIndex(nindices[0].get());
      currnormal.assign(normals[0],nindices[0].get());nindices[0].plusPlus();
      vertex.setNormal(currnormal.get(0));
    }

    if (tb.isFunction()) {
      vertex.setTextureCoords(tb.get(coords[0].get3(v1), currnormal.get(0)));
      if (tb.needIndices()) {
    	  pointDetail.setTextureCoordIndex(tindices[0] != null ? tindices[0].get() : texidx[0]); 
          if( tindices[0] != null) {
        	  tindices[0].plusPlus();
          }
          else {
        	  texidx[0]++;
          }
      }
    }
    else if (tbind != Binding.OVERALL) {
      pointDetail.setTextureCoordIndex(tindices[0] != null ? tindices[0].get() : texidx[0]);
      vertex.setTextureCoords(tb.get(tindices[0] != null ? tindices[0].get() : texidx[0]));
      if( tindices[0] != null) {
    	  tindices[0].plusPlus();
      }
      else {
    	  texidx[0]++;
      }
    }
    pointDetail.setCoordinateIndex(v1);
    vertex.setPoint(coords[0].get3(v1));
    this.shapeVertex(vertex);

    DO_VERTEX(pointDetail,vertex,mindices[0],nindices[0],tindices[0],mbind,nbind,tbind,matnr,normnr,currnormal,normals[0],tb,coords[0],texidx,v2);
    DO_VERTEX(pointDetail,vertex,mindices[0],nindices[0],tindices[0],mbind,nbind,tbind,matnr,normnr,currnormal,normals[0],tb,coords[0],texidx,v3);

    if (mode != TriangleShape.TRIANGLES) {
      DO_VERTEX(pointDetail,vertex,mindices[0],nindices[0],tindices[0],mbind,nbind,tbind,matnr,normnr,currnormal,normals[0],tb,coords[0],texidx,v4);
      if (mode == TriangleShape.POLYGON) {
        DO_VERTEX(pointDetail,vertex,mindices[0],nindices[0],tindices[0],mbind,nbind,tbind,matnr,normnr,currnormal,normals[0],tb,coords[0],texidx,v5);
        if(IntArrayPtr.lessThan(viptr,viendptr)) {
        	v1 = viptr.get();
        	viptr.plusPlus();
        }
        else {
        	v1 = -1;
        }
        //v1 = viptr < viendptr ? *viptr++ : -1;
        while (v1 >= 0) {
          DO_VERTEX(pointDetail,vertex,mindices[0],nindices[0],tindices[0],mbind,nbind,tbind,matnr,normnr,currnormal,normals[0],tb,coords[0],texidx,v1);
          if(IntArrayPtr.lessThan(viptr,viendptr)) {
        	  v1 = viptr.get();
        	  viptr.plusPlus();
          }
          else {
        	  v1 = -1;
          }
          //v1 = viptr < viendptr ? *viptr++ : -1;
        }
        this.endShape();
      }
    }
    faceDetail.incFaceIndex();
    faceDetail.incPartIndex();
    if (mbind == Binding.PER_VERTEX_INDEXED) {
      mindices[0].plusPlus();;
    }
    if (nbind == Binding.PER_VERTEX_INDEXED) {
      nindices[0].plusPlus();
    }
    if (tindices[0]!= null) tindices[0].plusPlus();
  }
  if (mode != TriangleShape.POLYGON) this.endShape();

  if (normalCacheUsed[0]) {
    this.readUnlockNormalCache();
  }
  if (convexcacheused) {
    this.pimpl.readUnlockConvexCache();
  }

  if (this.vertexProperty.getValue() != null) {
    state.pop();
  }
  
  tb.destructor(); // java port
}


// doc from parent
public void getPrimitiveCount(SoGetPrimitiveCountAction action)
{
  if (!this.shouldPrimitiveCount(action)) return;

  int n = this.coordIndex.getNum();
  if (n < 3) return;

  if (action.canApproximateCount()) {
    action.addNumTriangles(n/4);
  }
  else {
    IntArray ptr = coordIndex.getValues(0);
    final IntArray endptr = ptr.plus(n);
    int cnt = 0;
    int add = 0;
    while (IntArray.lessThan(ptr,endptr)) {
      if (ptr.get(0) >= 0) cnt++;
      else {
        add += cnt-2;
        cnt = 0;
      }
      ptr = ptr.plus(1);
    }
    // in case index array wasn't terminated with a -1
    if (cnt >= 3) add += cnt-2;
    action.addNumTriangles(add);
  }
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Overrides standard method to create an SoFaceDetail instance
//    representing a picked intersection with a triangle in the set.
//
// Use: protected, virtual

public SoDetail createTriangleDetail(SoRayPickAction action,
                                       SoPrimitiveVertex v1,
                                       SoPrimitiveVertex v2,
                                       SoPrimitiveVertex v3,
                                       SoPickedPoint pp)
                                       //
                                       ////////////////////////////////////////////////////////////////////////
{
  // When we get here, the detail in each vertex already points to an
  // SoFaceDetail (on the stack in generatePrimitives()). This detail
  // contains the correct face/part index for the intersected face.
  // However, this face detail does not contain any point details.
  // We have to create a new SoFaceDetail instance and set up the
  // SoPointDetail instances inside it to contain the vertices of
  // the picked face. We also need to compute texture coordinates.

  final SoFaceDetail        newFD = new SoFaceDetail();
  final SoFaceDetail  oldFD = ( SoFaceDetail ) v1.getDetail();

  // Get pointers to all indices, just in case
  IntArray       coordIndices, matlIndices;
  IntArray       normIndices, texCoordIndices;

  coordIndices    = coordIndex.getValues(0);
  matlIndices     = materialIndex.getValues(0);
  normIndices     = normalIndex.getValues(0);
  texCoordIndices = textureCoordIndex.getValues(0);
  if (materialIndex.getNum() == 1 && matlIndices.get(0) == SO_END_FACE_INDEX)
    matlIndices = coordIndices;
  if (normalIndex.getNum() == 1 && normIndices.get(0) == SO_END_FACE_INDEX) 
    normIndices = coordIndices;
  if (textureCoordIndex.getNum() == 1 &&
    texCoordIndices.get(0) == SO_END_FACE_INDEX);
    texCoordIndices = coordIndices;

  // Find out which face was hit
  int hitFace = (int) oldFD.getFaceIndex();

  // Find index of coordinate of first vertex of face by skipping
  // over previous faces. Also, count the number of vertices used.
  int curIndex = 0, curVert = 0, vertsInFace;
  for (int face = 0; face < hitFace; face++) {
    vertsInFace = getNumVerts(curIndex);
    curVert  += vertsInFace;
    curIndex += vertsInFace + 1;    // Skip over the END_FACE_INDEX
  }

  // Get number of vertices in the hit face
  vertsInFace = getNumVerts(curIndex);

  // Make room in the detail for vertices
  newFD.setNumPoints(vertsInFace);

  // We need the bindings to set up the material/normals stuff. The
  // normal binding was saved for us in generatePrimitives() so we
  // don't have to create a normal bundle to recompute it.
  Binding     materialBinding  = getMaterialBinding(action);
  Binding     normalBinding    = savedNormalBinding;
  boolean        texCoordsIndexed = areTexCoordsIndexed(action);

  // Store each vertex in the detail
  final SoPointDetail               pd = new SoPointDetail();
  final SoTextureCoordinateBundle   tcb = new SoTextureCoordinateBundle(action, false);
  for (int vert = 0; vert < vertsInFace; vert++) {

    int matlIndex = -1, normIndex = -1, tcIndex;

    switch (materialBinding) {
    case OVERALL:
      matlIndex = 0;
      break;
    case PER_FACE:
      matlIndex = hitFace;
      break;
    case PER_FACE_INDEXED:
      matlIndex = (int) matlIndices.get(hitFace);
      break;
    case PER_VERTEX:
      matlIndex = curVert;
      break;
    case PER_VERTEX_INDEXED:
      matlIndex = (int) matlIndices.get(curIndex);
      break;
    }
    switch (normalBinding) {
    case OVERALL:
      normIndex = 0;
      break;
    case PER_FACE:
      normIndex = hitFace;
      break;
    case PER_FACE_INDEXED:
      normIndex = (int) normIndices.get(hitFace);
      break;
    case PER_VERTEX:
      normIndex = curVert;
      break;
    case PER_VERTEX_INDEXED:
      normIndex = (int) normIndices.get(curIndex);
      break;
    }
    tcIndex = (texCoordsIndexed ?
      (int) texCoordIndices.get(curIndex) : curVert);

    pd.setCoordinateIndex(coordIndices.get(curIndex));
    pd.setMaterialIndex(matlIndex);
    pd.setNormalIndex(normIndex);
    pd.setTextureCoordIndex(tcb.isFunction() ? 0 : tcIndex);

    newFD.setPoint(vert, pd);

    curVert++;
    curIndex++;
  }

  // Compute texture coordinates at intersection point and store it
  // in the picked point
  if (tcb.isFunction())
    pp.setObjectTextureCoords(tcb.get(pp.getObjectPoint(),
    pp.getObjectNormal()));

  // The face/part indices are in the incoming details
  newFD.setFaceIndex(hitFace);
  newFD.setPartIndex(hitFace);

  return newFD;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns current material binding from action's state.
//
// Use: private, static

public SoIndexedFaceSet.Binding getMaterialBinding(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
  switch (SoMaterialBindingElement.get(action.getState())) {
      case OVERALL:
        return SoIndexedFaceSet.Binding.OVERALL;
      case PER_PART:
      case PER_FACE:
        return SoIndexedFaceSet.Binding.PER_FACE;
      case PER_PART_INDEXED:
      case PER_FACE_INDEXED:
        return SoIndexedFaceSet.Binding.PER_FACE_INDEXED;
      case PER_VERTEX:
        return SoIndexedFaceSet.Binding.PER_VERTEX;
      case PER_VERTEX_INDEXED:
        return SoIndexedFaceSet.Binding.PER_VERTEX_INDEXED;
  }
  return SoIndexedFaceSet.Binding.OVERALL;     // Shut SGI compiler up
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns current normal binding from action's state.
//
// Use: private, static

public SoIndexedFaceSet.Binding getNormalBinding(SoAction action, SoNormalBundle nb)
//
////////////////////////////////////////////////////////////////////////
{
  if (nb != null && figureNormals(action.getState(), nb))
    return SoIndexedFaceSet.Binding.PER_VERTEX;

  switch (SoNormalBindingElement.get(action.getState())) {
      case OVERALL:
        return SoIndexedFaceSet.Binding.OVERALL;

      case PER_PART:
      case PER_FACE:
        return SoIndexedFaceSet.Binding.PER_FACE;

      case PER_PART_INDEXED:
      case PER_FACE_INDEXED:
        return SoIndexedFaceSet.Binding.PER_FACE_INDEXED;

      case PER_VERTEX:
        return SoIndexedFaceSet.Binding.PER_VERTEX;

      case PER_VERTEX_INDEXED:
        return SoIndexedFaceSet.Binding.PER_VERTEX_INDEXED;
  }
  return SoIndexedFaceSet.Binding.OVERALL;     // Shut SGI compiler up
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Figures out normals, if necessary.  Returns true if normals were
//    figured out (and the normal binding should be PER_VERTEX).
//
// Use: private

public boolean figureNormals(SoState state, SoNormalBundle nb)
//
////////////////////////////////////////////////////////////////////////
{
  // See if there is a valid normal cache. If so, tell the normal
  // bundle to use it.
  SoNormalCache normCache = getNormalCache();
  if (normCache != null && normCache.isValid(state)) {
    nb.set(normCache.getNum(), normCache.getNormals());
    return true;
  }

  int                 numNeeded = 0, i, numVertices = 0;
  final SoMFInt32     nIndices;

  if (normalIndex.getNum() == 1 && normalIndex.operator_square_bracketI(0) == SO_END_FACE_INDEX)
    nIndices = coordIndex;
  else
    nIndices = normalIndex;

  // Find greatest index:
  for (i = 0; i < nIndices.getNum(); i++) {
    if ((nIndices).operator_square_bracketI(i) > numNeeded)
      numNeeded = (int) (nIndices).operator_square_bracketI(i);
    if ((nIndices).operator_square_bracketI(i) >= 0) // Count vertices
      ++numVertices;
  }

  if (nb.shouldGenerate(numNeeded)) {
    generateDefaultNormals(state, nb);
    return true;
  }
  return false;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Generates default normals using the given state and normal
//    bundle. Returns true if normals were generated.
//
// Use: extender, virtual

public boolean generateDefaultNormals(SoState state, SoNormalBundle nb)
//
////////////////////////////////////////////////////////////////////////
{
  int                         numIndices = coordIndex.getNum(), curIndex = 0;
  SoCoordinateElement   ce = null;
  SbVec3fArray               vpCoords = null;

  SoVertexProperty vp = getVertexProperty();
  if (vp != null && vp.vertex.getNum() > 0) {
    vpCoords = vp.vertex.getValues(0);
  } else {
    ce = SoCoordinateElement.getInstance(state);
  }

  while (curIndex < numIndices) {

    // Figure out how many vertices in this face:
    int vertsInFace;
    for (vertsInFace = 0;
      vertsInFace + curIndex < numIndices &&
      coordIndex.operator_square_bracketI(vertsInFace + curIndex) != SO_END_FACE_INDEX;
    vertsInFace++)
      ;

    // Forget about faces with fewer than three vertices...
    if (vertsInFace < 3) {
      curIndex += vertsInFace + 1;
      continue;
    }

    nb.beginPolygon();

    // Loop through all vertices of current face
    while (curIndex < numIndices &&
      coordIndex.operator_square_bracketI(curIndex) != SO_END_FACE_INDEX) {

        if (ce != null)
          nb.polygonVertex(ce.get3((int)coordIndex.operator_square_bracketI(curIndex)));
        else
          nb.polygonVertex(vpCoords.get(coordIndex.operator_square_bracketI(curIndex)));

        curIndex++;
    }
    nb.endPolygon();
  }
  nb.generate();

  // Cache the resulting normals
  setNormalCache(state, nb.getNumGeneratedNormals(),
    nb.getGeneratedNormals());

  return true;
}

// Documented in superclass.
public boolean
generateDefaultNormals(SoState state,
                                       SoNormalCache nc)
{
  boolean ccw = true;
  if (SoShapeHintsElement.getVertexOrdering(state) ==
      SoShapeHintsElement.VertexOrdering.CLOCKWISE) ccw = false;

  SbVec3fArray coords = SoCoordinateElement.getInstance(state).getArrayPtr3();
  assert(coords != null);

  int numcoords = SoCoordinateElement.getInstance(state).getNum();

  SoNormalBindingElement.Binding normbind =
    SoNormalBindingElement.get(state);

  switch (normbind) {
  case PER_VERTEX:
  case PER_VERTEX_INDEXED:
    nc.generatePerVertex(coords,
                          numcoords,
                          coordIndex.getValuesIntArrayPtr(0),
                          coordIndex.getNum(),
                          SoCreaseAngleElement.get(state),// this.getNodeType() == SoNode.NodeType.VRML1), //YB TODO
                          null,
                          -1,
                          ccw);
    break;
  case PER_FACE:
  case PER_FACE_INDEXED:
  case PER_PART:
  case PER_PART_INDEXED:
    nc.generatePerFace(coords,
                        numcoords,
                        coordIndex.getValuesIntArrayPtr(0),
                        coordIndex.getNum(),
                        ccw);
    break;
  default:
    break;
  }
  return true;
}



//
// translates current material binding into the internal Binding enum.
//
public SoIndexedFaceSet.Binding
findMaterialBinding(SoState state)
{
  Binding binding = Binding.OVERALL;
  SoMaterialBindingElement.Binding matbind =
    SoMaterialBindingElement.get(state);

  switch (matbind) {
  case /*SoMaterialBindingElement::*/OVERALL:
    binding = Binding.OVERALL;
    break;
  case /*SoMaterialBindingElement::*/PER_VERTEX:
    binding = Binding.PER_VERTEX;
    break;
  case /*SoMaterialBindingElement::*/PER_VERTEX_INDEXED:
    binding = Binding.PER_VERTEX_INDEXED;
    break;
  case /*SoMaterialBindingElement::*/PER_PART:
  case /*SoMaterialBindingElement::*/PER_FACE:
    binding = Binding.PER_FACE;
    break;
  case /*SoMaterialBindingElement::*/PER_PART_INDEXED:
  case /*SoMaterialBindingElement::*/PER_FACE_INDEXED:
    binding = Binding.PER_FACE_INDEXED;
    break;
  default:
//#if COIN_DEBUG
    SoDebugError.postWarning("SoIndexedFaceSet::findMaterialBinding",
                              "unknown material binding setting");
//#endif // COIN_DEBUG
    break;
  }
  return binding;
}


//
// translates current normal binding into the internal Binding enum.
//
public SoIndexedFaceSet.Binding
findNormalBinding(SoState state)
{
  Binding binding = Binding.PER_VERTEX_INDEXED;
  SoNormalBindingElement.Binding normbind =
    (SoNormalBindingElement.Binding) SoNormalBindingElement.get(state);

  switch (normbind) {
  case /*SoNormalBindingElement::*/OVERALL:
    binding = Binding.OVERALL;
    break;
  case /*SoNormalBindingElement::*/PER_VERTEX:
    binding = Binding.PER_VERTEX;
    break;
  case /*SoNormalBindingElement::*/PER_VERTEX_INDEXED:
    binding = Binding.PER_VERTEX_INDEXED;
    break;
  case /*SoNormalBindingElement::*/PER_PART:
  case /*SoNormalBindingElement::*/PER_FACE:
    binding = Binding.PER_FACE;
    break;
  case /*SoNormalBindingElement::*/PER_PART_INDEXED:
  case /*SoNormalBindingElement::*/PER_FACE_INDEXED:
    binding = Binding.PER_FACE_INDEXED;
    break;
  default:
//#if COIN_DEBUG
    SoDebugError.postWarning("SoIndexedFaceSet::findNormalBinding",
                              "unknown normal binding setting");
//#endif // COIN_DEBUG
    break;
  }
  return binding;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    invalidate vpCache when notified
//
// Use: private 

public void notify(SoNotList list)
//
////////////////////////////////////////////////////////////////////////
{
  if (list.getLastRec().getType() == SoNotRec.Type.CONTAINER &&
    list.getLastField() == coordIndex) {
      numTris = numQuads = numFaces = -1;
  }

  // Overridden to invalidate convex cache.
  pimpl.readLockConvexCache();
  if (pimpl.convexCache != null) pimpl.convexCache.invalidate();
  pimpl.readUnlockConvexCache();
  SoField f = list.getLastField();
  if (f == this.coordIndex) {
    pimpl.concavestatus = STATUS_UNKNOWN;
    LOCK_VAINDEXER(this);
    if (pimpl.vaindexer != null) {
      Destroyable.delete( pimpl.vaindexer);
      pimpl.vaindexer = null;
    }
    UNLOCK_VAINDEXER(this);
  }
  super.notify(list);
}

///////////////////////////////////////////////////////////////////////////
//
// Description:
//      Find out how many tris, quads, and polygons there are:
//
// use: private
//
///////////////////////////////////////////////////////////////////////////
public void setupNumTrisQuadsFaces()
{
  numTris = numQuads = numFaces = 0;
  int i = 0;
  final int numCoords = coordIndex.getNum();
  while ((i < numCoords - 2) && 
    ((i+3 == numCoords)||(coordIndex.operator_square_bracketI(i+3) == SO_END_FACE_INDEX))) {
      ++numTris;
      i += 4;  // Skip past three vertex indices and END_OF_FACE
      // marker
  }
  while ((i < numCoords - 3) && 
    ((i+4 == numCoords) ||(coordIndex.operator_square_bracketI(i+4) == SO_END_FACE_INDEX)) &&
    (coordIndex.operator_square_bracketI(i+3) != SO_END_FACE_INDEX)) {

      ++numQuads;
      i += 5;  // Skip past three vertex indices and END_OF_FACE
      // marker
  }
  /* if there aren't 3 vertices, no polygons are rendered */
  if (i > numCoords - 3 ) return;
  while (i < numCoords) {
    if ((i+1 == numCoords)||(coordIndex.operator_square_bracketI(i) == SO_END_FACE_INDEX))
      ++numFaces;
    ++i;
  }
}    

public void GLRenderInternal( SoGLRenderAction action , int useTexCoordsAnyway, final SoShapeStyleElement shapeStyle)
{
  SoState  state = action.getState();

  // If doing multiple colors, turn on ColorMaterial:
  if (vpCache.getNumColors() > 1) {
    SoGLLazyElement.setColorMaterial(state, true);
  }
  //
  // Ask LazyElement to setup:
  //
  SoGLLazyElement lazyElt = (SoGLLazyElement )
    SoLazyElement.getInstance(state);

  if (vpCache.colorIsInVtxProp()) {
    lazyElt.send(state, SoLazyElement.masks.ALL_MASK.getValue());
    lazyElt.sendVPPacked(state, (IntBuffer)vpCache.getColors(0).toIntBuffer()/*.asIntBuffer()*/);
  } else {
    lazyElt.send(state, SoLazyElement.masks.ALL_MASK.getValue());
  }

  boolean fastPathTaken = false;
  // check for cases where GL Vertex Arrays can be used:
  if (SoVBO.isVertexArrayRenderingAllowed() &&
      (vpCache.getNumVertices()>0) &&
      (vpCache.getNumNormals()==0 || (vpCache.getNormalBinding() == SoNormalBindingElement.Binding.PER_VERTEX_INDEXED)) &&
      (vpCache.getNumColors()==0 || (vpCache.getMaterialBinding() == SoMaterialBindingElement.Binding.PER_VERTEX_INDEXED || vpCache.getMaterialBinding() == SoMaterialBindingElement.Binding.OVERALL)) &&
      // VA rendering is only possible if there is a color VBO, since it manages the packed color swapping
      ((vpCache.getMaterialBinding() != SoMaterialBindingElement.Binding.PER_VERTEX_INDEXED) || SoGLVBOElement.getInstance(state).getColorVBO()/*getVBO(SoGLVBOElement.VBOType.COLOR_VBO)*/ != null) &&
      (vpCache.getNumTexCoords()==0 || (vpCache.getTexCoordBinding() == SoTextureCoordinateBindingElement.Binding.PER_VERTEX_INDEXED)) &&
      (materialIndex.getNum()==1 && materialIndex.getValues(0).get(0)==-1) && 
      (normalIndex.getNum()==1 && normalIndex.getValues(0).get(0)==-1) && 
      (textureCoordIndex.getNum()==1 && textureCoordIndex.getValues(0).get(0)==-1))
  {
    fastPathTaken = true;
    if (numTris > 0 || numQuads > 0) {
      boolean useVBO = beginVertexArrayRendering(action);

      if (numTris>0) {
        if (_triangleIndexer == null) {
          _triangleIndexer = new SoVertexArrayIndexer(GL2.GL_TRIANGLES);
        }
        if (_triangleIndexer.getDataId()!=getNodeId()) {
          _triangleIndexer.setInventorTriangles(numTris, coordIndex.getValuesI(0), getNodeId());
        }
        _triangleIndexer.render(state, useVBO);
      }

      if (numQuads>0) {
        if (_quadIndexer == null) {
          _quadIndexer = new SoVertexArrayIndexer(GL2.GL_QUADS);
        }
        if (_quadIndexer.getDataId()!=getNodeId()) {
          // offset is numTris * (3 indices + -1 triangle end code)
          _quadIndexer.setInventorQuads(numQuads, coordIndex.getValuesI(numTris * 4), getNodeId());
        }
        _quadIndexer.render(state, useVBO);
      }

//#ifdef DEBUG
      if (SoDebug.GetEnv("IV_DEBUG_VBO_RENDERING") != null) {
        SoDebugError.postInfo("GLRenderInternal", getTypeId().getName().getString()+" VA Rendering: "+numTris+" Tris "+numQuads+" Quads");
      }
//#endif

      endVertexArrayRendering(action, useVBO);
    }
  } 

  // Call the appropriate render loops:
  if (numTris > 0 && !fastPathTaken) {
    (this.TriRenderFunc[useTexCoordsAnyway | vpCache.getRenderCase(shapeStyle)]).run(this,action);
//#ifdef DEBUG
    if (SoDebug.GetEnv("IV_DEBUG_LEGACY_RENDERING") != null) {
      SoDebugError.postInfo("GLRenderInternal", getTypeId().getName().getString()+" Immediate Mode Rendering: "+numTris+" Triangles");
    }
//#endif
  }
  if (numQuads > 0 && !fastPathTaken) {
    (this.QuadRenderFunc[useTexCoordsAnyway | vpCache.getRenderCase(shapeStyle)]).run(this,action);
//#ifdef DEBUG
    if (SoDebug.GetEnv("IV_DEBUG_LEGACY_RENDERING") != null) {
      SoDebugError.postInfo("GLRenderInternal", getTypeId().getName().getString()+" Immediate Mode Rendering: "+numQuads+" Quads");
    }
//#endif
  }
  if (numFaces > 0) {
    (this.GenRenderFunc[useTexCoordsAnyway | vpCache.getRenderCase(shapeStyle)]).run(this,action);
//#ifdef DEBUG
    if (SoDebug.GetEnv("IV_DEBUG_LEGACY_RENDERING") != null) {
      SoDebugError.postInfo("GLRenderInternal", getTypeId().getName().getString()+" Immediate Mode Rendering: "+numFaces+" Polygons");
    }
//#endif
  }

  // If doing multiple colors, turn off ColorMaterial:
  if (vpCache.getNumColors() > 1) {
    SoGLLazyElement.setColorMaterial(state, false);
    ((SoGLLazyElement )SoLazyElement.getInstance(state)).
      reset(state, SoLazyElement.masks.DIFFUSE_MASK.getValue());
  }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoIndexedFaceSet class.
//
// Use: internal

public static void initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SO__NODE_INIT_CLASS(SoIndexedFaceSet.class, "IndexedFaceSet", SoIndexedShape.class);
}




public void TriOmVn (SoGLRenderAction action) {
	
	GL2 gl2 = Ctx.get(action.getCacheContext());
	
    final int[] vertexIndex = coordIndex.getValuesI(0);
    Buffer vertexPtr = vpCache.getVertices(0);
    final int vertexStride = vpCache.getVertexStride();
    SoVPCacheFunc vertexFunc = vpCache.vertexFunc;
    Buffer normalPtr = vpCache.getNormals(0);
    final int normalStride = vpCache.getNormalStride();
    SoVPCacheFunc normalFunc = vpCache.normalFunc;
    IntArray normalIndx = getNormalIndices();

    gl2.glBegin(GL2.GL_TRIANGLES);
    int vtxCtr = 0;
    for (int tri = 0; tri < numTris; tri++) {

    	normalPtr.position(normalStride*normalIndx.get(vtxCtr)/Float.BYTES); (normalFunc).run(gl2, normalPtr);
    	vertexPtr.position(vertexStride*vertexIndex[vtxCtr]/Float.BYTES); (vertexFunc).run(gl2, vertexPtr);

    	normalPtr.position(normalStride*normalIndx.get(vtxCtr+1)/Float.BYTES); (normalFunc).run(gl2, normalPtr);
    	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+1]/Float.BYTES); (vertexFunc).run(gl2, vertexPtr);

    	normalPtr.position(normalStride*normalIndx.get(vtxCtr+2)/Float.BYTES); (normalFunc).run(gl2, normalPtr);
    	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+2]/Float.BYTES); (vertexFunc).run(gl2, vertexPtr);
    	vtxCtr += 4; // Skip past END_OF_FACE_INDEX
    }
    gl2.glEnd();
}


public void
QuadOmVn
    (SoGLRenderAction action ) {
	
	GL2 gl2 = Ctx.get(action.getCacheContext());
	
    final int[] vertexIndex = coordIndex.getValuesI(0);
    Buffer vertexPtr = vpCache.getVertices(0);
    final int vertexStride = vpCache.getVertexStride();
    SoVPCacheFunc vertexFunc = vpCache.vertexFunc;
    Buffer normalPtr = vpCache.getNormals(0);
    final int normalStride = vpCache.getNormalStride();
    SoVPCacheFunc normalFunc = vpCache.normalFunc;
    IntArray normalIndx = getNormalIndices();

    gl2.glBegin(GL2.GL_QUADS);
    int vtxCtr = numTris*4;
    for (int quad = 0; quad < numQuads; quad++) {

//	(*normalFunc)(normalPtr+normalStride*normalIndx[vtxCtr]);
//	(*vertexFunc)(vertexPtr+vertexStride*vertexIndex[vtxCtr]);
//
//	(*normalFunc)(normalPtr+normalStride*normalIndx[vtxCtr+1]);
//	(*vertexFunc)(vertexPtr+vertexStride*vertexIndex[vtxCtr+1]);
//
//	(*normalFunc)(normalPtr+normalStride*normalIndx[vtxCtr+2]);
//	(*vertexFunc)(vertexPtr+vertexStride*vertexIndex[vtxCtr+2]);
//
//	(*normalFunc)(normalPtr+normalStride*normalIndx[vtxCtr+3]);
//	(*vertexFunc)(vertexPtr+vertexStride*vertexIndex[vtxCtr+3]);
    	normalPtr.position(normalStride*normalIndx.get(vtxCtr)/Float.BYTES); (normalFunc).run(gl2, normalPtr);
    	vertexPtr.position(vertexStride*vertexIndex[vtxCtr]/Float.BYTES); (vertexFunc).run(gl2, vertexPtr);

    	normalPtr.position(normalStride*normalIndx.get(vtxCtr+1)/Float.BYTES); (normalFunc).run(gl2, normalPtr);
    	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+1]/Float.BYTES); (vertexFunc).run(gl2, vertexPtr);

    	normalPtr.position(normalStride*normalIndx.get(vtxCtr+2)/Float.BYTES); (normalFunc).run(gl2, normalPtr);
    	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+2]/Float.BYTES); (vertexFunc).run(gl2, vertexPtr);

    	normalPtr.position(normalStride*normalIndx.get(vtxCtr+3)/Float.BYTES); (normalFunc).run(gl2, normalPtr);
    	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+3]/Float.BYTES); (vertexFunc).run(gl2, vertexPtr);
	vtxCtr += 5; // Skip past END_OF_FACE_INDEX
    }
    gl2.glEnd();
}



public void TriFmVn (SoGLRenderAction action) {
	
	GL2 gl2 = Ctx.get(action.getCacheContext());
	
    final int[] vertexIndex = coordIndex.getValuesI(0);
    Buffer vertexPtr = vpCache.getVertices(0);
    final int vertexStride = vpCache.getVertexStride();
    SoVPCacheFunc vertexFunc = vpCache.vertexFunc;
    Buffer colorPtr = vpCache.getColors(0).toBuffer();
    final int colorStride = vpCache.getColorStride();
    SoVPCacheFunc colorFunc = vpCache.colorFunc;
    IntArray colorIndx = getColorIndices();
    Buffer normalPtr = vpCache.getNormals(0);
    final int normalStride = vpCache.getNormalStride();
    SoVPCacheFunc normalFunc = vpCache.normalFunc;
    IntArray normalIndx = getNormalIndices();

    gl2.glBegin(GL2.GL_TRIANGLES);
    int vtxCtr = 0;
    for (int tri = 0; tri < numTris; tri++) {
	colorPtr.position(colorStride*colorIndx.get(tri)/Integer.BYTES);(colorFunc).run(gl2, colorPtr);

	normalPtr.position(normalStride*normalIndx.get(vtxCtr)/Float.BYTES);(normalFunc).run(gl2, normalPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr]/Float.BYTES);(vertexFunc).run(gl2, vertexPtr);

	normalPtr.position(normalStride*normalIndx.get(vtxCtr+1)/Float.BYTES);(normalFunc).run(gl2, normalPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+1]/Float.BYTES);(vertexFunc).run(gl2, vertexPtr);

	normalPtr.position(normalStride*normalIndx.get(vtxCtr+2)/Float.BYTES);(normalFunc).run(gl2, normalPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+2]/Float.BYTES);(vertexFunc).run(gl2, vertexPtr);
	vtxCtr += 4; // Skip past END_OF_FACE_INDEX
    }
    gl2.glEnd();
}


public void

QuadFmVn
    (SoGLRenderAction action) {
	
	GL2 gl2 = Ctx.get(action.getCacheContext());
	
    final int[] vertexIndex = coordIndex.getValuesI(0);
    Buffer vertexPtr = vpCache.getVertices(0);
    final int vertexStride = vpCache.getVertexStride();
    SoVPCacheFunc vertexFunc = vpCache.vertexFunc;
    Buffer colorPtr = vpCache.getColors(0).toBuffer();
    final int colorStride = vpCache.getColorStride();
    SoVPCacheFunc colorFunc = vpCache.colorFunc;
    IntArray colorIndx = getColorIndices();
    Buffer normalPtr = vpCache.getNormals(0);
    final int normalStride = vpCache.getNormalStride();
    SoVPCacheFunc normalFunc = vpCache.normalFunc;
    IntArray normalIndx = getNormalIndices();

    gl2.glBegin(GL2.GL_QUADS);
    int vtxCtr = numTris*4;
    int faceCtr = numTris;
    for (int quad = 0; quad < numQuads; quad++) {
    	colorPtr.position(colorStride*colorIndx.get(faceCtr)/Integer.BYTES);
	(colorFunc).run(gl2,colorPtr/*+colorStride*colorIndx[faceCtr]*/);
	++faceCtr;

	normalPtr.position(normalStride*normalIndx.get(vtxCtr)/Float.BYTES);
	(normalFunc).run(gl2,normalPtr/*+normalStride*normalIndx[vtxCtr]*/);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr]/Float.BYTES);
	(vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr]*/);

	normalPtr.position(normalStride*normalIndx.get(vtxCtr+1)/Float.BYTES);
	(normalFunc).run(gl2,normalPtr/*+normalStride*normalIndx[vtxCtr+1]*/);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+1]/Float.BYTES);
	(vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr+1]*/);

	normalPtr.position(normalStride*normalIndx.get(vtxCtr+2)/Float.BYTES);
	(normalFunc).run(gl2,normalPtr/*+normalStride*normalIndx[vtxCtr+2]*/);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+2]/Float.BYTES);
	(vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr+2]*/);

	normalPtr.position(normalStride*normalIndx.get(vtxCtr+3)/Float.BYTES);
	(normalFunc).run(gl2,normalPtr/*+normalStride*normalIndx[vtxCtr+3]*/);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+3]/Float.BYTES);
	(vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr+3]*/);
	vtxCtr += 5; // Skip past END_OF_FACE_INDEX
    }
    gl2.glEnd();
}



public void TriVmVn (SoGLRenderAction action ) {
	
	GL2 gl2 = Ctx.get(action.getCacheContext());
	
    final int[] vertexIndex = coordIndex.getValuesI(0);
    Buffer vertexPtr = vpCache.getVertices(0);
    final int vertexStride = vpCache.getVertexStride();
    SoVPCacheFunc vertexFunc = vpCache.vertexFunc;
    Buffer colorPtr = vpCache.getColors(0).toBuffer();
    final int colorStride = vpCache.getColorStride();
    SoVPCacheFunc colorFunc = vpCache.colorFunc;
    IntArray colorIndx = getColorIndices();
    Buffer normalPtr = vpCache.getNormals(0);
    final int normalStride = vpCache.getNormalStride();
    SoVPCacheFunc normalFunc = vpCache.normalFunc;
    IntArray normalIndx = getNormalIndices();

    gl2.glBegin(GL2.GL_TRIANGLES);
    int vtxCtr = 0;
    for (int tri = 0; tri < numTris; tri++) {

    	colorPtr.position(colorStride*colorIndx.get(vtxCtr)/Integer.BYTES);(colorFunc).run(gl2, colorPtr);
    	normalPtr.position(normalStride*normalIndx.get(vtxCtr)/Float.BYTES);(normalFunc).run(gl2, normalPtr);
    	vertexPtr.position(vertexStride*vertexIndex[vtxCtr]/Float.BYTES);(vertexFunc).run(gl2, vertexPtr);

	colorPtr.position(colorStride*colorIndx.get(vtxCtr+1)/Integer.BYTES);(colorFunc).run(gl2, colorPtr);
	normalPtr.position(normalStride*normalIndx.get(vtxCtr+1)/Float.BYTES);(normalFunc).run(gl2, normalPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+1]/Float.BYTES);(vertexFunc).run(gl2, vertexPtr);

	colorPtr.position(colorStride*colorIndx.get(vtxCtr+2)/Integer.BYTES);(colorFunc).run(gl2, colorPtr);
	normalPtr.position(normalStride*normalIndx.get(vtxCtr+2)/Float.BYTES);(normalFunc).run(gl2, normalPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+2]/Float.BYTES);(vertexFunc).run(gl2, vertexPtr);
	vtxCtr += 4; // Skip past END_OF_FACE_INDEX
    }
    gl2.glEnd();
}


public void GenOmVn(SoGLRenderAction action)
{
	
	GL2 gl2 = Ctx.get(action.getCacheContext());
	
    final int[] vertexIndex = coordIndex.getValuesI(0);
    final int numVI = coordIndex.getNum();
    Buffer vertexPtr = vpCache.getVertices(0);
    final int vertexStride = vpCache.getVertexStride();
    SoVPCacheFunc vertexFunc = vpCache.vertexFunc;
    Buffer normalPtr = vpCache.getNormals(0);
    final int normalStride = vpCache.getNormalStride();
    SoVPCacheFunc normalFunc = vpCache.normalFunc;
    IntArray normalIndx = getNormalIndices();
    int vtxCtr = numQuads*5 + numTris*4;
    while (vtxCtr < numVI) {
	gl2.glBegin(GL2.GL_POLYGON);
	while (vtxCtr < numVI && (vertexIndex[vtxCtr] != SO_END_FACE_INDEX)) {
		normalPtr.position(normalStride*normalIndx.get(vtxCtr)/Float.BYTES);
	    (normalFunc).run(gl2, normalPtr);
	    vertexPtr.position(vertexStride*vertexIndex[vtxCtr]/Float.BYTES);
	    (vertexFunc).run(gl2, vertexPtr);
	    vtxCtr++;
	}
	vtxCtr++; // Skip over END_FACE_INDEX
	gl2.glEnd();
    }
}


public void
GenVmOn
    (SoGLRenderAction action )
{
	
	GL2 gl2 = Ctx.get(action.getCacheContext());
	
    final int[] vertexIndex = coordIndex.getValuesI(0);
    final int numVI = coordIndex.getNum();
    // Send one normal, if there are any normals in vpCache:
    if (vpCache.getNumNormals() > 0)
	vpCache.sendNormal(gl2,(FloatBuffer)vpCache.getNormals(0)/*.asFloatBuffer()*/);
    Buffer vertexPtr = vpCache.getVertices(0);
    final int vertexStride = vpCache.getVertexStride();
    SoVPCacheFunc vertexFunc = vpCache.vertexFunc;
    Buffer colorPtr = vpCache.getColors(0).toBuffer();
    final int colorStride = vpCache.getColorStride();
    SoVPCacheFunc colorFunc = vpCache.colorFunc;
    IntArray colorIndx = getColorIndices();
    int vtxCtr = numQuads*5 + numTris*4;
    while (vtxCtr < numVI) {
	gl2.glBegin(GL2.GL_POLYGON);
	while (vtxCtr < numVI &&
	       (vertexIndex[vtxCtr] != SO_END_FACE_INDEX)) {
		colorPtr.position(colorStride*colorIndx.get(vtxCtr)/Integer.BYTES);
	    (colorFunc).run(gl2,colorPtr/*+colorStride*colorIndx[vtxCtr]*/);
	    vertexPtr.position(vertexStride*vertexIndex[vtxCtr]/Float.BYTES);
	    (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr]*/);
	    vtxCtr++;
	}
	vtxCtr++; // Skip over END_FACE_INDEX
	gl2.glEnd();
    }
}


public void

GenFmVn
    (SoGLRenderAction action)
{
	
	GL2 gl2 = Ctx.get(action.getCacheContext());
	
    final int[] vertexIndex = coordIndex.getValuesI(0);
    final int numVI = coordIndex.getNum();
    Buffer vertexPtr = vpCache.getVertices(0);
    final int vertexStride = vpCache.getVertexStride();
    SoVPCacheFunc vertexFunc = vpCache.vertexFunc;
    Buffer colorPtr = vpCache.getColors(0).toBuffer();
    final int colorStride = vpCache.getColorStride();
    SoVPCacheFunc colorFunc = vpCache.colorFunc;
    IntArray colorIndx = getColorIndices();
    Buffer normalPtr = vpCache.getNormals(0);
    final int normalStride = vpCache.getNormalStride();
    SoVPCacheFunc normalFunc = vpCache.normalFunc;
    final IntArray normalIndx = getNormalIndices();
    int vtxCtr = numQuads*5 + numTris*4;
    int faceCtr = numQuads + numTris;
    while (vtxCtr < numVI) {
    	colorPtr.position(colorStride*colorIndx.get(faceCtr)/Integer.BYTES);
	(colorFunc).run(gl2,colorPtr/*+colorStride*colorIndx[faceCtr]*/);
	++faceCtr;
	gl2.glBegin(GL2.GL_POLYGON);
	while (vtxCtr < numVI &&
	       (vertexIndex[vtxCtr] != SO_END_FACE_INDEX)) {
		normalPtr.position(normalStride*normalIndx.get(vtxCtr)/Float.BYTES);
	    (normalFunc).run(gl2,normalPtr/*+normalStride*normalIndx[vtxCtr]*/);
	    vertexPtr.position(vertexStride*vertexIndex[vtxCtr]/Float.BYTES);
	    (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr]*/);
	    vtxCtr++;
	}
	vtxCtr++; // Skip over END_FACE_INDEX
	gl2.glEnd();
    }
}


public void

GenOmFn
    (SoGLRenderAction action)
{
	
	GL2 gl2 = Ctx.get(action.getCacheContext());
	
    final int[] vertexIndex = coordIndex.getValuesI(0);
    final int numVI = coordIndex.getNum();
    Buffer vertexPtr = vpCache.getVertices(0);
    final int vertexStride = vpCache.getVertexStride();
    SoVPCacheFunc vertexFunc = vpCache.vertexFunc;
    Buffer normalPtr = vpCache.getNormals(0);
    final int normalStride = vpCache.getNormalStride();
    SoVPCacheFunc normalFunc = vpCache.normalFunc;
    final IntArray normalIndx = getNormalIndices();
    int vtxCtr = numQuads*5 + numTris*4;
    int faceCtr = numQuads + numTris;
    while (vtxCtr < numVI) {
    	normalPtr.position(+normalStride*normalIndx.get(faceCtr)/Float.BYTES);
	(normalFunc).run(gl2,normalPtr/*+normalStride*normalIndx[faceCtr]*/);
	++faceCtr;
	gl2.glBegin(GL2.GL_POLYGON);
	while (vtxCtr < numVI &&
	       (vertexIndex[vtxCtr] != SO_END_FACE_INDEX)) {
		vertexPtr.position(vertexStride*vertexIndex[vtxCtr]/Float.BYTES);
	    (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr]*/);
	    vtxCtr++;
	}
	vtxCtr++; // Skip over END_FACE_INDEX
	gl2.glEnd();
    }
}




}
