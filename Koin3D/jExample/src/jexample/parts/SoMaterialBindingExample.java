/**
 * 
 */
package jexample.parts;

import jscenegraph.coin3d.inventor.nodes.SoCoordinate3;
import jscenegraph.database.inventor.nodes.SoIndexedFaceSet;
import jscenegraph.database.inventor.nodes.SoMaterial;
import jscenegraph.database.inventor.nodes.SoMaterialBinding;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSeparator;

/**
 * @author Yves Boyadjian
 *
 */
public class SoMaterialBindingExample {


static float dodecaedre[][] =

{

	{ 0.0000f, 1.2142f, 0.7453f }, // top

	{ 0.0f, 1.2142f, -0.7453f }, // points surrounding top
	{ -1.21f, 0.7453f, 0.0000f },
	{ -0.745f, 0.0000f, 1.2142f },
	{ 0.745f,0.0000f, 1.2142f },
	{ 1.214f,0.7453f, 0.0000f },

	{ 0.0f, -1.2142f, 0.7453f }, // points surrounding bottom
	{ -1.2142f, -0.7453f, 0.0000f },
	{ -0.7453f, 0.0000f, -1.2142f },
	{ 0.7453f, 0.0000f, -1.2142f },
	{ 1.2142f, -0.7453f, 0.0000f },

	{ 0.0000f, -1.2142f, -0.7453f }, // bottom
};

static final int SO_END_FACE_INDEX = SoIndexedFaceSet.SO_END_FACE_INDEX;

static int indices_dodeca[] =
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
static float rgb_ambient[][] = {

	{ 0.00714286f,0.001690f,0 },
	{ 0.00746438f,0.00673081f,0.00690282f },
	{ 0.00746438f,0.00673081f,0.00690282f },

	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},

	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},

	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},

	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
};

static float rgb_diffuse[][] = {
	{ 0.314286f,0.0743647f,0 },
	{ 0.0291577f,0.0262922f,0.0269642f },
	{ 0.0291577f,0.0262922f,0.0269642f },


	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},

	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},

	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},

	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
};

static float rgb_specular[][] = {
	{ 1,0.76684f,0 },
	{ 0.641609f,0.976208f,0.979592f },
	{ 0.938776f,0.0550317f,0.0550317f },


	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},

	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},

	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},

	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
	{0,0,0},
};

static float shininess[] = {
	0.048484f,
	0.0612245f,
	0.0612245f,

	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,

	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,

	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,

	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,
	0,
};


static SoSeparator makeStellatedDodecahedron()

{
	SoSeparator result = new SoSeparator();
	result.ref();

	SoCoordinate3 myCoords = new SoCoordinate3();



	myCoords.point.setValues(0, 12, dodecaedre);
	result.addChild(myCoords);

	SoIndexedFaceSet myFaceSet = new SoIndexedFaceSet();
	myFaceSet.coordIndex.setValues(0, 80, indices_dodeca);
	result.addChild(myFaceSet);

	result.unrefNoDelete();
	return result;

}

static public SoNode createDemoSceneSoMaterialBinding() {

	SoSeparator root = new SoSeparator();
	SoMaterial coul = new SoMaterial();

	SoMaterialBinding attach = new SoMaterialBinding();

	root.ref();


coul.ambientColor.setValues(0,  rgb_ambient);
coul.diffuseColor.setValues(0,  rgb_diffuse);
coul.specularColor.setValues(0,  rgb_specular);
coul.shininess.setValues(0,  shininess);

char c = 'v';
switch (c)
{
case 'p':
	attach.value.setValue( SoMaterialBinding.Binding.PER_PART);
	break;
case 'v':
	attach.value.setValue( SoMaterialBinding.Binding.PER_VERTEX);

	break;

case 'f':
	attach.value.setValue( SoMaterialBinding.Binding.PER_FACE);
	break;

default:
	attach.value.setValue( SoMaterialBinding.Binding.OVERALL);

}

root.addChild(attach);
root.addChild(coul);
root.addChild( makeStellatedDodecahedron());

return root;
}


static int indices_coul_vertex[] = {
	1,2,0,
	1,2,0,
	1,2,0,
	1,2,0,
	1,2,0,
	1,2,0,
	1,2,0,
	1,2,0,
	1,2,0,
	1,2,0,
	1,2,0,
	1,2,0,
	1,2,0,
	1,2,0,
	1,2,0,
	1,2,0,
	1,2,0,
	1,2,0,
	1,2,0,
	1,2,0,
	1,2,0,
	1,2,0,
	1,2,0,
	1,2,0,
	1,2,0,
	1,2,0,
	1,2,0,
};

static int indices_coul_face[] = 
{
	1,1,1,1,1,
	2,2,2,2,2,
	0,0,0,0,0,
	0,0,0,0,0,
};


public static SoSeparator makeStellatedDodecahedron(char c)

{
	SoSeparator result = new SoSeparator();
	result.ref();

	SoCoordinate3 myCoords = new SoCoordinate3();
	myCoords.point.setValues(0, /*12,*/ dodecaedre);
	result.addChild(myCoords);

	SoIndexedFaceSet myFaceSet = new SoIndexedFaceSet();
	myFaceSet.coordIndex.setValues(0, 80, indices_dodeca);

	if (c == 'p')myFaceSet.materialIndex.setValues(0, 20, indices_coul_face);
	else myFaceSet.materialIndex.setValues(0, 80, indices_coul_vertex);
	result.addChild(myFaceSet);

	result.unrefNoDelete();
	return result;
}


public static SoNode createDemoSceneSoMaterialIndexedBinding() {
	SoSeparator root = new SoSeparator();
	SoMaterial coul = new SoMaterial();
	SoMaterialBinding attach = new SoMaterialBinding();

	root.ref();


coul.ambientColor.setValues(0, /*80,*/ rgb_ambient);
coul.diffuseColor.setValues(0, /*80,*/ rgb_diffuse);
coul.specularColor.setValues(0, /*80,*/ rgb_specular);
coul.shininess.setValues(0, /*80,*/ shininess);

root.addChild(attach);
root.addChild(coul);

char c = 'v';
if (c == 'v') {

	attach.value.setValue( SoMaterialBinding.Binding.PER_VERTEX_INDEXED);
	root.addChild(makeStellatedDodecahedron('v'));
}
else
{
	root.addChild(makeStellatedDodecahedron('p'));
	attach.value.setValue( SoMaterialBinding.Binding.PER_PART_INDEXED);
}

return root;
}


}
