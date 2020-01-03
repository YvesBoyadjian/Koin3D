/**
 * 
 */
package jsceneviewerglfw;

import org.lwjgl.glfw.GLFW;

/**
 * @author Yves Boyadjian
 *
 */
public class MouseEvent extends TypedEvent {

	public MouseEvent(Event e) {
		this.x = e.x;
		this.y = e.y;
		this.button = e.button;
		this.stateMask = e.stateMask;
		//this.count = e.count;		
	}
	public MouseEvent(double xpos, double ypos) {
		x = (int)xpos;
		y = (int)ypos;
	}
	public MouseEvent() {
		// TODO Auto-generated constructor stub
	}
	public MouseEvent(int button2) {
		switch(button2) {
		case GLFW.GLFW_MOUSE_BUTTON_1: this.button = 1; break;
		case GLFW.GLFW_MOUSE_BUTTON_2: this.button = 2; break;
		case GLFW.GLFW_MOUSE_BUTTON_3: this.button = 3; break;
		}
	}
	public int button;
	public int stateMask;
	public int x;
	public int y;
	public int count;

}
