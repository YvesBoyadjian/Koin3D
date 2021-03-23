/**
 * 
 */
package application.nodes;

import application.objects.Target;
import jscenegraph.coin3d.inventor.SbBSPTree;
import jscenegraph.coin3d.inventor.lists.SbListInt;
import jscenegraph.database.inventor.SbSphere;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.elements.SoCacheElement;
import jscenegraph.database.inventor.misc.SoChildList;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSeparator;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Yves Boyadjian
 *
 */
public class SoTargets extends SoSeparator {

	private static final float TWO_KILOMETERS = 2000;

	private Target target;

	private SbVec3f referencePoint;

	private SbVec3f cameraDirection;

	private final SbBSPTree bspTree = new SbBSPTree();

	private final SbSphere nearSphere = new SbSphere();

	private final SbListInt nearIDS = new SbListInt();

	private final Set<Integer> actualChildren = new HashSet<>();

	private final Set<Integer> nearChildren = new HashSet<>();

	public SoTargets(Target target) {
		super();
		renderCaching.setValue(SoSeparator.CacheEnabled.OFF);
		this.target = target;
	}

	public void
	GLRenderBelowPath(SoGLRenderAction action)

	////////////////////////////////////////////////////////////////////////
	{
		update_children_list();

		  SoState state = action.getState();

		  // never cache this node
		  SoCacheElement.invalidate(state);

		super.GLRenderBelowPath(action);
	}

	public void setReferencePoint(SbVec3f referencePoint) {
		this.referencePoint = referencePoint;
	}

	public void setCameraDirection(SbVec3f cameraDirection) {
		this.cameraDirection = cameraDirection;
	}

	// Adds a child as last one in group.
	public void addChild(SoNode child) {

		if ( child instanceof SoTarget) {
			child.ref();
			bspTree.addPoint(((SoTarget)child).getCoordinates(),child);
		}
		else {
			super.addChild(child);
		}
	}

	void update_children_list() {

		nearSphere.setValue(referencePoint.operator_add(cameraDirection.operator_mul(target.getViewDistance()*0.8f)), target.getViewDistance());

		nearIDS.truncate(0);
		bspTree.findPoints(nearSphere,nearIDS);

		nearChildren.clear();

		int nbIDS = nearIDS.size();
		for( int i=0;i<nbIDS;i++) {
			int id = nearIDS.get(i);
			if( !actualChildren.contains(id)) {
				SoTarget child = (SoTarget)bspTree.getUserData(id);
				addTarget(child, id);
			}
			nearChildren.add(id);
		}

		final Set<Integer> actualChildrenSaved = new HashSet<>();
		actualChildrenSaved.addAll(actualChildren);
		for( int id : actualChildrenSaved) {
			if(actualChildren.contains(id) && !nearChildren.contains(id)) {
				SoTarget child = (SoTarget)bspTree.getUserData(id);
				removeTarget(child,id);
			}
		}

	}

	void addTarget(SoTarget target, int id) {
		actualChildren.add(id);
		super.addChild(target);
		target.unref();
	}

	void removeTarget(SoTarget target, int id) {
		actualChildren.remove(id);
		target.ref();
		super.removeChild(target);
	}
}
