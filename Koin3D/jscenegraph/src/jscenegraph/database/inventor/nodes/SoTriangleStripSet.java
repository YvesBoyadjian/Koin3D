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
 |      This file defines the SoTriangleStripSet node class.
 |
 |   Author(s)          : Paul S. Strauss
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
import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec4f;
import jscenegraph.database.inventor.SoDebug;
import jscenegraph.database.inventor.SoPrimitiveVertex;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoRayPickAction;
import jscenegraph.database.inventor.bundles.SoNormalBundle;
import jscenegraph.database.inventor.bundles.SoTextureCoordinateBundle;
import jscenegraph.database.inventor.caches.SoNormalCache;
import jscenegraph.database.inventor.details.SoFaceDetail;
import jscenegraph.database.inventor.details.SoPointDetail;
import jscenegraph.database.inventor.elements.SoCoordinateElement;
import jscenegraph.database.inventor.elements.SoGLCacheContextElement;
import jscenegraph.database.inventor.elements.SoGLLazyElement;
import jscenegraph.database.inventor.elements.SoLazyElement;
import jscenegraph.database.inventor.elements.SoMaterialBindingElement;
import jscenegraph.database.inventor.elements.SoNormalBindingElement;
import jscenegraph.database.inventor.elements.SoNormalElement;
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

/**
 * @author Yves Boyadjian
 *
 */

////////////////////////////////////////////////////////////////////////////////
//! Triangle strip set shape node.
/*!
\class SoTriangleStripSet
\ingroup Nodes
This shape node constructs triangle strips out of vertices.
The vertices may be specified in
the \b vertexProperty  field (from SoVertexShape), 
or by the current inherited coordinates.
For optimal performance, the \b vertexProperty  field is recommended.


SoTriangleStripSet is one of the fastest ways of drawing
polygonal objects in Inventor. It uses the current
coordinates, in order, starting with the first one. 
The values in the \b numVertices  field indicate
the number of vertices to use for each triangle strip in the set. The
number of values in this field determines the number of strips.


For example, if 
\b numVertices  has the values [3,5], coordinates 1, 2, and 3 would be
used for the first triangle strip and coordinates 4, 5, 6, 7, and 8 
would be used for the second strip. This would result in 1 triangle in
the first strip and 3 in the second.


The coordinates of the strips are transformed by the current
cumulative transformation. The strips are drawn with the current light
model and drawing style.


Treatment of the current material and normal binding is as follows:
The <tt>PER_PART</tt> binding specifies a material or normal for each strip
of the set. The <tt>PER_FACE</tt> binding specifies a material or normal
for each triangle. The <tt>_INDEXED</tt> bindings are equivalent to
their non-indexed counterparts. The default normal binding is 
<tt>PER_VERTEX</tt>. The default material binding is 
<tt>OVERALL</tt>.


If any normals (or materials) are specified, Inventor assumes 
you provide the correct number of them, as indicated by the binding.
You will see unexpected results
if you specify fewer normals (or materials) than the shape requires.
If no normals are specified, they will be generated automatically.

\par File Format/Default
\par
\code
TriangleStripSet {
  startIndex 0
  numVertices -1
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
SoCoordinate3, SoDrawStyle, SoFaceDetail, SoFaceSet, SoIndexedTriangleStripSet, SoQuadMesh, SoVertexProperty
*/
////////////////////////////////////////////////////////////////////////////////

public class SoTriangleStripSet extends SoNonIndexedShape {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoTriangleStripSet.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoTriangleStripSet.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoTriangleStripSet.class); }    	  
	  
  public
    //! \name Fields
    //@{

    //! Number of vertices in each triangle strip. The number of strips is
    //! equal to the number of values in this field.
    final SoMFInt32   numVertices = new SoMFInt32();    

  //! This stores the total number of vertices; we use this
  //! information to influence Separator's auto-caching algorithm
  //! (shapes with very few triangles should be cached because
  //! traversing them can be expensive, shapes with lots of triangles
  //! shouldn't be cached because they'll take up too much memory).
  private int                         totalNumVertices;

  
  
//Constants for influencing auto-caching algorithm:

//If fewer than this many triangles, AND not using the vertexProperty
//node, auto-cache.
private static final int AUTO_CACHE_TSS_MIN_WITHOUT_VP = 20;

//And the number above which we'll say caches definitely SHOULDN'T be
//built (because they'll use too much memory):
private static final int AUTO_CACHE_TSS_MAX = SoGLCacheContextElement.OIV_AUTO_CACHE_DEFAULT_MAX;

//!Typedef of pointer to method on TriangleStripSet;
//!This will be used to simplify declaration and initialization.
private interface PMTSS  {
	void run(SoTriangleStripSet set, SoGLRenderAction action);
}

//! Array of function pointers to render functions:
	private static PMTSS[] renderFunc = new PMTSS[32];

	static {
    	for(int i = 0 ; i<32;i++) {
    		final int ii = i;
    		renderFunc[i] = (ifs,a) -> SoError.post("SoTriangleStripSet RenderFunc number "+ii+" not yet implemented");
    	}
    	
		renderFunc[0] = (SoTriangleStripSet set, SoGLRenderAction action) -> set.OmOn(action);			
		renderFunc[1] = (SoTriangleStripSet set, SoGLRenderAction action) -> set.OmOnT(action);			
		renderFunc[4] = (SoTriangleStripSet set, SoGLRenderAction action) -> set.OmFn(action);			
		renderFunc[6] = (SoTriangleStripSet set, SoGLRenderAction action) -> set.OmVn(action);			
	}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoTriangleStripSet()
//
////////////////////////////////////////////////////////////////////////
{
  nodeHeader.SO_NODE_CONSTRUCTOR(/*SoTriangleStripSet.class*/);
  nodeHeader.SO_NODE_ADD_MFIELD(numVertices,"numVertices",  (-1));

  totalNumVertices = -1;

  isBuiltIn = true;
}

	  
	  
	
  //! Computes bounding box of triangle strips
	/* (non-Javadoc)
	 * @see jscenegraph.database.inventor.nodes.SoShape#computeBBox(jscenegraph.database.inventor.actions.SoAction, jscenegraph.database.inventor.SbBox3f, jscenegraph.database.inventor.SbVec3f)
	 */
	@Override
	public void computeBBox(SoAction action, SbBox3f box, SbVec3f center) {
  // First, compute the number of vertices the set uses
  int numStrips = (int) numVertices.getNum();
  int numVerts = 0;

  if (numStrips != 0 && numVertices.operator_square_bracketI(numStrips-1) < 0) {
    numVerts = -1;
  } else for (int i = 0; i < numStrips; i++) {
    numVerts += (int) numVertices.operator_square_bracketI(i);
  }
  computeCoordBBox(action, numVerts, box, center);
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

    // If using USE_REST_OF_VERTICES (-1), need to figure out how
    // many vertices there are every time:
    boolean usingUSE_REST = false;
    boolean nvNotifyEnabled = true;
    int nstrips = numVertices.getNum();
    if (nstrips != 0 && numVertices.operator_square_bracketI(nstrips-1) < 0) {
      usingUSE_REST = true;
      nvNotifyEnabled = numVertices.enableNotify(false);
      totalNumVertices = 0;
      for (int i = 0; i < nstrips-1; i++) 
        totalNumVertices += numVertices.operator_square_bracketI(i);
      numVertices.set1Value(nstrips-1, 
        vpCache.numVerts - totalNumVertices - startIndex.getValue());
      totalNumVertices = vpCache.numVerts - startIndex.getValue();
      vpCache.needFromState |=
        SoVertexPropertyCache.BitMask.COORD_FROM_STATE_BIT.getValue();
    } else if (totalNumVertices < 0) {
      totalNumVertices = 0;
      for (int i = 0; i < nstrips; i++) 
        totalNumVertices += numVertices.operator_square_bracketI(i);
    }           

    if (vpCache.shouldGenerateNormals(shapeStyle)) {

      // See if there is a normal cache we can use. If not,
      // generate normals and cache them.
      SoNormalCache normCache = getNormalCache();
      if (normCache == null || ! normCache.isValid(state)) {

        if (totalNumVertices < 0) {
          int nstrips2 = numVertices.getNum();
          totalNumVertices = 0;
          for (int i = 0; i < nstrips2; i++) 
            totalNumVertices += numVertices.operator_square_bracketI(i);
        }           

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
      lazyElt.sendVPPacked(state, (IntBuffer)vpCache.getColors(0).toIntBuffer()/*.asIntBuffer()*/);
    }
    else lazyElt.send(state, SoLazyElement.masks.ALL_MASK.getValue());

//#ifdef DEBUG
    // Check for enough vertices:
    if (vpCache.numVerts < totalNumVertices + startIndex.getValue()){
      SoDebugError.post("SoTriangleStripSet.GLRender",
        "Too few vertices specified;"+
        " need "+(totalNumVertices+startIndex.getValue())+", have "+vpCache.numVerts);
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
      {
        for (int i = 0; i < nstrips; i++) 
          numNormalsNeeded += numVertices.operator_square_bracketI(i)-2;
      }
      break;
    case PER_PART:
    case PER_PART_INDEXED:
      numNormalsNeeded = nstrips;
      break;
    }
    if (vpCache.getNumNormals() < numNormalsNeeded)
      SoDebugError.post("SoTriangleStripSet.GLRender",
      "Too few normals specified;"+
      " need "+numNormalsNeeded+", have "+vpCache.getNumNormals());

    if ((shapeStyle.needTexCoords() || useTexCoordsAnyway != 0) && 
      !vpCache.shouldGenerateTexCoords(shapeStyle)) {

        if (vpCache.getNumTexCoords() < 
          totalNumVertices+startIndex.getValue())
          SoDebugError.post("SoTriangleStripSet.GLRender",
          "Too few texture coordinates specified;"+
          " need "+(totalNumVertices+startIndex.getValue())+", have "+vpCache.getNumTexCoords());
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
      {
        for (int i = 0; i < nstrips; i++) 
          numColorsNeeded += numVertices.operator_square_bracketI(i)-2;
      }
      break;
    case PER_PART:
    case PER_PART_INDEXED:
      numColorsNeeded = nstrips;
      break;
    }
    if (vpCache.getNumColors() < numColorsNeeded)
      SoDebugError.post("SoTriangleStripSet.GLRender",
      "Too few diffuse colors specified;"+
      " need "+numColorsNeeded+", have "+vpCache.getNumColors());
//#endif

    GLRenderInternal(action, useTexCoordsAnyway, shapeStyle);

    // If doing multiple colors, turn off ColorMaterial, invalidate
    // lazy-element diffuse color
    if (vpCache.getNumColors() > 1) {
      SoGLLazyElement.setColorMaterial(state, false);
      ((SoGLLazyElement )SoLazyElement.getInstance(state)).
        reset(state, SoLazyElement.masks.DIFFUSE_MASK.getValue());
    }

    // Restore USE_REST_OF_VERTICES (-1)
    if (usingUSE_REST) {
      numVertices.set1Value(nstrips-1, -1);
      numVertices.enableNotify(nvNotifyEnabled);
    }           

    // Influence auto-caching algorithm:
    if (totalNumVertices < AUTO_CACHE_TSS_MIN_WITHOUT_VP &&
      vpCache.mightNeedSomethingFromState(shapeStyle)) {
        SoGLCacheContextElement.shouldAutoCache(state,
          SoGLCacheContextElement.AutoCache.DO_AUTO_CACHE.getValue());
    } else if (totalNumVertices > AUTO_CACHE_TSS_MAX) {
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

    // If doing multiple colors, turn off ColorMaterial:
    if (vpCache.getNumColors() > 1) {
      SoGLLazyElement.setColorMaterial(state, false);
      ((SoGLLazyElement )SoLazyElement.getInstance(state)).
        reset(state, SoLazyElement.masks.DIFFUSE_MASK.getValue());
    }

    // Influence auto-caching algorithm:
    if (totalNumVertices > AUTO_CACHE_TSS_MAX) {

        SoGLCacheContextElement.shouldAutoCache(state,
          SoGLCacheContextElement.AutoCache.DONT_AUTO_CACHE.getValue());
    }           
  }

  return;
}

	

    //! Generates triangles representing strips
////////////////////////////////////////////////////////////////////////
//
//Description:
//Generates triangles representing a triangle strip set.
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
  final SoTextureCoordinateBundle   tcb = new SoTextureCoordinateBundle(action, false, !forPicking);
  SoCoordinateElement   ce;
  int                         curVert, vert;
  int                         curNormal, curMaterial;
  int                         strip, numStrips;

  // Get bindings:
  SoMaterialBindingElement.Binding mbind =
    SoMaterialBindingElement.get(state);
  SoNormalBindingElement.Binding nbind =
    SoNormalBindingElement.get(state);

  curVert = (int) startIndex.getValue();

  ce = SoCoordinateElement.getInstance(state);

  // Do auto-normal generation, if necessary:
  SoNormalCache normCache = null;
  SoNormalElement ne = SoNormalElement.getInstance(state);
  if (ne.getNum() == 0) {

    // See if there is a normal cache we can use. If not,
    // generate normals and cache them.
    normCache = getNormalCache();
    if (normCache == null || ! normCache.isValid(state)) {

      if (totalNumVertices < 0) {
        int nstrips = numVertices.getNum();
        totalNumVertices = 0;
        for (int i = 0; i < nstrips; i++) 
          totalNumVertices += numVertices.operator_square_bracketI(i);
      }       

      final SoNormalBundle nb = new SoNormalBundle(action, false);
      nb.initGenerator(totalNumVertices);
      generateDefaultNormals(state, nb);
      normCache = getNormalCache();
    }

    nbind = SoNormalBindingElement.Binding.PER_VERTEX;
  }

  curNormal = 0;
  curMaterial = 0;

  if (forPicking) {
    final SbVec4f tc = new SbVec4f(0.0f, 0.0f, 0.0f, 0.0f);
    pvs[0].setTextureCoords(tc);
    pvs[1].setTextureCoords(tc);
    pvs[2].setTextureCoords(tc);
  }

  detail.setNumPoints(3);
  SoPointDetail[] pd = detail.getPoints();

  pvs[0].setDetail(detail);
  pvs[1].setDetail(detail);
  pvs[2].setDetail(detail);

  // Do OVERALL stuff:
  if (mbind == SoMaterialBindingElement.Binding.OVERALL)
    curMaterial = 0;
  if (nbind == SoNormalBindingElement.Binding.OVERALL)
    curNormal = 0;

  numStrips = numVertices.getNum();

  // Handle USE_REST_OF_VERTICES:
  // If using USE_REST_OF_VERTICES (-1), need to figure out how
  // many vertices there are every time:
  boolean usingUSE_REST = false;
  boolean nvNotifyEnabled = true;
  if (numStrips != 0 && numVertices.operator_square_bracketI(numStrips-1) < 0) {
    usingUSE_REST = true;
    nvNotifyEnabled = numVertices.enableNotify(false);
    int nv = 0;
    for (int i = 0; i < numStrips-1; i++) nv += numVertices.operator_square_bracketI(i);
    numVertices.set1Value(numStrips-1, ce.getNum() - nv);
  }       

  for (strip = 0; strip < numStrips; strip++) {
    int vertsInStrip = numVertices.operator_square_bracketI(strip);

    detail.setPartIndex(strip);

    // Do PER_STRIP stuff:
    if (mbind == SoMaterialBindingElement.Binding.PER_PART ||
      mbind == SoMaterialBindingElement.Binding.PER_PART_INDEXED)
      curMaterial = strip;
    if (nbind == SoNormalBindingElement.Binding.PER_PART ||
      nbind == SoNormalBindingElement.Binding.PER_PART_INDEXED)
      curNormal = strip;

    for (vert = 0; vert < vertsInStrip; vert++) {
      // Do PER_VERTEX stuff
      if (mbind == SoMaterialBindingElement.Binding.PER_VERTEX ||
        mbind == SoMaterialBindingElement.Binding.PER_VERTEX_INDEXED)
        curMaterial = curVert;
      if (nbind == SoNormalBindingElement.Binding.PER_VERTEX ||
        nbind == SoNormalBindingElement.Binding.PER_VERTEX_INDEXED)
        curNormal = curVert;

      int thisVert = vert%3;

      pd[thisVert].setMaterialIndex(curMaterial);
      pd[thisVert].setNormalIndex(curNormal);
      pd[thisVert].setTextureCoordIndex(curVert);
      pd[thisVert].setCoordinateIndex(curVert);

      detail.setFaceIndex(vert);

      if (normCache != null)
        pvs[thisVert].setNormal(normCache.getNormals().get(curNormal));
      else
        pvs[thisVert].setNormal(ne.get(curNormal));

      pvs[thisVert].setPoint(ce.get3(curVert));
      pvs[thisVert].setMaterialIndex(curMaterial);
      if (tcb.isFunction()) {
        if (! forPicking)
          pvs[thisVert].setTextureCoords(
          tcb.get(pvs[thisVert].getPoint(),
          pvs[thisVert].getNormal()));
      }
      else
        pvs[thisVert].setTextureCoords(tcb.get(curVert));

      if (vert >= 2) {
        // Must handle per-triangle normals or materials
        // specially:
        if (mbind == SoMaterialBindingElement.Binding.PER_FACE ||
          mbind == SoMaterialBindingElement.Binding.PER_FACE_INDEXED) {
            int v = (vert-1)%3;
            pd[v].setMaterialIndex(curMaterial);
            pvs[v].setMaterialIndex(curMaterial);
            v = (vert-2)%3;
            pd[v].setMaterialIndex(curMaterial);
            pvs[v].setMaterialIndex(curMaterial);
            ++curMaterial;
        }
        if (nbind == SoNormalBindingElement.Binding.PER_FACE ||
          nbind == SoNormalBindingElement.Binding.PER_FACE_INDEXED) {
            int v = (vert-1)%3;
            pd[v].setNormalIndex(curNormal);
            pvs[v].setNormal(ne.get(curNormal));
            v = (vert-2)%3;
            pd[v].setNormalIndex(curNormal);
            pvs[v].setNormal(ne.get(curNormal));
            ++curNormal;
        }

        // Do three vertices of the triangle, being careful to
        // keep them oriented correctly (the orientation switches
        // on every other triangle):
        if ((vert & 1)!=0) {
          invokeTriangleCallbacks(action, pvs[0], pvs[2],
            pvs[1]);
        } else {
          invokeTriangleCallbacks(action, pvs[0], pvs[1],
            pvs[2]);
        }
      }
      curVert++;
    }
  }

  // Restore USE_REST_OF_VERTICES (-1)
  if (usingUSE_REST) {
    numVertices.set1Value(numStrips-1, -1);
    numVertices.enableNotify(nvNotifyEnabled);
  }       

  state.pop();
  
  pvs[0].destructor();
  pvs[1].destructor();
  pvs[2].destructor();
  detail.destructor();
  tcb.destructor();
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


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Generates default normals using the given state and normal
//    bundle. Returns TRUE if normals were generated.
//
// Use: public

public boolean generateDefaultNormals(SoState state, SoNormalBundle nb)
//
////////////////////////////////////////////////////////////////////////
{
  int numStrips = (int) numVertices.getNum();
  int curCoord  = (int) startIndex.getValue();

  int numCoords = 0;
  SoVertexProperty vp = getVertexProperty();
  SoCoordinateElement ce = null;
  SbVec3f[] coords = null;
  if (vp != null && (numCoords = vp.vertex.getNum()) > 0) {
    coords = vp.vertex.getValues(0);
  } else {
    ce = SoCoordinateElement.getInstance(state);
    numCoords = ce.getNum();
  }

  // Now pass the vertices from each strip to the normal bundle
  int strip, numVerts = 0;
  for (strip = 0; strip < numStrips; strip++) {
    // Figure out number of vertices in this strip
    int vertsInStrip = (int) numVertices.operator_square_bracketI(strip);

    // If we got USE_REST_OF_VERTICES, use the rest of the values
    // in the coordinate array:
    if (vertsInStrip < 0) {
      vertsInStrip = numCoords - numVerts;
    }

    numVerts += vertsInStrip;

    for (int vert = 0; vert < vertsInStrip-2; vert++) {
      SbVec3f coord0, coord1, coord2;
      if (coords != null) {
        coord0 = coords[curCoord];
        coord1 = coords[curCoord+1];
        coord2 = coords[curCoord+2];
      } else {
        coord0 = ce.get3(curCoord);
        coord1 = ce.get3(curCoord+1);
        coord2 = ce.get3(curCoord+2);
      }           
      if ((vert & 1)!=0)
        nb.triangle(coord1, coord0, coord2);
      else
        nb.triangle(coord0, coord1, coord2);
      curCoord++;
    }
    curCoord += 2; // Skip last two vertices
  } /* for strip = .. */

  nb.generate(startIndex.getValue());

  // Ok, we now have more normals than we need because we sent
  // most vertices three times.  
  // rearrange things to correspond to TriStrip's idea of
  // per-vertex normals:

  int vertexAtStartOfStrip = 0;
  int numTrisAtStartOfStrip = 0;
  for (strip = 0; strip < numStrips; strip++) {
    SbVec3f n;

    // Figure out number of vertices in this strip
    int vertsInStrip = (int) numVertices.operator_square_bracketI(strip);

    for (int v = 0; v < vertsInStrip; v++) {
      if (v < 2) {
        n = nb.generator.getNormal(numTrisAtStartOfStrip*3+v);
        nb.generator.setNormal(vertexAtStartOfStrip+v, n);
      } else {
        n = nb.generator.getNormal(numTrisAtStartOfStrip*3+
          2+(v-2)*3);
        nb.generator.setNormal(vertexAtStartOfStrip+v, n);
      }
    }
    vertexAtStartOfStrip += vertsInStrip;
    numTrisAtStartOfStrip += vertsInStrip-2;
  }
  nb.generator.setNumNormals(numVerts+startIndex.getValue());

  // Cache the resulting normals
  setNormalCache(state, nb.getNumGeneratedNormals(),
    nb.getGeneratedNormals());

  return true;
}


private void GLRenderInternal( SoGLRenderAction action, int useTexCoordsAnyway, final SoShapeStyleElement shapeStyle )
{
  // Call the appropriate render loop:
  (renderFunc[useTexCoordsAnyway | vpCache.getRenderCase(shapeStyle)]).run(this,action);

//#ifdef DEBUG
  if (SoDebug.GetEnv("IV_DEBUG_LEGACY_RENDERING") != null) {
    SoDebugError.postInfo("GLRenderInternal", getTypeId().getName().getString()+" Immediate Mode Rendering: "+totalNumVertices+" Vertices");
  }
//#endif

}

// Material overall:

private void OmOn(SoGLRenderAction action ) {
	
	GL2 gl2 = Ctx.get(action.getCacheContext());

    // Send one normal, if there are any normals in vpCache:
    if (vpCache.getNumNormals() > 0)
	vpCache.sendNormal(gl2, (FloatBuffer)vpCache.getNormals(0)/*.asFloatBuffer()*/);
    Buffer vertexPtr = vpCache.getVertices(startIndex.getValue());
    final int vertexStride = vpCache.getVertexStride();
    SoVPCacheFunc vertexFunc = vpCache.vertexFunc;
    final int numStrips = numVertices.getNum();
    final int[] numVerts = numVertices.getValuesI(0);

    int v;
	int numVertsIndex = 0;
	int vertexPtrIndex = 0;
    for (int strip = 0; strip < numStrips; strip++) {
	final int nv = (numVerts[numVertsIndex]);
	gl2.glBegin(GL2.GL_TRIANGLE_STRIP);
	for (v = 0; v < nv-1; v+=2) {
		vertexPtr.position(vertexPtrIndex/Float.BYTES);
	    (vertexFunc).run(gl2, vertexPtr);
	    vertexPtrIndex += vertexStride;
		vertexPtr.position(vertexPtrIndex/Float.BYTES);
	    (vertexFunc).run(gl2, vertexPtr);
	    vertexPtrIndex += vertexStride;
	}
	if (v < nv) { // Leftovers
		vertexPtr.position(vertexPtrIndex/Float.BYTES);
	    (vertexFunc).run(gl2, vertexPtr); vertexPtrIndex += vertexStride;
	}
	gl2.glEnd();
	++numVertsIndex;
    }
}


private void OmOnT (SoGLRenderAction action ) {

	GL2 gl2 = Ctx.get(action.getCacheContext());

    // Send one normal, if there are any normals in vpCache:
    if (vpCache.getNumNormals() > 0)
	vpCache.sendNormal(gl2, (FloatBuffer)vpCache.getNormals(0)/*.asFloatBuffer()*/);
    Buffer vertexPtr = vpCache.getVertices(startIndex.getValue());
    final int vertexStride = vpCache.getVertexStride();
    SoVPCacheFunc vertexFunc = vpCache.vertexFunc;
    Buffer texCoordPtr = vpCache.getTexCoords(startIndex.getValue());
    final int texCoordStride = vpCache.getTexCoordStride();
    SoVPCacheFunc texCoordFunc = vpCache.texCoordFunc;
    final int numStrips = numVertices.getNum();
    final int[] numVerts = numVertices.getValuesI(0);

    int v;
	int numVertsIndex = 0;
	int vertexPtrIndex = 0;
	int texCoordPtrIndex = 0;
    for (int strip = 0; strip < numStrips; strip++) {
	final int nv = (numVerts[numVertsIndex]);
	gl2.glBegin(GL2.GL_TRIANGLE_STRIP);
	for (v = 0; v < nv-1; v+=2) {
	    texCoordPtr.position(texCoordPtrIndex/Float.BYTES); 
	    (texCoordFunc).run(gl2, texCoordPtr); texCoordPtrIndex += texCoordStride;
		vertexPtr.position(vertexPtrIndex/Float.BYTES);
	    (vertexFunc).run(gl2, vertexPtr); vertexPtrIndex += vertexStride;
	    texCoordPtr.position(texCoordPtrIndex/Float.BYTES); 
	    (texCoordFunc).run(gl2, texCoordPtr); texCoordPtrIndex += texCoordStride;
		vertexPtr.position(vertexPtrIndex/Float.BYTES);
	    (vertexFunc).run(gl2, vertexPtr); vertexPtrIndex += vertexStride;
	}
	if (v < nv) { // Leftovers
	    texCoordPtr.position(texCoordPtrIndex/Float.BYTES); 
	    (texCoordFunc).run(gl2, texCoordPtr); texCoordPtrIndex += texCoordStride;
		vertexPtr.position(vertexPtrIndex/Float.BYTES);
	    (vertexFunc).run(gl2, vertexPtr); vertexPtrIndex += vertexStride;
	}
	gl2.glEnd();
	++numVertsIndex;
    }
}


public void OmFn (SoGLRenderAction action) {

	GL2 gl2 = Ctx.get(action.getCacheContext());

    Buffer vertexPtr = vpCache.getVertices(startIndex.getValue());
    final int vertexStride = vpCache.getVertexStride();
    SoVPCacheFunc vertexFunc = vpCache.vertexFunc;
    Buffer normalPtr = vpCache.getNormals(0);
    final int normalStride = vpCache.getNormalStride();
    SoVPCacheFunc normalFunc = vpCache.normalFunc;
    gl2.glShadeModel(GL2.GL_FLAT);
    final int numStrips = numVertices.getNum();
    final int[] numVerts = numVertices.getValuesI(0);

    int v;
	int numVertsIndex = 0;
	int vertexPtrIndex = 0;
	int normalPtrIndex = 0;
    for (int strip = 0; strip < numStrips; strip++) {
	final int nv = (numVerts[numVertsIndex]);
	gl2.glBegin(GL2.GL_TRIANGLE_STRIP);
	for (v = 0; v < nv-1; v+=2) {
	    // Per-face cases:
	    if (v != 0) {
			normalPtr.position(normalPtrIndex/Float.BYTES);
			(normalFunc).run(gl2,normalPtr); normalPtrIndex += normalStride;
	    }
		vertexPtr.position(vertexPtrIndex/Float.BYTES);
	    (vertexFunc).run(gl2,vertexPtr/*+0*vertexStride*/);
	    // Per-face cases:
	    if (v != 0) {
			normalPtr.position(normalPtrIndex/Float.BYTES);
			(normalFunc).run(gl2,normalPtr); normalPtrIndex += normalStride;
	    }
		vertexPtr.position((vertexPtrIndex+1*vertexStride)/Float.BYTES);
	    (vertexFunc).run(gl2,vertexPtr);
	    vertexPtrIndex += 2*vertexStride;
	}
	if (v < nv) { // Leftovers
		normalPtr.position(normalPtrIndex/Float.BYTES);
	    normalFunc.run(gl2,normalPtr); normalPtrIndex += normalStride;
		vertexPtr.position(vertexPtrIndex/Float.BYTES);
	    (vertexFunc).run(gl2,vertexPtr); vertexPtrIndex += vertexStride;
	}
	gl2.glEnd();
	++numVertsIndex;
    }
    gl2.glShadeModel(GL2.GL_SMOOTH);
}


public void
OmVn
    (SoGLRenderAction action ) {
	
	GL2 gl2 = Ctx.get(action.getCacheContext());	

    Buffer vertexPtr = vpCache.getVertices(startIndex.getValue());
    final int vertexStride = vpCache.getVertexStride();
    SoVPCacheFunc vertexFunc = vpCache.vertexFunc;
    Buffer normalPtr = vpCache.getNormals(startIndex.getValue());
    final int normalStride = vpCache.getNormalStride();
    SoVPCacheFunc normalFunc = vpCache.normalFunc;
    final int numStrips = numVertices.getNum();
    final int[] numVerts = numVertices.getValuesI(0);

    int v;
    int numVertsIndex = 0; // java port
	int vertexPtrIndex = 0;
	int normalPtrIndex = 0;
    for (int strip = 0; strip < numStrips; strip++) {
	final int nv = numVerts[numVertsIndex];
	gl2.glBegin(GL2.GL_TRIANGLE_STRIP);
	for (v = 0; v < nv-1; v+=2) {
		normalPtr.position(normalPtrIndex/Float.BYTES);
	    (normalFunc).run(gl2,normalPtr/*+0*normalStride*/);
		vertexPtr.position(vertexPtrIndex/Float.BYTES);
	    (vertexFunc).run(gl2,vertexPtr/*+0*vertexStride*/);
		normalPtr.position((normalPtrIndex+normalStride)/Float.BYTES);
	    (normalFunc).run(gl2,normalPtr/*+1*normalStride*/);
	    normalPtrIndex += 2*normalStride;
		vertexPtr.position((vertexPtrIndex+vertexStride)/Float.BYTES);
	    (vertexFunc).run(gl2,vertexPtr/*+1*vertexStride*/);
	    vertexPtrIndex += 2*vertexStride;
	}
	if (v < nv) { // Leftovers
		normalPtr.position(normalPtrIndex/Float.BYTES);
	    (normalFunc).run(gl2,normalPtr); normalPtrIndex += normalStride;
		vertexPtr.position(vertexPtrIndex/Float.BYTES);
	    (vertexFunc).run(gl2,vertexPtr); vertexPtrIndex += vertexStride;
	}
	gl2.glEnd();
	++numVertsIndex;
    }
}



////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoTriangleStripSet class.
//
// Use: internal

public static void initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SO__NODE_INIT_CLASS(SoTriangleStripSet.class, "TriangleStripSet",
                        SoNonIndexedShape.class);

    SO_ENABLE(SoGLRenderAction.class,         SoShapeStyleElement.class);
}

}
