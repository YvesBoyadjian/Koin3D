/**
 * 
 */
package jscenegraph.database.inventor.fields;

import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SoInput;

/**
 * @author Yves Boyadjian
 *
 */
public class SoSFName extends SoSField<SbName> {
	
	@Override
	protected SbName constructor() {		
		return new SbName();
	}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Reads value from file. Returns FALSE on error.
//
// Use: private

public boolean readValue(SoInput in)
//
////////////////////////////////////////////////////////////////////////
{
    return in.read(value);
}

/**
 * Java port
 * @param string
 */
public void setValue(String string) {
	setValue(new SbName(string));
}

}
