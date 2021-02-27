/**
 * 
 */
package jscenegraph.database.inventor.engines;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoMFRotation;
import jscenegraph.database.inventor.fields.SoMFVec3f;


/**
 * @author Yves Boyadjian
 *
 */
public class SoComposeMatrix extends SoEngine { //TODO
	
	  public final SoMFVec3f translation = new SoMFVec3f();
	   
	  public final   SoMFRotation rotation = new SoMFRotation();
	   
	  public final   SoMFVec3f scaleFactor = new SoMFVec3f();
	   
	  public final   SoMFRotation scaleOrientation = new SoMFRotation();
	   
	  public final   SoMFVec3f center = new SoMFVec3f();
	   
	   
	   
	   
	    public SoEngineOutput matrix;
	  	

	/* (non-Javadoc)
	 * @see com.openinventor.inventor.engines.SoEngine#evaluate()
	 */
	@Override
	protected void evaluate() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.openinventor.inventor.engines.SoEngine#getOutputData()
	 */
	@Override
	public SoEngineOutputData getOutputData() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.openinventor.inventor.misc.SoBase#getTypeId()
	 */
	@Override
	public SoType getTypeId() {
		// TODO Auto-generated method stub
		return null;
	}

}
