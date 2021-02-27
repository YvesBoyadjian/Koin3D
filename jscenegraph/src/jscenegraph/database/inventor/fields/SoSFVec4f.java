/**
 * 
 */
package jscenegraph.database.inventor.fields;

import jscenegraph.database.inventor.SbVec3d;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec4f;
import jscenegraph.database.inventor.SoInput;

/**
 * @author Yves Boyadjian
 *
 */
public class SoSFVec4f extends SoSField<SbVec4f> {
	
	// Sets the field to the given value. 
	 //
	   // Description:
	    //    Sets value from 3 separate floats. (Convenience function)
	    //
	    // Use: public
	    
	   public void setValue(float x, float y, float z, float w) {
		     setValue(new SbVec4f(x, y, z, w));
		}

	   @Override
		protected SbVec4f constructor() {		
			return new SbVec4f();
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
    return (in.read(value.getRef()[0]) &&
            in.read(value.getRef()[1]) &&
            in.read(value.getRef()[2]) &&
            in.read(value.getRef()[3]));
}

public void setValue(float[] vector) { // java port
	setValue(new SbVec4f(vector));
}

/**
 * Java port
 * @param vector
 */
//public void setValue(SbVec4d vector) {
//	double[] value = vector.getValue();
//	float[] valuef = new float[4];
//	valuef[0] = (float)value[0];
//	valuef[1] = (float)value[1];
//	valuef[2] = (float)value[2];
//	valuef[3] = (float)value[3];
//	setValue(valuef);	
//}


}
