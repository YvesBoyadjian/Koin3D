/**
 * 
 */
package jsceneviewerglfw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.util.Point;
import org.lwjgl.util.Rectangle;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * @author Yves Boyadjian
 *
 */
public class Composite {
	
	private Display display;
	
	private boolean visible;
	
	private Composite parent;
	
	private int style;
	
	private final List<PaintListener> paintListeners = new ArrayList<>();
	
	private final Map<Integer, List<Listener>> listeners = new HashMap<>();
	
	private final List<KeyListener> keyListeners = new ArrayList<>();
	
	private final List<MouseMoveListener> mouseMoveListeners = new ArrayList<>();
	
	private final List<MouseTrackListener> mouseTrackListeners = new ArrayList<>();
	
	private boolean redrawAsked;

	public Composite(Composite parent, int style) {
		this.style = style;
		if(parent != null) {
			display = parent.getDisplay();
			this.parent = parent;
		}
		else {
			display = Display.getCurrent();
		}
		display.register(this);
	}

	public Composite getParent () {
		return parent;
	}
	
	public int getStyle () {
		return style;
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
		return visible;
	}

	public void setVisible (boolean visible) {
		this.visible = visible;
		if(parent != null) {
			parent.setVisible(visible);
		}
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
		redrawAsked = true;
	}

	public Point getSize (){
		throw new UnsupportedOperationException("getSize");
	}

	public boolean setFocus () {
		return false;
	}

	public void addMouseTrackListener (MouseTrackListener listener) {
		mouseTrackListeners.add(listener);
	}

	public void addPaintListener (PaintListener listener) {
		paintListeners.add(listener);
	}
	public void addListener (int eventType, Listener listener) {
		List<Listener> eventList = listeners.get(eventType);
		if(eventList == null) {
			eventList = new ArrayList<>();
			listeners.put(eventType,eventList);
		}
		eventList.add(listener);
	}

	public void addKeyListener (KeyListener listener) {
		keyListeners.add(listener);
	}

	public void addMouseListener (MouseListener listener) {
		//TODO
	}
	
	public void addMouseMoveListener (MouseMoveListener listener) {
		mouseMoveListeners.add(listener);
	}
	
	public void addMouseWheelListener (MouseWheelListener listener) {
		//TODO
	}
	
	public Rectangle getBounds (){		
		throw new UnsupportedOperationException("getBounds");
	}

	public Point toDisplay(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void loop() {
		
		if(redrawAsked) {
			redrawAsked = false;
			PaintEvent pe = new PaintEvent();
			paintListeners.forEach((pl)-> pl.paintControl(pe));
		}
	}
	
	public void resizeCB(int width, int height) {
		List<Listener> resizeList = listeners.get(SWT.Resize);
		if(resizeList == null) {
			return;
		}
		Event e = new Event();
		resizeList.forEach((l)->l.handleEvent(e));
	}
	
	public void keyCB(int key, int scancode, int action, int mods) {
		
		KeyEvent e = new KeyEvent(key,scancode, action, mods);
		if(action == GLFW_PRESS) {
			keyListeners.forEach((kl)-> kl.keyPressed(e));
		}
		else if (action == GLFW_RELEASE) {
			keyListeners.forEach((kl)-> kl.keyReleased(e));
		}
	}

	public void mouseMoveCB(double xpos, double ypos) {
		
		MouseEvent me = new MouseEvent(xpos, ypos);
		mouseMoveListeners.forEach((mml)->mml.mouseMove(me));
		if(parent != null) {
			parent.mouseMoveCB(xpos, ypos);
		}
	}
	
	public void mouseEnterCB(boolean entered) {
		MouseEvent e = new MouseEvent();
		if(entered) {
			mouseTrackListeners.forEach((mtl)->mtl.mouseEnter(e));
		}
		else {
			mouseTrackListeners.forEach((mtl)->mtl.mouseExit(e));
		}
	}
	
	public boolean shouldClose() {
		return false;
	}
	
	public void dispose() {
		
	}
}
