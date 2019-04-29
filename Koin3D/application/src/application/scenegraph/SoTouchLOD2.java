/**
 * 
 */
package application.scenegraph;

import jscenegraph.coin3d.inventor.nodes.SoLOD;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.nodes.SoGroup;
import jscenegraph.database.inventor.nodes.SoIndexedFaceSet;
import jscenegraph.database.inventor.nodes.SoNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoTouchLOD2 extends SoLOD {

	private int previousChild = -1;
	
	protected int
	whichToTraverse(SoAction action)
	{
		int newChild = super.whichToTraverse(action);
		if(previousChild == 0) {
			if(newChild != previousChild) {
				SoGroup group = (SoGroup) getChild(previousChild);
				for(int i=0;i<4;i++) {
					SoNode child = group.getChild(i);
					if(child instanceof SoRecursiveIndexedFaceSet) {
					
						SoRecursiveIndexedFaceSet SoIndexedFaceSet = (SoRecursiveIndexedFaceSet)child;
						SoIndexedFaceSet.clear();
					}
				}
			}
		}
		previousChild = newChild;
		return newChild;
	}

}
