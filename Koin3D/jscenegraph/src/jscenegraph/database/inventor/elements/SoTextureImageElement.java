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
 * Copyright (C) 1990,91   Silicon Graphics, Inc.
 *
 _______________________________________________________________________
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 |
 |   $Revision: 1.1.1.1 $
 |
 |   Description:
 |      This file defines the SoTextureImageElement class.
 |
 |   Author(s)          : Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.elements;

import jscenegraph.database.inventor.SbColor;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SbVec3s;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;


///////////////////////////////////////////////////////////////////////////////
///
///  \class SoTextureImageElement
///  \ingroup Elements
///
///  Element storing the current texture image.
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoTextureImageElement extends SoReplacedElement {
	
  public enum Model { // COIN 3D
    // These should match GL_BLEND, GL_MODULATE and GL_DECAL for SGI
    // Inventor compatibility (these are also used by SoTexture2 and
    // SoTexture3).
    BLEND ( 0x0be2),
    MODULATE ( 0x2100),
    DECAL ( 0x2101),
    REPLACE ( 0x1E01); // must match GL_REPLACE
	  
	  private int value;
	  Model(int value) {
		  this.value = value;	  
	  }
	  public int getValue() {
		  return value;
	  }
	public static Model fromValue(int value2) {
		if(value2 == BLEND.getValue()) return BLEND;
		if(value2 == MODULATE.getValue()) return MODULATE;
		if(value2 == DECAL.getValue()) return DECAL;
		if(value2 == REPLACE.getValue()) return REPLACE;
		return null;
	}
  };

  public enum Wrap { // COIN 3D
    // These should match GL_CLAMP and GL_REPEAT for SGI Inventor
    // compatibility (these are also used by SoTexture2 and
    // SoTexture3).
    CLAMP ( 0x2900),
    REPEAT ( 0x2901),
    CLAMP_TO_BORDER ( 0x812D);
	  
	  private int value;
	  
	  Wrap(int value) {
		  this.value = value;
	  }
	  public int getValue() {
		  return value;
	  }
	public static Wrap fromValue(int value2) {
		if(value2 == CLAMP.getValue()) return Wrap.CLAMP;
		if(value2 == REPEAT.getValue()) return Wrap.REPEAT;
		if(value2 == CLAMP_TO_BORDER.getValue()) return Wrap.CLAMP_TO_BORDER;
		return null;
	}
  };

	

    //protected final SbVec2s     size = new SbVec2s(); // MEVISLAB
    protected final SbVec3s     size = new SbVec3s(); // COIN 3D
    int         numComponents;
    byte[] bytes;
    int         wrapS, wrapT, model;
    int wrapR; // COIN 3D
    final SbColor     blendColor = new SbColor();

	

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sets texture image in element accessed from state.
//
// Use: public, static

public static void
set(final SoState state, final SoNode node,
                           final SbVec2s size, int nc,
                           byte[] b,
                           int wrapS, int wrapT, int model,
                           final SbColor blendColor)
//
////////////////////////////////////////////////////////////////////////
{
    SoTextureImageElement       elt;

    // Get an instance we can change (pushing if necessary)
    elt = (SoTextureImageElement ) getElement(state, classStackIndexMap.get(SoTextureImageElement.class), node);

    elt.setElt(size, nc, b, wrapS, wrapT, model, blendColor);
}

/*! from COIN 3D
  FIXME: write doc.

  \COIN_FUNCTION_EXTENSION

  \since Coin 2.0
*/
public static void
set(SoState state, SoNode node,
                           final SbVec3s size, final int numComponents,
                           final byte[] bytes,
                           Wrap wrapS,
                           Wrap wrapT,
                           Wrap wrapR,
                           Model model, final SbColor blendColor)
{
  SoTextureImageElement elem = (SoTextureImageElement)
    (
     state.getElement(classStackIndexMap.get(SoTextureImageElement.class))
     );

  if (elem != null) {
    elem.setElt(size, numComponents, bytes, wrapS, wrapT, wrapR,
                 model, blendColor);
  }
}

        
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Really do the set.  This is virtual so the GLTextureImageElement
//    can easily override the generic behavior to send textures to GL.
//
// Use: protected, virtual
protected void
setElt(final SbVec2s _size, 
                              int _numComponents,
                              byte[] _bytes,
                              int _wrapS, int _wrapT, int _model,
                              final SbColor _blendColor)
                              {
	SoTextureImageElement_setElt(_size,_numComponents,_bytes,_wrapS,_wrapT,_model,_blendColor);
                              }

protected void
SoTextureImageElement_setElt(final SbVec2s _size, 
                              int _numComponents,
                              byte[] _bytes,
                              int _wrapS, int _wrapT, int _model,
                              final SbColor _blendColor)
//
////////////////////////////////////////////////////////////////////////
{
    size.copyFrom(new SbVec3s(_size.getValue()[0],_size.getValue()[1],(short)1));
    numComponents = _numComponents;
    bytes = _bytes;
    wrapS = _wrapS;
    wrapT = _wrapT;
    model = _model;
    blendColor.copyFrom(_blendColor);
}

/*! from COIN 3D
  FIXME: write doc.

  \COIN_FUNCTION_EXTENSION

  \since Coin 2.0
*/
protected void
setElt( SbVec3s sizearg, int numComponentsarg,
                              byte[] bytesarg, Wrap wrapSarg,
                              Wrap wrapTarg, Wrap wrapRarg,
                              Model modelarg,
                              SbColor blendColorarg)
{
  this.size.copyFrom(sizearg);
  this.numComponents = numComponentsarg;
  this.bytes = bytesarg;
  this.wrapS = wrapSarg.getValue();
  this.wrapT = wrapTarg.getValue();
  this.wrapR = wrapRarg.getValue();
  this.model = modelarg.getValue();
  this.blendColor.copyFrom( blendColorarg);
}



////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns texture image from state.
//
// Use: public, static

public static byte[]
get(final SoState state, final SbVec2s _size, 
                           final int[] _numComponents, final int[] _wrapS, final int[] _wrapT,
                           final int[] _model, final SbColor _blendColor)
//
////////////////////////////////////////////////////////////////////////
{
     SoTextureImageElement elt;

    elt = ( SoTextureImageElement )
        getConstElement(state, classStackIndexMap.get(SoTextureImageElement.class));

    _size.copyFrom(elt.size);
    _numComponents[0] = elt.numComponents;
    _wrapS[0] = elt.wrapS;
    _wrapT[0] = elt.wrapT;
    _model[0] = elt.model;
    _blendColor.copyFrom(elt.blendColor);

    return elt.bytes;
}

	
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns TRUE if the texture contains transparency info.
//
// Use: public, static

public static boolean
containsTransparency(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    SoTextureImageElement elt;

    elt = ( SoTextureImageElement )
        getConstElement(state, classStackIndexMap.get(SoTextureImageElement.class));

    return (elt.numComponents == 2 || elt.numComponents == 4);
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Initializes element
//
// Use: public

public void
init(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    super.init(state);

    int[] dummy = new int[1]; dummy[0] = numComponents;
    bytes = getDefault(size, dummy);
    numComponents = dummy[0];
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Get default image (a NULL 0 by 0 by 0 image)
//
// Use: public

public byte[]
getDefault(final SbVec2s s, final int[] nc)
//
////////////////////////////////////////////////////////////////////////
{
    s.getValue()[0] = 0;
    s.getValue()[1] = 0;
    nc[0] = 0;
    return null;
}

/*! from COIN 3D
  FIXME: write doc.

  \COIN_FUNCTION_EXTENSION

  \since Coin 2.0
*/
public byte[]
getDefault(final SbVec3s size, final int[] numComponents)
{
  size.setValue((short)0,(short)0,(short)0);
  numComponents[0] = 0;
  return null;
}

//
// private
//
private void
setDefaultValues()
{
	final int[] compo = new int[1]; // java port
  this.bytes = getDefault(this.size, /*this.numComponents*/compo); this.numComponents = compo[0];
  this.wrapS = Wrap.REPEAT.getValue();
  this.wrapT = Wrap.REPEAT.getValue();
  this.wrapR = Wrap.REPEAT.getValue();
  this.model = Model.MODULATE.getValue();
  this.blendColor.setValue(0.0f, 0.0f, 0.0f);
}

/*!
  Resets this element to its original values.
*/
public static void
setDefault(SoState state, SoNode node)
{
  SoTextureImageElement  elem = (SoTextureImageElement)
    (
     state.getElement(classStackIndexMap.get(SoTextureImageElement.class))
     );
  if (elem != null) {
    elem.setDefaultValues();
  }
}


}
