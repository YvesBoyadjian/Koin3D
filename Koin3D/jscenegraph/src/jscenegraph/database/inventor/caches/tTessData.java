/**
 * 
 */
package jscenegraph.database.inventor.caches;

import jscenegraph.coin3d.inventor.lists.SbList;
import jscenegraph.coin3d.inventor.lists.SbListInt;
import jscenegraph.database.inventor.SbIntList;
import jscenegraph.port.Array;

/**
 * @author Yves Boyadjian
 *
 */
//
//struct used to hold data for the tessellator callback
//
public class tTessData {
	  public boolean firstvertex;
	  public Array<tVertexInfo> vertexInfo; //ptr
	  public SoConvexDataCache.Binding matbind;
	  public SoConvexDataCache.Binding normbind;
	  public SoConvexDataCache.Binding texbind;

	  public SbListInt vertexIndex; //ptr
	  public SbListInt matIndex; //ptr
	  public SbListInt normIndex; //ptr
	  public SbListInt texIndex; //ptr
	  public int numvertexind;
	  public int nummatind;
	  public int numnormind;
	  public int numtexind;
}
