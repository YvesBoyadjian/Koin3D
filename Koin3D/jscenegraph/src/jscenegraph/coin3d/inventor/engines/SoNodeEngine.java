/**
 * 
 */
package jscenegraph.coin3d.inventor.engines;

import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.engines.SoEngineOutput;
import jscenegraph.database.inventor.engines.SoEngineOutputData;
import jscenegraph.database.inventor.nodes.SoNode;

/**
 * @author BOYADJIAN
 *
 */
public abstract class SoNodeEngine extends SoNode {

	/*!
	  Returns the output with name \a outputname, or \c NULL if no such
	  output exists.
	*/
	public SoEngineOutput getOutput(final SbName outputname)
	{
	  SoEngineOutputData outputs = this.getOutputData();
	  if (outputs == null) return null;
	  int n = outputs.getNumOutputs();
	  for (int i = 0; i < n; i++) {
	    if (outputs.getOutputName(i).operator_equal_equal(outputname))
	      return outputs.getOutput(this, i);
	  }
	  return null;
	}

	public abstract SoEngineOutputData getOutputData();

}