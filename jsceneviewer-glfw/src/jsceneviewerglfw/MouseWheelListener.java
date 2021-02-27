/**
 * 
 */
package jsceneviewerglfw;

import java.util.EventListener;

/**
 * @author Yves Boyadjian
 *
 */
public interface MouseWheelListener extends EventListener {


/**
 * Sent when the mouse wheel is scrolled.
 *
 * @param e an event containing information about the mouse wheel action
 */
void mouseScrolled (MouseEvent e);
}
