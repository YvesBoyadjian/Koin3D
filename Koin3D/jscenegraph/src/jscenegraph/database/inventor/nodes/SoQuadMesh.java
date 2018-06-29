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
 |      This file defines the SoQuadMesh node class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import java.nio.Buffer;
import java.nio.IntBuffer;

import com.jogamp.opengl.GL2;

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
import jscenegraph.database.inventor.elements.SoNormalElement;
import jscenegraph.database.inventor.elements.SoShapeHintsElement;
import jscenegraph.database.inventor.elements.SoShapeStyleElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.errors.SoError;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFInt32;
import jscenegraph.database.inventor.misc.SoNotList;
import jscenegraph.database.inventor.misc.SoNotRec;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoVertexPropertyCache.SoVPCacheFunc;

/**
 * @author Yves Boyadjian
 *
 */


////////////////////////////////////////////////////////////////////////////////
//! Quadrilateral mesh shape node.
/*!
\class SoQuadMesh
\ingroup Nodes
This shape node constructs quadrilaterals out of vertices.
The vertices may be specified in
the \b vertexProperty  field (from SoVertexShape), 
or by the current inherited coordinates.
For optimal performance, the \b vertexProperty  field is recommended.


SoQuadMesh uses the coordinates,
in order, starting with the first one. 
The number of vertices in the columns and rows of the mesh are
specified by the \b verticesPerColumn  and \b verticesPerRow 
fields. (Note that these numbers are 1 greater than the number of
quadrilaterals per row and per column.)


For example, an SoQuadMesh with 
\b verticesPerColumn  of 3, and \b verticesPerRow  of 4 would use
coordinates 1, 2, 3, and 4 for the first row of vertices, coordinates
5, 6, 7, and 8 for the second row, and coordinates 9, 10, 11, and 12
for the third (last) row. The result is a mesh of 3 quadrilaterals
across by 2 down. Note: non-planar quadrilaterals formed by a quad
mesh may cause interesting but unpredictable results.


The coordinates of the mesh are transformed by the current cumulative
transformation. The mesh is drawn with the current light model and
drawing style.


Treatment of the current material and normal binding is as follows:
The <tt>PER_PART</tt> binding specifies a material or normal for each row
of the mesh. The <tt>PER_FACE</tt> binding specifies a material or normal
for each quadrilateral. The <tt>_INDEXED</tt> bindings are equivalent to
their non-indexed counterparts. The default material binding is 
<tt>OVERALL</tt>.  The default normal binding is <tt>PER_VERTEX</tt>. 


If any normals (or materials) are specified, Inventor assumes 
you provide the correct number of them, as indicated by the binding.
You will see unexpected results
if you specify fewer normals (or materials) than the shape requires.
If no normals are specified, they will be generated automatically.

\par File Format/Default
\par
\code
QuadMesh {
  startIndex 0
  verticesPerColumn 1
  verticesPerRow 1
}
\endcode

\par Action Behavior
\par
SoGLRenderAction
<BR> Draws a mesh based on the current coordinates, normals, materials, drawing style, and so on. 
\par
SoRayPickAction
<BR> Picks on the mesh based on the current coordinates and transformation. Details about the intersection are returned in an SoFaceDetail. 
\par
SoGetBoundingBoxAction
<BR> Computes the bounding box that encloses all vertices of the mesh with the current transformation applied to them.  Sets the center to the average of the coordinates of all vertices. 
\par
SoCallbackAction
<BR> If any triangle callbacks are registered with the action, they will be invoked for each successive triangle forming the quadrilaterals of the mesh. 

\par See Also
\par
SoCoordinate3, SoDrawStyle, SoFaceDetail, SoFaceSet, SoTriangleStripSet, SoVertexProperty
*/
////////////////////////////////////////////////////////////////////////////////

public class SoQuadMesh extends SoNonIndexedShape {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoQuadMesh.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoQuadMesh.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoQuadMesh.class); }    	  	
	
	  
	    //! This enum is used to indicate the current material or normal binding
	    public enum Binding {
	        OVERALL, PER_ROW, PER_QUAD, PER_VERTEX
	    };
	    
    //! \name Fields
    //@{
    //! Number of vertices per column
    public final SoSFInt32   verticesPerColumn = new SoSFInt32();      
    //! Number of vertices per row
    public final SoSFInt32   verticesPerRow = new SoSFInt32();         

	private SbVec3f             generatedNormals;      //!< Array of generated normals

    //! This stores the total number of vertices; we use this
    //! information to influence Separator's auto-caching algorithm
    //! (shapes with very few triangles should be cached because
    //! traversing them can be expensive, shapes with lots of triangles
    //! shouldn't be cached because they'll take up too much memory).
    private int                         totalNumVertices;

    //!Typedef of pointer to method on QuadMesh;
    //!This will be used to simplify declaration and initialization.
    private interface PMQM {
    	void run(SoQuadMesh qm, SoGLRenderAction action);
    }    
    
    //! Array of function pointers to render functions:
    private static PMQM[] renderFunc = new PMQM[32];
    
    static {
    	for(int i = 0 ; i<32;i++) {
    		final int ii = i;
    		renderFunc[i] = (ifs,a) -> SoError.post("SoQuadMesh RenderFunc number "+ii+" not yet implemented");
    	}
    	
    	renderFunc[6] = (SoQuadMesh qm, SoGLRenderAction action) -> qm.OmVn(action);
    }

// Constants for influencing auto-caching algorithm:

// If fewer than this many vertices, AND not using the vertexProperty
// node, auto-cache.  

    private final int AUTO_CACHE_QM_MIN_WITHOUT_VP = 20;

// And the number above which we'll say caches definitely SHOULDN'T be
// built (because they'll use too much memory):
    private final int AUTO_CACHE_QM_MAX = SoGLCacheContextElement.OIV_AUTO_CACHE_DEFAULT_MAX;


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoQuadMesh()
//
////////////////////////////////////////////////////////////////////////
{
    nodeHeader.SO_NODE_CONSTRUCTOR(/*SoQuadMesh.class*/);
    nodeHeader.SO_NODE_ADD_FIELD(verticesPerColumn,"verticesPerColumn", (1));
    nodeHeader.SO_NODE_ADD_FIELD(verticesPerRow,"verticesPerRow",    (1));
    isBuiltIn = true;
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
	super.destructor();
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Computes bounding box of vertices of mesh.
//
// Use: protected

public void
computeBBox(SoAction action, final SbBox3f box, final SbVec3f center)
//
////////////////////////////////////////////////////////////////////////
{
    //  compute the number of vertices the mesh uses
    int numVerts = (int) (verticesPerColumn.getValue() *
			  verticesPerRow.getValue());
    // Next, call the method on SoNonIndexedShape that computes the
    // bounding box and center of the given number of coordinates
    computeCoordBBox(action, numVerts, box, center);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Overrides standard method to create an SoFaceDetail instance
//    representing a picked intersection with a quad in the mesh.
//
// Use: protected, virtual

protected SoDetail 
createTriangleDetail(final SoRayPickAction action,
				 final SoPrimitiveVertex v1,
				 final SoPrimitiveVertex v2,
				 final SoPrimitiveVertex v3,
				 SoPickedPoint pp)
//
////////////////////////////////////////////////////////////////////////
{
    // When we get here, the detail in each vertex already points to an
    // SoFaceDetail (on the stack in generatePrimitives()). This detail
    // contains the correct part index for the row and face index for the
    // correct quad. However, this face detail does not contain any point
    // details. We have to create a new SoFaceDetail instance and set up
    // the SoPointDetail instances inside it to contain the 4 vertices of
    // the picked quad.

    SoFaceDetail        newFD = new SoFaceDetail();
    final SoFaceDetail  oldFD = ( SoFaceDetail ) v1.getDetail();

    // Make room in the detail for 4 vertices
    newFD.setNumPoints(4);

    // Find index of quad that was hit and the row it is in
    int quadIndex = (int) oldFD.getFaceIndex();
    int row       = (int) oldFD.getPartIndex();

    // We need the bindings to set up the material/normals stuff
    Binding materialBinding = getMaterialBinding(action);
    Binding normalBinding   = getNormalBinding(action);
    if (normalBinding == Binding.PER_VERTEX)
	normalBinding = Binding.PER_VERTEX;

    final SoPointDetail               pd = new SoPointDetail();
    int                         vert;
    final SoTextureCoordinateBundle   tcb = new SoTextureCoordinateBundle(action, false);

    // "Upper left" corner
    vert = row + quadIndex;     // Number of quads + 1 extra per row
    pd.setCoordinateIndex(vert);
    pd.setMaterialIndex(getBindIndex(materialBinding, row, quadIndex, vert));
    pd.setNormalIndex(getBindIndex(normalBinding,     row, quadIndex, vert));
    pd.setTextureCoordIndex(tcb.isFunction() ? 0 : vert);
    newFD.setPoint(0, pd);

    // "Lower left" corner
    vert += (int) verticesPerRow.getValue();
    pd.setCoordinateIndex(vert);
    pd.setMaterialIndex(getBindIndex(materialBinding, row, quadIndex, vert));
    pd.setNormalIndex(getBindIndex(normalBinding,     row, quadIndex, vert));
    pd.setTextureCoordIndex(tcb.isFunction() ? 0 : vert);
    newFD.setPoint(1, pd);

    // "Lower right" corner
    vert++;
    pd.setCoordinateIndex(vert);
    pd.setMaterialIndex(getBindIndex(materialBinding, row, quadIndex, vert));
    pd.setNormalIndex(getBindIndex(normalBinding,     row, quadIndex, vert));
    pd.setTextureCoordIndex(tcb.isFunction() ? 0 : vert);
    newFD.setPoint(2, pd);

    // "Upper right" corner
    vert -= (int) verticesPerRow.getValue();
    pd.setCoordinateIndex(vert);
    pd.setMaterialIndex(getBindIndex(materialBinding, row, quadIndex, vert));
    pd.setNormalIndex(getBindIndex(normalBinding,     row, quadIndex, vert));
    pd.setTextureCoordIndex(tcb.isFunction() ? 0 : vert);
    newFD.setPoint(3, pd);

    // The face/part indices are in the incoming details
    newFD.setFaceIndex(quadIndex);
    newFD.setPartIndex(row);

    tcb.destructor();
    pd.destructor();
    return newFD;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns current material binding from action's state.
//
// Use: private, static

private static SoQuadMesh.Binding
getMaterialBinding(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
    switch (SoMaterialBindingElement.get(action.getState())) {
      case OVERALL:
	return SoQuadMesh.Binding.OVERALL;

      case PER_PART:
      case PER_PART_INDEXED:
	return SoQuadMesh.Binding.PER_ROW;

      case PER_FACE:
      case PER_FACE_INDEXED:
	return SoQuadMesh.Binding.PER_QUAD;

      case PER_VERTEX:
      case PER_VERTEX_INDEXED:
	return SoQuadMesh.Binding.PER_VERTEX;
    }
    return SoQuadMesh.Binding.OVERALL; // Shut up C++ compiler
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns current normal binding from action's state.
//
// Use: private, static

private static SoQuadMesh.Binding
getNormalBinding(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
    switch (SoNormalBindingElement.get(action.getState())) {
      case OVERALL:
	return SoQuadMesh.Binding.OVERALL;

      case PER_PART:
      case PER_PART_INDEXED:
	return SoQuadMesh.Binding.PER_ROW;

      case PER_FACE:
      case PER_FACE_INDEXED:
	return SoQuadMesh.Binding.PER_QUAD;

      case PER_VERTEX:
      case PER_VERTEX_INDEXED:
	return SoQuadMesh.Binding.PER_VERTEX;
    }
    return SoQuadMesh.Binding.OVERALL; // Shut up C++ compiler
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns index (of material or normal) based on given binding.
//
// Use: private, static

private static int
getBindIndex(SoQuadMesh.Binding binding, int row, int quad, int vert)
//
////////////////////////////////////////////////////////////////////////
{
    int ret = -1;

    switch (binding) {
      case OVERALL:
	ret = 0;
	break;
      case PER_ROW:
	ret = row;
	break;
      case PER_QUAD:
	ret = quad;
	break;
      case PER_VERTEX:
	ret = vert;
	break;
    }

    return ret;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Figures out normals, if necessary.  Returns true if normals were
//    figured out (and the normal binding should be PER_VERTEX).
//
// Use: private

private void
figureNormals(SoState state, SoNormalBundle nb)
//
////////////////////////////////////////////////////////////////////////
{
    // See if there is a valid normal cache. If so, tell the normal
    // bundle to use it.
    SoNormalCache normCache = getNormalCache();
    if (normCache != null && normCache.isValid(state)) {
	nb.set(normCache.getNum(), normCache.getNormals());
	return;
    }

    final SoNormalElement normElt = SoNormalElement.getInstance(state);

    int numNeeded = (int) (verticesPerColumn.getValue() *
			   verticesPerRow.getValue());

    // See if there are enough normals in the state...
    if (normElt.getNum() < numNeeded)
	generateDefaultNormals(state, nb);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Generate default normals, if necessary.  Returns non-NULL if normals
//    were generated (and the normal binding should be PER_VERTEX).
//    If they are generated, a state.push() is done-- the calling
//    code MUST do a state.pop() after using the normals!  Also, the
//    calling code is responsible for freeing the normals returned.
//
// Use: private

private SbVec3f VX(int r, int c, SbVec3f[] vpCoords, int numPerRow, int t) {
	return vpCoords[((int) (t + (r) * numPerRow + (c)))];
}
		
private SbVec3f CX(int r, int c, final SoCoordinateElement   ce, int numPerRow, int t) {
	return ce.get3((int) (t + (r) * numPerRow + (c)));
}

public boolean
generateDefaultNormals(SoState state, SoNormalBundle nb)
//
////////////////////////////////////////////////////////////////////////
{
//    Because we know the connectivity of a quad mesh, we can do this
//    much more efficiently than the other shapes.  Need to check for
//    wrap-around between the first and last rows and columns.

    int     c, r, t, r0, r1, c0, c1;
    final SbVec3f     dr = new SbVec3f(), dc = new SbVec3f(), norm = new SbVec3f();
    SbVec3f[] normals;
    SoCoordinateElement   ce = null;
    SbVec3f[]               vpCoords = null;

    SoVertexProperty vp = getVertexProperty();
    if (vp!=null && vp.vertex.getNum() > 0) {
	vpCoords = vp.vertex.getValues(0);
    } else {
	ce = SoCoordinateElement.getInstance(state);
    }


    // Figure out whether or not polys are clockwise or
    // counter-clockwise
    final SoShapeHintsElement.VertexOrdering[] vertexOrdering = new SoShapeHintsElement.VertexOrdering[1];
    SoShapeHintsElement.ShapeType[] shapeType = new SoShapeHintsElement.ShapeType[1];
    SoShapeHintsElement.FaceType[] faceType = new SoShapeHintsElement.FaceType[1];
    SoShapeHintsElement.get(state, vertexOrdering, shapeType, faceType);
    boolean ccw = true;
    if (vertexOrdering[0] == SoShapeHintsElement.VertexOrdering.CLOCKWISE) ccw = false;

    int curCoord  = startIndex.getValue();
    int startInd  = (int) curCoord;
    int numPerCol = verticesPerColumn.getValue();
    int numPerRow = verticesPerRow.getValue();

    t = startIndex.getValue();

    int numNeeded = startInd + (numPerCol*numPerRow);
    normals = new SbVec3f[numNeeded]; for(int i=0; i<numNeeded;i++) normals[i] = new SbVec3f();

    // r is row number, it goes from 0 to numPerCol
    // c is col number, it goes from 0 to numPerRow

    for(r = 0; r < numPerCol; r++) {
	for(c = 0; c < numPerRow; c++) {
	    // for now, forget about wraparound
	    r0 = r - 1;
	    r1 = r + 1;
	    c0 = c - 1;
	    c1 = c + 1;
	    if (ce == null){
		if (r0 < 0)
		    if (VX(0, c,vpCoords,numPerRow,t).operator_equal_equal(VX(numPerCol - 1, c,vpCoords,numPerRow,t))) r0 = numPerCol - 2;
		    else                                  r0 = 0;
		if (r1 == numPerCol)
		    if (VX(0, c,vpCoords,numPerRow,t).operator_equal_equal(VX(numPerCol - 1, c,vpCoords,numPerRow,t))) r1 = 1;
		    else                                  r1 = numPerCol - 1;

		if (c0 < 0)
		    if (VX(r, 0,vpCoords,numPerRow,t).operator_equal_equal(VX(r, numPerRow - 1,vpCoords,numPerRow,t))) c0 = numPerRow - 2;
		    else                                  c0 = 0;
		if (c1 == numPerRow)
		    if (VX(r, 0,vpCoords,numPerRow,t).operator_equal_equal(VX(r, numPerRow - 1,vpCoords,numPerRow,t))) c1 = 1;
		    else                                  c1 = numPerRow - 1;

		dr.copyFrom(VX(r1, c,vpCoords,numPerRow,t).operator_minus( VX(r0, c,vpCoords,numPerRow,t)));
		dc.copyFrom(VX(r, c1,vpCoords,numPerRow,t).operator_minus( VX(r, c0,vpCoords,numPerRow,t)));

	    }
	    else if (ce.is3D()) {
		if (r0 < 0)
		    if (CX(0, c,ce,numPerRow,t).operator_equal_equal(CX(numPerCol - 1, c,ce,numPerRow,t))) r0 = numPerCol - 2;
		    else                                  r0 = 0;
		if (r1 == numPerCol)
		    if (CX(0, c,ce,numPerRow,t).operator_equal_equal(CX(numPerCol - 1, c,ce,numPerRow,t))) r1 = 1;
		    else                                  r1 = numPerCol - 1;

		if (c0 < 0)
		    if (CX(r, 0,ce,numPerRow,t).operator_equal_equal(CX(r, numPerRow - 1,ce,numPerRow,t))) c0 = numPerRow - 2;
		    else                                  c0 = 0;
		if (c1 == numPerRow)
		    if (CX(r, 0,ce,numPerRow,t).operator_equal_equal(CX(r, numPerRow - 1,ce,numPerRow,t))) c1 = 1;
		    else                                  c1 = numPerRow - 1;

		dr.copyFrom(CX(r1, c,ce,numPerRow,t).operator_minus( CX(r0, c,ce,numPerRow,t)));
		dc.copyFrom(CX(r, c1,ce,numPerRow,t).operator_minus( CX(r, c0,ce,numPerRow,t)));
	    }
	    // For 4D coordinates, ce.get3() returns a reference to a
	    // value that goes away on the next call, so we have to
	    // save them
	    else {
		final SbVec3f t3 = new SbVec3f();
		if (r0 < 0) {
		    t3.copyFrom(CX(0, c,ce,numPerRow,t));
		    if (t3.operator_equal_equal(CX(numPerCol - 1, c,ce,numPerRow,t)))     r0 = numPerCol - 2;
		    else                                r0 = 0;
		}
		if (r1 == numPerCol) {
		    t3.copyFrom(CX(0, c,ce,numPerRow,t));
		    if (t3.operator_equal_equal(CX(numPerCol - 1, c,ce,numPerRow,t)))     r1 = 1;
		    else                                r1 = numPerCol - 1;
		}

		if (c0 < 0) {
		    t3.copyFrom(CX(r, 0,ce,numPerRow,t));
		    if (t3.operator_equal_equal(CX(r, numPerRow - 1,ce,numPerRow,t)))     c0 = numPerRow - 2;
		    else                                c0 = 0;
		}
		if (c1 == numPerRow) {
		    t3.copyFrom(CX(r, 0,ce,numPerRow,t));
		    if (t3.operator_equal_equal(CX(r, numPerRow - 1,ce,numPerRow,t)))     c1 = 1;
		    else                                c1 = numPerRow - 1;
		}

		t3.copyFrom(CX(r1, c,ce,numPerRow,t));
		dr.copyFrom(t3.operator_minus( CX(r0, c,ce,numPerRow,t)));
		t3.copyFrom(CX(r, c1,ce,numPerRow,t));
		dc.copyFrom(t3.operator_minus( CX(r, c0,ce,numPerRow,t)));
	    }
	    norm.copyFrom(dr.cross(dc));
	    norm.normalize();
	    if (!ccw) norm.negate();
	    normals[startInd + r * numPerRow + c].copyFrom(norm);
	}       
    }

    nb.set(numNeeded, normals);

    // Cache the resulting normals
    setNormalCache(state, numNeeded, normals);

    return true;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Implements GL rendering.
//
// Use: extender

public void
GLRender(SoGLRenderAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoState state = action.getState();

    // Get ShapeStyleElement
    final SoShapeStyleElement shapeStyle = SoShapeStyleElement.get(state);

    SoVertexProperty vp = getVertexProperty();
    
    // First see if the object is visible and should be rendered now:
    if (shapeStyle.mightNotRender()) {
	if (vpCache.mightNeedSomethingFromState(shapeStyle)) {
	    vpCache.fillInColorAndTranspAvail(vp, state);
	}
	if (! shouldGLRender(action))
	    return;
    }

    if (vpCache.mightNeedSomethingFromState(shapeStyle)) {
	vpCache.fillInCache(vp, state);

	totalNumVertices = verticesPerColumn.getValue()*
		verticesPerRow.getValue();

	if (vpCache.shouldGenerateNormals(shapeStyle)) {

	    // See if there is a normal cache we can use. If not,
	    // generate normals and cache them.
	    SoNormalCache normCache = getNormalCache();
	    if (normCache == null || ! normCache.isValid(state)) {
		final SoNormalBundle nb = new SoNormalBundle(action, false);
		nb.initGenerator(totalNumVertices);
		generateDefaultNormals(state, nb);
		normCache = getNormalCache();
		nb.destructor();
	    }
	    vpCache.numNorms = normCache.getNum();
	    vpCache.normalPtr = normCache.getNormalsFloat();
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

	if(vpCache.colorIsInVtxProp()){
	    lazyElt.send(state, SoLazyElement.masks.ALL_MASK.getValue());
	    lazyElt.sendVPPacked(state, (IntBuffer)
		vpCache.getColors(0).toIntBuffer()/*.asIntBuffer()*/);
	}
	else lazyElt.send(state, SoLazyElement.masks.ALL_MASK.getValue());

//#ifdef DEBUG
	// check if enough vertices:
	if (vpCache.numVerts < totalNumVertices + startIndex.getValue()){
	       SoDebugError.post("SoQuadMesh::GLRender",
		"Too few vertices specified;"+
		" need "+(totalNumVertices + startIndex.getValue())+", have "+vpCache.numVerts);
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
	  //Note that PER_FACE is really PER_QUAD
	  case PER_FACE:
	  case PER_FACE_INDEXED:
	    numNormalsNeeded = (verticesPerColumn.getValue()-1)*
		(verticesPerRow.getValue()-1);
	    break;
	  //Note that PER_PART is really PER_ROW
	  case PER_PART:
	  case PER_PART_INDEXED:
	    numNormalsNeeded = (verticesPerColumn.getValue()-1);
	    break;
	}
	if (vpCache.getNumNormals() < numNormalsNeeded)
	    SoDebugError.post("SoQuadMesh::GLRender",
			       "Too few normals specified;"+
			       " need "+numNormalsNeeded+", have "+ vpCache.getNumNormals());

	if ((shapeStyle.needTexCoords() || useTexCoordsAnyway != 0) && 
	    !vpCache.shouldGenerateTexCoords(shapeStyle)) {

	    if (vpCache.getNumTexCoords() < 
			totalNumVertices+startIndex.getValue())
		SoDebugError.post("SoQuadMesh::GLRender",
		   "Too few texture coordinates specified;"+
		   " need "+(totalNumVertices+startIndex.getValue())+", have "+ vpCache.getNumTexCoords());
	}
	int numColorsNeeded = 0;
	switch (vpCache.getMaterialBinding()) {
	  case OVERALL:
	    break;
	  case PER_VERTEX:
	  case PER_VERTEX_INDEXED:
	    numColorsNeeded = verticesPerColumn.getValue()*
		verticesPerRow.getValue() + startIndex.getValue();
	    break;
	  case PER_FACE:
	  case PER_FACE_INDEXED:
	     numColorsNeeded = (verticesPerColumn.getValue()-1)*
		(verticesPerRow.getValue()-1);  
	    break;
	  case PER_PART:
	  case PER_PART_INDEXED:
	    numColorsNeeded = (verticesPerColumn.getValue()-1);
	    break;
	}
	if (vpCache.getNumColors() < numColorsNeeded)
	    SoDebugError.post("SoQuadMesh::GLRender",
			       "Too few diffuse colors specified;"+
			       " need "+numColorsNeeded+", have "+ vpCache.getNumColors());
//#endif

	// Call the appropriate render loop:
	(this.renderFunc[useTexCoordsAnyway | 
		vpCache.getRenderCase(shapeStyle)]).run(this,action);

	// If doing multiple colors, turn off ColorMaterial:
	if (vpCache.getNumColors() > 1) {
	    SoGLLazyElement.setColorMaterial(state, false);
	    ((SoGLLazyElement )SoLazyElement.getInstance(state)).
		reset(state, SoLazyElement.masks.DIFFUSE_MASK.getValue());
	}

	// Influence auto-caching algorithm:
	if (verticesPerColumn.getValue() < AUTO_CACHE_QM_MIN_WITHOUT_VP &&
	    vpCache.mightNeedSomethingFromState(shapeStyle)) {
	    SoGLCacheContextElement.shouldAutoCache(state,
		SoGLCacheContextElement.AutoCache.DO_AUTO_CACHE.getValue());
	} else if (totalNumVertices > AUTO_CACHE_QM_MAX) {
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
	    lazyElt.sendVPPacked(state,(IntBuffer) 
		vpCache.getColors(0).toIntBuffer()/*.asIntBuffer()*/);
	}
	else lazyElt.send(state, SoLazyElement.masks.ALL_MASK.getValue());

	// Call the appropriate render loop:
	(this.renderFunc[vpCache.getRenderCase(shapeStyle)]).run(this,action); 

	// If doing multiple colors, turn off ColorMaterial:
	if (vpCache.getNumColors() > 1) {
	    SoGLLazyElement.setColorMaterial(state, false);
	    ((SoGLLazyElement )SoLazyElement.getInstance(state)).
		reset(state, SoLazyElement.masks.DIFFUSE_MASK.getValue());
	}

	// Influence auto-caching algorithm:
	if (totalNumVertices > AUTO_CACHE_QM_MAX) {

	    SoGLCacheContextElement.shouldAutoCache(state,
			    SoGLCacheContextElement.AutoCache.DONT_AUTO_CACHE.getValue());
	}           
    }

    return;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Redefines this to invalidate caches.
//
// Use: private 

public void
notify(SoNotList list)
//
////////////////////////////////////////////////////////////////////////
{
    if ((list.getLastRec().getType() == SoNotRec.Type.CONTAINER) &&
	((list.getLastField() == vertexProperty) ||
	(list.getLastField() == verticesPerRow) ||
	(list.getLastField() == verticesPerColumn))) {
	vpCache.invalidate();
    }

    SoShape_notify(list);
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Generates triangles representing a quad mesh.
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

    //put the vertexProperty into the state:
    SoState state = action.getState();
    state.push();
    SoVertexProperty vp = getVertexProperty();
    if(vp != null){
	vp.doAction(action);
    }


    final SoPrimitiveVertex           pv = new SoPrimitiveVertex();
    final SoFaceDetail                fd = new SoFaceDetail();
    final SoPointDetail               pd = new SoPointDetail();
    SoCoordinateElement   ce;
    int                         topVert, botVert, curVert;
    int                         quadIndex, matlIndex, normIndex, tcIndex;
    int                         row, col, numRows, numCols;
    Binding                     materialBinding, normalBinding;
    final SoNormalBundle              nb = new SoNormalBundle(action, false);
    final SoTextureCoordinateBundle   tcb = new SoTextureCoordinateBundle(action, false, ! forPicking);

    ce = SoCoordinateElement.getInstance(action.getState());

    materialBinding = getMaterialBinding(action);
    normalBinding   = getNormalBinding(action);

    // Get number of rows and columns (of quads, not vertices)
    numRows = (int) verticesPerColumn.getValue() - 1;
    numCols = (int) verticesPerRow.getValue()    - 1;

    topVert = (int) startIndex.getValue();
    botVert = topVert + numCols + 1;

    if (forPicking) {
	pv.setTextureCoords(new SbVec4f(0.0f, 0.0f, 0.0f, 0.0f));
	pv.setDetail(fd);
    }
    else
	pv.setDetail(pd);

    // Generate default normals, if necessary:
    if (SoNormalElement.getInstance(state).getNum() == 0) {
	figureNormals(action.getState(), nb);
	normalBinding = Binding.PER_VERTEX;
    }

    for (row = 0; row < numRows; row++) {

	fd.setPartIndex(row);

	// If either material or normal binding is per quad, we have
	// to generate each quad separately, to get the materials and
	// normals in the details to be correct
	if (materialBinding == Binding.PER_QUAD || normalBinding == Binding.PER_QUAD) {

	    for (col = -1; col < numCols; col++) {
		if (col >= 0) {
		    // Generate a triangle strip from the 4 vertices of the
		    // independent quad, in this order:
		    //          topVert-1, botVert-1, botVert, topVert

		    quadIndex = row * numCols + col;

		    fd.setFaceIndex(quadIndex);

		    beginShape(action, SoShape.TriangleShape.TRIANGLE_STRIP,
			       forPicking ? null : fd);

		    curVert = topVert - 1;
		    matlIndex = getBindIndex(materialBinding, row, quadIndex, curVert);
		    normIndex = getBindIndex(normalBinding,   row, quadIndex, curVert);
		    tcIndex   = tcb.isFunction() ? 0 : curVert;

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

		    curVert = botVert - 1;
		    matlIndex = getBindIndex(materialBinding, row, quadIndex, curVert);
		    normIndex = getBindIndex(normalBinding,   row, quadIndex, curVert);
		    tcIndex   = tcb.isFunction() ? 0 : curVert;

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

		    curVert = topVert;
		    matlIndex = getBindIndex(materialBinding, row, quadIndex, curVert);
		    normIndex = getBindIndex(normalBinding,   row, quadIndex, curVert);
		    tcIndex   = tcb.isFunction() ? 0 : curVert;

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

		    curVert = botVert;
		    matlIndex = getBindIndex(materialBinding, row, quadIndex, curVert);
		    normIndex = getBindIndex(normalBinding,   row, quadIndex, curVert);
		    tcIndex   = tcb.isFunction() ? 0 : curVert;

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

		    endShape();
		}

		topVert++;
		botVert++;
	    }
	}

	// We can generate triangle strips, which is a little easier
	else {
	    beginShape(action, SoShape.TriangleShape.TRIANGLE_STRIP, forPicking ? null : fd);

	    for (col = -1; col < numCols; col++) {

		quadIndex = row * numCols + col;

		fd.setFaceIndex(quadIndex);

		// Generate two vertices for each column:
		//      topVert, then botVert

		curVert = topVert;
		matlIndex = getBindIndex(materialBinding, row, quadIndex, curVert);
		normIndex = getBindIndex(normalBinding,   row, quadIndex, curVert);
		tcIndex   = tcb.isFunction() ? 0 : curVert;

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

		curVert = botVert;
		matlIndex = getBindIndex(materialBinding, row, quadIndex, curVert);
		normIndex = getBindIndex(normalBinding,   row, quadIndex, curVert);
		tcIndex   = tcb.isFunction() ? 0 : curVert;

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

		topVert++;
		botVert++;
	    }

	    endShape();
	}
    }

    state.pop();

    pv.destructor(); // java port
    fd.destructor();
    pd.destructor();
    nb.destructor();
    tcb.destructor();
}


public void

OmVn
    (SoGLRenderAction renderAction ) {
	
	GL2 gl2 = renderAction.getCacheContext();

    Buffer vertexPtr = vpCache.getVertices(startIndex.getValue());
    final int vertexStride = vpCache.getVertexStride();
    SoVPCacheFunc vertexFunc = vpCache.vertexFunc;
    final int vertexRowStride = vertexStride*
	verticesPerRow.getValue();
    Buffer normalPtr = vpCache.getNormals(startIndex.getValue());
    final int normalStride = vpCache.getNormalStride();
    final int normalRowStride = normalStride*verticesPerRow.getValue();
    SoVPCacheFunc normalFunc = vpCache.normalFunc;

    final int numRows = verticesPerColumn.getValue() - 1;
     final int nv = verticesPerRow.getValue();

    int v;
	int vertexPtrIndex = 0;
	int normalPtrIndex = 0;
    for (int row = 0; row < numRows; row++) {

//Do Strip rendering if both ~Fn and ~Fm
	gl2.glBegin(GL2.GL_TRIANGLE_STRIP);
	for (v = 0; v < nv; v++) {

		normalPtr.position(normalPtrIndex/Float.BYTES);
	    (normalFunc).run(gl2,normalPtr);
		vertexPtr.position(vertexPtrIndex/Float.BYTES);
	    (vertexFunc).run(gl2,vertexPtr);

		normalPtr.position((normalPtrIndex+normalRowStride)/Float.BYTES);
	    (normalFunc).run(gl2,normalPtr/*+normalRowStride*/);
	    normalPtrIndex += normalStride;
		vertexPtr.position((vertexPtrIndex+vertexRowStride)/Float.BYTES);
	    (vertexFunc).run(gl2,vertexPtr/*+vertexRowStride*/);
	    vertexPtrIndex += vertexStride;
	}

	gl2.glEnd();

    }
}


    

////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoQuadMesh class.
//
// Use: internal

public static void
initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SO__NODE_INIT_CLASS(SoQuadMesh.class, "QuadMesh", SoNonIndexedShape.class);
}

}
