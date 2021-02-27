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
  /*    FT_Outline                                                         */
  /*                                                                       */
  /* <Description>                                                         */
  /*    This structure is used to describe an outline to the scan-line     */
  /*    converter.                                                         */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    n_contours :: The number of contours in the outline.               */
  /*                                                                       */
  /*    n_points   :: The number of points in the outline.                 */
  /*                                                                       */
  /*    points     :: A pointer to an array of `n_points' @FT_Vector       */
  /*                  elements, giving the outline's point coordinates.    */
  /*                                                                       */
  /*    tags       :: A pointer to an array of `n_points' chars, giving    */
  /*                  each outline point's type.                           */
  /*                                                                       */
  /*                  If bit~0 is unset, the point is `off' the curve,     */
  /*                  i.e., a Bézier control point, while it is `on' if    */
  /*                  set.                                                 */
  /*                                                                       */
  /*                  Bit~1 is meaningful for `off' points only.  If set,  */
  /*                  it indicates a third-order Bézier arc control point; */
  /*                  and a second-order control point if unset.           */
  /*                                                                       */
  /*                  If bit~2 is set, bits 5-7 contain the drop-out mode  */
  /*                  (as defined in the OpenType specification; the value */
  /*                  is the same as the argument to the SCANMODE          */
  /*                  instruction).                                        */
  /*                                                                       */
  /*                  Bits 3 and~4 are reserved for internal purposes.     */
  /*                                                                       */
  /*    contours   :: An array of `n_contours' shorts, giving the end      */
  /*                  point of each contour within the outline.  For       */
  /*                  example, the first contour is defined by the points  */
  /*                  `0' to `contours[0]', the second one is defined by   */
  /*                  the points `contours[0]+1' to `contours[1]', etc.    */
  /*                                                                       */
  /*    flags      :: A set of bit flags used to characterize the outline  */
  /*                  and give hints to the scan-converter and hinter on   */
  /*                  how to convert/grid-fit it.  See @FT_OUTLINE_XXX.    */
  /*                                                                       */
  /* <Note>                                                                */
  /*    The B/W rasterizer only checks bit~2 in the `tags' array for the   */
  /*    first point of each contour.  The drop-out mode as given with      */
  /*    @FT_OUTLINE_IGNORE_DROPOUTS, @FT_OUTLINE_SMART_DROPOUTS, and       */
  /*    @FT_OUTLINE_INCLUDE_STUBS in `flags' is then overridden.           */
  /*                                                                       */
public class FT_Outline {

    short       n_contours;      /* number of contours in glyph        */
    public short       n_points;        /* number of points in the glyph      */

    public FT_Vector[]  points;          /* the outline's points               */
    char[]       tags;            /* the points flags                   */
    short[]      contours;        /* the contour end points             */

    public int         flags;           /* outline masks                      */

}
