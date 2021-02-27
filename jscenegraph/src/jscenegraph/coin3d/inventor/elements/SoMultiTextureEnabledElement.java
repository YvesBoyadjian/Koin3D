/**************************************************************************\
 * Copyright (c) Kongsberg Oil & Gas Technologies AS
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 
 * Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
\**************************************************************************/

/*!
  \class SoMultiTextureEnabledElement Inventor/elements/SoMultiTextureEnabledElement.h
  \brief The SoMultiTextureEnabledElement class is an element which stores whether texturing is enabled or not.
  \ingroup elements

  \COIN_CLASS_EXTENSION

  \since Coin 2.2
*/

/*!
  \fn void SoMultiTextureEnabledElement::set(SoState * state, const SbBool enabled)
  
  Coin-3 support.
*/

/*!
  \fn void SoMultiTextureEnabledElement::set(SoState * state, SoNode* node, const SbBool enabled)
  
  Coin-3 support.
*/

package jscenegraph.coin3d.inventor.elements;

import jscenegraph.coin3d.inventor.lists.SbList;
import jscenegraph.database.inventor.elements.SoElement;
import jscenegraph.database.inventor.elements.SoShapeStyleElement;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoMultiTextureEnabledElement extends SoElement {

	public
		  enum Mode {
		    DISABLED ( 0),
		    TEXTURE2D(1),
		    RECTANGLE(2),
		    CUBEMAP(3),
		    TEXTURE3D(4);
			  
			  private int value;
			  
			  Mode(int value) {
				  this.value = value;
			  }
			  
			  public int getValue() {
				  return value;
			  }

			public static Mode fromValue(int mode_in) {
				switch(mode_in) {
				case 0: return DISABLED;
				case 1: return TEXTURE2D;
				case 2: return RECTANGLE;
				case 3: return CUBEMAP;
				case 4: return TEXTURE3D;
				}
				return null;
			}
		  };

		final SbList<Boolean> enabled = new SbList<>();
		final SbList<SoMultiTextureEnabledElement.Mode> mode = new SbList<>();
	
		  
/*!
  The constructor.
*/
public SoMultiTextureEnabledElement()
{
  //PRIVATE(this) = new SoMultiTextureEnabledElementP;

  //this.setTypeId(SoMultiTextureEnabledElement::classTypeId);
  //this->setStackIndex(SoMultiTextureEnabledElement::classStackIndex);
}

/*!
  The destructor.
*/
public void destructor()
{
  //delete PRIVATE(this);
	super.destructor();
}

/*!
  Sets the state of this element.
*/
public static void
set(SoState state,
                                  SoNode node,
                                   int unit,
                                  boolean enabled)
{
  SoMultiTextureEnabledElement elem =
    (SoMultiTextureEnabledElement)
    (
     state.getElement(classStackIndexMap.get(SoMultiTextureEnabledElement.class))
     );

  elem.setElt(unit, enabled ? 1 : 0); //FIXME
  
  if (unit == 0) {
    // FIXME: check all units?
    SoShapeStyleElement.setTextureEnabled(state, enabled);
  }
}


// doc from parent
public void
init(SoState state)
{
  mode.truncate(0);
  enabled.truncate(0);
}

/*!
  Return current state of this element for \a unit.
*/
public static boolean
get(SoState state, int unit)
{
  SoMultiTextureEnabledElement elem =
    (SoMultiTextureEnabledElement)
    (SoElement.getConstElement(state, classStackIndexMap.get(SoMultiTextureEnabledElement.class)));

  if (unit < elem.enabled.getLength()) {
    return elem.enabled.operator_square_bracket(unit);
  }
  return false;
}

/*!
  virtual element set function.
*/
public void
setElt( int unit, int mode_in)
{
  Mode mode1 = Mode.fromValue(mode_in);
  for (int i = enabled.getLength(); i <= unit; i++) { 
    enabled.append(false);
    mode.append(Mode.DISABLED);
  }
  enabled.operator_square_bracket(unit, mode1 != Mode.DISABLED);
  mode.operator_square_bracket(unit, mode1);
}

/*!
  Returns a pointer to a boolean array. true means unit is enabled and
  that texture coordinates must be sent to the unit. \a lastenabled
  is set to the last enabled unit.

*/
public static boolean[] 
getEnabledUnits(SoState state,
                                             final int[] lastenabled)
{
  SoMultiTextureEnabledElement elem =
    (SoMultiTextureEnabledElement)
    (SoElement.getConstElement(state, classStackIndexMap.get(SoMultiTextureEnabledElement.class)));

  int i = elem.enabled.getLength()-1;
  while (i >= 0) {
    if (elem.enabled.operator_square_bracket(i)) break;
    i--;
  }
  if (i >= 0) {
    lastenabled[0] = i;
    Boolean[] retVal =  elem.enabled.getArrayPtr(new Boolean[elem.enabled.getLength()]);
    boolean[] retValn = new boolean[elem.enabled.getLength()];
    for ( i=0;i<elem.enabled.getLength();i++) {
    	retValn[i] = retVal[i];
    }
    return retValn;
  }
  lastenabled[0] = -1;
  return null;
}

/*!
  Returns true if unit is enabled (Mode == DISABLED).
*/
protected boolean
isEnabled( int unit) 
{
  if (unit < enabled.getLength()) {
    return enabled.operator_square_bracket(unit);
  }
  return false;
}

// doc in parent
public void
push(SoState state)
{
  SoMultiTextureEnabledElement prev = (SoMultiTextureEnabledElement)
    (this.getNextInStack());

  mode.operator_assign(prev.mode);
  enabled.operator_assign(prev.enabled);
}

public boolean
matches( SoElement elem)
{
  SoMultiTextureEnabledElement e =
    (SoMultiTextureEnabledElement)(elem);
  if (e.mode.getLength() != mode.getLength()) return false;
  
  for (int i = 0; i < e.mode.getLength(); i++) {
    if (e.mode.operator_square_bracket(i) != mode.operator_square_bracket(i)) {
      return false;
    }
  }
  return true;
}

public SoElement 
copyMatchInfo()
{
  SoMultiTextureEnabledElement elem =
    (SoMultiTextureEnabledElement)(getTypeId().createInstance());

  for (int i = 0; i < mode.getLength(); i++) {
    elem.mode.append(mode.operator_square_bracket(i));
  }
  return elem;
}

/*!
  Returns the mode of all units. Also returns the last enabled unit
  in \a lastenabled.

  \since Coin 2.5
*/
public static SoMultiTextureEnabledElement.Mode[]
getActiveUnits(SoState state, final int[] lastenabled)
{
  SoMultiTextureEnabledElement elem =
    (SoMultiTextureEnabledElement)
    (SoElement.getConstElement(state, classStackIndexMap.get(SoMultiTextureEnabledElement.class)));
  
  int i = elem.mode.getLength()-1;
  while (i >= 0) {
    if (elem.mode.operator_square_bracket(i) != Mode.DISABLED) break;
    i--;
  }
  if (i >= 0) {
    lastenabled[0] = i;
    return elem.mode.getArrayPtr(new SoMultiTextureEnabledElement.Mode[elem.mode.getLength()]);
  }
  return null;
}

/*!
  Enable RECTANGLE texture mode.

  \since Coin 2.5
*/
public static void
enableRectangle(SoState state,
                                              SoNode node,
                                              int unit)
{
  SoMultiTextureEnabledElement elem =
    (SoMultiTextureEnabledElement)
    (state.getElement(classStackIndexMap.get(SoMultiTextureEnabledElement.class)));
  elem.setElt(unit, (int)(Mode.RECTANGLE.getValue()));
}

/*!
  Enable CUBEMAP texture mode.

  \since Coin 2.5
*/
public static void
enableCubeMap(SoState state,
                                            SoNode node,
                                             int unit)
{
  SoMultiTextureEnabledElement elem = (SoMultiTextureEnabledElement)
    (state.getElement(classStackIndexMap.get(SoMultiTextureEnabledElement.class)));

  elem.setElt(unit, (int)(Mode.CUBEMAP.getValue()));
}

/*!
  Enable Texture3 texture mode.

  \since Coin 4.0
*/
public static void
enableTexture3(SoState state,
                                             SoNode node,
                                             int unit)
{
  SoMultiTextureEnabledElement elem = (SoMultiTextureEnabledElement)
    (state.getElement(classStackIndexMap.get(SoMultiTextureEnabledElement.class)));
  
  elem.setElt(unit, (int)(Mode.TEXTURE3D.getValue()));
}

/*!

  Disable all active texture units. Convenient when all textures needs
  to be disabled before rendering.

  \since Coin 2.5
*/
public static void
disableAll(SoState state)
{
  final int[] lastenabled = new int[1];
  boolean[] enabled = getEnabledUnits(state, lastenabled);
  if (enabled != null) {
    SoMultiTextureEnabledElement elem =
      (SoMultiTextureEnabledElement)
      (state.getElement(classStackIndexMap.get(SoMultiTextureEnabledElement.class)));

    for (int i = 0; i <= lastenabled[0]; i++) {
      if (enabled[i]) {
        elem.setElt(i, /*false*/0);
      }
    }
  }
  SoShapeStyleElement.setTextureEnabled(state, false);
}

/*!
  Returns the mode for a texture unit.

  \since Coin 2.5
*/
public static SoMultiTextureEnabledElement.Mode
getMode(SoState state) // java port
{
	return getMode(state,0);
}
public static SoMultiTextureEnabledElement.Mode
getMode(SoState state, int unit)
{
  final SoMultiTextureEnabledElement elem =
    (SoMultiTextureEnabledElement)
    (SoElement.getConstElement(state, classStackIndexMap.get(SoMultiTextureEnabledElement.class)));

  return elem.getMode(unit);
}

/*!
  Returns the max number of texture units enabled/disabled
  \since Coin 4.0
 */
protected int 
getMaxUnits()
{
  return mode.getLength();
}

//
// returns the texture mode for a unit.
//
protected SoMultiTextureEnabledElement.Mode getMode( int unit)
{
  if (unit < mode.getLength()) {
    return mode.operator_square_bracket(unit);
  }
  return Mode.DISABLED;
}

//#undef PRIVATE
	
}
