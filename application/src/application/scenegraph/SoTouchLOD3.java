/**
 * 
 */
package application.scenegraph;

import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.elements.SoGLCacheContextElement;
import jscenegraph.database.inventor.nodes.SoGroup;
import jscenegraph.database.inventor.nodes.SoNode;

/**
 * @author Yves Boyadjian
 *
 */
public abstract class SoTouchLOD3 extends SoCameraLOD implements SoTouchLODMaster.SoTouchLODSlave {

	public static final int MOST_DETAILED = 0;

	public static final int LEAST_DETAILED = 1;

	private int previousChild = -1;
	
	private int currentVisible = -1;
	
	private boolean cleared = true;
	
	private SoTouchLODMaster master;
	
	public SoTouchLOD3(SoTouchLODMaster master) {
		this.master = master;
		master.register(this);
	}

	public void destructor() {
		master.unregister(this);
		super.destructor();
	}
	
	//public static final int MAX_CHANGE = 9999;
	
	public void
	GLRenderBelowPath(SoGLRenderAction action)
	{
		  int idx = this.whichToTraverse(action);
		  
//		  int wanted_idx = idx;
		  
	    SoRecursiveIndexedFaceSet least_detailed = (SoRecursiveIndexedFaceSet) this.children.get(LEAST_DETAILED);
	    
		  boolean leastDetailedWasCleared = false;
		  if(idx == MOST_DETAILED) {
			    if(least_detailed.cleared) {
			    	idx = LEAST_DETAILED;
			    	leastDetailedWasCleared = true;
			    }
		  }
		  
		  if (idx >= 0) {
		    SoNode child = (SoNode) this.children.get(idx);
		    action.pushCurPath(idx, child);
		    if (!action.abortNow()) {
		      //SoNodeProfiling profiling; TODO
		      //profiling.preTraversal(action);
		      child.GLRenderBelowPath(action);
		      //profiling.postTraversal(action);
		    }
		    action.popCurPath();
		    
		    currentVisible = idx;
		  
			  if(/*idx == MOST_DETAILED*/!least_detailed.cleared) {
			  int other_idx = 1 -idx;
				  if(leastDetailedWasCleared || !all_children_have_been_loaded(child,action,0)) {
					    SoNode otherChild = (SoNode) this.children.get(other_idx);	
					    action.pushCurPath(other_idx, otherChild);
					    if (!action.abortNow()) {
					      //SoNodeProfiling profiling; TODO
					      //profiling.preTraversal(action);
					    	otherChild.GLRenderBelowPath(action);
					      //profiling.postTraversal(action);
					    }
					    action.popCurPath();				  
						  if( !all_children_have_been_loaded(otherChild,action,0)) {
							  currentVisible = -1;
						  }
						  else
							  currentVisible = other_idx;
							  
				  }
				  else {
					  if(other_idx == MOST_DETAILED) {
//						  if(wanted_idx == MOST_DETAILED) {
//							  int ii=0;
//						  }
//						  else {
							SoNode node = getChild(MOST_DETAILED);
							clearTree(node);
//						  }
					  }
				  }
			  }
		  }
		  
		    if(!least_detailed.cleared) {
		    	cleared = false;
		    }
		    else if( currentVisible == LEAST_DETAILED) {
		    	currentVisible = -1;
		    }
		    
		  // don't auto cache LOD nodes.
		  SoGLCacheContextElement.shouldAutoCache(action.getState(),
		                                           SoGLCacheContextElement.AutoCache.DONT_AUTO_CACHE.getValue());
	}
	
	public boolean all_children_have_been_loaded(SoNode child, SoAction action, int depth) {
//		if(depth > 2) {
//			return false;
//		}
		
		if( child instanceof SoTouchLOD3) {
			SoTouchLOD3 tl2 = (SoTouchLOD3)child;
			int visible = tl2.currentVisible;
			if( visible == -1 ) {
				return false;
			}
			return all_children_have_been_loaded(tl2.getChild(LEAST_DETAILED),action, depth+1);
		}
		else if(child instanceof SoGroup) {
			SoGroup subChunkGroup = (SoGroup)child;
			for(int i=0;i<4;i++) {
				if(!all_children_have_been_loaded(subChunkGroup.getChild(i),action,depth+1)) {
					return false;
				}				
			}
			return true;
		}
		else if(child instanceof SoRecursiveIndexedFaceSet) {
			SoRecursiveIndexedFaceSet ifs = (SoRecursiveIndexedFaceSet)child;
			return !ifs.cleared;
		}
		else {
			throw new IllegalStateException();
		}
	}
	
	protected int
	whichToTraverse(SoAction action)
	{
		// 0 is the most detailed
		// 1 is the least detailed
		
		int wantedChild = super.whichToTraverse(action);
		//if(previousChild == MOST_DETAILED) {
		if(wantedChild == previousChild) {
			return wantedChild;
		}
//		if(wantedChild == MOST_DETAILED) {
//			if(master.getCount() >= MAX_CHANGE) {
//				if(previousChild == -1) {
//					wantedChild = getNumChildren() - 1;
//				}
//				else {
//					wantedChild = previousChild; // Changing canceled
//				}
//			}
//			else {
//				master.increment(); // Changing accepted 
//			}
//		}
		//System.out.println("SoTouchLOD2");
		//long start = System.nanoTime();
//		if(previousChild != -1 && wantedChild != previousChild) {
//			SoNode node = getChild(previousChild);
//			clearTree(node);
//		}
		//long stop = System.nanoTime();
		//System.out.println("SoTouchLOD2 " + (stop - start)+" ns");				
		previousChild = wantedChild;
//		if (getChild(wantedChild) instanceof SoSeparatorWithDirty) {
//			SoSeparatorWithDirty sepwd = (SoSeparatorWithDirty)getChild(wantedChild);
//			sepwd.dirty = true;
//		}
		return wantedChild;
	}

	/**
	 * Returns true if there was action
	 * @param node
	 * @return
	 */
	public static boolean clearTree(SoNode node) {
		
		if(node instanceof SoRecursiveIndexedFaceSet) {
			
			SoRecursiveIndexedFaceSet SoIndexedFaceSet = (SoRecursiveIndexedFaceSet)node;
			return SoIndexedFaceSet.clear();
			//return;
		}
		else if( node instanceof SoTouchLOD3){
			SoTouchLOD3 group = (SoTouchLOD3) node;
			if(!group.cleared) {
				int nbChilds = group.getNumChildren();
				for( int i=0; i<nbChilds; i++) {
					 if(clearTree( group.getChild(i))) {
						 return true;
					 }
				}		
				group.cleared = true;
				return false;
			}
			return false;
		}			
		else if( node instanceof SoGroup){
			SoGroup group = (SoGroup) node;
			int nbChilds = group.getNumChildren();
			for( int i=0; i<nbChilds; i++) {
				if(clearTree( group.getChild(i))) {
					return true;
				}
			}				
			return false;
		}			
		else {
			throw new IllegalStateException();
		}
//		if (node instanceof SoSeparatorWithDirty) {
//			SoSeparatorWithDirty sepwd = (SoSeparatorWithDirty)node;
//			sepwd.dirty = true;
//		}
	}
}
