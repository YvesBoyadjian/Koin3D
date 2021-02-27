/**
 * 
 */
package jscenegraph.database.inventor.caches;

import jscenegraph.port.Mutable;

/**
 * @author Yves Boyadjian
 *
 */
public class tVertexInfo implements Mutable {
	  public int  matnr;
	  public int  texnr;
	  public int  normnr;
	  public int  vertexnr;
	  
	@Override
	public void copyFrom(Object other) {
		tVertexInfo ot = (tVertexInfo)other;
		matnr = ot.matnr;
		texnr = ot.texnr;
		normnr = ot.normnr;
		vertexnr = ot.vertexnr;
	}
}
