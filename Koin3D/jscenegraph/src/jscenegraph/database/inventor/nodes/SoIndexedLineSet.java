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
 |      This file defines the SoIndexedLineSet node class.
 |
 |   Author(s)          : Paul S. Strauss, Nick Thompson
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import java.nio.Buffer;
import java.nio.IntBuffer;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL3;

import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec4f;
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
import jscenegraph.database.inventor.elements.SoGLTextureCoordinateElement;
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
import jscenegraph.database.inventor.nodes.SoVertexPropertyCache.SoVPCacheFunc;
import jscenegraph.mevis.inventor.elements.SoGLVBOElement;
import jscenegraph.mevis.inventor.misc.SoVBO;
import jscenegraph.mevis.inventor.misc.SoVertexArrayIndexer;


////////////////////////////////////////////////////////////////////////////////
//! Indexed polyline shape node.
/*!
\class SoIndexedLineSet
\ingroup Nodes
This node represents a 3D shape formed by constructing polylines from
vertices located at the coordinates specified in the \b vertexProperty 
field (from SoVertexShape), or the current inherited coordinates. 
For optimal performance, the \b vertexProperty  field is recommended. 


SoIndexedLineSet
uses the indices in the \b coordIndex  field (from SoIndexedShape)
to specify the polylines. An index of <tt>SO_END_LINE_INDEX</tt> (-1)
indicates that the current polyline has ended and the next one begins.


The coordinates of the line set are transformed by the current
cumulative transformation. The lines are drawn with the current light
model and drawing style (drawing style <tt>FILLED</tt> is treated as
<tt>LINES</tt>).


Treatment of the current material and normal binding is as follows:
The <tt>PER_PART</tt> binding specifies a material or normal for each
segment of the line. The <tt>PER_FACE</tt> binding specifies a material or
normal for each polyline.  <tt>PER_VERTEX</tt> specifies a material or
normal for each vertex.  The corresponding <tt>_INDEXED</tt> bindings are
the same, but use the \b materialIndex  or \b normalIndex  indices
(see SoIndexedShape). The default material binding is 
<tt>OVERALL</tt>. The default normal binding is 
<tt>PER_VERTEX_INDEXED</tt> 


The current complexity value has no effect on the rendering of indexed
line sets.

\par File Format/Default
\par
\code
IndexedLineSet {
  coordIndex 0
  materialIndex -1
  normalIndex -1
  textureCoordIndex -1
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
SoCoordinate3, SoDrawStyle, SoLineDetail, SoLineSet, SoVertexProperty
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoIndexedLineSet extends SoIndexedShape {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoIndexedLineSet.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoIndexedLineSet.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoIndexedLineSet.class); }    	  	
	
	

	public static final int SO_END_LINE_INDEX = (-1);

    //! This enum is used to indicate the current material or normal binding
    public enum Binding {
        OVERALL,
        PER_SEGMENT,    PER_SEGMENT_INDEXED,
        PER_LINE,       PER_LINE_INDEXED,
        PER_VERTEX,     PER_VERTEX_INDEXED
    };

  public
    // \name Fields
    //@{
    //! flag that defines if the GL_LINES_ADJACENCY/GL_LINE_STRIP_ADJACENCY draw mode is used (MeVis Only)
    final SoSFBool sendAdjacency = new SoSFBool();
    //@}

    
    
    //! Number of polylines, number of line segments, and vertices per polyline

    private int         numSegments;
    private int         numPolylines;
    private int[]       numVertices;

    private SoVertexArrayIndexer _lineIndexer;
        
    // Constants for influencing auto-caching algorithm:
    private final int AUTO_CACHE_ILS_MIN_WITHOUT_VP = 20;

    // And the number above which we'll say caches definitely SHOULDN'T be
    // built (because they'll use too much memory):
    private final int AUTO_CACHE_ILS_MAX = SoGLCacheContextElement.OIV_AUTO_CACHE_DEFAULT_MAX;

    private interface PMILS {
    	void run(SoIndexedLineSet set, SoGLRenderAction action);
    }
    
    //! Array of function pointers to render functions:
    private static PMILS[] renderFunc = new PMILS[32];
    
    static {
    	for(int i = 0 ; i<32;i++) {
    		final int ii = i;
    		renderFunc[i] = (ifs,a) -> SoError.post("SoIndexedLineSet RenderFunc number "+ii+" not yet implemented");
    	}
    	
    	renderFunc[6] = (set, action) -> set.OmVn(action); 
    }
    
    
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoIndexedLineSet()
//
////////////////////////////////////////////////////////////////////////
{
  nodeHeader.SO_NODE_CONSTRUCTOR(/*SoIndexedLineSet*/);
  nodeHeader.SO_NODE_ADD_SFIELD(sendAdjacency,"sendAdjacency", (false));

  isBuiltIn = true;
  numSegments = numPolylines = -1;
  numVertices = null;

  _lineIndexer = null;
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
    //delete[] numVertices;
  }
  _lineIndexer.destructor();
  
  super.destructor();
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

  state.push();
  //  put VertProp into state, if it exists.
  SoVertexProperty vp = (SoVertexProperty)vertexProperty.getValue();
  if(vp != null){
    vp.doAction(action);
  }
  // When generating primitives for picking, delay computing default
  // texture coordinates
  boolean forPicking = action.isOfType(SoRayPickAction.getClassTypeId());

  final SoPrimitiveVertex[]           pvs = new SoPrimitiveVertex[2];
  pvs[0] = new SoPrimitiveVertex();
  pvs[1] = new SoPrimitiveVertex();
  SoPrimitiveVertex pv;
  final SoLineDetail                detail = new SoLineDetail();
  final SoPointDetail               pd = new SoPointDetail();
  final SoTextureCoordinateBundle   tcb = new SoTextureCoordinateBundle(action, false, ! forPicking);
  SoCoordinateElement   ce;
  int                         curLine, curSeg, curVert, curCoord;
  int                         curMaterial = -1, curNormal = -1, curTexCoord;
  int                         vertsInLine;
  int                         numIndices;
  int[]                       coordIndices, matlIndices;
  int[]                       normIndices, texCoordIndices;
  Binding                     materialBinding, normalBinding;
  boolean                      texCoordsIndexed;

  materialBinding  = getMaterialBinding(action);
  normalBinding    = getNormalBinding(action);
  texCoordsIndexed = areTexCoordsIndexed(action);

  // Test for auto-normal case
  SoNormalElement ne = SoNormalElement.getInstance(state);
  if (ne.getNum() == 0) {
    normalBinding = Binding.OVERALL;
  }

  curLine = curSeg = curCoord = curVert = vertsInLine = 0;

  ce = SoCoordinateElement.getInstance(state);

  pvs[0].setDetail(detail);
  pvs[1].setDetail(detail);

  numIndices      = coordIndex.getNum();
  coordIndices    = coordIndex.getValuesI(0);
  matlIndices     = materialIndex.getValuesI(0);
  normIndices     = normalIndex.getValuesI(0);
  texCoordIndices = textureCoordIndex.getValuesI(0);

  // Check for special case of 1 index of SO_END_LINE_INDEX. This
  // means that coord indices are to be used for materials, normals,
  // or texture coords as well
  if (materialIndex.getNum() == 1 && matlIndices[0] == SO_END_LINE_INDEX)
    matlIndices = coordIndices;
  if (normalIndex.getNum() == 1 && normIndices[0] == SO_END_LINE_INDEX) 
    normIndices = coordIndices;
  if (textureCoordIndex.getNum() == 1 &&
    texCoordIndices[0] == SO_END_LINE_INDEX)
    texCoordIndices = coordIndices;

  if (forPicking) {
    final SbVec4f tc = new SbVec4f(0.0f, 0.0f, 0.0f, 0.0f);
    pvs[0].setTextureCoords(tc);
    pvs[1].setTextureCoords(tc);
  }

  // Step through all the coordinate indices, building lines out
  // of them, until we run out of coordinate indices.

  while (curCoord < numIndices) {

    detail.setLineIndex(curLine);

    // Loop through all vertices of current line
    while (curCoord < numIndices &&
      coordIndices[curCoord] != SO_END_LINE_INDEX) {

        switch (materialBinding) {
        case OVERALL:
          curMaterial = 0;
          break;
        case PER_SEGMENT:
          curMaterial = curSeg;
          break;
        case PER_SEGMENT_INDEXED:
          curMaterial = (int) matlIndices[curSeg];
          break;
        case PER_LINE:
          curMaterial = curLine;
          break;
        case PER_LINE_INDEXED:
          curMaterial = (int) matlIndices[curLine];
          break;
        case PER_VERTEX:
          curMaterial = curVert;
          break;
        case PER_VERTEX_INDEXED:
          curMaterial = (int) matlIndices[curCoord];
          break;
        }
        switch (normalBinding) {
        case OVERALL:
          curNormal = 0;
          break;
        case PER_SEGMENT:
          curNormal = curSeg;
          break;
        case PER_SEGMENT_INDEXED:
          curNormal = (int) normIndices[curSeg];
          break;
        case PER_LINE:
          curNormal = curLine;
          break;
        case PER_LINE_INDEXED:
          curNormal = (int) normIndices[curLine];
          break;
        case PER_VERTEX:
          curNormal = curVert;
          break;
        case PER_VERTEX_INDEXED:
          curNormal = (int) normIndices[curCoord];
          break;
        }
        curTexCoord = (texCoordsIndexed ?
          (int) texCoordIndices[curCoord] : curCoord);

        pv = pvs[curVert % 2];

        pv.setPoint(ce.get3((int) coordIndices[curCoord]));
        pv.setMaterialIndex(curMaterial);
        if (curNormal < ne.getNum())
          pv.setNormal(ne.get(curNormal));
        else pv.setNormal(new SbVec3f(0,0,0));

        // Set up a point detail for the current vertex
        pd.setCoordinateIndex((int) coordIndices[curCoord]);
        pd.setMaterialIndex(curMaterial);
        pd.setNormalIndex(curNormal);
        pd.setTextureCoordIndex(curTexCoord);

        // Replace the appropriate point detail in the line
        // detail, based on the vertex index
        if ((curVert & 1) == 0)
          detail.setPoint0(pd);
        else
          detail.setPoint1(pd);

        if (tcb.isFunction()) {
          if (! forPicking)
            pv.setTextureCoords(tcb.get(pv.getPoint(),
            pv.getNormal()));
        }
        else
          pv.setTextureCoords(tcb.get(curTexCoord));

        // If we have at least two vertices in the current line
        if (++vertsInLine >= 2) {
          detail.setPartIndex(curSeg);

          // Handle per-segment stuff specially, since we have
          // to make sure both points/details are the same
          if (materialBinding == Binding.PER_SEGMENT ||
            materialBinding == Binding.PER_SEGMENT_INDEXED) {
              pvs[0].setMaterialIndex(curMaterial);
              pvs[1].setMaterialIndex(curMaterial);
          }
          if (normalBinding == Binding.PER_SEGMENT ||
            normalBinding == Binding.PER_SEGMENT_INDEXED) {
              pvs[0].setNormal(ne.get(curNormal));
              pvs[1].setNormal(ne.get(curNormal));
          }

          invokeLineSegmentCallbacks(action,
            pvs[(curVert - 1) % 2],
            pvs[(curVert - 0) % 2]);

          //
          // Increment per-segment stuff
          //
          curSeg++;
        }

        //
        // Increment per-vertex stuff
        //
        curVert++;
        curCoord++;
    }

    //
    // Increment per-line stuff
    //
    curCoord++;     // Skip over the END_LINE_INDEX
    curLine++;
    vertsInLine = 0;
  }
  state.pop();
  
  // java port
  pvs[0].destructor(); pvs[1].destructor();
  detail.destructor();
  pd.destructor();
  tcb.destructor();
}


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
//    Overrides standard method to create an SoLineDetail instance
//    representing a picked intersection with a vertex of a line set.
//
// Use: protected, virtual

protected SoDetail 
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

  tcb.destructor();
  return detail;
}



////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns current material binding from action's state.
//
// Use: private, static

private static SoIndexedLineSet.Binding
getMaterialBinding(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
  switch (SoMaterialBindingElement.get(action.getState())) {

      case OVERALL:
        return Binding.OVERALL;

      case PER_PART:
        return Binding.PER_SEGMENT;

      case PER_PART_INDEXED:
        return Binding.PER_SEGMENT_INDEXED;

      case PER_FACE:
        return Binding.PER_LINE;

      case PER_FACE_INDEXED:
        return Binding.PER_LINE_INDEXED;

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

private static SoIndexedLineSet.Binding
getNormalBinding(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
  switch (SoNormalBindingElement.get(action.getState())) {
      case OVERALL:
        return Binding.OVERALL;

      case PER_PART:
        return Binding.PER_SEGMENT;

      case PER_PART_INDEXED:
        return Binding.PER_SEGMENT_INDEXED;

      case PER_FACE:
        return Binding.PER_LINE;

      case PER_FACE_INDEXED:
        return Binding.PER_LINE_INDEXED;

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
      int i;
      int numNeeded = 0;
      SoMFInt32 nIndices;

      if (normalIndex.getNum() == 1 &&
        normalIndex.operator_square_bracketI(0) == SO_END_LINE_INDEX) {
          nIndices = coordIndex;
      }
      else {
        nIndices = normalIndex;
      }
      // Find greatest index:
      for (i = 0; i < nIndices.getNum(); i++) {
        if ((nIndices).operator_square_bracketI(i) > numNeeded)
          numNeeded = (int) (nIndices).operator_square_bracket(i);
      }
      SoNormalElement ne = SoNormalElement.getInstance(state);
      if (numNeeded > ne.getNum()) return true;
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
  SoShapeStyleElement shapeStyle = 
    (SoShapeStyleElement)SoShapeStyleElement.get(state);

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
    // Push state, in case need change to base_color lighting:
    // Rather than generate normals, we just turn off lighting if
    // normals are needed.
    state.push();

    vpCache.fillInCache(vp, state);

    if (vpCache.shouldGenerateNormals(shapeStyle)) {
      // turn off lighting
      SoGLLazyElement.setLightModel(state, SoLazyElement.LightModel.BASE_COLOR.getValue());
      // reobtain shapeStyleElement 
      shapeStyle =        
        (SoShapeStyleElement)SoShapeStyleElement.get(state);
    }

    // Setup numVertices, numPolylines and numSegments:
    if (numPolylines < 0)
      countPolylinesAndSegments();

    SoTextureCoordinateBundle tcb = null;
    int useTexCoordsAnyway = 0;
    if (vpCache.shouldGenerateTexCoords(shapeStyle)) {      
      tcb = new SoTextureCoordinateBundle(action, true, true);
    }
    else if (shapeStyle.isTextureFunction() && vpCache.haveTexCoordsInVP()){
      useTexCoordsAnyway = SoVertexPropertyCache.Bits.TEXCOORD_BIT.getValue();
      SoGLTextureCoordinateElement.setTexGen(state, this, null);
    }

    // set up pointers
    // (this is a method on SoIndexedShape):
    // note that segments correspond to parts, polylines to faces
    setupIndices(numSegments, numPolylines, shapeStyle.needNormals(), 
      (useTexCoordsAnyway != 0 || shapeStyle.needTexCoords()));

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

    GLRenderInternal(action, useTexCoordsAnyway, shapeStyle);

    // If doing multiple colors, turn off ColorMaterial:
    if (vpCache.getNumColors() > 1) {
      SoGLLazyElement.setColorMaterial(state, false);
      ((SoGLLazyElement )SoLazyElement.getInstance(state)).
        reset(state, SoLazyElement.masks.DIFFUSE_MASK.getValue());
    }


    // Influence auto-caching algorithm:
    if (coordIndex.getNum() < AUTO_CACHE_ILS_MIN_WITHOUT_VP &&
      vpCache.mightNeedSomethingFromState(shapeStyle)) {
        SoGLCacheContextElement.shouldAutoCache(state,
          SoGLCacheContextElement.AutoCache.DO_AUTO_CACHE.getValue());
    } else if (coordIndex.getNum() > AUTO_CACHE_ILS_MAX &&
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

    if (vpCache.getNumColors() > 1) {
      SoGLLazyElement.setColorMaterial(state, false);
      ((SoGLLazyElement )SoLazyElement.getInstance(state)).
        reset(state, SoLazyElement.masks.DIFFUSE_MASK.getValue());
    }

    // Influence auto-caching algorithm:
    if (coordIndex.getNum() > AUTO_CACHE_ILS_MAX &&
      !SoGLCacheContextElement.getIsRemoteRendering(state)) {

        SoGLCacheContextElement.shouldAutoCache(state,
          SoGLCacheContextElement.AutoCache.DONT_AUTO_CACHE.getValue());
    }           
  }
  return;
}

///////////////////////////////////////////////////////////////////////////
//
// Description:
//      Count vertices in each polyline, construct numVertices array.
//      must be invoked whenever vertexPropertyCache is built.
//
// use: private
//
///////////////////////////////////////////////////////////////////////////
private void 
countPolylinesAndSegments()
{
  if (numPolylines > 0) return; // Already counted
  numPolylines = 0;
  int i, numVerts = 0;
  for(i = 0; i < coordIndex.getNum(); i++){
    if (coordIndex.operator_square_bracketI(i) == SO_END_LINE_INDEX || 
      (i == coordIndex.getNum()-1)) {
        ++numPolylines;
    } 
    if (coordIndex.operator_square_bracketI(i) != SO_END_LINE_INDEX) {
      ++numVerts;
    }
  }
  numSegments = numVerts - numPolylines;

  numVertices = new int[numPolylines];
  // Then fill in its values:
  int np = 0;
  int nv = 0;
  for(i = 0; i< coordIndex.getNum(); i++){
    if (coordIndex.operator_square_bracketI(i) == SO_END_LINE_INDEX ){
      numVertices[np] = nv;
      nv=0;
      np++;               
    }
    else {
      nv++;
      if (i == coordIndex.getNum()-1){
        numVertices[np] = nv;
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

public void
notify(SoNotList list)
//
////////////////////////////////////////////////////////////////////////
{
  // If coordIndex changes, must recount:
  if (list.getLastRec().getType() == SoNotRec.Type.CONTAINER &&
    list.getLastField() == coordIndex) {
      if (numVertices != null) {
        //delete[] numVertices; java port
      }
      numVertices = null;
      numPolylines = numSegments = -1;
  }

  super.notify(list);
}    

private void GLRenderInternal( SoGLRenderAction  action, int useTexCoordsAnyway, SoShapeStyleElement  shapeStyle )
{
  SoState  state = action.getState();

  SoGLLazyElement lazyElt;
  lazyElt = (SoGLLazyElement ) SoLazyElement.getInstance(state);

  // check for cases where GL Vertex Arrays can be used:
  if ((numPolylines>0) && (numPolylines == numSegments) && 
      SoVBO.isVertexArrayRenderingAllowed() &&
      !lazyElt.isColorIndex(state) &&
      (vpCache.getNumVertices()>0) &&
      (vpCache.getNumNormals()==0 || (vpCache.getNormalBinding() == SoNormalBindingElement.Binding.PER_VERTEX_INDEXED)) &&
      (vpCache.getNumColors()==0 || (vpCache.getMaterialBinding() == SoMaterialBindingElement.Binding.PER_VERTEX_INDEXED || vpCache.getMaterialBinding() == SoMaterialBindingElement.Binding.OVERALL)) &&
      // VA rendering is only possible if there is a color VBO, since it manages the packed color swapping
      ((vpCache.getMaterialBinding() != SoMaterialBindingElement.Binding.PER_VERTEX_INDEXED) || SoGLVBOElement.getInstance(state).getVBO(SoGLVBOElement.VBOType.COLOR_VBO) != null) &&
      (vpCache.getNumTexCoords()==0 || (vpCache.getTexCoordBinding() == SoTextureCoordinateBindingElement.Binding.PER_VERTEX_INDEXED)) &&
      (materialIndex.getNum()==1 && materialIndex.getValuesI(0)[0]==-1) && 
      (normalIndex.getNum()==1 && normalIndex.getValuesI(0)[0]==-1) && 
      (textureCoordIndex.getNum()==1 && textureCoordIndex.getValuesI(0)[0]==-1) &&
      (SoDrawStyleElement.get(action.getState()) != SoDrawStyleElement.Style.POINTS))
  {
    // we have exactly N separate lines (numPolylines == numSegments)
    boolean useVBO = beginVertexArrayRendering(action);

    if (_lineIndexer == null) {
      _lineIndexer = new SoVertexArrayIndexer(GL2.GL_LINES);
    }
    _lineIndexer.setType(sendAdjacency.getValue()?GL3.GL_LINES_ADJACENCY:GL2.GL_LINES);

    if (_lineIndexer.getDataId()!=getNodeId()) {
      _lineIndexer.setInventorLines(numPolylines, coordIndex.getValuesI(0), getNodeId());
    }
    _lineIndexer.render(state, useVBO);

//#ifdef DEBUG
    if (SoDebug.GetEnv("IV_DEBUG_VBO_RENDERING") != null) {
      SoDebugError.postInfo("GLRenderInternal", getTypeId().getName().getString() + " VA Rendering: "+numPolylines+" Polylines");
    }
//#endif

    endVertexArrayRendering(action, useVBO);
  } else {
    // Call the appropriate render loop:
    (this.renderFunc[useTexCoordsAnyway | vpCache.getRenderCase(shapeStyle)]).run(this,action);
//#ifdef DEBUG
    if (SoDebug.GetEnv("IV_DEBUG_VBO_RENDERING") != null) {
      SoDebugError.postInfo("GLRenderInternal", getTypeId().getName().getString()+" Immediate Mode Rendering: "+numPolylines+" Polylines");
    }
//#endif
  }
}


public void

OmVn
    (SoGLRenderAction action ) {
	
	GL2 gl2 = action.getCacheContext();
	
    int np = numPolylines;
    final int[] numverts = numVertices;
    final int[] vertexIndex = coordIndex.getValuesI(0);
    boolean renderAsPoints = (SoDrawStyleElement.get(action.getState()) ==
		      SoDrawStyleElement.Style.POINTS);
    boolean sendAdj = sendAdjacency.getValue();

    Buffer vertexPtr = vpCache.getVertices(0);
    final int vertexStride = vpCache.getVertexStride();
    SoVPCacheFunc vertexFunc = vpCache.vertexFunc;
    Buffer normalPtr = vpCache.getNormals(0);
    final int normalStride = vpCache.getNormalStride();
    SoVPCacheFunc normalFunc = vpCache.normalFunc;
    int[] normalIndx = getNormalIndices();

    int vtxCtr = 0;
    int v;
    int numvertsIndex = 0;
    for (int polyline = 0; polyline < np; polyline++) {
	final int nv = numverts[numvertsIndex];      
	if(renderAsPoints){
	    gl2.glBegin(GL2.GL_POINTS);
	}
	else {

	    gl2.glBegin(sendAdj?GL3.GL_LINE_STRIP_ADJACENCY:GL2.GL_LINE_STRIP);      
	}
	for (v = 0; v < nv; v++) {                  
		normalPtr.position(normalStride*normalIndx[vtxCtr]/Float.BYTES);
		(normalFunc).run(gl2,normalPtr);
		vertexPtr.position(vertexStride*vertexIndex[vtxCtr]/Float.BYTES);
		(vertexFunc).run(gl2,vertexPtr);
		vtxCtr++;
	}
	gl2.glEnd();
	vtxCtr++;  //skip over -1 at end of polyline
	numvertsIndex++;
    }
}



////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoIndexedLineSet class.
//
// Use: internal

public static void initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SoSubNode.SO__NODE_INIT_CLASS(SoIndexedLineSet.class, "IndexedLineSet", SoIndexedShape.class);
}


}
