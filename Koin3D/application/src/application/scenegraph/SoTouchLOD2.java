/**
 * 
 */
package application.scenegraph;

import jscenegraph.coin3d.inventor.nodes.SoLOD;
import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbViewVolume;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.elements.SoModelMatrixElement;
import jscenegraph.database.inventor.elements.SoViewVolumeElement;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoCamera;
import jscenegraph.database.inventor.nodes.SoGroup;
import jscenegraph.database.inventor.nodes.SoIndexedFaceSet;
import jscenegraph.database.inventor.nodes.SoNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoTouchLOD2 extends SoLOD {

	public SoCamera camera; // ptr

	private int previousChild = -1;
	
	private SoTouchLODMaster master;
	
	public SoTouchLOD2(SoTouchLODMaster master) {
		this.master = master;
	}
	
	public static final int MAX_CHANGE = 1;
	
	protected int
	whichToTraverse(SoAction action)
	{
		int newChild = do_whichToTraverse(action);
		if(previousChild == 0) {
			if(newChild != 0) {
				if(master.getCount() >= MAX_CHANGE) {
					return 0;
				}
				else {
					master.increment();
				}
				//System.out.println("SoTouchLOD2");
				//long start = System.nanoTime();
				SoGroup group = (SoGroup) getChild(0);
				for(int i=0;i<4;i++) {
					SoNode child = group.getChild(i);
					if(child instanceof SoRecursiveIndexedFaceSet) {
					
						SoRecursiveIndexedFaceSet SoIndexedFaceSet = (SoRecursiveIndexedFaceSet)child;
						SoIndexedFaceSet.clear();
					}
				}
				//long stop = System.nanoTime();
				//System.out.println("SoTouchLOD2 " + (stop - start)+" ns");				
			}
		}
		previousChild = newChild;
		return newChild;
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
	  
	  SoRecursiveIndexedFaceSet SoRecursiveIndexedFaceSet = (SoRecursiveIndexedFaceSet)getChild(1);
	  
	  RecursiveChunk rc = SoRecursiveIndexedFaceSet.recursiveChunk;
	  
	  float model_x, model_y;
	  
	  SbVec3f model_xyz = new SbVec3f();
	  
	  SbVec3f world_camera_position = camera.position.getValue();

	  SbVec3f world_camera_direction = camera.orientation.getValue().multVec(new SbVec3f(0,0,-1)); 
	  
	  mat.inverse().multVecMatrix(world_camera_position, model_xyz);
	  
	  world_camera_direction.normalize();
	  
	  model_xyz.add(world_camera_direction.operator_mul(1000));
	  
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

	public void setCamera(SoCamera camera) {
		this.camera = camera;
	}

}
