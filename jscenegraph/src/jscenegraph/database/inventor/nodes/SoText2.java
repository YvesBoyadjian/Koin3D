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
 |      This file defines the SoText2 node class.
 |
 |   Author(s)          : Thad Beier
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import java.nio.CharBuffer;
import java.nio.ShortBuffer;

import com.jogamp.opengl.GL2;

import jscenegraph.coin3d.inventor.elements.SoGLMultiTextureEnabledElement;
import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec3fSingle;
import jscenegraph.database.inventor.SbVec4f;
import jscenegraph.database.inventor.SbViewportRegion;
import jscenegraph.database.inventor.SoPickedPoint;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoRayPickAction;
import jscenegraph.database.inventor.bundles.SoMaterialBundle;
import jscenegraph.database.inventor.details.SoTextDetail;
import jscenegraph.database.inventor.elements.SoGLCacheContextElement;
import jscenegraph.database.inventor.elements.SoLazyElement;
import jscenegraph.database.inventor.elements.SoModelMatrixElement;
import jscenegraph.database.inventor.elements.SoProjectionMatrixElement;
import jscenegraph.database.inventor.elements.SoViewingMatrixElement;
import jscenegraph.database.inventor.elements.SoViewportRegionElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFString;
import jscenegraph.database.inventor.fields.SoSFEnum;
import jscenegraph.database.inventor.fields.SoSFFloat;
import jscenegraph.database.inventor.misc.SoState;

// TODO YB : The code of this class is different from Coin3D 

////////////////////////////////////////////////////////////////////////////////
//! Screen-aligned 2D text shape node.
/*!
\class SoText2
\ingroup Nodes
This node defines one or more strings of 2D text. The text is always
aligned horizontally with the screen and does not change size with
distance in a perspective projection. The text origin is at (0,0,0)
after applying the current transformation. Rotations and scales have
no effect on the orientation or size of 2D text, just the location.


SoText2 uses the current font to determine the typeface and size.
The text is always drawn with the diffuse color of the current
material; it is not lit, regardless of the lighting model.
Furthermore, 2D text can not be textured, and it ignores the current
drawing style and complexity.


Because 2D text is screen-aligned, it has some unusual
characteristics.  For example, the 3D bounding box surrounding a 2D
text string depends on the current camera and the current viewport
size, since changing the field of view or the mapping onto the window
changes the relative size of the text with respect to the rest of the
scene. This has implications for caching as well, since a render cache
in an SoSeparator that contains an SoText2 node depends on the
current camera.

\par File Format/Default
\par
\code
Text2 {
  string ""
  spacing 1
  justification LEFT
}
\endcode

\par Action Behavior
\par
SoGLRenderAction
<BR> Draws text based on the current font, at a location based on the current transformation. 
\par
SoRayPickAction
<BR> Performs a pick on the text. Text will be picked if the picking ray intersects the bounding box of the strings. The string index and character position are available from the SoTextDetail. 
\par
SoGetBoundingBoxAction
<BR> Computes the bounding box that encloses the text. 

\par See Also
\par
SoFont, SoText3, SoTextDetail
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoText2 extends SoShape {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoText2.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoText2.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoText2.class); }    	  	
	
	
	
	public enum Justification {
		LEFT(1),
		RIGHT(2),
		CENTER(3);
		
		Justification(int value) {
			this.value = value;
		}
		private int value;
		
		public int getValue() {
			return value;
		}
	}

	public final SoMFString string = new SoMFString();
	  
	public final    SoSFFloat spacing = new SoSFFloat();
	  
	 public final SoSFEnum justification = new SoSFEnum();

    //! Internal class that allows Text2 nodes to share font
    //! information, GL display lists, etc.
    private SoBitmapFontCache   myFont;
	 

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoText2()
//
////////////////////////////////////////////////////////////////////////
{
    nodeHeader.SO_NODE_CONSTRUCTOR(/*SoText2*/);

    nodeHeader.SO_NODE_ADD_MFIELD(string,"string",   (""));
    nodeHeader.SO_NODE_ADD_SFIELD(spacing,"spacing",  (1.f));
    nodeHeader.SO_NODE_ADD_SFIELD(justification,"justification",    (Justification.LEFT.getValue()));

    // Set up static info for enumerated type field
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Justification.LEFT);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Justification.RIGHT);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Justification.CENTER);

    // Set up info in enumerated type field
    nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(justification,"justification", "Justification");

    isBuiltIn = true;
    myFont = null;
  
    
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
    if (myFont != null) myFont.unref();
    super.destructor();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Static helper routine; given a vector in object space, a
//    transformation matrix to screen (normalized-device coordinate)
//    space, and a viewportRegion, returns the 2D pixel coordinates of
//    the vector (relative to the origin of the viewport).  This
//    actually returns a 3D vector; the z value is just the NDC z value.
//
// Use: internal, static

private SbVec3f
fromObjectSpace(final SbVec3f vector, final SbMatrix matrix,
                final SbViewportRegion vpr)
//
////////////////////////////////////////////////////////////////////////
{
    // First, transform to NDC (-1 to 1 in viewport)
    final SbVec3f ndc = new SbVec3f();
    matrix.multVecMatrix(vector, ndc);

    // And do the viewport transformation:
    SbVec2s vpSize = vpr.getViewportSizePixels();
    final SbVec3f result = new SbVec3f();
    result.setValue(0, (ndc.getValueRead()[0]+1.0f)*vpSize.getValue()[0]/2.0f);
    result.setValue(1, (ndc.getValueRead()[1]+1.0f)*vpSize.getValue()[1]/2.0f);
    // Leave the z coordinate alone
    result.setValue(2, ndc.getValueRead()[2]);

    return result;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Static helper routine; given 2D pixel coordinates, a
//    transformation matrix to object space, and a viewportRegion,
//    return the object-space point (assuming the 2D pixel is at z
//    coordinate 0).
//
// Use: internal, static

private SbVec3f
toObjectSpace(final SbVec3f pixel, final SbMatrix matrix,
              final SbViewportRegion vpr)
//
////////////////////////////////////////////////////////////////////////
{
    // Viewport transformation, to normalized device coordinates:
    SbVec2s vpSize = vpr.getViewportSizePixels();
    final SbVec3fSingle ndc = new SbVec3fSingle();
    ndc.getValue()[0] = pixel.getValueRead()[0]*2.0f/vpSize.getValue()[0] - 1.0f;
    ndc.getValue()[1] = pixel.getValueRead()[1]*2.0f/vpSize.getValue()[1] - 1.0f;
    ndc.getValue()[2] = pixel.getValueRead()[2];

    final SbVec3f result = new SbVec3f();
    matrix.multVecMatrix(ndc, result);

    return result;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    render the text with the current font
//
// Use: protected

public void
GLRender(SoGLRenderAction action)
//
////////////////////////////////////////////////////////////////////////
{
    if (!shouldGLRender(action)) return;

    SoState state = action.getState();

    state.push();

    // Get a font cache we can pull stuff out of
    if (myFont != null) {
        if (!myFont.isRenderValid(state)) {
            myFont.unref(state);
            myFont = null;
        }
    }
    if (myFont == null) {
        myFont = SoBitmapFontCache.getFont(state, true);
        if (myFont == null) {
            state.pop();
            return;
        }
    }

    //The current text must be translated to UCS, unless this
    //translation has already been done.    
    if (myFont != null) myFont.convertToUCS(getNodeId(), string);
    
    // Turn off lighting
    SoLazyElement.setLightModel(state,
                             SoLazyElement.LightModel.BASE_COLOR.getValue());
    // Turn off texturing
    SoGLMultiTextureEnabledElement.set(state, this, 0, false);
    
    myFont.setupToRender(state);

    // Send first color
    SoMaterialBundle mb = new SoMaterialBundle(action);
    mb.sendFirst();
    
    GL2 gl2 = state.getGL2();
    
    // Special-case left-justified, single-line text, which we know
    // starts at (0,0,0) in object space, so we can help caching by
    // avoiding getting the projection/view/model matrices:
    if (string.getNum() == 1 && justification.getValue() == Justification.LEFT.getValue()) {
        gl2.glRasterPos3f(0,0,0);       

        myFont.drawString(0,gl2);
    }
    // General case:
    else {
        final SbMatrix objToScreen = new SbMatrix();
        objToScreen.copyFrom( SoProjectionMatrixElement.get(state));
        objToScreen.copyFrom(
            objToScreen.multLeft(SoViewingMatrixElement.get(state)));
        objToScreen.copyFrom(
            objToScreen.multLeft(SoModelMatrixElement.get(state)));

        SbMatrix screenToObj = objToScreen.inverse();
        
        SbViewportRegion vpr = SoViewportRegionElement.get(state);

        // The origin of the text on the screen is the object-space point
        // 0,0,0:
        SbVec3f screenOrigin =
            fromObjectSpace(new SbVec3f(0,0,0), objToScreen, vpr);
    
        for (int line = 0; line < string.getNum(); line++) {
        
            // Starting position of string, based on justification:
            SbVec3f charPosition = getPixelStringOffset(line).operator_add(
                screenOrigin);       

            // Transform the screen-space starting position into object
            // space, and feed that back to the glRasterPos command (which
            // will turn around and transform it back into screen-space,
            // but oh well).
            SbVec3f lineOrigin = toObjectSpace(charPosition, screenToObj,
                                               vpr);
            gl2.glRasterPos3fv(lineOrigin.getValueRead(),0);
            
            myFont.drawString(line,gl2);
        }
        // Don't auto-cache above, since dependent on camera:
        SoGLCacheContextElement.shouldAutoCache(state,
                SoGLCacheContextElement.AutoCache.DONT_AUTO_CACHE.getValue());
    }
    state.pop();
    mb.destructor();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Implements ray picking
//
// Use: protected

public void
rayPick(SoRayPickAction action)
//
////////////////////////////////////////////////////////////////////////
{
    // First see if the object is pickable
    if (! shouldRayPick(action))
        return;

    SoState state = action.getState();

    state.push();

    // Get a font cache we can pull stuff out of
    if (myFont != null) {
        if (!myFont.isValid(state)) {
            myFont.unref();
            myFont = null;
        }
    }
    if (myFont == null) {
        myFont = SoBitmapFontCache.getFont(state, false);
        if (myFont == null) {
            state.pop();
            return;
        }
    }
    
    //The current text must be translated to UCS, unless this
    //translation has already been done.    
    if (myFont != null) myFont.convertToUCS(getNodeId(), string);
 
    // Tell the action about our current object space
    action.setObjectSpace();

    SbMatrix objToScreen;
    objToScreen = SoProjectionMatrixElement.get(state);
    objToScreen =
        objToScreen.multLeft(SoViewingMatrixElement.get(state));
    objToScreen =
        objToScreen.multLeft(SoModelMatrixElement.get(state));

    SbMatrix screenToObj = objToScreen.inverse();

    SbViewportRegion vpr = SoViewportRegionElement.get(state);

    // The origin of the text on the screen is the object-space point
    // 0,0,0:
    SbVec3f screenOrigin =
        fromObjectSpace(new SbVec3f(0,0,0), objToScreen, vpr);

    for (int line = 0; line < string.getNum(); line++) {
        
        int len = myFont.getNumUCSChars(line);

        ShortBuffer str = myFont.getUCSString(line).asShortBuffer();
        
        // Intersect against each line of text's bounding box:
        final SbBox3f lineBbox = new SbBox3f(), charBbox = new SbBox3f();

        // Starting position of string, based on justification:
        final SbVec3f charPosition = getPixelStringOffset(line).operator_add(
            screenOrigin);

        SbVec3f p0, p1, p2, p3;
        int chr;
        for (chr = 0; chr < len; chr++) {
            myFont.getCharBbox((char)str.get(/*2**/chr), charBbox);

            if (!charBbox.isEmpty()) {

                SbVec3f charMin = charBbox.getMin().operator_add( charPosition);
                SbVec3f charMax = charBbox.getMax().operator_add( charPosition);

                // Extend the line's bounding box by this character's
                // bounding box (both boxes are in screen-space):
                lineBbox.extendBy(charMin);
                lineBbox.extendBy(charMax);

                // Advance to next character...
                charPosition.operator_add_equal(myFont.getCharOffset((char)str.get(/*2**/chr)));
            }
        }
        // And transform line's box into object space:
        SbVec3f min = lineBbox.getMin();
        SbVec3f max = lineBbox.getMax();

        final SbVec3f t = new SbVec3f();
        t.setValue(min.getValueRead()[0], min.getValueRead()[1], screenOrigin.getValueRead()[2]);
        p0 = toObjectSpace(t, screenToObj, vpr);
        t.setValue(max.getValueRead()[0], min.getValueRead()[1], screenOrigin.getValueRead()[2]);
        p1 = toObjectSpace(t, screenToObj, vpr);
        t.setValue(min.getValueRead()[0], max.getValueRead()[1], screenOrigin.getValueRead()[2]);
        p2 = toObjectSpace(t, screenToObj, vpr);
        t.setValue(max.getValueRead()[0], max.getValueRead()[1], screenOrigin.getValueRead()[2]);
        p3 = toObjectSpace(t, screenToObj, vpr);

        // intersect the two triangles:
        final SbVec3f point = new SbVec3f();
        // Info we get back from the pick that we don't need:
        final SbVec3f junk1 = new SbVec3f(); final boolean[] junk2 = new boolean[1];
        if (action.intersect(p0, p1, p2, point, junk1, junk2) ||
            action.intersect(p2, p1, p3, point, junk1, junk2)) {

            SoPickedPoint pp = action.addIntersection(point);
            if (pp != null) {
                SoTextDetail detail = new SoTextDetail();
                detail.setStringIndex(line);

                // Figure out which character was hit:
                // Transform picked point into screen space:
                final SbVec3f screenPoint =
                    fromObjectSpace(pp.getObjectPoint(), objToScreen,
                                    vpr);
                // Figure out which character that corresponds to, by
                // adding on the x-offset of each character until we
                // go past the picked point:
                charPosition.copyFrom( getPixelStringOffset(line).operator_add(
                    screenOrigin));
                for (chr = 0; chr < len; chr++) {
                    charPosition.operator_add_equal( 
                        myFont.getCharOffset((char)str.get(/* 2**/chr)));
                    // Assuming left-to-right drawing of characters:
                    if (charPosition.getValueRead()[0] >= screenPoint.getValueRead()[0]) break;
                }
                    
                detail.setCharacterIndex(chr);

                pp.setDetail(detail, this);
                pp.setMaterialIndex(0);

                // We'll define normal to be object-space 0,0,1:
                pp.setObjectNormal(new SbVec3f(0,0,1));
                // And texture coordinates to be zero:
                pp.setObjectTextureCoords(new SbVec4f(0,0,0,0));
            }
        }
    }
    state.pop();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Generates primitives - no-op.
//
// Use: protected

public void
generatePrimitives(SoAction action)
//
////////////////////////////////////////////////////////////////////////
{
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Computes bounding box of text. Since 2D text is screen-aligned,
//    we need to look at the current view volume and viewport region.
//
// Use: protected

public void
computeBBox(SoAction action, final SbBox3f box, final SbVec3f center)
//
////////////////////////////////////////////////////////////////////////
{
    SoState state = action.getState();

    state.push();

    // Get a font cache we can pull stuff out of
    if (myFont != null) {
        if (!myFont.isValid(state)) {
            myFont.unref();
            myFont = null;
        }
    }
    if (myFont == null) {
        myFont = SoBitmapFontCache.getFont(state, false);
        if (myFont == null) {
            state.pop();
            return;
        }
    }

    //The current text must be translated to UCS, unless this
    //translation has already been done.    
    if (myFont != null) myFont.convertToUCS(getNodeId(), string);
    
    final SbMatrix objToScreen = new SbMatrix();
    objToScreen.copyFrom(SoProjectionMatrixElement.get(state));
    objToScreen.copyFrom(
        objToScreen.multLeft(SoViewingMatrixElement.get(state)));
    objToScreen.copyFrom(
        objToScreen.multLeft(SoModelMatrixElement.get(state)));

    final SbMatrix screenToObj = new SbMatrix(objToScreen.inverse());

    final SbViewportRegion vpr = new SbViewportRegion(SoViewportRegionElement.get(state));

    // The origin of the text on the screen is the object-space point
    // 0,0,0:
    final SbVec3f screenOrigin = new SbVec3f(
        fromObjectSpace(new SbVec3f(0,0,0), objToScreen, vpr));

    // Figure out the screen-space bounding box of the characters:
    final SbBox3f screenBbox = new SbBox3f(), charBbox = new SbBox3f();

    for (int line = 0; line < string.getNum(); line++) {
    
        int len = myFont.getNumUCSChars(line);
        ShortBuffer str = myFont.getUCSString(line).asShortBuffer();
       
        // Starting position of string, based on justification:
        final SbVec3f charPosition = getPixelStringOffset(line).operator_add(
            screenOrigin);           

        for (int chr = 0; chr < len; chr++) {
            myFont.getCharBbox((char)str.get(/*2**/chr), charBbox);
            if (!charBbox.isEmpty()) {
                final SbVec3f min = charBbox.getMin().operator_add( charPosition);
                final SbVec3f max = charBbox.getMax().operator_add( charPosition);
                screenBbox.extendBy(min);
                screenBbox.extendBy(max);
            }

            // And advance...
            charPosition.operator_add_equal(myFont.getCharOffset((char)str.get(/*2**/chr)));
        }
    }
    // Ok, screenBbox now contains the pixel-space extent of the
    // characters.  We'll transform the bounds of that box back into
    // object space and extend the object-space bounding box:
    
    if (!screenBbox.isEmpty()) {
        // Do each of the 4 corners of the screen-space box:
        final SbVec3f min = screenBbox.getMin();
        final SbVec3f max = screenBbox.getMax();
        final SbVec3f objectPoint = new SbVec3f(), temp = new SbVec3f();

        temp.setValue(min.getValueRead()[0], min.getValueRead()[1], screenOrigin.getValueRead()[2]);
        objectPoint.copyFrom( toObjectSpace(temp, screenToObj, vpr));
        box.extendBy(objectPoint);

        temp.setValue(max.getValueRead()[0], max.getValueRead()[1], screenOrigin.getValueRead()[2]);
        objectPoint.copyFrom( toObjectSpace(temp, screenToObj, vpr));
        box.extendBy(objectPoint);

        temp.setValue(min.getValueRead()[0], max.getValueRead()[1], screenOrigin.getValueRead()[2]);
        objectPoint.copyFrom( toObjectSpace(temp, screenToObj, vpr));
        box.extendBy(objectPoint);

        temp.setValue(max.getValueRead()[0], min.getValueRead()[1], screenOrigin.getValueRead()[2]);
        objectPoint.copyFrom( toObjectSpace(temp, screenToObj, vpr));
        box.extendBy(objectPoint);

        // Set the center to be the origin, which is the natural "center"
        // of the text, regardless of justification
        center.setValue(0.0f, 0.0f, 0.0f);
    }

    state.pop();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns the number of pixels the current text string is offset from the
//    text origin.  Uses latest UCS-string in myFont. 
//
// Use: private

private SbVec3f
getPixelStringOffset(int line)
//
////////////////////////////////////////////////////////////////////////
{
    final SbVec3fSingle result = new SbVec3fSingle(0,0,0);

    if (justification.getValue() == Justification.RIGHT.getValue()) {
        float width = myFont.getWidth(line);
        result.getValue()[0] = -width;
    }
    if (justification.getValue() == Justification.CENTER.getValue()) {
        float width = myFont.getWidth(line);
        result.getValue()[0] = -width/2.0f;
    }
    result.getValue()[1] = -line*myFont.getHeight()*spacing.getValue();// *2; // java port

    return result;
}
	

////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoText2 class.
//
// Use: internal

public static void initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SoSubNode.SO__NODE_INIT_CLASS(SoText2.class, "Text2", SoShape.class);

    // Font size/name are enabled by SoFont node
}


}
