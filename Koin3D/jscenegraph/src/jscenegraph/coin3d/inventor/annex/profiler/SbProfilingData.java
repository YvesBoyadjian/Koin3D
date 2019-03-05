/**
 * 
 */
package jscenegraph.coin3d.inventor.annex.profiler;

import jscenegraph.database.inventor.SoPath;

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

}
