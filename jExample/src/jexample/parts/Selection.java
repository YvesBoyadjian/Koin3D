/**
 * 
 */
package jexample.parts;

import jscenegraph.database.inventor.SoDB;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.SoPath;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoLineHighlightRenderAction;
import jscenegraph.database.inventor.actions.SoSearchAction;
import jscenegraph.database.inventor.nodes.SoCube;
import jscenegraph.database.inventor.nodes.SoMaterial;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.database.inventor.nodes.SoSphere;
import jscenegraph.database.inventor.nodes.SoTransform;
import jscenegraph.interaction.inventor.nodes.SoSelection;
import jsceneviewer.inventor.qt.viewers.SoQtExaminerViewer;

/**
 * @author Yves Boyadjian
 *
 */
public class Selection {

	public static SoSeparator createScene(SoQtExaminerViewer viewer) {
		
		SoSeparator scene;
		
		scene = readFile("C:/eclipseWorkspaces/examples_iv/transparentbluecylinder.iv");
		if(scene == null) {
			System.exit(1);
		}
		SoSelection selectionRoot = new SoSelection();
		selectionRoot.ref();
		selectionRoot.addChild(scene);
		
		viewer.getSceneHandler().redrawOnSelectionChange(selectionRoot);
		
		SoLineHighlightRenderAction lineHL = new SoLineHighlightRenderAction();
		viewer.getSceneHandler().setGLRenderAction(lineHL);
		
		return selectionRoot;
	}
	
	private static SoSeparator readFile(String filename) {
		final SoInput mySceneInput = new SoInput();
		
		if(!mySceneInput.openFile(filename)) {
			System.err.println("Cannot open file "+filename);
			return null;
		}
		SoSeparator myGraph = SoDB.readAll(mySceneInput);
		if(myGraph == null) {
			System.err.println("Problem reading file "+filename);
			return null;
		}
		mySceneInput.closeFile();
		
		mySceneInput.destructor();
		return myGraph;
	}
	
	public static final SoSeparator createSceneSelection() {
		SoSelection selectionRoot = new SoSelection();		
		selectionRoot.ref();
		selectionRoot.addSelectionCallback((a,b)->selectionCallback(a,b),null);
		selectionRoot.addDeselectionCallback((a,b)->deselectionCallback(a,b),null);
		
		SoSeparator root = new SoSeparator();
		selectionRoot.addChild(root);
		
		SoSeparator cubeRoot = new SoSeparator();
		SoTransform cubeXform = new SoTransform();
		cubeXform.translation.setValue(-1,0,0);
		cubeRoot.addChild(cubeXform);
		
		SoMaterial cubeMat = new SoMaterial();
		cubeMat.diffuseColor.setValue(.8f,.1f,.1f);
		cubeRoot.addChild(cubeMat);
		cubeRoot.addChild(new SoCube());
		root.addChild(cubeRoot);
		
		SoSeparator sphereRoot = new SoSeparator();
		SoTransform sphereXform = new SoTransform();
		sphereXform.translation.setValue(1,0,0);
		sphereRoot.addChild(sphereXform);
		
		SoMaterial sphereMat = new SoMaterial();
		sphereMat.diffuseColor.setValue(.1f,.1f,.8f);
		sphereRoot.addChild(sphereMat);
		sphereRoot.addChild(new SoSphere());
		root.addChild(sphereRoot);
		
		
		return selectionRoot;
	}
	
	public static void selectionCallback(Object obj, SoPath selectionPath) {
		SoMaterial mat = (SoMaterial) searchLastType(selectionPath, SoMaterial.getClassTypeId());
		if(mat != null) {
			mat.emissiveColor.setValue(0.8f,0,0.8f);
		}
	}
	
	public static void deselectionCallback(Object obj, SoPath selectionPath) {
		SoMaterial mat = (SoMaterial) searchLastType(selectionPath, SoMaterial.getClassTypeId());
		if(mat != null) {
			mat.emissiveColor.setValue(0.0f,0,0.0f);
		}
	}
	
	private static SoNode searchLastType(SoPath p, SoType t) {
		final SoSearchAction sa = new SoSearchAction();
		sa.setType(t);
		sa.setInterest(SoSearchAction.Interest.LAST);
		sa.apply(p);
		SoPath outPath = sa.getPath();
		
		SoNode result = null;
		if(outPath != null && outPath.getLength() > 0) {
			result = outPath.getTail();
		}
		
		sa.destructor();
		return result;
	}
}
