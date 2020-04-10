/**
 * 
 */
package application.scenegraph;

import jscenegraph.coin3d.inventor.nodes.SoLOD;
import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbViewVolume;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.elements.SoGLCacheContextElement;
import jscenegraph.database.inventor.elements.SoModelMatrixElement;
import jscenegraph.database.inventor.elements.SoViewVolumeElement;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoCamera;
import jscenegraph.database.inventor.nodes.SoGroup;
import jscenegraph.database.inventor.nodes.SoIndexedFaceSet;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSeparator;

/**
 * @author Yves Boyadjian
 *
 */
public class SoTouchLOD2 extends SoLOD {
	
	public static final int FRONT_DISTANCE = 0;//1000;
	
	public static final int MOST_DETAILED = 0;

	public static final int LEAST_DETAILED = 1;

	private int previousChild = -1;
	
	private int currentVisible = -1;
	
	private boolean cleared = true;
	
	private SoTouchLODMaster master;
	
	public SoTouchLOD2(SoTouchLODMaster master) {
		this.master = master;
	}
	
//	public static final int MAX_CHANGE = 9999;
	
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
				  if(leastDetailedWasCleared || !all_children_have_been_loaded(child,action, 0)) {
					    SoNode otherChild = (SoNode) this.children.get(other_idx);	
					    action.pushCurPath(other_idx, otherChild);
					    if (!action.abortNow()) {
					      //SoNodeProfiling profiling; TODO
					      //profiling.preTraversal(action);
					    	otherChild.GLRenderBelowPath(action);
					      //profiling.postTraversal(action);
					    }
					    action.popCurPath();				  
						  if( !all_children_have_been_loaded(otherChild,action, 0)) {
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
		
		if( child instanceof SoTouchLOD2) {
			SoTouchLOD2 tl2 = (SoTouchLOD2)child;
			int visible = tl2.currentVisible;
			if( visible == -1 ) {
				return false;
			}
			return all_children_have_been_loaded(tl2.getChild(LEAST_DETAILED),action, depth+1);
		}
		else if(child instanceof SoGroup) {
			SoGroup subChunkGroup = (SoGroup)child;
			for(int i=0;i<4;i++) {
				if( ! all_children_have_been_loaded(subChunkGroup.getChild(i),action, depth+1)) {
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
		
		int wantedChild = do_whichToTraverse(action);
		//if(previousChild == MOST_DETAILED) {
		if(wantedChild == previousChild) {
//			if (getChild(wantedChild) instanceof SoSeparatorWithDirty) {
//				SoSeparatorWithDirty sepwd = (SoSeparatorWithDirty)getChild(wantedChild);
//				sepwd.dirty = true;
//			}
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

	public static boolean clearTree(SoNode node) {
		
		if(node instanceof SoRecursiveIndexedFaceSet) {
			
			SoRecursiveIndexedFaceSet SoIndexedFaceSet = (SoRecursiveIndexedFaceSet)node;
			//SoIndexedFaceSet.clear();
			//builder.append(depth+" ");
			return SoIndexedFaceSet.clear();
		}
		else if( node instanceof SoTouchLOD2){
			SoTouchLOD2 group = (SoTouchLOD2) node;
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
		else if( node.isOfType(SoGroup.getClassTypeId())){
			SoGroup group = (SoGroup) node;
			int nbChilds = group.getNumChildren();
			for( int i=0; i<nbChilds; i++) {
				if( clearTree( group.getChild(i))) {
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

	/*!
	  Returns the child to traverse based on the ranges in
	  SoLOD::range. Will clamp to index to the number of children.  This
	  method will return -1 if no child should be traversed.  This will
	  only happen if the node has no children though.
	*/
	protected int
	do_whichToTraverse(SoAction action)
	{
	  SoState state = action.getState();
	  final SbMatrix mat = SoModelMatrixElement.get(state); //ref
	  final SbViewVolume vv = SoViewVolumeElement.get(state); //ref

	  final SbVec3f worldcenter = new SbVec3f();
	  mat.multVecMatrix(this.center.getValue(), worldcenter);

	  float dist = (vv.getProjectionPoint().operator_minus( worldcenter)).length();
	  
	  SoNode node = getChild(1);
	  if(node instanceof SoGroup) {
		  node = ((SoGroup)node).getChild(0);
	  }
	  SoRecursiveIndexedFaceSet SoRecursiveIndexedFaceSet = (SoRecursiveIndexedFaceSet)node;
	  
	  RecursiveChunk rc = SoRecursiveIndexedFaceSet.recursiveChunk;
	  
	  float model_x, model_y;
	  
	  SbVec3f model_xyz = new SbVec3f();
	  
	  SoCamera camera = master.getCamera();

	  SbVec3f world_camera_position = camera.position.getValue();

	  SbVec3f world_camera_direction = camera.orientation.getValue().multVec(new SbVec3f(0,0,-1)); 
	  
	  mat.inverse().multVecMatrix(world_camera_position, model_xyz);
	  
	  world_camera_direction.normalize();
	  
	  model_xyz.add(world_camera_direction.operator_mul(FRONT_DISTANCE));
	  
	  model_x = model_xyz.getX();
	  model_y = model_xyz.getY();
	  
	  if( rc.isInside(model_x, model_y)) {
		  dist = 0;
	  }
	  else {
		  
		  //System.out.print("dist = "+dist);
		  dist = rc.distance(model_x, model_y);
		  
		  //System.out.println(" dist = "+dist);	  
	  }

	  int i;
	  int n = this.range.getNum();

	  for (i = 0; i < n; i++) {
	    if (dist < this.range.operator_square_bracket(i)) break;
	  }
	  if (i >= this.getNumChildren()) i = this.getNumChildren() - 1;
	  return i;
	}
}
