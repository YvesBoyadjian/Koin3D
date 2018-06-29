
  /*************************************************************************/
  /*                                                                       */
  /* <Type>                                                                */
  /*    TT_Face                                                            */
  /*                                                                       */
  /* <Description>                                                         */
  /*    A handle to a TrueType face/font object.  A TT_Face encapsulates   */
  /*    the resolution and scaling independent parts of a TrueType font    */
  /*    resource.                                                          */
  /*                                                                       */
  /* <Note>                                                                */
  /*    The TT_Face structure is also used as a `parent class' for the     */
  /*    OpenType-CFF class (T2_Face).                                      */
  /*                                                                       */
package jscenegraph.port;

import java.awt.Font;

import org.freetype.FT_Face;

/**
 * @author Yves Boyadjian
 *
 */

  /*************************************************************************/
  /*                                                                       */
  /*                         TrueType Face Type                            */
  /*                                                                       */
  /* <Struct>                                                              */
  /*    TT_Face                                                            */
  /*                                                                       */
  /* <Description>                                                         */
  /*    The TrueType face class.  These objects model the resolution and   */
  /*    point-size independent data found in a TrueType font file.         */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    root                 :: The base FT_Face structure, managed by the */
  /*                            base layer.                                */
  /*                                                                       */
  /*    ttc_header           :: The TrueType collection header, used when  */
  /*                            the file is a `ttc' rather than a `ttf'.   */
  /*                            For ordinary font files, the field         */
  /*                            `ttc_header.count' is set to 0.            */
  /*                                                                       */
  /*    format_tag           :: The font format tag.                       */
  /*                                                                       */
  /*    num_tables           :: The number of TrueType tables in this font */
  /*                            file.                                      */
  /*                                                                       */
  /*    dir_tables           :: The directory of TrueType tables for this  */
  /*                            font file.                                 */
  /*                                                                       */
  /*    header               :: The font's font header (`head' table).     */
  /*                            Read on font opening.                      */
  /*                                                                       */
  /*    horizontal           :: The font's horizontal header (`hhea'       */
  /*                            table).  This field also contains the      */
  /*                            associated horizontal metrics table        */
  /*                            (`hmtx').                                  */
  /*                                                                       */
  /*    max_profile          :: The font's maximum profile table.  Read on */
  /*                            font opening.  Note that some maximum      */
  /*                            values cannot be taken directly from this  */
  /*                            table.  We thus define additional fields   */
  /*                            below to hold the computed maxima.         */
  /*                                                                       */
  /*    vertical_info        :: A boolean which is set when the font file  */
  /*                            contains vertical metrics.  If not, the    */
  /*                            value of the `vertical' field is           */
  /*                            undefined.                                 */
  /*                                                                       */
  /*    vertical             :: The font's vertical header (`vhea' table). */
  /*                            This field also contains the associated    */
  /*                            vertical metrics table (`vmtx'), if found. */
  /*                            IMPORTANT: The contents of this field is   */
  /*                            undefined if the `verticalInfo' field is   */
  /*                            unset.                                     */
  /*                                                                       */
  /*    num_names            :: The number of name records within this     */
  /*                            TrueType font.                             */
  /*                                                                       */
  /*    name_table           :: The table of name records (`name').        */
  /*                                                                       */
  /*    os2                  :: The font's OS/2 table (`OS/2').            */
  /*                                                                       */
  /*    postscript           :: The font's PostScript table (`post'        */
  /*                            table).  The PostScript glyph names are    */
  /*                            not loaded by the driver on face opening.  */
  /*                            See the `ttpost' module for more details.  */
  /*                                                                       */
  /*    cmap_table           :: Address of the face's `cmap' SFNT table    */
  /*                            in memory (it's an extracted frame).       */
  /*                                                                       */
  /*    cmap_size            :: The size in bytes of the `cmap_table'      */
  /*                            described above.                           */
  /*                                                                       */
  /*    goto_table           :: A function called by each TrueType table   */
  /*                            loader to position a stream's cursor to    */
  /*                            the start of a given table according to    */
  /*                            its tag.  It defaults to TT_Goto_Face but  */
  /*                            can be different for strange formats (e.g. */
  /*                            Type 42).                                  */
  /*                                                                       */
  /*    access_glyph_frame   :: A function used to access the frame of a   */
  /*                            given glyph within the face's font file.   */
  /*                                                                       */
  /*    forget_glyph_frame   :: A function used to forget the frame of a   */
  /*                            given glyph when all data has been loaded. */
  /*                                                                       */
  /*    read_glyph_header    :: A function used to read a glyph header.    */
  /*                            It must be called between an `access' and  */
  /*                            `forget'.                                  */
  /*                                                                       */
  /*    read_simple_glyph    :: A function used to read a simple glyph.    */
  /*                            It must be called after the header was     */
  /*                            read, and before the `forget'.             */
  /*                                                                       */
  /*    read_composite_glyph :: A function used to read a composite glyph. */
  /*                            It must be called after the header was     */
  /*                            read, and before the `forget'.             */
  /*                                                                       */
  /*    sfnt                 :: A pointer to the SFNT service.             */
  /*                                                                       */
  /*    psnames              :: A pointer to the PostScript names service. */
  /*                                                                       */
  /*    hdmx                 :: The face's horizontal device metrics       */
  /*                            (`hdmx' table).  This table is optional in */
  /*                            TrueType/OpenType fonts.                   */
  /*                                                                       */
  /*    gasp                 :: The grid-fitting and scaling properties    */
  /*                            table (`gasp').  This table is optional in */
  /*                            TrueType/OpenType fonts.                   */
  /*                                                                       */
  /*    pclt                 :: The `pclt' SFNT table.                     */
  /*                                                                       */
  /*    num_sbit_scales      :: The number of sbit scales for this font.   */
  /*                                                                       */
  /*    sbit_scales          :: Array of sbit scales embedded in this      */
  /*                            font.  This table is optional in a         */
  /*                            TrueType/OpenType font.                    */
  /*                                                                       */
  /*    postscript_names     :: A table used to store the Postscript names */
  /*                            of  the glyphs for this font.  See the     */
  /*                            file  `ttconfig.h' for comments on the     */
  /*                            TT_CONFIG_OPTION_POSTSCRIPT_NAMES option.  */
  /*                                                                       */
  /*    num_locations        :: The number of glyph locations in this      */
  /*                            TrueType file.  This should be             */
  /*                            identical to the number of glyphs.         */
  /*                            Ignored for Type 2 fonts.                  */
  /*                                                                       */
  /*    glyph_locations      :: An array of longs.  These are offsets to   */
  /*                            glyph data within the `glyf' table.        */
  /*                            Ignored for Type 2 font faces.             */
  /*                                                                       */
  /*    glyf_len             :: The length of the `glyf' table.  Needed    */
  /*                            for malformed `loca' tables.               */
  /*                                                                       */
  /*    font_program_size    :: Size in bytecodes of the face's font       */
  /*                            program.  0 if none defined.  Ignored for  */
  /*                            Type 2 fonts.                              */
  /*                                                                       */
  /*    font_program         :: The face's font program (bytecode stream)  */
  /*                            executed at load time, also used during    */
  /*                            glyph rendering.  Comes from the `fpgm'    */
  /*                            table.  Ignored for Type 2 font fonts.     */
  /*                                                                       */
  /*    cvt_program_size     :: The size in bytecodes of the face's cvt    */
  /*                            program.  Ignored for Type 2 fonts.        */
  /*                                                                       */
  /*    cvt_program          :: The face's cvt program (bytecode stream)   */
  /*                            executed each time an instance/size is     */
  /*                            changed/reset.  Comes from the `prep'      */
  /*                            table.  Ignored for Type 2 fonts.          */
  /*                                                                       */
  /*    cvt_size             :: Size of the control value table (in        */
  /*                            entries).   Ignored for Type 2 fonts.      */
  /*                                                                       */
  /*    cvt                  :: The face's original control value table.   */
  /*                            Coordinates are expressed in unscaled font */
  /*                            units.  Comes from the `cvt ' table.       */
  /*                            Ignored for Type 2 fonts.                  */
  /*                                                                       */
  /*    num_kern_pairs       :: The number of kerning pairs present in the */
  /*                            font file.  The engine only loads the      */
  /*                            first horizontal format 0 kern table it    */
  /*                            finds in the font file.  Ignored for       */
  /*                            Type 2 fonts.                              */
  /*                                                                       */
  /*    kern_table_index     :: The index of the kerning table in the font */
  /*                            kerning directory.  Ignored for Type 2     */
  /*                            fonts.                                     */
  /*                                                                       */
  /*    interpreter          :: A pointer to the TrueType bytecode         */
  /*                            interpreters field is also used to hook    */
  /*                            the debugger in `ttdebug'.                 */
  /*                                                                       */
  /*    unpatented_hinting   :: If true, use only unpatented methods in    */
  /*                            the bytecode interpreter.                  */
  /*                                                                       */
  /*    doblend              :: A boolean which is set if the font should  */
  /*                            be blended (this is for GX var).           */
  /*                                                                       */
  /*    blend                :: Contains the data needed to control GX     */
  /*                            variation tables (rather like Multiple     */
  /*                            Master data).                              */
  /*                                                                       */
  /*    extra                :: Reserved for third-party font drivers.     */
  /*                                                                       */
  /*    postscript_name      :: The PS name of the font.  Used by the      */
  /*                            postscript name service.                   */
  /*                                                                       */
public class TT_Face extends FT_Face {
	public Font font;
	public float size;
}
