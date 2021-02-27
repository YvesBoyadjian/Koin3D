package jexample.parts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.lwjgl.opengl.swt.GLData;
import jscenegraph.coin3d.inventor.nodes.SoCoordinate3;
import jscenegraph.database.inventor.nodes.SoFaceSet;
import jscenegraph.database.inventor.nodes.SoMaterial;
import jscenegraph.database.inventor.nodes.SoMaterialBinding;
import jscenegraph.database.inventor.nodes.SoNormal;
import jscenegraph.database.inventor.nodes.SoNormalBinding;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.database.inventor.nodes.SoShapeHints;
import jscenegraph.database.inventor.nodes.SoTriangleStripSet;
import jsceneviewer.inventor.qt.SoQt;
import jsceneviewer.inventor.qt.SoQtCameraController.Type;
import jsceneviewer.inventor.qt.viewers.SoQtExaminerViewer;
import jsceneviewer.inventor.qt.viewers.SoQtFullViewer.BuildFlag;

public class SimpleExamples {


	// Make two simple polygons out of strips of triangles
	static SoSeparator makeSimpleStripSet() {

	  // Two polygons:
	  // - The first polygon is a bent rectangle built with 4 triangles defined by 6 vertices:
	  //   the first two triangles live in the x/y plane, the second two ones live in the z/y plane
	  // - The second polygon is a rotated square built with 2 triangles defined by 4 vertices; it lives in the x/y plane
	  float vertexPositions[/*10*/][/*3*/] =
	  { {   0,  0, 0  },{   0, 10, 0  },
	    {  10,  0, 0  },{  10, 10, 0  },
	    {  10,  0, 10  },{  10, 10, 10  },
	    {   5, 15, 0  },
	    {  10, 20, 0  },
	    {  10, 10, 0  },
	    {  15, 15, 0  } };

	  // We declare the number of vertices in each polygon:
	  // There are 2 polygons, we use one entry of the array per polygon:
	  // - the first polygon is a strip of 4 triangles using 6 vertices
	  // - the second polygon is composed by 2 triangles using 4 vertices
	  int numVertices[] = { 6,4 };

	  SoSeparator result = new SoSeparator();
	  result.ref();

	  // A shape hints tells the ordering of polygons.
	  // This ensures double-sided lighting.
	  SoShapeHints myHints = new SoShapeHints();
	  myHints.vertexOrdering.setValue( SoShapeHints.VertexOrdering.COUNTERCLOCKWISE);
	  result.addChild(myHints);

	  // define the coordinates of the vertices
	  SoCoordinate3 myCoords = new SoCoordinate3();
	  myCoords.point.setValues(0, 10, vertexPositions);
	  result.addChild(myCoords);

	  // define the SoTriangleStripSet, which will use the coordinates
	  // stored in the preceding SoCoordinate3 node,
	  // by using them accordingly to the information
	  // stored in the numVertices array.
	  SoTriangleStripSet s = new SoTriangleStripSet();
	  s.numVertices.setValues(0, 2, numVertices); // 2 is the number of polygons defined in numVertices

	  result.addChild(s);
	  result.unrefNoDelete();
	  return result;
	}

	// TODO: to be fixed! Normals are not calculated properly, visualization is wrong!
	// Make two simple polygons out of strips of triangles, with explicit normals
	static SoSeparator makeSimpleStripSetWithNorms() {

	  // Two polygons:
	  // - The first polygon is a bent rectangle built with 4 triangles defined by 6 vertices:
	  //   the first two triangles live in the x/y plane, the second two ones live in the z/y plane
	  // - The second polygon is a rotated square built with 2 triangles defined by 4 vertices; it lives in the x/y plane
	  float vertexPositions[/*10*/][/*3*/] =
	  { {   0,  0, 0  },{   0, 10, 0  },
	    {  10,  0, 0  },{  10, 10, 0  },
	    {  10,  0, 10  },{  10, 10, 10  },
	    {   5, 15, 0  },
	    {  10, 20, 0  },
	    {  10, 10, 0  },
	    {  15, 15, 0  } };

	  // We declare the number of vertices in each polygon:
	  // There are 2 polygons, we use one entry of the array per polygon:
	  // - the first polygon is a strip of 4 triangles using 6 vertices
	  // - the second polygon is composed by 2 triangles using 4 vertices
	  int numVertices[/*2*/] = { 6,4 };

	  // Normals for each polygon:
	  // Each entry defines a vector, which is the normal to the polygon's surface
	  float norms[/*6*/][/*3*/] =
	  {
	    { 0, 0, 1}, {  0, 0, 1}, //the first two triangles living in the x/y plane (normals are // z)
	    {1, 0, 0}, { 1, 0, 0}, //the second two triangles living in the z/y plane (normals are // -x)
	    { 0, 0, -1}, {  0, 0, 1}//the 4 triangles of the rotated square, all living in the x/y plane (normals are // z)
	  };


	  SoSeparator result = new SoSeparator();
	  result.ref();

	  // A shape hints tells the ordering of polygons.
	  // This ensures double-sided lighting.
	  SoShapeHints myHints = new SoShapeHints();
	  myHints.vertexOrdering.setValue( SoShapeHints.VertexOrdering.CLOCKWISE);
	  result.addChild(myHints);

	  // Define the normals used:
	  SoNormal myNormals = new SoNormal();
	  myNormals.vector.setValues(0, 6, norms);
	  result.addChild(myNormals);
	  SoNormalBinding myNormalBinding = new SoNormalBinding();
	  myNormalBinding.value.setValue( SoNormalBinding.Binding.PER_PART);
	  result.addChild(myNormalBinding);

	  // define the coordinates of the vertices
	  SoCoordinate3 myCoords = new SoCoordinate3();
	  myCoords.point.setValues(0, 10, vertexPositions);
	  result.addChild(myCoords);

	  // define the SoTriangleStripSet, which will use the coordinates
	  // stored in the preceding SoCoordinate3 node,
	  // by using them accordingly to the information
	  // stored in the numVertices array.
	  SoTriangleStripSet s = new SoTriangleStripSet();
	  s.numVertices.setValues(0, 2, numVertices); // 2 is the number of polygons defined in numVertices

	  result.addChild(s);
	  result.unrefNoDelete();
	  return result;
	}


	// Make a "circle shape" circle with a FaceSet
	static SoSeparator makeCircle() {


	  float sqrt2_2 = (float)Math.sin(Math.PI / 4);
	  float vertexPositions[/*9*/][/*3*/] =
	  { {  -1,  0, 0  },
	    {  -sqrt2_2,  sqrt2_2, 0 },
	    {   0,  1, 0  },
	    {   sqrt2_2,  sqrt2_2, 0 },
	    {   1,  0, 0  },
	    {   sqrt2_2, -sqrt2_2, 0 },
	    {   0, -1, 0  },
	    {  -sqrt2_2, -sqrt2_2, 0 },
	    {  -1,  0, 0  },
	    };

	  // We declare the number of vertices in each polygon:
	  // There are 2 polygons, we use one entry of the array per polygon:
	  // - the first polygon is a strip of 4 triangles using 6 vertices
	  // - the second polygon is composed by 2 triangles using 4 vertices
	  int numVertices[/*1*/] = { 9 };

	  SoSeparator result = new SoSeparator();
	  result.ref();

	  // A shape hints tells the ordering of polygons.
	  // This ensures double-sided lighting.
	  SoShapeHints myHints = new SoShapeHints();
	  myHints.vertexOrdering.setValue( SoShapeHints.VertexOrdering.COUNTERCLOCKWISE);
	  result.addChild(myHints);


	  // define the coordinates of the vertices
	  SoCoordinate3 myCoords = new SoCoordinate3();
	  myCoords.point.setValues(0, 9, vertexPositions);
	  result.addChild(myCoords);

	  // define the SoTriangleStripSet, which will use the coordinates
	  // stored in the preceding SoCoordinate3 node,
	  // by using them accordingly to the information
	  // stored in the numVertices array.
	  SoFaceSet s = new SoFaceSet();
	  s.numVertices.setValues(0, 1, numVertices); // 1 is the number of polygons defined in numVertices

	  result.addChild(s);
	  result.unrefNoDelete();
	  return result;
	}


	// Make a flag and a pole
	static SoSeparator makePennant() {

	  // Two polygons.
	  // The first is a flag made of a strip of many triangles defined by 32 vertices
	  // The second on is a pole defined by a strip if triangles defined by 8 vertices
	  float vertexPositions[/*40*/][/*3*/] =
	  { {  0, 12  ,    0},{  0,   15,    0},
	  {2.1f, 12.1f,  -.2f},{2.1f, 14.6f,  -.2f},
	  { 4,  12.5f,  -.7f},{  4, 14.5f,  -.7f},
	  {4.5f, 12.6f,  -.8f},{4.5f, 14.4f,  -.8f},
	  {  5, 12.7f,   -1},{  5, 14.4f,   -1},
	  {4.5f, 12.8f, -1.4f},{4.5f, 14.6f, -1.4f},
	  {  4, 12.9f, -1.6f},{  4, 14.8f, -1.6f},
	  {3.3f, 12.9f, -1.8f},{3.3f, 14.9f, -1.8f},
	  {  3, 13  , -2.0f},{  3, 14.9f, -2.0f},
	  {3.3f, 13.1f, -2.2f},{3.3f, 15.0f, -2.2f},
	  {  4, 13.2f, -2.5f},{  4, 15.0f, -2.5f},
	  {  6, 13.5f, -2.2f},{  6, 14.8f, -2.2f},
	  {  8, 13.4f,   -2},{  8, 14.6f,   -2},
	  { 10, 13.7f, -1.8f},{ 10, 14.4f, -1.8f},
	  { 12, 14  , -1.3f},{ 12, 14.5f, -1.3f},
	  { 15, 14.9f, -1.2f},{ 15, 15  , -1.2f},
	  {-.5f, 15,  0},{-.5f,0,  0},
	  {  0,15,.5f},{  0,0,.5f},
	  {  0, 15,-.5f},{  0,0,-.5f},
	  {-.5f,15, 0},{-.5f,0, 0} };

	  // We declare the number of vertices in each polygon:
	  // There are 2 polygons, we use one entry of the array per polygon:
	  // - the first polygon is a complex flag and it uses the first 32 vertices
	  // - the second polygon is a pole and it uses the last 8 vertices
	  int numVertices[/*2*/] = { 32,8 };

	  SoSeparator result = new SoSeparator();
	  result.ref();

	  // A shape hints tells the ordering of polygons.
	  // This ensures double-sided lighting.
	  SoShapeHints myHints = new SoShapeHints();
	  myHints.vertexOrdering.setValue(SoShapeHints.VertexOrdering.COUNTERCLOCKWISE);
	  result.addChild(myHints);

	  // Define colors for the strips
	  // Colors for the 12 faces
	  float colors[/*2*/][/*3*/] ={
	    { .5f, .5f,  1 }, // purple flag
	    { .4f, .4f, .4f }, // grey flagpole
	  };
	  SoMaterial myMaterials = new SoMaterial();
	  myMaterials.diffuseColor.setValues(0,/* 2,*/ colors);
	  result.addChild(myMaterials);
	  SoMaterialBinding myMaterialBinding = new SoMaterialBinding();
	  myMaterialBinding.value.setValue(SoMaterialBinding.Binding.PER_PART);
	  result.addChild(myMaterialBinding);   // D

	  SoCoordinate3 myCoords = new SoCoordinate3();
	  myCoords.point.setValues(0, 40, vertexPositions);
	  result.addChild(myCoords);

	  SoTriangleStripSet s = new SoTriangleStripSet();
	  s.numVertices.setValues(0, 2, numVertices);
	  result.addChild(s);
	  result.unrefNoDelete();
	  return result;
	}



	// creates an obelisk using a face set composed of eight faces.
	static SoSeparator makeObeliskFaceSet()
	{
	    //  Eight polygons. The first four are triangles
	    //  The second four are quadrilaterals for the sides.
	    float vertices[/*28*/][/*3*/] =
	    {
	      { 0, 30, 0}, {-2,27, 2}, { 2,27, 2},            //front tri
	      { 0, 30, 0}, {-2,27,-2}, {-2,27, 2},            //left  tri
	      { 0, 30, 0}, { 2,27,-2}, {-2,27,-2},            //rear  tri
	      { 0, 30, 0}, { 2,27, 2}, { 2,27,-2},            //right tri
	      {-2, 27, 2}, {-4,0, 4}, { 4,0, 4}, { 2,27, 2},  //front quad
	      {-2, 27,-2}, {-4,0,-4}, {-4,0, 4}, {-2,27, 2},  //left  quad
	      { 2, 27,-2}, { 4,0,-4}, {-4,0,-4}, {-2,27,-2},  //rear  quad
	      { 2, 27, 2}, { 4,0, 4}, { 4,0,-4}, { 2,27,-2}   //right quad
	    };

	    // We have to declare the number of vertices in each polygon:
	    // There are eight polygons, we use one entry of the array per polygon:
	    // - the first, second, third, and fourth polygons have 3 vertices (triangle)
	    // - the fifth, sixth, seventh, and eight polygons have 4 vertices (rectangles)
	    int numvertices[/*8*/] = {3, 3, 3, 3, 4, 4, 4, 4};

	    // Normals for each polygon:
	    // Each entry defines a vector, which is the normal to the polygon's surface
	    float norms[/*8*/][/*3*/] =
	    {
	      {0, .555f,  .832f}, {-.832f, .555f, 0}, //front, left tris
	      {0, .555f, -.832f}, { .832f, .555f, 0}, //rear, right tris
	      {0, .0739f,  .9973f}, {-.9972f, .0739f, 0},//front, left quads
	      {0, .0739f, -.9973f}, { .9972f, .0739f, 0},//rear, right quads
	    };

	   SoSeparator obelisk = new SoSeparator();
	   obelisk.ref();

	   // Define the normals used:
	   SoNormal myNormals = new SoNormal();
	   myNormals.vector.setValues(0, 8, norms);
	   obelisk.addChild(myNormals);
	   SoNormalBinding myNormalBinding = new SoNormalBinding();
	   myNormalBinding.value.setValue( SoNormalBinding.Binding.PER_FACE);
	   obelisk.addChild(myNormalBinding);

	   // Define material for obelisk
	   SoMaterial myMaterial = new SoMaterial();
	   myMaterial.diffuseColor.setValue(.4f, .4f, .4f);
	   obelisk.addChild(myMaterial);

	   // Define coordinates for vertices
	   SoCoordinate3 myCoords = new SoCoordinate3();
	   myCoords.point.setValues(0, 28, vertices);
	   obelisk.addChild(myCoords);

	   // Define the FaceSet
	   // it will use the coordinates stored in the preceding SoCoordinate3 node
	   // to build the faces, using them accordingly to the definitions
	   // stored in the numvertices array
	   SoFaceSet myFaceSet = new SoFaceSet();
	   myFaceSet.numVertices.setValues(0, 8, numvertices);
	   obelisk.addChild(myFaceSet);

	   obelisk.unrefNoDelete();
	   return obelisk;
	}



	public static void main(String[] argv)
	{
		Display display = new Display ();
		Shell shell = new Shell(display);
		
		FillLayout fillLayout = new FillLayout();
		fillLayout.type = SWT.VERTICAL;
		shell.setLayout(fillLayout);
		

	  // Initialize the Qt system:
	  //QApplication app(argc, argv);

	  // Make a main window:
	  //QWidget mainwin;
	  //mainwin.resize(400,400);

	  // Initialize SoQt
	  SoQt.init("");


	  //--- Define the scenegraph

	  // The root of a scene graph
	  SoSeparator root = new SoSeparator();
	  root.ref();

	  //--- Add 3D objects to the scene

	  //root.addChild( makeSimpleStripSet() );
	   //root.addChild( makeSimpleStripSetWithNorms() );
	 root.addChild( makePennant() );
	 //root.addChild( makeObeliskFaceSet() );
	  //root.addChild( makeCircle() );

	  //--- Init the viewer and launch the app

	  // Initialize an examiner viewer:
	  SoQtExaminerViewer eviewer = new SoQtExaminerViewer(BuildFlag.BUILD_ALL,Type.BROWSER,shell);
	  eviewer.setSceneGraph(root);
	  
	  eviewer.setFormat(eviewer.format(), 0);
	  //eviewer.show();
	  eviewer.buildWidget(0);

	  // Pop up the main window.
	  //SoQt::show(&mainwin);
	  shell.open();

	  // Loop until exit.
	  //SoQt::mainLoop();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}

	  // Clean up resources.
	  //delete eviewer;
	  root.unref();

	  //return app.exec();
	}
}
