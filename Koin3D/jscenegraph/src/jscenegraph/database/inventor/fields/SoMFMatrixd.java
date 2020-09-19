/**
 * 
 */
package jscenegraph.database.inventor.fields;

import java.util.function.DoubleConsumer;

import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbMatrixd;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.port.SbMatrixdArray;

/**
 * @author Yves Boyadjian
 *
 */
public class SoMFMatrixd extends SoMField<SbMatrixd,SbMatrixdArray> { //TODO

	@Override
	protected SbMatrixd constructor() {
		return new SbMatrixd();
	}

	@Override
	protected SbMatrixdArray arrayConstructor(int length) {
		return new SbMatrixdArray(length);
	}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Reads one (indexed) value from file. Returns FALSE on error.
//
// Use: private

public boolean read1Value(SoInput in, int index)
//
////////////////////////////////////////////////////////////////////////
{
	DoubleConsumer[][] ref = ((SbMatrixd)values.getO(index)).getRef();
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
