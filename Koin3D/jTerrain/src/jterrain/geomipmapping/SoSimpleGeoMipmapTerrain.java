
///////////////////////////////////////////////////////////////////////////////
//  SoTerrain
///////////////////////////////////////////////////////////////////////////////
/// Ter� vykreslovan algoritmem Geo Mip-Mapping.
/// \file SoSimpleGeoMipmapTerrain.h
/// \author Radek Barto�- xbarto33
/// \date 29.01.2006
///
/// Uzel grafu sc�y reprezentuj��ter� vykreslovan algoritmem Geo
/// Mip-Mapping. Pro pouit�je teba uzlu pedadit uzel s koordin�y typu
/// SoCordinate3 obsahuj��vkovou mapu ter�u a nastavit jej�rozm�y a
/// rozm�y dladic generovanch z t�o vkov�mapy Velikost vkov�mapy mus�
/// bt �vercov�o velikosti strany 2^n + 1, kde n je cel�kladn��slo.
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

package jterrain.geomipmapping;

import com.jogamp.opengl.GL2;

import jscenegraph.coin3d.inventor.elements.SoMultiTextureCoordinateElement;
import jscenegraph.coin3d.inventor.elements.SoMultiTextureEnabledElement;
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
import jscenegraph.database.inventor.elements.SoNormalBindingElement;
import jscenegraph.database.inventor.elements.SoNormalElement;
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
import jscenegraph.port.Array;
import jscenegraph.port.Ctx;
import jscenegraph.port.Destroyable;
import jscenegraph.port.SbVec2fArray;
import jscenegraph.port.SbVec3fArray;
import jterrain.profiler.PrProfiler;
import roam.SoSimpleROAMTerrain;

/**
 * @author Yves Boyadjian
 *
 */
public class SoSimpleGeoMipmapTerrain extends SoShape {
	
	  //SO_NODE_HEADER(SoSimpleGeoMipmapTerrain);
		private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoSimpleGeoMipmapTerrain.class,this);
		   
		   public                                                                     
		    static SoType       getClassTypeId()        /* Returns class type id */   
		                                    { return SoSubNode.getClassTypeId(SoSimpleGeoMipmapTerrain.class);  }                   
		  public  SoType      getTypeId()      /* Returns type id      */
		  {
			  return nodeHeader.getClassTypeId();
		  }
		  public                                                                  
		    SoFieldData   getFieldData()  {
			  return nodeHeader.getFieldData();
		  }
		  public  static SoFieldData[] getFieldDataPtr()                              
		        { return SoSubNode.getFieldDataPtr(SoSimpleGeoMipmapTerrain.class); }    	  	
		// End SO_NODE_HEADER
	  
	
    /* Pole. */
    /// Velikost strany vstupn�vkov�mapy.
    public final SoSFInt32 mapSize = new SoSFInt32();
    /// Velikost strany dladic.
    public final SoSFInt32 tileSize = new SoSFInt32();
    /// Chyba zobrazen�v pixelech.
    public final SoSFInt32 pixelError = new SoSFInt32();
    /// P�nak oez���pohledovm t�esem.
    public final SoSFBool frustrumCulling = new SoSFBool();
    /// P�nak "zmrazen� vykreslov��ter�u.
    public final SoSFBool freeze = new SoSFBool();
    
    /* Zkratky elementu. */
    /// Body vykov�mapy.
    SbVec3fArray coords;
    /// Texturov�souadnice pro body vkov�mapy.
    SbVec2fArray texture_coords;
    /// Norm�y.
    SbVec3fArray normals;
    /// Pohledov�t�eso.
    SbViewVolume view_volume; //ptr
    /// Vykreslovac�okno.
    SbViewportRegion viewport_region; //ptr
    /* Datove polozky. */
    /// Kvadrantov strom dladic.
    SbGeoMipmapTileTree tile_tree;
    /// Konstanta pro vpo�t dynamick��sti chybov�metriky.
    float distance_const;
    /// P�nak pouit�textury.
    boolean is_texture;
    /// P�nak pouit�morm�.
    boolean is_normals;
    /* Interni pole. */
    /// Velikost strany vstupn�vkov�mapy.
    int map_size;
    /// Velikost strany dladic.
    int tile_size;
    /// Chyba zobrazen�v pixelech.
    int pixel_error;
    /// P�nak oez���pohledovm t�esem.
    boolean is_frustrum_culling;
    /// P�nak "zmrazen� vykreslov��ter�u.
    boolean is_freeze;
    /* Sensory. */
    /// Senzor pole \p mapSize.
    SoFieldSensor map_size_sensor;
    /// Senzor pole \p tileSize.
    SoFieldSensor tile_size_sensor;
    /// Senzor pole \p pixelError.
    SoFieldSensor pixel_error_sensor;
    /// Senzor pole \p frustrumCulling.
    SoFieldSensor frustrum_culling_sensor;
    /// Senzor pole \p freeze.
    SoFieldSensor freeze_sensor;
    
	
public static void initClass()
{
  /* Inicializace tridy. */
  SoSubNode.SO_NODE_INIT_CLASS(SoSimpleGeoMipmapTerrain.class, SoShape.class, "Shape");
  SO_ENABLE(SoGLRenderAction.class, SoCoordinateElement.class);
  SO_ENABLE(SoGLRenderAction.class, SoMultiTextureCoordinateElement.class);
  SO_ENABLE(SoGLRenderAction.class, SoMultiTextureEnabledElement.class);
  SO_ENABLE(SoGLRenderAction.class, SoLightModelElement.class);
  SO_ENABLE(SoGLRenderAction.class, SoNormalElement.class);
  SO_ENABLE(SoGLRenderAction.class, SoNormalBindingElement.class);
  SO_ENABLE(SoGLRenderAction.class, SoViewVolumeElement.class);
  SO_ENABLE(SoGLRenderAction.class, SoViewportRegionElement.class);
  SO_ENABLE(SoGetBoundingBoxAction.class, SoCoordinateElement.class);
}

public SoSimpleGeoMipmapTerrain() {
  coords =null; texture_coords =null; normals =null; view_volume =null;
  viewport_region =null; tile_tree=null; distance_const=0.0f;
  is_texture=false; is_normals=false;
  map_size=2; tile_size=2; pixel_error=DEFAULT_PIXEL_ERROR;
  is_frustrum_culling=true; is_freeze=false;
  map_size_sensor=null; tile_size_sensor=null; pixel_error_sensor=null;
  frustrum_culling_sensor=null; freeze_sensor=null;

  /* Inicializace tridy. */
  nodeHeader.SO_NODE_CONSTRUCTOR(SoSimpleGeoMipmapTerrain.class);

  /* Inicializace poli */
  nodeHeader.SO_NODE_ADD_FIELD(mapSize,"mapSize", (2));
  nodeHeader.SO_NODE_ADD_FIELD(tileSize,"tileSize", (2));
  nodeHeader.SO_NODE_ADD_FIELD(pixelError,"pixelError", (DEFAULT_PIXEL_ERROR));
  nodeHeader.SO_NODE_ADD_FIELD(frustrumCulling,"frustrumCulling", (true));
  nodeHeader.SO_NODE_ADD_FIELD(freeze,"freeze", (false));

  /* Vytvoreni senzoru. */
  map_size_sensor = new SoFieldSensor(SoSimpleGeoMipmapTerrain::mapSizeChangedCB, this);
  tile_size_sensor = new SoFieldSensor(SoSimpleGeoMipmapTerrain::tileSizeChangedCB, this);
  pixel_error_sensor = new SoFieldSensor(SoSimpleGeoMipmapTerrain::pixelErrorChangedCB, this);
  frustrum_culling_sensor = new SoFieldSensor(SoSimpleGeoMipmapTerrain::frustrumCullingChangedCB, this);
  freeze_sensor = new SoFieldSensor(SoSimpleGeoMipmapTerrain::freezeChangedCB, this);

  /* Napojeni senzoru na pole */
  map_size_sensor.attach(mapSize);
  tile_size_sensor.attach(tileSize);
  pixel_error_sensor.attach(pixelError);
  frustrum_culling_sensor.attach(frustrumCulling);
  freeze_sensor.attach(freeze);
}

/******************************************************************************
* SoSimpleGeoMipmapTerrain - protected
******************************************************************************/

/* Staticke konstanty. */
final static int DEFAULT_PIXEL_ERROR = 20;

static boolean first_run = true;

public void GLRender(SoGLRenderAction action)
{
  if (!shouldGLRender(action))
  {
    return;
  }

  /* Ziskani informaci z grafu sceny. */
  SoState state = action.getState();
  view_volume = SoViewVolumeElement.get(state);
  viewport_region = SoViewportRegionElement.get(state);

  /* Pri prvnim prubehu se vygeneruje dlazdice */
  if (first_run)
  {
    PrProfiler.PR_START_PROFILE("preprocess");

    first_run = false;

    /* Pracujeme pouze s tridimenzionalnimi geometrickymi souradnicemi
    a dvoudimenzionalnimi texturovymi souradnicemi. */
    assert(SoCoordinateElement.getInstance(state).is3D() &&
      (SoMultiTextureCoordinateElement.getInstance(state).getDimension(0) == 2));

    /* Ziskani vrcholu a texturovych souradnic. */
    coords = SoCoordinateElement.getInstance(state).getArrayPtr3();
    texture_coords = SoMultiTextureCoordinateElement.getInstance(state).
      getArrayPtr2(0);
    normals = SoNormalElement.getInstance(state).getArrayPtr();

    /* Kontrlola velikosti mapy a dlazdice. */
    assert(((map_size - 1) % (tile_size - 1)) == 0);

    /* Celkovy pocet dlazdic. */
    int tile_count = (map_size - 1) / (tile_size - 1);
    tile_count = SbBasic.SbSqr(tile_count);

    /* Vytvoreni stromu dlazdic. */
    tile_tree = new SbGeoMipmapTileTree(tile_count, tile_size);
    initTree(0, new SbBox2s((short)0, (short)0, (short)(map_size - 1), (short)(map_size - 1)));
    PrProfiler.PR_STOP_PROFILE("preprocess");
  }

  /* Neni-li algoritmus vypnut provedeme vyber urovni dlazdic a frustrum
  culling. */
  if (!is_freeze)
  {
    /* Vypocet konstanty pro vypocet vzdalenosti pro zvoleni dane urovne
    dlazdice. */
    distance_const = (view_volume.getNearDist() *
      viewport_region.getViewportSizePixels().getValue()[1]) /
      (pixel_error * view_volume.getHeight());

    recomputeTree(0, true);
  }

  /* Inicializace vykreslovani. */
  beginSolidShape(action);
  SoNormalBindingElement.Binding norm_bind =
    SoNormalBindingElement.get(state);
  final SoMaterialBundle mat_bundle = new SoMaterialBundle(action);

  this.is_texture = (SoMultiTextureEnabledElement.get(state,0) &&
    SoMultiTextureCoordinateElement.getType(state,0) !=
    SoMultiTextureCoordinateElement.CoordType.NONE_TEXGEN);
  this.is_normals = (this.normals != null && SoLightModelElement.get(state) !=
    SoLightModelElement.Model.BASE_COLOR);

  mat_bundle.sendFirst();
  
  GL2 gl2 = Ctx.get(action.getCacheContext());

  if (!is_normals)
  {
    norm_bind = SoNormalBindingElement.Binding.OVERALL;
  }

  if (norm_bind == SoNormalBindingElement.Binding.OVERALL)
  {
    gl2.glNormal3f(0.0f, 0.0f, 1.0f);
  }

  renderTree(action, 0);
  endSolidShape(action);
  mat_bundle.destructor();
}

public final void SEND_VERTEX(final SoPrimitiveVertex vertex, int ind) { int index = (ind); 
   vertex.setPoint(coords.get(index)); 
   vertex.setTextureCoords(texture_coords.get(index)); 
   vertex.setNormal(normals.get(index)); 
   shapeVertex(vertex);
}
public void generatePrimitives(SoAction action)
{
  final SoPrimitiveVertex vertex = new SoPrimitiveVertex();
  int index;

  SoState state = action.getState();
  coords = SoCoordinateElement.getInstance(state).getArrayPtr3();
  texture_coords = SoMultiTextureCoordinateElement.getInstance(state).
    getArrayPtr2(0);
  normals = SoNormalElement.getInstance(state).getArrayPtr();

  /* Brutal-force vygenerovani triangle-stripu vyskove mapy. */
  for (int Y = 0; Y < (map_size - 1); ++Y)
  {
    beginShape(action, SoShape.TriangleShape.QUAD_STRIP);

    /* Prvni dva vrcholy pasu. */
    SEND_VERTEX(vertex,(Y + 1) * map_size);
    SEND_VERTEX(vertex,Y * map_size);

    for (int X = 1; X < map_size; ++X)
    {
      /* Dalsi vrcholy pasu. */
      SEND_VERTEX(vertex,((Y + 1)  * map_size) + X);
      SEND_VERTEX(vertex,(Y * map_size) + X);
    }
    endShape();
  }
  vertex.destructor();
}

public void computeBBox(SoAction action, final SbBox3f box,
  final SbVec3f center)
{
  /* Vypocet ohranicujiciho kvadru a jeho stredu. */
  if (tile_tree != null)
  {
    box.copyFrom(tile_tree.tiles.get(0).bounds);
  }
  /* Ohraniceni neni jeste spocitano v preprocesingu. */
  else
  {
    SoState state = action.getState();
    final SoCoordinateElement coords = SoCoordinateElement.getInstance(state);
    int map_size = mapSize.getValue();

    /* Vypocet ohraniceni podle dvou rohu vyskove mapy. */
    SbVec3f min = coords.get3(0);
    SbVec3f max = coords.get3(map_size * map_size - 1);
    max.setValue(2, (max.getValueRead()[1] - min.getValueRead()[1]) * 0.5f);
    min.setValue(2, -max.getValueRead()[2]);
    box.setBounds(min, max);
  }
  center.copyFrom(box.getCenter());
}

public SbBox3f initTree( int index, SbBox2s coord_box)
{
  SbGeoMipmapTile tile = tile_tree.tiles.get(index);

  if (index >= tile_tree.bottom_start)
  {
    /* Inicializace dlazdice na nejnizsi urovni. */
    initTile(tile, coord_box);
  }
  else
  {
    /* Vypocet indexu potomku. */
    int first_index = (index << 2) + 1;
    int second_index = first_index + 1;
    int third_index = second_index + 1;
    int fourth_index = third_index + 1;

    /* Zjisteni ohraniceni souradnic dlazdice. */
    SbVec2s min = coord_box.getMin();
    SbVec2s max = coord_box.getMax();
    SbVec2s center = new SbVec2s((max.operator_add(min)).operator_div(2));

    Array<SbGeoMipmapTile> tiles = tile_tree.tiles;

    /* Inicializace odkazu na sousedy prvni dlazdice. */
    tiles.get(first_index).left = (tile.left == null) ? null :
      tiles.get((Array.minus(tile.left, tiles) << 2) + 2);
    tiles.get(first_index).right = tiles.get(second_index);
    tiles.get(first_index).top = (tile.top == null) ? null :
      (tiles.get((Array.minus(tile.top , tiles) << 2) + 3));
    tiles.get(first_index).bottom = tiles.get(third_index);

    /* Inicializace odkazu na sousedy druhe dlazdice. */
    tiles.get(second_index).left = tiles.get(first_index);
    tiles.get(second_index).right = (tile.right == null) ? null :
      (tiles.get((Array.minus(tile.right , tiles) << 2) + 1));
    tiles.get(second_index).top = (tile.top == null) ? null :
      (tiles.get((Array.minus(tile.top , tiles) << 2) + 4));
    tiles.get(second_index).bottom = tiles.get(fourth_index);

    /* Inicializace odkazu na sousedy treti dlazdice. */
    tiles.get(third_index).left = (tile.left == null) ? null :
      (tiles.get((Array.minus(tile.left , tiles) << 2) + 4));
    tiles.get(third_index).right = tiles.get(fourth_index);
    tiles.get(third_index).top = tiles.get(first_index);
    tiles.get(third_index).bottom = (tile.bottom == null) ? null :
      (tiles.get((Array.minus(tile.bottom , tiles) << 2) + 1));

    /* Inicializace odkazu na sousedy ctvrte dlazdice. */
    tiles.get(fourth_index).left = tiles.get(third_index);
    tiles.get(fourth_index).right = (tile.right == null) ? null :
      (tiles.get((Array.minus(tile.right , tiles) << 2) + 3));
    tiles.get(fourth_index).top = tiles.get(second_index);
    tiles.get(fourth_index).bottom = (tile.bottom == null) ? null :
      tiles.get((Array.minus(tile.bottom , tiles) << 2) + 2);

    /* Rekurzivni inicializace stromu a vypocet ohraniceni dlazdice. */
    tile.bounds.extendBy(initTree(first_index, new SbBox2s(min.getValue()[0], min.getValue()[1],
      center.getValue()[0], center.getValue()[1])));
    tile.bounds.extendBy(initTree(second_index, new SbBox2s(center.getValue()[0], min.getValue()[1],
      max.getValue()[0], center.getValue()[1])));
    tile.bounds.extendBy(initTree(third_index, new SbBox2s(min.getValue()[0], center.getValue()[1],
      center.getValue()[0], max.getValue()[1])));
    tile.bounds.extendBy(initTree(fourth_index, new SbBox2s(center.getValue()[0], center.getValue()[1],
      max.getValue()[0], max.getValue()[1])));
  }

  return tile.bounds;
}

public void initTile(final SbGeoMipmapTile tile,
  final SbBox2s coord_box)
{
  /* Alokace vsech urovni detailu dlazdice. */
  tile.levels = new Array<>(SbGeoMipmapTileLevel.class,new SbGeoMipmapTileLevel[tile_tree.level_count]);

  /* Ziskani souradnic dlazdice ve vyskove mape */
  final SbVec2s min = coord_box.getMin();
  final SbVec2s max = coord_box.getMax();

  /* Alokace a inicializace nejlepsi urovne detailu dlazdice. */
  tile.levels.get(0).vertices = new int[SbBasic.SbSqr(tile_tree.level_sizes[0])];
  int index = 0;
  for (int Y = min.getValue()[1]; Y <= max.getValue()[1]; ++Y)
  {
    for (int X = min.getValue()[0]; X <= max.getValue()[0]; ++X, ++index)
    {
      int coord_index = Y * map_size + X;
      final SbVec3f vertex = coords.get(coord_index);

      /* Vypocet ohraniceni dlazdice a zapis indexu vrcholu do dlazdice. */
      tile.bounds.extendBy(vertex);
      tile.levels.get(0).vertices[index] = coord_index;
    }
  }

  tile.levels.get(0).error = 0.0f;

  /* Inicializace ostatnich urovni podle predchozich. */
  for (int I = 1; I < tile_tree.level_count; ++I)
  {
    int level_size = tile_tree.level_sizes[I];
    tile.levels.get(I).vertices = new int[SbBasic.SbSqr(level_size)];
    initLevel(tile.levels.get(I), tile.levels.get(I - 1), level_size);
  }

  /* Vypocet stredu dlazdice. */
  tile.center.copyFrom(tile.bounds.getCenter());
}

public void initLevel(final SbGeoMipmapTileLevel level,
  final SbGeoMipmapTileLevel parent, final int level_size)
{
  int parent_size = (level_size << 1) - 1;
  float max_error = 0.0f;

  for (int Y = 0; Y < parent_size; ++Y)
  {
    for (int X = 0; X < parent_size; ++X)
    {
      /* U lichych indexu se spocita chyba. */
      if ((X & 0x01)!=0 || (Y & 0x01)!=0)
      {
        /* Chyba v hornim a dolnim radku se spocita horizontalne. */
        if ((Y == 0) || (Y == (parent_size - 1)))
        {
          int index = (Y * parent_size) + X;
          float tmp_error = SbBasic.SbAbs(coords.get(parent.vertices[index]).getValueRead()[2] -
            ((coords.get(parent.vertices[index - 1]).getValueRead()[2] +
            coords.get(parent.vertices[index + 1]).getValueRead()[2]) * 0.5f));
          max_error = SbBasic.SbMax(max_error, tmp_error);
        }
        /* Chyba v levem a pravem sloupci se spocita vertikalne. */
        else if ((X == 0) || (X == (parent_size - 1)))
        {
          int index = (Y * parent_size) + X;
          float tmp_error = SbBasic.SbAbs(coords.get(parent.vertices[index]).getValueRead()[2] -
            ((coords.get(parent.vertices[index - parent_size]).getValueRead()[2] +
            coords.get(parent.vertices[index + parent_size]).getValueRead()[2]) * 0.5f));
          max_error = SbBasic.SbMax(max_error, tmp_error);
        }
        /* Uprostred dlazdice se chyba spocita podle prave diagonaly. */
        else
        {
          int index = (Y * parent_size) + X;
          float tmp_error = SbBasic.SbAbs(coords.get(parent.vertices[index]).getValueRead()[2] -
            ((coords.get(parent.vertices[index - parent_size + 1]).getValueRead()[2] +
            coords.get(parent.vertices[index + parent_size - 1]).getValueRead()[2]) * 0.5f));
          max_error = SbBasic.SbMax(max_error, tmp_error);
        }
      }
      /* Sude indexy se zaradi do triangulace. */
      else
      {
        level.vertices[((Y >> 1) * level_size) + (X >> 1)] = parent.vertices[
          (Y * parent_size) + X];
      }
    }
  }

  /* Ulozeni chyby a vypocet vzdalenosti pro zobrazeni urovne. */
  level.error = parent.error + max_error;
}

public void recomputeTree( int index,
  boolean render_parent)
{
  final SbGeoMipmapTile tile = tile_tree.tiles.get(index);

  /* Neni-li dlazdice pouze virtualni dlazdici */
  if (tile.levels != null)
  {
    /* Je-li dlazdice v pohledu kamery. */
    if (!is_frustrum_culling || (render_parent &&
      view_volume.intersect(tile.bounds)))
    {
      tile.level = pickLevel(tile);
    }
    else
    {
      tile.level = SbGeoMipmapTile.LEVEL_NONE;
    }
  }
  /* Dlazdice je virtualni. */
  else
  {
    /* Je-li dlazdice v pohledu kamery. */
    if (!is_frustrum_culling || (render_parent &&
      view_volume.intersect(tile.bounds)))
    {
      tile.level = 0;
    }
    else
    {
      tile.level = SbGeoMipmapTile.LEVEL_NONE;
    }

    /* Vypocet indexu potomku. */
    int first_index = (index << 2) + 1;
    int second_index = first_index + 1;
    int third_index = second_index + 1;
    int fourth_index = third_index + 1;

    /* Vykresleni potomku dlazdice. */
    recomputeTree(first_index, tile.level==0);
    recomputeTree(second_index, tile.level==0);
    recomputeTree(third_index, tile.level==0);
    recomputeTree(fourth_index, tile.level==0);
  }
}

public final void GL_SEND_VERTEX(GL2 gl2, int index) { int vertex_index = index; 
  if (is_texture) 
    gl2.glTexCoord2fv(texture_coords.get(vertex_index).getValueRead(),0); 
  if (is_normals) 
    gl2.glNormal3fv(normals.get(vertex_index).getValueRead(),0); 
  gl2.glVertex3fv(coords.get(vertex_index).getValueRead(),0);
}
public void renderTree(SoAction action,  int index)
{
	GL2 gl2 = action.getState().getGL2(); //java port
	
  final SbGeoMipmapTile tile = tile_tree.tiles.get(index);

  /* Ma-li se dlazdice vykreslit. */
  if (tile.level != SbGeoMipmapTile.LEVEL_NONE)
  {
    /* Neni-li dlazdice pouze virtualni dlazdici */
    if (tile.levels != null)
    {
      int vertex_index;

      /* Vyber vrcholu urovne dlazdice, ktera se ma vykreslit. */
      final SbGeoMipmapTileLevel level = tile.levels.get(tile.level);
      int size = tile_tree.level_sizes[tile.level];
      int[] vertices = level.vertices;
      int max_x = size;
      int max_y = size;

      // zkracene vyhodnoceni !!!
      boolean draw_right = (tile.right != null) && (tile.right.level !=
        tile.level) && (tile.right.level != SbGeoMipmapTile.LEVEL_NONE);
      boolean draw_bottom = (tile.bottom != null) && (tile.bottom.level !=
        tile.level) && (tile.bottom.level != SbGeoMipmapTile.LEVEL_NONE);

      /* Napojeni sousedni dlazdice zprava. */
      if (draw_right)
      {
        max_x--;

        int right_level = tile.right.level;
        int right_size = tile_tree.level_sizes[right_level];

        /* Ma-li prava dlazdice mensi dataily. */
        if (right_level > tile.level)
        {
          int fan_size = (size - 1) / (right_size - 1);
          for (int Y = fan_size; Y < size; Y += fan_size)
          {
            gl2.glBegin(GL2.GL_TRIANGLE_FAN);
            GL_SEND_VERTEX(gl2,vertices[(Y * size) + size - 1]);
            int max_i = (Y == (size - 1)) && (draw_bottom) ? Y - 1 : Y;
            for (int I = max_i; I >= (Y - fan_size); --I)
            {
              GL_SEND_VERTEX(gl2,vertices[(I * size) + size - 2]);
            }
            GL_SEND_VERTEX(gl2,vertices[((Y - fan_size) * size) + size - 1]);
            gl2.glEnd();
          }
        }
        /* Ma-li prava dlazdice vetsi detaily. */
        else if (tile.right.level < tile.level)
        {
          int fan_size = (right_size - 1) / (size - 1);
          int[] right_vertices = tile.right.levels.get(right_level).vertices;

          for (int Y = 0; Y < (size - 1); ++Y)
          {
            gl2.glBegin(GL2.GL_TRIANGLE_FAN);
            GL_SEND_VERTEX(gl2,vertices[(Y * size) + size - 2]);
            for (int I = (Y * fan_size); I <= (Y * fan_size) + fan_size; ++I)
            {
              GL_SEND_VERTEX(gl2,right_vertices[I * right_size]);
            }
            if ((Y != (size - 2)) || (!draw_bottom))
            {
              GL_SEND_VERTEX(gl2,vertices[((Y + 1) * size) + size - 2]);
            }
            gl2.glEnd();
          }
        }
      }

      /* Napojeni sousedni dlazdice zdola. */
      if (draw_bottom)
      {
        max_y--;

        int bottom_level = tile.bottom.level;
        int bottom_size = tile_tree.level_sizes[bottom_level];

        /* Ma-li spodni dlazdice mensi dataily. */
        if (bottom_level > tile.level)
        {
          int fan_size = (size - 1) / (bottom_size - 1);
          for (int X = fan_size; X < size; X += fan_size)
          {
            gl2.glBegin(GL2.GL_TRIANGLE_FAN);
            GL_SEND_VERTEX(gl2,vertices[((size - 1) * size) + X]);
            GL_SEND_VERTEX(gl2,vertices[((size - 1) * size) + X - fan_size]);
            int max_i = (X == (size - 1)) && (draw_right) ? X : X + 1;
            for (int I = (X - fan_size); I < max_i; ++I)
            {
              GL_SEND_VERTEX(gl2,vertices[((size - 2) * size) + I]);
            }
            gl2.glEnd();
          }
        }
        /* Ma-li spodni dlazdice vetsi detaily. */
        else if (tile.bottom.level < tile.level)
        {
          int fan_size = (bottom_size - 1) / (size - 1);
          int[] bottom_vertices = tile.bottom.levels.get(bottom_level).vertices;

          for (int X = 0; X < (size - 1); ++X)
          {
            gl2.glBegin(GL2.GL_TRIANGLE_FAN);
            GL_SEND_VERTEX(gl2,vertices[((size - 2) * size) + X]);
            if ((X != (size - 2)) || (!draw_right))
            {
              GL_SEND_VERTEX(gl2,vertices[((size - 2) * size) + X + 1]);
            }
            for (int I = (X * fan_size) + fan_size; I >= (X * fan_size); --I)
            {
              GL_SEND_VERTEX(gl2,bottom_vertices[I]);
            }
            gl2.glEnd();
          }
        }
      }

      /* Vykresleni vnitrku dlazdice. */
      for (int Y = 0; Y < (max_y - 1); ++Y)
      {
        gl2.glBegin(GL2.GL_QUAD_STRIP);

        /* Prvni dva vrcholy pasu. */
        GL_SEND_VERTEX(gl2,vertices[(Y + 1) * size]);
        GL_SEND_VERTEX(gl2,vertices[Y * size]);

        for (int X = 1; X < max_x; ++X)
        {
          /* Vykresleni dalsich dvou vrcholu pasu. */
          GL_SEND_VERTEX(gl2,vertices[((Y + 1)  * size) + X]);
          GL_SEND_VERTEX(gl2,vertices[(Y * size) + X]);
        }
        gl2.glEnd();
      }
    }
    /* Dlazdice je virtualni. */
    else
    {
      /* Vypocet indexu potomku. */
      int first_index = (index << 2) + 1;
      int second_index = first_index + 1;
      int third_index = second_index + 1;
      int fourth_index = third_index + 1;

      /* Vykresleni potomku dlazdice. */
      renderTree(action, first_index);
      renderTree(action, second_index);
      renderTree(action, third_index);
      renderTree(action, fourth_index);
    }
  }
}

public int pickLevel(final SbGeoMipmapTile  tile)
{
  /* Vypocet vzdalenosti kamery od dlazdice. */
  final SbVec3f camera_position = new SbVec3f(view_volume.getProjectionPoint());
  float distance = (tile.center.operator_minus(camera_position)).sqrLength();

  /* Vyber urovne detailu dlazdice podle vzdalenosti od kamery. */
  for (int I = 0; I < tile_tree.level_count; ++I)
  {
    if (distance < SbBasic.SbSqr(tile.levels.get(I).error * distance_const))
    {
      return I - 1;
    }
  }
  return tile_tree.level_count - 1;
}

public static void mapSizeChangedCB(Object _instance,
  SoSensor sensor)
{
  /* Aktualizace vnitrni hodnoty pole. */
  SoSimpleGeoMipmapTerrain instance =
    (SoSimpleGeoMipmapTerrain)(_instance);
  instance.map_size = instance.mapSize.getValue();
  first_run = true; //YB
  instance.touch(); //YB
}

public static void tileSizeChangedCB(Object _instance,
  SoSensor sensor)
{
  /* Aktualizace vnitrni hodnoty pole. */
  SoSimpleGeoMipmapTerrain instance =
    (SoSimpleGeoMipmapTerrain)(_instance);
  instance.tile_size = instance.tileSize.getValue();
}

public static void pixelErrorChangedCB(Object _instance,
  SoSensor sensor)
{
  /* Aktualizace vnitrni hodnoty pole. */
  SoSimpleGeoMipmapTerrain instance =
    (SoSimpleGeoMipmapTerrain)(_instance);
  instance.pixel_error = instance.pixelError.getValue();
}

public static void frustrumCullingChangedCB(Object _instance,
  SoSensor sensor)
{
  /* Aktualizace vnitrni hodnoty pole. */
  SoSimpleGeoMipmapTerrain instance =
    (SoSimpleGeoMipmapTerrain)(_instance);
  instance.is_frustrum_culling = instance.frustrumCulling.getValue();
}

public static void freezeChangedCB(Object _instance,
  SoSensor sensor)
{
  /* Aktualizace vnitrni hodnoty pole. */
  SoSimpleGeoMipmapTerrain instance =
    (SoSimpleGeoMipmapTerrain)(_instance);
  instance.is_freeze = instance.freeze.getValue();
}

/******************************************************************************
* SoSimpleGeoMipmapTerrain - private
******************************************************************************/

public void destructor()
{
  /* Uvolneni pameti. */
  Destroyable.delete( tile_tree);
  Destroyable.delete(map_size_sensor);
  Destroyable.delete(tile_size_sensor);
  Destroyable.delete(pixel_error_sensor);
  Destroyable.delete(frustrum_culling_sensor);
  Destroyable.delete(freeze_sensor);
  super.destructor();
}
	
}
