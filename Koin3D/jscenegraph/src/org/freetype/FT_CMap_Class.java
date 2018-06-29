/**
 * 
 */
package org.freetype;

/**
 * @author Yves Boyadjian
 *
 */
public class FT_CMap_Class {
	
	public interface FT_CMap_CharIndexFunc {
		public int run(FT_CMap cmap, int char_code);
	}
	
	//TODO
    FT_CMap_CharIndexFunc  char_index;
}
