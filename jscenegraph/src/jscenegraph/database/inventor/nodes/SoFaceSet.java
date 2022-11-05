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
 |      This file defines the SoFaceSet node class.
 |
 |   Author(s)          : Paul S. Strauss, Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import java.nio.Buffer;
import java.nio.FloatBuffer;

import jscenegraph.opengl.GL;
import jscenegraph.opengl.GL2;
import jscenegraph.opengl.GL3;

import jscenegraph.coin3d.inventor.bundles.SoVertexAttributeBundle;
import jscenegraph.coin3d.inventor.elements.SoVertexAttributeBindingElement;
import jscenegraph.coin3d.inventor.lists.SbListInt;
import jscenegraph.coin3d.inventor.misc.SoGLDriverDatabase;
import jscenegraph.coin3d.inventor.nodes.SoVertexProperty;
import jscenegraph.coin3d.misc.SoGL;
import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec3fSingle;
import jscenegraph.database.inventor.SoPickedPoint;
import jscenegraph.database.inventor.SoPrimitiveVertex;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
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
import jscenegraph.database.inventor.elements.SoShapeHintsElement;
import jscenegraph.database.inventor.elements.SoShapeStyleElement;
import jscenegraph.database.inventor.elements.SoTextureCoordinateBindingElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.errors.SoError;
import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFInt32;
import jscenegraph.database.inventor.fields.SoSFBool;
import jscenegraph.database.inventor.misc.SoNormalGenerator;
import jscenegraph.database.inventor.misc.SoNotList;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoVertexPropertyCache.SoVPCacheFunc;
import jscenegraph.mevis.inventor.elements.SoGLVBOElement;
import jscenegraph.mevis.inventor.misc.SoVBO;
import jscenegraph.port.Ctx;
import jscenegraph.port.Destroyable;
import jscenegraph.port.IntArrayPtr;
import jscenegraph.port.MutableSbVec3fArray;
import jscenegraph.port.SbVec3fArray;
import jscenegraph.port.SbVec4fArray;


////////////////////////////////////////////////////////////////////////////////
//! Polygonal face shape node.
/*!
\class SoFaceSet
\ingroup Nodes
This node represents a 3D shape formed by constructing faces
(polygons) from vertices located at the coordinates
specified in the \b vertexProperty  field (from SoVertexShape), 
or the current inherited state.
For optimal performance, the \b vertexProperty  field is recommended.


SoFaceSet uses the coordinates in order, starting with the
first one.
Each face has a number of
vertices specified by a value in the \b numVertices  field. For
example, an SoFaceSet  with 
\b numVertices  of [3,4,4] would use coordinates 1, 2, and 3 for the
first face, coordinates 4, 5, 6, and 7 for the second face, and
coordinates 8, 9, 10, and 11 for the third. 
For improved performance, arrange all the faces with only 3 
vertices at beginning of the list, then all faces with 4 vertices,
and finally all other faces.


The number of values in the \b numVertices  field indicates the
number of faces in the set.


The coordinates of the face set are transformed by the current
cumulative transformation. The faces are drawn with the current light
model and drawing style.


Treatment of the current material and normal binding is as follows:
The <tt>PER_PART</tt> and <tt>PER_FACE</tt> bindings specify a material or
normal for each face. The <tt>_INDEXED</tt> bindings are equivalent to
their non-indexed counterparts. The default material binding is 
<tt>OVERALL</tt>. The default normal binding is 
<tt>PER_VERTEX</tt>.


If any normals (or materials) are specified, Inventor assumes 
you provide the correct number of them, as indicated by the binding.
You will see unexpected results
if you specify fewer normals (or materials) than the shape requires.
If no normals are specified, they will be generated automatically.

\par File Format/Default
\par
\code
FaceSet {
  startIndex 0
  numVertices -1
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
SoCoordinate3, SoDrawStyle, SoIndexedFaceSet, SoFaceDetail, SoVertexProperty
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoFaceSet extends SoNonIndexedShape {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoFaceSet.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoFaceSet.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoFaceSet.class); }    	  	
	
	
	
	public static final int SO_FACE_SET_USE_REST_OF_VERTICES  =      (-1);
	
// Constants for influencing auto-caching algorithm:
private static final int AUTO_CACHE_FS_MIN_WITHOUT_VP = 20;

// And the number above which we'll say caches definitely SHOULDN'T be
// built (because they'll use too much memory):
private static final int AUTO_CACHE_FS_MAX = SoGLCacheContextElement.OIV_AUTO_CACHE_DEFAULT_MAX;

    //! This enum is used to indicate the current material or normal binding
    public enum Binding {
        OVERALL, PER_FACE, PER_VERTEX;

		public int getValue() {
			return ordinal();
		}
    };

 // for concavestatus
    private static final int STATUS_UNKNOWN = 0;
    private static final int STATUS_CONVEX = 1;
    private static final int STATUS_CONCAVE = 2;

    private static final int UNKNOWN_TYPE = -1;
    private static final int MIXED_TYPE = -2;

	

	 public final SoMFInt32 numVertices = new SoMFInt32();
		  
	 public final SoSFBool sendAdjacency = new SoSFBool();
		  
    
    //! Number of triangles, quads, (n>4)-vertex faces, total vertices
    private int     numTris, numQuads, numFaces, totalNumVertices;
    
    //! keep track of whether we are using USE_REST_OF_VERTICES
    private boolean usingUSE_REST;
    private boolean nvNotifyEnabled;
    
    //! Typedef of pointer to method on FaceSet;
    //! This will be used to simplify declaration and initialization.
    public interface PMFS {
    	void run(SoFaceSet set, SoGLRenderAction action);
    }
                                                  
    //! Array of function pointers to render functions:
    private static PMFS[] TriRenderFunc = new PMFS[32];
    private static PMFS[] QuadRenderFunc = new PMFS[32];
    private static PMFS[] GenRenderFunc = new PMFS[32];
    
    static {
    	for(int i = 0 ; i<32;i++) {
    		final int ii = i;
    		TriRenderFunc[i] = (ifs,a) -> SoError.post("SoFaceSet TriRenderFunc number "+ii+" not yet implemented");
    		QuadRenderFunc[i] = (ifs,a) -> SoError.post("SoFaceSet QuadRenderFunc number "+ii+" not yet implemented");
    		GenRenderFunc[i] = (ifs,a) -> SoError.post("SoFaceSet GenRenderFunc number "+ii+" not yet implemented");
    	}
    	
    	QuadRenderFunc[0] = (set, action)-> set.QuadOmOn(action);
    	QuadRenderFunc[1] = (set, action)-> set.QuadOmOnT(action);
    	QuadRenderFunc[7] = (set, action)-> set.QuadOmVnT(action);
    	QuadRenderFunc[8] = (set, action)-> set.QuadFmOn(action);
    	QuadRenderFunc[16] = (set, action)-> set.QuadFmOn(action);
    }
    
    SoFaceSetP pimpl;

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoFaceSet()
//
////////////////////////////////////////////////////////////////////////
{
	pimpl = new SoFaceSetP();
	pimpl.convexCache = null;
	pimpl.concavestatus = STATUS_UNKNOWN;
	pimpl.primitivetype = UNKNOWN_TYPE;

  nodeHeader.SO_NODE_CONSTRUCTOR(/*SoFaceSet*/);
  nodeHeader.SO_NODE_ADD_MFIELD(numVertices,"numVertices", (SO_FACE_SET_USE_REST_OF_VERTICES));
  nodeHeader.SO_NODE_ADD_SFIELD(sendAdjacency,"sendAdjacency", (false));

  isBuiltIn = true;
  numTris = numQuads = numFaces = -1;
}

	 
    
    
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Computes bounding box of vertices of face set.
//
// Use: protected

public void
computeBBox(SoAction action, final SbBox3f box, final SbVec3f center)
//
////////////////////////////////////////////////////////////////////////
{
  // First, compute the number of vertices the face set uses

  int numFaces = numVertices.getNum();
  int numVerts = 0;

  if (numFaces == 0)
    return;

  // Count up total number of vertices used. If the last entry in
  // numVerts is SO_FACE_SET_USE_REST_OF_VERTICES, then we need
  // to use all of the vertices.
  if (numVertices.operator_square_bracketI(numFaces-1) == SO_FACE_SET_USE_REST_OF_VERTICES)
    numVerts = -1;
  else
    for (int i = 0; i < numFaces; i++)
      numVerts += (int) numVertices.operator_square_bracketI(i);

  // Next, call the method on SoNonIndexedShape that computes the
  // bounding box and center of the given number of coordinates
  computeCoordBBox(action, numVerts, box, center);
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Overrides standard method to create an SoFaceDetail instance
//    representing a picked intersection with a triangle in the set.
//
// Use: protected, virtual

protected SoDetail 
createTriangleDetail(SoRayPickAction action,
                                final SoPrimitiveVertex v1,
                                final SoPrimitiveVertex v2,
                                final SoPrimitiveVertex v3,
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

  SoFaceDetail        newFD = new SoFaceDetail();
  final SoFaceDetail  oldFD = ( SoFaceDetail ) v1.getDetail();

  // Find out which face was hit
  int hitFace = (int) oldFD.getFaceIndex();

  // Find the index of the first vertex of the face: add up the
  // number of vertices in each previous face
  int curVert = (int) startIndex.getValue();
  for (int face = 0; face < hitFace; face++)
    curVert += (int) numVertices.operator_square_bracket(face);

  // Figure out how many vertices are in the hit face
  int vertsInFace = (int) numVertices.operator_square_bracketI(hitFace);
  if (vertsInFace == SO_FACE_SET_USE_REST_OF_VERTICES) {
    SoCoordinateElement ce = 
      SoCoordinateElement.getInstance(action.getState());
    vertsInFace = (int) ce.getNum() - curVert;
  }

  // Make room in the detail for vertices
  newFD.setNumPoints(vertsInFace);

  // We need the bindings to set up the material/normals stuff
  Binding materialBinding = getMaterialBinding(action);
  Binding normalBinding   = getNormalBinding(action, null);

  // Store each vertex in the detail
  final SoPointDetail               pd = new SoPointDetail();
  final SoTextureCoordinateBundle   tcb = new SoTextureCoordinateBundle(action, false);
  for (int vert = 0; vert < vertsInFace; vert++) {
    pd.setCoordinateIndex(curVert);
    pd.setMaterialIndex(materialBinding == Binding.PER_VERTEX ? curVert :
      materialBinding == Binding.PER_FACE   ? hitFace : 0);
    pd.setNormalIndex(normalBinding == Binding.PER_VERTEX ? curVert :
      normalBinding == Binding.PER_FACE   ? hitFace : 0);
    pd.setTextureCoordIndex(tcb.isFunction() ? 0 : curVert);

    newFD.setPoint(vert, pd);

    curVert++;
  }

  // Compute texture coordinates at intersection point and store it
  // in the picked point
  if (tcb.isFunction())
    pp.setObjectTextureCoords(tcb.get(pp.getObjectPoint(),
    pp.getObjectNormal()));

  // The face/part indices are in the incoming details
  newFD.setFaceIndex(hitFace);
  newFD.setPartIndex(hitFace);

  tcb.destructor();
  pd.destructor();
  return newFD;
}



///////////////////////////////////////////////////////////////////////////
//
// Description:
//      Find out how many tris, quads, and polygons there are,
//      also deal with SO_USE_REST_OF_VERTICES
//
// use: private
//
///////////////////////////////////////////////////////////////////////////
private void 
setupNumTrisQuadsFaces()
{
  numTris = numQuads = numFaces = 0;
  usingUSE_REST = false;
  nvNotifyEnabled = true;
  int nfaces = numVertices.getNum();
  if(nfaces != 0 && numVertices.operator_square_bracketI(nfaces-1)<0) {
    usingUSE_REST = true;
    nvNotifyEnabled = numVertices.enableNotify(false);
    totalNumVertices = 0;
    for(int i = 0; i<nfaces -1; i++)
      totalNumVertices += numVertices.operator_square_bracketI(i);
    numVertices.set1Value(nfaces-1, 
      vpCache.numVerts - totalNumVertices - startIndex.getValue());       
    vpCache.needFromState |=
      SoVertexPropertyCache.BitMask.COORD_FROM_STATE_BIT.getValue();
    totalNumVertices = vpCache.numVerts - startIndex.getValue();    
  }
  else if (totalNumVertices <0 ){
    totalNumVertices = 0;
    for (int i=0; i< nfaces; i++){
      totalNumVertices += numVertices.operator_square_bracketI(i);
    }
  }

  int i = 0;

  while ((i < nfaces) && (numVertices.operator_square_bracketI(i) == 3)) {
    ++numTris;
    ++i; 
  }
  while ((i < nfaces) && (numVertices.operator_square_bracketI(i) == 4) ) {
    ++numQuads;
    ++i; 
  }
  while (i < nfaces) {
    ++numFaces;
    ++i;
  }
}    


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Generates primitives (triangles) for face set
//
// Use: protected

protected void
generatePrimitives(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
	  if (this.numVertices.getNum() == 1 &&
	      this.numVertices.operator_square_bracketI(0) == 0) return;

	  SoState state = action.getState();

	  if (this.vertexProperty.getValue() != null) {
	    state.push();
	    this.vertexProperty.getValue().doAction(action);
	  }

	  final SoCoordinateElement[] coords = new SoCoordinateElement[1]; //ptr
	  final SbVec3fArray[] normals = new SbVec3fArray[1];
	  boolean doTextures;

	  boolean needNormals = true;

	  SoVertexShape.getVertexData(state, coords, normals,
	                               needNormals);

	  final SoTextureCoordinateBundle tb = new SoTextureCoordinateBundle(action, false, false);
	  doTextures = tb.needCoordinates();

	  Binding mbind = this.findMaterialBinding(state);
	  Binding nbind = this.findNormalBinding(state);

	  SoNormalCache nc = null;

	  if (needNormals && normals[0] == null) {
	    nc = this.generateAndReadLockNormalCache(state);
	    normals[0] = nc.getNormals();
	  }

	  int idx = startIndex.getValue();
	  int[] dummyarray = new int[1];
	  IntArrayPtr ptr = this.numVertices.getValuesIntArrayPtr(0);
	  IntArrayPtr end = IntArrayPtr.plus(ptr,this.numVertices.getNum());
	  IntArrayPtr[] dptr = new IntArrayPtr[1]; dptr[0] = ptr;
	  IntArrayPtr[] dend = new IntArrayPtr[1]; dend[0] = end;
	  this.fixNumVerticesPointers(state, dptr, dend, dummyarray);
	  ptr = dptr[0];
	  end = dend[0];

	  int matnr = 0;
	  int texnr = 0;
	  int normnr = 0;
	  TriangleShape mode = SoShape.TriangleShape.POLYGON;
	  TriangleShape newmode;
	  int n;

	  final SbVec3fSingle dummynormal = new SbVec3fSingle(0.0f, 0.0f, 1.0f);
	  SbVec3fArray currnormal = new SbVec3fArray(dummynormal);
	  if (normals[0] != null) currnormal = normals[0];

	  final SoPrimitiveVertex vertex = new SoPrimitiveVertex();
	  final SoFaceDetail faceDetail = new SoFaceDetail();
	  final SoPointDetail pointDetail = new SoPointDetail();

	  vertex.setDetail(pointDetail);
	  vertex.setNormal(currnormal.get(0));

	  while (IntArrayPtr.lessThan(ptr,end)) {
	    n = ptr.get(); ptr.plusPlus();
	    if (n == 3) newmode = SoShape.TriangleShape.TRIANGLES;
	    else if (n == 4) newmode = SoShape.TriangleShape.QUADS;
	    else newmode = SoShape.TriangleShape.POLYGON;
	    if (newmode != mode) {
	      if (mode != SoShape.TriangleShape.POLYGON) this.endShape();
	      mode = newmode;
	      this.beginShape(action, mode, faceDetail);
	    }
	    else if (mode == SoShape.TriangleShape.POLYGON) this.beginShape(action, mode, faceDetail);
	    if (nbind != Binding.OVERALL) {
	      pointDetail.setNormalIndex(normnr);
	      currnormal = normals[0].plus(normnr++);
	      vertex.setNormal(currnormal.get(0));
	    }
	    if (mbind != Binding.OVERALL) {
	      pointDetail.setMaterialIndex(matnr);
	      vertex.setMaterialIndex(matnr++);
	    }
	    if (doTextures) {
	      if (tb.isFunction()) {
	        vertex.setTextureCoords(tb.get(coords[0].get3(idx), currnormal.get(0)));
	        if (tb.needIndices()) pointDetail.setTextureCoordIndex(texnr++);
	      }
	      else {
	        pointDetail.setTextureCoordIndex(texnr);
	        vertex.setTextureCoords(tb.get(texnr++));
	      }
	    }
	    pointDetail.setCoordinateIndex(idx);
	    vertex.setPoint(coords[0].get3(idx++));
	    this.shapeVertex(vertex);

	    while (--n != 0) {
	      if (nbind == Binding.PER_VERTEX) {
	        pointDetail.setNormalIndex(normnr);
	        currnormal = normals[0].plus(normnr++);
	        vertex.setNormal(currnormal.get(0));
	      }
	      if (mbind == Binding.PER_VERTEX) {
	        pointDetail.setMaterialIndex(matnr);
	        vertex.setMaterialIndex(matnr++);
	      }
	      if (doTextures) {
	        if (tb.isFunction()) {
	          vertex.setTextureCoords(tb.get(coords[0].get3(idx), currnormal.get(0)));
	          if (tb.needIndices()) pointDetail.setTextureCoordIndex(texnr++);
	        }
	        else {
	          pointDetail.setTextureCoordIndex(texnr);
	          vertex.setTextureCoords(tb.get(texnr++));
	        }
	      }
	      pointDetail.setCoordinateIndex(idx);
	      vertex.setPoint(coords[0].get3(idx++));
	      this.shapeVertex(vertex);
	    }
	    if (mode == SoShape.TriangleShape.POLYGON) this.endShape();
	    faceDetail.incFaceIndex();
	  }
	  if (mode != SoShape.TriangleShape.POLYGON) this.endShape();

	  if (nc != null) {
	    this.readUnlockNormalCache();
	  }

	  if (this.vertexProperty.getValue() != null)
	    state.pop();
	  
	  tb.destructor(); // java port
	}



//{
//  // When generating primitives for picking, there is no need to
//  // create details now, since they will be created in
//  // createTriangleDetail() when an intersection is found (but we
//  // need to use the face detail to figure out the rest of it).
//  // Otherwise, we create a face detail containing the 3 points of
//  // the generated triangle, using the stuff in SoShape.
//  // We also delay computing default texture coordinates.
//  boolean forPicking = action.isOfType(SoRayPickAction.getClassTypeId());
//
//  SoState state = action.getState();
//  // put vertex property into state, if it exists:
//  state.push();
//  SoVertexProperty vp = (SoVertexProperty)vertexProperty.getValue();
//  if(vp != null){
//    vp.doAction(action);
//  }
//
//  final SoPrimitiveVertex           pv = new SoPrimitiveVertex();
//  final SoFaceDetail                fd = new SoFaceDetail();
//  final SoPointDetail               pd = new SoPointDetail();
//  final SoNormalBundle              nb = new SoNormalBundle(action, false);
//  final SoTextureCoordinateBundle   tcb = new SoTextureCoordinateBundle(action, false, ! forPicking);
//  SoCoordinateElement   ce;
//  int                         curVert, vert;
//  int                         face, numFaces, vertsInFace;
//  Binding                     materialBinding, normalBinding;
//
//  ce = SoCoordinateElement.getInstance(action.getState());
//
//  materialBinding = getMaterialBinding(action);
//  normalBinding   = getNormalBinding(action, nb);
//
//  curVert = (int) startIndex.getValue();
//
//  if (forPicking) {
//    pv.setTextureCoords(new SbVec4f(0.0f, 0.0f, 0.0f, 0.0f));
//    pv.setDetail(fd);
//  }
//  else
//    pv.setDetail(pd);
//
//  numFaces = numVertices.getNum();
//
//
//  // For each face
//  for (face = 0; face < numFaces; face++) {
//
//    // Figure out number of vertices in this face
//    vertsInFace = (int) numVertices.operator_square_bracketI(face);
//    if (vertsInFace == SO_FACE_SET_USE_REST_OF_VERTICES)
//      vertsInFace = (int) ce.getNum() - curVert;
//
//    if (forPicking) {
//      fd.setFaceIndex(face);
//      fd.setPartIndex(face);
//    }
//
//    beginShape(action, SoShape.TriangleShape.POLYGON, forPicking ? null : fd);
//
//    for (vert = 0; vert < vertsInFace; vert++) {
//
//      int matlIndex = (materialBinding == Binding.PER_VERTEX ? curVert :
//        materialBinding == Binding.PER_FACE   ? face : 0);
//      int normIndex = (normalBinding   == Binding.PER_VERTEX ? curVert :
//        normalBinding   == Binding.PER_FACE   ? face : 0);
//      int tcIndex   = tcb.isFunction() ? 0 : curVert;
//
//      pv.setPoint(ce.get3(curVert));
//      pv.setNormal(nb.get(normIndex));
//      pv.setMaterialIndex(matlIndex);
//
//      if (! tcb.isFunction())
//        pv.setTextureCoords(tcb.get(tcIndex));
//
//      if (! forPicking) {
//        if (tcb.isFunction())
//          pv.setTextureCoords(tcb.get(pv.getPoint(),
//          pv.getNormal()));
//        pd.setCoordinateIndex(curVert);
//        pd.setMaterialIndex(matlIndex);
//        pd.setNormalIndex(normIndex);
//        pd.setTextureCoordIndex(tcIndex);
//      }
//
//      shapeVertex(pv);
//
//      curVert++;
//    }
//
//    endShape();
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


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns current material binding from action's state.
//
// Use: private, static

private SoFaceSet.Binding
getMaterialBinding(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
  switch (SoMaterialBindingElement.get(action.getState())) {
      case OVERALL:
        return SoFaceSet.Binding.OVERALL;

      case PER_PART:
      case PER_PART_INDEXED:
      case PER_FACE:
      case PER_FACE_INDEXED:
        return SoFaceSet.Binding.PER_FACE;

      case PER_VERTEX:
      case PER_VERTEX_INDEXED:
        return SoFaceSet.Binding.PER_VERTEX;
  }
  return SoFaceSet.Binding.OVERALL; // Shut up SGI C++ compiler
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns current normal binding from action's state.
//
// Use: private

private SoFaceSet.Binding
getNormalBinding(SoAction action, SoNormalBundle nb)
//
////////////////////////////////////////////////////////////////////////
{
  // Do automatic normal generation, if necessary:
  if (nb != null && figureNormals(action.getState(), nb))
    return SoFaceSet.Binding.PER_VERTEX;

  switch (SoNormalBindingElement.get(action.getState())) {
      case OVERALL:
        return SoFaceSet.Binding.OVERALL;

      case PER_PART:
      case PER_PART_INDEXED:
      case PER_FACE:
      case PER_FACE_INDEXED:
        return SoFaceSet.Binding.PER_FACE;

      case PER_VERTEX:
      case PER_VERTEX_INDEXED:
        return SoFaceSet.Binding.PER_VERTEX;
  }
  return SoFaceSet.Binding.OVERALL; // Shut up SGI C++ compiler
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Figures out normals, if necessary.  Returns TRUE if it was
//    necessary.
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

  int numNeeded = 0, numFaces = (int) numVertices.getNum();

  if (numFaces == 0)
    return false;

  // Count number of vertices:
  if (numVertices.operator_square_bracketI(numFaces - 1) == SO_FACE_SET_USE_REST_OF_VERTICES) {
    SoCoordinateElement ce =
      SoCoordinateElement.getInstance(state);
    numNeeded = (int) ce.getNum();
  }
  else
    for (int i = 0; i < numFaces; i++)
      numNeeded += (int) numVertices.operator_square_bracketI(i);

  if (nb.shouldGenerate(numNeeded)) {
    generateDefaultNormals(state, nb);
    return true;
  }
  return false;
}

static int current_errors_generateDefaultNormals = 0;

// doc from parent
public boolean
generateDefaultNormals(SoState state, SoNormalCache nc)
{
  boolean ccw = true;
  if (SoShapeHintsElement.getVertexOrdering(state) ==
      SoShapeHintsElement.VertexOrdering.CLOCKWISE) ccw = false;

  SoNormalGenerator gen =
    new SoNormalGenerator(ccw, this.numVertices.getNum() * 3);

  int idx = startIndex.getValue();
  final int[] dummyarray = new int[1];
  IntArrayPtr ptr = this.numVertices.getValuesIntArrayPtr(0);
  IntArrayPtr end = IntArrayPtr.plus(ptr,this.numVertices.getNum());
  IntArrayPtr[] ptr_dummy = new IntArrayPtr[1];ptr_dummy[0] = ptr;
  IntArrayPtr[] end_dummy = new IntArrayPtr[1];end_dummy[0] = end;
  this.fixNumVerticesPointers(state, ptr_dummy, end_dummy, dummyarray);
  ptr = ptr_dummy[0];
  end = end_dummy[0];

  final SoCoordinateElement coords =
    SoCoordinateElement.getInstance(state);

  int numcoords = coords.getNum();

  // Robustness test to see if the startindex is valid.  If it is
  // not, print error message and return FALSE.
  if (idx < 0) {
    if (current_errors_generateDefaultNormals < 1) {
      SoDebugError.postWarning("SoFaceSet::generateDefaultNormals", "startIndex == "+idx+" "+
                                "< 0, which is erroneous. This message will only "+
                                "be printed once, but more errors might be present"
                                );
    }
    current_errors_generateDefaultNormals++;

    // Unable to generate normals for illegal faceset
    return false;
  }

  // Generate normals for the faceset
  while (IntArrayPtr.lessThan(ptr,end)) {
    int num = ptr.get(); ptr.plusPlus();
    // If a valid number of points for the faceset has been specified,
    // and the end index is below the number of points available, then
    // everything is okidoki, and a polygon is added to the normal
    // generator.
    if (num >= 3 && (idx + num) <= numcoords) {
      gen.beginPolygon();
      while (num-- != 0) {
        gen.polygonVertex(coords.get3(idx++));
      }
      gen.endPolygon();
    }
    // If an invalid polygon has been specified, print errormessage
    // and return FALSE.
    else {
      SoDebugError.postWarning("SoFaceSet::generateDefaultNormals", "Erroneous "+
                                "number of coordinates: "+num+" specified for FaceSet. "+
                                "Legal value is >= 3, with "+(numcoords - idx)+" coordinate(s) available"
                                 );

      // Not able to generate normals for invalid faceset
      return false;
    }
  }

  switch (this.findNormalBinding(state)) {
  case PER_VERTEX:
    gen.generate(SoCreaseAngleElement.get(state));
    break;
  case PER_FACE:
    gen.generatePerFace();
    break;
  case OVERALL:
    gen.generateOverall();
    break;
  }
  nc.set(gen);
  return true;
}



////////////////////////////////////////////////////////////////////////
//
// Description:
//    Generates default normals using the given state and normal
//    bundle. Returns TRUE if normals were generated.
//
// Use: extender, virtual

public boolean
generateDefaultNormals(SoState state, SoNormalBundle nb)
//
////////////////////////////////////////////////////////////////////////
{
  int                         numFaces, curCoord, vertsInFace, i, j;
  int                         startInd;
  SoCoordinateElement   ce = null;
  SbVec3fArray               vpCoords = null;

  int numCoords = 0;
  SoVertexProperty vp = (SoVertexProperty )vertexProperty.getValue();
  if (vp != null && (numCoords = vp.vertex.getNum()) > 0) {
    vpCoords = vp.vertex.getValues(0);
  } else {
    ce = SoCoordinateElement.getInstance(state);
    numCoords = ce.getNum();
  }


  numFaces = (int) numVertices.getNum();
  curCoord = startInd = (int) startIndex.getValue();

  for (i = 0; i < numFaces; i++) {
    nb.beginPolygon();

    vertsInFace = (int) numVertices.operator_square_bracketI(i);
    if (vertsInFace == SO_FACE_SET_USE_REST_OF_VERTICES)
      vertsInFace = numCoords - curCoord;

    for (j = 0; j < vertsInFace; j++) {
      if(ce != null)  nb.polygonVertex(ce.get3(curCoord));
      else    nb.polygonVertex(vpCoords.get(curCoord)); 
      curCoord++;
    }
    nb.endPolygon();
  }
  nb.generate(startInd);

  // Cache the resulting normals
  setNormalCache(state, nb.getNumGeneratedNormals(),
    nb.getGeneratedNormals());

  return true;
}

public void GLRenderInternal( SoGLRenderAction  action, int useTexCoordsAnyway, SoShapeStyleElement  shapeStyle )
{
  SoState state = action.getState();
  SoGLLazyElement lazyElt = null;
  lazyElt = (SoGLLazyElement )SoLazyElement.getInstance(state);
  
  GL2 gl2 = state.getGL2();

  boolean fastPathTaken = false;
  // check for cases where GL Vertex Arrays can be used:
  if (SoVBO.isVertexArrayRenderingAllowed() &&
    !lazyElt.isColorIndex(state) &&
    (vpCache.getNumVertices()>0) &&
    (vpCache.getNumNormals()==0 || (vpCache.getNormalBinding()==SoNormalBindingElement.Binding.PER_VERTEX || vpCache.getNormalBinding()==SoNormalBindingElement.Binding.PER_VERTEX_INDEXED)) &&
    // VA rendering is only possible if there is a color VBO, since it manages the packed color swapping
    ((getMaterialBinding(action)!=Binding.PER_VERTEX) || SoGLVBOElement.getInstance(state).getColorVBO()/*getVBO(SoGLVBOElement.VBOType.COLOR_VBO)*/ != null) &&
    (vpCache.getNumTexCoords()==0 || (vpCache.getTexCoordBinding() == SoTextureCoordinateBindingElement.Binding.PER_VERTEX_INDEXED)))
  {
    fastPathTaken = true;
    if (numTris > 0 || numQuads > 0) {
      boolean useVBO = beginVertexArrayRendering(action);
      if (numTris > 0) {
        gl2.glDrawArrays(sendAdjacency.getValue()?GL3.GL_TRIANGLES_ADJACENCY:GL2.GL_TRIANGLES, startIndex.getValue(), numTris*3);
      }
      if (numQuads > 0) {
        gl2.glDrawArrays(GL2.GL_QUADS, startIndex.getValue() + numTris*3, numQuads*4);
      }

//#ifdef DEBUG
//      if (SoDebug::GetEnv("IV_DEBUG_VBO_RENDERING")) {
//        SoDebugError::postInfo("GLRenderInternal", "%s VA Rendering: %d Tris %d Quads", getTypeId().getName().getString(), numTris, numQuads);
//      }
//#endif

      endVertexArrayRendering(action, useVBO);
    }
  }
  // Call the appropriate render loops:
  if (numTris > 0 && !fastPathTaken) {
    (this.TriRenderFunc[useTexCoordsAnyway | vpCache.getRenderCase(shapeStyle)]).run(this,action);
//#ifdef DEBUG
//    if (SoDebug::GetEnv("IV_DEBUG_LEGACY_RENDERING")) {
//      SoDebugError::postInfo("GLRenderInternal", "%s Immediate Mode Rendering: %d Triangles", getTypeId().getName().getString(), numTris);
//    }
//#endif
  }
  if (numQuads > 0 && !fastPathTaken) {
    (this.QuadRenderFunc[useTexCoordsAnyway | vpCache.getRenderCase(shapeStyle)]).run(this,action);
//#ifdef DEBUG
//    if (SoDebug::GetEnv("IV_DEBUG_LEGACY_RENDERING")) {
//      SoDebugError::postInfo("GLRenderInternal", "%s Immediate Mode Rendering: %d Quads", getTypeId().getName().getString(),
//        numQuads);
//    }
//#endif
  }
  if (numFaces > 0) {
    (this.GenRenderFunc[useTexCoordsAnyway | vpCache.getRenderCase(shapeStyle)]).run(this,action);
//#ifdef DEBUG
//    if (SoDebug::GetEnv("IV_DEBUG_LEGACY_RENDERING")) {
//      SoDebugError::postInfo("GLRenderInternal", "%s Immediate Mode Rendering: %d Faces", getTypeId().getName().getString(),
//        numFaces);
//    }
//#endif
  }
}



////////////////////////////////////////////////////////////////////////
//
// Description:
//    Performs GL rendering of a FaceSet.
//
// Use: protected

static int current_errors = 0;

public void
GLRender(SoGLRenderAction action)
//
////////////////////////////////////////////////////////////////////////
{
	  int[] dummyarray = new int[1];
	  IntArrayPtr ptr = this.numVertices.getValuesIntArrayPtr(0); //java port
	  IntArrayPtr end = IntArrayPtr.plus(ptr,this.numVertices.getNum());
	  if ((IntArrayPtr.minus(end,ptr) == 1) && (ptr.get(0) == 0)) return; // nothing to render

	  if (pimpl.primitivetype == UNKNOWN_TYPE) {
	    pimpl.primitivetype = MIXED_TYPE;
	    int numtriangles = 0;
	    int numquads = 0;
	    int numothers = 0;

	    IntArrayPtr nv = this.numVertices.getValuesIntArrayPtr(0);
	    final int n = this.numVertices.getNum();
	    for (int i = 0; i < n; i++) {
	      switch (nv.get(i)) {
	      case 3:
	        numtriangles++;
	        break;
	      case 4:
	        numquads++;
	        break;
	      default:
	        numothers++;
	        break;
	      }
	    }
	    if (numtriangles != 0 && numquads == 0 && numothers == 0) {
	      pimpl.primitivetype = GL.GL_TRIANGLES;
	    }
	    else if (numquads != 0 && numtriangles == 0 && numothers == 0) {
	      pimpl.primitivetype = GL2.GL_QUADS;
	    }
	  }

	  boolean didusevbo = false;
	  SoState state = action.getState();
	  IntArrayPtr[] ptrRef = new IntArrayPtr[1]; ptrRef[0] = ptr; // java port
	  IntArrayPtr[] endRef = new IntArrayPtr[1]; endRef[0] = end; // java port
	  this.fixNumVerticesPointers(state, ptrRef, endRef, dummyarray);
	  ptr = ptrRef[0]; end = endRef[0];// java port

	  boolean storedinvalid = SoCacheElement.setInvalid(false);
	  state.push(); // for convex cache

	  if (this.vertexProperty.getValue() != null) {
	    state.push();
	    this.vertexProperty.getValue().GLRender(action);
	  }

	  if (!this.shouldGLRender(action)) {
	    if (this.vertexProperty.getValue() != null) {
	      state.pop();
	    }
	    // for convex cache
	    SoCacheElement.setInvalid(storedinvalid);
	    state.pop();
	    return;
	  }
	  
	  SoMaterialBundle mb = null;
	  SoTextureCoordinateBundle tb = null;

		try {
	  if (!this.useConvexCache(action)) {
	    // render normally
	    final SoCoordinateElement[] tmp = new SoCoordinateElement[1]; // ptr
	    final SbVec3fArray[] normals = new SbVec3fArray[1]; // ptr
	    boolean doTextures;

	    mb = new SoMaterialBundle(action);
	    tb = new SoTextureCoordinateBundle(action, true, false);
	    doTextures = tb.needCoordinates();

	    boolean needNormals = !mb.isColorOnly() || tb.isFunction();

	    SoVertexShape.getVertexData(state, tmp, normals,
	                                 needNormals);

	    SoGLCoordinateElement coords = (SoGLCoordinateElement )tmp[0];

	    Binding mbind = this.findMaterialBinding(state);
	    Binding nbind = this.findNormalBinding(state);

	    if (!needNormals) nbind = Binding.OVERALL;

	    SoNormalCache nc = null; // ptr

	    if (needNormals && normals[0] == null) {
	      nc = this.generateAndReadLockNormalCache(state);
	      normals[0] = nc.getNormals();
	    }

	    mb.sendFirst(); // make sure we have the correct material

	    int idx = this.startIndex.getValue();

	    // Robustness test to see if the startindex is valid.  If it is
	    // not, print error message and exit.
	    if (idx < 0) {
	      if (current_errors < 1) {
	        SoDebugError.postWarning("SoFaceSet::GLRender", "startIndex == "+idx+" "+
	                                  "< 0, which is erroneous. This message will only "+
	                                  "be printed once, but more errors might be present"
	                                  );
	      }
	      current_errors++;

	      // Unlock resource if needed
	      if (nc != null) {
	        this.readUnlockNormalCache();
	      }

	      // Goto end of this method to clean up resources
	      return; //goto glrender_done; java port : execute finally
	    }

//	    // check if we can render things using glDrawArrays
//	    if (SoGLDriverDatabase.isSupported(SoGL.sogl_glue_instance(state), SoGLDriverDatabase.SO_GL_VERTEX_ARRAY) &&
//	        (pimpl.primitivetype == GL.GL_TRIANGLES) ||
//	        (pimpl.primitivetype == GL2.GL_QUADS) &&
//	        (nbind != Binding.PER_FACE) &&
//	        (mbind != Binding.PER_FACE) &&
//	        !tb.isFunction()) {}

	    	
	        int contextid = action.getCacheContext();
	        int numcoords = coords != null ? coords.getNum() : 0;
	        SoGLLazyElement lelem = null;
	        // check if we can render things using glDrawArrays
	        boolean dova =
	          SoVBO.shouldRenderAsVertexArrays(state, contextid, numcoords) &&
	          ((pimpl.primitivetype == GL2.GL_TRIANGLES) ||
	           (pimpl.primitivetype == GL2.GL_QUADS)) &&
	          (nbind != Binding.PER_FACE) &&
	          (mbind != Binding.PER_FACE) &&
	          !tb.isFunction() &&
	          SoGLDriverDatabase.isSupported(SoGL.sogl_glue_instance(state), SoGLDriverDatabase.SO_GL_VERTEX_ARRAY);

	    	
	        SoGLVBOElement vboelem = SoGLVBOElement.getInstance(state);
	    	SoVBO colorvbo = null;

	        if (dova && (mbind != Binding.OVERALL)) {
	            dova = false;
	            if (mbind == Binding.PER_VERTEX) {
	              lelem = (SoGLLazyElement)SoLazyElement.getInstance(state);
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
	                                            coords,
	                                            nbind == Binding.PER_VERTEX ? normals[0] : null,
	                                            doTextures,
	                                            (mbind == Binding.PER_VERTEX));
	      int numprimitives = this.numVertices.getNum();
	      if (pimpl.primitivetype == GL.GL_TRIANGLES) numprimitives *= 3;
	      else numprimitives *= 4; // quads
	      SoGL.cc_glglue_glDrawArrays(SoGL.sogl_glue_instance(state), pimpl.primitivetype,
	                             idx, numprimitives); 
	      this.finishVertexArray(action, dovbo, nbind == Binding.PER_VERTEX,
	                              doTextures, (mbind == Binding.PER_VERTEX));
	      
	    }
	    else {
	      SOGL_FACESET_GLRENDER(nbind, mbind, doTextures ? 1 : 0, /*(*/coords,
	                                                       normals[0],
	                                                       /*&*/mb,
	                                                       /*&*/tb,
	                                                       nbind.getValue(),
	                                                       mbind.getValue(),
	                                                       doTextures ? 1 : 0,
	                                                       idx,
	                                                       ptr,
	                                                       end,
	                                                       needNormals/*)*/);
	    }

	    if (nc != null) {
	      this.readUnlockNormalCache();
	    }
	  }
} 
	 //glrender_done:
		 finally {
		 Destroyable.delete(mb);
		 Destroyable.delete(tb);

	  if (this.vertexProperty.getValue() != null)
	    state.pop();

	  // needed for convex cache
	  SoCacheElement.setInvalid(storedinvalid);
	  state.pop();

	  int numv = this.numVertices.getNum();
	  // send approx number of triangles for autocache handling
	  SoGL.sogl_autocache_update(state, numv != 0 ?
	                        (this.numVertices.operator_square_bracketI(0)-2)*numv : 0, didusevbo);		 
		 }
}


//
// translates current material binding to the internal enum
//
private SoFaceSet.Binding
findMaterialBinding(SoState state)
{
  SoMaterialBindingElement.Binding matbind =
    SoMaterialBindingElement.get(state);

  Binding binding;
  switch (matbind) {
  case OVERALL:
    binding = Binding.OVERALL;
    break;
  case PER_VERTEX:
  case PER_VERTEX_INDEXED:
    binding = Binding.PER_VERTEX;
    break;
  case PER_PART:
  case PER_PART_INDEXED:
  case PER_FACE:
  case PER_FACE_INDEXED:
    binding = Binding.PER_FACE;
    break;
  default:
    binding = Binding.OVERALL;
//#if COIN_DEBUG
    SoDebugError.postWarning("SoFaceSet::findMaterialBinding",
                              "unknown material binding setting");
//#endif // COIN_DEBUG
    break;
  }
  return binding;
}


//
// translates current normal binding to the internal enum
//
private SoFaceSet.Binding
findNormalBinding(SoState state)
{
  SoNormalBindingElement.Binding normbind =
    SoNormalBindingElement.get(state);

  Binding binding;
  switch (normbind) {
  case OVERALL:
    binding = Binding.OVERALL;
    break;
  case PER_VERTEX:
  case PER_VERTEX_INDEXED:
    binding = Binding.PER_VERTEX;
    break;
  case PER_PART:
  case PER_PART_INDEXED:
  case PER_FACE:
  case PER_FACE_INDEXED:
    binding = Binding.PER_FACE;
    break;
  default:
    binding = Binding.PER_VERTEX;
//#if COIN_DEBUG
    SoDebugError.postWarning("SoFaceSet::findNormalBinding",
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

public void
notify(SoNotList l)
//
////////////////////////////////////////////////////////////////////////
{
//  if (list.getLastRec().getType() == SoNotRec.Type.CONTAINER &&
//    ((list.getLastField() == numVertices) ||
//    (list.getLastField() == vertexProperty)) ) {
//      numTris = numQuads = numFaces = totalNumVertices = -1;
//      vpCache.invalidate();
//  }
//
//  SoShape_notify(list);
	
	  // Overridden to invalidate convex cache.
	  pimpl.readLockConvexCache();
	  if (pimpl.convexCache != null) pimpl.convexCache.invalidate();
	  pimpl.readUnlockConvexCache();
	  SoField f = l.getLastField();
	  if (f == this.numVertices) {
	    pimpl.concavestatus = STATUS_UNKNOWN;
	    pimpl.primitivetype = UNKNOWN_TYPE;
	  }
	  super.notify(l);	
}

final SbVec3fSingle dummynormal = new SbVec3fSingle();

//
// internal method which checks if convex cache needs to be
// used or (re)created. Renders the shape if convex cache needs to be used.
//
private boolean
useConvexCache(SoAction action)
{
  SoState state = action.getState();
  if (SoShapeHintsElement.getFaceType(state) == SoShapeHintsElement.FaceType.CONVEX)
    return false;

  int idx = this.startIndex.getValue();
  final IntArrayPtr[] ptr = new IntArrayPtr[1]; ptr[0] = this.numVertices.getValuesIntArrayPtr(0);
  final IntArrayPtr[] end = new IntArrayPtr[1]; end[0] = IntArrayPtr.plus(ptr[0],this.numVertices.getNum());
  final int[] dummyarray = new int[1];
  this.fixNumVerticesPointers(state, ptr, end, dummyarray);

  if (pimpl.concavestatus == STATUS_UNKNOWN) {
    final IntArrayPtr tst = new IntArrayPtr(ptr[0]);
    while (IntArrayPtr.lessThan(tst,end[0])) {
      if (tst.get() > 3) break;
      tst.plusPlus();
    }
    if (IntArrayPtr.lessThan(tst,end[0])) pimpl.concavestatus = STATUS_CONCAVE;
    else pimpl.concavestatus = STATUS_CONVEX;
  }
  if (pimpl.concavestatus == STATUS_CONVEX) {
    return false;
  }

  pimpl.readLockConvexCache();

  boolean isvalid = pimpl.convexCache != null && pimpl.convexCache.isValid(state);

  final SbMatrix modelmatrix = new SbMatrix();
  if (!isvalid) {
    pimpl.readUnlockConvexCache();
    pimpl.writeLockConvexCache();
    if (pimpl.convexCache != null) pimpl.convexCache.unref();

    // use nopush to avoid cache dependencies.
    SoModelMatrixElement nopushelem = (SoModelMatrixElement)
      state.getElementNoPush(SoModelMatrixElement.getClassStackIndex(SoModelMatrixElement.class));

    // need to send matrix if we have some weird transformation
    modelmatrix.copyFrom(nopushelem.getModelMatrix());
    if (modelmatrix.getValue()[3][0] == 0.0f &&
        modelmatrix.getValue()[3][1] == 0.0f &&
        modelmatrix.getValue()[3][2] == 0.0f &&
        modelmatrix.getValue()[3][3] == 1.0f) modelmatrix.copyFrom(SbMatrix.identity());

    pimpl.convexCache = new SoConvexDataCache(state);
    pimpl.convexCache.ref();
    SoCacheElement.set(state, pimpl.convexCache);
  }

  final SoCoordinateElement[] tmp = new SoCoordinateElement[1]; // ptr
  final SbVec3fArray[] normals = new SbVec3fArray[1]; //ptr
  boolean doTextures;

  final SoMaterialBundle mb = new SoMaterialBundle(action);

  boolean needNormals = !mb.isColorOnly();

  SoVertexShape.getVertexData(state, tmp, normals,
                               needNormals);

  final SoGLCoordinateElement coords = (SoGLCoordinateElement )tmp[0];

  final SoTextureCoordinateBundle tb = new SoTextureCoordinateBundle(action, true, false);
  doTextures = tb.needCoordinates();

  SoConvexDataCache.Binding mbind;

  switch (this.findMaterialBinding(state)) {
  case OVERALL:
    mbind = SoConvexDataCache.Binding.NONE;
    break;
  case PER_VERTEX:
    mbind = SoConvexDataCache.Binding.PER_VERTEX;
    break;
  case PER_FACE:
    mbind = SoConvexDataCache.Binding.PER_FACE;
    break;
  default:
    mbind = SoConvexDataCache.Binding.NONE;
    break;
  }

  SoConvexDataCache.Binding nbind;
  switch (this.findNormalBinding(state)) {
  case OVERALL:
    nbind = SoConvexDataCache.Binding.NONE;
    break;
  case PER_VERTEX:
    nbind = SoConvexDataCache.Binding.PER_VERTEX;
    break;
  case PER_FACE:
    nbind = SoConvexDataCache.Binding.PER_FACE;
    break;
  default:
    nbind = SoConvexDataCache.Binding.NONE;
    break;
  }

  SoNormalCache nc = null; // ptr

  if (needNormals && normals[0] == null) {
    nc = this.generateAndReadLockNormalCache(state);
    normals[0] = nc.getNormals();
  }
  else if (!needNormals) {
    nbind = SoConvexDataCache.Binding.NONE;
  }
  if (nbind == SoConvexDataCache.Binding.NONE && normals == null) {
    dummynormal.setValue(0.0f, 0.0f, 1.0f);
    normals[0] = /*&*/new SbVec3fArray(dummynormal);
  }

  SoConvexDataCache.Binding tbind = SoConvexDataCache.Binding.NONE;
  if (tb.needCoordinates()) tbind = SoConvexDataCache.Binding.PER_VERTEX;

  if (!isvalid) {
    SoCacheElement.set(state, null); // close cache
    // create an index table to be able to use convex cache.
    // should be fast compared to the tessellation
    final int diff = end[0].minus(ptr[0]);
    final SbListInt dummyidx = new SbListInt((int)(diff * 4));
    final IntArrayPtr tptr = new IntArrayPtr(ptr[0]);
    while (IntArrayPtr.lessThan(tptr,end[0])) {
      int num = tptr.get(0); tptr.plusPlus();
      while (num-- != 0) {
        dummyidx.append(idx++);
      }
      dummyidx.append(-1);
    }
    pimpl.convexCache.generate(coords, modelmatrix,
                                dummyidx.getArrayPtr(), dummyidx.getLength(),
                                null, null, null,
                                mbind,
                                nbind,
                                tbind);

    pimpl.writeUnlockConvexCache();
    pimpl.readLockConvexCache();
  }

  mb.sendFirst(); // make sure we have the correct material

  // the convex data cache will change PER_VERTEX binding
  // to PER_VERTEX_INDEXED. We must do so also.
  int realmbind = (int) mbind.getValue();
  int realnbind = (int) nbind.getValue();

  // hack warning. We rely on PER_VERTEX_INDEXED == PER_VERTEX+1
  // and PER_FACE_INDEXED == PER_FACE+1 in SoGL.cpp
  if (mbind == SoConvexDataCache.Binding.PER_VERTEX ||
      mbind == SoConvexDataCache.Binding.PER_FACE) realmbind++;
  if (nbind == SoConvexDataCache.Binding.PER_VERTEX ||
      nbind == SoConvexDataCache.Binding.PER_FACE) realnbind++;

  final SoVertexAttributeBundle vab = new SoVertexAttributeBundle(action, true);
  boolean doattribs = vab.doAttributes();

  SoVertexAttributeBindingElement.Binding attribbind = 
    SoVertexAttributeBindingElement.get(state);

    if (!doattribs) { 
      // for overall attribute binding we check for doattribs before
      // sending anything in SoGL::FaceSet::GLRender
      attribbind = SoVertexAttributeBindingElement.Binding.OVERALL;
    }

  // use the IndededFaceSet rendering method.
  SoGL.sogl_render_faceset(coords,
                      pimpl.convexCache.getCoordIndices(),
                      pimpl.convexCache.getNumCoordIndices(),
                      normals[0],
                      pimpl.convexCache.getNormalIndices(),
                      /*&*/mb,
                      pimpl.convexCache.getMaterialIndices(),
                      /*&*/tb,
                      pimpl.convexCache.getTexIndices(),
                      /*&*/vab,
                      realnbind,
                      realmbind,
                      attribbind.getValue(),
                      doTextures ? 1 : 0,
                      doattribs ? 1 : 0);


  if (nc != null) {
    this.readUnlockNormalCache();
  }

  pimpl.readUnlockConvexCache();
  
  vab.destructor();
  mb.destructor();
  tb.destructor();

  return true;
}



////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoFaceSet class.
//
// Use: internal

public static void initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SoSubNode.SO__NODE_INIT_CLASS(SoFaceSet.class, "FaceSet", SoNonIndexedShape.class);
}

private void
QuadOmOn
    (SoGLRenderAction action) {

    GL2 gl2 = Ctx.get(action.getCacheContext());
    
    // Send one normal, if there are any normals in vpCache:
    if (vpCache.getNumNormals() > 0)
	vpCache.sendNormal(gl2,(FloatBuffer)vpCache.getNormals(0)/*.asFloatBuffer()*/);
    Buffer vertexPtr = vpCache.getVertices(startIndex.getValue()+3*numTris);
    int vertexStride = vpCache.getVertexStride();
    SoVPCacheFunc vertexFunc = vpCache.vertexFunc;

    gl2.glBegin(GL2.GL_QUADS);

	int vertexPtrIndex = 0;
    for (int quad = 0; quad < numQuads; quad++) {
    	vertexPtr.position(vertexPtrIndex/Float.BYTES);
	(vertexFunc).run(gl2,vertexPtr); vertexPtrIndex += vertexStride;

	vertexPtr.position(vertexPtrIndex/Float.BYTES);
	(vertexFunc).run(gl2,vertexPtr); vertexPtrIndex += vertexStride;

	vertexPtr.position(vertexPtrIndex/Float.BYTES);
	(vertexFunc).run(gl2,vertexPtr); vertexPtrIndex += vertexStride;

	vertexPtr.position(vertexPtrIndex/Float.BYTES);
	(vertexFunc).run(gl2,vertexPtr); vertexPtrIndex += vertexStride;

    }
    gl2.glEnd();
}


private void
QuadOmOnT
    (SoGLRenderAction action) {

    GL2 gl2 = Ctx.get(action.getCacheContext());
    
    // Send one normal, if there are any normals in vpCache:
    if (vpCache.getNumNormals() > 0)
	vpCache.sendNormal(gl2,(FloatBuffer)vpCache.getNormals(0)/*.asFloatBuffer()*/);
    Buffer vertexPtr = vpCache.getVertices(startIndex.getValue()+3*numTris);
    int vertexStride = vpCache.getVertexStride();
    SoVPCacheFunc vertexFunc = vpCache.vertexFunc;
    Buffer texCoordPtr = vpCache.getTexCoords(startIndex.getValue()+3*numTris);
    int texCoordStride = vpCache.getTexCoordStride();
    SoVPCacheFunc texCoordFunc = vpCache.texCoordFunc;

    gl2.glBegin(GL2.GL_QUADS);

	int vertexPtrIndex = 0;
	int texCoordPtrIndex = 0;
    for (int quad = 0; quad < numQuads; quad++) {
       	texCoordPtr.position(texCoordPtrIndex/Float.BYTES);
	(texCoordFunc).run(gl2,texCoordPtr); texCoordPtrIndex += texCoordStride;
	(vertexFunc).run(gl2,vertexPtr); vertexPtrIndex += vertexStride;

   	texCoordPtr.position(texCoordPtrIndex/Float.BYTES);
	(texCoordFunc).run(gl2,texCoordPtr); texCoordPtrIndex += texCoordStride;
	vertexPtr.position(vertexPtrIndex/Float.BYTES);
	(vertexFunc).run(gl2,vertexPtr); vertexPtrIndex += vertexStride;

   	texCoordPtr.position(texCoordPtrIndex/Float.BYTES);
	(texCoordFunc).run(gl2,texCoordPtr); texCoordPtrIndex += texCoordStride;
	vertexPtr.position(vertexPtrIndex/Float.BYTES);
	(vertexFunc).run(gl2,vertexPtr); vertexPtrIndex += vertexStride;

   	texCoordPtr.position(texCoordPtrIndex/Float.BYTES);
	(texCoordFunc).run(gl2,texCoordPtr); texCoordPtrIndex += texCoordStride;
	vertexPtr.position(vertexPtrIndex/Float.BYTES);
	(vertexFunc).run(gl2,vertexPtr); vertexPtrIndex += vertexStride;

    }
    gl2.glEnd();
}


private void QuadOmVnT (SoGLRenderAction action) {

    Buffer vertexPtr = vpCache.getVertices(startIndex.getValue()+3*numTris);
    int vertexStride = vpCache.getVertexStride();
    SoVPCacheFunc vertexFunc = vpCache.vertexFunc;
    Buffer normalPtr = vpCache.getNormals(startIndex.getValue()+3*numTris);
    int normalStride = vpCache.getNormalStride();
    SoVPCacheFunc normalFunc = vpCache.normalFunc;
    Buffer texCoordPtr = vpCache.getTexCoords(startIndex.getValue()+3*numTris);
    int texCoordStride = vpCache.getTexCoordStride();
    SoVPCacheFunc texCoordFunc = vpCache.texCoordFunc;

    GL2 gl2 = Ctx.get(action.getCacheContext());
    
    gl2.glBegin(GL2.GL_QUADS);

	int vertexPtrIndex = 0;
	int normalPtrIndex = 0;
	int texCoordPtrIndex = 0;
    for (int quad = 0; quad < numQuads; quad++) {
   	normalPtr.position(normalPtrIndex/Float.BYTES);
	(normalFunc).run(gl2, normalPtr); normalPtrIndex += normalStride;
   	texCoordPtr.position(texCoordPtrIndex/Float.BYTES);
	(texCoordFunc).run(gl2, texCoordPtr); texCoordPtrIndex += texCoordStride;
	vertexPtr.position(vertexPtrIndex/Float.BYTES);
	(vertexFunc).run(gl2, vertexPtr); vertexPtrIndex += vertexStride;

   	normalPtr.position(normalPtrIndex/Float.BYTES);
	(normalFunc).run(gl2, normalPtr); normalPtrIndex += normalStride;
   	texCoordPtr.position(texCoordPtrIndex/Float.BYTES);
	(texCoordFunc).run(gl2, texCoordPtr); texCoordPtrIndex += texCoordStride;
	vertexPtr.position(vertexPtrIndex/Float.BYTES);
	(vertexFunc).run(gl2, vertexPtr); vertexPtrIndex += vertexStride;

   	normalPtr.position(normalPtrIndex/Float.BYTES);
	(normalFunc).run(gl2, normalPtr); normalPtrIndex += normalStride;
   	texCoordPtr.position(texCoordPtrIndex/Float.BYTES);
	(texCoordFunc).run(gl2, texCoordPtr); texCoordPtrIndex += texCoordStride;
	vertexPtr.position(vertexPtrIndex/Float.BYTES);
	(vertexFunc).run(gl2, vertexPtr); vertexPtrIndex += vertexStride;

   	normalPtr.position(normalPtrIndex/Float.BYTES);
	(normalFunc).run(gl2, normalPtr); normalPtrIndex += normalStride;
   	texCoordPtr.position(texCoordPtrIndex/Float.BYTES);
	(texCoordFunc).run(gl2, texCoordPtr); texCoordPtrIndex += texCoordStride;
	vertexPtr.position(vertexPtrIndex/Float.BYTES);
	(vertexFunc).run(gl2, vertexPtr); vertexPtrIndex += vertexStride;

    }
    gl2.glEnd();
}


public void

QuadFmOn
    (SoGLRenderAction action) {


    GL2 gl2 = Ctx.get(action.getCacheContext());
    
    // Send one normal, if there are any normals in vpCache:
    if (vpCache.getNumNormals() > 0)
	vpCache.sendNormal(gl2,(FloatBuffer)vpCache.getNormals(0)/*.asFloatBuffer()*/);
    Buffer vertexPtr = vpCache.getVertices(startIndex.getValue()+3*numTris);
    final int vertexStride = vpCache.getVertexStride();
    SoVPCacheFunc vertexFunc = vpCache.vertexFunc;
    Buffer colorPtr = vpCache.getColors(numTris).toBuffer();
    final int colorStride = vpCache.getColorStride();
    SoVPCacheFunc colorFunc = vpCache.colorFunc;

    gl2.glBegin(GL2.GL_QUADS);

    int colorPtrIndex = 0; // java port
    int vertexPtrIndex = 0; // java port
    for (int quad = 0; quad < numQuads; quad++) {
    	colorPtr.position(colorPtrIndex/Integer.BYTES);
	(colorFunc).run(gl2,colorPtr); colorPtrIndex += colorStride;
	vertexPtr.position(vertexPtrIndex/Integer.BYTES);
	(vertexFunc).run(gl2,vertexPtr); vertexPtrIndex += vertexStride;

	vertexPtr.position(vertexPtrIndex/Integer.BYTES);
	(vertexFunc).run(gl2,vertexPtr); vertexPtrIndex += vertexStride;

	vertexPtr.position(vertexPtrIndex/Integer.BYTES);
	(vertexFunc).run(gl2,vertexPtr); vertexPtrIndex += vertexStride;

	vertexPtr.position(vertexPtrIndex/Integer.BYTES);
	(vertexFunc).run(gl2,vertexPtr); vertexPtrIndex += vertexStride;

    }
    gl2.glEnd();
}

enum AttributeBinding {
    OVERALL /*= 0*/,
    PER_FACE /*= 1*/,
    PER_VERTEX /*= 2*/
  };

private void SOGL_FACESET_GLRENDER(Binding normalbinding, Binding materialbinding, int texturing,
		SoGLCoordinateElement/*(*/coords,
		SbVec3fArray normals,
		SoMaterialBundle/*&*/mb,
		SoTextureCoordinateBundle/*&*/tb,
		int nbind,
		int mbind,
		int doTextures,
		int idx,
		IntArrayPtr ptr,
		IntArrayPtr end,
		boolean needNormals/*)*/)  {
	SOGL_FACESET_GLRENDER_RESOLVE_ARG1(normalbinding, materialbinding, texturing, /*(*/coords,
            normals,
            /*&*/mb,
            /*&*/tb,
            nbind,
            mbind,
            doTextures,
            idx,
            ptr,
            end,
            needNormals/*)*/);
}

private void SOGL_FACESET_GLRENDER_RESOLVE_ARG1(Binding normalbinding,Binding materialbinding,int texturing,
		SoGLCoordinateElement/*(*/coords,
		SbVec3fArray normals,
		SoMaterialBundle/*&*/mb,
		SoTextureCoordinateBundle/*&*/tb,
		int nbind,
		int mbind,
		int doTextures,
		int idx,
		IntArrayPtr ptr,
		IntArrayPtr end,
		boolean needNormals/*)*/)  {
switch (normalbinding) {                                               
case OVERALL:                                           
  SOGL_FACESET_GLRENDER_RESOLVE_ARG2(AttributeBinding.OVERALL, materialbinding, texturing, /*(*/coords,
          normals,
          /*&*/mb,
          /*&*/tb,
          nbind,
          mbind,
          doTextures,
          idx,
          ptr,
          end,
          needNormals/*)*/); 
  break;                                                               
case PER_FACE:                                          
  SOGL_FACESET_GLRENDER_RESOLVE_ARG2(AttributeBinding.PER_FACE, materialbinding, texturing, /*(*/coords,
          normals,
          /*&*/mb,
          /*&*/tb,
          nbind,
          mbind,
          doTextures,
          idx,
          ptr,
          end,
          needNormals/*)*/); 
  break;                                                               
case PER_VERTEX:                                        
  SOGL_FACESET_GLRENDER_RESOLVE_ARG2(AttributeBinding.PER_VERTEX, materialbinding, texturing, /*(*/coords,
          normals,
          /*&*/mb,
          /*&*/tb,
          nbind,
          mbind,
          doTextures,
          idx,
          ptr,
          end,
          needNormals/*)*/); 
  break;                                                               
default:                                                               
  throw new IllegalArgumentException("invalid materialbinding value");                            
  //break;                                                               
}
}

private void SOGL_FACESET_GLRENDER_RESOLVE_ARG2(AttributeBinding normalbinding, Binding materialbinding, int texturing,
		SoGLCoordinateElement/*(*/coords,
		SbVec3fArray normals,
		SoMaterialBundle/*&*/mb,
		SoTextureCoordinateBundle/*&*/tb,
		int nbind,
		int mbind,
		int doTextures,
		int idx,
		IntArrayPtr ptr,
		IntArrayPtr end,
		boolean needNormals/*)*/) { 
switch (materialbinding) {                                             
case OVERALL:                                           
  SOGL_FACESET_GLRENDER_RESOLVE_ARG3(normalbinding, AttributeBinding.OVERALL, texturing, /*(*/coords,
          normals,
          /*&*/mb,
          /*&*/tb,
          nbind,
          mbind,
          doTextures,
          idx,
          ptr,
          end,
          needNormals/*)*/); 
  break;                                                               
case PER_FACE:                                          
  SOGL_FACESET_GLRENDER_RESOLVE_ARG3(normalbinding, AttributeBinding.PER_FACE, texturing, /*(*/coords,
          normals,
          /*&*/mb,
          /*&*/tb,
          nbind,
          mbind,
          doTextures,
          idx,
          ptr,
          end,
          needNormals/*)*/); 
  break;                                                               
case PER_VERTEX:                                        
  SOGL_FACESET_GLRENDER_RESOLVE_ARG3(normalbinding, AttributeBinding.PER_VERTEX, texturing, /*(*/coords,
          normals,
          /*&*/mb,
          /*&*/tb,
          nbind,
          mbind,
          doTextures,
          idx,
          ptr,
          end,
          needNormals/*)*/); 
  break;                                                               
default:                                                               
  throw new IllegalArgumentException("invalid materialbinding value");                            
  //break;                                                               
}
}

private void SOGL_FACESET_GLRENDER_RESOLVE_ARG3(AttributeBinding normalbinding, AttributeBinding materialbinding, int texturing,
		SoGLCoordinateElement/*(*/coords,
		SbVec3fArray normals,
		SoMaterialBundle/*&*/mb,
		SoTextureCoordinateBundle/*&*/tb,
        int nbind,
        int mbind,
        int doTextures,
        int idx,
        IntArrayPtr ptr,
        IntArrayPtr end,
        boolean needNormals/*)*/) {
if (texturing != 0) {                                                       
  SOGL_FACESET_GLRENDER_CALL_FUNC(normalbinding, materialbinding, true, /*(*/coords,
          normals,
          /*&*/mb,
          /*&*/tb,
          nbind,
          mbind,
          doTextures,
          idx,
          ptr,
          end,
          needNormals/*)*/); 
} else {                                                               
  SOGL_FACESET_GLRENDER_CALL_FUNC(normalbinding, materialbinding, false, /*(*/coords,
          normals,
          /*&*/mb,
          /*&*/tb,
          nbind,
          mbind,
          doTextures,
          idx,
          ptr,
          end,
          needNormals/*)*/); 
}
}

private void SOGL_FACESET_GLRENDER_CALL_FUNC(AttributeBinding normalbinding, AttributeBinding materialbinding, boolean texturing, 
		SoGLCoordinateElement coords,
		SbVec3fArray normals,
        /*&*/SoMaterialBundle mb,
        /*&*/SoTextureCoordinateBundle tb,
        int nbind,
        int mbind,
        int doTextures,
        int idx,
        IntArrayPtr ptr,
        IntArrayPtr end,
        boolean needNormals/*)*/) { 
	/*SoGL::FaceSet::*/GLRender/*<normalbinding, materialbinding, texturing>*/(
			normalbinding, materialbinding, texturing,
			/*(*/coords,
        normals,
        /*&*/mb,
        /*&*/tb,
        nbind,
        mbind,
        doTextures,
        idx,
        ptr,
        end,
        needNormals/*)*/);
}

static int current_errors2 = 0;

// This is the same code as in SoGLCoordinateElement::send().
// It is inlined here for speed (~15% speed increase).
private static void SEND_VERTEX(int _idx_, boolean is3d,    SbVec3fArray coords3d,
SbVec4fArray coords4d, GL2 gl2) {
if (is3d) gl2.glVertex3fv(coords3d.get(_idx_).getValueRead(),0); 
else gl2.glVertex4fv(coords4d.get(_idx_).getValueRead(),0);
}

  private static void GLRender(AttributeBinding NormalBinding, AttributeBinding MaterialBinding, boolean TexturingEnabled,
		  final SoGLCoordinateElement coords,
                       final SbVec3fArray normals_,
                       final SoMaterialBundle mb,
                       final SoTextureCoordinateBundle tb,
                       int nbind,
                       int mbind,
                       int doTextures,
                       int idx,
                       IntArrayPtr ptr,
                       IntArrayPtr end,
                       boolean needNormals)
  {
	  ptr = new IntArrayPtr(ptr); // java port
	  MutableSbVec3fArray normals = MutableSbVec3fArray.from(normals_);
	  
    // Make sure specified coordinate startindex is valid
    assert(idx >= 0);

    SbVec3fArray coords3d = null;
    SbVec4fArray coords4d = null;
    boolean is3d = coords.is3D();
    if (is3d) {
      coords3d = coords.getArrayPtr3();
    }
    else {
      coords4d = coords.getArrayPtr4();
    }
    int numcoords = coords.getNum();

    int matnr = 0;
    int texnr = 0;
    int mode = GL2.GL_POLYGON;
    int newmode;
    int n;
    
    GL2 gl2 = new GL2() {};

    final SbVec3fSingle dummynormal = new SbVec3fSingle(0.0f, 0.0f, 1.0f);
    SbVec3fArray currnormal = new SbVec3fArray(dummynormal);
    if (normals != null) currnormal = new SbVec3fArray(normals);
    if ((AttributeBinding)NormalBinding == AttributeBinding.OVERALL) {
      if (needNormals) gl2.glNormal3fv(currnormal.get(0).getValueRead(),0);
    }

    while (IntArrayPtr.lessThan(ptr,end)) {
      n = ptr.get(); ptr.plusPlus();

      if (n < 3 || idx + n > numcoords) {
        if (current_errors2 < 1) {
          SoDebugError.postWarning("[nonindexedfaceset]::GLRender", "Erroneous "+
                                    "number of coordinates specified: "+n+". Must "+
                                    "be >= 3 and less than or equal to the number of "+
                                    "coordinates available (which is: "+(numcoords - idx)+"). Aborting "+
                                    "rendering. This message will be shown only once, "+
                                    "but more errors might be present");
        }

        current_errors2++;
        break;
      }

      if (n == 3) newmode = GL2.GL_TRIANGLES;
      else if (n == 4) newmode = GL2.GL_QUADS;
      else newmode = GL2.GL_POLYGON;
      if (newmode != mode) {
        if (mode != GL2.GL_POLYGON) gl2.glEnd();
        mode = newmode;
        gl2.glBegin( mode);
      }
      else if (mode == GL2.GL_POLYGON) gl2.glBegin(GL2.GL_POLYGON);

      if ((AttributeBinding)NormalBinding != AttributeBinding.OVERALL) {
        currnormal = new SbVec3fArray(normals); normals.plusPlus();
        gl2.glNormal3fv(currnormal.get(0).getValueRead(),0);
      }
      if ((AttributeBinding)MaterialBinding != AttributeBinding.OVERALL) {
        mb.send(matnr++, true);
      }
      if (TexturingEnabled == true) {
        tb.send(texnr++, coords.get3(idx), currnormal.get(0));
      }
      SEND_VERTEX(idx, is3d, coords3d, coords4d, gl2);
      idx++;
      while (--n != 0) {
        if ((AttributeBinding)NormalBinding == AttributeBinding.PER_VERTEX) {
          currnormal = new SbVec3fArray(normals); normals.plusPlus();
          gl2.glNormal3fv(currnormal.get(0).getValueRead(),0);
        }
        if ((AttributeBinding)MaterialBinding == AttributeBinding.PER_VERTEX) {
          mb.send(matnr++, true);
        } else if ((AttributeBinding)MaterialBinding != AttributeBinding.OVERALL) {
          // only needed for nvidia color-per-face bug workaround
          mb.send(matnr-1, true);
        }

        if (TexturingEnabled == true) {
          tb.send(texnr++, coords.get3(idx), currnormal.get(0));
        }
        SEND_VERTEX(idx, is3d, coords3d, coords4d, gl2);
        idx++;
      }
      if (mode == GL2.GL_POLYGON) gl2.glEnd();
    }
    if (mode != GL2.GL_POLYGON) gl2.glEnd();
//#undef SEND_VERTEX
  }

}
