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
 |      This file defines the SoCube node class.
 |
 |   Author(s)          : Paul S. Strauss, Nick Thompson
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import com.jogamp.opengl.GL2;

import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec3fSingle;
import jscenegraph.database.inventor.SbVec4f;
import jscenegraph.database.inventor.SoMachine;
import jscenegraph.database.inventor.SoPickedPoint;
import jscenegraph.database.inventor.SoPrimitiveVertex;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoRayPickAction;
import jscenegraph.database.inventor.bundles.SoMaterialBundle;
import jscenegraph.database.inventor.details.SoCubeDetail;
import jscenegraph.database.inventor.details.SoDetail;
import jscenegraph.database.inventor.elements.SoComplexityElement;
import jscenegraph.database.inventor.elements.SoComplexityTypeElement;
import jscenegraph.database.inventor.elements.SoDrawStyleElement;
import jscenegraph.database.inventor.elements.SoGLCacheContextElement;
import jscenegraph.database.inventor.elements.SoGLTextureEnabledElement;
import jscenegraph.database.inventor.elements.SoLazyElement;
import jscenegraph.database.inventor.elements.SoLightModelElement;
import jscenegraph.database.inventor.elements.SoMaterialBindingElement;
import jscenegraph.database.inventor.elements.SoModelMatrixElement;
import jscenegraph.database.inventor.elements.SoTextureCoordinateElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFFloat;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.mevis.inventor.misc.SoVBO;
import jscenegraph.port.CharPtr;
import jscenegraph.port.FloatPtr;
import jscenegraph.port.IntPtr;


////////////////////////////////////////////////////////////////////////////////
//! Cube shape node.
/*!
\class SoCube
\ingroup Nodes
This node represents a cuboid aligned with the coordinate axes. By
default, the cube is centered at (0,0,0) and measures 2 units in each
dimension, from -1 to +1. The cube is transformed by the current
cumulative transformation and is drawn with the current lighting
model, drawing style, material, and geometric complexity.


If the current material binding is <tt>PER_PART</tt>, <tt>PER_PART_INDEXED</tt>,
<tt>PER_FACE</tt>, or <tt>PER_FACE_INDEXED</tt>, materials will be bound to the
faces of the cube in this order: front, back, left, right, top, and
bottom.


Textures are applied individually to each face of the cube; the entire
texture goes on each face. On the front, back, right, and left sides
of the cube, the texture is applied right side up. On the top, the
texture appears right side up when the top of the cube is tilted
toward the camera. On the bottom, the texture appears right side up
when the top of the cube is tilted away from the camera.

\par File Format/Default
\par
\code
Cube {
width 2
height 2
depth 2
}
\endcode

\par Action Behavior
\par
SoGLRenderAction
<BR> Draws cube based on the current coordinates, materials, drawing style, and so on. 
\par
SoRayPickAction
<BR> Intersects the ray with the cube. The face of the cube that was picked is available from the SoCubeDetail. 
\par
SoGetBoundingBoxAction
<BR> Computes the bounding box that encloses the cube. 
\par
SoCallbackAction
<BR> If any triangle callbacks are registered with the action, they will be invoked for each successive triangle that approximates the cube. 

\par See Also
\par
SoCone, SoCubeDetail, SoCylinder, SoSphere
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoCube extends SoShape {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoCube.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoCube.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoCube.class); }    	  	
	
	

	  public
		    //! \name Fields
		    //@{
		    //! Size in x dimension
final		    SoSFFloat           width = new SoSFFloat();          
		    //! Size in y dimension
public		    final SoSFFloat           height = new SoSFFloat();         
		    //! Size in z dimension
public		    final SoSFFloat           depth = new SoSFFloat();          

  private
    static final SbVec3f[]      coords = new SbVec3f[8];      //!< Corner coordinates
  private    static final SbVec2f[]      texCoords = new SbVec2f[4];   //!< Face corner texture coordinates
  private    static final SbVec3f[]      normals = new SbVec3f[6];     //!< Face normals
  private    static final SbVec3f[]      edgeNormals = new SbVec3f[12];//!< Edge normals (for wire-frame)
  private    static final SbVec3f[][] verts = new SbVec3f[6][4];  //!< Vertex references to coords
  
  static {
	  for(int i=0; i<8; i++) coords[i] = new SbVec3f();
	  for(int i=0; i<4; i++) texCoords[i] = new SbVec2f();
	  for(int i=0; i<6; i++) normals[i] = new SbVec3f();
	  for(int i=0; i<12; i++) edgeNormals[i] = new SbVec3f();
  }

    //! This flag indicates whether picking is done on a real cube or a
    //! cube that is just a bounding box representing another shape. If
    //! this flag is TRUE, a pick on the cube should not generate a
    //! detail, since the bounding box is not really in the picked path.
  private    boolean              pickingBoundingBox;
  
  //! The state of the last cone tesselation,
  //! so that the VBO can be reused if nothing changed.
  private class CacheState extends SimpleVertexArrayCache {
	  
  }
	
  private final CacheState _cache = new CacheState();

  
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoCube()
//
////////////////////////////////////////////////////////////////////////
{
  nodeHeader.SO_NODE_CONSTRUCTOR();

  nodeHeader.SO_NODE_ADD_SFIELD(width,"width",  (2.0f));
  nodeHeader.SO_NODE_ADD_SFIELD(height,"height", (2.0f));
  nodeHeader.SO_NODE_ADD_SFIELD(depth,"depth",     (2.0f));

  isBuiltIn = true;

  if (nodeHeader.SO_NODE_IS_FIRST_INSTANCE()) {
    // Initialize corner coordinate values
    coords[0].setValue(-1.0f,  1.0f, -1.0f);   // Left  Top    Back
    coords[1].setValue( 1.0f,  1.0f, -1.0f);   // Right Top    Back
    coords[2].setValue(-1.0f, -1.0f, -1.0f);   // Left  Bottom Back
    coords[3].setValue( 1.0f, -1.0f, -1.0f);   // Right Bottom Back
    coords[4].setValue(-1.0f,  1.0f,  1.0f);   // Left  Top    Front
    coords[5].setValue( 1.0f,  1.0f,  1.0f);   // Right Top    Front
    coords[6].setValue(-1.0f, -1.0f,  1.0f);   // Left  Bottom Front
    coords[7].setValue( 1.0f, -1.0f,  1.0f);   // Right Bottom Front

    // Initialize face vertices to point into coords. The order of
    // vertices around the faces is chosen so that the texture
    // coordinates match up: texture coord (0,0) is at the first
    // vertex and (1,1) is at the third. The vertices obey the
    // right-hand rule for each face.
    verts[1][2] = verts[2][3] = verts[4][3] = coords[0];
    verts[1][3] = verts[3][2] = verts[4][2] = coords[1];
    verts[1][1] = verts[2][0] = verts[5][0] = coords[2];
    verts[1][0] = verts[3][1] = verts[5][1] = coords[3];
    verts[0][3] = verts[2][2] = verts[4][0] = coords[4];
    verts[0][2] = verts[3][3] = verts[4][1] = coords[5];
    verts[0][0] = verts[2][1] = verts[5][3] = coords[6];
    verts[0][1] = verts[3][0] = verts[5][2] = coords[7];

    // Initialize texture coordinates. These are for the 4 corners of
    // each face, starting at the lower left corner
    texCoords[0].setValue(0.0f, 0.0f);
    texCoords[1].setValue(1.0f, 0.0f);
    texCoords[2].setValue(1.0f, 1.0f);
    texCoords[3].setValue(0.0f, 1.0f);

    // Initialize face normals
    normals[0].setValue( 0.0f,  0.0f,  1.0f);  // Front
    normals[1].setValue( 0.0f,  0.0f, -1.0f);  // Back
    normals[2].setValue(-1.0f,  0.0f,  0.0f);  // Left
    normals[3].setValue( 1.0f,  0.0f,  0.0f);  // Right
    normals[4].setValue( 0.0f,  1.0f,  0.0f);  // Top
    normals[5].setValue( 0.0f, -1.0f,  0.0f);  // Bottom

  }
}

  

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Performs GL rendering of a cube.
//
// Use: extender

public void
GLRender(SoGLRenderAction action)
//
////////////////////////////////////////////////////////////////////////
{
  // First see if the object is visible and should be rendered now
  if (! shouldGLRender(action))
    return;

  SoState state = action.getState();
  // See if texturing is enabled
  boolean doTextures = SoGLTextureEnabledElement.get(state);

  // Render the cube. The GLRenderGeneric() method handles any
  // case.
  boolean sendNormals = (SoLightModelElement.get(state) !=
    SoLightModelElement.Model.BASE_COLOR);

  if ((SoDrawStyleElement.get(state) == SoDrawStyleElement.Style.FILLED) &&
      (SoVBO.isVertexArrayRenderingAllowed() && SoVBO.shouldUseVBO(state, SoVBO.getVboMinimumSizeLimit()+1 ))) {
    // only use vertex array rendering if VA/VBO is allowed and if draw style is FILLED.
    GLRenderVertexArray(action, sendNormals, doTextures);
  } else {
    GLRenderGeneric(action, sendNormals, doTextures);
  }
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Implements ray picking. We could just use the default mechanism,
//    generating primitives, but this would be inefficient if the
//    complexity is above 0.5. Therefore, we make sure that the
//    complexity is low and then use the primitive generation.
//
// Use: extender

public void
rayPick(SoRayPickAction action)
//
////////////////////////////////////////////////////////////////////////
{
  // First see if the object is pickable
  if (! shouldRayPick(action))
    return;

  // Save the state so we don't affect the real complexity
  action.getState().push();

  // Change the complexity
  SoComplexityElement.set(action.getState(), 0.0f);
  SoComplexityTypeElement.set(action.getState(),
    SoComplexityTypeElement.Type.OBJECT_SPACE);

  // Pick using primitive generation. Make sure we know that we are
  // really picking on a real cube, not just a bounding box of
  // another shape.
  pickingBoundingBox = false;
  super.rayPick(action);

  // Restore the state
  action.getState().pop();
}


	
	@Override
	public void computeBBox(SoAction action, SbBox3f box, SbVec3f center) {
		  final float[]       w = new float[1], h = new float[1], d = new float[1];

		  getSize(w, h, d);
		  box.setBounds(-w[0], -h[0], -d[0], w[0], h[0], d[0]);
		  center.setValue(0.0f, 0.0f, 0.0f);
	}
	

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Overrides standard method to create an SoCubeDetail instance
//    representing a picked intersection with a triangle that is half
//    of the face of a cube.
//
// Use: protected, virtual

protected SoDetail 
createTriangleDetail(SoRayPickAction action,
                             final SoPrimitiveVertex v1,
                             final SoPrimitiveVertex v2,
                             final SoPrimitiveVertex v3,
                             final SoPickedPoint pp)
                             //
                             ////////////////////////////////////////////////////////////////////////
{
  SoCubeDetail        detail;

  // Don't create a detail if the pick operation was performed on a
  // bounding box cube, not a real cube
  if (pickingBoundingBox)
    return null;

  detail = new SoCubeDetail();

  // The part code should be the same in all three details, so just use one
  detail.setPart((( SoCubeDetail ) v1.getDetail()).getPart());

  return detail;
}

	

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Computes real half-width, -height, -depth.
//
// Use: private

private void
getSize(final float[] hWidth, final float[] hHeight, final float[] hDepth) 
//
////////////////////////////////////////////////////////////////////////
{
  hWidth[0]  = ( width.isIgnored() ? 1.0f :  width.getValue() / 2.0f);
  hHeight[0] = (height.isIgnored() ? 1.0f : height.getValue() / 2.0f);
  hDepth[0]  = ( depth.isIgnored() ? 1.0f :  depth.getValue() / 2.0f);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Computes real half-width, -height, -depth.
//
// Use: private

private void
getSize(float[] hWidth_hHeight_hDepth)
//
////////////////////////////////////////////////////////////////////////
{
	hWidth_hHeight_hDepth[0]  = ( width.isIgnored() ? 1.0f :  width.getValue() / 2.0f);
	hWidth_hHeight_hDepth[1] = (height.isIgnored() ? 1.0f : height.getValue() / 2.0f);
	hWidth_hHeight_hDepth[2]  = ( depth.isIgnored() ? 1.0f :  depth.getValue() / 2.0f);
}

private void
getSize(SbVec3f hWidth_hHeight_hDepth)
//
////////////////////////////////////////////////////////////////////////
{
	hWidth_hHeight_hDepth.setValue(0, ( width.isIgnored() ? 1.0f :  width.getValue() / 2.0f));
	hWidth_hHeight_hDepth.setValue(1, (height.isIgnored() ? 1.0f : height.getValue() / 2.0f));
	hWidth_hHeight_hDepth.setValue(2, ( depth.isIgnored() ? 1.0f :  depth.getValue() / 2.0f));
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does GL rendering of a cube representing the given bounding box.
//    This is used for BOUNDING_BOX complexity. It does the minimum
//    work necessary.
//
// Use: private

public void
GLRenderBoundingBox(SoGLRenderAction action, final SbBox3f bbox)
//
////////////////////////////////////////////////////////////////////////
{
  int                 face, vert;
  final SoMaterialBundle    mb = new SoMaterialBundle(action);
  final SbVec3f             scale = new SbVec3f(), tmp = new SbVec3f();

  // Make sure textures are disabled, just to speed things up
  action.getState().push();
  SoGLTextureEnabledElement.set(action.getState(), false);

  // Make sure first material is sent if necessary
  mb.sendFirst();

  // Scale and translate the cube to the correct spot
  final SbVec3f       translate = bbox.getCenter();
  final SbVec3f size = new SbVec3f();
  bbox.getSize(size);
  scale.copyFrom(size.operator_mul(0.5f));

  GL2 gl2 = SoGLCacheContextElement.get(action.getState());  
  
  for (face = 0; face < 6; face++) {

    if (! mb.isColorOnly())
      gl2.glNormal3fv(normals[face].getValueRead(),0);

    gl2.glBegin(GL2.GL_POLYGON);

    for (vert = 0; vert < 4; vert++)
      gl2.glVertex3fv((SCALE(verts[face][vert],tmp,scale).operator_add(translate)).getValueRead(),0);

    gl2.glEnd();
  }

  // Restore state
  action.getState().pop();
  mb.destructor(); // java port
}

//
// Macro to multiply out coordinates to avoid extra GL calls:
//
private SbVec3f SCALE( SbVec3f vec, SbVec3f tmp, SbVec3f scale) {
	float[] pt = vec.getValueRead();
	tmp.setValue(0, (pt)[0]*scale.getValueRead()[0]);
			tmp.setValue(1, (pt)[1]*scale.getValueRead()[1]); 
		   tmp.setValue(2, (pt)[2]*scale.getValueRead()[2]);

	return tmp;

}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Does picking of a cube representing the given bounding box. This
//    is used for BOUNDING_BOX complexity. It uses the same code as
//    for rayPick(), except that it makes sure the cube is transformed
//    first to where the bounding box is.
//
// Use: private

public void
rayPickBoundingBox(SoRayPickAction action, final SbBox3f bbox)
//
////////////////////////////////////////////////////////////////////////
{
  // Save the state so we don't affect the real complexity
  action.getState().push();

  // Change the complexity
  SoComplexityElement.set(action.getState(), 0.0f);
  SoComplexityTypeElement.set(action.getState(),
    SoComplexityTypeElement.Type.OBJECT_SPACE);

  // Change the current matrix to scale and translate the cube to the
  // correct spot. (We can't just use an extra matrix passed to
  // computeObjectSpaceRay(), since the points generated by
  // generatePrimitives() have to be transformed, not just the ray.)
  final SbVec3fSingle             size = new SbVec3fSingle();
  bbox.getSize(size);

  // If any of the dimensions is 0, beef it up a little bit to avoid
  // scaling by 0
  if (size.getValueRead()[0] == 0.0)
    size.getValue()[0] = 0.00001f;
  if (size.getValueRead()[1] == 0.0)
    size.getValue()[1] = 0.00001f;
  if (size.getValueRead()[2] == 0.0)
    size.getValue()[2] = 0.00001f;

  SoModelMatrixElement.translateBy(action.getState(), this,
    bbox.getCenter());
  SoModelMatrixElement.scaleBy(action.getState(), this,
     size.operator_mul(0.5f));

  // Compute the picking ray in the space of the shape
  computeObjectSpaceRay(action);

  // Pick using primitive generation. Make sure we know that we are
  // picking on just a bounding box of another shape, so details
  // won't be created.
  pickingBoundingBox = true;
  generatePrimitives(action);

  // Restore the state
  action.getState().pop();
}


void GLRenderVertexArray(SoGLRenderAction action,
                                 boolean sendNormals, boolean doTextures)
{
  SoState state = action.getState();
  final SbVec3f scale = new SbVec3f();
  getSize(scale);

  boolean              materialPerFace;
  final SbVec3f             pt = new SbVec3f(), norm = new SbVec3f();
  final SoMaterialBundle    mb = new SoMaterialBundle(action);

  materialPerFace = isMaterialPerFace(action);

  // Make sure first material is sent if necessary
  if (materialPerFace) {
    mb.setUpMultiple();
  }
  mb.sendFirst();

  _cache.useColors = true;
  _cache.useNormals = sendNormals;
  _cache.useTexCoords = doTextures;

  // 6 faces with 2 triangles
  int numVertices = 6 * 2 * 3;
  
  _cache.numVertices = numVertices;

  // vertex (3 floats) + normal (3 floats) + texcood (2 floats) + color (1 uint32)
  int numBytes = ((3+3+2) * 4 + 4) * numVertices; 
  CharPtr data = new CharPtr(numBytes);
  FloatPtr verticesPtr = new FloatPtr(data);
  FloatPtr normalsPtr = verticesPtr.operator_add(numVertices*3);
  FloatPtr texCoordsPtr = normalsPtr.operator_add(numVertices*3);
  IntPtr colorsPtr = new IntPtr(texCoordsPtr.operator_add(numVertices*2));
  CharPtr vertexOffset = new CharPtr(verticesPtr); 
  CharPtr normalOffset = new CharPtr(normalsPtr); 
  CharPtr texCoordOffset = new CharPtr(texCoordsPtr); 
  CharPtr colorOffset = new CharPtr(colorsPtr);

  int[] colors =  SoLazyElement.getPackedColors(state);
  int color = colors[0];
  final SbVec3f normal = new SbVec3f();
  for (int face = 0; face < 6; face++) {
    if (materialPerFace && face > 0) {
      color = colors[face];
    }
    if (sendNormals) {
      normal.copyFrom(normals[face]);
    }
    int indices[] = {0,1,2, 0,2,3};
    for (int tri = 0; tri < 6; tri++) {
      int vert = indices[tri];
      final int[] swappedColor = new int[1];
      SoMachine.DGL_HTON_INT32(swappedColor, color);
      colorsPtr.asterisk(swappedColor[0]); colorsPtr.plusPlus();
      if (doTextures) {
        float[] tmp = texCoords[vert].getValueRead();
        texCoordsPtr.asterisk(tmp[0]); texCoordsPtr.plusPlus(); 
        texCoordsPtr.asterisk(tmp[1]); texCoordsPtr.plusPlus(); 
      }
      if (sendNormals) {
        final float[] tmp = normal.getValueRead();
        normalsPtr.asterisk(tmp[0]); normalsPtr.plusPlus();
        normalsPtr.asterisk(tmp[1]); normalsPtr.plusPlus();
        normalsPtr.asterisk(tmp[2]); normalsPtr.plusPlus();
      }
      verticesPtr.asterisk( (verts[face][vert]).getValueRead()[0]*scale.getValueRead()[0]); verticesPtr.plusPlus();
      verticesPtr.asterisk( (verts[face][vert]).getValueRead()[1]*scale.getValueRead()[1]); verticesPtr.plusPlus();
      verticesPtr.asterisk( (verts[face][vert]).getValueRead()[2]*scale.getValueRead()[2]); verticesPtr.plusPlus();
    }
  }
  
  GL2 gl2 = action.getCacheContext();

  _cache.vbo.setData(numBytes, null, 0, state);
  _cache.vbo.bind(state);
  _cache.vbo.updateData(gl2,data);
  
  _cache.vertexOffset = (vertexOffset.minus(data));
  _cache.colorOffset = (colorOffset.minus(data));
  _cache.normalOffset = (normalOffset.minus(data));
  _cache.texCoordOffset = (texCoordOffset.minus(data));

  _cache.drawArrays(this, action, GL2.GL_TRIANGLES);
  _cache.vbo.unbind(gl2);
  mb.destructor(); // java port
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Generates triangles representing a cube.
//
// Use: protected

protected void
generatePrimitives(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
    boolean              materialPerFace;
    int                 numDivisions, face, vert;
    float               s;
    final SbVec3fSingle             pt = new SbVec3fSingle(), norm = new SbVec3fSingle();
    final float[]               w = new float[1], h = new float[1], d = new float[1];
    final SbVec4f             tex = new SbVec4f();
    boolean              genTexCoords = false;
    final SoPrimitiveVertex   pv = new SoPrimitiveVertex();
    final SoCubeDetail        detail = new SoCubeDetail();
    SoTextureCoordinateElement    tce = null;

    materialPerFace = isMaterialPerFace(action);
    numDivisions    = computeNumDivisions(action);

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

    getSize(w, h, d);


    for (face = 0; face < 6; face++) {

	if (face == 0 || materialPerFace)
	    pv.setMaterialIndex(face);
	pv.setNormal(normals[face]);
	detail.setPart(face);

	// Simple case of one polygon per face 
	if (numDivisions == 1) {
	    beginShape(action, SoShape.TriangleShape.TRIANGLE_STRIP);
	    vert = 3;
	    pt.setValue((verts[face][vert]).getValueRead()[0] * w[0],
			(verts[face][vert]).getValueRead()[1] * h[0],
			(verts[face][vert]).getValueRead()[2] * d[0]);
	    if (genTexCoords) {
		tex.getValue()[0] = texCoords[vert].getValueRead()[0];
		tex.getValue()[1] = texCoords[vert].getValueRead()[1];
	    }
	    else
		tex.copyFrom( tce.get(pt, normals[face]));
	    pv.setPoint(pt);
	    pv.setTextureCoords(tex);
	    shapeVertex(pv);
	    vert = 0;
	    pt.setValue((verts[face][vert]).getValueRead()[0] * w[0],
			(verts[face][vert]).getValueRead()[1] * h[0],
			(verts[face][vert]).getValueRead()[2] * d[0]);
	    if (genTexCoords) {
		tex.getValue()[0] = texCoords[vert].getValueRead()[0];
		tex.getValue()[1] = texCoords[vert].getValueRead()[1];
	    }
	    else
		tex.copyFrom( tce.get(pt, normals[face]));
	    pv.setPoint(pt);
	    pv.setTextureCoords(tex);
	    shapeVertex(pv);
	    vert = 2;
	    pt.setValue((verts[face][vert]).getValueRead()[0] * w[0],
			(verts[face][vert]).getValueRead()[1] * h[0],
			(verts[face][vert]).getValueRead()[2] * d[0]);
	    if (genTexCoords) {
		tex.getValue()[0] = texCoords[vert].getValueRead()[0];
		tex.getValue()[1] = texCoords[vert].getValueRead()[1];
	    }
	    else
		tex.copyFrom( tce.get(pt, normals[face]));
	    pv.setPoint(pt);
	    pv.setTextureCoords(tex);
	    shapeVertex(pv);
	    vert = 1;
	    pt.setValue((verts[face][vert]).getValueRead()[0] * w[0],
			(verts[face][vert]).getValueRead()[1] * h[0],
			(verts[face][vert]).getValueRead()[2] * d[0]);
	    if (genTexCoords) {
		tex.getValue()[0] = texCoords[vert].getValueRead()[0];
		tex.getValue()[1] = texCoords[vert].getValueRead()[1];
	    }
	    else
		tex.copyFrom( tce.get(pt, normals[face]));
	    pv.setPoint(pt);
	    pv.setTextureCoords(tex);
	    shapeVertex(pv);
	    endShape();
	}

	// More than one polygon per face
	else {
	    float       di = 1.0f / numDivisions;
	    final SbVec3f     topPoint = new SbVec3f(),    botPoint = new SbVec3f(),    nextBotPoint = new SbVec3f();
	    final SbVec3f     horizSpace = new SbVec3f(), vertSpace = new SbVec3f();
	    int         strip, rect;

	    botPoint.copyFrom(verts[face][0]);

	    // Compute spacing between adjacent points in both directions
	    horizSpace.copyFrom( (verts[face][1].operator_minus(botPoint)).operator_mul(di));
	    vertSpace.copyFrom( (verts[face][3].operator_minus(botPoint)).operator_mul(di));

	    // For each horizontal strip
	    for (strip = 0; strip < numDivisions; strip++) {

		// Compute current top point. Save it to use as bottom
		// of next strip
		topPoint.copyFrom( botPoint.operator_add(vertSpace));
		nextBotPoint.copyFrom(topPoint);

		beginShape(action, SoShape.TriangleShape.TRIANGLE_STRIP);

		// Send points at left end of strip
		s = 0.0f;
		pt.copyFrom( topPoint);
		pt.getValue()[0] *= w[0];
		pt.getValue()[1] *= h[0];
		pt.getValue()[2] *= d[0];
		if (genTexCoords) {
		    tex.getValue()[0] = s;
		    tex.getValue()[1] = (strip + 1) * di;
		}
		else
		    tex.copyFrom( tce.get(pt, normals[face]));
		pv.setPoint(pt);
		pv.setTextureCoords(tex);
		shapeVertex(pv);
		pt.copyFrom( botPoint);
		pt.getValue()[0] *= w[0];
		pt.getValue()[1] *= h[0];
		pt.getValue()[2] *= d[0];
		if (genTexCoords) {
		    tex.getValue()[0] = s;
		    tex.getValue()[1] = strip * di;
		}
		else
		    tex.copyFrom(tce.get(pt, normals[face]));
		pv.setPoint(pt);
		pv.setTextureCoords(tex);
		shapeVertex(pv);

		// For each rectangular piece of strip
		for (rect = 0; rect < numDivisions; rect++) {

		    // Go to next rect
		    topPoint.operator_add_equal( horizSpace);
		    botPoint.operator_add_equal( horizSpace);
		    s += di;

		    // Send points at right side of rect
		    pt.copyFrom( topPoint);
		    pt.getValue()[0] *= w[0];
		    pt.getValue()[1] *= h[0];
		    pt.getValue()[2] *= d[0];
		    if (genTexCoords) {
			tex.getValue()[0] = s;
			tex.getValue()[1] = (strip + 1) * di;
		    }
		    else
			tex.copyFrom( tce.get(pt, normals[face]));
		    pv.setPoint(pt);
		    pv.setTextureCoords(tex);
		    shapeVertex(pv);
		    pt.copyFrom( botPoint);
		    pt.getValue()[0] *= w[0];
		    pt.getValue()[1] *= h[0];
		    pt.getValue()[2] *= d[0];
		    if (genTexCoords) {
			tex.getValue()[0] = s;
			tex.getValue()[1] = strip * di;
		    }
		    else
			tex.copyFrom( tce.get(pt, normals[face]));
		    pv.setPoint(pt);
		    pv.setTextureCoords(tex);
		    shapeVertex(pv);
		}

		endShape();

		// Get ready for next strip
		botPoint.copyFrom(nextBotPoint);
	    }
	}
    }
    pv.destructor(); // java port
    detail.destructor(); // java port
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns TRUE if per face materials are specified.
//
// Use: private

private boolean
isMaterialPerFace(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
  SoMaterialBindingElement.Binding binding;

  binding = SoMaterialBindingElement.get(action.getState());

  return (binding == SoMaterialBindingElement.Binding.PER_PART ||
    binding == SoMaterialBindingElement.Binding.PER_PART_INDEXED ||
    binding == SoMaterialBindingElement.Binding.PER_FACE ||
    binding == SoMaterialBindingElement.Binding.PER_FACE_INDEXED);
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Computes number of divisions per side based on complexity.
//
// Use: private

private int
computeNumDivisions(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
  int         numDivisions = -1;
  float       complexity;

  switch (SoComplexityTypeElement.get(action.getState())) {

      case OBJECT_SPACE:

        // In object space, the number of divisions is greater than 1
        // only for complexity values > 0.5. The maximum value is 16,
        // when complexity = 1.
        complexity = SoComplexityElement.get(action.getState());
        numDivisions = (complexity <= 0.5 ? 1 :
          -14 + (int) (complexity * 30.0));
        break;

      case SCREEN_SPACE:

        // In screen space, the number of divisions is based on the
        // complexity and the size of the cube when projected onto the
        // screen.
        short   maxSize;
        {
          final SbVec3f     p = new SbVec3f();
          final SbVec2s     rectSize = new SbVec2s();

          getSize(p);
          getScreenSize(action.getState(), new SbBox3f(p.operator_minus(), p), rectSize);
          maxSize = (rectSize.getValue()[0] > rectSize.getValue()[1] ? rectSize.getValue()[0] : rectSize.getValue()[1]);
        }

        // Square complexity to get a more even increase in the number
        // of tesselation squares. Maximum bound is 1/4 the number of
        // pixels per side.
        complexity = SoComplexityElement.get(action.getState());
        numDivisions = 1 + (int) (0.25 * maxSize * complexity * complexity);
        break;

      case BOUNDING_BOX:

        // Most shapes do not have to handle this case, since it is
        // handled for them. However, since it is handled by drawing
        // the shape as a cube, the SoCube class has to handle it.
        numDivisions = 1;
        break;
  }

  return numDivisions;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Generic rendering of cube with or without normals, with or
//    without texture coordinates.
//
// Use: private

private void
GLRenderGeneric(SoGLRenderAction action,
			boolean sendNormals, boolean doTextures)
//
////////////////////////////////////////////////////////////////////////
{
    final SbVec3f scale = new SbVec3f(), tmp = new SbVec3f();
    getSize(scale);

    boolean              materialPerFace;
    int                 numDivisions, face, vert;
    float               s;
    final SbVec3f             pt = new SbVec3f(), norm = new SbVec3f();
    final SoMaterialBundle    mb = new SoMaterialBundle(action);

    materialPerFace = isMaterialPerFace(action);
    numDivisions    = computeNumDivisions(action);

    // Make sure first material is sent if necessary
    if (materialPerFace)
	mb.setUpMultiple();
    mb.sendFirst();
    
    GL2 gl2 = action.getCacheContext();

    if (numDivisions == 1)
	gl2.glBegin(GL2.GL_QUADS);

    for (face = 0; face < 6; face++) {

	if (materialPerFace && face > 0)
	    mb.send(face, numDivisions == 1);
	if (sendNormals)
	    gl2.glNormal3fv(normals[face].getValueRead(),0);

	// Simple case of one polygon per face 
	if (numDivisions == 1) {
	    for (vert = 0; vert < 4; vert++) {
		if (doTextures)
		    gl2.glTexCoord2fv(texCoords[vert].getValueRead(),0);
		gl2.glVertex3fv(SCALE(verts[face][vert],tmp,scale).getValueRead(),0);
	    }
	}

	// More than one polygon per face
	else {
	    float       di = 1.0f / numDivisions;
	    final SbVec3f     topPoint = new SbVec3f(),    botPoint = new SbVec3f(),    nextBotPoint = new SbVec3f();
	    final SbVec3f     horizSpace = new SbVec3f(), vertSpace = new SbVec3f();
	    int         strip, rect;

	    botPoint.copyFrom(verts[face][0]);

	    // Compute spacing between adjacent points in both directions
	    horizSpace.copyFrom( (verts[face][1].operator_minus(botPoint)).operator_mul(di));
	    vertSpace.copyFrom( (verts[face][3].operator_minus(botPoint)).operator_mul(di));

	    // For each horizontal strip
	    for (strip = 0; strip < numDivisions; strip++) {

		// Compute current top point. Save it to use as bottom
		// of next strip
		topPoint.copyFrom( botPoint.operator_add(vertSpace));
		nextBotPoint.copyFrom(topPoint); 

		gl2.glBegin(GL2.GL_TRIANGLE_STRIP);

		// Send points at left end of strip
		s = 0.0f;
		if (doTextures) {
		    gl2.glTexCoord2f(s, (strip + 1) * di);
		    gl2.glVertex3fv(SCALE(topPoint,tmp,scale).getValueRead(),0);
		    gl2.glTexCoord2f(s, strip * di);
		    gl2.glVertex3fv(SCALE(botPoint,tmp,scale).getValueRead(),0);
		}
		else {
		    gl2.glVertex3fv(SCALE(topPoint,tmp,scale).getValueRead(),0);
		    gl2.glVertex3fv(SCALE(botPoint,tmp,scale).getValueRead(),0);
		}

		// For each rectangular piece of strip
		for (rect = 0; rect < numDivisions; rect++) {

		    // Go to next rect
		    topPoint.operator_add_equal(horizSpace);
		    botPoint.operator_add_equal(horizSpace);
		    s += di;

		    // Send points at right side of rect
		    if (doTextures) {
			gl2.glTexCoord2f(s, (strip + 1) * di);
			gl2.glVertex3fv(SCALE(topPoint,tmp,scale).getValueRead(),0);
			gl2.glTexCoord2f(s, strip * di);
			gl2.glVertex3fv(SCALE(botPoint,tmp,scale).getValueRead(),0);
		    }
		    else {
			gl2.glVertex3fv(SCALE(topPoint,tmp,scale).getValueRead(),0);
			gl2.glVertex3fv(SCALE(botPoint,tmp,scale).getValueRead(),0);
		    }
		}

		gl2.glEnd();

		// Get ready for next strip
		botPoint.copyFrom(nextBotPoint);
	    }
	}
    }

    if (numDivisions == 1)
	gl2.glEnd();
    
    mb.destructor(); // java port
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoCube class.
//
// Use: internal

public static void initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SoSubNode.SO__NODE_INIT_CLASS(SoCube.class, "Cube", SoShape.class);
}


}
