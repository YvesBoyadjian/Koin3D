/**
 * 
 */
package jscenegraph.coin3d.inventor.VRMLnodes;

import jscenegraph.coin3d.inventor.SbImage;
import jscenegraph.coin3d.inventor.misc.SoGLImage;
import jscenegraph.database.inventor.SbStringList;
import jscenegraph.database.inventor.sensors.SoFieldSensor;
import jscenegraph.database.inventor.sensors.SoSensor;
import jscenegraph.database.inventor.sensors.SoTimerSensor;

/**
 * @author BOYADJIAN
 *
 */
public class SoVRMLImageTextureP {

	  SoVRMLImageTexture master; //ptr
  
  int readstatus;
  SoGLImage glimage; //ptr
  // we don't want to delete or update the glimage in the scheduler thread, so use this
  // member to store whether we need to recreate the glimage in the next GLRender() pass
  boolean glimagevalid; 
  final SbImage image = new SbImage();
  SoFieldSensor urlsensor; // ptr
  boolean allowprequalifycb;

  SoTimerSensor timersensor; //ptr
  boolean finishedloading;
  //static void timersensor_cb(void * data, SoSensor * sensor);

  //void readimage_cleanup(void);
  boolean isdestructing;

  final SbStringList searchdirs = new SbStringList();

	
	public SoVRMLImageTextureP(SoVRMLImageTexture master) {
		this.master = master;
	}

	public static boolean is_exiting;
	public static Object scheduler; //ptr

public static void timersensor_cb(Object data, SoSensor sensor)
{
  SoVRMLImageTextureP thisp = (SoVRMLImageTextureP) data;
  
  if (thisp.finishedloading) {
    thisp.master.touch(); // trigger redraw
    thisp.timersensor.unschedule();
  }
}

public void lock_glimage() {
	// TODO Auto-generated method stub
	
}

public void unlock_glimage() {
	// TODO Auto-generated method stub
	
}

public void clearSearchDirs() {
    int n = this.searchdirs.getLength();
    for (int i = 0; i < n; i++) {
      //delete this.searchdirs.operator_square_bracket(i); // java port
    }
    this.searchdirs.truncate(0);
  }

public void setSearchDirs(final SbStringList sl) {
    this.clearSearchDirs();
    int n = sl.getLength();
    for (int i = 0; i < n; i++) {
      this.searchdirs.append(sl.operator_square_bracket(i));
    }
  }

}
