/**
 * 
 */
package jscenegraph.database.inventor.fields;

import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.engines.SoCalculator.Expression;
import jscenegraph.port.ObjectArray;

/**
 * @author BOYADJIAN
 *
 * Java port
 */
public class SoMFExpression extends SoMField<Expression,ObjectArray<Expression>> {

	@Override
	public boolean read1Value(SoInput in, int index) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected Expression constructor() {
		return new Expression() {

			@Override
			public void run(float[] abcdefgh, SbVec3f[] ABCDEFGH, float[][] oaobocod, SbVec3f[] oAoBoCoD2) {
				// do nothing				
			}			
		};
	}

	@Override
	protected ObjectArray<Expression> arrayConstructor(int length) {
		Expression[] values = new Expression[length];
		return new ObjectArray<Expression>(0,values);
	}

	@Override
	public ObjectArray<Expression> doGetValues(int start) {
		return new ObjectArray<Expression>(start,values);
	}

}
