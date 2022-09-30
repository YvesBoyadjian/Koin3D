/**
 * 
 */
package jscenegraph.port;

import java.util.HashMap;
import java.util.Map;

import jscenegraph.opengl.GL2;

/**
 * @author Yves Boyadjian
 *
 */
public class Ctx {

	private static Map<Integer,GL2> cacheContexts = new HashMap<>();

	public static void addContext(int ctx, GL2 gl2) {
		if(cacheContexts.get(ctx)!= null) {
			throw new IllegalArgumentException();
		}
		cacheContexts.put(ctx, gl2);
	}

	public static GL2 get(int shareID) {
		GL2 gl2 = cacheContexts.get(shareID);
		if(gl2 == null) {
			gl2 = new GL2(){};
			addContext(shareID, gl2);
		}
		return gl2;
	}
}
