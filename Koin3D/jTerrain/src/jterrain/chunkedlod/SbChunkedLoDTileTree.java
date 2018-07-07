///////////////////////////////////////////////////////////////////////////////
//  SoTerrain
///////////////////////////////////////////////////////////////////////////////
/// Basic data types of Chunked LoD algorithm.
/// \file SbChunkedLoDPrimitives.h
/// \author Radek Barton - xbarto33@stud.fit.vutbr.cz
/// \date 14.11.2006
/// There are defined basic data types of Chunked LoD algorithm in this file.
/// Tile quad-tree of class ::SbChunkedLoDTileTree splits input heightmap into
/// recursive structure of tiles of class ::SbChunkedLoDTile with gemetry on
/// different level of detail. Root node contains tile describing whole
/// terrain on coarsest level of detail and it has four children describing
/// quaters of root node on doubled level of detail. Child tiles are divided
/// recursively in same manner.
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

import jscenegraph.database.inventor.SbBasic;
import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.port.Destroyable;

/**
 * @author Yves Boyadjian
 *
 */
/** Chunked LoD algorithm tile quad-tree.
This quad-tree contains tiles with indexed geometry, static part of error
metric and bounding boxes of class ::SbChunkedLoDTile. */
public class SbChunkedLoDTileTree implements Destroyable {
    /* Methods. */
    /** Construtor.
    Creates instance of ::SbChunkedLoDTileTree with given number of tiles
    in tree \e tree_size and initializes tiles in this tree to have square of
    \e tile_size number of vertex indices and default all other values.
    \param tree_size Number of tiles in tile quad-tree.
    \param tile_size Size of tile side. Number of its vertex indices is equal
      to square of this value. */
    public SbChunkedLoDTileTree(int tree_size, int tile_size) {
    	  this.tree_size = tree_size;  this.tile_size = tile_size;
    	  tiles.copyFrom( new SbChunkedLoDTileList(tree_size));
    	
    	  int vertex_count = SbBasic.SbSqr(this.tile_size);
    	  for (int I = 0; I < this.tree_size; I++)
    	  {
    	    this.tiles.append(new SbChunkedLoDTile(0.0f, vertex_count, new SbBox3f()));
    	  }    	
    }
    /** Copy constructor.
    Creates new instance of ::SbChunkedLoDTileTree class from \e old_tree
    instance.
    \param old_tree Old instance of tile quad-tree. */
    public SbChunkedLoDTileTree(final SbChunkedLoDTileTree old_tree) {
    	  tree_size = old_tree.tree_size; tile_size = old_tree.tile_size;
    	  tiles.copyFrom(old_tree.tiles);    	
    }
    /** Destructor.
    Destroys instance of ::SbChunkedLoDTileTree class. */
    public void destructor() {
    	  // Nothing.    	
    }
    /* Attributes. */
    /// Number of tiles in tile quad-tree.
    public int tree_size;
    /// Size of tile geometry side.
    public int tile_size;
    /// List of tile quad-tree tiles.
    public final SbChunkedLoDTileList tiles = new SbChunkedLoDTileList();
}
