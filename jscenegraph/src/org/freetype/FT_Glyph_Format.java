/**
 * 
 */
package org.freetype;

/**
 * @author Yves Boyadjian
 *
 */
public enum FT_Glyph_Format {

     FT_GLYPH_FORMAT_NONE( FT_IMAGE_TAG(0, 0, 0, 0 )),
    FT_GLYPH_FORMAT_COMPOSITE( FT_IMAGE_TAG( 'c', 'o', 'm', 'p' )),
    FT_GLYPH_FORMAT_BITMAP( FT_IMAGE_TAG(    'b', 'i', 't', 's' )),
    FT_GLYPH_FORMAT_OUTLINE( FT_IMAGE_TAG(   'o', 'u', 't', 'l' )),
    FT_GLYPH_FORMAT_PLOTTER( FT_IMAGE_TAG(   'p', 'l', 'o', 't' ));
	
	FT_Glyph_Format(int value) {
		
	}

    /*************************************************************************/
    /*                                                                       */
    /* <Macro>                                                               */
    /*    FT_IMAGE_TAG                                                       */
    /*                                                                       */
    /* <Description>                                                         */
    /*    This macro converts four-letter tags to an unsigned long type.     */
    /*                                                                       */
    /* <Note>                                                                */
    /*    Since many 16-bit compilers don't like 32-bit enumerations, you    */
    /*    should redefine this macro in case of problems to something like   */
    /*    this:                                                              */
    /*                                                                       */
    /*    {                                                                  */
    /*      #define FT_IMAGE_TAG( value, _x1, _x2, _x3, _x4 )  value         */
    /*    }                                                                  */
    /*                                                                       */
    /*    to get a simple enumeration without assigning special numbers.     */
    /*                                                                       */
  private static int FT_IMAGE_TAG( int _x1, int _x2, int _x3, int _x4 )  {
            int value = ( ( _x1 << 24 ) | 
                      ( _x2 << 16 ) | 
                      ( _x3 << 8  ) | 
                        _x4         );
            return value;
    } /* FT_IMAGE_TAG */
}
