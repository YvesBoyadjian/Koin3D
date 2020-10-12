/**
 * 
 */
package jscenegraph.port;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import org.freetype.FT_Bitmap;
import org.freetype.FT_Driver;
import org.freetype.FT_Driver_Class;
import org.freetype.FT_Encoding;
import org.freetype.FT_Error;
import org.freetype.FT_Face;
import org.freetype.FT_Face_Internal;
import org.freetype.FT_GlyphSlot;
import org.freetype.FT_Glyph_Format;
import org.freetype.FT_Incremental_Interface;
import org.freetype.FT_Library;
import org.freetype.FT_ListNode;
import org.freetype.FT_Memory;
import org.freetype.FT_Open_Args;
import org.freetype.FT_Outline;
import org.freetype.FT_Outline_Funcs;
import org.freetype.FT_Parameter;
import org.freetype.FT_Pixel_Mode;
import org.freetype.FT_Raster;
import org.freetype.FT_Size;
import org.freetype.FT_Size_Request;
import org.freetype.FT_Size_Request_Type;
import org.freetype.FT_Vector;

import jscenegraph.database.inventor.libFL.FLcontext;
import jscenegraph.database.inventor.libFL.FLcontext.FLbitmap;

/**
 * @author Yves Boyadjian
 *
 */
public class fl {

	public static final int FT_PRECISION_BITS    =8;

	public static final int PRECISION_BITS    =FT_PRECISION_BITS;
	public static final int PRECISION         =(1 << PRECISION_BITS);
	public static final int PRECISION_MASK    =(-1 << PRECISION_BITS);
	public static final int PRECISION_HALF    =(PRECISION >> 1);
	public static final int PRECISION_JITTER  =(PRECISION >> 5);
	public static final int PRECISION_STEP    =PRECISION_HALF;

	/* The number fractional bits of *input* coordinates.  We always use the
	 * 26.6 format (i.e, 6 bits for the fractional part), but hackers are
	 * free to experiment with different values.
	 */
	public static final int INPUT_BITS              =6;

	
	public static final int ErrRaster_Invalid_Mode      =-2;
	public static final int ErrRaster_Invalid_Outline   =-1;
	public static final int ErrRaster_Invalid_Argument  =-3;
	public static final int ErrRaster_Memory_Overflow   =-4;

	public static final int BAD_FONT_NUMBER = 0;

	
	/* This file should be included in those OpenGL programs which need Font  */
	/* Library (FL) functions from the file /usr/lib/libFL.so.                */

	/* The fontNamePreference parameter in a call to flCreateContext can have  */
	/* one of the following values:                                            */
	public static final int FL_FONTNAME     =0;
	public static final int FL_FILENAME     =1;
	public static final int FL_XFONTNAME    =2;

/* The parameter hint in a call to flSetHint can have one of the following */
/* values:                                                                 */
public static final int FL_HINT_AABITMAPFONTS   =1;      /* bound to font                    */
public static final int FL_HINT_CHARSPACING     =2;      /* bound to font                    */
public static final int FL_HINT_FONTTYPE        =3;      /* bound to font                    */
public static final int FL_HINT_MAXAASIZE       =4;      /* bound to font                    */
public static final int FL_HINT_MINOUTLINESIZE  =5;      /* bound to font                    */
public static final int FL_HINT_ROUNDADVANCE    =6;      /* bound to font                    */
public static final int FL_HINT_SCALETHRESH     =7;      /* bound to font                    */
public static final int FL_HINT_TOLERANCE       =8;      /* bound to font                    */

public static final int FL_FONTTYPE_ALL         =0;      /* use all types of fonts (default) */
public static final int FL_FONTTYPE_BITMAP      =1;      /* use only bitmap fonts            */
public static final int FL_FONTTYPE_OUTLINE     =2;      /* use only outline fonts           */

/*************************************************************************/
/*                                                                       */
/* <Enum>                                                                */
/*    FT_OPEN_XXX                                                        */
/*                                                                       */
/* <Description>                                                         */
/*    A list of bit-field constants used within the `flags' field of the */
/*    @FT_Open_Args structure.                                           */
/*                                                                       */
/* <Values>                                                              */
/*    FT_OPEN_MEMORY   :: This is a memory-based stream.                 */
/*                                                                       */
/*    FT_OPEN_STREAM   :: Copy the stream from the `stream' field.       */
/*                                                                       */
/*    FT_OPEN_PATHNAME :: Create a new input stream from a C~path        */
/*                        name.                                          */
/*                                                                       */
/*    FT_OPEN_DRIVER   :: Use the `driver' field.                        */
/*                                                                       */
/*    FT_OPEN_PARAMS   :: Use the `num_params' and `params' fields.      */
/*                                                                       */
/* <Note>                                                                */
/*    The `FT_OPEN_MEMORY', `FT_OPEN_STREAM', and `FT_OPEN_PATHNAME'     */
/*    flags are mutually exclusive.                                      */
/*                                                                       */
public static final int FT_OPEN_MEMORY    =0x1;
public static final int FT_OPEN_STREAM    =0x2;
public static final int FT_OPEN_PATHNAME  =0x4;
public static final int FT_OPEN_DRIVER    =0x8;
public static final int FT_OPEN_PARAMS    =0x10;


  /*************************************************************************/
  /*                                                                       */
  /* <Enum>                                                                */
  /*    FT_FACE_FLAG_XXX                                                   */
  /*                                                                       */
  /* <Description>                                                         */
  /*    A list of bit flags used in the `face_flags' field of the          */
  /*    @FT_FaceRec structure.  They inform client applications of         */
  /*    properties of the corresponding face.                              */
  /*                                                                       */
  /* <Values>                                                              */
  /*    FT_FACE_FLAG_SCALABLE ::                                           */
  /*      Indicates that the face contains outline glyphs.  This doesn't   */
  /*      prevent bitmap strikes, i.e., a face can have both this and      */
  /*      and @FT_FACE_FLAG_FIXED_SIZES set.                               */
  /*                                                                       */
  /*    FT_FACE_FLAG_FIXED_SIZES ::                                        */
  /*      Indicates that the face contains bitmap strikes.  See also the   */
  /*      `num_fixed_sizes' and `available_sizes' fields of @FT_FaceRec.   */
  /*                                                                       */
  /*    FT_FACE_FLAG_FIXED_WIDTH ::                                        */
  /*      Indicates that the face contains fixed-width characters (like    */
  /*      Courier, Lucido, MonoType, etc.).                                */
  /*                                                                       */
  /*    FT_FACE_FLAG_SFNT ::                                               */
  /*      Indicates that the face uses the `sfnt' storage scheme.  For     */
  /*      now, this means TrueType and OpenType.                           */
  /*                                                                       */
  /*    FT_FACE_FLAG_HORIZONTAL ::                                         */
  /*      Indicates that the face contains horizontal glyph metrics.  This */
  /*      should be set for all common formats.                            */
  /*                                                                       */
  /*    FT_FACE_FLAG_VERTICAL ::                                           */
  /*      Indicates that the face contains vertical glyph metrics.  This   */
  /*      is only available in some formats, not all of them.              */
  /*                                                                       */
  /*    FT_FACE_FLAG_KERNING ::                                            */
  /*      Indicates that the face contains kerning information.  If set,   */
  /*      the kerning distance can be retrieved through the function       */
  /*      @FT_Get_Kerning.  Otherwise the function always return the       */
  /*      vector (0,0).  Note that FreeType doesn't handle kerning data    */
  /*      from the `GPOS' table (as present in some OpenType fonts).       */
  /*                                                                       */
  /*    FT_FACE_FLAG_FAST_GLYPHS ::                                        */
  /*      THIS FLAG IS DEPRECATED.  DO NOT USE OR TEST IT.                 */
  /*                                                                       */
  /*    FT_FACE_FLAG_MULTIPLE_MASTERS ::                                   */
  /*      Indicates that the font contains multiple masters and is capable */
  /*      of interpolating between them.  See the multiple-masters         */
  /*      specific API for details.                                        */
  /*                                                                       */
  /*    FT_FACE_FLAG_GLYPH_NAMES ::                                        */
  /*      Indicates that the font contains glyph names that can be         */
  /*      retrieved through @FT_Get_Glyph_Name.  Note that some TrueType   */
  /*      fonts contain broken glyph name tables.  Use the function        */
  /*      @FT_Has_PS_Glyph_Names when needed.                              */
  /*                                                                       */
  /*    FT_FACE_FLAG_EXTERNAL_STREAM ::                                    */
  /*      Used internally by FreeType to indicate that a face's stream was */
  /*      provided by the client application and should not be destroyed   */
  /*      when @FT_Done_Face is called.  Don't read or test this flag.     */
  /*                                                                       */
  /*    FT_FACE_FLAG_HINTER ::                                             */
  /*      Set if the font driver has a hinting machine of its own.  For    */
  /*      example, with TrueType fonts, it makes sense to use data from    */
  /*      the SFNT `gasp' table only if the native TrueType hinting engine */
  /*      (with the bytecode interpreter) is available and active.         */
  /*                                                                       */
  /*    FT_FACE_FLAG_CID_KEYED ::                                          */
  /*      Set if the font is CID-keyed.  In that case, the font is not     */
  /*      accessed by glyph indices but by CID values.  For subsetted      */
  /*      CID-keyed fonts this has the consequence that not all index      */
  /*      values are a valid argument to FT_Load_Glyph.  Only the CID      */
  /*      values for which corresponding glyphs in the subsetted font      */
  /*      exist make FT_Load_Glyph return successfully; in all other cases */
  /*      you get an `FT_Err_Invalid_Argument' error.                      */
  /*                                                                       */
  /*      Note that CID-keyed fonts that are in an SFNT wrapper don't      */
  /*      have this flag set since the glyphs are accessed in the normal   */
  /*      way (using contiguous indices); the `CID-ness' isn't visible to  */
  /*      the application.                                                 */
  /*                                                                       */
  /*    FT_FACE_FLAG_TRICKY ::                                             */
  /*      Set if the font is `tricky', this is, it always needs the        */
  /*      font format's native hinting engine to get a reasonable result.  */
  /*      A typical example is the Chinese font `mingli.ttf' that uses     */
  /*      TrueType bytecode instructions to move and scale all of its      */
  /*      subglyphs.                                                       */
  /*                                                                       */
  /*      It is not possible to autohint such fonts using                  */
  /*      @FT_LOAD_FORCE_AUTOHINT; it will also ignore                     */
  /*      @FT_LOAD_NO_HINTING.  You have to set both @FT_LOAD_NO_HINTING   */
  /*      and @FT_LOAD_NO_AUTOHINT to really disable hinting; however, you */
  /*      probably never want this except for demonstration purposes.      */
  /*                                                                       */
  /*      Currently, there are about a dozen TrueType fonts in the list of */
  /*      tricky fonts; they are hard-coded in file `ttobjs.c'.            */
  /*                                                                       */
  /*    FT_FACE_FLAG_COLOR ::                                              */
  /*      Set if the font has color glyph tables.  To access color glyphs  */
  /*      use @FT_LOAD_COLOR.                                              */
  /*                                                                       */
public static final int FT_FACE_FLAG_SCALABLE          =( 1 <<  0 );
public static final int FT_FACE_FLAG_FIXED_SIZES       =( 1 <<  1 );
public static final int FT_FACE_FLAG_FIXED_WIDTH       =( 1 <<  2 );
public static final int FT_FACE_FLAG_SFNT              =( 1 <<  3 );
public static final int FT_FACE_FLAG_HORIZONTAL        =( 1 <<  4 );
public static final int FT_FACE_FLAG_VERTICAL          =( 1 <<  5 );
public static final int FT_FACE_FLAG_KERNING           =( 1 <<  6 );
public static final int FT_FACE_FLAG_FAST_GLYPHS       =( 1 <<  7 );
public static final int FT_FACE_FLAG_MULTIPLE_MASTERS  =( 1 <<  8 );
public static final int FT_FACE_FLAG_GLYPH_NAMES       =( 1 <<  9 );
public static final int FT_FACE_FLAG_EXTERNAL_STREAM   =( 1 << 10 );
public static final int FT_FACE_FLAG_HINTER            =( 1 << 11 );
public static final int FT_FACE_FLAG_CID_KEYED         =( 1 << 12 );
public static final int FT_FACE_FLAG_TRICKY            =( 1 << 13 );
public static final int FT_FACE_FLAG_COLOR             =( 1 << 14 );


    /* Data type */
	static abstract class FLfontImpl {
  String             name;                /* vendor name */
  String             version;             /* version string */

  Supplier<Boolean>       initialize = () ->{ return _flFTInitialize();};          /* initialize handler */
  //FLShutdownFunc *         shutdown;            /* shutdown handler */
  FLfontStruct       createFont(String fontName, float[][]mat,int charNameCount, String[]charNameVector)
  {
	  return _flFTCreateFont(fontName,mat,charNameCount,charNameVector);
  };          /* font creation handler */
  //FLDestroyFontFunc *      destroyFont;         /* font destruction handler */
  //FLGetBitmapFunc *        getBitmap;           /* get bitmap handler */
  //FLGetScalableBitmapFunc *getScalableBitmap;   /* get scalable bitmap handler */
  //FLGetOutlineFunc *       getOutline;          /* get outline handler */
  //FLUniGetBitmapFunc *     getUniBitmap;        /* get Unicode bitmap handler */
  //FLUniGetOutlineFunc *    getUniOutline;       /* get Unicode outline handler */

  boolean                init;                /* initialized flag */
  boolean                available;           /* availability flag */
  
  public FLfontImpl(String name, String version, boolean init, boolean available) {
	  this.name = name;
	  this.version = version;
	  this.init = init;
	  this.available = available;
  }

public abstract FLoutline getUniOutline(FLfontStruct[] fsList, char uCS2);

public FLbitmap getUniBitmap(FLfontStruct[] fsList, char uCS2) {
	return _flFTUniGetBitmap( fsList, uCS2);
}
};


  /*************************************************************************
   *
   * @enum:
   *   FT_LOAD_XXX
   *
   * @description:
   *   A list of bit-field constants used with @FT_Load_Glyph to indicate
   *   what kind of operations to perform during glyph loading.
   *
   * @values:
   *   FT_LOAD_DEFAULT ::
   *     Corresponding to~0, this value is used as the default glyph load
   *     operation.  In this case, the following happens:
   *
   *     1. FreeType looks for a bitmap for the glyph corresponding to the
   *        face's current size.  If one is found, the function returns.
   *        The bitmap data can be accessed from the glyph slot (see note
   *        below).
   *
   *     2. If no embedded bitmap is searched or found, FreeType looks for a
   *        scalable outline.  If one is found, it is loaded from the font
   *        file, scaled to device pixels, then `hinted' to the pixel grid
   *        in order to optimize it.  The outline data can be accessed from
   *        the glyph slot (see note below).
   *
   *     Note that by default, the glyph loader doesn't render outlines into
   *     bitmaps.  The following flags are used to modify this default
   *     behaviour to more specific and useful cases.
   *
   *   FT_LOAD_NO_SCALE ::
   *     Don't scale the outline glyph loaded, but keep it in font units.
   *
   *     This flag implies @FT_LOAD_NO_HINTING and @FT_LOAD_NO_BITMAP, and
   *     unsets @FT_LOAD_RENDER.
   *
   *   FT_LOAD_NO_HINTING ::
   *     Disable hinting.  This generally generates `blurrier' bitmap glyph
   *     when the glyph is rendered in any of the anti-aliased modes.  See
   *     also the note below.
   *
   *     This flag is implied by @FT_LOAD_NO_SCALE.
   *
   *   FT_LOAD_RENDER ::
   *     Call @FT_Render_Glyph after the glyph is loaded.  By default, the
   *     glyph is rendered in @FT_RENDER_MODE_NORMAL mode.  This can be
   *     overridden by @FT_LOAD_TARGET_XXX or @FT_LOAD_MONOCHROME.
   *
   *     This flag is unset by @FT_LOAD_NO_SCALE.
   *
   *   FT_LOAD_NO_BITMAP ::
   *     Ignore bitmap strikes when loading.  Bitmap-only fonts ignore this
   *     flag.
   *
   *     @FT_LOAD_NO_SCALE always sets this flag.
   *
   *   FT_LOAD_VERTICAL_LAYOUT ::
   *     Load the glyph for vertical text layout.  _Don't_ use it as it is
   *     problematic currently.
   *
   *   FT_LOAD_FORCE_AUTOHINT ::
   *     Indicates that the auto-hinter is preferred over the font's native
   *     hinter.  See also the note below.
   *
   *   FT_LOAD_CROP_BITMAP ::
   *     Indicates that the font driver should crop the loaded bitmap glyph
   *     (i.e., remove all space around its black bits).  Not all drivers
   *     implement this.
   *
   *   FT_LOAD_PEDANTIC ::
   *     Indicates that the font driver should perform pedantic verifications
   *     during glyph loading.  This is mostly used to detect broken glyphs
   *     in fonts.  By default, FreeType tries to handle broken fonts also.
   *
   *   FT_LOAD_IGNORE_GLOBAL_ADVANCE_WIDTH ::
   *     Ignored.  Deprecated.
   *
   *   FT_LOAD_NO_RECURSE ::
   *     This flag is only used internally.  It merely indicates that the
   *     font driver should not load composite glyphs recursively.  Instead,
   *     it should set the `num_subglyph' and `subglyphs' values of the
   *     glyph slot accordingly, and set `glyph.format' to
   *     @FT_GLYPH_FORMAT_COMPOSITE.
   *
   *     The description of sub-glyphs is not available to client
   *     applications for now.
   *
   *     This flag implies @FT_LOAD_NO_SCALE and @FT_LOAD_IGNORE_TRANSFORM.
   *
   *   FT_LOAD_IGNORE_TRANSFORM ::
   *     Indicates that the transform matrix set by @FT_Set_Transform should
   *     be ignored.
   *
   *   FT_LOAD_MONOCHROME ::
   *     This flag is used with @FT_LOAD_RENDER to indicate that you want to
   *     render an outline glyph to a 1-bit monochrome bitmap glyph, with
   *     8~pixels packed into each byte of the bitmap data.
   *
   *     Note that this has no effect on the hinting algorithm used.  You
   *     should rather use @FT_LOAD_TARGET_MONO so that the
   *     monochrome-optimized hinting algorithm is used.
   *
   *   FT_LOAD_LINEAR_DESIGN ::
   *     Indicates that the `linearHoriAdvance' and `linearVertAdvance'
   *     fields of @FT_GlyphSlotRec should be kept in font units.  See
   *     @FT_GlyphSlotRec for details.
   *
   *   FT_LOAD_NO_AUTOHINT ::
   *     Disable auto-hinter.  See also the note below.
   *
   * @note:
   *   By default, hinting is enabled and the font's native hinter (see
   *   @FT_FACE_FLAG_HINTER) is preferred over the auto-hinter.  You can
   *   disable hinting by setting @FT_LOAD_NO_HINTING or change the
   *   precedence by setting @FT_LOAD_FORCE_AUTOHINT.  You can also set
   *   @FT_LOAD_NO_AUTOHINT in case you don't want the auto-hinter to be
   *   used at all.
   *
   *   See the description of @FT_FACE_FLAG_TRICKY for a special exception
   *   (affecting only a handful of Asian fonts).
   *
   *   Besides deciding which hinter to use, you can also decide which
   *   hinting algorithm to use.  See @FT_LOAD_TARGET_XXX for details.
   *
   */
public static final int FT_LOAD_DEFAULT                      =0x0;
public static final int FT_LOAD_NO_SCALE                     =0x1;
public static final int FT_LOAD_NO_HINTING                   =0x2;
public static final int FT_LOAD_RENDER                       =0x4;
public static final int FT_LOAD_NO_BITMAP                    =0x8;
public static final int FT_LOAD_VERTICAL_LAYOUT              =0x10;
public static final int FT_LOAD_FORCE_AUTOHINT               =0x20;
public static final int FT_LOAD_CROP_BITMAP                  =0x40;
public static final int FT_LOAD_PEDANTIC                     =0x80;
public static final int FT_LOAD_IGNORE_GLOBAL_ADVANCE_WIDTH  =0x200;
public static final int FT_LOAD_NO_RECURSE                   =0x400;
public static final int FT_LOAD_IGNORE_TRANSFORM             =0x800;
public static final int FT_LOAD_MONOCHROME                   =0x1000;
public static final int FT_LOAD_LINEAR_DESIGN                =0x2000;
public static final int FT_LOAD_NO_AUTOHINT                  =0x8000;

  /* */

  /* used internally only by certain font drivers! */
public static final int FT_LOAD_ADVANCE_ONLY                 =0x100;
public static final int FT_LOAD_SBITS_ONLY                   =0x4000;

public static final float KLUDGE_FACTOR           =80.0f;
public static final int PIXEL_ROW_ALIGNMENT     =4;


/***************************************************************************
 *
 * @constant:
 *   FT_PARAM_TAG_INCREMENTAL
 *
 * @description:
 *   A constant used as the tag of @FT_Parameter structures to indicate
 *   an incremental loading object to be used by FreeType.
 *
 */
public static final int FT_PARAM_TAG_INCREMENTAL = FT_MAKE_TAG( 'i', 'n', 'c', 'r' );

public static int FT_MAKE_TAG( char _x1, char _x2, char _x3, char _x4 ) {
	return (int)( ( (long)_x1 << 24 ) |     
  ( (long)_x2 << 16 ) |     
  ( (long)_x3 <<  8 ) |     
    (long)_x4         );
}



  /*************************************************************************/
  /*                                                                       */
  /* <Section>                                                             */
  /*    truetype_tables                                                    */
  /*                                                                       */


  /*************************************************************************/
  /*                                                                       */
  /* Possible values for the `platform' identifier code in the name        */
  /* records of the TTF `name' table.                                      */
  /*                                                                       */
  /*************************************************************************/


  /***********************************************************************
   *
   * @enum:
   *   TT_PLATFORM_XXX
   *
   * @description:
   *   A list of valid values for the `platform_id' identifier code in
   *   @FT_CharMapRec and @FT_SfntName structures.
   *
   * @values:
   *   TT_PLATFORM_APPLE_UNICODE ::
   *     Used by Apple to indicate a Unicode character map and/or name entry.
   *     See @TT_APPLE_ID_XXX for corresponding `encoding_id' values.  Note
   *     that name entries in this format are coded as big-endian UCS-2
   *     character codes _only_.
   *
   *   TT_PLATFORM_MACINTOSH ::
   *     Used by Apple to indicate a MacOS-specific charmap and/or name entry.
   *     See @TT_MAC_ID_XXX for corresponding `encoding_id' values.  Note that
   *     most TrueType fonts contain an Apple roman charmap to be usable on
   *     MacOS systems (even if they contain a Microsoft charmap as well).
   *
   *   TT_PLATFORM_ISO ::
   *     This value was used to specify ISO/IEC 10646 charmaps.  It is however
   *     now deprecated.  See @TT_ISO_ID_XXX for a list of corresponding
   *     `encoding_id' values.
   *
   *   TT_PLATFORM_MICROSOFT ::
   *     Used by Microsoft to indicate Windows-specific charmaps.  See
   *     @TT_MS_ID_XXX for a list of corresponding `encoding_id' values.
   *     Note that most fonts contain a Unicode charmap using
   *     (TT_PLATFORM_MICROSOFT, @TT_MS_ID_UNICODE_CS).
   *
   *   TT_PLATFORM_CUSTOM ::
   *     Used to indicate application-specific charmaps.
   *
   *   TT_PLATFORM_ADOBE ::
   *     This value isn't part of any font format specification, but is used
   *     by FreeType to report Adobe-specific charmaps in an @FT_CharMapRec
   *     structure.  See @TT_ADOBE_ID_XXX.
   */

public static final int TT_PLATFORM_APPLE_UNICODE  =0;
public static final int TT_PLATFORM_MACINTOSH      =1;
public static final int TT_PLATFORM_ISO            =2; /* deprecated */
public static final int TT_PLATFORM_MICROSOFT      =3;
public static final int TT_PLATFORM_CUSTOM         =4;
public static final int TT_PLATFORM_ADOBE          =7; /* artificial */


  /***********************************************************************
   *
   * @enum:
   *   TT_APPLE_ID_XXX
   *
   * @description:
   *   A list of valid values for the `encoding_id' for
   *   @TT_PLATFORM_APPLE_UNICODE charmaps and name entries.
   *
   * @values:
   *   TT_APPLE_ID_DEFAULT ::
   *     Unicode version 1.0.
   *
   *   TT_APPLE_ID_UNICODE_1_1 ::
   *     Unicode 1.1; specifies Hangul characters starting at U+34xx.
   *
   *   TT_APPLE_ID_ISO_10646 ::
   *     Deprecated (identical to preceding).
   *
   *   TT_APPLE_ID_UNICODE_2_0 ::
   *     Unicode 2.0 and beyond (UTF-16 BMP only).
   *
   *   TT_APPLE_ID_UNICODE_32 ::
   *     Unicode 3.1 and beyond, using UTF-32.
   *
   *   TT_APPLE_ID_VARIANT_SELECTOR ::
   *     From Adobe, not Apple.  Not a normal cmap.  Specifies variations
   *     on a real cmap.
   */

public static final int TT_APPLE_ID_DEFAULT           =0; /* Unicode 1.0 */
public static final int TT_APPLE_ID_UNICODE_1_1       =1; /* specify Hangul at U+34xx */
public static final int TT_APPLE_ID_ISO_10646         =2; /* deprecated */
public static final int TT_APPLE_ID_UNICODE_2_0       =3; /* or later */
public static final int TT_APPLE_ID_UNICODE_32        =4; /* 2.0 or later, full repertoire */
public static final int TT_APPLE_ID_VARIANT_SELECTOR  =5; /* variation selector data */


  /***********************************************************************
   *
   * @enum:
   *   TT_MS_ID_XXX
   *
   * @description:
   *   A list of valid values for the `encoding_id' for
   *   @TT_PLATFORM_MICROSOFT charmaps and name entries.
   *
   * @values:
   *   TT_MS_ID_SYMBOL_CS ::
   *     Corresponds to Microsoft symbol encoding. See
   *     @FT_ENCODING_MS_SYMBOL.
   *
   *   TT_MS_ID_UNICODE_CS ::
   *     Corresponds to a Microsoft WGL4 charmap, matching Unicode.  See
   *     @FT_ENCODING_UNICODE.
   *
   *   TT_MS_ID_SJIS ::
   *     Corresponds to SJIS Japanese encoding.  See @FT_ENCODING_SJIS.
   *
   *   TT_MS_ID_GB2312 ::
   *     Corresponds to Simplified Chinese as used in Mainland China.  See
   *     @FT_ENCODING_GB2312.
   *
   *   TT_MS_ID_BIG_5 ::
   *     Corresponds to Traditional Chinese as used in Taiwan and Hong Kong.
   *     See @FT_ENCODING_BIG5.
   *
   *   TT_MS_ID_WANSUNG ::
   *     Corresponds to Korean Wansung encoding.  See @FT_ENCODING_WANSUNG.
   *
   *   TT_MS_ID_JOHAB ::
   *     Corresponds to Johab encoding.  See @FT_ENCODING_JOHAB.
   *
   *   TT_MS_ID_UCS_4 ::
   *     Corresponds to UCS-4 or UTF-32 charmaps.  This has been added to
   *     the OpenType specification version 1.4 (mid-2001.)
   */

public static final int TT_MS_ID_SYMBOL_CS    =0;
public static final int TT_MS_ID_UNICODE_CS   =1;
public static final int TT_MS_ID_SJIS         =2;
public static final int TT_MS_ID_GB2312       =3;
public static final int TT_MS_ID_BIG_5        =4;
public static final int TT_MS_ID_WANSUNG      =5;
public static final int TT_MS_ID_JOHAB        =6;
public static final int TT_MS_ID_UCS_4       =10;


/*************************************************************************/
/*                                                                       */
/* <Enum>                                                                */
/*    FT_OUTLINE_XXX                                                     */
/*                                                                       */
/* <Description>                                                         */
/*    A list of bit-field constants use for the flags in an outline's    */
/*    `flags' field.                                                     */
/*                                                                       */
/* <Values>                                                              */
/*    FT_OUTLINE_NONE ::                                                 */
/*      Value~0 is reserved.                                             */
/*                                                                       */
/*    FT_OUTLINE_OWNER ::                                                */
/*      If set, this flag indicates that the outline's field arrays      */
/*      (i.e., `points', `flags', and `contours') are `owned' by the     */
/*      outline object, and should thus be freed when it is destroyed.   */
/*                                                                       */
/*    FT_OUTLINE_EVEN_ODD_FILL ::                                        */
/*      By default, outlines are filled using the non-zero winding rule. */
/*      If set to 1, the outline will be filled using the even-odd fill  */
/*      rule (only works with the smooth rasterizer).                    */
/*                                                                       */
/*    FT_OUTLINE_REVERSE_FILL ::                                         */
/*      By default, outside contours of an outline are oriented in       */
/*      clock-wise direction, as defined in the TrueType specification.  */
/*      This flag is set if the outline uses the opposite direction      */
/*      (typically for Type~1 fonts).  This flag is ignored by the scan  */
/*      converter.                                                       */
/*                                                                       */
/*    FT_OUTLINE_IGNORE_DROPOUTS ::                                      */
/*      By default, the scan converter will try to detect drop-outs in   */
/*      an outline and correct the glyph bitmap to ensure consistent     */
/*      shape continuity.  If set, this flag hints the scan-line         */
/*      converter to ignore such cases.  See below for more information. */
/*                                                                       */
/*    FT_OUTLINE_SMART_DROPOUTS ::                                       */
/*      Select smart dropout control.  If unset, use simple dropout      */
/*      control.  Ignored if @FT_OUTLINE_IGNORE_DROPOUTS is set.  See    */
/*      below for more information.                                      */
/*                                                                       */
/*    FT_OUTLINE_INCLUDE_STUBS ::                                        */
/*      If set, turn pixels on for `stubs', otherwise exclude them.      */
/*      Ignored if @FT_OUTLINE_IGNORE_DROPOUTS is set.  See below for    */
/*      more information.                                                */
/*                                                                       */
/*    FT_OUTLINE_HIGH_PRECISION ::                                       */
/*      This flag indicates that the scan-line converter should try to   */
/*      convert this outline to bitmaps with the highest possible        */
/*      quality.  It is typically set for small character sizes.  Note   */
/*      that this is only a hint that might be completely ignored by a   */
/*      given scan-converter.                                            */
/*                                                                       */
/*    FT_OUTLINE_SINGLE_PASS ::                                          */
/*      This flag is set to force a given scan-converter to only use a   */
/*      single pass over the outline to render a bitmap glyph image.     */
/*      Normally, it is set for very large character sizes.  It is only  */
/*      a hint that might be completely ignored by a given               */
/*      scan-converter.                                                  */
/*                                                                       */
/* <Note>                                                                */
/*    The flags @FT_OUTLINE_IGNORE_DROPOUTS, @FT_OUTLINE_SMART_DROPOUTS, */
/*    and @FT_OUTLINE_INCLUDE_STUBS are ignored by the smooth            */
/*    rasterizer.                                                        */
/*                                                                       */
/*    There exists a second mechanism to pass the drop-out mode to the   */
/*    B/W rasterizer; see the `tags' field in @FT_Outline.               */
/*                                                                       */
/*    Please refer to the description of the `SCANTYPE' instruction in   */
/*    the OpenType specification (in file `ttinst1.doc') how simple      */
/*    drop-outs, smart drop-outs, and stubs are defined.                 */
/*                                                                       */
public static final int FT_OUTLINE_NONE             =0x0;
public static final int FT_OUTLINE_OWNER            =0x1;
public static final int FT_OUTLINE_EVEN_ODD_FILL    =0x2;
public static final int FT_OUTLINE_REVERSE_FILL     =0x4;
public static final int FT_OUTLINE_IGNORE_DROPOUTS  =0x8;
public static final int FT_OUTLINE_SMART_DROPOUTS   =0x10;
public static final int FT_OUTLINE_INCLUDE_STUBS    =0x20;

public static final int FT_OUTLINE_HIGH_PRECISION   =0x100;
public static final int FT_OUTLINE_SINGLE_PASS      =0x200;



/*************************************************************************/
public static /*                                                                       */
/* <Macro>                                                               */
/*    FT_ENC_TAG                                                         */
/*                                                                       */
/* <Description>                                                         */
/*    This macro converts four-letter tags into an unsigned long.  It is */
/*    used to define `encoding' identifiers (see @FT_Encoding).          */
/*                                                                       */
/* <Note>                                                                */
/*    Since many 16-bit compilers don't like 32-bit enumerations, you    */
/*    should redefine this macro in case of problems to something like   */
/*    this:                                                              */
/*                                                                       */
/*    {                                                                  */
/*      #define FT_ENC_TAG( value, a, b, c, d )  value                   */
/*    }                                                                  */
/*                                                                       */
/*    to get a simple enumeration without assigning special numbers.     */
/*                                                                       */

int FT_ENC_TAG( char a, char b, char c, char d ) {         
        int value = ( ( (int)(a) << 24 ) |  
                  ( (int)(b) << 16 ) |  
                  ( (int)(c) <<  8 ) |  
                    (int)(d)         );
        return value;
}

/* Font implementation table */
static FLfontImpl freetypeImpl = new FLfontImpl(
  "FreeType", "2.0.1",
//  _flFTInitialize,
//  _flFTShutdown,
//  _flFTCreateFont,
//  _flFTDestroyFont,
//  _flFTGetBitmap,
//  _flFTGetScalableBitmap,
//  _flFTGetOutline,
//  _flFTUniGetBitmap,
//  _flFTUniGetOutline,
  false, true
) {

	@Override
	public FLoutline getUniOutline(FLfontStruct[] fsList, char uCS2) {
		  FLfontStruct fs;
		  FLoutline outline;
		//#ifdef FL_LITTLE_ENDIAN
		  int c = uCS2;//(UCS2[1] << 8) | UCS2[0];
		//#else
		  //GLuint c = (UCS2[0] << 8) | UCS2[1];
		//#endif

		  //TRACE(("_flFTUniGetOutline: 0x%04x\n", c));
		  int fsListIndex = 0;
		  while ((fs = fsList[fsListIndex++])!= null)
		    if ((outline = _flFTGetOutline(fs, c))!= null)
		      return outline;

		  return null;
	}
	
};

static FLfontImpl[] fontImplTable = new FLfontImpl[2];

	static {
		fontImplTable[0] = freetypeImpl;
		};



static FLcontext current_context = null;



public static void flSetHint(
	    int                          hint , 
	    float                        hintValue 
	) {
	  FLcontext ctx = current_context;
	  if (hint > 0 && hint <= 8)
	    ctx.hintValue[hint] = hintValue;
	}

	public static FLcontext flGetCurrentContext() {
		return current_context;
	}

	public static boolean flMakeCurrentContext(FLcontext ctx) {
		  if (ctx == null)
			    return false;

			  current_context = ctx;
			  return true;
	}

	public static void flDestroyFont(int operator_square_bracket) {
		// TODO Auto-generated method stub
		
	}

	public static FLcontext flCreateContext(String fontPath, int fontNamePreference, String fontNameRestriction, float pointsPerUMx, float pointsPerUMy) {
		FLcontext ctx = new FLcontext();
		ctx.fontPath = fontPath;
		ctx.fontNamePreference = fontNamePreference;
		ctx.fontNameRestriction = fontNameRestriction;
		ctx.pointsPerUMx = pointsPerUMx;
		ctx.pointsPerUMy = pointsPerUMy;
		ctx.current_font = BAD_FONT_NUMBER;
		ctx.numFont = 0;
		ctx.fontTable = null;

	    /* FIXME: The hint values are mostly guess work! */
	    ctx.hintValue[FL_HINT_AABITMAPFONTS]  = 0.0f;
	    ctx.hintValue[FL_HINT_CHARSPACING]    = 1.0f;
	    ctx.hintValue[FL_HINT_FONTTYPE]       = FL_FONTTYPE_OUTLINE;
	    ctx.hintValue[FL_HINT_MAXAASIZE]      = 2.0f;
	    ctx.hintValue[FL_HINT_MINOUTLINESIZE] = 12.0f;
	    ctx.hintValue[FL_HINT_ROUNDADVANCE]   = 1.0f;
	    ctx.hintValue[FL_HINT_SCALETHRESH]    = 1.0f;
	    ctx.hintValue[FL_HINT_TOLERANCE]      = 0.1f;
		return ctx;
	}

	public static int flCreateFont(String fontName, float[][] mat, int charNameCount, String[] charNameVector) {
  FLcontext ctx = current_context;
  FLfontStruct fs;
  int fn;


  if ( ctx == null)
    return BAD_FONT_NUMBER;

  if ( (fs = _flCreateFont(fontName, mat, charNameCount, charNameVector))==null)
    return BAD_FONT_NUMBER;

  /* Search for an not used font handle */
  fn = 1;
  if (ctx.fontTable != null) {
    while (fn <= ctx.numFont)
      if (ctx.fontTable[fn] != null)
        fn++;
      else
        break;
  }

  /* See if need to expand the font handle table */
  if (fn > ctx.numFont) {
    FLfontStruct[] table;

    ctx.numFont++;

    table = ctx.fontTable == null ? new FLfontStruct[ctx.numFont + 1] : Arrays.copyOf(ctx.fontTable, ctx.numFont + 1);
//    if (! (table = (FLfontStruct **) realloc(ctx.fontTable, (ctx.numFont + 1) * sizeof(FLfontStruct *)))) {
//      ctx.numFont--;
//      _flDestroyFont(fs);
//      return BAD_FONT_NUMBER;
//    }

    ctx.fontTable = table;
  }

  fs.fn = fn;
  ctx.fontTable[fn] = fs;

  return fn;
	}

	private static FLfontStruct _flCreateFont(String fontName, float[][] mat, int charNameCount,
			String[] charNameVector) {
		  FLfontImpl impl = fontImplTable[0];
		  FLfontStruct fs;

		  if (! impl.init && ! _flInitialize(impl))                            
		    return null;
		  
		  fs = impl.createFont(fontName, mat, charNameCount, charNameVector);
		  if ( fs == null)
		    return null;

		  /* Initialize font structure */
		  fs.direction         = FLcontext.FL_FONT_LEFTTORIGHT; /* hint about direction the font is painted */
		  fs.min_char_or_byte2 = 32;   /* first character */
		  fs.max_char_or_byte2 = 127;  /* last character */
		  fs.min_byte1         = 0;    /* first row that exists */
		  fs.max_byte1         = 0;    /* last row that exists */
		  fs.all_chars_exist   = false;/* flag if all characters have non-zero size */
		  fs.default_char      = 32;   /* char to print for undefined character */
		  fs.n_properties      = 0;    /* how many properties there are */
		  fs.properties        = null; /* pointer to array of additional properties */
//		#if 0
//		  fs.min_bounds;               /* minimum bounds over all existing char */
//		  fs.max_bounds;               /* maximum bounds over all existing char */
//		#endif
		  fs.per_char          = null; /* first_char to last_char information */
		  fs.ascent            = 8;    /* log. extent above baseline for spacing */
		  fs.descent           = 4;    /* log. descent below baseline for spacing */

		  return fs;
	}
	
	static String fontPath;
	static String fontDefault;
	static boolean fl_debug;

	private static boolean _flInitialize(FLfontImpl impl) {
		  boolean ret = false;
		  String ev;

		  /* obtain Windows font path */
		  String szPath = "";
		  szPath = System.getenv().get("SystemRoot")+"/fonts";//SHGetSpecialFolderPathA(NULL, szPath, CSIDL_FONTS, 0); //TODO
		  fontPath = szPath;

		  /* setup fallback font */
		  fontDefault = fontPath + "/times.ttf";

		  ev = System.getenv("FL_DEBUG");
		  fl_debug = (ev!=null && !ev.isEmpty());
		  
		  ev = System.getenv("FL_FONT_PATH");
		  if (ev!=null && !ev.isEmpty()) fontPath = ev;

		  if (impl.initialize != null)
		    ret = impl.initialize.get();

		  impl.init = true;

		  return ret;
	}
	
	static FT_Library               library = new FT_Library();


static boolean
_flFTInitialize()
{
  int error;

  error = FT_Init_FreeType(library);
  if (error!=0) {
    System.err.print("_flFTInitialize: initialise FreeType failed\n");
    return false;
  }

  return true;  /* OK */
}



	private static int FT_Init_FreeType(FT_Library alibrary) {
	// TODO Auto-generated method stub
	return 0;
}

	public static FLoutline flUniGetOutline(String fontList, char UCS2) {

		  FLfontStruct[] fsList;
		  FLoutline outline;

		  //TRACE(("flUniGetOutline\n"));

		  fsList  = _flGetFontInfoList(fontList);
		  outline = _flUniGetOutline(fsList, UCS2);
		  //free(fsList);
		  
		  
//		FLoutline outline = new FLoutline();
//		//TODO : dummy values
//		outline.outlinecount = 2;
//		outline.xadvance = 1.0f;
//		outline.yadvance = 0.0f;
//		outline.vertexcount = new short[2];
//		outline.vertexcount[0] = 5;
//		outline.vertexcount[1] = 4;
//		outline.vertex = new FLpt2[2][];
//		outline.vertex[0] = new FLpt2[5];
//		for(int i=0;i<5; i++) {
//			outline.vertex[0][i]= new FLpt2(
//				(float)Math.sin((float)i/4*(Math.PI*2)),
//						(float)Math.cos((float)i/4*(Math.PI*2))
//						);
//		}
//		outline.vertex[1] = new FLpt2[4];
//		outline.vertex[1][0]= new FLpt2(0, 0);
//		outline.vertex[1][1]= new FLpt2(0, 1);
//		outline.vertex[1][2]= new FLpt2(1, 0);
//		outline.vertex[1][3]= new FLpt2(0, 0);
		return outline;
	}

	public static void flFreeOutline(FLoutline flo) {
		// nothing to do 
	}

	
	public static FLbitmap flUniGetBitmap(
    String                        fontList , /* "fn1,fn2,fn3, ..." */
    char                      UCS2) {
  FLfontStruct[] fsList;
  FLbitmap bitmap;

  //TRACE(("flUniGetBitmap\n"));

  fsList = fl._flGetFontInfoList(fontList);
  bitmap = fl._flUniGetBitmap(fsList, UCS2);
  //free(fsList);
  return bitmap;
	}
	
	private static FLbitmap _flUniGetBitmap(FLfontStruct[] fsList, char uCS2) {
		FLfontImpl impl = _flGetFontImpl();

		//CHECK(fsList, impl, getUniBitmap, NULL);

		return impl.getUniBitmap(fsList, uCS2);
	}

	private static FLfontStruct[] 
	_flGetFontInfoList(String fontList /* "fn1,fn2,fn3, ..." */)
	{
	  FLfontStruct[] list;
	  int p, p2;
	  int numFonts, i; char c;

	  /* Count the number of fonts specified */
	  numFonts = 0;
	  for (p2 = p = /*fontList*/0; (p < fontList.length() ); p++) {
		  c = fontList.charAt(p);
	    if (c == ',') {
	      p2 = p;
	      numFonts++;
	    }
	  }
	  
	  if (p != p2)
	    numFonts++;

	  /* Create a list of pointers to font info struct */
	  list = new FLfontStruct[(numFonts + 1)];
	  if (list != null) {
	    for (i = 0, p = /*fontList*/0; p < fontList.length(); p++, i++) {
	      for (p2 = p++; (p<fontList.length()) && (c = fontList.charAt(p)) != ','; p++)
	        ;
	      //*p = '\0';
	      list[i] = flGetFontInfo(Integer.parseInt(fontList.substring(p2,p)));
	      //*p = c;
	    }
	    list[i] = null;
	  }

	  return list;
	}


static FLfontStruct 
flGetFontInfo(int fn)
{
  FLcontext ctx = current_context;
  return (ctx != null && fn > 0 && fn <= ctx.numFont) ? ctx.fontTable[fn] : null;
}


static FLoutline 
_flUniGetOutline(FLfontStruct[] fsList, char UCS2)
{
  FLfontImpl impl = _flGetFontImpl();

  //CHECK(fsList, impl, getUniOutline, null);

  return impl.getUniOutline(fsList, UCS2);
}


/*
 * Search for a suitable font implementation. For now, simply returns 
 * FreeType's implementation.
 */
static FLfontImpl 
_flGetFontImpl()
{
  return fontImplTable[0];
}


static FLoutline 
_flFTGetOutline(FLfontStruct _fs, int c)
{
  FLFreeTypeFontStruct fs = (FLFreeTypeFontStruct) _fs;
  FLFreeTypeOutline outline;
  int start, end, num, code, i;

  //TRACE(("_flFTGetOutline: '%c'(0x%04x)\n", c, c));

  // Search for the code point c in the 8 bit range
  if (c < 256) {
    outline = fs.char8[c];
    if ( outline == null)
      fs.char8[c] = outline = _flFTLoadChar(fs, (char)c);
    return (FLoutline ) outline;
  }

  // Search for the code point c in the 16 bit range
  start = 0;
  end   = fs.num16 - 1;

  for (i = end >> 1; start < end; i = (start + end) >> 1) {
    code = fs.index16[i];
    if (c < code)
      end = i - 1;
    else if (c > code)
      start = i + 1;
    else
      return (FLoutline ) fs.char16[i];
  }

  // Cannot find code point c, load the glyph char
  outline = _flFTLoadChar(fs, (char)c);
  if (outline != null) {
    char[] newIndex16;
    FLFreeTypeOutline[] newChar16;

    fs.num16++;
    newIndex16 = new char[fs.num16];//(FLchar *) malloc(fs.num16 * sizeof(FLchar));
    newChar16  = new FLFreeTypeOutline[fs.num16]; // malloc(fs.num16 * sizeof(FLFreeTypeOutline *));

    if (fs.index16 != null) {
      if (start > 0) {    	  
        //memcpy(newIndex16, fs.index16, sizeof(FLchar) * start);
    	  for(int ii=0; ii<start;ii++) {
    		  newIndex16[ii] = fs.index16[ii];
    	  }
        //memcpy(newChar16, fs.char16, sizeof(FLFreeTypeOutline *) * start);
    	  for(int ii=0; ii< start;ii++) {
    		  newChar16[ii] = fs.char16[ii];
    	  }
      }

      if ((num = fs.num16 - start - 1) > 0) {
        //memcpy(newIndex16 + start + 1, fs.index16 + start, sizeof(FLchar) * num);
    	  for(int ii=0; ii<num;ii++) {
    		  newIndex16[start+1+ii] = fs.index16[start+ii];
    	  }
        //memcpy(newChar16 + start  + 1, fs.char16 + start, sizeof(FLFreeTypeOutline *) * num);
        for(int ii=0;ii<num;ii++) {
        	newChar16[start+1+ii] = fs.char16[start+ii];
        }
      }

      //free(fs.index16); java port
      //free(fs.char16);
    }

    fs.index16 = newIndex16;
    fs.char16  = newChar16;

    fs.index16[start] = (char)c;
    fs.char16[start]  = outline;
  }
    
  return (FLoutline ) outline;
}


static FLFreeTypeOutline _flFTLoadChar(FLFreeTypeFontStruct fs, char c)
{
  FLFreeTypeOutline ch;
  FLoutline outline;
  int i;
  FT_Error error;
  short[] vertexcount; int vertexCountIndex; // java port
  FLpt2[][] vertex; int vertexIndex;// java port
  FLpt2 v;
  /*short[]*/int index, start, end; //java port
  int flags, j;

  flags = FT_LOAD_DEFAULT | FT_LOAD_NO_BITMAP;
  if (! fs.hint)
    flags |= FT_LOAD_NO_HINTING;

  i = FT_Get_Char_Index(fs.face, c);
  error = FT_Load_Glyph(fs.face, i, flags);

  ch = (error!=null) ? null : _flFTNewGlyphChar(fs, c, fs.face.glyph);
  if ( ch==null) {
    //TRACE(("_flFTLoadChar: c='%c'(%d) failed, error=%d\n", c, c, error));
    return null;
  }

//  /* Fix character advancement */
//  if (ch.size.x == 0.0)
//    ch.advance = (fs.face.max_advance_width >> 6) / KLUDGE_FACTOR;
//
//  //TRACE(("_flFTLoadChar: c='%c'(%d), numVertices=%d\n", c, c, ch.numVertices));
//
//  /* Fill up FL outline struct */
//  outline = ch/*.outline*/;
//  outline.outlinecount = 0;
//  outline.xadvance = ch.advance;
//  outline.yadvance = 0.0f;
//
//  if (ch.numVertices == 0) {
//    outline.vertexcount = null;
//    outline.vertex = null;
//    return ch;
//  }
//
//  /* Count the number of outline contours */
//  for (index = /*ch.indexes*/0 ; ch.indexes[index] != -1; index++, outline.outlinecount++)
//    for (index++; ch.indexes[index] != -1; index++)
//      ;
//
//  /* Create the vertex count and vertex arrays */
//  vertexcount = new short[outline.outlinecount];//(GLshort *) malloc(outline.outlinecount * sizeof(GLshort));
//  vertex = new FLpt2[outline.outlinecount][];//(FLpt2 **) malloc(outline.outlinecount * sizeof(FLpt2 *));
//  outline.vertexcount = vertexcount;
//  outline.vertex = vertex;
//
//  vertexCountIndex = 0;
//  vertexIndex = 0;
//  for (index = /*ch.indexes*/0 ; ch.indexes[(start = index)] != -1; index++) {
//    for (index++; ch.indexes[index] != -1; index++)
//      ;
//    vertexcount[vertexCountIndex] = (short)(index - start);
//    vertex[vertexIndex] = new FLpt2[vertexcount[vertexCountIndex]];//(FLpt2 *) malloc(*vertexcount * sizeof(FLpt2));
//    int vIndex = 0;
//    v = vertex[vertexIndex][vIndex];
//
//    end = index--;
//    while (index >= start) {
//      j = ch.indexes[index--];
//      v.x = ch.vertices[j].x;
//      v.y = ch.vertices[j].y;
//      vIndex++;
//    }
//    index = end;
//
//    vertexCountIndex++;
//    vertexIndex++;
//  }

  return ch;
}

private static FLFreeTypeOutline _flFTNewGlyphChar(FLFreeTypeFontStruct fs, char c, FT_GlyphSlot slot) {
	  FLFreeTypeOutline outline;
	  FLbitmap bitmap;

//	  TRACE(("_flFTNewGlyphChar: '%c'(0x%x), slot=%p\n",
//	         c < 128 ? c : '?', c, slot));

	  if ((outline = new FLFreeTypeOutline())==null)
	    return null;

	  outline.ch          = (short)c;
	  outline.size.x      = 0.0f;
	  outline.size.y      = 0.0f;
	  outline.advance     = 0.0f;
	  outline.indexes     = null;
	  outline.tessIndexes = null;
	  outline.numVertices = 0;
	  outline.vertices    = null;
	  outline.glyph       = slot;

	  _flFTGenerateGlyph(outline);

	  bitmap               = outline.bitmap;
	  bitmap.width        = 0;
	  bitmap.height       = 0;
	  bitmap.xorig        = 0.0f;
	  bitmap.yorig        = 0.0f;
	  bitmap.xmove        = 0.0f;
	  bitmap.ymove        = 0.0f;
	  bitmap.bitmap       = null;

	  return outline;
}


static int
_flFTGenerateGlyph(FLFreeTypeOutline ch)
{
  TPoint[] poolArray = new TPoint[4096];
  for(int i=0; i<4096; i++) { 
	  poolArray[i] = new TPoint(); // java port
  }
  int pool = 0; // java port
  final FT_Raster rec = new FT_Raster();
  FT_Raster     raster = rec;
  FT_Outline   outline = ch.glyph.outline;
  int           top, bottom, y_min, y_max;
  int           left, right, x_min, x_max;
  int           error;

  raster.pool = poolArray;
  raster.cursor = pool;
  raster.pool_size = 4096;//(TPoint ) pool + sizeof(pool);

//  /* return immediately if the outline is empty */
//  if (outline.n_points == 0 || outline.n_contours <= 0)
//    return 0;//ErrRaster_Ok;
//
//  if (outline== null || outline.contours == null || outline.points == null)
//    return ErrRaster_Invalid_Outline;
//
//  if (outline.n_points != outline.contours[outline.n_contours - 1] + 1)
//    return ErrRaster_Invalid_Outline;

  raster.outline  = outline;

  //SET_High_Precision((char)((outline.flags & ft_outline_high_precision)!= 0));

  raster.scale_shift  = PRECISION_BITS - INPUT_BITS;
  raster.scale_delta  = PRECISION_HALF;

  top      = BM_CEILING(ch.glyph.metrics.horiBearingY);
  bottom   = BM_FLOOR(ch.glyph.metrics.horiBearingY - ch.glyph.metrics.height);
  left     = BM_CEILING(ch.glyph.metrics.horiBearingX);
  right    = BM_FLOOR(ch.glyph.metrics.horiBearingX + ch.glyph.metrics.width);
  y_min    = 0;
  y_max    = BM_TRUNC(top - bottom);
  x_min    = 0;
  x_max    = BM_TRUNC(right - left);

  raster.maxY = ((long)y_max << PRECISION_BITS) - 1;
  raster.minY =  (long)y_min << PRECISION_BITS;
  raster.maxX = ((long)x_max << PRECISION_BITS) - 1;
  raster.minX =  (long)x_min << PRECISION_BITS;

  error = _flFTConvertGlyph(ch, raster,  raster.outline);
  if (error != 0)
    return error;

  return 0;
}

/* Bitmap precisions */
public static final int  BM_PRECISION_BITS       =6;
public static final int BM_PRECISION            =(1 << BM_PRECISION_BITS);
public static final int BM_PRECISION_MASK       =(-1 << BM_PRECISION_BITS);


static int BM_FLOOR(int x) {        return     ((x) & BM_PRECISION_MASK);}
static int BM_CEILING(int x) {      return     (((x) + BM_PRECISION - 1) & BM_PRECISION_MASK);}
static int BM_TRUNC(int x) {        return     ((int)(x) >> BM_PRECISION_BITS);}

	static final boolean FAILURE = true;

	  static FT_Outline_Funcs interface_table = new FT_Outline_Funcs(
			    new FT_Outline_Funcs.FT_Outline_MoveToFunc(){
			    	
			    },
			    new FT_Outline_Funcs.FT_Outline_LineToFunc(){
			    	
			    },
			    new FT_Outline_Funcs.FT_Outline_ConicToFunc(){
			    	
			    },
			    new FT_Outline_Funcs.FT_Outline_CubicToFunc(){
			    	
			    }
			  );
/*
 * Converts a glyph into a series of segments and arcs and makes a
 * faces list with them.
 */
static int
_flFTConvertGlyph(FLFreeTypeOutline ch, FT_Raster  raster, FT_Outline outline)
{
  int error = 0;

//  /* Set up state in the raster object */
//  raster.start_face = null;
//  raster.joint      = false;
//  raster.fresh      = false;
//
//  //raster.pool_limit = raster.pool_size - AlignFaceSize;
//
//  raster.n_extrema = 0;
//
//  raster.cur_face         = raster.cursor;
//  //raster.cur_face.offset = raster.cursor; TODO
//  raster.num_faces        = 0;
//
//  /* Now decompose curve */
//  error = FT_Outline_Decompose(outline, interface_table, raster);
//  if (error != 0)
//    return FAILURE? 1:0;
//
//  /* XXX: the error condition is in ras.error */
//
//  /* Check the last contour if needed */
//  if (Check_Contour(raster))
//    return FAILURE ? 1 :0;
//
//  /* Finalize faces list */
//  return _flFTFinalizeFaceTable(ch, raster);
  	
	FT_GlyphSlot slot = ch.glyph;
	FT_Face face = slot.face;
	TT_Face ttFace = (TT_Face) face;
	Font font = ttFace.font;
	AffineTransform affineTransform = new AffineTransform();
	FontRenderContext context = new FontRenderContext(affineTransform,false,true);
	char[] chars = new char[1];
	chars[0] = (char)ch.ch;
	GlyphVector gv = font.createGlyphVector(context, chars);
	Shape outlineShape = gv.getGlyphOutline(0);
	Rectangle2D logicalBounds = gv.getLogicalBounds();
	ch.xadvance = (float)logicalBounds.getWidth();
	if(ch.xadvance < 0.5 && chars[0] == ' ') ch.xadvance = 0.5f;
	float[] coords = new float[6];
	LinkedList<FLpt2> segment = null;
	final List<LinkedList<FLpt2>> segments = new ArrayList<>();
	for(PathIterator pi = outlineShape.getPathIterator(affineTransform); !pi.isDone(); pi.next()) {
		int windingRule = pi.getWindingRule();
		int type = pi.currentSegment(coords);
		switch(type) {
		case PathIterator.SEG_MOVETO: {
			float x = coords[0];
			float y = -coords[1];
			if(segment != null) {
				segments.add(segment);
			}
			segment = new LinkedList<>();
			segment.add(new FLpt2(x,y));
		}
		break;
		
		case PathIterator.SEG_LINETO: {
			float x = coords[0];
			float y = -coords[1];
			segment.add(new FLpt2(x,y));			
		}
		break;
		case PathIterator.SEG_QUADTO: {
			float xcp = segment.getLast().x;
			float ycp = segment.getLast().y;
			float p1x = coords[0];
			float p1y = -coords[1];
			float p2x = coords[2];
			float p2y = -coords[3];
			for(float t = 0.01f;t<1.0f; t+= 0.01f) {
				float ptx = b(2,0,t)*xcp+b(2,1,t)*p1x+b(2,2,t)*p2x;
				float pty = b(2,0,t)*ycp+b(2,1,t)*p1y+b(2,2,t)*p2y;
				segment.add(new FLpt2(ptx,pty));
			}
			segment.add(new FLpt2(p2x,p2y));						
		}
		break;
		case PathIterator.SEG_CUBICTO: {
			float xcp = segment.getLast().x;
			float ycp = segment.getLast().y;
			float p1x = coords[0];
			float p1y = -coords[1];
			float p2x = coords[2];
			float p2y = -coords[3];
			float p3x = coords[4];
			float p3y = -coords[5];
			for(float t = 0.01f;t<1.0f; t+= 0.01f) {
				float ptx = b(3,0,t)*xcp+b(3,1,t)*p1x+b(3,2,t)*p2x+ b(3,3,t)*p3x;
				float pty = b(3,0,t)*ycp+b(3,1,t)*p1y+b(3,2,t)*p2y+ b(3,3,t)*p3y;
				segment.add(new FLpt2(ptx,pty));
			}
			segment.add(new FLpt2(p3x,p3y));									
		}
		break;
		case PathIterator.SEG_CLOSE: {
			if(segment.size() >= 2 && !Objects.equals(segment.get(0), segment.get(segment.size()-1))) {
				segment.add(segment.get(0));
			}
		}
		break;		
		}		
	}
	if(segment != null) {
		segments.add(segment);		
	}
	int nbSegment = segments.size(); 
	ch.outlinecount = (short)nbSegment;
	ch.vertex = new FLpt2[nbSegment][];
	ch.vertexcount = new short[nbSegment];
	for(int segmentIndex = 0; segmentIndex<nbSegment; segmentIndex++) {
		segment = segments.get(segmentIndex);
		  ch.vertexcount[segmentIndex] = (short)segment.size();
		  ch.vertex[segmentIndex] = segment.toArray(new FLpt2[segment.size()]);
		  Collections.reverse(Arrays.asList(ch.vertex[segmentIndex]));
	}
	
	FT_Bitmap bitmap = slot.bitmap;
	float size = ttFace.size;
	
	BufferedImage bufferedImage = stringToBufferedImage(font, chars[0],size);
	Raster rasterData = bufferedImage.getData();
	if(rasterData != null) {
		DataBuffer dataBuffer = rasterData.getDataBuffer();
		if(dataBuffer instanceof DataBufferByte) {
			DataBufferByte dataBufferByte = (DataBufferByte)dataBuffer;
			int width = bufferedImage.getWidth();
			int height = bufferedImage.getHeight();
			byte[] ba = dataBufferByte.getData();
			byte[] binvert = new byte[ba.length];
			int nbByte = width/8;
			for(int row = 0; row<height;row++) {
				int rowInvert = height-row-1;
				for(int b = 0; b<nbByte;b++) {
					binvert[rowInvert*nbByte+b] = ba[row*nbByte+b];
				}
			}
			bitmap.buffer = binvert;
			bitmap.width = width;
			bitmap.rows = height;
			slot.metrics.horiAdvance = (width(font, chars[0],size)) << BM_PRECISION_BITS;
		}
	}

	return error;
}

public static BufferedImage stringToBufferedImage(Font font, char c, float size) {
    //First, we have to calculate the string's width and height
	AffineTransform trsf = new AffineTransform();
	trsf.scale(size, size);
	font = font.deriveFont(trsf);
	
	// create the binary mapping
    byte BLACK = (byte)0, WHITE = (byte)255;
    byte[] map = {BLACK, WHITE};
    IndexColorModel icm = new IndexColorModel(1, map.length, map, map, map);

    // create image from color model and data
    WritableRaster raster = icm.createCompatibleWritableRaster(1, 1);
    BufferedImage img = new BufferedImage(icm, raster, false, null);	

    //BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_BINARY);
    Graphics g = img.getGraphics();

    //Set the font to be used when drawing the string
    g.setFont(font);

    //Get the string visual bounds
    FontRenderContext frc = g.getFontMetrics().getFontRenderContext();
    String s = ""+c;
    Rectangle2D rect = font.getStringBounds(s, frc);
    //Release resources
    g.dispose();

    //Then, we have to draw the string on the final image

    //Create a new image where to print the character
    int width = Math.max(1,(int) Math.ceil(rect.getWidth()));
    width = (int)Math.ceil(width/32.0)*32;
    int height = Math.max(1,(int) Math.ceil(rect.getHeight()));
    raster = icm.createCompatibleWritableRaster(width, height);
    //img = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
    img = new BufferedImage(icm, raster, false, null);	
    g = img.getGraphics();
    g.setColor(Color.white); //Otherwise the text would be white
    g.setFont(font);

    //Calculate x and y for that string
    FontMetrics fm = g.getFontMetrics();
    int x = 0;
    int y = fm.getAscent(); //getAscent() = baseline
    g.drawString(s, x, y);

    //Release resources
    g.dispose();

    //Return the image
    return img;
}

public static int width(Font font, char c, float size) {
    //First, we have to calculate the string's width and height
	AffineTransform trsf = new AffineTransform();
	trsf.scale(size, size);
	font = font.deriveFont(trsf);
	
	// create the binary mapping
    byte BLACK = (byte)0, WHITE = (byte)255;
    byte[] map = {BLACK, WHITE};
    IndexColorModel icm = new IndexColorModel(1, map.length, map, map, map);

    // create image from color model and data
    WritableRaster raster = icm.createCompatibleWritableRaster(1, 1);
    BufferedImage img = new BufferedImage(icm, raster, false, null);	

    //BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_BINARY);
    Graphics g = img.getGraphics();

    //Set the font to be used when drawing the string
    g.setFont(font);

    //Get the string visual bounds
    FontRenderContext frc = g.getFontMetrics().getFontRenderContext();
    String s = ""+c;
    Rectangle2D rect = font.getStringBounds(s, frc);
    //Release resources
    g.dispose();

    return (int)rect.getWidth();
}

/**
 * mth coefficient of nth degree Bernstein polynomial
 * @param n
 * @param m
 * @param t
 * @return
 */
public static float b(int n, int m, float t) {
	return c(n,m) * (float)Math.pow(t,m) * (float)Math.pow(1 - t,n-m);
}

/**
 * Combinations of n things, taken m at a time
 * @param n
 * @param m
 * @return
 */
public static float c(int n, int m) {
	return fact(n)/fact(m)/fact(n-m);
}

/**
 * Factorial
 * @param n
 * @return
 */
public static float fact(int n) {
	float fact = 1.0f;
	for(int i=2;i<=n;i++) {
		fact *=i;
	}
	return fact;
}


  /*************************************************************************/
  /*                                                                       */
  /* <Function>                                                            */
  /*    FT_Load_Glyph                                                      */
  /*                                                                       */
  /* <Description>                                                         */
  /*    A function used to load a single glyph into the glyph slot of a    */
  /*    face object.                                                       */
  /*                                                                       */
  /* <InOut>                                                               */
  /*    face        :: A handle to the target face object where the glyph  */
  /*                   is loaded.                                          */
  /*                                                                       */
  /* <Input>                                                               */
  /*    glyph_index :: The index of the glyph in the font file.  For       */
  /*                   CID-keyed fonts (either in PS or in CFF format)     */
  /*                   this argument specifies the CID value.              */
  /*                                                                       */
  /*    load_flags  :: A flag indicating what to load for this glyph.  The */
  /*                   @FT_LOAD_XXX constants can be used to control the   */
  /*                   glyph loading process (e.g., whether the outline    */
  /*                   should be scaled, whether to load bitmaps or not,   */
  /*                   whether to hint the outline, etc).                  */
  /*                                                                       */
  /* <Return>                                                              */
  /*    FreeType error code.  0~means success.                             */
  /*                                                                       */
  /* <Note>                                                                */
  /*    The loaded glyph may be transformed.  See @FT_Set_Transform for    */
  /*    the details.                                                       */
  /*                                                                       */
  /*    For subsetted CID-keyed fonts, `FT_Err_Invalid_Argument' is        */
  /*    returned for invalid CID values (this is, for CID values that      */
  /*    don't have a corresponding glyph in the font).  See the discussion */
  /*    of the @FT_FACE_FLAG_CID_KEYED flag for more details.              */
  /*                                                                       */
private static FT_Error FT_Load_Glyph(FT_Face face, int glyph_index, int load_flags) {
	// TODO Auto-generated method stub
	return null;
}

private static int FT_Get_Char_Index(FT_Face face, int charcode) {
    int  result = 0;


//    if ( face != null && face.charmap != null)
//    {
//      FT_CMap  cmap = (FT_CMap)( face.charmap );
//
//
//      if ( charcode > 0xFFFFFFFFL )
//      {
//        //FT_TRACE1(( "FT_Get_Char_Index: too large charcode" ));
//        //FT_TRACE1(( " 0x%x is truncated\n", charcode ));
//      }
//      result = cmap.clazz.char_index.run( cmap, (int)charcode );
//    }
    result = charcode; // java port
    
    
    return result;
}


static FLfontStruct _flFTCreateFont(String fontName,
                float        mat[][],
                int          charNameCount,
                String[]     charNameVector)
{
  FLfontStruct fs;
  String        path;
  final FT_Face[]       face = new FT_Face[1];
  int      error;

  if ( (path = _flSearchFont(fontName))==null)
    return null;

  error = FT_New_Face(library, path, 0, face);

  //free(path); java port

  if ( error == 0) {
    int px = (mat[0][0] == 1.0f) ? 32 : (int)mat[0][0];
    int py = (mat[1][1] == 1.0f) ? 32 : (int)mat[1][1];

    error = FT_Set_Char_Size(face[0], px << 6, py << 6, (int)mat[0][1], (int)mat[1][0]);

    if ( error == 0) {
      fs = (FLfontStruct ) _flFTNewGlyphFont(fontName, face[0]);
      return fs;
    }
  }

  return null;
}


private static FLbitmap _flFTUniGetBitmap(FLfontStruct[] fsList, char uCS2) {

  FLfontStruct fs;
  FLbitmap bitmap;
//#ifdef FL_LITTLE_ENDIAN
  int c = uCS2;//(UCS2[1] << 8) | UCS2[0];
//#else
//  GLuint c = (UCS2[0] << 8) | UCS2[1];
//#endif

  //TRACE(("_flFTUniGetBitmap: 0x%04x\n", c));
  int fsListIndex = 0;
  while ((fs = fsList[fsListIndex++])!= null)
    if ((bitmap = _flFTGetBitmap(fs, c))!= null)
      return bitmap;

  return null;
}


private static FLbitmap _flFTGetBitmap(FLfontStruct _fs, int c) {
  FLFreeTypeFontStruct fs = (FLFreeTypeFontStruct ) _fs;
  FLFreeTypeOutline outline = (FLFreeTypeOutline ) _flFTGetOutline(_fs, c);
  FT_Face face = fs.face;
  FT_GlyphSlot glyph = outline.glyph;
  final FT_Bitmap  bit2 = new FT_Bitmap();
  FLbitmap bit3;
  FT_Error error;
  int width, height, pitch, size;
  int left, right, top, bottom;
  int bbox_width, bbox_height;
  int bearing_x, bearing_y;
  int pitch2, size2;

  //TRACE(("_flFTGetBitmap: '%c'(0x%04x)\n", c, c));

  /* See if it is already created */
  bit3 = outline.bitmap;
  if (bit3.bitmap != null)
    return bit3;

  /* No, proceed to create it */
  left        = BM_FLOOR(glyph.metrics.horiBearingX);
  right       = BM_CEILING(glyph.metrics.horiBearingX + glyph.metrics.width);
  width       = BM_TRUNC(right - left);
    
  top         = BM_CEILING(glyph.metrics.horiBearingY);
  bottom      = BM_FLOOR(glyph.metrics.horiBearingY - glyph.metrics.height);
  height      = BM_TRUNC(top - bottom);

  bearing_x   = BM_TRUNC(glyph.metrics.horiBearingX);
  bearing_y   = BM_TRUNC(glyph.metrics.horiBearingY);

  bbox_width  = BM_TRUNC(face.bbox.xMax - face.bbox.xMin);
  bbox_height = BM_TRUNC(face.bbox.yMax - face.bbox.yMin);

  if (glyph.format == FT_Glyph_Format.FT_GLYPH_FORMAT_OUTLINE) {    
    pitch     = (width + 7) >> 3;
    size      = pitch * height; 
    pitch2    = ((width + (PIXEL_ROW_ALIGNMENT << 3) - 1) >> 5) << 2;
    size2     = pitch2 * height;

    bit2.width      = width;
    bit2.rows       = height;
    bit2.pitch      = pitch;
    bit2.pixel_mode = FT_Pixel_Mode.FT_PIXEL_MODE_MONO;
    bit2.buffer     = new byte[size];

    bit3.width     = bit2.width;
    bit3.height    = bit2.rows;
    bit3.xorig     = -bearing_x;
    bit3.yorig     = height - bearing_y;
    bit3.xmove     = BM_TRUNC(glyph.metrics.horiAdvance);
    bit3.ymove     = 0.0f;
    bit3.bitmap    = new byte[size2];

    FT_Outline_Translate(glyph.outline, -left, -bottom);
    //memset(bit2.buffer, 0, size); java port
    //memset(bit3.bitmap, 0, size2); java port

    if (fs.lowPrec)
      glyph.outline.flags &= ~FT_OUTLINE_HIGH_PRECISION;
      
    error = FT_Outline_Get_Bitmap(library, glyph.outline, bit2);

    bitmap_convert(width, height, pitch, pitch2, bit2.buffer, bit3.bitmap);

    //free(bit2.buffer); java port
  }
  else {
    bit3.width     = glyph.bitmap.width;
    bit3.height    = glyph.bitmap.rows;
    bit3.xorig     = -bearing_x;
    bit3.yorig     = height - bearing_y;
    bit3.xmove     = BM_TRUNC(glyph.metrics.horiAdvance);
    bit3.ymove     = 0.0f;
    bit3.bitmap    = glyph.bitmap.buffer;
  }

  return bit3;
}

private static void bitmap_convert(int width, int height, int pitch, int pitch2, byte[] src, byte[] dst) {
	  int i, j, c;
	  byte bit;
	  int p;

	  int srcIndex=0;
	  for (j = 0; j < height; j++) {
	    bit = (byte)0x80;
	    p = 0/*src*/;
	    c = src[p+srcIndex];
	    for (i = 0; i < width; i++) {
	      if ((c & bit)!=0)
	        dst[(height - 1 - j) * pitch2 + i / 8] |= (1 << (7 - (i & 7)));

	      bit >>= 1;
	      if ( bit==0) {
	        bit = (byte)0x80;
	        p++;
	        c = src[p+srcIndex];
	      }
	    }

	    srcIndex += pitch;
	  }
}

private static FT_Error FT_Outline_Get_Bitmap(FT_Library library2, FT_Outline outline, FT_Bitmap bit2) {
	// TODO Auto-generated method stub
	return null;
}

private static void FT_Outline_Translate(FT_Outline outline, int xOffset, int yOffset) {
    short   n;
    FT_Vector[]  vec;


    if ( outline == null)
      return;

    vec = outline.points;

    int vecIndex =0;
    for ( n = 0; n < outline.n_points; n++ )
    {
      vec[vecIndex].x += xOffset;
      vec[vecIndex].y += yOffset;
      vecIndex++;
    }
}

private static int FT_New_Face(FT_Library library2, String pathname, int face_index, FT_Face[] aface) {
    final FT_Open_Args  args = new FT_Open_Args();


    /* test for valid `library' and `aface' delayed to `FT_Open_Face' */
    if ( pathname == null)
      return 0x06;//FT_THROW( Invalid_Argument );

    args.flags    = FT_OPEN_PATHNAME;
    args.pathname = (String)pathname;
    args.stream   = null;

    return FT_Open_Face( library, args, face_index, aface );
}

private static int FT_Open_Face(FT_Library library2, FT_Open_Args args, int face_index, FT_Face[] aface) {
	int error;
	FT_Driver driver = null;
	final InputStream[] stream = new InputStream[1];
	final FT_Face[] face = new FT_Face[1];
	FT_ListNode node = null;
    boolean      external_stream;
	
    /* test for valid `library' delayed to `FT_Stream_New' */

    if ( ( aface==null && face_index >= 0 ) || args==null )
      return 0x06;//FT_THROW( Invalid_Argument );

    external_stream = ( ( args.flags & FT_OPEN_STREAM )!=0 &&
            args.stream != null                    );
	
    /* create input stream */
    error = FT_Stream_New( library, args, stream );
    if ( error != 0)
      ;//goto Fail3;
    
    driver = new FT_Driver(); // java port
    driver.clazz = new FT_Driver_Class(); // java port
    
    int         num_params = 0;
    FT_Parameter[]  params     = null;
    
    error = open_face( driver, stream[0], external_stream, face_index,
            num_params, params, face );
    
    if(error == 0) {
    	node = new FT_ListNode();
    	
    	node.data = face[0];
    	
    	face[0].driver.faces_list.add(node);
    	
    if ( face_index >= 0 )
    {
      error = FT_New_GlyphSlot( face[0], null );
      if ( error !=0)
        return error;//goto Fail;

      /* finally, allocate a size object for the face */
      {
        final FT_Size  size = new FT_Size();


        //FT_TRACE4(( "FT_Open_Face: Creating size object\n" ));

        error = FT_New_Size( face[0], size );
        if ( error != 0)
          return error;//goto Fail;

        face[0].size = size;
      }
    }
    
    /* initialize internal face data */
    {
      FT_Face_Internal  internal = face[0].internal;


      internal.transform_matrix.xx = 0x10000L;
      internal.transform_matrix.xy = 0;
      internal.transform_matrix.yx = 0;
      internal.transform_matrix.yy = 0x10000L;

      internal.transform_delta.x = 0;
      internal.transform_delta.y = 0;

      internal.refcount = 1;
    }

    if ( aface != null)
      aface[0] = face[0];

    }
    
	return error;
}


  private static int FT_New_Size(FT_Face ft_Face, FT_Size size) {
	// TODO Auto-generated method stub
	return 0;
}

private static int FT_New_GlyphSlot(FT_Face face, Object object) {
	
	FT_GlyphSlot slot = null;
	
	slot = new FT_GlyphSlot();
	slot.face = face;
	face.glyph = slot;
	return 0;
}

/*************************************************************************/
  /*                                                                       */
  /* <Function>                                                            */
  /*    open_face                                                          */
  /*                                                                       */
  /* <Description>                                                         */
  /*    This function does some work for FT_Open_Face().                   */
  /*                                                                       */
  static int
  open_face( FT_Driver      driver,
             InputStream      astream,
             boolean        external_stream,
             long        face_index,
             int         num_params,
             FT_Parameter[]  params,
             final FT_Face[]       aface )
  {
    FT_Memory         memory;
    FT_Driver_Class   clazz;
    FT_Face           face     = null;
    FT_Face_Internal  internal = null;

    int          error, error2;


    clazz  = driver.clazz;
    memory = driver.root.memory;

    /* allocate the face object and perform basic initialization */
//    if ( FT_ALLOC( face, clazz.face_object_size ) )
//      return -1;//goto Fail;
    face = new TT_Face();

    face.driver = driver;
    face.memory = memory;
    face.stream = astream;

    /* set the FT_FACE_FLAG_EXTERNAL_STREAM bit for FT_Done_Face */
    if ( external_stream )
      face.face_flags |= FT_FACE_FLAG_EXTERNAL_STREAM;

//    if ( FT_NEW( internal ) )
//      return -1;//goto Fail;
    internal = new FT_Face_Internal();

    face.internal = internal;

//#ifdef FT_CONFIG_OPTION_INCREMENTAL
    {
      int  i;


      face.internal.incremental_interface = null;
      for ( i = 0; i < num_params && face.internal.incremental_interface==null;
            i++ )
        if ( params[i].tag == FT_PARAM_TAG_INCREMENTAL )
          face.internal.incremental_interface =
            (FT_Incremental_Interface)params[i].data;
    }
//#endif

    //if ( clazz.init_face != null)
      error = clazz.init_face( astream,
                                face,
                                (int)face_index,
                                num_params,
                                params );
    astream = face.stream; /* Stream may have been changed. */
    if ( error != 0)
      return -1;//goto Fail;

    /* select Unicode charmap by default */
    error2 = find_unicode_charmap( face );

    /* if no Unicode charmap can be found, FT_Err_Invalid_CharMap_Handle */
    /* is returned.                                                      */

    /* no error should happen, but we want to play safe */
    if ( error2 != 0 && error2 != 0x26/*Invalid_CharMap_Handle*/  )
    {
      error = error2;
      return error;//goto Fail;
    }

    aface[0] = face;

  Fail:
    if ( error != 0)
    {
//      destroy_charmaps( face, memory );
//      if ( clazz.done_face )
//        clazz.done_face( face );
//      FT_FREE( internal );
//      FT_FREE( face );
      aface[0] = null;
    }

    return error;
  }


  /*************************************************************************/
  /*                                                                       */
  /* <Function>                                                            */
  /*    find_unicode_charmap                                               */
  /*                                                                       */
  /* <Description>                                                         */
  /*    This function finds a Unicode charmap, if there is one.            */
  /*    And if there is more than one, it tries to favour the more         */
  /*    extensive one, i.e., one that supports UCS-4 against those which   */
  /*    are limited to the BMP (said UCS-2 encoding.)                      */
  /*                                                                       */
  /*    This function is called from open_face() (just below), and also    */
  /*    from FT_Select_Charmap( ..., FT_ENCODING_UNICODE ).                */
  /*                                                                       */
  static int
  find_unicode_charmap( FT_Face  face )
  {
    /*FT_CharMap*/int  first; // java port
    /*FT_CharMap*/int  cur;


    /* caller should have already checked that `face' is valid */
    //FT_ASSERT( face );

    first = 0/*face.charmaps*/;

    if ( face.charmaps == null )
      return 0x26;//FT_THROW( Invalid_CharMap_Handle );

    /*
     *  The original TrueType specification(s) only specified charmap
     *  formats that are capable of mapping 8 or 16 bit character codes to
     *  glyph indices.
     *
     *  However, recent updates to the Apple and OpenType specifications
     *  introduced new formats that are capable of mapping 32-bit character
     *  codes as well.  And these are already used on some fonts, mainly to
     *  map non-BMP Asian ideographs as defined in Unicode.
     *
     *  For compatibility purposes, these fonts generally come with
     *  *several* Unicode charmaps:
     *
     *   - One of them in the "old" 16-bit format, that cannot access
     *     all glyphs in the font.
     *
     *   - Another one in the "new" 32-bit format, that can access all
     *     the glyphs.
     *
     *  This function has been written to always favor a 32-bit charmap
     *  when found.  Otherwise, a 16-bit one is returned when found.
     */

    /* Since the `interesting' table, with IDs (3,10), is normally the */
    /* last one, we loop backwards.  This loses with type1 fonts with  */
    /* non-BMP characters (<.0001%), this wins with .ttf with non-BMP  */
    /* chars (.01% ?), and this is the same about 99.99% of the time!  */

    cur = first + face.num_charmaps;  /* points after the last one */

    for ( ; --cur >= first; )
    {
      if ( face.charmaps[cur].encoding == FT_Encoding.FT_ENCODING_UNICODE )
      {
        /* XXX If some new encodings to represent UCS-4 are added, */
        /*     they should be added here.                          */
        if ( ( face.charmaps[cur].platform_id == TT_PLATFORM_MICROSOFT &&
        		face.charmaps[cur].encoding_id == TT_MS_ID_UCS_4        )     ||
             ( face.charmaps[cur].platform_id == TT_PLATFORM_APPLE_UNICODE &&
            		 face.charmaps[cur].encoding_id == TT_APPLE_ID_UNICODE_32    ) )
        {
          face.charmap = face.charmaps[cur];
          return 0;//FT_Err_Ok;
        }
      }
    }

    /* We do not have any UCS-4 charmap.                */
    /* Do the loop again and search for UCS-2 charmaps. */
    cur = first + face.num_charmaps;

    for ( ; --cur >= first; )
    {
      if ( face.charmaps[cur].encoding == FT_Encoding.FT_ENCODING_UNICODE )
      {
        face.charmap = face.charmaps[cur];
        return 0;//FT_Err_Ok;
      }
    }

    return 0x26;//FT_THROW( Invalid_CharMap_Handle );
  }

  

private static int FT_Stream_New(FT_Library library2, FT_Open_Args args, InputStream[] astream) {
	
	int error = 0;
    FT_Memory  memory;
    final InputStream[]  stream = new InputStream[1];
	
	astream[0] = null;
	
    if ( library2 == null)
        return 0x21;//FT_THROW( Invalid_Library_Handle );

      if ( args == null)
        return  0x06;//FT_THROW( Invalid_Argument );

      memory = library.memory;

//      if ( FT_NEW( stream ) )
//         ;// goto Exit;
//
//        stream.memory = memory;

      if ( (args.flags & FT_OPEN_PATHNAME) != 0 )
      {
        /* create a normal system stream */
        error = FT_Stream_Open( stream, args.pathname );
        //stream.pathname.pointer = args.pathname;
      }

      astream[0] = stream[0];

      Exit:
	return error;
}

private static int FT_Stream_Open(InputStream[] stream, String pathname) {
	
    FileSystem fileSystem = FileSystems.getDefault();
    Path fileNamePath = fileSystem.getPath(pathname);
	OpenOption option = StandardOpenOption.READ;
	try {
		stream[0] = Files.newInputStream(fileNamePath, option);
		return 0;
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return 0x01;
	}	
}

/*
 * Search the given font name in font path, returns an allocated path if
 * found, else returns NULL.
 */
private static String _flSearchFont(String fontName) {
	
	FileSystem fs = FileSystems.getDefault();
	Path path = fs.getPath(fontPath,fontName+".ttf");
	if(path.toFile().exists() && path.toFile().isFile()) {
		return path.toString();
	}
	path = fs.getPath(fontDefault);
	if(path.toFile().exists() && path.toFile().isFile()) {
		return path.toString();
	}
	return null;
}


static int FT_Set_Char_Size( FT_Face     face,
                  int  char_width,
                  int  char_height,
                  int     horz_resolution,
                  int     vert_resolution )
{
  final FT_Size_Request  req = new FT_Size_Request();


  /* check of `face' delayed to `FT_Request_Size' */

  if ( char_width == 0)
    char_width = char_height;
  else if ( char_height ==0)
    char_height = char_width;

  if ( horz_resolution ==0)
    horz_resolution = vert_resolution;
  else if ( vert_resolution ==0)
    vert_resolution = horz_resolution;

  if ( char_width  < 1 * 64 )
    char_width  = 1 * 64;
  if ( char_height < 1 * 64 )
    char_height = 1 * 64;

  if ( horz_resolution ==0)
    horz_resolution = vert_resolution = 72;

  req.type           = FT_Size_Request_Type.FT_SIZE_REQUEST_TYPE_NOMINAL;
  req.width          = char_width;
  req.height         = char_height;
  req.horiResolution = horz_resolution;
  req.vertResolution = vert_resolution;

  return FT_Request_Size( face, req );
}

static int
FT_Request_Size( FT_Face          face,
                 FT_Size_Request  req )
{
  //FT_Driver_Class  clazz;
  long         strike_index;


  if ( face == null)
    return 0x23;//FT_THROW( Invalid_Face_Handle );

  if ( req == null || req.width < 0 || req.height < 0 ||
       req.type.ordinal() >= FT_Size_Request_Type.FT_SIZE_REQUEST_TYPE_MAX.ordinal() )
    return 0x06;//FT_THROW( Invalid_Argument );

  //clazz = face.driver.clazz; TODO

  if ( /*clazz.request_size !=null*/true) //TODO
  {
    int  error;

    TT_Face ttf = (TT_Face) face;
    ttf.size = req.height >> 6;

    error = 0;//clazz.request_size( face.size, req ); TODO

//#ifdef FT_DEBUG_LEVEL_TRACE
//    {
//      FT_Size_Metrics*  metrics = &face.size.metrics;
//
//
//      FT_TRACE5(( "FT_Request_Size (font driver's `request_size'):\n" ));
//      FT_TRACE5(( "  x scale: %d (%f)\n",
//                  metrics.x_scale, metrics.x_scale / 65536.0 ));
//      FT_TRACE5(( "  y scale: %d (%f)\n",
//                  metrics.y_scale, metrics.y_scale / 65536.0 ));
//      FT_TRACE5(( "  ascender: %f\n",    metrics.ascender / 64.0 ));
//      FT_TRACE5(( "  descender: %f\n",   metrics.descender / 64.0 ));
//      FT_TRACE5(( "  height: %f\n",      metrics.height / 64.0 ));
//      FT_TRACE5(( "  max advance: %f\n", metrics.max_advance / 64.0 ));
//      FT_TRACE5(( "  x ppem: %d\n",      metrics.x_ppem ));
//      FT_TRACE5(( "  y ppem: %d\n",      metrics.y_ppem ));
//    }
//#endif

    return error;
  }

  /*
   * The reason that a driver doesn't have `request_size' defined is
   * either that the scaling here suffices or that the supported formats
   * are bitmap-only and size matching is not implemented.
   *
   * In the latter case, a simple size matching is done.
   */
//  if ( !FT_IS_SCALABLE( face ) && FT_HAS_FIXED_SIZES( face ) ) TODO
//  {
//    FT_Error  error;
//
//
//    error = FT_Match_Size( face, req, 0, &strike_index );
//    if ( error )
//      return error;
//
//    return FT_Select_Size( face, (FT_Int)strike_index );
//  }
//
//  FT_Request_Metrics( face, req );
//
//  return FT_Err_Ok;
  return 0; //TODO
  }

/*------------------------------------------------------------------*/

static FLFreeTypeFontStruct 
_flFTNewGlyphFont(String fontName, FT_Face newFace)
{
  FLFreeTypeFontStruct fs;

  if ( (fs = new FLFreeTypeFontStruct() )==null)
    return null;

  fs.char8      = new FLFreeTypeOutline[256];
  fs.char16     = null;
  fs.index16    = null;
  fs.num16      = 0;
  //memset(fs.char8, 0, 256 * sizeof(FLFreeTypeOutline *)); java port

  fs.name       = fontName;
  fs.index16    = null;
  fs.face       = newFace;
  fs.hint       = true;
  fs.grayRender = true;
  fs.lowPrec    = false;

  fs.bound.x = (newFace.bbox.xMax - newFace.bbox.xMin) >> 5;
  fs.bound.y = (newFace.bbox.yMax - newFace.bbox.yMin) >> 5;

  fs.bound.x /= KLUDGE_FACTOR;
  fs.bound.y /= KLUDGE_FACTOR;
  if (fs.bound.y < 0.6)
    fs.bound.y = 1.2f;

//#if 0
//  {
//    int i;
//
//    printf(">>> num_charmaps=%d\n", newFace.num_charmaps);
//    for (i = 0; i < newFace.num_charmaps; i++) {
//      char *s = "???";
//      switch (newFace.charmaps[i].encoding) {
//      case ft_encoding_symbol:         s = "symbol"; break;
//      case ft_encoding_unicode:        s = "unicode"; break;
//      case ft_encoding_latin_2:        s = "latin_2"; break;
//      case ft_encoding_sjis:           s = "sjis"; break;
//      case ft_encoding_big5:           s = "big5"; break;
//      case ft_encoding_adobe_standard: s = "adobe_standard"; break;
//      case ft_encoding_adobe_expert:   s = "adobe_expert"; break;
//      case ft_encoding_adobe_custom:   s = "adobe_custom"; break;
//      case ft_encoding_apple_roman:    s = "apple_roman"; break;
//      }
//      printf(">>>   %d. encoding=%s, platform_id=%d, encoding_id=%d\n",
//             i, s, newFace.charmaps[i].platform_id, newFace.charmaps[i].encoding_id);
//    }
//  }
//#endif

  return fs;
}

	public static void flFreeBitmap(FLbitmap value) {
	}


}
