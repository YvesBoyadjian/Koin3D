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
import java.util.Objects;

import com.jogamp.opengl.GL2;

import jscenegraph.database.inventor.SbBasic;
import jscenegraph.database.inventor.SbColor;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoMFColor;
import jscenegraph.database.inventor.fields.SoMFFloat;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoPackedColor;
import jscenegraph.mevis.inventor.elements.SoGLVBOElement;
import jscenegraph.port.ByteBufferAble;
import jscenegraph.port.FloatArray;
import jscenegraph.port.IntArray;
import jscenegraph.port.IntArrayPtr;
import jscenegraph.port.SbColorArray;
import jscenegraph.port.memorybuffer.FloatMemoryBuffer;

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
	
	private static SbColorArray lazy_defaultdiffuse = null;
	private static FloatArray lazy_defaulttransp = null;
	private static IntArrayPtr lazy_defaultindex = null;
	private static IntArrayPtr lazy_defaultpacked = null;
	private static SbColorArray lazy_unpacked = null;

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

    public enum VertexOrdering { // COIN 3D
        CW(0),
        CCW(1);
    	
    	private int value;
    	
    	VertexOrdering(int value) {
    		this.value = value;
    	}
    	
    	public int getValue() {
    		return value;
    	}
    	
    	public static VertexOrdering fromValue(int value) {
    		switch(value) {
    		case 0: return CW;
    		case 1: return CCW;
    		default: return null;
    		}
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
	   public static final int SO_LAZY_NUM_COMPONENTS         = 15; // TODO
	    	

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
		                //int            diffuseNodeId;
		        
		                //! for transparency, keep either nodeid, or 0 if opaque.  Value of
		                //! 1 indicates invalid.
		                //int            transpNodeId;
		        
		                //! store a value of each  color component; ambient, emissive,
		                //! specular, shininess, or appropriate info to identify state.
		                final SbColor             ambientColor = new SbColor();
		                final SbColor             emissiveColor = new SbColor();
		                final SbColor             specularColor = new SbColor();
		                float               shininess;
		                boolean              colorMaterial;
		                //boolean              blending;
		                int             lightModel;
		                int             stippleNum;
		        
		                //! following are not used for matching GL & IV, but must
		                //! be copied on push:   
		                boolean              packed;
		                //boolean              packedTransparent;
		                int             numDiffuseColors;
		                int             numTransparencies;
		                //SbColorArray       diffuseColors;
		                FloatMemoryBuffer         transparencies;
		                //int[]      packedColors;
		                int[]      colorIndices;
		                //int              transpType;
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
			                           //diffuseNodeId = other.diffuseNodeId;
			        
			                //! for transparency, keep either nodeid, or 0 if opaque.  Value of
			                //! 1 indicates invalid.
			                            //transpNodeId = other.transpNodeId;
			        
			                //! store a value of each  color component; ambient, emissive,
			                //! specular, shininess, or appropriate info to identify state.
			                ambientColor.copyFrom(other.ambientColor);
			                emissiveColor.copyFrom(other.emissiveColor);
			                specularColor.copyFrom(other.specularColor);
			                               shininess = other.shininess;
			                              colorMaterial = other.colorMaterial;
			                              //blending = other.blending;
			                            lightModel = other.lightModel;
			                             stippleNum = other.stippleNum;
			        
			                //! following are not used for matching GL & IV, but must
			                //! be copied on push:   
			                              packed = other.packed;
			                              //packedTransparent = other.packedTransparent;
			                             numDiffuseColors = other.numDiffuseColors;
			                             numTransparencies = other.numTransparencies;
			                       //diffuseColors = other.diffuseColors;
			                         transparencies = other.transparencies;
			                      //packedColors = other.packedColors;
			                      colorIndices = other.colorIndices;
			                              //transpType = other.transpType;
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
   
		       
		       
		       //protected final ivStateStructName ivState = new ivStateStructName();
		       
		       class CoinState {
//		    	    public int glimageid;
//		    	    public boolean istransparent;
//		    	    public boolean alphatest;
//		    	    public boolean glimageusealphatest;
		    	    public final SbColor ambient = new SbColor();
		    	    public final SbColor specular = new SbColor();
		    	    public final SbColor emissive = new SbColor();
		    	    public float shininess;
		    	    public /*boolean*/int blending;
		    	    public int blend_sfactor;
		    	    public int blend_dfactor;
		    	    public int alpha_blend_sfactor;
		    	    public int alpha_blend_dfactor;
		    	    public int lightmodel;
		    	    public boolean packeddiffuse; // COIN 3D
		    	    public int numdiffuse; // COIN 3D
		    	    public int numtransp; // COIN 3D
		    	    public SbColorArray diffusearray; // COIN 3D
		    	    public IntArrayPtr packedarray;
		    	    public FloatArray transparray; // COIN 3D
		    	    public IntArrayPtr colorindexarray;
		    	    public int transptype;
		    	    public boolean istransparent;
		    	    public long diffusenodeid; // COIN 3D
		    	    public long transpnodeid; // COIN 3D
		    	    public int stipplenum; // COIN 3D
		    	    public /*VertexOrdering*/int vertexordering; // COIN 3D
		    	    public /*boolean*/int twoside; // COIN 3D
		    	    public /*boolean*/int culling;
		    	    public /*boolean*/int flatshading;
		    	    public int alphatestfunc;
		    	    public float alphatestvalue;
		    	    
		    	    public void copyFrom(CoinState other) {
			    	    ambient.copyFrom(other.ambient);
			    	    specular.copyFrom(other.specular);
			    	    emissive.copyFrom(other.emissive);
			    	    shininess = other.shininess;
			    	    blending = other.blending;
			    	    blend_sfactor = other.blend_sfactor;
			    	    blend_dfactor = other.blend_dfactor;
			    	    alpha_blend_sfactor = other.alpha_blend_sfactor;
			    	    alpha_blend_dfactor = other.alpha_blend_dfactor;
			    	    lightmodel = other.lightmodel;
			    	    packeddiffuse = other.packeddiffuse;
			    	    numdiffuse = other.numdiffuse;
			    	    numtransp = other.numtransp;
			    	    diffusearray = SbColorArray.copyOf(other.diffusearray);
			    	    packedarray = IntArrayPtr.copyOf(other.packedarray);
			    	    transparray = FloatArray.copyOf(other.transparray);
			    	    colorindexarray = IntArrayPtr.copyOf(other.colorindexarray);
			    	    transptype = other.transptype;
			    	    istransparent = other.istransparent;
			    	    diffusenodeid = other.diffusenodeid; // COIN 3D
			    	    transpnodeid = other.transpnodeid; // COIN 3D
			    	    stipplenum = other.stipplenum; // COIN 3D
			    	    vertexordering = other.vertexordering; // COIN 3D
			    	    twoside = other.twoside; // COIN 3D	
			    	    culling = other.culling;
			    	    flatshading = other.flatshading;
			    	    alphatestfunc = other.alphatestfunc;
			    	    alphatestvalue = other.alphatestvalue;
		    	    }
		       }
		       
		       protected final CoinState coinstate = new CoinState();
		        		       
    //! This is more convenient here, but might logically be kept with
    //! SoGLLazyElement.  This is a bitmask indicating what components
    //! have not been sent to GL.
    protected int invalidBits;
 
  
    //!  store pointers to the default color, transp so that we can set
    //!  point to them if no other color or transp has been set.
    
    protected static SbColorArray      defaultDiffuseColor;
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
	   
	   /*!
	   Internal function used for resetting the OpenGL state before FBO
	   rendering.
	 */
	 public static void
	 setToDefault(SoState state)
	 {
	   SoLazyElement elem = SoLazyElement.getWInstance(state);
	   elem./*SoLazyElement.*/init(state);
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
	    	  SoLazyElement elem = SoLazyElement.getInstance(state);
	    	  if (elem.coinstate.lightmodel != model) {
	    	    elem = getWInstance(state);
	    	    elem.setLightModelElt(state, model);
	    	    if (state.isCacheOpen()) elem.lazyDidSet(masks.LIGHT_MODEL_MASK.getValue());
	    	  }
	    	  else if (state.isCacheOpen()) {
	    	    elem.lazyDidntSet(masks.LIGHT_MODEL_MASK.getValue());
	    	  }
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
	         SoShapeStyleElement.setLightModel(state, model);
	    	this.coinstate.lightmodel = model;
	         // also set the shapestyle version of this:
	         //if (model == LightModel.BASE_COLOR.getValue()) setColorMaterialElt(false);
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
//	         if (ivState.lightModel == LightModel.BASE_COLOR.getValue()) value = false;
//	         ivState.colorMaterial = value;
	     }
	    
	    /**
	     * method to tell the cache that a redundant set was issued. only the GL version does any work. 
	     * 
	     * @param state
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
	             {return this.coinstate.numdiffuse;}
	     public int                     getNumTransparencies() 
	             {
	    	  if (this.coinstate.packeddiffuse) {
	    		    return this.coinstate.numdiffuse;
	    		  }
	    		  return this.coinstate.numtransp;
	    	 }
	     public int                     getNumColorIndices() 
	             {
	    	  return this.coinstate.numdiffuse;
	    	 }
	     public boolean                      isPacked() 
	             {
	    	  return this.coinstate.packeddiffuse;
	    	 }
	     public boolean                      isTransparent() 
	             {
	    	  return this.coinstate.istransparent;
	    	 }
	     
	     //!Following SoINTERNAL get() methods do NOT cause cache dependency, should
	          //!only be invoked by nodes that use the reallySend method on SoGLLazyElement
	          //!to establish correct cache dependencies by tracking what was actually
	          //!sent to GL.
	     public     IntArrayPtr getPackedPointer()
	              {
	    	  return this.coinstate.packedarray;
	    	 }
	      
	     public SbColorArray      getDiffusePointer()
	              {
	    	  return SbColorArray.copyOf(this.coinstate.diffusearray);
	    	 }
	      
	     public int[]       getColorIndexPointer()
	              {
	    	  //assert(0 && "color index mode is not supported in Coin");
	    	  return null;
	    	 }
	      
	     public FloatArray         getTransparencyPointer()
	              {
	    	  return this.coinstate.transparray;
	    	 }
	      	     
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
	  SoLazyElement elem = getInstance(state);
	  return elem.coinstate.lightmodel;
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
//    SoLazyElement curElt = SoLazyElement.getInstance(state);
//    if (value != curElt.ivState.colorMaterial)
//        getWInstance(state).setColorMaterialElt(value);
//    else if (state.isCacheOpen())
//        curElt.registerRedundantSet(state, masks.COLOR_MATERIAL_MASK.getValue());
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
//    ivState.ambientColor.copyFrom( getDefaultAmbient());
//    ivState.emissiveColor.copyFrom( getDefaultEmissive());
//    ivState.specularColor.copyFrom( getDefaultSpecular());
//    ivState.shininess           = getDefaultShininess();
//    ivState.colorMaterial       = false;
//    ivState.blending            = false;
//    ivState.lightModel          = LightModel.PHONG.getValue();
    
    // Initialize default color storage if not already done
    if (defaultDiffuseColor == null) {
        defaultDiffuseColor     = SbColorArray.allocate(1);
        defaultDiffuseColor.get(0).setValue(getDefaultDiffuse());
        defaultTransparency     = new float[1];
        defaultTransparency[0]    = getDefaultTransparency();
        defaultColorIndices     = new int[1];
        defaultColorIndices[0]    = getDefaultColorIndex();
        defaultPackedColor      = new int[1];
        defaultPackedColor[0]     = getDefaultPacked();
    }
    
    //following value will be matched with the default color, must
    //differ from 1 (invalid) and any  legitimate nodeid. 
//    ivState.diffuseNodeId       = 0;
//    ivState.transpNodeId        = 0;
//    //zero corresponds to transparency off (default).
//    ivState.stippleNum          = 0;
//    ivState.diffuseColors       = defaultDiffuseColor;
//    ivState.transparencies      = defaultTransparency;
//    ivState.colorIndices        = defaultColorIndices;
//    ivState.packedColors        = defaultPackedColor;
//
//    ivState.numDiffuseColors    = 1;
//    ivState.numTransparencies   = 1;
//    ivState.packed              = false;
//    ivState.packedTransparent   = false;
//    ivState.transpType          = SoGLRenderAction.TransparencyType.SCREEN_DOOR.ordinal();  
//    ivState.cacheLevelSetBits   = 0;
//    ivState.cacheLevelSendBits  = 0;
//    ivState.overrideBlending    = false;
//    
//    ivState.useVertexAttributes = false;
//
//    ivState.drawArraysCallback = null;
//    ivState.drawElementsCallback = null;    
//    ivState.drawArraysCallbackUserData = null;
//    ivState.drawElementsCallbackUserData = null;        

    coinstate.ambient.copyFrom(getDefaultAmbient());
    coinstate.specular.copyFrom(getDefaultSpecular());
    coinstate.emissive.copyFrom(getDefaultEmissive());
    coinstate.shininess = getDefaultShininess();
    coinstate.blending = /*false*/0;
    coinstate.blend_sfactor = 0;
    coinstate.blend_dfactor = 0;
    coinstate.alpha_blend_sfactor = 0;
    coinstate.alpha_blend_dfactor = 0;
    coinstate.lightmodel = LightModel.PHONG.getValue();
    coinstate.packeddiffuse = false;
    coinstate.numdiffuse = 1;
    coinstate.numtransp = 1;
    coinstate.diffusearray = SbColorArray.copyOf(lazy_defaultdiffuse);
    coinstate.packedarray = IntArrayPtr.copyOf(lazy_defaultpacked);
    coinstate.transparray = FloatArray.copyOf(lazy_defaulttransp);
    coinstate.colorindexarray = IntArrayPtr.copyOf(lazy_defaultindex);
    coinstate.istransparent = false;
    coinstate.transptype = (int)(SoGLRenderAction.TransparencyType.BLEND.getValue());
    coinstate.diffusenodeid = 0;
    coinstate.transpnodeid = 0;
    coinstate.stipplenum = 0;
    coinstate.vertexordering = VertexOrdering.CCW.getValue();
    coinstate.twoside = false ? 1 : 0;
    coinstate.culling = false ? 1 : 0;
    coinstate.flatshading = false ? 1 : 0;
    coinstate.alphatestfunc = 0;
    coinstate.alphatestvalue = 0.5f;
}

	private static final SbColor unpacker = new SbColor(0, 0, 0);

////////////////////////////////////////////////////////////////////////
//
// Description:
//    get the indexed diffuse color in the element
//
// Use: public, static
////////////////////////////////////////////////////////////////////////
//public static SbColor
//getDiffuse(SoState state, int index)
//{
//    SoLazyElement curElt = getInstance(state);
//    if(state.isCacheOpen()) curElt.registerGetDependence(state, masks.DIFFUSE_MASK.getValue());
////#ifdef DEBUG
//    if (index > curElt.ivState.numDiffuseColors || index < 0){
//        SoDebugError.post("SoLazyElement.getDiffuse",
//                        "invalid index");
//        return(new SbColor(defaultDiffuseColor.get(0)));
//    }
////#endif
//    if (!curElt.ivState.packed) return (curElt.coinstate.diffusearray.get(index));
//    unpacker.copyFrom( new SbColor(
//       ((curElt.coinstate.packedarray.get(index) & 0xff000000) >> 24) * 1.0f/255,
//       ((curElt.coinstate.packedarray.get(index) & 0xff0000) >> 16) * 1.0f/255,
//       ((curElt.coinstate.packedarray.get(index) & 0xff00)>> 8) * 1.0f/255));
//    return unpacker;
//
//}

public static SbColor
	getDiffuse(SoState state, int index)
	{
		SoLazyElement elem = getInstance(state);
		if (elem.coinstate.packeddiffuse) {
			final float[] dummy = new float[1];
			return lazy_unpacked.get(0).setPackedValue(elem.coinstate.packedarray.get(index), dummy);
		}
		return elem.coinstate.diffusearray.get(index);
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
	  SoLazyElement elem = getInstance(state);

	  if (elem.coinstate.packeddiffuse) {
	    final float[] transp = new float[1];
	    final SbColor dummy = new SbColor();
	    int numt = elem.coinstate.numdiffuse;
	    dummy.setPackedValue(elem.coinstate.packedarray.get(index < numt ? index : numt-1), transp);
	    return transp[0];
	  }
	  int numt = elem.coinstate.numtransp;
	  return elem.coinstate.transparray.get(index < numt ? index : numt-1);	
}
//{
//    SoLazyElement curElt = getInstance(state);
//    if(state.isCacheOpen()) curElt.registerGetDependence(state, masks.DIFFUSE_MASK.getValue());  
////#ifdef DEBUG
//    if (index > curElt.ivState.numTransparencies || index < 0){
//        SoDebugError.post("SoLazyElement.getTransparency", 
//                        "invalid index");
//        return(curElt.defaultTransparency[0]);
//    }
////#endif
//    if (!curElt.ivState.packed) return (curElt.ivState.transparencies[index]);
//    return( 1.0f - ((curElt.coinstate.packedarray.get(index) & 0xff) * 1.0f/255));
//             
//}
////////////////////////////////////////////////////////////////////////
//
// Description:
//    get the color index from the element 
//
// Use: public, static
////////////////////////////////////////////////////////////////////////
public static int
getColorIndex(SoState state, int num)
{
    SoLazyElement elem = getInstance(state);
    if(state.isCacheOpen()) elem.registerGetDependence(state, masks.DIFFUSE_MASK.getValue());
//#ifdef DEBUG
    if (num > elem.coinstate.numdiffuse || num < 0){
        SoDebugError.post("SoLazyElement.getColorIndex", 
                        "invalid index");
        return(elem.getDefaultColorIndex());
    }
//#endif
    //return (curElt.ivState.colorIndices[index]);
	return elem.coinstate.colorindexarray.get(num);

}
////////////////////////////////////////////////////////////////////////
//
// Description:
//    get the packed color from the element 
//
// Use: public, static
////////////////////////////////////////////////////////////////////////
public static IntArrayPtr
getPackedColors(SoState state) 
{ 
    SoLazyElement curElt = getInstance(state);
    if(state.isCacheOpen()) curElt.registerGetDependence(state, masks.DIFFUSE_MASK.getValue());    
    //return curElt.ivState.packedColors;
    return curElt.coinstate.packedarray;
}
////////////////////////////////////////////////////////////////////////
//
// Description:
//    get the color index from the element 
//
// Use: public, static
////////////////////////////////////////////////////////////////////////
public static IntArrayPtr
getColorIndices(SoState state) 
{   
    SoLazyElement elem = getInstance(state);
    if(state.isCacheOpen()) elem.registerGetDependence(state, masks.DIFFUSE_MASK.getValue());
    //return curElt.ivState.colorIndices;
	return new IntArrayPtr(elem.coinstate.colorindexarray);
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
    SoLazyElement elem = getInstance(state);
    if(state.isCacheOpen()) elem.registerGetDependence(state, masks.AMBIENT_MASK.getValue());
    //return curElt.ivState.ambientColor;
	return elem.coinstate.ambient;
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
    SoLazyElement elem = getInstance(state);
    if(state.isCacheOpen()) elem.registerGetDependence(state, masks.EMISSIVE_MASK.getValue());
    //return curElt.ivState.emissiveColor;
	return elem.coinstate.emissive;
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
    SoLazyElement elem = getInstance(state);
    if(state.isCacheOpen()) elem.registerGetDependence(state, masks.SPECULAR_MASK.getValue());
    //return curElt.ivState.specularColor;
	return elem.coinstate.specular;
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
    SoLazyElement elem = getInstance(state);
    if(state.isCacheOpen())
		elem.registerGetDependence(state, masks.SHININESS_MASK.getValue());
    //return curElt.ivState.shininess;
	return elem.coinstate.shininess;
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
    return true;//curElt.ivState.colorMaterial;
}  
////////////////////////////////////////////////////////////////////////
//
// Description:
//    get the blending state from the element 
//
// Use: public, static
////////////////////////////////////////////////////////////////////////
// ! FIXME: write doc

public static boolean
getBlending(SoState state, int[] sfactor, int[] dfactor)
{
  SoLazyElement elem = getInstance(state);
  sfactor[0] = elem.coinstate.blend_sfactor;
  dfactor[0] = elem.coinstate.blend_dfactor;
  return elem.coinstate.blending != 0;
}


///////////////////////////////////////////////////////////////////////
//
// Description: static set() method for diffuse color
//
// use:  public, SoEXTENDER, static
//
///////////////////////////////////////////////////////////////////////  
public static void    
setDiffuse(SoState state, SoNode node, int numcolors, 
            SbColorArray colors, SoColorPacker packer)
{
	  if (state.isElementEnabled(SoGLVBOElement.getClassStackIndex(SoGLVBOElement.class))) {
		    SoGLVBOElement.setColorVBO(state, null);
		  }
		  SoLazyElement elem = SoLazyElement.getInstance(state);
		  if (numcolors != 0 && (elem.coinstate.diffusenodeid !=
		                    get_diffuse_node_id(node, numcolors, colors))) {
		    elem = getWInstance(state);
		    elem.setDiffuseElt(node, numcolors, colors, packer);
		    if (state.isCacheOpen()) elem.lazyDidSet(masks.DIFFUSE_MASK.getValue());
		  }
		  else if (state.isCacheOpen()) {
		    elem.lazyDidntSet(masks.DIFFUSE_MASK.getValue());
		  }	
}
//{
//    // if someone sets this directly, remove any color VBO
//    //SoGLVBOElement.unsetVBOIfEnabled(state, SoGLVBOElement.VBOType.COLOR_VBO);
//    if (state.isElementEnabled(SoGLVBOElement.getClassStackIndex(SoGLVBOElement.class))) { // COIN 3D
//        SoGLVBOElement.setColorVBO(state, null);
//      }
//
//    SoLazyElement curElt = SoLazyElement.getInstance(state);
//    //Because we are getting the transparency value from state, there
//    //is a get-dependence
//    if(state.isCacheOpen())curElt.registerGetDependence(state, masks.DIFFUSE_MASK.getValue());
//    if (curElt.coinstate.diffusenodeid !=  node.getNodeId() ||
//         (!cPacker.transpMatch(curElt.ivState.transpNodeId))){
//        getWInstance(state).setDiffuseElt(node,  numColors, colors, cPacker);
//    }
//    else if (state.isCacheOpen()){       
//        curElt.registerRedundantSet(state, masks.DIFFUSE_MASK.getValue());
//    }
//}
///////////////////////////////////////////////////////////////////////
//
// Description: static set() method for transparency 
//
// use:  public, SoEXTENDER, static
//
///////////////////////////////////////////////////////////////////////  
public static void    
setTransparency(SoState state, SoNode node, int numvalues, 
            FloatMemoryBuffer transparency, SoColorPacker packer)
{
	  if (state.isElementEnabled(SoGLVBOElement.getClassStackIndex(SoGLVBOElement.class))) {
	    SoGLVBOElement.setColorVBO(state, null);
	  }
	  SoLazyElement elem = SoLazyElement.getInstance(state);
	  if (numvalues != 0 && (elem.coinstate.transpnodeid !=
	                    get_transp_node_id(node, numvalues, new FloatArray(0,transparency)))) {
	    elem = getWInstance(state);
	    elem.setTranspElt(node, numvalues, transparency, packer);
	    if (state.isCacheOpen()) elem.lazyDidSet(masks.TRANSPARENCY_MASK.getValue());
	  }
	  else if (state.isCacheOpen()) {
	    elem.lazyDidntSet(masks.TRANSPARENCY_MASK.getValue());
	  }
	  SoShapeStyleElement.setTransparentMaterial(state, elem.coinstate.istransparent);
	}

//{
//    // if someone sets this directly, remove any color VBO
//    //SoGLVBOElement.unsetVBOIfEnabled(state, SoGLVBOElement.VBOType.COLOR_VBO);
//    if (state.isElementEnabled(SoGLVBOElement.getClassStackIndex(SoGLVBOElement.class))) { // COIN 3D
//        SoGLVBOElement.setColorVBO(state, null);
//      }
//
//    SoLazyElement curElt = SoLazyElement.getInstance(state);
//    //Because we are getting the diffuse value from state, there
//    //is a get-dependence
//    if(state.isCacheOpen())curElt.registerGetDependence(state, masks.DIFFUSE_MASK.getValue());
//    
//    int testNodeId;
//    if(numTransp == 1 && transp[0] == 0.0) testNodeId = 0;
//    else testNodeId = node.getNodeId();
//    
//    if ((curElt.ivState.transpNodeId != testNodeId) || 
//        (!cPacker.diffuseMatch(curElt.coinstate.diffusenodeid)))
//        getWInstance(state).setTranspElt(node, numTransp, transp, cPacker);
//    else if (state.isCacheOpen()) 
//        curElt.registerRedundantSet(state, masks.TRANSPARENCY_MASK.getValue()|masks.DIFFUSE_MASK.getValue()); 
//}           


///////////////////////////////////////////////////////////////////////
//
// Description: static set() method for packed colors 
//
// use:  public, SoEXTERNAL, static
//
///////////////////////////////////////////////////////////////////////  
public static void setPacked(SoState state, SoNode node,
        int numcolors, IntArray colors) {
	
	setPacked(state,node,numcolors,colors,false);
}


public static void
setPacked(SoState state, SoNode node,
                         int numcolors, IntArray colors,
                         boolean packedtransparency)
{
  if (state.isElementEnabled(SoGLVBOElement.getClassStackIndex(SoGLVBOElement.class))) {
    SoGLVBOElement.setColorVBO(state, null);
  }
  SoLazyElement elem = SoLazyElement.getInstance(state);
  if (numcolors != 0 && elem.coinstate.diffusenodeid != node.getNodeId()) {
    elem = getWInstance(state);
    elem.setPackedElt(node, numcolors, colors, packedtransparency);
    if (state.isCacheOpen()) elem.lazyDidSet(masks.TRANSPARENCY_MASK.getValue()|masks.DIFFUSE_MASK.getValue());
  }
  else if (state.isCacheOpen()) {
    elem.lazyDidntSet(masks.TRANSPARENCY_MASK.getValue()|masks.DIFFUSE_MASK.getValue());
  }
  SoShapeStyleElement.setTransparentMaterial(state, elem.coinstate.istransparent);
}


//public static void    
//setPacked(SoState state, SoNode node,
//            int numColors, final int[] colors)
//{
//    // if someone sets this directly, remove any color VBO
//    //SoGLVBOElement.unsetVBOIfEnabled(state, SoGLVBOElement.VBOType.COLOR_VBO);
//    if (state.isElementEnabled(SoGLVBOElement.getClassStackIndex(SoGLVBOElement.class))) { // COIN 3D
//        SoGLVBOElement.setColorVBO(state, null);
//      }
//   
//    SoLazyElement curElt = SoLazyElement.getInstance(state);
//    if (curElt.coinstate.diffusenodeid != (node.getNodeId()) ||     
//            (!(curElt.ivState.packed)) || 
//            (curElt.coinstate.packedarray.getValues() != colors)){
//        getWInstance(state).setPackedElt( node, numColors, colors);
//    } 
//    else if (state.isCacheOpen()) 
//        curElt.registerRedundantSet(state, masks.DIFFUSE_MASK.getValue()|masks.TRANSPARENCY_MASK.getValue());
//}


///////////////////////////////////////////////////////////////////////
//
// Description: static set() method for color indices
//
// use:  public, SoEXTENDER, static
//
///////////////////////////////////////////////////////////////////////  
public static void    
setColorIndices(SoState state, SoNode node, int numIndices, 
            int[] indices)
{
    // if someone sets this directly, remove any color VBO
    //SoGLVBOElement.unsetVBOIfEnabled(state, SoGLVBOElement.VBOType.COLOR_VBO);
//    if (state.isElementEnabled(SoGLVBOElement.getClassStackIndex(SoGLVBOElement.class))) { // COIN 3D
//        SoGLVBOElement.setColorVBO(state, null); 
//      }

    SoLazyElement curElt = SoLazyElement.getInstance(state);
    if (curElt.coinstate.diffusenodeid !=  node.getNodeId())
        getWInstance(state).setColorIndexElt(node, numIndices, indices);
    else if (state.isCacheOpen()) 
        curElt.registerRedundantSet(state, masks.DIFFUSE_MASK.getValue()); 
}           



///////////////////////////////////////////////////////////////////////
//
// Description: static set() method for ambient color 
//
// use:  public, SoEXTERNAL, static
//
///////////////////////////////////////////////////////////////////////  
public static void    
setAmbient(SoState state, SbColor color)
{    
    SoLazyElement elem = SoLazyElement.getInstance(state);
    if (color.operator_not_equal( elem.coinstate.ambient)){
		elem = getWInstance(state);
		elem.setAmbientElt(color);
		if (state.isCacheOpen()) elem.lazyDidSet(SoLazyElement.masks.AMBIENT_MASK.getValue());
    }  
    else if (state.isCacheOpen()){
		elem.registerRedundantSet(state, masks.AMBIENT_MASK.getValue());
		elem.lazyDidntSet(SoLazyElement.masks.AMBIENT_MASK.getValue());
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
    SoLazyElement elem = SoLazyElement.getInstance(state);
    if (color.operator_not_equal(elem.coinstate.emissive)) {
		elem = getWInstance(state);
		elem.setEmissiveElt(color);
		if (state.isCacheOpen()) elem.lazyDidSet(masks.EMISSIVE_MASK.getValue());
	}
    else if  (state.isCacheOpen()) {
		elem.registerRedundantSet(state, masks.EMISSIVE_MASK.getValue());
		elem.lazyDidntSet(masks.EMISSIVE_MASK.getValue());
	}
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
    SoLazyElement elem = SoLazyElement.getInstance(state);
    if (color.operator_not_equal( elem.coinstate.specular)) {
		elem = getWInstance(state);
		elem.setSpecularElt(color);
		if (state.isCacheOpen()) elem.lazyDidSet(masks.SPECULAR_MASK.getValue());
	}
    else if   (state.isCacheOpen()) {
		elem.registerRedundantSet(state, masks.SPECULAR_MASK.getValue());
		elem.lazyDidntSet(masks.SPECULAR_MASK.getValue());
	}
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
    SoLazyElement elem = SoLazyElement.getInstance(state);
    if (Math.abs(value - elem.coinstate.shininess)> SO_LAZY_SHINY_THRESHOLD) {
		elem = getWInstance(state);
		elem.setShininessElt(value);
		if (state.isCacheOpen()) elem.lazyDidSet(masks.SHININESS_MASK.getValue());
	}
    else if (state.isCacheOpen()) {
		elem.registerRedundantSet(state, masks.SHININESS_MASK.getValue());
		elem.lazyDidntSet(masks.SHININESS_MASK.getValue());
	}
}
///////////////////////////////////////////////////////////////////////
//
// Description: static set() method for blending 
//
// use:  public, SoEXTERNAL, static
//
///////////////////////////////////////////////////////////////////////  
//public static void    
//setBlending(SoState state,  boolean value)
//{
//  SoLazyElement curElt = SoLazyElement.getInstance(state);
//  if (!curElt.ivState.overrideBlending) {
//    if (value != curElt.ivState.blending)
//      getWInstance(state).setBlendingElt( value);
//    else if (state.isCacheOpen())
//      curElt.registerRedundantSet(state, masks.BLENDING_MASK.getValue());
//  }
//}
///////////////////////////////////////////////////////////////////////
//
// Description: static set() method for overriding blending 
//
// use:  public, SoEXTERNAL, static
//
///////////////////////////////////////////////////////////////////////  
//public void
//  setOverrideBlending(SoState state,  boolean value)
//{
//  SoLazyElement curElt = SoLazyElement.getInstance(state);
//  if (value != curElt.ivState.overrideBlending) {
//    getWInstance(state).ivState.overrideBlending = value;
//  }
//}

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
	  if (state.isElementEnabled(SoGLVBOElement.getClassStackIndex(SoGLVBOElement.class))) { // COIN 3D
		    SoGLVBOElement.setColorVBO(state, null);
		  }
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
    if ((bitmask & masks.EMISSIVE_MASK.getValue()) != 0 &&(emissive.operator_square_bracket(0).operator_not_equal(curElt.coinstate.emissive)))
        realSet |= masks.EMISSIVE_MASK.getValue();
    if ((bitmask & masks.SPECULAR_MASK.getValue()) != 0 &&(specular.operator_square_bracket(0).operator_not_equal(curElt.coinstate.specular)))
        realSet |= masks.SPECULAR_MASK.getValue(); 
    if ((bitmask & masks.AMBIENT_MASK.getValue()) != 0 &&(ambient.operator_square_bracket(0).operator_not_equal(curElt.coinstate.ambient)))
        realSet |= masks.AMBIENT_MASK.getValue();
    if ((bitmask & masks.SHININESS_MASK.getValue()) != 0 &&
            Math.abs(shininess.operator_square_bracket(0) - curElt.coinstate.shininess)>
            SO_LAZY_SHINY_THRESHOLD) realSet |= masks.SHININESS_MASK.getValue();
            
    long nodeId = node.getNodeId();
    if ((bitmask & masks.DIFFUSE_MASK.getValue()) != 0 && 
        nodeId != curElt.coinstate.diffusenodeid) realSet |= masks.DIFFUSE_MASK.getValue();

    //For transparency nodeid, opaque nodes are identified as nodeId = 0:       
    if(transp.getNum() == 1 && transp.operator_square_bracket(0) == 0.0) nodeId = 0;
    
    if (/*curElt.ivState.transpNodeId*/curElt.coinstate.transpnodeid != nodeId && (bitmask & masks.TRANSPARENCY_MASK.getValue()) != 0)  
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

public static void // COIN 3D
setMaterials(SoState state, SoNode node, int bitmask,
                            SoColorPacker packer,
                            SbColorArray diffuse,
                            int numdiffuse,
                            FloatArray transp,
                            int numtransp,
                            final SbColor ambient,
                            final SbColor emissive,
                            final SbColor specular,
                            float shininess,
                            boolean istransparent)
{
  if (state.isElementEnabled(SoGLVBOElement.getClassStackIndex(SoGLVBOElement.class))) {
    SoGLVBOElement.setColorVBO(state, null);
  }
  SoLazyElement elem = SoLazyElement.getInstance(state);

  int eltbitmask = 0;
  if ((bitmask & masks.DIFFUSE_MASK.getValue())!=0) {
    if (elem.coinstate.diffusenodeid !=
        get_diffuse_node_id(node, numdiffuse, diffuse)) {
      eltbitmask |= masks.DIFFUSE_MASK.getValue();
    }
  }
  if ((bitmask & masks.TRANSPARENCY_MASK.getValue())!=0) {
    if (elem.coinstate.transpnodeid != get_transp_node_id(node, numtransp, transp)) {
      eltbitmask |= masks.TRANSPARENCY_MASK.getValue();
    }
  }
  if ((bitmask & masks.AMBIENT_MASK.getValue())!=0) {
    if (elem.coinstate.ambient.operator_not_equal(ambient)) {
      eltbitmask |= masks.AMBIENT_MASK.getValue();
    }
  }
  if ((bitmask & masks.EMISSIVE_MASK.getValue())!=0) {
    if (elem.coinstate.emissive.operator_not_equal(emissive)) {
      eltbitmask |= masks.EMISSIVE_MASK.getValue();
    }
  }
  if ((bitmask & masks.SPECULAR_MASK.getValue())!=0) {
    if (elem.coinstate.specular.operator_not_equal(specular)) {
      eltbitmask |= masks.SPECULAR_MASK.getValue();
    }
  }
  if ((bitmask & masks.SHININESS_MASK.getValue())!=0) {
    if (SbBasic.SbAbs(elem.coinstate.shininess-shininess) > SO_LAZY_SHINY_THRESHOLD) {
      eltbitmask |= masks.SHININESS_MASK.getValue();
    }
  }

  SoLazyElement welem = null;

  if (eltbitmask != 0) {
    welem = getWInstance(state);
    welem.setMaterialElt(node, eltbitmask, packer, diffuse,
                          numdiffuse, transp, numtransp,
                          ambient, emissive, specular, shininess,
                          istransparent);
    if (state.isCacheOpen()) welem.lazyDidSet(eltbitmask);
  }

  if ((eltbitmask != bitmask) && state.isCacheOpen()) {
    if (welem != null) elem = welem;
    elem.lazyDidntSet((~eltbitmask) & bitmask);
  }
  if ((bitmask & masks.TRANSPARENCY_MASK.getValue())!=0) {
    SoShapeStyleElement.setTransparentMaterial(state, istransparent);
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
        SbColorArray colors, SoColorPacker packer)
{
    this.coinstate.diffusenodeid = get_diffuse_node_id(node, numColors, colors);
    this.coinstate.diffusearray = colors;
    this.coinstate.numdiffuse = numColors;
    this.coinstate.packeddiffuse = false;
    
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
setTranspElt(SoNode node, int numtransp, 
		FloatMemoryBuffer transp, SoColorPacker packer )
{
    this.coinstate.transpnodeid = get_transp_node_id(node, numtransp, new FloatArray(0,transp));
    this.coinstate.transparray = new FloatArray(0,transp);
    this.coinstate.numtransp = numtransp;
    this.coinstate.stipplenum = SbBasic.SbClamp((int)(transp.getFloat(0) * 64.0f), 0, 64);

    this.coinstate.istransparent = false;
    for (int i = 0; i < numtransp; i++) {
      if (transp.getFloat(i) > 0.0f) {
        this.coinstate.istransparent = true;
        break;
      }
    }

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
    this.coinstate.colorindexarray = new IntArrayPtr(indices);
    this.coinstate.numdiffuse = numIndices;
    this.coinstate.packeddiffuse = false;
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
    coinstate.transptype = type;
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
        final IntArray colors)
{
	setPackedElt(node,numColors,colors, false);
}
protected void
setPackedElt( SoNode node,  int numColors,  
        final IntArray colors, boolean packedtransparency)
{
    this.coinstate.diffusenodeid = node.getNodeId();
    this.coinstate.transpnodeid = node.getNodeId();
    this.coinstate.numdiffuse = numColors;
    this.coinstate.packedarray = new IntArrayPtr(colors);
    this.coinstate.packeddiffuse = true;
    this.coinstate.istransparent = packedtransparency;

    int alpha = colors.get(0) & 0xff;
    float transp = (float)(255-alpha)/255.0f;
    this.coinstate.stipplenum = SbBasic.SbClamp((int)(transp * 64.0f), 0, 64);
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
    this.coinstate.ambient.copyFrom(color);
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
    coinstate.emissive.copyFrom(color);
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
    this.coinstate.specular.copyFrom(color);
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
    this.coinstate.shininess = value;
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
    	coinstate.diffusenodeid = node.getNodeId();
        coinstate.diffusearray = diffuse.getValuesSbColorArray();
        coinstate.numdiffuse = diffuse.getNum();
        coinstate.packeddiffuse=false;
        //ivState.packedTransparent = false;
    }
    if ((mask & masks.TRANSPARENCY_MASK.getValue()) != 0){
        coinstate.numtransp = transp.getNum();
        coinstate.transparray = transp.getValues(0);
        coinstate.stipplenum = 0;
        if ((coinstate.transparray.get(0)> 0.0) &&
                (coinstate.transptype == SoGLRenderAction.TransparencyType.SCREEN_DOOR.getValue())) {
            coinstate.stipplenum =
                (int)(coinstate.transparray.get(0)*getNumPatterns());
        }
        coinstate.packeddiffuse=false;
        //ivState.packedTransparent = false;
    }
    if ((mask & masks.AMBIENT_MASK.getValue())!=0)
        coinstate.ambient.copyFrom(ambient.operator_square_bracket(0));
    
    if ((mask & masks.EMISSIVE_MASK.getValue())!=0)
        coinstate.emissive.copyFrom(emissive.operator_square_bracket(0));
        
    if ((mask & masks.SPECULAR_MASK.getValue())!=0)
        coinstate.specular.copyFrom(specular.operator_square_bracket(0));
    
    if ((mask & masks.SHININESS_MASK.getValue())!=0)
        coinstate.shininess = shininess.operator_square_bracket(0);
}

public void // COIN 3D
setMaterialElt(SoNode node, int bitmask,
                              SoColorPacker packer,
                              SbColorArray diffuse, int numdiffuse,
                              FloatArray transp, int numtransp,
                              final SbColor ambient,
                              final SbColor emissive,
                              final SbColor specular,
                              float shininess,
                              boolean istransparent)
{
  if ((bitmask & masks.DIFFUSE_MASK.getValue())!=0) {
    this.coinstate.diffusenodeid = get_diffuse_node_id(node, numdiffuse, diffuse);
    this.coinstate.diffusearray = SbColorArray.copyOf(diffuse);
    this.coinstate.numdiffuse = numdiffuse;
    this.coinstate.packeddiffuse = false;
  }
  if ((bitmask & masks.TRANSPARENCY_MASK.getValue())!=0) {
    this.coinstate.transpnodeid = get_transp_node_id(node, numtransp, transp);
    this.coinstate.transparray = transp;
    this.coinstate.numtransp = numtransp;
    this.coinstate.stipplenum = SbBasic.SbClamp((int)(transp.get(0) * 64.0f), 0, 64);
    // check for common case
    if (numtransp == 1 && transp.get(0) == 0.0f) {
      this.coinstate.transpnodeid = 0;
      this.coinstate.istransparent = false;
    }
    else {
      this.coinstate.istransparent = istransparent;
    }
  }
  if ((bitmask & masks.AMBIENT_MASK.getValue())!=0) {
    this.coinstate.ambient.copyFrom(ambient);
  }
  if ((bitmask & masks.EMISSIVE_MASK.getValue())!=0) {
    this.coinstate.emissive.copyFrom(emissive);
  }
  if ((bitmask & masks.SPECULAR_MASK.getValue())!=0) {
    this.coinstate.specular.copyFrom(specular);
  }
  if ((bitmask & masks.SHININESS_MASK.getValue())!=0) {
    this.coinstate.shininess = shininess;
  }
}

public void
setAlphaTestElt(int func, float value)
{
  this.coinstate.alphatestfunc = func;
  this.coinstate.alphatestvalue = value;
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
	  SoLazyElement elem = SoLazyElement.getInstance(state);
	  if (elem.coinstate.transptype != type) {
	    getWInstance(state).setTranspTypeElt(type);
	  }
}


//public void setDrawElementsCallback( SoState state, SoDrawElementsCallback cb, Object userData )
//{
//  SoLazyElement curElt = SoLazyElement.getInstance(state);
//  curElt.ivState.drawElementsCallback = cb;
//  curElt.ivState.drawElementsCallbackUserData = userData;
//}

//public void setUseVertexAttributes( SoState state, boolean flag )
//{
//  SoLazyElement curElt = SoLazyElement.getInstance(state);
//  curElt.ivState.useVertexAttributes = flag;
//}

public static boolean shouldUseVertexAttributes( SoState state )
{
  SoLazyElement curElt = SoLazyElement.getInstance(state);
  return false;//curElt.ivState.useVertexAttributes;
}

//public void setDrawArraysCallback( SoState state, SoDrawArraysCallback cb, Object userData )
//{
//  SoLazyElement curElt = SoLazyElement.getInstance(state);
//  curElt.ivState.drawArraysCallback = cb;
//  curElt.ivState.drawArraysCallbackUserData = userData;
//}


public static void drawArrays( SoState state, /*GLenum*/int mode, /*GLint*/int first, /*GLsizei*/int count )
{
  SoLazyElement elt = getInstance(state);
//  if (elt.ivState.drawArraysCallback != null) {
//    elt.ivState.drawArraysCallback.call(elt.ivState.drawArraysCallbackUserData, state, mode, first, count);
//  } else
  	{
	  GL2 gl2 = state.getGL2();
    gl2.glDrawArrays(mode, first, count);
  }
}


public static void drawElements( SoState state, /*GLenum*/int mode, /*GLsizei*/int count, /*GLenum*/int type, /*GLvoid*/Object indices )
{
  SoLazyElement elt = getInstance(state);
//  if (elt.ivState.drawElementsCallback != null) {
//    elt.ivState.drawElementsCallback.call(elt.ivState.drawElementsCallbackUserData, state, mode, count, type, indices);
//  } else
  	{
	  GL2 gl2 = state.getGL2();
	  if(indices == null) { // java port
		  glDrawElements(mode, count, type, 0);
	  }
	  else {
		  if ( indices instanceof ByteBuffer ) {
			  glDrawElements(mode, /*count,*/ type, (ByteBuffer)indices);
		  }
		  else if ( indices instanceof ByteBufferAble ) {
			  glDrawElements(mode, /*count,*/ type, ((ByteBufferAble)indices).toByteBuffer());
		  }
		  else {
			  throw new IllegalArgumentException();
		  }
	  }
  }
}

//public static void
//setGLImageId(SoState state, int glimageid, boolean alphatest) // COIN 3D
//{
//  SoLazyElement elem = SoLazyElement.getInstance(state);
//  if (elem.coinstate.glimageid != glimageid) {
//    elem = getWInstance(state);
//    elem.setGLImageIdElt(glimageid, alphatest);
//    if (state.isCacheOpen()) elem.lazyDidSet(SoLazyElement.masks.GLIMAGE_MASK.getValue());
//  }
//  else if (state.isCacheOpen()) {
//    elem.lazyDidntSet(SoLazyElement.masks.GLIMAGE_MASK.getValue());
//  }
//  SoLazyElement.setAlphaTest(state, !elem.coinstate.istransparent && alphatest);
//}


public static void
setAlphaTest(SoState state, int func, float value)
{
  SoLazyElement elem = SoLazyElement.getInstance(state);
  if (elem.coinstate.alphatestfunc != func ||
      elem.coinstate.alphatestvalue != value) {
    elem = getWInstance(state);
    elem.setAlphaTestElt(func, value);
    if (state.isCacheOpen()) elem.lazyDidSet(masks.ALPHATEST_MASK.getValue());
  }
  else if (state.isCacheOpen()) {
    elem.lazyDidntSet(masks.ALPHATEST_MASK.getValue());
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
	super.push(state);
    SoLazyElement prevElt = (SoLazyElement)getNextInStack();
    this.coinstate.copyFrom( prevElt.coinstate);
}

public static boolean 
getTwoSidedLighting(SoState state)
{
  SoLazyElement elem = getInstance(state);
  return elem.coinstate.twoside != 0;
}

// helper functions to handle default diffuse/transp values
public static long
get_diffuse_node_id(SoNode node, int numdiffuse,
                    SbColorArray color)
{
  if (numdiffuse == 1 && color.get(0).operator_equal_equal(new SbColor(0.8f, 0.8f, 0.8f))) return 0;
  return node.getNodeId();
}

public static long
get_transp_node_id(SoNode node, int numtransp,
                   FloatArray transp)
{
  if (numtransp == 1 && transp.get(0) == 0.0f) return 0;
  return node.getNodeId();
}

/*!
This static method initializes static data for the
SoDiffuseColorElement class.
*/

public static void
initClass(final Class<? extends SoElement> javaClass)
{
//SO_ELEMENT_INIT_CLASS(SoLazyElement, inherited);
	SoElement.initClass(javaClass);

if (lazy_defaultdiffuse == null) {
  lazy_defaultdiffuse = new SbColorArray(FloatMemoryBuffer.allocateFloats(3));
  lazy_defaulttransp = new FloatArray(0,FloatMemoryBuffer.allocateFloats(1));
  lazy_defaultindex = new IntArrayPtr(new int[1]);
  lazy_defaultpacked = new IntArrayPtr(new int[1]);
  lazy_unpacked = new SbColorArray(FloatMemoryBuffer.allocateFloats(3));

  lazy_defaultdiffuse.get(0).copyFrom( getDefaultDiffuse());
  lazy_defaulttransp.set(0, getDefaultTransparency());
  lazy_defaultindex.set(0, getDefaultColorIndex());
  lazy_defaultpacked.set(0, getDefaultPacked());

  //coin_atexit(lazyelement_cleanup, CC_ATEXIT_NORMAL);
}
}


public static void
enableBlending(SoState state,  int sfactor, int dfactor)
{
  SoLazyElement.enableSeparateBlending(state, sfactor, dfactor, 0, 0);
}

public static void
enableSeparateBlending(SoState state,
                                      int sfactor, int dfactor,
                                      int alpha_sfactor, int alpha_dfactor)
{
  SoLazyElement elem = SoLazyElement.getInstance(state);
  if (elem.coinstate.blending == 0 ||
      elem.coinstate.blend_sfactor != sfactor ||
      elem.coinstate.blend_dfactor != dfactor ||
      elem.coinstate.alpha_blend_sfactor != alpha_sfactor ||
      elem.coinstate.alpha_blend_dfactor != alpha_dfactor) {
    elem = getWInstance(state);
    elem.enableBlendingElt(sfactor, dfactor, alpha_sfactor, alpha_dfactor);
    if (state.isCacheOpen()) elem.lazyDidSet(masks.BLENDING_MASK.getValue());
  }
  else if (state.isCacheOpen()) {
    elem.lazyDidntSet(masks.BLENDING_MASK.getValue());
  }
}

// ! FIXME: write doc

public static void
disableBlending(SoState state)
{
  SoLazyElement elem = SoLazyElement.getInstance(state);
  if (elem.coinstate.blending != 0) {
    elem = getWInstance(state);
    elem.disableBlendingElt();
    if (state.isCacheOpen()) elem.lazyDidSet(masks.BLENDING_MASK.getValue());
  }
  else if (state.isCacheOpen()) {
    elem.lazyDidntSet(masks.BLENDING_MASK.getValue());
  }
}


public void
enableBlendingElt(int sfactor, int dfactor, int alpha_sfactor, int alpha_dfactor)
{
  this.coinstate.blending = true ? 1 : 0;
  this.coinstate.blend_sfactor = sfactor;
  this.coinstate.blend_dfactor = dfactor;
  this.coinstate.alpha_blend_sfactor = alpha_sfactor;
  this.coinstate.alpha_blend_dfactor = alpha_dfactor;
}

public void
disableBlendingElt()
{
  this.coinstate.blending = false ? 1 : 0;
}


public static void
setBackfaceCulling(SoState state, boolean onoff)
{
  SoLazyElement elem = SoLazyElement.getInstance(state);
  if (elem.coinstate.culling != (onoff ? 1 : 0)) {
    elem = getWInstance(state);
    elem.setBackfaceCullingElt(onoff);
    if (state.isCacheOpen()) elem.lazyDidSet(masks.CULLING_MASK.getValue());
  }
  else if (state.isCacheOpen()) {
    elem.lazyDidntSet(masks.CULLING_MASK.getValue());
  }
}


public void
setBackfaceCullingElt(boolean onoff)
{
  this.coinstate.culling = onoff ? 1 : 0;
}

public static void
setVertexOrdering(SoState state, VertexOrdering ordering)
{
  SoLazyElement elem = SoLazyElement.getInstance(state);
  if (elem.coinstate.vertexordering != ordering.getValue()) {
    elem = getWInstance(state);
    elem.setVertexOrderingElt(ordering);
    if (state.isCacheOpen()) elem.lazyDidSet(masks.VERTEXORDERING_MASK.getValue());
  }
  else if (state.isCacheOpen()) {
    elem.lazyDidntSet(masks.VERTEXORDERING_MASK.getValue());
  }
}

public static void
setTwosideLighting(SoState state, boolean onoff)
{
  SoLazyElement elem = SoLazyElement.getInstance(state);
  if (elem.coinstate.twoside != (onoff ? 1 : 0)) {
    elem = getWInstance(state);
    elem.setTwosideLightingElt(onoff);
    if (state.isCacheOpen()) elem.lazyDidSet(masks.TWOSIDE_MASK.getValue());
  }
  else if (state.isCacheOpen()) {
    elem.lazyDidntSet(masks.TWOSIDE_MASK.getValue());
  }
  }

public void
setVertexOrderingElt(VertexOrdering ordering)
{
  this.coinstate.vertexordering = ordering.getValue();
}

public void
setTwosideLightingElt(boolean onoff)
{
  this.coinstate.twoside = (onoff ? 1 : 0);
}



}
