/**
 * 
 */
package application;

import java.awt.image.Raster;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import com.jogamp.opengl.GL2;

//import org.eclipse.swt.SWT;
//import org.eclipse.swt.graphics.Color;
//import org.eclipse.swt.graphics.Cursor;
//import org.eclipse.swt.graphics.ImageData;
//import org.eclipse.swt.graphics.PaletteData;
//import org.eclipse.swt.graphics.RGB;
//import org.eclipse.swt.layout.FillLayout;
//import org.eclipse.swt.widgets.Display;
//import org.eclipse.swt.widgets.Shell;

import application.scenegraph.SceneGraph;
import application.scenegraph.SceneGraphIndexedFaceSet;
import application.scenegraph.SceneGraphIndexedFaceSetShader;
import application.scenegraph.ShadowTestSceneGraph;
import application.scenegraph.Soleil;
import application.viewer.glfw.SoQtWalkViewer;
import jscenegraph.database.inventor.SbColor;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.actions.SoGLRenderAction.TransparencyType;
import jscenegraph.database.inventor.nodes.SoCamera;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jsceneviewerglfw.inventor.qt.SoQt;
import jsceneviewerglfw.inventor.qt.SoQtCameraController;
import jsceneviewerglfw.inventor.qt.viewers.SoQtFullViewer;
import jsceneviewerglfw.Cursor;
import jsceneviewerglfw.Display;
import jsceneviewerglfw.GLData;
import jsceneviewerglfw.SWT;
import loader.TerrainLoader;

/**
 * @author Yves Boyadjian
 *
 */
public class MainGLFW {
	
	public static final float MINIMUM_VIEW_DISTANCE = 2.5f;//1.0f;

	public static final float MAXIMUM_VIEW_DISTANCE = SceneGraphIndexedFaceSetShader.WATER_HORIZON;//5e4f;//SceneGraphIndexedFaceSet.SUN_FAKE_DISTANCE * 1.5f;
	
	public static final float Z_TRANSLATION = 2000;
	
	public static final String HERO_X = "hero_x";
	
	public static final String HERO_Y = "hero_y";
	
	public static final String HERO_Z = "hero_z";
	
	public static SbVec3f SCENE_POSITION;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = new Display();
		//Shell shell = new Shell(display);
		//shell.setLayout(new FillLayout());
		
		TerrainLoader l = new TerrainLoader();
		Raster rw = l.load("ned19_n47x00_w122x00_wa_mounttrainier_2008/ned19_n47x00_w122x00_wa_mounttrainier_2008.tif");
		Raster re = l.load("ned19_n47x00_w121x75_wa_mounttrainier_2008/ned19_n47x00_w121x75_wa_mounttrainier_2008.tif");
		
		SoQt.init("demo");
		
		int style = 0;//SWT.NO_BACKGROUND;
		
		//SoSeparator.setNumRenderCaches(0);
		//SceneGraph sg = new SceneGraphQuadMesh(r);
		
		int overlap = 13;		
		//SceneGraph sg = new SceneGraphIndexedFaceSet(rw,re,overlap,Z_TRANSLATION);
		SceneGraph sg = new SceneGraphIndexedFaceSetShader(rw,re,overlap,Z_TRANSLATION);
		//SceneGraph sg = new ShadowTestSceneGraph();
		
		SoQtWalkViewer viewer = new SoQtWalkViewer(SoQtFullViewer.BuildFlag.BUILD_NONE,SoQtCameraController.Type.BROWSER,/*shell*/null,style) {
			
			public void onClose() {
				File saveGameFile = new File("savegame.mri");
				
				Properties saveGameProperties = new Properties();
				
				
				try {
					OutputStream out = new FileOutputStream(saveGameFile);
					
					SoCamera camera = getCameraController().getCamera();
					
					saveGameProperties.setProperty(HERO_X, String.valueOf(camera.position.getValue().getX()));
					
					saveGameProperties.setProperty(HERO_Y, String.valueOf(camera.position.getValue().getY()));
					
					saveGameProperties.setProperty(HERO_Z, String.valueOf(camera.position.getValue().getZ() + Z_TRANSLATION));
					
					saveGameProperties.store(out, "Mount Rainier Island save game");
				
					out.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
	    GLData glf = new GLData(/*GLProfile.getDefault()*/);
	    glf.depthSize = 24;
	    glf.doubleBuffer = true;
	    glf.majorVersion = 3;
	    glf.minorVersion = 3;
	    glf.api = GLData.API.GL;
	    glf.profile = GLData.Profile.COMPATIBILITY;
	    glf.debug = true;
	    viewer.setFormat(glf, style);

		viewer.buildWidget(style);
		viewer.setHeadlight(false);
		
		
		viewer.setSceneGraph(sg.getSceneGraph());
		
		viewer.setHeightProvider(sg);
		
		SCENE_POSITION = new SbVec3f(/*sg.getCenterX()/2*/0,sg.getCenterY(),Z_TRANSLATION);

		viewer.setUpDirection(new SbVec3f(0,0,1));

		viewer.getCameraController().setAutoClipping(false);
		
		SoCamera camera = viewer.getCameraController().getCamera();
		
		sg.setCamera(camera);
		
		camera.nearDistance.setValue(MINIMUM_VIEW_DISTANCE);
		camera.farDistance.setValue(MAXIMUM_VIEW_DISTANCE);
		
		camera.position.setValue(0,0,0);
		camera.orientation.setValue(new SbVec3f(0,1,0), -(float)Math.PI/2.0f);
		
		File saveGameFile = new File("savegame.mri");
		if( saveGameFile.exists() ) {
			try {
				InputStream in = new FileInputStream(saveGameFile);
				
				Properties saveGameProperties = new Properties();
				
				saveGameProperties.load(in);
				
				float x = Float.valueOf(saveGameProperties.getProperty(HERO_X, "250"));
				
				float y = Float.valueOf(saveGameProperties.getProperty(HERO_Y, "305"));
				
				float z = Float.valueOf(saveGameProperties.getProperty(HERO_Z, String.valueOf(1279 - SCENE_POSITION.getZ())));
				
				camera.position.setValue(x,y,z);
				
				in.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
		}
		else {		
			camera.position.setValue(250, 305, 1279 - SCENE_POSITION.getZ());
		}
		viewer.getCameraController().changeCameraValues(camera);
		
		viewer.getSceneHandler().setBackgroundColor(new SbColor(0,0,1));

		viewer.getSceneHandler().setTransparencyType(TransparencyType.BLEND/*SORTED_LAYERS_BLEND*/);
		
		sg.setPosition(SCENE_POSITION.getX(),SCENE_POSITION.getY()/*,SCENE_POSITION.getZ()*/);
		
		final double startDate = (double)System.nanoTime() /1e9 - 60*60*4.5 / TimeConstants./*JMEMBA_TIME_ACCELERATION*/GTA_SA_TIME_ACCELERATION;
		
		viewer.addIdleListener((viewer1)->{
			double nanoTime = System.nanoTime();
			double nowSec = nanoTime / 1e9 - startDate;
			double nowHour = nowSec / 60 / 60;
			double nowDay = 100;//nowHour / 24; // always summer
			double nowGame = nowHour * TimeConstants./*JMEMBA_TIME_ACCELERATION*/GTA_SA_TIME_ACCELERATION;
			double Phi = 47;
			SbVec3f sunPosition = Soleil.soleil_xyz((float)nowDay, (float)nowGame, (float)Phi);
			sg.setSunPosition(new SbVec3f(-sunPosition.y(),-sunPosition.x(),sunPosition.z()));
		});		
		viewer.addIdleListener((viewer1)->{
			sg.idle();
		});
		
		//shell.open();
		
	    // create a cursor with a transparent image
//	    Color white = display.getSystemColor(SWT.COLOR_WHITE);
//	    Color black = display.getSystemColor(SWT.COLOR_BLACK);
//	    PaletteData palette = new PaletteData(new RGB[] { white.getRGB(), black.getRGB() });
//	    ImageData sourceData = new ImageData(16, 16, 1, palette);
//	    sourceData.transparentPixel = 0;
	    Cursor cursor = new Cursor();//display, /*sourceData*/null, 0, 0);
	    
//	    shell.getDisplay().asyncExec(new Runnable() {
//	        public void run() {
//	    		shell.setFullScreen(true);
//	            shell.forceActive();
//	        }
//	    });
//	    shell.forceActive();
//	    shell.forceFocus();
		
	    viewer.setCursor(cursor);
	    boolean success = viewer.setFocus();
	    
	    viewer.start();
		viewer.updateLocation(new SbVec3f(0.0f, 0.0f, 0.0f));
		
	    
	    //viewer.getGLWidget().maximize();
	    
		// run the event loop as long as the window is open
//		while (!shell.isDisposed()) {
//		    // read the next OS event queue and transfer it to a SWT event
//		    if (!display.readAndDispatch())
//		     {
//		    // if there are currently no other OS event to process
//		    // sleep until the next OS event is available
//			  //viewer.getSceneHandler().getSceneGraph().touch();
//		        display.sleep();
//		    	//viewer.idle();
//		     }
//		}
	    System.gc();
	    System.runFinalization();
	    
	    GL2 gl2 = new GL2() {};
	    
	    int[] depthBits = new int[1];
	    gl2.glGetIntegerv(GL2.GL_DEPTH_BITS, depthBits);
	    
	    System.out.println("Depth Buffer : "+depthBits[0]);
	    
	    display.loop();
	    
	    sg.preDestroy();
	    
	    viewer.destructor();

		// disposes all associated windows and their components
		display.dispose();		
	}

}
