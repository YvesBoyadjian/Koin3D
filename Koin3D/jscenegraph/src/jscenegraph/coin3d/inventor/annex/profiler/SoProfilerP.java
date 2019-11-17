/**
 * 
 */
package jscenegraph.coin3d.inventor.annex.profiler;

import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoGLRenderAction;

/**
 * @author Yves Boyadjian
 *
 */
public class SoProfilerP {

    static SoType profiler_console_actiontype = SoType.badType();
    
	public static SoType
	getActionType()
	{
	  if (profiler_console_actiontype.operator_equal_equal(SoType.badType())) {
	    profiler_console_actiontype = SoGLRenderAction.getClassTypeId();
	  }
	  return profiler_console_actiontype;
	}

	/*
	  Default implementation for dumping on console instead of overlaying
	  statistics over the 3D graphics.
	*/
	public static void
	dumpToConsole( SbProfilingData data)
	{
		//tODO
	}
}
