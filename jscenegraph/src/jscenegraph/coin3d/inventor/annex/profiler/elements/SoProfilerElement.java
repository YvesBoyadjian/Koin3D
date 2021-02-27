/**
 * 
 */
package jscenegraph.coin3d.inventor.annex.profiler.elements;

import jscenegraph.coin3d.inventor.annex.profiler.SbProfilingData;
import jscenegraph.database.inventor.elements.SoElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.misc.SoState;

/**
 * @author Yves Boyadjian
 *
 */
public class SoProfilerElement extends SoElement {
	
	protected final SbProfilingData data = new SbProfilingData();

	/* (non-Javadoc)
	 * @see jscenegraph.database.inventor.elements.SoElement#matches(jscenegraph.database.inventor.elements.SoElement)
	 */
	@Override
	public boolean matches(SoElement element) {
		  if (element == this) return true;
		  SoProfilerElement pelement =
		    (SoProfilerElement)(element);
		  if (pelement.getProfilingData().operator_equal_equal(this.getProfilingData())) return true;
		  return false;
	}

	/* (non-Javadoc)
	 * @see jscenegraph.database.inventor.elements.SoElement#copyMatchInfo()
	 */
	@Override
	public SoElement copyMatchInfo() {
		  SoProfilerElement elem =
				    (SoProfilerElement)(this.getTypeId().createInstance());
				  elem.getProfilingData().copyFrom(this.getProfilingData());
				  return elem;
	}

	/*!
	  This is the required method of fetching the SoProfilerElement instance.
	*/
	public static SoProfilerElement
	get(SoState state)
	{
	  assert(state != null);
	  if (!state.isElementEnabled(classStackIndexMap.get(SoProfilerElement.class))) {
	    return null;
	  }

	  SoElement elt = state.getElementNoPush(classStackIndexMap.get(SoProfilerElement.class));
	  return (SoProfilerElement)(elt);
	}

	public void
	push(SoState state)
	{
	  // NOTE: if this triggers, someone has probably used
	  // SoState->getElement(stackindex) to fetch the element instead of
	  // using the required SoProfilerElemenet::get(SoState *)
	  //assert(!"programming error: SoProfilerElement should not be stack-pushed");
	  SoDebugError.post("SoProfilerElement::push",
	                     "programming error: SoProfilerElement should not be stack-pushed");
	}

	public void
	pop(SoState state, SoElement elt)
	{
	  // NOTE: if this triggers, someone has probably used
	  // SoState->getElement(stackindex) to fetch the element instead of
	  // using the required SoProfilerElemenet::get(SoState *)
	  //assert(!"programming error: SoProfilerElement should not be stack-pushed");
	  SoDebugError.post("SoProfilerElement::pop",
	                     "programming error: SoProfilerElement should not be stack-pushed");
	}

public SbProfilingData 
getProfilingData() 
{
  return this.data;
}

}
