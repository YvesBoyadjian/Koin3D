/**
 * 
 */
package jscenegraph.database.inventor.actions;

import jscenegraph.coin3d.inventor.annex.profiler.nodes.SoProfilerStats;
import jscenegraph.coin3d.inventor.lists.SbList;
import jscenegraph.coin3d.inventor.lists.SbListInt;
import jscenegraph.database.inventor.SoPath;
import jscenegraph.database.inventor.SoPathList;
import jscenegraph.database.inventor.misc.SoCompactPathList;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.port.Destroyable;

/**
 * @author Yves Boyadjian
 *
 */
public class SoActionP implements Destroyable {
	public SoAction.AppliedCode appliedcode;
		  public int returnindex;
		  public static class AppliedData {
		    SoNode node; //ptr
		    SoPath path; //ptr
		    public class PathListData {
		      public SoPathList pathlist; //ptr
		      public SoPathList origpathlist; //ptr
		      public SoCompactPathList compactlist; //ptr
		    }
		    final PathListData pathlistdata = new PathListData();
			public AppliedData(AppliedData other) {
				super();
				copyFrom(other);
			}
			public AppliedData() {
				super();
			}
			public void copyFrom(AppliedData other) {
				node = other.node;
				path = other.path;
				pathlistdata.pathlist = other.pathlistdata.pathlist;
				pathlistdata.origpathlist = other.pathlistdata.origpathlist;
				pathlistdata.compactlist = other.pathlistdata.compactlist;
			}
		  }
		  public final AppliedData applieddata = new AppliedData(); 
		  public boolean terminated;
		  public final SbList <SbListInt > pathcodearray = new SbList<>();
		  public int prevenabledelementscounter;

		  public static SoNode getProfilerOverlay() {
			  return null; // TODO
		  }

		    static SoProfilerStats pstats = null;
		    
		  public static SoProfilerStats 
		  getProfilerStatsNode()
		  {
		    if ( pstats == null ) {
		      pstats = new SoProfilerStats();
		      pstats.ref();
		    }
		    return pstats;
		  }

		@Override
		public void destructor() {
			pathcodearray.destructor();
		}

}
