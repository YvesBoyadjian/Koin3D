///////////////////////////////////////////////////////////////////////////////
//  SoTerrain
///////////////////////////////////////////////////////////////////////////////
/// Prohl��pro voln pohyb ve sc��pomoc�myi a kl�esnice.
/// \file SoQtFreeViewer.h
/// \author Radek Barto�- xbarto33
/// \date 06.03.2006
///
/// Prohl��pro voln pohyb ve sc��pomoc�myi a kl�esnice.
/// Zobraz�okno se sc�ou, ve kter�je mono se pohybovat pomoc�myi a
/// kl�esnice. Kurzor myi je oknem zachycen a skryt, take je mono ot�et
/// pohledem do vech sm� dokola. Pozici kamery lze ovl�at t�ito kl�esami:
/// <DL>
///   <DT>W</DT><DD>posun kamery kupedu</DD>
///   <DT>S</DT><DD>posun kamery dozadu</DD>
///   <DT>A</DT><DD>posun kamery doleva</DD>
///   <DT>D</DT><DD>posun kamery doprava</DD>
///   <DT>Q</DT><DD>posun kamery nahoru</DD>
///   <DT>E</DT><DD>posun kamery dol</DD>
///   <DT>LEFT</DT><DD>nato�n�kamery doleva</DD>
///   <DT>RIGHT</DT><DD>nato�n�kamery doprava</DD>
///   <DT>UP</DT><DD>nato�n�kamery nahoru</DD>
///   <DT>DOWN</DT><DD>nato�n�kamery dol</DD>
///   <DT>PGUP</DT><DD>naklon��kamery doleva</DD>
///   <DT>PGDOWN</DT><DD>naklon��kamery doprava</DD>
/// </DL>
//////////////////////////////////////////////////////////////////////////////
// Copyright (C) 2006 Radek Barton
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
///////////////////////////////////////////////////////////////////////////////

package jterrain;

import java.awt.AWTException;
import java.awt.Robot;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import jscenegraph.database.inventor.SbRotation;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.events.SoEvent;
import jscenegraph.database.inventor.events.SoKeyboardEvent;
import jscenegraph.database.inventor.events.SoLocation2Event;
import jscenegraph.database.inventor.events.SoMouseButtonEvent;
import jscenegraph.database.inventor.nodes.SoCamera;
import jsceneviewer.inventor.qt.SoQtCameraController.Type;
import jsceneviewer.inventor.qt.viewers.SoQtViewer;

/**
 * @author Yves Boyadjian
 *
 */
public class SoQtFreeViewer extends SoQtViewer {

	protected float sensitivity;
	protected boolean invert;
	
/******************************************************************************
* SoQtFreeViewer - public
******************************************************************************/

	/**
	 * Java port
	 * @param parent
	 */
	public SoQtFreeViewer(Composite parent) {
		this(parent,null,true);
	}
	
public SoQtFreeViewer(Composite parent, String name,
  boolean embed) {
  super(parent, name, embed, Type.BROWSER, true);
  sensitivity = 1.0f; invert = false;

  constructorCommon(true);
  setKeyCommandsEnabled(false); // java port
}

public void destructor()
{
	super.destructor();
}

public float getMouseSensitivity()
{
  /* Vraceni citlivosti mysi. */
  return sensitivity;
}

public void setMouseSensitivity( float _sensitivity)
{
  /* Nastaveni citlivosti mysi. */
  sensitivity = _sensitivity;
}

public boolean getInvertMouse()
{
  /* Ziskani priznaku invertovane mysi. */
  return invert;
}

public void setInvertMouse( boolean enabled)
{
  /* Nastaveni priznaku invertovane mysi. */
  invert = enabled;
}


/******************************************************************************
* SoQtFreeViewer - protected
******************************************************************************/

protected SoQtFreeViewer(Composite parent, String name,
  boolean embed, boolean build) {
  super(parent, name, embed, Type.BROWSER, build);
  sensitivity = 1.0f; invert = false;

  constructorCommon(build);
}

protected boolean processSoEvent( SoEvent event)
{
  boolean result = false;

  /* Zjisteni typu udalosti a zavolani prislusne obsluzne rutiny. */
  if (event.isOfType(SoLocation2Event.getClassTypeId()))
  {
    result = processSoLocation2Event((SoLocation2Event)
      (event));
  }
  else if (event.isOfType(SoKeyboardEvent.getClassTypeId()))
  {
    result = processSoKeyboardEvent((SoKeyboardEvent)
      (event));
  }
  else if (event.isOfType(SoMouseButtonEvent.getClassTypeId()))
  {
    result = processSoMouseButtonEvent((SoMouseButtonEvent)
      (event));
  }

  if (!result)
  {
    setViewing(false);
    result = super.processSoEvent(event);
    setViewing(true);
  }

  return result;
}

static final SbVec2s old_position = new SbVec2s();

protected boolean processSoLocation2Event( SoLocation2Event _event)
{
  SoCamera  camera = getCameraController().getCamera();

  /* Zjisteni zmeny pozice kurzoru. */
  final SbVec2s position = getCursorPosition();
  old_position.copyFrom( position);
  final SbVec2s diff = old_position.operator_minus(position);

  float rotation_x = sensitivity * 0.001f * diff.getValue()[1];
  rotation_x = invert ? -rotation_x : rotation_x;
  float rotation_z = sensitivity * 0.001f * diff.getValue()[0];

  /* Rotace v X ose. */
  camera.orientation.setValue( new SbRotation(new SbVec3f(1.0f, 0.0f, 0.0f), rotation_x).operator_mul(
    camera.orientation.getValue()));

  /* Rotace v Z ose. */
  camera.orientation.setValue( camera.orientation.getValue().operator_mul(
    new SbRotation(new SbVec3f(0.0f, 0.0f, 1.0f), rotation_z)));

  old_position.copyFrom( getPosition().operator_add( getCenter()));
  // setCursorPosition(old_position); YB
  return true;
}

protected boolean processSoKeyboardEvent( SoKeyboardEvent event)
{
  SoCamera camera = getCameraController().getCamera();
  SbVec3f old_position = camera.position.getValue();
  SbRotation old_orientation = camera.orientation.getValue();

  if (SoKeyboardEvent.SO_KEY_PRESS_EVENT(event, SoKeyboardEvent.Key.ESCAPE))
  {
    System.exit(0);//SoQt.exitMainLoop();
    return true;
  }
  else if (SoKeyboardEvent.SO_KEY_PRESS_EVENT(event, SoKeyboardEvent.Key.ENTER))
  {
    if (event.wasAltDown())
    {
      //setFullScreen(!isFullScreen());
      return true;
    }
  }
  else if (SoKeyboardEvent.SO_KEY_PRESS_EVENT(event, SoKeyboardEvent.Key.A))
  {
    final SbVec3f diff_position = new SbVec3f(-0.01f, 0.0f, 0.0f);
    old_orientation.multVec(diff_position, diff_position);
    camera.position.setValue( old_position.operator_add(diff_position));
    return true;
  }
  else if (SoKeyboardEvent.SO_KEY_PRESS_EVENT(event, SoKeyboardEvent.Key.D))
  {
    SbVec3f diff_position = new SbVec3f(0.01f, 0.0f, 0.0f);
    old_orientation.multVec(diff_position, diff_position);
    camera.position.setValue( old_position.operator_add(diff_position));
    return true;
  }
  else if (SoKeyboardEvent.SO_KEY_PRESS_EVENT(event, SoKeyboardEvent.Key.Q))
  {
    SbVec3f diff_position = new SbVec3f(0.0f, -0.01f, 0.0f);
    old_orientation.multVec(diff_position, diff_position);
    camera.position.setValue( old_position.operator_add( diff_position));
    return true;
  }
  else if (SoKeyboardEvent.SO_KEY_PRESS_EVENT(event, SoKeyboardEvent.Key.E))
  {
    SbVec3f diff_position = new SbVec3f(0.0f, 0.01f, 0.0f);
    old_orientation.multVec(diff_position, diff_position);
    camera.position.setValue(old_position.operator_add( diff_position));
    return true;
  }
  else if (SoKeyboardEvent.SO_KEY_PRESS_EVENT(event, SoKeyboardEvent.Key.W))
  {
    SbVec3f diff_position = new SbVec3f(0.0f, 0.0f, -0.01f);
    old_orientation.multVec(diff_position, diff_position);
    camera.position.setValue( old_position.operator_add( diff_position));
    return true;
  }
  else if (SoKeyboardEvent.SO_KEY_PRESS_EVENT(event, SoKeyboardEvent.Key.S))
  {
    SbVec3f diff_position = new SbVec3f(0.0f, 0.0f, 0.01f);
    old_orientation.multVec(diff_position, diff_position);
    camera.position.setValue( old_position.operator_add(diff_position));
    return true;
  }
  else if (SoKeyboardEvent.SO_KEY_PRESS_EVENT(event, SoKeyboardEvent.Key.UP_ARROW))
  {
    SbRotation diff_orientation = new SbRotation(new SbVec3f(1.0f, 0.0f, 0.0f), 0.02f);
    camera.orientation.setValue(diff_orientation.operator_mul( old_orientation));
    return true;
  }
  else if (SoKeyboardEvent.SO_KEY_PRESS_EVENT(event, SoKeyboardEvent.Key.DOWN_ARROW))
  {
    SbRotation diff_orientation = new SbRotation(new SbVec3f(1.0f, 0.0f, 0.0f), -0.02f);
    camera.orientation.setValue(diff_orientation.operator_mul( old_orientation));
    return true;
  }
  else if (SoKeyboardEvent.SO_KEY_PRESS_EVENT(event, SoKeyboardEvent.Key.LEFT_ARROW))
  {
    SbRotation diff_orientation = new SbRotation(new SbVec3f(0.0f, 0.0f, 1.0f), 0.02f);
    camera.orientation.setValue(  old_orientation.operator_mul( diff_orientation));
    return true;
  }
  else if (SoKeyboardEvent.SO_KEY_PRESS_EVENT(event, SoKeyboardEvent.Key.RIGHT_ARROW))
  {
    SbRotation diff_orientation = new SbRotation(new SbVec3f(0.0f, 0.0f, 1.0f), -0.02f);
    camera.orientation.setValue( old_orientation.operator_mul( diff_orientation));
    return true;
  }
  else if (SoKeyboardEvent.SO_KEY_PRESS_EVENT(event, SoKeyboardEvent.Key.PAGE_UP))
  {
    SbRotation diff_orientation = new SbRotation(new SbVec3f(0.0f, 0.0f, 1.0f), 0.02f);
    camera.orientation.setValue(  diff_orientation.operator_mul( old_orientation));
    return true;
  }
  else if (SoKeyboardEvent.SO_KEY_PRESS_EVENT(event, SoKeyboardEvent.Key.PAGE_DOWN))
  {
    SbRotation diff_orientation = new SbRotation(new SbVec3f(0.0f, 0.0f, 1.0f), -0.02f);
    camera.orientation.setValue(  diff_orientation.operator_mul( old_orientation));
    return true;
  }

  return false;
}

protected boolean processSoMouseButtonEvent( SoMouseButtonEvent event)
{
  return false;
}

/******************************************************************************
* SoQtFreeViewer - private
******************************************************************************/

private void constructorCommon(boolean buildNow)
{
  if (buildNow)
  {
    /*Composite widget =*/ buildWidget(/*getParentWidget()*/0);
    //setBaseWidget(widget);
  }

  setCursor(/*SoQtCursor.getBlankCursor()*/null);
}

private SbVec2s getCenter()
{
  /* Ziskani stredu okna relativne. */
  Composite widget = getParentWidget();
  return new SbVec2s((short)(widget.getSize().x / 2), (short)(widget.getSize().y / 2));
}

private SbVec2s getPosition()
{
  /* Ziskani pocatku okna vuci obrazovce. */
  Composite widget = getParentWidget();
  Point position = widget.toDisplay(0, 0);
  return new SbVec2s((short)position.x, (short)position.y);
}

private void setCursorPosition(final SbVec2s position)
{
  /* Nastaveni absolutni pozice kurzoru. */
	try {
		new Robot().mouseMove(position.getValue()[0], position.getValue()[1]);
	} catch (AWTException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  
}

 private SbVec2s getCursorPosition()
{
  /* Ziskani absolutni pozice kurzoru. */
  //QPoint position = QCursor.pos();
  Point position = Display.getCurrent().getCursorLocation();		  
  return new SbVec2s((short)position.x, (short)position.y);
}
	
}
