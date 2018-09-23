/**
 * 
 */
package jexample.parts;

import jscenegraph.coin3d.inventor.nodes.SoCoordinate3;
import jscenegraph.database.inventor.nodes.SoFaceSet;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSeparator;

/**
 * @author Yves
 *
 */
public class SoFaceSetTest {


static float dodecaedre2[][] =
{
	{ 0.0000f,	1.2142f,	0.7453f },
	{ 0.0000f,	1.2142f,	-0.7453f },
	{ -1.2142f,	0.7453f,	0.0000f },
	{ 0.0000f,	1.2142f,	0.7453f },
	{ -1.2142f,	0.7453f,	0.0000f },
	{ -0.7453f,	0.0000f,	1.2142f },
	{ 0.0000f,	1.2142f,	0.7453f },
	{ -0.7453f,	0.0000f,	1.2142f },
	{ 0.7453f,	0.0000f,	1.2142f },
	{ 0.0000f,	1.2142f,	0.7453f },
	{ 0.7453f,	0.0000f,	1.2142f },
	{ 1.2142f,	0.7453f,	0.0000f },
	{ 0.0000f,	1.2142f,	0.7453f },
	{ 1.2142f,	0.7453f,	0.0000f },
	{ 0.0000f,	1.2142f,	-0.7453f },
	{ 0.0000f, -1.2142f,	0.7453f },
	{ 1.2142f, -0.7453f,	0.0000f },
	{ 0.7453f,	0.0000f,	1.2142f },
	{ 1.2142f, -0.7453f,	0.0000f },
	{ 0.7453f,	0.0000f,	-1.2142f },
	{ 1.2142f,	0.7453f,	0.0000f },
	{ 0.7453f,	0.0000f,	-1.2142f },
	{ -0.7453f,	0.0000f,	-1.2142f },
	{ 0.0000f,	1.2142f,	-0.7453f },
	{ -0.7453f,	0.0000f,	-1.2142f },
	{ -1.2142f, -0.7453f,	0.0000f },
	{ -1.2142f,	0.7453f,	0.0000f },
	{ -1.2142f, -0.7453f,	0.0000f },
	{ 0.0000f, -1.2142f,	0.7453f },
	{ -0.7453f,	0.0000f,	1.2142f },
	{ -1.2142f,	0.7453f,	0.0000f },
	{ 0.0000f,	1.2142f,	-0.7453f },
	{ -0.7453f,	0.0000f,	-1.2142f },
	{ -0.7453f,	0.0000f,	1.2142f },
	{ -1.2142f,	0.7453f,	0.0000f },
	{ -1.2142f, -0.7453f,	0.0000f },
	{ 0.7453f,	0.0000f,	1.2142f },
	{ -0.7453f,	0.0000f,	1.2142f },
	{ 0.0000f, -1.2142f,	0.7453f },
	{ 1.2142f,	0.7453f,	0.0000f },
	{ 0.7453f,	0.0000f,	1.2142f },
	{ 1.2142f, -0.7453f,	0.0000f },
	{ 0.0000f,	1.2142f,	-0.7453f },
{ 1.2142f,	0.7453f,	0.0000f },
{ 0.7453f,	0.0000f,	-1.2142f },
{ 0.0000f, -1.2142f,	0.7453f },
{ -1.2142f, -0.7453f,	0.0000f },
{ 0.0000f, -1.2142f, -0.7453f },
{ -1.2142f, -0.7453f,	0.0000f },
{ -0.7453f,	0.0000f,	-1.2142f },
{ 0.0000f, -1.2142f, -0.7453f },
{ -0.7453f,	0.0000f,	-1.2142f },
{ 0.7453f,	0.0000f,	-1.2142f },
{ 0.0000f, -1.2142f, -0.7453f },
{ 0.7453f,	0.0000f,	-1.2142f },
{ 1.2142f, -0.7453f,	0.0000f },
{ 0.0000f, -1.2142f, -0.7453f },
{ 1.2142f, -0.7453f,	0.0000f },
{ 0.0000f, -1.2142f,	0.7453f },
{ 0.0000f, -1.2142f, -0.7453f },
};



static int cotes[] = { 3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3 };

static SoSeparator makedodecaedre()
{
	SoSeparator result = new SoSeparator(); result.ref();
	// Define coordinates for vertices 
	SoCoordinate3 myCoords = new SoCoordinate3(); 
	myCoords.point.setValues(0, 60, dodecaedre2); 
	result.addChild(myCoords);
	SoFaceSet myFaceSet = new SoFaceSet(); 
	myFaceSet.numVertices.setValues(0, 20, cotes); 
	result.addChild(myFaceSet);
	result.unrefNoDelete(); 
	return result;
}

public static SoNode createDemoSceneSoFaceSet() {
	SoSeparator root = makedodecaedre();
	root.ref();

	return root;
}

}
