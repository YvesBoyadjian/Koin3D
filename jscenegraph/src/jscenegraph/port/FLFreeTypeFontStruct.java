/**
 * 
 */
package jscenegraph.port;

import org.freetype.FT_Face;

/**
 * @author Yves Boyadjian
 *
 */
public class FLFreeTypeFontStruct extends FLfontStruct {


	public String        name;           /* font name */
		  final FLpt2         bound = new FLpt2();          /* max char size */
		  FLFreeTypeOutline[] char8;    /* 8 bit char array */
		  FLFreeTypeOutline[] char16;   /* 16 bit char array */
		  char[]       index16;        /* code point index into char16 */
		  int           num16;          /* number of char16 */
		  FT_Face       face;           /* the font face */
		  boolean     hint;           /* is glyph hinting active? */
		  boolean     grayRender;     /* is anti-aliasing active? */
		  boolean     lowPrec;        /* force low precision */

}
