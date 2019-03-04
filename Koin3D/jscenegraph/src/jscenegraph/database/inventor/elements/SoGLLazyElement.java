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
 |   $Revision: 1.2 $
 |
 |   Description:
 |      This file defines the SoGLLazyElement class.
 |
 |   Author(s)          : Alan Norton,  Gavin Bell 
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

package jscenegraph.database.inventor.elements;

import java.nio.IntBuffer;

import com.jogamp.opengl.GL2;

import jscenegraph.coin3d.shaders.SoGLShaderProgram;
import jscenegraph.coin3d.shaders.inventor.elements.SoGLShaderProgramElement;
import jscenegraph.database.inventor.SbColor;
import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SoDebug;
import jscenegraph.database.inventor.SoMachine;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.caches.SoCache;
import jscenegraph.database.inventor.caches.SoGLRenderCache;
import jscenegraph.database.inventor.caches.SoNormalCache;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoMFColor;
import jscenegraph.database.inventor.fields.SoMFFloat;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoPackedColor;
import jscenegraph.mevis.inventor.misc.SoVBO;
import jscenegraph.port.Ctx;
import jscenegraph.port.SbColorArray;
import jscenegraph.port.Util;
import jscenegraph.port.VoidPtr;


///////////////////////////////////////////////////////////////////////////////
///
///  \class SoGLLazyElement
///  \ingroup Elements
///
///  Element that manages the GL state for the SoLazyElement. 
///
///  Note that this class relies on SoLazyElement to store the
///  inventor color(s), etc. in the instance.
///  This element keeps GL shadow copies of colors
///
//////////////////////////////////////////////////////////////////////////////

/**
 * @author Yves Boyadjian
 *
 */
public class SoGLLazyElement extends SoLazyElement {

	private static final int FLAG_FORCE_DIFFUSE      =0x0001;
	
    private GL2 gl2;
	
	   //!provide a public typedef for GLLazyState, so that GLRenderCache can use it:
	   public static class GLLazyState {
	        int        GLDiffuseNodeId;
	        int        GLTranspNodeId;
	        final float[]           GLAmbient = new float[4];
	        final float[]           GLEmissive = new float[4];
	        final float[]           GLSpecular = new float[4];
	        float           GLShininess;
	        int          GLColorMaterial; // int, not boolean        
	        int         GLLightModel;
	        int          GLBlending; // int, not boolean
	        int         GLStippleNum;
	        
	        /*VertexOrdering*/int vertexordering; //COIN 3D
	        int twoside; // COIN 3d
	        int culling; // COIN 3D
	        
	        public void copyFrom(GLLazyState other) {
		                GLDiffuseNodeId = other.GLDiffuseNodeId;
		                GLTranspNodeId = other.GLTranspNodeId;
		        GLAmbient[0] = other.GLAmbient[0];
		        GLAmbient[1] = other.GLAmbient[1];
		        GLAmbient[2] = other.GLAmbient[2];
		        GLAmbient[3] = other.GLAmbient[3];
		        GLEmissive[0] = other.GLEmissive[0];
		        GLEmissive[1] = other.GLEmissive[1];
		        GLEmissive[2] = other.GLEmissive[2];
		        GLEmissive[3] = other.GLEmissive[3];
		        GLSpecular[0] = other.GLSpecular[0];
		        GLSpecular[1] = other.GLSpecular[1];
		        GLSpecular[2] = other.GLSpecular[2];
		        GLSpecular[3] = other.GLSpecular[3];
		        GLShininess = other.GLShininess;
		        GLColorMaterial = other.GLColorMaterial;        
		        GLLightModel = other.GLLightModel;
		        GLBlending = other.GLBlending;
		        GLStippleNum = other.GLStippleNum;	    
		        
		        vertexordering = other.vertexordering; //COIN 3D
		        twoside = other.twoside; // COIN 3D
		        culling = other.culling; // COIN 3D
	        }
	    }; 
	    
    //! Copy of what has been sent to GL:
    //!struct SoGLLazyElement.GLLazyState glState;
	    private final SoGLLazyElement.GLLazyState glState = new SoGLLazyElement.GLLazyState();
  

    //! BitMap indicating what GL sends have been made:
    private int GLSendBits;
    
    private int didsetbitmask;
    private int didntsetbitmask;
    private int cachebitmask; // COIN 3D
    private int opencacheflags;

    //! Indicator of whether in colorIndex mode or not:
      private boolean colorIndex;  
      

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Initializes element.
//
// Use: public
////////////////////////////////////////////////////////////////////////
public void init(SoState state)
{
    super.init(state);
    
    gl2 = state.getGL2(); //java port
    
    // We begin with GL invalid
    // so it gets reset at first send.

    glState.GLAmbient[0]        = -1;
    glState.GLEmissive[0]       = -1;
    glState.GLSpecular[0]       = -1;

    glState.GLAmbient[1]    =   glState.GLAmbient[2]    = 0;
    glState.GLEmissive[1]   =   glState.GLEmissive[2]   = 0;
    glState.GLSpecular[1]   =   glState.GLSpecular[2]   = 0;
    
    glState.GLShininess         = -1;
    glState.GLColorMaterial     = -1;
    glState.GLBlending          = -1;

    glState.GLDiffuseNodeId     = 0;
    glState.GLTranspNodeId      = 0;
    //start with stipple undefined transparency
    glState.GLStippleNum        = -1;

    // set the last fields to 1., they will stay that way:
    glState.GLAmbient[3]        = 1.0f;
    glState.GLEmissive[3]       = 1.0f;
    glState.GLSpecular[3]       = 1.0f;
    
    // and lightModel invalid
    glState.GLLightModel        = -1;

    // Also, begin with GL and Inventor out of synch:
    invalidBits                 = masks.ALL_MASK.getValue();
    // and nothing sent to GL
    GLSendBits                  = 0;   
     
    // Determine if GL is in color index mode
    byte[]  b = new byte[1];
    gl2.glGetBooleanv(GL2.GL_RGBA_MODE, b, 0);
    colorIndex = ( b[0] == 0);
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Pushes element, copies GL state as needed:
//
// use: SoINTERNAL public
////////////////////////////////////////////////////////////////////////
public void
push(SoState state)
{
    gl2 = state.getGL2(); //java port    

    SoGLLazyElement prevElt = (SoGLLazyElement)getNextInStack();
     
    // The push always happens before a  true set()
    
    ivState.copyFrom(prevElt.ivState);        
    glState.copyFrom(prevElt.glState);
    
    colorIndex              = prevElt.colorIndex;      
    invalidBits             = prevElt.invalidBits;
    GLSendBits              = 0;
}


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Pops element, causing side effects in GL.
//
//
// Use: SoINTERNAL public
////////////////////////////////////////////////////////////////////////
public void
pop(SoState state, SoElement prevTopElement)
{
    // 
    //Copy all GL parts back from previous top element.
    //Mark those that changed with invalidBits.
    
    SoGLLazyElement prevTop = (SoGLLazyElement)prevTopElement;
    int sendBits = prevTop.GLSendBits;
    
    //merge GLSendBits into parent element
    GLSendBits |= sendBits;
    
    //mark corresponding parts as invalid:
    invalidBits |= sendBits;
    
    //copy GL state:
    glState.copyFrom(prevTop.glState);
    //this.colorindex = prevTop.colorindex; TODO
    this.didsetbitmask = prevTop.didsetbitmask;
    this.didntsetbitmask = prevTop.didntsetbitmask;
    this.cachebitmask = prevTop.cachebitmask;
    this.opencacheflags = prevTop.opencacheflags;
}


/////////////////////////////////////////////////////////////////////////
//
// Description:  set Diffuse color in element
// virtual method for GL rendering
//
// Use: private, virtual
//
////////////////////////////////////////////////////////////////////////
public void
setDiffuseElt(SoNode node,  
    int numColors,   SbColorArray colors, SoColorPacker cPacker)
{
    if (colorIndex) return;
    ivState.diffuseColors = colors;
    ivState.numDiffuseColors = numColors;        
    ivState.diffuseNodeId = (node.getNodeId()); 
    ivState.packed = false;
    ivState.packedTransparent = false;

    //Pack the colors and transparencies, if necessary:
    if(!cPacker.diffuseMatch(ivState.diffuseNodeId) ||
            (!cPacker.transpMatch(ivState.transpNodeId)))
        packColors(cPacker);
            
    ivState.packedColors = cPacker.getPackedColors();
                           
    // for open caches,  record the fact that set was called:
    ivState.cacheLevelSetBits |= masks.DIFFUSE_MASK.getValue();

    if (ivState.diffuseNodeId != glState.GLDiffuseNodeId)
        invalidBits |= masks.DIFFUSE_MASK.getValue();

  }
  
/////////////////////////////////////////////////////////////////////////
//
// Description:  set transparency type
// virtual method for GL rendering
//
// Use: private, virtual
//
////////////////////////////////////////////////////////////////////////
protected void
setTranspTypeElt(int type)
{
    if(ivState.transpType != type){
        ivState.transpType = type;
        // make sure transparencies send:
        invalidBits |= masks.TRANSPARENCY_MASK.getValue();
        glState.GLStippleNum = -1;
    }
}  
  
/////////////////////////////////////////////////////////////////////////
//
// Description:  set transparency in element
// virtual method for GL rendering
//
// Use: private, virtual
//
////////////////////////////////////////////////////////////////////////
public void
setTranspElt(SoNode node,  
    int numTrans,   float[] trans, SoColorPacker cPacker)
{
    ivState.numTransparencies = numTrans;
    ivState.transparencies = trans;

    // determine the appropriate stippleNum:
    // set to zero unless have nonzero stipple transparency.

    ivState.stippleNum = 0;
    if (trans[0] > 0.0) {
        if (ivState.transpType == SoGLRenderAction.TransparencyType.SCREEN_DOOR.getValue()){
            ivState.stippleNum =
                (int)(trans[0]*getNumPatterns());
        }       
    }
    if (numTrans == 1 && trans[0] == 0) ivState.transpNodeId = 0;
    else ivState.transpNodeId = node.getNodeId();
    ivState.packed = false;
    ivState.packedTransparent = false;
    if(!cPacker.diffuseMatch(ivState.diffuseNodeId) ||
            (!cPacker.transpMatch(ivState.transpNodeId)))
        packColors(cPacker);
            
    ivState.packedColors = cPacker.getPackedColors();
                           
    // for open caches, record the fact that set was called:
    ivState.cacheLevelSetBits |= (masks.TRANSPARENCY_MASK.getValue()|masks.DIFFUSE_MASK.getValue());

    if (ivState.transpNodeId != glState.GLTranspNodeId) 
        invalidBits |= masks.DIFFUSE_MASK.getValue();
        
    if (ivState.stippleNum != glState.GLStippleNum)
        invalidBits |= masks.TRANSPARENCY_MASK.getValue();
    else invalidBits &= ~masks.TRANSPARENCY_MASK.getValue();

  }
/////////////////////////////////////////////////////////////////////////
//
// Description:  set color indices in element
// virtual method for GL rendering
//
// Use: private, virtual
//
////////////////////////////////////////////////////////////////////////
public void
setColorIndexElt(SoNode node, 
         int numIndices,   int[] indices)
{
    if (!colorIndex) return;
    ivState.numDiffuseColors = numIndices;
    ivState.colorIndices = indices;
      
    ivState.diffuseNodeId = (node.getNodeId()); 
    ivState.packed = false;
    ivState.packedTransparent = false;

    // for open caches, record the fact that set was called:
    ivState.cacheLevelSetBits |= masks.DIFFUSE_MASK.getValue();

    if (ivState.diffuseNodeId != glState.GLDiffuseNodeId)
        invalidBits |= masks.DIFFUSE_MASK.getValue();
    else invalidBits &= ~masks.DIFFUSE_MASK.getValue();

}

/////////////////////////////////////////////////////////////////////////
//
// Description:  set Packed diffuse color in element
//               sets both diffuse color and transparency.
// virtual method for GL rendering
//
// Use: private, virtual
//
////////////////////////////////////////////////////////////////////////
protected void
setPackedElt(SoNode node, int numColors, 
      int[] colors)
{
    if (colorIndex) return;
    ivState.packedColors        = colors;
    ivState.numDiffuseColors    = numColors;
    ivState.numTransparencies   = numColors;
    ivState.diffuseNodeId       = node.getNodeId();
    ivState.transpNodeId        = node.getNodeId();
    
    ivState.stippleNum = 0;     
    if ((colors[0]&0xff) != 0xff){      
        if(ivState.transpType == SoGLRenderAction.TransparencyType.SCREEN_DOOR.getValue()){
            ivState.stippleNum = (int)(getNumPatterns()*
                (1.-(colors[0] & 0xff)*(1./255.)));
        }           
    }
    
    ivState.packed              = true;
    ivState.packedTransparent   = (node instanceof SoPackedColor) ? ((SoPackedColor) node).isTransparent() : false; //TODO COIN3D
     
    // For open caches, record the fact that set was called:
    ivState.cacheLevelSetBits |= (masks.DIFFUSE_MASK.getValue()|masks.TRANSPARENCY_MASK.getValue());      
 
    if ((invalidBits & (masks.DIFFUSE_MASK.getValue()|masks.TRANSPARENCY_MASK.getValue()))!=0) return;

    if (ivState.diffuseNodeId != glState.GLDiffuseNodeId ||
        ivState.transpNodeId != glState.GLTranspNodeId)
        invalidBits |= masks.DIFFUSE_MASK.getValue();
    else invalidBits &= ~masks.DIFFUSE_MASK.getValue();
    
    if (ivState.stippleNum != glState.GLStippleNum)
        invalidBits |= masks.TRANSPARENCY_MASK.getValue();
    else invalidBits &= ~masks.TRANSPARENCY_MASK.getValue();

}

//////////////////////////////////////////////////////////////////////////
//
// Description:  set Ambient color in element
// virtual method for GL rendering
//
// Use: private, virtual
//
/////////////////////////////////////////////////////////////////////////
public void
setAmbientElt(  SbColor color)
{
    ivState.ambientColor.setValue((float[])color.getValueRead());

    // For open caches, record the fact that set was called:
    ivState.cacheLevelSetBits |= masks.AMBIENT_MASK.getValue();  


    for(int i=0; i<3; i++){
        if (ivState.ambientColor.getValueRead()[i] != glState.GLAmbient[i]){
            invalidBits |= masks.AMBIENT_MASK.getValue();
            return;
        }
    }
    invalidBits &= ~masks.AMBIENT_MASK.getValue();
}

/////////////////////////////////////////////////////////////////////////
//
// Description:  set emissive color in element
// virtual method for GL rendering
//
// Use: private, virtual
//
/////////////////////////////////////////////////////////////////////////
public void
setEmissiveElt(  SbColor color)
{
    ivState.emissiveColor.setValue((float[])color.getValueRead());

    // For open caches, record the fact that set was called:
    ivState.cacheLevelSetBits |= masks.EMISSIVE_MASK.getValue(); 
 
    for(int i=0; i<3; i++){
        if (ivState.emissiveColor.getValueRead()[i] != glState.GLEmissive[i]){
            invalidBits |= masks.EMISSIVE_MASK.getValue();
            return;
        }
    }
    invalidBits &= ~masks.EMISSIVE_MASK.getValue();
}
/////////////////////////////////////////////////////////////////////////
//
// Description:  set specular color in element
// virtual method for GL rendering
//
// Use: private, virtual
//
/////////////////////////////////////////////////////////////////////////
public void
setSpecularElt(  SbColor color)
{
    ivState.specularColor.setValue((float[])color.getValueRead());

    // For open caches, record the fact that set was called:
    ivState.cacheLevelSetBits |= masks.SPECULAR_MASK.getValue(); 

    for(int i=0; i<3; i++){
        if (ivState.specularColor.getValueRead()[i] != glState.GLSpecular[i]){
            invalidBits |= masks.SPECULAR_MASK.getValue();
            return;
        }
    }
    invalidBits &= ~masks.SPECULAR_MASK.getValue();
}

/////////////////////////////////////////////////////////////////////////
//
// Description:  set shininess in element
// virtual method for GL rendering
//
// Use: private, virtual
//
/////////////////////////////////////////////////////////////////////////
public void
setShininessElt(float value)
{
    ivState.shininess = value;

    // For open caches, record the fact that set was called:
    ivState.cacheLevelSetBits |= masks.SHININESS_MASK.getValue();        

    // set invalid bit based on value
    if (Math.abs(ivState.shininess - glState.GLShininess) > SO_LAZY_SHINY_THRESHOLD)
            invalidBits |= masks.SHININESS_MASK.getValue();
        else invalidBits &= ~masks.SHININESS_MASK.getValue();
    return;
}
/////////////////////////////////////////////////////////////////////////
//
// Description:  set colorMaterial in element
// virtual method for GL rendering
//
// Use: private, virtual
//
/////////////////////////////////////////////////////////////////////////
public void
setColorMaterialElt(boolean value)
{
    // don't turn on colorMaterial if lighting off:
    if (ivState.lightModel == LightModel.BASE_COLOR.getValue()) value = false;
    ivState.colorMaterial = value;

    // For open caches, record the fact that set was called:
        ivState.cacheLevelSetBits |= masks.COLOR_MATERIAL_MASK.getValue();       
 

    // set invalid bit based on value
    if ((ivState.colorMaterial?1:0) != glState.GLColorMaterial)
            invalidBits |= masks.COLOR_MATERIAL_MASK.getValue();
        else invalidBits &= ~masks.COLOR_MATERIAL_MASK.getValue();
    return;
}
/////////////////////////////////////////////////////////////////////////
//
// Description:  set lightModel in element
// virtual method for GL rendering
//
// Use: private, virtual
//
/////////////////////////////////////////////////////////////////////////
public void
setLightModelElt(SoState state, int value)
{
    ivState.lightModel = value;

    // For Open caches, record the fact that set was called:
        ivState.cacheLevelSetBits |= masks.LIGHT_MODEL_MASK.getValue();  

    // also set the shapestyle version of this:
    SoShapeStyleElement.setLightModel(state, value);

    // set invalid bit based on value
    if (ivState.lightModel != glState.GLLightModel)
            invalidBits |= masks.LIGHT_MODEL_MASK.getValue();
        else invalidBits &= ~masks.LIGHT_MODEL_MASK.getValue();
    // set Color Material off if necessary:
    if (ivState.lightModel == LightModel.BASE_COLOR.getValue())
        setColorMaterialElt(false);
    return;
}
/////////////////////////////////////////////////////////////////////////
//
// Description:  set blending in element
// virtual method for GL rendering
//
// Use: private, virtual
//
/////////////////////////////////////////////////////////////////////////
public void
setBlendingElt(boolean value)
{

    ivState.blending = value;

    // For open caches, record the fact that set was called:
    ivState.cacheLevelSetBits |= masks.BLENDING_MASK.getValue(); 
 
    // set invalid bit based on value
    if ((ivState.blending?1:0) != glState.GLBlending)
        invalidBits |= masks.BLENDING_MASK.getValue();
    else invalidBits &= ~masks.BLENDING_MASK.getValue();

    return;
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    set all materials in the element 
//    special method for SoMaterial nodes 
//
// Use: private
////////////////////////////////////////////////////////////////////////

public void
setMaterialElt(SoNode node, int mask,
    SoColorPacker cPacker,  
      final SoMFColor diffuse, final  SoMFFloat transp, 
      final SoMFColor ambient, final  SoMFColor emissive, 
      final SoMFColor specular, final   SoMFFloat shininess)
{
    if ((mask & masks.DIFFUSE_MASK.getValue())!=0 && !colorIndex ){
        ivState.diffuseColors = diffuse.getValuesSbColorArray(/*0*/);
        ivState.numDiffuseColors = diffuse.getNum();        
        ivState.diffuseNodeId = (node.getNodeId()); 
        ivState.packed = false;
        ivState.packedTransparent = false;      
    }
    
    if ((mask & masks.TRANSPARENCY_MASK.getValue())!=0){
        ivState.numTransparencies = transp.getNum();
        ivState.transparencies = transp.getValuesFloat(0);    
        ivState.stippleNum = 0;
        if (ivState.numTransparencies == 1 && ivState.transparencies[0] == 0.0)
            ivState.transpNodeId = 0;
        else { 
            ivState.transpNodeId = node.getNodeId();
            if (ivState.transparencies[0] != 0.0 &&        
                    ivState.transpType == SoGLRenderAction.TransparencyType.SCREEN_DOOR.getValue()){
                ivState.stippleNum =
                    (int)(ivState.transparencies[0]*getNumPatterns());
            }   
        }
            
        if  (ivState.stippleNum != glState.GLStippleNum)
            invalidBits |= masks.TRANSPARENCY_MASK.getValue();
        else invalidBits &= ~masks.TRANSPARENCY_MASK.getValue();
    }
    
    // do combined packed color/transparency work:
    if ((mask & (masks.DIFFUSE_MASK.getValue()|masks.TRANSPARENCY_MASK.getValue()))!=0){
        if(!cPacker.diffuseMatch(ivState.diffuseNodeId) ||
                (!cPacker.transpMatch(ivState.transpNodeId)))
            packColors(cPacker);
         
        if (ivState.diffuseNodeId != glState.GLDiffuseNodeId ||
                ivState.transpNodeId != glState.GLTranspNodeId)
            invalidBits |= masks.DIFFUSE_MASK.getValue();
        else invalidBits &= ~masks.DIFFUSE_MASK.getValue();      
            
        ivState.packedColors = cPacker.getPackedColors(); 
    }
       
    if ((mask & masks.AMBIENT_MASK.getValue())!=0){
        ivState.ambientColor.copyFrom(ambient.operator_square_bracket(0));
        invalidBits &= ~masks.AMBIENT_MASK.getValue();
        for (int i=0; i<3; i++){
            if (ivState.ambientColor.operator_square_bracket(i) != glState.GLAmbient[i]){
                invalidBits |= masks.AMBIENT_MASK.getValue();
                break;
            }
        }   
    }  
    
    if ((mask & masks.EMISSIVE_MASK.getValue())!=0){
        ivState.emissiveColor.copyFrom( emissive.operator_square_bracket(0));
        invalidBits &= ~masks.EMISSIVE_MASK.getValue();
        for (int i=0; i<3; i++){
            if (ivState.emissiveColor.operator_square_bracket(i) != glState.GLEmissive[i]){
                invalidBits |= masks.EMISSIVE_MASK.getValue();
                break;
            }
        }   
    }
        
    if ((mask & masks.SPECULAR_MASK.getValue())!=0){
        ivState.specularColor.copyFrom( specular.operator_square_bracket(0));
        invalidBits &= ~masks.SPECULAR_MASK.getValue();
        for (int i=0; i<3; i++){
            if (ivState.specularColor.operator_square_bracket(i) != glState.GLSpecular[i]){
                invalidBits |= masks.SPECULAR_MASK.getValue();
                break;
            }
        }   
    }
    
    if ((mask & masks.SHININESS_MASK.getValue())!=0){
        ivState.shininess = shininess.operator_square_bracket(0);
        if (Math.abs(ivState.shininess - glState.GLShininess) 
                > SO_LAZY_SHINY_THRESHOLD){
            invalidBits |= masks.SHININESS_MASK.getValue();
        }      
        else invalidBits &= ~masks.SHININESS_MASK.getValue();       
    }
    // For open caches, record the fact that set was called:
    ivState.cacheLevelSetBits |= mask;    
}


//////////////////////////////////////////////////////////////////////////
//
// Description:
//  
//  Send a packed color to the GL, update GLstate  of current
//  GLLazyElement to indicate it.  Also do the right thing for
//  stipple transparency.
//  This is special method for sending the first color from a
//  vertexProperty node, and making that send consistent with the
//  transparency state.
//  MUST be invoked immediately after initial SoGLLazyElement::send.
//  AND that send must issue a lazy send of both diffuse color and transparency
//  or otherwise cache problems will result.
//
//  use: SoInternal, public
////////////////////////////////////////////////////////////////////////////
//
public void
sendVPPacked(SoState state, IntBuffer pcolor)
{
	GL2 gl2 = state.getGL2();
	
    byte[] _pcolor = new byte[4];
    SoMachine.DGL_HTON_INT32(_pcolor, pcolor.get(0));
  
    gl2.glColor4ubv((byte[])_pcolor,0);
    if (!(glState.GLColorMaterial!=0 || (glState.GLLightModel == LightModel.BASE_COLOR.getValue()))) {
        float[] col4 = new float[4];
        col4[3] = SoMachine.toUByte(_pcolor[3]) * 1.0f/255;
        col4[2] = SoMachine.toUByte(_pcolor[2]) * 1.0f/255;
        col4[1] = SoMachine.toUByte(_pcolor[1]) * 1.0f/255;
        col4[0] = SoMachine.toUByte(_pcolor[0]) * 1.0f/255;
        gl2.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, col4,0);
    }
    
    //Remember what things we are sending to GL, they have to be invalidated:
    int sendMask = masks.DIFFUSE_MASK.getValue();
    
    //Now see about sending transparency:

    // If Stipple is not being used, nothing to be done for transparency.
    if (ivState.transpType == SoGLRenderAction.TransparencyType.SCREEN_DOOR.getValue()){
    
        int trans = SoMachine.toUByte(_pcolor[3]);
    
        // If transparency was off and is still off, can quit here    
        if (glState.GLStippleNum != 0 || trans != 0xff) {

            // Setup GL stipple with the new value being specified                
            int newStipNum = (int)(getNumPatterns()*(1.-trans*(1.0/255.)));
            if (newStipNum != glState.GLStippleNum){
            
                // a real send is occurring: 
                sendMask |= masks.TRANSPARENCY_MASK.getValue();                          
                        
                if (newStipNum >0){
                    sendStipple(state, newStipNum);
                    // if previously was off, turn on GLStipple
                    if (glState.GLStippleNum <= 0)
                        gl2.glEnable(GL2.GL_POLYGON_STIPPLE);
                } 
                else{
                    gl2.glDisable(GL2.GL_POLYGON_STIPPLE);                          
                }
                glState.GLStippleNum = newStipNum;
        
            }
        }
    }
    
    //Now reset/invalidate the diffuse and possibly transparency components.
 
    reset(state, sendMask);
}


/////////////////////////////////////////////////////////////////////////
//
// Description:
//
// Register with the cache that a redundant set was called.
// Cache must know that a set was called, even if the set is redundant.
// Must only be invoked if cache is open.
//
// use: virtual, private, SoInternal
////////////////////////////////////////////////////////////////////////////
public void
registerRedundantSet(SoState state, int bitmask)
{
//#ifdef DEBUG
    if (!(state.isCacheOpen())){
        SoDebugError.post("SoGLLazyElement::registerRedundantSet",
                           "Cache is not open!"); 
    }
//#endif /*DEBUG*/
    //Must obtain a writable version of element (i.e. may need to push)
    SoGLLazyElement le = (SoGLLazyElement)getWInstance(state);
    le.ivState.cacheLevelSetBits |= bitmask;
 
}
/////////////////////////////////////////////////////////////////////////
//
// Description:
//
// Register with the cache that a get() was called.
// Must only be invoked if cache is open.
//
// use: virtual, private, SoINTERNAL
////////////////////////////////////////////////////////////////////////////
public void
registerGetDependence(SoState state, int bitmask)
{
//#ifdef DEBUG
    if (!(state.isCacheOpen())){
        SoDebugError.post("SoGLLazyElement::registerGetDependence",
                           "Cache is not open!"); 
    }
//#endif /*DEBUG*/
    SoGLRenderCache thisCache = (SoGLRenderCache)
            SoCacheElement.getCurrentCache((SoState)state);
    SoGLLazyElement cacheLazyElt = thisCache.getLazyElt();
    int levelSetBits = ivState.cacheLevelSetBits;
    int didRealSendBits = cacheLazyElt.GLSendBits;
        
    //If there was no set and no real send, there is an IV dependence:
    int checkIV = bitmask & (~didRealSendBits) & (~levelSetBits);

    if (checkIV != 0) { 
        copyIVValues(checkIV,cacheLazyElt);
        thisCache.setLazyBits(checkIV, 0, 0);
    } 
}



/////////////////////////////////////////////////////////////////////////
//
// Description:
//
// reset the element to have no knowledge of GL state
//
// use: public
/////////////////////////////////////////////////////////////////////////
public void
reset(SoState state, int bitmask)
{
    //cast away const...
    SoGLLazyElement le = (SoGLLazyElement )this;
    le.invalidBits |= bitmask;
    //Set GLSendBits, so invalidation will persist after pop().
    le.GLSendBits |= bitmask;
     
    //  If cache is open, we must assume that there has been a GLSend
    //  of the component being reset, and set the corresponding GLSendBit.
    if (state.isCacheOpen()){
    	SoCache cache = SoCacheElement.getCurrentCache((SoState)state); // YB
    	if(cache instanceof SoGLRenderCache) { // YB
    		SoGLRenderCache thisCache = (SoGLRenderCache) cache;
    		SoGLLazyElement cacheLazyElt = thisCache.getLazyElt();
    		cacheLazyElt.GLSendBits |= bitmask;
    	}
    	else {
    		// do nothing
    	}
    	  SoGLLazyElement elem = getInstance(state/*ptr*/); //COIN 3D
    	    elem.cachebitmask |= bitmask; // COIN 3D   	
    }
        
    for(int j=0; (j< SO_LAZY_NUM_COMPONENTS) && (bitmask != 0); j++, bitmask >>=1){ 
        if ((bitmask&1)!=0) {
            switch(cases.fromValue(j)) {
                case LIGHT_MODEL_CASE:
                    le.glState.GLLightModel = -1;
                    break;
                        
                case COLOR_MATERIAL_CASE:
                    le.glState.GLColorMaterial = -1;
                    break;
                
                case DIFFUSE_CASE:
                    le.glState.GLDiffuseNodeId = 1;
                    le.glState.GLTranspNodeId = 1;
                    break;
                    
                case AMBIENT_CASE:
                    le.glState.GLAmbient[0]= -1;                                   
                    break;

                case EMISSIVE_CASE:
                    le.glState.GLEmissive[0] = -1;
                    break;
                 
                case SPECULAR_CASE:
                    le.glState.GLSpecular[0] = -1;
                    break;
                
                case SHININESS_CASE:
                    le.glState.GLShininess = -1;
                    break;                  
                    
                case TRANSPARENCY_CASE:
                    le.glState.GLStippleNum = -1;
                    break;
                    
                case BLENDING_CASE:
                    le.glState.GLBlending = -1;
                    break;                  

                case VERTEXORDERING_CASE:
                    le.glState.vertexordering = -1; //COIN3D
                    break;
                    
                case CULLING_CASE:
                    le.glState.culling = -1; //COIN3D
                    break;
                    
                case TWOSIDE_CASE:
                    le.glState.twoside = -1; //COIN3D
                    break;
                    
                case SHADE_MODEL_CASE:
                    // le.glState.flatshading = -1; TODO COIN3D
                    break;
            }
        }
   }
   return;
}
      
      
 	
	public static boolean       isColorIndex(SoState state)
          {return(getInstance(state).colorIndex);}
   
    //! Return the top (current) instance of the element in the state
      //! Note it does NOT cause cache dependency!
      //! It also casts away the  .   
    public  static  SoGLLazyElement getInstance( SoState state)
          {return (SoGLLazyElement )
          (state.getConstElement(classStackIndexMap.get(SoGLLazyElement.class)));}
  
     //! Sends indicated component(s) to GL: 
    //! Only sends if value is not already in GL.
    //! note: has side effects, cannot really be  .
    //! however will not necessarily cause cache dependency.
    public void                send(final SoState state, int mask)
        {if (((mask & invalidBits)!=0) ||(state.isCacheOpen()))  
            ((SoGLLazyElement)(this)).reallySend(state, mask);} 
            
    //! note: matches, copyMatchinfo not used by this element.
    //! they are replaced by lazyMatches, copyLazyMatchInfo  
    public boolean      matches( SoElement element)
        {return false;}
    public SoElement   copyMatchInfo()  
        {return null;}
        
 
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sends to GL only what needs to go: 
//
// Use: private
////////////////////////////////////////////////////////////////////////
private void
reallySend(final SoState state, int bitmask) 

//Send only the parts that don't match inventor state: 
// does the following:
//   copies the inventor state to the GL state
//   sends the GL value to GL
//   sets the appropriate invalidBits off.
//   sets the appropriate GLSendBits on.
//
// If a renderCache is open, it does additional work,
// identifying if this is the first send of the component in the cache,
// identifying if it has been set in the cache, etc.
// This information is used to determine whether there is a cache dependency,
// and if so, whether it is dependent on the inventor state, GL state, or
// dependent on inventor matching GL.
{  
	final GL2 gl2 = state.getGL2(); // java port

    int sendBits = bitmask & invalidBits;
    int realSendBits = 0;
    // with base_color, don't send nondiffuse colors.
    if (ivState.lightModel == SoLazyElement.LightModel.BASE_COLOR.getValue()) sendBits &= ~SoLazyElement.internalMasks.OTHER_COLOR_MASK.getValue();

    //everything that was requested to send will be valid afterward, 
   
    invalidBits &= ~(sendBits);
    
    boolean sendit;

    for(int j = 0; (j< SO_LAZY_NUM_COMPONENTS) && (sendBits != 0); 
                j++,sendBits >>>=1){
        if ((sendBits & 1)!=0){
            int i;
            switch(cases.fromValue(j)){
                // Note that lightmodel and colormaterial have to send
                // before diffuse, so that we can force send diffuse
                                
                case LIGHT_MODEL_CASE :
                    if (glState.GLLightModel == ivState.lightModel) break;
                    SoGLShaderProgram prog = SoGLShaderProgramElement.get((SoState) state); //COIN 3D 
                    if (prog != null) prog.updateCoinParameter((SoState)state, new SbName("coin_light_model"), /*this.coinstate*/ivState.lightModel); // COIN 3D
                    ////if (prog != null) prog.updateCoinParameter((SoState)state, new SbName("coin_two_sided_lighting"), this.coinstate.twoside ? 1:0);
                    if (ivState.lightModel == SoLazyElement.LightModel.PHONG.getValue()){
                        gl2.glEnable(GL2.GL_LIGHTING);
                        if (colorIndex)gl2.glShadeModel(GL2.GL_FLAT);
                    }
                    else {
                        gl2.glDisable(GL2.GL_LIGHTING);
                        if (colorIndex) gl2.glShadeModel(GL2.GL_SMOOTH);
                    }
                    glState.GLLightModel = ivState.lightModel;
                    realSendBits |= masks.LIGHT_MODEL_MASK.getValue();
                    //force-send the diffuse color:
                    sendBits |= (masks.DIFFUSE_MASK.getValue() >>> cases.LIGHT_MODEL_CASE.getValue());
                    glState.GLDiffuseNodeId = 1;
                    break;
        
                case COLOR_MATERIAL_CASE :              
                    // Handle color material if light model does not change:
                    if ((ivState.colorMaterial?1:0) == glState.GLColorMaterial) break;                                                                
                    realSendBits |= masks.COLOR_MATERIAL_MASK.getValue();               
                    glState.GLColorMaterial = ivState.colorMaterial?1:0;
                    if (ivState.colorMaterial){
                            gl2.glColorMaterial(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE);
                            gl2.glEnable(GL2.GL_COLOR_MATERIAL);
                        }
                    else gl2.glDisable(GL2.GL_COLOR_MATERIAL);
//#ifdef DEBUG
                    if ((glState.GLLightModel == LightModel.BASE_COLOR.getValue()) &&
                        (glState.GLColorMaterial != 0)){
                            SoDebugError.post("SoGLLazyElement.reallySend", 
                            "ColorMaterial being used with BASE_COLOR");
                        }
//#endif /*DEBUG*/                  
                    // ensure diffuse color will send:
                    sendBits |= (masks.DIFFUSE_MASK.getValue() >>> cases.COLOR_MATERIAL_CASE.getValue());
                    glState.GLDiffuseNodeId = 1;                            
                    break;
            
                case DIFFUSE_CASE :              
                    // in this case, always send color[0]
                    if (glState.GLDiffuseNodeId == ivState.diffuseNodeId &&
                        glState.GLTranspNodeId == ivState.transpNodeId)
                        break;
                    realSendBits |= masks.DIFFUSE_MASK.getValue();
                    glState.GLDiffuseNodeId = ivState.diffuseNodeId;
                    glState.GLTranspNodeId = ivState.transpNodeId;
                    
                    if(colorIndex){
//#ifdef DEBUG
                        if (glState.GLLightModel != LightModel.BASE_COLOR.getValue()){
                            SoDebugError.post("SoGLLazyElement.reallySend", 
                            "PHONG shading used in colorIndex mode");
                        }
//#endif                                          
                        gl2.glIndexi((int)ivState.colorIndices[0]);
                        break;
                    }
                    final float[] col4 = new float[4];                  
                 
                    final byte[] pColors = new byte[4];
                    SoMachine.DGL_HTON_INT32(pColors, /*Util.toIntArray(*/ivState.packedColors/*)*/);
                    gl2.glColor4ubv(pColors,0);
                    if (!(glState.GLColorMaterial != 0 || (glState.GLLightModel == LightModel.BASE_COLOR.getValue()))) {
                        col4[3] =  (ivState.packedColors[0] & 
                            0xff)   * 1.0f/255;
                        col4[2] = ((ivState.packedColors[0] & 
                            0xff00) >>>  8) * 1.0f/255;
                        col4[1] = ((ivState.packedColors[0] & 
                            0xff0000)>>> 16) * 1.0f/255;
                        col4[0] = ((ivState.packedColors[0] & 
                            0xff000000)>>>24) * 1.0f/255;
                        gl2.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, col4,0);
                    }             
                    break;
                                      
                case AMBIENT_CASE :     
                    sendit = false;
                    for(i=0; i<3; i++){
                        if (glState.GLAmbient[i] != ivState.ambientColor.operator_square_bracket(i)){ 
                            sendit=true;
                            glState.GLAmbient[i]=ivState.ambientColor.operator_square_bracket(i);
                        }
                    }
                    if (!sendit) break;
                    realSendBits |= masks.AMBIENT_MASK.getValue();
                    gl2.glMaterialfv(GL2.GL_FRONT_AND_BACK,GL2.GL_AMBIENT,
                        glState.GLAmbient,0); 
                    break;

                case EMISSIVE_CASE :
                    sendit = false;
                    for(i=0; i<3; i++){
                        if (glState.GLEmissive[i]!=ivState.emissiveColor.operator_square_bracket(i)){
                            sendit = true;
                            glState.GLEmissive[i]=ivState.emissiveColor.operator_square_bracket(i);
                        }
                    }
                    if (!sendit) break;
                    realSendBits |= masks.EMISSIVE_MASK.getValue();
                    gl2.glMaterialfv(GL2.GL_FRONT_AND_BACK,GL2.GL_EMISSION,
                        glState.GLEmissive,0); 
                    break;

                case SPECULAR_CASE :
                    sendit = false;
                    for(i=0; i<3; i++){
                        if (glState.GLSpecular[i]!=ivState.specularColor.operator_square_bracket(i)){
                            sendit = true;
                            glState.GLSpecular[i]=ivState.specularColor.operator_square_bracket(i);
                        }
                    }
                    if (!sendit) break;
                    realSendBits |= masks.SPECULAR_MASK.getValue();
                    gl2.glMaterialfv(GL2.GL_FRONT_AND_BACK,GL2.GL_SPECULAR,
                        glState.GLSpecular,0); 
                    break;

                case SHININESS_CASE :
                    if (Math.abs(glState.GLShininess-ivState.shininess)<
                        SO_LAZY_SHINY_THRESHOLD) break;
                    realSendBits |= masks.SHININESS_MASK.getValue();
                    glState.GLShininess=ivState.shininess;
                    gl2.glMaterialf(GL2.GL_FRONT_AND_BACK,GL2.GL_SHININESS,
                        glState.GLShininess*128.0f); 
                    break;
                    
                case BLENDING_CASE :
                                    
                    if (glState.GLBlending == (ivState.blending?1:0)) break;
                    realSendBits |= masks.BLENDING_MASK.getValue();              
                    glState.GLBlending = (ivState.blending?1:0);
                    if (ivState.blending == true){
                        gl2.glEnable(GL2.GL_BLEND);                     
                    }
                    // blend is being turned off:
                    else gl2.glDisable(GL2.GL_BLEND);                                                   
                    break;
                                 
                case TRANSPARENCY_CASE :                
                        
                    if (ivState.stippleNum == glState.GLStippleNum) break;
                    
                    if (ivState.stippleNum == 0){
                        gl2.glDisable(GL2.GL_POLYGON_STIPPLE);
                    }
                    else{
                        sendStipple(state, ivState.stippleNum);
                        if (glState.GLStippleNum <= 0)
                            gl2.glEnable(GL2.GL_POLYGON_STIPPLE);
                    }
                    glState.GLStippleNum = ivState.stippleNum;                  
                                        
                    realSendBits |= masks.TRANSPARENCY_MASK.getValue();
                    break;
                    
                case VERTEXORDERING_CASE:
                	
                    if (this.glState.vertexordering != this.coinstate.vertexordering) {
                      this.sendVertexOrdering(SoLazyElement.VertexOrdering.fromValue(this.coinstate.vertexordering));
                    }
                    break;
                    
                case TWOSIDE_CASE:
                    if (this.glState.twoside != (this.coinstate.twoside ? 1:0)) {
                      /*SoGLShaderProgram*/ prog = SoGLShaderProgramElement.get((SoState) state);
                      if (prog != null) prog.updateCoinParameter((SoState)state, new SbName("coin_two_sided_lighting"), this.coinstate.twoside ? 1:0);
                      this.sendTwosideLighting(this.coinstate.twoside);
                    }
                    break;
                    
//                case SHADE_MODEL_CASE:
//                    if (this.glState.flatshading != this.coinstate.flatshading) {
//                      this.sendFlatshading(this.coinstate.flatshading);
//                    }
//                    break;
            }
        }
    }
    //Record the real_sends:
    GLSendBits |= realSendBits;
    
    // if cache is open, record required info:
    if (state.isCacheOpen()){
        SoGLRenderCache thisCache = (SoGLRenderCache)
                SoCacheElement.getCurrentCache((SoState)state);
        SoGLLazyElement cacheLazyElt = thisCache.getLazyElt();
        int didRealSendBits = cacheLazyElt.GLSendBits;
        int levelSetBits = ivState.cacheLevelSetBits;
        int levelSendBits = ivState.cacheLevelSendBits;
        
        // To determine which of doSend, checkGL, and checkIV bits to set,
        // All combinations of the bits
        //      A:  levelSendBits
        //      B:  levelSetBits
        //      C:  didRealSendBits
        //      D:  realSendBits
        // are considered.  The following matrix describes the resulting
        // dependencies:
        //
        //              A=0         A=1         A=0         A=1
        //              B=0         B=0         B=1         B=1
        //
        //  C=0,D=0     IV=GL       (IV=GL)     GL          GL
        //                          
        //  C=0,D=1     IV          (IV)        OK          OK
        //
        //  C=1,D=0     IV          (IV)        OK          OK
        //
        //  C=1,D=1     IV          IV          OK          OK
        //
        //  In the above, OK means no dependence, a dependence in parens
        //  means that the dependence is already implied by another
        //  send in the same scene graph.
                
        // doSend flag indicates dependencies on IV=GL; these are components
        // with no set and a non-real send in the cache.
        int doSend = bitmask&(~didRealSendBits)&(~realSendBits) 
            &(~levelSetBits)&(~levelSendBits);
                    
        // checkIV flag indicates dependence on IV.  These are components that
        // did a real send not preceded by a set:
        int checkIV = bitmask&(~levelSetBits)&
            (((~levelSendBits)&(realSendBits))|
            ((~levelSendBits)&(didRealSendBits))|
            ((didRealSendBits)&(realSendBits)));
            
        //checkGL indicates a dependence on GL.  These are components that did
        //a non-real send after a real set.
        int checkGL = bitmask&(~didRealSendBits)&(~realSendBits)
            &(levelSetBits);

        //with light model, a dependence on IV=GL is also a dependence on GL
        
        if((doSend & masks.LIGHT_MODEL_MASK.getValue())!=0){
            checkGL |= masks.LIGHT_MODEL_MASK.getValue();       
        }
        //with diffuse colors, if there are multiple colors, an IV=GL 
        //dependence is also an IV dependence
        if(((doSend & masks.DIFFUSE_MASK.getValue())!=0)&&(ivState.numDiffuseColors > 1)){
            checkIV |= masks.DIFFUSE_MASK.getValue();
        }           
        cacheLazyElt.GLSendBits |= realSendBits;
        if (checkGL != 0) copyGLValues(checkGL,cacheLazyElt);  
        if (checkIV != 0) copyIVValues(checkIV,cacheLazyElt);
        thisCache.setLazyBits(checkIV, checkGL, doSend);
        
        //If we set a bit in levelSendBits, must do this on a writable element:
        if (ivState.cacheLevelSendBits != (ivState.cacheLevelSendBits|bitmask)){
            SoGLLazyElement le = 
                (SoGLLazyElement)getWInstance((SoState )state);
            le.ivState.cacheLevelSendBits |= bitmask;
        }
    }   

    return;
}
 
//! Replace  matches() for this element:
//! matches "this" element GL or IV state with values in eltInState
	public boolean lazyMatches(int checkGLFlag, int checkIVFlag,
    SoGLLazyElement eltInState){
    if ((checkGLFlag!=0) || (checkIVFlag!=0) ||
        (ivState.transpType != eltInState.ivState.transpType))
        return (fullLazyMatches (checkGLFlag, checkIVFlag, eltInState));
    else return(true);
    }
	
////////////////////////////////////////////////////////////////////////
//
// Description:
//  used to test whether a cache can be used.  Invoked by lazyMatches.
//  
//  Compares element in state against cached element, looking for
//  matches in specified GL and/or IV state
//   
//
// Use: private
/////////////////////////////////////////////////////////////////////////

private boolean
fullLazyMatches(int checkGL, int checkIV, 
        SoGLLazyElement stateLazyElt) 

{
    // check to make sure transparency type has not changed:
    if(ivState.transpType != stateLazyElt.ivState.transpType){
//#ifdef DEBUG
        if (SoDebug.GetEnv("IV_DEBUG_CACHES") != null) {
            System.err.print( "CACHE DEBUG: cache not valid\n ");       
            System.err.print("transparency type match failed,\n");
            System.err.print("prev,  current "+ivState.transpType+" "+stateLazyElt.ivState.transpType+"\n");
        }
//#endif /*DEBUG*/       
        return false;
    }
    
    int i;
    int bitmask = checkIV;
 
    for(i=0; (i< SO_LAZY_NUM_COMPONENTS)&&(bitmask!=0); i++,bitmask>>=1){

        if ((bitmask & 1)!=0){

            int j;
            switch(cases.fromValue(i)){
                case LIGHT_MODEL_CASE :
                    if (ivState.lightModel != stateLazyElt.ivState.lightModel){
//#ifdef DEBUG
                        if (SoDebug.GetEnv("IV_DEBUG_CACHES") != null) {
                            System.err.printf("CACHE DEBUG: cache not valid\n ");       
                            System.err.printf("lightModel match failed,\n");
                            System.err.printf("prev,  current "+ivState.lightModel+" "+stateLazyElt.ivState.lightModel+"\n");
                        }
//#endif /*DEBUG*/           
                        return false;
                    }
                    break;
                                     
                case COLOR_MATERIAL_CASE :
                    if (ivState.colorMaterial != 
                        stateLazyElt.ivState.colorMaterial){
//#ifdef DEBUG
                        if (SoDebug.GetEnv("IV_DEBUG_CACHES") != null) {
                        	System.err.printf("CACHE DEBUG: cache not valid\n ");       
                        	System.err.printf("colorMaterial match failed,\n");
                        	System.err.printf("prev,  current "+ivState.colorMaterial+" "+stateLazyElt.ivState.colorMaterial+"\n");
                        }
//#endif /*DEBUG*/           
                        return false;
                    }                   
                    break;
                    
                case DIFFUSE_CASE :
                    if (ivState.diffuseNodeId != 
                        stateLazyElt.ivState.diffuseNodeId ||
                        ivState.transpNodeId !=
                        stateLazyElt.ivState.transpNodeId){
//#ifdef DEBUG
                        if (SoDebug.GetEnv("IV_DEBUG_CACHES") != null) {
                            System.err.print( "CACHE DEBUG: cache not valid\n");       
                            System.err.print( "diffuse&trans match failed,\n");
                            System.err.print( "prev,  current "+ivState.diffuseNodeId+" "+ivState.transpNodeId+", "+stateLazyElt.ivState.diffuseNodeId+" "+stateLazyElt.ivState.transpNodeId+"\n");
                        }
//#endif /*DEBUG*/                            
                        return (false);
                    }
                    break;                                  
                case AMBIENT_CASE :
                    for(j=0; j<3; j++){
                        if (ivState.ambientColor.operator_square_bracket(j)!=
                            stateLazyElt.ivState.ambientColor.operator_square_bracket(j)){
//#ifdef DEBUG
                            if (SoDebug.GetEnv("IV_DEBUG_CACHES") != null) {
                               System.err.print( "CACHE DEBUG: cache not valid\n");       
                               System.err.print( "ambient "+j+" match failed,\n");
                               System.err.print( "prev,  current "+ivState.ambientColor.operator_square_bracket(j)+" "+stateLazyElt.ivState.ambientColor.operator_square_bracket(j)+"\n");
                            }
//#endif /*DEBUG*/                                 
                            return(false);
                        }
                    }   
                    break;

                case EMISSIVE_CASE :
                    for(j=0; j<3; j++){
                        if (ivState.emissiveColor.operator_square_bracket(j)!=
                            stateLazyElt.ivState.emissiveColor.operator_square_bracket(j)){
//#ifdef DEBUG
                            if (SoDebug.GetEnv("IV_DEBUG_CACHES")!= null) {
                               System.err.print( "CACHE DEBUG: cache not valid\n");      
                               System.err.print( "emissive "+j+" match failed,\n");
                               System.err.print( "prev,  current "+ivState.emissiveColor.operator_square_bracket(j)+" "+stateLazyElt.ivState.emissiveColor.operator_square_bracket(j)+"\n");
                            }
//#endif /*DEBUG*/                         
                            return(false);
                        }
                    }   
                    break;

                case SPECULAR_CASE :
                    for(j=0; j<3; j++){
                        if (ivState.specularColor.operator_square_bracket(j)!=
                            stateLazyElt.ivState.specularColor.operator_square_bracket(j)){
//#ifdef DEBUG
                            if (SoDebug.GetEnv("IV_DEBUG_CACHES") != null) {
                               System.err.print( "CACHE DEBUG: cache not valid\n");       
                               System.err.print( "specular "+j+" match failed,\n");
                               System.err.print( "prev,  current "+ivState.specularColor.operator_square_bracket(j)+" "+stateLazyElt.ivState.specularColor.operator_square_bracket(j)+"\n");
                            }
//#endif /*DEBUG*/                                 
                             return(false);
                        }
                    }   
                    break;

                case SHININESS_CASE :
                    if (Math.abs(ivState.shininess - stateLazyElt.ivState.shininess)
                        >  SO_LAZY_SHINY_THRESHOLD){
//#ifdef DEBUG
                        if (SoDebug.GetEnv("IV_DEBUG_CACHES") != null) {
                            System.err.print( "CACHE DEBUG: cache not valid\n");       
                            System.err.print( "shininess match failed,\n");
                            System.err.print( "prev,  current "+ivState.shininess+" "+stateLazyElt.ivState.shininess+"\n");
                        }
//#endif /*DEBUG*/                                  
                        return (false);
                    }
                    break;
                     
                case BLENDING_CASE :
                    if (ivState.blending != stateLazyElt.ivState.blending){
//#ifdef DEBUG
                        if (SoDebug.GetEnv("IV_DEBUG_CACHES") != null) {
                            System.err.print( "CACHE DEBUG: cache not valid\n");       
                            System.err.print( "blend match failed,\n");
                            System.err.print( "prev,  current "+ivState.blending+" "+stateLazyElt.ivState.blending+"\n");
                        }
//#endif /*DEBUG*/                    
                        return false;
                    }
                    break;
                    

                case TRANSPARENCY_CASE :                                            
                    if (ivState.stippleNum != 
                            stateLazyElt.ivState.stippleNum){                  
//#ifdef DEBUG
                        if (SoDebug.GetEnv("IV_DEBUG_CACHES") != null) {
                            System.err.print( "CACHE DEBUG: cache not valid\n");       
                            System.err.print( "IVstipple match failed,\n");
                            System.err.print( "prev,  current "+ivState.stippleNum+" "+stateLazyElt.ivState.stippleNum+"\n");
                        }
//#endif /*DEBUG*/                        
                        return false;
                    }
                    break;
                                
                default:
//#ifdef DEBUG
                SoDebugError.post("SoGLLazyElement.matches",
                                   "Invalid component of element"); 
//#endif /*DEBUG*/
                break;
            } 
        }
    }
    
    if ((bitmask = checkGL)==0) return true;
 
    for(i=0;  (i< SO_LAZY_NUM_COMPONENTS)&&(bitmask!=0); i++,bitmask>>>=1){

        if ((bitmask & 1)!=0){

            int j;
            switch(cases.fromValue(i)){
                case LIGHT_MODEL_CASE :
                    if (glState.GLLightModel != 
                        stateLazyElt.glState.GLLightModel){
//#ifdef DEBUG
                        if (SoDebug.GetEnv("IV_DEBUG_CACHES") != null) {
                            System.err.print( "CACHE DEBUG: cache not valid\n ");       
                            System.err.print( "GLLightModel match failed,\n");
                            System.err.print( "prev,  current "+glState.GLLightModel+" "+stateLazyElt.glState.GLLightModel+"\n");
                        }
//#endif /*DEBUG*/                    
                        return false;
                    }
                    break;
                             
                case COLOR_MATERIAL_CASE :
                    if (glState.GLColorMaterial != 
                        stateLazyElt.glState.GLColorMaterial){
//#ifdef DEBUG
                        if (SoDebug.GetEnv("IV_DEBUG_CACHES") != null) {
                            System.err.print( "CACHE DEBUG: cache not valid\n");       
                            System.err.print( "GLColorMaterial match failed,\n");
                            System.err.print( "prev,  current "+glState.GLColorMaterial+" "+stateLazyElt.glState.GLColorMaterial+"\n");
                        }
//#endif /*DEBUG*/                    
                        return false;
                    }
                    break;

                case DIFFUSE_CASE :
                    if (glState.GLDiffuseNodeId != 
                        stateLazyElt.glState.GLDiffuseNodeId ||
                        glState.GLTranspNodeId != 
                        stateLazyElt.glState.GLTranspNodeId){
//#ifdef DEBUG
                        if (SoDebug.GetEnv("IV_DEBUG_CACHES") != null) {
                            System.err.print( "CACHE DEBUG: cache not valid\n");       
                            System.err.print( "GLDiffuse&Transp match failed,\n");
                            System.err.print( "prev,  current "+glState.GLDiffuseNodeId+" "+glState.GLTranspNodeId+",  "+stateLazyElt.glState.GLDiffuseNodeId+" "+stateLazyElt.glState.GLTranspNodeId+"\n");
                        }
//#endif /*DEBUG*/                    
                        return (false);
                    }
                    break;
                    
                case AMBIENT_CASE :
                    for(j=0; j<3; j++){
                        if (glState.GLAmbient[j]!=
                            stateLazyElt.glState.GLAmbient[j]) {
//#ifdef DEBUG
                            if (SoDebug.GetEnv("IV_DEBUG_CACHES") != null) {
                               System.err.print( "CACHE DEBUG: cache not valid\n");       
                               System.err.print( "GLambient "+j+" match failed,\n");
                               System.err.print( "prev,  current "+glState.GLAmbient[j]+" "+stateLazyElt.glState.GLAmbient[j]+"\n");
                            }
//#endif /*DEBUG*/                        
                            return(false);
                        }
                    }   
                    break;

                case EMISSIVE_CASE :
                    for(j=0; j<3; j++){
                        if (glState.GLEmissive[j]!=
                            stateLazyElt.glState.GLEmissive[j]){ 
//#ifdef DEBUG
                            if (SoDebug.GetEnv("IV_DEBUG_CACHES") != null) {
                              System.err.print( "CACHE DEBUG: cache not valid\n");       
                              System.err.print( "GLemissive "+j+" match failed,\n");
                              System.err.print( "prev,  current "+glState.GLEmissive[j]+" "+stateLazyElt.glState.GLEmissive[j]+"\n");
                            }
//#endif /*DEBUG*/                        
                            return(false);
                        }
                    }   
                    break;

                case SPECULAR_CASE :
                    for(j=0; j<3; j++){
                        if (glState.GLSpecular[j]!=
                            stateLazyElt.glState.GLSpecular[j]){
//#ifdef DEBUG
                            if (SoDebug.GetEnv("IV_DEBUG_CACHES") != null) {
                               System.err.print( "CACHE DEBUG: cache not valid\n");       
                               System.err.print( "GLspecular "+j+" match failed,\n");
                               System.err.print( "prev,  current "+glState.GLSpecular[j]+" "+stateLazyElt.glState.GLSpecular[j]+"\n");
                            }
//#endif /*DEBUG*/                         
                             return(false);
                        }
                    }   
                    break;

                case SHININESS_CASE :
                    if (Math.abs(glState.GLShininess - 
                        stateLazyElt.glState.GLShininess)> 
                            SO_LAZY_SHINY_THRESHOLD){
//#ifdef DEBUG
                        if (SoDebug.GetEnv("IV_DEBUG_CACHES") != null) {
                            System.err.print( "CACHE DEBUG: cache not valid\n");       
                            System.err.print( "GLshininess match failed,\n");
                            System.err.print( "prev,  current "+glState.GLShininess+" "+stateLazyElt.glState.GLShininess+"\n");
                        }
//#endif /*DEBUG*/                             
                        return (false);
                    }
                     break;
                     
                case BLENDING_CASE :
                    if (glState.GLBlending != 
                        stateLazyElt.glState.GLBlending){
//#ifdef DEBUG
                        if (SoDebug.GetEnv("IV_DEBUG_CACHES") != null) {
                            System.err.print( "CACHE DEBUG: cache not valid\n");       
                            System.err.print( "GLblending match failed,\n");
                            System.err.print( "prev,  current "+glState.GLBlending+" "+stateLazyElt.glState.GLBlending+"\n");
                        }
//#endif /*DEBUG*/                    
                        return false;
                    }
                    break;

                    
                case TRANSPARENCY_CASE :                        
                    if (glState.GLStippleNum !=
                            stateLazyElt.glState.GLStippleNum){                                               
//#ifdef DEBUG
                        if (SoDebug.GetEnv("IV_DEBUG_CACHES") != null) {
                            System.err.print( "CACHE DEBUG: cache not valid\n");       
                            System.err.print( "GLstipple match failed,\n");
                            System.err.print( "prev, current  "+glState.GLStippleNum+" "+stateLazyElt.glState.GLStippleNum+"\n");
                        }
//#endif /*DEBUG*/        
                        return false;                   
                    }
                    break; 
                    
                default:
//#ifdef DEBUG
                SoDebugError.post("SoGLLazyElement.matches",
                                   "Invalid component of element"); 
//#endif /*DEBUG*/
                break;
            }
            
        }
    }

    return(true);

}

	
////////////////////////////////////////////////////////////////////////
//
// Description:
//     Create a copy of this instance suitable for doing cache
//     matching on.  Needs to have bitfields zeroed, and copy the
//     transparency type.
//     and "this" lazy element needs its cacheLevelSetBits zeroed.
//     and its cacheLevelSendBits zeroed.
//
// Use: public,  SoINTERNAL
////////////////////////////////////////////////////////////////////////
public SoGLLazyElement 
copyLazyMatchInfo(SoState state)
{
//  force a push, so we can set the setBits in a new element.
    SoGLLazyElement newElt = 
        (SoGLLazyElement )SoLazyElement.getWInstance(state);
    newElt.ivState.cacheLevelSetBits = 0;
    newElt.ivState.cacheLevelSendBits = 0;    
    SoGLLazyElement result =
        (SoGLLazyElement )getTypeId().createInstance();

    result.GLSendBits = 0;
    result.ivState.transpType = newElt.ivState.transpType;    
    return result;
}

///////////////////////////////////////////////////////////////////////////
//
// Description:
//
// This is to be invoked when a cache ("child cache") is called while 
// another cache "parent cache" is open.  It sets the appropriate
// bitflags to make the lazy state of the child merge into the parent.
//
// use: SoINTERNAL public
//
////////////////////////////////////////////////////////////////////////////
public void 
mergeCacheInfo(SoGLRenderCache childCache, 
    SoGLRenderCache parentCache, int doSendFlag, int checkIVFlag, 
    int checkGLFlag)
{       
    SoGLLazyElement parentLazyElt = parentCache.getLazyElt();
    SoGLLazyElement childLazyElt = childCache.getLazyElt();
    int parentDidRealSendBits = parentLazyElt.GLSendBits;
    int parentSetBits = ivState.cacheLevelSetBits;
    int doSend = (~parentDidRealSendBits)& doSendFlag 
        &(~ivState.cacheLevelSendBits)&(~ivState.cacheLevelSetBits);
    int checkIV =  checkIVFlag & (~ivState.cacheLevelSetBits);
    int checkGL = (~parentDidRealSendBits)& checkGLFlag;
        
        
    if (checkGL != 0) 
        childLazyElt.copyGLValues(checkGL, parentLazyElt);
    if (checkIV != 0) 
        childLazyElt.copyIVValues(checkIV, parentLazyElt);  
    
    // in addition we must consider the case where the parent cache issued
    // a set, and the child issued a send which was not a real_send.    
    int moreGL = (~parentDidRealSendBits)&(parentSetBits)&
        ( checkGLFlag|doSendFlag );
    if (moreGL != 0) copyGLValues(moreGL,parentLazyElt);
    
    parentCache.setLazyBits
            (checkIV, checkGL|moreGL, doSend);
    // merge the child didRealSendBits into parent:
    parentLazyElt.GLSendBits |= childLazyElt.GLSendBits;
 
    
}       


////////////////////////////////////////////////////////////////////////
//
//  Description:
//  Copy GL values of state element into cacheGLState, based on GLSendBits
//  in the cacheLazyElement. 
//  This is intended to be called at 
//  cache close(), to keep track of what GL was sent in the cache.
//  "this" is the current lazy element in the state.
//
//  use: SO_INTERNAL public
//
////////////////////////////////////////////////////////////////////////
public void
getCopyGL(SoGLLazyElement cacheLazyElement, 
    SoGLLazyElement.GLLazyState cacheGLState)
{
    int bitmask = cacheLazyElement.GLSendBits;
    for(int j=0; (j<SO_LAZY_NUM_COMPONENTS)&&(bitmask!=0); j++, bitmask>>>=1){

        if ((bitmask&1) != 0){
            int i;         
            switch(cases.fromValue(j)){
                case LIGHT_MODEL_CASE :
                    cacheGLState.GLLightModel = glState.GLLightModel;
                    break;
            
                case COLOR_MATERIAL_CASE :
                    cacheGLState.GLColorMaterial = 
                        glState.GLColorMaterial;
                    break;
            
                case DIFFUSE_CASE :
                    cacheGLState.GLDiffuseNodeId = 
                        glState.GLDiffuseNodeId;
                    cacheGLState.GLTranspNodeId = 
                        glState.GLTranspNodeId;
                    break;
                    
                case AMBIENT_CASE :
                    for(i=0; i<3; i++)
                        cacheGLState.GLAmbient[i] = 
                            glState.GLAmbient[i];
                    break;

                case EMISSIVE_CASE :
                    for(i=0; i<3; i++)
                        cacheGLState.GLEmissive[i] = 
                            glState.GLEmissive[i];
                    break;

                case SPECULAR_CASE :
                    for(i=0; i<3; i++)
                        cacheGLState.GLSpecular[i] = 
                            glState.GLSpecular[i];
                    break;

                case SHININESS_CASE :
                    cacheGLState.GLShininess = 
                        glState.GLShininess;
                    break;
                    
                case BLENDING_CASE :
                    cacheGLState.GLBlending = 
                        glState.GLBlending;
                    break;
                                      
                case TRANSPARENCY_CASE :
                    cacheGLState.GLStippleNum =
                        glState.GLStippleNum;
                    break;
            }
        }
    }
}

    //! method that copies GL state back into "this" element
    //! after cache has been called.
    //! only copies if bit in bitmask is set.
    //! also sets invalidBits to false for these components.
    public void copyBackGL(SoGLLazyElement cacheLazyElement, 
        SoGLLazyElement.GLLazyState cacheGLState) 
        { if (cacheLazyElement.GLSendBits != 0) 
            reallyCopyBackGL(cacheLazyElement.GLSendBits, cacheGLState);}
              

////////////////////////////////////////////////////////////////////////
//
//  Description:
//  Copy GL values to state element from cacheLazyElement, based on GLSendBits 
//  in the cacheLazyElement.  To be used after a cache is called. 
//  also sets the GLsend bits and invalid bits in the state lazy element.
//
//  use: SO_INTERNAL private
//
////////////////////////////////////////////////////////////////////////
private void
reallyCopyBackGL(int bitmask, 
    SoGLLazyElement.GLLazyState cacheGLState)
{
    GLSendBits |= bitmask;
    invalidBits |= bitmask;
   
    for(int j=0; (j<SO_LAZY_NUM_COMPONENTS)&&(bitmask!=0); j++, bitmask>>>=1){

        if ((bitmask&1)!=0){
            int i;         
            switch(cases.fromValue(j)){
                case LIGHT_MODEL_CASE :
                    glState.GLLightModel = 
                        cacheGLState.GLLightModel;
                    break;
            
                case COLOR_MATERIAL_CASE :
                    glState.GLColorMaterial = 
                        cacheGLState.GLColorMaterial;
                    break;
                    
                case DIFFUSE_CASE :
                    glState.GLDiffuseNodeId = 
                        cacheGLState.GLDiffuseNodeId;
                    glState.GLTranspNodeId = 
                        cacheGLState.GLTranspNodeId;
                    break;

                case AMBIENT_CASE :
                    for(i=0; i<3; i++)
                        glState.GLAmbient[i] = 
                            cacheGLState.GLAmbient[i];
                    break;

                case EMISSIVE_CASE :
                    for(i=0; i<3; i++)
                        glState.GLEmissive[i] = 
                            cacheGLState.GLEmissive[i];
                    break;

                case SPECULAR_CASE :
                    for(i=0; i<3; i++)
                        glState.GLSpecular[i] = 
                            cacheGLState.GLSpecular[i];
                    break;

                case SHININESS_CASE :
                    glState.GLShininess = 
                        cacheGLState.GLShininess;
                    break;
   
                case BLENDING_CASE :
                    glState.GLBlending = 
                        cacheGLState.GLBlending;
                    break;
                  
                case TRANSPARENCY_CASE :                                
                    glState.GLStippleNum =
                        cacheGLState.GLStippleNum;                 
                    break;
            }
        }
    }
}

////////////////////////////////////////////////////////////////////////////////
//Data associated with stipple patterns
//Holds defined 32x32 bit stipple patterns. Each is defined as 32
//rows of 4 bytes (32 bits) each.
////////////////////////////////////////////////////////////////////////////////
private static final byte[][]  patterns = new byte[64+1][32 * 4];

//Indicates whether patterns were created and stored yet
private static boolean  patternsCreated = false;

//Holds flags to indicate whether we defined a display list for the
//corresponding stipple pattern
private static final boolean[]  patternListDefined = new boolean[64+1];

//Stores base display list index for patterns. Initialized to -1 to
//indicate that no pattern lists were allocated.
private static int     patternListBase = -1;

//Stores cache context in which display lists were created. We can't
//call a list from a context other than this one.
private static GL2     patternListContext;



////////////////////////////////////////////////////////////////////////
//
// Description:
//    Sends  stipple transparency.  Assumes it really needs to send it.
//
// Use: private
////////////////////////////////////////////////////////////////////////
private void
sendStipple( SoState state, int patIndex )
{
 
//#ifdef DEBUG
    if (patIndex <= 0 || patIndex > getNumPatterns()){
        SoDebugError.post("SoGLLazyElement.sendStipple", 
                           "Invalid pattern index");
    }
//#endif /*DEBUG*/

            // Create pattern arrays if not already done
            if (! patternsCreated) {
                createPatterns();
                patternsCreated = true;

                // Make sure we know that no patterns were sent
                int     i;
                for (i = 0; i <= getNumPatterns(); i++)
                    patternListDefined[i] = false;
            }

            // Determine the current cache context
            GL2 currentContext = Ctx.get(SoGLCacheContextElement.get((SoState)state));
            
            GL2 gl2 = currentContext;

            // If we already have a display list for this pattern
            if (patternListDefined[patIndex]) {

                // If the cache context is valid, just call the list
                if (currentContext == patternListContext)
                    gl2.glCallList(patternListBase + patIndex);

                // Otherwise, just send the pattern
                else
                    gl2.glPolygonStipple(patterns[patIndex],0);
            }

            // If we are in the middle of building a cache or the
            // context is different from the one in which we allocated
            // the display lists, we can't create a new display list,
            // so just send the pattern as is
            else if (state.isCacheOpen() ||
                     (patternListBase >= 0 &&
                      currentContext != patternListContext))
                gl2.glPolygonStipple(patterns[patIndex],0);

            // Otherwise, build a display list and send it
            else {

                // If we haven't allocated the pattern list indices, do so
                if (patternListBase < 0) {
                    patternListBase = (int) gl2.glGenLists(getNumPatterns() + 1);
                    patternListContext = currentContext;
                }

                // Create and send the list
                gl2.glNewList(patternListBase + patIndex, GL2.GL_COMPILE_AND_EXECUTE);
                gl2.glPolygonStipple(patterns[patIndex],0);
                gl2.glEndList();

                patternListDefined[patIndex] = true;
            }
        
}
////////////////////////////////////////////////////////////////////////
//
//  Description:
//  Copy GL values of specified components into specified 
//  cacheLazyElement.  Invoked by this element and GLRenderCache.
//
//  use: SoINTERNAL public
//
////////////////////////////////////////////////////////////////////////

public void
copyGLValues(int bitmask,SoGLLazyElement lazyElt)
{
    for(int j=0; (j<SO_LAZY_NUM_COMPONENTS)&&(bitmask!=0); j++, bitmask>>>=1){

        if ((bitmask&1)!=0){
            int i;         
            switch(cases.fromValue(j)){
                case LIGHT_MODEL_CASE :
                    lazyElt.glState.GLLightModel = glState.GLLightModel;
                    break;
                
                case COLOR_MATERIAL_CASE :
                    lazyElt.glState.GLColorMaterial = glState.GLColorMaterial;
                    break;
                    
                case DIFFUSE_CASE :
                    lazyElt.glState.GLDiffuseNodeId = glState.GLDiffuseNodeId;
                    lazyElt.glState.GLTranspNodeId = glState.GLTranspNodeId;
                    break;

                case AMBIENT_CASE :
                    for(i=0; i<3; i++)
                        lazyElt.glState.GLAmbient[i]=glState.GLAmbient[i];
                    break;

                case EMISSIVE_CASE :
                    for(i=0; i<3; i++)
                        lazyElt.glState.GLEmissive[i]=glState.GLEmissive[i];
                    break;

                case SPECULAR_CASE :
                    for(i=0; i<3; i++)
                        lazyElt.glState.GLSpecular[i]=glState.GLSpecular[i];
                    break;

                case SHININESS_CASE :
                    lazyElt.glState.GLShininess=glState.GLShininess;
                    break;
                    
                case BLENDING_CASE :
                    lazyElt.glState.GLBlending = glState.GLBlending;
                    break;
                                    
                case TRANSPARENCY_CASE :                  
                    lazyElt.glState.GLStippleNum = glState.GLStippleNum;
                    break;    
            }
        }
    }
}

////////////////////////////////////////////////////////////////////////
//
//  Description:
//  Copy Inventor values of specified components into specified 
//  cacheLazyElement
//
//  use: SoINTERNAL public
// 
////////////////////////////////////////////////////////////////////////
public void
copyIVValues(int bitmask,SoGLLazyElement lazyElt)
{
    for(int j=0; (j<SO_LAZY_NUM_COMPONENTS)&&(bitmask!=0); j++, bitmask>>>=1){

        if ((bitmask&1)!=0){
            int i;         
            switch(cases.fromValue(j)){
                
                case LIGHT_MODEL_CASE :
                    lazyElt.ivState.lightModel = ivState.lightModel;
                    break;
                
                case COLOR_MATERIAL_CASE :
                    lazyElt.ivState.colorMaterial = ivState.colorMaterial;
                    break;
                    
                case DIFFUSE_CASE :
                    lazyElt.ivState.diffuseNodeId = ivState.diffuseNodeId;
                    lazyElt.ivState.transpNodeId = ivState.transpNodeId;
                    break;
                    
                case AMBIENT_CASE :
                    for(i=0; i<3; i++)
                        lazyElt.ivState.ambientColor.operator_square_bracket(i, 
                            ivState.ambientColor.operator_square_bracket(i));
                    break;

                case EMISSIVE_CASE :
                    for(i=0; i<3; i++)
                        lazyElt.ivState.emissiveColor.operator_square_bracket(i, 
                            ivState.emissiveColor.operator_square_bracket(i));
                    break;

                case SPECULAR_CASE :
                    for(i=0; i<3; i++)
                        lazyElt.ivState.specularColor.operator_square_bracket(i,
                            ivState.specularColor.operator_square_bracket(i));
                    break;

                case SHININESS_CASE :
                    lazyElt.ivState.shininess = ivState.shininess;
                    break;
                    
                case BLENDING_CASE :
                    lazyElt.ivState.blending = ivState.blending;
                    break;

                case TRANSPARENCY_CASE :                
                    lazyElt.ivState.stippleNum = ivState.stippleNum;           
                    break;
            }
        }
    }
}

////////////////////////////////////////////////////////////////////////
//
//  Description:
//  Put the current diffuse diffuse color and transparency into a packed
//  color array, associated with specified SoColorPacker.
//
//  use: private
// 
////////////////////////////////////////////////////////////////////////
private void 
packColors(SoColorPacker cPacker)
{
    //First determine if we have enough space:
    if (cPacker.getSize() < ivState.numDiffuseColors)
        cPacker.reallocate(ivState.numDiffuseColors);

    int[] packedArray = cPacker.getPackedColors();
    boolean multTrans = (ivState.numTransparencies >= ivState.numDiffuseColors);    
    int indx = 0;
    int transp = -1;
    for (int i=0; i< ivState.numDiffuseColors; i++){
        if (isPacked()){
            if (i == 0 || multTrans) 
                transp = (int)((1.0 - ivState.transparencies[i])*255.);      
            packedArray[i] = (ivState.packedColors[i] & 0xffffff00)|
                (transp & 0xff);                                       
        }
        else{
            if (multTrans) indx = i;
            packedArray[i] = (ivState.diffuseColors.get(i)).
                getPackedValue(ivState.transparencies[indx]);       
        }
        
    }
    cPacker.setNodeIds(ivState.diffuseNodeId, ivState.transpNodeId);
}


/////////////////////////////////////////////////////////////////////////////
//
// Description:
//    This fills in the "patterns" array with polygon stipples that
//    simulate transparency levels using a standard dither matrix.
//    This code was adapted from Foley, van Dam, Feiner, and Hughes,
//    pages 569-572.
//
// Use: private, static
//////////////////////////////////////////////////////////////////////////
private static void
createPatterns()
{
    final short[][]        ditherMatrix = {
    	{ 0, 32,  8, 40,  2, 34, 10, 42},
        {48, 16, 56, 24, 50, 18, 58, 26},
        {12, 44,  4, 36, 14, 46,  6, 38},
        {60, 28, 52, 20, 62, 30, 54, 22},
        	{ 3, 35, 11, 43,  1, 33,  9, 41},
        {51, 19, 59, 27, 49, 17, 57, 25},
        {15, 47,  7, 39, 13, 45,  5, 37},
        {63, 31, 55, 23, 61, 29, 53, 21},
    };

    final byte[]              pat = new byte[8];
    int                 pattern, x, y;

    // For each pattern
    for (pattern = 0; pattern <= 64; pattern++) {

        // Set up an 8x8 pixel pattern in "pat", 1 bit per pixel
        for (y = 0; y < 8; y++) {
            pat[y] = 0;
            for (x = 0; x < 8; x++)
                if (ditherMatrix[y][x] >= pattern)
                    pat[y] |= (1 << (7 - x));
        }

        // Store the 8x8 pattern in the correct slot in "patterns".
        // Since we need a 32x32, replicate the pattern 4 times in
        // each dimension.

//#define PAT_INDEX(x, y)         ((y) * 4 + x)

        for (y = 0; y < 8; y++) {
            for (x = 0; x < 4; x++) {
                patterns[pattern][PAT_INDEX(x, y +  0)] = pat[y];
                patterns[pattern][PAT_INDEX(x, y +  8)] = pat[y];
                patterns[pattern][PAT_INDEX(x, y + 16)] = pat[y];
                patterns[pattern][PAT_INDEX(x, y + 24)] = pat[y];
            }
        }

//#undef PAT_INDEX

    }
}

private static int PAT_INDEX(int x, int y) {
	return ((y) * 4 + x);
}

public void updateColorVBO( SoState state, SoVBO vbo ) // FIXME YB not in synchro with Coin3D
{
  int maxId = ivState.diffuseNodeId;
  if (ivState.transpNodeId > maxId) {
    maxId = ivState.transpNodeId;
  }
  if (vbo.getDataId()!=maxId) {
    vbo.setData((int)(ivState.numDiffuseColors * (long)Integer.SIZE/Byte.SIZE), VoidPtr.create(/*Util.toByteBuffer(*/ivState.packedColors/*)*/), maxId, state);
  }
}

///////////////////////////////////////////////////////////////////////////
//
//  Description:
//  Send color and transparency to GL.
//  This method is included for compatibility with MaterialBundles.
//  Note that this does not update entire material state, should not be the
//  first "send" of a shape node.  
//
//  use: public, SoEXTENDER
////////////////////////////////////////////////////////////////////////////
public void
sendDiffuseByIndex(int index)
{
    final float[] col4 = new float[4];
//#ifdef DEBUG
    //check to make sure lazy element has updated GL state;
    //this method should only be called after an initial call to
    //SoLazyElement.reallySend
    if ((invalidBits & internalMasks.NO_COLOR_MASK.getValue()) != 0){
        SoDebugError.post("SoGLLazyElement.sendDiffuseByIndex", 
            "Indexed send not preceded by send of lazy element");       
    }
    if (index >= ivState.numDiffuseColors){
        SoDebugError.post("SoGLLazyElement.sendDiffuseByIndex", 
            "Not enough diffuse colors provided");
    }
    if (index >= ivState.numTransparencies && 
            ivState.numTransparencies > 1){
        SoDebugError.post("SoGLLazyElement.sendDiffuseByIndex", 
            "Not enough transparencies provided");
    }
//#endif /*DEBUG*/
    
    
    
    //If in color index mode, ignore transparency.
    if (colorIndex){
        gl2.glIndexi((int)ivState.colorIndices[index]);
        return;
    }
            
    final byte[] pColors = new byte[4];
    SoMachine.DGL_HTON_INT32(pColors, (ivState.packedColors[index]));
    gl2.glColor4ubv(pColors,0);
    if (!(glState.GLColorMaterial != 0 || (glState.GLLightModel == LightModel.BASE_COLOR.getValue()))) {
        col4[3] =  (ivState.packedColors[index] & 0xff)   * 1.0f/255;
        col4[2] = ((ivState.packedColors[index] & 0xff00) >>  8) * 1.0f/255;
        col4[1] = ((ivState.packedColors[index] & 0xff0000)>> 16) * 1.0f/255;
        col4[0] = ((ivState.packedColors[index] & 0xff000000)>>24) * 1.0f/255;
        gl2.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, col4,0);
    }
    return;
}

    //!Static sends, always send top-of-stack.  Intended for extender use.  
   public  static void         sendAllMaterial(SoState state)
        {SoGLLazyElement le = getInstance(state);
        if ((le.invalidBits != 0)||(state.isCacheOpen()))  
            le.reallySend(state, masks.ALL_MASK.getValue());}
            

   public void
   sendVertexOrdering( VertexOrdering ordering) 
   {
     gl2.glFrontFace(ordering == VertexOrdering.CW ? GL2.GL_CW : GL2.GL_CCW);
     this.glState.vertexordering = (int) ordering.getValue();
     this.cachebitmask |= masks.VERTEXORDERING_MASK.getValue();
   }

   public void
   sendTwosideLighting(boolean onoff)
   {
     gl2.glLightModeli(GL2.GL_LIGHT_MODEL_TWO_SIDE, onoff ? GL2.GL_TRUE : GL2.GL_FALSE);
     this.glState.twoside = (int) (onoff ? 1 : 0);
     this.cachebitmask |= masks.TWOSIDE_MASK.getValue();
   }

   public void
   sendBackfaceCulling( boolean onoff)
   {
     if (onoff) gl2.glEnable(GL2.GL_CULL_FACE);
     else gl2.glDisable(GL2.GL_CULL_FACE);
     this.glState.culling = (onoff ? 1:0);
     this.cachebitmask |= masks.CULLING_MASK.getValue();
   }

   public static void
   sendVertexOrdering(SoState state, VertexOrdering ordering)
   {
     boolean cacheopen = state.isCacheOpen();
     SoGLLazyElement elem = getInstance(state);
     if (elem.glState.vertexordering != (int) ordering.ordinal()) {
       elem.sendVertexOrdering(ordering);
       if (cacheopen) elem.lazyDidSet(masks.VERTEXORDERING_MASK.getValue());
     }
     else if (cacheopen) {
       elem.lazyDidntSet(masks.VERTEXORDERING_MASK.getValue());
     }
   }

   public void lazyDidntSet(int mask) {
	   if ((mask & masks.DIFFUSE_MASK.getValue())!=0) {
		    if (0==(this.didsetbitmask & masks.DIFFUSE_MASK.getValue())) {
		      // to be safe, always send first diffuse when a cache is open
		      this.didsetbitmask |= masks.DIFFUSE_MASK.getValue();
		      this.opencacheflags = FLAG_FORCE_DIFFUSE;
		    }
		  }
		  this.didntsetbitmask |= mask&(~this.didsetbitmask);
}


public void lazyDidSet(int mask) {
	  if ((mask & masks.DIFFUSE_MASK.getValue())!=0) {
		    if (0==(this.didsetbitmask & masks.DIFFUSE_MASK.getValue())) {
		      // to be safe, always send first diffuse when a cache is open
		      this.opencacheflags |= FLAG_FORCE_DIFFUSE;
		    }
		  }
		  this.didsetbitmask |= mask;
}


public static void
   sendTwosideLighting(SoState state, boolean onoff)
   {
     boolean cacheopen = state.isCacheOpen();
     SoGLLazyElement elem = getInstance(state);
     if (elem.glState.twoside != (int) (onoff?1:0)) {
       elem.sendTwosideLighting(onoff);
       if (cacheopen) elem.lazyDidSet(masks.TWOSIDE_MASK.getValue());
     }
     else if (cacheopen) {
       elem.lazyDidntSet(masks.TWOSIDE_MASK.getValue());
     }
   }

   public static void
   sendBackfaceCulling(SoState state, boolean onoff)
   {
     boolean cacheopen = state.isCacheOpen();
     SoGLLazyElement elem = getInstance(state);
     if (elem.glState.culling != (int) (onoff?1:0)) {
       elem.sendBackfaceCulling(onoff);
       if (cacheopen) elem.lazyDidSet(masks.CULLING_MASK.getValue());
     }
     else if (cacheopen) {
       elem.lazyDidntSet(masks.CULLING_MASK.getValue());
     }
   }
   
 }
