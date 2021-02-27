/**
 * 
 */
package jscenegraph.mevis.inventor.fields;

import java.util.function.IntConsumer;

import jscenegraph.database.inventor.SbVec3s;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.fields.SoSField;

/**
 * @author Yves Boyadjian
 *
 */
public class SoSFVec3s extends SoSField<SbVec3s> {

	@Override
	protected SbVec3s constructor() {
		return new SbVec3s();
	}

	public boolean readValue(SoInput in)
	{
		IntConsumer[] ret = value.getRef();
	   return(in.read(ret[0]) && in.read(ret[1]) && in.read(ret[2]));
	}

}
