/**
 * 
 */
package jscenegraph.coin3d.inventor.fields;

import jscenegraph.coin3d.inventor.SbImage;
import jscenegraph.database.inventor.SbVec3s;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.errors.SoReadError;
import jscenegraph.database.inventor.fields.SoSField;

/**
 * @author Yves Boyadjian
 *
 */
public class SoSFImage3 extends SoSField<SbImage> {

	@Override
	protected SbImage constructor() {
		return new SbImage();
	}

	@Override
	public boolean readValue(SoInput in) {
  final SbVec3s size = new SbVec3s();
  final int[] nc = new int[1];
  short[] s1 = new short[1];
  short[] s2 = new short[1];
  short[] s3 = new short[1];
  if (!in.read(/*size[0]*/s1) || !in.read(/*size[1]*/s2) || !in.read(/*size[2]*/s3) ||
      !in.read(nc)) {
    SoReadError.post(in, "Premature end of file reading images dimensions");
    return false;
  }
  size.getValue()[0] = s1[0];
  size.getValue()[1] = s2[0];
  size.getValue()[2] = s3[0];

  // Note: empty images (dimensions 0x0x0) are allowed.

  if (size.getValue()[0] < 0 || size.getValue()[1] < 0 || size.getValue()[2] < 0 || nc[0] < 0 || nc[0] > 4) {
    SoReadError.post(in, "Invalid image specification %dx%dx%dx%d"+size.getValue()[0]+"x"+size.getValue()[1]+"x"+size.getValue()[2]+"x"+ nc);
    return false;
  }

  int buffersize = (int)(size.getValue()[0]) * (int)(size.getValue()[1]) * (int)(size.getValue()[2]) * nc[0];

  if (buffersize == 0 &&
      (size.getValue()[0] != 0 || size.getValue()[1] != 0 || size.getValue()[2] != 0 || nc[0] != 0)) {
    SoReadError.post(in, "Invalid image specification "+size.getValue()[0]+"x"+size.getValue()[1]+"x"+size.getValue()[2]+"x"+ nc);
    return false;
  }

//#if COIN_DEBUG && 0 // debug
//  SoDebugError.postInfo("SoSFImage3::readValue",
//                         "image dimensions: %dx%dx%dx%d",
//                         size[0], size[1], size[2], nc);
//#endif // debug

  if (buffersize == 0) {
    value.setValue(new SbVec3s((short)0,(short)0,(short)0), 0, null);
    return true;
  }

  // allocate image data and get new pointer back
  value.setValue(size, nc[0], null);
  byte[] pixblock = value.getValue(size, nc);

  // The binary image format of 2.1 and later tries to be less
  // wasteful when storing images.
  if (in.isBinary() && in.getIVVersion() >= 2.1f) {
    if (!in.readBinaryArray(pixblock, buffersize)) {
      SoReadError.post(in, "Premature end of file reading images data");
      return false;
    }
  }
  else {
    int byte_ = 0;
    int numpixels = (int)(size.getValue()[0]) * (int)(size.getValue()[1]) * (int)(size.getValue()[2]);
    for (int i = 0; i < numpixels; i++) {
      final int[] l = new int[1];
      if (!in.read(l)) {
        SoReadError.post(in, "Premature end of file reading images data");
        return false;
      }
      for (int j = 0; j < nc[0]; j++) {
        pixblock[byte_++] =
          (byte)((l[0] >> (8 * (nc[0]-j-1))) & 0xFF);
      }
    }
  }
  return true;
	}

/*!
  Return pixel buffer, set \a size to contain the image dimensions and
  \a nc to the number of components in the image.
*/
public byte[]
getValue(final SbVec3s size, final int[]  nc) 
{
  return this.value.getValue(size, nc);
}

	/*!
  Initialize this field to \a size and \a nc.

  If \a bytes is not \c NULL, the image data is copied from \a bytes
  into this field.  If \a bytes is \c NULL, the image data is cleared
  by setting all bytes to 0 (note that the behavior on passing a \c
  NULL pointer is specific for Coin, Open Inventor will crash if you
  try it).
*/
public void
setValue(final SbVec3s size, int nc,
                     byte[] bytes)
{
  this./*image*/value.setValue(size, nc, bytes);
  this.valueChanged();
}

/*!
  Return pixel buffer, set \a size to contain the image dimensions and
  \a nc to the number of components in the image.

  The field's container will not be notified about the changes
  until you call finishEditing().
*/
public byte[]
startEditing(final SbVec3s size, final int[] nc)
{
  return this./*image*/value.getValue(size, nc);
}

/*!
  Notify the field's auditors that the image data has been
  modified.
*/
public void
finishEditing()
{
  this.valueChanged();
}

	
}
