/**
 * 
 */
package jsceneviewerglfw;

import jsceneviewerglfw.inventor.qt.SoQtGLWidget;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import org.lwjgl.util.Point;
import org.lwjgl.util.Rectangle;

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
public class GLCanvas extends Composite {

	// The window handle
	private long window;
	
	private GLData format;
		
	public GLCanvas(Composite parent, int style, GLData format) {
		super(parent, style);
		this.format = format;
		
		  GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		  
	      int  width = vidMode.width();
	      int  height = vidMode.height();
	      
	      if( format.majorVersion > 0 ) {
	    	  glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, format.majorVersion);
	      }
	      if( format.minorVersion >= 0 ) {
	    	  glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, format.minorVersion);
	      }
	      if( format.profile != null ) {
	    	  glfwWindowHint(GLFW_OPENGL_PROFILE, format.profile ==  GLData.Profile.COMPATIBILITY ? GLFW_OPENGL_COMPAT_PROFILE : GLFW_OPENGL_CORE_PROFILE);
	      }
			glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_FALSE);
			glfwWindowHint(GLFW_REFRESH_RATE, 120);
		if(format.depthSize > 0) {
			glfwWindowHint(GLFW_DEPTH_BITS, format.depthSize);			
		}
			
		// Create the window
		window = glfwCreateWindow(width, height, "Hello World!", glfwGetPrimaryMonitor(), NULL);
		if ( window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");
		
		// Get the thread stack and push a new frame
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(
				window,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
		} // the stack frame is popped automatically

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync (or not)
		glfwSwapInterval(format.waitForRefresh ? 1 : 0);

		// Make the window visible
		glfwShowWindow(window);
		setVisible(true);
		
		glfwSetWindowSizeCallback(window, new GLFWWindowSizeCallbackI() {

			@Override
			public void invoke(long window, int width, int height) {				
				resizeCB(width,height);
			}
			
		});		
		
        glfwSetKeyCallback(window, (windowHnd, key, scancode, action, mods) -> {
        	keyCB(key,scancode,action,mods);
        });
        
        glfwSetCursorPosCallback(window, new GLFWCursorPosCallbackI() {

			@Override
			public void invoke(long window, double xpos, double ypos) {
				mouseMoveCB(xpos, ypos);
			}
        	
        });
        
        glfwSetCursorEnterCallback(window, new GLFWCursorEnterCallbackI() {

			@Override
			public void invoke(long window, boolean entered) {
				mouseEnterCB(entered);
			}
        	
        });
        
        glfwSetInputMode(window, GLFW_CURSOR, format.grabCursor ? GLFW_CURSOR_DISABLED : GLFW_CURSOR_HIDDEN);
//        if ( format.grabCursor && glfwRawMouseMotionSupported()) does not work
//            glfwSetInputMode(window, GLFW_RAW_MOUSE_MOTION, GLFW_TRUE);	
        }
	
	public void setCurrent() {
		glfwMakeContextCurrent(window);
	}

	public boolean setFocus () {
		glfwFocusWindow(window);
		return true;
	}
	
	public GLData getGLData() {
		return format;
	}

	public void swapBuffers() {
		glfwSwapBuffers(window);	}

	public long getWindow() {
		return window;
	}

	public Point getSize (){
		IntBuffer sizex = BufferUtils.createIntBuffer(1);
		IntBuffer sizey = BufferUtils.createIntBuffer(1);
		glfwGetWindowSize(window,sizex,sizey);
		return new Point(sizex.get(),sizey.get());
	}

	public Rectangle getBounds (){		
		
		IntBuffer posx = BufferUtils.createIntBuffer(1);
		IntBuffer posy = BufferUtils.createIntBuffer(1);
		glfwGetWindowPos(window,posx,posy);
		IntBuffer sizex = BufferUtils.createIntBuffer(1);
		IntBuffer sizey = BufferUtils.createIntBuffer(1);
		glfwGetWindowSize(window,sizex,sizey);
		
		return new Rectangle(posx.get(),posy.get(),sizex.get(),sizey.get());
	}

	
	public boolean shouldClose() {
		return glfwWindowShouldClose(window) ;
	}
	
	public void maximize() {
		glfwMaximizeWindow(window);
	}

	public void dispose() {
		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);
		super.dispose();
	}
}
