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
 |      This file defines the SoOverrideElement class.
 |
 |   Author(s)          : Roger Chickering, Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.elements;

import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;

///////////////////////////////////////////////////////////////////////////////
///
///  \class SoOverrideElement
///  \ingroup Elements
///
///  Element that stores a flag for each type of element which can be
///  overridden.  Nodes implement override by setting the appropriate
///  bit if their override flag is on, and ignoring overridden elements
///  if the corresponding bit in the state's SoOverrideElement is set.
///  
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoOverrideElement extends SoElement {
	
    public enum ElementMask {
           AMBIENT_COLOR   ( 0x1),
          COLOR_INDEX     ( 0x2),
          COMPLEXITY      ( 0x4),
          COMPLEXITY_TYPE ( 0x8),
          CREASE_ANGLE    ( 0x10),
          DIFFUSE_COLOR   ( 0x20),
          TRANSPARENCY    ( 0x20), //!< TRANSPARENCY is same as diffuse color:  overriding one will override both.
          DRAW_STYLE      ( 0x40),
          EMISSIVE_COLOR  ( 0x80),
          FONT_NAME       ( 0x100),
          FONT_SIZE       ( 0x200),
          LIGHT_MODEL     ( 0x400),
          LINE_PATTERN    ( 0x800),
          LINE_WIDTH      ( 0x1000),
          MATERIAL_BINDING( 0x2000),
          POINT_SIZE      ( 0x4000),
          PICK_STYLE      ( 0x8000),
          SHAPE_HINTS     ( 0x10000),
          SHININESS       ( 0x20000),
          SPECULAR_COLOR  ( 0x40000),
          POLYGON_OFFSET  ( 0x80000),
          TRANSPARENCY_TYPE (0x100000),
          NORMAL_VECTOR (0x200000),
          NORMAL_BINDING (0x400000);
           
           private int value;
           
           ElementMask(int value) {
        	   this.value = value;
           }
           
           public int getValue() {
        	   return value;
           }
      };
  	
	
	
	private int flags;

  public SoOverrideElement() {
	  
  }
  
  public static int getFlags(SoState state) {
	    return ((SoOverrideElement)SoOverrideElement.getConstElement(state, classStackIndexMap.get(SoOverrideElement.class))).flags;
	  }

  

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Initializes element.  All flags are initialized to FALSE (0).
//
// Use: public

public void
init(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    flags = 0;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Pushes element, copying flags from previous top instance.
//
// Use: public
public void
push(SoState state)
//
////////////////////////////////////////////////////////////////////////
{
    SoOverrideElement elt = (SoOverrideElement )getNextInStack();

    flags = elt.flags;
    elt.capture(state);  // Capture previous element
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//     Create a copy of this instance suitable for calling matches()
//     on.
//
// Use: protected

  
  ////////////////////////////////////////////////////////////////////////
//
// Description:
//    Returns TRUE if elements match
//
// Use: public

public boolean
matches( SoElement elt)
//
////////////////////////////////////////////////////////////////////////
{
    return (flags == (( SoOverrideElement ) elt).flags);
}

  
  
  ////////////////////////////////////////////////////////////////////////
   //
   // Description:
   //     Create a copy of this instance suitable for calling matches()
   //     on.
   //
   // Use: protected
   
 public  SoElement 
  copyMatchInfo()
  //
  ////////////////////////////////////////////////////////////////////////
  {
      SoOverrideElement result =
          (SoOverrideElement )getTypeId().createInstance();
  
      result.flags = flags;
  
      return result;
  }    
 
     //!
    //! "set" methods for each element which can be overridden.
    //!

    //! set override flag for SoAmbientColorElement.
   public static void         setAmbientColorOverride(SoState state, SoNode node,
                                                boolean override)
        { SO_SET_OVERRIDE(state,ElementMask.AMBIENT_COLOR.getValue(),override); }

    //! set override flag for SoColorIndexElement.
   public static void         setColorIndexOverride(SoState state, SoNode node,
                                              boolean override)
        { SO_SET_OVERRIDE(state,ElementMask.COLOR_INDEX.getValue(),override); }

 
    //! set override flag for SoComplexityElement.
    public static void         setComplexityOverride(SoState state, SoNode node,
                                              boolean override)
        { SO_SET_OVERRIDE(state,ElementMask.COMPLEXITY.getValue(), override); }
    
    //! set override flag for SoComplexityTypeElement.
    public static void         setComplexityTypeOverride(SoState state, SoNode node,
                                                  boolean override)
        { SO_SET_OVERRIDE(state,ElementMask.COMPLEXITY_TYPE.getValue(), override); }
    
    //! set override flag for SoCreaseAngleElement.
    public static void         setCreaseAngleOverride(SoState state, SoNode node,
                                               boolean override)
        { SO_SET_OVERRIDE(state, ElementMask.CREASE_ANGLE.getValue(), override); } 
 
 //! set override flag for SoLightModelElement.
   public  static void setLightModelOverride(SoState state, SoNode node,
                                               boolean override)
         { SO_SET_OVERRIDE(state, ElementMask.LIGHT_MODEL.getValue(), override); }
 
   
    //!
    //! "get" methods for each element which can be overridden.
    //!

    //! Returns TRUE if SoAmbientColorElement is overridden.
   public static boolean         getAmbientColorOverride(SoState state)
        { return SO_GET_OVERRIDE(state, ElementMask.AMBIENT_COLOR.getValue()); }

    //! Returns TRUE if SoColorIndexElement is overridden.
   public static boolean         getColorIndexOverride(SoState state)
        { return SO_GET_OVERRIDE(state, ElementMask.COLOR_INDEX.getValue()); }   
   
    //! Returns TRUE if SoComplexityElement is overridden.
   public static boolean       getComplexityOverride(SoState state)
        { return SO_GET_OVERRIDE(state, ElementMask.COMPLEXITY.getValue()); }

    //! Returns TRUE if SoComplexityTypeElement is overridden.
   public static boolean       getComplexityTypeOverride(SoState state)
        { return SO_GET_OVERRIDE(state, ElementMask.COMPLEXITY_TYPE.getValue()); }

    //! Returns TRUE if SoCreaseAngleElement is overridden.
   public static boolean         getCreaseAngleOverride(SoState state)
        { return SO_GET_OVERRIDE(state, ElementMask.CREASE_ANGLE.getValue()); }

   
   //! Returns TRUE if SoDiffuseColorElement is overridden.
   public static boolean       getDiffuseColorOverride(SoState state)
         { return SO_GET_OVERRIDE(state,ElementMask.DIFFUSE_COLOR.getValue()); }
 
    //! Returns TRUE if SoDrawStyleElement is overridden.
   public static boolean         getDrawStyleOverride(SoState state)
        { return SO_GET_OVERRIDE(state, ElementMask.DRAW_STYLE.getValue()); }   
   
    //! Returns TRUE if SoEmissiveColorElement is overridden.
   public static boolean         getEmissiveColorOverride(SoState state)
        { return SO_GET_OVERRIDE(state, ElementMask.EMISSIVE_COLOR.getValue()); }

    //! Returns TRUE if SoFontNameElement is overridden.
   public static boolean         getFontNameOverride(SoState state)
        { return SO_GET_OVERRIDE(state, ElementMask.FONT_NAME.getValue()); }

    //! Returns TRUE if SoFontSizeElement is overridden.
   public static boolean         getFontSizeOverride(SoState state)
        { return SO_GET_OVERRIDE(state, ElementMask.FONT_SIZE.getValue()); }
   
   //! Returns TRUE if SoLightModelElement is overridden.
   public     static boolean       getLightModelOverride(SoState state)
         { return SO_GET_OVERRIDE(state, ElementMask.LIGHT_MODEL.getValue()); }
 
   //! Returns TRUE if SoMaterialBindingElement is overridden.
   public  static boolean       getMaterialBindingOverride(SoState state)
         { return SO_GET_OVERRIDE(state, ElementMask.MATERIAL_BINDING.getValue()); }
    
    //! Returns TRUE if SoLinePatternElement is overridden.
   public static boolean         getLinePatternOverride(SoState state)
        { return SO_GET_OVERRIDE(state, ElementMask.LINE_PATTERN.getValue()); }

    //! Returns TRUE if SoLineWidthElement is overridden.
    public static boolean         getLineWidthOverride(SoState state)
        { return SO_GET_OVERRIDE(state, ElementMask.LINE_WIDTH.getValue()); }
        
       //! Returns TRUE if SoPointSizeElement is overridden.
    public static boolean         getPointSizeOverride(SoState state)
        { return SO_GET_OVERRIDE(state, ElementMask.POINT_SIZE.getValue()); }

    //! Returns TRUE if SoPickStyleElement is overridden.
    public static boolean         getPickStyleOverride(SoState state)
        { return SO_GET_OVERRIDE(state, ElementMask.PICK_STYLE.getValue()); }

    //! Returns TRUE if SoShapeHintsElement is overridden.
    public static boolean         getShapeHintsOverride(SoState state)
        { return SO_GET_OVERRIDE(state, ElementMask.SHAPE_HINTS.getValue()); }

    //! Returns TRUE if SoShininessElement is overridden.
    public static boolean         getShininessOverride(SoState state)
        { return SO_GET_OVERRIDE(state, ElementMask.SHININESS.getValue()); }

    //! Returns TRUE if SoSpecularColorElement is overridden.
    public static boolean         getSpecularColorOverride(SoState state)
        { return SO_GET_OVERRIDE(state, ElementMask.SPECULAR_COLOR.getValue()); }

    //! Returns TRUE if SoTransparencyElement is overridden.
    public static boolean         getTransparencyOverride(SoState state)
        { return SO_GET_OVERRIDE(state, ElementMask.TRANSPARENCY.getValue()); }


public static boolean
getTransparencyTypeOverride(SoState state)
{
  return SO_GET_OVERRIDE(state, ElementMask.TRANSPARENCY_TYPE.getValue());
}

    
    //! Returns TRUE if SoPolygonOffsetElement is overridden.
    public static boolean         getPolygonOffsetOverride(SoState state)
        { return SO_GET_OVERRIDE(state, ElementMask.POLYGON_OFFSET.getValue()); }
    
    
   
   public static boolean SO_GET_OVERRIDE(SoState state, int flag) {
	   SoOverrideElement elt; 
	    elt = (SoOverrideElement ) 
	        getConstElement(state, classStackIndexMap.get(SoOverrideElement.class)); 
	    return (elt.flags & flag) != 0;
   }
   
   public static void SO_SET_OVERRIDE(SoState state, int flag, boolean override) {
	   SoOverrideElement   elt; 
	    elt = (SoOverrideElement )getElement(state, classStackIndexMap.get(SoOverrideElement.class)); 
	    if (override) 
	        elt.flags |= flag; 
	    else 
	        elt.flags &= ~flag;	   
   }
/////////////////////////////////////////////////////////////////////////
//
//  Description:  set diffuseColorOverride (plus set shapeStyle)
//
//
//  Use: static, public.
/////////////////////////////////////////////////////////////////////////
public static void
setDiffuseColorOverride(SoState state, SoNode node, 
    boolean override)
{
    SoOverrideElement   elt =
        (SoOverrideElement )getElement(state, classStackIndexMap.get(SoOverrideElement.class)); 
    if (elt != null) { 
        if (override){
            elt.flags |= ElementMask.DIFFUSE_COLOR.getValue();
            SoShapeStyleElement.setOverrides(state, true);
        } 
        else {
            elt.flags &= ~ElementMask.DIFFUSE_COLOR.getValue();
            if ((elt.flags & ElementMask.MATERIAL_BINDING.getValue()) == 0)
                SoShapeStyleElement.setOverrides(state, false);
        }
    }             
}

    //! set override flag for SoDrawStyleElement.
    public static void         setDrawStyleOverride(SoState state, SoNode node,
                                             boolean override)
        { SO_SET_OVERRIDE(state,ElementMask.DRAW_STYLE.getValue(),override); }

    //! set override flag for SoEmissiveColorElement.
    public static void         setEmissiveColorOverride(SoState state, SoNode node,
                                                 boolean override)
        { SO_SET_OVERRIDE(state,ElementMask.EMISSIVE_COLOR.getValue(),override); }
    
    //! set override flag for SoFontNameElement.
    public static void         setFontNameOverride(SoState state, SoNode node,
                                            boolean override)
        { SO_SET_OVERRIDE(state, ElementMask.FONT_NAME.getValue(),override); }

    //! set override flag for SoFontSizeElement.
    public static void         setFontSizeOverride(SoState state, SoNode node,
                                            boolean override)
        { SO_SET_OVERRIDE(state, ElementMask.FONT_SIZE.getValue(),override); }
    
    //! set override flag for SoLinePatternElement.
    public static void         setLinePatternOverride(SoState state, SoNode node,
                                               boolean override)
        { SO_SET_OVERRIDE(state,ElementMask.LINE_PATTERN.getValue(),override); }

    //! set override flag for SoLineWidthElement.
    public static void         setLineWidthOverride(SoState state, SoNode node,
                                             boolean override)
        { SO_SET_OVERRIDE(state,ElementMask.LINE_WIDTH.getValue(),override); }                                     
                                             
    //! set override flag for SoPointSizeElement.
    public static void         setPointSizeOverride(SoState state, SoNode node,
                                             boolean override)
        { SO_SET_OVERRIDE(state,ElementMask.POINT_SIZE.getValue(),override); }

    //! set override flag for SoShapeHintsElement.
    public static void         setShapeHintsOverride(SoState state, SoNode node,
                                              boolean override)
        { SO_SET_OVERRIDE(state,ElementMask.SHAPE_HINTS.getValue(),override); }
    
    //! set override flag for SoShininessElement.
    public static void         setShininessOverride(SoState state, SoNode node,
                                             boolean override)
        { SO_SET_OVERRIDE(state,ElementMask.SHININESS.getValue(),override); }
    
    //! set override flag for SoSpecularColorElement.
    public static void         setSpecularColorOverride(SoState state, SoNode node,
                                                 boolean override)
        { SO_SET_OVERRIDE(state,ElementMask.SPECULAR_COLOR.getValue(),override); }
    
    //! set override flag for SoTransparencyElement.
    public static void         setTransparencyOverride(SoState state, SoNode node,
                                                 boolean override) {
    SoOverrideElement   elt =
        (SoOverrideElement )getElement(state, classStackIndexMap.get(SoOverrideElement.class)); 
    if (elt != null) { 
        if (override){
            elt.flags |= ElementMask.TRANSPARENCY.getValue();
            SoShapeStyleElement.setOverrides(state, true);
        } 
        else {
            elt.flags &= ~ElementMask.TRANSPARENCY.getValue();
            if ((elt.flags & ElementMask.MATERIAL_BINDING.getValue())==0)
                SoShapeStyleElement.setOverrides(state, false);
        }
    }             
    	
    }
    
    /*!
    Can be used to set the transparency type override.

    \sa setDiffuseColorOverride().
  */
  public static void
  setTransparencyTypeOverride(SoState state,
                                                 SoNode  node ,
                                                 boolean override)
  {
    SO_SET_OVERRIDE(state, ElementMask.TRANSPARENCY_TYPE.getValue(), override);
  }

  /*!
  Can be used to set normal vector override.

  \CLASS_FUNCTION_EXTENSION

  \since Coin 2.0
*/
public static void
setNormalVectorOverride(SoState state,
                                           SoNode  node ,
                                           boolean override)
{
  SO_SET_OVERRIDE(state, ElementMask.NORMAL_VECTOR.getValue(), override);
}

/*!
  Can be used to set normal binding override.

  \CLASS_FUNCTION_EXTENSION

  \since Coin 2.0
*/
public static void
setNormalBindingOverride(SoState state,
                                            SoNode node,
                                            boolean override)
{
  SO_SET_OVERRIDE(state, ElementMask.NORMAL_BINDING.getValue(), override);
}

    

/////////////////////////////////////////////////////////////////////////
//
//  Description:  set Material Binding Override (plus set shapeStyle)
//
//
//  Use: static, public.
/////////////////////////////////////////////////////////////////////////
public static void
setMaterialBindingOverride(SoState state, SoNode node, 
    boolean override)
{
    SoOverrideElement   elt =
        (SoOverrideElement )getElement(state, classStackIndexMap.get(SoOverrideElement.class)); 
    if (elt != null) { 
        if (override){
            elt.flags |= ElementMask.MATERIAL_BINDING.getValue();
            SoShapeStyleElement.setOverrides(state, true);
        } 
        else {
            elt.flags &= ~ElementMask.MATERIAL_BINDING.getValue();
            if ((elt.flags & (ElementMask.DIFFUSE_COLOR.getValue()|ElementMask.TRANSPARENCY.getValue()))==0)
                SoShapeStyleElement.setOverrides(state, false);
        }
    }             
}

    //! set override flag for SoPickStyleElement.
    public static void         setPickStyleOverride(SoState state, SoNode node,
                                            boolean override)
        { SO_SET_OVERRIDE(state,ElementMask.PICK_STYLE.getValue(),override); }
        
    //! set override flag for SoPolygonOffsetElement.
    public static void         setPolygonOffsetOverride(SoState state, SoNode node,
                                                 boolean override)
         { SO_SET_OVERRIDE(state,ElementMask.POLYGON_OFFSET.getValue(),override); }


}
