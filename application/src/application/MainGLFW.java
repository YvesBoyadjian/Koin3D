/**
 * 
 */
package application;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.Raster;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.util.Properties;
import java.util.function.Consumer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

import application.gui.OptionDialog;
import application.scenario.FirstApproachQuest;
import application.scenario.Scenario;
import application.scenario.TargetsKillingQuest;
import application.viewer.glfw.ForceProvider;
import application.viewer.glfw.PositionProvider;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;
import com.badlogic.gdx.physics.bullet.linearmath.btTransform;
import com.badlogic.gdx.physics.bullet.linearmath.btVector3;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.badlogic.gdx.utils.SharedLibraryLoader;
import com.jogamp.opengl.GL2;

import application.physics.OpenGLMotionState;

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
import jscenegraph.database.inventor.*;
import jscenegraph.database.inventor.actions.SoGLRenderAction.TransparencyType;
import jscenegraph.database.inventor.events.SoMouseButtonEvent;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoRayPickAction;
import jscenegraph.database.inventor.nodes.SoCamera;
import jscenegraph.database.inventor.nodes.SoCube;
import jscenegraph.database.inventor.nodes.SoGroup;
import jscenegraph.database.inventor.nodes.SoMaterial;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.port.KDebug;
import jsceneviewerglfw.inventor.qt.SoQt;
import jsceneviewerglfw.inventor.qt.SoQtCameraController;
import jsceneviewerglfw.inventor.qt.viewers.SoQtFullViewer;
import jsceneviewerglfw.Cursor;
import jsceneviewerglfw.Display;
import jsceneviewerglfw.GLData;
import jsceneviewerglfw.SWT;
import loader.TerrainLoader;
import org.ode4j.math.DQuaternion;
import org.ode4j.math.DVector3;
import org.ode4j.math.DVector3C;
import org.ode4j.ode.*;
import org.ode4j.ode.internal.ErrorHandler;
import org.ode4j.ode.internal.ErrorHdl;
import org.ode4j.ode.internal.Rotation;

import static com.badlogic.gdx.physics.bullet.collision.CollisionConstants.DISABLE_DEACTIVATION;

/**
 * @author Yves Boyadjian
 *
 */
public class MainGLFW {

	public static final float MINIMUM_VIEW_DISTANCE = 2.5f;//1.0f;

	public static final float MAXIMUM_VIEW_DISTANCE = SceneGraphIndexedFaceSetShader.WATER_HORIZON;//5e4f;//SceneGraphIndexedFaceSet.SUN_FAKE_DISTANCE * 1.5f;

	public static final float Z_TRANSLATION = 2000;

	public static final double REAL_START_TIME_SEC = 60 * 60 * 4.5; // 4h30 in the morning

	public static final double COMPUTER_START_TIME_SEC = REAL_START_TIME_SEC / TimeConstants./*JMEMBA_TIME_ACCELERATION*/GTA_SA_TIME_ACCELERATION;

	public static final String HERO_X = "hero_x";

	public static final String HERO_Y = "hero_y";

	public static final String HERO_Z = "hero_z";

	public static final String TIME = "time_sec";

	public static final String TIME_STOP = "time_stop";

	public static final String FLY = "fly";

	public static final String SHADOW_PRECISION = "shadow_precision";

	public static final String LOD_FACTOR = "lod_factor";

	public static final String LOD_FACTOR_SHADOW = "lod_factor_shadow";

	public static final String TREE_DISTANCE = "tree_distance";

	public static final String TREE_SHADOW_DISTANCE = "tree_shadow_distance";

	public static final String MAX_I = "max_i";

	public static final String VOLUMETRIC_SKY = "volumetric_sky";

	public static final String DISPLAY_FPS = "display_fps";

	public static SbVec3f SCENE_POSITION;

	public static final SbColor SKY_BLUE = new SbColor(0.53f, 0.81f, 0.92f);

	public static final SbColor DEEP_SKY_BLUE = new SbColor(0.0f, 0.749f, 1.0f);

	public static final SbColor VERY_DEEP_SKY_BLUE = new SbColor(0.0f, 0.749f / 3.0f, 1.0f);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			showSplash();
		});
	}

	//System.loadLibrary("opengl32"); //for software rendering using mesa3d for Windows

	//System.setProperty("IV_DEBUG_CACHES", "1");

//		ImageIcon ii = new ImageIcon();
//		
//		try {
//			ii = new ImageIcon(new URL("https://github.com/YvesBoyadjian/Koin3D/blob/master/MountRainierIslandScreenShot.jpg"));
//		} catch (MalformedURLException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
//		Image splashScreen = ii.getImage();

	static JWindow window;

	public static void showSplash() {
		window = new JWindow();/* {
			public void paint(Graphics g) {
			      super.paint(g);
			      g.drawImage(splashScreen, 0, 0, this);
			   }			
		};*/
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();

		JLabel intro = new JLabel("Mount Rainier Island, an Adventure Game", null, SwingConstants.CENTER);
		intro.setForeground(Color.red.darker());
		intro.setFont(intro.getFont().deriveFont((float) height / 20f));
		window.getContentPane().add(
				intro);
		window.getContentPane().setBackground(Color.BLACK);
		window.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
		//window.getContentPane().setForeground(Color.white);

		window.setBounds(0, 0, (int) width, (int) height);

		window.setVisible(true);

		SwingUtilities.invokeLater(() -> {
			try {
				mainGame();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(window,e.toString(),"Exception in Mount Rainier Island",JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				System.exit(-1); // Necessary, because of Linux
			}
			catch (Error e) {
				JOptionPane.showMessageDialog(window,e.toString(),"Error in Mount Rainier Island",JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				System.exit(-1); // Necessary, because of Linux
			}
		});
	}

	static Display display;

	static SceneGraphIndexedFaceSetShader sg;

	static SoQtWalkViewer viewer;


	static boolean physics_error = false;

	public static void mainGame() {
		display = new Display();
		//Shell shell = new Shell(display);
		//shell.setLayout(new FillLayout());

		TerrainLoader l = new TerrainLoader();
		Raster rw = l.load("ned19_n47x00_w122x00_wa_mounttrainier_2008/ned19_n47x00_w122x00_wa_mounttrainier_2008.tif");
		Raster re = l.load("ned19_n47x00_w121x75_wa_mounttrainier_2008/ned19_n47x00_w121x75_wa_mounttrainier_2008.tif");

		SoQt.init("demo");

		SoDB.setDelaySensorTimeout(SbTime.zero()); // Necessary to avoid bug in Display

		int style = 0;//SWT.NO_BACKGROUND;

		//SoSeparator.setNumRenderCaches(0);
		//SceneGraph sg = new SceneGraphQuadMesh(r);

		float shadow_precision = (float)OptionDialog.DEFAULT_SHADOW_PRECISION;
		float level_of_detail = (float)OptionDialog.DEFAULT_LOD_FACTOR;
		float level_of_detail_shadow = (float) OptionDialog.DEFAULT_LOD_FACTOR_SHADOW;
		float tree_distance = (float)OptionDialog.DEFAULT_TREE_DISTANCE;
		float tree_shadow_distance = (float)OptionDialog.DEFAULT_TREE_SHADOW_DISTANCE;
		int max_i = OptionDialog.DEFAULT_ISLAND_DEPTH;
		boolean volumetric_sky = OptionDialog.DEFAULT_VOLUMETRIC_SKY;
		boolean display_fps = false;

		File graphicsFile = new File("graphics.mri");
		if (graphicsFile.exists()) {
			try {
				InputStream in = new FileInputStream(graphicsFile);

				Properties graphicsProperties = new Properties();

				graphicsProperties.load(in);

				shadow_precision = Float.valueOf(graphicsProperties.getProperty(SHADOW_PRECISION,"0.4"));

				level_of_detail = Float.valueOf(graphicsProperties.getProperty(LOD_FACTOR,"1"));

				level_of_detail_shadow = Float.valueOf(graphicsProperties.getProperty(LOD_FACTOR_SHADOW,"1"));

				tree_distance = Float.valueOf(graphicsProperties.getProperty(TREE_DISTANCE,"7000"));

				tree_shadow_distance = Float.valueOf(graphicsProperties.getProperty(TREE_SHADOW_DISTANCE,"3000"));

				max_i = Integer.valueOf(graphicsProperties.getProperty(MAX_I,"14000"));

				volumetric_sky = "true".equals(graphicsProperties.getProperty(VOLUMETRIC_SKY,"true"));

				display_fps = "true".equals(graphicsProperties.getProperty(DISPLAY_FPS,"false"));

				in.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}


		int overlap = 13;
		//SceneGraph sg = new SceneGraphIndexedFaceSet(rw,re,overlap,Z_TRANSLATION);
		sg = new SceneGraphIndexedFaceSetShader(rw, re, overlap, Z_TRANSLATION, max_i);
		//SceneGraph sg = new ShadowTestSceneGraph();
		rw = null; // for garbage collection
		re = null; // for garbage collection


		sg.getShadowGroup().precision.setValue(shadow_precision);

		sg.setLevelOfDetail(level_of_detail);

		sg.setLevelOfDetailShadow(level_of_detail_shadow);

		sg.setTreeDistance(tree_distance);

		sg.setTreeShadowDistance(tree_shadow_distance);

		sg.getShadowGroup().isVolumetricActive.setValue(volumetric_sky);
		sg.getEnvironment().fogColor.setValue(volumetric_sky ? sg.SKY_COLOR.darker().darker().darker().darker().darker().darker() : sg.SKY_COLOR.darker());

		sg.enableFPS(display_fps);

		viewer = new SoQtWalkViewer(SoQtFullViewer.BuildFlag.BUILD_NONE, SoQtCameraController.Type.BROWSER,/*shell*/null, style) {

			public void onClose(boolean resetToDefault) {
				File saveGameFile = new File("savegame.mri");

				Properties saveGameProperties = new Properties();


				try {
					OutputStream out = new FileOutputStream(saveGameFile);

					SoCamera camera = getCameraController().getCamera();

					if(!resetToDefault) {
						saveGameProperties.setProperty(HERO_X, String.valueOf(camera.position.getValue().getX()));

						saveGameProperties.setProperty(HERO_Y, String.valueOf(camera.position.getValue().getY()));

						saveGameProperties.setProperty(HERO_Z, String.valueOf(camera.position.getValue().getZ() + Z_TRANSLATION));

						saveGameProperties.setProperty(TIME, String.valueOf(getNow()));

						saveGameProperties.setProperty(TIME_STOP, isTimeStop() ? "true" : "false");

						saveGameProperties.setProperty(FLY, isFlying() ? "true" : "false");
					}
					saveGameProperties.store(out, "Mount Rainier Island save game");

					out.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				File graphicsFile = new File("graphics.mri");

				Properties graphicsProperties = new Properties();

				try {
					OutputStream out = new FileOutputStream(graphicsFile);

					graphicsProperties.setProperty(SHADOW_PRECISION, String.valueOf(sg.getShadowGroup().precision.getValue()));

					graphicsProperties.setProperty(LOD_FACTOR,String.valueOf(sg.getLevelOfDetail()));

					graphicsProperties.setProperty(LOD_FACTOR_SHADOW,String.valueOf(sg.getLevelOfDetailShadow()));

					graphicsProperties.setProperty(TREE_DISTANCE,String.valueOf(sg.getTreeDistance()));

					graphicsProperties.setProperty(TREE_SHADOW_DISTANCE,String.valueOf(sg.getTreeShadowDistance()));

					graphicsProperties.setProperty(MAX_I,String.valueOf(sg.getMaxI()));

					graphicsProperties.setProperty(VOLUMETRIC_SKY,sg.getShadowGroup().isVolumetricActive.getValue() ? "true":"false" );

					graphicsProperties.setProperty(DISPLAY_FPS,sg.isFPSEnabled() ? "true":"false" );

					graphicsProperties.store(out,"Mount Rainier Island graphics");

					out.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

			byte[] gunSound = loadSound("GUN_FIRE-GoodSoundForYou-820112263_10db.wav");

			protected void onFire(SoMouseButtonEvent event) {
				//playSound("shortened_40_smith_wesson_single-mike-koenig.wav"/*clipf*/);
				if (gunSound != null) {
					playSound(/*"GUN_FIRE-GoodSoundForYou-820112263_10db.wav"*//*clipf*/gunSound);
				}

				SbViewportRegion vr = this.getSceneHandler().getViewportRegion();
				SoNode sg_ = this.getSceneHandler().getSceneGraph();

//				TargetSearchRunnable tsr = new TargetSearchRunnable(this, vr, sg);
//				tsr.run();
				SwingUtilities.invokeLater(new TargetSearchRunnable(this, vr, sg_, sg));
				//new Thread(new TargetSearchRunnable(this, vr, sg)).start();
			}
		};
		GLData glf = new GLData(/*GLProfile.getDefault()*/);
		glf.redSize = 8;//10;
		glf.greenSize = 8;//10;
		glf.blueSize = 8;//10;
		glf.alphaSize = 0;
		glf.depthSize = 32;
		glf.doubleBuffer = true;
		glf.majorVersion = 2;//3;
		glf.minorVersion = 1;
		glf.api = GLData.API.GL;
		//glf.profile = GLData.Profile.COMPATIBILITY;
		glf.debug = false;//true; has no effect
		glf.grabCursor = true;
		viewer.setFormat(glf, style);

		viewer.buildWidget(style);
		viewer.setHeadlight(false);


		viewer.setSceneGraph(sg.getSceneGraph());

		viewer.setHeightProvider(sg);

		SCENE_POSITION = new SbVec3f(/*sg.getCenterX()/2*/0, sg.getCenterY(), Z_TRANSLATION);

		viewer.setUpDirection(new SbVec3f(0, 0, 1));

		viewer.getCameraController().setAutoClipping(false);

		SoCamera camera = viewer.getCameraController().getCamera();

		sg.setCamera(camera);

		camera.nearDistance.setValue(MINIMUM_VIEW_DISTANCE);
		camera.farDistance.setValue(MAXIMUM_VIEW_DISTANCE);

		camera.position.setValue(0, 0, 0);
		camera.orientation.setValue(new SbVec3f(0, 1, 0), -(float) Math.PI / 2.0f);

		double previousTimeSec = 0;
		boolean timeStop = false;
		boolean fly = false;

		File saveGameFile = new File("savegame.mri");
		if (saveGameFile.exists()) {
			try {
				InputStream in = new FileInputStream(saveGameFile);

				Properties saveGameProperties = new Properties();

				saveGameProperties.load(in);

				float x = Float.valueOf(saveGameProperties.getProperty(HERO_X, "250"));

				float y = Float.valueOf(saveGameProperties.getProperty(HERO_Y, "305"));

				float z = Float.valueOf(saveGameProperties.getProperty(HERO_Z, String.valueOf(1279/* - SCENE_POSITION.getZ()*/)));

				previousTimeSec = Double.valueOf(saveGameProperties.getProperty(TIME, "0"));

				camera.position.setValue(x, y, z - SCENE_POSITION.getZ());

				timeStop = "true".equals(saveGameProperties.getProperty(TIME_STOP, "false")) ? true : false;

				fly = "true".equals(saveGameProperties.getProperty(FLY, "false")) ? true : false;

				in.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}


		} else {
			camera.position.setValue(250, 305, 1279 - SCENE_POSITION.getZ());
		}
		viewer.getCameraController().changeCameraValues(camera);

		viewer.getSceneHandler().setClearBeforeRender(false);
		viewer.getSceneHandler().setBackgroundColor(/*new SbColor(0,0,1)*/SceneGraphIndexedFaceSetShader.SKY_COLOR.darker());

		viewer.getSceneHandler().setTransparencyType(TransparencyType.BLEND/*SORTED_LAYERS_BLEND*/);

		sg.setPosition(SCENE_POSITION.getX(), SCENE_POSITION.getY()/*,SCENE_POSITION.getZ()*/);

		final double computerStartTimeCorrected = COMPUTER_START_TIME_SEC - (double) System.nanoTime() / 1e9;//60*60*4.5 / TimeConstants./*JMEMBA_TIME_ACCELERATION*/GTA_SA_TIME_ACCELERATION;

		if (previousTimeSec == 0) {
			viewer.setStartDate(computerStartTimeCorrected);
		} else {
			viewer.setStartDate(previousTimeSec - (double) System.nanoTime() / 1e9);
		}

		if (timeStop) {
			viewer.toggleTimeStop();
		}

		if (fly) {
			viewer.toggleFly();
		}

		viewer.addIdleListener((viewer1) -> {
			double nowSec = viewer.getNow();
			double nowHour = nowSec / 60 / 60;
			double nowDay = 100;//nowHour / 24; // always summer
			double nowGame = nowHour * TimeConstants./*JMEMBA_TIME_ACCELERATION*/GTA_SA_TIME_ACCELERATION;
			double Phi = 47;
			SbVec3f sunPosition = Soleil.soleil_xyz((float) nowDay, (float) nowGame, (float) Phi);
			sg.setSunPosition(new SbVec3f(-sunPosition.y(), -sunPosition.x(), sunPosition.z()));
		});
		viewer.addIdleListener((viewer1) -> {
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

		viewer.start();
		viewer.updateLocation(new SbVec3f(0.0f, 0.0f, 0.0f),ForceProvider.Direction.STILL);


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

		GL2 gl2 = new GL2() {
		};

		int[] depthBits = new int[1];
		gl2.glGetIntegerv(GL2.GL_DEPTH_BITS, depthBits);

		System.out.println("Depth Buffer : " + depthBits[0]);

		// _____________________________________________________ Physics with bullet physics

//	    GdxNativesLoader.load();
//	    new SharedLibraryLoader().load("gdx-bullet");
//
//		Bullet.init();
//
//	    btBroadphaseInterface m_pBroadphase;
//	    btCollisionConfiguration m_pCollisionConfiguration;
//	    btCollisionDispatcher m_pDispatcher;
//	    btConstraintSolver m_pSolver;
//	    btDynamicsWorld m_pWorld;
//
//	    //Meanwhile, the code to initialize Bullet can be found in BasicDemo and looks as shown in the following code snippet:
//
//		btDefaultCollisionConstructionInfo info = new btDefaultCollisionConstructionInfo();
//		//info.setUseEpaPenetrationAlgorithm(99);
//
//	    m_pCollisionConfiguration = new btDefaultCollisionConfiguration(info);
//	    m_pDispatcher = new btCollisionDispatcher(m_pCollisionConfiguration);
//
//	    m_pBroadphase = new btDbvtBroadphase();
//	    m_pSolver = new btSequentialImpulseConstraintSolver();
//	    m_pWorld = new btDiscreteDynamicsWorld(m_pDispatcher, m_pBroadphase, m_pSolver, m_pCollisionConfiguration);
//
//		m_pWorld.setGravity(new Vector3(0,0,-9.81f));
//
//	    // create a box shape of size (1,1,1)
//		btSphereShape /*btBoxShape*/ pBoxShape = new btSphereShape/*btBoxShape*/(0.4f);//,1.75f-2*0.4f/*new Vector3(0.5f/2.0f, 0.5f/2.0f, 1.75f/2.0f)*/);
//
//		SbVec3f cameraPositionValue = camera.position.getValue();
//
//	    // give our box an initial position of (0,0,0)
//	    btTransform transform = new btTransform();
//	    transform.setIdentity();
//	    transform.setOrigin(new Vector3(cameraPositionValue.getX(), cameraPositionValue.getY(), cameraPositionValue.getZ() + 0.4f - 1.75f + 0.13f));
//
//	    // create a motion state
//	    OpenGLMotionState m_pMotionState = new OpenGLMotionState(transform);
//
//	    // create the rigid body construction info object, giving it a
//	    // mass of 1, the motion state, and the shape
//	    btRigidBody.btRigidBodyConstructionInfo rbInfo = new btRigidBody.btRigidBodyConstructionInfo(82.0f, m_pMotionState, pBoxShape);
//	    btRigidBody pRigidBody = new btRigidBody(rbInfo);
//
//		pRigidBody.setActivationState(DISABLE_DEACTIVATION);
//
//		//pRigidBody.setCollisionFlags(pRigidBody.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CHARACTER_OBJECT);
//
//	    // inform our world that we just created a new rigid body for
//	    // it to manage
//	    m_pWorld.addRigidBody(pRigidBody);
//
//	btBoxShape groundShape = new btBoxShape(new Vector3(99999,99999,1));
//
//		// give our box an initial position of (0,0,0)
//		btTransform groundTransform = new btTransform();
//		groundTransform.setIdentity();
//		groundTransform.setOrigin(new Vector3(cameraPositionValue.getX(), cameraPositionValue.getY(), cameraPositionValue.getZ()-1 - 1.75f + 0.13f/*-0.4f*/-0.01f));
//
//	    btRigidBody.btRigidBodyConstructionInfo groundInfo = new btRigidBody.btRigidBodyConstructionInfo(0, new OpenGLMotionState(groundTransform), groundShape);
//	    btRigidBody groundBody = new btRigidBody(groundInfo);
//
//		//groundBody.setCollisionFlags(pRigidBody.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_STATIC_OBJECT);
//
//	    m_pWorld.addRigidBody(groundBody);
//
//	    //m_pWorld.getSolverInfo().setSplitImpulse(1);
//		//m_pWorld.getSolverInfo().setSplitImpulsePenetrationThreshold(0);
//
//		//btHeightfieldTerrainShape terrainShape = new btHeightfieldTerrainShape();
//
//		viewer.addIdleListener((viewer1)->{
//			m_pWorld.stepSimulation((float)viewer1.dt());//,10,(float)viewer1.dt()/10.0f);
//		});
//
//		viewer.setPositionProvider(new PositionProvider() {
//			@Override
//			public SbVec3f getPosition() {
//				Matrix4 worldTrans = new Matrix4();
//				m_pMotionState.getWorldTransform(worldTrans);
//				Vector3 position = new Vector3();
//				worldTrans.getTranslation(position);
//				return new SbVec3f(position.x,position.y,position.z - 0.4f + 1.75f - 0.13f );
//			}
//		});
//
//		viewer.setForceProvider(new ForceProvider() {
//
//			@Override
//			public void apply(SbVec3f force) {
//				//pRigidBody.clearForces();
//				//pRigidBody.clearForces();
//				pRigidBody.applyCentralImpulse(new Vector3(force.getX(),force.getY(),force.getZ()));
//			}
//		});

		//Dickinson, Chris. Learning Game Physics with Bullet Physics and OpenGL (Kindle Locations 801-807). Packt Publishing. Kindle Edition.

		// _____________________________________________________ Physics with bullet physics (End)

		// _____________________________________________________ Physics with ODE4j

		OdeHelper.initODE2(0);
		DWorld world = OdeHelper.createWorld();
		world.setAutoDisableFlag(false);
		world.setERP(0.2);
		world.setCFM(1e-5);
		world.setContactMaxCorrectingVel(0.5);
		world.setContactSurfaceLayer(0.01);
		DSpace space = OdeHelper.createHashSpace();
		DJointGroup contactGroup = OdeHelper.createJointGroup();
		SbVec3f cameraPositionValue = camera.position.getValue();
		DGeom water = OdeHelper.createPlane(space, 0, 0, 1, -Z_TRANSLATION + 1000 - 150);

		sg.setSpace(space);

		DHeightfieldData heightFieldData = OdeHelper.createHeightfieldData();

		int nbi = sg.getNbI();
		int nbj = sg.getNbJ();
		float[] pHeightData = new float[nbi * nbj];
		int index = 0;
		for (int i = 0; i < nbi; i++) {
			for (int j = 0; j < nbj; j++) {
				index = i + nbi * j;
				pHeightData[index] = sg.getZ(i/*106*/,/*4927*/j) - Z_TRANSLATION;
				//pHeightData[index] = (float) (cameraPositionValue.getZ() - 1.75f + 0.13f - 0.01f);
			}
		}
		double heightFieldWidth = sg.getWidth();
		double heightFieldDepth = sg.getHeight();
		int widthSamples = nbi;
		int depthSamples = nbj;
		double scale = 1;
		double offset = 0;
		double thickness = 10;
		heightFieldData.build(pHeightData, false, heightFieldWidth, heightFieldDepth, widthSamples, depthSamples, scale, offset, thickness, false);
		final DHeightfield heightField = OdeHelper.createHeightfield(space, heightFieldData, true);
		DQuaternion q = new DQuaternion();
		Rotation.dQFromAxisAndAngle(q, 1, 0, 0, Math.PI / 2);
		heightField.setQuaternion(q);
		heightField.setPosition(-SCENE_POSITION.getX() + heightFieldWidth / 2, -SCENE_POSITION.getY() + sg.getExtraDY() + heightFieldDepth / 2, 0);

		world.setGravity(0, 0, -9.81);

		final float above_ground = 0.2f; // Necessary when respawning on water

		final DBody body = OdeHelper.createBody(world);
		body.setPosition(cameraPositionValue.getX(), cameraPositionValue.getY(), cameraPositionValue.getZ() - /*1.75f / 2*/0.4f + 0.13f + above_ground);
		DMass m = OdeHelper.createMass();
		m.setBox(1000.0f, 0.25, 0.25, 1.312);
		body.setMass(m);
		body.setMaxAngularSpeed(0);

		//DGeom box = OdeHelper.createCapsule(space, 0.4, 1.75 - 2 * 0.4);
		DGeom box = OdeHelper.createSphere(space,0.4);
		box.setBody(body);

		DRay ray = OdeHelper.createRay(space, 10000);
		DVector3C direction = ray.getDirection();
		//DBody rayBody = OdeHelper.createBody(world);
		ray.setBody(body);
		//ray.set(cameraPositionValue.getX(), cameraPositionValue.getY(), cameraPositionValue.getZ() - 1.75f/2 + 0.13f,0,0,-1);

		DGeom ball = OdeHelper.createSphere(space, 0.4);
		final DBody ballBody = OdeHelper.createBody(world);
		ballBody.setPosition(
				body.getPosition().get0()/*cameraPositionValue.getX()*/,
				body.getPosition().get1()/*cameraPositionValue.getY()*/,
				body.getPosition().get2() /*cameraPositionValue.getZ() - 0.4f + 0.13f + above_ground*/ - 1.75f+ 2*0.4f);
		DMass ballm = OdeHelper.createMass();
		ballm.setSphere(1000.0f, 0.25);
		ballBody.setMass(ballm);
		ball.setBody(ballBody);

//		final DAMotorJoint joint = OdeHelper.createAMotorJoint(world,null);
//		joint.attach(body,ballBody);
//		joint.setNumAxes(3);
//		joint.setAxis(0,1,1,0,0);
//		joint.setAxis(1,1,0,1,0);
//		joint.setAxis(2,1,0,0,1);
//		joint.setParamVel(0.0f);
//		joint.setParamFMax(9999);
//		joint.setParamVel2(0.0f);
//		joint.setParamFMax2(9999);
//		joint.setParamVel3(0.0f);
//		joint.setParamFMax3(9999);

		final DHingeJoint hinge2Joint = OdeHelper.createHingeJoint (world,null);
		hinge2Joint.attach(body,ballBody);
		hinge2Joint.setAnchor(ballBody.getPosition());
		//hinge2Joint.setAxis1 (0,0,1);
		hinge2Joint.setAxis (0,1,0);
		hinge2Joint.setParamVel(0);
		//hinge2Joint.setParamVel2(0);
		hinge2Joint.setParamFMax(1000);
		//hinge2Joint.setParamFMax2(100);
//		hinge2Joint.setParamFudgeFactor(0.1);
//		hinge2Joint.setParamSuspensionERP (0.4);
//		hinge2Joint.setParamSuspensionCFM (0.8);

		DGeom.DNearCallback callback = new DGeom.DNearCallback() {
			@Override
			public void call(Object data, DGeom geom1, DGeom geom2) {
				// Get the rigid bodies associated with the geometries
				DBody body1 = geom1.getBody();// dGeomGetBody(geom1);
				DBody body2 = geom2.getBody();// dGeomGetBody(geom2);

				// Maximum number of contacts to create between bodies (see ODE documentation)
				int MAX_NUM_CONTACTS = 8;
				//dContact contacts[MAX_NUM_CONTACTS];
				DContactBuffer contacts = new DContactBuffer(MAX_NUM_CONTACTS);

				// Add collision joints
				int numc = OdeHelper.collide(geom1, geom2, MAX_NUM_CONTACTS, contacts.getGeomBuffer());

				if ((geom1 instanceof DRay || geom2 instanceof DRay)) {

					double force = 0;
					if (numc != 0) {
						DContact contact = contacts.get(0);
						DContactGeom contactGeom = contact.getContactGeom();
						double depth = contactGeom.depth;

						force = 50 * depth;
						if (geom1 instanceof DRay) {
							double mass = body1.getMass().getMass();
							body1.addForce(0, 0, force * mass - 10 * body1.getLinearVel().get2() * mass);
							//if (depth < 0.2) {
							DVector3 lvDir = body1.getLinearVel().clone();
							if (lvDir.lengthSquared() != 0) {
								lvDir.normalize();
								lvDir.scale(-mass * 3);
								body1.addForce(lvDir);
							}
							DVector3 lv = body1.getLinearVel().clone();
							lv.scale(-mass * 0.5);
							body1.addForce(lv);
							//}
						} else {
							double mass = body2.getMass().getMass();
							body2.addForce(0, 0, force * mass - 10 * body2.getLinearVel().get2() * mass);
							//if (depth < 0.2) {
							DVector3 lvDir = body2.getLinearVel().clone();
							if (lvDir.lengthSquared() != 0) {
								lvDir.normalize();
								lvDir.scale(-mass * 3);
								body2.addForce(lvDir);
							}
							DVector3 lv = body2.getLinearVel().clone();
							lv.scale(-mass * 0.5);
							body2.addForce(lv);
							//}
						}
					}
					return;
				}

				for (int i = 0; i < numc; ++i) {
					DContact contact = contacts.get(i);
					contact.surface.mode = OdeConstants.dContactSoftERP | OdeConstants.dContactSoftCFM | OdeConstants.dContactApprox1 |
							OdeConstants.dContactSlip1 | OdeConstants.dContactSlip2;

					//contact.surface.bounce = 0.1;
					contact.surface.mu = ((double[]) data)[0];//0.8;//50.0;
					contact.surface.soft_erp = 0.96;
					contact.surface.soft_cfm = 1e-5;
					contact.surface.rho = 0;
					contact.surface.rho2 = 0;

					// struct dSurfaceParameters {
					//      int mode;
					//      dReal mu;
					//      dReal mu2;
					//      dReal rho;
					//      dReal rho2;
					//      dReal rhoN;
					//      dReal bounce;
					//      dReal bounce_vel;
					//      dReal soft_erp;
					//      dReal soft_cfm;
					//      dReal motion1, motion2, motionN;
					//      dReal slip1, slip2;
					// };

//					DContactJoint contactJoint = OdeHelper.createContactJoint(/*collision_data->world*/world,
//							/*collision_data->contact_group*/contactGroup, contacts.get(i));
//
//					contactJoint.attach(body1, body2);
				}
			}
		};

		DGeom.DNearCallback callback2 = new DGeom.DNearCallback() {

			@Override
			public void call(Object data, DGeom geom1, DGeom geom2) {

				boolean withCapsule = false;
				if(geom1 instanceof DCapsule || geom2 instanceof DCapsule) {
					withCapsule = true;
				}

				// Get the rigid bodies associated with the geometries
				DBody body1 = geom1.getBody();// dGeomGetBody(geom1);
				DBody body2 = geom2.getBody();// dGeomGetBody(geom2);

				// Can happen outside island
				if(body1 != null && Double.isNaN(body1.getPosition().get0())) {
					return;
				}

				if(body2 != null && Double.isNaN(body2.getPosition().get0())) {
					return;
				}

				if(body1 == null && body2 == null) {
					return; // no joint between two still objects
				}

//				if (body1 == body && body2 == ballBody) {
//					return;
//				}
//
//				if (body2 == body && body1 == ballBody) {
//					return;
//				}

				// Maximum number of contacts to create between bodies (see ODE documentation)
				int MAX_NUM_CONTACTS = 8;
				//dContact contacts[MAX_NUM_CONTACTS];
				DContactBuffer contacts = new DContactBuffer(MAX_NUM_CONTACTS);

				// Add collision joints
				int numc = OdeHelper.collide(geom1, geom2, MAX_NUM_CONTACTS, contacts.getGeomBuffer());

				for (int i = 0; i < numc; ++i) {
					DContact contact = contacts.get(i);
					contact.surface.mode = OdeConstants.dContactSoftERP | OdeConstants.dContactSoftCFM | OdeConstants.dContactApprox1 |
							OdeConstants.dContactSlip1 | OdeConstants.dContactSlip2;

					//contact.surface.bounce = 0.1;
					contact.surface.mu = 0.838;//((double[]) data)[0];//0.8;//50.0;
					contact.surface.slip1 = 0;//.1;
					contact.surface.slip2 = 0;//.1;
					contact.surface.soft_erp = 0.96;
					contact.surface.soft_cfm = 1e-5;
					contact.surface.rho = 0;
					contact.surface.rho2 = 0;

					// struct dSurfaceParameters {
					//      int mode;
					//      dReal mu;
					//      dReal mu2;
					//      dReal rho;
					//      dReal rho2;
					//      dReal rhoN;
					//      dReal bounce;
					//      dReal bounce_vel;
					//      dReal soft_erp;
					//      dReal soft_cfm;
					//      dReal motion1, motion2, motionN;
					//      dReal slip1, slip2;
					// };

					DContactJoint contactJoint = OdeHelper.createContactJoint(/*collision_data->world*/world,
							/*collision_data->contact_group*/contactGroup, contacts.get(i));

					contactJoint.attach(body1, body2);
				}

			}
		};

		final double[] data = new double[1];
		data[0] = 100.0;//0.8;

		int nb_step = 50;

		DVector3 saved_pos = new DVector3();
		viewer.addIdleListener((viewer1) -> {

			// TODO : getGroundZ() is not accurate
			float camz = sg.getGroundZ() + 1.75f - 0.13f;

			float zref = camz - 0.4f + 0.13f;

			if (viewer1.isFlying()) {
				saved_pos.set0(camera.position.getValue().getX());
				saved_pos.set1(camera.position.getValue().getY());
				saved_pos.set2(camera.position.getValue().getZ()/*zref + 1.0f*/ - /*1.75f / 2*/0.4f + 0.13f + above_ground);
				body.setPosition(saved_pos);
				//body.setLinearVel(0,0,0);
				//ballBody.setPosition(camera.position.getValue().getX(), camera.position.getValue().getY(), camera.position.getValue().getZ() - /*1.75f / 2*/0.4f + 0.13f - 1.75f+ 2*0.4f + above_ground);
				ballBody.setPosition(
						body.getPosition().get0()/*cameraPositionValue.getX()*/,
						body.getPosition().get1()/*cameraPositionValue.getY()*/,
						body.getPosition().get2() /*cameraPositionValue.getZ() - 0.4f + 0.13f + above_ground*/ - 1.75f+ 2*0.4f);
				return;
			}
			double dt = Math.min(0.5, viewer1.dt());
			for (int i = 0; i < nb_step; i++) {
				physics_error = false;
				saved_pos.set(body.getPosition());
				space.collide(data, /*callback*/callback2);
				world.step(dt / nb_step);
				contactGroup.empty();
				if(physics_error) {
					saved_pos.add2(0.1);
					body.setPosition(saved_pos);
					ballBody.setPosition(
							body.getPosition().get0()/*cameraPositionValue.getX()*/,
							body.getPosition().get1()/*cameraPositionValue.getY()*/,
							body.getPosition().get2() /*cameraPositionValue.getZ() - 0.4f + 0.13f + above_ground*/ - 1.75f+ 2*0.4f);
				}
			}
			if(body.getPosition().get2() < zref - 1.5f) {
				System.err.println("Error in placement, too low");
				saved_pos.set2(zref + 1.0f);
				body.setPosition(saved_pos);
				ballBody.setPosition(
						body.getPosition().get0()/*cameraPositionValue.getX()*/,
						body.getPosition().get1()/*cameraPositionValue.getY()*/,
						body.getPosition().get2() /*cameraPositionValue.getZ() - 0.4f + 0.13f + above_ground*/ - 1.75f+ 2*0.4f);
			}
		});

		ErrorHandler.dMessageFunction function = new ErrorHandler.dMessageFunction() {
			@Override
			public void call(int errnum, String msg, Object... ap) {
				physics_error = true;
				System.err.println(msg);
			}
		};

		ErrorHdl.dSetMessageHandler(function);

		viewer.setPositionProvider(new PositionProvider() {
			@Override
			public SbVec3f getPosition() {
				DVector3C position = body.getPosition();

				if(Double.isNaN(position.get0())) {
					return null;
				}

				return new SbVec3f((float) position.get0(), (float) position.get1(), (float) position.get2() + /*1.75f / 2*/0.4f - 0.13f);
			}
		});

		viewer.setForceProvider(new ForceProvider() {

			@Override
			public void apply(SbVec3f force, Direction direction) {
				if (force.length() == 0) {
					data[0] = 1.0;
				} else {
					//force.setZ(82*200/2000);
					data[0] = 0;
				}
				//body.addForce(force.getX() * 2000, force.getY() * 2000, force.getZ() * 2000);
				SbVec3f xvec = new SbVec3f(0,0,1);
				SbVec3f vwVec = camera.orientation.getValue().multVec( new SbVec3f(0,0,-1) );
				//SbVec3f dir = camera.orientation.getValue().multVec(xvec);
				switch (direction) {

					case STILL:
						hinge2Joint.setAxis ( - vwVec.y(),vwVec.x(),0);
						hinge2Joint.setParamVel(0);
						break;
					case FRONT:
						hinge2Joint.setAxis ( - vwVec.y(),vwVec.x(),0);
						hinge2Joint.setParamVel(-10/0.4);
						break;
					case BACK:
						hinge2Joint.setAxis ( - vwVec.y(),vwVec.x(),0);
						hinge2Joint.setParamVel(10);
						break;
					case LEFT:
						hinge2Joint.setAxis ( vwVec.x(),vwVec.y(),0);
						hinge2Joint.setParamVel(10);
						break;
					case RIGHT:
						hinge2Joint.setAxis ( vwVec.x(),vwVec.y(),0);
						hinge2Joint.setParamVel(-10);
						break;
				}
			}
		});

		OptionDialog dialog = new OptionDialog(viewer, sg);

		viewer.setEscapeCallback((viewer2) -> {
			if (!viewer2.isTimeStop()) {
				viewer2.toggleTimeStop();
			}
			viewer2.setVisible(false);
			//SwingUtilities.invokeLater(()->
			{
				dialog.pack();
				dialog.setLocationRelativeTo(null);
				dialog.setVisible(true);
			}
			//);
		});

		// _____________________________________________________ Physics with ODE4j (End)

		viewer.addIdleListener((viewer1) -> {
					sg.setFPS(viewer1.getFPS());
				}
		);

		window.setVisible(false);

		boolean success = viewer.setFocus();

		// _____________________________________________________ Story
		Scenario scenario = new Scenario(sg);

		// __________________________________________ Oracle encounter
		scenario.addQuest(new FirstApproachQuest());
		// __________________________________________ Killing targets
		scenario.addQuest(new TargetsKillingQuest());

		scenario.start();

		viewer.addIdleListener((viewer1)->{
			scenario.idle(viewer1);
		});

		SwingUtilities.invokeLater(() -> {
			loop();
		});
	}

	public static void loop() {
		display.readAndDispatch();

		if (display.shouldClose())
			SwingUtilities.invokeLater(() -> {

						sg.preDestroy();

						viewer.destructor();

						viewer = null;

						// disposes all associated windows and their components
						display.dispose();

						display = null;

						sg = null;

						window.dispose(); // must be done at the end, for linux

						window = null;

						KDebug.dump();

						System.exit(0); // Necessary, because of Linux
					}
			);
		else {
			SwingUtilities.invokeLater(() -> {
				loop();
			});
		}
	}

	public static byte[] loadSound(final String url) {
		String args = "ressource/" + url;
		File file = new File(args);
		if (!file.exists()) {
			file = new File("application/" + args);
		}
		byte[] fileContent = null;
		try {
			fileContent = Files.readAllBytes(file.toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return fileContent;
	}

	public static synchronized void playSound(final /*String url*/byte[] sound) {
		new Thread(new Runnable() {
			// The wrapper thread is unnecessary, unless it blocks on the
			// Clip finishing; see comments.
			public void run() {
				try {
					Clip clip = AudioSystem.getClip();
					clip.addLineListener(new LineListener() {

						@Override
						public void update(LineEvent event) {
							if (event.getType() == LineEvent.Type.STOP) {
								clip.close();
							}
						}

					});
					AudioInputStream inputStream = AudioSystem.getAudioInputStream(
							new ByteArrayInputStream(sound));
					clip.open(inputStream);
					clip.start();
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}).start();
	}

	public static synchronized void playSound(Clip clip) {
		new Thread(new Runnable() {
			// The wrapper thread is unnecessary, unless it blocks on the
			// Clip finishing; see comments.
			public void run() {
				try {
					clip.start();

				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}).start();
	}
}