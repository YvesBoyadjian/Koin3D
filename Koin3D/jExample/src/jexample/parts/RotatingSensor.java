/**
 * 
 */
package jexample.parts;

import jscenegraph.database.inventor.SbRotation;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoDB;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoRotation;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.database.inventor.sensors.SoSensor;
import jscenegraph.database.inventor.sensors.SoTimerSensor;

/**
 * @author Yves Boyadjian
 *
 */
public class RotatingSensor {
static void rotatingSensorCallback(Object data,
                                   SoSensor sensor)
  {
    // Rotate an object...
    SoRotation myRotation =(SoRotation ) data;
    SbRotation curRotation =
        myRotation.rotation.getValue();
    curRotation = new SbRotation(
            new SbVec3f(0,0,1),(float)Math.PI/90.0f).operator_mul(curRotation);
    myRotation.rotation.setValue(curRotation);
  }

static void schedulingSensorCallback(Object data,
                                     SoSensor sensor)
  { SoTimerSensor rotatingSensor =
        (SoTimerSensor )data;
    rotatingSensor.unschedule();
    if (rotatingSensor.getInterval().operator_equal_equal(1.0f))
      rotatingSensor.setInterval(1.0f/10.0f);
      else
      rotatingSensor.setInterval(1.0f);
    rotatingSensor.schedule();
  }

public static SoNode create(String file) {
	SoSeparator root = new SoSeparator();
    root.ref();
    SoRotation myRotation = new SoRotation();
    root.addChild(myRotation);
    SoTimerSensor rotatingSensor =
      new SoTimerSensor(RotatingSensor::rotatingSensorCallback,
      myRotation);
    rotatingSensor.setInterval(1.0f); 
    rotatingSensor.schedule();
    SoTimerSensor schedulingSensor =
      new SoTimerSensor(RotatingSensor::schedulingSensorCallback,
      rotatingSensor);
    schedulingSensor.setInterval(5.0f);
    schedulingSensor.schedule();
    final SoInput inputFile = new SoInput();
    if ( inputFile.openFile(file) == false ) {
      System.err.println("Could not open file "+file+"\n");
      System.exit(1); }
    root.addChild(SoDB.readAll(inputFile));
    
    inputFile.destructor();
    return root;
}
}
