/**
 * 
 */
package application;

import application.viewer.glfw.SoQtWalkViewer;
import jscenegraph.database.inventor.SbViewportRegion;
import jscenegraph.database.inventor.SoPath;
import jscenegraph.database.inventor.SoPickedPoint;
import jscenegraph.database.inventor.actions.SoRayPickAction;
import jscenegraph.database.inventor.nodes.SoCube;
import jscenegraph.database.inventor.nodes.SoGroup;
import jscenegraph.database.inventor.nodes.SoMaterial;
import jscenegraph.database.inventor.nodes.SoNode;

import javax.swing.*;

/**
 * @author Yves Boyadjian
 *
 */
public class TargetSearchRunnable implements Runnable {
	
	SoQtWalkViewer v;
	SbViewportRegion vr;
	SoNode sg;
	
	public TargetSearchRunnable(SoQtWalkViewer v, SbViewportRegion vr, SoNode sg) {
		this.v = v;
		this.vr = vr;
		this.sg = sg;
	}

	@Override
	public void run() {
		Thread pickThread = new Thread() {
			public void run() {
				SoRayPickAction fireAction = new SoRayPickAction(vr);
				//fireAction.setRay(new SbVec3f(0.0f,0.0f,0.0f), new SbVec3f(0.0f,0.0f,-1.0f),0.1f,1000f);
				fireAction.setPoint(vr.getViewportSizePixels().operator_div(2));
				fireAction.setRadius(2.0f);
				fireAction.apply(sg);
				SoPickedPoint pp = fireAction.getPickedPoint();
				if( pp == null) {
					fireAction.destructor();
					return;
				}
				SwingUtilities.invokeLater(()-> {
					SoPath p = pp.getPath();
					if( p != null) {
						SoNode n = p.getTail();
						if( n.isOfType(SoCube.getClassTypeId())) {
							int len = p.getLength();
							if( len > 1) {
								SoNode parent = p.getNode(len-2);
								if(parent.isOfType(SoGroup.getClassTypeId())) {
									v.addOneShotIdleListener((viewer1)->{
										SoGroup g = (SoGroup)parent;
										SoMaterial c = new SoMaterial();
										c.diffuseColor.setValue(1, 0, 0);
										g.enableNotify(false);
										g.insertChild(c, 0);
										g.enableNotify(true);
									});
								}
							}
							//System.out.println(pp.getPath().getTail().getClass());
						}
					}
					fireAction.destructor();
				});
			}
		};
		//pickThread.start();
		pickThread.run(); // No need now to do multithreading
	}

}
