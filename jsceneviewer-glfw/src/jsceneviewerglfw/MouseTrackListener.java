/**
 * 
 */
package jsceneviewerglfw;

import java.util.EventListener;

/**
 * @author Yves Boyadjian
 *
 */
public interface MouseTrackListener extends EventListener {

/**
 * Sent when the mouse pointer passes into the area of
 * the screen covered by a control.
 *
 * @param e an event containing information about the mouse enter
 */
void mouseEnter(MouseEvent e);

/**
 * Sent when the mouse pointer passes out of the area of
 * the screen covered by a control.
 *
 * @param e an event containing information about the mouse exit
 */
void mouseExit(MouseEvent e);

/**
 * Sent when the mouse pointer hovers (that is, stops moving
 * for an (operating system specified) period of time) over
 * a control.
 *
 * @param e an event containing information about the hover
 */
void mouseHover(MouseEvent e);
}
