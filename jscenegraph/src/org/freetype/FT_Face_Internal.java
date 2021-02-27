/**
 * 
 */
package org.freetype;

/**
 * @author Yves Boyadjian
 *
 */

  /*************************************************************************/
  /*                                                                       */
  /* <Struct>                                                              */
  /*    FT_Face_InternalRec                                                */
  /*                                                                       */
  /* <Description>                                                         */
  /*    This structure contains the internal fields of each FT_Face        */
  /*    object.  These fields may change between different releases of     */
  /*    FreeType.                                                          */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    max_points ::                                                      */
  /*      The maximum number of points used to store the vectorial outline */
  /*      of any glyph in this face.  If this value cannot be known in     */
  /*      advance, or if the face isn't scalable, this should be set to 0. */
  /*      Only relevant for scalable formats.                              */
  /*                                                                       */
  /*    max_contours ::                                                    */
  /*      The maximum number of contours used to store the vectorial       */
  /*      outline of any glyph in this face.  If this value cannot be      */
  /*      known in advance, or if the face isn't scalable, this should be  */
  /*      set to 0.  Only relevant for scalable formats.                   */
  /*                                                                       */
  /*    transform_matrix ::                                                */
  /*      A 2x2 matrix of 16.16 coefficients used to transform glyph       */
  /*      outlines after they are loaded from the font.  Only used by the  */
  /*      convenience functions.                                           */
  /*                                                                       */
  /*    transform_delta ::                                                 */
  /*      A translation vector used to transform glyph outlines after they */
  /*      are loaded from the font.  Only used by the convenience          */
  /*      functions.                                                       */
  /*                                                                       */
  /*    transform_flags ::                                                 */
  /*      Some flags used to classify the transform.  Only used by the     */
  /*      convenience functions.                                           */
  /*                                                                       */
  /*    services ::                                                        */
  /*      A cache for frequently used services.  It should be only         */
  /*      accessed with the macro `FT_FACE_LOOKUP_SERVICE'.                */
  /*                                                                       */
  /*    incremental_interface ::                                           */
  /*      If non-null, the interface through which glyph data and metrics  */
  /*      are loaded incrementally for faces that do not provide all of    */
  /*      this data when first opened.  This field exists only if          */
  /*      @FT_CONFIG_OPTION_INCREMENTAL is defined.                        */
  /*                                                                       */
  /*    ignore_unpatented_hinter ::                                        */
  /*      This boolean flag instructs the glyph loader to ignore the       */
  /*      native font hinter, if one is found.  This is exclusively used   */
  /*      in the case when the unpatented hinter is compiled within the    */
  /*      library.                                                         */
  /*                                                                       */
  /*    refcount ::                                                        */
  /*      A counter initialized to~1 at the time an @FT_Face structure is  */
  /*      created.  @FT_Reference_Face increments this counter, and        */
  /*      @FT_Done_Face only destroys a face if the counter is~1,          */
  /*      otherwise it simply decrements it.                               */
  /*                                                                       */
public class FT_Face_Internal {
	
    public final FT_Matrix           transform_matrix = new FT_Matrix();
    public final FT_Vector           transform_delta = new FT_Vector();

    public FT_Incremental_Interface  incremental_interface;

    public int              refcount;

}
