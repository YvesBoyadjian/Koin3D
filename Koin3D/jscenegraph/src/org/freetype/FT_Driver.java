/**
 * 
 */
package org.freetype;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yves Boyadjian
 *
 */

  /*************************************************************************/
  /*                                                                       */
  /* <Struct>                                                              */
  /*    FT_DriverRec                                                       */
  /*                                                                       */
  /* <Description>                                                         */
  /*    The root font driver class.  A font driver is responsible for      */
  /*    managing and loading font files of a given format.                 */
  /*                                                                       */
  /*  <Fields>                                                             */
  /*     root         :: Contains the fields of the root module class.     */
  /*                                                                       */
  /*     clazz        :: A pointer to the font driver's class.  Note that  */
  /*                     this is NOT root.clazz.  `class' wasn't used      */
  /*                     as it is a reserved word in C++.                  */
  /*                                                                       */
  /*     faces_list   :: The list of faces currently opened by this        */
  /*                     driver.                                           */
  /*                                                                       */
  /*     glyph_loader :: The glyph loader for all faces managed by this    */
  /*                     driver.  This object isn't defined for unscalable */
  /*                     formats.                                          */
  /*                                                                       */
public class FT_Driver {

    public final FT_Module     root = new FT_Module();
    public FT_Driver_Class  clazz;
    public final List       faces_list = new ArrayList();
}
