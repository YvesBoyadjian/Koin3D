/**
 * 
 */
package jsceneviewerglfw;

import java.util.EventListener;

/**
 * @author Yves Boyadjian
 *
 */
public interface MouseMoveListener extends EventListener {


/**
 * Sent when the mouse moves.
 *
 * @param e an event containing information about the mouse move
 */
void mouseMove(MouseEvent e);
}
