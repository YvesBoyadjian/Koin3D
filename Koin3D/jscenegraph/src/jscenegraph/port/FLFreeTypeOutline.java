/**
 * 
 */
package jscenegraph.port;

import org.freetype.FT_GlyphSlot;

import jscenegraph.database.inventor.libFL.FLcontext.FLbitmap;

/**
 * @author Yves Boyadjian
 *
 */
public class FLFreeTypeOutline extends FLoutline {

	  //FLoutline     outline;        /* FL font outline struct */
	  final FLbitmap      bitmap = new FLbitmap();         /* FL bitmap struct */

	  short        ch;             /* char code point */
	  final FLpt2         size = new FLpt2();           /* size of char */
	  float       advance;        /* advancement of char */
	  boolean[]    polygons;       /* flags indicating the face starts */
	                                /* a polygon or not */
	  short[]        indexes;        /* face indexes into vertices */
	  short[]        tessIndexes;    /* tesselated indexes */
	  short         numVertices;    /* number of vertices */
	  FLpt2[]        vertices;       /* vertices of character glyph */
	  FT_GlyphSlot  glyph;          /* the glyph slot */
}
