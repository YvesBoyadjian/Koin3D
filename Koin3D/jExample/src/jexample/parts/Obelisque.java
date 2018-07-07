/**
 * 
 */
package jexample.parts;

import jscenegraph.database.inventor.nodes.SoCoordinate3;
import jscenegraph.database.inventor.nodes.SoFaceSet;
import jscenegraph.database.inventor.nodes.SoMaterial;
import jscenegraph.database.inventor.nodes.SoSeparator;

/**
 * @author Yves Boyadjian
 *
 */
public class Obelisque {

	static float vertices[][] =
		 { { 0,30,0},{-2,27,2},{ 2,27,2},
		   { 0,30,0},{-2,27,-2},{-2,27,2},
		   { 0,30,0},{ 2,27,-2},{-2,27,-2},
		   { 0,30,0},{ 2,27,2},{ 2,27,-2},
		   {-2,27,2},{-4,0,4},{ 4,0,4},{ 2,27,2},
		   {-2,27,-2},{-4,0,-4},{-4,0,4},{-2,27,2},
		   { 2,27,-2},{ 4,0,-4},{-4,0,-4},{-2,27,-2},
		   { 2,27,2},{ 4,0,4},{ 4,0,-4},{ 2,27,-2}};
		static int numvertices[] = {3,3,3,3,4,4,4,4};

		
		//Création d'un obélisque par assemblage de facettes

		public static SoSeparator makeObeliskFaceSet()
		{ 
			SoSeparator obelisk = new SoSeparator();
		  obelisk.ref();
		  SoMaterial myMaterial = new SoMaterial();
		  myMaterial.diffuseColor.setValue(.4f,.4f,.4f);
		  obelisk.addChild(myMaterial);
		  SoCoordinate3 myCoords = new SoCoordinate3();
		  myCoords.point.setValues(0,28,vertices);
		  obelisk.addChild(myCoords);
		  SoFaceSet myFaceSet = new SoFaceSet();
		  myFaceSet.numVertices.setValues(0,/*8,*/numvertices);
		  obelisk.addChild(myFaceSet);
		  obelisk.unrefNoDelete();
		  return obelisk;
	}
}
