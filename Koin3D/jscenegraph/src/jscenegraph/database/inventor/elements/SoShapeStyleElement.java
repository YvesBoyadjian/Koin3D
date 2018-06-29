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
 * Copyright (C) 1995-96   Silicon Graphics, Inc.
 *
 _______________________________________________________________________
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 |
 |   $Revision: 1.1.1.1 $
 |
 |   Description:
 |      This file defines the SoShapeStyleElement class.
 |      It takes values from the following: 
 |      SoDrawStyleElement
 |      SoComplexityTypeElement
 |      SoGLTextureEnabledElement
 |      SoLazyElement
 |      TransparencyType field of SoGLRenderAction
 |      CoordType of SoTextureCoordinateElement
 |      number and value of SoTransparencyElement
 |      isColorIndex from SoGLLazyElement
 |              to determine 
 |          whether to use fast-path rendering, and
 |          which fast-path rendering code to use
 |
 |   Author(s)          : Alan Norton,  Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.elements;

import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoLightModel;
import jscenegraph.database.inventor.nodes.SoVertexPropertyCache;


///////////////////////////////////////////////////////////////////////////////
///
///  \class SoShapeStyleElement
///  \ingroup Elements
///
///  This elements stores some information shapes need to quickly
///  determine whether or not they should render, and, if they should
///  render, how they should render.  Specifically, it stores:
///
///  NeedNormals:   TRUE if lighting is on
///  NeedTexCoords: TRUE if texturing and texcoords not generated by GL
///
///  MightNotRender:  TRUE if:
///           -- bounding box complexity
///           -- invisible drawstyle
///           -- render abort callback registered
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoShapeStyleElement extends SoElement {
	
  // flags used for optimized testing of features
  public enum Flags {
    LIGHTING                ( 0x00000100),
    TEXENABLED              ( 0x00000200),
    TEXFUNC                 ( 0x00000400),
    BBOXCMPLX               ( 0x00000800),
    INVISIBLE               ( 0x00001000),
    ABORTCB                 ( 0x00002000),
    OVERRIDE                ( 0x00004000),
    TEX3ENABLED             ( 0x00008000),
    BIGIMAGE                ( 0x00010000),
    BUMPMAP                 ( 0x00020000),
    VERTEXARRAY             ( 0x00040000),
    TRANSP_TEXTURE          ( 0x00080000),
    TRANSP_MATERIAL         ( 0x00100000),
    TRANSP_SORTED_TRIANGLES ( 0x00200000),
    SHADOWMAP               ( 0x00400000),
    SHADOWS                 ( 0x00800000);
    private int value;
    Flags(int value) {
    	this.value = value;
    }
    public int getValue() {
    	return value;
    }
  };	
	
	public static final int INVISIBLE_BIT = 0x1;
	public static final int BBOX_BIT = 0x2;
	public static final int DELAY_TRANSP_BIT = 0x4;

	

	   private        int                 delayFlags; //!< True if rendering might be delayed
	   private        boolean              needNorms;
	   private        boolean              texEnabled, texFunc;
	   private        int                 renderCaseMask;
	   
	   private int flags; // COIN 3D

	
    //! Returns TRUE if shapes may not render for some reason:
    public boolean              mightNotRender() { return delayFlags != 0; }

    //! Returns TRUE if need normals:
    public boolean              needNormals() { return needNorms; }
    
    //! Returns TRUE if need texture coordinates:
    public boolean              needTexCoords()
        { return (texEnabled && (!texFunc)); }

    //! Returns a mask that can be used by the SoVertexPropertyCache
    //! class (see SoVertexProperty.h for the SoRenderInfo class) to
    //! quickly mask out the normal or texture coordinate cases if
    //! normals or texture coordinates aren't needed.
    public int                 getRenderCaseMask()
        { return renderCaseMask; }

  public
    boolean      isTextureFunction()
        {return texFunc;}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Initializes element
//
// Use: public
//
////////////////////////////////////////////////////////////////////////
public void
init(SoState state)
{
    delayFlags = 0;
    texEnabled = /*0*/false;
    texFunc = /*0*/false;
    needNorms = /*1*/true;
    renderCaseMask = SoVertexPropertyCache.BitMask.ALL_FROM_STATE_BITS.getValue() &
        (~SoVertexPropertyCache.Bits.TEXCOORD_BIT.getValue()) &
        (~SoVertexPropertyCache.BitMask.OVERRIDE_FROM_STATE_BIT.getValue());
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Destructor.
//
// Use: private
//
////////////////////////////////////////////////////////////////////////
public static SoShapeStyleElement 
get(SoState state)

{
    return (SoShapeStyleElement )getConstElement(state, classStackIndexMap.get(SoShapeStyleElement.class));
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Push
//
// Use: internal
//
////////////////////////////////////////////////////////////////////////
public void
push(SoState state)

{
    SoShapeStyleElement sse = (SoShapeStyleElement )getNextInStack();
    delayFlags = sse.delayFlags;
    texEnabled = sse.texEnabled;
    texFunc = sse.texFunc;
    needNorms = sse.needNorms;
    renderCaseMask = sse.renderCaseMask;
    //Capture previous element:
    sse.capture(state);
}

	   
	   
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Matches method for caches...
//
// Use: public
///////////////////////////////////////////////////////////////////////
public boolean
matches( SoElement elt)

{
    SoShapeStyleElement le = ( SoShapeStyleElement ) elt;
    return (delayFlags == le.delayFlags &&
            renderCaseMask == le.renderCaseMask);
}

	   
	   
	 ////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //     Create a copy of this instance suitable for calling matches()
	   //     on.
	   //
	   // Use: protected
	   //
	   ////////////////////////////////////////////////////////////////////////
	  public SoElement 
	   copyMatchInfo()
	   
	   {
	       SoShapeStyleElement result =
	           (SoShapeStyleElement )getTypeId().createInstance();
	   
	       result.delayFlags = delayFlags;
	       result.needNorms = needNorms;
	       result.texEnabled = texEnabled;
	       result.texFunc = texFunc;
	       result.renderCaseMask = renderCaseMask;
	   
	       return result;
	   }
	  
	  //////////////////////////////////////////////////////////////////////////////
	   //
	   //  Description:
	   //      set light model
	   //
	   /////////////////////////////////////////////////////////////////////////////
	   public static void
	   setLightModel(SoState state, int value)
	   {
	       SoShapeStyleElement elt = (SoShapeStyleElement )
	           getElement(state, classStackIndexMap.get(SoShapeStyleElement.class));
	   
	       if (value == (int)SoLightModel.Model.BASE_COLOR.getValue()) {
	           elt.needNorms = false;
	       } else {
	           elt.needNorms = true;
	       }
	   
	       // Clear renderCase bits
	       elt.renderCaseMask &= ~SoVertexPropertyCache.Bits.NORMAL_BITS.getValue();
	   
	       // Set renderCase bits, if need normals:
	       if (elt.needNormals())
	           elt.renderCaseMask |= SoVertexPropertyCache.Bits.NORMAL_BITS.getValue();
	   }	  

//////////////////////////////////////////////////////////////////////////////
//
//  Description:
//    Set the BBOX_BIT based on whether or not the value is BOUNDING_BOX
//
//
////////////////////////////////////////////////////////////////////////
public static void
setComplexityType( SoState state, int value) 
{
    SoShapeStyleElement elt = (SoShapeStyleElement )
        getElement(state, classStackIndexMap.get(SoShapeStyleElement.class));

    if (value == (int)SoComplexityTypeElement.Type.BOUNDING_BOX.getValue()) {
        elt.delayFlags |= BBOX_BIT;
    } else {
        elt.delayFlags &= ~BBOX_BIT;
    }
}

/////////////////////////////////////////////////////////////////////////////
//
//  Description:
//    Set invisible bit if invisible drawstyle
//
//
////////////////////////////////////////////////////////////////////////
public static void
setDrawStyle(SoState state, int value) 
{
    SoShapeStyleElement elt = (SoShapeStyleElement )
        getElement(state, classStackIndexMap.get(SoShapeStyleElement.class));

    if (value == (int)SoDrawStyleElement.Style.INVISIBLE.getValue()) {
        elt.delayFlags |= INVISIBLE_BIT;
    } else {
        elt.delayFlags &= ~INVISIBLE_BIT;
    }
}

/////////////////////////////////////////////////////////////////////////////
//
//  Description:
//    Set delayed transparency bit if anything other than screen door.
//
////////////////////////////////////////////////////////////////////////
public static void
setTransparencyType(SoState state, int value) 
{
    SoShapeStyleElement elt = (SoShapeStyleElement )
        getElement(state, classStackIndexMap.get(SoShapeStyleElement.class));

    if (value == (int)SoGLRenderAction.TransparencyType.SCREEN_DOOR.getValue()) {
        elt.delayFlags &= ~DELAY_TRANSP_BIT;
    } else {
        elt.delayFlags |= DELAY_TRANSP_BIT;
    }
}



//////////////////////////////////////////////////////////////////////////////
//
//  Description:
//
//
////////////////////////////////////////////////////////////////////////
public static void
setTextureEnabled(SoState state, boolean value)
{
    SoShapeStyleElement elt = (SoShapeStyleElement )
        getElement(state, classStackIndexMap.get(SoShapeStyleElement.class));

    elt.texEnabled = value;

    // Clear renderCase bits
    elt.renderCaseMask &= ~SoVertexPropertyCache.Bits.TEXCOORD_BIT.getValue();

    // Set renderCase bits, if need texcoords:
    if (elt.needTexCoords())
        elt.renderCaseMask |= SoVertexPropertyCache.Bits.TEXCOORD_BIT.getValue();
}

/*!
  FIXME: write doc.

  \COIN_FUNCTION_EXTENSION

  \since Coin 2.0
*/
public static void //TODO : merge coin3D with mevislab
setTexture3Enabled(SoState state,
                                       boolean value)
{
  SoShapeStyleElement elem = (SoShapeStyleElement )getElement(state, classStackIndexMap.get(SoShapeStyleElement.class));
  if (value) {
    elem.flags |= Flags.TEX3ENABLED.getValue();
  }
  else {
    elem.flags &= ~Flags.TEX3ENABLED.getValue();
  }
}

/*!
  Sets bigimage enabled.

  \since Coin 2.4
*/
public static void
setBigImageEnabled(SoState state, boolean value)
{
  SoShapeStyleElement elem = (SoShapeStyleElement )getElement(state, classStackIndexMap.get(SoShapeStyleElement.class));
  if (value) {
    elem.flags |= Flags.BIGIMAGE.getValue();
  }
  else {
    elem.flags &= ~Flags.BIGIMAGE.getValue();
  }
}


//////////////////////////////////////////////////////////////////////////////
//
//  Description:
//
////////////////////////////////////////////////////////////////////////////
public static void
setTextureFunction(SoState state, boolean value)
{
    SoShapeStyleElement elt = (SoShapeStyleElement )
        getElement(state, classStackIndexMap.get(SoShapeStyleElement.class));

    elt.texFunc = value;
    if (value) 
        elt.renderCaseMask |= SoVertexPropertyCache.BitMask.TEXTURE_FUNCTION_BIT.getValue();
    else
        elt.renderCaseMask &= ~SoVertexPropertyCache.BitMask.TEXTURE_FUNCTION_BIT.getValue();

    // Clear renderCase bits
    elt.renderCaseMask &= ~SoVertexPropertyCache.Bits.TEXCOORD_BIT.getValue();

    // Set renderCase bits, if need texcoords:
    if (elt.needTexCoords())
        elt.renderCaseMask |= SoVertexPropertyCache.Bits.TEXCOORD_BIT.getValue();
}

///////////////////////////////////////////////////////////////////////
//
// Description:
//    Turn on or off the override mask, indicating that there is
//    an override of transparency, diffuse color, or material binding
//    Invoked as a side-effect of SoOverrideElement::set..
//
// use: public, static
////////////////////////////////////////////////////////////////////////
public static void
setOverrides(SoState state, boolean value)
{
    SoShapeStyleElement elt = (SoShapeStyleElement )
        getElement(state, classStackIndexMap.get(SoShapeStyleElement.class));
    if (value) elt.renderCaseMask |= 
        SoVertexPropertyCache.BitMask.OVERRIDE_FROM_STATE_BIT.getValue();
    else elt.renderCaseMask &=
        (~SoVertexPropertyCache.BitMask.OVERRIDE_FROM_STATE_BIT.getValue());
}   


//////////////////////////////////////////////////////////////////////////////
//
//  Description:
//    Returns true if doing screen_door transparency
//
//
////////////////////////////////////////////////////////////////////////
public boolean
isScreenDoor(SoState state)
{
    SoShapeStyleElement elt = (SoShapeStyleElement )
        getElement(state, classStackIndexMap.get(SoShapeStyleElement.class));

    return (elt.delayFlags & DELAY_TRANSP_BIT) == 0;
}

/*!
  Sets texture transparency.

  \since Coin 2.4
*/
public static void
setTransparentTexture(SoState state, final boolean value) // COIN 3D
{
  SoShapeStyleElement elem = (SoShapeStyleElement) getElement(state, classStackIndexMap.get(SoShapeStyleElement.class));
  if (value) {
    elem.flags |= Flags.TRANSP_TEXTURE.getValue();
  }
  else {
    elem.flags &= ~Flags.TRANSP_TEXTURE.getValue();
  }
}


}
