
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

import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbIntList;

/**
 * @author Yves Boyadjian
 *
 */

/** Chunked LoD algorithm tile.
There is created quad-tree with this tiles serving for decision what level
of detail of terrain parts should be choosen during rendering. Tile contains
indices to heightmap vertices describing tile's geometry, static part of error
metric and bounding box for easy frustrum culling. */
public class SbChunkedLoDTile {

		    /* Methods. */
		    /** Construtor.
		    Creates instance of ::SbChunkedLoDTile class with given static part of
		    error metric \e error, prepares list of vertex indices to size
		    \e vertex_count and initializes them to zero values. Sets tile bunding box
		    to \e bunds value.
		    \param error Static part of error metric for this tile.
		    \param vertex_count Suggested count of vertex indices for tile geometry.
		    \param bounds Bounding box of this tile. */
		  public SbChunkedLoDTile() {
				this(0.0f, 0, new SbBox3f());
			}
		    public SbChunkedLoDTile(float error, int vertex_count,
		      SbBox3f bounds) {
		    	  this.error = error; this.vertices.copyFrom( new SbIntList(vertex_count));
		    	  this.bounds.copyFrom( bounds);
		    	  
		    	    for (int I = 0; I < vertex_count; I++)
		    	    {
		    	      this.vertices.append(0);
		    	    }		    	
		    }
		    /** Copy contructor.
		    Creates new instance of ::SbChunkedLoDTile class from \e old_tile instance.
		    \param old_tile Old instance of tile. */
		    public SbChunkedLoDTile(final SbChunkedLoDTile old_tile) {
		    	  error = old_tile.error; vertices.copyFrom(old_tile.vertices);
		    	  bounds.copyFrom( old_tile.bounds);		    	
		    }
		    /** Destructor.
		    Destroys instance of ::SbChunkedLoDTile class. */
		    public void destructor() {
		    	  // Nothing.		    	
		    }
		    /* Attributes. */
		    /// Static part of error metric.
		    float error;
		    /// List of geometry vertex indices.
		    public final SbIntList vertices = new SbIntList();
		    /// Bounding box of tile.
		    public final SbBox3f bounds = new SbBox3f();
}
