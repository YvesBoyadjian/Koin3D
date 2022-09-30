package jscenegraph.coin3d.inventor.nodes;

import jscenegraph.opengl.GL2;
import jscenegraph.database.inventor.*;
import jscenegraph.database.inventor.caches.SoCache;
import jscenegraph.database.inventor.elements.*;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.opengl.glu.GLU;
import jscenegraph.database.inventor.libFL.FLcontext;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoProfile;
import jscenegraph.database.inventor.nodes.SoShape;
import jscenegraph.mevis.inventor.system.SbSystem;
import jscenegraph.port.Destroyable;
import jscenegraph.port.FLoutline;
import jscenegraph.port.fl;
import org.lwjglx.BufferUtils;
import org.lwjglx.util.glu.GLUtessellator;

import java.nio.ByteBuffer;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public class MyOutlineFontCache extends SoCache {

    // Number of characters in this font. Until we internationalize,
    // this will be 128 or less.
    int         numChars;

    // Display list for fronts of characters
    SoGLDisplayList frontList;

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
    boolean[]        frontFlags;

    // List of outlines; these are also cached and created when
    // needed.
    MyFontOutline[]       outlines; //ptr

    // Font size
    float       fontSize;

    // Flag used to detect tesselation errors:
    static boolean tesselationError;

    // Font library identifier for this font
    /*FLfontNumber*/int        fontId;

    // Font library context for all outline fonts
    static FLcontext context;

    // Global list of available fonts; a 'font' in this case is a
    // unique set of font name, font size, complexity value/type, and
    // set of profiles-- if any of these changes, the set of polygons
    // representing the font will change, and a different font will be
    // used.
    static SbPList fonts; //ptr


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Find an appropriate font, given a state.  A bunch of elements
//    (fontName, fontSize, creaseAngle, complexity and profile) must
//    be enabled in the state...
//
// Use: static, internal

    public static MyOutlineFontCache
    getFont(SoState state, boolean forRender)
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
                SoDebugError.post("SoAsciiText::getFont",
                        "flCreateContext returned NULL");
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

        MyOutlineFontCache result = null; //ptr
        for (int i = 0; i < fonts.getLength() && result == null; i++) {
            MyOutlineFontCache c = (MyOutlineFontCache ) fonts.get(i);
            if (forRender ? c.isRenderValid(state) : c.isValid(state)) {
                result = c; // Loop will terminate...
                result.ref(); // Increment ref count
                if (fl.flGetCurrentFont() != result.fontId) {
                    fl.flMakeCurrentFont(result.fontId);
                }
            }
        }
        // If none match:
        if (result == null) {
            result = new MyOutlineFontCache(state);

            // If error:
            if (result.fontId == 0) {
                Destroyable.delete(result);
                return null;
            }
        }
        return result;
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
        boolean result = /*SoCache.*/super.isValid(state);

        if (result) {
            if (fl.flGetCurrentContext() != context) {
                fl.flMakeCurrentContext(context);
                fl.flMakeCurrentFont(fontId);
            }
            else if (fl.flGetCurrentFont() != fontId)
                fl.flMakeCurrentFont(fontId);
        }
        return result;
    }

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Figures out if this cache is valid for rendering.
//
// Use: internal

    public boolean
    isRenderValid(SoState state)
    //
////////////////////////////////////////////////////////////////////////
    {
        if (!isValid(state)) return false;

        if (frontList != null &&
                frontList.getContext() != SoGLCacheContextElement.get(state))
        return false;

        return true;
    }

    static float[][] m = { {1.0f, 0.0f}, {0.0f, 1.0f} };

    public MyOutlineFontCache(SoState state) {
        super(state);
        ref();

        // Add element dependencies explicitly here; making 'this' the
        // CacheElement doesn't work if we are being constructed in an
        // action that doesn't have caches.
        SbName font = SoFontNameElement.get(state);
        addElement(state.getConstElement(
                SoFontNameElement.getClassStackIndex(SoFontNameElement.class)));
        if (font == SoFontNameElement.getDefault()) {
            font = new SbName(SbSystem.IV_DEFAULT_FONTNAME);
        }

        float uems = 0; // java port

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
             float ZERO = 250;
             float HALF = 20;
             float ONE = 1;
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

        fontId = fl.flCreateFont(font.getString(), m, 0, null);

        // If error creating font:
        if (fontId == 0) {
            // Try IV_DEFAULT_FONTNAME, unless we just did!
            if (font.operator_not_equal(new SbName(SbSystem.IV_DEFAULT_FONTNAME))) {
//#ifdef DEBUG
                SoDebugError.post("SoAsciiText.getFont",
                        "Couldn't find font "+font.getString()+", replacing with "+ SbSystem.IV_DEFAULT_FONTNAME);
//#endif
                        fontId = fl.flCreateFont(SbSystem.IV_DEFAULT_FONTNAME, m, 0, null);
            }
            if (fontId == 0) {
//#ifdef DEBUG
                SoDebugError.post("SoAsciiText.getFont",
                        "Couldn't find font "+SbSystem.IV_DEFAULT_FONTNAME+"!" );
//#endif
                return;
            }
        }

        fl.flMakeCurrentFont(fontId);

        numChars = 256;  // ??? NEED TO REALLY KNOW HOW MANY CHARACTERS IN
        // FONT!
        frontList = null;

        frontFlags = new boolean[numChars];
        outlines = new MyFontOutline[numChars];
        int i;
        for (i = 0; i < numChars; i++) {
            frontFlags[i] = false;
            outlines[i] = null;
        }

        // Get profile info:
    SoNodeList profiles = SoProfileElement.get(state);
        addElement(state.getConstElement(
                SoProfileElement.getClassStackIndex(SoProfileElement.class)));
        addElement(state.getConstElement(
                SoProfileCoordinateElement.getClassStackIndex(SoProfileCoordinateElement.class)));
        nProfileVerts = 0;
        if (profiles.getLength() > 0) {
            SoProfile profileNode = (SoProfile )profiles.operator_square_bracket(0);
            final IntConsumer cons = new IntConsumer() {
                @Override
                public void accept(int value) {
                    nProfileVerts = value;
                }
            };
            final Consumer<SbVec2f[]> cons2 = new Consumer<SbVec2f[]>() {
                @Override
                public void accept(SbVec2f[] sbVec2fs) {
                    profileVerts = sbVec2fs;
                }
            };
            profileNode.getVertices(state, cons, cons2);
        } else {
            nProfileVerts = 2;
            profileVerts = new SbVec2f[2]; profileVerts[0] = new SbVec2f(); profileVerts[1] = new SbVec2f();
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
            figureSegmentNorms(profileNorms, (int) nProfileVerts, profileVerts,
                    cosCreaseAngle, false);
            // Need to flip all the normals because of the way the profiles
            // are defined:
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

    // Returns height of font
    float       getHeight() { return fontSize; }

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns the width of the given string
//
// Use: private

    public float
    getWidth(String string)
//
////////////////////////////////////////////////////////////////////////
    {
        float total = 0.0f;
    String chars = string/*.getString()*/;

        for (int i = 0; i < string.length(); i++) {
            MyFontOutline outline = getOutline(chars.charAt(i));
            total += outline.getCharAdvance().getValueRead()[0];
        }

        return total;
    }

    int VALIDATE_CHAR(int c) {
        return (((c) >= 0x20 && (c) <= 0x7F) ? (c) : 0x3f);
    }

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns the 2D bounding box of the given character.
//
// Use: private

    public void
    getCharBBox( char c, final SbBox2f result)
//
////////////////////////////////////////////////////////////////////////
    {
        result.makeEmpty();

        if (fontId==0) return;

        MyFontOutline outline = getOutline(c);

        for (int i = 0; i < outline.getNumOutlines(); i++) {
            for (int j = 0; j < outline.getNumVerts(i); j++) {
                result.extendBy(outline.getVertex(i,j));
            }
        }
    }

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Given a character, return an outline for the character.  If, for
//    some reason, we can't get the outline, an 'identity' or 'null'
//    outline is returned.
//
// Use: private

    public MyFontOutline
    getOutline( char c)
//
////////////////////////////////////////////////////////////////////////
    {
        if (fontId == 0) {
            return MyFontOutline.getNullOutline();
        }

        if (outlines[c] == null) {
            FLoutline flo = fl.flGetOutline(fontId, VALIDATE_CHAR((int)c));
            if (flo == null) {
                outlines[c] = MyFontOutline.getNullOutline();
            } else {
                outlines[c] = new MyFontOutline(flo, fontSize);
                fl.flFreeOutline(flo);
            }
        }
        return outlines[c];
    }

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Render the fronts of the given string.  The GL transformation
//    matrix is munged by this routine-- surround it by
//    PushMatrix/PopMatrix.
//
// Use: public, internal

    public void
    generateFrontChar(char c,
                                          /*gluTESSELATOR*/GLUtessellator tobj)
//
////////////////////////////////////////////////////////////////////////
    {
        if (fontId == 0) return;

        final double[] v = new double[3];

        tesselationError = false;
//#ifdef GLU_VERSION_1_2
        GLU.gluTessBeginPolygon(tobj, null);
        GLU.gluTessBeginContour(tobj);
//#else
//        gluBeginPolygon(tobj);
//#endif

        // Get outline for character
        MyFontOutline outline = getOutline(c);
        int i;
        for (i = 0; i < outline.getNumOutlines(); i++) {

            // It would be nice if the font manager told us the type of
            // each outline...
//#ifdef GLU_VERSION_1_2
            GLU.gluTessEndContour(tobj);
            GLU.gluTessBeginContour(tobj);
//#else
//            gluNextContour(tobj, (GLenum)GLU_UNKNOWN);
//#endif

            for (int j = 0; j < outline.getNumVerts(i); j++) {
                SbVec2f t = outline.getVertex(i,j); //ref
                v[0] = t.getValueRead()[0];
                v[1] = t.getValueRead()[1];
                v[2] = 0.0;

                // Note: The third argument MUST NOT BE a local variable,
                // since glu just stores the pointer and only calls us
                // back at the gluEndPolygon call.
                GLU.gluTessVertex(tobj, v, 0, t);
            }
        }
//#ifdef GLU_VERSION_1_2
        GLU.gluTessEndContour(tobj);
        GLU.gluTessEndPolygon(tobj);
//#else
//        gluEndPolygon(tobj);
//#endif

        // If there was an error tesselating the character, just generate
        // a bounding box for the character:
        if (tesselationError) {
            final SbBox2f charBBox = new SbBox2f();
            getCharBBox(c, charBBox);
            if (!charBBox.isEmpty()) {
                final SbVec2f[] boxVerts = new SbVec2f[4];
                boxVerts[0] = new SbVec2f();
                boxVerts[1] = new SbVec2f();
                boxVerts[2] = new SbVec2f();
                boxVerts[3] = new SbVec2f();
                charBBox.getBounds(boxVerts[0], boxVerts[2]);
                boxVerts[1].setValue(boxVerts[2].getValueRead()[0], boxVerts[0].getValueRead()[1]);
                boxVerts[3].setValue(boxVerts[0].getValueRead()[0], boxVerts[2].getValueRead()[1]);

//#ifdef GLU_VERSION_1_2
                GLU.gluTessBeginPolygon(tobj, null);
                GLU.gluTessBeginContour(tobj);
//#else
//                gluBeginPolygon(tobj);
//#endif
                for (i = 0; i < 4; i++) {
                    v[0] = boxVerts[i].getValueRead()[0];
                    v[1] = boxVerts[i].getValueRead()[1];
                    v[2] = 0.0;
                    GLU.gluTessVertex(tobj, v, 0, boxVerts[i]);
                }
//#ifdef GLU_VERSION_1_2
                GLU.gluTessEndContour(tobj);
                GLU.gluTessEndPolygon(tobj);
//#else
//                gluEndPolygon(tobj);
//#endif
            }
        }
    }

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets up for GL rendering.
//
// Use: internal

    public void
    setupToRenderFront(SoState state)
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
            // Set correct list base
            state.getGL2().glListBase(frontList.getFirstIndex());
            frontList.addDependency(state);
        }
    }

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns TRUE if a display lists exists for given character.
//    Tries to build a display list, if it can.
//
// Use: internal

    public boolean
    hasFrontDisplayList(final char c,
                                            /*gluTESSELATOR*/GLUtessellator tobj)
//
////////////////////////////////////////////////////////////////////////
    {
        // If we have one, return TRUE
        if (frontFlags[c] == true) return true;

        // If we don't and we can't build one, return FALSE.
        if (otherOpen) return false;

        // Build one:
        GL2 gl2 = new GL2(){};
        gl2.glNewList(frontList.getFirstIndex()+c, GL2.GL_COMPILE);
        generateFrontChar(c, tobj);
        SbVec2f t = getOutline(c).getCharAdvance();
        gl2.glTranslatef(t.getValueRead()[0], t.getValueRead()[1], 0.0f);
        gl2.glEndList();
        frontFlags[c] = true;

        return true;
    }

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Assuming that there are display lists built for all the
//    characters in given string, render them using the GL's CallLists
//    routine.
//
// Use: internal

    public void
    callFrontLists(String string, float off)
//
////////////////////////////////////////////////////////////////////////
    {
    ByteBuffer str = BufferUtils.createByteBuffer(string.length());
    for( int i=0;i<string.length();i++)
        str.put(i,(byte)string.charAt(i));/*.getString()*/;

    GL2 gl2 = new GL2() {
    };

        if (off == 0)
            gl2.glCallLists(string.length(), GL2.GL_UNSIGNED_BYTE, str);
        else {
            for (int i = 0; i < string.length(); ++i) {
                str.position(i);
                gl2.glCallLists(1, GL2.GL_UNSIGNED_BYTE, str/*+i*/);
                gl2.glTranslatef(off, 0.0f, 0.0f);
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

    public void
    renderFront(String string, float off,
                                    /*gluTESSELATOR*/GLUtessellator tobj)
//
////////////////////////////////////////////////////////////////////////
    {
    ByteBuffer str = BufferUtils.createByteBuffer(string.length());
        for( int i=0;i<string.length();i++)
            str.put(i,(byte)string.charAt(i));/*.getString()*/;

        GL2 gl2 = new GL2() {
        };

        //string.getString();

        for (int i = 0; i < string.length(); i++) {
            if (frontFlags[str.get(i)] && off == 0) {
                gl2.glCallList(frontList.getFirstIndex()+str.get(i));
            }
            else {
                generateFrontChar((char)str.get(i), tobj);
                SbVec2f t = getOutline((char)str.get(i)).getCharAdvance();
                gl2.glTranslatef(t.getValueRead()[0] + off, t.getValueRead()[1], 0.0f);
            }
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

    public void
    figureSegmentNorms(SbVec2f[] norms, int nPoints,
                            SbVec2f[] points,  float cosCreaseAngle,
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
            final SbVec2fSingle n = new SbVec2fSingle();
            // This is 2D perpendicular, assuming profile is increasing in
            // X (which becomes 'decreasing in Z' when we actually use
            // it...) (note: if a profile isn't increasing in X, the
            // character will be inside-out, with the front face drawn
            // behind the back face, etc).
            SbVec2f v = points[(i+1)%nPoints].operator_minus(points[i]);
            n.getValue()[0] = v.getValueRead()[1];
            n.getValue()[1] = -v.getValueRead()[0];
            n.normalize();

            norms[i*2] = n;
            norms[i*2+1] = n;
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
                SbVec2f average = norms[i*2+1].operator_add(norms[(i+1)*2]);
                average.normalize();
                norms[i*2+1] = average;
                norms[((i+1)%nPoints)*2] = average;
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

    public void
    figureSegmentTexCoords(float[] texCoords, int nPoints,
                            SbVec2f[] points, boolean isClosed)
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
            for (/*int*/ i = 0; i < nPoints; i++) {
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
//    Called by the GLU tesselator when there is an error
//
// Use: static, private
//
////////////////////////////////////////////////////////////////////////

    public static void
    errorCB(int whichErr)
    {
        SoDebugError.post("SoAsciiText.errorCB", GLU.gluErrorString(whichErr));
        tesselationError = true;
    }
}
