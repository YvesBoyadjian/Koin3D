/**
 * 
 */
package jsceneviewerawt;

import java.awt.event.KeyEvent;

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

public class KeyCodeCharacterState {

  static String stateMask(int stateMask) {
    String string = "";
    if ((stateMask & KeyEvent.CTRL_DOWN_MASK) != 0)
      string += " CTRL";
    if ((stateMask & KeyEvent.ALT_DOWN_MASK) != 0)
      string += " ALT";
    if ((stateMask & KeyEvent.SHIFT_DOWN_MASK) != 0)
      string += " SHIFT";
//    if ((stateMask & SWT.COMMAND) != 0)
//      string += " COMMAND";
    return string;
  }

  static String character(char character) {
    switch (character) {
    case 0:
      return "'\\0'";
    case KeyEvent.VK_BACK_SPACE:
      return "'\\b'";
//    case SWT.CR:
//      return "'\\r'";
    case KeyEvent.VK_DELETE:
      return "DEL";
    case KeyEvent.VK_ESCAPE:
      return "ESC";
    case KeyEvent.VK_ENTER:
      return "'\\n'";
    case KeyEvent.VK_TAB:
      return "'\\t'";
    }
    return "'" + character + "'";
  }

  static String keyCode(int keyCode) {
    switch (keyCode) {

    /* Keyboard and Mouse Masks */
    case KeyEvent.VK_ALT:
      return "ALT";
    case KeyEvent.VK_SHIFT:
      return "SHIFT";
    case KeyEvent.VK_CONTROL:
      return "CONTROL";
//    case KeyEvent.VK_COMMAND:
//      return "COMMAND";

    /* Non-Numeric Keypad Keys */
    case KeyEvent.VK_UP:
      return "ARROW_UP";
    case KeyEvent.VK_DOWN:
      return "ARROW_DOWN";
    case KeyEvent.VK_LEFT:
      return "ARROW_LEFT";
    case KeyEvent.VK_RIGHT:
      return "ARROW_RIGHT";
    case KeyEvent.VK_PAGE_UP:
      return "PAGE_UP";
    case KeyEvent.VK_PAGE_DOWN:
      return "PAGE_DOWN";
    case KeyEvent.VK_HOME:
      return "HOME";
    case KeyEvent.VK_END:
      return "END";
    case KeyEvent.VK_INSERT:
      return "INSERT";

    /* Virtual and Ascii Keys */
//    case KeyEvent.VK_BS:
//      return "BS";
//    case KeyEvent.VK_CR:
//      return "CR";
      case KeyEvent.VK_DELETE:
      return "DEL";
      case KeyEvent.VK_ESCAPE:
      return "ESC";
//    case KeyEvent.VK_LF:
//      return "LF";
    case KeyEvent.VK_TAB:
      return "TAB";

    /* Functions Keys */
    case KeyEvent.VK_F1:
      return "F1";
    case KeyEvent.VK_F2:
      return "F2";
    case KeyEvent.VK_F3:
      return "F3";
    case KeyEvent.VK_F4:
      return "F4";
    case KeyEvent.VK_F5:
      return "F5";
    case KeyEvent.VK_F6:
      return "F6";
    case KeyEvent.VK_F7:
      return "F7";
    case KeyEvent.VK_F8:
      return "F8";
    case KeyEvent.VK_F9:
      return "F9";
    case KeyEvent.VK_F10:
      return "F10";
    case KeyEvent.VK_F11:
      return "F11";
    case KeyEvent.VK_F12:
      return "F12";
    case KeyEvent.VK_F13:
      return "F13";
    case KeyEvent.VK_F14:
      return "F14";
    case KeyEvent.VK_F15:
      return "F15";

    /* Numeric Keypad Keys */
//    case KeyEvent.VK_KEYPAD_ADD:
//      return "KEYPAD_ADD";
//    case KeyEvent.VK_KEYPAD_SUBTRACT:
//      return "KEYPAD_SUBTRACT";
//    case KeyEvent.VK_KEYPAD_MULTIPLY:
//      return "KEYPAD_MULTIPLY";
//    case KeyEvent.VK_KEYPAD_DIVIDE:
//      return "KEYPAD_DIVIDE";
//    case KeyEvent.VK_KEYPAD_DECIMAL:
//      return "KEYPAD_DECIMAL";
//    case KeyEvent.VK_KP_CR:
//      return "KEYPAD_CR";
    case KeyEvent.VK_NUMPAD0:
      return "KEYPAD_0";
    case KeyEvent.VK_NUMPAD1:
      return "KEYPAD_1";
    case KeyEvent.VK_NUMPAD2:
      return "KEYPAD_2";
    case KeyEvent.VK_NUMPAD3:
      return "KEYPAD_3";
    case KeyEvent.VK_NUMPAD4:
      return "KEYPAD_4";
    case KeyEvent.VK_NUMPAD5:
      return "KEYPAD_5";
    case KeyEvent.VK_NUMPAD6:
      return "KEYPAD_6";
    case KeyEvent.VK_NUMPAD7:
      return "KEYPAD_7";
    case KeyEvent.VK_NUMPAD8:
      return "KEYPAD_8";
      case KeyEvent.VK_NUMPAD9:
      return "KEYPAD_9";
//    case KeyEvent.VK_KP_EQUAL:
//      return "KEYPAD_EQUAL";

    /* Other keys */
    case KeyEvent.VK_CAPS_LOCK:
      return "CAPS_LOCK";
    case KeyEvent.VK_NUM_LOCK:
      return "NUM_LOCK";
    case KeyEvent.VK_SCROLL_LOCK:
      return "SCROLL_LOCK";
    case KeyEvent.VK_PAUSE:
      return "PAUSE";
//    case KeyEvent.VK_BREAK:
//      return "BREAK";
    case KeyEvent.VK_PRINTSCREEN:
      return "PRINT_SCREEN";
    case KeyEvent.VK_HELP:
      return "HELP";
    }
    return character((char) keyCode);
  }

//  public static void main(String[] args) {
//    Display display = new Display();
//    Shell shell = new Shell(display);
//    Listener listener = new Listener() {
//      public void handleEvent(Event e) {
//        String string = e.type == SWT.KeyDown ? "DOWN:" : "UP  :";
//        string += " stateMask=0x" + Integer.toHexString(e.stateMask) + stateMask(e.stateMask) + ",";
//        string += " keyCode=0x" + Integer.toHexString(e.keyCode) + " " + keyCode(e.keyCode) + ",";
//        string += " character=0x" + Integer.toHexString(e.character) + " " + character(e.character);
//        System.out.println(string);
//      }
//    };
//    shell.addListener(SWT.KeyDown, listener);
//    shell.addListener(SWT.KeyUp, listener);
//    shell.setSize(200, 200);
//
//    shell.setText("Press Key on the blank window");
//
//    shell.open();
//    while (!shell.isDisposed()) {
//      if (!display.readAndDispatch())
//        display.sleep();
//    }
//    display.dispose();
//  }
}