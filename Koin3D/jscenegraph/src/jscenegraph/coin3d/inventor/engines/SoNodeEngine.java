/**
 * 
 */
package jscenegraph.coin3d.inventor.engines;

import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.engines.SoEngineOutput;
import jscenegraph.database.inventor.engines.SoEngineOutputData;
import jscenegraph.database.inventor.nodes.SoNode;

/**
 * @author BOYADJIAN
 *
 */
public abstract class SoNodeEngine extends SoNode implements SoEngineAble {

	private static SoType classTypeId = SoType.badType();

    private boolean notifying;
    

/*!
  \copybrief SoBase::initClass(void)
*/
	public static void initClass()
	{
		SoNodeEngine.classTypeId =
				SoType.createType(SoNode.getClassTypeId(), new SbName("NodeEngine"));
	}

/*!
  \copydetails SoEngine::getClassTypeId(void)
*/
	public static SoType
	getClassTypeId()
	{
		return SoNodeEngine.classTypeId;
	}

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

	public abstract void evaluate();
	  
	/*!
	  Returns the SoEngineOutputData class which holds information about
	  the outputs in this engine.
	*/
	public static SoEngineOutputData[]
	getOutputDataPtr()
	{
	  return null; // base class has no output
	}

	// Documented in superclass.
	public boolean readInstance(SoInput in, short flagsarg)
	{
	  // FIXME: I believe there's code missing here for reading
	  // SoUnknownEngine instances. 20000919 mortene.
	  return super.readInstance(in, flagsarg);
	}

    //! A very annoying double notification occurs with engines
    //! that enable their outputs during inputChanged; this flag
    //! prevents that:
    public boolean                isNotifying() { return notifying; }

    /*!
    Triggers an engine evaluation.
  */
  public void evaluateWrapper()
  {
    SoEngineOutputData outputs = this.getOutputData();
    int i, n = outputs.getNumOutputs();
    for (i = 0; i < n; i++) {
      outputs.getOutput(this, i).prepareToWrite();
    }
    this.evaluate();
    for (i = 0; i < n; i++) {
      outputs.getOutput(this, i).doneWriting();
    }
  }

}