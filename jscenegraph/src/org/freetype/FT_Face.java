/**
 * 
 */
package org.freetype;

import java.io.InputStream;

/**
 * @author Yves Boyadjian
 *
 */
/*************************************************************************/
/*                                                                       */
/* <Type>                                                                */
/*    FT_Face                                                            */
/*                                                                       */
/* <Description>                                                         */
/*    A handle to a given typographic face object.  A face object models */
/*    a given typeface, in a given style.                                */
/*                                                                       */
/* <Note>                                                                */
/*    Each face object also owns a single @FT_GlyphSlot object, as well  */
/*    as one or more @FT_Size objects.                                   */
/*                                                                       */
/*    Use @FT_New_Face or @FT_Open_Face to create a new face object from */
/*    a given filepathname or a custom input stream.                     */
/*                                                                       */
/*    Use @FT_Done_Face to destroy it (along with its slot and sizes).   */
/*                                                                       */
/* <Also>                                                                */
/*    See @FT_FaceRec for the publicly accessible fields of a given face */
/*    object.                                                            */
/*                                                                       */

  /*************************************************************************/
  /*                                                                       */
  /* <Struct>                                                              */
  /*    FT_FaceRec                                                         */
  /*                                                                       */
  /* <Description>                                                         */
  /*    FreeType root face class structure.  A face object models a        */
  /*    typeface in a font file.                                           */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    num_faces           :: The number of faces in the font file.  Some */
  /*                           font formats can have multiple faces in     */
  /*                           a font file.                                */
  /*                                                                       */
  /*    face_index          :: The index of the face in the font file.  It */
  /*                           is set to~0 if there is only one face in    */
  /*                           the font file.                              */
  /*                                                                       */
  /*    face_flags          :: A set of bit flags that give important      */
  /*                           information about the face; see             */
  /*                           @FT_FACE_FLAG_XXX for the details.          */
  /*                                                                       */
  /*    style_flags         :: A set of bit flags indicating the style of  */
  /*                           the face; see @FT_STYLE_FLAG_XXX for the    */
  /*                           details.                                    */
  /*                                                                       */
  /*    num_glyphs          :: The number of glyphs in the face.  If the   */
  /*                           face is scalable and has sbits (see         */
  /*                           `num_fixed_sizes'), it is set to the number */
  /*                           of outline glyphs.                          */
  /*                                                                       */
  /*                           For CID-keyed fonts, this value gives the   */
  /*                           highest CID used in the font.               */
  /*                                                                       */
  /*    family_name         :: The face's family name.  This is an ASCII   */
  /*                           string, usually in English, which describes */
  /*                           the typeface's family (like `Times New      */
  /*                           Roman', `Bodoni', `Garamond', etc).  This   */
  /*                           is a least common denominator used to list  */
  /*                           fonts.  Some formats (TrueType & OpenType)  */
  /*                           provide localized and Unicode versions of   */
  /*                           this string.  Applications should use the   */
  /*                           format specific interface to access them.   */
  /*                           Can be NULL (e.g., in fonts embedded in a   */
  /*                           PDF file).                                  */
  /*                                                                       */
  /*    style_name          :: The face's style name.  This is an ASCII    */
  /*                           string, usually in English, which describes */
  /*                           the typeface's style (like `Italic',        */
  /*                           `Bold', `Condensed', etc).  Not all font    */
  /*                           formats provide a style name, so this field */
  /*                           is optional, and can be set to NULL.  As    */
  /*                           for `family_name', some formats provide     */
  /*                           localized and Unicode versions of this      */
  /*                           string.  Applications should use the format */
  /*                           specific interface to access them.          */
  /*                                                                       */
  /*    num_fixed_sizes     :: The number of bitmap strikes in the face.   */
  /*                           Even if the face is scalable, there might   */
  /*                           still be bitmap strikes, which are called   */
  /*                           `sbits' in that case.                       */
  /*                                                                       */
  /*    available_sizes     :: An array of @FT_Bitmap_Size for all bitmap  */
  /*                           strikes in the face.  It is set to NULL if  */
  /*                           there is no bitmap strike.                  */
  /*                                                                       */
  /*    num_charmaps        :: The number of charmaps in the face.         */
  /*                                                                       */
  /*    charmaps            :: An array of the charmaps of the face.       */
  /*                                                                       */
  /*    generic             :: A field reserved for client uses.  See the  */
  /*                           @FT_Generic type description.               */
  /*                                                                       */
  /*    bbox                :: The font bounding box.  Coordinates are     */
  /*                           expressed in font units (see                */
  /*                           `units_per_EM').  The box is large enough   */
  /*                           to contain any glyph from the font.  Thus,  */
  /*                           `bbox.yMax' can be seen as the `maximal     */
  /*                           ascender', and `bbox.yMin' as the `minimal  */
  /*                           descender'.  Only relevant for scalable     */
  /*                           formats.                                    */
  /*                                                                       */
  /*                           Note that the bounding box might be off by  */
  /*                           (at least) one pixel for hinted fonts.  See */
  /*                           @FT_Size_Metrics for further discussion.    */
  /*                                                                       */
  /*    units_per_EM        :: The number of font units per EM square for  */
  /*                           this face.  This is typically 2048 for      */
  /*                           TrueType fonts, and 1000 for Type~1 fonts.  */
  /*                           Only relevant for scalable formats.         */
  /*                                                                       */
  /*    ascender            :: The typographic ascender of the face,       */
  /*                           expressed in font units.  For font formats  */
  /*                           not having this information, it is set to   */
  /*                           `bbox.yMax'.  Only relevant for scalable    */
  /*                           formats.                                    */
  /*                                                                       */
  /*    descender           :: The typographic descender of the face,      */
  /*                           expressed in font units.  For font formats  */
  /*                           not having this information, it is set to   */
  /*                           `bbox.yMin'.  Note that this field is       */
  /*                           usually negative.  Only relevant for        */
  /*                           scalable formats.                           */
  /*                                                                       */
  /*    height              :: The height is the vertical distance         */
  /*                           between two consecutive baselines,          */
  /*                           expressed in font units.  It is always      */
  /*                           positive.  Only relevant for scalable       */
  /*                           formats.                                    */
  /*                                                                       */
  /*    max_advance_width   :: The maximal advance width, in font units,   */
  /*                           for all glyphs in this face.  This can be   */
  /*                           used to make word wrapping computations     */
  /*                           faster.  Only relevant for scalable         */
  /*                           formats.                                    */
  /*                                                                       */
  /*    max_advance_height  :: The maximal advance height, in font units,  */
  /*                           for all glyphs in this face.  This is only  */
  /*                           relevant for vertical layouts, and is set   */
  /*                           to `height' for fonts that do not provide   */
  /*                           vertical metrics.  Only relevant for        */
  /*                           scalable formats.                           */
  /*                                                                       */
  /*    underline_position  :: The position, in font units, of the         */
  /*                           underline line for this face.  It is the    */
  /*                           center of the underlining stem.  Only       */
  /*                           relevant for scalable formats.              */
  /*                                                                       */
  /*    underline_thickness :: The thickness, in font units, of the        */
  /*                           underline for this face.  Only relevant for */
  /*                           scalable formats.                           */
  /*                                                                       */
  /*    glyph               :: The face's associated glyph slot(s).        */
  /*                                                                       */
  /*    size                :: The current active size for this face.      */
  /*                                                                       */
  /*    charmap             :: The current active charmap for this face.   */
  /*                                                                       */
  /* <Note>                                                                */
  /*    Fields may be changed after a call to @FT_Attach_File or           */
  /*    @FT_Attach_Stream.                                                 */
  /*                                                                       */
public class FT_Face {
	
    public long           face_flags;

    public int            num_charmaps;
    public FT_CharMap[]       charmaps;


    /*# The following member variables (down to `underline_thickness') */
    /*# are only relevant to scalable outlines; cf. @FT_Bitmap_Size    */
    /*# for bitmap fonts.                                              */
    public final FT_BBox           bbox = new FT_BBox();

    short          max_advance_width;
    short          max_advance_height;

    public FT_GlyphSlot      glyph;
    public FT_Size           size;
    public FT_CharMap	charmap; // pointer

    public FT_Driver         driver;
    public FT_Memory         memory;
    public InputStream         stream;

    public FT_Face_Internal  internal;

}
