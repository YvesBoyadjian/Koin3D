/**
 * 
 */
package jsceneviewer;

import org.eclipse.swt.widgets.Display;

/**
 * @author Yves Boyadjian
 *
 */
public class QTimer {
	
	private Display display;
	
	private Object object;
	
	private boolean singleShot;
	
	private QCallback callback;
	
	private boolean isActive;
	
	public static interface QCallback {
		public void run(Object obj);
	};

	public QTimer(Object object) {
		this.object = object;
		display = Display.getCurrent();
	}

	public void setSingleShot(boolean b) {
		singleShot = b;
	}

	public boolean isActive() {
		return isActive;
	}

	public void start(long msec) {
		
		isActive = true;
		display.timerExec((int)msec, ()->{
			if(isActive){
				isActive = false;
				callback.run(object);
			};
		});
	}

	public void stop() {
		isActive = false;
	}

	public void connect(QCallback callback) {
		this.callback = callback;
	}

}
