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
/*    FT_Size_RequestRec                                                 */
/*                                                                       */
/* <Description>                                                         */
/*    A structure used to model a size request.                          */
/*                                                                       */
/* <Fields>                                                              */
/*    type           :: See @FT_Size_Request_Type.                       */
/*                                                                       */
/*    width          :: The desired width.                               */
/*                                                                       */
/*    height         :: The desired height.                              */
/*                                                                       */
/*    horiResolution :: The horizontal resolution.  If set to zero,      */
/*                      `width' is treated as a 26.6 fractional pixel    */
/*                      value.                                           */
/*                                                                       */
/*    vertResolution :: The vertical resolution.  If set to zero,        */
/*                      `height' is treated as a 26.6 fractional pixel   */
/*                      value.                                           */
/*                                                                       */
/* <Note>                                                                */
/*    If `width' is zero, then the horizontal scaling value is set equal */
/*    to the vertical scaling value, and vice versa.                     */
/*                                                                       */
public class FT_Size_Request {

    public FT_Size_Request_Type  type;
    public long               width;
    public long               height;
    public int               horiResolution;
    public int               vertResolution;

}
