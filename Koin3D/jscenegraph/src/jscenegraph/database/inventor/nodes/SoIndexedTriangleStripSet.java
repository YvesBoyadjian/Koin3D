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
 |      This file defines the SoIndexedTriangleStripSet node class.
 |
 |   Author(s)          : Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.jogamp.opengl.GL2;

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
import jscenegraph.database.inventor.elements.SoShapeStyleElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.errors.SoError;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFInt32;
import jscenegraph.database.inventor.misc.SoNotList;
import jscenegraph.database.inventor.misc.SoNotRec;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoVertexPropertyCache.SoVPCacheFunc;
import jscenegraph.port.Ctx;
import jscenegraph.port.IntArray;
import jscenegraph.port.SbVec3fArray;

/**
 * @author Yves Boyadjian
 *
 */

////////////////////////////////////////////////////////////////////////////////
//! Indexed triangle strip set shape node.
/*!
\class SoIndexedTriangleStripSet
\ingroup Nodes
This shape node constructs triangle strips out of vertices located at
the coordinates specified in the \b vertexProperty  field
(from SoVertexShape), or the
current inherited coordinates. 
For optimal performance, the \b vertexProperty  field is recommended.


SoIndexedTriangleStripSet uses the
indices in the \b coordIndex  field (from SoIndexedShape) to
specify the vertices of the triangle strips. An index of
<tt>SO_END_STRIP_INDEX</tt> (-1) indicates that the current strip has ended
and the next one begins.


The vertices of the faces are transformed by the current
transformation matrix. The faces are drawn with the current light
model and drawing style.


Treatment of the current material and normal binding is as follows:
<tt>PER_PART</tt> specifies a material or normal per strip.  <tt>PER_FACE</tt>
binding specifies a material or normal for each triangle.
<tt>PER_VERTEX</tt> specifies a material or normal for each vertex.  The
corresponding <tt>_INDEXED</tt> bindings are the same, but use the
\b materialIndex  or \b normalIndex  indices (see SoIndexedShape).
The default material binding is <tt>OVERALL</tt>. The
default normal binding is <tt>PER_VERTEX_INDEXED</tt> 


If any normals (or materials) are specified, Inventor assumes 
you provide the correct number of them, as indicated by the binding.
You will see unexpected results
if you specify fewer normals (or materials) than the shape requires.
If no normals are specified, they will be generated automatically.

\par File Format/Default
\par
\code
IndexedTriangleStripSet {
  coordIndex 0
  materialIndex -1
  normalIndex -1
  textureCoordIndex -1
}
\endcode

\par Action Behavior
\par
SoGLRenderAction
<BR> Draws a strip set based on the current coordinates, normals, materials, drawing style, and so on. 
\par
SoRayPickAction
<BR> Picks on the strip set based on the current coordinates and transformation.  Details about the intersection are returned in an SoFaceDetail. 
\par
SoGetBoundingBoxAction
<BR> Computes the bounding box that encloses all vertices of the strip set with the current transformation applied to them. Sets the center to the average of the coordinates of all vertices. 
\par
SoCallbackAction
<BR> If any triangle callbacks are registered with the action, they will be invoked for each successive triangle forming the strips of the set. 

\par See Also
\par
SoIndexedTriangleSet, SoCoordinate3, SoDrawStyle, SoFaceDetail, SoIndexedFaceSet, SoTriangleStripSet, SoVertexProperty
*/
////////////////////////////////////////////////////////////////////////////////

public class SoIndexedTriangleStripSet extends SoIndexedShape {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoIndexedTriangleStripSet.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoIndexedTriangleStripSet.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoIndexedTriangleStripSet.class); }    	  
	        
//! This coordinate index indicates that the current triangle ends and the
//! next triangle begins
	public static final int SO_END_STRIP_INDEX     = (-1);

    //! This enum is used to indicate the current material or normal binding
    public enum Binding {
        OVERALL,
        PER_STRIP,      PER_STRIP_INDEXED,
        PER_TRIANGLE,   PER_TRIANGLE_INDEXED,
        PER_VERTEX,     PER_VERTEX_INDEXED
    };
    
    //! Number of strips, total number of triangles, and number of
    //! vertices per strip:
    private int         numStrips;
    private int         numTriangles;
    private int[]       numVertices;

    //!Typedef of pointer to method on IndexedTriangleStripSet;
    //!This will be used to simplify declaration and initialization.
    private interface PMTSS {
    	public void run(SoIndexedTriangleStripSet set,SoGLRenderAction action);
    }
                                                  
    
    //! Array of function pointers to render functions:
    private static PMTSS[] renderFunc = new PMTSS[32];
    
	static {
    	for(int i = 0 ; i<32;i++) {
    		final int ii = i;
    		renderFunc[i] = (ifs,a) -> SoError.post("SoIndexedTriangleStripSet RenderFunc number "+ii+" not yet implemented");
    	}
    	
		renderFunc[0] = (soIndexedTriangleStripSet, action) ->  soIndexedTriangleStripSet.OmOn(action);
		renderFunc[1] = (soIndexedTriangleStripSet, action) ->  soIndexedTriangleStripSet.OmOnT(action);
		renderFunc[4] = (soIndexedTriangleStripSet, action) ->  soIndexedTriangleStripSet.OmFn(action);
		renderFunc[6] = (soIndexedTriangleStripSet, action) ->  soIndexedTriangleStripSet.OmVn(action);
		renderFunc[7] = (soIndexedTriangleStripSet, action) ->  soIndexedTriangleStripSet.OmVnT(action);
	}
    
// Constants for influencing auto-caching algorithm:
    private final static int AUTO_CACHE_ITSS_MIN_WITHOUT_VP = 20;

// And the number above which we'll say caches definitely SHOULDN'T be
// built (because they'll use too much memory):
    private final static int AUTO_CACHE_ITSS_MAX = SoGLCacheContextElement.OIV_AUTO_CACHE_DEFAULT_MAX;

	        
	  
////////////////////////////////////////////////////////////////////////
//
//Description:
//Generates polys representing a triangleStrip set.
//
//Use: protected

	/* (non-Javadoc)
	 * @see jscenegraph.database.inventor.nodes.SoShape#generatePrimitives(jscenegraph.database.inventor.actions.SoAction)
	 */
	@Override
	protected void generatePrimitives(SoAction action) {

  SoState state = action.getState();
  // Put vertexProperty stuff into state:
  SoVertexProperty vp = getVertexProperty();
  state.push();
  if (vp != null) {
    vp.doAction(action);
  }

  // When generating primitives for picking, delay computing default
  // texture coordinates
  boolean forPicking = action.isOfType(SoRayPickAction.getClassTypeId());

  final SoPrimitiveVertex[]           pvs = new SoPrimitiveVertex[3];
  pvs[0] = new SoPrimitiveVertex();
  pvs[1] = new SoPrimitiveVertex();
  pvs[2] = new SoPrimitiveVertex();
  final SoFaceDetail                detail = new SoFaceDetail();
  final SoNormalBundle              nb = new SoNormalBundle(action, false);
  final SoTextureCoordinateBundle   tcb = new SoTextureCoordinateBundle(action, false, ! forPicking);
  SoCoordinateElement   ce; // ptr
  int                         curIndex, curStrip = 0;
  int                         curTri = 0;
  int                         curMaterial = -1, curNormal = -1;
  int                         curTexCoord = -1;
  int                         numIndices;
  int[]                       coordIndices, matlIndices;
  int[]                       normIndices, texCoordIndices;
  Binding                     materialBinding, normalBinding;
  boolean                        texCoordsIndexed;

  ce = SoCoordinateElement.getInstance(action.getState());

  materialBinding  = getMaterialBinding(action);
  texCoordsIndexed = areTexCoordsIndexed(action);
  normalBinding    = getNormalBinding(action, nb);

  curIndex = 0;

  numIndices      = coordIndex.getNum();
  coordIndices    = coordIndex.getValuesI(0);
  matlIndices     = materialIndex.getValuesI(0);
  normIndices     = normalIndex.getValuesI(0);
  texCoordIndices = textureCoordIndex.getValuesI(0);

  // Check for special case of 1 index of SO_END_TRIANGLESTRIP_INDEX. This
  // means that coord indices are to be used for materials, normals,
  // or texture coords as well
  if (materialIndex.getNum() == 1 && matlIndices[0] == SO_END_STRIP_INDEX)
    matlIndices = coordIndices;
  if (normalIndex.getNum() == 1 && normIndices[0] == SO_END_STRIP_INDEX) 
    normIndices = coordIndices;
  if (textureCoordIndex.getNum() == 1 &&
    texCoordIndices[0] == SO_END_STRIP_INDEX)
    texCoordIndices = coordIndices;

  if (forPicking) {
    final SbVec4f tc = new SbVec4f(0.0f, 0.0f, 0.0f, 0.0f);
    pvs[0].setTextureCoords(tc);
    pvs[1].setTextureCoords(tc);
    pvs[2].setTextureCoords(tc);
  }

  detail.setNumPoints(3);
  SoPointDetail[] pd = detail.getPoints(); // ptr

  pvs[0].setDetail(detail);
  pvs[1].setDetail(detail);
  pvs[2].setDetail(detail);

  // Step through all the coordinate indices, building triangleStrips out
  // of them, until we run out of coordinate indices.
  while (curIndex < numIndices) {

    // Figure out how many vertices are in this strip:
    int vertsInStrip;
    for (vertsInStrip = 0; vertsInStrip+curIndex <
      numIndices && coordIndex.operator_square_bracketI(vertsInStrip+curIndex) !=
      SO_END_STRIP_INDEX; vertsInStrip++);

    // Check to see whether to skip this strip due to 
    //  too few polygons:
    if (vertsInStrip < 3 ) {

      curIndex += vertsInStrip+1;
      curStrip++;
      if (materialBinding == Binding.PER_VERTEX)
        curMaterial += vertsInStrip;
      if (normalBinding == Binding.PER_VERTEX)
        curNormal += vertsInStrip;
      if (vertsInStrip > 2)
        curTri += vertsInStrip-2;
      continue;
    }

    detail.setPartIndex(curStrip);

    // Loop through all vertices of current triangleStrip
    for (int j = 0; j < vertsInStrip; j++) {

      switch (materialBinding) {
        case OVERALL:
          curMaterial = 0;
          break;
        case PER_STRIP:
          curMaterial = curStrip;
          break;
        case PER_STRIP_INDEXED:
          curMaterial = (int) matlIndices[curStrip];
          break;
        case PER_TRIANGLE:
          curMaterial = (int) curTri;
          break;
        case PER_TRIANGLE_INDEXED:
          curMaterial = (int) matlIndices[curTri];
          break;
        case PER_VERTEX:
          curMaterial++;
          break;
        case PER_VERTEX_INDEXED:
          curMaterial = (int) matlIndices[curIndex];
          break;
      }

      switch (normalBinding) {
        case OVERALL:
          curNormal = 0;
          break;
        case PER_STRIP:
          curNormal = curStrip;
          break;
        case PER_STRIP_INDEXED:
          curNormal = (int) normIndices[curStrip];
          break;
        case PER_TRIANGLE:
          curNormal = curTri;
          break;
        case PER_TRIANGLE_INDEXED:
          curNormal = (int) normIndices[curTri];
          break;
        case PER_VERTEX:
          curNormal++;
          break;
        case PER_VERTEX_INDEXED:
          curNormal = (int) normIndices[curIndex];
          break;
      }

      curTexCoord = (texCoordsIndexed ?
        (int) texCoordIndices[curIndex] : curIndex);

      int thisVert = j%3;

      detail.setFaceIndex(curTri);

      // Set up a point detail for the current vertex
      pd[thisVert].setCoordinateIndex((int) coordIndices[curIndex]);
      pd[thisVert].setMaterialIndex(curMaterial);
      pd[thisVert].setNormalIndex(curNormal);
      pd[thisVert].setTextureCoordIndex(curTexCoord);

      detail.setPoint(thisVert, pd[thisVert]);

      pvs[thisVert].setPoint(ce.get3((int) coordIndices[curIndex]));
      pvs[thisVert].setNormal(nb.get(curNormal));
      pvs[thisVert].setMaterialIndex(curMaterial);
      if (tcb.isFunction()) {
        if (! forPicking)
          pvs[thisVert].setTextureCoords(
          tcb.get(pvs[thisVert].getPoint(),
          pvs[thisVert].getNormal()));
      }
      else
        pvs[thisVert].setTextureCoords(tcb.get(curTexCoord));

      if (j >= 2) {
        // Must handle per-triangle normals or materials
        // specially:
        if (materialBinding == Binding.PER_TRIANGLE ||
          materialBinding == Binding.PER_TRIANGLE_INDEXED) {
            int v = (j-1)%3;
            pd[v].setMaterialIndex(curMaterial);
            pvs[v].setMaterialIndex(curMaterial);
            v = (j-2)%3;
            pd[v].setMaterialIndex(curMaterial);
            pvs[v].setMaterialIndex(curMaterial);
        }
        if (normalBinding == Binding.PER_TRIANGLE ||
          normalBinding == Binding.PER_TRIANGLE_INDEXED) {
            int v = (j-1)%3;
            pd[v].setNormalIndex(curNormal);
            pvs[v].setNormal(nb.get(curNormal));
            v = (j-2)%3;
            pd[v].setNormalIndex(curNormal);
            pvs[v].setNormal(nb.get(curNormal));
        }

        // Do three vertices of the triangle, being careful to
        // keep them oriented correctly (the orientation switches
        // on every other triangle):
        if ((j & 1)!=0) {
          invokeTriangleCallbacks(action, pvs[0], pvs[2],
            pvs[1]);
        } else {
          invokeTriangleCallbacks(action, pvs[0], pvs[1],
            pvs[2]);
        }
        curTri++;
      }

      //
      // Increment per-vertex stuff
      //
      curIndex++;
    }

    //
    // Increment per-strip stuff
    //
    curIndex++;     // Skip over the END_STRIP_INDEX
    curStrip++;
  }
  state.pop();
  
  pvs[0].destructor();
  pvs[1].destructor();
  pvs[2].destructor();
  detail.destructor();
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
createTriangleDetail(SoRayPickAction action,
                                                SoPrimitiveVertex v1,
                                                SoPrimitiveVertex v2,
                                                SoPrimitiveVertex v3,
                                                SoPickedPoint pp)
                                                //
                                                ////////////////////////////////////////////////////////////////////////
{
  SoFaceDetail        detail = new SoFaceDetail(); //ptr
  SoFaceDetail  d; // ptr

  detail.setNumPoints(3);

  d = ( SoFaceDetail ) v1.getDetail();

  // Copy the three point details
  detail.setPoint(0, d.getPoint(0));
  detail.setPoint(1, d.getPoint(1));
  detail.setPoint(2, d.getPoint(2));

  // The face/part indices are in the incoming details
  detail.setFaceIndex(d.getFaceIndex());
  detail.setPartIndex(d.getPartIndex());

  // Compute texture coordinates at intersection point and store it
  // in the picked point
  final SoTextureCoordinateBundle   tcb = new SoTextureCoordinateBundle(action, false);
  if (tcb.isFunction())
    pp.setObjectTextureCoords(tcb.get(pp.getObjectPoint(),
    pp.getObjectNormal()));

  tcb.destructor();
  return detail;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns current material binding from action's state.
//
// Use: private, static

Binding getMaterialBinding(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
  switch (SoMaterialBindingElement.get(action.getState())) {
      case OVERALL:
        return Binding.OVERALL;
      case PER_PART:
        return Binding.PER_STRIP;
      case PER_PART_INDEXED:
        return Binding.PER_STRIP_INDEXED;
      case PER_FACE:
        return Binding.PER_TRIANGLE;
      case PER_FACE_INDEXED:
        return Binding.PER_TRIANGLE_INDEXED;
      case PER_VERTEX:
        return Binding.PER_VERTEX;
      case PER_VERTEX_INDEXED:
        return Binding.PER_VERTEX_INDEXED;
  }
  return Binding.OVERALL; // Shut up C++ compiler
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns current normal binding from action's state.
//
// Use: private, static

Binding getNormalBinding(SoAction action,
                                            final SoNormalBundle nb)
                                            //
                                            ////////////////////////////////////////////////////////////////////////
{
  if (figureNormals(action.getState(), nb))
    return Binding.PER_VERTEX;

  switch (SoNormalBindingElement.get(action.getState())) {
      case OVERALL:
        return Binding.OVERALL;

      case PER_PART:
        return Binding.PER_STRIP;
      case PER_PART_INDEXED:
        return Binding.PER_STRIP_INDEXED;
      case PER_FACE:
        return Binding.PER_TRIANGLE;
      case PER_FACE_INDEXED:
        return Binding.PER_TRIANGLE_INDEXED;
      case PER_VERTEX:
        return Binding.PER_VERTEX;
      case PER_VERTEX_INDEXED:
        return Binding.PER_VERTEX_INDEXED;
  }
  return Binding.OVERALL; // Shut up C++ compiler
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Figures out normals, if necessary.  Returns TRUE if normals were
//    figured out (and the normal binding should be PER_VERTEX).
//
// Use: private

private boolean figureNormals(SoState state, SoNormalBundle nb)
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

  int                 numNeeded = 0, i, numTris = 0, numV = 0;
  SoMFInt32     nIndices;

  if (normalIndex.getNum() == 1 && normalIndex.operator_square_bracketI(0) == SO_END_STRIP_INDEX)
    nIndices = coordIndex;
  else
    nIndices = normalIndex;

  // Find greatest index:
  for (i = 0; i < nIndices.getNum(); i++) {
    if ((nIndices).operator_square_bracketI(i) > numNeeded)
      numNeeded = (int) (nIndices).operator_square_bracket(i);

    // Count number of triangles, too.  numV counts how many
    // vertices we've got since the beginning of the strip:
    if ((nIndices).operator_square_bracketI(i) >= 0) {
      ++numV;
      if (numV >= 3) ++numTris;
    }
    else numV = 0;
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
//    Constructor
//
// Use: public

public SoIndexedTriangleStripSet()
//
////////////////////////////////////////////////////////////////////////
{
  nodeHeader.SO_NODE_CONSTRUCTOR(/*SoIndexedTriangleStripSet.class*/);

  numStrips = numTriangles = -1;
  numVertices = null;

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
  if (numVertices != null) {
	  numVertices = null;
  }
  super.destructor();
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Generates default normals using the given state and normal
//    bundle. Returns TRUE if normals were generated.
//
// Use: extender, virtual

public boolean generateDefaultNormals(SoState state,
                                                  SoNormalBundle nb)
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

    // Reset vars for first strip:
    final SbVec3f[] verts = new SbVec3f[3];
    verts[0] = new SbVec3f();
    verts[1] = new SbVec3f();
    verts[2] = new SbVec3f();
    int whichVert = 0;
    int numInStrip = 0;

    // Loop through all vertices of current triangleStrip
    while (curIndex < numIndices &&
      coordIndex.operator_square_bracketI(curIndex) != SO_END_STRIP_INDEX) {

        if (ce != null)
          verts[whichVert%3] = ce.get3((int)coordIndex.operator_square_bracketI(curIndex));
        else
          verts[whichVert%3] = vpCoords.get(coordIndex.operator_square_bracketI(curIndex));

        ++numInStrip;

        if (numInStrip >= 3) {

          // Spit out a triangle: NOTE: the code below
          // assumes that the third vertex in each triangle
          // sent to the normal generator is the newest
          // vertex.
          nb.beginPolygon();
          if ((numInStrip % 2) != 0) {
            nb.polygonVertex(verts[(whichVert-2)%3]);
            nb.polygonVertex(verts[(whichVert-1)%3]);
            nb.polygonVertex(verts[whichVert%3]);
          }
          else {
            nb.polygonVertex(verts[(whichVert-1)%3]);
            nb.polygonVertex(verts[(whichVert-2)%3]);
            nb.polygonVertex(verts[whichVert%3]);
          }                       
          nb.endPolygon();
        }                   
        ++whichVert;
        curIndex++;
    }
    curIndex++;             // Skip past END_STRIP_INDEX
  }
  nb.generate();


  // Ok, we now have more normals than we need because we sent
  // most vertices three times.  So, we'll go through and
  // rearrange things to correspond to IndexedTriStrip's idea of
  // per-vertex normals:
  curIndex = 0;
  int triIndex = 0, curVert = 0;
  while (curIndex < numIndices) {

    SbVec3f n;

    // Figure out how many vertices are in this strip:
    int vertsInStrip;
    for (vertsInStrip = 0;
      vertsInStrip + curIndex < numIndices &&
      coordIndex.operator_square_bracketI(vertsInStrip + curIndex) != SO_END_STRIP_INDEX;
    vertsInStrip++)
      ;

    if (vertsInStrip > 2) {
      // The first three vertices are correct, but then we
      // only need to take one vertex per triangle after the
      // first three:
      int j;
      for (j = 0; j < 3; j++) {
        n = nb.generator.getNormal(triIndex*3+j);
        nb.generator.setNormal(curVert++, n);
      }
      triIndex++;
      for (j = 3; j < vertsInStrip; j++, triIndex++) {
        n = nb.generator.getNormal(triIndex*3+2);
        nb.generator.setNormal(curVert++, n);
      }
    }
    curIndex += vertsInStrip + 1; // Go to next strip
  }

  // Cache the resulting normals
  setNormalCache(state, nb.getNumGeneratedNormals(),
    nb.getGeneratedNormals());

  return true;
}



////////////////////////////////////////////////////////////////////////
//
// Description:
//    Implements GL rendering.
//
// Use: extender

public void GLRender(SoGLRenderAction action)
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

    // Setup numVertices, numStrips and numTriangles:
    if (numStrips < 0)
      countStripsAndTris();

    if (vpCache.shouldGenerateNormals(shapeStyle)) {

      // See if there is a normal cache we can use. If not,
      // generate normals and cache them.
      SoNormalCache normCache = getNormalCache();
      if (normCache == null || ! normCache.isValid(state)) {

        int numVerts = 0;
        for (int i = 0; i < numStrips; i++)
          numVerts += numVertices[i];

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
    setupIndices(numStrips, numTriangles, shapeStyle.needNormals(), 
      (shapeStyle.needTexCoords() || useTexCoordsAnyway != 0));

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
      lazyElt.sendVPPacked(state, (IntBuffer)vpCache.getColors(0).toIntBuffer()/*.asIntBuffer()*/);
    }
    else lazyElt.send(state, SoLazyElement.masks.ALL_MASK.getValue());

    GLRenderInternal(action, useTexCoordsAnyway, shapeStyle);

    // If doing multiple colors, turn off ColorMaterial:
    if (vpCache.getNumColors() > 1) {
      SoGLLazyElement.setColorMaterial(state, false);
      ((SoGLLazyElement )SoLazyElement.getInstance(state)).
        reset(state, SoLazyElement.masks.DIFFUSE_MASK.getValue());
    }


    // Influence auto-caching algorithm:
    if (coordIndex.getNum() < AUTO_CACHE_ITSS_MIN_WITHOUT_VP &&
      vpCache.mightNeedSomethingFromState(shapeStyle)) {
        SoGLCacheContextElement.shouldAutoCache(state,
          SoGLCacheContextElement.AutoCache.DO_AUTO_CACHE.getValue());
    } else if (coordIndex.getNum() > AUTO_CACHE_ITSS_MAX) {
        SoGLCacheContextElement.shouldAutoCache(state,
          SoGLCacheContextElement.AutoCache.DONT_AUTO_CACHE.getValue());
    }           

    if (tcb != null) {
      tcb.destructor();
      state.pop();
    }
    else if (useTexCoordsAnyway != 0) 
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
      lazyElt.sendVPPacked(state, (IntBuffer)vpCache.getColors(0).toIntBuffer()/*.asIntBuffer()*/);
    }
    else lazyElt.send(state, SoLazyElement.masks.ALL_MASK.getValue());

    GLRenderInternal(action, 0, shapeStyle);

    if (vpCache.getNumColors() > 1) {
      SoGLLazyElement.setColorMaterial(state, false);
      ((SoGLLazyElement )SoLazyElement.getInstance(state)).
        reset(state, SoLazyElement.masks.DIFFUSE_MASK.getValue());
    }

    // Influence auto-caching algorithm:
    if (coordIndex.getNum() > AUTO_CACHE_ITSS_MAX) {

        SoGLCacheContextElement.shouldAutoCache(state,
          SoGLCacheContextElement.AutoCache.DONT_AUTO_CACHE.getValue());
    }           
  }
  return;
}

	
///////////////////////////////////////////////////////////////////////////
//
// Description:
//      Count vertices in each strip, construct numVertices array.
//      must be invoked whenever is built, before
//      normal generation.
//
// use: private
//
///////////////////////////////////////////////////////////////////////////
private void  countStripsAndTris()
{
  if (numStrips > 0) return; // Already counted
  numStrips = 0;
  int i, numVerts = 0;
  for(i = 0; i < coordIndex.getNum(); i++){
    if (coordIndex.operator_square_bracketI(i) == SO_END_STRIP_INDEX || 
      (i == coordIndex.getNum()-1)) {
        ++numStrips;
    } 
    if (coordIndex.operator_square_bracketI(i) != SO_END_STRIP_INDEX) {
      ++numVerts;
    }
  }
  numTriangles = numVerts - 2*numStrips;

  numVertices = new int[numStrips];
  // Then fill in its values:
  int ns = 0;
  int nv = 0;
  for(i = 0; i< coordIndex.getNum(); i++){
    if (coordIndex.operator_square_bracketI(i) == SO_END_STRIP_INDEX ){
      numVertices[ns] = nv;
      nv=0;
      ns++;               
    }
    else {
      nv++;
      if (i == coordIndex.getNum()-1){
        numVertices[ns] = nv;
      }
    }       
  }
}    

	
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Keep things up to date when my fields change
//
// Use: protected

public void notify(SoNotList list)
//
////////////////////////////////////////////////////////////////////////
{
  // If coordIndex changes, must recount:
  if (list.getLastRec().getType() == SoNotRec.Type.CONTAINER &&
    list.getLastField() == coordIndex) {
//      if (numVertices)
//        delete[] numVertices; not needed in java
      numVertices = null;
      numStrips = numTriangles = -1;
  }

  super.notify(list);
}    

private void GLRenderInternal( SoGLRenderAction  action, int useTexCoordsAnyway, SoShapeStyleElement shapeStyle )
{
  // Call the appropriate render loop:
  (renderFunc[useTexCoordsAnyway | 
    vpCache.getRenderCase(shapeStyle)]).run(this, action);

//#ifdef DEBUG
  if (SoDebug.GetEnv("IV_DEBUG_LEGACY_RENDERING") != null) {
    SoDebugError.postInfo("GLRenderInternal", getTypeId().getName().getString()+" Immediate Mode Rendering: "+numTriangles+" Triangles");
  }
//#endif
}



//////////////////////////////////////////////////////////////////////////
// Following preprocessor-generated routines handle all combinations of
// Normal binding (per vertex, per face, per part, overall/none)
// Color Binding (per vertex, per face, per part, overall)
// Textures (on or off)
//////////////////////////////////////////////////////////////////////////

// Material overall:

private void OmOn (SoGLRenderAction action ) {
	
	GL2 gl2 = Ctx.get(action.getCacheContext());
	
    final int ns = numStrips;
    final int[] numverts = numVertices;
    final int[] vertexIndex = coordIndex.getValuesI(0);
    // Send one normal, if there are any normals in vpCache:
    if (vpCache.getNumNormals() > 0)
	vpCache.sendNormal(gl2, (FloatBuffer)vpCache.getNormals(0)/*.asFloatBuffer()*/);
    Buffer vertexPtr = vpCache.getVertices(0);
    final int vertexStride = vpCache.getVertexStride();
    SoVPCacheFunc vertexFunc = vpCache.vertexFunc;
    int v;
    int vtxCtr = 0;
    int numvertsIndex = 0;
    for (int strip = 0; strip < ns; strip++) {
	final int nv = (numverts[numvertsIndex]);
	gl2.glBegin(GL2.GL_TRIANGLE_STRIP);
	for (v = 0; v < nv-1; v+=2) {
		vertexPtr.position(vertexStride*vertexIndex[vtxCtr]/Float.BYTES);vtxCtr++;
	    (vertexFunc).run(gl2, vertexPtr);

		vertexPtr.position(vertexStride*vertexIndex[vtxCtr]/Float.BYTES);vtxCtr++;
	    (vertexFunc).run(gl2, vertexPtr);           
	}
	if (v < nv) { // Leftovers
		vertexPtr.position(vertexStride*vertexIndex[vtxCtr]/Float.BYTES);vtxCtr++;
	    (vertexFunc).run(gl2, vertexPtr);         
	}
	gl2.glEnd();
	vtxCtr++;
	numvertsIndex++;
    }
}


private void OmOnT (SoGLRenderAction action ) {
	
	GL2 gl2 = Ctx.get(action.getCacheContext());
	
    final int ns = numStrips;
    final int[] numverts = numVertices;
    final int[] vertexIndex = coordIndex.getValuesI(0);
    // Send one normal, if there are any normals in vpCache:
    if (vpCache.getNumNormals() > 0)
	vpCache.sendNormal(gl2, (FloatBuffer)vpCache.getNormals(0)/*.asFloatBuffer()*/);
    Buffer vertexPtr = vpCache.getVertices(0);
    final int vertexStride = vpCache.getVertexStride();
    SoVPCacheFunc vertexFunc = vpCache.vertexFunc;
    Buffer texCoordPtr = vpCache.getTexCoords(0);
    final int texCoordStride = vpCache.getTexCoordStride();
    SoVPCacheFunc texCoordFunc = vpCache.texCoordFunc;
    final IntArray tCoordIndx = getTexCoordIndices();
    int v;
    int vtxCtr = 0;
    int numvertsIndex = 0;
    for (int strip = 0; strip < ns; strip++) {
	final int nv = (numverts[numvertsIndex]);
	gl2.glBegin(GL2.GL_TRIANGLE_STRIP);
	for (v = 0; v < nv-1; v+=2) {
		texCoordPtr.position(texCoordStride*tCoordIndx.get(vtxCtr)/Float.BYTES);
		vertexPtr.position(vertexStride*vertexIndex[vtxCtr]/Float.BYTES); vtxCtr++;
	    (texCoordFunc).run(gl2, texCoordPtr);
	    (vertexFunc).run(gl2, vertexPtr);         

		texCoordPtr.position(texCoordStride*tCoordIndx.get(vtxCtr)/Float.BYTES);
		vertexPtr.position(vertexStride*vertexIndex[vtxCtr]/Float.BYTES); vtxCtr++;           
	    (texCoordFunc).run(gl2, texCoordPtr);
	    (vertexFunc).run(gl2, vertexPtr);         
	}
	if (v < nv) { // Leftovers
		texCoordPtr.position(texCoordStride*tCoordIndx.get(vtxCtr)/Float.BYTES);
		vertexPtr.position(vertexStride*vertexIndex[vtxCtr]/Float.BYTES); vtxCtr++;
	    (texCoordFunc).run(gl2, texCoordPtr);
	    (vertexFunc).run(gl2, vertexPtr);         
	}
	gl2.glEnd();
	vtxCtr++;
	numvertsIndex++;
    }
}


private void OmFn (SoGLRenderAction action) {
	
	GL2 gl2 = Ctx.get(action.getCacheContext());
	
    final int ns = numStrips;
    final int[] numverts = numVertices;
    final int[] vertexIndex = coordIndex.getValuesI(0);
    Buffer vertexPtr = vpCache.getVertices(0);
    final int vertexStride = vpCache.getVertexStride();
    SoVPCacheFunc vertexFunc = vpCache.vertexFunc;
    Buffer normalPtr = vpCache.getNormals(0);
    final int normalStride = vpCache.getNormalStride();
    SoVPCacheFunc normalFunc = vpCache.normalFunc;
    IntArray normalIndx = getNormalIndices();
    int nrmCtr=0;
    gl2.glShadeModel(GL2.GL_FLAT);
    int v;
    int vtxCtr = 0;
    int numvertsIndex = 0; // java port
    for (int strip = 0; strip < ns; strip++) {
	final int nv = (numverts[numvertsIndex]);
	gl2.glBegin(GL2.GL_TRIANGLE_STRIP);
	for (v = 0; v < nv-1; v+=2) {
	    // Per-face cases:
	    if (v != 0) {
	    	normalPtr.position(normalStride*normalIndx.get(nrmCtr)/Float.BYTES);nrmCtr++;             
		    (normalFunc).run(gl2, normalPtr);
	    }
	    vertexPtr.position(vertexStride*vertexIndex[vtxCtr]/Float.BYTES);vtxCtr++;
	    (vertexFunc).run(gl2, vertexPtr);

	    // Per-face cases:
	    if (v != 0) {
	    	normalPtr.position(normalStride*normalIndx.get(nrmCtr)/Float.BYTES);nrmCtr++;             
		    (normalFunc).run(gl2, normalPtr);
	    }
	    vertexPtr.position(vertexStride*vertexIndex[vtxCtr]/Float.BYTES);vtxCtr++ ;          
	    (vertexFunc).run(gl2, vertexPtr);
	}
	if (v < nv) { // Leftovers
		normalPtr.position(normalStride*normalIndx.get(nrmCtr)/Float.BYTES);nrmCtr++;
	    (normalFunc).run(gl2, normalPtr);
	    vertexPtr.position(vertexStride*vertexIndex[vtxCtr]/Float.BYTES);vtxCtr++;       
	    (vertexFunc).run(gl2, vertexPtr);
	}
	gl2.glEnd();
	vtxCtr++;
	++numvertsIndex;
    }
    gl2.glShadeModel(GL2.GL_SMOOTH);
}


public void OmVn (SoGLRenderAction action ) {
	
	GL2 gl2 = Ctx.get(action.getCacheContext());
	
    final int ns = numStrips;
    final int[] numverts = numVertices;
    final int[] vertexIndex = coordIndex.getValuesI(0);
    Buffer vertexPtr = vpCache.getVertices(0);
    final int vertexStride = vpCache.getVertexStride();
    SoVPCacheFunc vertexFunc = vpCache.vertexFunc;
    Buffer normalPtr = vpCache.getNormals(0);
    final int normalStride = vpCache.getNormalStride();
    SoVPCacheFunc normalFunc = vpCache.normalFunc;
    IntArray normalIndx = getNormalIndices();
    int v;
    int vtxCtr = 0;
    int numvertsIndex = 0; // java port
    for (int strip = 0; strip < ns; strip++) {
	final int nv = (numverts)[numvertsIndex];
	gl2.glBegin(GL2.GL_TRIANGLE_STRIP);
	for (v = 0; v < nv-1; v+=2) {
		normalPtr.position(normalStride*normalIndx.get(vtxCtr)/Float.BYTES);
	    (normalFunc).run(gl2, normalPtr);
	    vertexPtr.position(vertexStride*vertexIndex[vtxCtr++]/Float.BYTES);
	    (vertexFunc).run(gl2, vertexPtr);

	    normalPtr.position(normalStride*normalIndx.get(vtxCtr)/Float.BYTES);       
	    (normalFunc).run(gl2, normalPtr);
	    vertexPtr.position(vertexStride*vertexIndex[vtxCtr++]/Float.BYTES);           
	    (vertexFunc).run(gl2, vertexPtr);
	}
	if (v < nv) { // Leftovers
		normalPtr.position(normalStride*normalIndx.get(vtxCtr)/Float.BYTES);
	    (normalFunc).run(gl2, normalPtr);
	    vertexPtr.position(vertexStride*vertexIndex[vtxCtr++]/Float.BYTES);         
	    (vertexFunc).run(gl2, vertexPtr);
	}
	gl2.glEnd();
	vtxCtr++;
	++numvertsIndex;
    }
}


public void OmVnT
    (SoGLRenderAction action ) {
	
	GL2 gl2 = Ctx.get(action.getCacheContext());
	
    final int ns = numStrips;
    final int[] numverts = numVertices;
    final int[] vertexIndex = coordIndex.getValuesI(0);
    Buffer vertexPtr = vpCache.getVertices(0);
    final int vertexStride = vpCache.getVertexStride();
    SoVPCacheFunc vertexFunc = vpCache.vertexFunc;
    Buffer normalPtr = vpCache.getNormals(0);
    final int normalStride = vpCache.getNormalStride();
    SoVPCacheFunc normalFunc = vpCache.normalFunc;
    IntArray normalIndx = getNormalIndices();
    Buffer texCoordPtr = vpCache.getTexCoords(0);
    final int texCoordStride = vpCache.getTexCoordStride();
    SoVPCacheFunc texCoordFunc = vpCache.texCoordFunc;
    IntArray tCoordIndx = getTexCoordIndices();
    int v;
    int vtxCtr = 0;
    int numvertsIndex = 0; // java port    
    for (int strip = 0; strip < ns; strip++) {
	final int nv = (numverts)[numvertsIndex];
	gl2.glBegin(GL2.GL_TRIANGLE_STRIP);
	for (v = 0; v < nv-1; v+=2) {
	    (normalFunc).run(gl2, normalPtr.position(normalStride*normalIndx.get(vtxCtr)/Float.BYTES));
	    (texCoordFunc).run(gl2,texCoordPtr.position(texCoordStride*tCoordIndx.get(vtxCtr)/Float.BYTES));
	    (vertexFunc).run(gl2,vertexPtr.position(vertexStride*vertexIndex[vtxCtr++]/Float.BYTES));

	    (normalFunc).run(gl2,normalPtr.position(normalStride*normalIndx.get(vtxCtr)/Float.BYTES));       
	    (texCoordFunc).run(gl2,texCoordPtr.position(texCoordStride*tCoordIndx.get(vtxCtr)/Float.BYTES));        
	    (vertexFunc).run(gl2,vertexPtr.position(vertexStride*vertexIndex[vtxCtr++]/Float.BYTES));           
	}
	if (v < nv) { // Leftovers
	    (normalFunc).run(gl2,normalPtr.position(normalStride*normalIndx.get(vtxCtr)/Float.BYTES));
	    (texCoordFunc).run(gl2,texCoordPtr.position(texCoordStride*tCoordIndx.get(vtxCtr)/Float.BYTES));         
	    (vertexFunc).run(gl2,vertexPtr.position(vertexStride*vertexIndex[vtxCtr++]/Float.BYTES));         
	}
	gl2.glEnd();
	vtxCtr++;
	++numvertsIndex;
    }
}



	
////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoIndexedTriangleStripSet class.
//
// Use: internal

public static void initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SO__NODE_INIT_CLASS(SoIndexedTriangleStripSet.class,
                        "IndexedTriangleStripSet", SoIndexedShape.class);
}

	
}
