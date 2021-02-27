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
  /* <Type>                                                                */
  /*    FT_Module                                                          */
  /*                                                                       */
  /* <Description>                                                         */
  /*    A handle to a given FreeType module object.  Each module can be a  */
  /*    font driver, a renderer, or anything else that provides services   */
  /*    to the formers.                                                    */
  /*                                                                       */
public class FT_Module {

    FT_Library        library;
    public FT_Memory         memory;
}
