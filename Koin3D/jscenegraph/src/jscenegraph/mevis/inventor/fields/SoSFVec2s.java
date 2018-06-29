/**
 * 
 */
package jscenegraph.mevis.inventor.fields;

import java.util.function.IntConsumer;

import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.database.inventor.fields.SoSField;


/**
 * @author Yves Boyadjian
 *
 */
public class SoSFVec2s extends SoSField<SbVec2s> {

	// Set value from 2 shorts.
	public void setValue(short x, short y) {
		  setValue(new SbVec2s(x, y));		
	}

	@Override
	protected SbVec2s constructor() {		
		return new SbVec2s();
	}

	public boolean readValue(SoInput in)
	{
		IntConsumer[] ref = value.getRef();
	   return(in.read(ref[0]) && in.read(ref[1]));
	}

}
