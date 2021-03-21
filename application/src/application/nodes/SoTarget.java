/**
 * 
 */
package application.nodes;

import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec3fSingle;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoPickAction;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.database.inventor.nodes.SoTranslation;

/**
 * @author Yves Boyadjian
 *
 */
public class SoTarget extends SoSeparator {
	
	private static final float TWO_KILOMETERS = 2000;
	
	private static final float THREE_KILOMETERS = 3000;

	private static final float FIVE_HUNDRED_METERS = 500;
	
	public static final float MAX_VIEW_DISTANCE = TWO_KILOMETERS;//THREE_KILOMETERS;

	public static final float MAX_SHOOT_DISTANCE = TWO_KILOMETERS;//FIVE_HUNDRED_METERS;//THREE_KILOMETERS;

	public static final float MAX_SQUARE_VIEW_DISTANCE = MAX_VIEW_DISTANCE * MAX_VIEW_DISTANCE;

	public static final float MAX_SQUARE_SHOOT_DISTANCE = MAX_SHOOT_DISTANCE * MAX_SHOOT_DISTANCE;

	//private int sealIndex;
	private SbVec3f referencePoint;
	private final SbVec3fSingle dummyRender = new SbVec3fSingle();
	//private final SbVec3fSingle dummyPick = new SbVec3fSingle();

	public SoTarget() {
		super();
		renderCaching.setValue(SoSeparator.CacheEnabled.OFF);
		pickCulling.setValue(CacheEnabled.OFF); // Speed up picking
	}
	
//	public void setSealIndex( int sealIndex) {
//		this.sealIndex = sealIndex;
//	}
	
	public void setReferencePoint(SbVec3f referencePoint) {
		this.referencePoint = referencePoint;
	}
	
	private SbVec3f getCoordinates() {
		return ((SoTranslation)getChild(0)).translation.getValue();
	}

	public void
	GLRenderBelowPath(SoGLRenderAction action)

	////////////////////////////////////////////////////////////////////////
	{
		if(referencePoint.operator_minus(getCoordinates(), dummyRender).sqrLength() > MAX_SQUARE_VIEW_DISTANCE) {
			return;
		}		
		super.GLRenderBelowPath(action);
	}

	// Doc in parent
	public void
	pick(SoPickAction action)
	{
		if(referencePoint.operator_minus(getCoordinates()).sqrLength() > MAX_SQUARE_SHOOT_DISTANCE) {
			return;
		}		
	  super.pick( action);
	}

}
