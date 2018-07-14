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
 |      This file defines the SoSphere node class.
 |
 |   Author(s)          : Paul S. Strauss, Nick Thompson
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL2;

import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbSphere;
import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec4f;
import jscenegraph.database.inventor.SoPickedPoint;
import jscenegraph.database.inventor.SoPrimitiveVertex;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoRayPickAction;
import jscenegraph.database.inventor.bundles.SoMaterialBundle;
import jscenegraph.database.inventor.elements.SoComplexityElement;
import jscenegraph.database.inventor.elements.SoComplexityTypeElement;
import jscenegraph.database.inventor.elements.SoGLTextureEnabledElement;
import jscenegraph.database.inventor.elements.SoTextureCoordinateElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFFloat;
import jscenegraph.database.inventor.fields.SoSFInt32;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.mevis.inventor.misc.SoVBO;
import jscenegraph.port.CharPtr;
import jscenegraph.port.FloatPtr;
import jscenegraph.port.VectorOfSbVec3f;
import jscenegraph.port.VoidPtr;


////////////////////////////////////////////////////////////////////////////////
//! Sphere shape node.
/*!
\class SoSphere
\ingroup Nodes
This node represents a sphere. By default, the sphere is centered at
the origin and has a radius of 1. The sphere is transformed by the
current cumulative transformation and is drawn with the current
lighting model, drawing style, material, and geometric complexity.


A sphere does not have faces or parts. Therefore, the sphere ignores
material and normal bindings, using the first material for the entire
sphere and using its own normals. When a texture is applied to a
sphere, the texture covers the entire surface, wrapping
counterclockwise from the back of the sphere. The texture has a seam
at the back on the yz-plane.

\par File Format/Default
\par
\code
Sphere {
  radius 1
}
\endcode

\par Action Behavior
\par
SoGLRenderAction
<BR> Draws sphere based on the current coordinates, materials, drawing style, and so on. 
\par
SoRayPickAction
<BR> Intersects the ray with the sphere. No details are created for intersections. 
\par
SoGetBoundingBoxAction
<BR> Computes the bounding box that encloses the sphere. 
\par
SoCallbackAction
<BR> If any triangle callbacks are registered with the action, they will be invoked for each successive triangle that approximates the sphere. 

\par See Also
\par
SoCone, SoCube, SoCylinder
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoSphere extends SoShape {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoSphere.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoSphere.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoSphere.class); }    	  	
	
	 public final SoSFFloat radius = new SoSFFloat();
	 
	    //! Allows to explicitly specify the subdivision of the sphere.
	    //! (if set to <= 0, this is autodetected by the SoComplexityElement)
	 public final SoSFInt32           subdivision = new SoSFInt32();

    //! The state of the last sphere tesselation,
    //! so that the VBO can be reused if nothing changed.
    private class CacheState extends SimpleVertexArrayCache {
      CacheState() {
        radius = -1;
        depth = -1;
      }
      float radius;
      int depth;
    };

    private final CacheState _cache = new CacheState();
	 
// Computes S and T texture coordinates from point on surface
private final float COMPUTE_S( SbVec3f point) {                                              
  float s = (float)Math.atan2(point.getValueRead()[0], point.getValueRead()[2]) * .159f + .5f;
	return s;
}
private final float COMPUTE_T( SbVec3f point)  {                                            
	float t = (float)Math.atan2(point.getValueRead()[1], Math.sqrt(point.getValueRead()[0]*point.getValueRead()[0] + point.getValueRead()[2]*point.getValueRead()[2])) * .318f + .5f;
	return t;
}
// Adjusts S texture coordinate in unstable areas
private final float ADJUST_S(float s, int octant) {
  if (s < .001 && (octant == 1 || octant == 3))                             
  s = 1.0f;                                                              
    else if (s > .999 && (octant == 5 || octant == 7))                        
    s = 0.0f;
  return s;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoSphere()
//
////////////////////////////////////////////////////////////////////////
{
  nodeHeader.SO_NODE_CONSTRUCTOR(/*SoSphere*/);
  nodeHeader.SO_NODE_ADD_SFIELD(radius,"radius", (1.0f));
  nodeHeader.SO_NODE_ADD_SFIELD(subdivision,"subdivision", (0));
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
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Performs GL rendering of a sphere.
//
// Use: extender

public void GLRender(SoGLRenderAction action)
//
////////////////////////////////////////////////////////////////////////
{
  // First see if the object is visible and should be rendered now
  if (! shouldGLRender(action))
    return;

  // Make sure the first current material is sent to GL
  final SoMaterialBundle    mb = new SoMaterialBundle(action);
  mb.sendFirst();

  // See if texturing is enabled
  boolean doTextures = SoGLTextureEnabledElement.get(action.getState());

  // Render the sphere. The GLRenderGeneric() method handles any
  // case. The GLRenderNvertTnone() handles the case where we are
  // outputting normals but no texture coordinates. This case is
  // handled separately since it occurs often and warrants its own
  // method.
  if (SoVBO.isVertexArrayRenderingAllowed() && SoVBO.shouldUseVBO(action.getState(), SoVBO.getVboMinimumSizeLimit()+1 )) {
    GLRenderVertexArray(action, ! mb.isColorOnly(), doTextures);
  } else {
    if (! doTextures && ! mb.isColorOnly())
      GLRenderNvertTnone(action);
    else
      GLRenderGeneric(action, ! mb.isColorOnly(), doTextures);
  }
  mb.destructor(); // java port
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Implements ray picking
//
// Use: extender

public void
rayPick(SoRayPickAction action)
//
////////////////////////////////////////////////////////////////////////
{
  final SbVec3f             enterPoint = new SbVec3f(), exitPoint = new SbVec3f(), normal = new SbVec3f();
  final SbVec4f             texCoord = new SbVec4f(0.0f, 0.0f, 0.0f, 1.0f);
  SoPickedPoint       pp;

  // First see if the object is pickable
  if (! shouldRayPick(action))
    return;

  // Compute the picking ray in our current object space
  computeObjectSpaceRay(action);

  // Create SbSphere with correct radius, centered at zero
  float       rad = (radius.isIgnored() ? 1.0f : radius.getValue());
  final SbSphere    sph = new SbSphere(new SbVec3f(0.f, 0.f, 0.f), rad);

  // Intersect with pick ray. If found, set up picked point(s)
  if (sph.intersect(action.getLine(), enterPoint, exitPoint)) {
    if (action.isBetweenPlanes(enterPoint) &&
      (pp = action.addIntersection(enterPoint)) != null) {

        normal.copyFrom(enterPoint);
        normal.normalize();
        pp.setObjectNormal(normal);
        texCoord.getValue()[0] = COMPUTE_S(enterPoint);
        texCoord.getValue()[1] = COMPUTE_T(enterPoint);
        pp.setObjectTextureCoords(texCoord);
    }

    if (action.isBetweenPlanes(exitPoint) &&
      (pp = action.addIntersection(exitPoint)) != null) {

        normal.copyFrom(exitPoint);
        normal.normalize();
        pp.setObjectNormal(normal);
        texCoord.getValue()[0] = COMPUTE_S(exitPoint);
        texCoord.getValue()[1] = COMPUTE_T(exitPoint);
        texCoord.getValue()[2] = texCoord.getValue()[3] = 0.0f;
        pp.setObjectTextureCoords(texCoord);
    }
  }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Computes bounding box of sphere.
//
// Use: protected

@Override
public void computeBBox(SoAction action, SbBox3f box, SbVec3f center) {
float       rad = (radius.isIgnored() ? 1.0f : radius.getValue());

box.setBounds(-rad, -rad, -rad, rad, rad, rad);
center.setValue(0.0f, 0.0f, 0.0f);
}


// include ppp generated source code:
//#include "SoSphere_generated.cpp"

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Computes tesselation depth based on current complexity. Assumes
//    SoComplexityTypeElement and SoComplexityElement are enabled for
//    this action.
//
// Use: private

private int
computeDepth(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
	  if (subdivision.getValue() > 0) {
		    return subdivision.getValue();
		  }

  float       complexity = SoComplexityElement.get(action.getState());
  int         depth;

  // In screen space, set the tesselation depth based on the
  // complexity and the size of the sphere when projected onto the screen.
  if (SoComplexityTypeElement.get(action.getState()) ==
    SoComplexityTypeElement.Type.SCREEN_SPACE) {

      float   rad = (radius.isIgnored() ? 1.0f : radius.getValue());
      final SbVec3f p = new SbVec3f(rad, rad, rad);
      final SbVec2s rectSize = new SbVec2s();
      short   maxSize;

      getScreenSize(action.getState(), new SbBox3f(p.operator_minus(), p), rectSize);

      maxSize = (rectSize.getValue()[0] > rectSize.getValue()[1] ? rectSize.getValue()[0] : rectSize.getValue()[1]);

      depth = 1 + (int) (.1 * complexity * maxSize);
  }

  //
  // In object space, compute tesselation depth based only on
  // complexity value. We want the following correspondences between
  // complexity and depth:
  //          0.0 -> 5
  //          0.5 -> 20
  //          1.0 -> 70
  // So we'll use linear ramps from 0 to .5 and from .5 to 1
  //
  else {
    if (complexity < 0.5)
      depth = 10 + (int) ((complexity) * 20.0);
    else
      depth = -30 + (int) ((complexity) * 100.0);
  }

  return depth;
}

private final boolean ADD_TRIANGLE(VectorOfSbVec3f points, List<Integer> indices, boolean winding) { 
	int currentIndex = (int)points.size(); 
	indices.add(currentIndex-3); 
	indices.add(winding ? currentIndex-1 : currentIndex - 2); 
	indices.add(winding ? currentIndex-2 : currentIndex - 1); 
	winding = !winding;
	return winding;
}

public void GLRenderVertexArray(SoGLRenderAction action, boolean sendNormals, boolean doTextures)
{
  SoState state = action.getState();
  
  GL2 gl2 = action.getCacheContext();

  float rad = (radius.isIgnored() ? 1.0f : radius.getValue());

  // Compute depth based on complexity
  int depth = computeDepth(action);

  boolean cacheValid = _cache.radius == rad &&
                    _cache.depth == depth &&
                    _cache.useNormals == sendNormals &&
                    _cache.useTexCoords == doTextures &&
                    _cache.vbo.isValid(state);

  if (!cacheValid) {
    _cache.radius = rad;
    _cache.depth = depth;
    _cache.useNormals = sendNormals;
    _cache.useTexCoords = doTextures;

    List<Integer> indices = new ArrayList<Integer>();
    VectorOfSbVec3f points = new VectorOfSbVec3f();
    VectorOfSbVec3f normals = new VectorOfSbVec3f();
    List<SbVec2f> texCoords = new ArrayList<SbVec2f>();
    int currentIndex = 0;

    int         i, j, k, s_x, s_y, s_z, order;
    float       botWidth, topWidth, yTop, yBot, tmp;
    final SbVec3f     vec = new SbVec3f();
    float       s, t, sAvg = Float.NaN;
    boolean winding;


    for (int octant = 0; octant < 8; octant++) {
      s_x = -(((octant & 01) << 1) - 1);
      s_y = -( (octant & 02)       - 1);
      s_z = -(((octant & 04) >> 1) - 1);
      order = s_x * s_y * s_z;

      for (i = 0; i < depth - 1; i++) {
        yBot = (float) i      / depth;
        yTop = (float)(i + 1) / depth;

        botWidth = 1.0f - yBot;
        topWidth = 1.0f - yTop;

        winding = false;
        for (j = 0; j < depth - i; j++) {

          // First vertex
          k = order > 0 ? depth - i - j : j;
          tmp = (botWidth * k) / (depth - i);
          vec.setValue(s_x * tmp, s_y * yBot, s_z * (botWidth - tmp));
          vec.normalize();

          if (doTextures) {
            s = COMPUTE_S(vec);
            t = COMPUTE_T(vec);
            texCoords.add(new SbVec2f(s,t));
          }
          if (sendNormals)
            normals.add(vec);
          points.add(vec.operator_mul(rad));

          if (j>0) {
            winding = ADD_TRIANGLE(points, indices,winding);
          }

          // Second vertex
          k = order > 0 ? (depth - i - 1) - j : j;
          tmp = (topWidth * k) / (depth - i - 1);
          vec.setValue(s_x * tmp, s_y * yTop, s_z * (topWidth - tmp));
          vec.normalize();

          if (doTextures) {
            s = COMPUTE_S(vec);
            t = COMPUTE_T(vec);
            s = ADJUST_S(s, octant);
            texCoords.add(new SbVec2f(s,t));
          }
          if (sendNormals)
            normals.add(vec);
          points.add(vec.operator_mul(rad));

          if (j>0) {
            winding = ADD_TRIANGLE(points, indices,winding);
          }
        }

        // Last vertex
        k = order > 0 ? depth - i - j : j;
        tmp = (botWidth * k) / (depth - i);
        vec.setValue(s_x * tmp, s_y * yBot, s_z * (botWidth - tmp));
        vec.normalize();

        if (doTextures) {
          s = COMPUTE_S(vec);
          t = COMPUTE_T(vec);
          s = ADJUST_S(s, octant);
          texCoords.add(new SbVec2f(s,t));
        }
        if (sendNormals)
          normals.add(vec);
        points.add(vec.operator_mul(rad));
        {
          winding = ADD_TRIANGLE(points, indices,winding);
        }
      }

      // Handle the top/bottom polygons specially, to avoid divide by zero
      winding = false;
      yBot = (float) i / depth;
      yTop = 1.0f;
      botWidth = 1 - yBot;

      // First cap vertex
      if (order > 0)
        vec.setValue(0.0f, s_y * yBot, s_z * botWidth);
      else
        vec.setValue(s_x * botWidth, s_y * yBot, 0.0f);
      vec.normalize();

      if (doTextures) {
        s = COMPUTE_S(vec);
        t = COMPUTE_T(vec);
        s = ADJUST_S(s, octant);
        sAvg = s;
        texCoords.add(new SbVec2f(s,t));
      }
      if (sendNormals)
        normals.add(vec);
      points.add(vec.operator_mul(rad));

      // Second cap vertex
      if (order > 0)
        vec.setValue(s_x * botWidth, s_y * yBot, 0.0f);
      else
        vec.setValue(0.0f, s_y * yBot, s_z * botWidth);
      vec.normalize();

      if (doTextures) {
        s= COMPUTE_S(vec);
        t = COMPUTE_T(vec);
        s = ADJUST_S(s, octant);
        sAvg = (sAvg + s) / 2;
        texCoords.add(new SbVec2f(s,t));
      }
      if (sendNormals)
        normals.add(vec);
      points.add(vec.operator_mul(rad));

      // Third cap vertex
      vec.setValue(0.0f, s_y, 0.0f);

      if (doTextures) {
        s = sAvg;
        t = s_y * .5f + .5f;
        texCoords.add(new SbVec2f(s,t));
      }
      if (sendNormals)
        normals.add(vec);
      points.add(vec.operator_mul(rad));

      winding = ADD_TRIANGLE(points, indices,winding);
    }

    // vertex (3 floats) + normal (3 floats) + texcood (2 floats)
    final int numVertices = indices.size();
    final int numBytes = ((3+3+2) * 4) * numVertices; 
    CharPtr data = new CharPtr(numBytes);
    FloatPtr verticesPtr = new FloatPtr(data);
    FloatPtr normalsPtr = verticesPtr.operator_add(numVertices*3);
    FloatPtr texCoordsPtr = normalsPtr.operator_add(numVertices*3);
    CharPtr vertexOffset = new CharPtr(verticesPtr); 
    CharPtr normalOffset = new CharPtr(normalsPtr); 
    CharPtr texCoordOffset = new CharPtr(texCoordsPtr); 

    for (/*int*/ i = 0;i<numVertices;i++) {
      int index = indices.get(i);
      final SbVec3f point = points.get(index);
      verticesPtr.asterisk( point.getValueRead()[0]); verticesPtr.plusPlus();
      verticesPtr.asterisk( point.getValueRead()[1]); verticesPtr.plusPlus();
      verticesPtr.asterisk( point.getValueRead()[2]); verticesPtr.plusPlus();
      if (sendNormals) {
        final SbVec3f normal = normals.get(index);
        normalsPtr.asterisk( normal.getValueRead()[0]); normalsPtr.plusPlus();
        normalsPtr.asterisk( normal.getValueRead()[1]); normalsPtr.plusPlus();
        normalsPtr.asterisk( normal.getValueRead()[2]); normalsPtr.plusPlus();
      }
      if (doTextures) {
        final SbVec2f texCoord = texCoords.get(index);
        texCoordsPtr.asterisk( texCoord.getValueRead()[0]); texCoordsPtr.plusPlus();
        texCoordsPtr.asterisk( texCoord.getValueRead()[1]); texCoordsPtr.plusPlus();
      }
    }

    _cache.vbo.freeGL(state);
    _cache.vbo.setData(numBytes, VoidPtr.create(data.getBuffer()), 0, state);
    // force upload
    _cache.vbo.bind(state);

    _cache.vertexOffset = (vertexOffset.minus(data));
    _cache.normalOffset = (normalOffset.minus(data));
    _cache.texCoordOffset = (texCoordOffset.minus(data));
    _cache.numVertices = numVertices;

    data.destructor();
  }
  
  _cache.vbo.bind(state);
  _cache.drawArrays(this, action, GL2.GL_TRIANGLES);
  _cache.vbo.unbind(gl2);
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Generates triangles representing a sphere.
//
// Use: protected

protected void generatePrimitives(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
    int         i, j, k, s_x, s_y, s_z, order, octant;
    float       botWidth, topWidth, yTop, yBot, tmp;
    final SbVec3f     vec = new SbVec3f();
    int         depth;
    float       rad, sAvg = Float.NaN;
    final SbVec4f     tex = new SbVec4f();
    boolean      genTexCoords = false;
    final SoPrimitiveVertex   pv = new SoPrimitiveVertex();
    SoTextureCoordinateElement    tce = null;

    // Compute depth based on complexity
    depth = computeDepth(action);

    rad = (radius.isIgnored() ? 1.0f : radius.getValue());

    // Determine whether we should generate our own texture coordinates
    switch (SoTextureCoordinateElement.getType(action.getState())) {
      case EXPLICIT:
	genTexCoords = true;
	break;
      case FUNCTION:
	genTexCoords = false;
	break;
    }

    // If we're not generating our own coordinates, we'll need the
    // texture coordinate element to get coords based on points/normals.
    if (! genTexCoords)
	tce = SoTextureCoordinateElement.getInstance(action.getState());
    else {
	tex.getValue()[2] = 0.0f;
	tex.getValue()[3] = 1.0f;
    }

    for (octant = 0; octant < 8; octant++) {
	s_x = -(((octant & 01) << 1) - 1);
	s_y = -( (octant & 02)       - 1);
	s_z = -(((octant & 04) >> 1) - 1);
	order = s_x * s_y * s_z;

	for (i = 0; i < depth - 1; i++) {
	    yBot = (float) i      / depth;
	    yTop = (float)(i + 1) / depth;

	    botWidth = 1.0f - yBot;
	    topWidth = 1.0f - yTop;

	    beginShape(action, TriangleShape.TRIANGLE_STRIP);

	    for (j = 0; j < depth - i; j++) {

		// First vertex
		k = order > 0 ? depth - i - j : j;
		tmp = (botWidth * k) / (depth - i);
		vec.setValue(s_x * tmp, s_y * yBot, s_z * (botWidth - tmp));
		vec.normalize();

		if (genTexCoords) {
			tex.getValue()[0] = COMPUTE_S(vec);
			tex.getValue()[1] = COMPUTE_T(vec);
		}
		else
		    tex.copyFrom( tce.get(vec.operator_mul(rad), vec));
		pv.setPoint(vec.operator_mul(rad));
		pv.setNormal(vec);
		pv.setTextureCoords(tex);
		shapeVertex(pv);

		// Second vertex
		k = order > 0 ? (depth - i - 1) - j : j;
		tmp = (topWidth * k) / (depth - i - 1);
		vec.setValue(s_x * tmp, s_y * yTop, s_z * (topWidth - tmp));
		vec.normalize();

		if (genTexCoords) {
			tex.getValue()[0] = COMPUTE_S(vec);
			tex.getValue()[1] = COMPUTE_T(vec);
			tex.getValue()[0] = ADJUST_S(tex.getValue()[0], octant);
		}
		else
		    tex.copyFrom(tce.get(vec.operator_mul(rad), vec));
		pv.setPoint(vec.operator_mul(rad));
		pv.setNormal(vec);
		pv.setTextureCoords(tex);
		shapeVertex(pv);
	    }

	    // Last vertex
	    k = order > 0 ? depth - i - j : j;
	    tmp = (botWidth * k) / (depth - i);
	    vec.setValue(s_x * tmp, s_y * yBot, s_z * (botWidth - tmp));
	    vec.normalize();

	    if (genTexCoords) {
	    	tex.getValue()[0] = COMPUTE_S(vec);
	    	tex.getValue()[1] = COMPUTE_T(vec);
	    }
	    else
		tex.copyFrom( tce.get(vec.operator_mul(rad), vec));
	    pv.setPoint(vec.operator_mul(rad));
	    pv.setNormal(vec);
	    pv.setTextureCoords(tex);
	    shapeVertex(pv);

	    endShape();
	}

	// Handle the top/bottom polygons specially, to avoid divide by zero
	beginShape(action, TriangleShape.TRIANGLE_STRIP);

	yBot = (float) i / depth;
	yTop = 1.0f;
	botWidth = 1 - yBot;

	// First cap vertex
	if (order > 0)
	    vec.setValue(0.0f, s_y * yBot, s_z * botWidth);
	else
	    vec.setValue(s_x * botWidth, s_y * yBot, 0.0f);
	vec.normalize();

	if (genTexCoords) {
		tex.getValue()[0] = COMPUTE_S(vec);
		tex.getValue()[1] = COMPUTE_T(vec);
	    tex.getValue()[0] = ADJUST_S(tex.getValue()[0], octant);
	    sAvg = tex.getValue()[0];
	}
	else
	    tex.copyFrom(tce.get(vec.operator_mul(rad), vec));
	pv.setPoint(vec.operator_mul(rad));
	pv.setNormal(vec);
	pv.setTextureCoords(tex);
	shapeVertex(pv);

	// Second cap vertex
	if (order > 0)
	    vec.setValue(s_x * botWidth, s_y * yBot, 0.0f);
	else
	    vec.setValue(0.0f, s_y * yBot, s_z * botWidth);
	vec.normalize();

	if (genTexCoords) {
		tex.getValue()[0] = COMPUTE_S(vec);
		tex.getValue()[1] = COMPUTE_T(vec);
		tex.getValue()[0] = ADJUST_S(tex.getValue()[0], octant);
	    sAvg = (sAvg + tex.getValue()[0]) / 2;
	}
	else
	    tex.copyFrom( tce.get(vec.operator_mul(rad), vec));
	pv.setPoint(vec.operator_mul(rad));
	pv.setNormal(vec);
	pv.setTextureCoords(tex);
	shapeVertex(pv);

	// Third cap vertex
	vec.setValue(0.0f, s_y, 0.0f);

	if (genTexCoords) {
	    tex.getValue()[0] = sAvg;
	    tex.getValue()[1] = s_y * .5f + .5f;
	}
	else
	    tex.copyFrom(tce.get(vec.operator_mul(rad), vec));
	pv.setPoint(vec.operator_mul(rad));
	pv.setNormal(vec);
	pv.setTextureCoords(tex);
	shapeVertex(pv);

	endShape();
    }
    
    pv.destructor();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Generic rendering of sphere with or without normals, with or
//    without texture coordinates.
//
// Use: private

private void
GLRenderGeneric(SoGLRenderAction action,
			  boolean sendNormals, boolean doTextures)
//
////////////////////////////////////////////////////////////////////////
{
    float rad = (radius.isIgnored() ? 1.0f : radius.getValue());

    int         i, j, k, s_x, s_y, s_z, order, octant;
    float       botWidth, topWidth, yTop, yBot, tmp;
    final SbVec3f     vec = new SbVec3f();
    int         depth;
    float       s, t, sAvg = Float.NaN;

    // Compute depth based on complexity
    depth = computeDepth(action);


    for (octant = 0; octant < 8; octant++) {
	s_x = -(((octant & 01) << 1) - 1);
	s_y = -( (octant & 02)       - 1);
	s_z = -(((octant & 04) >> 1) - 1);
	order = s_x * s_y * s_z;
	
	final GL2 gl2 = action.getCacheContext();

	for (i = 0; i < depth - 1; i++) {
	    yBot = (float) i      / depth;
	    yTop = (float)(i + 1) / depth;

	    botWidth = 1.0f - yBot;
	    topWidth = 1.0f - yTop;

	    gl2.glBegin(GL2.GL_TRIANGLE_STRIP);

	    for (j = 0; j < depth - i; j++) {

		// First vertex
		k = order > 0 ? depth - i - j : j;
		tmp = (botWidth * k) / (depth - i);
		vec.setValue(s_x * tmp, s_y * yBot, s_z * (botWidth - tmp));
		vec.normalize();

		if (doTextures) {
		    s = COMPUTE_S(vec); t = COMPUTE_T(vec);
		    gl2.glTexCoord2f(s, t);
		}
		if (sendNormals)
		    gl2.glNormal3fv(vec.getValueRead(),0);
		gl2.glVertex3fv((vec.operator_mul(rad)).getValueRead(),0);

		// Second vertex
		k = order > 0 ? (depth - i - 1) - j : j;
		tmp = (topWidth * k) / (depth - i - 1);
		vec.setValue(s_x * tmp, s_y * yTop, s_z * (topWidth - tmp));
		vec.normalize();

		if (doTextures) {
		    s = COMPUTE_S(vec); t = COMPUTE_T(vec);
		    s = ADJUST_S(s, octant);
		    gl2.glTexCoord2f(s, t);
		}
		if (sendNormals)
			gl2.glNormal3fv(vec.getValueRead(),0);
		gl2.glVertex3fv((vec.operator_mul(rad)).getValueRead(),0);
	    }

	    // Last vertex
	    k = order > 0 ? depth - i - j : j;
	    tmp = (botWidth * k) / (depth - i);
	    vec.setValue(s_x * tmp, s_y * yBot, s_z * (botWidth - tmp));
	    vec.normalize();

	    if (doTextures) {
		s = COMPUTE_S(vec); t = COMPUTE_T(vec);
		s = ADJUST_S(s, octant);
		gl2.glTexCoord2f(s, t);
	    }
	    if (sendNormals)
	    	gl2.glNormal3fv(vec.getValueRead(),0);
	    gl2.glVertex3fv((vec.operator_mul(rad)).getValueRead(),0);

	    gl2.glEnd();
	}

	// Handle the top/bottom polygons specially, to avoid divide by zero
	gl2.glBegin(GL2.GL_TRIANGLE_STRIP);

	yBot = (float) i / depth;
	yTop = 1.0f;
	botWidth = 1 - yBot;

	// First cap vertex
	if (order > 0)
	    vec.setValue(0.0f, s_y * yBot, s_z * botWidth);
	else
	    vec.setValue(s_x * botWidth, s_y * yBot, 0.0f);
	vec.normalize();

	if (doTextures) {
	    s = COMPUTE_S(vec); t = COMPUTE_T(vec);
	    s = ADJUST_S(s, octant);
	    sAvg = s;
	    gl2.glTexCoord2f(s, t);
	}
	if (sendNormals)
		gl2.glNormal3fv(vec.getValueRead(),0);
	gl2.glVertex3fv((vec.operator_mul(rad)).getValueRead(),0);

	// Second cap vertex
	if (order > 0)
	    vec.setValue(s_x * botWidth, s_y * yBot, 0.0f);
	else
	    vec.setValue(0.0f, s_y * yBot, s_z * botWidth);
	vec.normalize();

	if (doTextures) {
	    s = COMPUTE_S(vec); t = COMPUTE_T(vec);
	    s = ADJUST_S(s, octant);
	    sAvg = (sAvg + s) / 2;
	    gl2.glTexCoord2f(s, t);
	}
	if (sendNormals)
		gl2.glNormal3fv(vec.getValueRead(),0);
	gl2.glVertex3fv((vec.operator_mul(rad)).getValueRead(),0);

	// Third cap vertex
	vec.setValue(0.0f, s_y, 0.0f);

	if (doTextures) {
	    s = sAvg;
	    t = s_y * .5f + .5f;
	    gl2.glTexCoord2f(s, t);
	}
	if (sendNormals)
		gl2.glNormal3fv(vec.getValueRead(),0);
	gl2.glVertex3fv((vec.operator_mul(rad)).getValueRead(),0);

	gl2.glEnd();
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Renders sphere with normals and without texture coordinates.
//
// Use: private

private void
GLRenderNvertTnone(SoGLRenderAction action)
//
////////////////////////////////////////////////////////////////////////
{
    float rad = (radius.isIgnored() ? 1.0f : radius.getValue());

    int         i, j, k, s_x, s_y, s_z, order, octant;
    float       botWidth, topWidth, yTop, yBot, tmp;
    final SbVec3f     vec = new SbVec3f();
    int         depth;

    // Compute depth based on complexity
    depth = computeDepth(action);

    final GL2 gl2 = action.getCacheContext();

    for (octant = 0; octant < 8; octant++) {
	s_x = -(((octant & 01) << 1) - 1);
	s_y = -( (octant & 02)       - 1);
	s_z = -(((octant & 04) >> 1) - 1);
	order = s_x * s_y * s_z;

	for (i = 0; i < depth - 1; i++) {
	    yBot = (float) i      / depth;
	    yTop = (float)(i + 1) / depth;

	    botWidth = 1.0f - yBot;
	    topWidth = 1.0f - yTop;

	    gl2.glBegin(GL2.GL_TRIANGLE_STRIP);

	    for (j = 0; j < depth - i; j++) {

		// First vertex
		k = order > 0 ? depth - i - j : j;
		tmp = (botWidth * k) / (depth - i);
		vec.setValue(s_x * tmp, s_y * yBot, s_z * (botWidth - tmp));
		vec.normalize();

		gl2.glNormal3fv(vec.getValueRead(),0);
		gl2.glVertex3fv((vec.operator_mul(rad)).getValueRead(),0);

		// Second vertex
		k = order > 0 ? (depth - i - 1) - j : j;
		tmp = (topWidth * k) / (depth - i - 1);
		vec.setValue(s_x * tmp, s_y * yTop, s_z * (topWidth - tmp));
		vec.normalize();

		gl2.glNormal3fv(vec.getValueRead(),0);
		gl2.glVertex3fv((vec.operator_mul(rad)).getValueRead(),0);
	    }

	    // Last vertex
	    k = order > 0 ? depth - i - j : j;
	    tmp = (botWidth * k) / (depth - i);
	    vec.setValue(s_x * tmp, s_y * yBot, s_z * (botWidth - tmp));
	    vec.normalize();

	    gl2.glNormal3fv(vec.getValueRead(),0);
	    gl2.glVertex3fv((vec.operator_mul(rad)).getValueRead(),0);

	    gl2.glEnd();
	}

	// Handle the top/bottom polygons specially, to avoid divide by zero
	gl2.glBegin(GL2.GL_TRIANGLE_STRIP);

	yBot = (float) i / depth;
	yTop = 1.0f;
	botWidth = 1 - yBot;

	// First cap vertex
	if (order > 0)
	    vec.setValue(0.0f, s_y * yBot, s_z * botWidth);
	else
	    vec.setValue(s_x * botWidth, s_y * yBot, 0.0f);
	vec.normalize();

	gl2.glNormal3fv(vec.getValueRead(),0);
	gl2.glVertex3fv((vec.operator_mul(rad)).getValueRead(),0);

	// Second cap vertex
	if (order > 0)
	    vec.setValue(s_x * botWidth, s_y * yBot, 0.0f);
	else
	    vec.setValue(0.0f, s_y * yBot, s_z * botWidth);
	vec.normalize();

	gl2.glNormal3fv(vec.getValueRead(),0);
	gl2.glVertex3fv((vec.operator_mul(rad)).getValueRead(),0);

	// Third cap vertex
	vec.setValue(0.0f, s_y, 0.0f);

	gl2.glNormal3fv(vec.getValueRead(),0);
	gl2.glVertex3fv((vec.operator_mul(rad)).getValueRead(),0);

	gl2.glEnd();
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoSphere class.
//
// Use: internal

public static void initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SoSubNode.SO__NODE_INIT_CLASS(SoSphere.class, "Sphere", SoShape.class);
}


}
