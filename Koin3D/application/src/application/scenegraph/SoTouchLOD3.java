/**
 * 
 */
package application.scenegraph;

import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.nodes.SoGroup;
import jscenegraph.database.inventor.nodes.SoNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoTouchLOD3 extends SoCameraLOD {

	public static final int MOST_DETAILED = 0;

	private int previousChild = -1;
	
	protected int
	whichToTraverse(SoAction action)
	{
		int newChild = super.whichToTraverse(action);
		//if(previousChild == 0) {
			if(newChild != previousChild) {
				//System.out.println("SoTouchLOD3");
				if(previousChild != -1) {
					SoNode node = getChild(previousChild);
					clearTree(node);
				}
			}
		//}
		previousChild = newChild;
		return newChild;
	}

	public static void clearTree(SoNode node) {
		
		if(node instanceof SoRecursiveIndexedFaceSet) {
			
			SoRecursiveIndexedFaceSet SoIndexedFaceSet = (SoRecursiveIndexedFaceSet)node;
			SoIndexedFaceSet.clear();
		}
		else if( node.isOfType(SoGroup.getClassTypeId())){
			SoGroup group = (SoGroup) node;
			int nbChilds = group.getNumChildren();
			for( int i=0; i<nbChilds; i++) {
				clearTree( group.getChild(i) );
			}				
		}			
	}
}
