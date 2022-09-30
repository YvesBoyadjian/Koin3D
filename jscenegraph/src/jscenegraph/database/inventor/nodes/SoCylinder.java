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
 |      This file defines the SoCylinder node class.
 |
 |   Author(s)          : Paul S. Strauss, Nick Thompson
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import java.util.ArrayList;
import java.util.List;

import jscenegraph.opengl.GL2;

import jscenegraph.coin3d.inventor.elements.SoGLMultiTextureEnabledElement;
import jscenegraph.coin3d.inventor.elements.SoMultiTextureCoordinateElement;
import jscenegraph.coin3d.inventor.elements.SoMultiTextureEnabledElement;
import jscenegraph.coin3d.misc.SoGL;
import jscenegraph.coin3d.misc.SoPick;
import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec3fSingle;
import jscenegraph.database.inventor.SbVec4fSingle;
import jscenegraph.database.inventor.SoPrimitiveVertex;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoRayPickAction;
import jscenegraph.database.inventor.bundles.SoMaterialBundle;
import jscenegraph.database.inventor.details.SoCylinderDetail;
import jscenegraph.database.inventor.elements.SoComplexityElement;
import jscenegraph.database.inventor.elements.SoComplexityTypeElement;
import jscenegraph.database.inventor.elements.SoMaterialBindingElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFBitMask;
import jscenegraph.database.inventor.fields.SoSFFloat;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.port.CharPtr;
import jscenegraph.port.Ctx;
import jscenegraph.port.FloatPtr;
import jscenegraph.port.VectorOfSbVec3f;
import jscenegraph.port.VoidPtr;


////////////////////////////////////////////////////////////////////////////////
//! Cylinder shape node.
/*!
\class SoCylinder
\ingroup Nodes
This node represents a simple capped cylinder centered around the
y-axis. By default, the cylinder is centered at (0,0,0) and has a
default size of -1 to +1 in all three dimensions.  You can use the
radius and height fields to create a cylinder with a different
size.


The cylinder is transformed by the current cumulative transformation
and is drawn with the current lighting model, drawing style, material,
and geometric complexity.


If the current material binding is <tt>PER_PART</tt> or
<tt>PER_PART_INDEXED</tt>, the first current material is used for the sides
of the cylinder, the second is used for the top, and the third is used
for the bottom. Otherwise, the first material is used for the entire
cylinder.


When a texture is applied to a cylinder, it is applied differently to
the sides, top, and bottom. On the sides, the texture wraps
counterclockwise (from above) starting at the back of the
cylinder. The texture has a vertical seam at the back, intersecting
the yz-plane. For the top and bottom, a circle is cut out of the
texture square and applied to the top or bottom circle. The top
texture appears right side up when the top of the cylinder is tilted
toward the camera, and the bottom texture appears right side up when
the top of the cylinder is tilted away from the camera.

\par File Format/Default
\par
\code
Cylinder {
  parts ALL
  radius 1
  height 2
}
\endcode

\par Action Behavior
\par
SoGLRenderAction
<BR> Draws cylinder based on the current coordinates, materials, drawing style, and so on. 
\par
SoRayPickAction
<BR> Intersects the ray with the cylinder. The part of the cylinder that was picked is available from the SoCylinderDetail. 
\par
SoGetBoundingBoxAction
<BR> Computes the bounding box that encloses the cylinder. 
\par
SoCallbackAction
<BR> If any triangle callbacks are registered with the action, they will be invoked for each successive triangle that approximates the cylinder. 

\par See Also
\par
SoCone, SoCube, SoCylinderDetail, SoSphere
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoCylinder extends SoShape {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoCylinder.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoCylinder.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoCylinder.class); }    	  	
	
	
	  private static final float CYL_SIDE_NUMTRIS =40.0f;


	public enum Part {
		SIDES(0x01),
		TOP(0x02),
		BOTTOM(0x04),
		ALL(0x07);
		
		private int value;
		
		private Part(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}

		public static Part fromValue(int value2) {
			switch(value2) {
			case 0x01: return SIDES;
			case 0x02: return TOP;
			case 0x04: return BOTTOM;
			case 0x07: return ALL;
			default:
				return null;
			}
		}
	}
	
    //! Visible parts of cylinder.
    public final SoSFBitMask         parts = new SoSFBitMask();          
    //! Radius in x and z dimensions
    public final SoSFFloat           radius = new SoSFFloat();         
    //! Size in y dimension
    public final SoSFFloat           height = new SoSFFloat();     
    
    //! Defines how many sides are used for tesselation. If set to <= 0, the sides are
    //! calculated by the SoComplexityElement.
    //public final SoSFInt32           sides = new SoSFInt32();

    //! Defines how many sections are used for tesselation. If set to <= 0, the sections are
    //! calculated by the SoComplexityElement.
    //public final SoSFInt32           sections = new SoSFInt32();
    //@}

    
      private
    static SbVec2f[]      coordsArray;   //!< Storage for ring coords
    private static int          maxCoords;      //!< Current max num of coords

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

    final CacheState _cache = new CacheState();

    
  
// Shorthand for testing whether current parts includes PART
private boolean HAS_PART(int PARTS, int PART) {
	return((PARTS & (PART)) != 0);
}

 
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoCylinder()
//
////////////////////////////////////////////////////////////////////////
{
  nodeHeader.SO_NODE_CONSTRUCTOR(/*SoCylinder.class*/);
  isBuiltIn = true;

  nodeHeader.SO_NODE_ADD_FIELD(parts,"parts",  (Part.ALL.getValue()));
  nodeHeader.SO_NODE_ADD_FIELD(radius,"radius", (1.0f));
  nodeHeader.SO_NODE_ADD_FIELD(height,"height", (2.0f));

  //nodeHeader.SO_NODE_ADD_FIELD(sides,"sides", (0));
  //nodeHeader.SO_NODE_ADD_FIELD(sections,"sections", (0));

  // Set up static info for enumerated type field
  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Part.SIDES);
  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Part.TOP);
  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Part.BOTTOM);
  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Part.ALL);

  // Set size of array to 0 so it will be allocated first time
  if (nodeHeader.SO_NODE_IS_FIRST_INSTANCE())
    maxCoords = 0;

  // Set up info in enumerated type field
  nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(parts,"parts", "Part");
}

    
    
  	// Turns on/off a part of the cylinder. (Convenience functions) 
	public void addPart(SoCylinder.Part part) {
		parts.setValue(parts.getValue() | part.value);
	}
	

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Turns off a part of the cylinder. (Convenience function.)
//
// Use: public

public void
removePart(SoCylinder.Part part)
//
////////////////////////////////////////////////////////////////////////
{
  parts.setValue(parts.getValue() & ~part.getValue());
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns whether a given part is on or off. (Convenience function.)
//
// Use: public

public boolean
hasPart(SoCylinder.Part part) 
//
////////////////////////////////////////////////////////////////////////
{
  return (parts.getValue() & part.getValue())!=0;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Performs GL rendering of a cylinder.
//
// Use: private

public void
GLRender(SoGLRenderAction action)
//
////////////////////////////////////////////////////////////////////////
{
	  if (!shouldGLRender(action)) return;

	  SoState state = action.getState();

	  int p = this.parts.getValue();
	  final SoMaterialBundle mb = new SoMaterialBundle(action);
	  mb.sendFirst();

	  boolean sendNormals = !mb.isColorOnly() ||
	    (SoMultiTextureCoordinateElement.getType(state) == SoMultiTextureCoordinateElement.CoordType.FUNCTION);

	  int flags = 0;
	  if (sendNormals)
	    flags |= SoGL.SOGL_NEED_NORMALS;
	  if (SoGLMultiTextureEnabledElement.get(state, 0)) {
	    if (SoGLMultiTextureEnabledElement.getMode(state, 0) ==
	        SoMultiTextureEnabledElement.Mode.TEXTURE3D) {
	      flags |= SoGL.SOGL_NEED_3DTEXCOORDS;
	    }
	    else {
	      flags |= SoGL.SOGL_NEED_TEXCOORDS;
	    }
	  }
	  if ((p & Part.SIDES.getValue())!=0) flags |= SoGL.SOGL_RENDER_SIDE;
	  if ((p & Part.TOP.getValue())!=0) flags |= SoGL.SOGL_RENDER_TOP;
	  if ((p & Part.BOTTOM.getValue())!=0) flags |= SoGL.SOGL_RENDER_BOTTOM;

	  SoMaterialBindingElement.Binding bind =
	    SoMaterialBindingElement.get(state);
	  if (bind == SoMaterialBindingElement.Binding.PER_PART ||
	      bind == SoMaterialBindingElement.Binding.PER_PART_INDEXED)
	    flags |= SoGL.SOGL_MATERIAL_PER_PART;

	  float complexity = this.getComplexityValue(action);

	  SoGL.sogl_render_cylinder(this.radius.getValue(),
	                       this.height.getValue(),
	                       (int)(CYL_SIDE_NUMTRIS * complexity),
	                       mb,
	                       flags, state);
	  mb.destructor();
	}

//{
//  // First see if the object is visible and should be rendered now
//  if (! shouldGLRender(action))
//    return;
//
//  // See if texturing is enabled
//  boolean doTextures = SoGLMultiTextureEnabledElement.get(action.getState(),0);
//
//  // Render the cylinder. The GLRenderGeneric() method handles any
//  // case. The GLRenderNvertTnone() handles the case where we are
//  // outputting normals but no texture coordinates. This case is
//  // handled separately since it occurs often and warrants its own
//  // method.
//  boolean sendNormals = (SoLightModelElement.get(action.getState()) !=
//    SoLightModelElement.Model.BASE_COLOR);
//  SoMaterialBindingElement.Binding mbe =
//    SoMaterialBindingElement.get(action.getState());
//  boolean materialPerPart =
//    (mbe == SoMaterialBindingElement.Binding.PER_PART_INDEXED ||
//    mbe == SoMaterialBindingElement.Binding.PER_PART);
//
//  if (!materialPerPart && SoVBO.isVertexArrayRenderingAllowed() && SoVBO.shouldUseVBO(action.getState(), SoVBO.getVboMinimumSizeLimit()+1 )) {
//    GLRenderVertexArray(action, sendNormals, doTextures);
//  } else {
//    if (! doTextures && sendNormals)
//      GLRenderNvertTnone(action);
//    else
//      GLRenderGeneric(action, sendNormals, doTextures);
//  }
//}

	
	

	@Override
	public void computeBBox(SoAction action, SbBox3f box, SbVec3f center) {
  int         curParts = (parts.isIgnored() ? Part.ALL.getValue() : parts.getValue());

  if (curParts == 0)          // No parts at all!
    box.setBounds(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);

  else {
    final float[]   r = new float[1], h = new float[1];
    final SbVec3f min = new SbVec3f(), max = new SbVec3f();

    getSize(r, h);

    if (HAS_PART(curParts, Part.SIDES.getValue() | Part.TOP.getValue()))
      max.setValue( r[0],  h[0],  r[0]);
    else
      max.setValue( r[0], -h[0],  r[0]);

    if (HAS_PART(curParts, Part.SIDES.getValue() | Part.BOTTOM.getValue()))
      min.setValue(-r[0], -h[0], -r[0]);
    else
      min.setValue(-r[0],  h[0], -r[0]);

    box.setBounds(min, max);
  }

  center.setValue(0.0f, 0.0f, 0.0f);
	}
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Computes real radius and half-height.
//
// Use: private

	// java port
	private void getSize( final float[] vec) {
		final float[] rad = new float[1], hHeight = new float[1];
		getSize(rad,hHeight);
		vec[0] = rad[0];
		vec[1] = hHeight[0];
	}
	
	private void getSize( final SbVec3f vec) {
		final float[] rad = new float[1], hHeight = new float[1];
		getSize(rad,hHeight);
		vec.setValue(0, rad[0]);
		vec.setValue(1, hHeight[0]);
	}
	
private void
getSize(final float[] rad, final float[] hHeight)
//
////////////////////////////////////////////////////////////////////////
{
  rad[0]     = (radius.isIgnored() ? 1.0f : radius.getValue());
  hHeight[0] = (height.isIgnored() ? 1.0f : height.getValue() / 2.0f);
}

private boolean ADD_TRIANGLE(final VectorOfSbVec3f points,final List<Integer> indices, boolean winding) {
	int currentIndex = (int)points.size(); 
	indices.add(currentIndex-3); 
	indices.add(winding ? currentIndex-1 : currentIndex - 2); 
	indices.add(winding ? currentIndex-2 : currentIndex - 1); 
	winding = !winding;
	return winding;
}

private void ADD_CENTER_TRIANGLE(final VectorOfSbVec3f points,final List<Integer> indices, int centerIndex) { 
	int currentIndex = (int)points.size(); 
	indices.add(currentIndex-1); 
	indices.add(centerIndex); 
	indices.add(currentIndex-2);
}

private void GLRenderVertexArray(SoGLRenderAction action,
                                 boolean sendNormals, boolean doTextures)
{
  SoState state = action.getState();
  final SbVec3fSingle scale = new SbVec3fSingle(), tmp = new SbVec3fSingle();
  getSize(scale);
  scale.getValue()[2] = scale.getValueRead()[0];

  int                 curParts, side, section;
  final int[] numSides = new int[1],numSections = new int[1];
  float               yTop, yBot, dy;
  float               s, ds, tTop, tBot, dt;
  float               outerRadius, innerRadius, dRadius;
  final SbVec2f[][]             ringCoords = new SbVec2f[1][];
  final SbVec3fSingle             pt = new SbVec3fSingle(), norm = new SbVec3fSingle();
  final SoMaterialBundle    mb = new SoMaterialBundle(action);

  curParts = (parts.isIgnored() ? Part.ALL.getValue() : parts.getValue());

  // Compute number of sides and sections to use to represent
  // cylinder, then compute ring of x,z coordinates around cylinder
  // and store in ringCoords.
  computeRing(action, numSides, numSections, ringCoords);

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

  if (!cacheValid) {
    _cache.scale.copyFrom(scale);
    _cache.curParts = curParts;
    _cache.numSides = numSides[0];
    _cache.numSections = numSections[0];
    _cache.useNormals = sendNormals;
    _cache.useTexCoords = doTextures;

    final List<Integer> indices = new ArrayList<Integer>();
    final VectorOfSbVec3f points = new VectorOfSbVec3f();
    final VectorOfSbVec3f normals = new VectorOfSbVec3f();
    final List<SbVec2f> texCoords = new ArrayList<SbVec2f>();
    int currentIndex = 0;
    boolean winding;
    final SbVec3f currentNormal = new SbVec3f();


    if (HAS_PART(curParts, Part.SIDES.getValue())) {

      // Draw each section of sides as a triangle mesh, from top to bottom
      yTop = 1.0f;
      dy   = -2.0f / numSections[0];
      tTop = 1.0f;
      dt   = -1.0f / numSections[0];
      ds   = -1.0f / numSides[0];

      for (section = 0; section < numSections[0]; section++) {

        yBot = yTop + dy;

        tBot = tTop + dt;
        s    = 1.0f;

        winding = false;

        for (side = 0; side < numSides[0]; side++) {
          pt.getValue()[0] = ringCoords[0][side].getValueRead()[0];
          pt.getValue()[2] = ringCoords[0][side].getValueRead()[1];

          // Deal with normal
          norm.setValue(pt.getValueRead()[0], 0.0f, pt.getValueRead()[2]);
          if (sendNormals)
            currentNormal.copyFrom(norm);

          // Point at bottom of section
          pt.getValue()[1] = yBot;
          if (doTextures)
            texCoords.add(new SbVec2f(s, tBot));
          if (sendNormals) { normals.add(new SbVec3f(currentNormal)); }
          points.add(new SbVec3f(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead())));
          if (side > 0) {
            winding=ADD_TRIANGLE(points,indices,winding);
          }

          // Point at top of section
          pt.getValue()[1] = yTop;
          if (doTextures)
            texCoords.add(new SbVec2f(s, tTop));
          if (sendNormals) { normals.add(new SbVec3f(currentNormal)); }
          points.add(new SbVec3f(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead())));
          if (side > 0) {
            winding=ADD_TRIANGLE(points,indices,winding);
          }
          s += ds;
        }

        // Join end of strip back to beginning
        side = 0;
        s = 0.0f;
        pt.getValue()[0] = ringCoords[0][side].getValueRead()[0];
        pt.getValue()[2] = ringCoords[0][side].getValueRead()[1];

        // Deal with normal
        norm.setValue(pt.getValueRead()[0], 0.0f, pt.getValueRead()[2]);
        if (sendNormals)
          currentNormal.copyFrom(norm);
        
        // Point at bottom of section
        pt.getValue()[1] = yBot;
        if (doTextures)
          texCoords.add(new SbVec2f(s, tBot));
        if (sendNormals) { normals.add(new SbVec3f(currentNormal)); }
        points.add(new SbVec3f(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead())));
        winding=ADD_TRIANGLE(points,indices,winding);

        // Point at top of section
        pt.getValue()[1] = yTop;
        if (doTextures)
          texCoords.add(new SbVec2f(s, tTop));
        if (sendNormals) { normals.add(new SbVec3f(currentNormal)); }
        points.add(new SbVec3f(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead())));
        winding=ADD_TRIANGLE(points,indices,winding);
        s += ds;

        // Prepare for next section down
        yTop = yBot;
        tTop = tBot;
      }
    }

    // Draw top face as a series of concentric rings. The number of
    // rings is the same as the number of sections of the sides of the
    // cylinder.
    if (HAS_PART(curParts, Part.TOP.getValue())) {
      norm.setValue(0.0f, 1.0f, 0.0f);
      pt.getValue()[1] = 1.0f;

      if (sendNormals)
        currentNormal.copyFrom(norm);

      // Start at the outside and work in
      outerRadius = 1.0f;
      dRadius     = -1.0f / numSections[0];
      for (section = numSections[0] - 1; section >= 0; --section) {

        innerRadius = outerRadius + dRadius;

        // Innermost ring is treated as a triangle fan. This not
        // only gets better shading (because the center vertex is
        // sent), but also avoids the problem of having a polygon
        // with too many vertices.
        if (section == 0) {
          // Center point comes first
          pt.getValue()[0] = pt.getValue()[2] = 0.0f;
          if (doTextures)
            texCoords.add(new SbVec2f(0.5f, 0.5f));
          if (sendNormals) { normals.add(new SbVec3f(currentNormal)); }
          points.add(new SbVec3f(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead())));
          int centerIndex = (int)points.size()-1;

          // Send all vertices around ring. Go in reverse order
          // so that vertex ordering is correct
          for (side = numSides[0] - 1; side >= 0; side--) {
            pt.getValue()[0] = outerRadius * ringCoords[0][side].getValueRead()[0];
            pt.getValue()[2] = outerRadius * ringCoords[0][side].getValueRead()[1];
            if (doTextures)
              texCoords.add(new SbVec2f(TOP_TEX_S(pt.getValueRead()[0]), TOP_TEX_T(pt.getValueRead()[2])));
            if (sendNormals) { normals.add(new SbVec3f(currentNormal)); }
            points.add(new SbVec3f(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead())));
            if (side < numSides[0] - 1) {
              ADD_CENTER_TRIANGLE(points, indices, centerIndex);
            }
          }
          // Send first vertex again
          pt.getValue()[0] = outerRadius * ringCoords[0][numSides[0] - 1].getValueRead()[0];
          pt.getValue()[2] = outerRadius * ringCoords[0][numSides[0] - 1].getValueRead()[1];
          if (doTextures)
            texCoords.add(new SbVec2f(TOP_TEX_S(pt.getValueRead()[0]), TOP_TEX_T(pt.getValueRead()[2])));
          if (sendNormals) { normals.add(new SbVec3f(currentNormal)); }
          points.add(new SbVec3f(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead())));
          ADD_CENTER_TRIANGLE(points, indices, centerIndex);
        }

        // Other rings are triangle strips
        else {
          winding = false;

          for (side = 0; side < numSides[0]; side++) {
            // Send points on outer and inner rings
            pt.getValue()[0] = outerRadius * ringCoords[0][side].getValueRead()[0];
            pt.getValue()[2] = outerRadius * ringCoords[0][side].getValueRead()[1];
            if (doTextures)
              texCoords.add(new SbVec2f(TOP_TEX_S(pt.getValueRead()[0]), TOP_TEX_T(pt.getValueRead()[2])));
            if (sendNormals) { normals.add(new SbVec3f(currentNormal)); }
            points.add(new SbVec3f(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead())));
            if (side > 0) {
              winding=ADD_TRIANGLE(points,indices,winding);
            }

            pt.getValue()[0] = innerRadius * ringCoords[0][side].getValueRead()[0];
            pt.getValue()[2] = innerRadius * ringCoords[0][side].getValueRead()[1];
            if (doTextures)
              texCoords.add(new SbVec2f(TOP_TEX_S(pt.getValueRead()[0]), TOP_TEX_T(pt.getValueRead()[2])));
            if (sendNormals) { normals.add(new SbVec3f(currentNormal)); }
            points.add(new SbVec3f(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead())));
            if (side > 0) {
              winding=ADD_TRIANGLE(points,indices,winding);
            }
          }

          // Join end of strip back to beginning
          pt.getValue()[0] = outerRadius * ringCoords[0][0].getValueRead()[0];
          pt.getValue()[2] = outerRadius * ringCoords[0][0].getValueRead()[1];
          if (doTextures)
            texCoords.add(new SbVec2f(TOP_TEX_S(pt.getValueRead()[0]), TOP_TEX_T(pt.getValueRead()[2])));
          if (sendNormals) { normals.add(new SbVec3f(currentNormal)); }
          points.add(new SbVec3f(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead())));
          winding=ADD_TRIANGLE(points,indices,winding);
          pt.getValue()[0] = innerRadius * ringCoords[0][0].getValueRead()[0];
          pt.getValue()[2] = innerRadius * ringCoords[0][0].getValueRead()[1];
          if (doTextures)
            texCoords.add(new SbVec2f(TOP_TEX_S(pt.getValueRead()[0]), TOP_TEX_T(pt.getValueRead()[2])));
          if (sendNormals) { normals.add(new SbVec3f(currentNormal)); }
          points.add(new SbVec3f(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead())));
          winding=ADD_TRIANGLE(points,indices,winding);
          
          // Prepare for next ring
          outerRadius = innerRadius;
        }
      }
    }

    // Draw bottom face the same way as the top
    if (HAS_PART(curParts, Part.BOTTOM.getValue())) {
      norm.setValue(0.0f, -1.0f, 0.0f);
      pt.getValue()[1] = -1.0f;

      if (sendNormals)
        currentNormal.copyFrom(norm);

      // Start at the outside and work in
      outerRadius = 1.0f;
      dRadius     = -1.0f / numSections[0];
      for (section = numSections[0] - 1; section >= 0; --section) {

        innerRadius = outerRadius + dRadius;

        // Innermost ring is drawn as a triangle fan. This not
        // only gets better shading (because the center vertex is
        // sent), but also avoids the problem of having a polygon
        // with too many vertices.
        if (section == 0) {
          
          // Center point comes first
          pt.getValue()[0] = pt.getValue()[2] = 0.0f;
          if (doTextures)
            texCoords.add(new SbVec2f(0.5f, 0.5f));
          if (sendNormals) { normals.add(new SbVec3f(currentNormal)); }
          points.add(new SbVec3f(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead())));
          int centerIndex = (int)points.size()-1;

          // Send all vertices around ring
          for (side = 0; side < numSides[0]; side++) {
            pt.getValue()[0] = outerRadius * ringCoords[0][side].getValueRead()[0];
            pt.getValue()[2] = outerRadius * ringCoords[0][side].getValueRead()[1];
            if (doTextures)
              texCoords.add(new SbVec2f(BOT_TEX_S(pt.getValueRead()[0]), BOT_TEX_T(pt.getValueRead()[2])));
            if (sendNormals) { normals.add(new SbVec3f(currentNormal)); }
            points.add(new SbVec3f(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead())));
            if (side > 0) {
              ADD_CENTER_TRIANGLE(points, indices, centerIndex);
            }
          }
          // Send first vertex again
          pt.getValue()[0] = outerRadius * ringCoords[0][0].getValueRead()[0];
          pt.getValue()[2] = outerRadius * ringCoords[0][0].getValueRead()[1];
          if (doTextures)
            texCoords.add(new SbVec2f(BOT_TEX_S(pt.getValueRead()[0]), BOT_TEX_T(pt.getValueRead()[2])));
          if (sendNormals) { normals.add(new SbVec3f(currentNormal)); }
          points.add(new SbVec3f(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead())));
          ADD_CENTER_TRIANGLE(points, indices, centerIndex);
        }

        // Other rings are triangle strips
        else {
          winding = false;

          // Go in reverse order so that vertex ordering is correct
          for (side = numSides[0] - 1; side >= 0; side--) {
            // Send points on outer and inner rings
            pt.getValue()[0] = outerRadius * ringCoords[0][side].getValueRead()[0];
            pt.getValue()[2] = outerRadius * ringCoords[0][side].getValueRead()[1];
            if (doTextures)
              texCoords.add(new SbVec2f(BOT_TEX_S(pt.getValueRead()[0]), BOT_TEX_T(pt.getValueRead()[2])));
            if (sendNormals) { normals.add(new SbVec3f(currentNormal)); }
            points.add(new SbVec3f(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead())));
            if (side < numSides[0]-1) {
              winding=ADD_TRIANGLE(points,indices,winding);
            }
            pt.getValue()[0] = innerRadius * ringCoords[0][side].getValueRead()[0];
            pt.getValue()[2] = innerRadius * ringCoords[0][side].getValueRead()[1];
            if (doTextures)
              texCoords.add(new SbVec2f(BOT_TEX_S(pt.getValueRead()[0]), BOT_TEX_T(pt.getValueRead()[2])));
            if (sendNormals) { normals.add(new SbVec3f(currentNormal)); }
            points.add(new SbVec3f(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead())));
            if (side < numSides[0]-1) {
              winding=ADD_TRIANGLE(points,indices,winding);
            }
          }

          // Join end of strip back to beginning
          side = numSides[0] - 1;
          pt.getValue()[0] = outerRadius * ringCoords[0][side].getValueRead()[0];
          pt.getValue()[2] = outerRadius * ringCoords[0][side].getValueRead()[1];
          if (doTextures)
            texCoords.add(new SbVec2f(BOT_TEX_S(pt.getValueRead()[0]), BOT_TEX_T(pt.getValueRead()[2])));
          if (sendNormals) { normals.add(new SbVec3f(currentNormal)); }
          points.add(new SbVec3f(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead())));
          winding=ADD_TRIANGLE(points,indices,winding);
          pt.getValue()[0] = innerRadius * ringCoords[0][side].getValueRead()[0];
          pt.getValue()[2] = innerRadius * ringCoords[0][side].getValueRead()[1];
          if (doTextures)
            texCoords.add(new SbVec2f(BOT_TEX_S(pt.getValueRead()[0]), BOT_TEX_T(pt.getValueRead()[2])));
          if (sendNormals) { normals.add(new SbVec3f(currentNormal)); }
          points.add(new SbVec3f(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead())));
          winding=ADD_TRIANGLE(points,indices,winding);

          // Prepare for next ring
          outerRadius = innerRadius;
        }
      }
    }
    // vertex (3 floats) + normal (3 floats) + texcood (2 floats)
    final int numVertices = indices.size();
    final int numBytes = ((3+3+2) * 4) * numVertices; 
    final CharPtr data = new CharPtr(numBytes);
    FloatPtr verticesPtr = new FloatPtr(data);
    FloatPtr normalsPtr = verticesPtr.operator_add(numVertices*3);
    FloatPtr texCoordsPtr = normalsPtr.operator_add(numVertices*3);
    CharPtr vertexOffset = new CharPtr(verticesPtr); 
    CharPtr normalOffset = new CharPtr(normalsPtr); 
    CharPtr texCoordOffset = new CharPtr(texCoordsPtr); 

    for (int i = 0;i<numVertices;i++) {
      int index = indices.get(i);
      final SbVec3f point = points.get(index);
      verticesPtr.asterisk(point.getValueRead()[0]); verticesPtr.plusPlus();
      verticesPtr.asterisk(point.getValueRead()[1]); verticesPtr.plusPlus();
      verticesPtr.asterisk(point.getValueRead()[2]); verticesPtr.plusPlus();
      if (sendNormals) {
        final SbVec3f normal = normals.get(index);
        normalsPtr.asterisk(normal.getValueRead()[0]); normalsPtr.plusPlus();
        normalsPtr.asterisk(normal.getValueRead()[1]); normalsPtr.plusPlus();
        normalsPtr.asterisk(normal.getValueRead()[2]); normalsPtr.plusPlus();
      }
      if (doTextures) {
        final SbVec2f texCoord = texCoords.get(index);
        texCoordsPtr.asterisk(texCoord.getValueRead()[0]); texCoordsPtr.plusPlus();
        texCoordsPtr.asterisk(texCoord.getValueRead()[1]); texCoordsPtr.plusPlus();
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

    //delete[] data; java port
  }

  _cache.vbo.bind(state);
  _cache.drawArrays(this, action, GL2.GL_TRIANGLES);
  
  GL2 gl2 = Ctx.get(action.getCacheContext()); // java port
  
  _cache.vbo.unbind(gl2);
  
  mb.destructor(); // java port
}



////////////////////////////////////////////////////////////////////////
//
// Description:
//    Generates triangles representing a cylinder.
//
// Use: protected

protected void
generatePrimitives(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
    boolean              materialPerPart;
    int                 curParts, side, section;
    final int[] numSides = new int[1], numSections = new int[1];
    float               yTop, yBot, dy;
    float               s, ds, tTop, tBot, dt;
    float               outerRadius, innerRadius, dRadius;
    final SbVec2f[][]             ringCoords = new SbVec2f[1][];
    final SbVec3fSingle             pt = new SbVec3fSingle(), norm = new SbVec3fSingle();
    final float[]               radius = new float[1], halfHeight = new float[1];
    final SbVec4fSingle             tex = new SbVec4fSingle();
    boolean              genTexCoords = false;
    final SoPrimitiveVertex   pv = new SoPrimitiveVertex();
    final SoCylinderDetail    detail = new SoCylinderDetail();
    SoMultiTextureCoordinateElement    tce = null;

    SoMaterialBindingElement.Binding mbe =
	SoMaterialBindingElement.get(action.getState());
    materialPerPart =
	(mbe == SoMaterialBindingElement.Binding.PER_PART_INDEXED ||
	 mbe == SoMaterialBindingElement.Binding.PER_PART);

    curParts = (parts.isIgnored() ? Part.ALL.getValue() : parts.getValue());

    // Compute number of sides and sections to use to represent
    // cylinder, then compute ring of x,z coordinates around cylinder
    // and store in ringCoords.
    computeRing(action, numSides, numSections, ringCoords);

    pv.setDetail(detail);

    // Determine whether we should generate our own texture coordinates
    switch (SoMultiTextureCoordinateElement.getType(action.getState(),0)) {
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
	tce = SoMultiTextureCoordinateElement.getInstance(action.getState());
    else {
	tex.getValue()[2] = 0.0f;
	tex.getValue()[3] = 1.0f;
    }

    getSize(radius, halfHeight);

    if (HAS_PART(curParts, SoCylinder.Part.SIDES.getValue())) {

	// Draw each section of sides as a triangle mesh, from top to bottom
	yTop = 1.0f;
	dy   = -2.0f / numSections[0];
	tTop = 1.0f;
	dt   = -1.0f / numSections[0];
	ds   = -1.0f / numSides[0];

	for (section = 0; section < numSections[0]; section++) {

	    yBot = yTop + dy;

	    tBot = tTop + dt;
	    s    = 1.0f;

	    detail.setPart(SoCylinder.Part.SIDES.getValue());

	    beginShape(action, SoShape.TriangleShape.TRIANGLE_STRIP);

	    for (side = 0; side < numSides[0]; side++) {
		pt.getValue()[0] = ringCoords[0][side].getValueRead()[0];
		pt.getValue()[2] = ringCoords[0][side].getValueRead()[1];

		// Deal with normal
		norm.setValue(pt.getValueRead()[0], 0.0f, pt.getValueRead()[2]);
		pv.setNormal(norm);

		// Point at bottom of section
		pt.getValue()[1] = yBot;
		pt.getValue()[0] *= radius[0];
		pt.getValue()[1] *= halfHeight[0];
		pt.getValue()[2] *= radius[0];

		if (genTexCoords) {
		    tex.getValue()[0] = s;
		    tex.getValue()[1] = tBot;
		}
		else
		    tex.copyFrom( tce.get(pt, norm));
		pv.setPoint(pt);
		pv.setTextureCoords(tex);
		shapeVertex(pv);

		// Point at top of section
		pt.getValue()[1] = yTop;
		pt.getValue()[1] *= halfHeight[0];
		if (genTexCoords) {
		    tex.getValue()[0] = s;
		    tex.getValue()[1] = tTop;
		}
		else
		    tex.copyFrom(tce.get(pt, norm));
		pv.setPoint(pt);
		pv.setTextureCoords(tex);
		shapeVertex(pv);
		s += ds;
	    }

	    // Join end of strip back to beginning
	    side = 0;
	    s = 0.0f;
	    pt.getValue()[0] = ringCoords[0][side].getValueRead()[0];
	    pt.getValue()[2] = ringCoords[0][side].getValueRead()[1];

	    // Deal with normal
	    norm.setValue(pt.getValueRead()[0], 0.0f, pt.getValueRead()[2]);
	    pv.setNormal(norm);

	    // Point at bottom of section
	    pt.getValue()[1] = yBot;
	    pt.getValue()[0] *= radius[0];
	    pt.getValue()[1] *= halfHeight[0];
	    pt.getValue()[2] *= radius[0];

	    if (genTexCoords) {
		tex.getValue()[0] = s;
		tex.getValue()[1] = tBot;
	    }
	    else
		tex.copyFrom( tce.get(pt, norm));
	    pv.setPoint(pt);
	    pv.setTextureCoords(tex);
	    shapeVertex(pv);

	    // Point at top of section
	    pt.getValue()[1] = yTop;
	    pt.getValue()[1] *= halfHeight[0];
	    if (genTexCoords) {
		tex.getValue()[0] = s;
		tex.getValue()[1] = tTop;
	    }
	    else
		tex.copyFrom( tce.get(pt, norm));
	    pv.setPoint(pt);
	    pv.setTextureCoords(tex);
	    shapeVertex(pv);
	    s += ds;

	    endShape();

	    // Prepare for next section down
	    yTop = yBot;
	    tTop = tBot;
	}
    }

    // Draw top face as a series of concentric rings. The number of
    // rings is the same as the number of sections of the sides of the
    // cylinder.
    if (HAS_PART(curParts, Part.TOP.getValue())) {
	norm.setValue(0.0f, 1.0f, 0.0f);
	pt.getValue()[1] = halfHeight[0];

	if (materialPerPart)
	    pv.setMaterialIndex(1);
	pv.setNormal(norm);
	detail.setPart(Part.TOP.getValue());

	// Start at the outside and work in
	outerRadius = 1.0f;
	dRadius     = -1.0f / numSections[0];
	for (section = numSections[0] - 1; section >= 0; --section) {

	    innerRadius = outerRadius + dRadius;

	    // Innermost ring is treated as a triangle fan. This not
	    // only gets better shading (because the center vertex is
	    // sent), but also avoids the problem of having a polygon
	    // with too many vertices.
	    if (section == 0) {
		beginShape(action, SoShape.TriangleShape.TRIANGLE_FAN);

		// Center point comes first
		pt.getValue()[0] = pt.getValue()[2] = 0.0f;
		if (genTexCoords)
		    tex.getValue()[0] = tex.getValue()[1] = 0.5f;
		else
		    tex.copyFrom( tce.get(norm, norm));
		pv.setPoint(pt);
		pv.setTextureCoords(tex);
		shapeVertex(pv);

		// Send all vertices around ring. Go in reverse order
		// so that vertex ordering is correct
		for (side = numSides[0] - 1; side >= 0; side--) {
		    pt.getValue()[0] = outerRadius * ringCoords[0][side].getValueRead()[0];
		    pt.getValue()[2] = outerRadius * ringCoords[0][side].getValueRead()[1];
		    pt.getValue()[0] *= radius[0];
		    pt.getValue()[2] *= radius[0];
		    if (genTexCoords) {
			tex.getValue()[0] = TOP_TEX_S(pt.getValueRead()[0]);
			tex.getValue()[1] = TOP_TEX_T(pt.getValueRead()[2]);
		    }
		    else
			tex.copyFrom( tce.get(pt, norm));
		    pv.setPoint(pt);
		    pv.setTextureCoords(tex);
		    shapeVertex(pv);
		}
		// Send first vertex again
		pt.getValue()[0] = outerRadius * ringCoords[0][numSides[0] - 1].getValueRead()[0];
		pt.getValue()[2] = outerRadius * ringCoords[0][numSides[0] - 1].getValueRead()[1];
		pt.getValue()[0] *= radius[0];
		pt.getValue()[2] *= radius[0];
		if (genTexCoords) {
		    tex.getValue()[0] = TOP_TEX_S(pt.getValueRead()[0]);
		    tex.getValue()[1] = TOP_TEX_T(pt.getValueRead()[2]);
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
		beginShape(action, SoShape.TriangleShape.TRIANGLE_STRIP);

		for (side = 0; side < numSides[0]; side++) {
		    // Send points on outer and inner rings
		    pt.getValue()[0] = outerRadius * ringCoords[0][side].getValueRead()[0];
		    pt.getValue()[2] = outerRadius * ringCoords[0][side].getValueRead()[1];
		    pt.getValue()[0] *= radius[0];
		    pt.getValue()[2] *= radius[0];
		    if (genTexCoords) {
			tex.getValue()[0] = TOP_TEX_S(pt.getValueRead()[0]);
			tex.getValue()[1] = TOP_TEX_T(pt.getValueRead()[2]);
		    }
		    else
			tex.copyFrom( tce.get(pt, norm));
		    pv.setPoint(pt);
		    pv.setTextureCoords(tex);
		    shapeVertex(pv);
		    pt.getValue()[0] = innerRadius * ringCoords[0][side].getValueRead()[0];
		    pt.getValue()[2] = innerRadius * ringCoords[0][side].getValueRead()[1];
		    pt.getValue()[0] *= radius[0];
		    pt.getValue()[2] *= radius[0];
		    if (genTexCoords) {
			tex.getValue()[0] = TOP_TEX_S(pt.getValueRead()[0]);
			tex.getValue()[1] = TOP_TEX_T(pt.getValueRead()[2]);
		    }
		    else
			tex.copyFrom( tce.get(pt, norm));
		    pv.setPoint(pt);
		    pv.setTextureCoords(tex);
		    shapeVertex(pv);
		}

		// Join end of strip back to beginning
		pt.getValue()[0] = outerRadius * ringCoords[0][0].getValueRead()[0];
		pt.getValue()[2] = outerRadius * ringCoords[0][0].getValueRead()[1];
		pt.getValue()[0] *= radius[0];
		pt.getValue()[2] *= radius[0];
		if (genTexCoords) {
		    tex.getValue()[0] = TOP_TEX_S(pt.getValueRead()[0]);
		    tex.getValue()[1] = TOP_TEX_T(pt.getValueRead()[2]);
		}
		else
		    tex.copyFrom( tce.get(pt, norm));
		pv.setPoint(pt);
		pv.setTextureCoords(tex);
		shapeVertex(pv);
		pt.getValue()[0] = innerRadius * ringCoords[0][0].getValueRead()[0];
		pt.getValue()[2] = innerRadius * ringCoords[0][0].getValueRead()[1];
		pt.getValue()[0] *= radius[0];
		pt.getValue()[2] *= radius[0];
		if (genTexCoords) {
		    tex.getValue()[0] = TOP_TEX_S(pt.getValueRead()[0]);
		    tex.getValue()[1] = TOP_TEX_T(pt.getValueRead()[2]);
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

    // Draw bottom face the same way as the top
    if (HAS_PART(curParts, Part.BOTTOM.getValue())) {
	norm.setValue(0.0f, -1.0f, 0.0f);
	pt.getValue()[1] = -halfHeight[0];

	if (materialPerPart)
	    pv.setMaterialIndex(2);
	pv.setNormal(norm);
	detail.setPart(Part.BOTTOM.getValue());

	// Start at the outside and work in
	outerRadius = 1.0f;
	dRadius     = -1.0f / numSections[0];
	for (section = numSections[0] - 1; section >= 0; --section) {

	    innerRadius = outerRadius + dRadius;

	    // Innermost ring is drawn as a triangle fan. This not
	    // only gets better shading (because the center vertex is
	    // sent), but also avoids the problem of having a polygon
	    // with too many vertices.
	    if (section == 0) {
		beginShape(action, SoShape.TriangleShape.TRIANGLE_FAN);

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
		    pt.getValue()[0] = outerRadius * ringCoords[0][side].getValueRead()[0];
		    pt.getValue()[2] = outerRadius * ringCoords[0][side].getValueRead()[1];
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
		pt.getValue()[0] = outerRadius * ringCoords[0][0].getValueRead()[0];
		pt.getValue()[2] = outerRadius * ringCoords[0][0].getValueRead()[1];
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
		beginShape(action, SoShape.TriangleShape.TRIANGLE_STRIP);

		// Go in reverse order so that vertex ordering is correct
		for (side = numSides[0] - 1; side >= 0; side--) {
		    // Send points on outer and inner rings
		    pt.getValue()[0] = outerRadius * ringCoords[0][side].getValueRead()[0];
		    pt.getValue()[2] = outerRadius * ringCoords[0][side].getValueRead()[1];
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
		    pt.getValue()[0] = innerRadius * ringCoords[0][side].getValueRead()[0];
		    pt.getValue()[2] = innerRadius * ringCoords[0][side].getValueRead()[1];
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

		// Join end of strip back to beginning
		side = numSides[0] - 1;
		pt.getValue()[0] = outerRadius * ringCoords[0][side].getValueRead()[0];
		pt.getValue()[2] = outerRadius * ringCoords[0][side].getValueRead()[1];
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
		pt.getValue()[0] = innerRadius * ringCoords[0][side].getValueRead()[0];
		pt.getValue()[2] = innerRadius * ringCoords[0][side].getValueRead()[1];
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

//
// Macro to multiply out coordinates to avoid extra GL calls:
//
public static float[] SCALE(float[] pt, float[] tmp, float[] scale) {
	tmp[0] = (pt)[0]*scale[0]; 
	tmp[1] = (pt)[1]*scale[1]; 
	tmp[2] = (pt)[2]*scale[2];
	return tmp;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Generic rendering of cylinder with or without normals, with or
//    without texture coordinates.
//
// Use: private

private void
GLRenderGeneric(SoGLRenderAction action,
			    boolean sendNormals, boolean doTextures)
//
////////////////////////////////////////////////////////////////////////
{
    final SbVec3fSingle scale = new SbVec3fSingle(), tmp = new SbVec3fSingle();
    getSize(scale);
    scale.getValue()[2] = scale.getValueRead()[0];

    boolean              materialPerPart;
    int                 curParts,  side, section;
    final int[] numSides = new int[1], numSections = new int[1];
    float               yTop, yBot, dy;
    float               s, ds, tTop, tBot, dt;
    float               outerRadius, innerRadius, dRadius;
    final SbVec2f[][]             ringCoords = new SbVec2f[1][];
    final SbVec3fSingle             pt = new SbVec3fSingle(), norm = new SbVec3fSingle();
    final SoMaterialBundle    mb = new SoMaterialBundle(action);

    SoMaterialBindingElement.Binding mbe =
	SoMaterialBindingElement.get(action.getState());
    materialPerPart =
	(mbe == SoMaterialBindingElement.Binding.PER_PART_INDEXED ||
	 mbe == SoMaterialBindingElement.Binding.PER_PART);

    curParts = (parts.isIgnored() ? Part.ALL.getValue() : parts.getValue());

    // Compute number of sides and sections to use to represent
    // cylinder, then compute ring of x,z coordinates around cylinder
    // and store in ringCoords.
    computeRing(action, numSides, numSections, ringCoords);

    // Make sure first material is sent if necessary
    mb.sendFirst();
    
    GL2 gl2 = Ctx.get(action.getCacheContext());

    if (HAS_PART(curParts, Part.SIDES.getValue())) {

	// Draw each section of sides as a triangle mesh, from top to bottom
	yTop = 1.0f;
	dy   = -2.0f / numSections[0];
	tTop = 1.0f;
	dt   = -1.0f / numSections[0];
	ds   = -1.0f / numSides[0];

	for (section = 0; section < numSections[0]; section++) {

	    yBot = yTop + dy;

	    tBot = tTop + dt;
	    s    = 1.0f;

	    gl2.glBegin(GL2.GL_TRIANGLE_STRIP);

	    for (side = 0; side < numSides[0]; side++) {
		pt.getValue()[0] = ringCoords[0][side].getValueRead()[0];
		pt.getValue()[2] = ringCoords[0][side].getValueRead()[1];

		// Deal with normal
		norm.setValue(pt.getValueRead()[0], 0.0f, pt.getValueRead()[2]);
		if (sendNormals)
		    gl2.glNormal3fv(norm.getValueRead(),0);

		// Point at bottom of section
		pt.getValue()[1] = yBot;
		if (doTextures)
		    gl2.glTexCoord2f(s, tBot);
		gl2.glVertex3fv(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead()),0);

		// Point at top of section
		pt.getValue()[1] = yTop;
		if (doTextures)
		    gl2.glTexCoord2f(s, tTop);
		gl2.glVertex3fv(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead()),0);
		s += ds;
	    }

	    // Join end of strip back to beginning
	    side = 0;
	    s = 0.0f;
	    pt.getValue()[0] = ringCoords[0][side].getValueRead()[0];
	    pt.getValue()[2] = ringCoords[0][side].getValueRead()[1];

	    // Deal with normal
	    norm.setValue(pt.getValueRead()[0], 0.0f, pt.getValueRead()[2]);
	    if (sendNormals)
		gl2.glNormal3fv(norm.getValueRead(),0);

	    // Point at bottom of section
	    pt.getValue()[1] = yBot;
	    if (doTextures)
		gl2.glTexCoord2f(s, tBot);
	    gl2.glVertex3fv(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead()),0);

	    // Point at top of section
	    pt.getValue()[1] = yTop;
	    if (doTextures)
		gl2.glTexCoord2f(s, tTop);
	    gl2.glVertex3fv(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead()),0);
	    s += ds;

	    gl2.glEnd();

	    // Prepare for next section down
	    yTop = yBot;
	    tTop = tBot;
	}
    }

    // Draw top face as a series of concentric rings. The number of
    // rings is the same as the number of sections of the sides of the
    // cylinder.
    if (HAS_PART(curParts, Part.TOP.getValue())) {
	norm.setValue(0.0f, 1.0f, 0.0f);
	pt.getValue()[1] = 1.0f;

	if (materialPerPart)
	    mb.send(1, false);
	if (sendNormals)
	    gl2.glNormal3fv(norm.getValueRead(),0);

	// Start at the outside and work in
	outerRadius = 1.0f;
	dRadius     = -1.0f / numSections[0];
	for (section = numSections[0] - 1; section >= 0; --section) {

	    innerRadius = outerRadius + dRadius;

	    // Innermost ring is treated as a triangle fan. This not
	    // only gets better shading (because the center vertex is
	    // sent), but also avoids the problem of having a polygon
	    // with too many vertices.
	    if (section == 0) {
		gl2.glBegin(GL2.GL_TRIANGLE_FAN);

		// Center point comes first
		pt.getValue()[0] = pt.getValue()[2] = 0.0f;
		if (doTextures)
		    gl2.glTexCoord2f(0.5f, 0.5f);
		gl2.glVertex3fv(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead()),0);

		// Send all vertices around ring. Go in reverse order
		// so that vertex ordering is correct
		for (side = numSides[0] - 1; side >= 0; side--) {
		    pt.getValue()[0] = outerRadius * ringCoords[0][side].getValueRead()[0];
		    pt.getValue()[2] = outerRadius * ringCoords[0][side].getValueRead()[1];
		    if (doTextures)
			gl2.glTexCoord2f(TOP_TEX_S(pt.getValueRead()[0]), TOP_TEX_T(pt.getValueRead()[2]));
		    gl2.glVertex3fv(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead()),0);
		}
		// Send first vertex again
		pt.getValue()[0] = outerRadius * ringCoords[0][numSides[0] - 1].getValueRead()[0];
		pt.getValue()[2] = outerRadius * ringCoords[0][numSides[0] - 1].getValueRead()[1];
		if (doTextures)
		    gl2.glTexCoord2f(TOP_TEX_S(pt.getValueRead()[0]), TOP_TEX_T(pt.getValueRead()[2]));
		gl2.glVertex3fv(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead()),0);

		gl2.glEnd();
	    }

	    // Other rings are triangle strips
	    else {
		gl2.glBegin(GL2.GL_TRIANGLE_STRIP);

		for (side = 0; side < numSides[0]; side++) {
		    // Send points on outer and inner rings
		    pt.getValue()[0] = outerRadius * ringCoords[0][side].getValueRead()[0];
		    pt.getValue()[2] = outerRadius * ringCoords[0][side].getValueRead()[1];
		    if (doTextures)
			gl2.glTexCoord2f(TOP_TEX_S(pt.getValueRead()[0]), TOP_TEX_T(pt.getValueRead()[2]));
		    gl2.glVertex3fv(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead()),0);
		    pt.getValue()[0] = innerRadius * ringCoords[0][side].getValueRead()[0];
		    pt.getValue()[2] = innerRadius * ringCoords[0][side].getValueRead()[1];
		    if (doTextures)
			gl2.glTexCoord2f(TOP_TEX_S(pt.getValueRead()[0]), TOP_TEX_T(pt.getValueRead()[2]));
		    gl2.glVertex3fv(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead()),0);
		}

		// Join end of strip back to beginning
		pt.getValue()[0] = outerRadius * ringCoords[0][0].getValueRead()[0];
		pt.getValue()[2] = outerRadius * ringCoords[0][0].getValueRead()[1];
		if (doTextures)
		    gl2.glTexCoord2f(TOP_TEX_S(pt.getValueRead()[0]), TOP_TEX_T(pt.getValueRead()[2]));
		gl2.glVertex3fv(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead()),0);
		pt.getValue()[0] = innerRadius * ringCoords[0][0].getValueRead()[0];
		pt.getValue()[2] = innerRadius * ringCoords[0][0].getValueRead()[1];
		if (doTextures)
		    gl2.glTexCoord2f(TOP_TEX_S(pt.getValueRead()[0]), TOP_TEX_T(pt.getValueRead()[2]));
		gl2.glVertex3fv(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead()),0);

		gl2.glEnd();

		// Prepare for next ring
		outerRadius = innerRadius;
	    }
	}
    }

    // Draw bottom face the same way as the top
    if (HAS_PART(curParts, Part.BOTTOM.getValue())) {
	norm.setValue(0.0f, -1.0f, 0.0f);
	pt.getValue()[1] = -1.0f;

	if (materialPerPart)
	    mb.send(2, false);
	if (sendNormals)
	    gl2.glNormal3fv(norm.getValueRead(),0);

	// Start at the outside and work in
	outerRadius = 1.0f;
	dRadius     = -1.0f / numSections[0];
	for (section = numSections[0] - 1; section >= 0; --section) {

	    innerRadius = outerRadius + dRadius;

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
		gl2.glVertex3fv(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead()),0);

		// Send all vertices around ring
		for (side = 0; side < numSides[0]; side++) {
		    pt.getValue()[0] = outerRadius * ringCoords[0][side].getValueRead()[0];
		    pt.getValue()[2] = outerRadius * ringCoords[0][side].getValueRead()[1];
		    if (doTextures)
			gl2.glTexCoord2f(BOT_TEX_S(pt.getValueRead()[0]), BOT_TEX_T(pt.getValueRead()[2]));
		    gl2.glVertex3fv(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead()),0);
		}
		// Send first vertex again
		pt.getValue()[0] = outerRadius * ringCoords[0][0].getValueRead()[0];
		pt.getValue()[2] = outerRadius * ringCoords[0][0].getValueRead()[1];
		if (doTextures)
		    gl2.glTexCoord2f(BOT_TEX_S(pt.getValueRead()[0]), BOT_TEX_T(pt.getValueRead()[2]));
		gl2.glVertex3fv(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead()),0);

		gl2.glEnd();
	    }

	    // Other rings are triangle strips
	    else {
		gl2.glBegin(GL2.GL_TRIANGLE_STRIP);

		// Go in reverse order so that vertex ordering is correct
		for (side = numSides[0] - 1; side >= 0; side--) {
		    // Send points on outer and inner rings
		    pt.getValue()[0] = outerRadius * ringCoords[0][side].getValueRead()[0];
		    pt.getValue()[2] = outerRadius * ringCoords[0][side].getValueRead()[1];
		    if (doTextures)
			gl2.glTexCoord2f(BOT_TEX_S(pt.getValueRead()[0]), BOT_TEX_T(pt.getValueRead()[2]));
		    gl2.glVertex3fv(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead()),0);
		    pt.getValue()[0] = innerRadius * ringCoords[0][side].getValueRead()[0];
		    pt.getValue()[2] = innerRadius * ringCoords[0][side].getValueRead()[1];
		    if (doTextures)
			gl2.glTexCoord2f(BOT_TEX_S(pt.getValueRead()[0]), BOT_TEX_T(pt.getValueRead()[2]));
		    gl2.glVertex3fv(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead()),0);
		}

		// Join end of strip back to beginning
		side = numSides[0] - 1;
		pt.getValue()[0] = outerRadius * ringCoords[0][side].getValueRead()[0];
		pt.getValue()[2] = outerRadius * ringCoords[0][side].getValueRead()[1];
		if (doTextures)
		    gl2.glTexCoord2f(BOT_TEX_S(pt.getValueRead()[0]), BOT_TEX_T(pt.getValueRead()[2]));
		gl2.glVertex3fv(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead()),0);
		pt.getValue()[0] = innerRadius * ringCoords[0][side].getValueRead()[0];
		pt.getValue()[2] = innerRadius * ringCoords[0][side].getValueRead()[1];
		if (doTextures)
		    gl2.glTexCoord2f(BOT_TEX_S(pt.getValueRead()[0]), BOT_TEX_T(pt.getValueRead()[2]));
		gl2.glVertex3fv(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead()),0);

		gl2.glEnd();

		// Prepare for next ring
		outerRadius = innerRadius;
	    }
	}
    }
    mb.destructor(); // java port
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Renders cylinder with normals and without texture coordinates.
//
// Use: private

private void
GLRenderNvertTnone(SoGLRenderAction action)
//
////////////////////////////////////////////////////////////////////////
{
    final SbVec3fSingle scale = new SbVec3fSingle(), tmp = new SbVec3fSingle();
    getSize(scale);
    scale.getValue()[2] = scale.getValueRead()[0];

    boolean              materialPerPart;
    int                 curParts, side, section;
    final int[] numSides = new int[1], numSections = new int[1];
    float               yTop, yBot, dy;
    float               outerRadius, innerRadius, dRadius;
    final SbVec2f[][]             ringCoords = new SbVec2f[1][];
    final SbVec3fSingle             pt = new SbVec3fSingle(), norm = new SbVec3fSingle();
    final SoMaterialBundle    mb = new SoMaterialBundle(action);

    SoMaterialBindingElement.Binding mbe =
	SoMaterialBindingElement.get(action.getState());
    materialPerPart =
	(mbe == SoMaterialBindingElement.Binding.PER_PART_INDEXED ||
	 mbe == SoMaterialBindingElement.Binding.PER_PART);

    curParts = (parts.isIgnored() ? Part.ALL.getValue() : parts.getValue());

    // Compute number of sides and sections to use to represent
    // cylinder, then compute ring of x,z coordinates around cylinder
    // and store in ringCoords.
    computeRing(action, numSides, numSections, ringCoords);

    // Make sure first material is sent if necessary
    mb.sendFirst();

	GL2 gl2 = Ctx.get(action.getCacheContext()); //java port

    if (HAS_PART(curParts, Part.SIDES.getValue())) {

	// Draw each section of sides as a triangle mesh, from top to bottom
	yTop = 1.0f;
	dy   = -2.0f / numSections[0];
	
	for (section = 0; section < numSections[0]; section++) {

	    yBot = yTop + dy;


	    gl2.glBegin(GL2.GL_TRIANGLE_STRIP);

	    for (side = 0; side < numSides[0]; side++) {
		pt.getValue()[0] = ringCoords[0][side].getValueRead()[0];
		pt.getValue()[2] = ringCoords[0][side].getValueRead()[1];

		// Deal with normal
		norm.setValue(pt.getValueRead()[0], 0.0f, pt.getValueRead()[2]);
		gl2.glNormal3fv(norm.getValueRead(),0);

		// Point at bottom of section
		pt.getValue()[1] = yBot;
		gl2.glVertex3fv(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead()),0);

		// Point at top of section
		pt.getValue()[1] = yTop;
		gl2.glVertex3fv(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead()),0);
	    }

	    // Join end of strip back to beginning
	    side = 0;
	    pt.getValue()[0] = ringCoords[0][side].getValueRead()[0];
	    pt.getValue()[2] = ringCoords[0][side].getValueRead()[1];

	    // Deal with normal
	    norm.setValue(pt.getValueRead()[0], 0.0f, pt.getValueRead()[2]);
	    gl2.glNormal3fv(norm.getValueRead(),0);

	    // Point at bottom of section
	    pt.getValue()[1] = yBot;
	    gl2.glVertex3fv(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead()),0);

	    // Point at top of section
	    pt.getValue()[1] = yTop;
	    gl2.glVertex3fv(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead()),0);

	    gl2.glEnd();

	    // Prepare for next section down
	    yTop = yBot;
	}
    }

    // Draw top face as a series of concentric rings. The number of
    // rings is the same as the number of sections of the sides of the
    // cylinder.
    if (HAS_PART(curParts, Part.TOP.getValue())) {
	norm.setValue(0.0f, 1.0f, 0.0f);
	pt.getValue()[1] = 1.0f;

	if (materialPerPart)
	    mb.send(1, false);
	gl2.glNormal3fv(norm.getValueRead(),0);

	// Start at the outside and work in
	outerRadius = 1.0f;
	dRadius     = -1.0f / numSections[0];
	for (section = numSections[0] - 1; section >= 0; --section) {

	    innerRadius = outerRadius + dRadius;

	    // Innermost ring is treated as a triangle fan. This not
	    // only gets better shading (because the center vertex is
	    // sent), but also avoids the problem of having a polygon
	    // with too many vertices.
	    if (section == 0) {
		gl2.glBegin(GL2.GL_TRIANGLE_FAN);

		// Center point comes first
		pt.getValue()[0] = pt.getValue()[2] = 0.0f;
		gl2.glVertex3fv(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead()),0);

		// Send all vertices around ring. Go in reverse order
		// so that vertex ordering is correct
		for (side = numSides[0] - 1; side >= 0; side--) {
		    pt.getValue()[0] = outerRadius * ringCoords[0][side].getValueRead()[0];
		    pt.getValue()[2] = outerRadius * ringCoords[0][side].getValueRead()[1];
		    gl2.glVertex3fv(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead()),0);
		}
		// Send first vertex again
		pt.getValue()[0] = outerRadius * ringCoords[0][numSides[0] - 1].getValueRead()[0];
		pt.getValue()[2] = outerRadius * ringCoords[0][numSides[0] - 1].getValueRead()[1];
		gl2.glVertex3fv(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead()),0);

		gl2.glEnd();
	    }

	    // Other rings are triangle strips
	    else {
		gl2.glBegin(GL2.GL_TRIANGLE_STRIP);

		for (side = 0; side < numSides[0]; side++) {
		    // Send points on outer and inner rings
		    pt.getValue()[0] = outerRadius * ringCoords[0][side].getValueRead()[0];
		    pt.getValue()[2] = outerRadius * ringCoords[0][side].getValueRead()[1];
		    gl2.glVertex3fv(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead()),0);
		    pt.getValue()[0] = innerRadius * ringCoords[0][side].getValueRead()[0];
		    pt.getValue()[2] = innerRadius * ringCoords[0][side].getValueRead()[1];
		    gl2.glVertex3fv(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead()),0);
		}

		// Join end of strip back to beginning
		pt.getValue()[0] = outerRadius * ringCoords[0][0].getValueRead()[0];
		pt.getValue()[2] = outerRadius * ringCoords[0][0].getValueRead()[1];
		gl2.glVertex3fv(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead()),0);
		pt.getValue()[0] = innerRadius * ringCoords[0][0].getValueRead()[0];
		pt.getValue()[2] = innerRadius * ringCoords[0][0].getValueRead()[1];
		gl2.glVertex3fv(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead()),0);

		gl2.glEnd();

		// Prepare for next ring
		outerRadius = innerRadius;
	    }
	}
    }

    // Draw bottom face the same way as the top
    if (HAS_PART(curParts, Part.BOTTOM.getValue())) {
	norm.setValue(0.0f, -1.0f, 0.0f);
	pt.getValue()[1] = -1.0f;

	if (materialPerPart)
	    mb.send(2, false);
	gl2.glNormal3fv(norm.getValueRead(),0);

	// Start at the outside and work in
	outerRadius = 1.0f;
	dRadius     = -1.0f / numSections[0];
	for (section = numSections[0] - 1; section >= 0; --section) {

	    innerRadius = outerRadius + dRadius;

	    // Innermost ring is drawn as a triangle fan. This not
	    // only gets better shading (because the center vertex is
	    // sent), but also avoids the problem of having a polygon
	    // with too many vertices.
	    if (section == 0) {
		gl2.glBegin(GL2.GL_TRIANGLE_FAN);

		// Center point comes first
		pt.getValue()[0] = pt.getValue()[2] = 0.0f;
		gl2.glVertex3fv(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead()),0);

		// Send all vertices around ring
		for (side = 0; side < numSides[0]; side++) {
		    pt.getValue()[0] = outerRadius * ringCoords[0][side].getValueRead()[0];
		    pt.getValue()[2] = outerRadius * ringCoords[0][side].getValueRead()[1];
		    gl2.glVertex3fv(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead()),0);
		}
		// Send first vertex again
		pt.getValue()[0] = outerRadius * ringCoords[0][0].getValueRead()[0];
		pt.getValue()[2] = outerRadius * ringCoords[0][0].getValueRead()[1];
		gl2.glVertex3fv(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead()),0);

		gl2.glEnd();
	    }

	    // Other rings are triangle strips
	    else {
	    	gl2.glBegin(GL2.GL_TRIANGLE_STRIP);

		// Go in reverse order so that vertex ordering is correct
		for (side = numSides[0] - 1; side >= 0; side--) {
		    // Send points on outer and inner rings
		    pt.getValue()[0] = outerRadius * ringCoords[0][side].getValueRead()[0];
		    pt.getValue()[2] = outerRadius * ringCoords[0][side].getValueRead()[1];
		    gl2.glVertex3fv(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead()),0);
		    pt.getValue()[0] = innerRadius * ringCoords[0][side].getValueRead()[0];
		    pt.getValue()[2] = innerRadius * ringCoords[0][side].getValueRead()[1];
		    gl2.glVertex3fv(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead()),0);
		}

		// Join end of strip back to beginning
		side = numSides[0] - 1;
		pt.getValue()[0] = outerRadius * ringCoords[0][side].getValueRead()[0];
		pt.getValue()[2] = outerRadius * ringCoords[0][side].getValueRead()[1];
		gl2.glVertex3fv(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead()),0);
		pt.getValue()[0] = innerRadius * ringCoords[0][side].getValueRead()[0];
		pt.getValue()[2] = innerRadius * ringCoords[0][side].getValueRead()[1];
		gl2.glVertex3fv(SCALE(pt.getValueRead(),tmp.getValueRead(),scale.getValueRead()),0);

		gl2.glEnd();

		// Prepare for next ring
		outerRadius = innerRadius;
	    }
	}
    }
	mb.destructor();
}



////////////////////////////////////////////////////////////////////////
//
// Description:
//    Computes number of sides per circular cross-section and number
//    of sections, based on complexity, then computes ring of x,z
//    coordinates around cylinder and stores in ringCoords.
//
// Use: private

private void
computeRing(SoAction action, final int[] numSides, final int[] numSections,
                        final SbVec2f[][] ringCoords)
                        //
                        ////////////////////////////////////////////////////////////////////////
{
  float       complexity = SoComplexityElement.get(action.getState());
  float       theta, dTheta;
  int         side;

  // In object space, just base number of divisions on complexity
  if (SoComplexityTypeElement.get(action.getState()) ==
    SoComplexityTypeElement.Type.OBJECT_SPACE) {

      // If complexity is between 0 and .5 (inclusive), use 4 sections
      // and between 32 and 64 sides:
      if (complexity <= 0.5) {
        numSections[0] = 4;
        numSides[0]    = (int) (complexity * 64.0 + 32.0);
      }

      // If complexity is between .5 and 1, use between 4 and 16 sections
      // and between 64 and 192 sides:
      else {
        numSections[0] = (int) (24.0 * complexity - 8.0);
        numSides[0]    = (int) (complexity * 256.0 - 64);
      }
  }

  // In screen space, set the number of sides/sections based on the
  // complexity and the size of the cylinder when projected onto the screen.
  else {
    final SbVec2s         rectSize = new SbVec2s();
    short           maxSize;
    final float[]           radius = new float[1], halfHeight = new float[1];

    getSize(radius, halfHeight);
    final SbVec3f         p = new SbVec3f(radius[0], halfHeight[0], radius[0]);

    getScreenSize(action.getState(), new SbBox3f(p.operator_minus(), p), rectSize);

    maxSize = (rectSize.getValue()[0] > rectSize.getValue()[1] ? rectSize.getValue()[0] : rectSize.getValue()[1]);

    numSections[0] = 1 + (int) (0.2  * complexity * maxSize);
    numSides[0]    = 3 + (int) (0.25 * complexity * maxSize);
  }

  // Make sure the current storage for ring coordinates is big enough
  if (numSides[0] > maxCoords) {

//    if (maxCoords > 0)
//      delete [] coordsArray; java port

    maxCoords = numSides[0];

    coordsArray = new SbVec2f[maxCoords];
    for(int i=0; i<maxCoords;i++) { 
    	coordsArray[i] = new SbVec2f(); // java port
    }
  }

  ringCoords[0] = coordsArray;

  // Compute x and z coordinates around ring
  theta  = 0.0f;
  dTheta = 2.0f * (float)Math.PI / numSides[0];
  for (side = 0; side < numSides[0]; side++) {
    ringCoords[0][side].setValue((float)Math.sin(theta), (float)-Math.cos(theta));
    theta += dTheta;
  }
}

// Returns S or T texture coord for point on top or bottom of
// cylinder, given x or z coord
public static final float BOT_TEX_S( float x) { return   ((x) * .5f + .5f);}
public static final float BOT_TEX_T(float z) { return   ((z) * .5f + .5f);}
public static final float  TOP_TEX_S(float x) { return   BOT_TEX_S(x);}
public static final float TOP_TEX_T( float z) {return    (1.0f - BOT_TEX_T(z));}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoCylinder class.
//
// Use: internal

public static void initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SoSubNode.SO__NODE_INIT_CLASS(SoCylinder.class, "Cylinder", SoShape.class);
}


// Doc in parent.
public void
rayPick(SoRayPickAction action)
{
  if (!shouldRayPick(action)) return;


  int flags = 0;
  int p = this.parts.getValue();
  if ((p & Part.SIDES.getValue())!=0) flags |= SoPick.SOPICK_SIDES;
  if ((p & Part.TOP.getValue())!=0) flags |= SoPick.SOPICK_TOP;
  if ((p & Part.BOTTOM.getValue())!=0) flags |= SoPick.SOPICK_BOTTOM;

  SoMaterialBindingElement.Binding bind =
    SoMaterialBindingElement.get(action.getState());
  if (bind == SoMaterialBindingElement.Binding.PER_PART ||
      bind == SoMaterialBindingElement.Binding.PER_PART_INDEXED)
    flags |= SoPick.SOPICK_MATERIAL_PER_PART;

  SoPick.sopick_pick_cylinder(this.radius.getValue(),
                       this.height.getValue(),
                       flags,
                       this, action);
}


}
