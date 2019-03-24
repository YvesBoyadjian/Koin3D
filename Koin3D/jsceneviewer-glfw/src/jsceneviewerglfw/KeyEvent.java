/**
 * 
 */
package jsceneviewerglfw;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_0;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_1;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_2;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_3;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_4;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_5;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_6;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_7;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_8;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_9;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_ADD;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_DECIMAL;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_DIVIDE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_EQUAL;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_MULTIPLY;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_SUBTRACT;

/**
 * @author Yves Boyadjian
 *
 */
public class KeyEvent extends TypedEvent {

	public KeyEvent(Event e) {
		this.keyCode = e.keyCode;
		//this.keyLocation = e.keyLocation;
		this.stateMask = e.stateMask;
	}

	public KeyEvent(int key, int scancode, int action, int mods) {
		switch(key) {
	    case GLFW_KEY_KP_ADD:
	      case GLFW_KEY_KP_SUBTRACT:
	      case GLFW_KEY_KP_MULTIPLY:
	      case GLFW_KEY_KP_DIVIDE:
	      case GLFW_KEY_KP_DECIMAL:
	      case GLFW_KEY_KP_ENTER:
	      case GLFW_KEY_KP_0:
	      case GLFW_KEY_KP_1:
	      case GLFW_KEY_KP_2:
	      case GLFW_KEY_KP_3:
	      case GLFW_KEY_KP_4:
	      case GLFW_KEY_KP_5:
	      case GLFW_KEY_KP_6:
	      case GLFW_KEY_KP_7:
	      case GLFW_KEY_KP_8:
	      case GLFW_KEY_KP_9:
	      case GLFW_KEY_KP_EQUAL:
			keyLocation = SWT.KEYPAD;
		}
		keyCode = key;
	}

	public int stateMask;
	public int keyCode;
	public int keyLocation;

}
