/**
 * 
 */
package org.freetype;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

import jscenegraph.port.TT_Face;

/**
 * @author Yves Boyadjian
 *
 */
public class FT_Driver_Class {

	public int init_face( InputStream stream,
            FT_Face        ttface,      /* TT_Face */
            int         face_index,
            int         num_params,
            FT_Parameter[]  params ) {
		int error;
		FT_Library library;
		TT_Face face = (TT_Face)ttface;
		
		library = ttface.driver.root.library;
		
		try {
			face.font = Font.createFont(Font.TRUETYPE_FONT, stream);
		} catch (FontFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		
		return 0;
	}

}
