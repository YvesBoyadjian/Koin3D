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

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;

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
import jscenegraph.database.inventor.SbViewportRegion;
import jscenegraph.database.inventor.SoPath;
import jscenegraph.database.inventor.SoPickedPoint;
import jscenegraph.database.inventor.actions.SoGLRenderAction.TransparencyType;
import jscenegraph.database.inventor.events.SoMouseButtonEvent;
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

/**
 * @author Yves Boyadjian
 *
 */
public class MainGLFW {
	
	public static final float MINIMUM_VIEW_DISTANCE = 2.5f;//1.0f;

	public static final float MAXIMUM_VIEW_DISTANCE = SceneGraphIndexedFaceSetShader.WATER_HORIZON;//5e4f;//SceneGraphIndexedFaceSet.SUN_FAKE_DISTANCE * 1.5f;
	
	public static final float Z_TRANSLATION = 2000;
	
	public static final double REAL_START_TIME_SEC = 60*60*4.5; // 4h30 in the morning
	
	public static final double COMPUTER_START_TIME_SEC = REAL_START_TIME_SEC / TimeConstants./*JMEMBA_TIME_ACCELERATION*/GTA_SA_TIME_ACCELERATION;
	
	public static final String HERO_X = "hero_x";
	
	public static final String HERO_Y = "hero_y";
	
	public static final String HERO_Z = "hero_z";
	
	public static final String TIME = "time_sec";
	
	public static SbVec3f SCENE_POSITION;
	
	public static final SbColor SKY_BLUE = new SbColor(0.53f,0.81f,0.92f);

	public static final SbColor DEEP_SKY_BLUE = new SbColor( 0.0f, 0.749f, 1.0f);

	public static final SbColor VERY_DEEP_SKY_BLUE = new SbColor( 0.0f, 0.749f/3.0f, 1.0f);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
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
		
		JWindow window = new JWindow();/* {
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
		intro.setFont(intro.getFont().deriveFont((float)height/20f));
		window.getContentPane().add(
			    intro);
		window.getContentPane().setBackground(Color.BLACK);
		window.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
		//window.getContentPane().setForeground(Color.white);
		
		window.setBounds(0, 0, (int)width, (int)height);
		
		window.setVisible(true);
		
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
		rw = null; // for garbage collection
		re = null; // for garbage collection
		        
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
					
					saveGameProperties.setProperty(TIME, String.valueOf(System.nanoTime()/1e9 + getStartDate()));
					
					saveGameProperties.store(out, "Mount Rainier Island save game");
				
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
				if(gunSound != null) {
					playSound(/*"GUN_FIRE-GoodSoundForYou-820112263_10db.wav"*//*clipf*/gunSound);
				}
				SbViewportRegion vr = this.getSceneHandler().getViewportRegion();
				SoRayPickAction fireAction = new SoRayPickAction(vr);
				//fireAction.setRay(new SbVec3f(0.0f,0.0f,0.0f), new SbVec3f(0.0f,0.0f,-1.0f),0.1f,1000f);
				fireAction.setPoint(vr.getViewportSizePixels().operator_div(2));
				fireAction.setRadius(2.0f);
				fireAction.apply(this.getSceneHandler().getSceneGraph());
				SoPickedPoint pp = fireAction.getPickedPoint();
				if( pp != null) {
					SoPath p = pp.getPath();
					if( p != null) {
						SoNode n = p.getTail();
						if( n.isOfType(SoCube.getClassTypeId())) {
							int len = p.getLength();
							if( len > 1) {
								SoNode parent = p.getNode(len-2);
								if(parent.isOfType(SoGroup.getClassTypeId())) {
									SoGroup g = (SoGroup)parent;
									SoMaterial c = new SoMaterial();
									c.diffuseColor.setValue(1, 0, 0);
									g.insertChild(c, 0);
								}
							}
							//System.out.println(pp.getPath().getTail().getClass());							
						}
					}
				}
				fireAction.destructor();
			}
		};
	    GLData glf = new GLData(/*GLProfile.getDefault()*/);
	    glf.redSize = 10;
	    glf.greenSize = 10;
	    glf.blueSize = 10;
	    glf.alphaSize = 0;
	    glf.depthSize = 24;
	    glf.doubleBuffer = true;
	    glf.majorVersion = 3;
	    glf.minorVersion = 3;
	    glf.api = GLData.API.GL;
	    glf.profile = GLData.Profile.COMPATIBILITY;
	    glf.debug = false;//true; has no effect
	    glf.grabCursor = true;
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
		
		double previousTimeSec = 0;
		
		File saveGameFile = new File("savegame.mri");
		if( saveGameFile.exists() ) {
			try {
				InputStream in = new FileInputStream(saveGameFile);
				
				Properties saveGameProperties = new Properties();
				
				saveGameProperties.load(in);
				
				float x = Float.valueOf(saveGameProperties.getProperty(HERO_X, "250"));
				
				float y = Float.valueOf(saveGameProperties.getProperty(HERO_Y, "305"));
				
				float z = Float.valueOf(saveGameProperties.getProperty(HERO_Z, String.valueOf(1279/* - SCENE_POSITION.getZ()*/)));
				
				previousTimeSec = Double.valueOf(saveGameProperties.getProperty(TIME,"0"));
				
				camera.position.setValue(x,y,z- SCENE_POSITION.getZ());
				
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
		
		viewer.getSceneHandler().setBackgroundColor(/*new SbColor(0,0,1)*/SceneGraphIndexedFaceSetShader.SKY_COLOR.darker());

		viewer.getSceneHandler().setTransparencyType(TransparencyType.BLEND/*SORTED_LAYERS_BLEND*/);
		
		sg.setPosition(SCENE_POSITION.getX(),SCENE_POSITION.getY()/*,SCENE_POSITION.getZ()*/);
		
		final double computerStartTimeCorrected =  COMPUTER_START_TIME_SEC - (double)System.nanoTime() /1e9;//60*60*4.5 / TimeConstants./*JMEMBA_TIME_ACCELERATION*/GTA_SA_TIME_ACCELERATION;
		
		if( previousTimeSec == 0) {
			viewer.setStartDate(computerStartTimeCorrected);
		}
		else {
			viewer.setStartDate(previousTimeSec - (double)System.nanoTime() /1e9);
		}
		
		viewer.addIdleListener((viewer1)->{
			double nanoTime = System.nanoTime();
			double nowSec = nanoTime / 1e9 + viewer.getStartDate();
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
	    
	    window.setVisible(false);
	    
	    boolean success = viewer.setFocus();
	    
	    display.loop();
	    
	    sg.preDestroy();
	    
	    viewer.destructor();

		// disposes all associated windows and their components
		display.dispose();
		
	    window.dispose(); // must be done at the end, for linux		
	    
	    KDebug.dump();
	}
	
	public static byte[] loadSound(final String url) {
		String args = "ressource/"+url;
		File file = new File(args);
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
