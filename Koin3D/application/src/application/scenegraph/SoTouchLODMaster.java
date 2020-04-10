/**
 * 
 */
package application.scenegraph;

import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.nodes.SoCamera;
import jscenegraph.database.inventor.nodes.SoGroup;
import jscenegraph.database.inventor.nodes.SoNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoTouchLODMaster extends SoNode {
	
	private final static int MAX_CHANGE = 3;
	
	private int counter;
	
	private SoCamera camera;
	
	private boolean firstRender = true;
	
	private String name;
	
	public SoTouchLODMaster(String name) {
		this.name = name;
	}

	private void reset() {
		counter = firstRender ? -9999 : 0;
		SoRecursiveIndexedFaceSet.nbDoLoad = firstRender ? -999999 : -2;
	}
	
	public void increment() {
		counter++;
	}
	
	public int getCount() {
		return counter;
	}
	
	public int getMaxChange() {
		return MAX_CHANGE;
	}
	
	public void GLRender(SoGLRenderAction action) {
		
		reset();
		super.GLRender(action);
		
//		if( SoRecursiveIndexedFaceSet.nbDoLoad != 0)
//		System.out.println(name+" : nbLoad : "+SoRecursiveIndexedFaceSet.nbDoLoad);
		
		if(firstRender) {
			firstRender = false;
		}
	}
	public void setCamera(SoCamera camera) {
		this.camera = camera;
	}

	public SoCamera getCamera() {
		return camera;
	}
}
