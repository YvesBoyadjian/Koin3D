
///////////////////////////////////////////////////////////////////////////////
//  SoTerrain
///////////////////////////////////////////////////////////////////////////////
/// Terrain rendered by Chunked LoD algorithm.
/// \file SoSimpleChunkedLoDTerrain.h
/// \author Radek Barton - xbarto33@stud.fit.vutbr.cz
/// \date 08.11.2006
///
/// This is a scene graph node representing terrain rendered by Chunked LoD
/// algorithm. To use this node you must preppend ::SoCordinate3 node with input
/// heightmap coordinates, set \e mapSize field to size of side of that
/// heightmap and then set \e tileSize field to desired size of tiles on any
/// level of detail. \e mapSize \p - \p 1 have to be dividable by \e mapSize
/// \p - \p 1 and this fraction have to be \p 2^n where \p n is whole positive
/// number. Moreover \e mapSize and \e tileSize have to be even but not
/// necessarily \p 2^n \p + \p 1 as it is in ::SoSimpleROAMTerrain node.
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

package jterrain.chunkedlod;

import com.jogamp.opengl.GL2;

import jscenegraph.coin3d.inventor.elements.SoTextureEnabledElement;
import jscenegraph.database.inventor.SbBasic;
import jscenegraph.database.inventor.SbBox2s;
import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbViewVolume;
import jscenegraph.database.inventor.SbViewportRegion;
import jscenegraph.database.inventor.SoPrimitiveVertex;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoGetBoundingBoxAction;
import jscenegraph.database.inventor.bundles.SoMaterialBundle;
import jscenegraph.database.inventor.elements.SoCoordinateElement;
import jscenegraph.database.inventor.elements.SoLightModelElement;
import jscenegraph.database.inventor.elements.SoMaterialBindingElement;
import jscenegraph.database.inventor.elements.SoNormalBindingElement;
import jscenegraph.database.inventor.elements.SoNormalElement;
import jscenegraph.database.inventor.elements.SoTextureCoordinateElement;
import jscenegraph.database.inventor.elements.SoViewVolumeElement;
import jscenegraph.database.inventor.elements.SoViewportRegionElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFBool;
import jscenegraph.database.inventor.fields.SoSFInt32;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoShape;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.database.inventor.sensors.SoFieldSensor;
import jscenegraph.database.inventor.sensors.SoSensor;
import jscenegraph.port.Destroyable;
import jterrain.profiler.PrProfiler;

/**
 * @author Yves Boyadjian
 *
 */
/** Terrain rendered by Chunked LoD algorithm.
This is a scene graph node representing terrain rendered by Chunked LoD
algorithm. To use this node you must preppend ::SoCordinate3 node with input
heightmap coordinates, set \e mapSize field to size of side of that heightmap
and then set \e tileSize field to desired size of tiles on any level of detail.
\e mapSize \p - \p 1 have to be dividable by \e mapSize \p - \p 1 and this
fraction have to be \p 2^n where \p n is whole positive number. Moreover
\e mapSize and \e tileSize have to be odd but not necessarily \p 2^n \p +
\p 1 as it is in ::SoSimpleROAMTerrain node. */
public class SoSimpleChunkedLoDTerrain extends SoShape {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoSimpleChunkedLoDTerrain.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoSimpleChunkedLoDTerrain.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoSimpleChunkedLoDTerrain.class); }    	  	
	
    /* Fields. */
    /// Size of side of input height map.
    public final SoSFInt32 mapSize = new SoSFInt32();
    /// Size of side of tile.
    public final SoSFInt32 tileSize = new SoSFInt32();
    /// Desired pixel error of rendered terrain.
    public final SoSFInt32 pixelError = new SoSFInt32();
    /// Flag of enabled frustrum culling.
    public final SoSFBool frustrumCulling = new SoSFBool();
    /// Flag of freezed algorithm.
    public final SoSFBool freeze = new SoSFBool();
	
    /* Elements shortcuts. */
    /// Coordinates of input heightmap.
    protected SbVec3f[] coords;
    /// Coordinates of heightmap texture.
    protected SbVec2f[] texture_coords;
    /// Normals of input heightmap.
    protected SbVec3f[] normals;
    /// Current view volume. 
    protected final SbViewVolume view_volume = new SbViewVolume();
    /// Current viewport region.
    protected final SbViewportRegion viewport_region = new SbViewportRegion();
    /* Internal data. */
    /// Tile quad-tree.
    protected SbChunkedLoDTileTree tile_tree; //ptr
    /// Distance constant for coumputing dynamic part of error metric.
    protected float distance_const;
    /// Flag that texture is pressent and should be rendered.
    protected boolean is_texture;
    /// Flag that normals are present and should be rendered.
    protected boolean is_normals;
    /* Internal fields values. */
    /// Internal value of SoSimpleChunkedLoDTerrain::mapSize field.
    protected int map_size;
    /// Internal value of SoSimpleChunkedLoDTerrain::tileSize field.
    protected int tile_size;
    /// Internal value of SoSimpleChunkedLoDTerrain::pixelError field.
    protected int pixel_error;
    /// Internal value of SoSimpleChunkedLoDTerrain::frustrumCulling field.
    protected boolean is_frustrum_culling;
    /// Internal value of SoSimpleChunkedLoDTerrain::freeze field.
    protected boolean is_freeze;
    /* Sensors. */
    /// Sensor watching SoSimpleChunkedLoDTerrain::mapSize field changes.
    protected SoFieldSensor map_size_sensor;
    /// Sensor watching SoSimpleChunkedLoDTerrain::tileSize field changes.
    protected SoFieldSensor tile_size_sensor;
    /// Sensor watching SoSimpleChunkedLoDTerrain::pixelError field changes.
    protected SoFieldSensor pixel_error_sensor;
    /// Sensor watching SoSimpleChunkedLoDTerrain::frustrumCulling field changes.
    protected SoFieldSensor frustrum_culling_sensor;
    /// Sensor watching SoSimpleChunkedLoDTerrain::freeze field changes.
    protected SoFieldSensor freeze_sensor;
    /* Constants. */
    /// Constants for default pixel error of tile.
 // Init constants.
    protected static final int DEFAULT_PIXEL_ERROR = 20;
    
    
    /** Run-time class initialisation.
    This method must be called before any instance of
    ::SoSimpleChunkedLoDTerrain class is created. */
	public static void initClass()
{
  // Init class.
  SoSubNode.SO_NODE_INIT_CLASS(SoSimpleChunkedLoDTerrain.class, SoShape.class, "Shape");
  SO_ENABLE(SoGLRenderAction.class, SoMaterialBindingElement.class);
  SO_ENABLE(SoGLRenderAction.class, SoCoordinateElement.class);
  SO_ENABLE(SoGLRenderAction.class, SoTextureCoordinateElement.class);
  SO_ENABLE(SoGLRenderAction.class, SoTextureEnabledElement.class);
  SO_ENABLE(SoGLRenderAction.class, SoLightModelElement.class);
  SO_ENABLE(SoGLRenderAction.class, SoNormalElement.class);
  SO_ENABLE(SoGLRenderAction.class, SoNormalBindingElement.class);
  SO_ENABLE(SoGLRenderAction.class, SoViewVolumeElement.class);
  SO_ENABLE(SoGLRenderAction.class, SoViewportRegionElement.class);
  SO_ENABLE(SoGetBoundingBoxAction.class, SoCoordinateElement.class);
}

    /** Constructor.
    Creates instance of ::SoSimpleChunkedLoDTerrain class. */
public SoSimpleChunkedLoDTerrain() {
  coords = null; texture_coords = null; normals = null;
  view_volume.copyFrom( new SbViewVolume()); viewport_region.copyFrom( new SbViewportRegion());
  tile_tree = null; distance_const = 0.0f; is_texture = false; is_normals = false;
  map_size = 2; tile_size = 2; pixel_error = DEFAULT_PIXEL_ERROR;
  is_frustrum_culling = true; is_freeze =false; map_size_sensor =null;
  tile_size_sensor =null; pixel_error_sensor =null;
  frustrum_culling_sensor =null; freeze_sensor =null;

  // Init object.
  nodeHeader.SO_NODE_CONSTRUCTOR(SoSimpleChunkedLoDTerrain.class);

  // Init fields.
  nodeHeader.SO_NODE_ADD_FIELD(mapSize,"mapSize", (2));
  nodeHeader.SO_NODE_ADD_FIELD(tileSize,"tileSize", (2));
  nodeHeader.SO_NODE_ADD_FIELD(pixelError,"pixelError", (DEFAULT_PIXEL_ERROR));
  nodeHeader.SO_NODE_ADD_FIELD(frustrumCulling,"frustrumCulling", (true));
  nodeHeader.SO_NODE_ADD_FIELD(freeze,"freeze", (false));

  // Create sensors.
  this.map_size_sensor = new SoFieldSensor(SoSimpleChunkedLoDTerrain::mapSizeChangedCB, this);
  this.tile_size_sensor = new SoFieldSensor(SoSimpleChunkedLoDTerrain::tileSizeChangedCB, this);
  this.pixel_error_sensor = new SoFieldSensor(SoSimpleChunkedLoDTerrain::pixelErrorChangedCB, this);
  this.frustrum_culling_sensor = new SoFieldSensor(SoSimpleChunkedLoDTerrain::frustrumCullingChangedCB,
    this);
  this.freeze_sensor = new SoFieldSensor(SoSimpleChunkedLoDTerrain::freezeChangedCB, this);

  // Connect fields to sensors.
  this.map_size_sensor.attach((this.mapSize));
  this.tile_size_sensor.attach((this.tileSize));
  this.pixel_error_sensor.attach((this.pixelError));
  this.frustrum_culling_sensor.attach((this.frustrumCulling));
  this.freeze_sensor.attach((this.freeze));
}

/** Renders terrain.
Creates tile quad-tree from input heightmap when called first time. Then
and anytime when called again parses that tree to decide which tiles on
appropriate level of detail shoud be rendered accordingly to setted pixel
error. Tiles which are inside view frustrum or all of them if frustrum
culling is disabled are rendered during this parsing.
\param action Object with scene graph informations. */
static boolean first_run = true;
public void GLRender(SoGLRenderAction action)
{
  if (!this.shouldGLRender(action))
  {
    return;
  }

  // Get information from scene graph.
  SoState state = action.getState();

  if (first_run)
  {
    first_run = false;

    // Only 3D geometic coordinates and 2D texture coordinates are supported.
    assert(SoCoordinateElement.getInstance(state).is3D() &&
      (SoTextureCoordinateElement.getInstance(state).getDimension() == 2));

    // Check map and tile size values.
    assert(((this.map_size - 1) % (this.tile_size - 1)) == 0);

    // Get texture and geomety coordinates and normals.
    this.coords = SoCoordinateElement.getInstance(state).getArrayPtr3();
    this.texture_coords = SoTextureCoordinateElement.getInstance(state).
      getArrayPtr2();
    this.normals = SoNormalElement.getInstance(state).getArrayPtr();

    // Count tile tree size.
    int tile_count = (this.map_size - 1) / (this.tile_size - 1);
    int level_size = SbBasic.SbSqr(tile_count);
    int tree_size = level_size;
    while (level_size > 1)
    {
      level_size >>= 2;
      tree_size+= level_size;
    }

    // Create tile tree.
    PrProfiler.PR_START_PROFILE("preprocess");
    this.tile_tree = new SbChunkedLoDTileTree(tree_size, this.tile_size);
    initTree(0, new SbBox2s((short)0, (short)0, (short)(this.map_size - 1), (short)(this.map_size - 1)));
    PrProfiler.PR_STOP_PROFILE("preprocess");

    // Init rendering.
    this.is_texture = (SoTextureEnabledElement.get(state) &&
      SoTextureCoordinateElement.getType(state) !=
      SoTextureCoordinateElement.CoordType.NONE);
    this.is_normals = (this.normals != null && SoLightModelElement.get(state) !=
      SoLightModelElement.Model.BASE_COLOR);
  }

  // If is't algorithm freezed, recompute displayed tiles from tree.
  if (!this.is_freeze)
  {
    // Update view volume, viewport.
    this.view_volume.copyFrom( SoViewVolumeElement.get(state));
    this.viewport_region.copyFrom( SoViewportRegionElement.get(state));

    // Recompute distance constant.
    distance_const = (this.view_volume.getNearDist() *
    this.viewport_region.getViewportSizePixels().getValue()[1]) /
    (this.pixel_error * this.view_volume.getHeight());
  }

  // Render tile tree.
  this.beginSolidShape(action);
  final SoMaterialBundle mat_bundle = new SoMaterialBundle(action);
  mat_bundle.sendFirst();
  this.renderTree(action, 0);
  this.endSolidShape(action);
  mat_bundle.destructor();
}

/** Creates terrain geometry triangles.
Creates all triangles from input heightmap in brutal-force manner for
collision detection, ray picking and other purposes.
\param action Object with scene graph informations. */
protected void generatePrimitives(SoAction action)
{
  SoState state = action.getState();
  this.coords = SoCoordinateElement.getInstance(state).getArrayPtr3();
  this.texture_coords = SoTextureCoordinateElement.getInstance(state).
    getArrayPtr2();
  this.normals = SoNormalElement.getInstance(state).getArrayPtr();

  // Brutal-force generation of height map triangles.
  for (int Y = 0; Y < (this.map_size - 1); ++Y)
  {
    beginShape(action, SoShape.TriangleShape.QUAD_STRIP);
    for (int X = 0; X < this.map_size; ++X)
    {
      int index;
      final SoPrimitiveVertex vertex = new SoPrimitiveVertex();

      // First vertex of strip.
      index = ((Y + 1) * this.map_size) + X;
      vertex.setPoint(this.coords[index]);
      vertex.setTextureCoords(this.texture_coords[index]);
      vertex.setNormal(this.normals[index]);
      shapeVertex(vertex);

      // Second vertex of strip.
      index = index - this.map_size;
      vertex.setPoint(this.coords[index]);
      vertex.setTextureCoords(this.texture_coords[index]);
      vertex.setNormal(this.normals[index]);
      shapeVertex(vertex);
      vertex.destructor();
    }
    endShape();
  }
}

/** Computes bounding box and its center.
Computes bounding box of whole terrain from input heightmap and its center
and returns them in \e box and \e center paremeters.
\param action Object with scene graph informations.
\param box Resulting bounding box of terrain.
\param center Center of resulting bounding box of terrain. */
public void computeBBox(SoAction action, final SbBox3f box,
  final SbVec3f center)
{
  // Return bounding box and center of tile tree if exists.
  if (this.tile_tree != null)
  {
    box.copyFrom(this.tile_tree.tiles.operator_square_bracket(0).bounds);
  }
  // Compute bounding box from height map.
  else
  {
    SoState state = action.getState();
    final SoCoordinateElement coords =
      SoCoordinateElement.getInstance(state);
    this.map_size = this.mapSize.getValue();

    // Take two corners to compute.
    SbVec3f min = coords.get3(0);
    SbVec3f max = coords.get3(this.map_size * this.map_size - 1);
    max.setValue(2, (max.getValueRead()[1] - min.getValueRead()[1]) * 0.5f);
    min.setValue(2, -max.getValueRead()[2]);
    box.setBounds(min, max);
  }
  center.copyFrom(box.getCenter());
}

/******************************************************************************
* SoSimpleChunkedLoDTerrain - protected
******************************************************************************/


/** Callback for change of SoSimpleChunkedLoDTerrain::mapSize field.
Sets internal value of SoSimpleChunkedLoDTerrain::mapSize field to
new value when this fields has changed.
\param instance Pointer to affected ::SoSimpleChunkedLoDTerrain class
  instance.
\param sensor Sensor which called this callback. */
protected static void mapSizeChangedCB(Object _instance,
  SoSensor sensor)
{
  // Actualize map size field internal value.
  SoSimpleChunkedLoDTerrain instance =
    (SoSimpleChunkedLoDTerrain)(_instance);
  instance.map_size = instance.mapSize.getValue();
  first_run = true; //YB
  instance.touch(); //YB
}

/** Callback for change of SoSimpleChunkedLoDTerrain::tileSize field.
Sets internal value of SoSimpleChunkedLoDTerrain::tileSize field to
new value when this fields has changed.
\param instance Pointer to affected ::SoSimpleChunkedLoDTerrain class
  instance.
\param sensor Sensor which called this callback. */
protected static void tileSizeChangedCB(Object _instance,
  SoSensor sensor)
{
  // Actualize tile size field internal value.
  SoSimpleChunkedLoDTerrain instance =
    (SoSimpleChunkedLoDTerrain)(_instance);
  instance.tile_size = instance.tileSize.getValue();
}

/** Callback for change of SoSimpleChunkedLoDTerrain::pixelError field.
Sets internal value of SoSimpleChunkedLoDTerrain::pixelError field to
new value when this fields has changed.
\param instance Pointer to affected ::SoSimpleChunkedLoDTerrain class
  instance.
\param sensor Sensor which called this callback. */
protected static void pixelErrorChangedCB(Object _instance,
  SoSensor sensor)
{
  // Actualize pixel error field internal value.
  SoSimpleChunkedLoDTerrain instance =
    (SoSimpleChunkedLoDTerrain)(_instance);
  instance.pixel_error = instance.pixelError.getValue();
}

/** Callback for change of SoSimpleChunkedLoDTerrain::frustrumCulling field.
Sets internal value of SoSimpleChunkedLoDTerrain::frustrumCulling field to
new value when this fields has changed.
\param instance Pointer to affected ::SoSimpleChunkedLoDTerrain class
  instance.
\param sensor Sensor which called this callback. */
protected static void frustrumCullingChangedCB(Object _instance,
  SoSensor sensor)
{
  // Actualize frustrum culling field internal value.
  SoSimpleChunkedLoDTerrain instance =
    (SoSimpleChunkedLoDTerrain)(_instance);
  instance.is_frustrum_culling = instance.frustrumCulling.getValue();
}

/** Callback for change of SoSimpleChunkedLoDTerrain::freeze field.
Sets internal value of SoSimpleChunkedLoDTerrain::freeze field to
new value when this fields has changed.
\param instance Pointer to affected ::SoSimpleChunkedLoDTerrain class
  instance.
\param sensor Sensor which called this callback. */
protected static void freezeChangedCB(Object _instance,
  SoSensor sensor)
{
  // Actualize freeze field internal value.
  SoSimpleChunkedLoDTerrain instance =
    (SoSimpleChunkedLoDTerrain)(_instance);
  instance.is_freeze = instance.freeze.getValue();
}

/******************************************************************************
* SoSimpleGeoMipmapTerrain - private
******************************************************************************/

/* Methods. */
/** Initialises tile quad-tree.
Intializes tile quad-tree in recursive way starting with root or subroot
tile on index \e index with coordinates of input heightmap bounded by
\e coord_box rectangle. It parses tree bottom-up acctually but should be
called with \e index value setted to zero.
\param index Index of root tile, should be setted always to zero.
\param coord_box Bounding rectangle of input heightmap coordinates. */
private SbBox3f initTree( int index, SbBox2s coord_box)
{
  SbChunkedLoDTile tile = this.tile_tree.tiles.operator_square_bracket(index);

  // Recurse to tile's child tiles if not at bottom.
  if (((index << 2 ) + 4) < (this.tile_tree.tree_size))
  {
    // Count indices of succesors.
    int first_index = (index << 2) + 1;
    int second_index = first_index + 1;
    int third_index = second_index + 1;
    int fourth_index = third_index + 1;

    // Get corners and center of tile bounding area.
    final SbVec2s min = coord_box.getMin();
    final SbVec2s max = coord_box.getMax();
    SbVec2s center = new SbVec2s((max.operator_add(min)).operator_div(2));

    // Compute tile bounding box.
    tile.bounds.extendBy(initTree(first_index, new SbBox2s(min.getValue()[0], min.getValue()[1],
      center.getValue()[0], center.getValue()[1])));
    tile.bounds.extendBy(initTree(second_index, new SbBox2s(center.getValue()[0], min.getValue()[1],
      max.getValue()[0], center.getValue()[1])));
    tile.bounds.extendBy(initTree(third_index, new SbBox2s(min.getValue()[0], center.getValue()[1],
      center.getValue()[0], max.getValue()[1])));
    tile.bounds.extendBy(initTree(fourth_index, new SbBox2s(center.getValue()[0], center.getValue()[1],
      max.getValue()[0], max.getValue()[1])));
  }

  // Init this tile and return bounding box.
  this.initTile(tile, index, coord_box);
  return tile.bounds;
}

/** Initialises quad-tree tile.
Initialises quad-tree tile \e tile geometry, static part of error metric
and bounding box. Parametr \e index is index of tile in quad-tree and
\e coord_box parameter is bounding rectangle of input heightmap
coordinates.
\param tile Initialised quad-tree tile.
\param index Index of tile in quad-tree
\param coord_box Bounding rectangle of input heightmap coordinates. */
private void initTile(final SbChunkedLoDTile tile,
  int index, SbBox2s coord_box)
{
  final SbVec2s min = coord_box.getMin();
  final SbVec2s max = coord_box.getMax();
  int min_x = min.getValue()[0];
  int min_y = min.getValue()[1];
  int max_x = max.getValue()[0];
  int max_y = max.getValue()[1];
  int inc_x = (max.getValue()[0] - min.getValue()[0]) / (this.tile_size - 1);
  int inc_y = (max.getValue()[1] - min.getValue()[1]) / (this.tile_size - 1);

  // Simplified intitialization for tiles on bottom level of tile tree.
  if (((index << 2) + 4) >= this.tile_tree.tree_size)
  {
    // Copy every vertex index within coordinates box.
    int vertex_index = 0;
    for (int Y = min_y; Y <= max_y; ++Y)
    {
      for (int X = min_x; X <= max_x; ++X, ++vertex_index)
      {
        int coord_index = Y * this.map_size + X;
        final SbVec3f vertex = this.coords[coord_index];

        tile.bounds.extendBy(vertex);
        tile.vertices.operator_square_bracket(vertex_index, coord_index);
      }
    }

    // Full detailed tile.
    tile.error = (tile.bounds.getMax().getValueRead()[2] - tile.bounds.getMax().getValueRead()[2]) * 0.01f;
  }
  else
  {
    float max_error = 0.0f;
    int half_inc_x = inc_x >> 1;
    int half_inc_y = inc_y >> 1;

    int vertex_index = 0;
    for (int Y = min_y; Y <= max_y; Y+= inc_y)
    {
      for (int X = min_x; X <= max_x; X+= inc_x, ++vertex_index)
      {
        int this_index = Y * this.map_size + X;
        final SbVec3f this_vertex = this.coords[this_index];

        // Don't compute error on borders.
        if ((Y < max_y) && (X < max_x))
        {
          // Horizontal direction.
          final SbVec3f next_h_vertex = this.coords[this_index + inc_x];
          final SbVec3f half_h_vertex = this.coords[this_index +
            half_inc_x];
          float tmp_error = SbBasic.SbAbs(half_h_vertex.getValueRead()[2] - ((this_vertex.getValueRead()[2] +
            next_h_vertex.getValueRead()[2]) * 0.5f));
          max_error = SbBasic.SbMax(max_error, tmp_error);

          // Vertical direction.
          final SbVec3f next_v_vertex = this.coords[this_index + (inc_y *
            this.map_size)];
          final SbVec3f half_v_vertex = this.coords[this_index + (half_inc_y
            * this.map_size)];
          tmp_error = SbBasic.SbAbs(half_v_vertex.getValueRead()[2] - ((this_vertex.getValueRead()[2] + next_v_vertex.getValueRead()[2])
            * 0.5f));
          max_error = SbBasic.SbMax(max_error, tmp_error);

          // Diagonal direction.
          final SbVec3f next_d_vertex = this.coords[this_index + (inc_y *
            this.map_size) + inc_x];
          final SbVec3f half_d_vertex = this.coords[this_index + (half_inc_y *
            this.map_size) + half_inc_x];
          tmp_error = SbBasic.SbAbs(half_d_vertex.getValueRead()[2] - ((this_vertex.getValueRead()[2] + next_d_vertex.getValueRead()[2])
            * 0.5f));
          max_error = SbBasic.SbMax(max_error, tmp_error);
        }

        tile.bounds.extendBy(this_vertex);
        tile.vertices.operator_square_bracket(vertex_index, this_index);
      }
    }

    // Tile error si max error of its own geometry and geomety of all children.
    int child_index = index << 2;
    SbChunkedLoDTile first_child = this.tile_tree.tiles.operator_square_bracket(++child_index);
    tile.error = SbBasic.SbMax(max_error, first_child.error);
    SbChunkedLoDTile second_child = this.tile_tree.tiles.operator_square_bracket(++child_index);
    tile.error = SbBasic.SbMax(tile.error, second_child.error);
    SbChunkedLoDTile third_child = this.tile_tree.tiles.operator_square_bracket(++child_index);
    tile.error = SbBasic.SbMax(tile.error, third_child.error);
    SbChunkedLoDTile fourth_child = this.tile_tree.tiles.operator_square_bracket(++child_index);
    tile.error = SbBasic.SbMax(tile.error, fourth_child.error);
  }
}

/** Renders vertex morphed.
Sends vertex coordinate to rendering depending on \e morph morphing factor.
If morphing factor is equal or lower than zero computes and sends average
vertex of vertices on \e first_index and  \e second_index indices in
\e coords coordinate array. If morphing factor is eqal or greater than one
sends vertex on index \e center_index. It morphs between this two vertices
with morphing factor otherwise.
\param coords Array of heightmap vertices.
\param morph Morphing factor.
\param center_index Index of center vertex.
\param first_index Index of first vertex to theirs average morph.
\param second_index Index of second vertex to theirs average morp. */
private void morphVertex(GL2 gl2, final SbVec3f[] coords,
  float morph, int center_index, int first_index, int second_index)
{
	if(second_index == -1) {
		second_index = first_index; //YB
	}
	
	if(first_index == -1) {
		first_index = second_index; //YB
	}
	
  // Morph between center vertex and average of first and second vertices.
  final SbVec3f tmp_vertex = new SbVec3f(coords[center_index].getValueRead());
  final SbVec3f first_vertex = new SbVec3f(coords[first_index].getValueRead());
  final SbVec3f second_vertex = new SbVec3f(coords[second_index].getValueRead());
  tmp_vertex.setValue(2, (tmp_vertex.getValueRead()[2] * morph) + ((first_vertex.getValueRead()[2] +
    second_vertex.getValueRead()[2]) * 0.5f * (1.0f - morph)));
  gl2.glVertex3fv(tmp_vertex.getValueRead(),0);
}

/** Renders tile skirt.
Renders skirt around tile \e tile to fill gaps between neightbouring tiles
on different level of detail.
\param action Object with scene graph informations.
\param tile Rendered skirt tile. */
private void renderSkirt(SoGLRenderAction action,
  final SbChunkedLoDTile tile)
{
	GL2 gl2 = action.getCacheContext();
	
  int[] vertices = tile.vertices.getArrayPtr();
  int max_x = this.tile_size;
  int max_y = this.tile_size;
  float skirt_height = (tile.bounds.getMax().operator_minus( tile.bounds.getMin())).getValueRead()[2] * 0.2f;

  // Render top skirt strip.
  gl2.glBegin(GL2.GL_QUAD_STRIP);
  for (int X = 0; X < max_x; ++X)
  {
    int index = vertices[X];
    final SbVec3f first_vertex = this.coords[index];
    SbVec3f second_vertex = new SbVec3f(first_vertex);
    second_vertex.getValueRead()[2]-= skirt_height;

    if (this.is_texture)
    {
      gl2.glTexCoord2fv(this.texture_coords[index].getValue(),0);
    }

    gl2.glVertex3fv(first_vertex.getValueRead(),0);
    gl2.glVertex3fv(second_vertex.getValueRead(),0);
  }
  gl2.glEnd();

  // Render bottom skirt strip.
  gl2.glBegin(GL2.GL_QUAD_STRIP);
  for (int X = 0; X < max_x; ++X)
  {
    int index = vertices[(max_y - 1) * max_x + X];
    final SbVec3f first_vertex = this.coords[index];
    SbVec3f second_vertex = new SbVec3f(first_vertex);
    second_vertex.getValueRead()[2]-= skirt_height;

    if (this.is_texture)
    {
      gl2.glTexCoord2fv(this.texture_coords[index].getValue(),0);
    }

    gl2.glVertex3fv(second_vertex.getValueRead(),0);
    gl2.glVertex3fv(first_vertex.getValueRead(),0);
  }
  gl2.glEnd();

  // Render left skirt strip.
  gl2.glBegin(GL2.GL_QUAD_STRIP);
  for (int Y = 0; Y < max_y; ++Y)
  {
    int index = vertices[Y * max_x];
    final SbVec3f first_vertex = this.coords[index];
    SbVec3f second_vertex = new SbVec3f(first_vertex);
    second_vertex.getValueRead()[2]-= skirt_height;

    if (this.is_texture)
    {
      gl2.glTexCoord2fv(this.texture_coords[index].getValue(),0);
    }

    gl2.glVertex3fv(second_vertex.getValueRead(),0);
    gl2.glVertex3fv(first_vertex.getValueRead(),0);
  }
  gl2.glEnd();

  // Render right skirt strip.
  gl2.glBegin(GL2.GL_QUAD_STRIP);
  for (int Y = 0; Y < max_y; ++Y)
  {
    int index = vertices[Y * max_x + (max_x - 1)];
    final SbVec3f first_vertex = this.coords[index];
    SbVec3f second_vertex = new SbVec3f(first_vertex);
    second_vertex.getValueRead()[2]-= skirt_height;

    if (this.is_texture)
    {
      gl2.glTexCoord2fv(this.texture_coords[index].getValue(),0);
    }

    gl2.glVertex3fv(first_vertex.getValueRead(),0);
    gl2.glVertex3fv(second_vertex.getValueRead(),0);
  }
  gl2.glEnd();
}

/** Renders tile skirt (morphed version).
Renders skirt around tile \e tile with morphing factor \e morph to fill
gaps between neightbouring tiles on different level of detail.
\param action Object with scene graph informations.
\param tile Rendered skirt tile.
\param morph Morphing factor of rendering. */
private void renderSkirt(SoGLRenderAction action,
  final SbChunkedLoDTile tile, float morph)
{
	GL2 gl2 = action.getCacheContext();
	
  final int[] vertices = tile.vertices.getArrayPtr();
  int max_x = this.tile_size;
  int max_y = this.tile_size;
  float skirt_height = (tile.bounds.getMax().operator_minus( tile.bounds.getMin())).getValueRead()[2] * 0.2f;

  // Render top skirt strip.
  gl2.glBegin(GL2.GL_QUAD_STRIP);
  for (int X = 0; X < max_x; ++X)
  {
    int index = X;
    int vertex_index = vertices[index];
    final SbVec3f first_vertex = this.coords[vertex_index];
    SbVec3f second_vertex = new SbVec3f(first_vertex);
    second_vertex.getValueRead()[2]-= skirt_height;

    if (this.is_texture)
    {
      gl2.glTexCoord2fv(this.texture_coords[vertex_index].getValue(),0);
    }

    if ((X % 2)!=0)
    {
      // Morph between left and right vertices.
      this.morphVertex(gl2,this.coords, morph, vertex_index, vertex_index - 1,
        vertex_index + 1);
    }
    else
    {
      gl2.glVertex3fv(first_vertex.getValueRead(),0);
    }
    gl2.glVertex3fv(second_vertex.getValueRead(),0);
  }
  gl2.glEnd();

  // Render bottom skirt strip.
  gl2.glBegin(GL2.GL_QUAD_STRIP);
  for (int X = 0; X < max_x; ++X)
  {
    int index = (max_y - 1) * max_x + X;
    int vertex_index = vertices[index];
    final SbVec3f first_vertex = this.coords[vertex_index];
    SbVec3f second_vertex = new SbVec3f(first_vertex);
    second_vertex.getValueRead()[2]-= skirt_height;

    if (this.is_texture)
    {
      gl2.glTexCoord2fv(this.texture_coords[vertex_index].getValue(),0);
    }

    gl2.glVertex3fv(second_vertex.getValueRead(),0);
    if ((X % 2)!=0)
    {
      // Morph between left and right vertices.
      this.morphVertex(gl2,this.coords, morph, vertex_index, vertex_index - 1,
        vertex_index + 1);
    }
    else
    {
      gl2.glVertex3fv(first_vertex.getValueRead(),0);
    }
  }
  gl2.glEnd();

  // Render left skirt strip.
  gl2.glBegin(GL2.GL_QUAD_STRIP);
  for (int Y = 0; Y < max_y; ++Y)
  {
    int index = Y * max_x;
    int vertex_index = vertices[index];
    final SbVec3f first_vertex = this.coords[vertex_index];
    SbVec3f second_vertex = new SbVec3f(first_vertex);
    second_vertex.getValueRead()[2]-= skirt_height;

    if (this.is_texture)
    {
      gl2.glTexCoord2fv(this.texture_coords[vertex_index].getValue(),0);
    }

    gl2.glVertex3fv(second_vertex.getValueRead(),0);
    if ((Y % 2)!=0)
    {
      // Morph between previous and next vertices.
      int prev_index = index - max_x;
      int next_index = index + max_x;
      this.morphVertex(gl2,this.coords, morph, vertex_index,
        vertices[prev_index], next_index < vertices.length ? vertices[next_index] : vertices[prev_index]); //YB
    }
    else
    {
      gl2.glVertex3fv(first_vertex.getValueRead(),0);
    }
  }
  gl2.glEnd();

  // Render right skirt strip.
  gl2.glBegin(GL2.GL_QUAD_STRIP);
  for (int Y = 0; Y < max_y; ++Y)
  {
    int index = Y * max_x + (max_x - 1);
    int vertex_index = vertices[index];
    final SbVec3f first_vertex = this.coords[vertex_index];
    SbVec3f second_vertex = new SbVec3f(first_vertex);
    second_vertex.getValueRead()[2]-= skirt_height;

    if (this.is_texture)
    {
      gl2.glTexCoord2fv(this.texture_coords[vertex_index].getValue(),0);
    }

    if ((Y % 2)!=0)
    {
      // Morph between previous and next vertices.
      int prev_index = index - max_x;
      int next_index = index + max_x;
      this.morphVertex(gl2,this.coords, morph, vertex_index,
        vertices[prev_index], next_index < vertices.length ? vertices[next_index] : vertices[prev_index]); //YB
    }
    else
    {
      gl2.glVertex3fv(first_vertex.getValueRead(),0);
    }
    gl2.glVertex3fv(second_vertex.getValueRead(),0);
  }
  gl2.glEnd();
}

/** Renders tile.
Renders \e tile tile geometry.
\param action Object with scene graph informations.
\param tile Rendered tile. */
private void renderTile(SoGLRenderAction action,
  final SbChunkedLoDTile tile)
{
	GL2 gl2 = action.getCacheContext();
	
  final int[] vertices = tile.vertices.getArrayPtr();
  int max_x = this.tile_size;
  int max_y = this.tile_size;

  // Render tile geometry
  for (int Y = 0; Y < (max_y - 1); ++Y)
  {
    gl2.glBegin(GL2.GL_QUAD_STRIP);
    for (int X = 0; X < max_x; ++X)
    {
      int top_index = vertices[(Y * max_x) + X];
      int bottom_index = vertices[((Y + 1) * max_x) + X];

      // Render bottom vertices of strip.
      if (this.is_texture)
      {
        gl2.glTexCoord2fv(this.texture_coords[bottom_index].getValue(),0);
      }
      if (this.is_normals)
      {
        gl2.glNormal3fv(this.normals[bottom_index].getValueRead(),0);
      }
      gl2.glVertex3fv(this.coords[bottom_index].getValueRead(),0);

      // Render current vertices of strip.
      if (this.is_texture)
      {
        gl2.glTexCoord2fv(this.texture_coords[top_index].getValue(),0);
      }
      if (this.is_normals)
      {
        gl2.glNormal3fv(this.normals[top_index].getValueRead(),0);
      }
      gl2.glVertex3fv(this.coords[top_index].getValueRead(),0);
    }
    gl2.glEnd();
  }
}

/** Renders tile (morphed version).
Renders \e tile tile geometry with morphing factor \e morph.
\param action Object with scene graph informations.
\param tile Rendered tile.
\param morph Morphing factor of rendering. */
private void renderTile(SoGLRenderAction action,
  final SbChunkedLoDTile tile, float morph)
{
	GL2 gl2 = action.getCacheContext();
	
  int[] vertices = tile.vertices.getArrayPtr();
  int max_x = this.tile_size;
  int max_y = this.tile_size;

  // Render tile geometry
  for (int Y = 0; Y < (max_y - 1); ++Y)
  {
    gl2.glBegin(GL2.GL_QUAD_STRIP);
    for (int X = 0; X < max_x; ++X)
    {
    	int i = ((Y - 1) * max_x) + X; int prev_index = i>0 ? vertices[i] : -1; //YB
      int top_index = vertices[(Y * max_x) + X];
      int bottom_index = vertices[((Y + 1) * max_x) + X];
      i = ((Y + 2) * max_x) + X; int next_index = i<vertices.length ? vertices[i] : -1; //YB

      // Render bottom vertices of strip.
      if (this.is_texture)
      {
        gl2.glTexCoord2fv(this.texture_coords[bottom_index].getValue(),0);
      }
      if (this.is_normals)
      {
        gl2.glNormal3fv(this.normals[bottom_index].getValueRead(),0);
      }

      // Lichy radek?
      if ((Y % 2)!=0)
      {
        // Lichy slopec licheho radku?
        if ((X % 2)!=0)
        {
          // Morph between left and right vertices.
          this.morphVertex(gl2,this.coords, morph, bottom_index,
            bottom_index - 1, bottom_index + 1);
        }
        // Sudy slopec licheho radku.
        else
        {
          gl2.glVertex3fv(this.coords[bottom_index].getValueRead(),0);
        }
      }
      // Sudy radek.
      else
      {
        // Lichy slopec sudeho radku?
        if ((X % 2)!=0)
        {
          // Morph between top-left and bottom-right vertices.
          this.morphVertex(gl2,this.coords, morph, bottom_index,
            top_index - 1, next_index + 1);
        }
        // Sudy slopec sudeho radku.
        else
        {
          // Morph between top and bottom vertices.
            this.morphVertex(gl2,this.coords, morph, bottom_index, top_index,
            next_index);
        }
      }

      // Render current vertices of strip.
      if (this.is_texture)
      {
        gl2.glTexCoord2fv(this.texture_coords[top_index].getValue(),0);
      }
      if (this.is_normals)
      {
        gl2.glNormal3fv(this.normals[top_index].getValueRead(),0);
      }

      // Lichy radek?
      if ((Y % 2)!=0)
      {
        // Lichy slopec licheho radku?
        if ((X % 2)!=0)
        {
          // Morph between top-left and bottom-right vertices.
          this.morphVertex(gl2,this.coords, morph, top_index, prev_index - 1,
            bottom_index + 1);
        }
        // Sudy slopec licheho radku.
        else
        {
          // Morph between top and bottom vertices.
          this.morphVertex(gl2,this.coords, morph, top_index, prev_index,
            bottom_index);
        }
      }
      // Sudy radek.
      else
      {
        // Lichy slopec sudeho radku?
        if ((X % 2)!=0)
        {
          // Morph between left and right vertices.
          this.morphVertex(gl2,this.coords, morph, top_index, top_index - 1,
            top_index + 1);
        }
        // Sudy slopec sudeho radku.
        else
        {
          gl2.glVertex3fv(this.coords[top_index].getValueRead(),0);
        }
      }
    }
    gl2.glEnd();
  }
}

/** Renders tile tree.
Renders whole terrain on appropriate level of detail starting with root
tile on index \e index in tile quad-tree. This index should be always setted
to zere when called not recursively.
\param action Object with scene graph informations.
\param index Index of root tile, should be always setted to zero. */
private void renderTree(SoGLRenderAction action,
  int index)
{
  // Compute distance from camera to tile.
  final SbChunkedLoDTile tile = tile_tree.tiles.operator_square_bracket(index);
  final SbVec3f camera_position = new SbVec3f(this.view_volume.getProjectionPoint());
  float distance = (tile.bounds.getCenter().operator_minus(camera_position)).sqrLength();

  // Recurse if tile isn't fine enough and tile isn't at bottom level of tree.
  if ((((index << 2) + 4) < (this.tile_tree.tree_size)) &&
    (distance < SbBasic.SbSqr(tile.error * distance_const)))
  {
    int first_index = (index << 2) + 1;
    int second_index = first_index + 1;
    int third_index = second_index + 1;
    int fourth_index = third_index + 1;

    this.renderTree(action, first_index);
    this.renderTree(action, second_index);
    this.renderTree(action, third_index);
    this.renderTree(action, fourth_index);
  }
  else
  {
    // Render tile if is in view volume or frustrum culling is disabled.
    if (!this.is_frustrum_culling || this.view_volume.intersect(tile.bounds))
    {
      distance = (float)Math.sqrt(distance);
      float morph = (distance_const * tile.error) / distance;
      morph = SbBasic.SbClamp(2.0f * ((2.0f * morph) - 1.0f), 0.0f, 1.0f);

      //Debug(morph);

      if (morph < 1.0f)
      {
        this.renderTile(action, tile, morph);
        this.renderSkirt(action, tile, morph);
      }
      else
      {
        this.renderTile(action, tile);
        this.renderSkirt(action, tile);
      }
    }
  }
}

/** Destructor.
 Privatised because Coin handles nodes memory frees itself. */
public void destructor()
{
  // Free allocated memory.
  Destroyable.delete(this.tile_tree);
  Destroyable.delete(this.map_size_sensor);
  Destroyable.delete(this.tile_size_sensor);
  Destroyable.delete(this.pixel_error_sensor);
  Destroyable.delete(this.frustrum_culling_sensor);
  Destroyable.delete(this.freeze_sensor);
}
	
}
