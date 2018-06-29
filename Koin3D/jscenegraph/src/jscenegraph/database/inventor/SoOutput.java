/**
 * 
 */
package jscenegraph.database.inventor;

/**
 * @author Yves Boyadjian
 *
 */
public class SoOutput {
	
	public static final String defaultASCIIHeader =  "#Inventor V2.1 ascii";
	public static final String defaultBinaryHeader = "#Inventor V2.1 binary";
	    
	

////////////////////////////////////////////////////////////////////////
//
//Description:
//Return the default ASCII header string (ie the latest version
//of the standard Inventor ascii header)
//
//Use: public, static

    //! Returns the string representing the default ASCII header.
    public static String     getDefaultASCIIHeader() {
    	return defaultASCIIHeader;
    }

////////////////////////////////////////////////////////////////////////
//
//Description:
//Return the default Binary header string (ie the latest version
//of the standard Inventor binary header).  Note binary headers
//must always be padded for correct alignment in binary files.
//
//Use: public, static

    //! Returns the string representing the default binary header.
    public static String     getDefaultBinaryHeader() {
    	return SoOutput.padHeader(defaultBinaryHeader);
    }

	

    //! Pad a header so that it is correctly aligned for reading from
    //! binary files into memory
////////////////////////////////////////////////////////////////////////
//
//Description:
//Pad a string to a valid length for binary headers (ie one less
//than a multiple of 4)
//
//Use: private, static

    public static String     padHeader(final String str) {
        String paddedStr = new String(str);
        
        int pad = 3 - (str.length()%4);    
        for (int i = 0; i < pad; i++)
            paddedStr += " ";
            
        return (paddedStr);    	
    }

}
