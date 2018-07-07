/**
 * 
 */
package jexample.parts;

import jscenegraph.database.inventor.SbColor;
import jscenegraph.database.inventor.SoPath;
import jscenegraph.database.inventor.actions.SoSearchAction;
import jscenegraph.database.inventor.nodes.SoCube;
import jscenegraph.database.inventor.nodes.SoMaterial;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.database.inventor.nodes.SoSphere;
import jscenegraph.database.inventor.nodes.SoTransform;
import jscenegraph.interaction.inventor.draggers.SoDragger;
import jscenegraph.interaction.inventor.manips.SoHandleBoxManip;
import jscenegraph.interaction.inventor.manips.SoTrackballManip;
import jscenegraph.interaction.inventor.manips.SoTransformBoxManip;
import jscenegraph.interaction.inventor.nodes.SoSelection;

/**
 * @author Yves Boyadjian
 *
 */
public class PickTrackball {

	SoSeparator root;
	SoHandleBoxManip myHandleBox;
	SoTrackballManip myTrackball;
	SoTransformBoxManip myTransformBox;
	SoPath cubeXformPath = null;
	SoPath sphereXformPath = null;
	
	final SoSearchAction rech = new SoSearchAction();
	
	public SoSeparator createScene() {
		SoSelection selectionRoot = new SoSelection();
		selectionRoot.ref();
		selectionRoot.addSelectionCallback(PickTrackball::selectionCallback,this);
		selectionRoot.addDeselectionCallback(PickTrackball::deselectionCallback,this);
		
		root = new SoSeparator();
		selectionRoot.addChild(root);
		
		SoSeparator cubeRoot = new SoSeparator();
		SoTransform cubeXform = new SoTransform();
		cubeXform.translation.setValue(-4,0,0);
		root.addChild(cubeRoot);
		cubeRoot.addChild(cubeXform);
		
		SoMaterial cubeMat = new SoMaterial();
		cubeMat.diffuseColor.setValue(.8f,.8f,.8f);
		cubeRoot.addChild(cubeMat);
		SoCube cube = new SoCube();
		cubeRoot.addChild(cube);
		rech.setNode(cubeXform);
		rech.apply(root);
		cubeXformPath = rech.getPath();
		cubeXformPath.ref();
		
		SoSeparator sphereRoot = new SoSeparator();
		SoTransform sphereXform = new SoTransform();
		SoMaterial sphereMat = new SoMaterial();
		
		root.addChild(sphereRoot);
		sphereRoot.addChild(sphereXform);
		sphereRoot.addChild(sphereMat);
		
		SoSphere sphere = new SoSphere();
		sphereRoot.addChild(sphere);
		sphereMat.diffuseColor.setValue(.8f, .8f, .8f);
		rech.setNode(sphereXform);
		rech.apply(root);
		sphereXformPath = rech.getPath();
		sphereXformPath.ref();
		
		myHandleBox = new SoHandleBoxManip();
		myHandleBox.ref();
		myTrackball = new SoTrackballManip();
		myTrackball.ref();
		myTransformBox = new SoTransformBoxManip();
		myTransformBox.ref();
		
		SoDragger myDragger;
		myDragger = myTrackball.getDragger();
		myDragger.addStartCallback(PickTrackball::dragStartCallback,cubeMat);
		myDragger.addFinishCallback(PickTrackball::dragFinishCallback,cubeMat);
		
		myDragger = myHandleBox.getDragger();
		myDragger.addStartCallback(PickTrackball::dragStartCallback,sphereMat);
		myDragger.addFinishCallback(PickTrackball::dragFinishCallback,sphereMat);
		
		return selectionRoot;
	}
	
	public static void selectionCallback(Object object, SoPath selectionPath) {
		PickTrackball pickTrackball = (PickTrackball)object;
		if(selectionPath.getTail().isOfType(SoSphere.getClassTypeId())) {
			pickTrackball.myHandleBox.replaceNode(pickTrackball.sphereXformPath);
		}
		else if(selectionPath.getTail().isOfType(SoCube.getClassTypeId())) {
			pickTrackball.myTrackball.replaceNode(pickTrackball.cubeXformPath);
		}
	}
	
	public static void deselectionCallback(Object object, SoPath deselectionPath) {
		PickTrackball pickTrackball = (PickTrackball)object;
		if(deselectionPath.getTail().isOfType(SoSphere.getClassTypeId())) {
			pickTrackball.myHandleBox.replaceManip(pickTrackball.sphereXformPath, null);
		}
		else if(deselectionPath.getTail().isOfType(SoCube.getClassTypeId())) {
			pickTrackball.myTrackball.replaceManip(pickTrackball.cubeXformPath, null);
		}
	}
	
	public static void dragStartCallback(Object myMaterial, SoDragger dragger) {
		((SoMaterial)myMaterial).diffuseColor.setValue(new SbColor(1.0f,.2f,.2f));
	}
	
	public static void dragFinishCallback(Object myMaterial, SoDragger dragger) {
		((SoMaterial)myMaterial).diffuseColor.setValue(new SbColor(.8f,.8f,.8f));
	}
}

