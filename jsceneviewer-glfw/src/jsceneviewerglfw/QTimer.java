/**
 * 
 */
package jsceneviewerglfw;

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

	/**
	 * Java port : microsec instead of millisec
	 * @param microsec
	 */
	public void start(long microsec) {
		
		isActive = true;
		display.timerExec( microsec, ()->{
			if(isActive){
				if(singleShot) {
					isActive = false;
				}
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
