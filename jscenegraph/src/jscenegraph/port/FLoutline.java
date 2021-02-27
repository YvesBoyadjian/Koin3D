/**
 * 
 */
package jscenegraph.port;

/**
 * @author Yves Boyadjian
 *
 */
public class FLoutline implements Destroyable {
    public short        outlinecount;
    public short[]        vertexcount;
    public FLpt2[][]          vertex;
    public float        xadvance;
    public float        yadvance;
	public void destructor() {
		vertexcount = null;
		vertex = null;		
	}
}
