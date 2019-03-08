/**
 * 
 */
package jexample.parts;

import jscenegraph.coin3d.inventor.nodes.SoCoordinate3;
import jscenegraph.database.inventor.nodes.SoIndexedFaceSet;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSeparator;

/**
 * @author Yves
 *
 */
public class SoIndexedFaceSetTest {

	final static int SO_END_FACE_INDEX = SoIndexedFaceSet.SO_END_FACE_INDEX;

static float dodecaedre3[][] =
{
	{ 0.0000f,	1.2142f,	0.7453f },	//	top
	{ 0.0000f,	1.2142f,	-0.7453f },	//	points surrounding top
	{ -1.2142f,	0.7453f,	0.0000f},
	{ -0.7453f,	0.0000f,	1.2142f },
	{ 0.7453f,	0.0000f,	1.2142f},
	{ 1.2142f,	0.7453f,	0.0000f },
	{ 0.0000f, -1.2142f, 0.7453f }, // points surrounding bottom 
	{-1.2142f,	-0.7453f,	0.0000f},
	{ -0.7453f,	0.0000f,	-1.2142f },
	{ 0.7453f, 0.0000f, -1.2142f },	
{ 1.2142f, -0.7453f, 0.0000f },	
{ 0.0000f, -1.2142f, -0.7453f }, // bottom
	};
	static int indices_dodeca3[] =
	{
		0,1,2,SO_END_FACE_INDEX,
		0,2,3,SO_END_FACE_INDEX,
		0,3,4,SO_END_FACE_INDEX,
		0,4,5,SO_END_FACE_INDEX,
		0,5,1,SO_END_FACE_INDEX,
		
		11,6,7,SO_END_FACE_INDEX,
		11,7,8,SO_END_FACE_INDEX,
		11,8,9,SO_END_FACE_INDEX,
		11,9,10,SO_END_FACE_INDEX,
		11,10,6,SO_END_FACE_INDEX,
		1,8,2,SO_END_FACE_INDEX, 
		8,7,2,SO_END_FACE_INDEX,
		2,7,3,SO_END_FACE_INDEX,
		7,6,3,SO_END_FACE_INDEX,
		3,6,4,SO_END_FACE_INDEX,
		6,10,4,SO_END_FACE_INDEX,
		4,10,5,SO_END_FACE_INDEX,
		10,9,5,SO_END_FACE_INDEX,
		5,9,1,SO_END_FACE_INDEX,
		9,8,1,SO_END_FACE_INDEX
	};
		static int indices3[] =
		{
			1,	2,	3,	4,	5,	SO_END_FACE_INDEX,	//	top	face
			0,	1,	8,	7,	3,	SO_END_FACE_INDEX,	//	5	faces about top	j
			0,	2,	7,	6,	4,	SO_END_FACE_INDEX, 
			0,	3,	6,	10,	5,	SO_END_FACE_INDEX,
			0,	4,	10,	9,	1,	SO_END_FACE_INDEX,
			0, 5, 9, 8, 2, SO_END_FACE_INDEX,
			9,	5,	4,	6,	11,	SO_END_FACE_INDEX,	//	5	faces about bottoi
			10,	4,	3,	7,	11,	SO_END_FACE_INDEX,
			6,	3,	2,	8,	11,	SO_END_FACE_INDEX,	
			7,	2,	1,	9,	11,	SO_END_FACE_INDEX,	
			8,	1, 5,10, 11, SO_END_FACE_INDEX,
			6,	7,	8,	9,	10,	SO_END_FACE_INDEX,	//	bottom face
		};
		static SoSeparator 
			makeStellatedDodecahedron(boolean mode)
		{
			SoSeparator result = new SoSeparator(); 
			result.ref();
			SoCoordinate3 myCoords = new SoCoordinate3(); 
			myCoords.point.setValues(0, 12, dodecaedre3); 
			result.addChild(myCoords);

			SoIndexedFaceSet myFaceSet = new SoIndexedFaceSet(); 
			if (mode) myFaceSet.coordIndex.setValues(0, 72, indices3); 
			else myFaceSet.coordIndex.setValues(0, 80, indices_dodeca3); 
			result.addChild( myFaceSet);
			result.unrefNoDelete(); return result;
}

public static SoNode createDemoSceneSoIndexedFaceSet() {
	SoSeparator root = makeStellatedDodecahedron(true); 
	root.ref();
	return root;
}

}
