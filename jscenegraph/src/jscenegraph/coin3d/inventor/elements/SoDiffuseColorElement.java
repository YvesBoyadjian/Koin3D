/**
 * 
 */
package jscenegraph.coin3d.inventor.elements;

import jscenegraph.database.inventor.elements.SoLazyElement;
import jscenegraph.database.inventor.elements.SoReplacedElement;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.port.IntArray;

/**
 * @author Yves Boyadjian
 *
 */
public class SoDiffuseColorElement extends SoReplacedElement {

	public void
	set(SoState state, SoNode node,
	                           int numcolors,
	                           IntArray colors,
	                           final boolean packedtransparency)
	{
	  SoLazyElement.setPacked(state, node, numcolors, colors, packedtransparency);
	}

}
