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
  /*    FT_Parameter                                                       */
  /*                                                                       */
  /* <Description>                                                         */
  /*    A simple structure used to pass more or less generic parameters to */
  /*    @FT_Open_Face.                                                     */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    tag  :: A four-byte identification tag.                            */
  /*                                                                       */
  /*    data :: A pointer to the parameter data.                           */
  /*                                                                       */
  /* <Note>                                                                */
  /*    The ID and function of parameters are driver-specific.  See the    */
  /*    various FT_PARAM_TAG_XXX flags for more information.               */
  /*                                                                       */
public class FT_Parameter {

    public long    tag;
    public Object  data;

}
