/**
 * 
 */
package jscenegraph.coin3d.inventor.nodes;

import jscenegraph.coin3d.inventor.lists.SbList;
import jscenegraph.mevis.inventor.misc.SoVBO;
import jscenegraph.port.Destroyable;

/**
 * @author Yves Boyadjian
 *
 */
public class SoVertexPropertyP implements Destroyable {

	  public SoVertexProperty master;//ptr
	  public boolean transparent;
	  public boolean checktransparent;

	  public final SoVBO[] vertexvbo = new SoVBO[1]; //ptr
	  public final SoVBO[] normalvbo = new SoVBO[1]; //ptr
	  public final SoVBO[] colorvbo = new SoVBO[1]; //ptr

	  public final SbList<SoVBO[]> texcoordvbo = new SbList<>();

	@Override
	public void destructor() {
	    for (int i = 0; i < this.texcoordvbo.getLength(); i++) {
	        Destroyable.delete( this.texcoordvbo.operator_square_bracket(i)[0]);
	      }
	    Destroyable.delete( this.vertexvbo[0]);
	    Destroyable.delete( this.normalvbo[0]);
	    Destroyable.delete( this.colorvbo[0]);
	}
}
