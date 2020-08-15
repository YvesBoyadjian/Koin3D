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
 |      This file defines the SoIndexedTriangleSet node class.
 |
 |   Author(s)          : Paul S. Strauss, Gavin Bell, Florian Link
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL3;

import jscenegraph.coin3d.inventor.elements.SoGLMultiTextureCoordinateElement;
import jscenegraph.coin3d.inventor.nodes.SoVertexProperty;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec4f;
import jscenegraph.database.inventor.SoDebug;
import jscenegraph.database.inventor.SoPickedPoint;
import jscenegraph.database.inventor.SoPrimitiveVertex;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoRayPickAction;
import jscenegraph.database.inventor.bundles.SoNormalBundle;
import jscenegraph.database.inventor.bundles.SoTextureCoordinateBundle;
import jscenegraph.database.inventor.caches.SoNormalCache;
import jscenegraph.database.inventor.details.SoDetail;
import jscenegraph.database.inventor.details.SoFaceDetail;
import jscenegraph.database.inventor.details.SoPointDetail;
import jscenegraph.database.inventor.elements.SoCoordinateElement;
import jscenegraph.database.inventor.elements.SoGLCacheContextElement;
import jscenegraph.database.inventor.elements.SoGLLazyElement;
import jscenegraph.database.inventor.elements.SoLazyElement;
import jscenegraph.database.inventor.elements.SoMaterialBindingElement;
import jscenegraph.database.inventor.elements.SoNormalBindingElement;
import jscenegraph.database.inventor.elements.SoShapeHintsElement;
import jscenegraph.database.inventor.elements.SoShapeStyleElement;
import jscenegraph.database.inventor.elements.SoTextureCoordinateBindingElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFInt32;
import jscenegraph.database.inventor.fields.SoSFBool;
import jscenegraph.database.inventor.misc.SoNotList;
import jscenegraph.database.inventor.misc.SoNotRec;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoVertexPropertyCache.SoVPCacheFunc;
import jscenegraph.mevis.inventor.elements.SoGLVBOElement;
import jscenegraph.mevis.inventor.misc.SoVBO;
import jscenegraph.mevis.inventor.misc.SoVertexArrayIndexer;
import jscenegraph.port.Ctx;

/**
 * @author Yves Boyadjian
 *
 */

///////////////////////////////////////////////////////////////////////////////
///
///  \class SoIndexedTriangleSet
///  \ingroup Nodes
///
///  Indexed set of triangles. Each triangle consists of 3 vertices,
///  each of which is denoted by an integer index (from the coordIndex
///  field) into the current coordinates.
///  This is a specialized version of SoIndexedFaceSet, which offers
///  slightly faster updates/rendering, since it only renders triangles.
///  Unlike SoIndexedFaceSet, no -1 separators in the coordIndex field
///  are needed nor supported.
///  Depending on the current material and normal binding values, the materials and normals for
///  the faces or vertices may be accessed in order or indexed. If they
///  are indexed, the materialIndex and normalIndex fields are used.
///
///  (MeVis ONLY, VSG3D also has this node)
///
//////////////////////////////////////////////////////////////////////////////

public class SoIndexedTriangleSet extends SoIndexedShape {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoIndexedTriangleSet.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoIndexedTriangleSet.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoIndexedTriangleSet.class); }    	  	
	
    // \name Fields
    //@{
    //! flag that defines if the GL_TRIANGLES_ADJACENCY draw mode is used
    public final SoSFBool sendAdjacency = new SoSFBool();
    //@}

    //! This enum is used to indicate the current material or normal binding
    public enum Binding {
        OVERALL, PER_FACE, PER_FACE_INDEXED, PER_VERTEX,
            PER_VERTEX_INDEXED;
    	
    	public int getValue() {
    		return ordinal();
    	}
    };

    //! Saves normal binding when generating primitives for picking
    private Binding             savedNormalBinding;

    //! Number of triangles
    private int     numTris;

    private SoVertexArrayIndexer _triangleIndexer;
    

    // Constants for influencing auto-caching algorithm:

    private final int AUTO_CACHE_IFS_MIN_WITHOUT_VP = 20;

    // And the number above which we'll say caches definitely SHOULDN'T be
    // built (because they'll use too much memory):
    private final int AUTO_CACHE_IFS_MAX = SoGLCacheContextElement.OIV_AUTO_CACHE_DEFAULT_MAX;

    
    
    //! Typedef of pointer to method on IndexedTriangleSet;
    //! This will be used to simplify declaration and initialization.
    private interface PMFS {
    	void run(SoIndexedTriangleSet ts, SoGLRenderAction action);
    };
                                                  

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
    //! Array of function pointers to render functions:
    private static PMFS[] TriRenderFunc = {
    	    (its,ra)-> its.TriOmOn(ra), (its,ra)-> its.TriOmOnT(ra),
    	    (its,ra)-> its.TriOmFn(ra), (its,ra)-> its.TriOmFnT(ra),
    	    (its,ra)-> its.TriOmFn(ra), (its,ra)-> its.TriOmFnT(ra),
    	    (its,ra)-> its.TriOmVn(ra), (its,ra)-> its.TriOmVnT(ra),
    	    (its,ra)-> its.TriFmOn(ra), (its,ra)-> its.TriFmOnT(ra),
    	    (its,ra)-> its.TriFmFn(ra), (its,ra)-> its.TriFmFnT(ra),
    	    (its,ra)-> its.TriFmFn(ra), (its,ra)-> its.TriFmFnT(ra),
    	    (its,ra)-> its.TriFmVn(ra), (its,ra)-> its.TriFmVnT(ra),
    	    (its,ra)-> its.TriFmOn(ra), (its,ra)-> its.TriFmOnT(ra),
    	    (its,ra)-> its.TriFmFn(ra), (its,ra)-> its.TriFmFnT(ra),
    	    (its,ra)-> its.TriFmFn(ra), (its,ra)-> its.TriFmFnT(ra),
    	    (its,ra)-> its.TriFmVn(ra), (its,ra)-> its.TriFmVnT(ra),
    	    (its,ra)-> its.TriVmOn(ra), (its,ra)-> its.TriVmOnT(ra),
    	    (its,ra)-> its.TriVmFn(ra), (its,ra)-> its.TriVmFnT(ra),
    	    (its,ra)-> its.TriVmFn(ra), (its,ra)-> its.TriVmFnT(ra),
    	    (its,ra)-> its.TriVmVn(ra), (its,ra)-> its.TriVmVnT(ra),    	   
    	    };
        
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoIndexedTriangleSet()
//
////////////////////////////////////////////////////////////////////////
{
  nodeHeader.SO_NODE_CONSTRUCTOR(/*SoIndexedTriangleSet.class*/);

  nodeHeader.SO_NODE_ADD_FIELD(sendAdjacency,"sendAdjacency", (false));

  numTris = -1;

  isBuiltIn = true;

  savedNormalBinding = Binding.OVERALL;

  _triangleIndexer = null;
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
  _triangleIndexer.destructor();
  super.destructor();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Performs GL rendering of a IndexedTriangleSet.
//
// Use: protected

public void
GLRender(SoGLRenderAction action)
//
////////////////////////////////////////////////////////////////////////
{
  SoState state = action.getState();

  // Get ShapeStyleElement
  SoShapeStyleElement shapeStyle = SoShapeStyleElement.get(state);

  SoVertexProperty vp = getVertexProperty();
  
  // First see if the object is visible and should be rendered now:
  if (shapeStyle.mightNotRender()) {
    if (vpCache.mightNeedSomethingFromState(shapeStyle)) {
      vpCache.fillInColorAndTranspAvail(vp, state);
    }
    if (! shouldGLRender(action))
      return;
  }

  if (vp != null) {
    vp.putVBOsIntoState(state);
  }

  if (vpCache.mightNeedSomethingFromState(shapeStyle)) {
    // Set up numTris/Quads/Faces
    if (numTris < 0)
      setupNumTrisQuadsFaces();

    // If faces might be concave, we have to send them through GLU:
    final SoShapeHintsElement.VertexOrdering[] vo = new SoShapeHintsElement.VertexOrdering[1];
    final SoShapeHintsElement.ShapeType[] st = new SoShapeHintsElement.ShapeType[1];
    final SoShapeHintsElement.FaceType[] ft = new SoShapeHintsElement.FaceType[1];
    SoShapeHintsElement.get(state, vo, st, ft);

    if (numTris == 0 ) return;

    vpCache.fillInCache(vp, state);

    if (vpCache.shouldGenerateNormals(shapeStyle)) {
      // See if there is a valid normal cache. If so, use it:
      SoNormalCache normCache = getNormalCache();
      if (normCache == null || !normCache.isValid(state)) {

        // Compute the number of vertices. This is just an
        // approximation, so using 3 verts per unknown polygon
        // is close enough. 
        int numVerts = 3 * numTris;

        final SoNormalBundle nb = new SoNormalBundle(action, false);
        nb.initGenerator(numVerts);
        generateDefaultNormals(state, nb);
        normCache = getNormalCache();
        nb.destructor();
      }
      vpCache.numNorms = normCache.getNum();
      vpCache.normalPtr = normCache.getNormals();

    }

    SoTextureCoordinateBundle tcb = null;
    int useTexCoordsAnyway = 0;
    if (vpCache.shouldGenerateTexCoords(shapeStyle)) {
      state.push();
      tcb = new SoTextureCoordinateBundle(action, true, true);
    }
    else if (shapeStyle.isTextureFunction() && vpCache.haveTexCoordsInVP()){
      state.push();
      useTexCoordsAnyway = SoVertexPropertyCache.Bits.TEXCOORD_BIT.getValue();
      SoGLMultiTextureCoordinateElement.setTexGen(state, this, 0, null);
    }

    // Now that normals have been generated, can set up pointers
    // (this is a method on SoIndexedShape):

    setupIndices(numTris, numTris, shapeStyle.needNormals(), 
      (useTexCoordsAnyway != 0 || shapeStyle.needTexCoords()));

    //If lighting or texturing is off, this vpCache and other things
    //need to be reconstructed when lighting or texturing is turned
    //on, so we set the bits in the VP cache:
    if(! shapeStyle.needNormals()) vpCache.needFromState |= 
      SoVertexPropertyCache.Bits.NORMAL_BITS.getValue();
    if(! shapeStyle.needTexCoords()) vpCache.needFromState |= 
      SoVertexPropertyCache.Bits.TEXCOORD_BIT.getValue();

    GLRenderInternal(action, useTexCoordsAnyway, shapeStyle);

    if (coordIndex.getNum() < AUTO_CACHE_IFS_MIN_WITHOUT_VP &&
      vpCache.mightNeedSomethingFromState(shapeStyle)) {
        SoGLCacheContextElement.shouldAutoCache(state,
          SoGLCacheContextElement.AutoCache.DO_AUTO_CACHE.getValue());
    } else if (coordIndex.getNum() > AUTO_CACHE_IFS_MAX) {
        SoGLCacheContextElement.shouldAutoCache(state,
          SoGLCacheContextElement.AutoCache.DONT_AUTO_CACHE.getValue());
    }           

    if (tcb != null) {
      tcb.destructor();
      state.pop();
    } else if (useTexCoordsAnyway != 0)
      state.pop();
  }
  else {

    GLRenderInternal(action, 0, shapeStyle);

    // Influence auto-caching algorithm:
    if (coordIndex.getNum() > AUTO_CACHE_IFS_MAX) {

        SoGLCacheContextElement.shouldAutoCache(state,
          SoGLCacheContextElement.AutoCache.DONT_AUTO_CACHE.getValue());
    }           
  }
  return;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Generates polys representing a face set.
//
// Use: protected

protected void
generatePrimitives(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
  SoState state = action.getState();
  // Put vertexProperty stuff into state:
  SoVertexProperty vp = getVertexProperty();
  state.push();
  if (vp != null) {
    vp.doAction(action);
  }

  // When generating primitives for picking, there is no need to
  // create details now, since they will be created in
  // createTriangleDetail() when an intersection is found (but we
  // need to use the face detail to figure out the rest of it).
  // Otherwise, we create a face detail containing the 3 points of
  // the generated triangle, using the stuff in SoShape.
  // We also delay computing default texture coordinates.
  boolean forPicking = action.isOfType(SoRayPickAction.getClassTypeId());

  final SoPrimitiveVertex           pv = new SoPrimitiveVertex();
  final SoFaceDetail                fd = new SoFaceDetail();
  final SoPointDetail               pd = new SoPointDetail();
  final SoNormalBundle              nb = new SoNormalBundle(action, false);
  final SoTextureCoordinateBundle   tcb = new SoTextureCoordinateBundle(action, false, ! forPicking);
  SoCoordinateElement   ce;
  int                         curVert, curIndex, curFace, curCoord;
  int                         numIndices;
  int[]                       coordIndices, matlIndices;
  int[]                       normIndices, texCoordIndices;
  Binding                     materialBinding, normalBinding;
  boolean                        texCoordsIndexed;

  ce = SoCoordinateElement.getInstance(action.getState());

  materialBinding  = getMaterialBinding(action);
  normalBinding    = getNormalBinding(action, nb);
  texCoordsIndexed = areTexCoordsIndexed(action);

  numIndices      = coordIndex.getNum();
  coordIndices    = coordIndex.getValuesI(0);
  matlIndices     = materialIndex.getValuesI(0);
  normIndices     = normalIndex.getValuesI(0);
  texCoordIndices = textureCoordIndex.getValuesI(0);

  // Check for special case of 1 index of SO_END_FACE_INDEX. This
  // means that coord indices are to be used for materials, normals,
  // or texture coords as well
  if (materialIndex.getNum() == 1 && matlIndices[0] == -1)
    matlIndices = coordIndices;
  if (normalIndex.getNum() == 1 && normIndices[0] == -1) 
    normIndices = coordIndices;
  if (textureCoordIndex.getNum() == 1 &&
    texCoordIndices[0] == -1)
    texCoordIndices = coordIndices;

  if (forPicking) {
    pv.setTextureCoords(new SbVec4f(0.0f, 0.0f, 0.0f, 0.0f));
    pv.setDetail(fd);
  }
  else
    pv.setDetail(pd);

  // Also, save normal binding in instance so createTriangleDetail()
  // will know what it is without having to reconstruct a normal bundle
  savedNormalBinding = normalBinding;

  // Step through all the coordinate indices, building faces out
  // of them, until we run out of coordinate indices.
  curFace = curVert = curIndex = 0;

  while (curIndex < numIndices) {

    int vertsInFace = (numIndices-curIndex)>3?3:(numIndices-curIndex);

    // Forget about last incomplete triangle
    if (vertsInFace < 3) {
      break;
    }

    if (forPicking) {
      fd.setFaceIndex(curFace);
      fd.setPartIndex(curFace);
    }

    beginShape(action, SoShape.TriangleShape.POLYGON, forPicking ? null : fd);

    // Loop through all vertices of current face
    for (int i = 0; i<3; i++) {
      curCoord = (int)coordIndices[curIndex];
      int matlIndex = -1, normIndex = -1, tcIndex;

      switch (materialBinding) 
      {
      case OVERALL:
        matlIndex = 0;
        break;
      case PER_FACE:
        matlIndex = curFace;
        break;
      case PER_FACE_INDEXED:
        matlIndex = (int) matlIndices[curFace];
        break;
      case PER_VERTEX:
        matlIndex = curVert;
        break;
      case PER_VERTEX_INDEXED:
        matlIndex = (int) matlIndices[curIndex];
        break;
      }
      switch (normalBinding)
      {
        case OVERALL:
          normIndex = 0;
          break;
        case PER_FACE:
          normIndex = curFace;
          break;
        case PER_FACE_INDEXED:
          normIndex = (int) normIndices[curFace];
          break;
        case PER_VERTEX:
          normIndex = curVert;
          break;
        case PER_VERTEX_INDEXED:
          normIndex = (int) normIndices[curIndex];
          break;
      }
      tcIndex = (texCoordsIndexed ?
        (int) texCoordIndices[curIndex] : curVert);

      pv.setPoint(ce.get3(curCoord));
      pv.setNormal(nb.get(normIndex));
      pv.setMaterialIndex(matlIndex);

      if (! tcb.isFunction()) {
        pv.setTextureCoords(tcb.get(tcIndex));
      }

      if (! forPicking) {
        if (tcb.isFunction()) {
          pv.setTextureCoords(tcb.get(pv.getPoint(),
          pv.getNormal()));
        }
        pd.setCoordinateIndex(curCoord);
        pd.setMaterialIndex(matlIndex);
        pd.setNormalIndex(normIndex);
        pd.setTextureCoordIndex(tcIndex);
      }

      shapeVertex(pv);
  
      curVert++;
      curIndex++;
    }

    endShape();
    curFace++;
  }
  state.pop();
  
  pv.destructor(); // java port
  fd.destructor();
  pd.destructor();
  nb.destructor();
  tcb.destructor();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Overrides standard method to create an SoFaceDetail instance
//    representing a picked intersection with a triangle in the set.
//
// Use: protected, virtual

protected SoDetail 
createTriangleDetail(final SoRayPickAction action,
                                           final SoPrimitiveVertex v1,
                                           final SoPrimitiveVertex v2,
                                           final SoPrimitiveVertex v3,
                                           final SoPickedPoint pp)
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

  SoFaceDetail        newFD = new SoFaceDetail();
  final SoFaceDetail  oldFD = ( SoFaceDetail ) v1.getDetail();

  // Get pointers to all indices, just in case
   int[]       coordIndices, matlIndices;
   int[]       normIndices, texCoordIndices;

  coordIndices    = coordIndex.getValuesI(0);
  matlIndices     = materialIndex.getValuesI(0);
  normIndices     = normalIndex.getValuesI(0);
  texCoordIndices = textureCoordIndex.getValuesI(0);
  if (materialIndex.getNum() == 1 && matlIndices[0] == -1)
    matlIndices = coordIndices;
  if (normalIndex.getNum() == 1 && normIndices[0] == -1) 
    normIndices = coordIndices;
  if (textureCoordIndex.getNum() == 1 &&
    texCoordIndices[0] == -1)
    texCoordIndices = coordIndices;

  // Find out which face was hit
  int hitFace = (int) oldFD.getFaceIndex();

  // Find index of coordinate of first vertex of face by skipping
  // over previous faces. Also, count the number of vertices used.
  int curIndex = 0, curVert = 0, vertsInFace;
  curIndex = hitFace*3;
  curVert  = hitFace*3;

  // Get number of vertices in the hit face
  vertsInFace = 3;

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
      matlIndex = (int) matlIndices[hitFace];
      break;
    case PER_VERTEX:
      matlIndex = curVert;
      break;
    case PER_VERTEX_INDEXED:
      matlIndex = (int) matlIndices[curIndex];
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
      normIndex = (int) normIndices[hitFace];
      break;
    case PER_VERTEX:
      normIndex = curVert;
      break;
    case PER_VERTEX_INDEXED:
      normIndex = (int) normIndices[curIndex];
      break;
    }
    tcIndex = (texCoordsIndexed ?
      (int) texCoordIndices[curIndex] : curVert);

    pd.setCoordinateIndex(coordIndices[curIndex]);
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

  pd.destructor(); // java port
  tcb.destructor();
  return newFD;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns current material binding from action's state.
//
// Use: private, static

private static SoIndexedTriangleSet.Binding
getMaterialBinding(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
  switch (SoMaterialBindingElement.get(action.getState())) {
      case OVERALL:
        return Binding.OVERALL;
      case PER_PART:
      case PER_FACE:
        return Binding.PER_FACE;
      case PER_PART_INDEXED:
      case PER_FACE_INDEXED:
        return Binding.PER_FACE_INDEXED;
      case PER_VERTEX:
        return Binding.PER_VERTEX;
      case PER_VERTEX_INDEXED:
        return Binding.PER_VERTEX_INDEXED;
  }
  return Binding.OVERALL;     // Shut SGI compiler up
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns current normal binding from action's state.
//
// Use: private, static

private SoIndexedTriangleSet.Binding
getNormalBinding(SoAction action, SoNormalBundle nb)
//
////////////////////////////////////////////////////////////////////////
{
  if (nb != null && figureNormals(action.getState(), nb))
    return Binding.PER_VERTEX;

  switch (SoNormalBindingElement.get(action.getState())) {
      case OVERALL:
        return Binding.OVERALL;

      case PER_PART:
      case PER_FACE:
        return Binding.PER_FACE;

      case PER_PART_INDEXED:
      case PER_FACE_INDEXED:
        return Binding.PER_FACE_INDEXED;

      case PER_VERTEX:
        return Binding.PER_VERTEX;

      case PER_VERTEX_INDEXED:
        return Binding.PER_VERTEX_INDEXED;
  }
  return Binding.OVERALL;     // Shut SGI compiler up
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Figures out normals, if necessary.  Returns true if normals were
//    figured out (and the normal binding should be PER_VERTEX).
//
// Use: private

private boolean
figureNormals(SoState state, SoNormalBundle nb)
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
  SoMFInt32     nIndices;

  if (normalIndex.getNum() == 1 && normalIndex.operator_square_bracketI(0) == -1)
    nIndices = coordIndex;
  else
    nIndices = normalIndex;

  // Find greatest index:
  for (i = 0; i < nIndices.getNum(); i++) {
    if ((nIndices).operator_square_bracketI(i) > numNeeded)
      numNeeded = (int) (nIndices).operator_square_bracket(i);
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

public boolean
generateDefaultNormals(SoState state, SoNormalBundle nb)
//
////////////////////////////////////////////////////////////////////////
{
  int                         numIndices = coordIndex.getNum(), curIndex = 0;
  SoCoordinateElement   ce = null;
  SbVec3f[]               vpCoords = null;

  SoVertexProperty vp = getVertexProperty();
  if (vp != null && vp.vertex.getNum() > 0) {
    vpCoords = vp.vertex.getValues(0);
  } else {
    ce = SoCoordinateElement.getInstance(state);
  }

  while (curIndex < numIndices) {

    int vertsInFace = (numIndices-curIndex)>3?3:(numIndices-curIndex);

    // Forget about faces with fewer than three vertices...
    if (vertsInFace < 3) {
      break;
    }

    nb.beginPolygon();

    // Loop through all vertices of current face
    for (int i = 0;i<3;i++) {
      if (ce != null)
        nb.polygonVertex(ce.get3((int)coordIndex.operator_square_bracket(curIndex)));
      else
        nb.polygonVertex(vpCoords[coordIndex.operator_square_bracketI(curIndex)]);

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

////////////////////////////////////////////////////////////////////////
//
// Description:
//    invalidate vpCache when notified
//
// Use: private 

public void
notify(SoNotList list)
//
////////////////////////////////////////////////////////////////////////
{
  if (list.getLastRec().getType() == SoNotRec.Type.CONTAINER &&
    list.getLastField() == coordIndex) {
      numTris = -1;
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
private void 
setupNumTrisQuadsFaces()
{
  numTris = coordIndex.getNum()/3;
}    

private void GLRenderInternal( final SoGLRenderAction action , int useTexCoordsAnyway, final SoShapeStyleElement shapeStyle)
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
    lazyElt.sendVPPacked(state, (IntBuffer)
      vpCache.getColors(0).toIntBuffer()/*.asIntBuffer()*/);
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
    (materialIndex.getNum()==1 && materialIndex.getValuesI(0)[0]==-1) && 
    (normalIndex.getNum()==1 && normalIndex.getValuesI(0)[0]==-1) && 
    (textureCoordIndex.getNum()==1 && textureCoordIndex.getValuesI(0)[0]==-1))
  {
    fastPathTaken = true;
    if (numTris > 0) {
      boolean useVBO = beginVertexArrayRendering(action);
      if (_triangleIndexer == null) {
        _triangleIndexer = new SoVertexArrayIndexer(GL2.GL_TRIANGLES);
      }
      _triangleIndexer.setType(sendAdjacency.getValue()?GL3.GL_TRIANGLES_ADJACENCY:GL2.GL_TRIANGLES);

      if (_triangleIndexer.getDataId()!=getNodeId()) {
        _triangleIndexer.setIndices(coordIndex.getNum(), coordIndex.getValuesI(0), getNodeId());
      }
      _triangleIndexer.render(state, useVBO);

//#ifdef DEBUG
      if (SoDebug.GetEnv("IV_DEBUG_VBO_RENDERING") != null) {
        SoDebugError.postInfo("GLRenderInternal", getTypeId().getName().getString()+" VA Rendering: "+numTris+" Triangles");
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

  // If doing multiple colors, turn off ColorMaterial:
  if (vpCache.getNumColors() > 1) {
    SoGLLazyElement.setColorMaterial(state, false);
    ((SoGLLazyElement )SoLazyElement.getInstance(state)).
      reset(state, SoLazyElement.masks.DIFFUSE_MASK.getValue());
  }
}


void

TriOmOn
    (SoGLRenderAction action) { GL2 gl2 = Ctx.get(action.getCacheContext());
    final int[] vertexIndex = coordIndex.getValuesI(0);
    boolean sendAdj = sendAdjacency.getValue();
    // Send one normal, if there are any normals in vpCache:
    if (vpCache.getNumNormals() > 0)
	vpCache.sendNormal(gl2,(FloatBuffer)vpCache.getNormals(0)/*.asFloatBuffer()*/);
    Buffer vertexPtr = vpCache.getVertices(0);
    final int vertexStride = vpCache.getVertexStride();
    SoVPCacheFunc vertexFunc = vpCache.vertexFunc;

    gl2.glBegin(sendAdj?GL3.GL_TRIANGLES_ADJACENCY:GL2.GL_TRIANGLES);
    int vtxCtr = 0;
    for (int tri = 0; tri < numTris; tri++) {

	vertexPtr.position(vertexStride*vertexIndex[vtxCtr]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr]*/);

	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+1]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr+1]*/);

	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+2]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr+2]*/);
	vtxCtr += 3;
    }
    gl2.glEnd();
}


void

TriOmOnT
    (SoGLRenderAction action) { GL2 gl2 = Ctx.get(action.getCacheContext());
    final int[] vertexIndex = coordIndex.getValuesI(0);
    boolean sendAdj = sendAdjacency.getValue();
    // Send one normal, if there are any normals in vpCache:
    if (vpCache.getNumNormals() > 0)
	vpCache.sendNormal(gl2,(FloatBuffer)vpCache.getNormals(0)/*.asFloatBuffer()*/);
    Buffer vertexPtr = vpCache.getVertices(0);
    final int vertexStride = vpCache.getVertexStride();
    SoVPCacheFunc vertexFunc = vpCache.vertexFunc;
    Buffer texCoordPtr = vpCache.getTexCoords(0);
    final int texCoordStride = vpCache.getTexCoordStride();
    SoVPCacheFunc texCoordFunc = vpCache.texCoordFunc;
    int[] tCoordIndx = getTexCoordIndices();

    gl2.glBegin(sendAdj?GL3.GL_TRIANGLES_ADJACENCY:GL2.GL_TRIANGLES);
    int vtxCtr = 0;
    for (int tri = 0; tri < numTris; tri++) {

	texCoordPtr.position(texCoordStride*tCoordIndx[vtxCtr]/Float.BYTES); (texCoordFunc).run(gl2,texCoordPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr]*/);

	texCoordPtr.position(texCoordStride*tCoordIndx[vtxCtr+1]/Float.BYTES); (texCoordFunc).run(gl2,texCoordPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+1]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr+1]*/);

	texCoordPtr.position(texCoordStride*tCoordIndx[vtxCtr+2]/Float.BYTES); (texCoordFunc).run(gl2,texCoordPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+2]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr+2]*/);
	vtxCtr += 3;
    }
    gl2.glEnd();
}


void

TriOmFn
    (SoGLRenderAction action) { GL2 gl2 = Ctx.get(action.getCacheContext());
    final int[] vertexIndex = coordIndex.getValuesI(0);
    boolean sendAdj = sendAdjacency.getValue();
    Buffer vertexPtr = vpCache.getVertices(0);
    final int vertexStride = vpCache.getVertexStride();
    SoVPCacheFunc vertexFunc = vpCache.vertexFunc;
    Buffer normalPtr = vpCache.getNormals(0);
    final int normalStride = vpCache.getNormalStride();
    SoVPCacheFunc normalFunc = vpCache.normalFunc;
    int[] normalIndx = getNormalIndices();

    gl2.glBegin(sendAdj?GL3.GL_TRIANGLES_ADJACENCY:GL2.GL_TRIANGLES);
    int vtxCtr = 0;
    for (int tri = 0; tri < numTris; tri++) {
	normalPtr.position(normalStride*normalIndx[tri]/Float.BYTES);(normalFunc).run(gl2,normalPtr);

	vertexPtr.position(vertexStride*vertexIndex[vtxCtr]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr]*/);

	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+1]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr+1]*/);

	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+2]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr+2]*/);
	vtxCtr += 3;
    }
    gl2.glEnd();
}


void

TriOmFnT
    (SoGLRenderAction action) { GL2 gl2 = Ctx.get(action.getCacheContext());
    final int[] vertexIndex = coordIndex.getValuesI(0);
    boolean sendAdj = sendAdjacency.getValue();
    Buffer vertexPtr = vpCache.getVertices(0);
    final int vertexStride = vpCache.getVertexStride();
    SoVPCacheFunc vertexFunc = vpCache.vertexFunc;
    Buffer normalPtr = vpCache.getNormals(0);
    final int normalStride = vpCache.getNormalStride();
    SoVPCacheFunc normalFunc = vpCache.normalFunc;
    int[] normalIndx = getNormalIndices();
    Buffer texCoordPtr = vpCache.getTexCoords(0);
    final int texCoordStride = vpCache.getTexCoordStride();
    SoVPCacheFunc texCoordFunc = vpCache.texCoordFunc;
    int[] tCoordIndx = getTexCoordIndices();

    gl2.glBegin(sendAdj?GL3.GL_TRIANGLES_ADJACENCY:GL2.GL_TRIANGLES);
    int vtxCtr = 0;
    for (int tri = 0; tri < numTris; tri++) {
	normalPtr.position(normalStride*normalIndx[tri]/Float.BYTES);(normalFunc).run(gl2,normalPtr);

	texCoordPtr.position(texCoordStride*tCoordIndx[vtxCtr]/Float.BYTES); (texCoordFunc).run(gl2,texCoordPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr]*/);

	texCoordPtr.position(texCoordStride*tCoordIndx[vtxCtr+1]/Float.BYTES); (texCoordFunc).run(gl2,texCoordPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+1]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr+1]*/);

	texCoordPtr.position(texCoordStride*tCoordIndx[vtxCtr+2]/Float.BYTES); (texCoordFunc).run(gl2,texCoordPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+2]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr+2]*/);
	vtxCtr += 3;
    }
    gl2.glEnd();
}


void

TriOmVn
    (SoGLRenderAction action) { GL2 gl2 = Ctx.get(action.getCacheContext());
    final int[] vertexIndex = coordIndex.getValuesI(0);
    boolean sendAdj = sendAdjacency.getValue();
    Buffer vertexPtr = vpCache.getVertices(0);
    final int vertexStride = vpCache.getVertexStride();
    SoVPCacheFunc vertexFunc = vpCache.vertexFunc;
    Buffer normalPtr = vpCache.getNormals(0);
    final int normalStride = vpCache.getNormalStride();
    SoVPCacheFunc normalFunc = vpCache.normalFunc;
    int[] normalIndx = getNormalIndices();

    gl2.glBegin(sendAdj?GL3.GL_TRIANGLES_ADJACENCY:GL2.GL_TRIANGLES);
    int vtxCtr = 0;
    for (int tri = 0; tri < numTris; tri++) {

	normalPtr.position(normalStride*normalIndx[vtxCtr]/Float.BYTES);(normalFunc).run(gl2,normalPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr]*/);

	normalPtr.position(normalStride*normalIndx[vtxCtr+1]/Float.BYTES);(normalFunc).run(gl2,normalPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+1]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr+1]*/);

	normalPtr.position(normalStride*normalIndx[vtxCtr+2]/Float.BYTES);(normalFunc).run(gl2,normalPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+2]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr+2]*/);
	vtxCtr += 3;
    }
    gl2.glEnd();
}


void

TriOmVnT
    (SoGLRenderAction action) { GL2 gl2 = Ctx.get(action.getCacheContext());
    final int[] vertexIndex = coordIndex.getValuesI(0);
    boolean sendAdj = sendAdjacency.getValue();
    Buffer vertexPtr = vpCache.getVertices(0);
    final int vertexStride = vpCache.getVertexStride();
    SoVPCacheFunc vertexFunc = vpCache.vertexFunc;
    Buffer normalPtr = vpCache.getNormals(0);
    final int normalStride = vpCache.getNormalStride();
    SoVPCacheFunc normalFunc = vpCache.normalFunc;
    int[] normalIndx = getNormalIndices();
    Buffer texCoordPtr = vpCache.getTexCoords(0);
    final int texCoordStride = vpCache.getTexCoordStride();
    SoVPCacheFunc texCoordFunc = vpCache.texCoordFunc;
    int[] tCoordIndx = getTexCoordIndices();

    gl2.glBegin(sendAdj?GL3.GL_TRIANGLES_ADJACENCY:GL2.GL_TRIANGLES);
    int vtxCtr = 0;
    for (int tri = 0; tri < numTris; tri++) {

	normalPtr.position(normalStride*normalIndx[vtxCtr]/Float.BYTES);(normalFunc).run(gl2,normalPtr);
	texCoordPtr.position(texCoordStride*tCoordIndx[vtxCtr]/Float.BYTES); (texCoordFunc).run(gl2,texCoordPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr]*/);

	normalPtr.position(normalStride*normalIndx[vtxCtr+1]/Float.BYTES);(normalFunc).run(gl2,normalPtr);
	texCoordPtr.position(texCoordStride*tCoordIndx[vtxCtr+1]/Float.BYTES); (texCoordFunc).run(gl2,texCoordPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+1]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr+1]*/);

	normalPtr.position(normalStride*normalIndx[vtxCtr+2]/Float.BYTES);(normalFunc).run(gl2,normalPtr);
	texCoordPtr.position(texCoordStride*tCoordIndx[vtxCtr+2]/Float.BYTES); (texCoordFunc).run(gl2,texCoordPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+2]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr+2]*/);
	vtxCtr += 3;
    }
    gl2.glEnd();
}


void

TriFmOn
    (SoGLRenderAction action) { GL2 gl2 = Ctx.get(action.getCacheContext());
    final int[] vertexIndex = coordIndex.getValuesI(0);
    boolean sendAdj = sendAdjacency.getValue();
    // Send one normal, if there are any normals in vpCache:
    if (vpCache.getNumNormals() > 0)
	vpCache.sendNormal(gl2,(FloatBuffer)vpCache.getNormals(0)/*.asFloatBuffer()*/);
    Buffer vertexPtr = vpCache.getVertices(0);
    final int vertexStride = vpCache.getVertexStride();
    SoVPCacheFunc vertexFunc = vpCache.vertexFunc;
    Buffer colorPtr = vpCache.getColors(0).toBuffer();
    final int colorStride = vpCache.getColorStride();
    SoVPCacheFunc colorFunc = vpCache.colorFunc;
    int[] colorIndx = getColorIndices();

    gl2.glBegin(sendAdj?GL3.GL_TRIANGLES_ADJACENCY:GL2.GL_TRIANGLES);
    int vtxCtr = 0;
    for (int tri = 0; tri < numTris; tri++) {
	colorPtr.position(colorStride*colorIndx[tri]/Integer.BYTES);(colorFunc).run(gl2,colorPtr);

	vertexPtr.position(vertexStride*vertexIndex[vtxCtr]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr]*/);

	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+1]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr+1]*/);

	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+2]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr+2]*/);
	vtxCtr += 3;
    }
    gl2.glEnd();
}


void

TriFmOnT
    (SoGLRenderAction action) { GL2 gl2 = Ctx.get(action.getCacheContext());
    final int[] vertexIndex = coordIndex.getValuesI(0);
    boolean sendAdj = sendAdjacency.getValue();
    // Send one normal, if there are any normals in vpCache:
    if (vpCache.getNumNormals() > 0)
	vpCache.sendNormal(gl2,(FloatBuffer)vpCache.getNormals(0)/*.asFloatBuffer()*/);
    Buffer vertexPtr = vpCache.getVertices(0);
    final int vertexStride = vpCache.getVertexStride();
    SoVPCacheFunc vertexFunc = vpCache.vertexFunc;
    Buffer colorPtr = vpCache.getColors(0).toBuffer();
    final int colorStride = vpCache.getColorStride();
    SoVPCacheFunc colorFunc = vpCache.colorFunc;
    int[] colorIndx = getColorIndices();
    Buffer texCoordPtr = vpCache.getTexCoords(0);
    final int texCoordStride = vpCache.getTexCoordStride();
    SoVPCacheFunc texCoordFunc = vpCache.texCoordFunc;
    int[] tCoordIndx = getTexCoordIndices();

    gl2.glBegin(sendAdj?GL3.GL_TRIANGLES_ADJACENCY:GL2.GL_TRIANGLES);
    int vtxCtr = 0;
    for (int tri = 0; tri < numTris; tri++) {
	colorPtr.position(colorStride*colorIndx[tri]/Integer.BYTES);(colorFunc).run(gl2,colorPtr);

	texCoordPtr.position(texCoordStride*tCoordIndx[vtxCtr]/Float.BYTES); (texCoordFunc).run(gl2,texCoordPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr]*/);

	texCoordPtr.position(texCoordStride*tCoordIndx[vtxCtr+1]/Float.BYTES); (texCoordFunc).run(gl2,texCoordPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+1]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr+1]*/);

	texCoordPtr.position(texCoordStride*tCoordIndx[vtxCtr+2]/Float.BYTES); (texCoordFunc).run(gl2,texCoordPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+2]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr+2]*/);
	vtxCtr += 3;
    }
    gl2.glEnd();
}


void

TriFmFn
    (SoGLRenderAction action) { GL2 gl2 = Ctx.get(action.getCacheContext());
    final int[] vertexIndex = coordIndex.getValuesI(0);
    boolean sendAdj = sendAdjacency.getValue();
    Buffer vertexPtr = vpCache.getVertices(0);
    final int vertexStride = vpCache.getVertexStride();
    SoVPCacheFunc vertexFunc = vpCache.vertexFunc;
    Buffer colorPtr = vpCache.getColors(0).toBuffer();
    final int colorStride = vpCache.getColorStride();
    SoVPCacheFunc colorFunc = vpCache.colorFunc;
    int[] colorIndx = getColorIndices();
    Buffer normalPtr = vpCache.getNormals(0);
    final int normalStride = vpCache.getNormalStride();
    SoVPCacheFunc normalFunc = vpCache.normalFunc;
    int[] normalIndx = getNormalIndices();

    gl2.glBegin(sendAdj?GL3.GL_TRIANGLES_ADJACENCY:GL2.GL_TRIANGLES);
    int vtxCtr = 0;
    for (int tri = 0; tri < numTris; tri++) {
	colorPtr.position(colorStride*colorIndx[tri]/Integer.BYTES);(colorFunc).run(gl2,colorPtr);
	normalPtr.position(normalStride*normalIndx[tri]/Float.BYTES);(normalFunc).run(gl2,normalPtr);

	vertexPtr.position(vertexStride*vertexIndex[vtxCtr]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr]*/);

	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+1]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr+1]*/);

	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+2]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr+2]*/);
	vtxCtr += 3;
    }
    gl2.glEnd();
}


void

TriFmFnT
    (SoGLRenderAction action) { GL2 gl2 = Ctx.get(action.getCacheContext());
    final int[] vertexIndex = coordIndex.getValuesI(0);
    boolean sendAdj = sendAdjacency.getValue();
    Buffer vertexPtr = vpCache.getVertices(0);
    final int vertexStride = vpCache.getVertexStride();
    SoVPCacheFunc vertexFunc = vpCache.vertexFunc;
    Buffer colorPtr = vpCache.getColors(0).toBuffer();
    final int colorStride = vpCache.getColorStride();
    SoVPCacheFunc colorFunc = vpCache.colorFunc;
    int[] colorIndx = getColorIndices();
    Buffer normalPtr = vpCache.getNormals(0);
    final int normalStride = vpCache.getNormalStride();
    SoVPCacheFunc normalFunc = vpCache.normalFunc;
    int[] normalIndx = getNormalIndices();
    Buffer texCoordPtr = vpCache.getTexCoords(0);
    final int texCoordStride = vpCache.getTexCoordStride();
    SoVPCacheFunc texCoordFunc = vpCache.texCoordFunc;
    int[] tCoordIndx = getTexCoordIndices();

    gl2.glBegin(sendAdj?GL3.GL_TRIANGLES_ADJACENCY:GL2.GL_TRIANGLES);
    int vtxCtr = 0;
    for (int tri = 0; tri < numTris; tri++) {
	colorPtr.position(colorStride*colorIndx[tri]/Integer.BYTES);(colorFunc).run(gl2,colorPtr);
	normalPtr.position(normalStride*normalIndx[tri]/Float.BYTES);(normalFunc).run(gl2,normalPtr);

	texCoordPtr.position(texCoordStride*tCoordIndx[vtxCtr]/Float.BYTES); (texCoordFunc).run(gl2,texCoordPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr]*/);

	texCoordPtr.position(texCoordStride*tCoordIndx[vtxCtr+1]/Float.BYTES); (texCoordFunc).run(gl2,texCoordPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+1]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr+1]*/);

	texCoordPtr.position(texCoordStride*tCoordIndx[vtxCtr+2]/Float.BYTES); (texCoordFunc).run(gl2,texCoordPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+2]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr+2]*/);
	vtxCtr += 3;
    }
    gl2.glEnd();
}


void

TriFmVn
    (SoGLRenderAction action) { GL2 gl2 = Ctx.get(action.getCacheContext());
    final int[] vertexIndex = coordIndex.getValuesI(0);
    boolean sendAdj = sendAdjacency.getValue();
    Buffer vertexPtr = vpCache.getVertices(0);
    final int vertexStride = vpCache.getVertexStride();
    SoVPCacheFunc vertexFunc = vpCache.vertexFunc;
    Buffer colorPtr = vpCache.getColors(0).toBuffer();
    final int colorStride = vpCache.getColorStride();
    SoVPCacheFunc colorFunc = vpCache.colorFunc;
    int[] colorIndx = getColorIndices();
    Buffer normalPtr = vpCache.getNormals(0);
    final int normalStride = vpCache.getNormalStride();
    SoVPCacheFunc normalFunc = vpCache.normalFunc;
    int[] normalIndx = getNormalIndices();

    gl2.glBegin(sendAdj?GL3.GL_TRIANGLES_ADJACENCY:GL2.GL_TRIANGLES);
    int vtxCtr = 0;
    for (int tri = 0; tri < numTris; tri++) {
	colorPtr.position(colorStride*colorIndx[tri]/Integer.BYTES);(colorFunc).run(gl2,colorPtr);

	normalPtr.position(normalStride*normalIndx[vtxCtr]/Float.BYTES);(normalFunc).run(gl2,normalPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr]*/);

	normalPtr.position(normalStride*normalIndx[vtxCtr+1]/Float.BYTES);(normalFunc).run(gl2,normalPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+1]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr+1]*/);

	normalPtr.position(normalStride*normalIndx[vtxCtr+2]/Float.BYTES);(normalFunc).run(gl2,normalPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+2]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr+2]*/);
	vtxCtr += 3;
    }
    gl2.glEnd();
}


void

TriFmVnT
    (SoGLRenderAction action) { GL2 gl2 = Ctx.get(action.getCacheContext());
    final int[] vertexIndex = coordIndex.getValuesI(0);
    boolean sendAdj = sendAdjacency.getValue();
    Buffer vertexPtr = vpCache.getVertices(0);
    final int vertexStride = vpCache.getVertexStride();
    SoVPCacheFunc vertexFunc = vpCache.vertexFunc;
    Buffer colorPtr = vpCache.getColors(0).toBuffer();
    final int colorStride = vpCache.getColorStride();
    SoVPCacheFunc colorFunc = vpCache.colorFunc;
    int[] colorIndx = getColorIndices();
    Buffer normalPtr = vpCache.getNormals(0);
    final int normalStride = vpCache.getNormalStride();
    SoVPCacheFunc normalFunc = vpCache.normalFunc;
    int[] normalIndx = getNormalIndices();
    Buffer texCoordPtr = vpCache.getTexCoords(0);
    final int texCoordStride = vpCache.getTexCoordStride();
    SoVPCacheFunc texCoordFunc = vpCache.texCoordFunc;
    int[] tCoordIndx = getTexCoordIndices();

    gl2.glBegin(sendAdj?GL3.GL_TRIANGLES_ADJACENCY:GL2.GL_TRIANGLES);
    int vtxCtr = 0;
    for (int tri = 0; tri < numTris; tri++) {
	colorPtr.position(colorStride*colorIndx[tri]/Integer.BYTES);(colorFunc).run(gl2,colorPtr);

	normalPtr.position(normalStride*normalIndx[vtxCtr]/Float.BYTES);(normalFunc).run(gl2,normalPtr);
	texCoordPtr.position(texCoordStride*tCoordIndx[vtxCtr]/Float.BYTES); (texCoordFunc).run(gl2,texCoordPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr]*/);

	normalPtr.position(normalStride*normalIndx[vtxCtr+1]/Float.BYTES);(normalFunc).run(gl2,normalPtr);
	texCoordPtr.position(texCoordStride*tCoordIndx[vtxCtr+1]/Float.BYTES); (texCoordFunc).run(gl2,texCoordPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+1]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr+1]*/);

	normalPtr.position(normalStride*normalIndx[vtxCtr+2]/Float.BYTES);(normalFunc).run(gl2,normalPtr);
	texCoordPtr.position(texCoordStride*tCoordIndx[vtxCtr+2]/Float.BYTES); (texCoordFunc).run(gl2,texCoordPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+2]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr+2]*/);
	vtxCtr += 3;
    }
    gl2.glEnd();
}


void

TriVmOn
    (SoGLRenderAction action) { GL2 gl2 = Ctx.get(action.getCacheContext());
    final int[] vertexIndex = coordIndex.getValuesI(0);
    boolean sendAdj = sendAdjacency.getValue();
    // Send one normal, if there are any normals in vpCache:
    if (vpCache.getNumNormals() > 0)
	vpCache.sendNormal(gl2,(FloatBuffer)vpCache.getNormals(0)/*.asFloatBuffer()*/);
    Buffer vertexPtr = vpCache.getVertices(0);
    final int vertexStride = vpCache.getVertexStride();
    SoVPCacheFunc vertexFunc = vpCache.vertexFunc;
    Buffer colorPtr = vpCache.getColors(0).toBuffer();
    final int colorStride = vpCache.getColorStride();
    SoVPCacheFunc colorFunc = vpCache.colorFunc;
    int[] colorIndx = getColorIndices();

    gl2.glBegin(sendAdj?GL3.GL_TRIANGLES_ADJACENCY:GL2.GL_TRIANGLES);
    int vtxCtr = 0;
    for (int tri = 0; tri < numTris; tri++) {

	colorPtr.position(colorStride*colorIndx[vtxCtr]/Integer.BYTES);(colorFunc).run(gl2,colorPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr]*/);

	colorPtr.position(colorStride*colorIndx[vtxCtr+1]/Integer.BYTES);(colorFunc).run(gl2,colorPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+1]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr+1]*/);

	colorPtr.position(colorStride*colorIndx[vtxCtr+2]/Integer.BYTES);(colorFunc).run(gl2,colorPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+2]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr+2]*/);
	vtxCtr += 3;
    }
    gl2.glEnd();
}


void

TriVmOnT
    (SoGLRenderAction action) { GL2 gl2 = Ctx.get(action.getCacheContext());
    final int[] vertexIndex = coordIndex.getValuesI(0);
    boolean sendAdj = sendAdjacency.getValue();
    // Send one normal, if there are any normals in vpCache:
    if (vpCache.getNumNormals() > 0)
	vpCache.sendNormal(gl2,(FloatBuffer)vpCache.getNormals(0)/*.asFloatBuffer()*/);
    Buffer vertexPtr = vpCache.getVertices(0);
    final int vertexStride = vpCache.getVertexStride();
    SoVPCacheFunc vertexFunc = vpCache.vertexFunc;
    Buffer colorPtr = vpCache.getColors(0).toBuffer();
    final int colorStride = vpCache.getColorStride();
    SoVPCacheFunc colorFunc = vpCache.colorFunc;
    int[] colorIndx = getColorIndices();
    Buffer texCoordPtr = vpCache.getTexCoords(0);
    final int texCoordStride = vpCache.getTexCoordStride();
    SoVPCacheFunc texCoordFunc = vpCache.texCoordFunc;
    int[] tCoordIndx = getTexCoordIndices();

    gl2.glBegin(sendAdj?GL3.GL_TRIANGLES_ADJACENCY:GL2.GL_TRIANGLES);
    int vtxCtr = 0;
    for (int tri = 0; tri < numTris; tri++) {

	colorPtr.position(colorStride*colorIndx[vtxCtr]/Integer.BYTES);(colorFunc).run(gl2,colorPtr);
	texCoordPtr.position(texCoordStride*tCoordIndx[vtxCtr]/Float.BYTES); (texCoordFunc).run(gl2,texCoordPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr]*/);

	colorPtr.position(colorStride*colorIndx[vtxCtr+1]/Integer.BYTES);(colorFunc).run(gl2,colorPtr);
	texCoordPtr.position(texCoordStride*tCoordIndx[vtxCtr+1]/Float.BYTES); (texCoordFunc).run(gl2,texCoordPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+1]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr+1]*/);

	colorPtr.position(colorStride*colorIndx[vtxCtr+2]/Integer.BYTES);(colorFunc).run(gl2,colorPtr);
	texCoordPtr.position(texCoordStride*tCoordIndx[vtxCtr+2]/Float.BYTES); (texCoordFunc).run(gl2,texCoordPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+2]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr+2]*/);
	vtxCtr += 3;
    }
    gl2.glEnd();
}


void

TriVmFn
    (SoGLRenderAction action) { GL2 gl2 = Ctx.get(action.getCacheContext());
    final int[] vertexIndex = coordIndex.getValuesI(0);
    boolean sendAdj = sendAdjacency.getValue();
    Buffer vertexPtr = vpCache.getVertices(0);
    final int vertexStride = vpCache.getVertexStride();
    SoVPCacheFunc vertexFunc = vpCache.vertexFunc;
    Buffer colorPtr = vpCache.getColors(0).toBuffer();
    final int colorStride = vpCache.getColorStride();
    SoVPCacheFunc colorFunc = vpCache.colorFunc;
    int[] colorIndx = getColorIndices();
    Buffer normalPtr = vpCache.getNormals(0);
    final int normalStride = vpCache.getNormalStride();
    SoVPCacheFunc normalFunc = vpCache.normalFunc;
    int[] normalIndx = getNormalIndices();

    gl2.glBegin(sendAdj?GL3.GL_TRIANGLES_ADJACENCY:GL2.GL_TRIANGLES);
    int vtxCtr = 0;
    for (int tri = 0; tri < numTris; tri++) {
	normalPtr.position(normalStride*normalIndx[tri]/Float.BYTES);(normalFunc).run(gl2,normalPtr);

	colorPtr.position(colorStride*colorIndx[vtxCtr]/Integer.BYTES);(colorFunc).run(gl2,colorPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr]*/);

	colorPtr.position(colorStride*colorIndx[vtxCtr+1]/Integer.BYTES);(colorFunc).run(gl2,colorPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+1]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr+1]*/);

	colorPtr.position(colorStride*colorIndx[vtxCtr+2]/Integer.BYTES);(colorFunc).run(gl2,colorPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+2]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr+2]*/);
	vtxCtr += 3;
    }
    gl2.glEnd();
}


void

TriVmFnT
    (SoGLRenderAction action) { GL2 gl2 = Ctx.get(action.getCacheContext());
    final int[] vertexIndex = coordIndex.getValuesI(0);
    boolean sendAdj = sendAdjacency.getValue();
    Buffer vertexPtr = vpCache.getVertices(0);
    final int vertexStride = vpCache.getVertexStride();
    SoVPCacheFunc vertexFunc = vpCache.vertexFunc;
    Buffer colorPtr = vpCache.getColors(0).toBuffer();
    final int colorStride = vpCache.getColorStride();
    SoVPCacheFunc colorFunc = vpCache.colorFunc;
    int[] colorIndx = getColorIndices();
    Buffer normalPtr = vpCache.getNormals(0);
    final int normalStride = vpCache.getNormalStride();
    SoVPCacheFunc normalFunc = vpCache.normalFunc;
    int[] normalIndx = getNormalIndices();
    Buffer texCoordPtr = vpCache.getTexCoords(0);
    final int texCoordStride = vpCache.getTexCoordStride();
    SoVPCacheFunc texCoordFunc = vpCache.texCoordFunc;
    int[] tCoordIndx = getTexCoordIndices();

    gl2.glBegin(sendAdj?GL3.GL_TRIANGLES_ADJACENCY:GL2.GL_TRIANGLES);
    int vtxCtr = 0;
    for (int tri = 0; tri < numTris; tri++) {
	normalPtr.position(normalStride*normalIndx[tri]/Float.BYTES);(normalFunc).run(gl2,normalPtr);

	colorPtr.position(colorStride*colorIndx[vtxCtr]/Integer.BYTES);(colorFunc).run(gl2,colorPtr);
	texCoordPtr.position(texCoordStride*tCoordIndx[vtxCtr]/Float.BYTES); (texCoordFunc).run(gl2,texCoordPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr]*/);

	colorPtr.position(colorStride*colorIndx[vtxCtr+1]/Integer.BYTES);(colorFunc).run(gl2,colorPtr);
	texCoordPtr.position(texCoordStride*tCoordIndx[vtxCtr+1]/Float.BYTES); (texCoordFunc).run(gl2,texCoordPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+1]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr+1]*/);

	colorPtr.position(colorStride*colorIndx[vtxCtr+2]/Integer.BYTES);(colorFunc).run(gl2,colorPtr);
	texCoordPtr.position(texCoordStride*tCoordIndx[vtxCtr+2]/Float.BYTES); (texCoordFunc).run(gl2,texCoordPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+2]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr+2]*/);
	vtxCtr += 3;
    }
    gl2.glEnd();
}


void

TriVmVn
    (SoGLRenderAction action) { GL2 gl2 = Ctx.get(action.getCacheContext());
    final int[] vertexIndex = coordIndex.getValuesI(0);
    boolean sendAdj = sendAdjacency.getValue();
    Buffer vertexPtr = vpCache.getVertices(0);
    final int vertexStride = vpCache.getVertexStride();
    SoVPCacheFunc vertexFunc = vpCache.vertexFunc;
    Buffer colorPtr = vpCache.getColors(0).toBuffer();
    final int colorStride = vpCache.getColorStride();
    SoVPCacheFunc colorFunc = vpCache.colorFunc;
    int[] colorIndx = getColorIndices();
    Buffer normalPtr = vpCache.getNormals(0);
    final int normalStride = vpCache.getNormalStride();
    SoVPCacheFunc normalFunc = vpCache.normalFunc;
    int[] normalIndx = getNormalIndices();

    gl2.glBegin(sendAdj?GL3.GL_TRIANGLES_ADJACENCY:GL2.GL_TRIANGLES);
    int vtxCtr = 0;
    for (int tri = 0; tri < numTris; tri++) {

	colorPtr.position(colorStride*colorIndx[vtxCtr]/Integer.BYTES);(colorFunc).run(gl2,colorPtr);
	normalPtr.position(normalStride*normalIndx[vtxCtr]/Float.BYTES);(normalFunc).run(gl2,normalPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr]*/);

	colorPtr.position(colorStride*colorIndx[vtxCtr+1]/Integer.BYTES);(colorFunc).run(gl2,colorPtr);
	normalPtr.position(normalStride*normalIndx[vtxCtr+1]/Float.BYTES);(normalFunc).run(gl2,normalPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+1]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr+1]*/);

	colorPtr.position(colorStride*colorIndx[vtxCtr+2]/Integer.BYTES);(colorFunc).run(gl2,colorPtr);
	normalPtr.position(normalStride*normalIndx[vtxCtr+2]/Float.BYTES);(normalFunc).run(gl2,normalPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+2]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr+2]*/);
	vtxCtr += 3;
    }
    gl2.glEnd();
}


private void

TriVmVnT
    (SoGLRenderAction action ) { GL2 gl2 = Ctx.get(action.getCacheContext());
    final int[] vertexIndex = coordIndex.getValuesI(0);
    boolean sendAdj = sendAdjacency.getValue();
    Buffer vertexPtr = vpCache.getVertices(0);
    final int vertexStride = vpCache.getVertexStride();
    SoVPCacheFunc vertexFunc = vpCache.vertexFunc;
    Buffer colorPtr = vpCache.getColors(0).toBuffer();
    final int colorStride = vpCache.getColorStride();
    SoVPCacheFunc colorFunc = vpCache.colorFunc;
    int[] colorIndx = getColorIndices();
    Buffer normalPtr = vpCache.getNormals(0);
    final int normalStride = vpCache.getNormalStride();
    SoVPCacheFunc normalFunc = vpCache.normalFunc;
    int[] normalIndx = getNormalIndices();
    Buffer texCoordPtr = vpCache.getTexCoords(0);
    final int texCoordStride = vpCache.getTexCoordStride();
    SoVPCacheFunc texCoordFunc = vpCache.texCoordFunc;
    int[] tCoordIndx = getTexCoordIndices();

    gl2.glBegin(sendAdj?GL3.GL_TRIANGLES_ADJACENCY:GL2.GL_TRIANGLES);
    int vtxCtr = 0;
    for (int tri = 0; tri < numTris; tri++) {

	colorPtr.position(colorStride*colorIndx[vtxCtr]/Integer.BYTES);(colorFunc).run(gl2,colorPtr);
	normalPtr.position(normalStride*normalIndx[vtxCtr]/Float.BYTES);(normalFunc).run(gl2,normalPtr);
	texCoordPtr.position(texCoordStride*tCoordIndx[vtxCtr]/Float.BYTES); (texCoordFunc).run(gl2,texCoordPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr]*/);

	colorPtr.position(colorStride*colorIndx[vtxCtr+1]/Integer.BYTES);(colorFunc).run(gl2,colorPtr);
	normalPtr.position(normalStride*normalIndx[vtxCtr+1]/Float.BYTES);(normalFunc).run(gl2,normalPtr);
	texCoordPtr.position(texCoordStride*tCoordIndx[vtxCtr+1]/Float.BYTES); (texCoordFunc).run(gl2,texCoordPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+1]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr+1]*/);

	colorPtr.position(colorStride*colorIndx[vtxCtr+2]/Integer.BYTES);(colorFunc).run(gl2,colorPtr);
	normalPtr.position(normalStride*normalIndx[vtxCtr+2]/Float.BYTES);(normalFunc).run(gl2,normalPtr);
	texCoordPtr.position(texCoordStride*tCoordIndx[vtxCtr+2]/Float.BYTES); (texCoordFunc).run(gl2,texCoordPtr);
	vertexPtr.position(vertexStride*vertexIndex[vtxCtr+2]/Float.BYTES); (vertexFunc).run(gl2,vertexPtr/*+vertexStride*vertexIndex[vtxCtr+2]*/);
	vtxCtr += 3;
    }
    gl2.glEnd();
}



////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoIndexedTriangleSet class.
//
// Use: internal

public static void
initClass()
//
////////////////////////////////////////////////////////////////////////
{
  SO__NODE_INIT_CLASS(SoIndexedTriangleSet.class, "IndexedTriangleSet", SoIndexedShape.class);
}

}
