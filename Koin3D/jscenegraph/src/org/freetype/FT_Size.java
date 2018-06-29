
  /*************************************************************************/
  /*                                                                       */
  /* <Type>                                                                */
  /*    FT_Size                                                            */
  /*                                                                       */
  /* <Description>                                                         */
  /*    A handle to an object used to model a face scaled to a given       */
  /*    character size.                                                    */
  /*                                                                       */
  /* <Note>                                                                */
  /*    Each @FT_Face has an _active_ @FT_Size object that is used by      */
  /*    functions like @FT_Load_Glyph to determine the scaling             */
  /*    transformation that in turn is used to load and hint glyphs and    */
  /*    metrics.                                                           */
  /*                                                                       */
  /*    You can use @FT_Set_Char_Size, @FT_Set_Pixel_Sizes,                */
  /*    @FT_Request_Size or even @FT_Select_Size to change the content     */
  /*    (i.e., the scaling values) of the active @FT_Size.                 */
  /*                                                                       */
  /*    You can use @FT_New_Size to create additional size objects for a   */
  /*    given @FT_Face, but they won't be used by other functions until    */
  /*    you activate it through @FT_Activate_Size.  Only one size can be   */
  /*    activated at any given time per face.                              */
  /*                                                                       */
  /* <Also>                                                                */
  /*    See @FT_SizeRec for the publicly accessible fields of a given size */
  /*    object.                                                            */
  /*                                                                       */
package org.freetype;

/**
 * @author Yves Boyadjian
 *
 */
  /*************************************************************************/
  /*                                                                       */
  /* <Struct>                                                              */
  /*    FT_SizeRec                                                         */
  /*                                                                       */
  /* <Description>                                                         */
  /*    FreeType root size class structure.  A size object models a face   */
  /*    object at a given size.                                            */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    face    :: Handle to the parent face object.                       */
  /*                                                                       */
  /*    generic :: A typeless pointer, unused by the FreeType library or   */
  /*               any of its drivers.  It can be used by client           */
  /*               applications to link their own data to each size        */
  /*               object.                                                 */
  /*                                                                       */
  /*    metrics :: Metrics for this size object.  This field is read-only. */
  /*                                                                       */
public class FT_Size {

    FT_Face           face;      /* parent face object              */
    FT_Generic        generic;   /* generic pointer for client uses */
    FT_Size_Metrics   metrics;   /* size metrics                    */
    FT_Size_Internal  internal;

}
