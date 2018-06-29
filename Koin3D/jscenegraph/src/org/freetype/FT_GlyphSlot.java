  /*************************************************************************/
  /*                                                                       */
  /* <Type>                                                                */
  /*    FT_GlyphSlot                                                       */
  /*                                                                       */
  /* <Description>                                                         */
  /*    A handle to a given `glyph slot'.  A slot is a container where it  */
  /*    is possible to load any of the glyphs contained in its parent      */
  /*    face.                                                              */
  /*                                                                       */
  /*    In other words, each time you call @FT_Load_Glyph or               */
  /*    @FT_Load_Char, the slot's content is erased by the new glyph data, */
  /*    i.e., the glyph's metrics, its image (bitmap or outline), and      */
  /*    other control information.                                         */
  /*                                                                       */
  /* <Also>                                                                */
  /*    See @FT_GlyphSlotRec for the publicly accessible glyph fields.     */
  /*                                                                       */
package org.freetype;

/**
 * @author Yves Boyadjian
 *
 */
  /*************************************************************************/
  /*                                                                       */
  /* <Struct>                                                              */
  /*    FT_GlyphSlotRec                                                    */
  /*                                                                       */
  /* <Description>                                                         */
  /*    FreeType root glyph slot class structure.  A glyph slot is a       */
  /*    container where individual glyphs can be loaded, be they in        */
  /*    outline or bitmap format.                                          */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    library           :: A handle to the FreeType library instance     */
  /*                         this slot belongs to.                         */
  /*                                                                       */
  /*    face              :: A handle to the parent face object.           */
  /*                                                                       */
  /*    next              :: In some cases (like some font tools), several */
  /*                         glyph slots per face object can be a good     */
  /*                         thing.  As this is rare, the glyph slots are  */
  /*                         listed through a direct, single-linked list   */
  /*                         using its `next' field.                       */
  /*                                                                       */
  /*    generic           :: A typeless pointer unused by the FreeType     */
  /*                         library or any of its drivers.  It can be     */
  /*                         used by client applications to link their own */
  /*                         data to each glyph slot object.               */
  /*                                                                       */
  /*    metrics           :: The metrics of the last loaded glyph in the   */
  /*                         slot.  The returned values depend on the last */
  /*                         load flags (see the @FT_Load_Glyph API        */
  /*                         function) and can be expressed either in 26.6 */
  /*                         fractional pixels or font units.              */
  /*                                                                       */
  /*                         Note that even when the glyph image is        */
  /*                         transformed, the metrics are not.             */
  /*                                                                       */
  /*    linearHoriAdvance :: The advance width of the unhinted glyph.      */
  /*                         Its value is expressed in 16.16 fractional    */
  /*                         pixels, unless @FT_LOAD_LINEAR_DESIGN is set  */
  /*                         when loading the glyph.  This field can be    */
  /*                         important to perform correct WYSIWYG layout.  */
  /*                         Only relevant for outline glyphs.             */
  /*                                                                       */
  /*    linearVertAdvance :: The advance height of the unhinted glyph.     */
  /*                         Its value is expressed in 16.16 fractional    */
  /*                         pixels, unless @FT_LOAD_LINEAR_DESIGN is set  */
  /*                         when loading the glyph.  This field can be    */
  /*                         important to perform correct WYSIWYG layout.  */
  /*                         Only relevant for outline glyphs.             */
  /*                                                                       */
  /*    advance           :: This shorthand is, depending on               */
  /*                         @FT_LOAD_IGNORE_TRANSFORM, the transformed    */
  /*                         (hinted) advance width for the glyph, in 26.6 */
  /*                         fractional pixel format.  As specified with   */
  /*                         @FT_LOAD_VERTICAL_LAYOUT, it uses either the  */
  /*                         `horiAdvance' or the `vertAdvance' value of   */
  /*                         `metrics' field.                              */
  /*                                                                       */
  /*    format            :: This field indicates the format of the image  */
  /*                         contained in the glyph slot.  Typically       */
  /*                         @FT_GLYPH_FORMAT_BITMAP,                      */
  /*                         @FT_GLYPH_FORMAT_OUTLINE, or                  */
  /*                         @FT_GLYPH_FORMAT_COMPOSITE, but others are    */
  /*                         possible.                                     */
  /*                                                                       */
  /*    bitmap            :: This field is used as a bitmap descriptor     */
  /*                         when the slot format is                       */
  /*                         @FT_GLYPH_FORMAT_BITMAP.  Note that the       */
  /*                         address and content of the bitmap buffer can  */
  /*                         change between calls of @FT_Load_Glyph and a  */
  /*                         few other functions.                          */
  /*                                                                       */
  /*    bitmap_left       :: The bitmap's left bearing expressed in        */
  /*                         integer pixels.  Only valid if the format is  */
  /*                         @FT_GLYPH_FORMAT_BITMAP, this is, if the      */
  /*                         glyph slot contains a bitmap.                 */
  /*                                                                       */
  /*    bitmap_top        :: The bitmap's top bearing expressed in integer */
  /*                         pixels.  Remember that this is the distance   */
  /*                         from the baseline to the top-most glyph       */
  /*                         scanline, upwards y~coordinates being         */
  /*                         *positive*.                                   */
  /*                                                                       */
  /*    outline           :: The outline descriptor for the current glyph  */
  /*                         image if its format is                        */
  /*                         @FT_GLYPH_FORMAT_OUTLINE.  Once a glyph is    */
  /*                         loaded, `outline' can be transformed,         */
  /*                         distorted, embolded, etc.  However, it must   */
  /*                         not be freed.                                 */
  /*                                                                       */
  /*    num_subglyphs     :: The number of subglyphs in a composite glyph. */
  /*                         This field is only valid for the composite    */
  /*                         glyph format that should normally only be     */
  /*                         loaded with the @FT_LOAD_NO_RECURSE flag.     */
  /*                                                                       */
  /*    subglyphs         :: An array of subglyph descriptors for          */
  /*                         composite glyphs.  There are `num_subglyphs'  */
  /*                         elements in there.  Currently internal to     */
  /*                         FreeType.                                     */
  /*                                                                       */
  /*    control_data      :: Certain font drivers can also return the      */
  /*                         control data for a given glyph image (e.g.    */
  /*                         TrueType bytecode, Type~1 charstrings, etc.). */
  /*                         This field is a pointer to such data.         */
  /*                                                                       */
  /*    control_len       :: This is the length in bytes of the control    */
  /*                         data.                                         */
  /*                                                                       */
  /*    other             :: Really wicked formats can use this pointer to */
  /*                         present their own glyph image to client       */
  /*                         applications.  Note that the application      */
  /*                         needs to know about the image format.         */
  /*                                                                       */
  /*    lsb_delta         :: The difference between hinted and unhinted    */
  /*                         left side bearing while autohinting is        */
  /*                         active.  Zero otherwise.                      */
  /*                                                                       */
  /*    rsb_delta         :: The difference between hinted and unhinted    */
  /*                         right side bearing while autohinting is       */
  /*                         active.  Zero otherwise.                      */
  /*                                                                       */
  /* <Note>                                                                */
  /*    If @FT_Load_Glyph is called with default flags (see                */
  /*    @FT_LOAD_DEFAULT) the glyph image is loaded in the glyph slot in   */
  /*    its native format (e.g., an outline glyph for TrueType and Type~1  */
  /*    formats).                                                          */
  /*                                                                       */
  /*    This image can later be converted into a bitmap by calling         */
  /*    @FT_Render_Glyph.  This function finds the current renderer for    */
  /*    the native image's format, then invokes it.                        */
  /*                                                                       */
  /*    The renderer is in charge of transforming the native image through */
  /*    the slot's face transformation fields, then converting it into a   */
  /*    bitmap that is returned in `slot->bitmap'.                         */
  /*                                                                       */
  /*    Note that `slot->bitmap_left' and `slot->bitmap_top' are also used */
  /*    to specify the position of the bitmap relative to the current pen  */
  /*    position (e.g., coordinates (0,0) on the baseline).  Of course,    */
  /*    `slot->format' is also changed to @FT_GLYPH_FORMAT_BITMAP.         */
  /*                                                                       */
  /* <Note>                                                                */
  /*    Here a small pseudo code fragment that shows how to use            */
  /*    `lsb_delta' and `rsb_delta':                                       */
  /*                                                                       */
  /*    {                                                                  */
  /*      FT_Pos  origin_x       = 0;                                      */
  /*      FT_Pos  prev_rsb_delta = 0;                                      */
  /*                                                                       */
  /*                                                                       */
  /*      for all glyphs do                                                */
  /*        <compute kern between current and previous glyph and add it to */
  /*         `origin_x'>                                                   */
  /*                                                                       */
  /*        <load glyph with `FT_Load_Glyph'>                              */
  /*                                                                       */
  /*        if ( prev_rsb_delta - face->glyph->lsb_delta >= 32 )           */
  /*          origin_x -= 64;                                              */
  /*        else if ( prev_rsb_delta - face->glyph->lsb_delta < -32 )      */
  /*          origin_x += 64;                                              */
  /*                                                                       */
  /*        prev_rsb_delta = face->glyph->rsb_delta;                       */
  /*                                                                       */
  /*        <save glyph image, or render glyph, or ...>                    */
  /*                                                                       */
  /*        origin_x += face->glyph->advance.x;                            */
  /*      endfor                                                           */
  /*    }                                                                  */
  /*                                                                       */
public class FT_GlyphSlot {
    public FT_Face           face;
    public final FT_Glyph_Metrics  metrics = new FT_Glyph_Metrics();

    public FT_Glyph_Format   format;

    public final FT_Bitmap         bitmap = new FT_Bitmap();

    public final FT_Outline        outline = new FT_Outline();
}
