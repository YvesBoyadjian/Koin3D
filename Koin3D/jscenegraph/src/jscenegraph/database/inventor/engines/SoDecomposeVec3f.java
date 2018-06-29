/**
 * 
 */
package jscenegraph.database.inventor.engines;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.fields.SoMFVec3f;


/**
 * @author Yves
 *
 */
public class SoDecomposeVec3f extends SoEngine {
	
    public final SoMFVec3f           vector = new SoMFVec3f(); 
  
  
  
  
      public final SoEngineOutput      x = new SoEngineOutput();      
  
      public final SoEngineOutput      y = new SoEngineOutput();      
  
      public final SoEngineOutput      z = new SoEngineOutput();      
  	

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
