/**
 * 
 */
package jscenegraph.database.inventor.engines;

/**
 * @author Yves Boyadjian
 *
 */
public class SoUnknownEngine extends SoEngine {

    //! The class name read from the file.  This is used when writing the
    //! engine back out.
    private String className;

	/* (non-Javadoc)
	 * @see jscenegraph.database.inventor.engines.SoEngine#evaluate()
	 */
	@Override
	protected void evaluate() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see jscenegraph.database.inventor.engines.SoEngine#getOutputData()
	 */
	@Override
	public SoEngineOutputData getOutputData() {
		// TODO Auto-generated method stub
		return null;
	}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Set the class name of the unknown engine
//
// Use: public

public void setClassName( String name )

//
////////////////////////////////////////////////////////////////////////
{
    className =  name;
}

}
