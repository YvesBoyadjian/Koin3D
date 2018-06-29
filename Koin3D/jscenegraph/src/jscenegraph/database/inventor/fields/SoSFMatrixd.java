/**
 * 
 */
package jscenegraph.database.inventor.fields;

import java.util.function.DoubleConsumer;

import jscenegraph.database.inventor.SbMatrixd;
import jscenegraph.database.inventor.SoInput;

/**
 * @author Yves Boyadjian
 *
 */
public class SoSFMatrixd extends SoSField<SbMatrixd> { //TODO

	@Override
	protected SbMatrixd constructor() {
		return new SbMatrixd();
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
	DoubleConsumer[][] ref = value.getRef();
    return  in.read(ref[0][0]) && in.read(ref[0][1])
         && in.read(ref[0][2]) && in.read(ref[0][3])
         && in.read(ref[1][0]) && in.read(ref[1][1])
         && in.read(ref[1][2]) && in.read(ref[1][3])
         && in.read(ref[2][0]) && in.read(ref[2][1])
         && in.read(ref[2][2]) && in.read(ref[2][3])
         && in.read(ref[3][0]) && in.read(ref[3][1])
         && in.read(ref[3][2]) && in.read(ref[3][3]);
}

	
}
