package jscenegraph.coin3d.inventor.nodes;

import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.port.FLoutline;

public class MyFontOutline {

    // This basically mimics the FLoutline structure, with the
    // exception that the font size is part of the outline:
    int numOutlines;
    int[] numVerts;
    SbVec2f[][] verts;
    final SbVec2f charAdvance = new SbVec2f();


////////////////////////////////////////////////////////////////////////
//
// Description:
//   Internal constructor used by getNullOutline
//
// Use: internal, static

    public MyFontOutline()
//
////////////////////////////////////////////////////////////////////////
    {
        charAdvance.copyFrom( new SbVec2f(0,0));
        numOutlines = 0;
        numVerts = null;
        verts = null;
    }

////////////////////////////////////////////////////////////////////////
//
// Description:
//   Copy info from the font library into a more convenient form.
//
// Use: internal

    MyFontOutline(FLoutline outline, float fontSize)
//
////////////////////////////////////////////////////////////////////////
    {
        charAdvance.copyFrom( new SbVec2f(outline.xadvance,
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

    // Query routines:
    public int         getNumOutlines() { return numOutlines; }
    public int         getNumVerts(int i) { return numVerts[i]; }
    public SbVec2f     getVertex(int i, int j) { return verts[i][j]; }
    public SbVec2f     getCharAdvance() { return charAdvance; }

    static MyFontOutline getNullOutline() {
        return new MyFontOutline();
    }

}
