/**************************************************************************\
 * Copyright (c) Kongsberg Oil & Gas Technologies AS
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 
 * Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
\**************************************************************************/

/*!
  \class SoOffscreenRenderer SoOffscreenRenderer.h Inventor/SoOffscreenRenderer.h
  \brief The SoOffscreenRenderer class is used for rendering scenes in offscreen buffers.
  \ingroup general

  If you want to render to a memory buffer instead of an on-screen
  OpenGL context, use this class. Rendering to a memory buffer can be
  used to generate texture maps on-the-fly, or for saving snapshots of
  the scene to disk files (as pixel bitmaps or as Postscript files for
  sending to a Postscript-capable printer).

  Here's a dead simple usage example, just the code directly related
  to the SoOffscreenRenderer:

  \code
  SoOffscreenRenderer myRenderer(vpregion);
  SoNode * root = myViewer.getSceneManager().getSceneGraph();
  SbBool ok = myRenderer.render(root);
  unsigned char * imgbuffer = myRenderer.getBuffer();
  // [then use image buffer in a texture, or write it to file, or whatever]
  \endcode

  And here a complete stand-alone example with a moving camera saving multiple
  frames to disk as JPGs:

  \code
  #include <Inventor/SoDB.h>
  #include <Inventor/SoOffscreenRenderer.h>
  #include <Inventor/engines/SoInterpolateVec3f.h>
  #include <Inventor/nodes/SoCube.h>
  #include <Inventor/nodes/SoDirectionalLight.h>
  #include <Inventor/nodes/SoPerspectiveCamera.h>
  #include <Inventor/nodes/SoSeparator.h>

  #include <iostream>

  int main()
  {
    // Init Coin
    SoDB::init();

    // The root node
    SoSeparator * root = new SoSeparator;
    root.ref();

    // It is mandatory to have at least one light for the offscreen renderer
    SoDirectionalLight * light = new SoDirectionalLight;
    root.addChild(light);

    // It is mandatory to have at least one camera for the offscreen renderer
    SoPerspectiveCamera * camera = new SoPerspectiveCamera;
    SbRotation cameraRotation = SbRotation::identity();
    cameraRotation *= SbRotation(SbVec3f(1, 0, 0), -0.4f);
    cameraRotation *= SbRotation(SbVec3f(0, 1, 0), 0.4f);
    camera.orientation = cameraRotation;
    root.addChild(camera);

    // Something to show... A box
    SoCube * cube = new SoCube;
    root.addChild(cube);

    // Set up the two camera positions we want to move the camera between
    SoInterpolateVec3f * interpolate = new SoInterpolateVec3f;
    interpolate.input0 = SbVec3f(2, 2, 9);
    interpolate.input1 = SbVec3f(2, 2, 5);
    camera.position.connectFrom(&interpolate.output);

    // Set up the offscreen renderer
    SbViewportRegion vpRegion(400, 300);
    SoOffscreenRenderer offscreenRenderer(vpRegion);

    // How many frames to render for the video
    int frames = 5;
    std::cout << "Writing " << frames << " frames..." << std::endl;

    for (int i = 0; i < frames; i++) {
      // Update the camera position
      interpolate.alpha = float(i) / (frames - 1);

      // Render the scene
      SbBool ok = offscreenRenderer.render(root);

      // Save the image to disk
      SbString filename = SbString("coinvideo-") + (i + 1) + ".jpg";
      if (ok) {
        offscreenRenderer.writeToFile(filename.getString(), "jpg");
      } else {
        std::cout << "Error saving image: " << filename.getString() << std::endl;
        break;
      }
    }

    std::cout << "Done!" << std::endl;

    root.unref();
    return 0;
  }
  \endcode


  Note that the SoOffscreenRenderer potentially allocates a fairly
  large amount of resources, both OpenGL and general system resources,
  for each instance. You will therefore be well adviced to try to
  reuse SoOffscreenRenderer instances, instead of constructing and
  destructing a new instance e.g. for each frame when generating
  pictures for video.

  Offscreen rendering is internally done through either a GLX
  offscreen context (i.e. OpenGL on X11), WGL (i.e. OpenGL on
  Win32), AGL (old-style OpenGL on the Mac OS X) or CGL (new-style Mac OS X).

  If the OpenGL driver supports the pbuffer extension, it is detected
  and used to provide hardware-accelerated offscreen rendering.

  The pixeldata is fetched from the OpenGL buffer with glReadPixels(),
  with the format and type arguments set to GL_RGBA and
  GL_UNSIGNED_BYTE, respectively. This means that the maximum
  resolution is 32 bits, 8 bits for each of the R/G/B/A components.


  One particular usage of the SoOffscreenRenderer is to make it render
  frames to be used for the construction of movies. The general
  technique for doing this is to iterate over the following actions:

  <ul>
  <li>move camera to correct position for frame</li>
  <li>update the \c realTime global field (see explanation below)</li>
  <li>invoke the SoOffscreenRenderer</li>
  <li>dump rendered scene to file</li>
  </ul>

  ..then you use some external tool or library to construct the movie
  file, for instance in MPEG format, from the set of files dumped to
  disk from the iterative process above.

  The code would go something like the following (pseudo-code
  style). First we need to stop the Coin library itself from doing any
  automatic updating of the \c realTime field, so your application
  initialization for Coin should look something like:


  \code
   [...] = SoQt::init([...]); // or SoWin::init() or SoDB::init()
   // ..and then immediately:

   // Control realTime field ourselves, so animations within the scene
   // follows "movie-time" and not "wallclock-time".
   SoDB::enableRealTimeSensor(FALSE);
   SoSceneManager::enableRealTimeUpdate(FALSE);
   SoSFTime * realtime = SoDB::getGlobalField("realTime");
   realtime.setValue(0.0);
  \endcode

  Note that it is important that the \c realTime field is initialized
  to \e your start-time \e before setting up any engines or other
  entities in the system that uses the \c realTime field.

  Then for the rendering loop, something like:

  \code
   for (int i=0; i < NRFRAMES; i++) {
     // [...reposition camera here, if necessary...]

     // render
     offscreenrend.render(root);

     // dump to file
     SbString framefile;
     framefile.sprintf("frame%06d.rgb", i);
     offscreenrend.writeToRGB(framefile.getString());

     // advance "current time" by the frames-per-second value, which
     // is 24 fps in this example
     realtime.setValue(realtime.getValue() + 1/24.0);
   }
  \endcode

  When making movies you need to write your application control code
  to take care of moving the camera along the correct trajectory
  yourself, and to explicitly control the global \c realTime field.
  The latter is so you're able to "step" with appropriate time units
  for each render operation (e.g. if you want a movie that has a 24
  FPS refresh rate, first render with \c realTime=0.0, then add 1/24s
  to the \c realTime field, render again to a new frame, add another
  1/24s to the \c realTime field, render, and so on).

  For further information about how to control the \c realTime field,
  see documentation of SoDB::getGlobalField(),
  SoDB::enableRealTimeSensor(), and
  SoSceneManager::enableRealTimeUpdate().

  If you want to use this class to create snapshots of your current
  viewer's view, but want to control the size of the snapshot, you
  need to modify the camera a bit while rendering to be sure that
  everything you see in the current view is visible in the snapshot.

  Below you'll find some pseude-code that does this. There are
  probably other ways to do this as well.

  \code
  void render_offscreen(const SbVec2s size)
  {
    SbVec2s glsize = this.getGLSize(); // size of your normal viewer
    float glar = float(glsize[0] / float(glsize[1]));
    float ar = float(size[0]) / float(size[1]);
    SoCamera * camera = this.getCamera(); // the camera you're using
    SoCamera::ViewportMapping oldmap = (SoCamera::ViewportMapping)
      camera.viewportMapping.getValue();
    float oldar = camera.aspectRatio.getValue();

    camera.viewportMapping = SoCamera::LEAVE_ALONE;
    camera.aspectRatio = ar;

    float scaleheight = 1.0f;
    if (glar > ar) {
      scaleheight = glar / ar;
      camera.scaleHeight(scaleheight);
    }
    else {
      scaleheight = ar / glar;
      camera.scaleHeight(scaleheight);
    }
    SoOffscreenRenderer * renderer = new SoOffscreenRenderer(size);
    renderer.render(root);

    // ... save image

    // restore camera
    camera.viewportMapping = oldmap;
    camera.aspectRatio = oldar;

    if (scaleheight != 1.0f) {
      camera.scaleHeight(1.0f / scaleheight);
    }
  }
  \endcode

*/

// As first mentioned to me by kyrah, the functionality of this class
// should really have been outside the core Coin library, seeing how
// it makes heavy use of window-system specifics. To be SGI Inventor
// compatible we need it to be part of the Coin API, though.
//
// mortene.

// *************************************************************************

// FIXME: we don't set up and render to RGBA-capable OpenGL-contexts,
// even when the requested format from the app-programmer is
// RGBA.
//
// I think this is what we should do:
//
//        1) first, try to get hold of a p-buffer with destination
//        alpha (p-buffers are faster to render into, as they can take
//        advantage of hardware acceleration)
//
//        2) failing that, try to make WGL/GLX/AGL/CGL set up a buffer
//        with destination alpha
//
//        3) failing that, get hold of either a p-buffer or a straight
//        WGL buffer with only RGB (no destination alpha -- this
//        should never fail), and do post-processing on the rendered
//        scene pixel-by-pixel to convert it into an RGBA texture
//
// 20020604 mortene.
//
// UPDATE 20041111 mortene: TGS Inventor has a new set of classes,
// e.g. "SoGLGraphicConfigTemplate", which makes it possible to set up
// wanted attributes with GL contexts. Audit their interface and
// implement, if well designed.

// *************************************************************************

package jscenegraph.coin3d.inventor;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import jscenegraph.coin3d.rendering.SoOffscreenWGLData;
import jscenegraph.database.inventor.SbColor;
import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SbPList;
import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SbViewportRegion;
import jscenegraph.database.inventor.SoPath;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.port.Destroyable;
import jscenegraph.port.FILE;

/**
 * @author Yves Boyadjian
 *
 */
public class SoOffscreenRenderer implements Destroyable {

	  enum Components {
		  NONE,
		    LUMINANCE,// = 1,
		    LUMINANCE_TRANSPARENCY,// = 2,
		    RGB,// = 3,
		    RGB_TRANSPARENCY,// = 4
		  };

			private
				  //friend class SoOffscreenRendererP;
				  SoOffscreenRendererP pimpl; //ptr
			
// *************************************************************************

/*!
  \enum SoOffscreenRenderer::Components

  Enumerated values for the available image formats.

  \sa setComponents()
*/

// *************************************************************************

// *************************************************************************

/*!
  Constructor. Argument is the \a viewportregion we should use when
  rendering. An internal SoGLRenderAction will be constructed.
*/
public SoOffscreenRenderer( final SbViewportRegion viewportregion)
{
  pimpl = new SoOffscreenRendererP(this, viewportregion);
}

/*!
  Constructor. Argument is the \a action we should apply to the
  scene graph when rendering the scene. Information about the
  viewport is extracted from the \a action.
*/
public SoOffscreenRenderer(SoGLRenderAction action)
{
  pimpl = new SoOffscreenRendererP(this, action.getViewportRegion(),
                                           action);
}

/*!
  Destructor.
*/
public void destructor()
{
  pimpl.buffer = null;
  Destroyable.delete( pimpl);
}

/*!
  Returns the screen pixels per inch resolution of your monitor.
*/
public float
getScreenPixelsPerInch()
{
  final SbVec2f pixmmres = new SbVec2f(72.0f / 25.4f, 72.0f / 25.4f);
//#ifdef HAVE_GLX
//  pixmmres = SoOffscreenGLXData::getResolution();
//#elif defined(HAVE_WGL)
  pixmmres.copyFrom(SoOffscreenWGLData.getResolution());
//#elif defined(COIN_MACOS_10)
//  pixmmres = SoOffscreenCGData::getResolution();
//#endif // COIN_MACOS_10

  // The API-signature of this method is not what it should be: it
  // assumes the same resolution in the vertical and horizontal
  // directions.
  float pixprmm = (pixmmres.getValueRead()[0] + pixmmres.getValueRead()[1]) / 2.0f; // find average

  return pixprmm * 25.4f; // an inch is 25.4 mm.
}

/*!
  Get maximum dimensions (width, height) of the offscreen buffer.

  Note that from Coin version 2 onwards, the returned value will
  always be (\c SHRT_MAX, \c SHRT_MAX), where \c SHRT_MAX on most
  systems is equal to 32767.

  This because the SoOffscreenRenderer can in principle generate
  unlimited size offscreen canvases by tiling together multiple
  renderings of the same scene.
*/
public SbVec2s
getMaximumResolution()
{
  return new SbVec2s(Short.MAX_VALUE, Short.MAX_VALUE);
}

/*!
  Sets the component format of the offscreen buffer.

  If set to \c LUMINANCE, a grayscale image is rendered, \c
  LUMINANCE_TRANSPARENCY gives us a grayscale image with transparency,
  \c RGB will give us a 24-bit image with 8 bits each for the red,
  green and blue component, and \c RGB_TRANSPARENCY yields a 32-bit
  image (\c RGB plus transparency).

  The default format to render to is \c RGB.

  This will invalidate the current buffer, if any. The buffer will not
  contain valid data until another call to
  SoOffscreenRenderer::render() happens.
*/
public void
setComponents(final Components components)
{
  pimpl.components = components;
}

/*!
  Returns the component format of the offscreen buffer.

  \sa setComponents()
 */
public Components
getComponents() 
{
  return pimpl.components;

}

/*!
  Sets the viewport region.

  This will invalidate the current buffer, if any. The buffer will not
  contain valid data until another call to
  SoOffscreenRenderer::render() happens.
*/
public void
setViewportRegion(final SbViewportRegion region)
{
  pimpl.viewport.copyFrom( region);
}

/*!
  Returns the viewerport region.
*/
public SbViewportRegion 
getViewportRegion() 
{
  return pimpl.viewport;
}

/*!
  Sets the background color. The buffer is cleared to this color
  before rendering.
*/
public void
setBackgroundColor(final SbColor color)
{
  pimpl.backgroundcolor.copyFrom( color);
}

/*!
  Returns the background color.
*/
public SbColor 
getBackgroundColor()
{
  return pimpl.backgroundcolor;
}

/*!
  Sets the render action. Use this if you have special rendering needs.
*/
public void
setGLRenderAction(SoGLRenderAction action)
{
  if (action == pimpl.renderaction) { return; }

  if (pimpl.didallocation) { Destroyable.delete( pimpl.renderaction); }
  pimpl.renderaction = action;
  pimpl.didallocation = false;
}

/*!
  Returns the rendering action currently used.
*/
public SoGLRenderAction 
getGLRenderAction()
{
  return pimpl.renderaction;
}

/*!
  Render the scenegraph rooted at \a scene into our internal pixel
  buffer.


  Important note: make sure you pass in a \a scene node pointer which
  has both a camera and at least one lightsource below it -- otherwise
  you are likely to end up with just a blank or black image buffer.

  This mistake is easily made if you use an SoOffscreenRenderer on a
  scenegraph from one of the standard viewer components, as you will
  often just leave the addition of a camera and a headlight
  lightsource to the viewer to set up. This camera and lightsource are
  then part of the viewer's private "super-graph" outside of the scope
  of the scenegraph passed in by the application programmer. To make
  sure the complete scenegraph (including the viewer's "private parts"
  (*snicker*)) are passed to this method, you can get the scenegraph
  root from the viewer's internal SoSceneManager instance instead of
  from the viewer's own getSceneGraph() method, like this:

  \code
  SoOffscreenRenderer * myRenderer = new SoOffscreenRenderer(vpregion);
  SoNode * root = myViewer.getSceneManager().getSceneGraph();
  SbBool ok = myRenderer.render(root);
  // [then use image buffer in a texture, or write it to file, or whatever]
  \endcode

  If you do this and still get a blank buffer, another common problem
  is to have a camera which is not actually pointing at the scene
  geometry you want a snapshot of. If you suspect that could be the
  cause of problems on your end, take a look at SoCamera::pointAt()
  and SoCamera::viewAll() to see how you can make a camera node
  guaranteed to be directed at the scene geometry.

  Yet another common mistake when setting up the camera is to specify
  values for the SoCamera::nearDistance and SoCamera::farDistance
  fields which doesn't not enclose the full scene. This will result in
  either just the background color, or that parts at the front or the
  back of the scene will not be visible in the rendering.

  \sa writeToRGB()
*/
public boolean
render(SoNode scene)
{
  return pimpl.renderFromBase(scene);
}

/*!
  Render the \a scene path into our internal memory buffer.
*/
public boolean
render(SoPath scene)
{
  return pimpl.renderFromBase(scene);
}

// *************************************************************************

/*!
  Returns the offscreen memory buffer.
*/
public byte[]
getBuffer()
{
  if (!pimpl.didreadbuffer) {
    final SbVec2s dims = new SbVec2s(this.getViewportRegion().getViewportSizePixels());
    //fprintf(stderr,"reading pixels: %d %d\n", dims[0], dims[1]);

    pimpl.glcanvas.activateGLContext();
    pimpl.glcanvas.readPixels(pimpl.buffer, dims, dims.getValue()[0],
                                       (int) this.getComponents().ordinal());
    pimpl.glcanvas.deactivateGLContext();
    pimpl.didreadbuffer = true;
  }
  return pimpl.buffer;
}

/*!
  Win32 only:

  returns a direct handle to the internal DC of the offscreen
  context.

  Useful for efficient access to the raw image under certain special
  circumstances. getBuffer() might be too slow, for instance due to
  pixel format conversion (Windows DCs are usually BGRA, while the
  32-bit buffers returned from getBuffer() are RGBA).

  Notes:

  The return value is a reference to a HDC. The HDC typedef has been
  unwound to a native C++ type for multiplatform compatibility
  reasons.

  Returned reference will contain a NULL value on other platforms.

  Important limitation: if the current dimensions of the
  SoOffscreenRenderer instance are larger than what can be rendered
  with a single offscreen buffer, tiling will be used by the
  SoOffscreenRenderer, and the returned HDC will contain only part of
  the full rendered image.

  \sa getBuffer()
  \since Coin 3.1
*/
public Object /*const void * const &*/
getDC() 
{
  if(!pimpl.useDC)
  {
	pimpl.useDC = true;
	pimpl.updateDCBitmap();
  }
  
  return pimpl.glcanvas.getHDC();
}

/*!
  Writes the buffer in SGI RGB format by appending it to the already
  open file. Returns \c FALSE if writing fails.

  Important note: do \e not use this method when the Coin library has
  been compiled as an MSWindows DLL, as passing FILE* instances back
  or forth to DLLs is dangerous and will most likely cause a
  crash. This is an intrinsic limitation for MSWindows DLLs.
*/
public boolean
writeToRGB(FILE fp)
{
  if (SoOffscreenRendererP.offscreenContextsNotSupported()) { return false; }

  SbVec2s size = pimpl.viewport.getViewportSizePixels();

  return SoOffscreenRendererP.writeToRGB(fp, size.getValue()[0], size.getValue()[1],
                                          this.getComponents().ordinal(),
                                          this.getBuffer());
}

/*!
  Opens a file with the given name and writes the offscreen buffer in
  SGI RGB format to the new file. If the file already exists, it will
  be overwritten (if permitted by the filesystem).

  Returns \c TRUE if all went ok, otherwise \c FALSE.
*/
public boolean
writeToRGB(String filename)
{
  FILE rgbfp = FILE.fopen(filename, "wb");
  if (rgbfp == null) {
    SoDebugError.postWarning("SoOffscreenRenderer::writeToRGB",
                              "couldn't open file '"+filename+"'");
    return false;
  }
  boolean result = this.writeToRGB(rgbfp);
  FILE.fclose(rgbfp);
  return result;
}

/*!
  Writes the buffer in Postscript format by appending it to the
  already open file. Returns \c FALSE if writing fails.

  Important note: do \e not use this method when the Coin library has
  been compiled as an MSWindows DLL, as passing FILE* instances back
  or forth to DLLs is dangerous and will most likely cause a
  crash. This is an intrinsic limitation for MSWindows DLLs.
*/
//public boolean
//writeToPostScript(FILE fp)
//{
//  // just choose a page size of 8.5 x 11 inches (A4)
//  return this.writeToPostScript(fp, new SbVec2f(8.5f, 11.0f));
//}

/*!
  Opens a file with the given name and writes the offscreen buffer in
  Postscript format to the new file. If the file already exists, it
  will be overwritten (if permitted by the filesystem).

  Returns \c TRUE if all went ok, otherwise \c FALSE.
*/
//public boolean
//writeToPostScript(String filename)
//{
//  FILE psfp = FILE.fopen(filename, "wb");
//  if (psfp == null) {
//    SoDebugError.postWarning("SoOffscreenRenderer::writeToPostScript",
//                              "couldn't open file '"+filename+"'");
//    return false;
//  }
//  boolean result = this.writeToPostScript(psfp);
//  FILE.fclose(psfp);
//  return result;
//}

/*!
  Writes the buffer to a file in Postscript format, with \a printsize
  dimensions.

  Important note: do \e not use this method when the Coin library has
  been compiled as an MSWindows DLL, as passing FILE* instances back
  or forth to DLLs is dangerous and will most likely cause a
  crash. This is an intrinsic limitation for MSWindows DLLs.
*/
//public boolean
//writeToPostScript(FILE fp,
//                                       final SbVec2f printsize)
//{
//  if (SoOffscreenRendererP.offscreenContextsNotSupported()) { return FALSE;}
//
//  final SbVec2s size = pimpl.viewport.getViewportSizePixels();
//  final int nc = this.getComponents();
//  final float defaultdpi = 72.0f; // we scale against this value
//  final float dpi = this.getScreenPixelsPerInch();
//  final SbVec2s pixelsize((short)(printsize[0]*defaultdpi),
//                          (short)(printsize[1]*defaultdpi));
//
//  final byte[] src = this.getBuffer();
//  final int chan = nc <= 2 ? 1 : 3;
//  final SbVec2s scaledsize((short) ceil(size[0]*defaultdpi/dpi),
//                           (short) ceil(size[1]*defaultdpi/dpi));
//
//  cc_string storedlocale;
//  SbBool changed = coin_locale_set_portable(&storedlocale);
//
//  fprintf(fp, "%%!PS-Adobe-2.0 EPSF-1.2\n");
//  fprintf(fp, "%%%%BoundingBox: 0 %d %d %d\n",
//          pixelsize[1]-scaledsize[1],
//          scaledsize[0],
//          pixelsize[1]);
//  fprintf(fp, "%%%%Creator: Coin <http://www.coin3d.org>\n");
//  fprintf(fp, "%%%%EndComments\n");
//
//  fprintf(fp, "\n");
//  fprintf(fp, "/origstate save def\n");
//  fprintf(fp, "\n");
//  fprintf(fp, "%% workaround for bug in some PS interpreters\n");
//  fprintf(fp, "%% which doesn't skip the ASCII85 EOD marker.\n");
//  fprintf(fp, "/~ {currentfile read pop pop} def\n\n");
//  fprintf(fp, "/image_wd %d def\n", size[0]);
//  fprintf(fp, "/image_ht %d def\n", size[1]);
//  fprintf(fp, "/pos_wd %d def\n", size[0]);
//  fprintf(fp, "/pos_ht %d def\n", size[1]);
//  fprintf(fp, "/image_dpi %g def\n", dpi);
//  fprintf(fp, "/image_scale %g image_dpi div def\n", defaultdpi);
//  fprintf(fp, "/image_chan %d def\n", chan);
//  fprintf(fp, "/xpos_offset 0 image_scale mul def\n");
//  fprintf(fp, "/ypos_offset 0 image_scale mul def\n");
//  fprintf(fp, "/pix_buf_size %d def\n\n", size[0]*chan);
//  fprintf(fp, "/page_ht %g %g mul def\n", printsize[1], defaultdpi);
//  fprintf(fp, "/page_wd %g %g mul def\n", printsize[0], defaultdpi);
//  fprintf(fp, "/image_xpos 0 def\n");
//  fprintf(fp, "/image_ypos page_ht pos_ht image_scale mul sub def\n");
//  fprintf(fp, "image_xpos xpos_offset add image_ypos ypos_offset add translate\n");
//  fprintf(fp, "\n");
//  fprintf(fp, "/pix pix_buf_size string def\n");
//  fprintf(fp, "image_wd image_scale mul image_ht image_scale mul scale\n");
//  fprintf(fp, "\n");
//  fprintf(fp, "image_wd image_ht 8\n");
//  fprintf(fp, "[image_wd 0 0 image_ht 0 0]\n");
//  fprintf(fp, "currentfile\n");
//  fprintf(fp, "/ASCII85Decode filter\n");
//  // fprintf(fp, "/RunLengthDecode filter\n"); // FIXME: add later. 2003???? pederb.
//  if (chan == 3) fprintf(fp, "false 3\ncolorimage\n");
//  else fprintf(fp,"image\n");
//
//  const int rowlen = 72;
//  int num = size[0] * size[1];
//  unsigned char tuple[4];
//  unsigned char linebuf[rowlen+5];
//  int tuplecnt = 0;
//  int linecnt = 0;
//  int cnt = 0;
//  while (cnt < num) {
//    switch (nc) {
//    default: // avoid warning
//    case 1:
//      coin_output_ascii85(fp, src[cnt], tuple, linebuf, &tuplecnt, &linecnt, rowlen, FALSE);
//      break;
//    case 2:
//      coin_output_ascii85(fp, src[cnt*2], tuple, linebuf, &tuplecnt, &linecnt, rowlen, FALSE);
//      break;
//    case 3:
//      coin_output_ascii85(fp, src[cnt*3], tuple, linebuf, &tuplecnt, &linecnt, rowlen, FALSE);
//      coin_output_ascii85(fp, src[cnt*3+1], tuple, linebuf, &tuplecnt, &linecnt, rowlen, FALSE);
//      coin_output_ascii85(fp, src[cnt*3+2], tuple, linebuf, &tuplecnt, &linecnt, rowlen, FALSE);
//      break;
//    case 4:
//      coin_output_ascii85(fp, src[cnt*4], tuple, linebuf, &tuplecnt, &linecnt, rowlen, FALSE);
//      coin_output_ascii85(fp, src[cnt*4+1], tuple, linebuf, &tuplecnt, &linecnt,rowlen, FALSE);
//      coin_output_ascii85(fp, src[cnt*4+2], tuple, linebuf, &tuplecnt, &linecnt, rowlen, FALSE);
//      break;
//    }
//    cnt++;
//  }
//
//  // flush data in ascii85 encoder
//  coin_flush_ascii85(fp, tuple, linebuf, &tuplecnt, &linecnt, rowlen);
//
//  fprintf(fp, "~>\n\n"); // ASCII85 EOD marker
//  fprintf(fp, "origstate restore\n");
//  fprintf(fp, "\n");
//  fprintf(fp, "%%%%Trailer\n");
//  fprintf(fp, "\n");
//  fprintf(fp, "%%%%EOF\n");
//
//  if (changed) { coin_locale_reset(&storedlocale); }
//
//  return (SbBool) (ferror(fp) == 0);
//}

/*!
  Opens a file with the given name and writes the offscreen buffer in
  Postscript format with \a printsize dimensions to the new file. If
  the file already exists, it will be overwritten (if permitted by the
  filesystem).

  Returns \c TRUE if all went ok, otherwise \c FALSE.
*/
//public boolean
//writeToPostScript(String filename,
//                                       final SbVec2f printsize)
//{
//  FILE * psfp = fopen(filename, "wb");
//  if (!psfp) {
//    SoDebugError::postWarning("SoOffscreenRenderer::writeToPostScript",
//                              "couldn't open file '%s'", filename);
//    return FALSE;
//  }
//  SbBool result = this.writeToPostScript(psfp, printsize);
//  (void)fclose(psfp);
//  return result;
//}

// FIXME: the file format support checking could have been done
// better, for instance by using MIME types. Consider fixing the API
// for later major releases. 20020206 mortene.
//
// UPDATE 20050711 mortene: it seems like TGS has extended their API
// in an even worse way; by adding separate writeToJPEG(),
// writeToPNG(), etc etc functions.

/*!
  Returns \c TRUE if the buffer can be saved as a file of type \a
  filetypeextension, using SoOffscreenRenderer::writeToFile().  This
  function needs simage v1.1 or newer.

  Examples of possibly supported extensions are: "jpg", "png", "tiff",
  "gif", "bmp", etc. The extension match is not case sensitive.

  Which formats are \e actually supported depends on the capabilities
  of Coin's support library for handling import and export of
  pixel-data files: the simage library. If the simage library is not
  installed on your system, no extension output formats will be
  supported.

  Also, note that it is possible to build and install a simage library
  that lacks support for most or all of the file formats it is \e
  capable of supporting. This is so because the simage library depends
  on other, external 3rd party libraries -- in the same manner as Coin
  depends on the simage library for added file format support.

  The two built-in formats that are supported through the
  SoOffscreenRenderer::writeToRGB() and
  SoOffscreenRenderer::writeToPostScript() methods (for SGI RGB format
  and for Adobe Postscript files, respectively) are \e not considered
  by this method, as those two formats are guaranteed to \e always be
  supported through those functions.

  So if you want to be guaranteed to be able to export a screenshot in
  your wanted format, you will have to use either one of the above
  mentioned method for writing SGI RGB or Adobe Postscript directly,
  or make sure the Coin library has been built and is running on top
  of a version of the simage library (that you have preferably built
  yourself) with the file format(s) you want support for.


  This method is an extension versus the original SGI Open Inventor
  API.

  \sa  getNumWriteFiletypes(), getWriteFiletypeInfo(), writeToFile()
*/
public boolean
isWriteSupported(final SbName filetypeextension)
{
	if( filetypeextension.getString().equalsIgnoreCase("jpg") ||
		filetypeextension.getString().equalsIgnoreCase("jpeg")	) {
		return true;
	}
	
	return false;
//  if (!simage_wrapper().versionMatchesAtLeast(1,1,0)) {
//
//    if (CoinOffscreenGLCanvas.debug()) {
//      if (!simage_wrapper().available) {
//        SoDebugError.postInfo("SoOffscreenRenderer::isWriteSupported",
//                               "simage library not available.");
//      } else {
//        SoDebugError.postInfo("SoOffscreenRenderer::isWriteSupported",
//                               "You need simage v1.1 for this functionality.");
//      }
//    }
//    return FALSE;
//  }
//  int ret = simage_wrapper().simage_check_save_supported(filetypeextension.getString());
//  return ret ? TRUE : FALSE;
}

/*!
  Returns the number of available exporters. Detailed information
  about the exporters can then be found using getWriteFiletypeInfo().

  See SoOffscreenRenderer::isWriteSupported() for information about
  which file formats you can expect to be present.

  Note that the two built-in export formats, SGI RGB and Adobe
  Postscript, are not counted.

  This method is an extension versus the original SGI Open Inventor
  API.

  \sa getWriteFiletypeInfo()
*/
public int
getNumWriteFiletypes() 
{
	return 1;
//  if (!simage_wrapper().versionMatchesAtLeast(1,1,0)) {
////#if COIN_DEBUG
//    SoDebugError.postInfo("SoOffscreenRenderer::getNumWriteFiletypes",
//                           "You need simage v1.1 for this functionality.");
////#endif // COIN_DEBUG
//    return 0;
//  }
//  return simage_wrapper().simage_get_num_savers();
}

/*!
  Returns information about an image exporter. \a extlist is a list
  of filename extensions for a file format. E.g. for JPEG it is legal
  to use both jpg and jpeg. Extlist will contain const char * pointers
  (you need to cast the void * pointers to const char * before using
  them).

  \a fullname is the full name of the image format. \a description is
  an optional string with more information about the file format.

  See SoOffscreenRenderer::isWriteSupported() for information about
  which file formats you can expect to be present.

  This method is an extension versus the original SGI Open Inventor
  API.

  Here is a stand-alone, complete code example that shows how you can
  check exactly which output formats are supported:

  \code
  #include <Inventor/SoDB.h>
  #include <Inventor/SoOffscreenRenderer.h>

  int
  main(int argc, char **argv)
  {
    SoDB::init();
    SoOffscreenRenderer * r = new SoOffscreenRenderer(*(new SbViewportRegion));
    int num = r.getNumWriteFiletypes();

    if (num == 0) {
      (void)fprintf(stdout,
                    "No image formats supported by the "
                    "SoOffscreenRenderer except SGI RGB and Postscript.\n");
    }
    else {
      for (int i=0; i < num; i++) {
        SbPList extlist;
        SbString fullname, description;
        r.getWriteFiletypeInfo(i, extlist, fullname, description);
        (void)fprintf(stdout, "%s: %s (extension%s: ",
                      fullname.getString(), description.getString(),
                      extlist.getLength() > 1 ? "s" : "");
        for (int j=0; j < extlist.getLength(); j++) {
          (void)fprintf(stdout, "%s%s", j>0 ? ", " : "", (const char*) extlist[j]);
        }
        (void)fprintf(stdout, ")\n");
      }
    }

    delete r;
    return 0;
  }
  \endcode

  \sa getNumWriteFiletypes(), writeToFile()

  \since Coin 2.3
*/
public void
getWriteFiletypeInfo(final int idx,
                                          final SbPList extlist,
                                          final String[] fullname,
                                          final String[] description)
{
	if(idx == 0) {
		extlist.append("jpg");
		fullname[0] = "jpeg";
		description[0] = "Joint Photographic Experts Group";
	}
//  if (!simage_wrapper().versionMatchesAtLeast(1,1,0)) {
////#if COIN_DEBUG
//    SoDebugError.postInfo("SoOffscreenRenderer::getNumWriteFiletypes",
//                           "You need simage v1.1 for this functionality.");
////#endif // COIN_DEBUG
//    return;
//  }
//  extlist.truncate(0);
//  assert(idx >= 0 && idx < this.getNumWriteFiletypes());
//  void * saver = simage_wrapper().simage_get_saver_handle(idx);
//  SbString allext(simage_wrapper().simage_get_saver_extensions(saver));
//  const char * start = allext.getString();
//  const char * curr = start;
//  const char * end = strchr(curr, ',');
//  while (end) {
//    const ptrdiff_t offset_start = curr - start;
//    const ptrdiff_t offset_end = end - start - 1;
//    SbString ext = allext.getSubString((int)offset_start, (int)offset_end);
//    SbName extname = new SbName(ext.getString());
//    extlist.append((void*)extname.getString());
//    curr = end+1;
//    end = strchr(curr, ',');
//  }
//  final /*ptrdiff_t*/int offset = curr - start;
//  String ext = allext.getSubString((int)offset);
//  SbName extname(ext.getString());
//  extlist.append((void*)extname.getString());
//  const char * fullname_s = simage_wrapper().simage_get_saver_fullname(saver);
//  const char * desc_s = simage_wrapper().simage_get_saver_description(saver);
//  fullname = fullname_s ? SbString(fullname_s) : SbString("");
//  description = desc_s ? SbString(desc_s) : SbString("");
}

/*!
  Saves the buffer to \a filename, in the filetype specified by \a
  filetypeextensions.

  Note that you must still specify the \e full \a filename for the
  first argument, i.e. the second argument will not automatically be
  attached to the filename -- it is only used to decide the filetype.

  This method is an extension versus the orignal SGI Open Inventor
  API.

  \sa isWriteSupported()
*/
public boolean
writeToFile(final String filename, final SbName filetypeextension)
{
//  if (!simage_wrapper().versionMatchesAtLeast(1,1,0)) {
//    //FIXME: Shouldn't use BOOST_CURRENT_FUNCTION here, the
//    //HAVE_CPP_COMPILER_FUNCTION_NAME_VAR should be massaged correctly
//    //to fit here. BFG 20090917
//    if (!simage_wrapper().available) {
//      SoDebugError.post(BOOST_CURRENT_FUNCTION,
//                             "simage library not available.");
//    }
//    else {
//      final int[] major = new int[1], minor = new int[1], micro = new int[1];
//      simage_wrapper().simage_version(major,minor,micro);
//      SoDebugError.post(BOOST_CURRENT_FUNCTION,
//                         "simage version is older than 1.1.0, available version is %d.%d.%d", major,minor,micro);
//    }
//    return FALSE;
//  }
  if (SoOffscreenRendererP.offscreenContextsNotSupported()) {
    SoDebugError.post("writeToFile",
                       "Offscreen contexts not supported.");
    return false;
  }

  final SbVec2s size = new SbVec2s(pimpl.viewport.getViewportSizePixels());
  int comp = (int) this.getComponents().ordinal();
  byte[] bytes = this.getBuffer();
  int ret = /*simage_wrapper().*/simage_save_image(filename,
                                                bytes,
                                                (int)(size.getValue()[0]), (int)(size.getValue()[1]), comp,
                                                filetypeextension.getString());
  
  
  
  return (ret != 0) ? true : false;
}

public  int simage_save_image(String filename,
        byte[] bytes,
        int w, int h, int numcomponents,
        String filenameextension) {
	
	//BufferedImage img = null;
	BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
	byte[] array = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
	System.arraycopy(bytes, 0, array, 0, array.length);
	File f = new File(filename+"."+filenameextension);
			try {
				ImageIO.write(img, filenameextension, f);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 0;
			}	
	return 1;
}

// *************************************************************************

/*!
  Control whether or not SoOffscreenRenderer can use the "pbuffer"
  feature of OpenGL to render the scenes with hardware acceleration.

  This is a dummy function in Coin, provided for API compatibility
  reasons, as it is really superfluous:

  Coin has internal heuristics to figure out if pbuffers are available
  and can be allocated and used for the SoOffscreenRenderer.  The
  SoOffscreenRenderer will also automatically fall back on "soft"
  buffers if it can not use pbuffers (or any other hardware
  accelerated rendering technique).

  \since Coin 3.1
*/
public void
setPbufferEnable(boolean enable)
{
  // FIXME: change the semantics of this function from just ignoring
  // the input argument, to using it for shutting off pbuffers if
  // FALSE?
  //
  // not sure there's really any good reason to do that, however.
  //
  // mortene.
}

/*!
  See SoOffscreenRenderer::setPbufferEnable().

  \since Coin 3.1
*/
public boolean
getPbufferEnable()
{
  // FIXME: should perhaps return a flag indicating whether or not the
  // system can use pbuffers. this depends on the GL context, however,
  // so the design of this Mercury Inventor API function is inherently
  // flawed.
  //
  // hardly any GL driver these days does *not* provide pbuffers,
  // though, so this is unlikely to be an important issue.
  //
  // mortene.

  return true;
}

// *************************************************************************

}
