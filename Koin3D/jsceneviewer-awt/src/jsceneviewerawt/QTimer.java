/**
 * 
 */
package jsceneviewerawt;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Yves Boyadjian
 *
 */
public class QTimer {

	private Timer timer;
	
	private Object object;
	
	private boolean singleShot;
	
	private QCallback callback;
	
	private boolean isActive;
	
	public static interface QCallback {
		public void run(Object obj);
	};

	public QTimer(Object object) {
		this.object = object;
	}

	public void setSingleShot(boolean b) {
		singleShot = b;
	}

	public boolean isActive() {
		return isActive;
	}

	/**
	 * java port : microsec instead of millisec
	 * @param microsec
	 */
	public void start(long microsec) {
		
		isActive = true;

		int delay = (int)(microsec/1000);

		timer = new Timer(delay, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(isActive){
					isActive = false;
					callback.run(object);
				};
			}
		});
		timer.setRepeats(false);
		timer.start();

//		timer.schedule(new TimerTask() {
//			@Override
//			public void run() {
//				if(isActive){
//					isActive = false;
//					callback.run(object);
//				};
//			}
//		},delay);
	}

	public void stop() {
		isActive = false;
	}

	public void connect(QCallback callback) {
		this.callback = callback;
	}

}
