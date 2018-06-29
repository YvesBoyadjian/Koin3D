/**
 * 
 */
package org.freetype;

import jscenegraph.port.fl;

/**
 * @author Yves Boyadjian
 *
 */
public enum FT_Encoding {

	FT_ENCODING_NONE(fl.FT_ENC_TAG((char)0,(char)0,(char)0,(char)0)),
    FT_ENCODING_MS_SYMBOL(fl.FT_ENC_TAG( 's', 'y', 'm', 'b' )),
    FT_ENCODING_UNICODE(fl.FT_ENC_TAG( 'u', 'n', 'i', 'c' ));
	
	private int value;
	FT_Encoding(int value) {
		this.value = value;
	}
	public int getValue() {
		return value;
	}
}
