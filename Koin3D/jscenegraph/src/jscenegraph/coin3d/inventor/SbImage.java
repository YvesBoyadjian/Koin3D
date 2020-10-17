
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

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import jscenegraph.database.inventor.SbStringList;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SbVec3s;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.port.Destroyable;
import jscenegraph.port.FILE;
import jscenegraph.port.Util;
import jscenegraph.port.memorybuffer.MemoryBuffer;

/**
 * @author Yves Boyadjian
 *
 */
public class SbImage implements Destroyable {
	
	public interface SbImageScheduleReadCB {
		boolean invoke(String str, SbImage img, Object obj);
	}
	
	public interface SbImageReadImageCB {
		boolean invoke(String str, SbImage img, Object obj);
	}

	
	  private MemoryBuffer  bytes;
	  //DataType datatype;
	  private final SbVec3s size = new SbVec3s();
	  int bpp;
	  private String schedulename;
	  SbImageScheduleReadCB schedulecb;
	  Object scheduleclosure;
	  
	/**
	 * 
	 */
	public SbImage() {
	}

/*!
  Constructor which sets 2D data using setValue().
  \sa setValue()
*/
    public SbImage(MemoryBuffer bytes,
                 final SbVec2s size, int bytesperpixel)
    {
        //PRIVATE(this) = new SbImageP;
        this.setValue(size, bytesperpixel, bytes);
    }

/*!
  Constructor which sets 3D data using setValue().

  \COIN_FUNCTION_EXTENSION

  \sa setValue()
  \since Coin 2.0
*/
public SbImage(MemoryBuffer bytes,
                 final SbVec3s size, final int bytesperpixel)
{
  //PRIVATE(this) = new SbImageP;
  this.setValue(size, bytesperpixel, bytes);
}


/*!
  Convenience 2D version of setValue.
*/
    public void
    setValue(final SbVec2s size, final int bytesperpixel,
                  final MemoryBuffer bytes)
    {
        SbVec3s tmpsize = new SbVec3s(size.getValue()[0], size.getValue()[1], (short)0);
        this.setValue(tmpsize, bytesperpixel, bytes);
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
                  MemoryBuffer bytes)
{
  //writeLock();
  schedulename = "";
  //schedulecb = null;
  if (bytes != null /*&& datatype == SbImageP::INTERNAL_DATA*/) {
    // check for special case where we don't have to reallocate
    if (bytes != null&& (size.operator_equal_equal(this.size)) && (bytesperpixel == bpp)) {    	
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
    bytes = MemoryBuffer.allocateBytes(buffersize);
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
  Returns the 2D image data.
*/
public MemoryBuffer
getValue(SbVec2s size, final int[] bytesperpixel)
{
  final SbVec3s tmpsize = new SbVec3s();
  MemoryBuffer bytes = this.getValue(tmpsize, bytesperpixel);
  size.setValue(tmpsize.getValue()[0], tmpsize.getValue()[1]);
  return bytes;
}



/*!
  Returns the 3D image data.

  \since Coin 2.0
*/
public MemoryBuffer
getValue(final SbVec3s  size, final int[] bytesperpixel)
{
  this.readLock();
  if (this.schedulecb != null) {
    // start a thread to read the image.
    boolean scheduled = this.schedulecb.invoke(this.schedulename, (SbImage)(this),
                                        this.scheduleclosure);
    if (scheduled) {
      this.schedulecb = null;
    }
  }
  size.copyFrom(this.size);
  bytesperpixel[0] = bpp;
  MemoryBuffer bytes = this.bytes;
  this.readUnlock();
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
  subdirectories.append("texture");
  subdirectories.append("textures");
  subdirectories.append("images");
  subdirectories.append("pics");
  subdirectories.append("pictures");

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
  final int[] w = new int[1], h = new int[1], nc = new int[1];
  final MemoryBuffer[] simagedata = new MemoryBuffer[1];
  readImage(finalname, w, h, nc, 
		  simagedata);
  if (simagedata[0] != null) {
    //FIXME: Add 3'rd dimension (kintel 20011110)
    this.setValuePtr(
                    new SbVec3s((short)(w[0]),
                           (short)(h[0]),
                           (short)(0)
                           ),
                    nc[0], simagedata[0]);
//    // NB, this is a trick. We use setValuePtr() to set the size
//    // and data pointer, and then we change the data type to simage
//    // peder, 2002-03-22
//    this.datatype = SbImageP::SIMAGE_DATA;
    return true;
  }

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
  //pimpl.readLock(); TODO
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
  //pimpl.readUnlock(); TODO
}


/*!
  Convenience 2D version of setValuePtr.

  \sa setValue()
  \since Coin 2.0
*/
public void
setValuePtr(final SbVec2s size, final int bytesperpixel,
                     MemoryBuffer bytes)
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
                     MemoryBuffer bytes)
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

@Override
public void destructor() {
	bytes = null;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    read passed image file
//
// Use: static, protected

public static boolean readImage(final String fname, final int[] w, final int[] h, final int[] nc, 
                      final MemoryBuffer[] bytes)
//
////////////////////////////////////////////////////////////////////////
{
    w[0] = h[0] = nc[0] = 0;
    bytes[0] = null;
    
    // Empty file means an empty image...
    if (fname.isEmpty()) // java port
        return true;

    final SoInput in = new SoInput();
    try {
    if (!in.openFile(fname, true)) {
        return false;
    }

//#ifdef DEBUG
    SoDebugError.postInfo("SoTexture2::readImage",
                           "Reading texture image "+ fname);
//#endif

/* Florian Link: Disabled RGB image loading, because the libimage uses close
   on the fileno of the fopen stream, which crashes in fclose of SoInput

    if (ReadSGIImage(in, w, h, nc, bytes))
        return TRUE;

    // fiopen() closes the file even if it can't read the data, so 
    // reopen it
    in.closeFile();
    if (!in.openFile(fname.getString(), TRUE))
        return FALSE;
*/
    if (ReadImage(in, w, h, nc, bytes))
        return true;

    //java port
//    if (ReadJPEGImage(in.getCurFile(), w, h, nc, bytes)!=0)
//        return true;

    return false;
    } finally {
    	in.destructor();
    }
}

private static boolean ReadImage(final SoInput in, final int[] w, final int[] h, final int[] nc,  
        MemoryBuffer[] bytes) {

    FILE fp = in.getCurFile();
    
    if (fp == null) return false;
    
    try {
		BufferedImage image = ImageIO.read(fp.getInputStream());
		
		if(image == null) {
			return false;
		}
		w[0] = image.getWidth();
		h[0] = image.getHeight();
	    
	    nc[0] = 3;
	    
	    int nbPixels = w[0]*h[0];
	    
	    byte[] bytesRGB = new byte[nbPixels*3];
	    int j=0;
	    for(int i=0; i< nbPixels;i++) {
	    	int x = i%w[0];
	    	int y = h[0] - i/w[0] -1;
	    	int rgb = image.getRGB(x, y);
	    	bytesRGB[j] = (byte)((rgb & 0x00FF0000) >>> 16) ; j++;
	    	bytesRGB[j] = (byte)((rgb & 0x0000FF00) >>> 8); j++;
	    	bytesRGB[j] = (byte)((rgb & 0x000000FF) >>> 0); j++;	    	
	    }
	    bytes[0] = MemoryBuffer.allocateBytes(nbPixels*3); bytes[0].setBytes(bytesRGB,nbPixels*3);
	    
	    return true;
	} catch (IOException e) {
		return false;
	}
}

/*!
  Schedule a file for reading. \a cb will be called the first time
  getValue() is called for this image, and the callback should then
  start a thread to read the image. Do not read the image in the
  callback, as this will lock up the application.

  \sa readFile()
  \since Coin 2.0
*/
public boolean scheduleReadFile(SbImageScheduleReadCB cb,
                          Object closure,
                          final String filename,
                          final String[] searchdirectories,
                          final int numdirectories)
{
  this.setValue(new SbVec3s((short)0,(short)0,(short)0), 0, null);
  /*pimpl.*/writeLock();
  /*pimpl.*/schedulecb = null;
  /*pimpl.*/schedulename =
    this.searchForFile(filename, searchdirectories, numdirectories);
  int len = /*pimpl.*/schedulename.length();
  if (len > 0) {
    /*pimpl.*/schedulecb = cb;
    /*pimpl.*/scheduleclosure = closure;
  }
  /*pimpl.*/writeUnlock();
  return len > 0;
}


}
