/**
 * 
 */
package jscenegraph.database.inventor.engines;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoMFRotation;


/**
 * @author Yves Boyadjian
 *
 */
public class SoDecomposeRotation extends SoEngine {

	  public final SoMFRotation rotation = new SoMFRotation();
	   
	   
	   
	   
	  public final   SoEngineOutput axis = new SoEngineOutput();
	   
	  public final   SoEngineOutput angle = new SoEngineOutput();
	   
	  
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
