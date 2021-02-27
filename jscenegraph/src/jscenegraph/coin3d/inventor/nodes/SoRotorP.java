/**
 * 
 */
package jscenegraph.coin3d.inventor.nodes;

import jscenegraph.database.inventor.SbRotation;
import jscenegraph.database.inventor.SbTime;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoDB;
import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.database.inventor.fields.SoSFTime;
import jscenegraph.database.inventor.sensors.SoFieldSensor;
import jscenegraph.database.inventor.sensors.SoSensor;

/**
 * @author Yves Boyadjian
 *
 */
public class SoRotorP {


  final SbTime starttime = new SbTime();
  final SbVec3f startaxis = new SbVec3f();
  float startangle;
  SoFieldSensor rtfieldsensor; //ptr
  SoFieldSensor onfieldsensor; //ptr
  SoFieldSensor rotfieldsensor; //ptr
  SoFieldSensor speedfieldsensor; //ptr

  SoRotor master;


public SoRotorP(SoRotor soRotor) {
	master = soRotor;
}


// detects when some field changes value
static void fieldSensorCB(Object d, SoSensor s)
{
  SoRotor thisp = (SoRotor) d;

  if (s == thisp.pimpl.onfieldsensor) {
    if (thisp.on.getValue() == true) {
    	thisp.pimpl.startangle = thisp.rotation.getValue(thisp.pimpl.startaxis);
      thisp.pimpl.starttime.copyFrom(get_current_time());
    }
  }
  else if (s == thisp.pimpl.speedfieldsensor) {
	  thisp.pimpl.startangle = thisp.rotation.getValue(thisp.pimpl.startaxis);
    thisp.pimpl.starttime.copyFrom(get_current_time());
  }
  else if (s == thisp.pimpl.rotfieldsensor) {
	  thisp.pimpl.startangle = thisp.rotation.getValue(thisp.pimpl.startaxis);
    thisp.pimpl.starttime.copyFrom(get_current_time());
  }
}


static void rtFieldSensorCB(Object d, SoSensor sensor)
{
  SoRotor thisp = (SoRotor) d;
  // got to check value of on field here in case rtfieldsensor
  // triggers before onfieldsensor.
  if (thisp.on.getValue()) {
    thisp.pimpl.setRotation();
  }
}

// sets rotation based on time passed from starttime
void setRotation()
{
	double M_PI = Math.PI;
	
  if (this.starttime.operator_equal_equal(SbTime.zero())) {
    // don't do anything first time we get here
    this.starttime.copyFrom( get_current_time() );
    return;
  }
  final SbTime difftime = get_current_time().operator_minus( this.starttime );

  float diffangle = (float)
    (difftime.getValue() *
     ((double)master.speed.getValue()) * M_PI * 2.0);

  float angle = this.startangle + diffangle;

  if (angle < 0.0f) {
    angle = (float) (2.0 * M_PI - ((double)-angle % M_PI*2.0));
  }
  if (angle > M_PI * 2.0f) {
    angle = (float) ((double)angle % (M_PI * 2.0));
  }
  
  this.rotfieldsensor.detach();
  master.rotation.setValue(new SbRotation(this.startaxis, angle));
  this.rotfieldsensor.attach(master.rotation);
}

//
// returns the current time. First tries the realTime field, then
// SbTime::getTimeOfDay() if field is not found.
//
static SbTime
get_current_time()
{
  SoField realtime = SoDB.getGlobalField("realTime");
  if (realtime!=null && realtime.isOfType(SoSFTime.getClassTypeId(SoSFTime.class))) {
    return ((SoSFTime)realtime).getValue();
  }
  return SbTime.getTimeOfDay();
}


}
