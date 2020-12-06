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
    //return in.read(value);
	// Reading as SbString instead of as SbName, because the semantics
	// of SoInput::read(SbName&) is to read token identifiers, such as
	// node or field names, and doesn't e.g. handle quotes as expected
	// for a "free-form" string.
	final String[] s = new String[1];
	boolean ok = in.read(s);
	if (!ok) return false;
	this.value.copyFrom(new SbName(s[0]));
	return true;
}

/**
 * Java port
 * @param string
 */
public void setValue(String string) {
	setValue(new SbName(string));
}

}
