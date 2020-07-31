/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.coin3d.inventor.SbImage;
import jscenegraph.database.inventor.SbStringList;
import jscenegraph.database.inventor.SbTime;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoRayPickAction;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFString;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.database.inventor.sensors.SoFieldSensor;
import jscenegraph.database.inventor.sensors.SoOneShotSensor;
import jscenegraph.database.inventor.sensors.SoSensor;
import jscenegraph.database.inventor.sensors.SoTimerSensor;
import jscenegraph.port.Destroyable;
import jscenegraph.port.memorybuffer.MemoryBuffer;

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
    SbStringList sl = SoInput.getDirectories();
    ret = thisp.pimpl.image.readFile(url, sl.getArrayPtr(), sl.getLength());
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
    final SbStringList sl = pimpl.searchdirs;
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
