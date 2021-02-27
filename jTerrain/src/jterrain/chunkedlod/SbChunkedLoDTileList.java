/**
 * 
 */
package jterrain.chunkedlod;

import jscenegraph.coin3d.inventor.lists.SbList;

/**
 * @author Yves Boyadjian
 *
 */
public class SbChunkedLoDTileList extends SbList<SbChunkedLoDTile> {

	public SbChunkedLoDTileList(int tree_size) {
		super(tree_size);
	}
	/// ::SbList template instance type for list of ::SbChunkedLoDTile tiles.

	public SbChunkedLoDTileList() {
		super();
	}
}
