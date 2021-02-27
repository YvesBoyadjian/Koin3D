
  /*************************************************************************/
  /*                                                                       */
  /* <Type>                                                                */
  /*    FT_ListNode                                                        */
  /*                                                                       */
  /* <Description>                                                         */
  /*     Many elements and objects in FreeType are listed through an       */
  /*     @FT_List record (see @FT_ListRec).  As its name suggests, an      */
  /*     FT_ListNode is a handle to a single list element.                 */
  /*                                                                       */
package org.freetype;

/**
 * @author Yves Boyadjian
 *
 */

  /*************************************************************************/
  /*                                                                       */
  /* <Struct>                                                              */
  /*    FT_ListNodeRec                                                     */
  /*                                                                       */
  /* <Description>                                                         */
  /*    A structure used to hold a single list element.                    */
  /*                                                                       */
  /* <Fields>                                                              */
  /*    prev :: The previous element in the list.  NULL if first.          */
  /*                                                                       */
  /*    next :: The next element in the list.  NULL if last.               */
  /*                                                                       */
  /*    data :: A typeless pointer to the listed object.                   */
  /*                                                                       */
public class FT_ListNode {
	public Object data;
}
