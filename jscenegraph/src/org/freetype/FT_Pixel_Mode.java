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
  /* <Enum>                                                                */
  /*    FT_Pixel_Mode                                                      */
  /*                                                                       */
  /* <Description>                                                         */
  /*    An enumeration type used to describe the format of pixels in a     */
  /*    given bitmap.  Note that additional formats may be added in the    */
  /*    future.                                                            */
  /*                                                                       */
  /* <Values>                                                              */
  /*    FT_PIXEL_MODE_NONE ::                                              */
  /*      Value~0 is reserved.                                             */
  /*                                                                       */
  /*    FT_PIXEL_MODE_MONO ::                                              */
  /*      A monochrome bitmap, using 1~bit per pixel.  Note that pixels    */
  /*      are stored in most-significant order (MSB), which means that     */
  /*      the left-most pixel in a byte has value 128.                     */
  /*                                                                       */
  /*    FT_PIXEL_MODE_GRAY ::                                              */
  /*      An 8-bit bitmap, generally used to represent anti-aliased glyph  */
  /*      images.  Each pixel is stored in one byte.  Note that the number */
  /*      of `gray' levels is stored in the `num_grays' field of the       */
  /*      @FT_Bitmap structure (it generally is 256).                      */
  /*                                                                       */
  /*    FT_PIXEL_MODE_GRAY2 ::                                             */
  /*      A 2-bit per pixel bitmap, used to represent embedded             */
  /*      anti-aliased bitmaps in font files according to the OpenType     */
  /*      specification.  We haven't found a single font using this        */
  /*      format, however.                                                 */
  /*                                                                       */
  /*    FT_PIXEL_MODE_GRAY4 ::                                             */
  /*      A 4-bit per pixel bitmap, representing embedded anti-aliased     */
  /*      bitmaps in font files according to the OpenType specification.   */
  /*      We haven't found a single font using this format, however.       */
  /*                                                                       */
  /*    FT_PIXEL_MODE_LCD ::                                               */
  /*      An 8-bit bitmap, representing RGB or BGR decimated glyph images  */
  /*      used for display on LCD displays; the bitmap is three times      */
  /*      wider than the original glyph image.  See also                   */
  /*      @FT_RENDER_MODE_LCD.                                             */
  /*                                                                       */
  /*    FT_PIXEL_MODE_LCD_V ::                                             */
  /*      An 8-bit bitmap, representing RGB or BGR decimated glyph images  */
  /*      used for display on rotated LCD displays; the bitmap is three    */
  /*      times taller than the original glyph image.  See also            */
  /*      @FT_RENDER_MODE_LCD_V.                                           */
  /*                                                                       */
  /*    FT_PIXEL_MODE_BGRA ::                                              */
  /*      An image with four 8-bit channels per pixel, representing a      */
  /*      color image (such as emoticons) with alpha channel.  For each    */
  /*      pixel, the format is BGRA, which means, the blue channel comes   */
  /*      first in memory.  The color channels are pre-multiplied and in   */
  /*      the sRGB colorspace.  For example, full red at half-translucent  */
  /*      opacity will be represented as `00,00,80,80', not `00,00,FF,80'. */
  /*      See also @FT_LOAD_COLOR.                                         */
  /*                                                                       */
public enum FT_Pixel_Mode {

    FT_PIXEL_MODE_NONE,
    FT_PIXEL_MODE_MONO,
    FT_PIXEL_MODE_GRAY,
    FT_PIXEL_MODE_GRAY2,
    FT_PIXEL_MODE_GRAY4,
    FT_PIXEL_MODE_LCD,
    FT_PIXEL_MODE_LCD_V,
    FT_PIXEL_MODE_BGRA,

    FT_PIXEL_MODE_MAX      /* do not remove */

}
