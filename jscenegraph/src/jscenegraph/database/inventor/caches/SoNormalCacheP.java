/**
 * 
 */
package jscenegraph.database.inventor.caches;

import jscenegraph.coin3d.inventor.lists.SbList;
import jscenegraph.coin3d.inventor.lists.SbListInt;
import jscenegraph.coin3d.inventor.lists.SbListOfMutableRefs;
import jscenegraph.database.inventor.SbIntList;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.misc.SoNormalGenerator;
import jscenegraph.port.SbVec3fArray;

/**
 * @author Yves Boyadjian
 *
 */
public class SoNormalCacheP {

	public
		  int numNormals;
		  //union {
		    public SbVec3fArray normalData_normals; // ptr
		    public SoNormalGenerator normalData_generator; // ptr
		  //} normalData;
		  public final SbListInt indices = new SbListInt();
		  public final SbListOfMutableRefs<SbVec3f> normalArray = new SbListOfMutableRefs<>(()->new SbVec3f());
}
