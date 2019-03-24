/**
 * 
 */
package jsceneviewerglfw;

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
	public int button;
	public int stateMask;
	public int x;
	public int y;
	public int count;

}
