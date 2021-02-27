/**
 * 
 */
package org.freetype;

import java.util.stream.Stream;

/**
 * @author Yves Boyadjian
 *
 */
  /*************************************************************************/
  /*                                                                       */
  /* <Struct>                                                              */
  /*    FT_Open_Args                                                       */
  /*                                                                       */
  /* <Description>                                                         */
  /*    A structure used to indicate how to open a new font file or        */
  /*    stream.  A pointer to such a structure can be used as a parameter  */
  /*    for the functions @FT_Open_Face and @FT_Attach_Stream.             */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    flags       :: A set of bit flags indicating how to use the        */
  /*                   structure.                                          */
  /*                                                                       */
  /*    memory_base :: The first byte of the file in memory.               */
  /*                                                                       */
  /*    memory_size :: The size in bytes of the file in memory.            */
  /*                                                                       */
  /*    pathname    :: A pointer to an 8-bit file pathname.                */
  /*                                                                       */
  /*    stream      :: A handle to a source stream object.                 */
  /*                                                                       */
  /*    driver      :: This field is exclusively used by @FT_Open_Face;    */
  /*                   it simply specifies the font driver to use to open  */
  /*                   the face.  If set to~0, FreeType tries to load the  */
  /*                   face with each one of the drivers in its list.      */
  /*                                                                       */
  /*    num_params  :: The number of extra parameters.                     */
  /*                                                                       */
  /*    params      :: Extra parameters passed to the font driver when     */
  /*                   opening a new face.                                 */
  /*                                                                       */
  /* <Note>                                                                */
  /*    The stream type is determined by the contents of `flags' that      */
  /*    are tested in the following order by @FT_Open_Face:                */
  /*                                                                       */
  /*    If the @FT_OPEN_MEMORY bit is set, assume that this is a           */
  /*    memory file of `memory_size' bytes, located at `memory_address'.   */
  /*    The data are are not copied, and the client is responsible for     */
  /*    releasing and destroying them _after_ the corresponding call to    */
  /*    @FT_Done_Face.                                                     */
  /*                                                                       */
  /*    Otherwise, if the @FT_OPEN_STREAM bit is set, assume that a        */
  /*    custom input stream `stream' is used.                              */
  /*                                                                       */
  /*    Otherwise, if the @FT_OPEN_PATHNAME bit is set, assume that this   */
  /*    is a normal file and use `pathname' to open it.                    */
  /*                                                                       */
  /*    If the @FT_OPEN_DRIVER bit is set, @FT_Open_Face only tries to     */
  /*    open the file with the driver whose handler is in `driver'.        */
  /*                                                                       */
  /*    If the @FT_OPEN_PARAMS bit is set, the parameters given by         */
  /*    `num_params' and `params' is used.  They are ignored otherwise.    */
  /*                                                                       */
  /*    Ideally, both the `pathname' and `params' fields should be tagged  */
  /*    as `const'; this is missing for API backwards compatibility.  In   */
  /*    other words, applications should treat them as read-only.          */
  /*                                                                       */
public class FT_Open_Args {
    public int         flags;
    byte[]  memory_base;
    long         memory_size;
    public String      pathname;
    public Stream       stream;
    FT_Module       driver; // pointer
    int          num_params;
    FT_Parameter   params;

}
