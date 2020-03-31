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
 |   $Revision: 1.6 $
 |
 |   Classes:
 |      SoText2
 |
 |   Author(s)          : Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.nodes;

import static jscenegraph.database.inventor.libFL.FLcontext.FL_FONTNAME;
import static jscenegraph.database.inventor.libFL.FLcontext.FL_HINT_MINOUTLINESIZE;
import static org.lwjgl.opengl.GL11.glBitmap;
import static org.lwjgl.opengl.GL11.glCallLists;
import static org.lwjgl.opengl.GL11.glListBase;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.ShortBuffer;

import org.lwjglx.BufferUtils;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2;

import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbDict;
import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SbPList;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbViewportRegion;
import jscenegraph.database.inventor.caches.SoCache;
import jscenegraph.database.inventor.elements.SoCacheElement;
import jscenegraph.database.inventor.elements.SoFontNameElement;
import jscenegraph.database.inventor.elements.SoFontSizeElement;
import jscenegraph.database.inventor.elements.SoGLCacheContextElement;
import jscenegraph.database.inventor.elements.SoGLDisplayList;
import jscenegraph.database.inventor.elements.SoViewportRegionElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoMFString;
import jscenegraph.database.inventor.libFL.FLcontext;
import jscenegraph.database.inventor.libFL.FLcontext.FLbitmap;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.port.fl;


// An internal class that makes life easier:

// This very specialized cache class is used to cache bitmaps and GL
// display lists containing bitmaps.  It is strange because it doesn't
// use the normal list of elements used to determine validity, etc,
// and knows exactly which elements it depends on.

/**
 * @author Yves Boyadjian
 *
 */
public class SoBitmapFontCache extends SoCache {

	public static final String IV_DEFAULT_FONTNAME = "Times";

    // Static list of all fonts.  OPTIMIZATION:  If there turn out to
    // be applications that use lots of fonts, we could change this
    // list into a dictionary keyed off the font name.
    private static SbPList      fonts;

    int         numChars;  // Number of characters in this font
         
    private SoGLDisplayList list;
    
    // Dictionary to point to unicode-character display lists;
    // Keyed by unicode value
    private SbDict      displayListDict;
        
    // Dictionary to point to bitmap; keyed by unicode value.
    private SbDict      bitmapDict;

    // This flag will be true if there is another cache open (if
    // building GL display lists for render caching, that means we
    // can't also build display lists).
    private boolean        otherOpen;
    
    // This indicates the nodeId of the last created cache, so we can
    // know when a new UCS-2 translation is required
    private long    currentNodeId;

    // And some font library stuff:
    private static FLcontext    flContext;

    private String fontNumList;
    private SbPList fontNums;

    // char* pointers of UCS-2 strings:
    private final SbPList     UCSStrings = new SbPList();
    // size of these strings, in UCS-2 characters:
    private final SbPList     UCSNumChars = new SbPList();

    
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Construct a bitmap font cache, given the state and a dummy
//    (empty) list of overridden elements (needed only to pass to the
//    SoCache constructor).
//
// Use: internal, private

public SoBitmapFontCache(SoState state) { super(state);
//
////////////////////////////////////////////////////////////////////////

    ref();
    
    list = null;

    // Grab all the stuff we'll need to determine our validity from
    // the state.
    SbName fontName = SoFontNameElement.get(state);
    addElement(state.getConstElement(
        SoFontNameElement.getClassStackIndex(SoFontNameElement.class)));
    if (fontName.operator_equal_equal(SoFontNameElement.getDefault())) {
        fontName = new SbName(IV_DEFAULT_FONTNAME);
    }
    final SbViewportRegion vpr = SoViewportRegionElement.get(state);
    addElement(state.getConstElement(
        SoViewportRegionElement.getClassStackIndex(SoViewportRegionElement.class)));
    float fontSize = SoFontSizeElement.get(state) * vpr.getPixelsPerPoint();
    addElement(state.getConstElement(
        SoFontSizeElement.getClassStackIndex(SoFontSizeElement.class)));

    // Initialize font list
    fontNumList = createUniFontList(fontName.getString(), fontSize);
 

    if (fontNumList == null) {
        // Try IV_DEFAULT_FONTNAME, unless we just did!
        if (fontName.operator_not_equal(new SbName(IV_DEFAULT_FONTNAME))) {
//#ifdef DEBUG
            SoDebugError.post("SoText2::getFont",
                      "Couldn't find font "+fontName.getString()+", replacing with "+IV_DEFAULT_FONTNAME);
//#endif
            fontNumList = createUniFontList(IV_DEFAULT_FONTNAME, fontSize);
        
        }
        if (fontNumList == null) {
//#ifdef DEBUG
            SoDebugError.post("SoText3::getFont",
                               "Couldn't find font "+IV_DEFAULT_FONTNAME+"!");
//#endif
            numChars = 0;
        }
    }

    
    numChars = 65536;  // ??? Just do UCS-2
    currentNodeId = 0;
    displayListDict = new SbDict();
    bitmapDict = new SbDict();
    
    fonts.append(this);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Create a list of font numbers from a list of font names
//
// Use: private
private String createUniFontList(String fontNameList, float size) 
//
////////////////////////////////////////////////////////////////////////
{
    String s;//char *s, *s1, *ends;
    int fn;
    float[][] mat = new float[2][2];

    mat[0][0] = mat[1][1] = size;
    mat[0][1] = mat[1][0] = 0.0f;
    
    //Make a copy of fontNameList so we don't disturb the one we are passed.    
    //Find \n at end of namelist:
    String nameCopy = fontNameList;

    //find the last null in nameCopy.    
    s = nameCopy+ ';';  /* put a guard in the end of string */
    
    fontNums = new SbPList();
      
//    while (s1 = (char *)strchr(s, ';')) {
//       *s1 = (char)NULL;  /* font name is pointed to s */

       if ((fn = fl.flCreateFont((String)s.substring(0,s.length()-1), mat, 0, null)) == 0) {
//#ifdef DEBUG
            SoDebugError.post("SoBitmapFontCache::createUniFontList", 
                "Cannot create font "+ s);         
//#endif          
       }
       else {
         fontNums.append((int)fn);       
       }
//       if(s1 == ends) break;
//       s = (s1 + 1);  /* move to next font name */
//    }
  
    if (fontNums.getLength() == 0 ) return null;
    
    // create a comma-separated list of font numbers:
    String fontList = "";
    //fontList[0] = '\0';
    for (int i = 0; i< fontNums.getLength(); i++ ){
        fn = (int)(fontNums).operator_square_bracket(i);
        fontList += fn; fontList +=',';//sprintf(&fontList[strlen(fontList)], "%d,", fn);        
    }
    //fontList[strlen(fontList) - 1] = '\0'; // the last ',' is replaced with NULL
    fontList = fontList.substring(0, fontList.length()-1);
    
    //delete [] nameCopy;
                                                    
    return (String)fontList;
   
}



////////////////////////////////////////////////////////////////////////
//
//Description:
//Returns TRUE if this cache is valid
//
//Use: internal, public

public boolean isRenderValid(SoState state) {
    if (list == null) return isValid(state);
    else
        return (list.getContext() == SoGLCacheContextElement.get(state)
                 && isValid(state));
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sees if this font is valid.  If it is valid, it also makes it
//    current.
//
// Use: public

public boolean
isValid( SoState state) 
//
////////////////////////////////////////////////////////////////////////
{
    boolean result = super.isValid(state);
    
    if (result) {
        if (fl.flGetCurrentContext() != flContext) {
            fl.flMakeCurrentContext(flContext);
        }
    }
    return result;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Get a font cache suitable for using with the given state.
//
// Use: static, internal, public

public static SoBitmapFontCache 
getFont(SoState state, boolean forRender)
//
////////////////////////////////////////////////////////////////////////
{
    if (fonts == null) {
        // One-time font library initialization
        fonts = new SbPList();
        flContext = fl.flCreateContext(null, FL_FONTNAME, null,
                                  1.0f, 1.0f);
        if (flContext == null) {
//#ifdef DEBUG
            SoDebugError.post("SoText2::getFont",
                               "flCreateContext returned null");
//#endif
            return null;
        }
        if (fl.flGetCurrentContext() != flContext)
            fl.flMakeCurrentContext(flContext);
        fl.flSetHint(FL_HINT_MINOUTLINESIZE, 24.0f);
    }
    else if (flContext == null) return null;
    else {
        if (fl.flGetCurrentContext() != flContext)
            fl.flMakeCurrentContext(flContext);
    }
    
    SoBitmapFontCache result = null;
    for (int i = 0; i < fonts.getLength() && result == null; i++) {
        SoBitmapFontCache fc = (SoBitmapFontCache )(fonts).get(i);
        if (fc.fontNumList == null) continue;
        if (forRender ? fc.isRenderValid(state) : fc.isValid(state)) {
            result = fc;
            result.ref();

        }           
    }
    if (result == null) {
        result = new SoBitmapFontCache(state);

    }    
    return result;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Convert MFString to UCS string, if necessary.
//
// Use: internal

public boolean convertToUCS(long nodeid, final SoMFString strings)                              
//
////////////////////////////////////////////////////////////////////////
{
    if (nodeid == currentNodeId) return true;
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
    
    return true;
}

    //Returns line of UCS-2 text
    public ByteBuffer      getUCSString(int line)
        { return (ByteBuffer)UCSStrings.operator_square_bracket(line);}
        
    int         getNumUCSChars(int line)
        { return (int)((Integer)UCSNumChars.operator_square_bracket(line));}
        

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets up for GL rendering.
//
// Use: internal

public void
setupToRender(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    otherOpen = SoCacheElement.anyOpen(state);

    if (!otherOpen && list == null) {
        list = new SoGLDisplayList(state,
                                   SoGLDisplayList.Type.DISPLAY_LIST,
                                   numChars);
        list.ref();
    }
    if (list != null) {
    	
    	GL2 gl2 = state.getGL2();
        // Set correct list base
        glListBase(list.getFirstIndex());
        list.addDependency(state);
    }
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns the pixel-space bounding box of given UCS-2 character.
//
// Use: internal, public

public void getCharBbox(char c, final SbBox3f box)
//
////////////////////////////////////////////////////////////////////////
{
    box.makeEmpty();

    FLbitmap bmap = getBitmap((char)c);
    if (bmap == null) return;
    
    box.extendBy(new SbVec3f(-bmap.xorig, -bmap.yorig, 0));
    box.extendBy(new SbVec3f(bmap.width - bmap.xorig,
                         bmap.height - bmap.yorig, 0));
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns the amount the current raster position will be advanced
//    after drawing the given UCS-2 character.
//
// Use: internal, public

public SbVec3f getCharOffset(char c)
//
////////////////////////////////////////////////////////////////////////
{
    FLbitmap bmap = getBitmap((char)c);
    if (bmap != null)
        return new SbVec3f(bmap.xmove, bmap.ymove, 0);
    else return new SbVec3f(0,0,0);
}
    
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns the width of specified UCS2 string.
//
// Use: internal, public

public float getWidth(int line)
//
////////////////////////////////////////////////////////////////////////
{
    float result = 0.0f;

    ShortBuffer str = getUCSString(line).asShortBuffer();
    for (int i = 0; i < getNumUCSChars(line); i++) {
        FLbitmap bmap = getBitmap((char)(str.get(/*2**/i)));
        if (bmap != null)
            result += bmap.xmove;
    }
    return result;
}
    
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns the height of given string.
//
// Use: internal, public

public float
getHeight()
//
////////////////////////////////////////////////////////////////////////
{
    //take height from UCS-2 code for "M"
    FLbitmap bmap = getBitmap((char)'M');
    if (bmap != null)
        return bmap.height - bmap.yorig;
    else return 0;
}



////////////////////////////////////////////////////////////////////////
//
// Description:
//    Draws a bitmap, using UCS-2 character
//
// Use: internal public

public void
drawCharacter(char c, GL2 gl2)
//
////////////////////////////////////////////////////////////////////////
{
    char uc = (char)c;
    final FLbitmap bmap = getBitmap(uc);
    
    if (bmap != null)
        glBitmap(bmap.width, bmap.height, bmap.xorig, bmap.yorig,
             bmap.xmove, bmap.ymove, Buffers.newDirectByteBuffer(bmap.bitmap));
//#ifdef DEBUG
    else SoDebugError.post("SoBitmapFontCache::drawCharacter", 
        "no bitmap for character "+uc+" "/*, uc[0]*256+uc[1]*/);
//#endif  
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Draws a whole string.  Tries to build display lists, if it can.
//    Assumes string is in UCS-2 format.
//
// Use: internal public

public void
drawString(int line, GL2 gl2)
//
////////////////////////////////////////////////////////////////////////
{
    boolean useCallLists = true;
    
    ByteBuffer strb = getUCSString(line);
    ShortBuffer str = strb.asShortBuffer();
    //String ustr = (String) str.toString();

    // If there aren't any other caches open, build display lists for
    // the characters we can:
    for (int i = 0; i < getNumUCSChars(line); i++) {
        // See if the font cache already has (or can build) a display
        // list for this character:
        if (!hasDisplayList((char)str.get(/*2**/i),gl2)) {// java port
            useCallLists = false;
            break;
        }
    }
        
    // if we have display lists for all of the characters, use
    // glCallLists:
    if (useCallLists) {
        callLists(strb, getNumUCSChars(line),gl2);
    } else {
        // if we don't, draw the string character-by-character, using the
        // display lists we do have:
        for (int i = 0; i < getNumUCSChars(line); i++) {
            if (!hasDisplayList((char)str.get(/* 2**/i),gl2)) {
                drawCharacter((char)str.get(/*2**/i),gl2);
            }
            else gl2.glCallList(list.getFirstIndex()+ str.get(i)/*ustr.charAt(i)*//*((ustr[2*i]<<8) | ustr[2*i+1])*/); // java port
        }
    }
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns a bitmap.
//
// Use: private

private FLbitmap getBitmap(char c)
//
////////////////////////////////////////////////////////////////////////
{
    if (fontNumList == null) return null;

    int key = (int)(/*c[0]<<8 | c[1]*/c);
    final Object[] value = new Object[1];
    if(!bitmapDict.find(key, value)){
        value[0] = (Object)fl.flUniGetBitmap(fontNumList, c);
        
//#ifdef DEBUG    
        if(value[0] == null){
            SoDebugError.post("SoBitmapFontCache::getBitmap", 
                "Invalid Unicode bitmap for character "+ key);
        }
//#endif /*DEBUG*/

        bitmapDict.enter(key, value[0]);
    }

    return (FLbitmap)value[0];
}



////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns TRUE if a display lists exists for given character.
//    Tries to build a display list, if it can.
//
// Use: internal

private boolean
hasDisplayList(char c, GL2 gl2)
//
////////////////////////////////////////////////////////////////////////
{
    char uc = (char)c;
    int key = Character.reverseBytes(uc);//(uc[0]<<8)|uc[1]; java port
    // If we have one, return TRUE
    final Object[] value = new Object[1];
    if (displayListDict.find(key, value)) return true;

    // If we don't and we can't build one, return FALSE.
    if (otherOpen) return false;
    
    // Build one:
    gl2.glNewList(list.getFirstIndex()+key, GL2.GL_COMPILE);
    drawCharacter(c,gl2);
    gl2.glEndList();
    
    displayListDict.enter(key, value[0]);

    return true;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Assuming that there are display lists built for all the
//    UCS-2 characters in given string, render them.
//    string should have already been converted from UTF8 form.
//
// Use: internal

private void
callLists(ByteBuffer string, int len, GL2 gl2)
//
////////////////////////////////////////////////////////////////////////
{

    glCallLists(GL2.GL_2_BYTES, string);
}



}
