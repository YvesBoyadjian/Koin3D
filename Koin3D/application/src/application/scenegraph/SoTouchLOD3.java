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
	
	private SoTouchLODMaster master;
	
	public SoTouchLOD3(SoTouchLODMaster master) {
		this.master = master;
	}
	
	public static final int MAX_CHANGE = 1;
	
	protected int
	whichToTraverse(SoAction action)
	{
		// 0 is the most detailed
		// 1 is the least detailed
		
		int newChild = super.whichToTraverse(action);
		//if(previousChild == MOST_DETAILED) {
		if(newChild == previousChild) {
			return newChild;
		}
		if(newChild == MOST_DETAILED) {
			if(master.getCount() >= MAX_CHANGE) {
				if(previousChild == -1) {
					newChild = getNumChildren() - 1;
				}
				else {
					newChild = previousChild; // Changing canceled
				}
			}
			else {
				master.increment(); // Changing accepted 
			}
		}
		//System.out.println("SoTouchLOD2");
		//long start = System.nanoTime();
		if(previousChild != -1) {
			SoNode node = getChild(previousChild);
			clearTree(node);
		}
		//long stop = System.nanoTime();
		//System.out.println("SoTouchLOD2 " + (stop - start)+" ns");				
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
