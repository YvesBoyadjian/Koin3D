/**
 * 
 */
package jscenegraph.coin3d.misc;

/**
 * @author Yves Boyadjian
 *
 */
public class cc_glglue_offscreen_cb_functions {
	
	public interface CreateOffscreen {
		Object apply( int width, int height);
	}

    //cc_glglue_offscreen_data (*create_offscreen)( int width, int height);
	CreateOffscreen create_offscreen;
	
	public interface MakeCurrent {
		boolean apply(/*cc_glglue_offscreen_data*/Object context);
	}
	
	MakeCurrent make_current;
	
	public interface ReinstatePrevious {
		Object apply(Object context);
	}
	
	ReinstatePrevious reinstate_previous;
	
	public interface Destruct {
		Object apply(Object context);
	}
	
	Destruct destruct;
}
