package jscenegraph.coin3d.inventor.nodes;

import jscenegraph.coin3d.inventor.caches.SoGlyphCache;
import jscenegraph.coin3d.inventor.lists.SbList;
import jscenegraph.database.inventor.SbBox3f;

public class SoAsciiTextP {

    public SoAsciiText master;


    public final SbList <Float> glyphwidths = new SbList<>();
    public final SbList<Float> stringwidths = new SbList<>();
    public final SbBox3f maxglyphbbox = new SbBox3f();

    SoGlyphCache cache; // ptr

    public SoAsciiTextP( SoAsciiText master ) {
        this.master = master;
    }
}
