/**
 * 
 */
package jscenegraph.coin3d.inventor.elements;

import jscenegraph.database.inventor.elements.SoInt32Element;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoTextureScalePolicyElement extends SoInt32Element {

	public
		  enum Policy {
		    USE_TEXTURE_QUALITY,
		    SCALE_DOWN,
		    SCALE_UP,
		    FRACTURE;
			  
			  public int getValue() {
				  return ordinal();
			  }
			  
			  public static Policy fromValue(int value) {
				  return Policy.values()[value];
			  }
		  };


public static void
set(SoState state,
                                 SoNode node,
                                 Policy policy)
{
	SoInt32Element.set(classStackIndexMap.get(SoTextureScalePolicyElement.class), state, /*node,*/ (int)(policy.getValue())); // YB
}

//! FIXME: write doc.

public void
init(SoState state)
{
  super.init(state);
  this.data = getDefault().getValue();
}

//! FIXME: write doc.
public static void
set(SoState state, Policy policy)
{
  SoTextureScalePolicyElement.set(state, null, policy);
}

//! FIXME: write doc.
public static SoTextureScalePolicyElement.Policy
get(SoState state)
{
  return Policy.fromValue(SoInt32Element.get(classStackIndexMap.get(SoTextureScalePolicyElement.class), state));
}

//! FIXME: write doc.
public SoTextureScalePolicyElement.Policy
getDefault()
{
  return Policy.USE_TEXTURE_QUALITY;
}
}
