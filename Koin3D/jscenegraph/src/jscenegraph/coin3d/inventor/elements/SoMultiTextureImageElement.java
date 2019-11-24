
/**************************************************************************\
 *
 *  This file is part of the Coin 3D visualization library.
 *  Copyright (C) by Kongsberg Oil & Gas Technologies.
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  ("GPL") version 2 as published by the Free Software Foundation.
 *  See the file LICENSE.GPL at the root directory of this source
 *  distribution for additional information about the GNU GPL.
 *
 *  For using Coin with software that can not be combined with the GNU
 *  GPL, and for taking advantage of the additional benefits of our
 *  support services, please contact Kongsberg Oil & Gas Technologies
 *  about acquiring a Coin Professional Edition License.
 *
 *  See http://www.coin3d.org/ for more information.
 *
 *  Kongsberg Oil & Gas Technologies, Bygdoy Alle 5, 0257 Oslo, NORWAY.
 *  http://www.sim.no/  sales@sim.no  coin-support@coin3d.org
 *
\**************************************************************************/

package jscenegraph.coin3d.inventor.elements;

import jscenegraph.coin3d.inventor.lists.SbList;
import jscenegraph.coin3d.inventor.lists.SbListOfMutableRefs;
import jscenegraph.database.inventor.SbColor;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SbVec3s;
import jscenegraph.database.inventor.elements.SoElement;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.port.Mutable;
import jscenegraph.port.memorybuffer.MemoryBuffer;

/**
 * @author Yves Boyadjian
 *
 */
/*!
  \class SoMultiTextureImageElement Inventor/elements/SoMultiTextureImageElement.h
  \brief The SoMultiTextureImageElement class is yet to be documented.
  \ingroup elements

  FIXME: write doc.

  \COIN_CLASS_EXTENSION

  \since Coin 2.2
*/

public class SoMultiTextureImageElement extends SoElement {
	
	  public enum Model {
		    // These should match GL_BLEND, GL_MODULATE and GL_DECAL for SGI
		    // Inventor compatibility (these are also used by SoTexture2 and
		    // SoTexture3).
		    BLEND (0x0be2),
		    MODULATE (0x2100),
		    DECAL (0x2101),
		    REPLACE(0x1E01); // must match GL_REPLACE
		  
		  private int value;
		  
		  Model(int value) {
			  this.value = value;
		  }
		  
		  public int getValue() {
			  return value;
		  }

		public static Model fromValue(int value2) {
			switch(value2) {
			case 0x0be2:
				return BLEND;
			case 0x2100:
				return MODULATE;
			case 0x2101:
				return DECAL;
			case 0x1E01:
				return REPLACE;
			}
			return null;
		}
		  };

		  public enum Wrap {
		    // These should match GL_CLAMP and GL_REPEAT for SGI Inventor
		    // compatibility (these are also used by SoTexture2 and
		    // SoTexture3).
		    CLAMP(0x2900),
		    REPEAT(0x2901),
		    CLAMP_TO_BORDER(0x812D);
			  
			  private int value;
			  
			  Wrap(int value) {
				  this.value = value;
			  }
			  
			  public int getValue() {
				  return value;
			  }
			  
			  public static Wrap fromValue(int value) {
				  if(value == CLAMP.getValue()) return CLAMP;
				  else if(value == REPEAT.getValue()) return REPEAT;
				  else if(value == CLAMP_TO_BORDER.getValue()) return CLAMP_TO_BORDER;
				  else return null;
			  }
		  };

	

	//public static final int MAX_UNITS =16; // FIXME: make dynamic?????
	
	

  public static class UnitData implements Mutable {
  
	  public  int nodeid;
	  public final SbVec3s size = new SbVec3s();
	  public int numComponents;
	  public MemoryBuffer bytes;
	  public SoMultiTextureImageElement.Wrap wrapS, wrapT, wrapR;
	  public SoMultiTextureImageElement.Model model;
	  public final SbColor blendColor = new SbColor();
	  
	  public UnitData() {
	   nodeid = 0;
	    size.setValue((short)0,(short)0,(short)0);
	    numComponents = 0;
	    bytes = null;
	    wrapS = SoMultiTextureImageElement.Wrap.REPEAT; 
	    wrapT = SoMultiTextureImageElement.Wrap.REPEAT; 
	    wrapR = SoMultiTextureImageElement.Wrap.REPEAT;
	    model = SoMultiTextureImageElement.Model.MODULATE;
	    blendColor.setValue(0.0f, 0.0f, 0.0f);
	  
	  }

	  public UnitData(final UnitData  org) {
		  copyFrom(org);
	  }
	  public void copyFrom(final Object  other) {
		  final UnitData  org = (UnitData)other;
		  nodeid = org.nodeid;
		  size.copyFrom(org.size);
	    numComponents = org.numComponents;
	    bytes = org.bytes;
	    wrapS = org.wrapS;
	    wrapT = org.wrapT; 
	    wrapR = org.wrapR;
	    model = org.model;
	    blendColor.copyFrom(org.blendColor);
	  
	  }

	  
  };
  
  private static class SoMultiTextureImageElementP {
	  public
	    void ensureCapacity(int unit) {
	      while (unit >= this.unitdata.getLength()) {
	        this.unitdata.append(new SoMultiTextureImageElement.UnitData());
	      }
	    }
	    final SbListOfMutableRefs<SoMultiTextureImageElement.UnitData> unitdata = new SbListOfMutableRefs<>(()->new SoMultiTextureImageElement.UnitData());
	  };

  private SoMultiTextureImageElementP pimpl;

  //private final SoMultiTextureImageElement.UnitData[] unitdata = new SoMultiTextureImageElement.UnitData[MAX_UNITS];
  
  public SoMultiTextureImageElement()
  {
    pimpl = new SoMultiTextureImageElementP();

  }
  
/*!
Resets this element to its original values.
*/
public static void
setDefault(SoState state, SoNode node, int unit)
{
SoMultiTextureImageElement elem =
  (SoMultiTextureImageElement)
  (state.getElement(classStackIndexMap.get(SoMultiTextureImageElement.class)));
elem.pimpl.ensureCapacity(unit);
elem.pimpl.unitdata.operator_square_bracket(unit, new UnitData());  
}

//! FIXME: write doc.
public static void
set(SoState state, SoNode node,
                              int unit,
                              SbVec2s size, int numComponents,
                              MemoryBuffer bytes,
                              Wrap wrapS,
                              Wrap wrapT,
                              Model model,
                              SbColor blendColor)
{
SoMultiTextureImageElement elem =
  (SoMultiTextureImageElement)
  (state.getElement(classStackIndexMap.get(SoMultiTextureImageElement.class)));

elem.pimpl.ensureCapacity(unit);
elem.setElt(unit, node.getNodeId(), size, numComponents, bytes, wrapS, wrapT,
             model, blendColor);
}

/*!
FIXME: write doc.

\COIN_FUNCTION_EXTENSION
*/
public static void
set(SoState state, SoNode node,
                              int unit,
                              SbVec3s size, int numComponents,
                              MemoryBuffer bytes,
                               Wrap wrapS,
                               Wrap wrapT,
                               Wrap wrapR,
                               Model model,
                               SbColor blendColor)
{
SoMultiTextureImageElement elem = (SoMultiTextureImageElement)
  (state.getElement(classStackIndexMap.get(SoMultiTextureImageElement.class)));

elem.pimpl.ensureCapacity(unit);
elem.setElt(unit, node.getNodeId(), size, numComponents, bytes, wrapS, wrapT, wrapR,
             model, blendColor);
}

//! FIXME: write doc.

public static MemoryBuffer get(SoState state,
                              int unit,
                              final SbVec2s size,
                              final int[] numComponents,
                              final Wrap[]  wrapS,
                              final Wrap[] wrapT,
                              final Model[] model,
                              final SbColor blendColor)
{
SoMultiTextureImageElement elem =
  (SoMultiTextureImageElement)
  (getConstElement(state, classStackIndexMap.get(SoMultiTextureImageElement.class)));

elem.pimpl.ensureCapacity(unit);
UnitData ud = elem.pimpl.unitdata.operator_square_bracket(unit);

wrapS[0] = ud.wrapS;
wrapT[0] = ud.wrapT;
model[0] = ud.model;
blendColor.copyFrom( ud.blendColor);

return getImage(state, unit, size, numComponents);
}

/*!
FIXME: write doc.

\COIN_FUNCTION_EXTENSION

*/
public static MemoryBuffer get(SoState state,
                              int unit,
                              SbVec3s size,
                              final int[] numComponents,
                              final Wrap[] wrapS,
                              final Wrap[] wrapT,
                              final Wrap[] wrapR,
                              final Model[] model,
                              final SbColor blendColor)
{
	SoMultiTextureImageElement elem =
  (SoMultiTextureImageElement)
  (getConstElement(state, classStackIndexMap.get(SoMultiTextureImageElement.class)));

elem.pimpl.ensureCapacity(unit);
UnitData ud = elem.pimpl.unitdata.operator_square_bracket(unit);

wrapS[0] = ud.wrapS;
wrapT[0] = ud.wrapT;
wrapR[0] = ud.wrapR;
model[0] = ud.model;
blendColor.copyFrom( ud.blendColor);

return getImage(state, unit, size, numComponents);
}

/*!
FIXME: write doc
*/
public static MemoryBuffer
getImage(SoState state,
                                   int unit,
                                   SbVec2s size,
                                   final int[] numComponents)
{
	SoMultiTextureImageElement elem =
  (SoMultiTextureImageElement)
  (getConstElement(state, classStackIndexMap.get(SoMultiTextureImageElement.class)));

	elem.pimpl.ensureCapacity(unit);
	UnitData ud = elem.pimpl.unitdata.operator_square_bracket(unit);

	size.setValue(ud.size.getValue()[0], ud.size.getValue()[1]);
	numComponents[0] = ud.numComponents;
	return ud.bytes;
}

/*!
FIXME: write doc
*/
public static MemoryBuffer getImage(SoState state,
                                   int unit,
                                   SbVec3s size,
                                   final int[] numComponents)
{
	SoMultiTextureImageElement elem =
  (SoMultiTextureImageElement)
  (getConstElement(state, classStackIndexMap.get(SoMultiTextureImageElement.class)));

	elem.pimpl.ensureCapacity(unit);
	UnitData ud = elem.pimpl.unitdata.operator_square_bracket(unit);

	size = ud.size;
	numComponents[0] = ud.numComponents;
	return ud.bytes;
}


//! FIXME: write doc.

public static boolean
containsTransparency(SoState state)
{
	SoMultiTextureImageElement elem =
  (SoMultiTextureImageElement)
  (getConstElement(state, classStackIndexMap.get(SoMultiTextureImageElement.class)));

for (int i = 0; i < elem.pimpl.unitdata.getLength(); i++) {
  if (elem.hasTransparency(i)) return true;
}
	return false;
}

/*!
Called by containsTransparency(). Returns \e TRUE if image data has
transparency. Default method does a very poor job of detecting this,
since it returns \e TRUE when the number of components are 2 or
4. Override whenever it is important to know this
(SoGLTextureImageElement overrides it to avoid transparency handling
where possible).
*/
public boolean
hasTransparency( int unit)
{
if (unit < pimpl.unitdata.getLength()) {
  UnitData ud = pimpl.unitdata.operator_square_bracket(unit);
  return (ud.numComponents==2 || ud.numComponents==4);
}
return false;
}

public SoMultiTextureImageElement.UnitData
getUnitData( int unit)
{
	assert(unit < pimpl.unitdata.getLength());
	return pimpl.unitdata.operator_square_bracket(unit);
}

/*!
Get the number of units with image data set.
*/
public int 
getNumUnits() 
{
return pimpl.unitdata.getLength();
}

//! FIXME: write doc.

public byte[]
getDefault(SbVec2s size, final int[] numComponents)
{
size.setValue((short)0,(short)0);
numComponents[0] = 0;
return null;
}

/*!
FIXME: write doc.

*/
public byte[]
getDefault(SbVec3s size, final int[] numComponents)
{
size.setValue((short)0,(short)0,(short)0);
numComponents[0] = 0;
return null;
}

public void
push(SoState state)
{
SoMultiTextureImageElement prev =
  (SoMultiTextureImageElement)
  (this.getNextInStack());

pimpl.unitdata.copyFrom(prev.pimpl.unitdata);
}

public boolean
matches( SoElement elem)
{
SoMultiTextureImageElement e =
  (SoMultiTextureImageElement)
  (elem);
int n = e.pimpl.unitdata.getLength();
if (n != pimpl.unitdata.getLength()) return false;

for (int i = 0; i < n; i++) {
  if (e.pimpl.unitdata.operator_square_bracket(i).nodeid != pimpl.unitdata.operator_square_bracket(i).nodeid) {
    return false;
  }
}
return true;
}

public SoElement
copyMatchInfo() 
{
SoMultiTextureImageElement elem = (SoMultiTextureImageElement)
  (getTypeId().createInstance());
elem.pimpl.unitdata.copyFrom(pimpl.unitdata);
return elem;
}


//! FIXME: write doc.

public void
setElt( int unit,
                                 int nodeid,
                                 SbVec2s size, int numComponents,
                                 MemoryBuffer bytes,
                                 Wrap wrapS,
                                 Wrap wrapT,
                                 Model model,
                                 SbColor blendColor)
{
assert(unit < pimpl.unitdata.getLength());
UnitData ud = pimpl.unitdata.operator_square_bracket(unit);

ud.nodeid = nodeid;
ud.size.setValue(size.getValue()[0],size.getValue()[1],(short)1);
ud.numComponents = numComponents;
ud.bytes = bytes;
ud.wrapS = wrapS;
ud.wrapT = wrapT;
ud.wrapR = Wrap.REPEAT;
ud.model = model;
ud.blendColor.copyFrom(blendColor);
}

/*!
FIXME: write doc.
*/
public void
setElt( int unit,
                                  int nodeid,
                                  SbVec3s size, int numComponents,
                                 MemoryBuffer bytes,
                                  Wrap wrapS,
                                  Wrap wrapT,
                                  Wrap wrapR,
                                  Model model,
                                  SbColor blendColor)
{
assert(unit < pimpl.unitdata.getLength());
UnitData ud = pimpl.unitdata.operator_square_bracket(unit);

ud.nodeid = nodeid;
ud.size.copyFrom(size);
ud.numComponents = numComponents;
ud.bytes = bytes;
ud.wrapS = wrapS;
ud.wrapT = wrapT;
ud.wrapR = wrapR;
ud.model = model;
ud.blendColor.copyFrom(blendColor);
}

/*!
FIXME: write doc.
*/
public SbColor 
getBlendColor(SoState state, int unit)
{
SoMultiTextureImageElement elem =
  (SoMultiTextureImageElement)
  (getConstElement(state, classStackIndexMap.get(SoMultiTextureImageElement.class)));

elem.pimpl.ensureCapacity(unit);
return elem.pimpl.unitdata.operator_square_bracket(unit).blendColor;
}

/*!
FIXME: write doc.
*/
public SoMultiTextureImageElement.Wrap
getWrapS(SoState state, int unit)
{
SoMultiTextureImageElement elem =
  (SoMultiTextureImageElement)
  (getConstElement(state, classStackIndexMap.get(SoMultiTextureImageElement.class)));

elem.pimpl.ensureCapacity(unit);
return elem.pimpl.unitdata.operator_square_bracket(unit).wrapT;
}

/*!
FIXME: write doc.
*/
public SoMultiTextureImageElement.Wrap
getWrapT(SoState state, int unit)
{
SoMultiTextureImageElement elem =
  (SoMultiTextureImageElement)
  (getConstElement(state, classStackIndexMap.get(SoMultiTextureImageElement.class)));

elem.pimpl.ensureCapacity(unit);
return elem.pimpl.unitdata.operator_square_bracket(unit).wrapS;
}

/*!
FIXME: write doc.
*/
public SoMultiTextureImageElement.Wrap
getWrapR(SoState state, int unit)
{
SoMultiTextureImageElement elem =
  (SoMultiTextureImageElement)
  (getConstElement(state, classStackIndexMap.get(SoMultiTextureImageElement.class)));

elem.pimpl.ensureCapacity(unit);
return elem.pimpl.unitdata.operator_square_bracket(unit).wrapR;
}

/*!
FIXME: write doc.
*/
public SoMultiTextureImageElement.Model
getModel(SoState state, int unit)
{
SoMultiTextureImageElement elem =
  (SoMultiTextureImageElement)
  (getConstElement(state, classStackIndexMap.get(SoMultiTextureImageElement.class)));

elem.pimpl.ensureCapacity(unit);
return elem.pimpl.unitdata.operator_square_bracket(unit).model;
}

//#undef PRIVATE
}
