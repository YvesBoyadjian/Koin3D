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
 |      This file defines the SoLazyElement and SoColorPacker classes.
 |
 |   Author(s)          : Alan Norton, Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.elements;

import static org.lwjgl.opengl.GL11.glDrawElements;

import java.nio.ByteBuffer;

import com.jogamp.opengl.GL2;

import jscenegraph.database.inventor.SbColor;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoMFColor;
import jscenegraph.database.inventor.fields.SoMFFloat;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoPackedColor;
import jscenegraph.mevis.inventor.elements.SoGLVBOElement;

///////////////////////////////////////////////////////////////////////////////
///
///  \class SoLazyElement
///  \ingroup Elements
///
///  Element that manages several properties such as colors, that need to
///  be lazily tracked by GL.  Also includes:
///      Transparencies
///      GLColorMaterial
///      GLBlendEnablement 
///      PolygonStipple
///      Light model
///      Color index
///
//////////////////////////////////////////////////////////////////////////////
/**
 * @author Yves Boyadjian
 *
 */
public class SoLazyElement extends SoElement {
	
    // Fixed positions for attributes, shaders need to use these fixed attribute locations:
    public enum VertexAttribs {
      ATTRIB_VERTEX ( 0),
      ATTRIB_NORMAL ( 1),
      ATTRIB_COLOR  ( 2),
      ATTRIB_TEXCOORD ( 3);
      
      private int value;
      
      VertexAttribs(int value) {
    	  this.value = value;
      }
      public int getValue() {
    	  return value;
      }
    };

	
	
	public static interface SoDrawElementsCallback {
		void call(Object userData, SoState state, /*GLenum*/int mode,
                    /*GLsizei*/int count,
                    /*GLenum*/int type,
                    /*GLvoid*/Object  indices);
	}

	public static interface SoDrawArraysCallback {
		void call(Object userData, SoState state, /*GLenum*/int mode,	
                                  /*GLint*/int first,
                                  /*GLsizei*/int count);
}
	
	
	//! threshold to ignore changes in shininess:
	public static final float SO_LAZY_SHINY_THRESHOLD       =  0.005f;

	 //! number of components (subelements) in this element:
	   public static final int SO_LAZY_NUM_COMPONENTS         = 9; // TODO
	    	

	   public
		            //! Following masks and cases define the components of the
		            //! lazy element.  Masks are needed by SoEXTENDER apps that
		            //! need to use SoGLLazyElement.reset(bitmask) to invalidate
		            //! GL values of particular components.
		            //! NOTE: the order of these cases should not be changed without
		            //! careful consideration of dependencies in the reallySend method.
		       enum cases{
		           LIGHT_MODEL_CASE        (   0),
		           COLOR_MATERIAL_CASE     (   1),
		           DIFFUSE_CASE            (   2),
		           AMBIENT_CASE            (   3),
		           EMISSIVE_CASE           (   4),
		           SPECULAR_CASE           (   5),
		           SHININESS_CASE          (   6),
		           BLENDING_CASE           (   7),
		           TRANSPARENCY_CASE       (   8),
		    	    VERTEXORDERING_CASE(9), // COIN 3D
		    	    TWOSIDE_CASE(10), // COIN 3D
		    	    CULLING_CASE(11), // COIN 3D
		    	    SHADE_MODEL_CASE(12), // COIN 3D
		    	    GLIMAGE_CASE(13), // COIN 3D
		    	    ALPHATEST_CASE(14), // COIN 3D
		    	    LAZYCASES_LAST(15); // must be last
		           
		           private int value;
		           
		           cases(int value) {
		        	   this.value = value;
		           }
		           
		           public int getValue() {
		        	   return value;
		           }
		           
		           public static cases fromValue(int value) {
		        	   switch(value) {
		        	   case 0: return LIGHT_MODEL_CASE;
		        	   case 1: return COLOR_MATERIAL_CASE;
		        	   case 2: return DIFFUSE_CASE;
		        	   case 3: return AMBIENT_CASE;
		        	   case 4: return EMISSIVE_CASE;
		        	   case 5: return SPECULAR_CASE;
		        	   case 6: return SHININESS_CASE;
		        	   case 7: return BLENDING_CASE;
		        	   case 8: return TRANSPARENCY_CASE;
		        	   case 9: return	VERTEXORDERING_CASE; // COIN 3D
		        	   case 10: return	 TWOSIDE_CASE; // COIN 3D
		        	   case 11: return	 CULLING_CASE; // COIN 3D
		        	   case 12: return	 SHADE_MODEL_CASE; // COIN 3D
		        	   case 13: return	 GLIMAGE_CASE; // COIN 3D
		        	   case 14: return	 ALPHATEST_CASE; // COIN 3D
		        	   case 15: return	 LAZYCASES_LAST; // must be last
		        	   default: return null;
		        	   }
		           }
		       };
		       public enum masks{
		           LIGHT_MODEL_MASK        ( 1<<cases.LIGHT_MODEL_CASE.getValue()),
		           COLOR_MATERIAL_MASK     ( 1<<cases.COLOR_MATERIAL_CASE.getValue()),
		           DIFFUSE_MASK            ( 1<<cases.DIFFUSE_CASE.getValue()),
		           AMBIENT_MASK            ( 1<<cases.AMBIENT_CASE.getValue()),
		           EMISSIVE_MASK           ( 1<<cases.EMISSIVE_CASE.getValue()),
		           SPECULAR_MASK           ( 1<<cases.SPECULAR_CASE.getValue()),
		           SHININESS_MASK          ( 1<<cases.SHININESS_CASE.getValue()),
		           TRANSPARENCY_MASK       ( 1<<cases.TRANSPARENCY_CASE.getValue()),
		           BLENDING_MASK           ( 1<<cases.BLENDING_CASE.getValue()),
		           VERTEXORDERING_MASK ( 1 << cases.VERTEXORDERING_CASE.getValue()),     // 0x0200
		           TWOSIDE_MASK ( 1 << cases.TWOSIDE_CASE.getValue()),                   // 0x0400
		           CULLING_MASK ( 1 << cases.CULLING_CASE.getValue()),                   // 0x0800
		           SHADE_MODEL_MASK ( 1 << cases.SHADE_MODEL_CASE.getValue()),           // 0x1000
		           GLIMAGE_MASK ( 1 << cases.GLIMAGE_CASE.getValue()),                   // 0x2000
		           ALPHATEST_MASK ( 1 << cases.ALPHATEST_CASE.getValue()),               // 0x4000
		           ALL_MASK                ( (1<</*SO_LAZY_NUM_COMPONENTS*/cases.LAZYCASES_LAST.getValue())-1);
		           
		           private int value;
		           
		           masks(int value) {
		        	   this.value = value;
		           }
		           
		           public int getValue() {
		        	   return value;
		           }
		       };
		   
		       //!Enum values to be used in setting/getting light model: 
		       public	enum LightModel {
		           BASE_COLOR      (0),
		           PHONG           (1);
		           
		           private int value;
		           
		           LightModel(int value) {
		        	   this.value = value;
		           }
		           
		           public int getValue() {
		        	   return value;
		           }
		           
		           public static LightModel fromValue(int value) {
		        	   switch(value) {
		        	   case 0:
		        		   return BASE_COLOR;
		        	   case 1:
		        		   return PHONG;
		        	   default:
		        		   return null;
		        	   }
		           }
		       };
		       
		       //!Struct to hold the inventor state:
		       class  ivStateStructName{
		                //!Keep nodeID to compare diffuse GL and diffuse inventor state:
		                //!0 is initial value, 1 is invalid    
		                int            diffuseNodeId;
		        
		                //! for transparency, keep either nodeid, or 0 if opaque.  Value of
		                //! 1 indicates invalid.
		                int            transpNodeId;
		        
		                //! store a value of each  color component; ambient, emissive,
		                //! specular, shininess, or appropriate info to identify state.
		                final SbColor             ambientColor = new SbColor();
		                final SbColor             emissiveColor = new SbColor();
		                final SbColor             specularColor = new SbColor();
		                float               shininess;
		                boolean              colorMaterial;
		                boolean              blending;
		                int             lightModel;
		                int             stippleNum;
		        
		                //! following are not used for matching GL & IV, but must
		                //! be copied on push:   
		                boolean              packed;
		                boolean              packedTransparent;
		                int             numDiffuseColors;
		                int             numTransparencies;
		                SbColor[]       diffuseColors;
		                float[]         transparencies;
		                int[]      packedColors;
		                int[]      colorIndices;
		                int              transpType;
		                int            cacheLevelSetBits;
		                int            cacheLevelSendBits;
        boolean                overrideBlending;

        boolean                useVertexAttributes;

        SoDrawArraysCallback   drawArraysCallback;
        Object                   drawArraysCallbackUserData;
        SoDrawElementsCallback drawElementsCallback;
        Object                   drawElementsCallbackUserData;
		                
		                public void copyFrom(ivStateStructName other) {
			                //!Keep nodeID to compare diffuse GL and diffuse inventor state:
			                //!0 is initial value, 1 is invalid    
			                           diffuseNodeId = other.diffuseNodeId;
			        
			                //! for transparency, keep either nodeid, or 0 if opaque.  Value of
			                //! 1 indicates invalid.
			                            transpNodeId = other.transpNodeId;
			        
			                //! store a value of each  color component; ambient, emissive,
			                //! specular, shininess, or appropriate info to identify state.
			                ambientColor.copyFrom(other.ambientColor);
			                emissiveColor.copyFrom(other.emissiveColor);
			                specularColor.copyFrom(other.specularColor);
			                               shininess = other.shininess;
			                              colorMaterial = other.colorMaterial;
			                              blending = other.blending;
			                            lightModel = other.lightModel;
			                             stippleNum = other.stippleNum;
			        
			                //! following are not used for matching GL & IV, but must
			                //! be copied on push:   
			                              packed = other.packed;
			                              packedTransparent = other.packedTransparent;
			                             numDiffuseColors = other.numDiffuseColors;
			                             numTransparencies = other.numTransparencies;
			                       diffuseColors = other.diffuseColors;
			                         transparencies = other.transparencies;
			                      packedColors = other.packedColors;
			                      colorIndices = other.colorIndices;
			                              transpType = other.transpType;
			                            cacheLevelSetBits = other.cacheLevelSetBits;
			                            cacheLevelSendBits = other.cacheLevelSendBits;
		                }
		            }
		       
		       public enum internalMasks{
        OTHER_COLOR_MASK(         masks.AMBIENT_MASK.getValue()|masks.EMISSIVE_MASK.getValue()|masks.SPECULAR_MASK.getValue()|masks.SHININESS_MASK.getValue()),   
        ALL_COLOR_MASK(           OTHER_COLOR_MASK.getValue()|masks.DIFFUSE_MASK.getValue()), 
        NO_COLOR_MASK(            masks.ALL_MASK.getValue() & (~ALL_COLOR_MASK.getValue())), 
        ALL_BUT_DIFFUSE_MASK(           masks.ALL_MASK.getValue() &(~ masks.DIFFUSE_MASK.getValue())), 
        DIFFUSE_ONLY_MASK(         masks.ALL_MASK.getValue() &(~ OTHER_COLOR_MASK.getValue()));
        
        private int value;
        
        	internalMasks(int value) {
        		this.value = value;
        	}
        	
        	public int getValue() {
        		return value;
        	}
        };
   
		       
		       
		       protected final ivStateStructName ivState = new ivStateStructName();
		       
		       private class CoinState {
		    	    public int glimageid;
		    	    public boolean istransparent;
		    	    public boolean alphatest;
		    	    public boolean glimageusealphatest;
		       }
		       
		       protected final CoinState coinstate = new CoinState();
		        		       
    //! This is more convenient here, but might logically be kept with
    //! SoGLLazyElement.  This is a bitmask indicating what components
    //! have not been sent to GL.
    protected int invalidBits;
 
  
    //!  store pointers to the default color, transp so that we can set
    //!  point to them if no other color or transp has been set.
    
    protected static SbColor[]      defaultDiffuseColor;
    protected static float[]        defaultTransparency;
    protected static int[]      defaultColorIndices;
    protected static int[]     defaultPackedColor;
		       
		       
		       //!Get a Writable instance, so will force a push if needed:
		      public      static SoLazyElement getWInstance(SoState state)
		                {return (SoLazyElement )
		                (state.getElement(classStackIndexMap.get(SoLazyElement.class)));}
		        		       
		       
		       //! Returns the top (current) instance of the element in the state
		            //! Note that the cache dependencies associated with this element
		            //! are managed differently from other elements:
		            //! this replaces the SoElement.getConstElement that is used by
		            //! standard elements, but which causes cache dependency.  Note
		            //! that this element is not const; however modifications to it
		            //! can cause problems.  SoEXTENDER apps should use only SoEXTENDER
		            //! methods on this element.
		       public     static  SoLazyElement  getInstance(SoState state)
		            {  return (SoLazyElement )
		                (state.getElementNoPush(classStackIndexMap.get(SoLazyElement.class)));}
		        		       
		       //!Specify inventor defaults for colors, etc.
		  public          static SbColor      getDefaultDiffuse()
		                    {return new SbColor(0.8f, 0.8f, 0.8f);}
		  public static SbColor      getDefaultAmbient()
		                    {return new SbColor(0.2f, 0.2f, 0.2f);}
		  public static SbColor      getDefaultSpecular()
		                    {return new SbColor(0.0f, 0.0f, 0.0f);}
		  public static SbColor      getDefaultEmissive()
		                    {return new SbColor(0.0f, 0.0f, 0.0f);}
		  public static float        getDefaultShininess()
		                    {return 0.2f;}
		  public static int     getDefaultPacked()
		                    {return (0xccccccff);}
		  public static float        getDefaultTransparency()
		                    {return 0.0f;}
		  public static int      getDefaultLightModel()
		                    {return LightModel.PHONG.getValue();}
		  public static int      getDefaultColorIndex()
		                    {return 1;}
		        		       
		  
/////////////////////////////////////////////////////////////////////////
//
// Description:
//  matches is not used by this element:
//
/////////////////////////////////////////////////////////////////////////
public boolean
matches( SoElement element)
{
//#ifdef DEBUG
    SoDebugError.post("SoLazyElement.matches", 
            "Should never be called\n");
//#endif
    return true;
}
		  
	 /////////////////////////////////////////////////////////////////////////
	   //
	   // Description:
	   //  copyMatchInfo is not used by this element:
	   //
	   /////////////////////////////////////////////////////////////////////////
	   public SoElement
	   copyMatchInfo()
	   {
//	   #ifdef DEBUG
	       SoDebugError.post("SoLazyElement.copyMatchInfo",
	               "Should never be called\n");
//	   #endif
	       return null;
	   }

	   ///////////////////////////////////////////////////////////////////////
	    //
	    // Description: static set() method for light model 
	    //
	    // use:  public, SoEXTERNAL, static
	    //
	    ///////////////////////////////////////////////////////////////////////  
	    public static void
	    setLightModel(SoState state, int model)
	    {
	        SoLazyElement curElt = SoLazyElement.getInstance(state);
	        if (model != curElt.ivState.lightModel)
	            getWInstance(state).setLightModelElt(state,  model);
	        else if (state.isCacheOpen())
	            curElt.registerRedundantSet(state, masks.LIGHT_MODEL_MASK.getValue());
	    }	   
	    	    
	    ////////////////////////////////////////////////////////////////////////
	     //
	     // Description:
	     //    set the light model in the element 
	     //    virtual, to be overridden 
	     //
	     // Use: protected
	     ////////////////////////////////////////////////////////////////////////
	     
	    public void
	     setLightModelElt(SoState state, int model)
	     
	     {
	         ivState.lightModel = model;
	         // also set the shapestyle version of this:
	         SoShapeStyleElement.setLightModel(state, model);
	         if (model == LightModel.BASE_COLOR.getValue()) setColorMaterialElt(false);
	     }

	    ////////////////////////////////////////////////////////////////////////
	     //
	     // Description:
	     //    set the ColorMaterial in the element 
	     //    virtual, to be overridden 
	     //
	     // Use: protected
	     ////////////////////////////////////////////////////////////////////////
	     
	    public void
	     setColorMaterialElt( boolean value )
	    
	     {
	         if (ivState.lightModel == LightModel.BASE_COLOR.getValue()) value = false;
	         ivState.colorMaterial = value;
	     }
	    
	    /**
	     * method to tell the cache that a redundant set was issued. only the GL version does any work. 
	     * 
	     * @param statet
	     */
	    /////////////////////////////////////////////////////////////////////////////
	     //
	     //  Description:
	     //
	     //  virtual method does no work (GL version does more)
	     //
	     ////////////////////////////////////////////////////////////////////////////
	    public void
	     registerRedundantSet(SoState state, int mask)
	     {
	     }	    

	     //! Methods to inquire about current colors:
	     public    int                     getNumDiffuse() 
	             {return ivState.numDiffuseColors;}
	     public int                     getNumTransparencies() 
	             {return ivState.numTransparencies;}
	     public int                     getNumColorIndices() 
	             {if (ivState.colorIndices != null)
	                 return ivState.numDiffuseColors;
	              else return 0;}
	     public boolean                      isPacked() 
	             {return ivState.packed;}
	     public boolean                      isTransparent() 
	             {return(ivState.packedTransparent ||
	                 (ivState.numTransparencies > 1)||
	                 (ivState.transparencies[0]>0.0));}
	     
	     //!Following SoINTERNAL get() methods do NOT cause cache dependency, should
	          //!only be invoked by nodes that use the reallySend method on SoGLLazyElement
	          //!to establish correct cache dependencies by tracking what was actually
	          //!sent to GL.
	     public     int[] getPackedPointer()
	              {return ivState.packedColors;}
	      
	     public SbColor[]      getDiffusePointer()
	              {return ivState.diffuseColors; }
	      
	     public int[]       getColorIndexPointer()
	              {return ivState.colorIndices;}
	      
	     public float[]         getTransparencyPointer()
	              {return ivState.transparencies;}
	      	     
	     //! Returns number of transparency levels supported with stipple
	     //! patterns. (Add one - solid - that is not included in this number.)
	     static int          getNumPatterns()        { return 64; }
	     
////////////////////////////////////////////////////////////////////////
//
// Description:
//    get the light model from the element 
//
// Use: public, static
////////////////////////////////////////////////////////////////////////
public static int
getLightModel(SoState state) 
{   
    SoLazyElement curElt = getInstance(state);
    if(state.isCacheOpen()) 
        curElt.registerGetDependence(state, masks.LIGHT_MODEL_MASK.getValue());  
    return curElt.ivState.lightModel;
}   
/////////////////////////////////////////////////////////////////////////////
//
//  Description:
//
//  virtual method does no work (GL version does more)
//
////////////////////////////////////////////////////////////////////////////
public void
registerGetDependence(SoState state , int mask)
{
}
///////////////////////////////////////////////////////////////////////
//
// Description: static set() method for color material 
//
// use:  public, SoEXTERNAL, static
//
///////////////////////////////////////////////////////////////////////  
public static void    
setColorMaterial(SoState state, boolean value)
{
    SoLazyElement curElt = SoLazyElement.getInstance(state);
    if (value != curElt.ivState.colorMaterial)
        getWInstance(state).setColorMaterialElt(value);
    else if (state.isCacheOpen())
        curElt.registerRedundantSet(state, masks.COLOR_MATERIAL_MASK.getValue());
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Initializes element.
//
// Use: public
////////////////////////////////////////////////////////////////////////
public void init(SoState state)

{
    //  Set to GL defaults:
    ivState.ambientColor.copyFrom( getDefaultAmbient());
    ivState.emissiveColor.copyFrom( getDefaultEmissive());
    ivState.specularColor.copyFrom( getDefaultSpecular());
    ivState.shininess           = getDefaultShininess();
    ivState.colorMaterial       = false;
    ivState.blending            = false;
    ivState.lightModel          = LightModel.PHONG.getValue();
    
    // Initialize default color storage if not already done
    if (defaultDiffuseColor == null) {
        defaultDiffuseColor     = new SbColor[1];
        defaultDiffuseColor[0] = new SbColor(getDefaultDiffuse());
        defaultTransparency     = new float[1];
        defaultTransparency[0]    = getDefaultTransparency();
        defaultColorIndices     = new int[1];
        defaultColorIndices[0]    = getDefaultColorIndex();
        defaultPackedColor      = new int[1];
        defaultPackedColor[0]     = getDefaultPacked();
    }
    
    //following value will be matched with the default color, must
    //differ from 1 (invalid) and any  legitimate nodeid. 
    ivState.diffuseNodeId       = 0;
    ivState.transpNodeId        = 0;
    //zero corresponds to transparency off (default).
    ivState.stippleNum          = 0;
    ivState.diffuseColors       = defaultDiffuseColor;
    ivState.transparencies      = defaultTransparency;
    ivState.colorIndices        = defaultColorIndices;
    ivState.packedColors        = defaultPackedColor;

    ivState.numDiffuseColors    = 1;
    ivState.numTransparencies   = 1;
    ivState.packed              = false;
    ivState.packedTransparent   = false;
    ivState.transpType          = SoGLRenderAction.TransparencyType.SCREEN_DOOR.ordinal();  
    ivState.cacheLevelSetBits   = 0;
    ivState.cacheLevelSendBits  = 0;
    ivState.overrideBlending    = false;
    
    ivState.useVertexAttributes = false;

    ivState.drawArraysCallback = null;
    ivState.drawElementsCallback = null;    
    ivState.drawArraysCallbackUserData = null;
    ivState.drawElementsCallbackUserData = null;        
}

	private static final SbColor unpacker = new SbColor(0, 0, 0);

////////////////////////////////////////////////////////////////////////
//
// Description:
//    get the indexed diffuse color in the element 
//
// Use: public, static
////////////////////////////////////////////////////////////////////////
public static SbColor 
getDiffuse(SoState state, int index) 
{
    SoLazyElement curElt = getInstance(state);
    if(state.isCacheOpen()) curElt.registerGetDependence(state, masks.DIFFUSE_MASK.getValue());  
//#ifdef DEBUG
    if (index > curElt.ivState.numDiffuseColors || index < 0){
        SoDebugError.post("SoLazyElement.getDiffuse", 
                        "invalid index");
        return(new SbColor(defaultDiffuseColor[0]));
    }
//#endif
    if (!curElt.ivState.packed) return (curElt.ivState.diffuseColors[index]);
    unpacker.copyFrom( new SbColor( 
       ((curElt.ivState.packedColors[index] & 0xff000000) >> 24) * 1.0f/255,  
       ((curElt.ivState.packedColors[index] & 0xff0000) >> 16) * 1.0f/255,              
       ((curElt.ivState.packedColors[index] & 0xff00)>> 8) * 1.0f/255)); 
    return unpacker;
      
}
////////////////////////////////////////////////////////////////////////
//
// Description:
//    get the indexed transparency in the element 
//
// Use: public, static
////////////////////////////////////////////////////////////////////////
public static float
getTransparency(SoState state, int index)  
{
    SoLazyElement curElt = getInstance(state);
    if(state.isCacheOpen()) curElt.registerGetDependence(state, masks.DIFFUSE_MASK.getValue());  
//#ifdef DEBUG
    if (index > curElt.ivState.numTransparencies || index < 0){
        SoDebugError.post("SoLazyElement.getTransparency", 
                        "invalid index");
        return(curElt.defaultTransparency[0]);
    }
//#endif
    if (!curElt.ivState.packed) return (curElt.ivState.transparencies[index]);
    return( 1.0f - ((curElt.ivState.packedColors[index] & 0xff) * 1.0f/255));
             
}
////////////////////////////////////////////////////////////////////////
//
// Description:
//    get the color index from the element 
//
// Use: public, static
////////////////////////////////////////////////////////////////////////
public static int
getColorIndex(SoState state, int index)  
{
    SoLazyElement curElt = getInstance(state);
    if(state.isCacheOpen()) curElt.registerGetDependence(state, masks.DIFFUSE_MASK.getValue());  
//#ifdef DEBUG
    if (index > curElt.ivState.numDiffuseColors || index < 0){
        SoDebugError.post("SoLazyElement.getColorIndex", 
                        "invalid index");
        return(curElt.getDefaultColorIndex());
    }
//#endif
    return (curElt.ivState.colorIndices[index]);
             
}
////////////////////////////////////////////////////////////////////////
//
// Description:
//    get the packed color from the element 
//
// Use: public, static
////////////////////////////////////////////////////////////////////////
public static int[]
getPackedColors(SoState state) 
{ 
    SoLazyElement curElt = getInstance(state);
    if(state.isCacheOpen()) curElt.registerGetDependence(state, masks.DIFFUSE_MASK.getValue());    
    return curElt.ivState.packedColors;
}
////////////////////////////////////////////////////////////////////////
//
// Description:
//    get the color index from the element 
//
// Use: public, static
////////////////////////////////////////////////////////////////////////
public static int[]
getColorIndices(SoState state) 
{   
    SoLazyElement curElt = getInstance(state);
    if(state.isCacheOpen()) curElt.registerGetDependence(state, masks.DIFFUSE_MASK.getValue());           
    return curElt.ivState.colorIndices;
}
////////////////////////////////////////////////////////////////////////
//
// Description:
//    get the ambient color from the element 
//
// Use: public, static
////////////////////////////////////////////////////////////////////////
public static SbColor
getAmbient(SoState state) 
{
    SoLazyElement curElt = getInstance(state);
    if(state.isCacheOpen()) curElt.registerGetDependence(state, masks.AMBIENT_MASK.getValue());      
    return curElt.ivState.ambientColor;
} 
////////////////////////////////////////////////////////////////////////
//
// Description:
//    get the emissive color from the element 
//
// Use: public, static
////////////////////////////////////////////////////////////////////////
public static SbColor
getEmissive(SoState state) 
{ 
    SoLazyElement curElt = getInstance(state);
    if(state.isCacheOpen()) curElt.registerGetDependence(state, masks.EMISSIVE_MASK.getValue());   
    return curElt.ivState.emissiveColor;
} 
////////////////////////////////////////////////////////////////////////
//
// Description:
//    get the specular color from the element 
//
// Use: public, static
////////////////////////////////////////////////////////////////////////
public static SbColor
getSpecular(SoState state) 
{   
    SoLazyElement curElt = getInstance(state);
    if(state.isCacheOpen()) curElt.registerGetDependence(state, masks.SPECULAR_MASK.getValue()); 
    return curElt.ivState.specularColor;
} 
////////////////////////////////////////////////////////////////////////
//
// Description:
//    get the shininess from the element 
//
// Use: public, static
////////////////////////////////////////////////////////////////////////
public static float
getShininess(SoState state) 
{       
    SoLazyElement curElt = getInstance(state);
    if(state.isCacheOpen()) 
        curElt.registerGetDependence(state, masks.SHININESS_MASK.getValue());     
    return curElt.ivState.shininess;
} 
////////////////////////////////////////////////////////////////////////
//
// Description:
//    get the color material state from the element 
//
// Use: public, static
////////////////////////////////////////////////////////////////////////
public static boolean
getColorMaterial(SoState state) 
{   
    SoLazyElement curElt = getInstance(state);
    if(state.isCacheOpen()) 
        curElt.registerGetDependence(state, masks.COLOR_MATERIAL_MASK.getValue());  
    return curElt.ivState.colorMaterial;
}  
////////////////////////////////////////////////////////////////////////
//
// Description:
//    get the blending state from the element 
//
// Use: public, static
////////////////////////////////////////////////////////////////////////
public static boolean
getBlending(SoState state) 
{   
    SoLazyElement curElt = getInstance(state);
    if(state.isCacheOpen()) curElt.registerGetDependence(state, masks.BLENDING_MASK.getValue());  
    return curElt.ivState.blending;
}  

///////////////////////////////////////////////////////////////////////
//
// Description: static set() method for diffuse color
//
// use:  public, SoEXTENDER, static
//
///////////////////////////////////////////////////////////////////////  
public static void    
setDiffuse(SoState state, SoNode node, int numColors, 
            SbColor[] colors, SoColorPacker cPacker)
{
    // if someone sets this directly, remove any color VBO
    SoGLVBOElement.unsetVBOIfEnabled(state, SoGLVBOElement.VBOType.COLOR_VBO);

    SoLazyElement curElt = SoLazyElement.getInstance(state);
    //Because we are getting the transparency value from state, there
    //is a get-dependence
    if(state.isCacheOpen())curElt.registerGetDependence(state, masks.DIFFUSE_MASK.getValue());
    if (curElt.ivState.diffuseNodeId !=  node.getNodeId() ||
         (!cPacker.transpMatch(curElt.ivState.transpNodeId))){
        getWInstance(state).setDiffuseElt(node,  numColors, colors, cPacker);
    }
    else if (state.isCacheOpen()){       
        curElt.registerRedundantSet(state, masks.DIFFUSE_MASK.getValue());
    }
}
///////////////////////////////////////////////////////////////////////
//
// Description: static set() method for transparency 
//
// use:  public, SoEXTENDER, static
//
///////////////////////////////////////////////////////////////////////  
public static void    
setTransparency(SoState state, SoNode node, int numTransp, 
            float[] transp, SoColorPacker cPacker)
{
    // if someone sets this directly, remove any color VBO
    SoGLVBOElement.unsetVBOIfEnabled(state, SoGLVBOElement.VBOType.COLOR_VBO);

    SoLazyElement curElt = SoLazyElement.getInstance(state);
    //Because we are getting the diffuse value from state, there
    //is a get-dependence
    if(state.isCacheOpen())curElt.registerGetDependence(state, masks.DIFFUSE_MASK.getValue());
    
    int testNodeId;
    if(numTransp == 1 && transp[0] == 0.0) testNodeId = 0;
    else testNodeId = node.getNodeId();
    
    if ((curElt.ivState.transpNodeId != testNodeId) || 
        (!cPacker.diffuseMatch(curElt.ivState.diffuseNodeId)))
        getWInstance(state).setTranspElt(node, numTransp, transp, cPacker);
    else if (state.isCacheOpen()) 
        curElt.registerRedundantSet(state, masks.TRANSPARENCY_MASK.getValue()|masks.DIFFUSE_MASK.getValue()); 
}           


///////////////////////////////////////////////////////////////////////
//
// Description: static set() method for packed colors 
//
// use:  public, SoEXTERNAL, static
//
///////////////////////////////////////////////////////////////////////  
public static void    
setPacked(SoState state, SoNode node,
            int numColors, final int[] colors)
{
    // if someone sets this directly, remove any color VBO
    SoGLVBOElement.unsetVBOIfEnabled(state, SoGLVBOElement.VBOType.COLOR_VBO);
   
    SoLazyElement curElt = SoLazyElement.getInstance(state);
    if (curElt.ivState.diffuseNodeId != (node.getNodeId()) ||     
            (!(curElt.ivState.packed)) || 
            (curElt.ivState.packedColors != colors)){
        getWInstance(state).setPackedElt( node, numColors, colors);
    } 
    else if (state.isCacheOpen()) 
        curElt.registerRedundantSet(state, masks.DIFFUSE_MASK.getValue()|masks.TRANSPARENCY_MASK.getValue());
}

///////////////////////////////////////////////////////////////////////
//
// Description: static set() method for ambient color 
//
// use:  public, SoEXTERNAL, static
//
///////////////////////////////////////////////////////////////////////  
public void    
setAmbient(SoState state, SbColor color)
{    
    SoLazyElement curElt = SoLazyElement.getInstance(state);
    if (color.operator_not_equal( curElt.ivState.ambientColor)){
        getWInstance(state).setAmbientElt(color);
    }  
    else if (state.isCacheOpen()){         
        curElt.registerRedundantSet(state, masks.AMBIENT_MASK.getValue());
    }
}       
///////////////////////////////////////////////////////////////////////
//
// Description: static set() method for emissive color 
//
// use:  public, SoEXTERNAL, static
//
///////////////////////////////////////////////////////////////////////  
public static void    
setEmissive(SoState state, SbColor color)
{
    SoLazyElement curElt = SoLazyElement.getInstance(state);
    if (color.operator_not_equal(curElt.ivState.emissiveColor))
        getWInstance(state).setEmissiveElt(color);
    else if  (state.isCacheOpen())         
        curElt.registerRedundantSet(state, masks.EMISSIVE_MASK.getValue());
}       
///////////////////////////////////////////////////////////////////////
//
// Description: static set() method for specular color 
//
// use:  public, SoEXTERNAL, static
//
///////////////////////////////////////////////////////////////////////  
public void    
setSpecular(SoState state, SbColor color)
{
    SoLazyElement curElt = SoLazyElement.getInstance(state);
    if (color.operator_not_equal( curElt.ivState.specularColor))
        getWInstance(state).setSpecularElt(color);         
    else if   (state.isCacheOpen())        
        curElt.registerRedundantSet(state, masks.SPECULAR_MASK.getValue());
}
///////////////////////////////////////////////////////////////////////
//
// Description: static set() method for shininess 
//
// use:  public, SoEXTERNAL, static
//
///////////////////////////////////////////////////////////////////////  
public void    
setShininess(SoState state, float value)
{
    SoLazyElement curElt = SoLazyElement.getInstance(state);
    if (Math.abs(value - curElt.ivState.shininess)> SO_LAZY_SHINY_THRESHOLD)
        getWInstance(state).setShininessElt(value);
    else if (state.isCacheOpen())
        curElt.registerRedundantSet(state, masks.SHININESS_MASK.getValue());
}
///////////////////////////////////////////////////////////////////////
//
// Description: static set() method for blending 
//
// use:  public, SoEXTERNAL, static
//
///////////////////////////////////////////////////////////////////////  
public static void    
setBlending(SoState state,  boolean value)
{
  SoLazyElement curElt = SoLazyElement.getInstance(state);
  if (!curElt.ivState.overrideBlending) {
    if (value != curElt.ivState.blending)
      getWInstance(state).setBlendingElt( value);
    else if (state.isCacheOpen())
      curElt.registerRedundantSet(state, masks.BLENDING_MASK.getValue());
  }
}
///////////////////////////////////////////////////////////////////////
//
// Description: static set() method for overriding blending 
//
// use:  public, SoEXTERNAL, static
//
///////////////////////////////////////////////////////////////////////  
public void    
  setOverrideBlending(SoState state,  boolean value)
{
  SoLazyElement curElt = SoLazyElement.getInstance(state);
  if (value != curElt.ivState.overrideBlending) {
    getWInstance(state).ivState.overrideBlending = value;
  }
}

///////////////////////////////////////////////////////////////////////
//
// Description: static set() method for all materials
//
// use:  public, SoEXTERNAL, static
//
///////////////////////////////////////////////////////////////////////  
public static void    
setMaterials(SoState state,  SoNode node, 
    int bitmask, SoColorPacker cPacker,  
    final SoMFColor diffuse, final SoMFFloat transp, final SoMFColor ambient,
    final SoMFColor emissive, final SoMFColor specular, 
    final SoMFFloat shininess)
{
    int realSet = 0;    
    SoLazyElement curElt = SoLazyElement.getInstance(state);
    
    // If we are setting transparency and not diffuse, or vice-versa,
    // then there is a get-dependence:
    if(state.isCacheOpen()){
        int tempMask = bitmask & (masks.DIFFUSE_MASK.getValue() | masks.TRANSPARENCY_MASK.getValue());
        if (tempMask != 0 && tempMask != (masks.DIFFUSE_MASK.getValue() | masks.TRANSPARENCY_MASK.getValue()))
            curElt.registerGetDependence(state, masks.DIFFUSE_MASK.getValue());
    }
    // build a mask (realSet) indicating what really will be set in the state:
    if ((bitmask & masks.EMISSIVE_MASK.getValue()) != 0 &&(emissive.operator_square_bracket(0).operator_not_equal(curElt.ivState.emissiveColor)))
        realSet |= masks.EMISSIVE_MASK.getValue();
    if ((bitmask & masks.SPECULAR_MASK.getValue()) != 0 &&(specular.operator_square_bracket(0).operator_not_equal(curElt.ivState.specularColor)))
        realSet |= masks.SPECULAR_MASK.getValue(); 
    if ((bitmask & masks.AMBIENT_MASK.getValue()) != 0 &&(ambient.operator_square_bracket(0).operator_not_equal(curElt.ivState.ambientColor)))
        realSet |= masks.AMBIENT_MASK.getValue();
    if ((bitmask & masks.SHININESS_MASK.getValue()) != 0 &&
            Math.abs(shininess.operator_square_bracket(0) - curElt.ivState.shininess)> 
            SO_LAZY_SHINY_THRESHOLD) realSet |= masks.SHININESS_MASK.getValue();
            
    int nodeId = node.getNodeId();
    if ((bitmask & masks.DIFFUSE_MASK.getValue()) != 0 && 
        nodeId != curElt.ivState.diffuseNodeId) realSet |= masks.DIFFUSE_MASK.getValue();

    //For transparency nodeid, opaque nodes are identified as nodeId = 0:       
    if(transp.getNum() == 1 && transp.operator_square_bracket(0) == 0.0) nodeId = 0;
    
    if (curElt.ivState.transpNodeId != nodeId && (bitmask & masks.TRANSPARENCY_MASK.getValue()) != 0)  
        realSet |= masks.TRANSPARENCY_MASK.getValue();
        
    if (realSet != 0){ 
        curElt = getWInstance(state);
        curElt.setMaterialElt(node, realSet, cPacker,  
            diffuse, transp, ambient, emissive, specular, shininess);
    }
    //Indicate redundant set for colors that matched the one in the state:
    if (state.isCacheOpen()){ 
        int notRealSet = bitmask & (~realSet);
        if(notRealSet != 0) curElt.registerRedundantSet(state, notRealSet);
    }  
    
}
////////////////////////////////////////////////////////////////////////
//
// Description:
//    set the Diffuse color in the element 
//    virtual, to be overridden
//
// Use: protected
////////////////////////////////////////////////////////////////////////

public void
setDiffuseElt(SoNode  node,  int numColors,  
        SbColor[] colors, SoColorPacker packer)
{

    ivState.diffuseNodeId = node.getNodeId();
    ivState.diffuseColors = colors;
    ivState.numDiffuseColors = numColors;
  
    ivState.packed=false;
    ivState.packedTransparent = false;
}
////////////////////////////////////////////////////////////////////////
//
// Description:
//    set the transparency in the element 
//    virtual, to be overridden
//
// Use: protected
////////////////////////////////////////////////////////////////////////

public void
setTranspElt(SoNode node, int numTrans, 
    float[] transpar, SoColorPacker packer )
{

    ivState.numTransparencies = numTrans;
    ivState.transparencies = transpar;
    ivState.stippleNum = 0;
    if (transpar[0] > 0.0) {
        if (ivState.transpType == SoGLRenderAction.TransparencyType.SCREEN_DOOR.getValue()){
            ivState.stippleNum =
                (int)(transpar[0]*getNumPatterns());
        }       
    }
    if (numTrans == 1 && transpar[0] == 0.0) ivState.transpNodeId = 0;
        else ivState.transpNodeId = node.getNodeId();
    ivState.packed=false;
    ivState.packedTransparent = false;
  

}
////////////////////////////////////////////////////////////////////////
//
// Description:
//    set the color indices in the element 
//    virtual, to be overridden
//
// Use: protected
////////////////////////////////////////////////////////////////////////

public void
setColorIndexElt( SoNode node,  int numIndices,  
        int[] indices)
{

    ivState.diffuseNodeId = node.getNodeId();
    ivState.numDiffuseColors = numIndices;
    ivState.colorIndices = indices;
    ivState.packed=false;
    ivState.packedTransparent = false;

}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    set the transparency type in the element 
//    (not virtual)
//
// Use: protected
////////////////////////////////////////////////////////////////////////

protected void
setTranspTypeElt(  int type)  

{
    ivState.transpType = type;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    set the packed diffuse color in the element 
//    virtual, to be overridden
//
// Use: protected
////////////////////////////////////////////////////////////////////////

protected void
setPackedElt( SoNode node,  int numColors,  
        final int[] colors)
{  
    ivState.diffuseNodeId   = node.getNodeId();
    ivState.numDiffuseColors = numColors;
    ivState.numTransparencies = numColors;
    ivState.stippleNum = 0;     
    if ((ivState.transpType == SoGLRenderAction.TransparencyType.SCREEN_DOOR.ordinal()) &&
            ((colors[0]&0xff) != 0xff)){        
        ivState.stippleNum = (int)(getNumPatterns()*
                (1.-(colors[0] & 0xff)*(1./255.)));         
    }   
    ivState.packedColors = colors;
    ivState.packed = true;
    ivState.packedTransparent = ((SoPackedColor)node).isTransparent();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    set the Ambient color in the element 
//    virtual, to be overridden
//
// Use: protected
////////////////////////////////////////////////////////////////////////

public void
setAmbientElt( SbColor color )

{
    ivState.ambientColor.setValue((float[])color.getValueRead());
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    set the Emissive color in the element 
//    virtual, to be overridden
//
// Use: protected
////////////////////////////////////////////////////////////////////////

public void
setEmissiveElt( SbColor color )

{
    ivState.emissiveColor.setValue((float[])color.getValueRead());
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    set the Specular color in the element 
//    Virtual, to be overridden
//
// Use: protected
////////////////////////////////////////////////////////////////////////

public void
setSpecularElt( SbColor color )
//
{
    ivState.specularColor.setValue((float[])color.getValueRead());
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    set the Shininess in the element 
//    virtual, to be overridden 
//
// Use: protected
////////////////////////////////////////////////////////////////////////

public void
setShininessElt(float value )

{
    ivState.shininess = value;
}
////////////////////////////////////////////////////////////////////////
//
// Description:
//    set the blending enablement in the element 
//    virtual, to be overridden 
//
// Use: protected
////////////////////////////////////////////////////////////////////////

public void
setBlendingElt(boolean value )

{
    ivState.blending = value;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    set all materials in the element 
//    virtual, to be overridden 
//
// Use: protected
////////////////////////////////////////////////////////////////////////

public void
setMaterialElt(SoNode node, int mask, SoColorPacker packer,  
    final SoMFColor diffuse, final SoMFFloat transp, 
    final SoMFColor ambient, final SoMFColor emissive, 
    final SoMFColor specular, final SoMFFloat shininess)
{
    if ((mask & masks.DIFFUSE_MASK.getValue()) != 0){
        ivState.diffuseNodeId = node.getNodeId();
        ivState.diffuseColors = diffuse.getValues(0);
        ivState.numDiffuseColors = diffuse.getNum(); 
        ivState.packed=false;
        ivState.packedTransparent = false;
    }
    if ((mask & masks.TRANSPARENCY_MASK.getValue()) != 0){
        ivState.numTransparencies = transp.getNum();
        ivState.transparencies = transp.getValuesFloat(0);
        ivState.stippleNum = 0;
        if ((ivState.transparencies[0]> 0.0) &&
                (ivState.transpType == SoGLRenderAction.TransparencyType.SCREEN_DOOR.getValue())) {
            ivState.stippleNum = 
                (int)(ivState.transparencies[0]*getNumPatterns());
        }
        ivState.packed=false;
        ivState.packedTransparent = false;
    }
    if ((mask & masks.AMBIENT_MASK.getValue())!=0)
        ivState.ambientColor.copyFrom(ambient.operator_square_bracket(0));  
    
    if ((mask & masks.EMISSIVE_MASK.getValue())!=0)
        ivState.emissiveColor.copyFrom(emissive.operator_square_bracket(0));
        
    if ((mask & masks.SPECULAR_MASK.getValue())!=0)
        ivState.specularColor.copyFrom(specular.operator_square_bracket(0));
    
    if ((mask & masks.SHININESS_MASK.getValue())!=0)
        ivState.shininess = shininess.operator_square_bracket(0);  
}


public void
setGLImageIdElt(int glimageid, boolean alphatest) // COIN 3D
{
  this.coinstate.glimageid = glimageid;
  this.coinstate.glimageusealphatest = alphatest;
}

public void
setAlphaTestElt(boolean onoff)
{
  this.coinstate.alphatest = onoff;
}


///////////////////////////////////////////////////////////////////////
//
// Description: static set() method for color indices
//
// use:  public, SoEXTENDER, static
//
///////////////////////////////////////////////////////////////////////  
public void    
setColorIndices(SoState state, SoNode node, int numIndices, 
            int[] indices)
{
    // if someone sets this directly, remove any color VBO
    SoGLVBOElement.unsetVBOIfEnabled(state, SoGLVBOElement.VBOType.COLOR_VBO);

    SoLazyElement curElt = SoLazyElement.getInstance(state);
    if (curElt.ivState.diffuseNodeId !=  node.getNodeId())
        getWInstance(state).setColorIndexElt(node, numIndices, indices);
    else if (state.isCacheOpen()) 
        curElt.registerRedundantSet(state, masks.DIFFUSE_MASK.getValue()); 
}           


///////////////////////////////////////////////////////////////////////
//
// Description: static set() method for transparencyType 
//
// use:  public, SoINTERNAL, static
//
///////////////////////////////////////////////////////////////////////  
public static void    
setTransparencyType(SoState state, int type)
{
    SoLazyElement curElt = SoLazyElement.getInstance(state);
    if (curElt.ivState.transpType != type)
        curElt.setTranspTypeElt( type);  
}


public void setDrawElementsCallback( SoState state, SoDrawElementsCallback cb, Object userData )
{
  SoLazyElement curElt = SoLazyElement.getInstance(state);
  curElt.ivState.drawElementsCallback = cb;
  curElt.ivState.drawElementsCallbackUserData = userData;
}

public void setUseVertexAttributes( SoState state, boolean flag )
{
  SoLazyElement curElt = SoLazyElement.getInstance(state);
  curElt.ivState.useVertexAttributes = flag;
}

public static boolean shouldUseVertexAttributes( SoState state )
{
  SoLazyElement curElt = SoLazyElement.getInstance(state);
  return curElt.ivState.useVertexAttributes;
}

public void setDrawArraysCallback( SoState state, SoDrawArraysCallback cb, Object userData )
{
  SoLazyElement curElt = SoLazyElement.getInstance(state);
  curElt.ivState.drawArraysCallback = cb;
  curElt.ivState.drawArraysCallbackUserData = userData;
}


public static void drawArrays( SoState state, /*GLenum*/int mode, /*GLint*/int first, /*GLsizei*/int count )
{
  SoLazyElement elt = getInstance(state);
  if (elt.ivState.drawArraysCallback != null) {
    elt.ivState.drawArraysCallback.call(elt.ivState.drawArraysCallbackUserData, state, mode, first, count);
  } else {
	  GL2 gl2 = state.getGL2();
    gl2.glDrawArrays(mode, first, count);
  }
}


public static void drawElements( SoState state, /*GLenum*/int mode, /*GLsizei*/int count, /*GLenum*/int type, /*GLvoid*/Object indices )
{
  SoLazyElement elt = getInstance(state);
  if (elt.ivState.drawElementsCallback != null) {
    elt.ivState.drawElementsCallback.call(elt.ivState.drawElementsCallbackUserData, state, mode, count, type, indices);
  } else {
	  GL2 gl2 = state.getGL2();
	  if(indices == null) { // java port
		  glDrawElements(mode, count, type, 0);
	  }
	  else {
		  glDrawElements(mode, /*count,*/ type, (ByteBuffer)indices);
	  }
  }
}

public static void
setGLImageId(SoState state, int glimageid, boolean alphatest) // COIN 3D
{
  SoLazyElement elem = SoLazyElement.getInstance(state);
  if (elem.coinstate.glimageid != glimageid) {
    elem = getWInstance(state);
    elem.setGLImageIdElt(glimageid, alphatest);
    if (state.isCacheOpen()) elem.lazyDidSet(SoLazyElement.masks.GLIMAGE_MASK.getValue());
  }
  else if (state.isCacheOpen()) {
    elem.lazyDidntSet(SoLazyElement.masks.GLIMAGE_MASK.getValue());
  }
  SoLazyElement.setAlphaTest(state, !elem.coinstate.istransparent && alphatest);
}


public static void
setAlphaTest(SoState  state, boolean onoff) // COIN 3D
{
  SoLazyElement elem = SoLazyElement.getInstance(state);
  if (elem.coinstate.alphatest != onoff) {
    elem = getWInstance(state);
    elem.setAlphaTestElt(onoff);
    if (state.isCacheOpen()) elem.lazyDidSet(SoLazyElement.masks.ALPHATEST_MASK.getValue());
  }
  else if (state.isCacheOpen()) {
    elem.lazyDidntSet(SoLazyElement.masks.ALPHATEST_MASK.getValue());
  }
}

public void
lazyDidSet(int mask) // TODO : implement in derived class
{
}

public void
lazyDidntSet(int mask)
{
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Pushes element, copies inventor state
//
// public, virtual
////////////////////////////////////////////////////////////////////////

public void
push(SoState state)
{
    SoLazyElement prevElt = (SoLazyElement)getNextInStack();
  
    ivState.copyFrom(prevElt.ivState);
   
}


}
