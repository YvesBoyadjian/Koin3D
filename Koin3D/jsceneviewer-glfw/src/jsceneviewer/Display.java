/**
 * 
 */
package jsceneviewer;

/**
 * @author Yves Boyadjian
 *
 */
public class Display {
	
	static Display current;

	public static Display getCurrent() {
		if(current == null) {
			current = new Display();
		}
		return current;
	}

	public void timerExec(int msec, Runnable object) {
		// TODO Auto-generated method stub
		
	}

}
