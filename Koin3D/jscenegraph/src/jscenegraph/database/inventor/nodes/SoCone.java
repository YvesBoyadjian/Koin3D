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
 |      This file defines the SoCone node class.
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
import jscenegraph.database.inventor.SbLine;
import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbPlane;
import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec3fSingle;
import jscenegraph.database.inventor.SbVec4f;
import jscenegraph.database.inventor.SoPickedPoint;
import jscenegraph.database.inventor.SoPrimitiveVertex;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoRayPickAction;
import jscenegraph.database.inventor.bundles.SoMaterialBundle;
import jscenegraph.database.inventor.details.SoConeDetail;
import jscenegraph.database.inventor.elements.SoComplexityElement;
import jscenegraph.database.inventor.elements.SoComplexityTypeElement;
import jscenegraph.database.inventor.elements.SoGLTextureEnabledElement;
import jscenegraph.database.inventor.elements.SoLightModelElement;
import jscenegraph.database.inventor.elements.SoMaterialBindingElement;
import jscenegraph.database.inventor.elements.SoTextureCoordinateElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFBitMask;
import jscenegraph.database.inventor.fields.SoSFFloat;
import jscenegraph.database.inventor.fields.SoSFInt32;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.mevis.inventor.misc.SoVBO;
import jscenegraph.port.CharPtr;
import jscenegraph.port.FloatPtr;
import jscenegraph.port.VectorOfSbVec3f;
import jscenegraph.port.VoidPtr;

/**
 * @author Yves Boyadjian
 *
 */

////////////////////////////////////////////////////////////////////////////////
//! Cone shape node.
/*!
\class SoCone
\ingroup Nodes
This node represents a simple cone whose central axis is aligned with
the y-axis. By default, the cone is centered at (0,0,0) and has a size
of -1 to +1 in all three directions. The cone has a radius of 1 at the
bottom and a height of 2, with its apex at 1. The cone has two parts:
the sides and the bottom.


The cone is transformed by the current cumulative transformation and
is drawn with the current lighting model, drawing style, material, and
geometric complexity.


If the current material binding is <tt>PER_PART</tt> or
<tt>PER_PART_INDEXED</tt>, the first current material is used for the sides
of the cone, and the second is used for the bottom. Otherwise, the
first material is used for the entire cone.


When a texture is applied to a cone, it is applied differently to the
sides and bottom. On the sides, the texture wraps counterclockwise
(from above) starting at the back of the cone. The texture has a
vertical seam at the back, intersecting the yz-plane. For the bottom,
a circle is cut out of the texture square and applied to the cone's
base circle. The texture appears right side up when the top of the
cone is tilted away from the camera.

\par File Format/Default
\par
\code
Cone {
  parts ALL
  bottomRadius 1
  height 2
}
\endcode

\par Action Behavior
\par
SoGLRenderAction
<BR> Draws cone based on the current coordinates, materials, drawing style, and so on. 
\par
SoRayPickAction
<BR> Intersects the ray with the cone. The part of the cone that was picked is available from the SoConeDetail. 
\par
SoGetBoundingBoxAction
<BR> Computes the bounding box that encloses the cone. 
\par
SoCallbackAction
<BR> If any triangle callbacks are registered with the action, they will be invoked for each successive triangle that approximates the cone. 

\par See Also
\par
SoConeDetail, SoCube, SoCylinder, SoSphere
*/
////////////////////////////////////////////////////////////////////////////////

public class SoCone extends SoShape {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoCone.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoCone.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoCone.class); }    	  	
	

    //! Cone parts:
    public enum Part {
        SIDES   ( 0x01),                 //!< The conical part
        BOTTOM  ( 0x02),                 //!< The bottom circular face
        ALL     ( 0x03);                  //!< All parts
    	
    	private int value;
    	
    	Part(int value) {
    		this.value = value;
    	}
    	
    	public int getValue() {
    		return value;
    	}
    };

    //! \name Fields
    //@{

    //! Visible parts of cone.
    public final SoSFBitMask         parts = new SoSFBitMask();          
    //! Radius of bottom circular face
    public final SoSFFloat           bottomRadius = new SoSFFloat();   
    //! Size in y dimension
    public final SoSFFloat           height = new SoSFFloat();         

    //! Defines how many sides are used for tesselation. If set to <= 0, the sides are
    //! calculated by the SoComplexityElement.
    public final SoSFInt32           sides = new SoSFInt32();

    //! Defines how many sections are used for tesselation. If set to <= 0, the sections are
    //! calculated by the SoComplexityElement.
    public final SoSFInt32           sections = new SoSFInt32();

    //@}

////////////////////////////////////////////////////////////////////////
//
//Description:
//Constructor
//
//Use: public

    //! Creates a cone node with default settings.
    public SoCone() {
    	nodeHeader.SO_NODE_CONSTRUCTOR(/*SoCone*/);
    	  isBuiltIn = true;

    	  nodeHeader.SO_NODE_ADD_FIELD(parts,"parts",            (Part.ALL.getValue()));
    	  nodeHeader.SO_NODE_ADD_FIELD(bottomRadius,"bottomRadius",     (1.0f));
    	  nodeHeader.SO_NODE_ADD_FIELD(height,"height",   (2.0f));

    	  nodeHeader.SO_NODE_ADD_FIELD(sides,"sides", (0));
    	  nodeHeader.SO_NODE_ADD_FIELD(sections,"sections", (0));

    	  // Set up static info for enumerated type field
    	  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Part.SIDES);
    	  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Part.BOTTOM);
    	  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Part.ALL);

    	  // Set up info in enumerated type field
    	  nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(parts,"parts", "Part");

    	  // Set size of arrays to 0 so they will be allocated first time
    	  if (nodeHeader.SO_NODE_IS_FIRST_INSTANCE()) {
    	    maxCoords = 0;
    	  }    	
    }

////////////////////////////////////////////////////////////////////////
//
//Description:
//Turns on a part of the cone. (Convenience function.)
//
//Use: public

    //! Turns on/off a part of the cone. (Convenience functions)
    public void                addPart(SoCone.Part part) {
    	  parts.setValue(parts.getValue() | part.getValue());    	
    }

////////////////////////////////////////////////////////////////////////
//
//Description:
//Turns off a part of the cone. (Convenience function.)
//
//Use: public

    public void                removePart(SoCone.Part part) {
    	  parts.setValue(parts.getValue() & ~part.getValue());
    }

////////////////////////////////////////////////////////////////////////
//
//Description:
//Returns whether a given part is on or off. (Convenience function.)
//
//Use: public

    //! Returns whether a given part is on or off. (Convenience function)
    public boolean                hasPart(SoCone.Part part) {
    	  return HAS_PART(parts.getValue(), part);    	
    }
    
 // Shorthand for testing whether current parts includes PART
    private boolean HAS_PART(int PARTS, SoCone.Part PART) {
    	return ((PARTS & (PART.getValue())) != 0);
    }
   
////////////////////////////////////////////////////////////////////////
//
//Description:
//Performs GL rendering of a cone.
//
//Use: protected

    //! Implements actions
    public void        GLRender(SoGLRenderAction action) {
	  // First see if the object is visible and should be rendered now
	  if (! shouldGLRender(action))
	    return;
	
	  // See if texturing is enabled
	  boolean doTextures = SoGLTextureEnabledElement.get(action.getState());
	
	  // Render the cone. The GLRenderGeneric() method handles any
	  // case. The GLRenderNvertTnone() handles the case where we are
	  // outputting normals but no texture coordinates. This case is
	  // handled separately since it occurs often and warrants its own
	  // method.
	  boolean sendNormals = (SoLightModelElement.get(action.getState()) !=
	    SoLightModelElement.Model.BASE_COLOR);
	
	  SoMaterialBindingElement.Binding mbe =
	    SoMaterialBindingElement.get(action.getState());
	  boolean materialPerPart =
	    (mbe == SoMaterialBindingElement.Binding.PER_PART_INDEXED ||
	    mbe == SoMaterialBindingElement.Binding.PER_PART);
	
	  if (!materialPerPart && SoVBO.isVertexArrayRenderingAllowed() && SoVBO.shouldUseVBO(action.getState(), SoVBO.getVboMinimumSizeLimit()+1 )) {
	    GLRenderVertexArray(action, sendNormals, doTextures);
	  } else {
	    if (! doTextures && sendNormals)
	      GLRenderNvertTnone(action);
	    else
	      GLRenderGeneric(action, sendNormals, doTextures);
	  }    	
    }
    
////////////////////////////////////////////////////////////////////////
//
//Description:
//Implements ray picking
//
//Use: protected

    public void        rayPick(SoRayPickAction action) {
  // First see if the object is pickable
  if (! shouldRayPick(action))
    return;

  int                 curParts =(parts.isIgnored() ? Part.ALL.getValue() : parts.getValue());
  final SbMatrix            matrix = new SbMatrix(), matrix2 = new SbMatrix();
  final float[]               radius = new float[1], halfHeight = new float[1];
  final SbVec3fSingle             enterPoint = new SbVec3fSingle(), exitPoint = new SbVec3fSingle(), objectPoint = new SbVec3fSingle(), normal = new SbVec3fSingle();
  final SbVec4f             texCoord = new SbVec4f();
  SoPickedPoint       pp;
  SoConeDetail        detail;
  boolean                materialPerPart;
  int                 numHits = 0;

  // Get size of this cone
  getSize(radius, halfHeight);

  // Compute a matrix that will transform a canonical cone (apex at
  // the origin, bottom radius 1) to this cone
  matrix.setTranslate(new SbVec3f(0.0f, halfHeight[0], 0.0f));
  matrix2.setScale(new SbVec3f(radius[0], 2.0f * halfHeight[0], radius[0]));
  matrix.multLeft(matrix2);

  // Compute the object-space picking ray, using the matrix we
  // computed in addition to the one stored in the action
  computeObjectSpaceRay(action, matrix);

  SoMaterialBindingElement.Binding mbe =
    SoMaterialBindingElement.get(action.getState());
  materialPerPart =
    (mbe == SoMaterialBindingElement.Binding.PER_PART_INDEXED ||
    mbe == SoMaterialBindingElement.Binding.PER_PART);

  // See if the line intersects an infinite cone
  if (HAS_PART(curParts, Part.SIDES) &&
    intersectInfiniteCone(action.getLine(), enterPoint, exitPoint)) {

      // Convert the point into object space from "canonical" space
      matrix.multVecMatrix(enterPoint, objectPoint);

      // See if the enter point is within the part of the cone we
      // are interested in: between the apex (at y = 0) and the base
      // (at y = -1). Also check if the intersection is between the
      // near and far clipping planes.

      if (enterPoint.getValueRead()[1] <= 0.0 && enterPoint.getValueRead()[1] >= -1.0) {

        numHits++;

        if (action.isBetweenPlanes(objectPoint) &&
          (pp = action.addIntersection(objectPoint)) != null) {

            // The normal is perpendicular to the vector V from the
            // apex to the intersection point in the plane containing
            // both  V and the y-axis. Using a couple of cross
            // products, we arrive at the following result.
            // (First, translate the cone point back down as if the
            // center were at the origin, making the math easier.)
            objectPoint.getValue()[1] -= halfHeight[0];
            normal.setValue(-objectPoint.getValueRead()[0] * objectPoint.getValueRead()[1],
              objectPoint.getValueRead()[0] * objectPoint.getValueRead()[0] + 
              objectPoint.getValueRead()[2] * objectPoint.getValueRead()[2],
              -objectPoint.getValueRead()[1] * objectPoint.getValueRead()[2]);
            normal.normalize();
            pp.setObjectNormal(normal);

            texCoord.setValue((float)Math.atan2(enterPoint.getValueRead()[0], enterPoint.getValueRead()[2])
              * (1.0f / (2.0f * (float)Math.PI)) + 0.5f,
              enterPoint.getValueRead()[1] + 1.0f,
              0.0f, 1.0f);
            pp.setObjectTextureCoords(texCoord);

            detail = new SoConeDetail();
            detail.setPart(Part.SIDES.getValue());
            pp.setDetail(detail, this);
        }
      }

      // Do same for exit point

      // Convert the point into object space from "canonical" space
      matrix.multVecMatrix(exitPoint, objectPoint);

      if (exitPoint.getValueRead()[1] <= 0.0 && exitPoint.getValueRead()[1] >= -1.0) {

        numHits++;

        if (action.isBetweenPlanes(objectPoint) &&
          (pp = action.addIntersection(objectPoint)) != null) {

            // The normal is perpendicular to the vector V from the
            // apex to the intersection point in the plane containing
            // both  V and the y-axis. Using a couple of cross
            // products, we arrive at the following result.
            // (First, translate the cone point back down as if the
            // apex were at the origin, making the math easier.)
            objectPoint.getValue()[1] -= halfHeight[0];
            normal.setValue(-objectPoint.getValueRead()[0] * objectPoint.getValueRead()[1],
              objectPoint.getValueRead()[0] * objectPoint.getValueRead()[0] + 
              objectPoint.getValueRead()[2] * objectPoint.getValueRead()[2],
              -objectPoint.getValueRead()[1] * objectPoint.getValueRead()[2]);
            normal.normalize();
            pp.setObjectNormal(normal);

            texCoord.setValue((float)Math.atan2(exitPoint.getValueRead()[0], exitPoint.getValueRead()[2])
              * (1.0f / (2.0f * (float)Math.PI)) + 0.5f,
              exitPoint.getValueRead()[1] + 1.0f,
              0.0f, 1.0f);
            pp.setObjectTextureCoords(texCoord);

            detail = new SoConeDetail();
            detail.setPart(Part.SIDES.getValue());
            pp.setDetail(detail, this);
        }
      }
  }

  // If we haven't hit the cone twice already, check for an
  // intersection with the bottom face
  if (numHits < 2 && HAS_PART(curParts, Part.BOTTOM)) {
    final SbVec3f norm = new SbVec3f(0.0f, -1.0f, 0.0f);

    // Construct a plane containing the bottom face (in canonical space)
    final SbPlane         bottomFacePlane = new SbPlane(norm, 1.0f);

    // See if the ray hits this plane
    if (bottomFacePlane.intersect(action.getLine(), enterPoint)) {

      // Convert the point into object space from "canonical" space
      matrix.multVecMatrix(enterPoint, objectPoint);

      // See if the intersection is within the correct radius
      // and is within the clipping planes
      float distFromYAxisSquared = (enterPoint.getValueRead()[0] * enterPoint.getValueRead()[0] +
        enterPoint.getValueRead()[2] * enterPoint.getValueRead()[2]);

      if (distFromYAxisSquared <= 1.0 &&
        action.isBetweenPlanes(objectPoint) &&
        (pp = action.addIntersection(objectPoint)) != null) {

          pp.setObjectNormal(norm);

          texCoord.setValue(0.5f + enterPoint.getValueRead()[0] / 2.0f,
            0.5f + enterPoint.getValueRead()[2] / 2.0f,
            0.0f, 1.0f);
          pp.setObjectTextureCoords(texCoord);

          if (materialPerPart)
            pp.setMaterialIndex(1);

          detail = new SoConeDetail();
          detail.setPart(Part.BOTTOM.getValue());
          pp.setDetail(detail, this);
      }
    }
  }
    }

    public static void         initClass() {
        SO__NODE_INIT_CLASS(SoCone.class, "Cone", SoShape.class);    	
    }

////////////////////////////////////////////////////////////////////////
//
//Description:
//Generates triangles representing a cone.
//
//Use: protected

    //! Generates triangles representing a cone
    protected void        generatePrimitives(SoAction action) {
    boolean              materialPerPart;
    int                 curParts, side, section;
    final int[]			 numSides = new int[1], numSections = new int[1];
    float               yTop, yBot, dy;
    float               s, ds, tTop, tBot, dt;
    float               outerRadius, innerRadius, dRadius;
    final SbVec2f[][]             baseCoords = new SbVec2f[1][];
    final SbVec3f[][]             sideNormals = new SbVec3f[1][];
    final SbVec3fSingle pt = new SbVec3fSingle(), norm = new SbVec3fSingle();
    final float[]               radius = new float[1], halfHeight = new float[1];
    final SbVec4f             tex = new SbVec4f();
    boolean              genTexCoords = false;
    final SoPrimitiveVertex   pv = new SoPrimitiveVertex();
    final SoConeDetail        detail = new SoConeDetail();
    SoTextureCoordinateElement    tce = null;

    SoMaterialBindingElement.Binding mbe =
	SoMaterialBindingElement.get(action.getState());
    materialPerPart =
	(mbe == SoMaterialBindingElement.Binding.PER_PART_INDEXED ||
	 mbe == SoMaterialBindingElement.Binding.PER_PART);

    curParts = (parts.isIgnored() ? Part.ALL.getValue() : parts.getValue());

    // Compute number of sides and sections to use to represent
    // cone, then compute ring of x,z coordinates around cone
    // and store in baseCoords.
    computeBase(action, numSides, numSections, baseCoords, sideNormals);

    pv.setDetail(detail);

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

    getSize(radius, halfHeight);

    dRadius = 1.0f / numSections[0];

    if (HAS_PART(curParts, Part.SIDES)) {

	// Draw each section of sides as a triangle mesh, from top to bottom
	yTop = 1.0f;
	dy   = -2.0f / numSections[0];
	tTop = 1.0f;
	dt   = -1.0f / numSections[0];
	ds   =  1.0f / numSides[0];

	innerRadius = 0.0f;

	for (section = 0; section < numSections[0]; section++) {

	    outerRadius = innerRadius + dRadius;
	    yBot = yTop + dy;

	    tBot = tTop + dt;
	    s    = 0.0f;

	    detail.setPart(Part.SIDES.getValue());

	    beginShape(action, TriangleShape.TRIANGLE_STRIP);

	    for (side = 0; side < numSides[0]; side++) {
		// Deal with normal
		pv.setNormal(sideNormals[0][side]);

		// Point at bottom of section
		pt.setValue(outerRadius * baseCoords[0][side].getValue()[0], yBot,
			    outerRadius * baseCoords[0][side].getValue()[1]);
		pt.getValue()[0] *= radius[0];
		pt.getValue()[1] *= halfHeight[0];
		pt.getValue()[2] *= radius[0];
		if (genTexCoords) {
		    tex.getValue()[0] = s;
		    tex.getValue()[1] = tBot;
		}
		else
		    tex.copyFrom( tce.get(pt, sideNormals[0][side]));
		pv.setPoint(pt);
		pv.setTextureCoords(tex);
		shapeVertex(pv);

		// Point at top of section
		pt.setValue(innerRadius * baseCoords[0][side].getValue()[0], yTop,
			    innerRadius * baseCoords[0][side].getValue()[1]);
		pt.getValue()[0] *= radius[0];
		pt.getValue()[1] *= halfHeight[0];
		pt.getValue()[2] *= radius[0];
		if (genTexCoords) {
		    tex.getValue()[0] = s;
		    tex.getValue()[1] = tTop;
		}
		else
		    tex.copyFrom(tce.get(pt, sideNormals[0][side]));
		pv.setPoint(pt);
		pv.setTextureCoords(tex);
		shapeVertex(pv);
		s += ds;
	    }

	    // Join end of strip back to beginning
	    side = 0;
	    s = 1.0f;
	    // Deal with normal
	    pv.setNormal(sideNormals[0][side]);

	    // Point at bottom of section
	    pt.setValue(outerRadius * baseCoords[0][side].getValue()[0], yBot,
			outerRadius * baseCoords[0][side].getValue()[1]);
	    pt.getValue()[0] *= radius[0];
	    pt.getValue()[1] *= halfHeight[0];
	    pt.getValue()[2] *= radius[0];
	    if (genTexCoords) {
	    	tex.getValue()[0] = s;
	    	tex.getValue()[1] = tBot;
	    }
	    else
	    	tex.copyFrom( tce.get(pt, sideNormals[0][side]));
	    pv.setPoint(pt);
	    pv.setTextureCoords(tex);
	    shapeVertex(pv);

	    // Point at top of section
	    pt.setValue(innerRadius * baseCoords[0][side].getValue()[0], yTop,
			innerRadius * baseCoords[0][side].getValue()[1]);
	    pt.getValue()[0] *= radius[0];
	    pt.getValue()[1] *= halfHeight[0];
	    pt.getValue()[2] *= radius[0];
	    if (genTexCoords) {
	    	tex.getValue()[0] = s;
	    	tex.getValue()[1] = tTop;
	    }
	    else
	    	tex.copyFrom( tce.get(pt, sideNormals[0][side]));
	    pv.setPoint(pt);
	    pv.setTextureCoords(tex);
	    shapeVertex(pv);
	    s += ds;

	    endShape();

	    // Prepare for next section down
	    innerRadius = outerRadius;
	    yTop = yBot;
	    tTop = tBot;
	}
    }

    // Draw bottom face as a series of concentric rings. The number of
    // rings is the same as the number of sections of the sides of the
    // cone.
    if (HAS_PART(curParts, Part.BOTTOM)) {
	norm.setValue(0.0f, -1.0f, 0.0f);
	pt.getValue()[1] = -halfHeight[0];

	if (materialPerPart)
	    pv.setMaterialIndex(1);
	pv.setNormal(norm);
	detail.setPart(Part.BOTTOM.getValue());

	// Start at the outside and work in
	outerRadius = 1.0f;
	for (section = numSections[0] - 1; section >= 0; --section) {

	    innerRadius = outerRadius - dRadius;

	    // Innermost ring is drawn as a triangle fan. This not
	    // only gets better shading (because the center vertex is
	    // sent), but also avoids the problem of having a polygon
	    // with too many vertices.
	    if (section == 0) {
		beginShape(action, TriangleShape.TRIANGLE_FAN);

		// Center point comes first
		pt.getValue()[0] = pt.getValue()[2] = 0.0f;
		if (genTexCoords)
		    tex.getValue()[0] = tex.getValue()[1] = 0.5f;
		else
		    tex.copyFrom( tce.get(norm, norm));
		pv.setPoint(pt);
		pv.setTextureCoords(tex);
		shapeVertex(pv);

		// Send all vertices around ring
		for (side = 0; side < numSides[0]; side++) {
		    pt.getValue()[0] = outerRadius * baseCoords[0][side].getValue()[0];
		    pt.getValue()[2] = outerRadius * baseCoords[0][side].getValue()[1];
		    pt.getValue()[0] *= radius[0];
		    pt.getValue()[2] *= radius[0];
		    if (genTexCoords) {
			tex.getValue()[0] = BOT_TEX_S(pt.getValueRead()[0]);
			tex.getValue()[1] = BOT_TEX_T(pt.getValueRead()[2]);
		    }
		    else
			tex.copyFrom( tce.get(pt, norm));
		    pv.setPoint(pt);
		    pv.setTextureCoords(tex);
		    shapeVertex(pv);
		}
		// Send first vertex again
		pt.getValue()[0] = outerRadius * baseCoords[0][0].getValue()[0];
		pt.getValue()[2] = outerRadius * baseCoords[0][0].getValue()[1];
		pt.getValue()[0] *= radius[0];
		pt.getValue()[2] *= radius[0];
		if (genTexCoords) {
		    tex.getValue()[0] = BOT_TEX_S(pt.getValueRead()[0]);
		    tex.getValue()[1] = BOT_TEX_T(pt.getValueRead()[2]);
		}
		else
		    tex.copyFrom( tce.get(pt, norm));
		pv.setPoint(pt);
		pv.setTextureCoords(tex);
		shapeVertex(pv);

		endShape();
	    }

	    // Other rings are triangle strips
	    else {
		beginShape(action, TriangleShape.TRIANGLE_STRIP);

		// Go in reverse order so that vertex ordering is correct
		for (side = numSides[0] - 1; side >= 0; side--) {
		    // Send points on outer and inner rings
		    pt.getValue()[0] = outerRadius * baseCoords[0][side].getValue()[0];
		    pt.getValue()[2] = outerRadius * baseCoords[0][side].getValue()[1];
		    pt.getValue()[0] *= radius[0];
		    pt.getValue()[2] *= radius[0];
		    if (genTexCoords) {
			tex.getValue()[0] = BOT_TEX_S(pt.getValueRead()[0]);
			tex.getValue()[1] = BOT_TEX_T(pt.getValueRead()[2]);
		    }
		    else
			tex.copyFrom( tce.get(pt, norm));
		    pv.setPoint(pt);
		    pv.setTextureCoords(tex);
		    shapeVertex(pv);
		    pt.getValue()[0] = innerRadius * baseCoords[0][side].getValue()[0];
		    pt.getValue()[2] = innerRadius * baseCoords[0][side].getValue()[1];
		    pt.getValue()[0] *= radius[0];
		    pt.getValue()[2] *= radius[0];
		    if (genTexCoords) {
			tex.getValue()[0] = BOT_TEX_S(pt.getValueRead()[0]);
			tex.getValue()[1] = BOT_TEX_T(pt.getValueRead()[2]);
		    }
		    else
			tex.copyFrom(tce.get(pt, norm));
		    pv.setPoint(pt);
		    pv.setTextureCoords(tex);
		    shapeVertex(pv);
		}

		// Join end of strip back to beginning
		side = numSides[0] - 1;
		pt.getValue()[0] = outerRadius * baseCoords[0][side].getValue()[0];
		pt.getValue()[2] = outerRadius * baseCoords[0][side].getValue()[1];
		pt.getValue()[0] *= radius[0];
		pt.getValue()[2] *= radius[0];
		if (genTexCoords) {
		    tex.getValue()[0] = BOT_TEX_S(pt.getValueRead()[0]);
		    tex.getValue()[1] = BOT_TEX_T(pt.getValueRead()[2]);
		}
		else
		    tex.copyFrom( tce.get(pt, norm));
		pv.setPoint(pt);
		pv.setTextureCoords(tex);
		shapeVertex(pv);
		pt.getValue()[0] = innerRadius * baseCoords[0][side].getValue()[0];
		pt.getValue()[2] = innerRadius * baseCoords[0][side].getValue()[1];
		pt.getValue()[0] *= radius[0];
		pt.getValue()[2] *= radius[0];
		if (genTexCoords) {
		    tex.getValue()[0] = BOT_TEX_S(pt.getValueRead()[0]);
		    tex.getValue()[1] = BOT_TEX_T(pt.getValueRead()[2]);
		}
		else
		    tex.copyFrom( tce.get(pt, norm));
		pv.setPoint(pt);
		pv.setTextureCoords(tex);
		shapeVertex(pv);

		endShape();

		// Prepare for next ring
		outerRadius = innerRadius;
	    }
	}
    }
    pv.destructor(); // java port
    detail.destructor(); // java port
    }
    
 // Returns S or T texture coord for point on bottom of cone, given x
 // or z coord
    private float BOT_TEX_S(float x) { return   ((x) * .5f + .5f); }
    private float BOT_TEX_T(float z) { return   ((z) * .5f + .5f); }

    

////////////////////////////////////////////////////////////////////////
//
//Description:
//Computes bounding box of cone.
//
//Use: protected

    //! Computes bounding box of cone
    public void        computeBBox(SoAction action, final SbBox3f box,
                                    final SbVec3f center) {
  int         curParts = (parts.isIgnored() ? Part.ALL.getValue() : parts.getValue());

  if (curParts == 0)          // No parts at all!
    box.setBounds(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);

  else {
    final float[]   br = new float[1], h = new float[1];
    final SbVec3f min = new SbVec3f(), max = new SbVec3f();

    getSize(br, h);

    min.setValue(-br[0], -h[0], -br[0]);

    if (HAS_PART(curParts, Part.SIDES))
      max.setValue(br[0],  h[0],  br[0]);
    else
      max.setValue(br[0], -h[0],  br[0]);

    box.setBounds(min, max);
  }

  center.setValue(0.0f, 0.0f, 0.0f);
    	
    }
    
////////////////////////////////////////////////////////////////////////
//
//Description:
//Destructor
//
//Use: private

    public void destructor() {
    	super.destructor();
    }

    private static SbVec2f[]      coordsArray;   //!< Storage for base coords
    private static SbVec3f[]      normalsArray;  //!< Storage for base normals
    private static int          maxCoords;      //!< Current max num of coords/normals

    //! The state of the last cone tesselation,
    //! so that the VBO can be reused if nothing changed.
    private class CacheState extends SimpleVertexArrayCache {
      CacheState() {
        curParts = 0;
        numSides = 0;
        numSections = 0;
      }
      int curParts, numSides, numSections;
      final SbVec3f scale = new SbVec3f();
    };

    private final CacheState _cache = new CacheState();

////////////////////////////////////////////////////////////////////////
//
//Description:
//Computes number of sides per circular cross-section and number
//of sections, based on complexity, then computes ring of x,z
//coordinates around base of cone and stores in baseCoords. It
//computes and stores normals in sideNormals, too.
//
//Use: private

    //! Computes number of sides and sections to use to represent
    //! cone (based on complexity), then computes ring of x,z
    //! coordinates around base of cone and stores in baseCoords. It
    //! computes and stores normals in sideNormals, too.
    void                computeBase(SoAction action,
                                    final int[] numSides, final int[] numSections,
                                    final SbVec2f[][] baseCoords,
                                    final SbVec3f[][] sideNormals)  {
  float       complexity = SoComplexityElement.get(action.getState());
  float       theta, dTheta, cosTheta, sinTheta, t1, t2;
  int         side;

  final float[]       radius = new float[1], halfHeight = new float[1];
  getSize(radius, halfHeight);
  float       height = 2*halfHeight[0];

  // In object space, just base number of divisions on complexity
  if (SoComplexityTypeElement.get(action.getState()) ==
    SoComplexityTypeElement.Type.OBJECT_SPACE) {

      // If complexity is between 0 and .5 (inclusive), use 4 sections
      // and between 32 and 64 sides:
      if (complexity <= 0.5) {
        numSections[0] = 4;
        numSides[0]    = (int) (complexity * 64.0f + 32.0f);
      }

      // If complexity is between .5 and 1, use between 4 and 16 sections
      // and between 64 and 192 sides:
      else {
        numSections[0] = (int) (24.0f * complexity - 8.0f);
        numSides[0]    = (int) (complexity * 256.0f - 64f);
      }
  }

  // In screen space, set the number of sides/sections based on the
  // complexity and the size of the cone when projected onto the screen.
  else {
    final SbVec2s         rectSize = new SbVec2s();
    short           maxSize;

    final SbVec3f         p = new SbVec3f(radius[0], halfHeight[0], radius[0]);

    getScreenSize(action.getState(), new SbBox3f(p.operator_minus(), p), rectSize);

    maxSize = (rectSize.getValue()[0] > rectSize.getValue()[1] ? rectSize.getValue()[0] : rectSize.getValue()[1]);

    numSections[0] = 1 + (int) (0.2f  * complexity * maxSize);
    numSides[0]    = 3 + (int) (0.25f * complexity * maxSize);
  }

  if (sections.getValue() > 0) {
    numSections[0] = sections.getValue();
  }
  if (sides.getValue() > 0) {
    numSides[0] = sides.getValue();
  }

  // Make sure the current storage for base coordinates is big enough
  if (numSides[0] > maxCoords) {

    if (maxCoords > 0) {
    	// no delete in java
      //delete [] coordsArray;
      //delete [] normalsArray;
    }

    maxCoords = numSides[0];

    coordsArray  = SbVec2f.allocate(maxCoords);
    normalsArray = SbVec3f.allocate(maxCoords);
  }

  baseCoords[0]  = coordsArray;
  sideNormals[0] = normalsArray;

  // Compute x and z coordinates around base
  theta  = 0.0f;
  dTheta = 2.0f * (float)Math.PI / numSides[0];

  // Looking at the XY silhouette of the cone, (t1,t2) is the normal
  // in the XY plane.
  t1 = radius[0] / (float)Math.sqrt(radius[0]*radius[0] + height*height);
  t2 = height / (float)Math.sqrt(radius[0]*radius[0] + height*height);

  for (side = 0; side < numSides[0]; side++) {
    cosTheta = (float)Math.cos(theta);
    sinTheta = (float)Math.sin(theta);

    // Theta == 0 generates a point down the -Z axis, which
    // explains the weird (sinTheta, -cosTheta)...
    baseCoords[0][side].setValue(sinTheta, -cosTheta);
    sideNormals[0][side].setValue(t2 * sinTheta, t1, -t2 * cosTheta);
    theta += dTheta;
  }
    	
    }

////////////////////////////////////////////////////////////////////////
//
//Description:
//Computes real bottom radius, half-height.
//
//Use: private

    //! Computes real radius and half-height
    private void                getSize(final float[] rad, final float[] hHeight)  {
    	  rad[0]  = (bottomRadius.isIgnored() ? 1.0f : bottomRadius.getValue());
    	  hHeight[0] = (      height.isIgnored() ? 1.0f :       height.getValue() / 2.0f);
    }
    
    //
 // Macro to multiply out coordinates to avoid extra GL calls:
 //
 private final SbVec3f SCALE(SbVec3f pt, SbVec3f scale, SbVec3f tmp){
	 tmp.setValue(0, (pt).getValueRead()[0]*scale.getValueRead()[0]);
	 tmp.setValue(1, (pt).getValueRead()[1]*scale.getValueRead()[1]);
	 tmp.setValue(2, (pt).getValueRead()[2]*scale.getValueRead()[2]);
	 return tmp;
 }
    

////////////////////////////////////////////////////////////////////////
//
//Description:
//Generic rendering of cone with or without normals, with or
//without texture coordinates.
//
//Use: private

    //! These render the cone
    private void                GLRenderGeneric(SoGLRenderAction action,
                                        boolean sendNormals, boolean doTextures) {
    final SbVec3fSingle scale = new SbVec3fSingle(), tmp = new SbVec3fSingle();
    final float[] s1 = new float[1], s2 = new float[1];
    getSize(s1, s2);
    scale.getValue()[0] = s1[0];
    scale.getValue()[1] = s2[0];
    scale.getValue()[2] = scale.getValueRead()[0];

    boolean              materialPerPart;
    int                 curParts, side, section;
    final int[]	numSides = new int[1], numSections = new int[1];
    float               yTop, yBot, dy;
    float               s, ds, tTop, tBot, dt;
    float               outerRadius, innerRadius, dRadius;
    final SbVec2f[][]             baseCoords = new SbVec2f[1][];
    final SbVec3f[][]             sideNormals = new SbVec3f[1][];
    final SbVec3fSingle pt = new SbVec3fSingle(), norm = new SbVec3fSingle();
    final SoMaterialBundle    mb = new SoMaterialBundle(action);

    SoMaterialBindingElement.Binding mbe =
	SoMaterialBindingElement.get(action.getState());
    materialPerPart =
	(mbe == SoMaterialBindingElement.Binding.PER_PART_INDEXED ||
	 mbe == SoMaterialBindingElement.Binding.PER_PART);

    curParts = (parts.isIgnored() ? Part.ALL.getValue() : parts.getValue());

    // Compute number of sides and sections to use to represent
    // cone, then compute ring of x,z coordinates around cone
    // and store in baseCoords.
    computeBase(action, numSides, numSections, baseCoords, sideNormals);

    // Make sure first material is sent if necessary
    mb.sendFirst();

    dRadius = 1.0f / numSections[0];

	GL2 gl2 = action.getCacheContext();

    if (HAS_PART(curParts, Part.SIDES)) {

	// Draw each section of sides as a triangle mesh, from top to bottom
	yTop = 1.0f;
	dy   = -2.0f / numSections[0];
	tTop = 1.0f;
	dt   = -1.0f / numSections[0];
	ds   =  1.0f / numSides[0];

	innerRadius = 0.0f;
	
	for (section = 0; section < numSections[0]; section++) {

	    outerRadius = innerRadius + dRadius;
	    yBot = yTop + dy;

	    tBot = tTop + dt;
	    s    = 0.0f;

	    gl2.glBegin(GL2.GL_TRIANGLE_STRIP);

	    for (side = 0; side < numSides[0]; side++) {
		// Deal with normal
		if (sendNormals)
		    gl2.glNormal3fv(sideNormals[0][side].getValueGL());

		// Point at bottom of section
		pt.setValue(outerRadius * baseCoords[0][side].getValue()[0], yBot,
			    outerRadius * baseCoords[0][side].getValue()[1]);
		if (doTextures)
		    gl2.glTexCoord2f(s, tBot);
		gl2.glVertex3fv(SCALE(pt,scale,tmp).getValueGL());

		// Point at top of section
		pt.setValue(innerRadius * baseCoords[0][side].getValue()[0], yTop,
			    innerRadius * baseCoords[0][side].getValue()[1]);
		if (doTextures)
		    gl2.glTexCoord2f(s, tTop);
		gl2.glVertex3fv(SCALE(pt,scale,tmp).getValueGL());
		s += ds;
	    }

	    // Join end of strip back to beginning
	    side = 0;
	    s = 1.0f;
	    // Deal with normal
	    if (sendNormals)
		gl2.glNormal3fv(sideNormals[0][side].getValueGL());

	    // Point at bottom of section
	    pt.setValue(outerRadius * baseCoords[0][side].getValue()[0], yBot,
			outerRadius * baseCoords[0][side].getValue()[1]);
	    if (doTextures)
		gl2.glTexCoord2f(s, tBot);
	    gl2.glVertex3fv(SCALE(pt,scale,tmp).getValueGL());

	    // Point at top of section
	    pt.setValue(innerRadius * baseCoords[0][side].getValue()[0], yTop,
			innerRadius * baseCoords[0][side].getValue()[1]);
	    if (doTextures)
		gl2.glTexCoord2f(s, tTop);
	    gl2.glVertex3fv(SCALE(pt,scale,tmp).getValueGL());
	    s += ds;

	    gl2.glEnd();

	    // Prepare for next section down
	    innerRadius = outerRadius;
	    yTop = yBot;
	    tTop = tBot;
	}
    }

    // Draw bottom face as a series of concentric rings. The number of
    // rings is the same as the number of sections of the sides of the
    // cone.
    if (HAS_PART(curParts, Part.BOTTOM)) {
	norm.setValue(0.0f, -1.0f, 0.0f);
	pt.getValue()[1] = -1.0f;

	if (materialPerPart)
	    mb.send(1, false);
	if (sendNormals)
	    gl2.glNormal3fv(norm.getValueGL());

	// Start at the outside and work in
	outerRadius = 1.0f;
	for (section = numSections[0] - 1; section >= 0; --section) {

	    innerRadius = outerRadius - dRadius;

	    // Innermost ring is drawn as a triangle fan. This not
	    // only gets better shading (because the center vertex is
	    // sent), but also avoids the problem of having a polygon
	    // with too many vertices.
	    if (section == 0) {
		gl2.glBegin(GL2.GL_TRIANGLE_FAN);

		// Center point comes first
		pt.getValue()[0] = pt.getValue()[2] = 0.0f;
		if (doTextures)
		    gl2.glTexCoord2f(0.5f, 0.5f);
		gl2.glVertex3fv(SCALE(pt,scale,tmp).getValueGL());

		// Send all vertices around ring
		for (side = 0; side < numSides[0]; side++) {
		    pt.getValue()[0] = outerRadius * baseCoords[0][side].getValue()[0];
		    pt.getValue()[2] = outerRadius * baseCoords[0][side].getValue()[1];
		    if (doTextures)
			gl2.glTexCoord2f(BOT_TEX_S(pt.getValueRead()[0]), BOT_TEX_T(pt.getValueRead()[2]));
		    gl2.glVertex3fv(SCALE(pt,scale,tmp).getValueGL());
		}
		// Send first vertex again
		pt.getValue()[0] = outerRadius * baseCoords[0][0].getValue()[0];
		pt.getValue()[2] = outerRadius * baseCoords[0][0].getValue()[1];
		if (doTextures)
		    gl2.glTexCoord2f(BOT_TEX_S(pt.getValueRead()[0]), BOT_TEX_T(pt.getValueRead()[2]));
		gl2.glVertex3fv(SCALE(pt,scale,tmp).getValueGL());

		gl2.glEnd();
	    }

	    // Other rings are triangle strips
	    else {
		gl2.glBegin(GL2.GL_TRIANGLE_STRIP);

		// Go in reverse order so that vertex ordering is correct
		for (side = numSides[0] - 1; side >= 0; side--) {
		    // Send points on outer and inner rings
		    pt.getValue()[0] = outerRadius * baseCoords[0][side].getValue()[0];
		    pt.getValue()[2] = outerRadius * baseCoords[0][side].getValue()[1];
		    if (doTextures)
			gl2.glTexCoord2f(BOT_TEX_S(pt.getValueRead()[0]), BOT_TEX_T(pt.getValueRead()[2]));
		    gl2.glVertex3fv(SCALE(pt,scale,tmp).getValueGL());
		    pt.getValue()[0] = innerRadius * baseCoords[0][side].getValue()[0];
		    pt.getValue()[2] = innerRadius * baseCoords[0][side].getValue()[1];
		    if (doTextures)
			gl2.glTexCoord2f(BOT_TEX_S(pt.getValueRead()[0]), BOT_TEX_T(pt.getValueRead()[2]));
		    gl2.glVertex3fv(SCALE(pt,scale,tmp).getValueGL());
		}

		// Join end of strip back to beginning
		side = numSides[0] - 1;
		pt.getValue()[0] = outerRadius * baseCoords[0][side].getValue()[0];
		pt.getValue()[2] = outerRadius * baseCoords[0][side].getValue()[1];
		if (doTextures)
		    gl2.glTexCoord2f(BOT_TEX_S(pt.getValueRead()[0]), BOT_TEX_T(pt.getValueRead()[2]));
		gl2.glVertex3fv(SCALE(pt,scale,tmp).getValueGL());
		pt.getValue()[0] = innerRadius * baseCoords[0][side].getValue()[0];
		pt.getValue()[2] = innerRadius * baseCoords[0][side].getValue()[1];
		if (doTextures)
		    gl2.glTexCoord2f(BOT_TEX_S(pt.getValueRead()[0]), BOT_TEX_T(pt.getValueRead()[2]));
		gl2.glVertex3fv(SCALE(pt,scale,tmp).getValueGL());

		gl2.glEnd();

		// Prepare for next ring
		outerRadius = innerRadius;
	    }
	}
    }
    mb.destructor(); //java port
    }
    
////////////////////////////////////////////////////////////////////////
//
//Description:
//Renders cone with normals and without texture coordinates.
//
//Use: private

    private void                GLRenderNvertTnone(SoGLRenderAction action) {
    	    final SbVec3fSingle scale = new SbVec3fSingle(), tmp = new SbVec3fSingle();
    	    final float[] s1 = new float[1], s2 = new float[1];
    getSize(s1, s2);
    scale.getValue()[0] = s1[0];
    scale.getValue()[1] = s2[0];
    scale.getValue()[2] = scale.getValueRead()[0];

    boolean              materialPerPart;
    int                 curParts, side, section;
    final int[] numSides = new int[1], numSections = new int[1];
    float               yTop, yBot, dy;
    float               outerRadius, innerRadius, dRadius;
    final SbVec2f[][]             baseCoords = new SbVec2f[1][];
    final SbVec3f[][]             sideNormals = new SbVec3f[1][];
    final SbVec3fSingle pt = new SbVec3fSingle(), norm = new SbVec3fSingle();
    final SoMaterialBundle    mb = new SoMaterialBundle(action);

    SoMaterialBindingElement.Binding mbe =
	SoMaterialBindingElement.get(action.getState());
    materialPerPart =
	(mbe == SoMaterialBindingElement.Binding.PER_PART_INDEXED ||
	 mbe == SoMaterialBindingElement.Binding.PER_PART);

    curParts = (parts.isIgnored() ? Part.ALL.getValue() : parts.getValue());

    // Compute number of sides and sections to use to represent
    // cone, then compute ring of x,z coordinates around cone
    // and store in baseCoords.
    computeBase(action, numSides, numSections, baseCoords, sideNormals);

    // Make sure first material is sent if necessary
    mb.sendFirst();

    dRadius = 1.0f / numSections[0];

	GL2 gl2 = action.getCacheContext();

    if (HAS_PART(curParts, Part.SIDES)) {

	// Draw each section of sides as a triangle mesh, from top to bottom
	yTop = 1.0f;
	dy   = -2.0f / numSections[0];

	innerRadius = 0.0f;

	for (section = 0; section < numSections[0]; section++) {

	    outerRadius = innerRadius + dRadius;
	    yBot = yTop + dy;


	    gl2.glBegin(GL2.GL_TRIANGLE_STRIP);

	    for (side = 0; side < numSides[0]; side++) {
		// Deal with normal
		gl2.glNormal3fv(sideNormals[0][side].getValueGL());

		// Point at bottom of section
		pt.setValue(outerRadius * baseCoords[0][side].getValue()[0], yBot,
			    outerRadius * baseCoords[0][side].getValue()[1]);
		gl2.glVertex3fv(SCALE(pt,scale,tmp).getValueGL());

		// Point at top of section
		pt.setValue(innerRadius * baseCoords[0][side].getValue()[0], yTop,
			    innerRadius * baseCoords[0][side].getValue()[1]);
		gl2.glVertex3fv(SCALE(pt,scale,tmp).getValueGL());
	    }

	    // Join end of strip back to beginning
	    side = 0;
	    // Deal with normal
	    gl2.glNormal3fv(sideNormals[0][side].getValueGL());

	    // Point at bottom of section
	    pt.setValue(outerRadius * baseCoords[0][side].getValue()[0], yBot,
			outerRadius * baseCoords[0][side].getValue()[1]);
	    gl2.glVertex3fv(SCALE(pt,scale,tmp).getValueGL());

	    // Point at top of section
	    pt.setValue(innerRadius * baseCoords[0][side].getValue()[0], yTop,
			innerRadius * baseCoords[0][side].getValue()[1]);
	    gl2.glVertex3fv(SCALE(pt,scale,tmp).getValueGL());

	    gl2.glEnd();

	    // Prepare for next section down
	    innerRadius = outerRadius;
	    yTop = yBot;
	}
    }

    // Draw bottom face as a series of concentric rings. The number of
    // rings is the same as the number of sections of the sides of the
    // cone.
    if (HAS_PART(curParts, Part.BOTTOM)) {
	norm.setValue(0.0f, -1.0f, 0.0f);
	pt.getValue()[1] = -1.0f;

	if (materialPerPart)
	    mb.send(1, false);
	gl2.glNormal3fv(norm.getValueGL());

	// Start at the outside and work in
	outerRadius = 1.0f;
	for (section = numSections[0] - 1; section >= 0; --section) {

	    innerRadius = outerRadius - dRadius;

	    // Innermost ring is drawn as a triangle fan. This not
	    // only gets better shading (because the center vertex is
	    // sent), but also avoids the problem of having a polygon
	    // with too many vertices.
	    if (section == 0) {
		gl2.glBegin(GL2.GL_TRIANGLE_FAN);

		// Center point comes first
		pt.getValue()[0] = pt.getValue()[2] = 0.0f;
		gl2.glVertex3fv(SCALE(pt,scale,tmp).getValueGL());

		// Send all vertices around ring
		for (side = 0; side < numSides[0]; side++) {
		    pt.getValue()[0] = outerRadius * baseCoords[0][side].getValue()[0];
		    pt.getValue()[2] = outerRadius * baseCoords[0][side].getValue()[1];
		    gl2.glVertex3fv(SCALE(pt,scale,tmp).getValueGL());
		}
		// Send first vertex again
		pt.getValue()[0] = outerRadius * baseCoords[0][0].getValue()[0];
		pt.getValue()[2] = outerRadius * baseCoords[0][0].getValue()[1];
		gl2.glVertex3fv(SCALE(pt,scale,tmp).getValueGL());

		gl2.glEnd();
	    }

	    // Other rings are triangle strips
	    else {
		gl2.glBegin(GL2.GL_TRIANGLE_STRIP);

		// Go in reverse order so that vertex ordering is correct
		for (side = numSides[0] - 1; side >= 0; side--) {
		    // Send points on outer and inner rings
		    pt.getValue()[0] = outerRadius * baseCoords[0][side].getValue()[0];
		    pt.getValue()[2] = outerRadius * baseCoords[0][side].getValue()[1];
		    gl2.glVertex3fv(SCALE(pt,scale,tmp).getValueGL());
		    pt.getValue()[0] = innerRadius * baseCoords[0][side].getValue()[0];
		    pt.getValue()[2] = innerRadius * baseCoords[0][side].getValue()[1];
		    gl2.glVertex3fv(SCALE(pt,scale,tmp).getValueGL());
		}

		// Join end of strip back to beginning
		side = numSides[0] - 1;
		pt.getValue()[0] = outerRadius * baseCoords[0][side].getValue()[0];
		pt.getValue()[2] = outerRadius * baseCoords[0][side].getValue()[1];
		gl2.glVertex3fv(SCALE(pt,scale,tmp).getValueGL());
		pt.getValue()[0] = innerRadius * baseCoords[0][side].getValue()[0];
		pt.getValue()[2] = innerRadius * baseCoords[0][side].getValue()[1];
		gl2.glVertex3fv(SCALE(pt,scale,tmp).getValueGL());

		gl2.glEnd();

		// Prepare for next ring
		outerRadius = innerRadius;
	    }
	}
    }
    mb.destructor(); // java port
    }


    private void                GLRenderVertexArray(SoGLRenderAction action,
                                            boolean sendNormals, boolean doTextures) {
    	  SoState state = action.getState();
  final SbVec3fSingle scale = new SbVec3fSingle(), tmp = new SbVec3fSingle();
  final float[] s1 = new float[1], s2 = new float[1];
  getSize(s1, s2);
  scale.getValue()[0] = s1[0];
  scale.getValue()[1] = s2[0];
  scale.getValue()[2] = scale.getValueRead()[0];

  int                 curParts, side, section;
  final int[] numSides = new int[1], numSections = new int[1];
  float               yTop, yBot, dy;
  float               s, ds, tTop, tBot, dt;
  float               outerRadius, innerRadius, dRadius;
  final SbVec2f[][]             baseCoords = new SbVec2f[1][];
  final SbVec3f[][]             sideNormals = new SbVec3f[1][];
  final SbVec3fSingle pt = new SbVec3fSingle(), norm = new SbVec3fSingle();
  final SoMaterialBundle    mb = new SoMaterialBundle(action);

  curParts = (parts.isIgnored() ? Part.ALL.getValue() : parts.getValue());

  // Compute number of sides and sections to use to represent
  // cone, then compute ring of x,z coordinates around cone
  // and store in baseCoords.
  computeBase(action, numSides, numSections, baseCoords, sideNormals);

  // Make sure first material is sent if necessary
  mb.sendFirst();

  boolean cacheValid = 
    _cache.scale.operator_equal_equal(scale) &&
    _cache.curParts == curParts &&
    _cache.numSides == numSides[0] &&
    _cache.numSections == numSections[0] &&
    _cache.useNormals == sendNormals &&
    _cache.useTexCoords == doTextures &&
    _cache.vbo.isValid(state);
  
	GL2 gl2 = action.getCacheContext();

  if (!cacheValid) {
    _cache.scale.copyFrom(scale);
    _cache.curParts = curParts;
    _cache.numSides = numSides[0];
    _cache.numSections = numSections[0];
    _cache.useNormals = sendNormals;
    _cache.useTexCoords = doTextures;

    List<Integer> indices = new ArrayList<Integer>();
    VectorOfSbVec3f points = new VectorOfSbVec3f();
    VectorOfSbVec3f normals = new VectorOfSbVec3f();
    List<SbVec2f> texCoords = new ArrayList<SbVec2f>();
    int currentIndex = 0;
    boolean winding;

    dRadius = 1.0f / numSections[0];

    SbVec3f currentNormal = null;

    if (HAS_PART(curParts, Part.SIDES)) {

      // Draw each section of sides as a triangle mesh, from top to bottom
      yTop = 1.0f;
      dy   = -2.0f / numSections[0];
      tTop = 1.0f;
      dt   = -1.0f / numSections[0];
      ds   =  1.0f / numSides[0];

      innerRadius = 0.0f;

      for (section = 0; section < numSections[0]; section++) {

        outerRadius = innerRadius + dRadius;
        yBot = yTop + dy;

        tBot = tTop + dt;
        s    = 0.0f;

        winding = false;
        for (side = 0; side < numSides[0]; side++) {
          // Deal with normal
          if (sendNormals)
            currentNormal = sideNormals[0][side];

          // Point at bottom of section
          pt.setValue(outerRadius * baseCoords[0][side].getValue()[0], yBot,
            outerRadius * baseCoords[0][side].getValue()[1]);
          if (doTextures)
            texCoords.add(new SbVec2f(s, tBot));
          if (sendNormals) { normals.add(currentNormal); }
          points.add(SCALE(pt,scale,tmp));
          if (side>0) {
            winding = ADD_TRIANGLE( points, indices, winding);
          }

          // Point at top of section
          pt.setValue(innerRadius * baseCoords[0][side].getValue()[0], yTop,
            innerRadius * baseCoords[0][side].getValue()[1]);
          if (doTextures)
            texCoords.add(new SbVec2f(s, tTop));
          if (sendNormals) { normals.add(currentNormal); }
          points.add(SCALE(pt,scale,tmp));
          if (side>0) {
            winding = ADD_TRIANGLE( points, indices, winding);
          }
          s += ds;
        }

        // Join end of strip back to beginning
        side = 0;
        s = 1.0f;
        // Deal with normal
        if (sendNormals)
          currentNormal = sideNormals[0][side];

        // Point at bottom of section
        pt.setValue(outerRadius * baseCoords[0][side].getValue()[0], yBot,
          outerRadius * baseCoords[0][side].getValue()[1]);
        if (doTextures)
          texCoords.add(new SbVec2f(s, tBot));
        if (sendNormals) { normals.add(currentNormal); }
        points.add(SCALE(pt,scale,tmp));
        winding = ADD_TRIANGLE( points, indices, winding);

        // Point at top of section
        pt.setValue(innerRadius * baseCoords[0][side].getValue()[0], yTop,
        innerRadius * baseCoords[0][side].getValue()[1]);
        if (doTextures)
          texCoords.add(new SbVec2f(s, tTop));
        if (sendNormals) { normals.add(currentNormal); }
        points.add(SCALE(pt,scale,tmp));
        s += ds;
        winding = ADD_TRIANGLE( points, indices, winding);

        // Prepare for next section down
        innerRadius = outerRadius;
        yTop = yBot;
        tTop = tBot;
      }
    }

    // Draw bottom face as a series of concentric rings. The number of
    // rings is the same as the number of sections of the sides of the
    // cone.
    if (HAS_PART(curParts, Part.BOTTOM)) {
      norm.setValue(0.0f, -1.0f, 0.0f);
      pt.getValue()[1] = -1.0f;

      if (sendNormals)
        currentNormal = norm;

      // Start at the outside and work in
      outerRadius = 1.0f;
      for (section = numSections[0] - 1; section >= 0; --section) {

        innerRadius = outerRadius - dRadius;

        // Innermost ring is drawn as a triangle fan. This not
        // only gets better shading (because the center vertex is
        // sent), but also avoids the problem of having a polygon
        // with too many vertices.
        if (section == 0) {
          // Center point comes first
          pt.getValue()[0] = pt.getValue()[2] = 0.0f;
          if (doTextures)
            texCoords.add(new SbVec2f(0.5f, 0.5f));
          if (sendNormals) { normals.add(currentNormal); }
          points.add(SCALE(pt,scale,tmp));
          int centerIndex = (int)points.size()-1;

          // Send all vertices around ring
          for (side = 0; side < numSides[0]; side++) {
            pt.getValue()[0] = outerRadius * baseCoords[0][side].getValue()[0];
            pt.getValue()[2] = outerRadius * baseCoords[0][side].getValue()[1];
            if (doTextures)
              texCoords.add(new SbVec2f(BOT_TEX_S(pt.getValueRead()[0]), BOT_TEX_T(pt.getValueRead()[2])));
            if (sendNormals) { normals.add(currentNormal); }
            points.add(SCALE(pt,scale,tmp));
            if (side>0) {
              ADD_CENTER_TRIANGLE( points, indices, centerIndex);
            }
          }
          // Send first vertex again
          pt.getValue()[0] = outerRadius * baseCoords[0][0].getValue()[0];
          pt.getValue()[2] = outerRadius * baseCoords[0][0].getValue()[1];
          if (doTextures)
            texCoords.add(new SbVec2f(BOT_TEX_S(pt.getValueRead()[0]), BOT_TEX_T(pt.getValueRead()[2])));
          if (sendNormals) { normals.add(currentNormal); }
          points.add(SCALE(pt,scale,tmp));
          ADD_CENTER_TRIANGLE( points, indices, centerIndex);
        }

        // Other rings are triangle strips
        else {
          winding = false;

          // Go in reverse order so that vertex ordering is correct
          for (side = numSides[0] - 1; side >= 0; side--) {
            // Send points on outer and inner rings
            pt.getValue()[0] = outerRadius * baseCoords[0][side].getValue()[0];
            pt.getValue()[2] = outerRadius * baseCoords[0][side].getValue()[1];
            if (doTextures)
              texCoords.add(new SbVec2f(BOT_TEX_S(pt.getValueRead()[0]), BOT_TEX_T(pt.getValueRead()[2])));
            if (sendNormals) { normals.add(currentNormal); }
            points.add(SCALE(pt,scale,tmp));
            if (side<numSides[0] - 1) {
              winding = ADD_TRIANGLE( points, indices, winding);
            }

            pt.getValue()[0] = innerRadius * baseCoords[0][side].getValue()[0];
            pt.getValue()[2] = innerRadius * baseCoords[0][side].getValue()[1];
            if (doTextures)
              texCoords.add(new SbVec2f(BOT_TEX_S(pt.getValueRead()[0]), BOT_TEX_T(pt.getValueRead()[2])));
            if (sendNormals) { normals.add(currentNormal); }
            points.add(SCALE(pt,scale,tmp));
            if (side<numSides[0] - 1) {
              winding = ADD_TRIANGLE( points, indices, winding);
            }
          }

          // Join end of strip back to beginning
          side = numSides[0] - 1;
          pt.getValue()[0] = outerRadius * baseCoords[0][side].getValue()[0];
          pt.getValue()[2] = outerRadius * baseCoords[0][side].getValue()[1];
          if (doTextures)
            texCoords.add(new SbVec2f(BOT_TEX_S(pt.getValueRead()[0]), BOT_TEX_T(pt.getValueRead()[2])));
          if (sendNormals) { normals.add(currentNormal); }
          points.add(SCALE(pt,scale,tmp));
          winding = ADD_TRIANGLE( points, indices, winding);

          pt.getValue()[0] = innerRadius * baseCoords[0][side].getValue()[0];
          pt.getValue()[2] = innerRadius * baseCoords[0][side].getValue()[1];
          if (doTextures)
            texCoords.add(new SbVec2f(BOT_TEX_S(pt.getValueRead()[0]), BOT_TEX_T(pt.getValueRead()[2])));
          if (sendNormals) { normals.add(currentNormal); }
          points.add(SCALE(pt,scale,tmp));
          winding = ADD_TRIANGLE( points, indices, winding);

          // Prepare for next ring
          outerRadius = innerRadius;
        }
      }
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

    for (int i = 0;i<numVertices;i++) {
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
        texCoordsPtr.asterisk( texCoord.getValue()[0]); texCoordsPtr.plusPlus();
        texCoordsPtr.asterisk( texCoord.getValue()[1]); texCoordsPtr.plusPlus();
      }
    }

    _cache.vbo.freeGL(state);
    _cache.vbo.setData(numBytes, VoidPtr.create(data.getBuffer()), 0, state);
    // force upload
    _cache.vbo.bind(state);
    _cache.vertexOffset = vertexOffset.minus(data);
    _cache.normalOffset = normalOffset.minus(data);
    _cache.texCoordOffset = texCoordOffset.minus(data);
    _cache.numVertices = numVertices;

    data.destructor();
  }

  _cache.vbo.bind(state);
  _cache.drawArrays(this, action, GL2.GL_TRIANGLES);
  _cache.vbo.unbind(gl2);

  mb.destructor(); // java port
    }

////////////////////////////////////////////////////////////////////////
//
//Description:
//Computes intersection of given ray with an infinite cone with
//its apex at (0,0,0) and a cross-section of radius 1 at y = -1.
//The equation of this cone is   x*x - y*y + z*z = 0. This returns
//FALSE if no intersection was found. Otherwise, it fills in
//enterPoint and exitPoint with the two intersections, ordered by
//distance from the start of the ray.
//
//Use: private

    //! Computes intersection of ray with infinite canonical cone
    private boolean                intersectInfiniteCone(final SbLine ray,
                                              final SbVec3f enterPoint,
                                              final SbVec3f exitPoint)  {
    	  final SbVec3f       pos = ray.getPosition();
  final SbVec3f       dir = ray.getDirection();
  float               a, b, c, discriminant, sqroot, t0, t1;

  // The equation of the ray is I = pos + t * dir, where "pos" is the
  // starting position and "dir" is the direction.

  // Substituting the intersection point "I" into the equation of
  // the cone gives us a quadratic, whose a, b, and c coefficients
  // are as follows
  a =  dir.getValueRead()[0] * dir.getValueRead()[0] - dir.getValueRead()[1] * dir.getValueRead()[1] + dir.getValueRead()[2] * dir.getValueRead()[2];
  b = (pos.getValueRead()[0] * dir.getValueRead()[0] - pos.getValueRead()[1] * dir.getValueRead()[1] + pos.getValueRead()[2] * dir.getValueRead()[2]) * 2.0f;
  c =  pos.getValueRead()[0] * pos.getValueRead()[0] - pos.getValueRead()[1] * pos.getValueRead()[1] + pos.getValueRead()[2] * pos.getValueRead()[2];

  // If the discriminant of the quadratic is negative, there's no
  // intersection
  discriminant = b * b - 4.0f * a * c;
  if (discriminant < 0.0)
    return false;

  sqroot = (float)Math.sqrt(discriminant);

  // Some Magic to stabilize the answer
  if (b > 0.0) {
    t0 = -(2.0f * c) / (sqroot + b);
    t1 = -(sqroot + b) / (2.0f * a);
  }
  else {
    t0 = (2.0f * c) / (sqroot - b);
    t1 = (sqroot - b) / (2.0f * a);
  }       

  enterPoint.copyFrom(pos.operator_add(dir.operator_mul(t0)));
  exitPoint.copyFrom(pos.operator_add(dir.operator_mul(t1)));

  return true;
    	
    }
    
    private final boolean ADD_TRIANGLE(VectorOfSbVec3f points, List<Integer> indices, boolean winding) {
	    int currentIndex = (int)points.size(); 
	    indices.add(currentIndex-3); 
	    indices.add(winding ? currentIndex-1 : currentIndex - 2); 
	    indices.add(winding ? currentIndex-2 : currentIndex - 1); 
	    winding = !winding;
    return winding;
    }
    
    private final void ADD_CENTER_TRIANGLE(VectorOfSbVec3f points, List<Integer> indices, int centerIndex) {
	    int currentIndex = (int)points.size(); 
	    indices.add(currentIndex-1); 
	    indices.add(centerIndex); 
	    indices.add(currentIndex-2);
    }
    
};

	