/**
 * 
 */
package jscenegraph.database.inventor.fields;

import java.util.function.DoubleConsumer;

import jscenegraph.database.inventor.SoInput;

/**
 * @author Yves Boyadjian
 *
 */
public class SoSFDouble extends SoSField<Double> { //TODO

	/* (non-Javadoc)
	 * @see jscenegraph.database.inventor.fields.SoSField#constructor()
	 */
	@Override
	protected Double constructor() {
		return new Double(0);
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
    return in.read(new DoubleConsumer(){
		@Override
		public void accept(double val) {
			value = val;			
		}    	
    });
}

}
