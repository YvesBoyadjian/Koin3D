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
 |      This file defines the SoLineSet node class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import java.nio.IntBuffer;

import jscenegraph.opengl.GL2;
import jscenegraph.opengl.GL3;

import jscenegraph.coin3d.inventor.elements.SoGLMultiTextureCoordinateElement;
import jscenegraph.coin3d.inventor.nodes.SoVertexProperty;
import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec3fSingle;
import jscenegraph.database.inventor.SoDebug;
import jscenegraph.database.inventor.SoPickedPoint;
import jscenegraph.database.inventor.SoPrimitiveVertex;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoGetBoundingBoxAction;
import jscenegraph.database.inventor.actions.SoRayPickAction;
import jscenegraph.database.inventor.bundles.SoTextureCoordinateBundle;
import jscenegraph.database.inventor.caches.SoBoundingBoxCache;
import jscenegraph.database.inventor.details.SoDetail;
import jscenegraph.database.inventor.details.SoLineDetail;
import jscenegraph.database.inventor.details.SoPointDetail;
import jscenegraph.database.inventor.elements.SoCoordinateElement;
import jscenegraph.database.inventor.elements.SoDrawStyleElement;
import jscenegraph.database.inventor.elements.SoGLCacheContextElement;
import jscenegraph.database.inventor.elements.SoGLLazyElement;
import jscenegraph.database.inventor.elements.SoLazyElement;
import jscenegraph.database.inventor.elements.SoMaterialBindingElement;
import jscenegraph.database.inventor.elements.SoNormalBindingElement;
import jscenegraph.database.inventor.elements.SoNormalElement;
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
import jscenegraph.mevis.inventor.elements.SoGLVBOElement;
import jscenegraph.mevis.inventor.misc.SoVBO;
import jscenegraph.port.IntArrayPtr;
import jscenegraph.port.IntPtr;
import jscenegraph.port.SbVec3fArray;


////////////////////////////////////////////////////////////////////////////////
//! Polyline shape node.
/*!
\class SoLineSet
\ingroup Nodes
This node represents a 3D shape formed by constructing polylines from
vertices located at the coordinates specified in the
\b vertexProperty  field (from SoVertexShape), 
or the current inherited coordinates. 
For optimal performance, the \b vertexProperty  field is recommended.


SoLineSet uses the
coordinates in order, starting with the first one. 
Each line has a number of vertices specified by
a value in the \b numVertices  field. For example, an SoLineSet
with a \b numVertices  of [3,4,2] would use
coordinates 1, 2, and 3 for the first line, coordinates 4, 5, 6, and 7
for the second line, and coordinates 8 and 9 for the third. 

The number of values in the \b numVertices  field indicates the
number of polylines in the set.


The coordinates of the line set are transformed by the current
cumulative transformation. The lines are drawn with the current light
model and drawing style (drawing style <tt>FILLED</tt> is treated as
<tt>LINES</tt>).


Treatment of the current material and normal binding is as follows:
The <tt>PER_PART</tt> binding specifies a material or normal for each
segment of the line. The <tt>PER_FACE</tt> binding specifies a material or
normal for each polyline. The <tt>_INDEXED</tt> bindings are equivalent to
their non-indexed counterparts. The default material binding is
<tt>OVERALL</tt>. The default normal binding is 
<tt>PER_VERTEX</tt>.


The current complexity value has no effect on the rendering of line
sets.

\par File Format/Default
\par
\code
LineSet {
  startIndex 0
  numVertices -1
}
\endcode

\par Action Behavior
\par
SoGLRenderAction
<BR> Draws lines based on the current coordinates, normals, materials, drawing style, and so on. 
\par
SoRayPickAction
<BR> Picks lines based on the current coordinates and transformation. Details about the intersection are returned in an SoLineDetail. 
\par
SoGetBoundingBoxAction
<BR> Computes the bounding box that encloses all vertices of the line set with the current transformation applied to them.  Sets the center to the average of the coordinates of all vertices. 
\par
SoCallbackAction
<BR> If any line segment callbacks are registered with the action, they will be invoked for each successive segment in the line set. 

\par See Also
\par
SoCoordinate3, SoDrawStyle, SoIndexedLineSet, SoLineDetail, SoVertexProperty
*/
////////////////////////////////////////////////////////////////////////////////


/**
 * @author Yves Boyadjian
 *
 */
public class SoLineSet extends SoNonIndexedShape {
	
// Constants for influencing auto-caching algorithm:

// If fewer than this many lines, AND not using the vertexProperty
// node, auto-cache.  

	private static final int AUTO_CACHE_LS_MIN_WITHOUT_VP = 20;

// And the number above which we'll say caches definitely SHOULDN'T be
// built (because they'll use too much memory):
	private static final int AUTO_CACHE_LS_MAX = SoGLCacheContextElement.OIV_AUTO_CACHE_DEFAULT_MAX;

	
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoLineSet.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoLineSet.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoLineSet.class); }    	  	
	
	
	
	public static final int SO_LINE_SET_USE_REST_OF_VERTICES  =      (-1);
	
	    //! This enum is used to indicate the current material or normal binding
    enum Binding {
        OVERALL, PER_LINE, PER_SEGMENT, PER_VERTEX
    };
    
    //!Typedef of pointer to method on IndexedLineSet;
    //!This will be used to simplify declaration and initialization.
    private interface PMLS {
    	void run(SoLineSet ls, SoGLRenderAction action);
    }    	

	 public final SoMFInt32 numVertices = new SoMFInt32();
	  
	   public final SoSFBool sendAdjacency = new SoSFBool();
	  
    //! This stores the total number of vertices; we use this
    //! information to influence Separator's auto-caching algorithm
    //! (shapes with very few triangles should be cached because
    //! traversing them can be expensive, shapes with lots of triangles
    //! shouldn't be cached because they'll take up too much memory).
    private int                         totalNumVertices;

    //! Array of function pointers to render functions:
    private static PMLS[] renderFunc = new PMLS[32];
    
    static {
    	for(int i = 0 ; i<32;i++) {
    		final int ii = i;
    		renderFunc[i] = (ifs,a) -> SoError.post("SoLineSet RenderFunc number "+ii+" not yet implemented");
    	}
    	    	
    }
    
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoLineSet()
//
////////////////////////////////////////////////////////////////////////
{
  nodeHeader.SO_NODE_CONSTRUCTOR(/*SoLineSet*/);
  nodeHeader.SO_NODE_ADD_MFIELD(numVertices,"numVertices",  (SO_LINE_SET_USE_REST_OF_VERTICES));
  nodeHeader.SO_NODE_ADD_SFIELD(sendAdjacency,"sendAdjacency", (false));
  isBuiltIn = true;
  totalNumVertices = -1;
}

	   
	   
	   
	   
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Computes bounding box of vertices of line set.
//
// Use: protected

public void
computeBBox(SoAction action, final SbBox3f box, final SbVec3f center)
//
////////////////////////////////////////////////////////////////////////
{
  // First, compute the number of vertices the line set uses
  int numLines = (int) numVertices.getNum(), numVerts = 0, i;

  if (numLines == 0)
    return;

  // Count up total number of vertices used. If the last entry in
  // numVertices is SO_LINE_SET_USE_REST_OF_VERTICES, then we need
  // to use all of the vertices.
  if (numVertices.operator_square_bracketI(numLines - 1) == SO_LINE_SET_USE_REST_OF_VERTICES)
    numVerts = -1;

  else
    for (i = 0; i < numLines; i++)
      numVerts += (int) numVertices.operator_square_bracketI(i);

  // Next, call the method on SoNonIndexedShape that computes the
  // bounding box and center of the given number of coordinates
  computeCoordBBox(action, numVerts, box, center);
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Overrides standard method to create an SoLineDetail instance
//    representing a picked intersection with a vertex of a line set.
//
// Use: protected, virtual

public SoDetail 
createLineSegmentDetail(SoRayPickAction action,
                                   final SoPrimitiveVertex v1,
                                   final SoPrimitiveVertex v2,
                                   SoPickedPoint pp)
                                   //
                                   ////////////////////////////////////////////////////////////////////////
{
  SoLineDetail        detail = new SoLineDetail();
  SoLineDetail  d;

  d = ( SoLineDetail ) v1.getDetail();

  detail.setPoint0(d.getPoint0());
  detail.setPoint1(d.getPoint1());

  // Compute texture coordinates at intersection point and store it
  // in the picked point
  final SoTextureCoordinateBundle   tcb = new SoTextureCoordinateBundle(action, false);
  if (tcb.isFunction())
    pp.setObjectTextureCoords(tcb.get(pp.getObjectPoint(),
    pp.getObjectNormal()));

  // The face/part indices are in the incoming details
  detail.setLineIndex(d.getLineIndex());
  detail.setPartIndex(d.getPartIndex());

  return detail;
}



////////////////////////////////////////////////////////////////////////
//
// Description:
//    Generates line segments representing a line set.
//
// Use: protected

protected void
generatePrimitives(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
	  SoState state = action.getState();

	  if (this.vertexProperty.getValue() != null) {
	    state.push();
	    this.vertexProperty.getValue().doAction(action);
	  }

	  final SoCoordinateElement[] coords = new SoCoordinateElement[1]; // ptr
	  final SbVec3fArray[] normals = new SbVec3fArray[1];
	  boolean doTextures;
	  boolean needNormals = true;

	  SoVertexShape.getVertexData(action.getState(), coords, normals,
	                               needNormals);

	  if (normals[0] == null) needNormals = false;

	  final SoTextureCoordinateBundle tb = new SoTextureCoordinateBundle(action, false, false);
	  doTextures = tb.needCoordinates();

	  Binding mbind = findMaterialBinding(action.getState());
	  Binding nbind = findNormalBinding(action.getState());

	  if (!needNormals) nbind = Binding.OVERALL;

	  final SoPrimitiveVertex vertex = new SoPrimitiveVertex();
	  final SoLineDetail lineDetail = new SoLineDetail();
	  final SoPointDetail pointDetail = new SoPointDetail();

	  vertex.setDetail(pointDetail);

	  final SbVec3fSingle dummynormal = new SbVec3fSingle(0.0f, 0.0f, 1.0f);
	  SbVec3fArray currnormal = new SbVec3fArray(dummynormal);
	  if (normals[0] != null) currnormal = normals[0];
	  if (nbind == Binding.OVERALL && needNormals) {
	    vertex.setNormal(currnormal.get(0));
	  }

	  int idx = this.startIndex.getValue();
	  final int[] dummyarray = new int[1];
	  IntArrayPtr ptr = this.numVertices.getValuesIntArrayPtr(0);
	  IntArrayPtr end = IntArrayPtr.plus(ptr,this.numVertices.getNum());
	  IntArrayPtr[] dptr = new IntArrayPtr[1]; dptr[0] = ptr;
	  IntArrayPtr[] dend = new IntArrayPtr[1]; dend[0] = end;
	  this.fixNumVerticesPointers(state, dptr, dend, dummyarray);
	  ptr = dptr[0];
	  end = dend[0];

	  int normnr = 0;
	  int matnr = 0;
	  int texnr = 0;

	  if (nbind == Binding.PER_SEGMENT || mbind == Binding.PER_SEGMENT) {
	    this.beginShape(action, SoShape.TriangleShape.LINES, lineDetail);

	    while (IntArrayPtr.lessThan(ptr,end)) {
	      int n = ptr.get(); ptr.plusPlus();
	      if (n < 2) {
	        idx += n;
	        continue;
	      }
	      if (nbind == Binding.PER_LINE || nbind == Binding.PER_VERTEX) {
	        pointDetail.setNormalIndex(normnr);
	        currnormal = normals[0].plus(normnr++);
	        vertex.setNormal(currnormal.get(0));
	      }
	      if (mbind == Binding.PER_LINE || mbind == Binding.PER_VERTEX) {
	        pointDetail.setMaterialIndex(matnr);
	        vertex.setMaterialIndex(matnr++);
	      }
	      if (doTextures) {
	        if (tb.isFunction())
	          vertex.setTextureCoords(tb.get(coords[0].get3(idx), currnormal.get(0)));
	        else {
	          pointDetail.setTextureCoordIndex(texnr);
	          vertex.setTextureCoords(tb.get(texnr++));
	        }
	      }
	      while (--n != 0) {
	        if (nbind == Binding.PER_SEGMENT) {
	          pointDetail.setNormalIndex(normnr);
	          currnormal = normals[0].plus(normnr++);
	          vertex.setNormal(currnormal.get(0));
	        }
	        if (mbind == Binding.PER_SEGMENT) {
	          pointDetail.setMaterialIndex(matnr);
	          vertex.setMaterialIndex(matnr++);
	        }
	        pointDetail.setCoordinateIndex(idx);
	        vertex.setPoint(coords[0].get3(idx++));
	        this.shapeVertex(vertex);

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
	          if (tb.isFunction())
	            vertex.setTextureCoords(tb.get(coords[0].get3(idx), currnormal.get(0)));
	          else {
	            pointDetail.setTextureCoordIndex(texnr);
	            vertex.setTextureCoords(tb.get(texnr++));
	          }
	        }
	        pointDetail.setCoordinateIndex(idx);
	        vertex.setPoint(coords[0].get3(idx));
	        this.shapeVertex(vertex);
	        lineDetail.incPartIndex();
	      }
	      lineDetail.incLineIndex();
	      idx++; // next (poly)line should use the next index
	    }
	    this.endShape();
	  }
	  else {
	    while (IntArrayPtr.lessThan(ptr,end)) {
	      lineDetail.setPartIndex(0);
	      int n = ptr.get(); ptr.plusPlus();
	      if (n < 2) {
	        idx += n;
	        continue;
	      }
	      n -= 2;
	      this.beginShape(action, SoShape.TriangleShape.LINE_STRIP, lineDetail);
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
	        if (tb.isFunction())
	          vertex.setTextureCoords(tb.get(coords[0].get3(idx), currnormal.get(0)));
	        else {
	          pointDetail.setTextureCoordIndex(texnr);
	          vertex.setTextureCoords(tb.get(texnr++));
	        }
	      }
	      pointDetail.setCoordinateIndex(idx);
	      vertex.setPoint(coords[0].get3(idx++));
	      this.shapeVertex(vertex);
	      do {
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
	          if (tb.isFunction())
	            vertex.setTextureCoords(tb.get(coords[0].get3(idx), currnormal.get(0)));
	          else {
	            pointDetail.setTextureCoordIndex(texnr);
	            vertex.setTextureCoords(tb.get(texnr++));
	          }
	        }
	        pointDetail.setCoordinateIndex(idx);
	        vertex.setPoint(coords[0].get3(idx++));
	        this.shapeVertex(vertex);
	        lineDetail.incPartIndex();
	      } while (n-- != 0);
	      this.endShape();
	      lineDetail.incLineIndex();
	    }
	  }
	  if (this.vertexProperty.getValue() != null)
	    state.pop();
	  
	  tb.destructor();
	}


//{
//  SoState state = action.getState();
//  state.push();
//  // put vertex property into state, if it exists:
//  SoVertexProperty vp = getVertexProperty();
//  if(vp != null){
//    vp.doAction(action);
//  }
//
//  // When generating primitives for picking, delay computing default
//  // texture coordinates
//  boolean forPicking = action.isOfType(SoRayPickAction.getClassTypeId());
//
//  final SoPrimitiveVertex[]           pvs = new SoPrimitiveVertex[2];
//  pvs[0] = new SoPrimitiveVertex();
//  pvs[1] = new SoPrimitiveVertex();
//  SoPrimitiveVertex pv;
//  final SoLineDetail                detail = new SoLineDetail();
//  final SoPointDetail               pd = new SoPointDetail();
//  final SoTextureCoordinateBundle   tcb = new SoTextureCoordinateBundle(action, false, ! forPicking);
//  SoCoordinateElement   ce;
//  int                         curVert, curSeg, curNormal, curMaterial, vert;
//  int                         line, numLines, vertsInLine;
//  Binding                     materialBinding, normalBinding;
//
//
//  materialBinding = getMaterialBinding(action);
//  normalBinding   = getNormalBinding(action);
//
//  // Test for auto-normal case
//  final SoNormalElement ne = SoNormalElement.getInstance(state);
//  if (ne.getNum() == 0) {
//    normalBinding = Binding.OVERALL;
//  }
//
//  curVert = (int) startIndex.getValue();
//  curSeg  = 0;
//
//  ce = SoCoordinateElement.getInstance(state);
//
//  curMaterial = (materialBinding == Binding.PER_VERTEX ? curVert : 0);
//  curNormal   = (normalBinding   == Binding.PER_VERTEX ? curVert : 0);
//
//  if (forPicking) {
//    final SbVec4f tc = new SbVec4f(0.0f, 0.0f, 0.0f, 0.0f);
//    pvs[0].setTextureCoords(tc);
//    pvs[1].setTextureCoords(tc);
//  }
//
//  pvs[0].setDetail(detail);
//  pvs[1].setDetail(detail);
//
//  if (normalBinding == Binding.OVERALL) {
//    if (ne.getNum() > 0) {
//      pvs[0].setNormal(ne.get(0));
//      pvs[1].setNormal(ne.get(0));
//    } else {
//      pvs[0].setNormal(new SbVec3f(0,0,0));
//      pvs[1].setNormal(new SbVec3f(0,0,0));
//    }           
//  }
//
//  // For each polyline
//  numLines = numVertices.getNum();
//  for (line = 0; line < numLines; line++) {
//
//    detail.setLineIndex(line);
//
//    // Figure out number of vertices in this line
//    vertsInLine = (int) numVertices.operator_square_bracketI(line);
//    if (vertsInLine == SO_LINE_SET_USE_REST_OF_VERTICES)
//      vertsInLine = (int) ce.getNum() - curVert;
//
//    for (vert = 0; vert < vertsInLine; vert++) {
//
//      pv = pvs[vert % 2];
//
//      pv.setPoint(ce.get3(curVert));
//
//      if (materialBinding == Binding.PER_VERTEX && vert > 0)
//        pv.setMaterialIndex(++curMaterial);
//      if (normalBinding == Binding.PER_VERTEX && vert > 0)
//        pv.setNormal(ne.get(++curNormal));
//
//      // Set up a point detail for the current vertex
//      pd.setCoordinateIndex(curVert);
//      pd.setMaterialIndex(curMaterial);
//      pd.setNormalIndex(curNormal);
//      pd.setTextureCoordIndex(curVert);
//
//      // Replace the appropriate point detail in the line
//      // detail, based on the vertex index
//      if ((vert & 1) == 0)
//        detail.setPoint0(pd);
//      else
//        detail.setPoint1(pd);
//
//      if (tcb.isFunction()) {
//        if (! forPicking)
//          pv.setTextureCoords(tcb.get(pv.getPoint(),
//          pv.getNormal()));
//      }
//      else
//        pv.setTextureCoords(tcb.get(curVert));
//
//      if (vert > 0) {
//        detail.setPartIndex(curSeg++);
//
//        invokeLineSegmentCallbacks(action,
//          pvs[(vert - 1) % 2],
//          pvs[(vert - 0) % 2]);
//
//        if (materialBinding == Binding.PER_SEGMENT) {
//          curMaterial++;
//          pvs[0].setMaterialIndex(curMaterial);
//          pvs[1].setMaterialIndex(curMaterial);
//        }
//        if (normalBinding == Binding.PER_SEGMENT) {
//          curNormal++;
//          pvs[0].setNormal(ne.get(curNormal));
//          pvs[1].setNormal(ne.get(curNormal));
//        }
//      }
//
//      curVert++;
//    }
//
//    if (materialBinding == Binding.PER_LINE) {
//      curMaterial++;
//      pvs[0].setMaterialIndex(curMaterial);
//      pvs[1].setMaterialIndex(curMaterial);
//    }
//    if (normalBinding == Binding.PER_LINE) {
//      curNormal++;
//      pvs[0].setNormal(ne.get(curNormal));
//      pvs[1].setNormal(ne.get(curNormal));
//    }
//  }
//  state.pop();
//  
//  pvs[0].destructor();
//  pvs[1].destructor();
//  detail.destructor();
//  pd.destructor();
//  tcb.destructor();
//}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Redefines this to tell open caches that they contain lines.
//
// Use: extender

public void
getBoundingBox(SoGetBoundingBoxAction action)
//
////////////////////////////////////////////////////////////////////////
{
  // Let our parent class do the real work
  super.getBoundingBox(action);

  // If there are any open bounding box caches, tell them that they
  // contain lines
  SoBoundingBoxCache.setHasLinesOrPoints(action.getState());
}



////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns current material binding from action's state.
//
// Use: private, static

private static SoLineSet.Binding
getMaterialBinding(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
  switch (SoMaterialBindingElement.get(action.getState())) {
      case OVERALL:
        return Binding.OVERALL;

      case PER_PART:
      case PER_PART_INDEXED:
        return Binding.PER_SEGMENT;

      case PER_FACE:
      case PER_FACE_INDEXED:
        return Binding.PER_LINE;

      case PER_VERTEX:
      case PER_VERTEX_INDEXED:
        return Binding.PER_VERTEX;
  }
  return Binding.OVERALL; // Shut up C++ compiler
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns current normal binding from action's state.
//
// Use: private, static

private static SoLineSet.Binding
getNormalBinding(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
  switch (SoNormalBindingElement.get(action.getState())) {

      case OVERALL:
        return Binding.OVERALL;

      case PER_PART:
      case PER_PART_INDEXED:
        return Binding.PER_SEGMENT;

      case PER_FACE:
      case PER_FACE_INDEXED:
        return Binding.PER_LINE;

      case PER_VERTEX:
      case PER_VERTEX_INDEXED:
        return Binding.PER_VERTEX;
  }
  return Binding.OVERALL; // Shut up C++ compiler
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns true if normal binding is AUTO and there aren't enough
//    PER_VERTEX normals, in which case we turn off lighting and draw
//    BASE_COLOR.
//
// Use: private, static

private boolean
wouldGenerateNormals(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
  if (SoNormalBindingElement.get(state) ==
    SoNormalBindingElement.Binding.PER_VERTEX_INDEXED) {

      // Figure out how many normals we need:
      int numVerts = 0;
      int numLines = (int) numVertices.getNum();

      if (numLines == 0)
        return false;

      // Count up total number of vertices used. If the last entry in
      // numVertices is SO_LINE_SET_USE_REST_OF_VERTICES, then we need
      // to use all of the vertices.
      if (numVertices.operator_square_bracketI(numLines - 1) == SO_LINE_SET_USE_REST_OF_VERTICES)
        numVerts =
        (int)(SoCoordinateElement.getInstance(state)).getNum();
      else for (int i = 0; i < numLines; i++)
        numVerts += (int) numVertices.operator_square_bracket(i);
      SoNormalElement ne = SoNormalElement.getInstance(state);
      if (numVerts > ne.getNum()) return true;
  }

  return false;
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
    // push state, in case we need to turn off lighting:
    state.push();
    vpCache.fillInCache(vp, state);

    if (vpCache.shouldGenerateNormals(shapeStyle)) {
      // turn off lighting
      SoGLLazyElement.setLightModel(state, SoLazyElement.LightModel.BASE_COLOR.getValue());
      // reset shapeStyle
      shapeStyle =        
        (SoShapeStyleElement)SoShapeStyleElement.get(state);
    }


    // If using USE_REST_OF_VERTICES (-1), need to figure out how
    // many vertices there are every time:
    boolean usingUSE_REST = false;
    boolean nvNotifyEnabled = true;
    int numPolylines = numVertices.getNum();
    if (numPolylines != 0 && numVertices.operator_square_bracketI(numPolylines-1) < 0) {
      usingUSE_REST = true;
      nvNotifyEnabled = numVertices.enableNotify(false);
      totalNumVertices = 0;
      for (int i = 0; i < numPolylines-1; i++) 
        totalNumVertices += numVertices.operator_square_bracketI(i);

      numVertices.set1Value(numPolylines-1, 
        vpCache.numVerts - totalNumVertices - startIndex.getValue());
      totalNumVertices = vpCache.numVerts - startIndex.getValue();
      vpCache.needFromState |=
        SoVertexPropertyCache.BitMask.COORD_FROM_STATE_BIT.getValue();
    } else if (totalNumVertices < 0) {
      totalNumVertices = 0;
      for (int i = 0; i < numPolylines; i++) 
        totalNumVertices += numVertices.operator_square_bracketI(i);
    }           

    SoTextureCoordinateBundle tcb = null;
    int useTexCoordsAnyway = 0;
    if (vpCache.shouldGenerateTexCoords(shapeStyle)) {
      tcb = new SoTextureCoordinateBundle(action, true, true);
    }
    else if (shapeStyle.isTextureFunction() && vpCache.haveTexCoordsInVP()){    
      useTexCoordsAnyway = SoVertexPropertyCache.Bits.TEXCOORD_BIT.getValue();
      SoGLMultiTextureCoordinateElement.setTexGen(state, this, 0, null);
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
      lazyElt.sendVPPacked(state, ( IntBuffer)
        vpCache.getColors(0).toIntBuffer()/*.asIntBuffer()*/);
    }
    else lazyElt.send(state, SoLazyElement.masks.ALL_MASK.getValue());

//#ifdef DEBUG    
    // Check for enough colors, normals, texcoords, vertices:
    if (vpCache.numVerts < startIndex.getValue()+totalNumVertices){
      SoDebugError.post("SoLineSet.GLRender",
        "Too few vertices specified;"+
        " need "+startIndex.getValue()+totalNumVertices+", have "+vpCache.numVerts);
    }
    int numNormalsNeeded = 0;
    if (shapeStyle.needNormals()) switch (vpCache.getNormalBinding()) {
    case OVERALL:
      numNormalsNeeded = 1;
      break;
    case PER_VERTEX:
    case PER_VERTEX_INDEXED:
      numNormalsNeeded = totalNumVertices + startIndex.getValue();
      break;
    case PER_PART:
    case PER_PART_INDEXED:
      {
        for (int i = 0; i < numPolylines; i++) 
          numNormalsNeeded += numVertices.operator_square_bracketI(i)-1;
      }
      break;
    case PER_FACE:
    case PER_FACE_INDEXED:
      numNormalsNeeded = numPolylines;
      break;
    }
    if (vpCache.getNumNormals() < numNormalsNeeded)
      SoDebugError.post("SoLineSet.GLRender",
      "Too few normals specified;"+
      " need "+numNormalsNeeded+", have "+vpCache.getNumNormals());

    if ((shapeStyle.needTexCoords() || useTexCoordsAnyway != 0) && 
      !vpCache.shouldGenerateTexCoords(shapeStyle)) {

        if (vpCache.getNumTexCoords() < 
          totalNumVertices+startIndex.getValue())
          SoDebugError.post("SoLineSet.GLRender",
          "Too few texture coordinates specified;"+
          " need "+totalNumVertices + startIndex.getValue()+", have "+vpCache.getNumTexCoords());
    }
    int numColorsNeeded = 0;
    switch (vpCache.getMaterialBinding()) {
    case OVERALL:
      break;
    case PER_VERTEX:
    case PER_VERTEX_INDEXED:
      numColorsNeeded = totalNumVertices+startIndex.getValue();
      break;
    case PER_PART:
    case PER_PART_INDEXED:
      {
        for (int i = 0; i < numPolylines; i++) 
          numColorsNeeded += numVertices.operator_square_bracketI(i)-1;
      }
      break;
    case PER_FACE:
    case PER_FACE_INDEXED:
      numColorsNeeded = numPolylines;
      break;
    }
    if (vpCache.getNumColors() < numColorsNeeded)
      SoDebugError.post("SoLineSet.GLRender",
      "Too few diffuse colors specified;"+
      " need "+numColorsNeeded+", have "+vpCache.getNumColors());
//#endif

    GLRenderInternal(action, useTexCoordsAnyway, shapeStyle);

    // If doing multiple colors, turn off ColorMaterial:
    if (vpCache.getNumColors() > 1) {
      ((SoGLLazyElement )SoLazyElement.getInstance(state)).
        reset(state, SoLazyElement.masks.DIFFUSE_MASK.getValue());
      SoGLLazyElement.setColorMaterial(state, false);
    }

    // Restore USE_REST_OF_VERTICES (-1)
    if (usingUSE_REST) {
      numVertices.set1Value(numPolylines-1, -1);
      numVertices.enableNotify(nvNotifyEnabled);
    }           

    // Influence auto-caching algorithm:
    if (totalNumVertices < AUTO_CACHE_LS_MIN_WITHOUT_VP &&
      vpCache.mightNeedSomethingFromState(shapeStyle)) {
        SoGLCacheContextElement.shouldAutoCache(state,
          SoGLCacheContextElement.AutoCache.DO_AUTO_CACHE.getValue());
    } else if (totalNumVertices > AUTO_CACHE_LS_MAX &&
      !SoGLCacheContextElement.getIsRemoteRendering(state)) {
        SoGLCacheContextElement.shouldAutoCache(state,
          SoGLCacheContextElement.AutoCache.DONT_AUTO_CACHE.getValue());
    }           

    if (tcb != null) {
      tcb.destructor();
    }
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
    if (totalNumVertices > AUTO_CACHE_LS_MAX &&
      !SoGLCacheContextElement.getIsRemoteRendering(state)) {

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

public void notify(SoNotList list)
//
////////////////////////////////////////////////////////////////////////
{
  if ((list.getLastRec().getType() == SoNotRec.Type.CONTAINER) &&
    ((list.getLastField() == vertexProperty) ||
    (list.getLastField() == numVertices))) {
      vpCache.invalidate();
      totalNumVertices = -1;
  }

  SoShape_notify(list);
}

void GLRenderInternal( SoGLRenderAction  action, int useTexCoordsAnyway, SoShapeStyleElement  shapeStyle )
{
  SoState  state = action.getState();

  SoGLLazyElement lazyElt;
  lazyElt = (SoGLLazyElement ) SoLazyElement.getInstance(state);

  // check for cases where GL Vertex Arrays can be used:
  if ((totalNumVertices>0) && 
    SoVBO.isVertexArrayRenderingAllowed() &&
    !lazyElt.isColorIndex(state) &&
    (vpCache.getNumVertices()>0) &&
    (vpCache.getNumNormals()==0 || (vpCache.getNormalBinding()==SoNormalBindingElement.Binding.PER_VERTEX || vpCache.getNormalBinding()==SoNormalBindingElement.Binding.PER_VERTEX_INDEXED)) &&
    // VA rendering is only possible if there is a color VBO, since it manages the packed color swapping
    ((getMaterialBinding(action)!= Binding.PER_VERTEX) || SoGLVBOElement.getInstance(state).getColorVBO()/*getVBO(SoGLVBOElement.VBOType.COLOR_VBO)*/ != null) &&
    (vpCache.getNumTexCoords()==0 || (vpCache.getTexCoordBinding() == SoTextureCoordinateBindingElement.Binding.PER_VERTEX_INDEXED)) &&
    (SoDrawStyleElement.get(action.getState()) != SoDrawStyleElement.Style.POINTS))
  {
    boolean useVBO = beginVertexArrayRendering(action);

    int np = numVertices.getNum();
    IntPtr numverts = new IntPtr(numVertices.getValuesI(0));
    int mode = sendAdjacency.getValue()?GL3.GL_LINE_STRIP_ADJACENCY:GL2.GL_LINE_STRIP;

    int offset = startIndex.getValue();
    for (int polyline = 0; polyline < np; polyline++) {
      int nv = (numverts.get());
      SoGLLazyElement.drawArrays(state, mode, offset, nv);
      numverts.plusPlus();
      offset += nv;
    }

//#ifdef DEBUG
    if (SoDebug.GetEnv("IV_DEBUG_VBO_RENDERING") != null) {
      SoDebugError.postInfo("GLRenderInternal", getTypeId().getName().getString()+" VA Rendering: "+totalNumVertices+" Vertices");
    }
//#endif

    endVertexArrayRendering(action, useVBO);
  } else {

    // Call the appropriate render loop:
    this.renderFunc[useTexCoordsAnyway | vpCache.getRenderCase(shapeStyle)].run(this,action);

//#ifdef DEBUG
    if (SoDebug.GetEnv("IV_DEBUG_LEGACY_RENDERING") != null) {
      SoDebugError.postInfo("GLRenderInternal", getTypeId().getName().getString()+" Immediate Mode Rendering: "+totalNumVertices+" Polylines");
    }
//#endif
  }
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoLineSet class.
//
// Use: internal

public static void initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SoSubNode.SO__NODE_INIT_CLASS(SoLineSet.class, "LineSet", SoNonIndexedShape.class);
}


//
// translates the current material binding to the internal Binding enum
//
public Binding
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
  case /*SoMaterialBindingElement::*/PER_VERTEX_INDEXED:
    binding = Binding.PER_VERTEX;
    break;
  case /*SoMaterialBindingElement::*/PER_PART:
  case /*SoMaterialBindingElement::*/PER_PART_INDEXED:
    binding = Binding.PER_SEGMENT;
    break;
  case /*SoMaterialBindingElement::*/PER_FACE:
  case /*SoMaterialBindingElement::*/PER_FACE_INDEXED:
    binding = Binding.PER_LINE;
    break;
  default:
    binding = Binding.OVERALL;
//#if COIN_DEBUG
    SoDebugError.postWarning("SoLineSet::findMaterialBinding",
                              "unknown material binding setting");
//#endif // COIN_DEBUG
    break;
  }
  return binding;
}


//
// translates the current normal binding to the internal Binding enum
//
public Binding
findNormalBinding(SoState state)
{
  Binding binding = Binding.PER_VERTEX;

  SoNormalBindingElement.Binding normbind =
    SoNormalBindingElement.get(state);

  switch (normbind) {
  case /*SoMaterialBindingElement::*/OVERALL:
    binding = Binding.OVERALL;
    break;
  case /*SoMaterialBindingElement::*/PER_VERTEX:
  case /*SoMaterialBindingElement::*/PER_VERTEX_INDEXED:
    binding = Binding.PER_VERTEX;
    break;
  case /*SoMaterialBindingElement::*/PER_PART:
  case /*SoMaterialBindingElement::*/PER_PART_INDEXED:
    binding = Binding.PER_SEGMENT;
    break;
  case /*SoMaterialBindingElement::*/PER_FACE:
  case /*SoMaterialBindingElement::*/PER_FACE_INDEXED:
    binding = Binding.PER_LINE;
    break;
  default:
    binding = Binding.PER_VERTEX;
//#if COIN_DEBUG
    SoDebugError.postWarning("SoLineSet::findNormalBinding",
                              "unknown normal binding setting");
//#endif // COIN_DEBUG
    break;
  }
  return binding;
}


	 }
