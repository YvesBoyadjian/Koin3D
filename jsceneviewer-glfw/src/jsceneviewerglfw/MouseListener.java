/**
 * 
 */
package jsceneviewerglfw;

import java.util.EventListener;

/**
 * @author Yves Boyadjian
 *
 */

/**
 * Classes which implement this interface provide methods
 * that deal with the events that are generated as mouse buttons
 * are pressed.
 * <p>
 * After creating an instance of a class that implements
 * this interface it can be added to a control using the
 * <code>addMouseListener</code> method and removed using
 * the <code>removeMouseListener</code> method. When a
 * mouse button is pressed or released, the appropriate method
 * will be invoked.
 * </p>
 *
 * @see MouseAdapter
 * @see MouseEvent
 */
public interface MouseListener extends EventListener {

/**
 * Sent when a mouse button is pressed twice within the
 * (operating system specified) double click period.
 *
 * @param e an event containing information about the mouse double click
 *
 * @see org.eclipse.swt.widgets.Display#getDoubleClickTime()
 */
void mouseDoubleClick(MouseEvent e);

/**
 * Sent when a mouse button is pressed.
 *
 * @param e an event containing information about the mouse button press
 */
void mouseDown(MouseEvent e);

/**
 * Sent when a mouse button is released.
 *
 * @param e an event containing information about the mouse button release
 */
void mouseUp(MouseEvent e);
}
