///////////////////////////////////////////////////////////////////////////////
//  SoTerrain
///////////////////////////////////////////////////////////////////////////////
///
/// \file SoTerrainTest.cpp
/// \author Radek Barto�- xbarto33
/// \date 06.03.2006
///
/// </DL>
//////////////////////////////////////////////////////////////////////////////
// Copyright (C) 2006 Radek Barton
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
///////////////////////////////////////////////////////////////////////////////

package jterrain;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferUShort;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import gnu.getopt.Getopt;
import jscenegraph.coin3d.inventor.nodes.SoCoordinate3;
import jscenegraph.coin3d.inventor.nodes.SoTexture2;
import jscenegraph.database.inventor.SbRotation;
import jscenegraph.database.inventor.SbTime;
import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SoDB;
import jscenegraph.database.inventor.events.SoKeyboardEvent;
import jscenegraph.database.inventor.nodes.SoDirectionalLight;
import jscenegraph.database.inventor.nodes.SoDrawStyle;
import jscenegraph.database.inventor.nodes.SoEventCallback;
import jscenegraph.database.inventor.nodes.SoIndexedTriangleStripSet;
import jscenegraph.database.inventor.nodes.SoNormal;
import jscenegraph.database.inventor.nodes.SoNormalBinding;
import jscenegraph.database.inventor.nodes.SoPerspectiveCamera;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.database.inventor.nodes.SoTextureCoordinate2;
import jscenegraph.database.inventor.sensors.SoSensor;
import jscenegraph.database.inventor.sensors.SoTimerSensor;
import jscenegraph.interaction.inventor.SoSceneManager;
import jscenegraph.port.Array;
import jscenegraph.port.Destroyable;
import jscenegraph.port.SbVec2fArray;
import jscenegraph.port.SbVec3fArray;
import jsceneviewer.inventor.qt.SoQt;
import jsceneviewer.inventor.qt.SoQtRenderArea;
import jterrain.chunkedlod.SoSimpleChunkedLoDTerrain;
import jterrain.geomipmapping.SoSimpleGeoMipmapTerrain;
import jterrain.profiler.PrProfiler;
import jterrain.profiler.SoProfileGroup;
import roam.SoSimpleROAMTerrain;

/**
 * @author Yves Boyadjian
 *
 */
public class SoTerrainTest {

	public enum ID_ALG
	{
	  ID_ALG_BRUAL_FORCE,
	  ID_ALG_ROAM,
	  ID_ALG_GEO_MIPMAP,
	  ID_ALG_CHUNKED_LOD
	};

	static ID_ALG algorithm = ID_ALG.ID_ALG_ROAM;
	static float animation_time = 30.0f;
	static float frame_time = 0.04f;
	static boolean is_synchronize = false;

	/* Change terrain properties by key press callback. */
	static void terrainCallback(Object userData, SoEventCallback eventCB)
	{
	  final SoKeyboardEvent event =
	    ( SoKeyboardEvent )(eventCB.getEvent());

	  if (SoKeyboardEvent.SO_KEY_PRESS_EVENT(event, SoKeyboardEvent.Key.ENTER))
	  {
	    switch (algorithm)
	    {
	      case ID_ALG_ROAM:
	      {
	        SoSimpleROAMTerrain terrain = (SoSimpleROAMTerrain)
	          (userData);
	        terrain.freeze.setValue(!terrain.freeze.getValue());
	      }
	      break;
	      case ID_ALG_GEO_MIPMAP:
	      {
	        SoSimpleGeoMipmapTerrain terrain = (SoSimpleGeoMipmapTerrain)
	          (userData);
	        terrain.freeze.setValue(!terrain.freeze.getValue());
	      }
	      break;
	      case ID_ALG_CHUNKED_LOD:
	      {
	        SoSimpleChunkedLoDTerrain terrain = (SoSimpleChunkedLoDTerrain)
	          (userData);
	        terrain.freeze.setValue(!terrain.freeze.getValue());
	      }
	      break;
	    }
	    eventCB.setHandled();
	  }
	  else if (SoKeyboardEvent.SO_KEY_PRESS_EVENT(event, SoKeyboardEvent.Key.PAD_ADD))
	  {
	    switch (algorithm)
	    {
	      case ID_ALG_ROAM:
	      {
	        SoSimpleROAMTerrain terrain = (SoSimpleROAMTerrain)
	          (userData);
	        terrain.pixelError.setValue(terrain.pixelError.getValue() + 1);
	      }
	      break;
	      case ID_ALG_GEO_MIPMAP:
	      {
	        SoSimpleGeoMipmapTerrain terrain = (SoSimpleGeoMipmapTerrain)
	          (userData);
	        terrain.pixelError.setValue(terrain.pixelError.getValue() + 1);
	      }
	      break;
	      case ID_ALG_CHUNKED_LOD:
	      {
	        SoSimpleChunkedLoDTerrain terrain = (SoSimpleChunkedLoDTerrain)
	          (userData);
	        terrain.pixelError.setValue(terrain.pixelError.getValue() + 1);
	      }
	      break;
	    }
	    eventCB.setHandled();
	  }
	  else if (SoKeyboardEvent.SO_KEY_PRESS_EVENT(event, SoKeyboardEvent.Key.PAD_SUBTRACT))
	  {
	    switch (algorithm)
	    {
	      case ID_ALG_ROAM:
	      {
	        SoSimpleROAMTerrain terrain = (SoSimpleROAMTerrain)
	          (userData);
	        if (terrain.pixelError.getValue() > 1)
	        {
	          terrain.pixelError.setValue(terrain.pixelError.getValue() - 1);
	        }
	      }
	      break;
	      case ID_ALG_GEO_MIPMAP:
	      {
	        SoSimpleGeoMipmapTerrain terrain = (SoSimpleGeoMipmapTerrain)
	          (userData);
	        if (terrain.pixelError.getValue() > 1)
	        {
	          terrain.pixelError.setValue(terrain.pixelError.getValue() - 1);
	        }
	      }
	      break;
	      case ID_ALG_CHUNKED_LOD:
	      {
	        SoSimpleChunkedLoDTerrain terrain = (SoSimpleChunkedLoDTerrain)
	          (userData);
	        if (terrain.pixelError.getValue() > 1)
	        {
	          terrain.pixelError.setValue(terrain.pixelError.getValue() - 1);
	        }
	      }
	      break;
	    }
	    eventCB.setHandled();
	  }
	  else if (SoKeyboardEvent.SO_KEY_PRESS_EVENT(event, SoKeyboardEvent.Key.F))
	  {
	    switch (algorithm)
	    {
	      case ID_ALG_ROAM:
	      {
	        SoSimpleROAMTerrain terrain = (SoSimpleROAMTerrain)
	          (userData);
	        terrain.frustrumCulling.setValue(!terrain.frustrumCulling.getValue());
	      }
	      break;
	      case ID_ALG_GEO_MIPMAP:
	      {
	        SoSimpleGeoMipmapTerrain terrain = (SoSimpleGeoMipmapTerrain)
	          (userData);
	        terrain.frustrumCulling.setValue(!terrain.frustrumCulling.getValue());
	      }
	      break;
	      case ID_ALG_CHUNKED_LOD:
	      {
	        SoSimpleChunkedLoDTerrain terrain = (SoSimpleChunkedLoDTerrain)
	          (userData);
	        terrain.frustrumCulling.setValue(!terrain.frustrumCulling.getValue());
	      }
	      break;
	    }
	    eventCB.setHandled();
	  }
	}

	static void styleCallback(Object userData, SoEventCallback eventCB)
	{
	  SoDrawStyle style = (SoDrawStyle)(userData);
	  final SoKeyboardEvent event =
	    (SoKeyboardEvent)(eventCB.getEvent());

	  if (SoKeyboardEvent.SO_KEY_PRESS_EVENT(event, SoKeyboardEvent.Key.L))
	  {
	    if (style.style.getValue() == SoDrawStyle.Style.FILLED.getValue())
	    {
	      style.style.setValue(SoDrawStyle.Style.LINES);
	    }
	    else
	    {
	      style.style.setValue(SoDrawStyle.Style.FILLED);
	    }
	  }
	}

	  static final int CAMERA_POINTS_COUNT = 10;
	  
	  /* Spatial points for camera movement. */
	  static final Array<SbVec3f> camera_positions = Array.createWithValues(SbVec3f.class, 
	    new SbVec3f(0.0f, 0.1f, 0.03f), new SbVec3f(0.6f, 0.1f, 0.03f),
	    	new SbVec3f(0.6f, 0.1f, 0.03f), new SbVec3f(0.6f, 0.6f, 0.03f),
	     new SbVec3f(0.6f, 0.6f, 0.03f), new SbVec3f(0.2f, 0.6f, 0.03f),
	     new SbVec3f(0.2f, 0.6f, 0.03f), new SbVec3f(0.6f, 0.6f, 0.03f),
	     new SbVec3f(0.6f, 0.6f, 0.03f), new SbVec3f(0.6f, 0.1f, 0.03f));
	  
	  static final Array<SbRotation> camera_orientations = Array.createWithValues(SbRotation.class,
	    new SbRotation(new SbVec3f(1.0f, 0.0f, 0.0f), (float)Math.PI / 2.0f).operator_mul(
	    	new SbRotation(new SbVec3f(0.0f, 0.0f, 1.0f), -(float)Math.PI / 2.0f)),
	       new SbRotation(new SbVec3f(1.0f, 0.0f, 0.0f), (float)Math.PI / 2.0f).operator_mul(
	     new SbRotation(new SbVec3f(0.0f, 0.0f, 1.0f), -(float)Math.PI / 2.0f)),
	       new SbRotation(new SbVec3f(1.0f, 0.0f, 0.0f), (float)Math.PI / 2.0f).operator_mul(
	     new SbRotation(new SbVec3f(0.0f, 0.0f, 1.0f), 0.0f)),
	       new SbRotation(new SbVec3f(1.0f, 0.0f, 0.0f), (float)Math.PI / 2.0f).operator_mul(
	     new SbRotation(new SbVec3f(0.0f, 0.0f, 1.0f), 0.0f)),
	       new SbRotation(new SbVec3f(1.0f, 0.0f, 0.0f), (float)Math.PI / 2.0f).operator_mul(
	     new SbRotation(new SbVec3f(0.0f, 0.0f, 1.0f), (float)Math.PI / 2.0f)),
	       new SbRotation(new SbVec3f(1.0f, 0.0f, 0.0f), (float)Math.PI / 2.0f).operator_mul(
	     new SbRotation(new SbVec3f(0.0f, 0.0f, 1.0f), (float)Math.PI / 2.0f)),
	       new SbRotation(new SbVec3f(1.0f, 0.0f, 0.0f), (float)Math.PI / 2.0f).operator_mul(
	     new SbRotation(new SbVec3f(0.0f, 0.0f, 1.0f), -(float)Math.PI / 2.0f)),
	       new SbRotation(new SbVec3f(1.0f, 0.0f, 0.0f), (float)Math.PI / 2.0f).operator_mul(
	     new SbRotation(new SbVec3f(0.0f, 0.0f, 1.0f), -(float)Math.PI / 2.0f)),
	       new SbRotation(new SbVec3f(1.0f, 0.0f, 0.0f), (float)Math.PI / 2.0f).operator_mul(
	     new SbRotation(new SbVec3f(0.0f, 0.0f, 1.0f), -(float)Math.PI)),
	       new SbRotation(new SbVec3f(1.0f, 0.0f, 0.0f), (float)Math.PI / 2.0f).operator_mul(
	     new SbRotation(new SbVec3f(0.0f, 0.0f, 1.0f), -(float)Math.PI)));

	  static int frame = 0;
	  static SbTime start_time = SbTime.zero();
	  
	/* Camera animation callback.  */
	static void cameraTimerCallback(Object userData, SoSensor sensor)
	{
	  SoPerspectiveCamera camera = (SoPerspectiveCamera)
	    (userData);

	  if (frame == 0)
	  {
	    camera.position.setValue(camera_positions.get(0));
	    camera.orientation.setValue(camera_orientations.get(0));
	  }
	  else
	  {
	    if (frame == 1)
	    {
	      start_time = SbTime.getTimeOfDay();
	    }

	    float position = 0.0f;
	    if (is_synchronize)
	    {
	      position = (float)((SbTime.getTimeOfDay().operator_minus(start_time)).getValue() / animation_time)
	        * CAMERA_POINTS_COUNT;
	    }
	    else
	    {
	      position = ((frame * frame_time) / animation_time) * CAMERA_POINTS_COUNT;
	    }

	    int point = (int)(position);
	    float ratio = position - point;

	    if (point >= CAMERA_POINTS_COUNT - 1)
	    {
	      System.exit(0);//SoQt.exitMainLoop();
	    }

	    camera.position.setValue( camera_positions.get(point).operator_mul(1.0f - ratio).operator_add(
	      camera_positions.get(point + 1).operator_mul(ratio)));
	    SbRotation orientation_1 = camera_orientations.get(point);
	    SbRotation orientation_2 = camera_orientations.get(point + 1);
	    camera.orientation.setValue(SbRotation.slerp(orientation_1, orientation_2, ratio));
	  }

	  frame++;
	}

	static void renderCallback(Object _render_area, SoSceneManager scene_manager)
	{
	  /* Redraw render area when using custom scene manager. */
	  SoQtRenderArea render_area = (SoQtRenderArea)(_render_area);
	  render_area.render();
	}

	static void help()
	{
	  System.out.println( "Usage: SoTerrainTest -h heightmap [-t texture] [-p profile_file] "+
	    "[-a algorithm] [-A animation_time] [-F frame_time] [-e pixel_error] "+
	    "[-r triangle_count] [-g tile_size] [-f] [-c] [-v] [-s]" );
	  System.out.println( "\t-h heightmap\t\tImage with input heightmap." 
	    );
	  System.out.println( "\t-t texture\t\tImage with terrain texture." );
	//#ifdef PROFILE
	  System.out.println( "\t-p profile_file\t\tFile for profiling output (default: profile.txt)."
	    );
	//#endif
	  System.out.println( "\t-a algorithm\t\tAlgorithm of terrain visualization. (default: roam)"
	    );
	  System.out.println( "\t\tbrutalforce\t\tBrutal force terrain rendering."
	    );
	  System.out.println( "\t\troam\t\t\tROAM algorithm terrain rendeing." );
	  System.out.println( "\t\tgeomipmapping\t\tGeo Mip-Mapping algorithm terrain rendering."
	    );
	  System.out.println( "\t-A animation_time\tLength of animation in miliseconds (default: 30 s)."
	    );
	  System.out.println( "\t-F frame_time\t\tFrame time in miliseconds (default: 40 ms)."
	    );
	  System.out.println( "\t-e pixel_error\t\tDisplay error of rendering in pixels (default: 6)."
	    );
	  System.out.println( "\t-r triangle_count\tMaximal number of triangles in triangulation. (default 10.000)"
	    );
	  System.out.println( "\t-g tile_size\t\tSize of side of each tile in tile based algorithms. (default: 33)"
	    );
	  System.out.println( "\t-f\t\t\tRun application at fullscreen." );
	  System.out.println( "\t-c\t\t\tEnable frustrum culling." );
	  System.out.println( "\t-v\t\t\tRun animation at application start." );
	  System.out.println( "\t-s\t\t\tEnable animation synchronization with time." );
	}

	public static void main(String argv[])
	{
		int argc = argv.length; // java port
		
	  /* Default values of program arguments. */
	  String heightmap_name = "";
	  String texture_name = ""; // java port
	  String profile_name = "profile.txt";
	  final int[] triangle_count = new int[1]; triangle_count[0] = 10000;
	  final int[] tile_size = new int[1]; tile_size[0] = 33;
	  final int[] pixel_error = new int[1]; pixel_error[0] = 6;
	  boolean is_animation = false;
	  boolean is_full_screen = false;
	  boolean is_frustrum_culling = true;

	  /* Get program arguments. */
	  int command = 0;
	  Getopt getopt = new Getopt("SoTerrain", argv, "h:t:p:a:A:F:e:r:g:fcvs");
	  while ((command = getopt.getopt()) != -1)
	  {
	    switch (command)
	    {
	      /* Heightmap. */
	      case 'h':
	      {
	        heightmap_name = getopt.getOptarg();
	      }
	      break;
	      /* Texture. */
	      case 't':
	      {
	        texture_name = getopt.getOptarg();
	      }
	      break;
	      /* File for output of profiler. */
	      case 'p':
	      {
	        profile_name = getopt.getOptarg();
	      }
	      break;
	      /* Algorithm. */
	      case 'a':
	      {
	        if (!strcmp(getopt.getOptarg(), "roam"))
	        {
	          algorithm = ID_ALG.ID_ALG_ROAM;
	        }
	        else if (!strcmp(getopt.getOptarg(), "geomipmapping"))
	        {
	          algorithm = ID_ALG.ID_ALG_GEO_MIPMAP;
	        }
	        else if (!strcmp(getopt.getOptarg(), "chunkedlod"))
	        {
	          algorithm = ID_ALG.ID_ALG_CHUNKED_LOD;
	        }
	        else if (!strcmp(getopt.getOptarg(), "brutalforce"))
	        {
	          algorithm = ID_ALG.ID_ALG_BRUAL_FORCE;
	        }
	      }
	      break;
	      /* Animation time. */
	      case 'A':
	      {
	        final int[] tmp = new int[1];
	        sscanf(getopt.getOptarg(), "%d", tmp);
	        animation_time = tmp[0] * 0.001f;
	      }
	      break;
	      /* Animation frame time. */
	      case 'F':
	      {
	        final int[] tmp = new int[1];
	        sscanf(getopt.getOptarg(), "%d", tmp);
	        frame_time = tmp[0] * 0.001f;
	      }
	      break;
	      /* Pixel error of rendering. */
	      case 'e':
	      {
	        sscanf(getopt.getOptarg(), "%d", pixel_error);
	      }
	      break;
	      /* Number of triangles in triangulation. */
	      case 'r':
	      {
	        sscanf(getopt.getOptarg(), "%d", triangle_count);
	      }
	      break;
	      /* Tile side size. */
	      case 'g':
	      {
	        sscanf(getopt.getOptarg(), "%d", tile_size);
	      }
	      break;
	      /* Fullscreen. */
	      case 'f':
	      {
	        is_full_screen = true;
	      }
	      break;
	      /* Frustrum culling. */
	      case 'c':
	      {
	        is_frustrum_culling = false;
	      }
	      break;
	      /* Do animation. */
	      case 'v':
	      {
	        is_animation = true;
	      }
	      break;
	      /* Synchronize animation with time. */
	      case 's':
	      {
	        is_synchronize = true;
	      }
	      break;
	      case '?':
	      {
	        System.out.println( "Unknown option!" );
	        help();
	        System.exit(1);
	      }
	      break;
	    }
	  }

	  /* Check obligatory arguments. */
	  if (heightmap_name == null)
	  {
	    System.out.println( "Input height map wasn't specified!" );
	    help();
	    System.exit(1);
	  }

	  /* Load heightmap. */
	  final int[] width = new int[1];
	  final int[] height = new int[1];
	  final int[] components = new int[1];
	  short[] heightmap = simage_read_image(heightmap_name, width,
	    height, components);
	  if (heightmap == null)
	  {
	    System.out.println( "Error loading height map " + heightmap_name + "!"
	      );
	    System.exit(1);
	  }
	  
	  PrProfiler.PR_INIT_PROFILER();

	  /* Set environment variables. */
	  //putenv("IV_SEPARATOR_MAX_CACHES=0");
	  System.setProperty("COIN_SHOW_FPS_COUNTER","1");
	  //putenv("COIN_AUTO_CACHING=0");

	  /* Create window. */
	  Display display = new Display();
	  Shell window;
	  if ((window = new Shell(display)/*SoQt.init(argc, argv, "SoTerrain Test Application")*/) == null)
	  {
	    System.exit(1);
	  }
	  
	  SoQt.init();

	  /* Initialization of custom Inventor classes. */
	  SoSimpleROAMTerrain.initClass();
	  SoSimpleGeoMipmapTerrain.initClass();
	  SoSimpleChunkedLoDTerrain.initClass();
	  //SoProfileGroup.initClass();

	  /* Create scene graph. */
	  SoProfileGroup root = new SoProfileGroup();
	  SoEventCallback style_callback = new SoEventCallback();
	  SoPerspectiveCamera camera = new SoPerspectiveCamera();
	  SoDrawStyle style = new SoDrawStyle();
	  SoDirectionalLight light = new SoDirectionalLight();
	  SoSeparator separator = new SoSeparator();
	  SoEventCallback terrain_callback = new SoEventCallback();
	  SoTexture2 texture = new SoTexture2();
	  SoTextureCoordinate2 texture_coords = new SoTextureCoordinate2();
	  SoCoordinate3 coords = new SoCoordinate3();
	  SoNormal normals = new SoNormal();
	  SoNormalBinding normal_binding = new SoNormalBinding();

	  /* Set scene graph nodes properties. */
	  style_callback.addEventCallback(SoKeyboardEvent.getClassTypeId(),
			  SoTerrainTest::styleCallback, style);
	  light.direction.setValue(0.5f, 0.5f, -1.0f);
	  texture.filename.setValue(texture_name);
	  coords.point.setNum(width[0] * height[0]);
	  texture_coords.point.setNum(width[0] * height[0]);
	  normals.vector.setNum(width[0] * height[0]);
	  normal_binding.value.setValue(SoNormalBinding.Binding.PER_VERTEX_INDEXED);

	  /* Create heightmap. */
	  SbVec3fArray points = coords.point.startEditingFast();
	  SbVec2fArray texture_points = texture_coords.point.startEditingFast();
	  SbVec3fArray normal_points = normals.vector.startEditingFast();
	  for (int I = 0; I < width[0] * height[0]; ++I)
	  {
	    float x = (float)(I % width[0]) / (float)(width[0]);
	    float y = (float)(I / width[0]) / (float)(height[0]);	    
	   
	    int i = I%width[0];
	    int j = I/width[0];
	    //i = width[0] - i -1;
	    j = height[0] - j - 1;
	    int index = i + j*width[0]; 
	    
	    int hm = heightmap[/*I*/index]; if(hm<0)hm+=(256*256);
	    points.get(I).copyFrom( new SbVec3f(x, y, /*heightmap[I]*/hm * 0.0002f));
	    texture_points.get(I).copyFrom( new SbVec2f(x, y));
	  }

	  /* Compute inner normals. */
	  for (int Y = 1; Y < (height[0] - 1); ++Y)
	  {
	    for (int X = 1; X < (width[0] - 1); ++X)
	    {
	      int index = Y * width[0] + X;
	      final SbVec3f normal = new SbVec3f(0.0f, 0.0f, 0.0f);

	      normal.operator_add_equal((points.get(index - 1).operator_minus(points.get(index))).cross(points.get(index - width[0]).operator_minus(points.get(index))));
	      normal.operator_add_equal((points.get(index - width[0]).operator_minus(points.get(index))).cross(points.get(index - width[0] + 1).operator_minus(points.get(index))));
	      normal.operator_add_equal((points.get(index - width[0] + 1).operator_minus(points.get(index))).cross(points.get(index + 1).operator_minus( points.get(index))));
	      normal.operator_add_equal((points.get(index + 1).operator_minus(points.get(index))).cross(points.get(index + width[0]).operator_minus( points.get(index))));
	      normal.operator_add_equal((points.get(index + width[0]).operator_minus(points.get(index))).cross(points.get(index + width[0] - 1).operator_minus( points.get(index))));
	      normal.operator_add_equal((points.get(index + width[0] - 1).operator_minus(points.get(index))).cross(points.get(index - 1).operator_minus( points.get(index))));
	      normal.normalize();
	      normal_points.get(index).copyFrom(normal);
	    }
	  }

	  /* Compute normals at top and bottom border. */
	  for (int X = 1; X < (width[0] - 1); ++X)
	  {
	    int index_1 = X;
	    int index_2 = (height[0] - 1) * width[0] + X;
	    final SbVec3f normal_1 = new SbVec3f(0.0f, 0.0f, 0.0f);
	    final SbVec3f normal_2 = new SbVec3f(0.0f, 0.0f, 0.0f);

	    /* Top border. */
	    normal_1 .operator_add_equal( (points.get(index_1 + 1).operator_minus(points.get(index_1))).cross(points.get(index_1 + width[0]).operator_minus( points.get(index_1))));
	    normal_1 .operator_add_equal( (points.get(index_1 + width[0]).operator_minus( points.get(index_1))).cross(points.get(index_1 + width[0] - 1).operator_minus( points.get(index_1))));
	    normal_1 .operator_add_equal( (points.get(index_1 + width[0] - 1).operator_minus( points.get(index_1))).cross(points.get(index_1 - 1).operator_minus( points.get(index_1))));

	    /* Bottom border. */
	    normal_2 .operator_add_equal( (points.get(index_2 - 1) .operator_minus(points.get(index_2))).cross(points.get(index_2 - width[0]) .operator_minus(points.get(index_2))));
	    normal_2 .operator_add_equal( (points.get(index_2 - width[0]) .operator_minus(points.get(index_2))).cross(points.get(index_2 - width[0] + 1) .operator_minus(points.get(index_2))));
	    normal_2 .operator_add_equal( (points.get(index_2 - width[0] + 1).operator_minus( points.get(index_2))).cross(points.get(index_2 + 1).operator_minus( points.get(index_2))));

	    normal_1.normalize();
	    normal_2.normalize();
	    normal_points.get(index_1).copyFrom( normal_1);
	    normal_points.get(index_2).copyFrom( normal_2);
	  }

	  /* Compute normals at left and right border. */
	  for (int Y2 = 1; Y2 < (height[0] - 1); ++Y2)
	  {
	    int index_1 = Y2 * width[0];
	    int index_2 = index_1 + width[0] - 1;
	    final SbVec3f normal_1 = new SbVec3f(0.0f, 0.0f, 0.0f);
	    final SbVec3f normal_2 = new SbVec3f(0.0f, 0.0f, 0.0f);

	    /* Left border. */
	    normal_1 .operator_add_equal( (points.get(index_1 - width[0]).operator_minus(points.get(index_1))).cross(points.get(index_1 - width[0] + 1).operator_minus( points.get(index_1))));
	    normal_1 .operator_add_equal( (points.get(index_1 - width[0] + 1).operator_minus( points.get(index_1))).cross(points.get(index_1 + 1).operator_minus( points.get(index_1))));
	    normal_1 .operator_add_equal( (points.get(index_1 + 1).operator_minus( points.get(index_1))).cross(points.get(index_1 + width[0]).operator_minus( points.get(index_1))));

	    /* Right border. */
	    normal_2 .operator_add_equal( (points.get(index_2 - 1).operator_minus( points.get(index_2))).cross(points.get(index_2 - width[0]).operator_minus( points.get(index_2))));
	    normal_2 .operator_add_equal( (points.get(index_2 + width[0]).operator_minus( points.get(index_2))).cross(points.get(index_2 + width[0] - 1).operator_minus( points.get(index_2))));
	    normal_2 .operator_add_equal( (points.get(index_2 + width[0] - 1).operator_minus( points.get(index_2))).cross(points.get(index_2 - 1).operator_minus( points.get(index_2))));

	    normal_1.normalize();
	    normal_2.normalize();
	    normal_points.get(index_1).copyFrom(normal_1);
	    normal_points.get(index_2).copyFrom(normal_2);
	  }

	  /* Compute normals in corners. */
	  int index;
	  final SbVec3f normal = new SbVec3f();

	  index = 0;
	  normal.copyFrom( (points.get(index + 1).operator_minus( points.get(index))).cross(points.get(index + width[0]).operator_minus( points.get(index))));
	  normal.normalize();
	  normal_points.get(index).copyFrom(normal);

	  index = (height[0] * width[0]) - 1;
	  normal.copyFrom((points.get(index - 1).operator_minus( points.get(index))).cross(points.get(index - width[0]).operator_minus( points.get(index))));
	  normal.normalize();
	  normal_points.get(index).copyFrom(normal);

	  index = (height[0] - 1) * width[0];
	  normal.copyFrom((points.get(index - width[0]).operator_minus( points.get(index))).cross(points.get(index - width[0] + 1).operator_minus( points.get(index))));
	  normal .operator_add_equal( (points.get(index - width[0] + 1).operator_minus( points.get(index))).cross(points.get(index + 1).operator_minus( points.get(index))));
	  normal.normalize();
	  normal_points.get(index).copyFrom(normal);

	  index = width[0] - 1;
	  normal .operator_add_equal( (points.get(index + width[0]).operator_minus( points.get(index))).cross(points.get(index + width[0] - 1).operator_minus( points.get(index))));
	  normal .operator_add_equal( (points.get(index + width[0] - 1).operator_minus( points.get(index))).cross(points.get(index - 1).operator_minus( points.get(index))));
	  normal.normalize();
	  normal_points.get(index).copyFrom(normal);

	  coords.point.finishEditing();
	  texture_coords.point.finishEditing();
	  normals.vector.finishEditing();
	  simage_free_image(heightmap);

	  /* Connect scene graph nodes. */
	  root.ref();
	  root.addChild(style);
	  root.addChild(separator);
	  separator.addChild(terrain_callback);
	  separator.addChild(style_callback);
	  separator.addChild(camera);
	  separator.addChild(light);
	  separator.addChild(texture);
	  separator.addChild(texture_coords);
	  separator.addChild(coords);
	  separator.addChild(normals);
	  separator.addChild(normal_binding);

	  switch (algorithm)
	  {
	    case ID_ALG_ROAM:
	    {
	      SoSimpleROAMTerrain terrain = new SoSimpleROAMTerrain();
	      terrain.mapSize.setValue(width[0]);
	      terrain.pixelError.setValue(pixel_error[0]);
	      terrain.triangleCount.setValue(triangle_count[0]);
	      terrain.frustrumCulling.setValue(is_frustrum_culling);
	      terrain_callback.addEventCallback(SoKeyboardEvent.getClassTypeId(),
	    		  SoTerrainTest::terrainCallback, terrain);
	      separator.addChild(terrain);
	    }
	    break;
	    case ID_ALG_GEO_MIPMAP:
	    {
	      SoSimpleGeoMipmapTerrain terrain = new SoSimpleGeoMipmapTerrain();
	      terrain.mapSize.setValue(width[0]);
	      terrain.tileSize.setValue(tile_size[0]);
	      terrain.pixelError.setValue(pixel_error[0]);
	      terrain_callback.addEventCallback(SoKeyboardEvent.getClassTypeId(),
	    		  SoTerrainTest::terrainCallback, terrain);
	      separator.addChild(terrain);
	    }
	    break;
	    case ID_ALG_CHUNKED_LOD:
	    {
	      SoSimpleChunkedLoDTerrain terrain = new SoSimpleChunkedLoDTerrain();
	      terrain.mapSize.setValue(width[0]);
	      terrain.tileSize.setValue(tile_size[0]);
	      terrain.pixelError.setValue(pixel_error[0]);
	      terrain_callback.addEventCallback(SoKeyboardEvent.getClassTypeId(),
	    		  SoTerrainTest::terrainCallback, terrain);
	      separator.addChild(terrain);
	    }
	    break;
	    case ID_ALG_BRUAL_FORCE:
	    {
	      SoIndexedTriangleStripSet terrain = new SoIndexedTriangleStripSet();

	      /* Create terrain heightmap vertices indices. */
	      terrain.coordIndex.setNum((height[0] - 1) * ((2 * width[0]) + 1));
	      int[] indices = terrain.coordIndex.startEditingI();

	      int I = 0;
	      for (int Y = 0; Y < (height[0] - 1); Y++)
	      {
	        for (int X = 0; X < width[0]; X++)
	        {
	          indices[I++] = (Y * width[0]) + X;
	          indices[I++] = ((Y + 1) * width[0]) + X;
	        }
	        indices[I++] = -1;
	      }
	      terrain.coordIndex.finishEditing();
	      separator.addChild(terrain);
	    }
	    break;
	  }
	  
	  window.setLayout(new FillLayout());

	  /* Setup camera and render area. */
	  SoQtFreeViewer render_area = new SoQtFreeViewer(window);
	  SoSceneManager scene_manager = new SoSceneManager();
	  render_area.getCameraController().setHeadlight(false);
	  render_area.getSceneHandler().setSceneManager(scene_manager);
	  scene_manager.setRenderCallback(SoTerrainTest::renderCallback, render_area);
	  scene_manager.activate();
	  camera.viewAll(root, render_area.getSceneHandler().getViewportRegion());
	  camera.nearDistance.setValue(0.01f);
	  render_area.setSceneGraph(root);
	  render_area.setTitle("SoTerrain Test Application");
	  render_area.show();
	  render_area.setFullScreen(is_full_screen);


	  /* Run animation or set camera position and orientation. */
	  SoTimerSensor camera_timer = null;
	  if (is_animation)
	  {
	    camera_timer = new SoTimerSensor(SoTerrainTest::cameraTimerCallback,
	      camera);
	    camera_timer.setInterval(frame_time);
	    camera_timer.schedule();
	  }
	  else
	  {
	    camera.position.setValue(0.365994f, 0.281897f, 0.023945f);
	    camera.orientation.setValue(-0.452279f, 0.426091f, 0.537269f, -0.570291f);
	  }

	  /* Run application. */
	  window.open();
	  window.setSize((short)2500, (short)1400);
	  window.setLocation(10, 10);
	  
	  while (!window.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();	 

	  PrProfiler.PR_PRINT_RESULTS(profile_name);

	  /* Free memory. */
	  root.unref();
	  Destroyable.delete(camera_timer);
	  Destroyable.delete(render_area);

	  //return /*EXIT_SUCCESS*/0;
	}

	private static void simage_free_image(short[] heightmap) {
		// TODO Auto-generated method stub
		
	}

	private static short[] simage_read_image(String heightmap_name, int[] width, int[] height, int[] components) {
		
		File imageFile = new File(heightmap_name);
		try {
			BufferedImage image = ImageIO.read(imageFile);
			width[0] = image.getWidth();
			height[0] = image.getHeight();
			components[0] = 1;
			WritableRaster raster = image.getRaster();
			DataBuffer buffer = raster.getDataBuffer();
			if(buffer instanceof DataBufferByte) {
				DataBufferByte bufferByte = (DataBufferByte)buffer;
				byte imBytes[] = bufferByte.getData().clone();
				short[] imgShort = new short[imBytes.length];
				for(int i=0;i<imBytes.length;i++) {
					imgShort[i] = imBytes[i];
					if (imgShort[i]<0) {
						imgShort[i] += 256;
					}
				}
				return imgShort;
			}
			else {
				DataBufferUShort bufferUShort = (DataBufferUShort) buffer;
				short[] imgShort = bufferUShort.getData().clone();
				return imgShort;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO Auto-generated method stub
		return null;
	}

	private static void sscanf(String optarg, String string, int[] tmp) {
		// TODO Auto-generated method stub
		if(Objects.equals(string,"%d")) {
			tmp[0] = Integer.parseInt(optarg);
		}
	}

	private static boolean strcmp(String optarg, String string) {
		return !Objects.equals(optarg, string);
	}
}
