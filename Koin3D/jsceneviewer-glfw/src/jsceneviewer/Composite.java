/**
 * 
 */
package jsceneviewer;

import org.lwjgl.util.Point;
import org.lwjgl.util.Rectangle;

/**
 * @author Yves Boyadjian
 *
 */
public class Composite {
	
	private Display display;

	public Composite(Composite parent, int style) {
		// TODO Auto-generated constructor stub
	}

	public Composite getParent () {
		return null;
	}
	
	public int getStyle () {
		return 0; // TODO
	}
	
	/**
	 * Returns the <code>Display</code> that is associated with
	 * the receiver.
	 * <p>
	 * A widget's display is either provided when it is created
	 * (for example, top level <code>Shell</code>s) or is the
	 * same as its parent's display.
	 * </p>
	 *
	 * @return the receiver's display
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 * </ul>
	 */
	public Display getDisplay () {
		Display display = this.display;
		if (display == null) throw new IllegalStateException("Widget disposed");
		return display;
	}
	
	public boolean isVisible() {
		return false; //TODO
	}

	public void setVisible (boolean visible) {
		
	}
	
	public void setCursor (Cursor cursor) {
		//TODO
	}
	
	/**
	 * Enables the receiver if the argument is <code>true</code>,
	 * and disables it otherwise. A disabled control is typically
	 * not selectable from the user interface and draws with an
	 * inactive or "grayed" look.
	 *
	 * @param enabled the new enabled state
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public void setEnabled (boolean enabled) {
		
	}
	
	public void redraw () {
		//TODO
	}

	public Point getSize (){
		return null; //TODO
	}

	public boolean setFocus () {
		return false; //TODO
	}

	public void addMouseTrackListener (MouseTrackListener listener) {
		//TODO
	}

	public void addPaintListener (PaintListener listener) {
		//TODO
	}
	public void addListener (int eventType, Listener listener) {
		//TODO
	}

	public void addKeyListener (KeyListener listener) {
		//TODO
	}

	public void addMouseListener (MouseListener listener) {
		//TODO
	}
	
	public void addMouseMoveListener (MouseMoveListener listener) {
		//TODO
	}
	
	public void addMouseWheelListener (MouseWheelListener listener) {
		//TODO
	}
	
	public Rectangle getBounds (){
		return null; //TODO
	}
}
