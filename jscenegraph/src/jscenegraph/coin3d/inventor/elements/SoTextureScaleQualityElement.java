/**
 * 
 */
package jscenegraph.coin3d.inventor.elements;

import jscenegraph.database.inventor.elements.SoFloatElement;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoTextureScaleQualityElement extends SoFloatElement {

	//! FIXME: write doc.

	public static void
	set(SoState state,
	                                  SoNode node,
	                                   float quality)
	{
	  SoFloatElement.set(classStackIndexMap.get(SoTextureScaleQualityElement.class), state, /*node,*/ quality); // YB
	}

	//! FIXME: write doc.

	public void
	init(SoState state)
	{
	  super.init(state);
	  this.data = getDefault();
	}

	//! FIXME: write doc.
	public static float
	get(SoState state)
	{
	  return SoFloatElement.get(classStackIndexMap.get(SoTextureScaleQualityElement.class), state);
	}

	//! FIXME: write doc.
	public static float getDefault()
	{
	  return 0.5f;
	}
}
