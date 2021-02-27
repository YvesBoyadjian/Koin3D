/**
 * 
 */
package jexample.parts;

import jscenegraph.database.inventor.SoDB;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.engines.SoBoolOperation;
import jscenegraph.database.inventor.engines.SoElapsedTime;
import jscenegraph.database.inventor.engines.SoGate;
import jscenegraph.database.inventor.events.SoEvent;
import jscenegraph.database.inventor.events.SoMouseButtonEvent;
import jscenegraph.database.inventor.fields.SoMFFloat;
import jscenegraph.database.inventor.nodes.SoCylinder;
import jscenegraph.database.inventor.nodes.SoDirectionalLight;
import jscenegraph.database.inventor.nodes.SoEventCallback;
import jscenegraph.database.inventor.nodes.SoMaterial;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoPerspectiveCamera;
import jscenegraph.database.inventor.nodes.SoRotationXYZ;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.database.inventor.nodes.SoSphere;
import jscenegraph.database.inventor.nodes.SoTransform;
import jscenegraph.database.inventor.nodes.SoTranslation;

/**
 * @author Yves Boyadjian
 *
 */
public class Canards {

static void myMousePressCB(Object userData,
               SoEventCallback eventCB){
  SoGate gate =(SoGate ) userData;
  SoEvent event = eventCB.getEvent();
  if (SoMouseButtonEvent.SO_MOUSE_PRESS_EVENT(event,SoMouseButtonEvent.Button.ANY)) {
    gate.enable.setValue(!gate.enable.getValue());
    eventCB.setHandled(); } 
}

	public static SoNode create(String file) {
		SoSeparator root = new SoSeparator();
  root.ref();
  SoPerspectiveCamera myCamera =
      new SoPerspectiveCamera();
  myCamera.position.setValue(0.f,-4.f,8.0f);
  myCamera.heightAngle.setValue( (float)Math.PI/2.5f); 
  myCamera.nearDistance.setValue( 1.0f);
  myCamera.farDistance.setValue(15.0f);
  root.addChild(myCamera);
  root.addChild(new SoDirectionalLight());
  SoRotationXYZ globalRotXYZ = new SoRotationXYZ();
  globalRotXYZ.axis.setValue(SoRotationXYZ.Axis.X);
  globalRotXYZ.angle.setValue((float)Math.PI/9);
  root.addChild(globalRotXYZ);
  SoSeparator pond = new SoSeparator();
  root.addChild(pond);
  SoTranslation pondTranslation = new SoTranslation();
  pondTranslation.translation.setValue(0.f,-6.725f,0.f);
  pond.addChild(pondTranslation);
  SoMaterial waterMaterial = new SoMaterial();
  waterMaterial.diffuseColor.setValue(0.f, 0.3f, 0.8f);
  pond.addChild(waterMaterial);
  SoCylinder waterCylinder = new SoCylinder();
  waterCylinder.radius.setValue(4.0f);
  waterCylinder.height.setValue(0.5f);
  pond.addChild(waterCylinder);
  SoMaterial rockMaterial = new SoMaterial();
  rockMaterial.diffuseColor.setValue(0.8f,0.23f,0.03f);
  pond.addChild(rockMaterial);
  SoSphere rockSphere = new SoSphere();
  rockSphere.radius.setValue(0.9f);
  pond.addChild(rockSphere);
  final SoInput myInput = new SoInput();
  if (!myInput.openFile(file)) System.exit (1);
  SoSeparator duckObject = SoDB.readAll(myInput);
  if (duckObject == null) System.exit (1);
  SoSeparator bDuck = new SoSeparator();
  root.addChild(bDuck);
  SoRotationXYZ bDuckRotXYZ = new SoRotationXYZ();
  bDuck.addChild(bDuckRotXYZ);
  SoTransform bInitTransform = new SoTransform();
  bInitTransform.translation.setValue(0.f,0.f,3.5f);
  bInitTransform.scaleFactor.setValue(6.f,6.f,6.f);
  bDuck.addChild(bInitTransform);
  bDuck.addChild(duckObject);
  SoSeparator sDuck = new SoSeparator();
  root.addChild(sDuck);
  SoRotationXYZ sDuckRotXYZ = new SoRotationXYZ();
  sDuck.addChild(sDuckRotXYZ);
  SoTransform sInitTransform =
      new SoTransform();
  sInitTransform.translation.setValue(0.f,-2.2f,1.5f);
  sInitTransform.scaleFactor.setValue(4.f,4.f,4.f);
  sDuck.addChild(sInitTransform);
  sDuck.addChild(duckObject);
  SoGate bDuckGate =
      new SoGate(SoMFFloat.getClassTypeId(SoMFFloat.class));
  SoElapsedTime bDuckTime = new SoElapsedTime();
  bDuckGate.input.connectFrom(bDuckTime.timeOut); 
  bDuckRotXYZ.axis.setValue(SoRotationXYZ.Axis.Y);
  bDuckRotXYZ.angle.connectFrom(bDuckGate.output);
  SoEventCallback myEventCB = new SoEventCallback();
  myEventCB.addEventCallback(
          SoMouseButtonEvent.getClassTypeId(),
          Canards::myMousePressCB,bDuckGate);
  root.addChild(myEventCB);
  SoBoolOperation myBoolean = new SoBoolOperation();
  myBoolean.a.connectFrom(bDuckGate.enable);
  myBoolean.operation.setValue(SoBoolOperation.Operation.NOT_A.ordinal());
  SoGate sDGate =
      new SoGate(SoMFFloat.getClassTypeId(SoMFFloat.class));
  SoElapsedTime sDuckTime = new SoElapsedTime();
  sDGate.input.connectFrom(sDuckTime.timeOut);
  sDGate.enable.connectFrom(myBoolean.output); 
  sDuckRotXYZ.axis.setValue(SoRotationXYZ.Axis.Y);  // Y axis
  sDuckRotXYZ.angle.connectFrom(sDGate.output);
  
  myInput.destructor();
  return root;
	}
}
