/**
 * 
 */
package jexample.parts;

import jscenegraph.database.inventor.nodes.SoCoordinate3;
import jscenegraph.database.inventor.nodes.SoIndexedTriangleStripSet;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSeparator;

/**
 * @author Yves
 *
 */
public class IndexedTriangleStrip {


public static SoSeparator makeCircle(int nb_sect) {
	float[][] tab;
	int[] indices;
	tab = new float[nb_sect + 1][3]; 
	indices = new int[4 * nb_sect];

	tab[nb_sect][0] = tab[nb_sect][1] = tab[nb_sect][2] = 0.f; 
	for (int i = 0; i<nb_sect; i++)
	{
		tab[i][0] = (float)Math.cos(2 * Math.PI*(float)i / nb_sect); 
		tab[i][1] = (float)Math.sin(2 * Math.PI*(float)i / nb_sect); 
		tab[i][2] = 0;
	}
	for (int i = 0; i< 4 * nb_sect; i += 4)
	{
		indices[i] = nb_sect; 
		indices[i + 1] = (i / 4) % nb_sect; 
		indices[i + 2] = (i / 4 + 1) % nb_sect; 
		indices[i + 3] = -1;
	}
	SoSeparator result = new SoSeparator(); 
	result.ref();
		SoCoordinate3 myCoords = new SoCoordinate3(); 
		myCoords.point.setValues(0, nb_sect + 1, tab); 
		result.addChild(myCoords);
		SoIndexedTriangleStripSet myTriangleStripSet = new SoIndexedTriangleStripSet();
		myTriangleStripSet.coordIndex.setValues(0, 4 * nb_sect, indices); 
		result.addChild(myTriangleStripSet);
		result.unrefNoDelete(); 
		//delete [] tab;
		//delete[] indices; 
		return result;
		}


	public static SoNode createDemoSceneSoIndexedTriangleStrip() {
	SoSeparator root = makeCircle(36); 
	root.ref();

	return root;
}

}
