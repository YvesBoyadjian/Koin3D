/*
*
*  Copyright (C) 2000 Silicon Graphics, Inc.  All Rights Reserved.
*
*  This library is free software; you can redistribute it and/or
*  modify it under the terms of the GNU Lesser General Public
*  License as published by the Free Software Foundation; either
*  version 2.1 of the License, or (at your option) any later version.
*
*  This library is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
*  Lesser General Public License for more details.
*
*  Further, this software is distributed without any warranty that it is
*  free of the rightful claim of any third person regarding infringement
*  or the like.  Any license provided herein, whether implied or
*  otherwise, applies only to this software file.  Patent licenses, if
*  any, provided herein do not apply to combinations of this program with
*  other software, or any other product whatsoever.
*
*  You should have received a copy of the GNU Lesser General Public
*  License along with this library; if not, write to the Free Software
*  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*
*  Contact information: Silicon Graphics, Inc., 1600 Amphitheatre Pkwy,
*  Mountain View, CA  94043, or:
*
*  http://www.sgi.com
*
*  For further information regarding this notice, see:
*
*  http://oss.sgi.com/projects/GenInfo/NoticeExplan/
*
*/

/*
* Copyright 2010 by MeVis Medical Solutions AG
* Author(s): Uwe Siems, Stephan Palmer
*/

package jsceneviewerawt.inventor.qt;

import jscenegraph.database.inventor.SbTime;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.events.SoButtonEvent;
import jscenegraph.database.inventor.events.SoButtonEvent.State;
import jscenegraph.database.inventor.events.SoEvent;
import jscenegraph.database.inventor.events.SoKeyboardEvent;
import jscenegraph.database.inventor.events.SoLocation2Event;
import jscenegraph.database.inventor.events.SoMouseButtonEvent;
import jscenegraph.mevis.inventor.events.SoLocation2RefreshEvent;
import jsceneviewerawt.inventor.qt.SoQtGLWidget.EventType;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * @author Yves Boyadjian
 *
 */
/**
 * This class encapsulates the translation from Qt event to Open Inventor event for a default set of mouse and keyboard
 * events.
 * Intended usage: Classes which want to use the event translation should add a member of this class. Since objects of
 * this class have an internal state which depends on the render area (the area which produces the Qt event) they are
 * used in, every render area needs its own instance of this class.
 * @note The position of Open Inventor key events will default to (-1,-1) until at least one mouse event was processed
 *       by an object of this class.
 */
public class SoQtStandardEventTranslator {

	  /**
	   * The following values are used to remember the position of the last evaluated mouse event. This is done since
	   * in Open Inventor, all events (even keyboard events) have a location, but Qt events have not.
	   * Values default to (-1,-1) until the first mouse event is processed.
	   */
	  //@{
	  private int lastMouseX = -1;
	  private int lastMouseY = -1;
	  //@}

	  /**
	   * Reused Open Inventor event objects which are filled with values of the translated Qt event.
	   */
	  //@{
	  private final SoMouseButtonEvent buttonEvent = new SoMouseButtonEvent();
	  private final SoLocation2Event   loc2Event = new SoLocation2Event();
	  private final SoKeyboardEvent    keyEvent = new SoKeyboardEvent();
	  private final SoLocation2RefreshEvent loc2RefreshEvent = new SoLocation2RefreshEvent();
	  //@}

    public static class LocationRefreshMouseEvent extends MouseEvent {

        public LocationRefreshMouseEvent(Component source, int id, long when, int modifiers, int x, int y, int clickCount, boolean popupTrigger, int button) {
            super(source, id, when, modifiers, x, y, clickCount, popupTrigger, button);
        }

        public LocationRefreshMouseEvent(Component source, int id, long when, int modifiers, int x, int y, int clickCount, boolean popupTrigger) {
            super(source, id, when, modifiers, x, y, clickCount, popupTrigger);
        }

        public LocationRefreshMouseEvent(Component source, int id, long when, int modifiers, int x, int y, int xAbs, int yAbs, int clickCount, boolean popupTrigger, int button) {
            super(source, id, when, modifiers, x, y, xAbs, yAbs, clickCount, popupTrigger, button);
        }
    }

    /**
	   * Translate given Qt event.
	   * @param anyEvent The event to be translated.
	   * @param viewportSize Width and height of the current viewport
	   * @return Pointer to Open Inventor event if translation was successful. NULL if event type is not supported.
	   * @note Open Inventor and Qt coordinate systems have a different point of origin (lower left vs. upper left). Thus
	   *       the current viewport dimensions are required to use this method. Furthermore, the returned pointer points
	   *       to a member of this class. It must never be deleted, since ownership remains with this class!
	   */
	   public SoEvent translateEvent (ComponentEvent anyEvent, EventType type, final SbVec2s viewportSize) {
		   return translateEvent(anyEvent,type,viewportSize,1.0);
	   }

	   public SoEvent translateEvent(ComponentEvent anyEvent, EventType type, final SbVec2s viewportSize, double devicePixelRatio)
{
  SoEvent event = null;
  
  MouseEvent mouseEvent = null;
  
//  if (anyEvent->type() == QEvent::MouseButtonPress ||
//      anyEvent->type() == QEvent::MouseButtonRelease ||
//      anyEvent->type() == QEvent::MouseButtonDblClick ||
//      anyEvent->type() == QEvent::MouseMove ||
//      anyEvent->type() == locationRefreshEventType()) {
//    QMouseEvent me = static_cast<QMouseEvent >(anyEvent);
//    mouseEvent = new QMouseEvent(anyEvent->type(), me->pos() * devicePixelRatio, me->button(), me->buttons(), me->modifiers());
//  }
  if(anyEvent instanceof MouseEvent) {
	  mouseEvent = (MouseEvent)anyEvent;
  }
  
  if (anyEvent instanceof LocationRefreshMouseEvent) {
    event = translateLocationRefreshEvent(mouseEvent, viewportSize);
  } else {
    // switch on event type
    //switch (anyEvent.getClass()) {
      //case QEvent::MouseButtonPress:
      //case QEvent::MouseButtonDblClick:
      //  event = translateButtonEvent(mouseEvent, SoButtonEvent::DOWN, viewportSize);
        //break;
      //case QEvent::MouseButtonRelease:
      //  event = translateButtonEvent(mouseEvent, SoButtonEvent::UP, viewportSize);
        //break;
  if( anyEvent instanceof MouseEvent /*&& mouseEvent.button != 0*/ &&
		  ( 
		  type == EventType.MOUSE_EVENT_MOUSE_DOWN) ||
		  type == EventType.MOUSE_EVENT_DOUBLE_CLICK ||
		  type == EventType.MOUSE_EVENT_MOUSE_UP
		  ){
        	event = translateButtonEvent(mouseEvent, type, viewportSize);
  }
  else if( anyEvent instanceof MouseEvent) {
      //case QEvent::MouseMove:
        event = translateMotionEvent(mouseEvent, viewportSize);
  }
  else if ( anyEvent instanceof KeyEvent) {
	  //SoButtonEvent.State whichState = State.UNKNOWN;
	  if(type == EventType.KEY_EVENT_KEY_PRESSED) {
	        event = translateKeyEvent((KeyEvent ) anyEvent, SoButtonEvent.State.DOWN, viewportSize);
	  }
	  else {
	        event = translateKeyEvent((KeyEvent ) anyEvent, SoButtonEvent.State.UP, viewportSize);
	  }
      //case QEvent::KeyPress:
        //break;
      //case QEvent::KeyRelease:
        //break;
  }
//      case QEvent::Wheel:
//        // wheel events are currently missing a translation,
//        // since there is no standard Inventor wheel event
//        break;
//
//      default:
//        break;
//    }
  }
  
  return event;
}

	   	private void fillInEventState (SoEvent event, ComponentEvent ie, final SbVec2s viewportSize)
	   {
	     // mirror y-position:
	     event.setPosition (new SbVec2s ((short)lastMouseX, (short)(viewportSize.getValue()[1] - lastMouseY)));
	     event.setTime (SbTime.getTimeOfDay());
	     int stateMask = 0;
	     if( ie instanceof KeyEvent) {
	    	 stateMask = ((KeyEvent)ie).getModifiersEx();
	     }
	     else if ( ie instanceof MouseEvent) {
	    	 stateMask = ((MouseEvent)ie).getModifiersEx();
	     }
	     event.setShiftDown ((stateMask & InputEvent.SHIFT_DOWN_MASK)!=0);
	     event.setCtrlDown ((stateMask & InputEvent.CTRL_DOWN_MASK)!=0);
	     event.setAltDown ((stateMask & InputEvent.ALT_DOWN_MASK)!=0);
	   }


private SoMouseButtonEvent translateButtonEvent (MouseEvent be, EventType type, final SbVec2s viewportSize)
{
  SoMouseButtonEvent.Button whichButton;
  switch (be.getButton()) {
    case 1:
      whichButton = SoMouseButtonEvent.Button.BUTTON1;
      break;
    case 2:
      whichButton = SoMouseButtonEvent.Button.BUTTON2;
      break;
    case 3:
      whichButton = SoMouseButtonEvent.Button.BUTTON3;
      break;
    default:
      whichButton = SoMouseButtonEvent.Button.ANY;
  }
  lastMouseX = be.getX();
  lastMouseY = be.getY();
  fillInEventState (buttonEvent, be, viewportSize);
  
  SoButtonEvent.State whichState = State.UNKNOWN;
  if(type == EventType.MOUSE_EVENT_MOUSE_DOWN || type == EventType.MOUSE_EVENT_DOUBLE_CLICK) {
	  whichState = State.DOWN;
  }
  else if(type == EventType.MOUSE_EVENT_MOUSE_UP){
	  whichState = State.UP;
  }

  buttonEvent.setState (whichState);
  buttonEvent.setButton (whichButton);
  buttonEvent.setIsDoubleClick(be.getClickCount() > 1);

  return buttonEvent;
}


private SoLocation2Event translateMotionEvent (MouseEvent me, final SbVec2s viewportSize)
{
  lastMouseX = me.getX();
  lastMouseY = me.getY();
  fillInEventState (loc2Event, me, viewportSize);

  //    qDebug("mouse move %d %d", loc2Event->getPosition()[0],loc2Event->getPosition()[1]);
  return loc2Event;
}



private SoLocation2Event translateLocationRefreshEvent (MouseEvent me, final SbVec2s viewportSize)
{
  fillInEventState (loc2RefreshEvent, me, viewportSize);

  //    qDebug("location refresh %d %d", loc2Event->getPosition()[0],loc2Event->getPosition()[1]);
  return loc2RefreshEvent;
}

private SoKeyboardEvent.Key lookupKey (int qtKeyCode, boolean keyPad)
{
  // NOTE: SoKeyboardConstants.h has provided an exact mapping
  // from X11/keysymdef.h for keyboard symbols. So this file was
  // referenced for missing key codes.
  // some key codes have been skipped out of lazyness...
  if (keyPad) {
    // keypad keys are marked with a modifier flag under Qt
    switch (qtKeyCode) {
            //case SWT.Key_Comma:   // better safe than sorry
            case KeyEvent.VK_DECIMAL:  return SoKeyboardEvent.Key.PAD_PERIOD;
        case KeyEvent.VK_NUMPAD0:       return SoKeyboardEvent.Key.PAD_0;
            case KeyEvent.VK_NUMPAD1:       return SoKeyboardEvent.Key.PAD_1;
            case KeyEvent.VK_NUMPAD2:       return SoKeyboardEvent.Key.PAD_2;
            case KeyEvent.VK_NUMPAD3:       return SoKeyboardEvent.Key.PAD_3;
            case KeyEvent.VK_NUMPAD4:       return SoKeyboardEvent.Key.PAD_4;
            case KeyEvent.VK_NUMPAD5:       return SoKeyboardEvent.Key.PAD_5;
            case KeyEvent.VK_NUMPAD6:       return SoKeyboardEvent.Key.PAD_6;
            case KeyEvent.VK_NUMPAD7:       return SoKeyboardEvent.Key.PAD_7;
            case KeyEvent.VK_NUMPAD8:       return SoKeyboardEvent.Key.PAD_8;
            case KeyEvent.VK_NUMPAD9:       return SoKeyboardEvent.Key.PAD_9;
            case KeyEvent.VK_ADD:    return SoKeyboardEvent.Key.PAD_ADD;
            case KeyEvent.VK_SUBTRACT: return SoKeyboardEvent.Key.PAD_SUBTRACT;
            case KeyEvent.VK_MULTIPLY: return SoKeyboardEvent.Key.PAD_MULTIPLY;
            case KeyEvent.VK_DIVIDE:   return SoKeyboardEvent.Key.PAD_DIVIDE;
        case KeyEvent.VK_ENTER:   return SoKeyboardEvent.Key.PAD_ENTER;
              // Qt (Version 4.2.2) doesn't get the numpad modifier right for MacOSX:
//#if !defined(__APPLE__)
              // Keypad-Keys without numlock, but we return the number codes anyway:
        case KeyEvent.VK_DELETE:  return SoKeyboardEvent.Key.PAD_PERIOD;
            case KeyEvent.VK_INSERT:  return SoKeyboardEvent.Key.PAD_0;
            case KeyEvent.VK_END:     return SoKeyboardEvent.Key.PAD_1;
            case KeyEvent.VK_DOWN:    return SoKeyboardEvent.Key.PAD_2;
            case KeyEvent.VK_PAGE_DOWN: return SoKeyboardEvent.Key.PAD_3;
            case KeyEvent.VK_LEFT:    return SoKeyboardEvent.Key.PAD_4;
            //case SWT.Key_Clear:   return SoKeyboardEvent.Key.PAD_5;
            case KeyEvent.VK_RIGHT:   return SoKeyboardEvent.Key.PAD_6;
            case KeyEvent.VK_HOME:    return SoKeyboardEvent.Key.PAD_7;
            case KeyEvent.VK_UP:      return SoKeyboardEvent.Key.PAD_8;
            case KeyEvent.VK_PAGE_UP:  return SoKeyboardEvent.Key.PAD_9;
              // These ones you normally don't get with a PC keyboard:
//            case KeyEvent.F1:      return SoKeyboardEvent.Key.PAD_F1;
//            case KeyEvent.F2:      return SoKeyboardEvent.Key.PAD_F2;
//            case KeyEvent.F3:      return SoKeyboardEvent.Key.PAD_F3;
//            case KeyEvent.F4:      return SoKeyboardEvent.Key.PAD_F4;
//            case KeyEvent.SPACE:   return SoKeyboardEvent.Key.PAD_SPACE;
//            case KeyEvent.TAB:     return SoKeyboardEvent.Key.PAD_TAB;
//#endif
    }
  }
  switch (qtKeyCode) {
        case KeyEvent.VK_ESCAPE:      return SoKeyboardEvent.Key.ESCAPE;
        case KeyEvent.VK_TAB:         return SoKeyboardEvent.Key.TAB;
          // case SWT.Key_Backtab:     return SoKeyboardEvent.Key.ANY;
        case KeyEvent.VK_BACK_SPACE: return SoKeyboardEvent.Key.BACKSPACE;
        //case KeyEvent.VK_ENTER:      return SoKeyboardEvent.Key.RETURN;
      case KeyEvent.VK_ENTER:       return SoKeyboardEvent.Key.ENTER;
        case KeyEvent.VK_INSERT:      return SoKeyboardEvent.Key.INSERT;
        case KeyEvent.VK_DELETE:      return SoKeyboardEvent.Key.KEY_DELETE; // taken from X11/keysymdef.h
        case KeyEvent.VK_PAUSE:       return SoKeyboardEvent.Key.PAUSE;
        case KeyEvent.VK_PRINTSCREEN:       return SoKeyboardEvent.Key.PRINT;
        //case SWT.Key_SysReq:      return (SoKeyboardEvent.Key.Key) 0xFF15; // taken from X11/keysymdef.h
          // case SWT.Key_Clear:       return SoKeyboardEvent.Key.ANY;
        case KeyEvent.VK_HOME:        return SoKeyboardEvent.Key.HOME;
        case KeyEvent.VK_END:         return SoKeyboardEvent.Key.END;
        case KeyEvent.VK_LEFT:        return SoKeyboardEvent.Key.LEFT_ARROW;
        case KeyEvent.VK_UP:          return SoKeyboardEvent.Key.UP_ARROW;
        case KeyEvent.VK_RIGHT:       return SoKeyboardEvent.Key.RIGHT_ARROW;
        case KeyEvent.VK_DOWN:        return SoKeyboardEvent.Key.DOWN_ARROW;
        case KeyEvent.VK_PAGE_UP:      return SoKeyboardEvent.Key.PAGE_UP;
        case KeyEvent.VK_PAGE_DOWN:    return SoKeyboardEvent.Key.PAGE_DOWN;
        case KeyEvent.VK_SHIFT:       return SoKeyboardEvent.Key.LEFT_SHIFT;
        case KeyEvent.VK_CONTROL:     return SoKeyboardEvent.Key.LEFT_CONTROL;
        //case SWT.Key_Meta:        return (SoKeyboardEvent.Key.Key) 0xFFE7; // taken from X11/keysymdef.h
        case KeyEvent.VK_ALT:         return SoKeyboardEvent.Key.LEFT_ALT;
        //case SWT.Key_AltGr:       return SoKeyboardEvent.Key.RIGHT_ALT;
        case KeyEvent.VK_CAPS_LOCK:    return SoKeyboardEvent.Key.CAPS_LOCK;
        case KeyEvent.VK_NUM_LOCK:     return SoKeyboardEvent.Key.NUM_LOCK;
        case KeyEvent.VK_SCROLL_LOCK:  return SoKeyboardEvent.Key.SCROLL_LOCK;
//        case SWT.Key_Super_L:     return (SoKeyboardEvent.Key.Key) 0xFFEB; // taken from X11/keysymdef.h
//        case SWT.Key_Super_R:     return (SoKeyboardEvent.Key.Key) 0xFFEC; // taken from X11/keysymdef.h
//        case SWT.Key_Menu:        return (SoKeyboardEvent.Key.Key) 0xFF67; // taken from X11/keysymdef.h
//        case SWT.Key_Hyper_L:     return (SoKeyboardEvent.Key.Key) 0xFFED; // taken from X11/keysymdef.h
//        case SWT.Key_Hyper_R:     return (SoKeyboardEvent.Key.Key) 0xFFEE; // taken from X11/keysymdef.h
//        case SWT.Key_Help:        return (SoKeyboardEvent.Key.Key) 0xFF6A; // taken from X11/keysymdef.h
          // case SWT.Key_Direction_L: return SoKeyboardEvent.Key.ANY;
          // case SWT.Key_Direction_R: return SoKeyboardEvent.Key.ANY;
        case KeyEvent.VK_A:           return SoKeyboardEvent.Key.A;
        case KeyEvent.VK_B:           return SoKeyboardEvent.Key.B;
        case KeyEvent.VK_C:           return SoKeyboardEvent.Key.C;
        case KeyEvent.VK_D:           return SoKeyboardEvent.Key.D;
        case KeyEvent.VK_E:           return SoKeyboardEvent.Key.E;
        case KeyEvent.VK_F:           return SoKeyboardEvent.Key.F;
        case KeyEvent.VK_G:           return SoKeyboardEvent.Key.G;
        case KeyEvent.VK_H:           return SoKeyboardEvent.Key.H;
        case KeyEvent.VK_I:           return SoKeyboardEvent.Key.I;
        case KeyEvent.VK_J:           return SoKeyboardEvent.Key.J;
        case KeyEvent.VK_K:           return SoKeyboardEvent.Key.K;
        case KeyEvent.VK_L:           return SoKeyboardEvent.Key.L;
        case KeyEvent.VK_M:           return SoKeyboardEvent.Key.M;
        case KeyEvent.VK_N:           return SoKeyboardEvent.Key.N;
        case KeyEvent.VK_O:           return SoKeyboardEvent.Key.O;
        case KeyEvent.VK_P:           return SoKeyboardEvent.Key.P;
        case KeyEvent.VK_Q:           return SoKeyboardEvent.Key.Q;
        case KeyEvent.VK_R:           return SoKeyboardEvent.Key.R;
        case KeyEvent.VK_S:           return SoKeyboardEvent.Key.S;
        case KeyEvent.VK_T:           return SoKeyboardEvent.Key.T;
        case KeyEvent.VK_U:           return SoKeyboardEvent.Key.U;
        case KeyEvent.VK_V:           return SoKeyboardEvent.Key.V;
        case KeyEvent.VK_W:           return SoKeyboardEvent.Key.W;
        case KeyEvent.VK_X:           return SoKeyboardEvent.Key.X;
        case KeyEvent.VK_Y:           return SoKeyboardEvent.Key.Y;
        case KeyEvent.VK_Z:           return SoKeyboardEvent.Key.Z;
//        case SWT.Key_Multi_key:         return (SoKeyboardEvent.Key) 0xFF20; // taken from X11/keysymdef.h
//        case SWT.Key_Codeinput:         return (SoKeyboardEvent.Key) 0xFF37; // taken from X11/keysymdef.h
//        case SWT.Key_SingleCandidate:   return (SoKeyboardEvent.Key) 0xFF3C; // taken from X11/keysymdef.h
//        case SWT.Key_MultipleCandidate: return (SoKeyboardEvent.Key) 0xFF3D; // taken from X11/keysymdef.h
//        case SWT.Key_PreviousCandidate: return (SoKeyboardEvent.Key) 0xFF3E; // taken from X11/keysymdef.h
//        case SWT.Key_Mode_switch:       return (SoKeyboardEvent.Key) 0xFF7E; // taken from X11/keysymdef.h
        default:
          if (qtKeyCode >= KeyEvent.VK_F1 && qtKeyCode <= KeyEvent.VK_F24) {
            // handle function keys economically
            return SoKeyboardEvent.Key.fromValue(qtKeyCode - KeyEvent.VK_F1 + SoKeyboardEvent.Key.F1.getValue());
          } else if (((qtKeyCode >= 0x20) && (qtKeyCode <= 0x7f)) ||
                     ((qtKeyCode >= 0xa0) && (qtKeyCode <= 0xff)))
          {
            // ascii+latin1 key codes are the same, except for the
            // letters A-Z (upper vs. lower case) which are handled above
            return SoKeyboardEvent.Key.fromValue(qtKeyCode);
          } else {
            return SoKeyboardEvent.Key.ANY;
          }
  }
}


private SoKeyboardEvent translateKeyEvent (KeyEvent ke, SoButtonEvent.State whichState, final SbVec2s viewportSize)
{
  SoKeyboardEvent.Key whichKey = lookupKey (ke.getKeyCode(), ke.getKeyLocation() == KeyEvent.KEY_LOCATION_NUMPAD);
  fillInEventState (keyEvent, ke, viewportSize);
  // Qt returns the modifiers _before_ the event, but we need them after the event:
  if ((ke.getModifiersEx() & KeyEvent.ALT_DOWN_MASK)!=0) {
    keyEvent.setAltDown (whichState == SoButtonEvent.State.DOWN);
  }
  if ((ke.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK)!=0) {
    keyEvent.setCtrlDown (whichState == SoButtonEvent.State.DOWN);
  }
  if ((ke.getModifiersEx() & KeyEvent.SHIFT_DOWN_MASK)!=0) {
    keyEvent.setShiftDown (whichState == SoButtonEvent.State.DOWN);
  }
  keyEvent.setState (whichState);
  keyEvent.setKey (whichKey);
  //keyEvent.setIsAutoRepeat(ke.isAutoRepeat());
  return keyEvent;
}	  
}
