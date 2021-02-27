/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.coin3d.inventor.SbImage;
import jscenegraph.coin3d.inventor.elements.SoMultiTextureEnabledElement;
import jscenegraph.coin3d.inventor.elements.SoMultiTextureImageElement;
import jscenegraph.coin3d.inventor.elements.SoTextureScalePolicyElement;
import jscenegraph.coin3d.inventor.elements.SoTextureUnitElement;
import jscenegraph.coin3d.inventor.elements.gl.SoGLMultiTextureImageElement;
import jscenegraph.coin3d.inventor.misc.SoGLBigImage;
import jscenegraph.coin3d.inventor.misc.SoGLImage;
import jscenegraph.database.inventor.*;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoRayPickAction;
import jscenegraph.database.inventor.elements.SoCacheElement;
import jscenegraph.database.inventor.elements.SoTextureOverrideElement;
import jscenegraph.database.inventor.elements.SoTextureQualityElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFString;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.database.inventor.sensors.SoFieldSensor;
import jscenegraph.database.inventor.sensors.SoOneShotSensor;
import jscenegraph.database.inventor.sensors.SoSensor;
import jscenegraph.database.inventor.sensors.SoTimerSensor;
import jscenegraph.port.Destroyable;
import jscenegraph.port.memorybuffer.MemoryBuffer;

import java.nio.file.Path;

/**
 * @author BOYADJIAN
 *
 */
public class SoVRMLImageTexture extends SoVRMLTexture {
		
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoVRMLImageTexture.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoVRMLImageTexture.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoVRMLImageTexture.class); }    	  	
	
  public final SoMFString url = new SoMFString();	

	static int imagedata_maxage = 0;
	static boolean imagetexture_delay_fetch = true;
	
	private SoVRMLImageTextureP pimpl;

	// needed to pass data to a new thread
public static class imagetexture_thread_data {
public
  SoVRMLImageTexture thisp; // ptr
  public String filename;
};

public interface VRMLPrequalifyFileCallback {
	boolean invoke(String str, Object obj,
        SoVRMLImageTexture texture);
}


public static VRMLPrequalifyFileCallback imagetexture_prequalify_cb = null;
public static Object imagetexture_prequalify_closure = null;
	
/*!
  Constructor.
*/
public SoVRMLImageTexture()
{
  pimpl = new SoVRMLImageTextureP(this);

  nodeHeader.SO_VRMLNODE_INTERNAL_CONSTRUCTOR(SoVRMLImageTexture.class);
  nodeHeader.SO_VRMLNODE_ADD_EMPTY_EXPOSED_MFIELD(url,"url");

  pimpl.glimage = null;
  pimpl.glimagevalid = false;
  pimpl.readstatus = 1;
  pimpl.allowprequalifycb = true;
  pimpl.timersensor = 
    new SoTimerSensor(SoVRMLImageTextureP::timersensor_cb, pimpl);
  pimpl.timersensor.setInterval(new SbTime(0.5f));
  
  // use field sensor for url since we will load an image if
  // filename changes. This is a time-consuming task which should
  // not be done in notify().
  pimpl.urlsensor = new SoFieldSensor(SoVRMLImageTexture::urlSensorCB, this);
  pimpl.urlsensor.setPriority(0);
  pimpl.urlsensor.attach(this.url);
  pimpl.isdestructing = false;
}


/*!
  Sets whether the image loading is delayed until the first time the
  image is needed, or if the image is loaded immediately when the
  url field is changed/set. Default value is \e TRUE.
*/
  public static void
  setDelayFetchURL( boolean onoff)
  {
    imagetexture_delay_fetch = onoff;
  }

  static SoGLImage.Wrap
  imagetexture_translate_wrap( boolean repeat)
  {
    if (repeat) return SoGLImage.Wrap.REPEAT;
    return SoGLImage.Wrap.CLAMP_TO_EDGE;
  }

// Doc in parent
  public void
  GLRender(SoGLRenderAction action)
  {
    SoState state = action.getState();

    int unit = SoTextureUnitElement.get(state);
    if ((unit == 0) && SoTextureOverrideElement.getImageOverride(state))
    return;

    float quality = SoTextureQualityElement.get(state);

    pimpl.lock_glimage();

    SoTextureScalePolicyElement.Policy scalepolicy =
          SoTextureScalePolicyElement.get(state);
    boolean needbig = (scalepolicy == SoTextureScalePolicyElement.Policy.FRACTURE);
    boolean isbig =
            pimpl.glimage != null &&
          pimpl.glimage.getTypeId().operator_equal_equal(SoGLBigImage.getClassTypeId());

    if (!pimpl.glimagevalid ||
          (pimpl.glimage == null || (needbig != isbig))) {
    if (pimpl.glimage != null) {
      pimpl.glimage.unref(state);
    }
    if (needbig) {
      pimpl.glimage = new SoGLBigImage();
    }
    else {
      pimpl.glimage = new SoGLImage();
    }
    pimpl.glimagevalid = true;
    if (scalepolicy == SoTextureScalePolicyElement.Policy.SCALE_DOWN) {
      pimpl.glimage.setFlags(pimpl.glimage.getFlags()|SoGLImage.Flags.SCALE_DOWN.getValue());
    }

    pimpl.glimage.setData(pimpl.image,
            imagetexture_translate_wrap(this.repeatS.getValue()),
    imagetexture_translate_wrap(this.repeatT.getValue()),
    quality);
    pimpl.glimage.setEndFrameCallback(SoVRMLImageTexture::glimage_callback, this);

    // don't cache while creating a texture object
    SoCacheElement.setInvalid(true);
    if (state.isCacheOpen()) {
      SoCacheElement.invalidate(state);
    }
  }

    if (pimpl.glimage != null && pimpl.glimage.getTypeId().operator_equal_equal(SoGLBigImage.getClassTypeId())) {
    SoCacheElement.invalidate(state);
  }

    pimpl.unlock_glimage();

    SoGLMultiTextureImageElement.set(state, this, unit,
                                      pimpl.glimage,
          SoMultiTextureImageElement.Model.MODULATE,
          new SbColor(1.0f, 1.0f, 1.0f));

    boolean enable = pimpl.glimage != null &&
          quality > 0.0f &&
          pimpl.glimage.getImage() != null &&
          pimpl.glimage.getImage().hasData();

    SoMultiTextureEnabledElement.set(state,
          this,
          unit,
          enable);

    if (this.isOverride() && (unit == 0)) {
    SoTextureOverrideElement.setImageOverride(state, true);
  }
  }


//
// called when filename changes
//
public static void urlSensorCB(Object data, SoSensor sensor)
{
  SoVRMLImageTexture thisp = (SoVRMLImageTexture) data;
  
  thisp.pimpl.lock_glimage();
  thisp.pimpl.glimagevalid = false;
  thisp.pimpl.unlock_glimage();
  
  thisp.setReadStatus(1);
  if (thisp.url.getNum() != 0 && thisp.url.operator_square_bracket(0).length() != 0 &&
      !thisp.loadUrl()) {
    SoDebugError.postWarning("SoVRMLImageTexture::urlSensorCB",
                              "Image file could not be read: "+
                              thisp.url.operator_square_bracket(0));
    thisp.setReadStatus(0);
  }
  else { // empty image?
    if (thisp.url.getNum() == 0 || thisp.url.operator_square_bracket(0).length() == 0) {
      // wait for threads to finish in case a new thread is used to
      // load the previous image, and the thread has not finished yet.
      if (SoVRMLImageTextureP.scheduler != null) {
        //cc_sched_wait_all(SoVRMLImageTextureP::scheduler); TODO YB
      }

      thisp.pimpl.image.setValuePtr(new SbVec2s((short)0,(short)0), 0, (MemoryBuffer)null); // YB java port
    }
  }
}


// helper function that either loads the image using the default
// loader, or calls the prequalify callback
public boolean readImage( String filename)
{
  boolean retval = true;
  if (pimpl.allowprequalifycb && imagetexture_prequalify_cb != null) {
    retval = imagetexture_prequalify_cb.invoke(filename, imagetexture_prequalify_closure,
                                        this);
  }
  else {
    retval = default_prequalify_cb(filename, null, this); 
  }
  pimpl.lock_glimage();
  pimpl.glimagevalid = false;
  pimpl.unlock_glimage();

  // set flag that timer sensor will test. 
  pimpl.finishedloading = true;
  return retval;
}

public static boolean default_prequalify_cb(String url, Object closure, 
                                          SoVRMLImageTexture thisp)
{
  boolean ret = true;
  if (!SoVRMLImageTextureP.is_exiting && !thisp.pimpl.isdestructing) {
    SbPList sl = SoInput.getDirectories();
    Object[] ptr = sl.getArrayPtr();
    Path[] paths = new Path[ptr.length];
    for( int i=0;i<ptr.length;i++) { paths[i] = (Path)ptr[i];}
    ret = thisp.pimpl.image.readFile(url, paths, sl.getLength());
  }
  return ret;
}


	
/*!
  Sets the read status.
*/
public void setReadStatus(int status)
{
  pimpl.readstatus = status;
}

	//
// Called from readInstance() or when user changes the
// filename field.
//
public boolean loadUrl()
{
  pimpl.lock_glimage();
  pimpl.glimagevalid = false;
  pimpl.unlock_glimage();

  boolean retval = true;
  if (this.url.getNum() != 0 && this.url.operator_square_bracket(0).length() != 0) {
    final SbPList sl = pimpl.searchdirs;
    if (sl.getLength() == 0) { // will be empty if the node isn't read but created in C++
      pimpl.setSearchDirs(SoInput.getDirectories());
    }
    if (imagetexture_delay_fetch) {
      // instruct SbImage to call image_read_cb the first time the image
      // data is requested (typically when some shape using the texture
      // is inside the view frustum).
      retval = pimpl.image.scheduleReadFile(SoVRMLImageTexture::image_read_cb, this,
                                                     this.url.operator_square_bracket(0),
                                                     sl.getArrayPtr(), sl.getLength());
      
    }
    else {
      retval = this.readImage(this.url.operator_square_bracket(0));
    }
  }
  else {
    retval = true;
  }
  return retval;
}

  // sensor callback used for deleting old GLImage instances
  static void
  imagetexture_glimage_delete(Object closure, SoSensor s)
  {
    SoGLImage img = (SoGLImage) closure;
    img.unref(null);
    Destroyable.delete(s);
  }

//
// used for checking if this texture should be purged from memory
//
  public static void
  glimage_callback(Object closure)
  {
    SoVRMLImageTexture thisp = (SoVRMLImageTexture) closure;
    thisp.pimpl.lock_glimage();
    if (thisp.pimpl.glimage != null) {
    int age = thisp.pimpl.glimage.getNumFramesSinceUsed();
    if (age > imagedata_maxage) {
      // we can't delete the glimage here, since it's locked by
      // SoGLImage. Use a sensor to delete it the next time the
      // delayqueue sensors are processed.
      if (thisp.pimpl.glimage != null) {
        thisp.pimpl.glimage.setEndFrameCallback(null, null);
        // allocate new sensor. It will be deleted in the sensor
        // callback. We do this here since this node might be outside
        // the view frustum, and GLRender() may not be called anytime
        // soon.
        SoOneShotSensor s = new SoOneShotSensor(SoVRMLImageTexture::imagetexture_glimage_delete, thisp.pimpl.glimage);
        s.schedule();
        // clear the GLImage in this node. The sensor has a pointer to it and will delete it
        thisp.pimpl.glimage = null;
        thisp.pimpl.glimagevalid = false;
      }
      thisp.pimpl.unlock_glimage();
      thisp.pimpl.image.setValue(new SbVec2s((short)0,(short)0), 0, null);
      thisp.loadUrl();
      return;
    }
  }
    thisp.pimpl.unlock_glimage();
  }


//
// multithread loading thread.
//
public static void read_thread(Object closure)
{
  imagetexture_thread_data data = (imagetexture_thread_data) closure;
  data.thisp.readImage(data.filename);
  // we allocated this before staring the thread
  //delete data; // java port
}

// callback for SoOneShotSensor which is used to read image when
// Coin is compiled without the threads module.
public static void oneshot_readimage_cb(Object closure, SoSensor sensor)
{
  imagetexture_thread_data data = (imagetexture_thread_data) closure;
  data.thisp.readImage(data.filename);
  // delete both the sensor and the data
  Destroyable.delete( sensor);
  //delete data; java port
}



	//
// called (from SbImage) when image data is needed.
//
public static boolean image_read_cb(final String filename, SbImage image, Object closure)
{
  SoVRMLImageTexture thisp = (SoVRMLImageTexture) closure;
  assert(thisp.pimpl.image == image);
  
  // start a timer sensor which polls the thread that loads images, to
  // detect when it's done:
  thisp.pimpl.finishedloading = false; // this will be TRUE when finished
  thisp.pimpl.timersensor.schedule();

  imagetexture_thread_data data = new imagetexture_thread_data();
  data.thisp = thisp;
  data.filename = filename;

  if (SoVRMLImageTextureP.scheduler != null) {
    // use a separate thread to load the image
//    cc_sched_schedule(SoVRMLImageTextureP::scheduler, // TODO YB
//                      SoVRMLImageTexture::read_thread, data, 0);
  }
  else {
    // schedule a sensor to read the image as soon as the delay sensor
    // queue is processed (typically when the runtime system is idle)
    SoOneShotSensor sensor = new SoOneShotSensor(SoVRMLImageTexture::oneshot_readimage_cb, data);
    sensor.schedule();
  }

  return true;
}

	
	
/*!
  \copydetails SoNode::initClass(void)
*/
public static void initClass() // static
{
  SoVRMLImageTextureP.is_exiting = false;
  //SO_NODE_INTERNAL_INIT_CLASS(SoVRMLImageTexture, SO_VRML97_NODE_TYPE);
  SoSubNode.SO__NODE_INIT_CLASS(SoVRMLImageTexture.class, "VRMLImageTexture", SoVRMLTexture.class);
  imagedata_maxage = 500;
  
  SoType type = new SoType(SoVRMLImageTexture.getClassTypeId());
  SoRayPickAction.addMethod(type, SoNode::rayPickS);

  // only use/create scheduler if COIN_THREADSAFE is defined, since we need
  // the mutex below for this to be safely used
//#ifdef COIN_THREADSAFE
//  if (cc_thread_implementation() != CC_NO_THREADS) {
//    SoVRMLImageTextureP::scheduler = cc_sched_construct(1);
//  }
//#endif
//  
//#ifdef COIN_THREADSAFE
//  SoVRMLImageTextureP::glimagemutex = new SbMutex;
//#endif // COIN_THREADSAFE
//
//  coin_atexit((coin_atexit_f *)SoVRMLImageTextureP::cleanup, CC_ATEXIT_NORMAL);
}

}
