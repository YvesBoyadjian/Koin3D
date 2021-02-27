  /*************************************************************************/
  /*                                                                       */
  /* <Type>                                                                */
  /*    FT_CharMap                                                         */
  /*                                                                       */
  /* <Description>                                                         */
  /*    A handle to a given character map.  A charmap is used to translate */
  /*    character codes in a given encoding into glyph indexes for its     */
  /*    parent's face.  Some font formats may provide several charmaps per */
  /*    font.                                                              */
  /*                                                                       */
  /*    Each face object owns zero or more charmaps, but only one of them  */
  /*    can be `active' and used by @FT_Get_Char_Index or @FT_Load_Char.   */
  /*                                                                       */
  /*    The list of available charmaps in a face is available through the  */
  /*    `face->num_charmaps' and `face->charmaps' fields of @FT_FaceRec.   */
  /*                                                                       */
  /*    The currently active charmap is available as `face->charmap'.      */
  /*    You should call @FT_Set_Charmap to change it.                      */
  /*                                                                       */
  /* <Note>                                                                */
  /*    When a new face is created (either through @FT_New_Face or         */
  /*    @FT_Open_Face), the library looks for a Unicode charmap within     */
  /*    the list and automatically activates it.                           */
  /*                                                                       */
  /* <Also>                                                                */
  /*    See @FT_CharMapRec for the publicly accessible fields of a given   */
  /*    character map.                                                     */
  /*                                                                       */
package org.freetype;

/**
 * @author Yves Boyadjian
 *
 */
  /*************************************************************************/
  /*                                                                       */
  /* <Struct>                                                              */
  /*    FT_CharMapRec                                                      */
  /*                                                                       */
  /* <Description>                                                         */
  /*    The base charmap structure.                                        */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    face        :: A handle to the parent face object.                 */
  /*                                                                       */
  /*    encoding    :: An @FT_Encoding tag identifying the charmap.  Use   */
  /*                   this with @FT_Select_Charmap.                       */
  /*                                                                       */
  /*    platform_id :: An ID number describing the platform for the        */
  /*                   following encoding ID.  This comes directly from    */
  /*                   the TrueType specification and should be emulated   */
  /*                   for other formats.                                  */
  /*                                                                       */
  /*    encoding_id :: A platform specific encoding number.  This also     */
  /*                   comes from the TrueType specification and should be */
  /*                   emulated similarly.                                 */
  /*                                                                       */
public class FT_CharMap {

    FT_Face      face; // pointer
    public FT_Encoding  encoding;
    public short    platform_id;
    public short    encoding_id;

}
