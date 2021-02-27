/**
 * 
 */
package jscenegraph.coin3d.inventor.annex.profiler;

import jscenegraph.database.inventor.SbTime;
import jscenegraph.database.inventor.SoPath;
import jscenegraph.database.inventor.SoType;

/**
 * @author Yves Boyadjian
 *
 */
public class SbProfilingData {

	  // profiling setters
	  public enum FootprintType {
	    MEMORY_SIZE,
	    VIDEO_MEMORY_SIZE
	  };

	  public enum NodeFlag {
	    GL_CACHED_FLAG,
	    CULLED_FLAG
	  };

	  public enum NodeDataQueryFlags {
	    INCLUDE_CHILDREN(0x01);
		  private int value;
		  NodeDataQueryFlags(int value) {
			  this.value = value;
		  }
	  };

	  protected
		  final SoType actionType = new SoType();
		  final SbTime actionStartTime = new SbTime();
		  final SbTime actionStopTime = new SbTime();

		private
		  SbProfilingDataP pimpl = new SbProfilingDataP();

		/*!
		  Constructor.
		*/
		public SbProfilingData()
		{
		  this.constructorInit();
		}

	public boolean operator_equal_equal(SbProfilingData profilingData) {
		// TODO Auto-generated method stub
		return false;
	}

	public void copyFrom(SbProfilingData profilingData) {
		// TODO Auto-generated method stub
		
	}

	public int getIndex(SoPath curPath, boolean b) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getNodeFootprint(int entry, Object object) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setNodeFootprint(int entry, Object object, int i) {
		// TODO Auto-generated method stub
		
	}

	/*!
	  Remove all stored data.
	*/
	public void
	reset()
	{
	  this.constructorInit();
	  pimpl.nodeData.clear();
	  pimpl.nodeTypeData.clear();
	  pimpl.nodeNameData.clear();
	  assert(pimpl.nodeData.size() == 0);
	  assert(pimpl.nodeTypeData.size() == 0);
	  assert(pimpl.nodeNameData.size() == 0);
	}

	private void constructorInit() {
		  // NB: if resource allocation is added, rewrite reset() to not call here
		  this.actionType.copyFrom( SoType.badType());
		  this.actionStartTime.copyFrom( SbTime.zero());
		  this.actionStopTime.copyFrom( SbTime.zero());
		  pimpl.lastPathIndex = -1;
	}

	/*!
	  Register which type of action we are recording statistics for.
	*/

	public void
	setActionType(SoType actiontype)
	{
	  this.actionType.copyFrom( actiontype);
	}

	/*!
	  Set traversal start time.
	*/

	public void
	setActionStartTime(SbTime starttime)
	{
	  this.actionStartTime.copyFrom( starttime);
	}

	/*!
	  Set traversal stop time.
	*/

	public void
	setActionStopTime(SbTime stoptime)
	{
	  this.actionStopTime.copyFrom( stoptime);
	}

}
