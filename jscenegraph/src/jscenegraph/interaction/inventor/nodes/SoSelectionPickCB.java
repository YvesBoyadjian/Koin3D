/**
 * 
 */
package jscenegraph.interaction.inventor.nodes;

import jscenegraph.database.inventor.SoPath;
import jscenegraph.database.inventor.SoPickedPoint;

/**
 * @author Yves Boyadjian
 *
 */
public interface SoSelectionPickCB {

	public SoPath invoke(Object userData, SoPickedPoint pick);
}
