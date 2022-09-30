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
  \class SoVRMLIndexedFaceSet SoVRMLIndexedFaceSet.h Inventor/VRMLnodes/SoVRMLIndexedFaceSet.h
  \brief The SoVRMLIndexedFaceSet class is used for representing a generic 3D shape.

  \ingroup VRMLnodes

  \WEB3DCOPYRIGHT

  \verbatim
  IndexedFaceSet {
    eventIn       MFInt32 set_colorIndex
    eventIn       MFInt32 set_coordIndex
    eventIn       MFInt32 set_normalIndex
    eventIn       MFInt32 set_texCoordIndex
    exposedField  SFNode  color             NULL
    exposedField  SFNode  coord             NULL
    exposedField  SFNode  normal            NULL
    exposedField  SFNode  texCoord          NULL
    field         SFBool  ccw               TRUE
    field         MFInt32 colorIndex        []        # [-1,)
    field         SFBool  colorPerVertex    TRUE
    field         SFBool  convex            TRUE
    field         MFInt32 coordIndex        []        # [-1,)
    field         SFFloat creaseAngle       0         # [0,)
    field         MFInt32 normalIndex       []        # [-1,)
    field         SFBool  normalPerVertex   TRUE
    field         SFBool  solid             TRUE
    field         MFInt32 texCoordIndex     []        # [-1,)
  }
  \endverbatim

  The IndexedFaceSet node represents a 3D shape formed by constructing
  faces (polygons) from vertices listed in the coord field. The coord
  field contains a Coordinate node that defines the 3D vertices
  referenced by the coordIndex field. IndexedFaceSet uses the indices
  in its coordIndex field to specify the polygonal faces by indexing
  into the coordinates in the Coordinate node. An index of "-1"
  indicates that the current face has ended and the next one
  begins. The last face may be (but does not have to be) followed by a
  "-1" index. If the greatest index in the coordIndex field is N, the
  Coordinate node shall contain N+1 coordinates (indexed as 0 to
  N). Each face of the IndexedFaceSet shall have:

  - at least three non-coincident vertices;
  - vertices that define a planar polygon;
  - vertices that define a non-self-intersecting polygon.

  Otherwise, The results are undefined.

  The IndexedFaceSet node is specified in the local coordinate system
  and is affected by the transformations of its ancestors.

  Descriptions of the coord, normal, and texCoord fields are provided
  in the SoVRMLCoordinate, SoVRMLNormal, and SoVRMLTextureCoordinate nodes,
  respectively.

  Details on lighting equations and the interaction
  between color field, normal field, textures, materials, and
  geometries are provided in 4.14, Lighting model.

  If the color field is not NULL, it shall contain a Color node whose
  colours are applied to the vertices or faces of the IndexedFaceSet
  as follows:

  - If colorPerVertex is FALSE, colours are applied to each face, as
    follows:

    - If the colorIndex field is not empty, then one colour is used
      for each face of the IndexedFaceSet. There shall be at least as many indices
      in the colorIndex field as there are faces in the IndexedFaceSet.
      If the greatest index in the colorIndex field is N, then there shall
      be N+1 colours in the Color node. The colorIndex field shall not
      contain any negative entries.

    - If the colorIndex field is empty, then the colours in the Color
      node are applied to each face of the IndexedFaceSet in order. There shall
      be at least as many colours in the Color node as there are faces.

  - If colorPerVertex is TRUE, colours are applied to each vertex,
    as follows:

    - If the colorIndex field is not empty, then colours are applied
      to each vertex of the IndexedFaceSet in exactly the same manner
      that the coordIndex field is used to choose coordinates for each
      vertex from the Coordinate node. The colorIndex field shall
      contain at least as many indices as the coordIndex field, and
      shall contain end-of-face markers (-1) in exactly the same places
      as the coordIndex field.  If the greatest index in the colorIndex
      field is N, then there shall be N+1 colours in the Color node.

    - If the colorIndex field is empty, then the coordIndex field is
      used to choose colours from the Color node. If the greatest index
      in the coordIndex field is N, then there shall be N+1 colours in
      the Color node.

  If the color field is NULL, the geometry shall be rendered normally
  using the Material and texture defined in the Appearance node (see
  4.14, Lighting model, for details
  http://www.web3d.org/documents/specifications/14772/V2.0/part1/concepts.html#4.6.5).

  If the normal field is not NULL, it shall contain a Normal node
  whose normals are applied to the vertices or faces of the
  IndexedFaceSet in a manner exactly equivalent to that described
  above for applying colours to vertices/faces (where normalPerVertex
  corresponds to colorPerVertex and normalIndex corresponds to
  colorIndex). If the normal field is NULL, the browser shall
  automatically generate normals, using creaseAngle to determine if
  and how normals are smoothed across shared vertices (see 4.6.3.5,
  Crease angle field).

  If the texCoord field is not NULL, it shall contain a
  TextureCoordinate node. The texture coordinates in that node are
  applied to the vertices of the IndexedFaceSet as follows: If the
  texCoordIndex field is not empty, then it is used to choose texture
  coordinates for each vertex of the IndexedFaceSet in exactly the
  same manner that the coordIndex field is used to choose coordinates
  for each vertex from the Coordinate node. The texCoordIndex field
  shall contain at least as many indices as the coordIndex field, and
  shall contain end-of-face markers (-1) in exactly the same places as
  the coordIndex field. If the greatest index in the texCoordIndex
  field is N, then there shall be N+1 texture coordinates in the
  TextureCoordinate node.

  If the texCoordIndex field is empty, then the coordIndex array is
  used to choose texture coordinates from the TextureCoordinate
  node. If the greatest index in the coordIndex field is N, then there
  shall be N+1 texture coordinates in the TextureCoordinate node.  If
  the texCoord field is NULL, a default texture coordinate mapping is
  calculated using the local coordinate system bounding box of the
  shape.  The longest dimension of the bounding box defines the S
  coordinates, and the next longest defines the T coordinates. If two
  or all three dimensions of the bounding box are equal, ties shall be
  broken by choosing the X, Y, or Z dimension in that order of
  preference. The value of the S coordinate ranges from 0 to 1, from
  one end of the bounding box to the other. The T coordinate ranges
  between 0 and the ratio of the second greatest dimension of the
  bounding box to the greatest dimension. Figure 6.10 illustrates the
  default texture coordinates for a simple box shaped IndexedFaceSet
  with an X dimension twice as large as the Z dimension and four times
  as large as the Y dimension. Figure 6.11 illustrates the original
  texture image used on the IndexedFaceSet used in Figure 6.10.

  <center>
  <img src="http://www.web3d.org/documents/specifications/14772/V2.0/Images/IFStexture.gif">
  Figure 6.10
  </center>

  <center>
  <img src="http://www.web3d.org/documents/specifications/14772/V2.0/Images/IFStexture2.gif">
  Figure 6.11
  </center>

  Subclause 4.6.3, Shapes and geometry
  (<http://www.web3d.org/documents/specifications/14772/V2.0/part1/concepts.html#4.6.5>),
  provides a description of the ccw, solid, convex, and creaseAngle
  fields.

*/

/*!
  \var SoSFBool SoVRMLIndexedFaceSet::ccw
  Specifies if vertex ordering is counterclockwise. Default value is TRUE.
*/

/*!
  \var SoSFBool SoVRMLIndexedFaceSet::solid
  Can be used to enable backface culling. Default value is TRUE.
*/

/*!
  \var SoSFBool SoVRMLIndexedFaceSet::convex
  Specifies if all polygons are convex. Default value is TRUE.
*/

/*!
  \var SoSFFloat SoVRMLIndexedFaceSet::creaseAngle
  Specifies the crease angle for the generated normals. Default value is 0.0.
*/

package jscenegraph.coin3d.inventor.VRMLnodes;

import java.util.Objects;

import jscenegraph.opengl.GL2;

import jscenegraph.coin3d.inventor.bundles.SoVertexAttributeBundle;
import jscenegraph.coin3d.inventor.elements.SoVertexAttributeBindingElement;
import jscenegraph.coin3d.inventor.misc.SoGLDriverDatabase;
import jscenegraph.coin3d.inventor.rendering.SoVertexArrayIndexer;
import jscenegraph.coin3d.misc.SoGL;
import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec3fSingle;
import jscenegraph.database.inventor.SoPrimitiveVertex;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoGetPrimitiveCountAction;
import jscenegraph.database.inventor.bundles.SoMaterialBundle;
import jscenegraph.database.inventor.bundles.SoNormalBundle;
import jscenegraph.database.inventor.bundles.SoTextureCoordinateBundle;
import jscenegraph.database.inventor.caches.SoConvexDataCache;
import jscenegraph.database.inventor.caches.SoNormalCache;
import jscenegraph.database.inventor.details.SoFaceDetail;
import jscenegraph.database.inventor.details.SoPointDetail;
import jscenegraph.database.inventor.elements.SoCacheElement;
import jscenegraph.database.inventor.elements.SoCoordinateElement;
import jscenegraph.database.inventor.elements.SoGLCoordinateElement;
import jscenegraph.database.inventor.elements.SoGLLazyElement;
import jscenegraph.database.inventor.elements.SoLazyElement;
import jscenegraph.database.inventor.elements.SoMaterialBindingElement;
import jscenegraph.database.inventor.elements.SoModelMatrixElement;
import jscenegraph.database.inventor.elements.SoNormalBindingElement;
import jscenegraph.database.inventor.elements.SoOverrideElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFBool;
import jscenegraph.database.inventor.fields.SoSFFloat;
import jscenegraph.database.inventor.misc.SoBase;
import jscenegraph.database.inventor.misc.SoNotList;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.mevis.inventor.elements.SoGLVBOElement;
import jscenegraph.mevis.inventor.misc.SoVBO;
import jscenegraph.port.Destroyable;
import jscenegraph.port.IntArrayPtr;
import jscenegraph.port.MutableSbVec3fArray;
import jscenegraph.port.SbVec3fArray;

/**
 * @author BOYADJIAN
 *
 */
public class SoVRMLIndexedFaceSet extends SoVRMLIndexedShape {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVRMLIndexedFaceSet.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLIndexedFaceSet.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLIndexedFaceSet.class); }

	// for concavestatus
	  public static final int STATUS_UNKNOWN = 0;
	  public static final int STATUS_CONVEX  = 1;
	  public static final int STATUS_CONCAVE = 2;

private

  enum Binding {
    //OVERALL,
    NONE_OVERALL,
    PER_FACE,
    PER_FACE_INDEXED,
    PER_VERTEX,
    PER_VERTEX_INDEXED;

	int getValue() {
		return ordinal();
	}
  };

	  
	  
	  public final SoSFBool ccw = new SoSFBool();
	  public final SoSFBool solid = new SoSFBool();
	  public final SoSFBool convex = new SoSFBool();
	  public final SoSFFloat creaseAngle = new SoSFFloat();	  
	  
	  private SoVRMLIndexedFaceSetP pimpl;
	  
	  /*!
	  Constructor.
	*/
	public SoVRMLIndexedFaceSet()
	{
	  pimpl = new SoVRMLIndexedFaceSetP();
	  pimpl.convexCache = null;
	  pimpl.concavestatus = STATUS_UNKNOWN;
	  pimpl.vaindexer = null;

	  nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLIndexedFaceSet.class);

	  nodeHeader.SO_VRMLNODE_ADD_FIELD(ccw,"ccw", (true));
	  nodeHeader.SO_VRMLNODE_ADD_FIELD(solid,"solid", (true));
	  nodeHeader.SO_VRMLNODE_ADD_FIELD(convex,"convex", (true));
	  nodeHeader.SO_VRMLNODE_ADD_FIELD(creaseAngle,"creaseAngle", (0.0f));

	}


/*!
  Destructor.
*/
public void destructor() // virtual, protected
{
  if (pimpl.convexCache != null) pimpl.convexCache.unref();
  Destroyable.delete(pimpl.vaindexer);
  Destroyable.delete(pimpl);
}

//
// translates current material binding into the internal Binding enum.
//
SoVRMLIndexedFaceSet.Binding findMaterialBinding(SoState state)
{
  Binding binding = Binding.NONE_OVERALL;

  if (SoOverrideElement.getMaterialBindingOverride(state)) {
    switch (SoMaterialBindingElement.get(state)) {
    case OVERALL:
      binding = Binding.NONE_OVERALL;
      break;
    case PER_VERTEX:
      binding = Binding.PER_VERTEX;
      break;
    case PER_VERTEX_INDEXED:
      binding = Binding.PER_VERTEX_INDEXED;
      break;
    case PER_PART:
    case PER_FACE:
      binding = Binding.PER_FACE;
      break;
    case PER_PART_INDEXED:
    case PER_FACE_INDEXED:
      binding = Binding.PER_FACE_INDEXED;
      break;
    default:
//#if COIN_DEBUG
      SoDebugError.postWarning("SoVRMLIndexedFaceSet::findMaterialBinding",
                                "unknown material binding setting");
//#endif // COIN_DEBUG
      break;
    }
  }
  else {
    if (this.color.getValue() != null) {
      if (this.colorPerVertex.getValue()) {
        binding = Binding.PER_VERTEX;
        if (this.colorIndex.getNum() != 0 && this.colorIndex.operator_square_bracketI(0) >= 0) 
          binding = Binding.PER_VERTEX_INDEXED;
      }
      else {
        binding = Binding.PER_FACE;
        if (this.colorIndex.getNum() != 0 && this.colorIndex.operator_square_bracketI(0) >= 0) 
          binding = Binding.PER_FACE_INDEXED;
      }
    }
  }
  return binding;
}


//
// translates current normal binding into the internal Binding enum.
//
SoVRMLIndexedFaceSet.Binding
findNormalBinding(SoState state)
{
  Binding binding = SoVRMLIndexedFaceSet.Binding.NONE_OVERALL;

  if (SoOverrideElement.getNormalBindingOverride(state)) {
    switch (SoNormalBindingElement.get(state)) {
    case OVERALL:
      binding = Binding.NONE_OVERALL;
      break;
    case PER_VERTEX:
      binding = Binding.PER_VERTEX;
      break;
    case PER_VERTEX_INDEXED:
      binding = Binding.PER_VERTEX_INDEXED;
      break;
    case PER_PART:
    case PER_FACE:
      binding = Binding.PER_FACE;
      break;
    case PER_PART_INDEXED:
    case PER_FACE_INDEXED:
      binding = Binding.PER_FACE_INDEXED;
      break;
    default:
//#if COIN_DEBUG
      SoDebugError.postWarning("SoVRMLIndexedFaceSet::findNormalBinding",
                                "unknown normal binding setting");
//#endif // COIN_DEBUG
      break;
    }
  }
  else {
    if (this.normalPerVertex.getValue()) {
      binding = Binding.PER_VERTEX_INDEXED;
      if (this.normal.getValue() != null && 
          (this.normalIndex.getNum() == 0 ||
           this.normalIndex.operator_square_bracketI(0) < 0)) binding = Binding.PER_VERTEX;
    }
    else {
      binding = Binding.PER_FACE;
      if (this.normalIndex.getNum() != 0 && this.normalIndex.operator_square_bracketI(0) >= 0) binding = Binding.PER_FACE_INDEXED;
    }
  }
  return binding;
}


// Doc in parent
public void GLRender(SoGLRenderAction action)
{
  if (this.coordIndex.getNum() < 3 || this.coord.getValue() == null) return;
  SoState state = action.getState();

  GL2 gl2 = state.getGL2();
  
  state.push();
  // update state with coordinates, normals and texture information
  SoVRMLVertexShape_GLRender(action);

  if (!this.shouldGLRender(action)) { 
    state.pop();
    return;
  }

  this.setupShapeHints(state, this.ccw.getValue(), this.solid.getValue());

  Binding mbind = this.findMaterialBinding(state);
  Binding nbind = this.findNormalBinding(state);

  final SoCoordinateElement[] coords = new SoCoordinateElement[1]; //ptr
  final SbVec3fArray[] normals = new SbVec3fArray[1];
  final IntArrayPtr[] cindices = new IntArrayPtr[1];
  final int[] numindices = new int[1];
  final IntArrayPtr[] nindices = new IntArrayPtr[1];
  final IntArrayPtr[] tindices = new IntArrayPtr[1];
  final IntArrayPtr[] mindices = new IntArrayPtr[1];
  boolean doTextures;
  final boolean[] normalCacheUsed = new boolean[1];
  final SoMaterialBundle mb = new SoMaterialBundle(action);
  SoTextureCoordinateBundle tb = new SoTextureCoordinateBundle(action, true, false);
  doTextures = tb.needCoordinates();

  boolean sendNormals = !mb.isColorOnly() || tb.isFunction();

  this.getVertexData(state, coords, normals, cindices,
                      nindices, tindices, mindices, numindices,
                      sendNormals, normalCacheUsed);

  if (!sendNormals) {
    nbind = Binding.NONE_OVERALL;
    normals[0] = null;
    nindices[0] = null;
  }
  else if (nbind == Binding.NONE_OVERALL) {
    if (normals[0] != null) gl2.glNormal3fv(normals[0].get(0).getValueRead());
    else gl2.glNormal3f(0.0f, 0.0f, 1.0f);
  }
  else if (normalCacheUsed[0] && nbind == Binding.PER_VERTEX) {
    nbind = Binding.PER_VERTEX_INDEXED;
  }
  else if (normalCacheUsed[0] && nbind == Binding.PER_FACE_INDEXED) {
    nbind = Binding.PER_FACE;
  }

  if (mbind == Binding.PER_VERTEX) {
    mbind = Binding.PER_VERTEX_INDEXED;
    mindices[0] = cindices[0];
  }
  if (nbind == Binding.PER_VERTEX) {
    nbind = Binding.PER_VERTEX_INDEXED;
    nindices[0] = cindices[0];
  }

  Binding tbind = Binding.NONE_OVERALL;
  if (doTextures) {
    if (tb.isFunction() && !tb.needIndices()) {
      tbind = Binding.NONE_OVERALL;
      tindices[0] = null;
    }
    else {
      tbind = Binding.PER_VERTEX_INDEXED;
      if (tindices[0] == null) tindices[0] = cindices[0];
    }
  }
  boolean convexcacheused = false;

  if (this.useConvexCache(action, normals[0], nindices, normalCacheUsed[0])) {
    cindices[0] = pimpl.convexCache.getCoordIndices();
    numindices[0] = pimpl.convexCache.getNumCoordIndices();
    mindices[0] = pimpl.convexCache.getMaterialIndices();
    nindices[0] = pimpl.convexCache.getNormalIndices();
    tindices[0] = pimpl.convexCache.getTexIndices();

    if (mbind == Binding.PER_VERTEX) mbind = Binding.PER_VERTEX_INDEXED;
    else if (mbind == Binding.PER_FACE) mbind = Binding.PER_FACE_INDEXED;
    if (nbind == Binding.PER_VERTEX) nbind = Binding.PER_VERTEX_INDEXED;
    else if (nbind == Binding.PER_FACE) nbind = Binding.PER_FACE_INDEXED;

    if (tbind != Binding.NONE_OVERALL) tbind = Binding.PER_VERTEX_INDEXED;
    convexcacheused = true;
  }

  mb.sendFirst(); // make sure we have the correct material

  SoGLLazyElement lelem = null;
  int contextid = action.getCacheContext();

  boolean dova = 
    SoVBO.shouldRenderAsVertexArrays(state, contextid, numindices[0]) &&
    !convexcacheused && !normalCacheUsed[0] &&
    ((nbind == Binding.NONE_OVERALL) || ((nbind == Binding.PER_VERTEX_INDEXED) && ((Objects.equals(nindices[0], cindices[0])) || (nindices[0] == null)))) &&
    ((tbind == Binding.NONE_OVERALL && !tb.needCoordinates()) || 
     ((tbind == Binding.PER_VERTEX_INDEXED) && ((Objects.equals(tindices[0], cindices[0])) || (tindices[0] == null)))) &&
    ((mbind == Binding.NONE_OVERALL) || ((mbind == Binding.PER_VERTEX_INDEXED) && ((Objects.equals(mindices[0], cindices[0])) || (mindices[0] == null)))) &&
    SoGLDriverDatabase.isSupported(SoGL.sogl_glue_instance(state), SoGLDriverDatabase.SO_GL_VERTEX_ARRAY);

  final SoGLVBOElement vboelem = SoGLVBOElement.getInstance(state);
  SoVBO colorvbo = null;

  if (dova && (mbind != Binding.NONE_OVERALL)) {
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
  boolean didrenderasvbo = false;
  if (dova) {
    boolean dovbo = this.startVertexArray(action,
                                          coords[0],
                                          (nbind != Binding.NONE_OVERALL) ? normals[0] : null,
                                          doTextures,
                                          mbind != Binding.NONE_OVERALL);
    didrenderasvbo = dovbo;
    SoBase.staticDataLock();//LOCK_VAINDEXER(this);
    if (pimpl.vaindexer == null) {
      SoVertexArrayIndexer indexer = new SoVertexArrayIndexer();
      int i = 0;
      while (i < numindices[0]) {
        int cnt = 0;
        while (i + cnt < numindices[0] && cindices[0].get(i+cnt) >= 0) cnt++;
        
        switch (cnt) {
        case 3:
          indexer.addTriangle(cindices[0].get(i),cindices[0].get(i+1), cindices[0].get(i+2));
          break;
        case 4:
          indexer.addQuad(cindices[0].get(i),cindices[0].get(i+1),cindices[0].get(i+2),cindices[0].get(i+3));
          break;
        default:
          if (cnt > 4) {
            indexer.beginTarget(GL2.GL_POLYGON);
            for (int j = 0; j < cnt; j++) {
              indexer.targetVertex(GL2.GL_POLYGON, cindices[0].get(i+j));
            }
            indexer.endTarget(GL2.GL_POLYGON);
          }
        }
        i += cnt + 1;
      }
      indexer.close();
      if (indexer.getNumVertices() != 0) {
        pimpl.vaindexer = indexer;
      }
      else {
        Destroyable.delete(indexer);
      }
//#if 0
//      fprintf(stderr,"XXX: create VRML VertexArrayIndexer: %d\n", indexer.getNumVertices());
//#endif
    }

    if (pimpl.vaindexer != null) {
      pimpl.vaindexer.render(SoGL.sogl_glue_instance(state), dovbo, contextid);
    }
    SoBase.staticDataUnlock();//UNLOCK_VAINDEXER(this);
    this.finishVertexArray(action,
                            dovbo,
                            (nbind != Binding.NONE_OVERALL),
                            doTextures,
                            mbind != Binding.NONE_OVERALL);
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

    SoGL.sogl_render_faceset((SoGLCoordinateElement )coords[0],
                        cindices[0],
                        numindices[0],
                        normals[0],
                        nindices[0],
                        mb,
                        mindices[0],
                        tb,
                        tindices[0],
                        vab,
                        (int)nbind.getValue(),
                        (int)mbind.getValue(),
                        (int)attribbind.getValue(),
                        doTextures ? 1 : 0,
                        doattribs ? 1 : 0);

    vab.destructor();
  }
  if (normalCacheUsed[0]) {
    this.readUnlockNormalCache();
  }

  if (convexcacheused) {
    pimpl.readUnlockConvexCache();
  }

  // send approx number of triangles for autocache handling
  SoGL.sogl_autocache_update(state, this.coordIndex.getNum() / 4, didrenderasvbo);

  state.pop();
  
  tb.destructor();
  mb.destructor();
}

// Doc in parent
public void getPrimitiveCount(SoGetPrimitiveCountAction action)
{
  if (!this.shouldPrimitiveCount(action)) return;

  int n = this.coordIndex.getNum();
  if (n < 3) return;

  if (action.canApproximateCount()) {
    action.addNumTriangles(n/4);
  }
  else {
    IntArrayPtr ptr = this.coordIndex.getValuesIntArrayPtr(0);
    IntArrayPtr endptr = IntArrayPtr.plus(ptr,n);
    int cnt = 0;
    int add = 0;
    while (IntArrayPtr.lessThan(ptr,endptr)) {
    	ptr.plusPlus();
      if (ptr.get(-1) >= 0) {
    	  cnt++;
      }
      else {
        add += cnt-2;
        cnt = 0;
      }
    }
    // in case index array wasn't terminated with a -1
    if (cnt >= 3) add += cnt-2;
    action.addNumTriangles(add);
  }
}

// Doc in parent
public boolean generateDefaultNormals(SoState s,
                                             SoNormalBundle nb)
{
  return false;
}

  // this macro actually makes the code below more readable  :-)
private void DO_VERTEX(
		SoPointDetail pointDetail,
		SoPrimitiveVertex vertex,
		IntArrayPtr mindices, 
		IntArrayPtr nindices, 
		final IntArrayPtr tindices,
		Binding mbind, 
		Binding nbind, 
		Binding tbind,
		int[] matnr,
		int[]normnr, 
		final MutableSbVec3fArray currnormal, 
		final SbVec3fArray normals,
		final SoTextureCoordinateBundle tb,
		final SoCoordinateElement coords,
		final int[] texidx,
		int idx
		) {
  if (mbind == Binding.PER_VERTEX) {                  
    pointDetail.setMaterialIndex(matnr[0]);      
    vertex.setMaterialIndex(matnr[0]++);         
  }                                           
  else if (mbind == Binding.PER_VERTEX_INDEXED) {     
    pointDetail.setMaterialIndex(mindices.star()); 
    vertex.setMaterialIndex(mindices.starPlusPlus()); 
  }                                         
  if (nbind == Binding.PER_VERTEX) {                
    pointDetail.setNormalIndex(normnr[0]);     
    currnormal.assign(normals,normnr[0]); normnr[0]++;        
    vertex.setNormal(new SbVec3f(currnormal.get(0)));          
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
    	if ( tindices != null) {
    		tindices.plusPlus();
    	}
    	else {
    		texidx[0]++;
    	}
    }
  }                                         
  else if (tbind != Binding.NONE_OVERALL) {                      
    pointDetail.setTextureCoordIndex(tindices != null ? tindices.get() : texidx[0]); 
    vertex.setTextureCoords(tb.get(tindices != null ? tindices.get() : texidx[0]));
    
	if ( tindices != null) {
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
	  

	// Doc in parent
	public void generatePrimitives(SoAction action)
	{
  if (this.coordIndex.getNum() < 3) return;

  SoState state = action.getState();

  state.push();
  SoVRMLVertexShape_doAction(action);

  Binding mbind = this.findMaterialBinding(state);
  Binding nbind = this.findNormalBinding(state);

  final SoCoordinateElement[] coords = new SoCoordinateElement[1]; //ptr
  final SbVec3fArray[] normals = new SbVec3fArray[1];
  final IntArrayPtr[] cindices = new IntArrayPtr[1];
  final int[] numindices = new int[1];
  final IntArrayPtr[] nindices = new IntArrayPtr[1];
  final IntArrayPtr[] tindices = new IntArrayPtr[1];
  final IntArrayPtr[] mindices = new IntArrayPtr[1];
  boolean doTextures;
  boolean sendNormals;
  final boolean[] normalCacheUsed = new boolean[1];

  sendNormals = true; // always generate normals

  this.getVertexData(state, coords, normals, cindices,
                      nindices, tindices, mindices, numindices,
                      sendNormals, normalCacheUsed);

  if (!sendNormals) {
    nbind = Binding.NONE_OVERALL;
    normals[0] = null;
    nindices[0] = null;
  }
  else if (normalCacheUsed[0] && nbind == Binding.PER_VERTEX) {
    nbind = Binding.PER_VERTEX_INDEXED;
  }
  else if (normalCacheUsed[0] && nbind == Binding.PER_FACE_INDEXED) {
    nbind = Binding.PER_FACE;
  }

  if (mbind == Binding.PER_VERTEX) {
    mbind = Binding.PER_VERTEX_INDEXED;
    mindices[0] = cindices[0];
  }
  if (nbind == Binding.PER_VERTEX) {
    nbind = Binding.PER_VERTEX_INDEXED;
    nindices[0] = cindices[0];
  }

  final SoTextureCoordinateBundle tb = new SoTextureCoordinateBundle(action, false, false);
  doTextures = tb.needCoordinates();

  Binding tbind = Binding.NONE_OVERALL;
  if (doTextures) {
    if (tb.isFunction() && !tb.needIndices()) {
      tbind = Binding.NONE_OVERALL;
      tindices[0] = null;
    }
    else {
      tbind = Binding.PER_VERTEX_INDEXED;
      if (tindices[0] == null) tindices[0] = cindices[0];
    }
  }
  
  boolean convexcacheused = false;
  if (this.useConvexCache(action, normals[0], nindices, normalCacheUsed[0])) {
    cindices[0] = pimpl.convexCache.getCoordIndices();
    numindices[0] = pimpl.convexCache.getNumCoordIndices();
    mindices[0] = pimpl.convexCache.getMaterialIndices();
    nindices[0] = pimpl.convexCache.getNormalIndices();
    tindices[0] = pimpl.convexCache.getTexIndices();

    if (mbind == Binding.PER_VERTEX) mbind = Binding.PER_VERTEX_INDEXED;
    else if (mbind == Binding.PER_FACE) mbind = Binding.PER_FACE_INDEXED;
    if (nbind == Binding.PER_VERTEX) nbind = Binding.PER_VERTEX_INDEXED;
    else if (nbind == Binding.PER_FACE) nbind = Binding.PER_FACE_INDEXED;

    if (tbind != Binding.NONE_OVERALL) tbind = Binding.PER_VERTEX_INDEXED;
    convexcacheused = true;
  }

  final int[] texidx = new int[1];
  TriangleShape mode = TriangleShape.POLYGON;
  TriangleShape newmode;
  IntArrayPtr viptr = new IntArrayPtr(cindices[0]);
  IntArrayPtr viendptr = IntArrayPtr.plus(viptr,numindices[0]);
  int v1, v2, v3, v4, v5 = 0; // v5 init unnecessary, but kills a compiler warning.

  final SoPrimitiveVertex vertex = new SoPrimitiveVertex();
  final SoPointDetail pointDetail = new SoPointDetail();
  final SoFaceDetail faceDetail = new SoFaceDetail();

  vertex.setDetail(pointDetail);

  final SbVec3fSingle dummynormal = new SbVec3fSingle(0,0,1);
  final MutableSbVec3fArray currnormal = (normals[0] == null) ? new MutableSbVec3fArray(dummynormal) :
    MutableSbVec3fArray.from(normals[0]);//currnormal.assign(normals[0],0);
  vertex.setNormal(currnormal.get(0));

  final int[] matnr = new int [1];
  final int[] normnr = new int[1];

  while (viptr.plusLessThan(2, viendptr)) {
    v1 = viptr.starPlusPlus();
    v2 = viptr.starPlusPlus();
    v3 = viptr.starPlusPlus();
    assert(v1 >= 0 && v2 >= 0 && v3 >= 0);
    v4 = IntArrayPtr.lessThan(viptr,viendptr) ? viptr.starPlusPlus() : -1;
    if (v4  < 0) newmode = TriangleShape.TRIANGLES;
    else {
      v5 = IntArrayPtr.lessThan(viptr,viendptr) ? viptr.starPlusPlus() : -1;
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
      pointDetail.setMaterialIndex(mindices[0].star());
      vertex.setMaterialIndex(mindices[0].starPlusPlus());
    }
    if (nbind == Binding.PER_VERTEX || nbind == Binding.PER_FACE) {
      pointDetail.setNormalIndex(normnr[0]);
      currnormal.assign(normals[0],normnr[0]++);
      vertex.setNormal(currnormal.get(0));
    }
    else if (nbind == Binding.PER_FACE_INDEXED || nbind == Binding.PER_VERTEX_INDEXED) {
      pointDetail.setNormalIndex(nindices[0].star());
      currnormal.assign(normals[0],nindices[0].starPlusPlus());
      vertex.setNormal(currnormal.get(0));
    }

    if (tb.isFunction()) {
      vertex.setTextureCoords(tb.get(coords[0].get3(v1), currnormal.get(0)));
      if (tb.needIndices()) pointDetail.setTextureCoordIndex(tindices[0] != null ? tindices[0].starPlusPlus() : texidx[0]++);
    }
    else if (tbind != Binding.NONE_OVERALL) {
      pointDetail.setTextureCoordIndex(tindices[0] != null ? tindices[0].star() : texidx[0]);
      vertex.setTextureCoords(tb.get(tindices[0] != null ? tindices[0].starPlusPlus() : texidx[0]++));
    }
    pointDetail.setCoordinateIndex(v1);
    vertex.setPoint(coords[0].get3(v1));
    this.shapeVertex(vertex);

    DO_VERTEX(pointDetail,vertex,mindices[0],nindices[0],tindices[0],mbind,nbind,tbind,matnr,normnr,currnormal,normals[0],tb,coords[0],texidx,
    		v2);
    DO_VERTEX(pointDetail,vertex,mindices[0],nindices[0],tindices[0],mbind,nbind,tbind,matnr,normnr,currnormal,normals[0],tb,coords[0],texidx,
    		v3);

    if (mode != TriangleShape.TRIANGLES) {
      DO_VERTEX(pointDetail,vertex,mindices[0],nindices[0],tindices[0],mbind,nbind,tbind,matnr,normnr,currnormal,normals[0],tb,coords[0],texidx,
    		  v4);
      if (mode == TriangleShape.POLYGON) {
        DO_VERTEX(pointDetail,vertex,mindices[0],nindices[0],tindices[0],mbind,nbind,tbind,matnr,normnr,currnormal,normals[0],tb,coords[0],texidx,
        		v5);
        v1 = IntArrayPtr.lessThan(viptr,viendptr) ? viptr.starPlusPlus() : -1;
        while (v1 >= 0) {
          DO_VERTEX(pointDetail,vertex,mindices[0],nindices[0],tindices[0],mbind,nbind,tbind,matnr,normnr,currnormal,normals[0],tb,coords[0],texidx,
        		  v1);
          v1 = IntArrayPtr.lessThan(viptr,viendptr) ? viptr.starPlusPlus() : -1;
        }
        this.endShape();
      }
    }
    faceDetail.incFaceIndex();
    if (mbind == Binding.PER_VERTEX_INDEXED) {
      mindices[0].plusPlus();
    }
    if (nbind == Binding.PER_VERTEX_INDEXED) {
      nindices[0].plusPlus();
    }
    if (tindices[0] != null) tindices[0].plusPlus();
  }
  if (mode != TriangleShape.POLYGON) this.endShape();

  if (normalCacheUsed[0]) {
    this.readUnlockNormalCache();
  }
  if (convexcacheused) {
    pimpl.readUnlockConvexCache();
  }
  state.pop();
  
  tb.destructor();
	}


// Doc in parent
public boolean generateDefaultNormals(SoState state,
                                             SoNormalCache nc)
{
  SoVRMLCoordinate node = (SoVRMLCoordinate) this.coord.getValue();
  if (node == null) return true; // ok, empty ifs

  SbVec3fArray coords = node.point.getValuesSbVec3fArray(/*0*/);

  int numcoords = node.point.getNum();

  switch (this.findNormalBinding(state)) {
  case PER_VERTEX:
  case PER_VERTEX_INDEXED:
    nc.generatePerVertex(coords,
                          numcoords,
                          coordIndex.getValuesIntArrayPtr(0),
                          coordIndex.getNum(),
                          this.creaseAngle.getValue(),
                          null, // face normals
                          0,    // num face normals
                          this.ccw.getValue());
    break;
  case PER_FACE:
  case PER_FACE_INDEXED:
    nc.generatePerFace(coords,
                        numcoords,
                        coordIndex.getValuesIntArrayPtr(0),
                        coordIndex.getNum(),
                        this.ccw.getValue());
    break;
  default:
    break;
  }
  return true;
}

// Doc in parent
public void notify(SoNotList list)
{
  if (pimpl.convexCache != null) pimpl.convexCache.invalidate();
  SoField f = list.getLastField();
  if (f == this.coordIndex) {
    pimpl.concavestatus = STATUS_UNKNOWN;
    SoBase.staticDataLock();//LOCK_VAINDEXER(this);
    Destroyable.delete(pimpl.vaindexer);
    pimpl.vaindexer = null;
    SoBase.staticDataUnlock();//UNLOCK_VAINDEXER(this);
  }
  super.notify(list);
}

//
// internal method which checks if convex cache needs to be
// used or (re)created. Returns TRUE if convex cache must be
// used. pimpl.convexCache is then guaranteed to be != NULL.
//
public boolean useConvexCache(SoAction action, 
                                     final SbVec3fArray normals, 
                                     final IntArrayPtr[] nindices,
                                     final boolean normalsfromcache)
{
  SoState state = action.getState();
  if (this.convex.getValue())
    return false;

  if (pimpl.concavestatus == STATUS_UNKNOWN) {
    final int[] ptr = this.coordIndex.getValuesI(0); int ptr_index = 0;
    final int endptr = /*ptr + */this.coordIndex.getNum();
    int cnt = 0;
    pimpl.concavestatus = STATUS_CONVEX;
    while (ptr_index < endptr) {
    	ptr_index ++;
      if (ptr[ptr_index-1] >= 0) {
    	  cnt++;
      }
      else {
        if (cnt > 3) { pimpl.concavestatus = STATUS_CONCAVE; break; }
        cnt = 0;
      }
    }
  }
  if (pimpl.concavestatus == STATUS_CONVEX) return false;

  pimpl.readLockConvexCache();

  if (pimpl.convexCache != null && pimpl.convexCache.isValid(state)) {
    // check if convex cache has normal indices. The convex cache
    // might be generated without normals.
    if (normals == null || pimpl.convexCache.getNormalIndices() != null) {
      return true;
    }
  }

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

  SoVRMLVertexShape_doAction(action);

  final SoCoordinateElement[] coords = new SoCoordinateElement[1];
  final IntArrayPtr[] cindices = new IntArrayPtr[1];
  final SbVec3fArray[] dummynormals = new SbVec3fArray[1]; //ptr
  final int[] numindices = new int[1];
  final IntArrayPtr[] dummynindices = new IntArrayPtr[1];
  final IntArrayPtr[] tindices = new IntArrayPtr[1];
  final IntArrayPtr[] mindices = new IntArrayPtr[1];
  final boolean[] dummy = new boolean[1];

  this.getVertexData(state, coords, dummynormals, cindices,
                      dummynindices, tindices, mindices, numindices,
                      false, dummy);

  Binding mbind = this.findMaterialBinding(state);
  Binding nbind = normals != null ? this.findNormalBinding(state) : Binding.NONE_OVERALL;
  
  if (normalsfromcache && nbind == Binding.PER_VERTEX) {
    nbind = Binding.PER_VERTEX_INDEXED;
  }

  if (mbind == Binding.PER_VERTEX) {
    mbind = Binding.PER_VERTEX_INDEXED;
    mindices[0] = tindices[0];
  }
  if (nbind == Binding.PER_VERTEX) {
    nbind = Binding.PER_VERTEX_INDEXED;
    nindices[0] = cindices[0];
  }

  Binding tbind = Binding.PER_VERTEX_INDEXED;
  if (tindices[0] == null) tindices[0] = cindices[0];

  if (nbind == Binding.PER_VERTEX_INDEXED && nindices[0] == null) {
    nindices[0] = cindices[0];
  }
  if (mbind == Binding.PER_VERTEX_INDEXED && mindices[0] == null) {
    mindices[0] = cindices[0];
  }
  pimpl.convexCache.generate(coords[0], modelmatrix,
                              cindices[0], numindices[0],
                              mindices[0], nindices[0], tindices[0],
                              SoConvexDataCache.Binding.fromValue(mbind.getValue()),
                              SoConvexDataCache.Binding.fromValue(nbind.getValue()),
                              SoConvexDataCache.Binding.fromValue(tbind.getValue()));
  
  pimpl.writeUnlockConvexCache();

  state.pop();
  SoCacheElement.setInvalid(storedinvalid);

  pimpl.readLockConvexCache();

  return true;
}

	
	
	/*!
	  \copydetails SoNode::initClass(void)
	*/
	public static void initClass() // static
	{
	  //SO_NODE_INTERNAL_INIT_CLASS(SoVRMLIndexedFaceSet, SO_VRML97_NODE_TYPE);
		  SoSubNode.SO__NODE_INIT_CLASS(SoVRMLIndexedFaceSet.class, "VRMLIndexedFaceSet", SoVRMLIndexedShape.class);
	}

}
