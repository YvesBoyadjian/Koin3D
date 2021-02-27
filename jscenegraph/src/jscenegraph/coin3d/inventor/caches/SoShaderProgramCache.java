/**
 * 
 */
package jscenegraph.coin3d.inventor.caches;

import jscenegraph.database.inventor.caches.SoCache;
import jscenegraph.database.inventor.misc.SoState;

/**
 * @author Yves Boyadjian
 *
 */
public class SoShaderProgramCache extends SoCache {

	static class SoShaderProgramCacheP {
		public
		  String program;
		};
		
		private SoShaderProgramCacheP pimpl;

	public SoShaderProgramCache(SoState state) {
		super(state);
		
		pimpl = new SoShaderProgramCacheP();
	}

	/*!
	  Sets the shader program for this cache.
	*/
	public void
	set( String program)
	{
	  pimpl.program = program;
	}

	/*!
	  Returns the shader program for this cache.
	*/
	public String 
	get()
	{
	  return pimpl.program;
	}

}
