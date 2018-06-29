
/**************************************************************************\
 *
 *  This file is part of the Coin 3D visualization library.
 *  Copyright (C) by Kongsberg Oil & Gas Technologies.
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  ("GPL") version 2 as published by the Free Software Foundation.
 *  See the file LICENSE.GPL at the root directory of this source
 *  distribution for additional information about the GNU GPL.
 *
 *  For using Coin with software that can not be combined with the GNU
 *  GPL, and for taking advantage of the additional benefits of our
 *  support services, please contact Kongsberg Oil & Gas Technologies
 *  about acquiring a Coin Professional Edition License.
 *
 *  See http://www.coin3d.org/ for more information.
 *
 *  Kongsberg Oil & Gas Technologies, Bygdoy Alle 5, 0257 Oslo, NORWAY.
 *  http://www.sim.no/  sales@sim.no  coin-support@coin3d.org
 *
\**************************************************************************/

package jscenegraph.coin3d.inventor;

import jscenegraph.database.inventor.SbStringList;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SbVec3s;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.port.Util;

/**
 * @author Yves Boyadjian
 *
 */
public class SbImage {
	
	  private byte[]  bytes;
	  //DataType datatype;
	  private final SbVec3s size = new SbVec3s();
	  int bpp;
	  private String schedulename;
	  
	/**
	 * 
	 */
	public SbImage() {
	}
	
/*!
  Constructor which sets 3D data using setValue().

  \COIN_FUNCTION_EXTENSION

  \sa setValue()
  \since Coin 2.0
*/
public SbImage(byte[] bytes,
                 final SbVec3s size, final int bytesperpixel)
{
  //PRIVATE(this) = new SbImageP;
  this.setValue(size, bytesperpixel, bytes);
}

	

/*!
  Sets the image to \a size and \a bytesperpixel. If \a bytes !=
  NULL, data is copied from \a bytes into this class' image data. If
  \a bytes == NULL, the image data is left uninitialized.

  The image data will always be allocated in multiples of four. This
  means that if you set an image with size == (1,1,1) and bytesperpixel
  == 1, four bytes will be allocated to hold the data. This is mainly
  done to simplify the export code in SoSFImage and normally you'll
  not have to worry about this feature.

  If the depth of the image (size[2]) is zero, the image is considered
  a 2D image.

  \since Coin 2.0
*/
public void
setValue(final SbVec3s size, int bytesperpixel,
                  byte[] bytes)
{
  //writeLock();
  schedulename = "";
  //schedulecb = null;
  if (bytes != null /*&& datatype == SbImageP::INTERNAL_DATA*/) {
    // check for special case where we don't have to reallocate
    if (bytes != null&& (size == this.size) && (bytesperpixel == bpp)) {    	
      Util.memcpy(bytes, bytes, 
             (int)(size.getValue()[0]) * (int)(size.getValue()[1]) * (int)(size.getValue()[2]==0?1:size.getValue()[2]) *
             bytesperpixel);
      //writeUnlock();
      return;
    }
  }
  freeData();
  this.size.copyFrom(size);
  bpp = bytesperpixel;
  int buffersize = (int)(size.getValue()[0]) * (int)(size.getValue()[1]) * (int)(size.getValue()[2]==0?1:size.getValue()[2]) * 
    bytesperpixel;
  if (buffersize != 0) {
    // Align buffers because the binary file format has the data aligned
    // (simplifies export code in SoSFImage).
    buffersize = ((buffersize + 3) / 4) * 4;
    bytes = new byte[buffersize];
    //datatype = SbImageP::INTERNAL_DATA;

    if (bytes != null) {
      // Important: don't copy buffersize num bytes here!
    	Util.memcpy(this.bytes, bytes,
                   (int)(size.getValue()[0]) * (int)(size.getValue()[1]) * (int)(size.getValue()[2]==0?1:size.getValue()[2]) * 
                   bytesperpixel);
    }
  }
  //writeUnlock();
}

private void freeData() {
	bytes = null;
}

/*!
  Returns the 3D image data.

  \since Coin 2.0
*/
public byte[]
getValue(final SbVec3s  size, final int[] bytesperpixel)
{
  //this.readLock();
//  if (this.schedulecb) {
//    // start a thread to read the image.
//    SbBool scheduled = this.schedulecb(this.schedulename, const_cast<SbImage *>(this),
//                                        this.scheduleclosure);
//    if (scheduled) {
//      this.schedulecb = NULL;
//    }
//  }
  size.copyFrom(this.size);
  bytesperpixel[0] = bpp;
  byte[] bytes = this.bytes;
  //this.readUnlock();
  return bytes;

}


/*!
  Given a \a basename for a file and and array of directories to
  search (in \a dirlist, of length \a numdirs), returns the full name
  of the file found.

  In addition to looking at the root of each directory in \a dirlist,
  we also look into the subdirectories \e texture/, \e textures/, \e
  images/, \e pics/ and \e pictures/ of each \a dirlist directory.

  If no file matching \a basename could be found, returns an empty
  string.
*/
public static String
searchForFile(final String  basename,
                       final String[] dirlist, final int numdirs)
{
  int i;
  final SbStringList directories = new SbStringList();
  final SbStringList subdirectories = new SbStringList();

  for (i = 0; i < numdirs; i++) {
    directories.append((String)(dirlist[i]));
  }
  subdirectories.append(new String("texture"));
  subdirectories.append(new String("textures"));
  subdirectories.append(new String("images"));
  subdirectories.append(new String("pics"));
  subdirectories.append(new String("pictures"));

  String ret = SoInput.searchForFile(basename, directories, subdirectories);
  for (i = 0; i < subdirectories.getLength(); i++) {
    //delete subdirectories[i]; // java port
  }
  return ret;
}


/*!
  Reads image data from \a filename. In Coin, simage is used to
  load image files, and several common file formats are supported.
  simage can be downloaded from our webpages.  If loading
  fails for some reason this method returns FALSE, and the instance
  is set to an empty image. If the file is successfully loaded, the
  file image data is copied into this class.

  If \a numdirectories > 0, this method will search for \a filename
  in all directories in \a searchdirectories.
*/
public boolean
readFile(final String  filename,
                  final String[] searchdirectories,
                  final int numdirectories)
{
  // FIXME: Add 3D image support when that is added to simage (kintel 20011118)

  if (filename.length() == 0) {
    // This is really an internal error, should perhaps assert. <mortene>.
    SoDebugError.post("SbImage::readFile",
                       "attempted to read file from empty filename.");
    return false;
  }

  String finalname = SbImage.searchForFile(filename, searchdirectories,
                                              numdirectories);
  if (finalname.length() == 0) {
    SoDebugError.post("SbImage::readFile",
                       "couldn't find '"+filename+"'.");
    return false;
  }

  // TODO : to implement with java classes
//  // use callback to load the image if it's set
//  if (SbImageP::readimagecallbacks) {
//    for (int i = 0; i < SbImageP::readimagecallbacks.getLength(); i++) {
//      SbImageP::ReadImageCBData cbdata = (*SbImageP::readimagecallbacks)[i];
//      if (cbdata.cb(finalname, this, cbdata.closure)) return true;
//    }
//    if (!simage_wrapper().available) {
//      return false;
//    }
//  }
//  
//  // try simage
//  if (!simage_wrapper().available) {
//    SoDebugError.postWarning("SbImage::readFile",
//                              "The simage library is not available, "+
//                              "can not import any images from disk.");
//    return false;
//  }
//
//
//
//  assert(simage_wrapper().simage_read_image);
//  final int[] w = new int[1], h = new int[1], nc = new int[1];
//  byte[] simagedata =
//    simage_wrapper().simage_read_image(finalname, 
//                                        w, h, nc);
//  if (simagedata != null) {
//    //FIXME: Add 3'rd dimension (kintel 20011110)
//    this.setValuePtr(
//                    new SbVec3s((short)(w[0]),
//                           (short)(h[0]),
//                           (short)(0)
//                           ),
//                    nc, simagedata);
//    // NB, this is a trick. We use setValuePtr() to set the size
//    // and data pointer, and then we change the data type to simage
//    // peder, 2002-03-22
//    this.datatype = SbImageP::SIMAGE_DATA;
//    return true;
//  }
//#if COIN_DEBUG
//  else {
//    SoDebugError::post("SbImage::readFile", "(%s) %s",
//                       filename.getString(),
//                       // FIXME: "getlasterror" is a crap strategy, as
//                       // it places extra burden on the client to
//                       // lock. Should keep a single entry-lock within
//                       // simage_wrapper() to work around
//                       // this. 20020628 mortene.
//                       simage_wrapper()->simage_get_last_error ?
//                       simage_wrapper()->simage_get_last_error() :
//                       "Unknown error");
//  }
//#endif // COIN_DEBUG
    
  this.setValue(new SbVec3s((short)0,(short)0,(short)0), 0, null);
  return false;
}

/*!
  Apply a read lock on this image. This will make it impossible for
  other threads to change the image while this lock is active. Other
  threads can do read-only operations on this image, of course.

  For the single thread version of Coin, this method does nothing.

  \sa readUnlock()
  \since Coin 2.0
*/
public void
readLock() 
{
  //PRIVATE(this)->readLock(); TODO
}

/*!
  Release a read lock on this image.

  For the single thread version of Coin, this method does nothing.

  \sa readLock()
  \since Coin 2.0
*/
public void
readUnlock() 
{
  //PRIVATE(this)->readUnlock(); TODO
}


/*!
  Convenience 2D version of setValuePtr.

  \sa setValue()
  \since Coin 2.0
*/
public void
setValuePtr(final SbVec2s size, final int bytesperpixel,
                     byte[] bytes)
{
  final SbVec3s tmpsize = new SbVec3s(size.getValue()[0], size.getValue()[1], (short)0);
  this.setValuePtr(tmpsize, bytesperpixel, bytes);
}


/*!
  Sets the image data without copying the data. \a bytes will be used
  directly, and the data will not be freed when the image instance is
  destructed.

  If the depth of the image (size[2]) is zero, the image is considered
  a 2D image.

  \sa setValue()
  \since Coin 2.0
*/
public void
setValuePtr(final SbVec3s size, int bytesperpixel,
                     byte[] bytes)
{
  this.writeLock();
  this.schedulename = "";
  //this.schedulecb = null; TODO
  this.freeData();
  this.bytes = (bytes);
  //this.datatype = SbImage.SETVALUEPTR_DATA; TODO
  this.size.copyFrom( size);
  this.bpp = bytesperpixel;
  this.writeUnlock();
}

public void writeLock() { }
public void writeUnlock() { }


/*!
  Returns \a TRUE if the image is not empty. This can be useful, since
  getValue() will start loading the image if scheduleReadFile() has
  been used to set the image data.

  \since Coin 2.0
*/
public boolean 
hasData() 
{
  boolean ret;
  this.readLock();
  ret = (this.bytes != null);
  this.readUnlock();
  return ret;
}

/*!
  Returns the size of the image. If this is a 2D image, the
  z component is zero. If this is a 3D image, the z component is
  >= 1.

  \since Coin 2.0
 */
public SbVec3s
getSize() 
{
  return new SbVec3s(this.size);
}


}
