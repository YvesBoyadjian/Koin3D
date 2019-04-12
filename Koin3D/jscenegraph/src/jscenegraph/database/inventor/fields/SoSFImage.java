/*
 *
 *  Copyright (C) 2000 Silicon Graphics, Inc.  All Rights Reserved. 
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  Further, this software is distributed without any warranty that it is
 *  free of the rightful claim of any third person regarding infringement
 *  or the like.  Any license provided herein, whether implied or
 *  otherwise, applies only to this software file.  Patent licenses, if
 *  any, provided herein do not apply to combinations of this program with
 *  other software, or any other product whatsoever.
 * 
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Contact information: Silicon Graphics, Inc., 1600 Amphitheatre Pkwy,
 *  Mountain View, CA  94043, or:
 * 
 *  http://www.sgi.com 
 * 
 *  For further information regarding this notice, see: 
 * 
 *  http://oss.sgi.com/projects/GenInfo/NoticeExplan/
 *
 */


/*
 * Copyright (C) 1990,91   Silicon Graphics, Inc.
 *
 _______________________________________________________________________
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 |
 |   $Revision: 1.1.1.1 $
 |
 |   Classes:
 |      SoSFImage
 |
 |   Author(s)          : Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.fields;

import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.port.Util;


////////////////////////////////////////////////////////////////////////////////
//! Field containing a 2D image.
/*!
\class SoSFImage
\ingroup Fields
A field containing a two-dimensional image.  Images can be greyscale
(intensity), greyscale with transparency information, RGB, or RGB with
transparency.  Each component of the image (intensity, red, green,
blue or transparency (alpha)) can have an unsigned one-byte value from
0 to 255.


Values are returned as arrays of unsigned chars.  The image is stored
in this array starting at the bottom left corner of the image with the
intensity or red component of that pixel, followed by either the alpha,
the green and blue, or the green, blue and alpha components (depending
on the number of components in the image).  The next value is the
first component of the next pixel to the right.


SoSFImages are written to file as three integers representing the
width, height and number of components in the image, followed by
width*height hexadecimal values representing the pixels in the
image, separated by whitespace.  A one-component image will have
one-byte hexadecimal values representing the intensity of the image.
For example, 0xFF is full intensity, 0x00 is no intensity.  A two-component
image puts the intensity in the first (high) byte and the transparency
in the second (low) byte.  Pixels in a three-component image have the red
component in the first (high) byte, followed by the green and blue
components (so 0xFF0000 is red).  Four-component images put the
transparency byte after red/green/blue (so 0x0000FF80 is
semi-transparent blue).  Note:  each pixel is actually read as a single
unsigned number, so a 3-component pixel with value "0x0000FF" can also
be written as "0xFF" or "255" (decimal).


For example,
\code
1 2 1 0xFF 0x00
\endcode
is a 1 pixel wide by 2 pixel high greyscale image, with the bottom pixel 
white and the top pixel black.  And:
\code
2 4 3 0xFF0000 0xFF00   0 0   0 0   0xFFFFFF 0xFFFF00
\endcode
is a 2 pixel wide by 4 pixel high RGB image, with the bottom left pixel red, 
the bottom right pixel green, the two middle rows of pixels black, the top
left pixel white, and the top right pixel yellow.

\par See Also
\par
SoField, SoSField
*/
////////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoSFImage extends SoSField<Object> {
	
  private
    final SbVec2s             size = new SbVec2s();           //!< Width and height of image
    private int         numComponents;  //!< Number of components per pixel
    private byte[]     bytes;          //!< Array of pixels

	@Override
	protected Object constructor() {
		return null; // value not used
	}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoSFImage()
//
////////////////////////////////////////////////////////////////////////
{
    size.getValue()[0] = size.getValue()[1] = 0;
    numComponents = 0;
    bytes = null;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Destructor.
//
// Use: public

public void destructor()
//
////////////////////////////////////////////////////////////////////////
{
    if (bytes != null) { /*delete[] bytes;*/bytes = null; } // java port
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets value, given image dimensions and bytes...
//
// Use: public

public void
setValue(final SbVec2s s, int nc, byte[] b) {
	setValue(s,nc,b,false);
}
public void
setValue(final SbVec2s s, int nc, byte[] b, boolean dontCopy)
//
////////////////////////////////////////////////////////////////////////
{
    if (bytes != null) {
        //delete[] bytes; java port
        bytes = null;
    }

    size.copyFrom(s);
    numComponents = nc;
    
    int numBytes = size.getValue()[0]*size.getValue()[1]*numComponents;

    if (numBytes != 0) {
    	if(dontCopy) {
    		if(numBytes != b.length) {
    			throw new IllegalArgumentException("wrong array size");
    		}
    		bytes = b;
    	}
    	else {
    		bytes = new byte[numBytes];
    		Util.memcpy(bytes, b, numBytes);
    	}
    }
    else
        bytes = null;

    valueChanged();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Gets value, which is the image's dimensions and bytes.
//
// Use: public

public byte[] getValue(final SbVec2s s, final int[] nc)
//
////////////////////////////////////////////////////////////////////////
{
    evaluate();

    s.copyFrom(size);
    nc[0] = numComponents;
    
    return bytes;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Copy image from another field.
//
// Use: public

public SoSFImage operator_equal(final SoSFImage f)
//
////////////////////////////////////////////////////////////////////////
{
    final SbVec2s s = new SbVec2s();
    final int[] nc = new int[1];
    byte[] b = f.getValue(s, nc);
    setValue(s, nc[0], b);

    return this;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns true if field has same value as given field.
//
// Use: public

public boolean operator_equal_equal(
		final SoSFImage f)
//
////////////////////////////////////////////////////////////////////////
{
    // Check easy stuff first
    if ((!size.equals(f.size)) || numComponents != f.numComponents)
        return false;

    if (Util.memcmp(bytes, f.bytes, size.getValue()[0] * size.getValue()[1] * numComponents) != 0)
        return false;

    return true;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Edit the values of the image
//
// Use: public

public byte[] startEditing(final SbVec2s s, final int[] nc)
//
////////////////////////////////////////////////////////////////////////
{
    s.copyFrom(size);
    nc[0] = numComponents;
    return bytes;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Must be called sometime after editValue to let node notify.
//
// Use: public

public void finishEditing()
//
////////////////////////////////////////////////////////////////////////
{
    valueChanged();
}

	
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Reads value from file. Returns false on error.
//
// Use: private

public boolean readValue(SoInput in)
//
////////////////////////////////////////////////////////////////////////
{
    if (!in.read(size.getRef()[0])  ||
        !in.read(size.getRef()[1]) ||
        !in.read( (int val) -> numComponents = val))
        return false;
    
    //if (bytes != NULL) delete[] bytes; java port
    bytes = new byte[size.getValue()[0]*size.getValue()[1]*numComponents];

    int _byte = 0;
    if (in.isBinary()) {
        // Inventor version 2.1 and later binary file
        if (in.getIVVersion() > 2.0) {
            int numBytes = size.getValue()[0]*size.getValue()[1]*numComponents;
            if (! in.readBinaryArray(bytes, numBytes))
                return false;
        }

        // Pre version 2.1 Inventor binary files
        else {
            for (int i = 0; i < size.getValue()[0]*size.getValue()[1]; i++) {
                final int[] l = new int[1];

                if (!in.read(l)) return false;
                for (int j = 0; j < numComponents; j++) {
                    bytes[_byte++] = (byte)(
                        (l[0] >> (8*(numComponents-j-1))) & 0xFF);
                }
            }
        }
    }
    else {
        for (int i = 0; i < size.getValue()[0]*size.getValue()[1]; i++) {
            final int[] l = new int[1];
    
            if (!in.readHex(l)) return false;
            for (int j = 0; j < numComponents; j++) {
                bytes[_byte++] = (byte)(
                    (l[0] >> (8*(numComponents-j-1))) & 0xFF);
            }
        }
    }

    return true;
}

	
}
