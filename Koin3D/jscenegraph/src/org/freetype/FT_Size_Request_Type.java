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
/*    FT_Size_Request_Type                                               */
/*                                                                       */
/* <Description>                                                         */
/*    An enumeration type that lists the supported size request types.   */
/*                                                                       */
/* <Values>                                                              */
/*    FT_SIZE_REQUEST_TYPE_NOMINAL ::                                    */
/*      The nominal size.  The `units_per_EM' field of @FT_FaceRec is    */
/*      used to determine both scaling values.                           */
/*                                                                       */
/*    FT_SIZE_REQUEST_TYPE_REAL_DIM ::                                   */
/*      The real dimension.  The sum of the the `ascender' and (minus    */
/*      of) the `descender' fields of @FT_FaceRec are used to determine  */
/*      both scaling values.                                             */
/*                                                                       */
/*    FT_SIZE_REQUEST_TYPE_BBOX ::                                       */
/*      The font bounding box.  The width and height of the `bbox' field */
/*      of @FT_FaceRec are used to determine the horizontal and vertical */
/*      scaling value, respectively.                                     */
/*                                                                       */
/*    FT_SIZE_REQUEST_TYPE_CELL ::                                       */
/*      The `max_advance_width' field of @FT_FaceRec is used to          */
/*      determine the horizontal scaling value; the vertical scaling     */
/*      value is determined the same way as                              */
/*      @FT_SIZE_REQUEST_TYPE_REAL_DIM does.  Finally, both scaling      */
/*      values are set to the smaller one.  This type is useful if you   */
/*      want to specify the font size for, say, a window of a given      */
/*      dimension and 80x24 cells.                                       */
/*                                                                       */
/*    FT_SIZE_REQUEST_TYPE_SCALES ::                                     */
/*      Specify the scaling values directly.                             */
/*                                                                       */
/* <Note>                                                                */
/*    The above descriptions only apply to scalable formats.  For bitmap */
/*    formats, the behaviour is up to the driver.                        */
/*                                                                       */
/*    See the note section of @FT_Size_Metrics if you wonder how size    */
/*    requesting relates to scaling values.                              */
/*                                                                       */
public enum FT_Size_Request_Type {
    FT_SIZE_REQUEST_TYPE_NOMINAL,
    FT_SIZE_REQUEST_TYPE_REAL_DIM,
    FT_SIZE_REQUEST_TYPE_BBOX,
    FT_SIZE_REQUEST_TYPE_CELL,
    FT_SIZE_REQUEST_TYPE_SCALES,

    FT_SIZE_REQUEST_TYPE_MAX

}
