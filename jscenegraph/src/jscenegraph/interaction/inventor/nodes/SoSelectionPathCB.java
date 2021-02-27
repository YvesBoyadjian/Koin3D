/**
 * 
 */
package jscenegraph.interaction.inventor.nodes;

import jscenegraph.database.inventor.SoPath;

/**
 * @author Yves Boyadjian
 *
 */
public interface SoSelectionPathCB {

	void invoke(Object userData, SoPath path);
}
