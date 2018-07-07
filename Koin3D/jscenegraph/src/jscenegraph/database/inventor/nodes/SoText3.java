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
 |      This file defines the SoText3 node class.
 |
 |   Author(s)          : Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import org.lwjglx.BufferUtils;
import org.lwjglx.util.glu.GLUtessellator;
import org.lwjglx.util.glu.GLUtessellatorCallback;
import org.lwjglx.util.glu.GLUtessellatorCallbackAdapter;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.gl2.GLUgl2;

import jscenegraph.database.inventor.SbBox2f;
import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbDict;
import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SbPList;
import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec3fSingle;
import jscenegraph.database.inventor.SbVec4f;
import jscenegraph.database.inventor.SoNodeList;
import jscenegraph.database.inventor.SoPrimitiveVertex;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.bundles.SoMaterialBundle;
import jscenegraph.database.inventor.caches.SoCache;
import jscenegraph.database.inventor.details.SoTextDetail;
import jscenegraph.database.inventor.elements.SoCacheElement;
import jscenegraph.database.inventor.elements.SoComplexityElement;
import jscenegraph.database.inventor.elements.SoComplexityTypeElement;
import jscenegraph.database.inventor.elements.SoCreaseAngleElement;
import jscenegraph.database.inventor.elements.SoFontNameElement;
import jscenegraph.database.inventor.elements.SoFontSizeElement;
import jscenegraph.database.inventor.elements.SoGLCacheContextElement;
import jscenegraph.database.inventor.elements.SoGLDisplayList;
import jscenegraph.database.inventor.elements.SoGLTextureEnabledElement;
import jscenegraph.database.inventor.elements.SoMaterialBindingElement;
import jscenegraph.database.inventor.elements.SoModelMatrixElement;
import jscenegraph.database.inventor.elements.SoProfileCoordinateElement;
import jscenegraph.database.inventor.elements.SoProfileElement;
import jscenegraph.database.inventor.elements.SoProjectionMatrixElement;
import jscenegraph.database.inventor.elements.SoTextureCoordinateElement;
import jscenegraph.database.inventor.elements.SoViewingMatrixElement;
import jscenegraph.database.inventor.elements.SoViewportRegionElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFString;
import jscenegraph.database.inventor.fields.SoSFBitMask;
import jscenegraph.database.inventor.fields.SoSFEnum;
import jscenegraph.database.inventor.fields.SoSFFloat;
import jscenegraph.database.inventor.libFL.FLcontext;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.mevis.inventor.system.SbSystem;
import jscenegraph.port.FLoutline;
import jscenegraph.port.fl;
import jscenegraph.port.iconv_t;


////////////////////////////////////////////////////////////////////////////////
//! 3D text shape node.
/*!
\class SoText3
\ingroup Nodes
This node defines one or more strings of 3D text. In contrast with
SoText2, 3D text can be rotated, scaled, lighted, and textured,
just like all other 3D shapes. Each character in a 3D text string is
created by extruding an outlined version of the character (in the
current typeface) along the current profile, as defined by nodes
derived from SoProfile. The default text profile, if none is
specified, is a straight line segment one unit long.


The text origin is at (0,0,0) after applying the current
transformation. The scale of the text is affected by the \b size 
field of the current SoFont as well as the current transformation.


SoText3 uses the current set of materials when rendering. If the
material binding is <tt>OVERALL</tt>, then the whole text is drawn with the
first material. If it is <tt>PER_PART</tt> or <tt>PER_PART_INDEXED</tt>, the
front part of the text is drawn with the first material, the sides
with the second, and the back with the third.


Textures are applied to 3D text as follows.  On the front and back
faces of the text, the texture origin is at the base point of the
first string; the base point is at the lower left for justification
<tt>LEFT</tt>, at the lower right for <tt>RIGHT</tt>, and at the lower center
for <tt>CENTER</tt>. The texture is scaled equally in both S and T
dimensions, with the font height representing 1 unit. S increases to
the right on the front faces and to the left on the back faces. On the
sides, the texture is scaled the same as on the front and back. S is
equal to 0 at the rear edge of the side faces. The T origin can occur
anywhere along each character, depending on how that character's
outline is defined.

\par File Format/Default
\par
\code
Text3 {
  string ""
  spacing 1
  justification LEFT
  parts FRONT
}
\endcode

\par Action Behavior
\par
SoGLRenderAction
<BR> Draws text based on the current font, profiles, transformation, drawing style, material, texture, complexity, and so on. 
\par
SoRayPickAction
<BR> Performs a pick on the text. The string index and character position are available from the SoTextDetail. 
\par
SoGetBoundingBoxAction
<BR> Computes the bounding box that encloses the text. 
\par
SoCallbackAction
<BR> If any triangle callbacks are registered with the action, they will be invoked for each successive triangle used to approximate the text geometry. 

\par See Also
\par
SoFont, SoProfile, SoText2, SoTextDetail
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 * TODO : classe à implémenter
 */
public class SoText3 extends SoShape {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoText3.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoText3.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoText3.class); }    
	  
   public
    //! Justification types
    enum Justification {
        LEFT    ( 0x01),
        RIGHT   ( 0x02),
        CENTER  ( 0x03);
        
        private int value;
        
        Justification(int value) {
        	this.value = value;
        }
        
        public int getValue() {
        	return value;
        }
    };

    //! Justification types
    public enum Part {
        FRONT   ( 0x01),
        SIDES   ( 0x02),
        BACK    ( 0x04),
        ALL     ( 0x07);
        
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

    //! The text string(s) to display. Each string will appear on its own line.
    //! The string(s) can be ascii or UTF-8.
    public final SoMFString          string = new SoMFString();         

    //! Defines the distance (in the negative y direction) between the base
    //! points of successive strings, measured with respect to the current
    //! font height. A value of 1 indicates single spacing, a value of 2
    //! indicates double spacing, and so on.
    public final SoSFFloat           spacing = new SoSFFloat();        

    //! Which parts of text are visible. Note that, for speed, the default for
    //! this field is <tt>FRONT</tt> only.
    public final SoSFBitMask         parts = new SoSFBitMask();          

    //! Indicates placement and alignment of strings. With <tt>LEFT</tt>
    //! justification, the left edge of the first line is at the (transformed)
    //! origin, and all left edges are aligned. <tt>RIGHT</tt> justification is
    //! similar.  <tt>CENTER</tt> justification places the center of the first
    //! string at the (transformed) origin, with the centers of all remaining
    //! strings aligned under it.
    public final SoSFEnum            justification = new SoSFEnum();
    
    
// First, a more convenient structure for outlines:
    static class SoFontOutline {

    // Constructor, takes a pointer to the font-library outline
    // structure and the font's size:
////////////////////////////////////////////////////////////////////////
//
//Description:
//Copy info from the font library into a more convenient form.
//
//Use: internal

  public SoFontOutline(FLoutline outline, float fontSize) {
    charAdvance.copyFrom(new SbVec2f(outline.xadvance,
                          outline.yadvance).operator_mul(fontSize));
    numOutlines = outline.outlinecount;
    if (numOutlines != 0) {
        numVerts = new int[numOutlines];
        verts = new SbVec2f[numOutlines][];
        for (int i = 0; i < numOutlines; i++) {
            numVerts[i] = outline.vertexcount[i];
            if (numVerts[i] != 0) {
                verts[i] = new SbVec2f[numVerts[i]];
                for (int j = 0; j < numVerts[i]; j++) {
                    verts[i][j] = new SbVec2f(outline.vertex[i][j].x,
                                          outline.vertex[i][j].y).operator_mul(fontSize);
                }
            } else {
                verts[i] = null;
            }
        }
    } else {
        numVerts = null;
        verts = null;
    }	  
  }
    // Destructor
  public void destructor() {
	  //nothing to do (java port)
  }

    // Query routines:
  public int         getNumOutlines() { return numOutlines; }
  public int         getNumVerts(int i) { return numVerts[i]; }
  public SbVec2f     getVertex(int i, int j) { return verts[i][j]; }
  public SbVec2f     getCharAdvance() { return new SbVec2f(charAdvance); }
    
  public static SoFontOutline getNullOutline() {
	  return new SoFontOutline();
  }

    // Internal constructor used by getNullOutline:
  private SoFontOutline() {
	    charAdvance.copyFrom(new SbVec2f(0,0));
	    numOutlines = 0;
	    numVerts = null;
	    verts = null;
  }
    
    // This basically mimics the FLoutline structure, with the
    // exception that the font size is part of the outline:
  private int numOutlines;
  private int[] numVerts;
  private SbVec2f[][] verts;
  private final SbVec2f charAdvance = new SbVec2f();
};

    
// Callback function for sides of characters-- passed the number of
// points going back, and points and normals on either edge of the
// strip.  tTexCoords[0] and [1] are for the two edges, and the
// sTexCoords are the same for both edges.
		interface SideCB {
			void run(int nPoints,
                    final SbVec3f[] points1, final SbVec3f[] norms1,
                    final SbVec3f[] points2, final SbVec3f[] norms2,
                    final float[] sTexCoords, final float[] tTexCoords, final int tTexCoordsIndex);
		};

 // This is pretty heavyweight-- it is responsible for doing all of the
 // grunt work of figuring out the polygons making up the characters in
 // the font.
    private static class SoOutlineFontCache extends SoCache
 {


    // Texture coordinates in side display lists
    int         sidesHaveTexCoords;

    // Number of characters in this font.
 
    int         numChars;

    // Display lists for fronts, sides:
    SoGLDisplayList frontList;
    SoGLDisplayList sideList;

    // Profile information:
    float       cosCreaseAngle;
    int     nProfileVerts;  // Number of points in profile
    SbVec2f[]     profileVerts;  // Profile vertices
    float[]       sTexCoords;    // Texture coordinates along profile
                                // (nProfileVerts of them)
    SbVec2f[]     profileNorms;  // Profile normals
                                // ((nProfileVerts-1)*2 of them)

    // This flag will be true if there is another cache open (if
    // building GL display lists for render caching, that means we
    // can't also build display lists).
    boolean        otherOpen;

    // And tables telling us if a display list has been created for
    // each character in the font (we do that lazily since it is
    // expensive):	
    SbDict frontDict;
    SbDict sideDict;

    // dictionary of outlines; these are also cached and created when
    // needed.
    SbDict outlineDict;
    
    // Remember nodeId that was used to do UCS translation.
    int    currentNodeId;
    
    static iconv_t      conversionCode;

    // Font size
    float       fontSize;

    // Flag used to detect tesselation errors:
    static boolean tesselationError;

    // List of font numbers for this font-list
    String fontNumList;
    SbPList fontNums; // pointer
    
    // char* pointers of UCS-2 strings:
    final SbPList     UCSStrings = new SbPList();
    // size of these strings, in UCS-2 characters:
    final SbPList     UCSNumChars = new SbPList();

    // Font library context for all outline fonts
    static FLcontext    context;

    // Global list of available fonts; a 'font' in this case is a
    // unique set of font name, font size, complexity value/type, and
    // set of profiles-- if any of these changes, the set of polygons
    // representing the font will change, and a different font will be
    // used.
    static SbPList      fonts;
    
        // Constructor
	public SoOutlineFontCache(SoState state) {
		super(state);
    ref();

    frontList = sideList = null;

    // Add element dependencies explicitly here; making 'this' the
    // CacheElement doesn't work if we are being constructed in an
    // action that doesn't have caches.
    SbName font = SoFontNameElement.get(state);
    addElement(state.getConstElement(
        SoFontNameElement.getClassStackIndex(SoFontNameElement.class)));
    if (font.operator_equal_equal(SoFontNameElement.getDefault())) {
        font = new SbName(SbSystem.IV_DEFAULT_FONTNAME);
    }
    
    float uems = Float.NaN;
    
    // Remember size
    fontSize = SoFontSizeElement.get(state);
    addElement(state.getConstElement(
        SoFontSizeElement.getClassStackIndex(SoFontSizeElement.class)));
    
    // Figure out complexity...
    float complexity = SoComplexityElement.get(state);
    addElement(state.getConstElement(
        SoComplexityElement.getClassStackIndex(SoComplexityElement.class)));
    addElement(state.getConstElement(
        SoComplexityTypeElement.getClassStackIndex(SoComplexityTypeElement.class)));

    switch (SoComplexityTypeElement.get(state)) {
      case OBJECT_SPACE:
        {
            // Two ramps-- complexity of zero  == 250/1000 of an em
            //             complexity of .5    == 20/1000 of an em
            //             complexity of 1     == 1/1000 of an em
            final float ZERO = 250;
            final float HALF = 20;
            final float ONE = 1;
            if (complexity > 0.5) uems = (2.0f-complexity*2.0f)*(HALF-ONE)+ONE;
            else uems = (1.0f-complexity*2.0f)*(ZERO-HALF)+HALF;
        }
        break;

      case SCREEN_SPACE:
        {
            final SbVec3f p = new SbVec3f(fontSize, fontSize, fontSize);
            final SbVec2s rectSize = new SbVec2s();
            
            SoShape.getScreenSize(state, new SbBox3f(p.operator_minus(), p), rectSize);
            float maxSize =
                (rectSize.getValue()[0] > rectSize.getValue()[1] ? rectSize.getValue()[0] : rectSize.getValue()[1]);
            uems = 250.0f / (1.0f + 0.25f * maxSize * complexity *
                            complexity);
            
            // We have to manually add the dependency on the
            // projection, view and model matrix elements (these are
            // gotten in the SoShape.getScreenSize routine), and the
            // ViewportRegionElement:
            addElement(state.getConstElement(
                SoProjectionMatrixElement.getClassStackIndex(SoProjectionMatrixElement.class)));
            addElement(state.getConstElement(
                SoViewingMatrixElement.getClassStackIndex(SoViewingMatrixElement.class)));
            addElement(state.getConstElement(
                SoModelMatrixElement.getClassStackIndex(SoModelMatrixElement.class)));
            addElement(state.getConstElement(
                SoViewportRegionElement.getClassStackIndex(SoViewportRegionElement.class)));
        }
        break;

      case BOUNDING_BOX:
        {
            uems = 20;
        }
        break;
    }
    fl.flSetHint(fl.FL_HINT_TOLERANCE, uems);

    fontNumList = createUniFontList(font.getString());

    // If error creating font:
    if (fontNumList == null) {
        // Try IV_DEFAULT_FONTNAME, unless we just did!
        if (!font.operator_equals(new SbName(SbSystem.IV_DEFAULT_FONTNAME))) {
//#ifdef DEBUG
            SoDebugError.post("SoText3.getFont",
                      "Couldn't find font "+font.getString()+", replacing with "+SbSystem.IV_DEFAULT_FONTNAME);
//#endif
            fontNumList = createUniFontList(SbSystem.IV_DEFAULT_FONTNAME);
        }
        if (fontNumList == null) {
//#ifdef DEBUG
            SoDebugError.post("SoText3.getFont",
                               "Couldn't find font "+SbSystem.IV_DEFAULT_FONTNAME+"!");
//#endif
        }
    }

    numChars = 65536;  //Allow for all UCS-2 possibilities.
    sidesHaveTexCoords = false ? 1 : 0;
    currentNodeId = 0; //guarantee UCS translation occurs first time.

    //sideDict and frontDict indicate if display lists exist for front,sides
    //outlineDict has pointer to outline.
    sideDict = new SbDict();
    frontDict = new SbDict();
    outlineDict = new SbDict();

    // Get profile info:
    final SoNodeList profiles = SoProfileElement.get(state);
    addElement(state.getConstElement(
        SoProfileElement.getClassStackIndex(SoProfileElement.class)));
    addElement(state.getConstElement(
        SoProfileCoordinateElement.getClassStackIndex(SoProfileCoordinateElement.class)));
    nProfileVerts = 0;
    if (profiles.getLength() > 0) {
        SoProfile profileNode = (SoProfile )profiles.operator_square_bracket(0);
        profileNode.getVertices(state, n -> nProfileVerts = n, verts -> profileVerts = verts);
    } else {
        nProfileVerts = 2;
        profileVerts = new SbVec2f[2];
        profileVerts[0] = new SbVec2f();
        profileVerts[1] = new SbVec2f();
        profileVerts[0].setValue(0, 0);
        profileVerts[1].setValue(1, 0);
    }   

    if (nProfileVerts > 1) {
        cosCreaseAngle = (float)Math.cos(SoCreaseAngleElement.get(state));
        addElement(state.getConstElement(
            SoCreaseAngleElement.getClassStackIndex(SoCreaseAngleElement.class)));
        int nSegments = (int) nProfileVerts - 1;

        // Figure out normals for profiles; there are twice as many
        // normals as segments.  The two normals for each segment endpoint
        // may be averaged with the normal for the next segment, depending
        // on whether or not the angle between the segments is greater
        // than the creaseAngle.
        profileNorms = new SbVec2f[nSegments*2];
        for(int i=0;i<nSegments*2;i++) profileNorms[i] = new SbVec2f();
        figureSegmentNorms(profileNorms, (int) nProfileVerts, profileVerts,
                           cosCreaseAngle, false);
        // Need to flip all the normals because of the way the profiles
        // are defined:
        int i;
        for (i = 0; i < nSegments*2; i++) {
            profileNorms[i].operator_mul_equal( -1.0f);
        }
    
        // Figure out S texture coordinates, which run along the profile:
        sTexCoords = new float[nProfileVerts];
        figureSegmentTexCoords(sTexCoords, (int) nProfileVerts,
                               profileVerts, false);
        // And reverse them, so 0 is at the back of the profile:
        float max = sTexCoords[nProfileVerts-1];
        for (i = 0; i < nProfileVerts; i++) {
            sTexCoords[i] = max - sTexCoords[i];
        }
    } else {
        profileNorms = null;
        sTexCoords = null;
    }

    fonts.append(this);
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
    if (fontNumList != null) {
        if (fl.flGetCurrentContext() != context) {
            fl.flMakeCurrentContext(context);      
        }
        
        // Free up cached outlines, display lists
    
        if (hasProfile()) { //java port
//            delete[] profileVerts;
//            delete[] sTexCoords;
//            delete[] profileNorms;
        }
        //Must free every outline in dictionary:
        
        outlineDict.applyToAll((key,value) -> freeOutline(key,value));

        // Only destroy the font library font if no other font caches
        // are using the same font identifier:
        // Must go through fontlist and destroy every font that isn't used
        // by any other cache.
        
        boolean otherFonts = (fonts.getLength() > 1);
        SbDict otherFontDict = null;
        if (otherFonts){
            otherFontDict = new SbDict();
            //Enter all the other fontnums into the dictionary:
            for (int i = 0; i< fonts.getLength(); i++) {
                SoOutlineFontCache t = (SoOutlineFontCache )(fonts).operator_square_bracket(i);
                if ( t == this) continue;       
                for (int j = 0; j< (t.fontNums.getLength()); j++){
                    int key = (int)((t.fontNums)).operator_square_bracket(j);            
                    otherFontDict.enter(key, null);
                }       
            }
        }
        // Now destroy any fonts that don't appear in otherFontDict
        for (int i = 0; i < fontNums.getLength(); i++){
            final Object[] value = new Object[1];
            if ( !otherFonts || 
                    !otherFontDict.find((int)(fontNums).operator_square_bracket(i), value)){
                fl.flDestroyFont(/*(FLfontNumber)*/(int)(fontNums).operator_square_bracket(i));
            }
        }
        if (otherFonts) otherFontDict.destructor();   
        frontDict.destructor();
        sideDict.destructor();
        outlineDict.destructor();
        
        //if (fontNumList)        delete [] fontNumList;
        //if (fontNums != null)           fontNums.destructor();

        fonts.remove(fonts.find(this));
    }
}
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Destroy this cache.  Called by unref(); frees up OpenGL display
//    lists.
//
// Use: protected, virtual

public void destroy(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    // Pass in null to unref because this cache may be destroyed
    // from an action _other_ than GLRender:
    if (frontList != null) {
        frontList.unref(null);
        frontList = null;
    }
    if (sideList != null) {
        sideList.unref(null);
        sideList = null;
    }
    super.destroy(null);
}

	

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Figures out if this cache is valid for rendering.
//
// Use: internal

public boolean isRenderValid(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    // Special cache case here:  if we generated side display lists
    // without texture coordinates AND we need texture coordinates,
    // we'll have to regenerate and this cache is invalid:
    if (sideList != null) {
        if (sidesHaveTexCoords==0 &&
            SoGLTextureEnabledElement.get(state)) {
            return false;
        }
    }

    if (!isValid(state)) return false;

    if (frontList!=null && 
        frontList.getContext() != SoGLCacheContextElement.get(state))
        return false;
    if (sideList!=null && 
        sideList.getContext() != SoGLCacheContextElement.get(state))
        return false;

    return true;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Find an appropriate font, given a state.  A bunch of elements
//    (fontName, fontSize, creaseAngle, complexity and profile) must
//    be enabled in the state...
//
// Use: static, internal

public static SoOutlineFontCache getFont(SoState state, boolean forRender)
//
////////////////////////////////////////////////////////////////////////
{
    if (fonts == null) {
        // One-time font library initialization
        fonts = new SbPList();
        context = fl.flCreateContext(null, fl.FL_FONTNAME, null,
                                  1.0f, 1.0f);
        if (context == null) {
//#ifdef DEBUG
            SoDebugError.post("SoText3.getFont",
                               "flCreateContext returned null");
//#endif
            return null;
        }
        fl.flMakeCurrentContext(context);
        fl.flSetHint(fl.FL_HINT_FONTTYPE, fl.FL_FONTTYPE_OUTLINE);
    }
    else if (context == null) return null;
    else {
        if (fl.flGetCurrentContext() != context)
        	fl.flMakeCurrentContext(context);
    }

    SoOutlineFontCache result = null;
    for (int i = 0; i < fonts.getLength() && result == null; i++) {
        SoOutlineFontCache c = (SoOutlineFontCache ) (fonts).operator_square_bracket(i);
        if (forRender ? c.isRenderValid(state) : c.isValid(state)) {
            result = c; // Loop will terminate...
            result.ref(); // Increment ref count
        }
    }
    // If none match:
    if (result == null) {
        result = new SoOutlineFontCache(state);

    }
    return result;
}
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Create a list of font numbers from a list of font names
//
// Use: private

String createUniFontList(String fontNameList) 
//
////////////////////////////////////////////////////////////////////////
{
    String s;//char *s, *s1, *ends;
    int fn;
    float[][] mat = new float[2][2];

    mat[0][0] = mat[1][1] = 1.0f;
    mat[0][1] = mat[1][0] = 0.0f;
    
    //Make a copy of fontNameList so we don't disturb the one we are passed.    
    //Find \n at end of namelist:
    String nameCopy;// = new char[strlen(fontNameList)+1];
    nameCopy = fontNameList;//strcpy(nameCopy, fontNameList);

    //find the last null in nameCopy.    
    //s = ends = (char *)strrchr(nameCopy, '\0');        
    //*s = ';';  /* put a guard in the end of string */
    s = nameCopy+';';

    //s = (char*)nameCopy;
    fontNums = new SbPList(); 
      
    //while (s1 = (char *)strchr(s, ';')) {
    //   *s1 = (char)NULL;  /* font name is pointed to s */

       if ((fn = fl.flCreateFont((String)s.substring(0,s.length()-1), mat, 0, null)) == 0) {
//#ifdef DEBUG
            SoDebugError.post("SoOutlineFontCache::createUniFontList", 
                "Cannot create font "+ s);         
//#endif          
       }
       else fontNums.append((int)fn);
       //if(s1 == ends) break;
       //s = (s1 + 1);  /* move to next font name */
    //}
  
    if (fontNums.getLength() == 0 ) return null;
    
    // create a comma-separated list of font numbers:
    String fontList = "";// = new char[10*fontNums->getLength()];
    //fontList[0] = '\0';
    for (int i = 0; i< fontNums.getLength(); i++ ){
        fn = (int)(fontNums).operator_square_bracket(i);
        fontList += fn; fontList +=',';//sprintf(fontList[strlen(fontList)], "%d,", fn);        
    }
    //fontList[strlen(fontList) - 1] = '\0'; // the last ',' is replaced with NULL
    fontList = fontList.substring(0, fontList.length()-1);
    //delete [] nameCopy;
                                                    
    return (String)fontList;
   
}


//Convert string to UCS format, keep a copy in this cache.
//Use nodeid to know when to reconvert.    
public void convertToUCS(int nodeid, SoMFString strings) {
    if (nodeid == currentNodeId) return;
    currentNodeId = nodeid;
    
    //delete previously converted UCS string
    UCSStrings.truncate(0);
    UCSNumChars.truncate(0);
    
    //for each line of text, allocate a sufficiently large buffer
    //An extra two bytes are allocated.
    for (int i = 0; i< strings.getNum(); i++){
        String input = (String)strings.operator_square_bracket(i);
        char[] chars = input.toCharArray();
        short[] shorts = new short[chars.length];
        for(int ii=0;ii <shorts.length;ii++) {
        	shorts[ii] = (short)chars[ii];
        }
        ByteBuffer sb = BufferUtils.createByteBuffer(shorts.length*2);
        sb.asShortBuffer().put(shorts).flip();
    	UCSStrings.operator_square_bracket(i, sb);
        int inbytes = strings.operator_square_bracket(i).length();
        UCSNumChars.operator_square_bracket(i, inbytes);
    }    
}

    //Returns line of UCS-2 text
    ByteBuffer     getUCSString(int line)
        { return (ByteBuffer)UCSStrings.operator_square_bracket(line);}
        
    int         getNumUCSChars(int line)
        { return (int)UCSNumChars.operator_square_bracket(line);}
        

////////////////////////////////////////////////////////////////////////
//
// Description:
//   Find the first and last points in the bevel-- that is where the
//   front and back of the character will be.
// Use:
//   internal

public void getProfileBounds(final float[] firstZ, final float[] lastZ)
//
////////////////////////////////////////////////////////////////////////
{
    if (hasProfile()) {
        firstZ[0] = -profileVerts[0].getValue()[0];
        lastZ[0] = -profileVerts[nProfileVerts-1].getValue()[0];
    } else {
        firstZ[0] = lastZ[0] = 0;
    }
}

// Returns TRUE if there _is_ any profile
// (if not, act as if SIDES of text are off)
boolean        hasProfile() { return  (nProfileVerts > 1); }


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Called by the GLU tesselator when there is an error
//
// Use: static, private
//
////////////////////////////////////////////////////////////////////////

//#ifdef DEBUG
public static void errorCB(int whichErr)
{
	GLUgl2 glu = new GLUgl2();
    SoDebugError.post("SoText3.errorCB", glu.gluErrorString(whichErr));
    tesselationError = true;
}
//#else  /* DEBUG */


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets up for GL rendering.
//
// Use: internal

public void setupToRenderSide(SoState state, boolean willTexture)
//
////////////////////////////////////////////////////////////////////////
{
    otherOpen = SoCacheElement.anyOpen(state);
    if (!otherOpen && sideList == null) {
        sideList = new SoGLDisplayList(state,
                                        SoGLDisplayList.Type.DISPLAY_LIST,
                                        numChars);
        sideList.ref();
        sidesHaveTexCoords = willTexture ? 1 : 0;
    }
    if (sideList != null) {
    	GL2 gl2 = state.getGL2();
        // Set correct list base
        gl2.glListBase(sideList.getFirstIndex());
        sideList.addDependency(state);
    }
}

    // Returns the width of specified line number
////////////////////////////////////////////////////////////////////////
//
//Description:
//Returns the width of the specified line in the cache.
//
//Use: private

    float       getWidth(int line) {
    float total = 0.0f;
    ShortBuffer chars = getUCSString(line).asShortBuffer();
    
    for (int i = 0; i < getNumUCSChars(line); i++) {
        SoFontOutline outline = getOutline((char)chars.get(i));
        total += outline.getCharAdvance().getValue()[0];
    }

    return total;
    }

    
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns the 2D bounding box of the given character.
//
// Use: private

void getCharBBox(char c, final SbBox2f result)
//
////////////////////////////////////////////////////////////////////////
{
    result.makeEmpty();

    if (fontNumList == null) return;

    SoFontOutline outline = getOutline(c);
    
    for (int i = 0; i < outline.getNumOutlines(); i++) {
        for (int j = 0; j < outline.getNumVerts(i); j++) {
            result.extendBy(outline.getVertex(i,j));
        }
    }
}

    
    // Returns height of font
    float       getHeight() { return fontSize; }

    // Return a convnient little class representing a UCS character's
    // outline.
////////////////////////////////////////////////////////////////////////
//
//Description:
//Given a UCS character, return an outline for the character.  If, for
//some reason, we can't get the outline, an 'identity' or 'null'
//outline is returned.
//
//Use: private

    SoFontOutline       getOutline(char c) {
    if (fontNumList == null) {
        return SoFontOutline.getNullOutline();
    }
    char uc = c;
    int key = Character.reverseBytes(uc);//(uc[0]<<8)|uc[1];
    final Object[] value = new Object[1];
    if (!outlineDict.find(key, value)){
        
        FLoutline flo = fl.flUniGetOutline(fontNumList, c);
        if (flo == null) {
            value[0] = SoFontOutline.getNullOutline();
        } else {
            value[0] = new SoFontOutline(flo, fontSize);
            fl.flFreeOutline(flo);
        }
        outlineDict.enter(key, value[0]);
    }
    return (SoFontOutline)value[0];
    	
    }
    

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets up for GL rendering.
//
// Use: internal

public void setupToRenderFront(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    otherOpen = SoCacheElement.anyOpen(state);
    if (!otherOpen && frontList == null) {
        frontList = new SoGLDisplayList(state,
                                        SoGLDisplayList.Type.DISPLAY_LIST,
                                        numChars);
        frontList.ref();
    }
    if (frontList != null) {
    	GL2 gl2 = state.getGL2();
        // Set correct list base
        gl2.glListBase(frontList.getFirstIndex());
        frontList.addDependency(state);
    }
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Assuming that there are not display lists built for all the
//    characters in given line, render the line.
//
// Use: private internal

public void renderSide(GL2 gl2, int line, SideCB callbackFunc)
//
////////////////////////////////////////////////////////////////////////
{
    ShortBuffer str = getUCSString(line).asShortBuffer();
    ShortBuffer ustr = str;
    final Object[] value = new Object[1];
    for (int i = 0; i < getNumUCSChars(line); i++) {
        int key = Character.reverseBytes((char)ustr.get(i));//(ustr[2*i]<<8)|ustr[2*i+1]; // java port
        if (sideDict.find(key, value)) {
            gl2.glCallList(sideList.getFirstIndex()+key);
        }
        else {
            gl2.glBegin(GL2.GL_QUADS);
            generateSideChar((char)str.get(i), callbackFunc);
            gl2.glEnd();

            final SbVec2f t = getOutline((char)str.get(i)).getCharAdvance();
            gl2.glTranslatef(t.getValue()[0], t.getValue()[1], 0.0f);
        }
    }
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Assuming that there are not display lists built for all the
//    characters in given string, render the string.
//
// Use: internal

void renderFront(GL2 gl2, int line,
                                GLUtessellator tobj)
//
////////////////////////////////////////////////////////////////////////
{
    ShortBuffer str = getUCSString(line).asShortBuffer();
    ShortBuffer ustr = str;

    final Object[] value = new Object[1];
    for (int i = 0; i < getNumUCSChars(line); i++) {
        int key = Character.reverseBytes((char)ustr.get(i));//ustr[2*i]<<8 | ustr[2*i+1];  
        if (frontDict.find(key, value)) {
            gl2.glCallList(frontList.getFirstIndex()+key);
        }
        else {
            generateFrontChar(gl2, (char)str.get(i), tobj);
            SbVec2f t = getOutline((char)str.get(i)).getCharAdvance();
            gl2.glTranslatef(t.getValue()[0], t.getValue()[1], 0.0f);
        }
    }
}



////////////////////////////////////////////////////////////////////////
//
// Description:
//    Assuming that there are display lists built for all the
//    characters in given string, render them using the GL's CallLists
//    routine.
//
// Use: internal

void callFrontLists(GL2 gl2, int line)
//
////////////////////////////////////////////////////////////////////////
{
    ByteBuffer str = getUCSString(line);

    gl2.glCallLists(getNumUCSChars(line), GL2.GL_2_BYTES, str);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Assuming that there are display lists built for all the
//    characters in given line, render them using the GL's CallLists
//    routine.
//
// Use: internal

void callSideLists(GL2 gl2, int line)
//
////////////////////////////////////////////////////////////////////////
{
    ByteBuffer str = getUCSString(line);

    gl2.glCallLists(getNumUCSChars(line), GL2.GL_2_BYTES, str);
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns TRUE if a display lists exists for given character.
//    Tries to build a display list, if it can.
//
// Use: internal

public boolean hasFrontDisplayList(GL2 gl2, char c,
                                        GLUtessellator tobj)
//
////////////////////////////////////////////////////////////////////////
{
    // If we have one, return TRUE
    char uc = c;
    int key = Character.reverseBytes(uc);//(uc[0]<<8) | uc[1];
    final Object[] value = new Object[1];
    if (frontDict.find(key, value)) return true;
    
    // If we don't and we can't build one, return FALSE.
    if (otherOpen) return false;
    
    // Build one:
    gl2.glNewList(frontList.getFirstIndex()+key, GL2.GL_COMPILE);
    generateFrontChar(gl2,c, tobj);
    SbVec2f t = getOutline(c).getCharAdvance();
    gl2.glTranslatef(t.getValue()[0], t.getValue()[1], 0.0f);
    gl2.glEndList();
    
    frontDict.enter(key, value);

    return true;
}
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns TRUE if a display lists exists for given character.
//    Tries to build a display list, if it can.
//
// Use: internal

boolean hasSideDisplayList(GL2 gl2, char c,
                                       SideCB callbackFunc)
//
////////////////////////////////////////////////////////////////////////
{
    char uc = c;
    int key = Character.reverseBytes(uc);//(uc[0]<<8) | uc[1];
    final Object[] value = new Object[1];
    // If we have one, return TRUE
    if (sideDict.find(key, value)) return true;  
    
    // If we don't and we can't build one, return FALSE.
    if (otherOpen) return false;
    
    // Build one:
    gl2.glNewList(sideList.getFirstIndex()+key, GL2.GL_COMPILE);

    gl2.glBegin(GL2.GL_QUADS);    // Render as independent quads:
    generateSideChar(c, callbackFunc);
    gl2.glEnd();

    SbVec2f t = getOutline(c).getCharAdvance();
    gl2.glTranslatef(t.getValue()[0], t.getValue()[1], 0.0f);
    gl2.glEndList();
    sideDict.enter(key, value);

    return true;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//   Get the 2D bounding box of the bevel.
//
//  Use:
//    internal

void getProfileBBox(final SbBox2f profileBox)
//
////////////////////////////////////////////////////////////////////////
{
    for (int i = 0; i < nProfileVerts; i++) {
        profileBox.extendBy(profileVerts[i]);
    }
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Called by generateSide.  This generates the bevel triangles for
//    one character.
//
// Use: private

private void generateSideChar(char c, SideCB callbackFunc)
//
////////////////////////////////////////////////////////////////////////
{
    if (!hasProfile()) return;

    // Get the outline of the character:
    SoFontOutline outline = getOutline(c);
    
    for (int i = 0; i < outline.getNumOutlines(); i++) {
        // For each outline:

        int nOutline = outline.getNumVerts(i);

        SbVec2f[] oVerts = new SbVec2f[nOutline];
        // Copy in verts so figureSegmentNorms can handle them..
        int j;
        for (j = 0; j < nOutline; j++) {
            oVerts[j] = new SbVec2f(outline.getVertex(i, j));
        }

        // First, figure out a set of normals for the outline:
        SbVec2f[] oNorms = new SbVec2f[nOutline*2]; 
        for(int ii=0;ii<nOutline*2;ii++) oNorms[ii] = new SbVec2f(); //java port
        figureSegmentNorms(oNorms, nOutline, oVerts, cosCreaseAngle, true);

        // And appropriate texture coordinates:
        // Figure out T texture coordinates, which run along the
        // outline:
        float[] tTexCoords = new float[nOutline+1];
        figureSegmentTexCoords(tTexCoords, nOutline, oVerts, true);

        // Now, generate a set of triangles for each segment in the
        // outline.  A bevel of profiles is built at each point in the
        // outline; each profile must be flipped perpendicular to the
        // outline (x coordinate becomes -z), rotated to be the
        // average of the normals of the two adjoining segments at
        // that point, and translated to that point.  Triangles are
        // formed between consecutive bevels.
        // Normals are just taken from the 'pNorms' array, after being
        // rotated the appropriate amount.

        SbVec3f[] bevel1 = SbVec3f.allocate(nProfileVerts);
        SbVec3f[] bevelN1 = SbVec3f.allocate((nProfileVerts-1)*2);
        SbVec3f[] bevel2 = SbVec3f.allocate(nProfileVerts);
        SbVec3f[] bevelN2 = SbVec3f.allocate((nProfileVerts-1)*2);
            
        // fill out first bevel:
        fillBevel(bevel1, (int) nProfileVerts, profileVerts,
                  outline.getVertex(i,0),
                  oNorms[(nOutline-1)*2+1], oNorms[0*2]);
        
        SbVec3f[] s1 = bevel1;
        SbVec3f[] s2 = bevel2;
        
        for (j = 0; j < nOutline; j++) {
            // New normals are calculated for both ends of this
            // segment, since the normals may or may not be shared
            // with the previous segment.
            fillBevelN(bevelN1, (int)(nProfileVerts-1)*2, profileNorms,
                       oNorms[j*2]);

            int j2 = (j+1)%nOutline;
            // fill out second bevel:
            fillBevel(s2, (int) nProfileVerts, profileVerts,
                      outline.getVertex(i,j2),
                      oNorms[j*2+1], oNorms[j2*2]);
            fillBevelN(bevelN2, (int)(nProfileVerts-1)*2, profileNorms,
                       oNorms[j*2+1]);

            // And generate triangles between the two bevels:
            (callbackFunc).run((int) nProfileVerts, s1, bevelN1, s2, bevelN2,
                             sTexCoords, tTexCoords, j);

            // Swap bevel1/2 (avoids some recomputation)
            SbVec3f[] t;
            t = s1; s1 = s2; s2 = t;
        }
//        delete [] bevelN2; java port
//        delete [] bevel2;
//        delete [] bevelN1;
//        delete [] bevel1;
//        delete [] tTexCoords;
//        delete [] oNorms;
//        delete [] oVerts;
    }
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Given a set of line segments, this figures out the normal at
//    each point in each segment.  It uses the creaseAngle passed in
//    to decide whether or not adjacent segments share normals.  The
//    isClosed flag is used to determine whether or not the first and
//    last points should be considered another segment.
//
//
// Use: private

void figureSegmentNorms(final SbVec2f[] norms, int nPoints,
                            final SbVec2f[] points,  float cosCreaseAngle,
                            boolean isClosed)
//
////////////////////////////////////////////////////////////////////////
{
    int nSegments;
    
    if (isClosed) nSegments = nPoints;
    else nSegments = nPoints-1;
    
    // First, we'll just make all the normals perpendicular to their
    // segments:
    int i;
    for (i = 0; i < nSegments; i++) {
        final SbVec2f n = new SbVec2f();
        // This is 2D perpendicular, assuming profile is increasing in
        // X (which becomes 'decreasing in Z' when we actually use
        // it...) (note: if a profile isn't increasing in X, the
        // character will be inside-out, with the front face drawn
        // behind the back face, etc).
        SbVec2f v = points[(i+1)%nPoints].operator_minus(points[i]);
        n.getValue()[0] = v.getValue()[1];
        n.getValue()[1] = -v.getValue()[0];
        n.normalize();
        
        norms[i*2].copyFrom(n);
        norms[i*2+1].copyFrom(n);
    }
    // Now, figure out if the angle between any two segments is small
    // enough to average two of their normals.
    for (i = 0; i < (isClosed ? nSegments : nSegments-1); i++) {
        SbVec2f seg1 = points[(i+1)%nPoints].operator_minus(points[i]);
        seg1.normalize();
        SbVec2f seg2 = points[(i+2)%nPoints].operator_minus(points[(i+1)%nPoints]);
        seg2.normalize();
        
        float dp = seg2.dot(seg1);
        if (dp > cosCreaseAngle) {
            // Average the second normal for this segment, and the
            // first normal for the next segment:
            SbVec2f average = norms[i*2+1].operator_add(norms[((i+1)%nPoints)*2]);
            average.normalize();
            norms[i*2+1].copyFrom(average);
            norms[((i+1)%nPoints)*2].copyFrom(average);
        }
    }
}    


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Given a set of line segments, this figures out the texture
//    coordinates for each vertex.  If the isClosed flag is TRUE,
//    an extra texture coordinate is calculated, AND the points are
//    traversed in reverse order.
//
// Use: private

void figureSegmentTexCoords(float[] texCoords, int nPoints,
                            final SbVec2f[] points, boolean isClosed)
//
////////////////////////////////////////////////////////////////////////
{
    float total = 0.0f;
    
    int i;

    if (isClosed) {
        for (i = nPoints; i >= 0; i--) {
            texCoords[i] = total / getHeight();
            if (i > 0) {
                total += (points[i%nPoints].operator_minus(points[i-1])).length();
            }
        }
    } else {
        for ( i = 0; i < nPoints; i++) {
            texCoords[i] = total / getHeight();
            if (i+1 < nPoints) {
                total += (points[i+1].operator_minus(points[i])).length();
            }
        }
    }
}



////////////////////////////////////////////////////////////////////////
//
// Description:
//    Given a set of segments that make up a profile or bevel along
//    which we'll extrude the front face of the text, this routine
//    transforms the bevel from its default orientation (in the x-y
//    plane) to its correct position for a particular point on the
//    text outline, rotated and translated into position.  The
//    translation is the point on the outline, and the two normals
//    passed in are the normals for the segments adjoining that point.
//
// Use: private

void fillBevel(final SbVec3f[] result, int nPoints,
          final SbVec2f[] points,
          final SbVec2f translation,
          final SbVec2f n1, final SbVec2f n2)
//
////////////////////////////////////////////////////////////////////////
{
    // First, figure out a rotation for this bevel:
    SbVec2f n = n1.operator_add(n2);
    n.normalize();
    
    // Now, for each point:
    for (int i = 0; i < nPoints; i++) {
        // This is really the 2D rotation formula,
        // x = x' cos(angle) - y' sin(angle)
        // y = x' sin(angle) + y' cos(angle)
        // Because of the geometry, cos(angle) is n[1] and sin(angle)
        // is -n[0], and x' is zero (the bevel always goes straight
        // back).
        result[i].setValue(0, points[i].getValue()[1] * n.getValue()[0] + translation.getValue()[0]);
        result[i].setValue(1, points[i].getValue()[1] * n.getValue()[1] + translation.getValue()[1]);
        result[i].setValue(2, -points[i].getValue()[0]);
    }
} 

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Given a set of normals for a profile, this rotates the normals
//    from their default position (int the x-y plane) to the correct
//    orientation for a particular point on the texts outline.  The
//    normal passed in is the normal for one end of one of the
//    outline's segments.

void fillBevelN(final SbVec3f[] result, int nNorms,
          final SbVec2f[] norms,
          final SbVec2f n)
//
////////////////////////////////////////////////////////////////////////
{
    // Now, for each point:
    for (int i = 0; i < nNorms; i++) {
        // This is really the 2D rotation formula,
        // x = x' cos(angle) - y' sin(angle)
        // y = x' sin(angle) + y' cos(angle)
        // Because of the geometry, cos(angle) is n[1] and sin(angle)
        // is -n[0], and x' is zero (the bevel always goes straight
        // back).
        result[i].setValue(0, norms[i].getValue()[1] * n.getValue()[0]);
        result[i].setValue(1, norms[i].getValue()[1] * n.getValue()[1]);
        result[i].setValue(2, -norms[i].getValue()[0]);
    }
} 



////////////////////////////////////////////////////////////////////////
//
// Description:
//    Render the fronts of the given string.  The GL transformation
//    matrix is munged by this routine-- surround it by
//    PushMatrix/PopMatrix.
//
// Use: public, internal

public void generateFrontChar(GL2 gl2, char c,
                                      GLUtessellator tobj)
//
////////////////////////////////////////////////////////////////////////
{
    if (fontNumList == null) return;

    final double[] v = new double[3];

    tesselationError = false;
//#ifdef GLU_VERSION_1_2
    GLU.gluTessBeginPolygon(tobj, null);
    GLU.gluTessBeginContour(tobj);
//#else
//    gluBeginPolygon(tobj);
//#endif
    
    // Get outline for character
    SoFontOutline outline = getOutline(c);
    int i;
    for (i = 0; i < outline.getNumOutlines(); i++) {

        // It would be nice if the font manager told us the type of
        // each outline...
//#ifdef GLU_VERSION_1_2
        GLU.gluTessEndContour(tobj);
        GLU.gluTessBeginContour(tobj);
//#else
//        gluNextContour(tobj, (GLenum)GLU_UNKNOWN);
//#endif

        for (int j = 0; j < outline.getNumVerts(i); j++) {
            SbVec2f t = outline.getVertex(i,j);
            v[0] = t.getValue()[0];
            v[1] = t.getValue()[1];
            v[2] = 0.0;

            // Note: The third argument MUST NOT BE a local variable,
            // since glu just stores the pointer and only calls us
            // back at the gluEndPolygon call.
            GLU.gluTessVertex(tobj, v,0, t);
        }
    }
//#ifdef GLU_VERSION_1_2
    GLU.gluTessEndContour(tobj);
    GLU.gluTessEndPolygon(tobj);
//#else
//    gluEndPolygon(tobj);
//#endif

    // If there was an error tesselating the character, just generate
    // a bounding box for the character:
    if (tesselationError) {
        final SbBox2f charBBox = new SbBox2f();
        getCharBBox(c, charBBox);
        if (!charBBox.isEmpty()) {
            final SbVec2f[] boxVerts = new SbVec2f[4];
            for(int ii=0;ii<4;ii++)boxVerts[ii] = new SbVec2f();
            charBBox.getBounds(boxVerts[0], boxVerts[2]);
            boxVerts[1].setValue(boxVerts[2].getValue()[0], boxVerts[0].getValue()[1]);
            boxVerts[3].setValue(boxVerts[0].getValue()[0], boxVerts[2].getValue()[1]);

//#ifdef GLU_VERSION_1_2
            GLU.gluTessBeginPolygon(tobj, null);
            GLU.gluTessBeginContour(tobj);
//#else
//            gluBeginPolygon(tobj);
//#endif
            for (i = 0; i < 4; i++) {
                v[0] = boxVerts[i].getValue()[0];
                v[1] = boxVerts[i].getValue()[1];
                v[2] = 0.0;
                GLU.gluTessVertex(tobj, v, 0, boxVerts[i]);
            }
//#ifdef GLU_VERSION_1_2
            GLU.gluTessEndContour(tobj);
            GLU.gluTessEndPolygon(tobj);
//#else
//            gluEndPolygon(tobj);
//#endif
        }
    }
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Given a character, return the correct amount to advance after
//    drawing that character.  Note:  if we ever wanted to deal with
//    kerning, we'd have to fix this...
//
// Use: private

SbVec2f getCharOffset( char c)
//
////////////////////////////////////////////////////////////////////////
{
    if (fontNumList == null) return new SbVec2f(0,0);

    return getOutline(c).getCharAdvance();
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    function used to free up outline storage
//
// Use: static, private
//
////////////////////////////////////////////////////////////////////////

void freeOutline(Object key, Object value) 
{
    ((/*FLoutline*/SoFontOutline)value).destructor();    
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sees if this font is valid.  If it is valid, it also makes it
//    current.
//
// Use: public

public boolean isValid(final SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    boolean result = super.isValid(state);
    
    if (result) {
        if (fl.flGetCurrentContext() != context) {
            fl.flMakeCurrentContext(context);
        }
    }
    return result;
}


 }
    //! Private data:
    //! SoOutlineFontCache is an internal, opaque class used to
    //! maintain gl display lists and other information for each
    //! character in a font.
    private SoOutlineFontCache myFont;

    //! All this stuff is used while generating primitives:
    private static SoText3 currentGeneratingNode;
    private final static SoPrimitiveVertex[] genPrimVerts = new SoPrimitiveVertex[3];
    private static final SbVec3fSingle genTranslate = new SbVec3fSingle();
    private static int genWhichVertex = -1;
    private static int genPrimType;
    private static SoAction genAction;
    private static boolean genBack;
    private static boolean genTexCoord = true;
    private static SoTextureCoordinateElement tce;

    
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoText3()
//
////////////////////////////////////////////////////////////////////////
{
    nodeHeader.SO_NODE_CONSTRUCTOR(/*SoText3.class*/);

    nodeHeader.SO_NODE_ADD_MFIELD(string,"string",   (""));
    nodeHeader.SO_NODE_ADD_FIELD(spacing,"spacing",  (1.0f));
    nodeHeader.SO_NODE_ADD_FIELD(justification,"justification",    (Justification.LEFT.getValue()));
    nodeHeader.SO_NODE_ADD_FIELD(parts,"parts",            (Part.FRONT.getValue()));

    // Set up static info for enumerated type field
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Justification.LEFT);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Justification.RIGHT);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Justification.CENTER);

    // Set up static info for enumerated type field
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Part.SIDES);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Part.FRONT);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Part.BACK);
    nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Part.ALL);

    // Set up info in enumerated type field
    nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(justification,"justification", "Justification");
    nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(parts,"parts", "Part");

    isBuiltIn = true;
    myFont = null;
}

////////////////////////////////////////////////////////////////////////
//
//Description:
//Destructor
//
//Use: private

public void destructor() {
	if (myFont != null) myFont.unref();
	super.destructor();
}
    

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Performs GL rendering of a Text3.
//
// Use: extender

static GLUtessellator tobj = null;

public void GLRender(SoGLRenderAction action)
//
////////////////////////////////////////////////////////////////////////
{

    // First see if the object is visible and should be rendered now
    if (! shouldGLRender(action))
        return;

    SoState state = action.getState();

    if (!setupFontCache(state, true))
        return;

    SoMaterialBindingElement.Binding mbe =
        SoMaterialBindingElement.get(state);
    boolean materialPerPart =
        (mbe == SoMaterialBindingElement.Binding.PER_PART_INDEXED ||
         mbe == SoMaterialBindingElement.Binding.PER_PART);

    final SoMaterialBundle    mb = new SoMaterialBundle(action);
    if (!materialPerPart) {
        // Make sure the fist current material is sent to GL
        mb.sendFirst();
    }

    final float[] firstZ = new float[1], lastZ = new float[1];
    myFont.getProfileBounds(firstZ, lastZ);

    final GL2 gl2 = state.getGL2(); // java port
    
    if (tobj == null) {
        tobj = GLU.gluNewTess();
        GLUtessellatorCallback cb = new GLUtessellatorCallbackAdapter(){

			@Override
			public void begin(int arg) {
				gl2.glBegin(arg);
			}

			@Override
			public void end() {
				gl2.glEnd();
			}

			@Override
			public void error(int arg) {
				SoOutlineFontCache.errorCB(arg);
			}

			@Override
			public void vertex(Object object) {
				if(object instanceof FloatBuffer) {
					gl2.glVertex2fv((FloatBuffer)object);
				}
				else if(object instanceof SbVec2f) {
					SbVec2f vec = (SbVec2f)object;
					gl2.glVertex2fv(vec.getValue(), 0);
				}
				else {
					gl2.glVertex2fv((float[])object, 0);
				}
			}

        };
        GLU.gluTessCallback(tobj, GLU.GLU_BEGIN, cb);
        GLU.gluTessCallback(tobj, GLU.GLU_END, cb);
        GLU.gluTessCallback(tobj, GLU.GLU_VERTEX, cb);
        GLU.gluTessCallback(tobj, GLU.GLU_ERROR, cb);
    }

    // See if texturing is enabled
    genTexCoord = SoGLTextureEnabledElement.get(action.getState());

    if ((parts.getValue() & Part.SIDES.getValue())!=0 && (myFont.hasProfile())) {
        if (materialPerPart) mb.send(1, false);

        myFont.setupToRenderSide(state, genTexCoord);
        for (int line = 0; line < string.getNum(); line++) {
            gl2.glPushMatrix();
            SbVec2f p = getStringOffset(line);
            if (p.getValue()[0] != 0.0 || p.getValue()[1] != 0.0)
                gl2.glTranslatef(p.getValue()[0], p.getValue()[1], 0.0f);
            renderSide(action, line);
            gl2.glPopMatrix();
        }
    }
    if ((parts.getValue() & Part.BACK.getValue())!=0) {
        if (materialPerPart) mb.send(2, false);

        if (lastZ[0] != 0.0) {
            gl2.glTranslatef(0, 0, lastZ[0]);
        }
        gl2.glNormal3f(0, 0, -1);
        gl2.glFrontFace(GL2.GL_CW);

        myFont.setupToRenderFront(state);
        
        if (genTexCoord) {
            gl2.glPushAttrib(GL2.GL_TEXTURE_BIT);
            gl2.glTexGeni(GL2.GL_S, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_OBJECT_LINEAR);
            gl2.glTexGeni(GL2.GL_T, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_OBJECT_LINEAR);
            final float[] params = new float[4];
            params[0] = -1.0f/myFont.getHeight();
            params[1] = params[2] = params[3] = 0.0f;
            gl2.glTexGenfv(GL2.GL_S, GL2.GL_OBJECT_PLANE, params,0);
            params[1] = -params[0];
            params[0] = 0.0f;
            gl2.glTexGenfv(GL2.GL_T, GL2.GL_OBJECT_PLANE, params,0);
            
            gl2.glEnable(GL2.GL_TEXTURE_GEN_S);
            gl2.glEnable(GL2.GL_TEXTURE_GEN_T);
        }
        
        for (int line = 0; line < string.getNum(); line++) {
            if (string.operator_square_bracket(line).length() <= 0) continue;
            
            gl2.glPushMatrix();
            final SbVec2f p = new SbVec2f(getStringOffset(line));
            if (p.getValue()[0] != 0.0 || p.getValue()[1] != 0.0)
                gl2.glTranslatef(p.getValue()[0], p.getValue()[1], 0.0f);
            renderFront(action, line, tobj);
            gl2.glPopMatrix();
        }
        
        if (genTexCoord) {
            gl2.glPopAttrib();
        }

        gl2.glFrontFace(GL2.GL_CCW);

        if (lastZ[0] != 0)
            gl2.glTranslatef(0, 0, -lastZ[0]);
    }   
    if ((parts.getValue() & Part.FRONT.getValue())!=0) {
        if (materialPerPart) mb.sendFirst();

        if (firstZ[0] != 0.0) {
            gl2.glTranslatef(0, 0, firstZ[0]);
        }

        gl2.glNormal3f(0, 0, 1);
        
        myFont.setupToRenderFront(state);

        if (genTexCoord) {
            gl2.glPushAttrib(GL2.GL_TEXTURE_BIT);
            gl2.glTexGeni(GL2.GL_S, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_OBJECT_LINEAR);
            gl2.glTexGeni(GL2.GL_T, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_OBJECT_LINEAR);
            final float[] params = new float[4];
            params[0] = 1.0f/myFont.getHeight();
            params[1] = params[2] = params[3] = 0.0f;
            gl2.glTexGenfv(GL2.GL_S, GL2.GL_OBJECT_PLANE, params,0);
            params[1] = params[0];
            params[0] = 0.0f;
            gl2.glTexGenfv(GL2.GL_T, GL2.GL_OBJECT_PLANE, params,0);
            
            gl2.glEnable(GL2.GL_TEXTURE_GEN_S);
            gl2.glEnable(GL2.GL_TEXTURE_GEN_T);
        }
        
        for (int line = 0; line < string.getNum(); line++) {
        	gl2.glPushMatrix();
            SbVec2f p = new SbVec2f(getStringOffset(line));
            if (p.getValue()[0] != 0.0 || p.getValue()[1] != 0.0)
                gl2.glTranslatef(p.getValue()[0], p.getValue()[1], 0.0f);
            renderFront(action, line, tobj);
            gl2.glPopMatrix();
        }
        
        if (genTexCoord) {
            gl2.glPopAttrib();
        }

        if (firstZ[0] != 0.0) {
            gl2.glTranslatef(0, 0, -firstZ[0]);
        }
    }
    mb.destructor();
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Render the fronts of the given string.  The GL transformation
//    matrix is munged by this routine-- surround it by
//    PushMatrix/PopMatrix.
//
// Use: private, internal

private void renderFront(SoGLRenderAction action, int line,
                     GLUtessellator tobj)
//
////////////////////////////////////////////////////////////////////////
{
	GL2 gl2 = action.getCacheContext();
	
    ShortBuffer chars = myFont.getUCSString(line).asShortBuffer();

    // First, try to figure out if we can use glCallLists:
    boolean useCallLists = true;

    for (int i = 0; i < myFont.getNumUCSChars(line); i++) {
        // See if the font cache already has (or can build) a display
        // list for this character:
        if (!myFont.hasFrontDisplayList(gl2, (char)chars.get(i), tobj)) {
            useCallLists = false;
            break;
        }
    }
    // if we have display lists for all of the characters, use
    // glCallLists:
    if (useCallLists) {
        myFont.callFrontLists(gl2, line);
    }
    // if we don't, draw the string character-by-character, using the
    // display lists we do have:
    else {
        myFont.renderFront(gl2, line, tobj);
    }
}    


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Render the sides of the given string.  The GL transformation
//    matrix is munged by this routine-- surround it by
//    PushMatrix/PopMatrix.
//
// Use: private

private void renderSide(SoGLRenderAction action, int line)
//
////////////////////////////////////////////////////////////////////////
{
    ShortBuffer chars = myFont.getUCSString(line).asShortBuffer();

    // First, try to figure out if we can use glCallLists:
    boolean useCallLists = true;
    
    GL2 gl2 = action.getCacheContext();

    for (int i = 0; i < myFont.getNumUCSChars(line); i++) {
        // See if the font cache already has (or can build) a display
        // list for this character:
        if (!myFont.hasSideDisplayList(gl2, (char)chars.get(i),  new SideCB() {

			@Override
			public void run(int nPoints, SbVec3f[] points1, SbVec3f[] norms1, SbVec3f[] points2, SbVec3f[] norms2,
					float[] sTexCoords, float[] tTexCoords, final int tTexCoordsIndex) {
				renderSideTris(gl2, nPoints, points1, norms1, points2, norms2,
						sTexCoords, tTexCoords, tTexCoordsIndex);
			}
        	
        })) {
            useCallLists = false;
            break;
        }
    }
    // if we have display lists for all of the characters, use
    // glCallLists:
    if (useCallLists) {
        myFont.callSideLists(gl2, line);
    }
    // if we don't, draw the string character-by-character, using the
    // display lists we do have:
    else {
        myFont.renderSide(gl2, line, new SideCB() {

			@Override
			public void run(int nPoints, SbVec3f[] points1, SbVec3f[] norms1, SbVec3f[] points2, SbVec3f[] norms2,
					float[] sTexCoords, float[] tTexCoords, int tTexCoordsIndex) {
				renderSideTris(gl2, nPoints, points1, norms1, points2, norms2,
						sTexCoords, tTexCoords, tTexCoordsIndex);
			}
        	
        });
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Given two correctly rotated and positioned bevels, this routine
//    renders triangles in between them.
//
// Use: private, static

private static void renderSideTris(GL2 gl2, int nPoints, final SbVec3f[] p1, final SbVec3f[] n1,
                        final SbVec3f[] p2, final SbVec3f[] n2,
                        final float[] sTex, final float[] tTex, final int tTexIndex)
//
////////////////////////////////////////////////////////////////////////
{
    // Note:  the glBegin(GL_QUADS) is optimized up into the
    // routine that calls generateSideChar, so there is one glBegin
    // per character.
    for (int i = 0; i < nPoints-1; i++) {
        if (genTexCoord) gl2.glTexCoord2f(sTex[i+1], tTex[0+tTexIndex]);
        gl2.glNormal3fv(n1[i*2+1].getValueRead(),0);
        gl2.glVertex3fv(p1[i+1].getValueRead(),0);

        if (genTexCoord) gl2.glTexCoord2f(sTex[i+1], tTex[1+tTexIndex]);
        gl2.glNormal3fv(n2[i*2+1].getValueRead(),0);
        gl2.glVertex3fv(p2[i+1].getValueRead(),0);

        if (genTexCoord) gl2.glTexCoord2f(sTex[i], tTex[1+tTexIndex]);
        gl2.glNormal3fv(n2[i*2].getValueRead(),0);
        gl2.glVertex3fv(p2[i].getValueRead(),0);

        if (genTexCoord) gl2.glTexCoord2f(sTex[i], tTex[0+tTexIndex]);
        gl2.glNormal3fv(n1[i*2].getValueRead(),0);
        gl2.glVertex3fv(p1[i].getValueRead(),0);
    }
}
            


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Figure out how much to offset the given line of text.  The X
//    offset depends on the justification and the width of the string
//    in the current font.  The Y offset depends on the line spacing
//    and font height.
//
// Use: private

public SbVec2f getStringOffset(int line)
//
////////////////////////////////////////////////////////////////////////
{
    final SbVec2f result = new SbVec2f(0,0);
    
    if (justification.getValue() == Justification.RIGHT.getValue()) {
        float width = myFont.getWidth(line);
        result.getValue()[0] = -width;
    }
    if (justification.getValue() == Justification.CENTER.getValue()) {
        float width = myFont.getWidth(line);
        result.getValue()[0] = -width/2.0f;
    }
    result.getValue()[1] = -line*myFont.getHeight()*spacing.getValue();

    return result;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Setup internal font cache.  Called by all action methods before
//    doing their thing.  GLRender passes TRUE to do special rendering
//    setup.  Returns FALSE if there are problems and the action
//    should bail.
//
// Use: private

//! Setup the internal font cache
private boolean setupFontCache(SoState state) {
	return setupFontCache(state, false);
}
private boolean setupFontCache(SoState state, boolean forRender)
//
////////////////////////////////////////////////////////////////////////
{
    // The state must be pushed here because myFont.isRenderValid
    // depends on the state being the correct depth (it must be the
    // same depth as when the font cache was built).
    state.push();

    if (myFont != null) {
        boolean isValid;
        if (forRender)
            isValid = myFont.isRenderValid(state);
        else
            isValid = myFont.isValid(state);

        if (!isValid) {
            myFont.unref(state);
            myFont = null;
        }
    }
    if (myFont == null) {
        myFont = SoOutlineFontCache.getFont(state, forRender);
    }
    
    //The current text must be translated to UCS, unless this
    //translation has already been done.
    
    if(myFont!=null) myFont.convertToUCS(getNodeId(), string);
    state.pop();
    return  myFont != null;
}



	@Override
	public void computeBBox(SoAction action, final SbBox3f box, final SbVec3f center) {
    // Set the center to be the origin, which is the natural "center"
    // of the text, regardless of justification
    center.setValue(0.0f, 0.0f, 0.0f);

    int prts = parts.getValue();
    if (prts == 0)
        return;

    SoState state = action.getState();

    if (!setupFontCache(state))
        return;

    // Get the bounding box of all the characters:
    final SbBox2f outlineBox = new SbBox2f();
    getFrontBBox(outlineBox);

    // If no lines and no characters, return empty bbox:
    if (outlineBox.isEmpty()) return;
    
    // .. and extend it based on what parts are turned on:
    final float[] firstZ = new float[1], lastZ = new float[1];
    myFont.getProfileBounds(firstZ, lastZ);

    final SbVec2f boxMin = outlineBox.getMin();
    final SbVec2f boxMax = outlineBox.getMax();
                     

    // Front and back are straightforward:
    if ((prts & Part.FRONT.getValue())!= 0) {
        SbVec3f min = new SbVec3f(boxMin.getValue()[0], boxMin.getValue()[1], firstZ[0]);
        SbVec3f max = new SbVec3f(boxMax.getValue()[0], boxMax.getValue()[1], firstZ[0]);
        box.extendBy(min);
        box.extendBy(max);
    }
    if ((prts & Part.BACK.getValue())!=0) {
        SbVec3f min = new SbVec3f(boxMin.getValue()[0], boxMin.getValue()[1], lastZ[0]);
        SbVec3f max = new SbVec3f(boxMax.getValue()[0], boxMax.getValue()[1], lastZ[0]);
        box.extendBy(min);
        box.extendBy(max);
    }
    //
    // Sides are trickier.  We figure out the maximum offset
    // of the profile we're using.  If the offset is
    // negative, we use its absolute value; normally, negative
    // (indented) characters won't expand the bounding box, but if the
    // offset is negative enough the character will inter-penetrate
    // itself-- think of a Helvetica 'I', with a bevel of a big
    // negative spike.  The bounding box is either the bounding box of
    // the front/back, or, if the spike is big enough, the size of the
    // spike minus the width of the I.  I'm being conservative here
    // and just expanding the front/back bounding boxes by the maximum
    // offset (correct for positive offsets, conservative for negative
    // offsets).
    //
    if ((prts & Part.SIDES.getValue())!=0 && myFont.hasProfile()) {
        final SbBox2f profileBox = new SbBox2f();
        myFont.getProfileBBox(profileBox);

        final SbVec2f pBoxMin = profileBox.getMin();
        final SbVec2f pBoxMax = profileBox.getMax();
        
        // If no profile, return the front/back bbox:
        if (profileBox.isEmpty()) return;

        //
        // Expand the bounding box forward/backward in case the
        // profile extends forwards/backwards:
        //
        final SbVec3f min = new SbVec3f(), max = new SbVec3f();
        min.setValue(boxMin.getValue()[0], boxMin.getValue()[1], pBoxMin.getValue()[0]);
        max.setValue(boxMax.getValue()[0], boxMax.getValue()[1], pBoxMax.getValue()[0]);
        box.extendBy(min);
        box.extendBy(max);

        //
        // And figure out the maximum profile offset, and expand
        // out the outline's bbox:
        //
//# define max(a,b)               (a<b ? b : a)
//# define abs(x)                 (x>=0 ? x : -(x))
        float maxOffset = Math.max(Math.abs(pBoxMin.getValue()[1]), Math.abs(pBoxMax.getValue()[1]));
//#undef max
//#undef abs
        min.setValue(boxMin.getValue()[0]-maxOffset, boxMin.getValue()[1]-maxOffset, firstZ[0]);
        max.setValue(boxMax.getValue()[0]+maxOffset, boxMax.getValue()[1]+maxOffset, lastZ[0]);
        box.extendBy(min);
        box.extendBy(max);
    }
	}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Gets the 2D bounding box of the outlines of the letters in our
//    string.  How?  Get all the outlines and extend the bbox by every
//    point in every outline... (assuming the lines or characters
//    don't overlap, this could be optimized by just considering the
//    characters on the borders of the string-- although there are
//    some gotchas, like spaces at the beginning of a line...).
//
// Use: private

private void getFrontBBox(final SbBox2f result)
//
////////////////////////////////////////////////////////////////////////
{
    final SbBox2f charBBox = new SbBox2f();

    int line, character;
    for (line = 0; line < string.getNum(); line++) {
        // Starting position of string, based on justification:
        SbVec2f charPosition = getStringOffset(line);
       
        ShortBuffer chars = myFont.getUCSString(line).asShortBuffer();
        
        for (character = 0; character < myFont.getNumUCSChars(line); 
                character++) {
            myFont.getCharBBox((char)chars.get(character), charBBox);
            if (!charBBox.isEmpty()) {
                SbVec2f min = charBBox.getMin().operator_add(charPosition);
                SbVec2f max = charBBox.getMax().operator_add(charPosition);
                result.extendBy(min);
                result.extendBy(max);
            }

            // And advance...
            charPosition.operator_add_equal( myFont.getCharOffset((char)chars.get(character)));
        }
    }
}

	
////////////////////////////////////////////////////////////////////////
//
//Description:
//Generates triangles representing a Text3.
//
//Use: protected

	@Override
	protected void generatePrimitives(SoAction action) {
    SoState state = action.getState();
    
    GL2 gl2 = state.getGL2(); // java port

    if (!setupFontCache(state))
        return;

    currentGeneratingNode = this;

    // Set up default texture coordinate mapping, if necessary:
    SoTextureCoordinateElement.CoordType tcType =
        SoTextureCoordinateElement.getType(state);
    if (tcType == SoTextureCoordinateElement.CoordType.EXPLICIT) {
        genTexCoord = true;
        tce = null;
    } else {
        genTexCoord = false;
        tce = SoTextureCoordinateElement.getInstance(state);
    }

    // Set up 3 vertices we can use
    final SoPrimitiveVertex   v1 = new SoPrimitiveVertex(), v2 = new SoPrimitiveVertex(), v3 = new SoPrimitiveVertex();
    final SoTextDetail detail = new SoTextDetail();
    v1.setDetail(detail);
    v2.setDetail(detail);
    v3.setDetail(detail);

    // dont copy
    genPrimVerts[0] = v1;
    genPrimVerts[1] = v2;
    genPrimVerts[2] = v3;

    genAction = action;
    genBack = false;

    SoMaterialBindingElement.Binding mbe =
        SoMaterialBindingElement.get(state);
    boolean materialPerPart =
        (mbe == SoMaterialBindingElement.Binding.PER_PART_INDEXED ||
         mbe == SoMaterialBindingElement.Binding.PER_PART);
    if (!materialPerPart) {
        v1.setMaterialIndex(0);
        v2.setMaterialIndex(0);
        v3.setMaterialIndex(0);
    }

    final float[] firstZ = new float[1], lastZ = new float[1];
    myFont.getProfileBounds(firstZ, lastZ);

    int prts = parts.getValue();
    if ((prts & Part.SIDES.getValue())!=0 && myFont.hasProfile()) {
        if (materialPerPart) {
            v1.setMaterialIndex(1);
            v2.setMaterialIndex(1);
            v3.setMaterialIndex(1);
        }
        detail.setPart(Part.SIDES);

        for (int line = 0; line < string.getNum(); line++) {
            detail.setStringIndex(line);

            SbVec2f p = getStringOffset(line);
            genTranslate.setValue(p.getValue()[0], p.getValue()[1], lastZ[0]);
            generateSide(line);
        }
    }
    if ((prts & Part.BACK.getValue())!=0) {
        genBack = true;
        if (materialPerPart) {
            v1.setMaterialIndex(2);
            v2.setMaterialIndex(2);
            v3.setMaterialIndex(2);
        }
        detail.setPart(Part.BACK);

        v1.setNormal(new SbVec3f(0, 0, -1));
        v2.setNormal(new SbVec3f(0, 0, -1));
        v3.setNormal(new SbVec3f(0, 0, -1));
        
        for (int line = 0; line < string.getNum(); line++) {
            detail.setStringIndex(line);

            SbVec2f p = getStringOffset(line);
            genTranslate.setValue(p.getValue()[0], p.getValue()[1], lastZ[0]);
            generateFront(gl2, line);
        }
        genBack = false;
    }   
    if ((prts & Part.FRONT.getValue())!= 0) {
        if (materialPerPart) {
            v1.setMaterialIndex(0);
            v2.setMaterialIndex(0);
            v3.setMaterialIndex(0);
        }
        detail.setPart(Part.FRONT);

        v1.setNormal(new SbVec3f(0, 0, 1));
        v2.setNormal(new SbVec3f(0, 0, 1));
        v3.setNormal(new SbVec3f(0, 0, 1));
        
        for (int line = 0; line < string.getNum(); line++) {
            detail.setStringIndex(line);

            SbVec2f p = getStringOffset(line);
            genTranslate.setValue(p.getValue()[0], p.getValue()[1], firstZ[0]);
            generateFront(gl2, line);
        }
    }
    
    v1.destructor();
    v2.destructor();
    v3.destructor();
    detail.destructor();
	}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Generates sides of the characters.
//
// Use: private internal

void generateSide(int line)
//
////////////////////////////////////////////////////////////////////////
{
    ShortBuffer chars = myFont.getUCSString(line).asShortBuffer();

    SoTextDetail d = (SoTextDetail )genPrimVerts[0].getDetail();

    for (int i = 0; i < myFont.getNumUCSChars(line); i++) {
        d.setCharacterIndex(i);

        myFont.generateSideChar((char)chars.get(i),new SideCB() {

			@Override
			public void run(int nPoints, SbVec3f[] points1, SbVec3f[] norms1, SbVec3f[] points2, SbVec3f[] norms2,
					float[] sTexCoords, float[] tTexCoords, int tTexCoordsIndex) {
				generateSideTris( nPoints, points1, norms1, points2, norms2,
						sTexCoords, tTexCoords, tTexCoordsIndex);
			}
        	
        });

        SbVec2f p = myFont.getCharOffset((char)chars.get(i));
        genTranslate.getValue()[0] += p.getValue()[0];
        genTranslate.getValue()[1] += p.getValue()[1];
    }
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Generates primitives for the fronts of characters.  Assumes that
//    genTranslate has been set to the starting position of the
//    string, and that the genPrimVerts array points to three
//    appropriate vertices.
//
// Use: private internal

static GLUtessellator tobjgenerateFront = null;

void generateFront(GL2 gl2, int line)
//
////////////////////////////////////////////////////////////////////////
{

    ShortBuffer chars = myFont.getUCSString(line).asShortBuffer();

    if (tobjgenerateFront == null) {
    	tobjgenerateFront = GLU.gluNewTess();
    	GLUtessellatorCallback cb = new GLUtessellatorCallbackAdapter() {
    		
    		public void begin(int val) {
    			beginCB(val);
    		}
    		
    		public void end() {
    			endCB();
    		}
    		
    		public void vertex(Object obj) {
    			vtxCB(obj);
    		}
    		
    		public void error(int val) {
    			SoOutlineFontCache.errorCB(val);
    		}
    	};
        GLU.gluTessCallback(tobjgenerateFront, GLU.GLU_BEGIN, cb/*(OPENGL_CALLBACKFUNC)SoText3::beginCB*/);
        GLU.gluTessCallback(tobjgenerateFront, GLU.GLU_END, cb/*(OPENGL_CALLBACKFUNC)SoText3::endCB*/);
        GLU.gluTessCallback(tobjgenerateFront, GLU.GLU_VERTEX, cb/*(OPENGL_CALLBACKFUNC)SoText3::vtxCB*/);
        GLU.gluTessCallback(tobjgenerateFront, GLU.GLU_ERROR, cb/*(OPENGL_CALLBACKFUNC)SoOutlineFontCache::errorCB*/);
    }

    genWhichVertex = 0;

    SoTextDetail d = (SoTextDetail )genPrimVerts[0].getDetail();

    for (int i = 0; i < myFont.getNumUCSChars(line); i++) {
        d.setCharacterIndex(i);

        myFont.generateFrontChar(gl2, (char)chars.get(i), tobjgenerateFront);

        SbVec2f p = myFont.getCharOffset((char)chars.get(i));
        genTranslate.setValue(0, genTranslate.getValueRead()[0] + p.getValue()[0]);
        genTranslate.setValue(1, genTranslate.getValueRead()[1] + p.getValue()[1]);
    }
}

            
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Called by the GLU tesselator when we are beginning a triangle
//    strip, fan, or set of independent triangles.
//
// Use: static, private

void beginCB(int primType)
//
////////////////////////////////////////////////////////////////////////
{
    genPrimType = primType;
    genWhichVertex = 0;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Called by the GLU tesselator when we are done with the
//    strip/fan/etc.
//
// Use: static, private

void endCB()
//
////////////////////////////////////////////////////////////////////////
{
    genWhichVertex = 0;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Called by the GLU tesselator when we are generating primitives.
//
// Use: static, private

void vtxCB(Object v)
//
////////////////////////////////////////////////////////////////////////
{
    SbVec2f vv = (SbVec2f)v;
    final float[] vertex = new float[3];
    vertex[0] = vv.getValue()[0] + genTranslate.getValueRead()[0];
    vertex[1] = vv.getValue()[1] + genTranslate.getValueRead()[1];
    vertex[2] = genTranslate.getValueRead()[2];

    SoText3 t3 = currentGeneratingNode;
    
    // Fill in one of the primitive vertices:
    genPrimVerts[genWhichVertex].setPoint(new SbVec3f(vertex));

    final SbVec4f texCoord = new SbVec4f();
    
    // And texture coordinates:
    if (genTexCoord) {
        float textHeight = t3.myFont.getHeight();
        texCoord.setValue(vertex[0]/textHeight, vertex[1]/textHeight,
                          0.0f, 1.0f);
        // S coordinates go other way on back...
        if (genBack) texCoord.getValue()[0] = -texCoord.getValue()[0];
    } else {
        texCoord.copyFrom( tce.get(new SbVec3f(vertex), genPrimVerts[0].getNormal()));
    }
    genPrimVerts[genWhichVertex].setTextureCoords(texCoord);
        
    genWhichVertex = (genWhichVertex+1)%3;

    // If we just filled in the third vertex, we can spit out a
    // triangle:
    if (genWhichVertex == 0) {
        // If we are doing the BACK part, reverse the triangle:
        if (genBack) {
            t3.invokeTriangleCallbacks(genAction,
                                        genPrimVerts[2],
                                        genPrimVerts[1],
                                        genPrimVerts[0]);
        } else {
            t3.invokeTriangleCallbacks(genAction,
                                        genPrimVerts[0],
                                        genPrimVerts[1],
                                        genPrimVerts[2]);
        }
        // Now, need to set-up for the next vertex.
        // Three cases to deal with-- independent triangles, triangle
        // strips, and triangle fans.
        switch (genPrimType) {
          case GL2.GL_TRIANGLES:
            // Don't need to do anything-- every three vertices
            // defines a triangle.
            break;

// Useful macro:
//#define SWAP(a, b) { SoPrimitiveVertex *t = a; a = b; b = t; }

          case GL2.GL_TRIANGLE_FAN:
            // For triangle fans, vertex zero stays the same, but
            // vertex 2 becomes vertex 1, and the next vertex to come
            // in will replace vertex 2 (the old vertex 1).
            SWAP(1, 2, genPrimVerts);
            genWhichVertex = 2;
            break;

          case GL2.GL_TRIANGLE_STRIP:
            // For triangle strips, vertex 1 becomes vertex 0, vertex
            // 2 becomes vertex 1, and the new triangle will replace
            // vertex 2 (the old vertex 0).
            SWAP(1, 0, genPrimVerts);
            SWAP(2, 1, genPrimVerts);
            genWhichVertex = 2;
            break;
//#undef SWAP
        }
    }
}

private void SWAP(int a, int b, SoPrimitiveVertex[] array) { 
	SoPrimitiveVertex t = array[a]; array[a] = array[b]; array[b] = t; 
	}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Given two correctly rotated and positioned bevels, this routine
//    fills in the triangles in between them.
//
// Use: private, static

//Handy little macro to set a primitiveVertice's point, normal, and
//texture coordinate:
private void SET(int pv, int i, int row, int col,final SbVec3f[][] p,final SbVec3f[][] n, final SbVec4f texCoord,final float[] sTexCoords, final float[] tTexCoords, final int tTexCoordsIndex) 
 {
    final float[] vertex = new float[3];
	vertex[0] = p[col][i+row].getValueRead()[0] + genTranslate.getValueRead()[0]; 
  vertex[1] = p[col][i+row].getValueRead()[1] + genTranslate.getValueRead()[1]; 
  vertex[2] = p[col][i+row].getValueRead()[2]; 
  genPrimVerts[pv].setPoint(new SbVec3f(vertex));
  genPrimVerts[pv].setNormal(n[col][i*2+row]); 
  texCoord.getValue()[0] = sTexCoords[i+row]; 
  texCoord.getValue()[1] = tTexCoords[col+tTexCoordsIndex]; 
  genPrimVerts[pv].setTextureCoords(texCoord); 
 }

void generateSideTris(int nPoints, final SbVec3f[] p1, final SbVec3f[] n1,
                final SbVec3f[] p2, final SbVec3f[] n2,
                final float[] sTexCoords, final float[] tTexCoords, final int tTexCoordsIndex)
//
////////////////////////////////////////////////////////////////////////
{
    final float[] vertex = new float[3];
    final SbVec4f texCoord = new SbVec4f(0,0,0,1);
    
    final SbVec3f[][] p = new SbVec3f[2][]; p[0] = p1; p[1] = p2;
    final SbVec3f[][] n = new SbVec3f[2][]; n[0] = n1; n[1] = n2;

    SoText3 t3 = currentGeneratingNode;

    for (int i = 0; i < nPoints-1; i++) {
        // First triangle: 
        SET(0, i, 0, 0,p,n,texCoord,sTexCoords,tTexCoords,tTexCoordsIndex);
        SET(1, i, 1, 0,p,n,texCoord,sTexCoords,tTexCoords,tTexCoordsIndex);
        SET(2, i, 0, 1,p,n,texCoord,sTexCoords,tTexCoords,tTexCoordsIndex);
        t3.invokeTriangleCallbacks(genAction, genPrimVerts[0],
                                    genPrimVerts[1], genPrimVerts[2]);

        // Second triangle:
        SET(0, i, 1, 1,p,n,texCoord,sTexCoords,tTexCoords,tTexCoordsIndex);
        SET(1, i, 0, 1,p,n,texCoord,sTexCoords,tTexCoords,tTexCoordsIndex);
        SET(2, i, 1, 0,p,n,texCoord,sTexCoords,tTexCoords,tTexCoordsIndex);
        t3.invokeTriangleCallbacks(genAction, genPrimVerts[0],
                                    genPrimVerts[1], genPrimVerts[2]);
//#undef SET
    }
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    This initializes the SoText3 class.
//
// Use: internal

public static void initClass()
//
////////////////////////////////////////////////////////////////////////
{
    SO__NODE_INIT_CLASS(SoText3.class, "Text3", SoShape.class);

    // Font name and size elements are enabled by the SoFont node
}

}
