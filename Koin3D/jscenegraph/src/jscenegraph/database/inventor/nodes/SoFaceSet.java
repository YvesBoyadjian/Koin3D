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
import java.nio.IntBuffer;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL3;

import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec4f;
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
import jscenegraph.database.inventor.elements.SoGLTextureCoordinateElement;
import jscenegraph.database.inventor.elements.SoLazyElement;
import jscenegraph.database.inventor.elements.SoMaterialBindingElement;
import jscenegraph.database.inventor.elements.SoNormalBindingElement;
import jscenegraph.database.inventor.elements.SoShapeHintsElement;
import jscenegraph.database.inventor.elements.SoShapeStyleElement;
import jscenegraph.database.inventor.elements.SoTextureCoordinateBindingElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.errors.SoError;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFInt32;
import jscenegraph.database.inventor.fields.SoSFBool;
import jscenegraph.database.inventor.misc.SoNotList;
import jscenegraph.database.inventor.misc.SoNotRec;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoVertexPropertyCache.SoVPCacheFunc;
import jscenegraph.mevis.inventor.elements.SoGLVBOElement;
import jscenegraph.mevis.inventor.misc.SoVBO;


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
        OVERALL, PER_FACE, PER_VERTEX
    };

	

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
      numVerts += (int) numVertices.operator_square_bracket(i);

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
  int vertsInFace = (int) numVertices.operator_square_bracket(hitFace);
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
  // When generating primitives for picking, there is no need to
  // create details now, since they will be created in
  // createTriangleDetail() when an intersection is found (but we
  // need to use the face detail to figure out the rest of it).
  // Otherwise, we create a face detail containing the 3 points of
  // the generated triangle, using the stuff in SoShape.
  // We also delay computing default texture coordinates.
  boolean forPicking = action.isOfType(SoRayPickAction.getClassTypeId());

  SoState state = action.getState();
  // put vertex property into state, if it exists:
  state.push();
  SoVertexProperty vp = (SoVertexProperty)vertexProperty.getValue();
  if(vp != null){
    vp.doAction(action);
  }

  final SoPrimitiveVertex           pv = new SoPrimitiveVertex();
  final SoFaceDetail                fd = new SoFaceDetail();
  final SoPointDetail               pd = new SoPointDetail();
  final SoNormalBundle              nb = new SoNormalBundle(action, false);
  final SoTextureCoordinateBundle   tcb = new SoTextureCoordinateBundle(action, false, ! forPicking);
  SoCoordinateElement   ce;
  int                         curVert, vert;
  int                         face, numFaces, vertsInFace;
  Binding                     materialBinding, normalBinding;

  ce = SoCoordinateElement.getInstance(action.getState());

  materialBinding = getMaterialBinding(action);
  normalBinding   = getNormalBinding(action, nb);

  curVert = (int) startIndex.getValue();

  if (forPicking) {
    pv.setTextureCoords(new SbVec4f(0.0f, 0.0f, 0.0f, 0.0f));
    pv.setDetail(fd);
  }
  else
    pv.setDetail(pd);

  numFaces = numVertices.getNum();


  // For each face
  for (face = 0; face < numFaces; face++) {

    // Figure out number of vertices in this face
    vertsInFace = (int) numVertices.operator_square_bracket(face);
    if (vertsInFace == SO_FACE_SET_USE_REST_OF_VERTICES)
      vertsInFace = (int) ce.getNum() - curVert;

    if (forPicking) {
      fd.setFaceIndex(face);
      fd.setPartIndex(face);
    }

    beginShape(action, SoShape.TriangleShape.POLYGON, forPicking ? null : fd);

    for (vert = 0; vert < vertsInFace; vert++) {

      int matlIndex = (materialBinding == Binding.PER_VERTEX ? curVert :
        materialBinding == Binding.PER_FACE   ? face : 0);
      int normIndex = (normalBinding   == Binding.PER_VERTEX ? curVert :
        normalBinding   == Binding.PER_FACE   ? face : 0);
      int tcIndex   = tcb.isFunction() ? 0 : curVert;

      pv.setPoint(ce.get3(curVert));
      pv.setNormal(nb.get(normIndex));
      pv.setMaterialIndex(matlIndex);

      if (! tcb.isFunction())
        pv.setTextureCoords(tcb.get(tcIndex));

      if (! forPicking) {
        if (tcb.isFunction())
          pv.setTextureCoords(tcb.get(pv.getPoint(),
          pv.getNormal()));
        pd.setCoordinateIndex(curVert);
        pd.setMaterialIndex(matlIndex);
        pd.setNormalIndex(normIndex);
        pd.setTextureCoordIndex(tcIndex);
      }

      shapeVertex(pv);

      curVert++;
    }

    endShape();
  }
  state.pop();
  
  // java port
  pv.destructor();
  fd.destructor();
  pd.destructor();
  nb.destructor();
  tcb.destructor();
}


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
      numNeeded += (int) numVertices.operator_square_bracket(i);

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
  SbVec3f[]               vpCoords = null;

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

    vertsInFace = (int) numVertices.operator_square_bracket(i);
    if (vertsInFace == SO_FACE_SET_USE_REST_OF_VERTICES)
      vertsInFace = numCoords - curCoord;

    for (j = 0; j < vertsInFace; j++) {
      if(ce != null)  nb.polygonVertex(ce.get3(curCoord));
      else    nb.polygonVertex(vpCoords[curCoord]); 
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
    ((getMaterialBinding(action)!=Binding.PER_VERTEX) || SoGLVBOElement.getInstance(state).getVBO(SoGLVBOElement.VBOType.COLOR_VBO) != null) &&
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

public void
GLRender(SoGLRenderAction action)
//
////////////////////////////////////////////////////////////////////////
{
  SoState state = action.getState();

  // Get ShapeStyleElement
  SoShapeStyleElement shapeStyle = SoShapeStyleElement.get(state);

  SoVertexProperty vp = (SoVertexProperty )vertexProperty.getValue();

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

    vpCache.fillInCache(vp,  state);

    // Set up numTris/Quads/Faces and count vertices if necessary
    if (numTris < 0)
      setupNumTrisQuadsFaces();

    // If faces might be concave, we have to send them through GLU:
    final SoShapeHintsElement.VertexOrdering[] vo = new SoShapeHintsElement.VertexOrdering[1];
    final SoShapeHintsElement.ShapeType[] st = new SoShapeHintsElement.ShapeType[1];
    final SoShapeHintsElement.FaceType[] ft = new SoShapeHintsElement.FaceType[1];
    SoShapeHintsElement.get(state, vo, st, ft);

    if ((numQuads > 0 || numFaces > 0)
      && ft[0] != SoShapeHintsElement.FaceType.CONVEX) {

        // Use generatePrimitives for now...
        SoShape_GLRender(action);
        return;
    }


    if (vpCache.shouldGenerateNormals(shapeStyle)) {
      // See if there is a valid normal cache. If so, use it:
      SoNormalCache normCache = getNormalCache();
      if (normCache == null || !normCache.isValid(state)) {
        final SoNormalBundle nb = new SoNormalBundle(action, false);
        nb.initGenerator(totalNumVertices);
        generateDefaultNormals(state, nb);
        normCache = getNormalCache();
        nb.destructor();
      }
      vpCache.numNorms = normCache.getNum();
      vpCache.normalPtr = /*(byte[])*/normCache.getNormalsFloat(); // java port
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
      SoGLTextureCoordinateElement.setTexGen(state, this, null);
    }           

    //If lighting or texturing is off, this vpCache and other things
    //need to be reconstructed when lighting or texturing is turned
    //on, so we set the bits in the VP cache:
    if(! shapeStyle.needNormals()) vpCache.needFromState |= 
      SoVertexPropertyCache.Bits.NORMAL_BITS.getValue();
    if(! shapeStyle.needTexCoords()) vpCache.needFromState |= 
      SoVertexPropertyCache.Bits.TEXCOORD_BIT.getValue();

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
    }
    else lazyElt.send(state, SoLazyElement.masks.ALL_MASK.getValue());

//#ifdef DEBUG
    // Check for enough vertices:
    if (vpCache.numVerts < totalNumVertices + startIndex.getValue()){
      SoDebugError.post("SoFaceSet::GLRender",
        "Too few vertices specified;"+
        " need "+(totalNumVertices + startIndex.getValue())+", have "+ vpCache.numVerts);
    }
    // Check for enough colors, normals, texcoords:
    int numNormalsNeeded = 0;
    if (shapeStyle.needNormals()) switch (vpCache.getNormalBinding()) {
    case OVERALL:
      numNormalsNeeded = 1;
      break;
    case PER_VERTEX:
    case PER_VERTEX_INDEXED:
      numNormalsNeeded = totalNumVertices + startIndex.getValue();
      break;
    case PER_FACE:
    case PER_FACE_INDEXED:
    case PER_PART:
    case PER_PART_INDEXED:
      numNormalsNeeded = numTris + numQuads + numFaces;              
      break;
    }
    if (vpCache.getNumNormals() < numNormalsNeeded)
      SoDebugError.post("SoFaceSet::GLRender",
      "Too few normals specified;"+
      " need "+numNormalsNeeded+", have "+ vpCache.getNumNormals());

    if ((shapeStyle.needTexCoords() || useTexCoordsAnyway != 0) && 
      !vpCache.shouldGenerateTexCoords(shapeStyle)) {

        if (vpCache.getNumTexCoords() < 
          totalNumVertices+startIndex.getValue())
          SoDebugError.post("SoFaceSet::GLRender",
          "Too few texture coordinates specified;"+
          " need "+(totalNumVertices+startIndex.getValue())+", have "+ vpCache.getNumTexCoords());
    }
    int numColorsNeeded = 0;
    switch (vpCache.getMaterialBinding()) {
    case OVERALL:
      break;
    case PER_VERTEX:
    case PER_VERTEX_INDEXED:
      numColorsNeeded = totalNumVertices + startIndex.getValue();
      break;
    case PER_FACE:
    case PER_FACE_INDEXED:
    case PER_PART:
    case PER_PART_INDEXED:
      numColorsNeeded = numTris + numQuads + numFaces;
      break;
    }
    if (vpCache.getNumColors() < numColorsNeeded)
      SoDebugError.post("SoFaceSet::GLRender",
      "Too few diffuse colors specified;"+
      " need "+numColorsNeeded+", have "+ vpCache.getNumColors());
//#endif

    GLRenderInternal(action, useTexCoordsAnyway, shapeStyle);

    // If doing multiple colors, turn off ColorMaterial:
    if (vpCache.getNumColors() > 1) {
      SoGLLazyElement.setColorMaterial(state, false);
      ((SoGLLazyElement )SoLazyElement.getInstance(state)).
        reset(state, SoLazyElement.masks.DIFFUSE_MASK.getValue());
    }

    if (totalNumVertices < AUTO_CACHE_FS_MIN_WITHOUT_VP &&
      vpCache.mightNeedSomethingFromState(shapeStyle)) {
        SoGLCacheContextElement.shouldAutoCache(state,
          SoGLCacheContextElement.AutoCache.DO_AUTO_CACHE.getValue());
    } else if (totalNumVertices > AUTO_CACHE_FS_MAX &&
      !SoGLCacheContextElement.getIsRemoteRendering(state)) {
        SoGLCacheContextElement.shouldAutoCache(state,
          SoGLCacheContextElement.AutoCache.DONT_AUTO_CACHE.getValue());
    }           

    if (tcb != null) {
      tcb.destructor();//delete tcb;
      state.pop();
    } else if (useTexCoordsAnyway != 0) 
      state.pop();

  }
  else {
    // If doing multiple colors, turn on ColorMaterial:
    if (vpCache.getNumColors() > 1) {
      SoGLLazyElement.setColorMaterial(state, true);
    }
    //
    // Ask LazyElement to setup:
    //
    SoGLLazyElement lazyElt = (SoGLLazyElement )
      SoLazyElement.getInstance(state);

    if(vpCache.colorIsInVtxProp()){
      lazyElt.send(state, SoLazyElement.masks.ALL_MASK.getValue());
      lazyElt.sendVPPacked(state, (IntBuffer)
        vpCache.getColors(0).toIntBuffer()/*.asIntBuffer()*/);
    }
    else lazyElt.send(state, SoLazyElement.masks.ALL_MASK.getValue());

    GLRenderInternal(action, 0, shapeStyle);

    // If doing multiple colors, turn off ColorMaterial:
    if (vpCache.getNumColors() > 1) {
      SoGLLazyElement.setColorMaterial(state, false);
      ((SoGLLazyElement )SoLazyElement.getInstance(state)).
        reset(state, SoLazyElement.masks.DIFFUSE_MASK.getValue());
    }
    // Influence auto-caching algorithm:
    if (totalNumVertices > AUTO_CACHE_FS_MAX &&
      !SoGLCacheContextElement.getIsRemoteRendering(state)) {

        SoGLCacheContextElement.shouldAutoCache(state,
          SoGLCacheContextElement.AutoCache.DONT_AUTO_CACHE.getValue());
    }

    // restore USE_REST_OF_VERTICES (-1)
    if (usingUSE_REST){
      numVertices.set1Value(numVertices.getNum()-1, -1);
      numVertices.enableNotify(nvNotifyEnabled);
    }           
  }
  return;
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
    ((list.getLastField() == numVertices) ||
    (list.getLastField() == vertexProperty)) ) {
      numTris = numQuads = numFaces = totalNumVertices = -1;
      vpCache.invalidate();
  }

  SoShape_notify(list);
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

    GL2 gl2 = action.getCacheContext();
    
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

    GL2 gl2 = action.getCacheContext();
    
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

    GL2 gl2 = action.getCacheContext();
    
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


    GL2 gl2 = action.getCacheContext();
    
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


}
