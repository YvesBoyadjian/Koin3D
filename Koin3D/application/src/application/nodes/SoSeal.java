/**
 * 
 */
package application.nodes;

import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoPickAction;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.database.inventor.nodes.SoTranslation;

/**
 * @author Yves Boyadjian
 *
 */
public class SoSeal extends SoSeparator {
	
	private static final float TWO_KILOMETERS = 2000;
	
	private static final float THREE_KILOMETERS = 3000;
	
	public static final float MAX_DISTANCE = TWO_KILOMETERS;//THREE_KILOMETERS;
	
	public static final float MAX_SQUARE_DISTANCE = MAX_DISTANCE * MAX_DISTANCE;
	
	//private int sealIndex;
	private SbVec3f referencePoint;
	private final SbVec3f dummy = new SbVec3f();

	public SoSeal() {
		super();
		renderCaching.setValue(SoSeparator.CacheEnabled.OFF);
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
		if(referencePoint.operator_minus(getCoordinates(), dummy).sqrLength() > MAX_SQUARE_DISTANCE) {
			return;
		}		
		super.GLRenderBelowPath(action);
	}

	// Doc in parent
	public void
	pick(SoPickAction action)
	{
		if(referencePoint.operator_minus(getCoordinates(), dummy).sqrLength() > MAX_SQUARE_DISTANCE) {
			return;
		}		
	  super.pick( action);
	}

}
