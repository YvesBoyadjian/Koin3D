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
 * Copyright (C) 1990-93   Silicon Graphics, Inc.
 * Author(s): David Mott, Alain Dumesny
 * Ported to Qt4 by MeVis (http://www.mevis.de), 2006
 */

package jsceneviewerawt.inventor.qt;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jscenegraph.database.inventor.SbColor;
import jscenegraph.database.inventor.SbTime;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SbViewportRegion;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.elements.SoGLLazyElement;
import jscenegraph.database.inventor.elements.SoLazyElement;
import jscenegraph.database.inventor.events.SoEvent;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoCallback;
import jscenegraph.database.inventor.nodes.SoCallbackCB;
import jscenegraph.database.inventor.nodes.SoGroup;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.interaction.inventor.SoSceneManager;
import jscenegraph.interaction.inventor.SoSceneManager.SoSceneManagerRenderCB;
import jscenegraph.interaction.inventor.nodes.SoSelection;
import jscenegraph.interaction.inventor.nodes.SoSelectionClassCB;
import jsceneviewerawt.inventor.qt.SoQtGLWidget.EventType;
import jsceneviewerawt.inventor.qt.devices.SoQtDevice;

/**
 * @author Yves Boyadjian
 *
 */
//////////////////////////////////////////////////////////////////////////////
//
//Class: SoQtSceneHandler
//
//Class to provide scene handling, core rendering and event translation
//(Extracted from SoQtRenderArea)
//
//////////////////////////////////////////////////////////////////////////////

public abstract class SoQtSceneHandler {
	
	private SoQtRenderArea parent;

    // subclasses have access to the device list for event processing
    private final List<SoQtDevice> deviceList = new ArrayList<>();        // list of devices

	private
	    // these make rendering/redrawing happen
	    SoSceneManager sceneMgr;   // this manages rendering and events
	private    SoGroup        sceneContainer;  // used to take on SoCallback nodes
	private    SoNode         mainScene;       // this is the scene set with setSceneGraph
	private    int            preCallbackCount; // number of pre-scene callback nodes

	private    boolean         clearFirst;       // call clear() before rendering
    private boolean         autoRedraw = true;  // if true, then we automatically redraw
    
    private SoSelection    selection;

    // static callbacks
    private boolean         firstEvent; // used to init the action SoWindowElement
    
    // current size of the rendering surface:
    private final SbVec2s        currentSize = new SbVec2s();

    private final SoQtStandardEventTranslator standardEventTranslator = new SoQtStandardEventTranslator();

    
    private double devicePixelRatio;
    private /*Qt.MouseButtons*/int storedButtonState;
    private /*Qt.KeyboardModifiers*/int storedModifierState;
    private static SoQtSceneHandler lastPositionEventHandler; // last event handler that had a mouse event
    private Point lastEventPosition;
    private Component lastEventWidget;
    private boolean lastEventPositionValid;
    private boolean lockEventHandling; // make sure events can't be handled recursively
    
    
    //! get auto redraw setting
    public boolean      isAutoRedraw() { return autoRedraw; }
    
    //! set the scene manager
    public void        setSceneManager (SoSceneManager sm) { sceneMgr = sm; }
    //! get scene manager
    public SoSceneManager getSceneManager() { return sceneMgr; }

    //! Set GL render action
    public void        setGLRenderAction (SoGLRenderAction ra)
                            { sceneMgr.setGLRenderAction(ra); }
    //! Get GL render action
    public SoGLRenderAction getGLRenderAction() 
                            { return sceneMgr.getGLRenderAction(); }    
    
    public SoQtSceneHandler (SoQtRenderArea parent)
    {
    	this.parent = parent;
    	
        SoQt.init("Dummy");

        devicePixelRatio = 1.0;
      
        firstEvent = true;
        storedButtonState = /*Qt::NoButton*/0;
        storedModifierState = /*Qt::NoModifier*/0;
        lastEventPositionValid = false;
        lockEventHandling = false;

        clearFirst = true;
        autoRedraw = true;
        selection = null;

        sceneMgr = new SoSceneManager();
        sceneMgr.setRenderCallback ( new SoSceneManagerRenderCB() {

			@Override
			public void apply(Object userData, SoSceneManager mgr) {
				renderCB(userData,mgr);
			}
        	
        }     		
        		, this);
        sceneContainer = new SoGroup();
        sceneContainer.ref();
        mainScene = null;
        preCallbackCount = 0;
    }

    public void destructor()
    {
        // Remove our callback from old selection node
        if (selection != null) {
            selection.removeChangeCallback (new SoSelectionClassCB() {

				@Override
				public void invoke(Object userData, SoSelection sel) {
					selectionChangeCB(userData,sel);					
				}
            	
            }            		
            	, this);

            // Unref the old selection node
            selection.unref();
        }

        sceneContainer.unref(); sceneContainer = null;
        sceneMgr.destructor(); sceneMgr = null;
    }


//void registerNamedDevices(final String d)
//{
//  final SoTypeList registeredDeviceTypes = new SoTypeList();
//  foreach (SoQtDevice* dev, deviceList) {
//    registeredDeviceTypes.append(dev->getTypeId());
//  }
//
//  QStringList devices = QString(QLatin1String(d.getString())).split(QRegExp("\\s+"), QString::SkipEmptyParts);
//  foreach (QString dev, devices) {
//    SoType devType = SoType::fromName(dev.toLatin1().constData());
//    if (devType == SoType::badType()) {
//      SoDebugError::post("SoQtSceneHandler::setInputDevicesList", "Type '%s' is unknown to the runtime type system", dev.toLatin1().constData());
//
//      SoTypeList knownDevices;
//      if (SoType::getAllDerivedFrom(SoQtDevice::getClassTypeId(), knownDevices) > 0) {
//        for (int i = 0; i < knownDevices.getLength(); ++i) {
//          if (knownDevices[i].canCreateInstance()) {
//            SoDebugError::postInfo("SoQtSceneHandler::setInputDevicesList", "A known device type name is '%s'", knownDevices[i].getName().getString());
//          }
//        }
//      }
//    }
//    else {
//      if (! devType.isDerivedFrom(SoQtDevice::getClassTypeId())) {
//        SoDebugError::post("SoQtSceneHandler::setInputDevicesList", "Type '%s' is no descendant of SoQtDevice", dev.toLatin1().constData());
//      }
//      else {
//        if (registeredDeviceTypes.find(devType) >= 0) {
//          SoDebugError::post("SoQtSceneHandler::setInputDevicesList", "A device of type '%s' is already registered", dev.toLatin1().constData());
//        }
//        else {
//          if (! devType.canCreateInstance()) {
//            SoDebugError::post("SoQtSceneHandler::setInputDevicesList", "Type '%s' cannot create an instance", dev.toLatin1().constData());
//          }
//          else {
//            SoQtDevice *qd = (SoQtDevice *)devType.createInstance();
//            if (! qd) {
//              SoDebugError::post("SoQtSceneHandler::setInputDevicesList", "Could not create an instance of type '%s'", dev.toLatin1().constData());
//            }
//            else {
//              registerDevice(qd);
//            }
//          }
//        }
//      }
//    }
//  }
//}    

public void initializeScene(int shareID)
{
    sceneMgr.reinitialize();

    SoGLRenderAction ra = sceneMgr.getGLRenderAction();
    ra.setCacheContext(shareID);
}

void
resizeScene (int width, int height, double devicePixelRatio)
{
  this.devicePixelRatio = devicePixelRatio;
  
  currentSize.copyFrom(new SbVec2s((short)width, (short)height));

  sceneMgr.setWindowSize(currentSize);

  for (int i=0; i < deviceList.size(); i++) {
    deviceList.get(i).setWindowSize(currentSize);
    deviceList.get(i).setDevicePixelRatio(devicePixelRatio);
  }
}


//! override this to set the SoWindowElement for the scene
	protected void setWindowElement(SoState state) {}

//! override this to enable/disable devices for the correct widget
	protected Component getDeviceWidget() { return null; }


//! override this to initiate a redraw of the scene
	protected abstract void updateScene();

//! override this to tell if the scene should be considered visible
//! (also look at the comments of activate/deactivate)
	protected abstract boolean isSceneVisible();

    

    //! activate/deactivate scene update sensors, call this if the return value
    //! of isSceneVisible changes (you will probably have to call activate()
    //! at least once!)
    public void
    activate()
    {
        // if autoRedraw is off, then don't attach the scene sensor
        if (! autoRedraw) {
            return;
        }

        // Activate the scene manager
        sceneMgr.activate();
    }

    public void
    deactivate()
    {
        // detach sensors
        sceneMgr.deactivate();
    }

    

void
setAutoRedraw(boolean flag)
{
    if (flag == autoRedraw) {
        return;
    }

    autoRedraw = flag;

    if (autoRedraw) {
        if (isSceneVisible()) {
            activate();
        }
    } else {
        deactivate();
    }
}

    
    //! Paint/render the Inventor scene using the current OpenGL context.
    //! It also resets the lazy element and resends the last mouse position.
    //! After this, actualRedraw() is called.
    public void paintScene() {
    	  // Reset the lazy element state to make sure that
    	  // Inventor resends all GL state and does not
    	  // expect the GL state to be like it was on the last
    	  // paintScene() call. This is important when rendering
    	  // to FBOs on a shared GL context.
    	  // It has the performance overhead of resending more GL
    	  // state, but that is insignificant on todays hardware/drivers.
    	  SoState state = sceneMgr.getGLRenderAction().getState();
    	  if (state != null) {
    	    SoGLLazyElement glLazyElement = SoGLLazyElement.getInstance(state);
    	    if (glLazyElement != null) {
    	      glLazyElement.reset(state, SoLazyElement.masks.ALL_MASK.getValue());
    	    }
    	  }

    	  resendLastMousePosition();
    	  actualRedraw();    	
    }

    //! Re-sends the last mouse position as a mouse move event, e.g. because the scene
    //! layout has changed spontaneously and the mouse positions needs to be re-evaluated.
    public void resendLastMousePosition() {
        if (lastEventPositionValid && (lastPositionEventHandler == this)) {
        	MouseEvent e = new SoQtStandardEventTranslator.LocationRefreshMouseEvent(
        	        lastEventWidget,
                    MouseEvent.MOUSE_MOVED,
                    System.currentTimeMillis(),
                    storedModifierState +	storedButtonState,
                    lastEventPosition.x,
                    lastEventPosition.y,
                    0,
                    false
                    );
        	//e.widget = lastEventWidget;
        	//e.x = lastEventPosition.x;
        	//e.y = lastEventPosition.y;
        	//e.stateMask = storedModifierState +	storedButtonState;
            MouseEvent me = e;
            processSceneEvent(me, EventType.LOCATION_REFRESH_MOUSE_EVENT);
          }
    }


public void
scheduleRedraw()
{
    if (isAutoRedraw()) {
        sceneMgr.scheduleRedraw();
    } else {
        updateScene();
    }
}

public abstract void actualRedraw();

public void
doActualRedraw()
{
    sceneMgr.render (clearFirst);
}


public void
setSceneGraph(SoNode newScene)
{
    // Deactivate while we change the scene so that our sensors
    // get unhooked before the data changes beneath us.
    sceneMgr.deactivate();

    if (mainScene != null) {
        sceneContainer.removeChild (mainScene);
        mainScene = null;
    }
    if (newScene != null) {
        // insert scene node after the pre-scene callback nodes, but before the post-scene ones
        sceneContainer.insertChild (newScene, preCallbackCount);
    }
    mainScene = newScene;

    sceneMgr.setSceneGraph(sceneContainer); // treat as totally new scene

    // we activate only if we are visible.
    // after all, if we're not on screen, the visibility change
    // callback will invoke activate() once we are on screen.
    if (isSceneVisible() && autoRedraw) {
        sceneMgr.activate();
        sceneMgr.scheduleRedraw();
    }
}

public SoNode 
getSceneGraph()
{
    return mainScene;
}

SoCallback addSceneCallback (SoCallbackCB func, Object userData, boolean afterScene)
{
    SoCallback cb = new SoCallback();
    cb.setCallback (func, userData);
    if (afterScene) {
        sceneContainer.addChild (cb);
    } else {
        sceneContainer.insertChild (cb, 0);
        preCallbackCount++;
    }
    return cb;
}

boolean removeSceneCallback (SoCallback callbackNode)
{
    if (callbackNode != null && (callbackNode != mainScene)) { // check against removing the main scene
        int index = sceneContainer.findChild (callbackNode);
        boolean ok = (index >= 0);
        sceneContainer.removeChild (callbackNode);
        if (ok && index < preCallbackCount) { preCallbackCount--; } // we removed a pre-scene callback node
        return ok;
    } else {
        return false;
    }
}
    
    //! This must be called to pass events to the scene
    public void processSceneEvent (ComponentEvent anyEvent, EventType type) {
        boolean isKeyEvent = false;
        boolean isMouseEvent = false;
        boolean isKnownInputEvent = false;
        // check for special key which turns viewing on/off
        if (anyEvent instanceof KeyEvent)
        {
            isKeyEvent = true;
        } else if (anyEvent instanceof MouseEvent)
        {
            isMouseEvent = true;
        }
        if (isKeyEvent || isMouseEvent )
        {
            isKnownInputEvent = true;
        }

        if (lockEventHandling) {
          if (isKnownInputEvent) {
           // qWarning("SoQtSceneHandler: Recursive event handling; event was ignored.");
          }
          return;
        }
        lockEventHandling = true;

        if (isMouseEvent && (type != EventType.MOUSE_EVENT_MOUSE_MOVE)) {
          MouseEvent me = ((MouseEvent)anyEvent);
          if (( !Objects.equals(new Point(me.getX(),me.getY()), lastEventPosition)) ||
              !lastEventPositionValid || (lastPositionEventHandler == null))
          {
            // synthesize event MouseMove to get to press/release position;
            // many OpenInventor nodes only act on the last mouse move before the release,
            // but especially in remote rendering scenarios these positions are not the same
        	  MouseEvent e = new MouseEvent(
        	          me.getComponent(),
                      MouseEvent.MOUSE_MOVED,
                      System.currentTimeMillis(),
                      storedButtonState + storedModifierState,
                      me.getX(),
                      me.getY(),
                      0,
                      false
              );
        	  //e.widget = me.widget;
        	  //e.x = me.x;
        	  //e.y = me.y;
        	  //e.stateMask = storedButtonState + storedModifierState;
            MouseEvent synthEvent = e;
            translateAndSendEvent(synthEvent, EventType.MOUSE_EVENT_MOUSE_MOVE);

          }
        }
        // restore modifier state because some key events may not have reached us:
        if (isKeyEvent || isMouseEvent) {
        	int modifier = 0;
        	if(isKeyEvent) {
        		modifier = ((KeyEvent)anyEvent).getModifiersEx()/* & SWT.MODIFIER_MASK*/;
        	}
        	else if(isMouseEvent) {
        		modifier = ((MouseEvent)anyEvent).getModifiersEx()/* & SWT.MODIFIER_MASK*/;
        	}
            synthesizeCurrentModifierState (modifier, anyEvent.getComponent());
        }
        // restore button state because some mouse events may not have reached us:
        if (isMouseEvent) {
            synthesizeCurrentButtonState ((MouseEvent)anyEvent, type);
        }
        // pass event to inventor scene:
        translateAndSendEvent(anyEvent, type);
        // store last button state for comparison (shouldn't really be necessary):
        if (isKeyEvent || isMouseEvent) {
        	if(isKeyEvent) {
        		storedModifierState = ((KeyEvent)anyEvent).getModifiersEx() /*& SWT.MODIFIER_MASK*/;
        	}
        	else if(isMouseEvent) {
        		storedModifierState = ((MouseEvent)anyEvent).getModifiersEx()/*& SWT.MODIFIER_MASK*/;
        	}
            //storedModifierState = ((QInputEvent)anyEvent).modifiers();
            if (isKeyEvent) {
                // with modifier key events the modifier mask changes _after_ the event:
                KeyEvent ke = ((KeyEvent)anyEvent);
                if (ke.getKeyCode() == KeyEvent.VK_SHIFT) {
                    storedModifierState ^= KeyEvent.SHIFT_DOWN_MASK;
                }
                if (ke.getKeyCode() == KeyEvent.VK_CONTROL) {
                    storedModifierState ^= KeyEvent.CTRL_DOWN_MASK;
                }
                if (ke.getKeyCode() == KeyEvent.VK_ALT) {
                    storedModifierState ^= KeyEvent.ALT_DOWN_MASK;
                }
            }
        }

        lockEventHandling = false;

        if (isMouseEvent) {
            storedButtonState = buttons((MouseEvent)anyEvent);
            lastEventPosition = new Point(((MouseEvent)anyEvent).getX(),((MouseEvent)anyEvent).getY());
            lastEventWidget = anyEvent.getComponent();
            lastEventPositionValid = true;
            lastPositionEventHandler = this;
        } else if (type == EventType.MOUSE_EVENT_MOUSE_EXIT) {
            lastEventPositionValid = false;
        }    	
    }
    

void synthesizeCurrentButtonState (MouseEvent me, EventType type)
{
	
    // synthesize input state _before_ this event
    int buttonState = (type == EventType.MOUSE_EVENT_MOUSE_MOVE) ? buttons(me) : (buttons(me) ^ button(me));
    if (buttonState != storedButtonState) {
        // first generate button release events for every button that is different:
        synthesizeButtonState (buttonState, MouseEvent.BUTTON1,  me, false);
        synthesizeButtonState (buttonState, MouseEvent.BUTTON2,   me, false);
        synthesizeButtonState (buttonState, MouseEvent.BUTTON3, me, false);
        // then generate button press events for every button that is different:
        synthesizeButtonState (buttonState, MouseEvent.BUTTON1,  me, true);
        synthesizeButtonState (buttonState, MouseEvent.BUTTON2,   me, true);
        synthesizeButtonState (buttonState, MouseEvent.BUTTON3, me, true);
    }
}

private static final int buttons(MouseEvent me) {
	return (me.getModifiersEx() & (MouseEvent.BUTTON1_DOWN_MASK | MouseEvent.BUTTON2_DOWN_MASK |MouseEvent.BUTTON3_DOWN_MASK ) ) ^ button(me);
}

private static final int button(MouseEvent me) {
	int buttonCode = 0;
	switch(me.getButton()) {
	case 1: buttonCode = MouseEvent.BUTTON1; break;
	case 2: buttonCode = MouseEvent.BUTTON2; break;
	case 3: buttonCode = MouseEvent.BUTTON3; break;
	}
	return buttonCode;
}

void synthesizeButtonState (int newButtons,
                                         int button,
                                         MouseEvent me, boolean press)
{
    // generate a button press or release event if the button state of the selected button
    // is different and the new state correlates with the "press" flag:
    if ((((storedButtonState ^ newButtons) & button) != 0) &&
        (((newButtons & button) != 0) == press))
    {
        storedButtonState ^= button;
        // the event is generated at the same position as the final event:
        MouseEvent e = new MouseEvent(
                me.getComponent(),
                press ? MouseEvent.MOUSE_PRESSED : MouseEvent.MOUSE_RELEASED,
                System.currentTimeMillis(),
                storedButtonState,
                me.getX(),
                me.getY(),
                1,
                false,
                button
        );
//        e.stateMask = storedButtonState + (me.stateMask & SWT.MODIFIER_MASK);
        MouseEvent synthEvent = e;
        translateAndSendEvent(synthEvent, press ? EventType.MOUSE_EVENT_MOUSE_DOWN : EventType.MOUSE_EVENT_MOUSE_UP);
    }
}


void synthesizeCurrentModifierState (int modifiers, Component widget)
{
    if (modifiers != storedModifierState) {
        // first generate key release events for every modifier that is different:
        synthesizeModifierState (modifiers, KeyEvent.SHIFT_DOWN_MASK,   KeyEvent.VK_SHIFT,   false, widget);
        synthesizeModifierState (modifiers, KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_CONTROL, false, widget);
        synthesizeModifierState (modifiers, KeyEvent.ALT_DOWN_MASK, KeyEvent.VK_ALT,     false, widget);
        // then generate key press events for every modifier that is different:
        synthesizeModifierState (modifiers, KeyEvent.SHIFT_DOWN_MASK,   KeyEvent.VK_SHIFT,   true, widget);
        synthesizeModifierState (modifiers, KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_CONTROL, true, widget);
        synthesizeModifierState (modifiers, KeyEvent.ALT_DOWN_MASK, KeyEvent.VK_ALT,     true, widget);
    }
}


void synthesizeModifierState (int newModifiers,
                                           int modifier,
                                           int keyCode, boolean press, Component widget)
{
    // generate a button press or release event if the button state of the selected button
    // is different and the new state correlates with the "press" flag:
    if ((((storedModifierState ^ newModifiers) & modifier) != 0) &&
        (((newModifiers & modifier) != 0) == press))
    {
    	KeyEvent e = new KeyEvent(
    	        widget,
                press ? KeyEvent.KEY_PRESSED : KeyEvent.KEY_RELEASED,
                System.currentTimeMillis(),
                storedModifierState,
                keyCode
        );
        KeyEvent synthEvent = e;
        storedModifierState ^= modifier;
        translateAndSendEvent(synthEvent, press ? EventType.KEY_EVENT_KEY_PRESSED : EventType.KEY_EVENT_KEY_RELEASED);
    }
}



void translateAndSendEvent(ComponentEvent anyevent, EventType type)
{
    // send event to the scene
    final SoEvent soevent = translateEvent (anyevent, type);
    
    // if no translation possible, return...
    if (soevent == null) { return; }

    // now send the event to
    // the regular scene graph.
    boolean handled = parent.processSoEvent(soevent);//sceneMgr.processEvent (soevent);
    // now check to make sure that we updated the handle event action
    // with the current window the very first time. This is needed
    // because the SoState does not exists until the action is
    // applied, and we only update those during enter/leave notify.
    if (firstEvent) {
      SoState state = sceneMgr.getHandleEventAction().getState();
      if (state != null) {
        setWindowElement(state);
        firstEvent = false;
      }
    }

    // consume event if it was handled
    //anyevent.setAccepted (handled);
}


public void registerDevice (SoQtDevice d)
{
    int index = deviceList.indexOf (d);
    if (index < 0) {
        // only append if not yet contained in list
        deviceList.add (d);
    }

    // tell the device the window size
    d.setWindowSize(currentSize);
    d.setDevicePixelRatio(devicePixelRatio);

    // Tell the device to register event interest for our widget
    Component w = getDeviceWidget();
    if (w != null) {
        d.enable(w, /*(SoQtEventHandler) SoQtGLWidget::eventHandler*/ null, (Object) this);
    }
}

void unregisterDevice (SoQtDevice d)
{
    int index = deviceList.indexOf (d);
    if (index < 0) {
        return;
    }

    deviceList.remove (deviceList.indexOf (d));

    // Tell the device to unregister event interest for our widget
    Component w = getDeviceWidget();
    if (w != null) {
        d.disable(w, /*(SoQtEventHandler) SoQtGLWidget::eventHandler*/ null, (Object) this);
    }
}

void detachAllDevices()
{
  Component w = getDeviceWidget();
  if (w == null)
    return;

  for (int i=0; i < deviceList.size(); i++) {
    deviceList.get(i).disable(w, /*(SoQtEventHandler) SoQtGLWidget::eventHandler*/ null, (Object) this);
  }
}

void reattachAllDevices()
{
  Component w = getDeviceWidget();
  if (w == null)
    return;

  for (int i=0; i < deviceList.size(); i++) {
    deviceList.get(i).enable(w, /*(SoQtEventHandler) SoQtGLWidget::eventHandler*/ null, (Object) this);
  }
}


//! Set the window background color when in RGB mode (defaults to black (0,0,0))
public void        setBackgroundColor (final SbColor c)
                            { sceneMgr.setBackgroundColor(c); }
//! Get the window background color when in RGB mode
public SbColor  getBackgroundColor() 
                            { return sceneMgr.getBackgroundColor(); }

//! Set current viewport region to use for rendering
public void        setViewportRegion (final SbViewportRegion newRegion)
                { sceneMgr.getGLRenderAction().setViewportRegion(newRegion); }
//! Get current viewport region to use for rendering
public SbViewportRegion getViewportRegion() 
                { return sceneMgr.getGLRenderAction().getViewportRegion(); }

//! Transparency level setting methods which specifies how
//! transparent objects are rendered (quality/speed trade off).
public void        setTransparencyType (SoGLRenderAction.TransparencyType type) {
    sceneMgr.getGLRenderAction().setTransparencyType(type);
    sceneMgr.scheduleRedraw();	
}
//! Get transparency level setting
public SoGLRenderAction.TransparencyType  getTransparencyType()
                { return sceneMgr.getGLRenderAction().getTransparencyType(); }


//! Set anti-aliasing hints, returns true if the values have indeed changed
public boolean setAntialiasing (boolean smoothing, int numPasses)
{
    SoGLRenderAction ra = getGLRenderAction();

    if (smoothing != ra.isSmoothing() || numPasses != ra.getNumPasses()) {

        // this must probably set first (don't know when the redraw happens
        // from the format change:
        sceneMgr.setAntialiasing(smoothing, numPasses);
        return true;
    } else {
        return false;
    }
}


//! Get anti-aliasing hints
public void        getAntialiasing (final boolean[] smoothing, final int[] numPasses) 
                        { sceneMgr.getAntialiasing(smoothing, numPasses); }

//! Enable/prevent window clearing from happening before a rendering
//! starts (default is clearBeforeRender TRUE).
public void        setClearBeforeRender(boolean trueOrFalse) { clearFirst = trueOrFalse; }
//! get window clearing setting
public boolean      isClearBeforeRender() { return clearFirst; }



public void redrawOnSelectionChange (SoSelection s)
{
    if (s != selection) {

        // Remove our callback from old selection node
        if (selection != null) {
            selection.removeChangeCallback (
            		new SoSelectionClassCB() {

						@Override
						public void invoke(Object userData, SoSelection sel) {
							selectionChangeCB(userData,sel);
						}
            				
            		}
            		
            		, this);
            // Unref the old selection node
            selection.unref();
        }
        selection = s;
        // Add our callback to this selection node. (We've already ref'd this new sel node)
        if (selection != null) {
            selection.addChangeCallback (
            		new SoSelectionClassCB() {

						@Override
						public void invoke(Object userData, SoSelection sel) {
							selectionChangeCB(userData,sel);
						}
            				
            		}
            		, this);
            // Ref the new selection node
            selection.ref();
        }
    }
}

void selectionChangeCB(Object p, SoSelection sel)
{
    ((SoQtSceneHandler )p).scheduleRedraw();
}


void
renderCB(Object p, SoSceneManager manager)
{
    ((SoQtSceneHandler )p).updateScene();
}

SoEvent translateEvent(ComponentEvent anyEvent, EventType type)
{
    // send event to the scene
    SoEvent soevent = null;

    // process list of special devices
    for (int i = 0; (soevent == null) && (i < deviceList.size()); i++) {
        SoQtDevice device = deviceList.get(i);
        soevent = device.translateEvent (anyEvent, type);
    }

    // if no device found, try standard translations for mouse and keyboard
    if ( soevent == null) {
        soevent = translateStdEvent (anyEvent, type);
    } else {
        // Set event time stamp in any case, otherwise we might risk
        // inconsistent time stamps between internally and externally
        // translated events. Since QEvent has no time stamps, we must do
        // something like this in any case...
        // [Casting to non-const is ugly, but changing the signature of
        // SoQtDevice::translateEvent is somewhat too much hassle. Setting
        // the time on the event should be possible in any case.]
        ((SoEvent)soevent).setTime (SbTime.getTimeOfDay());
    }

    return soevent;
}

SoEvent translateStdEvent(ComponentEvent xe, EventType type)
{
  return standardEventTranslator.translateEvent(xe, type, currentSize, devicePixelRatio);
}
    
}
