/**
 * 
 */
package jscenegraph.coin3d.shaders.inventor.nodes;

import jscenegraph.database.inventor.fields.SoMFNode;
import jscenegraph.database.inventor.nodes.SoNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoMFUniformShaderParameter extends SoMFNode {

	/**
	 * Adding an uniform shader parameter
	 * @param uniformShaderParameter
	 */
	public void addShaderParameter(SoUniformShaderParameter uniformShaderParameter) {
		int num = getNum();
		int index = num;
		SoNode[] newValue = new SoNode[1];
		newValue[0] = uniformShaderParameter;
		set1Value(index, newValue);
	}

}
