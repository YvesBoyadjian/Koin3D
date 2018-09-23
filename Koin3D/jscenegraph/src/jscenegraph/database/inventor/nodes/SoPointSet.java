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
 |      This file defines the SoPointSet node class.
 |
 |   Author(s)          : Paul S. Strauss
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import com.jogamp.opengl.GL2;

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
import jscenegraph.database.inventor.bundles.SoMaterialBundle;
import jscenegraph.database.inventor.bundles.SoTextureCoordinateBundle;
import jscenegraph.database.inventor.details.SoPointDetail;
import jscenegraph.database.inventor.elements.SoComplexityElement;
import jscenegraph.database.inventor.elements.SoCoordinateElement;
import jscenegraph.database.inventor.elements.SoGLCoordinateElement;
import jscenegraph.database.inventor.elements.SoGLLazyElement;
import jscenegraph.database.inventor.elements.SoGLNormalElement;
import jscenegraph.database.inventor.elements.SoLazyElement;
import jscenegraph.database.inventor.elements.SoLightModelElement;
import jscenegraph.database.inventor.elements.SoMaterialBindingElement;
import jscenegraph.database.inventor.elements.SoNormalBindingElement;
import jscenegraph.database.inventor.elements.SoNormalElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFInt32;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.mevis.inventor.misc.SoVBO;
import jscenegraph.port.Ctx;


////////////////////////////////////////////////////////////////////////////////
//! Point set shape node.
/*!
\class SoPointSet
\ingroup Nodes
This node represents a set of points located at the 
coordinates specified by the \b vertexProperty  field (from SoVertexShape)
or the current inherited coordinates. For optimal performance, 
the \b vertexProperty  field is recommended.


SoPointSet uses the coordinates in order,
starting with the first one. The
number of points in the set is specified by the \b numPoints  field.


The coordinates of the point set are transformed by the current
cumulative transformation. The points are drawn with the current light
model and drawing style (drawing styles <tt>FILLED</tt> and <tt>LINES</tt> are
treated as <tt>POINTS</tt>).


Treatment of the current material and normal binding is as follows:
<tt>PER_PART</tt>, <tt>PER_FACE</tt>, and <tt>PER_VERTEX</tt> bindings bind one
material or normal to each point. The default material binding is
 <tt>OVERALL</tt>. The default normal binding is 
<tt>PER_VERTEX</tt>.

\par File Format/Default
\par
\code
PointSet {
  startIndex 0
  numPoints -1
}
\endcode

\par Action Behavior
\par
SoGLRenderAction
<BR> Draws points based on the current coordinates, normals, materials, drawing style, and so on. 
\par
SoRayPickAction
<BR> Picks points based on the current coordinates and transformation. Details about the intersection are returned in an SoPointDetail. 
\par
SoGetBoundingBoxAction
<BR> Computes the bounding box that encloses all points in the set with the current transformation applied to them.  Sets the center to the average of the coordinates of all points. 
\par
SoCallbackAction
<BR> If any point callbacks are registered with the action, they will be invoked for each point in the set. 

\par See Also
\par
SoCoordinate3, SoDrawStyle, SoPointDetail, SoVertexProperty
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * 
 * @author Yves Boyadjian
 *
 */
public class SoPointSet extends SoNonIndexedShape
{
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoPointSet.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoPointSet.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoPointSet.class); }    	  	
	

//! This value, when used in the numPoints field, means use the rest of
//! the coordinates as points
public static final int SO_POINT_SET_USE_REST_OF_POINTS = -1;

    public final SoSFInt32 numPoints = new SoSFInt32();
    
////////////////////////////////////////////////////////////////////////
//
//Description:
//Constructor
//
//Use: public

    public SoPointSet() {
    	  nodeHeader.SO_NODE_CONSTRUCTOR(/*SoPointSet*/);
    	  nodeHeader.SO_NODE_ADD_SFIELD(numPoints,"numPoints",  (SO_POINT_SET_USE_REST_OF_POINTS));
    	  isBuiltIn = true;
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
  boolean                        materialPerPoint, normalPerPoint;
  int                     numPts;
  int                         curCoord, i;

  // First see if the object is visible and should be rendered now
  if (! shouldGLRender(action))
    return;

  SoState state = action.getState();
  // Push state, just in case we decide to turn on BASE_COLOR (when
  // rendering) because we don't have enough normals:
  // ??? temp fix: also, push enables us to put vertex prop in state.
  state.push();

  SoVertexProperty vp = getVertexProperty();
  if(vp != null) {
    vp.doAction(action);
  }

  // Figure out number of points in set
  final SoGLCoordinateElement ce = (SoGLCoordinateElement )
    SoCoordinateElement.getInstance(action.getState());
  curCoord = (int) startIndex.getValue();
  numPts = numPoints.getValue();
  if (numPts == SO_POINT_SET_USE_REST_OF_POINTS)
    numPts = ce.getNum() - curCoord;


  // This extra level of brackets is to make bundle destructors get
  // called before state.pop() is called:
  {
    materialPerPoint = areMaterialsPerPoint(action);
    normalPerPoint   = areNormalsPerPoint(action);

    // Test for auto-normal case; since this modifies an element this
    // MUST BE DONE BEFORE ANY BUNDLES ARE CREATED!
    final SoGLNormalElement ne = ( SoGLNormalElement )
      SoGLNormalElement.getInstance(state);

    if (ne.getNum() == 0) {
      SoLightModelElement.set(state,
        SoLightModelElement.Model.BASE_COLOR);
      normalPerPoint = false;
    }

    final SoMaterialBundle                mb = new SoMaterialBundle(action);
    final SoTextureCoordinateBundle       tcb = new SoTextureCoordinateBundle(action, true);

    // Make sure first material and normal are sent if necessary
    mb.sendFirst();
    if (mb.isColorOnly())
      normalPerPoint = false;
    if (! mb.isColorOnly() && ! normalPerPoint && ne.getNum() > 0)
      ne.send(curCoord);

    /*SoState*/ state = action.getState();
    SoGLLazyElement lazyElt = null;
    lazyElt = (SoGLLazyElement )SoLazyElement.getInstance(state);

    // check for cases where GL Vertex Arrays can be used:
    if ((numPts > 0) &&
        (ce.getNum()>0) &&
        SoVBO.isVertexArrayRenderingAllowed() &&
        !lazyElt.isColorIndex(state))
    {
      // since doAction of vertex property does not put VBOs into the state, we need to do that
      // here...
      /*SoVertexProperty*/ vp = getVertexProperty();
      if (vp != null) {
        vp.putVBOsIntoState(state);
      }
      // and we need to fill in the vpCache, since beginVertexArrayRendering expects this
      vpCache.fillInCache(vp, state);

      boolean useVBO = beginVertexArrayRendering(action);
      SoGLLazyElement.drawArrays(state, GL2.GL_POINTS, curCoord, numPts);
      endVertexArrayRendering(action, useVBO);

//#ifdef DEBUG
      if (SoDebug.GetEnv("IV_DEBUG_VBO_RENDERING") != null) {
        SoDebugError.postInfo("GLRenderInternal", getTypeId().getName().getString()+" VA Rendering: "+numPts+" Points");
      }
//#endif

    } else {
//#ifdef DEBUG
      if (SoDebug.GetEnv("IV_DEBUG_LEGACY_RENDERING") != null) {
        SoDebugError.postInfo("GLRenderInternal", getTypeId().getName().getString()+" Immediate Mode Rendering: "+numPts+" Points");
      }
//#endif
      
      GL2 gl2 = Ctx.get(action.getCacheContext());
      
      gl2.glBegin(GL2.GL_POINTS);

      for (i = 0; i < numPts; i++) {
        // Send next material, normal, and texture coordinate if necessary
        if (materialPerPoint && i > 0)
          mb.send(curCoord, true);
        if (normalPerPoint)
          ne.send(curCoord);
        if (tcb.needCoordinates())
          tcb.send(curCoord);

        // Send coordinate
        ce.send(curCoord,gl2);

        curCoord++;
      }
      gl2.glEnd();
    }
    mb.destructor(); // java port
    tcb.destructor(); // java port
  }
  // Restore Normal or LightModel element back to what they were.
  state.pop();
}

    
    
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Computes bounding box of vertices of point set.
//
// Use: protected

public void
computeBBox(SoAction action, final SbBox3f box, final SbVec3f center)
//
////////////////////////////////////////////////////////////////////////
{
  // Call the method on SoNonIndexedShape that computes the bounding
  // box and center of the given number of coordinates
  computeCoordBBox(action, (int) numPoints.getValue(), box, center);
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Generates points representing a point set.
//
// Use: protected

protected void
generatePrimitives(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
  // When generating primitives for picking, delay computing default
  // texture coordinates
  boolean forPicking = action.isOfType(SoRayPickAction.getClassTypeId());

  boolean                      materialPerPoint, normalPerPoint;
  int                     numPts;
  int                         curCoord, i;
  final SoPrimitiveVertex           pv = new SoPrimitiveVertex();
  final SoPointDetail               detail = new SoPointDetail();

  // Push state, just in case we decide to set the NormalElement
  // because we're doing auto-normal generation.
  SoState state = action.getState();
  state.push();

  // Put vertexProperty stuff into state:
  SoVertexProperty vp = (SoVertexProperty)vertexProperty.getValue();
  if (vp != null) {
    vp.doAction(action);
  }

  // This extra level of brackets is to make bundle constructors get
  // called before state.pop() is called:
  {
     SoGLCoordinateElement     ce = ( SoGLCoordinateElement )
      SoCoordinateElement.getInstance(action.getState());

    // Figure out number of points in set
    curCoord = (int) startIndex.getValue();
    numPts = numPoints.getValue();
    if (numPts == SO_POINT_SET_USE_REST_OF_POINTS)
      numPts = ce.getNum() - curCoord;

    materialPerPoint = areMaterialsPerPoint(action);
    normalPerPoint   = areNormalsPerPoint(action);

    // Test for auto-normal case; since this modifies an element this
    // MUST BE DONE BEFORE ANY BUNDLES ARE CREATED!
    SoNormalElement ne = SoNormalElement.getInstance(state);
    if (ne.getNum() == 0) {
      normalPerPoint = false;
    }

    if (forPicking)
      pv.setTextureCoords(new SbVec4f(0.0f, 0.0f, 0.0f, 0.0f));

    pv.setDetail(detail);

    final SoTextureCoordinateBundle       tcb = new SoTextureCoordinateBundle(action, false, ! forPicking);

    pv.setMaterialIndex(curCoord);
    detail.setMaterialIndex(curCoord);

    if (! normalPerPoint) {
      if (ne.getNum() == 0) pv.setNormal(new SbVec3f(0,0,0));
      else pv.setNormal(ne.get(0));
      detail.setNormalIndex(0);
    }

    // Get the complexity element and decide how points will be skipped
    // during processing; note that we don't want to skip anything
    // when picking.
    float cmplxValue = SoComplexityElement.get(action.getState());
    float delta = 1.8f * (0.5f - ((cmplxValue < 0.5) ? cmplxValue : 0.5f));
    float fraction = 0.0f;
    if (forPicking)
      delta = 0.0f;

    for (i = 0; i < numPts; i++, fraction += delta) {

      // Check to see if this point should be skipped due to complexity
      if (fraction >= 1.0) {
        fraction -= 1.0;
        curCoord++;
        continue;
      }

      // Set coordinates, normal, and texture coordinates in
      // detail

      pv.setPoint(ce.get3(curCoord));
      detail.setCoordinateIndex(curCoord);
      if (normalPerPoint) {
        pv.setNormal(ne.get(curCoord));
        detail.setNormalIndex(curCoord);
      }
      if (materialPerPoint) {
        pv.setMaterialIndex(curCoord);
        detail.setMaterialIndex(curCoord);
      }
      if (tcb.isFunction()) {
        if (! forPicking)
          pv.setTextureCoords(tcb.get(pv.getPoint(),
          pv.getNormal()));
        detail.setTextureCoordIndex(0);
      }
      else {
        pv.setTextureCoords(tcb.get(curCoord));
        detail.setTextureCoordIndex(curCoord);
      }

      // Generate a point primitive
      invokePointCallbacks(action, pv);

      curCoord++;
    }

  }
  state.pop();     // Restore NormalElement
  
  pv.destructor();
  detail.destructor();
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns TRUE if materials are bound to individual points.
//
// Use: private

private boolean
areMaterialsPerPoint(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
  if (SoMaterialBindingElement.get(action.getState()) ==
    SoMaterialBindingElement.Binding.OVERALL)
    return false;

  return true;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns TRUE if normals are bound to individual points.
//
// Use: private

private boolean
areNormalsPerPoint(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
  if (SoNormalBindingElement.get(action.getState())
    == SoNormalBindingElement.Binding.OVERALL)
    return false;

  return true;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoPointSet class.
//
// Use: internal

public static void initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SoSubNode.SO__NODE_INIT_CLASS(SoPointSet.class, "PointSet", SoNonIndexedShape.class);
}


}
