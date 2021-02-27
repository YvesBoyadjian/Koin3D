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
  /*    FT_Glyph_Metrics                                                   */
  /*                                                                       */
  /* <Description>                                                         */
  /*    A structure used to model the metrics of a single glyph.  The      */
  /*    values are expressed in 26.6 fractional pixel format; if the flag  */
  /*    @FT_LOAD_NO_SCALE has been used while loading the glyph, values    */
  /*    are expressed in font units instead.                               */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    width ::                                                           */
  /*      The glyph's width.                                               */
  /*                                                                       */
  /*    height ::                                                          */
  /*      The glyph's height.                                              */
  /*                                                                       */
  /*    horiBearingX ::                                                    */
  /*      Left side bearing for horizontal layout.                         */
  /*                                                                       */
  /*    horiBearingY ::                                                    */
  /*      Top side bearing for horizontal layout.                          */
  /*                                                                       */
  /*    horiAdvance ::                                                     */
  /*      Advance width for horizontal layout.                             */
  /*                                                                       */
  /*    vertBearingX ::                                                    */
  /*      Left side bearing for vertical layout.                           */
  /*                                                                       */
  /*    vertBearingY ::                                                    */
  /*      Top side bearing for vertical layout.  Larger positive values    */
  /*      mean further below the vertical glyph origin.                    */
  /*                                                                       */
  /*    vertAdvance ::                                                     */
  /*      Advance height for vertical layout.  Positive values mean the    */
  /*      glyph has a positive advance downward.                           */
  /*                                                                       */
  /* <Note>                                                                */
  /*    If not disabled with @FT_LOAD_NO_HINTING, the values represent     */
  /*    dimensions of the hinted glyph (in case hinting is applicable).    */
  /*                                                                       */
  /*    Stroking a glyph with an outside border does not increase          */
  /*    `horiAdvance' or `vertAdvance'; you have to manually adjust these  */
  /*    values to account for the added width and height.                  */
  /*                                                                       */
public class FT_Glyph_Metrics {

    public int  width;
    public int  height;

    public int  horiBearingX;
    public int  horiBearingY;
    public int  horiAdvance;

    int  vertBearingX;
    int  vertBearingY;
    int  vertAdvance;

}
