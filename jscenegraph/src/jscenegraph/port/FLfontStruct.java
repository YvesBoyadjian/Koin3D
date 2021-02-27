/**
 * 
 */
package jscenegraph.port;

/**
 * @author Yves Boyadjian
 *
 */
public class FLfontStruct {

    public int       fn;      /* font number (handle) */
    int    direction;      /* hint about direction the font is painted */
    int    min_char_or_byte2;/* first character */
    int    max_char_or_byte2;/* last character */
    int    min_byte1;      /* first row that exists */
    int    max_byte1;      /* last row that exists */
    boolean         all_chars_exist;/* flag if all characters have non-zero size*/
    int    default_char;   /* char to print for undefined character */
    int         n_properties;   /* how many properties there are */
    FLFontProp[]  properties;    /* pointer to array of additional properties*/
    final FLCharStruct min_bounds = new FLCharStruct();    /* minimum bounds over all existing char*/
    final FLCharStruct max_bounds = new FLCharStruct();    /* maximum bounds over all existing char*/
    FLCharStruct per_char;     /* first_char to last_char information */
    int         ascent;         /* log. extent above baseline for spacing */
    int         descent;        /* log. descent below baseline for spacing */
}
