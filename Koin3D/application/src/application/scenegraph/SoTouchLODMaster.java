/**
 * 
 */
package application.scenegraph;

import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.nodes.SoCamera;
import jscenegraph.database.inventor.nodes.SoGroup;
import jscenegraph.database.inventor.nodes.SoNode;

import java.util.ArrayList;
import java.util.List;

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

	private float lodFactor;

	private List<SoTouchLODSlave> slaves = new ArrayList<>();

	public interface SoTouchLODSlave {
		void setLodFactor(float lodFactor);
	}
	
	public SoTouchLODMaster(String name) {
		this.name = name;
	}

	public void register(SoTouchLODSlave slave) {
		slaves.add(slave);
	}

	public void unregister(SoTouchLODSlave slave) {
		slaves.remove(slave);
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

	public void setLodFactor(float lodFactor) {
		this.lodFactor = lodFactor;
		for(SoTouchLODSlave slave:slaves) {
			slave.setLodFactor(lodFactor);
		}
	}

	public float getLodFactor() {
		return lodFactor;
	}
}
