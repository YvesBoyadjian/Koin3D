package jexample.parts;

import java.io.IOException;
import java.net.URL;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GLDebugMessageCallbackI;
import org.osgi.framework.Bundle;

import jscenegraph.coin3d.fxviz.nodes.SoShadowGroup;
import jscenegraph.coin3d.inventor.nodes.SoCoordinate3;
import jscenegraph.coin3d.inventor.nodes.SoTexture2;
import jscenegraph.coin3d.inventor.nodes.SoVertexProperty;
import jscenegraph.database.inventor.SbColor;
import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SoDB;
import jscenegraph.database.inventor.actions.SoGLRenderAction.TransparencyType;
import jscenegraph.database.inventor.engines.SoElapsedTime;
import jscenegraph.database.inventor.nodes.SoBaseColor;
import jscenegraph.database.inventor.nodes.SoCamera;
import jscenegraph.database.inventor.nodes.SoCone;
import jscenegraph.database.inventor.nodes.SoCube;
import jscenegraph.database.inventor.nodes.SoCylinder;
import jscenegraph.database.inventor.nodes.SoDirectionalLight;
import jscenegraph.database.inventor.nodes.SoDrawStyle;
import jscenegraph.database.inventor.nodes.SoEnvironment;
import jscenegraph.database.inventor.nodes.SoFile;
import jscenegraph.database.inventor.nodes.SoGroup;
import jscenegraph.database.inventor.nodes.SoIndexedFaceSet;
import jscenegraph.database.inventor.nodes.SoIndexedTriangleStripSet;
import jscenegraph.database.inventor.nodes.SoLightModel;
import jscenegraph.database.inventor.nodes.SoMaterial;
import jscenegraph.database.inventor.nodes.SoMaterialBinding;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoPerspectiveCamera;
import jscenegraph.database.inventor.nodes.SoPointLight;
import jscenegraph.database.inventor.nodes.SoRotationXYZ;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.database.inventor.nodes.SoSphere;
import jscenegraph.database.inventor.nodes.SoText3;
import jscenegraph.database.inventor.nodes.SoTextureCoordinate2;
import jscenegraph.database.inventor.nodes.SoTranslation;
import jscenegraph.database.inventor.nodes.SoTriangleStripSet;
import jsceneviewer.inventor.qt.SoQt;
import jsceneviewer.inventor.qt.SoQtCameraController.Type;
import jsceneviewer.inventor.qt.viewers.SoQtExaminerViewer;
import jsceneviewer.inventor.qt.viewers.SoQtFullViewer.BuildFlag;

public class SamplePart {

	private SoQtExaminerViewer viewer;

	@Inject
	private MDirtyable dirty;

	@PostConstruct
	public void createComposite(Composite parent) {
		//parent.setLayout(new GridLayout(1, false));

		SoQt.init("demo");
		//SoDB.setDelaySensorTimeout(new SbTime(10.0));
		//SoDB.setRealTimeInterval(new SbTime(10.0));
		
		int style = SWT.NO_BACKGROUND;
		
		viewer = new SoQtExaminerViewer(BuildFlag.BUILD_ALL,Type.BROWSER,parent,style);
	    //viewer.setColorBitDepth (10);
		//viewer.setAntialiasing(true, 16);
		viewer.setHeadlight(true);
		//viewer.getSceneHandler().setTransparencyType(TransparencyType.DELAYED_ADD);
		viewer.getSceneHandler().setTransparencyType(TransparencyType.BLEND);
		//viewer.getSceneHandler().setTransparencyType(TransparencyType.SORTED_LAYERS_BLEND);
		
	    viewer.buildWidget(style);
	    
	    Bundle bundle = Platform.getBundle("jExample");
	    URL fileURL = bundle.getEntry("examples_iv/duck.iv");
	    String fileStr = null;
	    try {
	    	URL url = FileLocator.resolve(fileURL);
	    	//URI uri = fileURL.toURI();
	    	//File file = new File(uri); 
	        fileStr = url.getPath().substring(1);//file.getAbsolutePath();
	    } /*catch (URISyntaxException e1) {
	        e1.printStackTrace();
	    } */catch (IOException e1) {
	        e1.printStackTrace();
	    }	    
	    
	    viewer.setSceneGraph(
	    		//SoMaterialBindingExample.createDemoSceneSoMaterialBinding()
	    		//SoMaterialBindingExample.createDemoSceneSoMaterialIndexedBinding()
	    		//IndexedTriangleStrip.createDemoSceneSoIndexedTriangleStrip()
	    		createDemoScenePerformance()
	    		//createDemoSceneSoMaterialShapeBinding()
	    		//SoFaceSetTest.createDemoSceneSoFaceSet()
	    		//SoIndexedFaceSetTest.createDemoSceneSoIndexedFaceSet()
	    		//createDemoSceneTimeWatch()
	    		//TextureCoordinatePlane.computeRoot()
	    		//TextureCoordinates.createScene()
	    		//Text2.createScene()
	    		//Text3.createScene()
	    		//FancyText3.createScene()
	    		//Selection.createScene(viewer)
	    		//Selection.createSceneSelection()
	    		//Manips.createScene()
	    		//(new PickTrackball()).createScene()
	    		//FrolickingWords.createRoot()
	    		//Balance.createScene(viewer)
	    		//Obelisque.makeObeliskFaceSet()
	    		//Drapeau.makePennant()
	    		//Arche.makeArch()
	    		//RotatingSensor.create("C:/eclipseWorkspaces/examples_iv/transparentbluecylinder.iv")
	    		//Canards.create("C:\\eclipseWorkspaces\\inventor-2.1.5-10.src\\inventor\\apps\\examples\\data\\duck.iv")
	    		//Orbits.main()
	    		//WorldAnimated.main()
	    		//DualWorld.main()
	    		//createDemoSceneTransparentCubes()
	    		);
	    //CameraSensor.attach(viewer);
	    viewer.viewAll();
	}

	@Focus
	public void setFocus() {
		if(viewer != null)
			viewer.setFocus();
	}

	@Persist
	public void save() {
		dirty.setDirty(false);
	}
	
	static SoNode createDemoScene()
{
    SoSeparator scene = new SoSeparator();
    scene.ref();
//    SoTransformManip manip = new SoTransformManip();
//    scene.addChild (manip);
    //scene.addChild (new SoCube());
    scene.addChild (new SoCone());
    //scene.addChild (new SoSphere());
    return scene;
}

	static SoNode createDemoSceneRedCone()
	{
		SoSeparator root = new SoSeparator();
		root.ref();

		// material
		SoMaterial material = new SoMaterial();
		material.diffuseColor.setValue(1.0f, 0.0f, 0.0f);
		root.addChild(material);

		root.addChild(new SoCone());
		return root;
	}

	static SoNode createDemoSceneRedConeEngine()
	{
		// create scene root
		  SoSeparator root = new SoSeparator();
		  root.ref();
		 
		  // camera
		  SoPerspectiveCamera camera = new SoPerspectiveCamera();
		  root.addChild(camera);
		 
		  // light
		  root.addChild(new SoDirectionalLight());
		 
		  // material
		  SoMaterial material = new SoMaterial();
		  material.diffuseColor.setValue(1.0f, 0.0f, 0.0f);
		  root.addChild(material);
		 
		  // rotation node
		  SoRotationXYZ rotXYZ = new SoRotationXYZ();
		  rotXYZ.axis.setValue(SoRotationXYZ.Axis.X);
		  root.addChild(rotXYZ);
		 
		  // connect engine to rotation node
		  SoElapsedTime counter = new SoElapsedTime();
		  rotXYZ.angle.connectFrom(counter.timeOut);
		 
		  // cone
		  root.addChild(new SoCone());
		  
		  return root;
	}


static SoSeparator createPlanet(float radius, float distance,
                          float initialAngle, SbColor color)
{
  SoSeparator root = new SoSeparator();

  // material of planet
  SoMaterial material = new SoMaterial();
  material.diffuseColor.setValue(color);
  root.addChild(material);

  // revolution around the Sun
  SoRotationXYZ rotY = new SoRotationXYZ();
  rotY.axis.setValue( SoRotationXYZ.Axis.Y);
  root.addChild(rotY);

  // connect engine to rotation node
  SoElapsedTime counter = new SoElapsedTime();
  counter.speed.setValue(20.f/distance);
  rotY.angle.connectFrom(counter.timeOut);

  // translation from the Sun
  SoTranslation trans = new SoTranslation();
  trans.translation.setValue(distance*(float)(Math.cos(initialAngle)), 0.f,
                              distance*(float)(-Math.sin(initialAngle)));
  root.addChild(trans);

  // planet geometry
  SoSphere sphere = new SoSphere();
  sphere.radius.setValue(radius);
  root.addChild(sphere);

  return root;
}

	
	static SoNode createDemoSceneSolarSystem() {
		  // create scene root
		  SoSeparator root = new SoSeparator();
		  root.ref();


		  // model of the Sun

		  // diffuse material
		  SoMaterial sunMat = new SoMaterial();
		  sunMat.diffuseColor.setValue(1.0f, 1.0f, 0.3f);
		  root.addChild(sunMat);

		  // sphere of radius 10
		  SoSphere sun = new SoSphere();
		  sun.radius.setValue(10.f);
		  root.addChild(sun);


		  // model of the Earth
		  root.addChild(createPlanet(4.0f, 45.f, 0.f,
		                              new SbColor(0.7f, 0.7f, 1.0f)));

		  // model of the Mercury
		  root.addChild(createPlanet(2.0f, 20.f, (float)(4*Math.PI/3),
		                              new SbColor(1.0f, 0.3f, 0.3f)));

		  // model of the Venus
		  root.addChild(createPlanet(3.0f, 30.f, (float)(3*Math.PI/3),
		                              new SbColor(1.0f, 0.6f, 0.0f)));

		  return root;
	}
	

static SoSeparator createPlanetSolarLight(float radius, float distance,
                          float initialAngle, SbColor color)
{
  SoSeparator root = new SoSeparator();

  // material of planet
  // ambient and diffuse color is set to the planet color,
  // specular and emissive colors are left on their default value (0.f, 0.f, 0.f)
  SoMaterial material = new SoMaterial();
  material.ambientColor.setValue(color);
  material.diffuseColor.setValue(color);
  root.addChild(material);

  // revolution around the Sun
  SoRotationXYZ rotY = new SoRotationXYZ();
  rotY.axis.setValue(SoRotationXYZ.Axis.Y);
  root.addChild(rotY);

  // connect engine to rotation node
  SoElapsedTime counter = new SoElapsedTime();
  counter.speed.setValue(20.f/distance);
  rotY.angle.connectFrom(counter.timeOut);

  // translation from the Sun
  SoTranslation trans = new SoTranslation();
  trans.translation.setValue(distance*(float)(Math.cos(initialAngle)), 0.f,
                              distance*(float)(-Math.sin(initialAngle)));
  root.addChild(trans);

  // planet geometry
  SoSphere sphere = new SoSphere();
  sphere.radius.setValue(radius);
  root.addChild(sphere);

  return root;
}


	static SoNode createDemoSceneSolarLight() {
		  // create scene root
		  SoSeparator root = new SoSeparator();
		  root.ref();

		  // environment
		  // ambientColor is left on white (default value)
		  // while we set its intensity on 0.25.
		  SoEnvironment envir = new SoEnvironment();
		  envir.ambientIntensity.setValue(0.25f);
		  root.addChild(envir);


		  // model of the Sun

		  // ambient and diffuse color is set to dark yellow,
		  // emissive on light yellow and specular is left on its default value (black)
		  SoMaterial sunMat = new SoMaterial();
		  sunMat.ambientColor.setValue(0.5f, 0.5f, 0.15f);
		  sunMat.diffuseColor.setValue(0.5f, 0.5f, 0.15f);
		  sunMat.emissiveColor.setValue(0.9f, 0.9f, 0.3f);
		  root.addChild(sunMat);

		  // sphere of radius 10
		  SoSphere sun = new SoSphere();
		  sun.radius.setValue(10.f);
		  root.addChild(sun);

		  // light in the middle of the Sun
		  // all the objects in the scene graph following the light
		  // will be lit by the light
		  SoPointLight sunLight = new SoPointLight();
		  sunLight.location.setValue(0.f, 0.f, 0.f);
		  sunLight.intensity.setValue(0.75f);
		  root.addChild(sunLight);


		  // model of the Earth
		  root.addChild(createPlanetSolarLight(4.0f, 45.f, 0.f,
		                              new SbColor(0.7f, 0.7f, 1.0f)));

		  // model of the Mercury
		  root.addChild(createPlanetSolarLight(2.0f, 20.f, (float)(4*Math.PI/3),
		                              new SbColor(1.0f, 0.3f, 0.3f)));

		  // model of the Venus
		  root.addChild(createPlanetSolarLight(3.0f, 30.f, (float)(3*Math.PI/3),
		                              new SbColor(1.0f, 0.6f, 0.0f)));

		  return root;
	}	
	

// size of the skybox divided by two
private static final float SKY_BOX_SIZE2  = 50;

// coordinates of vertices for sky box
static final float[] P0  = { -SKY_BOX_SIZE2, -SKY_BOX_SIZE2, -SKY_BOX_SIZE2 };
static final float[] P1 ={  SKY_BOX_SIZE2, -SKY_BOX_SIZE2, -SKY_BOX_SIZE2 };
static final float[] P2 ={ -SKY_BOX_SIZE2,  SKY_BOX_SIZE2, -SKY_BOX_SIZE2 };
static final float[] P3 ={  SKY_BOX_SIZE2,  SKY_BOX_SIZE2, -SKY_BOX_SIZE2 };
static final float[] P4 ={ -SKY_BOX_SIZE2, -SKY_BOX_SIZE2,  SKY_BOX_SIZE2 };
static final float[] P5 ={  SKY_BOX_SIZE2, -SKY_BOX_SIZE2,  SKY_BOX_SIZE2 };
static final float[] P6 ={ -SKY_BOX_SIZE2,  SKY_BOX_SIZE2,  SKY_BOX_SIZE2 };
static final float[] P7 ={  SKY_BOX_SIZE2,  SKY_BOX_SIZE2,  SKY_BOX_SIZE2 };


// indices of sky box
static float[][][] skyBoxVertices/*[6][4][3]*/ =
{
  { P0, P1, P2, P3 }, // sky00
  { P6, P4, P2, P0 }, // sky06
  { P5, P4, P7, P6 }, // sky12
  { P5, P7, P1, P3 }, // sky18
  { P2, P3, P6, P7 }, // skyN0
  { P4, P5, P0, P1 }  // skyS0
};



static SoSeparator createSkyBox()
{
  SoSeparator root = new SoSeparator();

  // Set light model to BASE_COLOR. It means,
  // we will render pre-lit scene.
  // All the geometry will receive the color by its diffuse color
  // while lights and other material components have no effect.
  SoLightModel lmodel = new SoLightModel();
  lmodel.model.setValue(SoLightModel.Model.BASE_COLOR);
  root.addChild(lmodel);

  // SoBaseColor is setting just diffuse color of the material.
  // It is often used to speed-up rendering or together with
  // pre-lit scene (lightModel set to BASE_COLOR).
  SoBaseColor baseColor = new SoBaseColor();
  baseColor.rgb.setValue(1.f, 1.f, 1.f);
  root.addChild(baseColor);

  // SoDrawStyle is set to FILLED by default - resulting in filled triangles.
  // We are going to set it to LINES to render it as wire frame.
  SoDrawStyle drawStyle = new SoDrawStyle();
  drawStyle.style.setValue(SoDrawStyle.Style.LINES);
  drawStyle.lineWidth.setValue(2.f);
  root.addChild(drawStyle);

  // Coordinates of vertices
  // They are taken from skyBoxVertices.
  SoCoordinate3 coords = new SoCoordinate3();
  coords.point.setValues(0, 4*6, skyBoxVertices/*[0]*/);
  root.addChild(coords);

  // SoTriangleStripSet node is rendering triangles.
  // Each value in numVertices is determining the number of vertices
  // from SoCoordinate3 that will be used for the particular triangle strip.
  SoTriangleStripSet strip = new SoTriangleStripSet();
  for (int i=0; i<6; i++)
    strip.numVertices.set1Value(i, 4);
  root.addChild(strip);

  return root;
}



static SoSeparator createPlanetWireBox(float radius, float distance,
                          float initialAngle, SbColor color)
{
  SoSeparator root = new SoSeparator();

  // material of planet
  // ambient and diffuse color is set to the planet color,
  // specular and emissive colors are left on their default value (0.f, 0.f, 0.f)
  SoMaterial material = new SoMaterial();
  material.ambientColor.setValue(color);
  material.diffuseColor.setValue(color);
  root.addChild(material);

  // revolution around the Sun
  SoRotationXYZ rotY = new SoRotationXYZ();
  rotY.axis.setValue( SoRotationXYZ.Axis.Y);
  root.addChild(rotY);

  // connect engine to rotation node
  SoElapsedTime counter = new SoElapsedTime();
  counter.speed.setValue(20.f/distance);
  rotY.angle.connectFrom(counter.timeOut);

  // translation from the Sun
  SoTranslation trans = new SoTranslation();
  trans.translation.setValue(distance*(float)(+Math.cos(initialAngle)), 0.f,
                              distance*(float)(-Math.sin(initialAngle)));
  root.addChild(trans);

  // planet geometry
  SoSphere sphere = new SoSphere();
  sphere.radius.setValue(radius);
  root.addChild(sphere);

  return root;
}

	
	
	static SoNode createDemoSceneWireBox() {
		  // create scene root
		  SoSeparator root = new SoSeparator();
		  root.ref();

		  root.addChild(createSkyBox());

		  // environment
		  // ambientColor is left on white (default value)
		  // while we set its intensity on 0.25.
		  SoEnvironment envir = new SoEnvironment();
		  envir.ambientIntensity.setValue(0.25f);
		  root.addChild(envir);


		  // model of the Sun

		  // ambient and diffuse color is set to dark yellow,
		  // emissive on light yellow and specular is left on its default value (black)
		  SoMaterial sunMat = new SoMaterial();
		  sunMat.ambientColor.setValue(0.5f, 0.5f, 0.15f);
		  sunMat.diffuseColor.setValue(0.5f, 0.5f, 0.15f);
		  sunMat.emissiveColor.setValue(0.9f, 0.9f, 0.3f);
		  root.addChild(sunMat);

		  // sphere of radius 10
		  SoSphere sun = new SoSphere();
		  sun.radius.setValue(10.f);
		  root.addChild(sun);

		  // light in the middle of the Sun
		  // all the objects in the scene graph following the light
		  // will be lit by the light
		  SoPointLight sunLight = new SoPointLight();
		  sunLight.location.setValue(0.f, 0.f, 0.f);
		  sunLight.intensity.setValue(0.75f);
		  root.addChild(sunLight);


		  // model of the Earth
		  root.addChild(createPlanetWireBox(4.0f, 45.f, 0.f,
		                              new SbColor(0.7f, 0.7f, 1.0f)));

		  // model of the Mercury
		  root.addChild(createPlanetWireBox(2.0f, 20.f, (float)(4*Math.PI/3),
		                              new SbColor(1.0f, 0.3f, 0.3f)));

		  // model of the Venus
		  root.addChild(createPlanetWireBox(3.0f, 30.f, (float)(3*Math.PI/3),
		                              new SbColor(1.0f, 0.6f, 0.0f)));


		  return root;
	}
	

	// size of the skybox divided by two
	//private static final float SKY_BOX_SIZE2 = 50;

	// skybox vertices
	static float[][] skyBoxVerticesWireBoxIndexed =
	{
	  {-SKY_BOX_SIZE2, -SKY_BOX_SIZE2, -SKY_BOX_SIZE2},
	   {SKY_BOX_SIZE2, -SKY_BOX_SIZE2, -SKY_BOX_SIZE2},
	  {-SKY_BOX_SIZE2,  SKY_BOX_SIZE2, -SKY_BOX_SIZE2},
	   {SKY_BOX_SIZE2,  SKY_BOX_SIZE2, -SKY_BOX_SIZE2},
	  {-SKY_BOX_SIZE2, -SKY_BOX_SIZE2,  SKY_BOX_SIZE2},
	   {SKY_BOX_SIZE2, -SKY_BOX_SIZE2,  SKY_BOX_SIZE2},
	  {-SKY_BOX_SIZE2,  SKY_BOX_SIZE2,  SKY_BOX_SIZE2},
	   {SKY_BOX_SIZE2,  SKY_BOX_SIZE2,  SKY_BOX_SIZE2},
	};

	// skybox indices
	// indices are used to access skyBoxVertices when rendering,
	// -1 is used to terminate triangle strip
	static int skyBoxIndices[] =
	{
	  0, 1, 2, 3, -1,  // sky00
	  6, 4, 2, 0, -1,  // sky06
	  5, 4, 7, 6, -1,  // sky12
	  5, 7, 1, 3, -1,  // sky18
	  2, 3, 6, 7, -1,  // skyN0
	  4, 5, 0, 1, -1,  // skyS0
	};



	static SoSeparator createSkyBoxWireBoxIndexed()
	{
	  SoSeparator root = new SoSeparator();

	  // Set light model to BASE_COLOR. It means,
	  // we will render pre-lit scene.
	  // All the geometry will receive the color by its diffuse color
	  // while lights and other material components have no effect.
	  SoLightModel lmodel = new SoLightModel();
	  lmodel.model.setValue(SoLightModel.Model.BASE_COLOR);
	  root.addChild(lmodel);

	  // SoBaseColor is setting just diffuse color of the material.
	  // It is often used to speed-up rendering or together with
	  // pre-lit scene (lightModel set to BASE_COLOR).
	  SoBaseColor baseColor = new SoBaseColor();
	  baseColor.rgb.setValue(1.f, 1.f, 1.f);
	  root.addChild(baseColor);

	  // SoDrawStyle is set to FILLED by default - resulting in filled triangles.
	  // We are going to set it to LINES to render it as wire frame.
	  SoDrawStyle drawStyle = new SoDrawStyle();
	  drawStyle.style.setValue(SoDrawStyle.Style.LINES);
	  drawStyle.lineWidth.setValue(2.f);
	  root.addChild(drawStyle);

	  // Coordinates of vertices
	  // They are taken from skyBoxVertices.
	  SoCoordinate3 coords = new SoCoordinate3();
	  coords.point.setValues(0, 8, skyBoxVerticesWireBoxIndexed);
	  root.addChild(coords);

	  // SoIndexedTriangleStripSet node is rendering triangles and triangle strips.
	  // Each value in coordIndex is used as the index to SoCoordinate3
	  // for particular vertex.
	  SoIndexedTriangleStripSet strip = new SoIndexedTriangleStripSet();
	  strip.coordIndex.setValues(0, 5*6, skyBoxIndices);
	  root.addChild(strip);

	  return root;
	}



	static SoSeparator createPlanetWireBoxIndexed(float radius, float distance,
	                          float initialAngle, SbColor color)
	{
	  SoSeparator root = new SoSeparator();

	  // material of planet
	  // ambient and diffuse color is set to the planet color,
	  // specular and emissive colors are left on their default value (0.f, 0.f, 0.f)
	  SoMaterial material = new SoMaterial();
	  material.ambientColor.setValue(color);
	  material.diffuseColor.setValue(color);
	  root.addChild(material);

	  // revolution around the Sun
	  SoRotationXYZ rotY = new SoRotationXYZ();
	  rotY.axis.setValue(SoRotationXYZ.Axis.Y);
	  root.addChild(rotY);

	  // connect engine to rotation node
	  SoElapsedTime counter = new SoElapsedTime();
	  counter.speed.setValue(20.f/distance);
	  rotY.angle.connectFrom(counter.timeOut);

	  // translation from the Sun
	  SoTranslation trans = new SoTranslation();
	  trans.translation.setValue(distance*(float)(+Math.cos(initialAngle)), 0.f,
	                              distance*(float)(-Math.sin(initialAngle)));
	  root.addChild(trans);

	  // planet geometry
	  SoSphere sphere = new SoSphere();
	  sphere.radius.setValue(radius);
	  root.addChild(sphere);

	  return root;
	}

	


	
	
	static SoNode createDemoSceneWireBoxIndexed() {
		
		  // create scene root
		  SoSeparator root = new SoSeparator();
		  root.ref();

		  // create skybox
		  root.addChild(createSkyBoxWireBoxIndexed());

		  // environment
		  // ambientColor is left on white (default value)
		  // while we set its intensity on 0.25.
		  SoEnvironment envir = new SoEnvironment();
		  envir.ambientIntensity.setValue(0.25f);
		  root.addChild(envir);


		  // model of the Sun

		  // ambient and diffuse color is set to dark yellow,
		  // emissive on light yellow and specular is left on its default value (black)
		  SoMaterial sunMat = new SoMaterial();
		  sunMat.ambientColor.setValue(0.5f, 0.5f, 0.15f);
		  sunMat.diffuseColor.setValue(0.5f, 0.5f, 0.15f);
		  sunMat.emissiveColor.setValue(0.9f, 0.9f, 0.3f);
		  root.addChild(sunMat);

		  // sphere of radius 10
		  SoSphere sun = new SoSphere();
		  sun.radius.setValue(10.f);
		  root.addChild(sun);

		  // light in the middle of the Sun
		  // all the objects in the scene graph following the light
		  // will be lit by the light
		  SoPointLight sunLight = new SoPointLight();
		  sunLight.location.setValue(0.f, 0.f, 0.f);
		  sunLight.intensity.setValue(0.75f);
		  root.addChild(sunLight);


		  // model of the Earth
		  root.addChild(createPlanetWireBoxIndexed(4.0f, 45.f, 0.f,
		                              new SbColor(0.7f, 0.7f, 1.0f)));

		  // model of the Mercury
		  root.addChild(createPlanetWireBoxIndexed(2.0f, 20.f, (float)(4*Math.PI/3),
		                              new SbColor(1.0f, 0.3f, 0.3f)));

		  // model of the Venus
		  root.addChild(createPlanetWireBoxIndexed(3.0f, 30.f, (float)(3*Math.PI/3),
		                              new SbColor(1.0f, 0.6f, 0.0f)));

		  return root;
	}

	// size of the skybox divided by two
	//#define SKY_BOX_SIZE2 50


	// skybox vertices
	static float skyBoxVertices1[][] =
	{
		{-SKY_BOX_SIZE2, -SKY_BOX_SIZE2, -SKY_BOX_SIZE2},
		{SKY_BOX_SIZE2, -SKY_BOX_SIZE2, -SKY_BOX_SIZE2},
		{-SKY_BOX_SIZE2,  SKY_BOX_SIZE2, -SKY_BOX_SIZE2},
		{SKY_BOX_SIZE2,  SKY_BOX_SIZE2, -SKY_BOX_SIZE2},
		{-SKY_BOX_SIZE2, -SKY_BOX_SIZE2,  SKY_BOX_SIZE2},
		{SKY_BOX_SIZE2, -SKY_BOX_SIZE2,  SKY_BOX_SIZE2},
		{-SKY_BOX_SIZE2,  SKY_BOX_SIZE2,  SKY_BOX_SIZE2},
		{SKY_BOX_SIZE2,  SKY_BOX_SIZE2,  SKY_BOX_SIZE2},
	};


	// skybox indices
	// indices are used to access skyBoxVertices when rendering,
	// -1 is used to terminate triangle strip
	static int skyBoxIndices1[][] =
	{
		{0, 1, 2, 3, -1},  // sky00
		{6, 4, 2, 0, -1},  // sky06
		{5, 4, 7, 6, -1},  // sky12
		{5, 7, 1, 3, -1},  // sky18
		{2, 3, 6, 7, -1},  // skyN0
		{4, 5, 0, 1, -1},  // skyS0
	};


	// indices for texturing coordinates
	static int skyBoxTexCoordIndex[] =
	{
		0, 1, 2, 3, -1,
	};


	static SoSeparator createSkyBox1()
	{
		int i;
		SoSeparator root = new SoSeparator();

		// Set light model to BASE_COLOR. It means,
		// we will render pre-lit scene.
		// All the geometry will receive the color by its diffuse color
		// while lights and other material components have no effect.
		SoLightModel lmodel = new SoLightModel();
		lmodel.model.setValue(SoLightModel.Model.BASE_COLOR);
		root.addChild(lmodel);

		// Coordinates of vertices
		// They are taken from skyBoxVertices.
		SoCoordinate3 coords = new SoCoordinate3();
		coords.point.setValues(0, 8, skyBoxVertices1);
		root.addChild(coords);

		// Texturing coordinates
		// (applied on all 6 sides of the box)
		SoTextureCoordinate2 texCoord = new SoTextureCoordinate2();
		texCoord.point.set1Value(0, new SbVec2f(0, 0));
		texCoord.point.set1Value(1, new SbVec2f(1, 0));
		texCoord.point.set1Value(2, new SbVec2f(0, 1));
		texCoord.point.set1Value(3, new SbVec2f(1, 1));
		root.addChild(texCoord);

		// Textures
		// There is one for each sky box side.
		// DECAL means to replace original object color by the texture (no lighting is applied).
		SoTexture2[] textures = new SoTexture2[6];
		for (i = 0; i<6; i++) {
			textures[i] = new SoTexture2();
			textures[i].model.setValue(SoTexture2.Model.DECAL);
		}
		textures[0].filename.setValue("G:/eclipseWorkspaces/2-7-SkyBox1/sky00.gif");
		textures[1].filename.setValue("G:/eclipseWorkspaces/2-7-SkyBox1/sky06.gif");
		textures[2].filename.setValue("G:/eclipseWorkspaces/2-7-SkyBox1/sky12.gif");
		textures[3].filename.setValue("G:/eclipseWorkspaces/2-7-SkyBox1/sky18.gif");
		textures[4].filename.setValue("G:/eclipseWorkspaces/2-7-SkyBox1/skyN0.gif");
		textures[5].filename.setValue("G:/eclipseWorkspaces/2-7-SkyBox1/skyS0.gif");

		for (i = 0; i<6; i++) {
			root.addChild(textures[i]);

			// SoIndexedTriangleStripSet node is rendering triangles and triangle strips.
			// Each value in coordIndex is used as the index to SoCoordinate3
			// for particular vertex.
			SoIndexedTriangleStripSet strip = new SoIndexedTriangleStripSet();
			strip.coordIndex.setValues(0, 5, skyBoxIndices1[i]);
			strip.textureCoordIndex.setValues(0, 5, skyBoxTexCoordIndex);
			root.addChild(strip);
		}

		return root;
	}



	SoSeparator createPlanet1(float radius, float distance,
		float initialAngle, SbColor color, String textureName)
	{
		SoSeparator root = new SoSeparator();

		// material of planet
		if (textureName != null) {
			SoMaterial material = new SoMaterial();
			material.ambientColor.setValue(new SbColor(1.f, 1.f, 1.f));
			material.diffuseColor.setValue(new SbColor(1.f, 1.f, 1.f));
			root.addChild(material);
		}
		else {
			SoMaterial material = new SoMaterial();
			material.ambientColor.setValue(color);
			material.diffuseColor.setValue(color);
			root.addChild(material);
		}

		// texture of planet
		if (textureName != null) {
			SoTexture2 texture = new SoTexture2();
			texture.filename.setValue(textureName);
			root.addChild(texture);
		}

		// revolution around the Sun
		SoRotationXYZ rotY = new SoRotationXYZ();
		rotY.axis.setValue(SoRotationXYZ.Axis.Y);
		root.addChild(rotY);

		// connect engine to rotation node
		SoElapsedTime counter = new SoElapsedTime();
		counter.speed.setValue(20.f / distance);
		rotY.angle.connectFrom(counter.timeOut);

		// translation from the Sun
		SoTranslation trans = new SoTranslation();
		trans.translation.setValue(distance*(float)(+Math.cos(initialAngle)), 0.f,
			distance*(float)(-Math.sin(initialAngle)));
		root.addChild(trans);

		// planet geometry
		SoSphere sphere = new SoSphere();
		sphere.radius.setValue(radius);
		root.addChild(sphere);

		return root;
	}

	SoNode createDemoSceneSkyBox1() {

		// create scene root
		SoSeparator root = new SoSeparator();
		root.ref();

		// create skybox
		root.addChild(createSkyBox1());

		// environment
		// ambientColor is left on white (default value)
		// while we set its intensity on 0.25.
		SoEnvironment envir = new SoEnvironment();
		envir.ambientIntensity.setValue(0.25f);
		root.addChild(envir);


		// model of the Sun

		// ambient and diffuse color is set to dark yellow,
		// emissive on light yellow and specular is left on its default value (black)
		SoMaterial sunMat = new SoMaterial();
		sunMat.ambientColor.setValue(0.5f, 0.5f, 0.15f);
		sunMat.diffuseColor.setValue(0.5f, 0.5f, 0.15f);
		sunMat.emissiveColor.setValue(0.9f, 0.9f, 0.3f);
		root.addChild(sunMat);

		// sphere of radius 10
		SoSphere sun = new SoSphere();
		sun.radius.setValue(10.f);
		root.addChild(sun);

		// light in the middle of the Sun
		// all the objects in the scene graph following the light
		// will be lit by the light
		SoPointLight sunLight = new SoPointLight();
		sunLight.location.setValue(0.f, 0.f, 0.f);
		sunLight.intensity.setValue(0.75f);
		root.addChild(sunLight);


		// model of the Earth
		root.addChild(createPlanet1(4.0f, 45.f, 0.f,
			new SbColor(0.7f, 0.7f, 1.0f), "G:/eclipseWorkspaces/2-7-SkyBox1/earth.jpg"));

		// model of the Mercury
		root.addChild(createPlanet1(2.0f, 20.f, (float)(4 * Math.PI / 3),
			new SbColor(1.0f, 0.3f, 0.3f), null));

		// model of the Venus
		root.addChild(createPlanet1(3.0f, 30.f, (float)(3 * Math.PI / 3),
			new SbColor(1.0f, 0.6f, 0.0f), "G:/eclipseWorkspaces/2-7-SkyBox1/venus.jpg"));

		return root;
	}

	
// skybox indices
// indices are used to access skyBoxVertices when rendering,
// -1 is used to terminate triangle strip
static int skyBoxIndices2[][] =
{
  {0, 1, 2, 3, -1},  // sky00
  {6, 4, 2, 0, -1},  // sky06
  {5, 4, 7, 6, -1},  // sky12
  {5, 7, 1, 3, -1},  // sky18
  {2, 3, 6, 7, -1},  // skyN0
  {4, 5, 0, 1, -1},  // skyS0
};


// skybox vertices
static float skyBoxVertices2[][] =
{
  {-SKY_BOX_SIZE2, -SKY_BOX_SIZE2, -SKY_BOX_SIZE2},
	  { SKY_BOX_SIZE2, -SKY_BOX_SIZE2, -SKY_BOX_SIZE2},
  {-SKY_BOX_SIZE2,  SKY_BOX_SIZE2, -SKY_BOX_SIZE2},
   {SKY_BOX_SIZE2,  SKY_BOX_SIZE2, -SKY_BOX_SIZE2},
  {-SKY_BOX_SIZE2, -SKY_BOX_SIZE2,  SKY_BOX_SIZE2},
	   { SKY_BOX_SIZE2, -SKY_BOX_SIZE2,  SKY_BOX_SIZE2},
  {-SKY_BOX_SIZE2,  SKY_BOX_SIZE2,  SKY_BOX_SIZE2},
   {SKY_BOX_SIZE2,  SKY_BOX_SIZE2,  SKY_BOX_SIZE2},
};


// indices for texturing coordinates
static int skyBoxTexCoordIndex2[] =
{
  0, 1, 2, 3, -1,
};

	
static SoSeparator createSkyBox2(SoCamera camera)
{
  int i;
  SoSeparator root = new SoSeparator();

  // Translation
  // We are moving sky box in a way that its centre
  // is always in the camera position
  SoTranslation trans = new SoTranslation();
  trans.translation.connectFrom(camera.position);
  root.addChild(trans);

  // Set light model to BASE_COLOR. It means,
  // we will render pre-lit scene.
  // All the geometry will receive the color by its diffuse color
  // while lights and other material components have no effect.
  SoLightModel lmodel = new SoLightModel();
  lmodel.model.setValue(SoLightModel.Model.BASE_COLOR);
  root.addChild(lmodel);

  // Coordinates of vertices
  // They are taken from skyBoxVertices.
  SoCoordinate3 coords = new SoCoordinate3();
  coords.point.setValues(0, 8, skyBoxVertices2);
  root.addChild(coords);

  // Texturing coordinates
  // (applied on all 6 sides of the box)
  SoTextureCoordinate2 texCoord = new SoTextureCoordinate2();
  texCoord.point.set1Value(0, new SbVec2f(0,0));
  texCoord.point.set1Value(1, new SbVec2f(1,0));
  texCoord.point.set1Value(2, new SbVec2f(0,1));
  texCoord.point.set1Value(3, new SbVec2f(1,1));
  root.addChild(texCoord);

  // Textures
  // There is one for each sky box side.
  // DECAL means to replace original object color by the texture (no lighting is applied).
  SoTexture2[] textures = new SoTexture2[6];
  for (i=0; i<6; i++) {
    textures[i] = new SoTexture2();
    textures[i].model.setValue(SoTexture2.Model.DECAL);
  }
  textures[0].filename.setValue("G:/eclipseWorkspaces/2-7-SkyBox1/sky00.gif");
  textures[1].filename.setValue("G:/eclipseWorkspaces/2-7-SkyBox1/sky06.gif");
  textures[2].filename.setValue("G:/eclipseWorkspaces/2-7-SkyBox1/sky12.gif");
  textures[3].filename.setValue("G:/eclipseWorkspaces/2-7-SkyBox1/sky18.gif");
  textures[4].filename.setValue("G:/eclipseWorkspaces/2-7-SkyBox1/skyN0.gif");
  textures[5].filename.setValue("G:/eclipseWorkspaces/2-7-SkyBox1/skyS0.gif");

  for (i=0; i<6; i++) {
    root.addChild(textures[i]);

    // SoIndexedTriangleStripSet node is rendering triangles and triangle strips.
    // Each value in coordIndex is used as the index to SoCoordinate3
    // for particular vertex.
    SoIndexedTriangleStripSet strip = new SoIndexedTriangleStripSet();
    strip.coordIndex.setValues(0, 5, skyBoxIndices2[i]);
    strip.textureCoordIndex.setValues(0, 5, skyBoxTexCoordIndex2);
    root.addChild(strip);
  }

  return root;
}

	
SoSeparator createPlanet2(float radius, float distance,
                          float initialAngle, SbColor color, String textureName)
{
  SoSeparator root = new SoSeparator();

  // material of planet
  if (textureName != null) {
    SoMaterial material = new SoMaterial();
    material.ambientColor.setValue(new SbColor(1.f, 1.f, 1.f));
    material.diffuseColor.setValue(new SbColor(1.f, 1.f, 1.f));
    root.addChild(material);
  } else {
    SoMaterial material = new SoMaterial();
    material.ambientColor.setValue(color);
    material.diffuseColor.setValue(color);
    root.addChild(material);
  }

  // texture of planet
  if (textureName != null) {
    SoTexture2 texture = new SoTexture2();
    texture.filename.setValue(textureName);
    root.addChild(texture);
  }

  // revolution around the Sun
  SoRotationXYZ rotY = new SoRotationXYZ();
  rotY.axis.setValue( SoRotationXYZ.Axis.Y);
  root.addChild(rotY);

  // connect engine to rotation node
  SoElapsedTime counter = new SoElapsedTime();
  counter.speed.setValue(20.f/distance);
  rotY.angle.connectFrom(counter.timeOut);

  // translation from the Sun
  SoTranslation trans = new SoTranslation();
  trans.translation.setValue(distance*(float)(Math.cos(initialAngle)), 0.f,
                              distance*(float)(-Math.sin(initialAngle)));
  root.addChild(trans);

  // planet geometry
  SoSphere sphere = new SoSphere();
  sphere.radius.setValue(radius);
  root.addChild(sphere);

  return root;
}

	
	
	SoNode createDemoSceneSkyBox2() {
	  // create scene root
  SoSeparator root = new SoSeparator();
  root.ref();

  // Camera
  // Although camera can be created automatically by the viewer,
  // we need our own camera because of moving skybox.
  SoPerspectiveCamera camera = new SoPerspectiveCamera();
  root.addChild(camera);

  // create skybox
  root.addChild(createSkyBox2(camera));

  // environment
  // ambientColor is left on white (default value)
  // while we set its intensity on 0.25.
  SoEnvironment envir = new SoEnvironment();
  envir.ambientIntensity.setValue(0.25f);
  root.addChild(envir);


  // model of the Sun

  // ambient and diffuse color is set to dark yellow,
  // emissive on light yellow and specular is left on its default value (black)
  SoMaterial sunMat = new SoMaterial();
  sunMat.ambientColor.setValue(0.5f, 0.5f, 0.15f);
  sunMat.diffuseColor.setValue(0.5f, 0.5f, 0.15f);
  sunMat.emissiveColor.setValue(0.9f, 0.9f, 0.3f);
  root.addChild(sunMat);

  // sphere of radius 10
  SoSphere sun = new SoSphere();
  sun.radius.setValue(10.f);
  root.addChild(sun);

  // light in the middle of the Sun
  // all the objects in the scene graph following the light
  // will be lit by the light
  SoPointLight sunLight = new SoPointLight();
  sunLight.location.setValue(0.f, 0.f, 0.f);
  sunLight.intensity.setValue(0.75f);
  root.addChild(sunLight);


  // model of the Earth
  root.addChild(createPlanet2(4.0f, 45.f, 0.f,
                              new SbColor(0.7f, 0.7f, 1.0f), "G:/eclipseWorkspaces/2-7-SkyBox1/earth.jpg"));

  // model of the Mercury
  root.addChild(createPlanet2(2.0f, 20.f, (float)(4*Math.PI/3),
                              new SbColor(1.0f, 0.3f, 0.3f), null));

  // model of the Venus
  root.addChild(createPlanet2(3.0f, 30.f, (float)(3*Math.PI/3),
                              new SbColor(1.0f, 0.6f, 0.0f), "G:/eclipseWorkspaces/2-7-SkyBox1/venus.jpg"));

	return root;	
	}
	
	SoNode createDemoScenePerformance() {

	//String fileName = "C:/eclipseWorkspaces/2-6-Performance/AztecCityI.iv"; // default model
		//String fileName = "G:/eclipseWorkspaces/5-1-Tanky1/models/tank.wrl";
		//String fileName = "C:/eclipseWorkspaces/examples_iv/transparentbluecylinder.iv"; // default model
		//String fileName = "G:/eclipseWorkspaces/examples_iv/sphere.iv"; // default model
		//String fileName = "C:/eclipseWorkspaces/examples_iv/text3.iv"; // default model
		//String fileName = "C:/eclipseWorkspaces/examples_iv/texSphereTransf.iv"; // default model
		//String fileName = "C:/eclipseWorkspaces/examples_iv/text3Rusty.iv"; // default model
		//String fileName = "C:/eclipseWorkspaces/examples_iv/bricks.iv"; // default model
		//String fileName = "C:/eclipseWorkspaces/examples_iv/simple2d.iv"; // default model
		//String fileName = "C:/eclipseWorkspaces/examples_iv/simple3D.iv"; // default model
		//String fileName = "C:/eclipseWorkspaces/examples_iv/pc.iv"; // default model
		//String fileName = "C:/eclipseWorkspaces/examples_iv/01.1.Windmill.iv"; // default model
		//String fileName = "C:/eclipseWorkspaces/inventor-2.1.5-10.src/inventor/data/models/chair.iv"; // default model
	//String fileName = "C:/eclipseWorkspaces/inventor-2.1.5-10.src/inventor/data/models/buildings/Barcelona.iv";
	//String fileName = "C:/eclipseWorkspaces/inventor-2.1.5-10.src/inventor/data/models/vehicles/spacestation.iv";
	//String fileName = "C:/eclipseWorkspaces/inventor-2.1.5-10.src/inventor/data/models/sgi/logo.iv";
	//String fileName = "C:/eclipseWorkspaces/inventor-2.1.5-10.src/inventor/data/models/CyberHeads/josie.iv";
	//String fileName = "C:/eclipseWorkspaces/inventor-2.1.5-10.src/inventor/data/models/food/apple.iv";
	//String fileName = "C:/eclipseWorkspaces/inventor-2.1.5-10.src/inventor/data/models/scenes/chesschairs.iv";
		//String fileName = "F:/test_oiv/Renderismissingtriangles.iv"; // default model
		//String fileName = "F:/test_oiv/test.iv"; // default model
		//String fileName = "F:/test_oiv/Issue177Renderismissingtrianglesround.iv"; // default model
		//String fileName = "F:/basic_examples/Road/road.iv";
		String fileName = "F:/basic_examples/Carport/carport.iv";
	boolean baseColor = false;

	// create scene root
	SoSeparator root = new SoSeparator();
	root.ref();

	// base color
	if (baseColor) {
		SoLightModel lm = new SoLightModel();
		lm.model.setValue(SoLightModel.Model.BASE_COLOR);
		lm.setOverride(true);
		root.addChild(lm);
	}

	// load scene from file
	SoFile file = new SoFile();
	file.name.setValue(fileName);
	root.addChild(file);

	return root;
}
	
	SoNode createDemoSceneSoMaterialShapeBinding() {
		
		float[][] rgb_ambient = {
				{ 0.00714286f,0.00169011f,0 },
				{ 0.00746438f,0.00673081f,0.00690282f },
				{ 0.00746438f,0.00673081f,0.00690282f }
		};
		
		float[][] rgb_diffuse = {
				{ 0.314286f,0.0743647f,0 },
				{ 0.0291577f,0.0262922f,0.0269642f },
				{ 0.0291577f,0.0262922f,0.0269642f }
		};
		
		float[][] rgb_specular = {
				{ 1,0.766841f,0 },
				{ 0.641609f,0.976208f,0.979592f },
				{ 0.938776f,0.0550317f,0.0550317f }
		};
		
		float[] shininess = {0.048f,0.062f,0.062f};
		
		SoTranslation trans = new SoTranslation();
		SoSeparator root = new SoSeparator();
		SoMaterial coul = new SoMaterial();
		SoMaterial coul_sphere = new SoMaterial();
		SoMaterialBinding attach =new SoMaterialBinding();
		root.ref();
		
		coul.ambientColor.setValues(0, rgb_ambient);
		coul.diffuseColor.setValues(0, rgb_diffuse);
		coul.specularColor.setValues(0, rgb_specular);
		coul.shininess.setValues(0,shininess);
		
		coul_sphere.ambientColor.setValue( rgb_ambient[1]);
		coul_sphere.diffuseColor.setValue( rgb_diffuse[1]);
		coul_sphere.specularColor.setValue( rgb_specular[1]);
		coul_sphere.shininess.setValue(shininess[1]);
		
		attach.value.setValue(SoMaterialBinding.Binding.PER_PART_INDEXED);
		
		trans.translation.setValue(0, 1, 5);
		
		root.addChild(attach);
		root.addChild(coul);
		root.addChild(new SoCylinder());
		root.addChild(trans);
		root.addChild(coul_sphere);
		root.addChild(new SoSphere());
		
		return root;
	}

	SoNode createDemoSceneSoMaterial() {
		SoSeparator root = new SoSeparator();				
		SoMaterial coul = new SoMaterial();
		root.ref();
		
		coul.ambientColor.setValue(0.01f,0.02f,0.02f);
		coul.diffuseColor.setValue(0.0291577f,0.03f,0.03f);
		coul.specularColor.setValue(0.641609f,0.976208f,0.979592f);
		coul.shininess.setValue(0.061f);
		
		root.addChild(coul);
		SoSphere sphere = new SoSphere();
		sphere.subdivision.setValue(40);
		root.addChild(sphere);
		
		return root;
	}
	
	SoNode createDemoSceneTimeWatch() {
		SoSeparator root = new SoSeparator();
		root.ref();

		SoPerspectiveCamera myCamera = new SoPerspectiveCamera();
		root.addChild(myCamera);
		root.addChild(new SoDirectionalLight());
		SoMaterial myMaterial = new SoMaterial();
		myMaterial.diffuseColor.setValue(1.0f, 0.0f, 0.0f);
		root.addChild(myMaterial);

		SoText3 myText = new SoText3();
		root.addChild(myText);
		myText.string.connectFrom(SoDB.getGlobalField("realTime"));

		return root;
	}

	SoNode createDemoSceneTransparentCubes() {
		SoSeparator root = new SoSeparator();
		root.ref();

		SoSeparator root2 = new SoShadowGroup();
		root.addChild(root2);
		
		SoGroup root3 = new SoGroup();
		root2.addChild(root3);
		
		SoMaterial mat = new SoMaterial();
		mat.diffuseColor.setValue(.5f, .6f, 0.7f);
		mat.transparency.setValue(0.5f);
		
		root3.addChild(mat);
		
		SoCube bigCube;
		
//		bigCube = new SoCube();
//		bigCube.depth.setValue(5.0f);
//		bigCube.height.setValue(5.0f);
//		bigCube.width.setValue(5.0f);
//				
//		root3.addChild(bigCube);
		
		SoIndexedFaceSet faceSet = new SoIndexedFaceSet();
		
		SoVertexProperty vp = new SoVertexProperty();
		
		int nbRepeat = 10000;
		
		float[] xyz = new float[9*nbRepeat];
		for(int i=0;i<nbRepeat;i++) {
		xyz[3+9*i] = 1.0f;
		xyz[4+9*i] = 1.0f;
		xyz[6+9*i] = -1.0f;
		xyz[7+9*i] = 1.0f;
		}
		vp.vertex.setValues(0,xyz);
		
		faceSet.vertexProperty.setValue(vp);
		
		int[] indices = new int[4*nbRepeat];
		for(int i=0;i<nbRepeat;i++) {
		indices[0+4*i] = 0+3*i;
		indices[1+4*i] = 1+3*i;
		indices[2+4*i] = 2+3*i;
		indices[3+4*i] = -1;
		}		
		faceSet.coordIndex.setValues(0, indices);
		
		SoSeparator root4 = new SoSeparator();
		
		SoTranslation transl = new SoTranslation();
		
		transl.translation.setValue(1, 2, 3);
		root4.addChild(transl);
		root4.addChild(faceSet);
		root3.addChild(root4);
		
		bigCube = new SoCube();
		bigCube.depth.setValue(10.0f);
		bigCube.height.setValue(10.0f);
		bigCube.width.setValue(10.0f);
				
		root4 = new SoSeparator();
		root4.addChild(bigCube);
		root3.addChild(root4);
		
		return root;
	}
	
	
	public static void main(String[] args) {

		Display display = new Display ();
		Shell shell = new Shell(display);
		
		FillLayout fillLayout = new FillLayout();
		fillLayout.type = SWT.VERTICAL;
		shell.setLayout(fillLayout);
		
    	GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_DEBUG_CONTEXT, GLFW.GLFW_TRUE);
    	
		SoQt.init("demo");
		//SoDB.setDelaySensorTimeout(new SbTime(1.0/120.0));
		//SoDB.setRealTimeInterval(new SbTime(1.0/120.0));
		
		int style = SWT.NO_BACKGROUND;
		
		SoQtExaminerViewer viewer = new SoQtExaminerViewer(BuildFlag.BUILD_ALL,Type.BROWSER,shell,style);
	    //viewer.setColorBitDepth (10);
		//viewer.setAntialiasing(true,4);
		
		viewer.setHeadlight(false);
		
	    viewer.buildWidget(style);
	    
	    viewer.setSceneGraph(/*createDemoScene()*//*Orbits*//*Shadows.main()*/ShadowTest.create());
	    
	    shell.pack();
		shell.setSize(700, 700);
	    Monitor primary = display.getPrimaryMonitor();
	    Rectangle bounds = primary.getBounds();
	    Rectangle rect = shell.getBounds();
	    
	    int x = bounds.x + (bounds.width - rect.width) / 2;
	    int y = bounds.y + (bounds.height - rect.height) / 2;
	    
	    shell.setLocation(x, y);		shell.open ();
		shell.setLocation(x, y);
		
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
}
}