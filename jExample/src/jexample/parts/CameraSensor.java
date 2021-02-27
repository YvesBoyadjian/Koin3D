/**
 * 
 */
package jexample.parts;

import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.nodes.SoCamera;
import jscenegraph.database.inventor.sensors.SoFieldSensor;
import jscenegraph.database.inventor.sensors.SoSensor;
import jsceneviewer.inventor.qt.viewers.SoQtExaminerViewer;

/**
 * @author Yves Boyadjian
 *
 */
public class CameraSensor {

	//Attachement d’un senseur de champ à la position d’une caméra. La fonction associée affiche la position de la caméra.
	public static void cameraChangedCB(Object data,SoSensor sensor)
	  { SoCamera viewerCamera = (SoCamera )data;
	    SbVec3f cameraPosition =
	        viewerCamera.position.getValue();
	    System.out.println("Camera position: ("+cameraPosition.operator_square_bracket(0)+","+cameraPosition.operator_square_bracket(1)+","+cameraPosition.operator_square_bracket(2)+")\n"); 
	  }
	
	public static void attach(SoQtExaminerViewer myViewer) {
		SoCamera camera = myViewer.getCameraController().getCamera();
	    SoFieldSensor mySensor =
	        new SoFieldSensor(CameraSensor::cameraChangedCB, camera);
	    mySensor.attach(camera.position);
	}
}
