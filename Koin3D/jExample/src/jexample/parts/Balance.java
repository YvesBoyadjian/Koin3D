/**
 * 
 */
package jexample.parts;

import jscenegraph.database.inventor.SbRotation;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbViewportRegion;
import jscenegraph.database.inventor.events.SoEvent;
import jscenegraph.database.inventor.events.SoKeyboardEvent;
import jscenegraph.database.inventor.nodes.SoCone;
import jscenegraph.database.inventor.nodes.SoCube;
import jscenegraph.database.inventor.nodes.SoCylinder;
import jscenegraph.database.inventor.nodes.SoEventCallback;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoPerspectiveCamera;
import jscenegraph.database.inventor.nodes.SoText2;
import jscenegraph.database.inventor.nodes.SoTransform;
import jscenegraph.nodekits.inventor.nodekits.SoBaseKit;
import jscenegraph.nodekits.inventor.nodekits.SoCameraKit;
import jscenegraph.nodekits.inventor.nodekits.SoLightKit;
import jscenegraph.nodekits.inventor.nodekits.SoSceneKit;
import jscenegraph.nodekits.inventor.nodekits.SoShapeKit;
import jsceneviewer.inventor.qt.SoQtRenderArea;

/**
 * @author Yves Boyadjian
 *
 */
public class Balance {

	public static SoNode createScene(SoQtRenderArea myRenderArea) {
		
		   SoSceneKit myScene = new SoSceneKit();
		   myScene.ref();

		   myScene.setPart("lightList[0]", new SoLightKit());
		   myScene.setPart("cameraList[0]", new SoCameraKit());
		   myScene.setCameraNumber(0);

		   // Create the Balance Scale -- put each part in the 
		   // childList of its parent, to build up this hierarchy:
		   //
		   //                    myScene
		   //                       |
		   //                     support
		   //                       |
		   //                     beam
		   //                       |
		   //                   --------
		   //                   |       |
		   //                string1  string2
		   //                   |       |
		   //                tray1     tray2

		   SoShapeKit support = new SoShapeKit();
		   support.setPart("shape", new SoCone());
		   support.set("shape { height 3 bottomRadius .3 }");
		   myScene.setPart("childList[0]", support);

		   SoShapeKit beam = new SoShapeKit();
		   beam.setPart("shape", new SoCube());
		   beam.set("shape { width 3 height .2 depth .2 }");
		   beam.set("transform { translation 0 1.5 0 } ");
		   support.setPart("childList[0]", beam);

		   SoShapeKit string1 = new SoShapeKit();
		   string1.setPart("shape", new SoCylinder());
		   string1.set("shape { radius .05 height 2}");
		   string1.set("transform { translation -1.5 -1 0 }");
		   string1.set("transform { center 0 1 0 }");
		   beam.setPart("childList[0]", string1);

		   SoShapeKit string2 = new SoShapeKit();
		   string2.setPart("shape", new SoCylinder());
		   string2.set("shape { radius .05 height 2}");
		   string2.set("transform { translation 1.5 -1 0 } ");
		   string2.set("transform { center 0 1 0 } ");
		   beam.setPart("childList[1]", string2);

		   SoShapeKit tray1 = new SoShapeKit();
		   tray1.setPart("shape", new SoCylinder());
		   tray1.set("shape { radius .75 height .1 }");
		   tray1.set("transform { translation 0 -1 0 } ");
		   string1.setPart("childList[0]", tray1);

		   SoShapeKit tray2 = new SoShapeKit();
		   tray2.setPart("shape", new SoCylinder());
		   tray2.set("shape { radius .75 height .1 }");
		   tray2.set("transform { translation 0 -1 0 } ");
		   string2.setPart("childList[0]", tray2);

		   // Add EventCallback so Balance Responds to Events
		   SoEventCallback myCallbackNode = new SoEventCallback();
		   myCallbackNode.addEventCallback(
		        SoKeyboardEvent.getClassTypeId(), 
		        Balance::tipTheBalance, support); 
		   support.setPart("callbackList[0]", myCallbackNode);

		   // Add Instructions as Text in the Scene...
		   SoShapeKit myText = new SoShapeKit();
		   myText.setPart("shape", new SoText2());
		   myText.set("shape { string \"Press Left or Right Arrow Key\" }");
		   myText.set("shape { justification CENTER }");
		   myText.set("font { name \"Helvetica-Bold\" }");
		   myText.set("font { size 16.0 }");
		   myText.set("transform { translation 0 -2 0 }");
		   myScene.setPart("childList[1]", myText);

		   // Get camera from scene and tell it to viewAll...
		   SoPerspectiveCamera myCamera = (SoPerspectiveCamera)SoBaseKit.SO_GET_PART(myScene,
		      "cameraList[0].camera", SoPerspectiveCamera.class);
		   myCamera.viewAll(myScene, new SbViewportRegion());

		   return myScene;
	}
	// Callback Function for Animating the Balance Scale.
	// -- used to make the balance tip back and forth
	// -- Note: this routine is only called in response to KeyPress
//	    events since the call 'setEventInterest(KeyPressMask)' is
//	    made on the SoEventCallback node that uses it.
	// -- The routine checks if the key pressed was left arrow (which
//	    is XK_Left in X-windows talk), or right arrow (which is
//	    XK_Right)
	// -- The balance is made to tip by rotating the beam part of the
//	    scale (to tip it) and then compensating (making the strings
//	    vertical again) by rotating the string parts in the opposite
//	    direction.
	static void
	tipTheBalance(
	   Object userData, // The nodekit representing 'support', the
	                   // fulcrum of the balance. Passed in during
	                   // main routine, below. 
	   SoEventCallback eventCB)
	{
	   SoEvent ev = eventCB.getEvent();
	   
	   // Which Key was pressed?
	   // If Right or Left Arrow key, then continue...
	   if (SoKeyboardEvent.SO_KEY_PRESS_EVENT(ev, SoKeyboardEvent.Key.RIGHT_ARROW) || 
			   SoKeyboardEvent.SO_KEY_PRESS_EVENT(ev, SoKeyboardEvent.Key.LEFT_ARROW)) {
	      SoShapeKit  support, beam1, string1, string2;
	      final SbRotation  startRot = new SbRotation(), beamIncrement = new SbRotation(), stringIncrement = new SbRotation();

	      // Get the different nodekits from the userData.
	      support = (SoShapeKit ) userData;

	      // These three parts are extracted based on knowledge of the
	      // motion hierarchy (see the diagram in the main routine.
	      beam1   = (SoShapeKit ) support.getPart("childList[0]",true);
	      string1 = (SoShapeKit )   beam1.getPart("childList[0]",true);
	      string2 = (SoShapeKit )   beam1.getPart("childList[1]",true);

	      // Set angular increments to be .1 Radians about the Z-Axis
	      // The strings rotate opposite the beam, and the two types
	      // of key press produce opposite effects.
	      if (SoKeyboardEvent.SO_KEY_PRESS_EVENT(ev, SoKeyboardEvent.Key.RIGHT_ARROW)) {
	         beamIncrement.setValue(new SbVec3f(0, 0, 1), -.1f);
	         stringIncrement.setValue(new SbVec3f(0, 0, 1), .1f);
	      } 
	      else {
	         beamIncrement.setValue(new SbVec3f(0, 0, 1), .1f);
	         stringIncrement.setValue(new SbVec3f(0, 0, 1), -.1f);
	      }

	      // Use SO_GET_PART to find the transform for each of the 
	      // rotating parts and modify their rotations.

	      SoTransform xf;
	      xf = (SoTransform)SoBaseKit.SO_GET_PART(beam1, "transform", SoTransform.class);
	      startRot.copyFrom( xf.rotation.getValue());
	      xf.rotation.setValue(startRot.operator_mul(  beamIncrement));

	      xf = (SoTransform)SoBaseKit.SO_GET_PART(string1, "transform", SoTransform.class);
	      startRot.copyFrom( xf.rotation.getValue());
	      xf.rotation.setValue(startRot.operator_mul(  stringIncrement));

	      xf = (SoTransform)SoBaseKit.SO_GET_PART(string2, "transform", SoTransform.class);
	      startRot.copyFrom( xf.rotation.getValue());
	      xf.rotation.setValue(startRot.operator_mul(  stringIncrement));

	      eventCB.setHandled();
	   }
	}

}
