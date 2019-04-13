/**
 * 
 */
package application.scenegraph;

/**
 * @author Yves Boyadjian
 *
 */
public class SoTouchLODMaster {
	
	private int counter;

	public void reset() {
		counter = 0;
	}
	
	public void increment() {
		counter++;
	}
	
	public int getCount() {
		return counter;
	}
}
