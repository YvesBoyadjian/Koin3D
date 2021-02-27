/**
 * 
 */
package jscenegraph.coin3d.inventor.annex.profiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Yves Boyadjian
 *
 */
public class SbProfilingDataP {

	public

		  final List<SbNodeProfilingData> nodeData = new ArrayList<>();
		  public int lastPathIndex;
 
		  public Map</*SbProfilingNodeTypeKey*/Short, SbTypeProfilingData> nodeTypeData = new HashMap<>();
		  public Map</*SbProfilingNodeNameKey*/String, SbNameProfilingData> nodeNameData = new HashMap<>();

}
