/**
 * 
 */
package jsceneviewerglfw;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.util.Point;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.BinaryOperator;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;
/**
 * @author Yves Boyadjian
 *
 */
public class Display {
	
	static Display current;
	
	Set<Composite> composites = Collections.newSetFromMap(new WeakHashMap<Composite,Boolean>());
	
	final Map<Long, List<Runnable>> timers = new HashMap<>();
	
	public Display() {
		current = this;
		
		// Setup an error callback. The default implementation
				// will print the error message in System.err.
				GLFWErrorCallback.createPrint(System.err).set();

				// Initialize GLFW. Most GLFW functions will not work before doing this.
				if ( !glfwInit() )
					throw new IllegalStateException("Unable to initialize GLFW");

				// Configure GLFW
				glfwDefaultWindowHints(); // optional, the current window hints are already the default
				glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
				glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable								
	}

	public static Display getCurrent() {
		if(current == null) {
			current = new Display();
		}
		return current;
	}

	public void timerExec(long microsec, Runnable object) {
		long currentTimeMicro = System.nanoTime()/1000;//Instant.now().toEpochMilli();
		long startTimeMicro = currentTimeMicro + microsec;
		
		List<Runnable> runnablesForTime = timers.get(startTimeMicro);
		if( runnablesForTime == null) {
			runnablesForTime = new ArrayList<>();
			timers.put(startTimeMicro, runnablesForTime);
		}
		runnablesForTime.add(object);
	}

	public Point getCursorLocation() {
		throw new UnsupportedOperationException("getCursorLocation");
	}

	public void dispose() {
		composites.forEach(Composite::dispose);
		
		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	public void sleep() {
		// TODO Auto-generated method stub
		
	}

	public boolean readAndDispatch() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void register(Composite composite) {
		composites.add(composite);
	}
	
	public void loop() {
		while( !composites.stream().map(Composite::shouldClose).reduce(false, new BinaryOperator<Boolean>() {

			@Override
			public Boolean apply(Boolean first, Boolean second) {
				return first || second;
			}
			
		})) {
			glfwPollEvents();
			
			long currentTimeMicro = System.nanoTime()/1000;//Instant.now().toEpochMilli();
			
			boolean treated;
			//do {
				treated = false;
				for(Long timerStartTime : timers.keySet()) {
					if(timerStartTime <= currentTimeMicro) {
						List<Runnable> runnables = timers.get(timerStartTime);
						timers.remove(timerStartTime);
						runnables.forEach(Runnable::run);
						treated = true;
						break;
					}
				}
			//} while(treated);
			
			composites.forEach(Composite::loop);
			
//		    Thread t = new Thread(()->  {
//		    	System.gc();
//		    });
//		    t.start();
		    
			
//			System.gc();
//			System.runFinalization();
		}
	}

	
}
