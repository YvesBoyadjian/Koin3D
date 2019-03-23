/**
 * 
 */
package jsceneviewer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwTerminate;

import org.lwjgl.glfw.GLFWKeyCallbackI;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import example.HelloWorld;

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

/*
 * Control example snippet: print key state, code and character
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.0
 */
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.widgets.Display;
//import org.eclipse.swt.widgets.Event;
//import org.eclipse.swt.widgets.Listener;
//import org.eclipse.swt.widgets.Shell;

public class KeyCodeCharacterState {

  static String stateMask(int stateMask) {
    String string = "";
    if ((stateMask & GLFW_MOD_CONTROL) != 0)
      string += " CTRL";
    if ((stateMask & GLFW_MOD_ALT) != 0)
      string += " ALT";
    if ((stateMask & GLFW_MOD_SHIFT) != 0)
      string += " SHIFT";
    if ((stateMask & GLFW_MOD_SUPER) != 0)
      string += " COMMAND";
    return string;
  }

  static String character(int character) {
    switch (character) {
    case 0:
      return "'\\0'";
    case GLFW_KEY_BACKSPACE:
      return "'\\b'";
    case GLFW_KEY_ENTER:
      return "'\\r'";
    case GLFW_KEY_DELETE:
      return "DEL";
    case GLFW_KEY_ESCAPE:
      return "ESC";
//    case GLFW_KEY_LF:
//      return "'\\n'";
    case GLFW_KEY_TAB:
      return "'\\t'";
    }
    return "'" + glfwGetKeyName(character,0) + "'";
  }

  static String keyCode(int keyCode) {
    switch (keyCode) {

    /* Keyboard and Mouse Masks */
    case GLFW_KEY_LEFT_ALT:
      return "ALT";
    case GLFW_KEY_LEFT_SHIFT:
      return "SHIFT";
    case GLFW_KEY_LEFT_CONTROL:
      return "CONTROL";
//    case GLFW_KEY_COMMAND:
//      return "COMMAND";

    /* Non-Numeric Keypad Keys */
    case GLFW_KEY_UP:
      return "ARROW_UP";
    case GLFW_KEY_DOWN:
      return "ARROW_DOWN";
    case GLFW_KEY_LEFT:
      return "ARROW_LEFT";
    case GLFW_KEY_RIGHT:
      return "ARROW_RIGHT";
    case GLFW_KEY_PAGE_UP:
      return "PAGE_UP";
    case GLFW_KEY_PAGE_DOWN:
      return "PAGE_DOWN";
    case GLFW_KEY_HOME:
      return "HOME";
    case GLFW_KEY_END:
      return "END";
    case GLFW_KEY_INSERT:
      return "INSERT";

    /* Virtual and Ascii Keys */
    case GLFW_KEY_BACKSPACE:
      return "BS";
    case GLFW_KEY_ENTER:
      return "CR";
    case GLFW_KEY_DELETE:
      return "DEL";
    case GLFW_KEY_ESCAPE:
      return "ESC";
//    case GLFW_KEY_LF:
//      return "LF";
    case GLFW_KEY_TAB:
      return "TAB";

    /* Functions Keys */
    case GLFW_KEY_F1:
      return "F1";
    case GLFW_KEY_F2:
      return "F2";
    case GLFW_KEY_F3:
      return "F3";
    case GLFW_KEY_F4:
      return "F4";
    case GLFW_KEY_F5:
      return "F5";
    case GLFW_KEY_F6:
      return "F6";
    case GLFW_KEY_F7:
      return "F7";
    case GLFW_KEY_F8:
      return "F8";
    case GLFW_KEY_F9:
      return "F9";
    case GLFW_KEY_F10:
      return "F10";
    case GLFW_KEY_F11:
      return "F11";
    case GLFW_KEY_F12:
      return "F12";
    case GLFW_KEY_F13:
      return "F13";
    case GLFW_KEY_F14:
      return "F14";
    case GLFW_KEY_F15:
      return "F15";

    /* Numeric Keypad Keys */
    case GLFW_KEY_KP_ADD:
      return "KEYPAD_ADD";
    case GLFW_KEY_KP_SUBTRACT:
      return "KEYPAD_SUBTRACT";
    case GLFW_KEY_KP_MULTIPLY:
      return "KEYPAD_MULTIPLY";
    case GLFW_KEY_KP_DIVIDE:
      return "KEYPAD_DIVIDE";
    case GLFW_KEY_KP_DECIMAL:
      return "KEYPAD_DECIMAL";
    case GLFW_KEY_KP_ENTER:
      return "KEYPAD_CR";
    case GLFW_KEY_KP_0:
      return "KEYPAD_0";
    case GLFW_KEY_KP_1:
      return "KEYPAD_1";
    case GLFW_KEY_KP_2:
      return "KEYPAD_2";
    case GLFW_KEY_KP_3:
      return "KEYPAD_3";
    case GLFW_KEY_KP_4:
      return "KEYPAD_4";
    case GLFW_KEY_KP_5:
      return "KEYPAD_5";
    case GLFW_KEY_KP_6:
      return "KEYPAD_6";
    case GLFW_KEY_KP_7:
      return "KEYPAD_7";
    case GLFW_KEY_KP_8:
      return "KEYPAD_8";
    case GLFW_KEY_KP_9:
      return "KEYPAD_9";
    case GLFW_KEY_KP_EQUAL:
      return "KEYPAD_EQUAL";

    /* Other keys */
    case GLFW_KEY_CAPS_LOCK:
      return "CAPS_LOCK";
    case GLFW_KEY_NUM_LOCK:
      return "NUM_LOCK";
    case GLFW_KEY_SCROLL_LOCK:
      return "SCROLL_LOCK";
    case GLFW_KEY_PAUSE:
      return "PAUSE";
//    case GLFW_KEY_BREAK:
//      return "BREAK";
    case GLFW_KEY_PRINT_SCREEN:
      return "PRINT_SCREEN";
//    case GLFW_KEY_HELP:
//      return "HELP";
    }
    return character((char) keyCode);
  }

  public static void main(String[] args) {
	  
	HelloWorld hw = new HelloWorld();		

	hw.init();
	  
	GLFWKeyCallbackI keycb = new GLFWKeyCallbackI() {

		@Override
		public void invoke(long window, int key, int scancode, int action, int mods) {
			
			if(action == GLFW_REPEAT) {
				return; //Ignore repeating input...
			}
			
			// TODO Auto-generated method stub
	        String string = action == GLFW_PRESS ? "DOWN:" : "UP  :";
	        string += " stateMask=0x" + Integer.toHexString(mods) + stateMask(mods) + ",";
	        string += " keyCode=0x" + Integer.toHexString(key) + " " + keyCode(key) + ",";
	        string += " character=0x" + Integer.toHexString(key) + " " + character(key);
	        System.out.println(string);
			
		}
		
	};
	
	glfwSetKeyCallback(hw.getWindow(),keycb);
    
    hw.loop();

	// Free the window callbacks and destroy the window
	glfwFreeCallbacks(hw.getWindow());
	glfwDestroyWindow(hw.getWindow());

	// Terminate GLFW and free the error callback
	glfwTerminate();
	glfwSetErrorCallback(null).free();
    
  }
}