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
/*    FT_Bitmap                                                          */
/*                                                                       */
/* <Description>                                                         */
/*    A structure used to describe a bitmap or pixmap to the raster.     */
/*    Note that we now manage pixmaps of various depths through the      */
/*    `pixel_mode' field.                                                */
/*                                                                       */
/* <Fields>                                                              */
/*    rows         :: The number of bitmap rows.                         */
/*                                                                       */
/*    width        :: The number of pixels in bitmap row.                */
/*                                                                       */
/*    pitch        :: The pitch's absolute value is the number of bytes  */
/*                    taken by one bitmap row, including padding.        */
/*                    However, the pitch is positive when the bitmap has */
/*                    a `down' flow, and negative when it has an `up'    */
/*                    flow.  In all cases, the pitch is an offset to add */
/*                    to a bitmap pointer in order to go down one row.   */
/*                                                                       */
/*                    Note that `padding' means the alignment of a       */
/*                    bitmap to a byte border, and FreeType functions    */
/*                    normally align to the smallest possible integer    */
/*                    value.                                             */
/*                                                                       */
/*                    For the B/W rasterizer, `pitch' is always an even  */
/*                    number.                                            */
/*                                                                       */
/*                    To change the pitch of a bitmap (say, to make it a */
/*                    multiple of 4), use @FT_Bitmap_Convert.            */
/*                    Alternatively, you might use callback functions to */
/*                    directly render to the application's surface; see  */
/*                    the file `example2.cpp' in the tutorial for a      */
/*                    demonstration.                                     */
/*                                                                       */
/*    buffer       :: A typeless pointer to the bitmap buffer.  This     */
/*                    value should be aligned on 32-bit boundaries in    */
/*                    most cases.                                        */
/*                                                                       */
/*    num_grays    :: This field is only used with                       */
/*                    @FT_PIXEL_MODE_GRAY; it gives the number of gray   */
/*                    levels used in the bitmap.                         */
/*                                                                       */
/*    pixel_mode   :: The pixel mode, i.e., how pixel bits are stored.   */
/*                    See @FT_Pixel_Mode for possible values.            */
/*                                                                       */
/*    palette_mode :: This field is intended for paletted pixel modes;   */
/*                    it indicates how the palette is stored.  Not       */
/*                    used currently.                                    */
/*                                                                       */
/*    palette      :: A typeless pointer to the bitmap palette; this     */
/*                    field is intended for paletted pixel modes.  Not   */
/*                    used currently.                                    */
/*                                                                       */
/* <Note>                                                                */
/*   For now, the only pixel modes supported by FreeType are mono and    */
/*   grays.  However, drivers might be added in the future to support    */
/*   more `colorful' options.                                            */
/*                                                                       */
public class FT_Bitmap {

    public int    rows;
    public int    width;
    public int             pitch;
    public byte[]  buffer;
    public short  num_grays;
    public FT_Pixel_Mode   pixel_mode;
    public char   palette_mode;
    public Object          palette;

}
