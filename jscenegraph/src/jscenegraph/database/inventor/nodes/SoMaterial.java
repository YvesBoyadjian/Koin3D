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
 |      This file defines the SoMaterial node class.
 |
 |   Author(s)          : Paul S. Strauss, Gavin Bell
 |
 ______________  S I L I C O N   G R A P H I C S   I N C .  ____________
 _______________________________________________________________________
 */

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
  \class SoMaterial SoMaterial.h Inventor/nodes/SoMaterial.h
  \brief The SoMaterial class is a node type for setting up material values for scene geometry.
  \ingroup nodes

  After traversing an SoMaterial node, subsequent shape nodes with
  geometry in the scene graph will use values from the material "pool"
  of the traversal state set up from nodes of this type.

  For detailed information on the various components, see the OpenGL
  color model, presented in the chapter "Colors and Coloring" (chapter
  2.13 in the OpenGL 1.4 specification).

  Note that values from a material node will \e replace the previous
  values from the traversal state, they will \e not accumulate. That's
  the case even when e.g. material changes are \e implicit in an
  iv-file, as illustrated by the following example:
  
  Also note that support for multiple values in ambientColor,
  emissiveColor, specularColor and shininess was obsoleted in Open
  Inventor 2.1. The reason for this design change was performance
  driven, since it's relatively slow to change the OpenGL material
  properties. Changing the diffuse color value is fast though, so it's
  still possible to have multiple diffuseColor and transparency
  values.

  \verbatim
  #Inventor V2.1 ascii

  Material { ambientColor 1 0 0 }
  Cone { }

  Translation { translation 5 0 0 }

  Material { }
  Sphere { }
  \endverbatim

  (The SoSphere will not "inherit" the SoMaterial::ambientColor from
  the first SoMaterial node, even though it is not explicitly set in
  the second material node. The default value of
  SoMaterial::ambientColor will be used.)

  Note that nodes imported as part of a VRML V1.0 file has a special
  case, where the fields SoMaterial::ambientColor,
  SoMaterial::diffuseColor and SoMaterial::specularColor contains zero
  values, and SoMaterial::emissiveColor contains one or more
  values. The values in SoMaterial::emissiveColor should then be
  treated as precalculated lighting, and the other fields should be
  ignored.

  You can detect this case by checking the values of the material
  elements when the scene graph is traversed using an
  SoCallbackAction. SoDiffuseColorElement, SoAmbientColorElement, and
  SoSpecularColorElement will contain one value with a completely
  black color (0.0f, 0.0f, 0.0f), SoShininessElement will contain one
  value of 0.0f, and SoEmissiveColorElement will contain one or more
  values. It is done like this to make rendering work correctly on
  systems that do not test for this specific case.

  You should only check for this case when you're traversing a VRML
  V1.0 file scene graph, of course. See SoNode::getNodeType() for
  information about how nodes can be tested for whether or not they
  have been imported or otherwise set up as of VRML1 type versus
  Inventor type.

  When the scene graph is rendered using an SoGLRenderAction, the
  elements will be set differently to optimize rendering.  The
  SoDiffuseColorElement will be set to the values in
  SoMaterial::emissiveColor, and the light model will be set to
  SoLightModel::BASE_COLOR.

  The SoMaterial::transparency values will always be treated normally.

  Here is a very simple usage example:

  \verbatim
  #Inventor V2.1 ascii

  Separator {
     Coordinate3 {
        point [ 0 0 0, 1 0 0, 1 1 0 ]
     }

     Material {
        diffuseColor [ 1 0 0, 1 1 0, 0 0 1 ]
     }

     MaterialBinding {
        value PER_VERTEX
     }

     IndexedFaceSet {
        coordIndex [ 0, 1, 2, -1 ]
     }
  }
  \endverbatim

  <b>FILE FORMAT/DEFAULTS:</b>
  \code
    Material {
        ambientColor 0.2 0.2 0.2
        diffuseColor 0.8 0.8 0.8
        specularColor 0 0 0
        emissiveColor 0 0 0
        shininess 0.2
        transparency 0
    }
  \endcode

  \sa SoMaterialBinding, SoBaseColor, SoPackedColor
*/

// *************************************************************************

// FIXME: should also describe what happens if the number of values in
// the fields are not consistent. 20020119 mortene.

// *************************************************************************


package jscenegraph.database.inventor.nodes;

import jscenegraph.opengl.GL2;

import jscenegraph.coin3d.inventor.annex.profiler.SbProfilingData;
import jscenegraph.coin3d.inventor.annex.profiler.SoProfiler;
import jscenegraph.coin3d.inventor.annex.profiler.elements.SoProfilerElement;
import jscenegraph.coin3d.inventor.elements.SoDiffuseColorElement;
import jscenegraph.database.inventor.SbBasic;
import jscenegraph.database.inventor.SbColor;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.elements.SoColorPacker;
import jscenegraph.database.inventor.elements.SoGLLazyElement;
import jscenegraph.database.inventor.elements.SoLazyElement;
import jscenegraph.database.inventor.elements.SoLightModelElement;
import jscenegraph.database.inventor.elements.SoOverrideElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFColor;
import jscenegraph.database.inventor.fields.SoMFFloat;
import jscenegraph.database.inventor.misc.SoBase;
import jscenegraph.database.inventor.misc.SoNotList;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.mevis.inventor.elements.SoGLVBOElement;
import jscenegraph.mevis.inventor.misc.SoVBO;
import jscenegraph.port.FloatArray;
import jscenegraph.port.SbColorArray;


////////////////////////////////////////////////////////////////////////////////
//! Surface material definition node.
/*!
\class SoMaterial
\ingroup Nodes
This node defines the current surface material properties for all
subsequent shapes. SoMaterial sets several components of the
current material during traversal. 


Multiple values can be specified for
the \b diffuseColor  and \b transparency .
Different shapes interpret
materials with multiple values differently. To bind materials to
shapes, use an SoMaterialBinding node.

\par File Format/Default
\par
\code
Material {
  ambientColor 0.2 0.2 0.2
  diffuseColor 0.8 0.8 0.8
  specularColor 0 0 0
  emissiveColor 0 0 0
  shininess 0.2
  transparency 0
}
\endcode

\par Action Behavior
\par
SoGLRenderAction, SoCallbackAction
<BR> Sets the ambient color, the diffuse color, the specular color, the emissive color, the shininess, and the transparency of the current material. 

\par See Also
\par
SoBaseColor, SoLightModel, SoMaterialBinding, SoPackedColor
*/
////////////////////////////////////////////////////////////////////////////////

// *************************************************************************

/*!
  \var SoMFColor SoMaterial::ambientColor

  Ambient material part color values. Will by default contain a single
  color value of [0.2, 0.2, 0.2] (ie dark gray).

  The ambient part of the material is not influenced by any
  lightsources, and should be thought of conceptually as the constant,
  but small contribution of light to a scene "seeping in" from
  everywhere.

  (Think of the ambient contribution in the context that there's
  always photons fizzing around everywhere -- even in a black,
  lightsource-less room, for instance).

  Only the first value in this field will be used. All other values
  will be ignored.

  \sa SoEnvironment::ambientIntensity
*/
/*!
  \var SoMFColor SoMaterial::diffuseColor

  Diffuse material part color values. This field is by default
  initialized to contain a single color value of [0.8, 0.8, 0.8]
  (light gray).

  The diffuse part is combined with the light emitted from the scene's
  light sources.

  Traditional Open Inventor uses the same override bit for both
  diffuse color and transparency.  To get around this problem if you
  need to override one without the other, set the environment
  variable "COIN_SEPARATE_DIFFUSE_TRANSPARENCY_OVERRIDE".  This is
  a Coin extension, and  will not work on the other Open Inventor
  implementations.
*/
/*!
  \var SoMFColor SoMaterial::specularColor

  Specular material part color values. Defaults to a single color
  value of [0, 0, 0] (black).

  Only the first value in this field will be used. All other values
  will be ignored.  
*/

/*!
  \var SoMFColor SoMaterial::emissiveColor

  The color of the light "emitted" by the subsequent geometry,
  independent of lighting / shading.

  Defaults to contain a single color value of [0, 0, 0] (black, ie no
  contribution).

  Only the first value in this field will be used. All other values will be ignored.
*/

/*!
  \var SoMFFloat SoMaterial::shininess

  Shininess values. Decides how the light from light sources are
  distributed across the geometry surfaces. Valid range is from 0.0
  (which gives a dim appearance), to 1.0 (glossy-looking surfaces).

  Defaults to contain a single value of 0.2.

  Only the first value in this field will be used. All other values
  will be ignored.
*/

/*!
  \var SoMFFloat SoMaterial::transparency

  Transparency values. Valid range is from 0.0 (completely opaque,
  which is the default) to 1.0 (completely transparent,
  i.e. invisible).

  Defaults to contain a single value of 0.0.

  Traditional Open Inventor uses the same override bit for both
  transparency and diffuse color.  To get around this problem if you
  need to override one without the other, set the environment
  variable "COIN_SEPARATE_DIFFUSE_TRANSPARENCY_OVERRIDE".  This is
  a Coin extension, and  will not work on the other Open Inventor
  implementations.
*/


/**
 * @author Yves Boyadjian
 *
 */
public class SoMaterial extends SoNode {
	
	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoMaterial.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoMaterial.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoMaterial.class); }    	  	
	
	// defines for materialtype
	  private static final int TYPE_UNKNOWN =           0;
	  private static final int TYPE_NORMAL             =1;
	  private static final int TYPE_VRML1_ONLYEMISSIVE =2; // special case in vrml1

	  private SoMaterialP pimpl = new SoMaterialP();

	 public final SoMFColor ambientColor = new SoMFColor();
	  
	 public final SoMFColor diffuseColor = new SoMFColor();
	  
	 public final SoMFColor specularColor = new SoMFColor();
	  
	 public final SoMFColor emissiveColor = new SoMFColor();
	  
    //! Shininess coefficient of the surface. Values can range from 0.0 for
    //! no shininess (a diffuse surface) to 1.0 for maximum shininess (a
    //! highly polished surface).
	 public final SoMFFloat shininess = new SoMFFloat();
	  
    //! Transparency value(s) of the surface. Values can range from 0.0 for
    //! opaque surfaces to 1.0 for completely transparent surfaces.  If the
    //! transparency type is SoGLRenderAction::SCREEN_DOOR then only the
    //! first transparency value will be used.  With other transparency types,
    //! multiple transparencies will be used, if the SoMaterial node
    //! contains as many transparencies as diffuse colors.  If there are not
    //! as many transparencies as diffuse colors, only the first transparency
    //! will be used.
	 public final SoMFFloat transparency = new SoMFFloat();
	  	

//    protected SoColorPacker colorPacker;

//    protected final SoVBO[] _vbo = new SoVBO[1];


////////////////////////////////////////////////////////////////////////
//
// Description:
//    Constructor
//
// Use: public

public SoMaterial()
//
////////////////////////////////////////////////////////////////////////
{
    nodeHeader.SO_NODE_CONSTRUCTOR(/*SoMaterial*/);

    nodeHeader.SO_NODE_ADD_MFIELD(ambientColor,"ambientColor",  (SoLazyElement.getDefaultAmbient()));
    nodeHeader.SO_NODE_ADD_MFIELD(diffuseColor,"diffuseColor",  (SoLazyElement.getDefaultDiffuse()));
    nodeHeader.SO_NODE_ADD_MFIELD(specularColor,"specularColor",(SoLazyElement.getDefaultSpecular()));
    nodeHeader.SO_NODE_ADD_MFIELD(emissiveColor,"emissiveColor",(SoLazyElement.getDefaultEmissive()));
    nodeHeader.SO_NODE_ADD_MFIELD(shininess,"shininess",        (SoLazyElement.getDefaultShininess()));
    nodeHeader.SO_NODE_ADD_MFIELD(transparency,"transparency",  (SoLazyElement.getDefaultTransparency()));    
    isBuiltIn = true;
    
    pimpl.materialtype = TYPE_NORMAL;
    pimpl.transparencyflag = (false ? 1 :0); // we know it's not transparent
    
}

	
	
	 ////////////////////////////////////////////////////////////////////////
	    //
	    // Description:
	    //    This initializes the SoMaterial class.
	    //
	    // Use: internal
	    
	  public static  void
	    initClass()
	    //
	    ////////////////////////////////////////////////////////////////////////
	    {
	        SoSubNode.SO__NODE_INIT_CLASS(SoMaterial.class, "Material", SoNode.class);
	   
	       // Enable elements:
	       SO_ENABLE(SoCallbackAction.class, SoLazyElement.class);
	        //SoCallbackAction.enableElement(SoLazyElement.class);
	       SO_ENABLE(SoGLRenderAction.class, SoGLLazyElement.class);
	        //SoGLRenderAction.enableElement(SoGLLazyElement.class);
	       //SO_ENABLE(SoGLRenderAction.class, SoGLVBOElement.class);
	        //SoGLRenderAction.enableElement(SoGLVBOElement.class);
	       
//	       SO_ENABLE(SoCallbackAction.class, SoAmbientColorElement.class); TODO
	       SO_ENABLE(SoCallbackAction.class, SoDiffuseColorElement.class);
//	       SO_ENABLE(SoCallbackAction.class, SoEmissiveColorElement.class);
//	       SO_ENABLE(SoCallbackAction.class, SoSpecularColorElement.class);
//	       SO_ENABLE(SoCallbackAction.class, SoShininessElement.class);
//	       SO_ENABLE(SoCallbackAction.class, SoTransparencyElement.class);
//
//	       SO_ENABLE(SoGLRenderAction.class, SoAmbientColorElement.class);
	       SO_ENABLE(SoGLRenderAction.class, SoDiffuseColorElement.class);
//	       SO_ENABLE(SoGLRenderAction.class, SoEmissiveColorElement.class);
//	       SO_ENABLE(SoGLRenderAction.class, SoSpecularColorElement.class);
//	       SO_ENABLE(SoGLRenderAction.class, SoShininessElement.class);
//	       SO_ENABLE(SoGLRenderAction.class, SoTransparencyElement.class);
	       
	   }
	  
////////////////////////////////////////////////////////////////////////
//
// Description:
//    Destructor (necessary since inline destructor is too complex)
//
// Use: private

public void destructor()
//
////////////////////////////////////////////////////////////////////////
{
	pimpl.destructor();
	super.destructor();
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Performs accumulation of state for actions.
//
// Use: extender

/*!
This function performs the typical operation of a node for any
action.
*/
public void
doAction(SoAction action)
{
	SoMaterial_doAction(action);
}

private static boolean TEST_OVERRIDE(SoOverrideElement.ElementMask bit, int flags) {
	return ((bit.getValue() & flags) != 0);
}

static int didwarn = 0;

// Doc from superclass.
public void // COIN 3D
SoMaterial_doAction(SoAction action)
{
  boolean istransparent = false;

  SoState state = action.getState();

  if (SoProfiler.isEnabled()) {
    // register the SoColorPacker memory usage
    if (state.isElementEnabled(SoProfilerElement.getClassStackIndex(SoProfilerElement.class))) {
      SoColorPacker packer = pimpl.getColorPacker();
      if (packer != null) {
        SoProfilerElement profilerelt = SoProfilerElement.get(state);
        assert(profilerelt != null);
        final SbProfilingData data = profilerelt.getProfilingData();
        int entry = data.getIndex(action.getCurPath(), true);
        assert(entry != -1);
        int mem = data.getNodeFootprint(entry, SbProfilingData.FootprintType.MEMORY_SIZE);
        data.setNodeFootprint(entry, SbProfilingData.FootprintType.MEMORY_SIZE,
                              mem + packer.getSize() * Integer.BYTES);
      }
    }
  }

  int bitmask = 0;
  int flags = SoOverrideElement.getFlags(state);

  if (!this.ambientColor.isIgnored() && this.ambientColor.getNum()!=0 &&
      !TEST_OVERRIDE(SoOverrideElement.ElementMask.AMBIENT_COLOR,flags)) {
    bitmask |= SoLazyElement.masks.AMBIENT_MASK.getValue();
    if (this.isOverride()) {
      SoOverrideElement.setAmbientColorOverride(state, this, true);
    }
  }
  if (!this.diffuseColor.isIgnored() && this.diffuseColor.getNum()!=0 &&
      !TEST_OVERRIDE(SoOverrideElement.ElementMask.DIFFUSE_COLOR,flags)) {
    // Note: the override flag bit values for diffuseColor and
    // transparency are equal (done like that to match SGI/TGS
    // Inventor behavior), so overriding one will also override the
    // other.
    bitmask |= SoLazyElement.masks.DIFFUSE_MASK.getValue();
    if (this.isOverride()) {
      SoOverrideElement.setDiffuseColorOverride(state, this, true);
    }
  }
  if (!this.emissiveColor.isIgnored() && this.emissiveColor.getNum()!=0 &&
      !TEST_OVERRIDE(SoOverrideElement.ElementMask.EMISSIVE_COLOR,flags)) {
    bitmask |= SoLazyElement.masks.EMISSIVE_MASK.getValue();
    if (this.isOverride()) {
      SoOverrideElement.setEmissiveColorOverride(state, this, true);
    }
  }
  if (!this.specularColor.isIgnored() && this.specularColor.getNum()!=0 &&
      !TEST_OVERRIDE(SoOverrideElement.ElementMask.SPECULAR_COLOR,flags)) {
    bitmask |= SoLazyElement.masks.SPECULAR_MASK.getValue();
    if (this.isOverride()) {
      SoOverrideElement.setSpecularColorOverride(state, this, true);
    }
  }
  if (!this.shininess.isIgnored() && this.shininess.getNum()!=0 &&
      !TEST_OVERRIDE(SoOverrideElement.ElementMask.SHININESS,flags)) {
    bitmask |= SoLazyElement.masks.SHININESS_MASK.getValue();
    if (this.isOverride()) {
      SoOverrideElement.setShininessOverride(state, this, true);
    }
  }
  if (!this.transparency.isIgnored() && this.transparency.getNum()!=0 &&
      !TEST_OVERRIDE(SoOverrideElement.ElementMask.TRANSPARENCY,flags)) {
    // Note: the override flag bit values for diffuseColor and
    // transparency are equal (done like that to match SGI/TGS
    // Inventor behavior), so overriding one will also override the
    // other.
    bitmask |= SoLazyElement.masks.TRANSPARENCY_MASK.getValue();
    if (this.isOverride()) {
      SoOverrideElement.setTransparencyOverride(state, this, true);
    }
    // if we don't know if material is transparent, run through all
    // values and test
    if (pimpl.transparencyflag < 0) {
      int i, n = this.transparency.getNum();
      FloatArray p = new FloatArray(0,this.transparency.getValuesFloat(/*0*/));
      for (i = 0; i < n; i++) {
        if (p.get(i) > 0.0f) {
          istransparent = true;
          break;
        }
      }
      // we now know whether material is transparent or not
      pimpl.transparencyflag = (int) (istransparent ? 1 : 0);
    }
    istransparent = (boolean) (pimpl.transparencyflag != 0);
  }
//#undef TEST_OVERRIDE

  if (bitmask != 0) {
    final SbColor dummycolor = new SbColor(0.8f, 0.8f, 0.0f);
    float dummyval = 0.2f;
    SbColorArray diffuseptr = this.diffuseColor.getValuesSbColorArray(/*0*/);
    int numdiffuse = this.diffuseColor.getNum();

    if (this.getMaterialType() == TYPE_VRML1_ONLYEMISSIVE) {
      bitmask |= SoLazyElement.masks.DIFFUSE_MASK.getValue();
      bitmask &= ~SoLazyElement.masks.EMISSIVE_MASK.getValue();
      diffuseptr = this.emissiveColor.getValuesSbColorArray(/*0*/);
      numdiffuse = this.emissiveColor.getNum();
      // if only emissive color, turn off lighting and render as diffuse.
      // this is much faster
      SoLightModelElement.set(state, /*this,*/ SoLightModelElement.Model.BASE_COLOR); // YB
    }
    else if (this.getNodeType() == SoNode.NodeType.VRML1) {
      SoLightModelElement.set(state, /*this,*/ SoLightModelElement.Model.PHONG); // YB
    }

//#if COIN_DEBUG
    if ((bitmask & SoLazyElement.masks.SHININESS_MASK.getValue())!=0) {
      if (didwarn==0 && (this.shininess.operator_square_bracket(0) < 0.0f || this.shininess.operator_square_bracket(0) > 1.0f)) {
        SoDebugError.postWarning("SoMaterial::GLRender",
                                  "Shininess out of range [0-1]. "+
                                  "The shininess value will be clamped."+
                                  "This warning will be printed only once, but there might be more errors. "+
                                  "You should check and fix your code and/or Inventor exporter.");

        didwarn = 1;
      }
    }
//#endif // COIN_DEBUG

    int numtransp = this.transparency.getNum();
    SoLazyElement.setMaterials(state, this, bitmask,
                                pimpl.getColorPacker(),
                                diffuseptr, numdiffuse,
                                new FloatArray(0,this.transparency.getValuesFloat(/*0*/)), numtransp,
                                (bitmask & SoLazyElement.masks.AMBIENT_MASK.getValue())!=0 ?
                                this.ambientColor.operator_square_bracket(0) : dummycolor,
                                (bitmask & SoLazyElement.masks.EMISSIVE_MASK.getValue())!=0 ?
                                this.emissiveColor.operator_square_bracket(0) : dummycolor,
                                (bitmask & SoLazyElement.masks.SPECULAR_MASK.getValue())!=0 ?
                                this.specularColor.operator_square_bracket(0) : dummycolor,
                                (bitmask & SoLazyElement.masks.SHININESS_MASK.getValue())!=0 ?
                                SbBasic.SbClamp(this.shininess.operator_square_bracket(0), 0.0f, 1.0f) : dummyval,
                                istransparent);
	if (state.isElementEnabled(SoGLVBOElement.getClassStackIndex(SoGLVBOElement.class))) { // COIN 3D
		SoBase.staticDataLock();
		boolean setvbo = false;
		if (SoGLVBOElement.shouldCreateVBO(state, numdiffuse)) {
			setvbo = true;
			if (pimpl.vbo == null) {
				pimpl.vbo = new SoVBO(GL2.GL_ARRAY_BUFFER/*, GL2.GL_STATIC_DRAW*/); // YB
			}
		}
		else if (pimpl.vbo != null) {
			pimpl.vbo.setBufferData(null, 0, 0);
		}
		// don't fill in any data in the VBO. Data will be filled in
		// using the ColorPacker right before the VBO is used
		SoBase.staticDataUnlock();
		if (setvbo) {
			SoGLVBOElement.setColorVBO(state, pimpl.vbo);
		}
	}
  }
}

//public void SoMaterial_doAction(SoAction action)
////
//////////////////////////////////////////////////////////////////////////
//{
//    SoState             state = action.getState();
//    int bitmask = 0;    
//
//    // Set all non-ignored components
//
//    if (! ambientColor.isIgnored() && ambientColor.getNum() > 0
//        && ! SoOverrideElement.getAmbientColorOverride(state)) {
//        if (isOverride()) {
//            SoOverrideElement.setAmbientColorOverride(state, this, true);
//        }
//        bitmask |= SoLazyElement.masks.AMBIENT_MASK.getValue(); 
//    }
//
//    if (! diffuseColor.isIgnored() && diffuseColor.getNum() > 0
//        && ! SoOverrideElement.getDiffuseColorOverride(state)) {
//        if (isOverride()) {
//            SoOverrideElement.setDiffuseColorOverride(state, this, true);
//            // Diffuse color and transparency share override state
//            if (! transparency.isIgnored() && transparency.getNum() > 0)
//                bitmask |= SoLazyElement.masks.TRANSPARENCY_MASK.getValue();
//        }
//        bitmask |= SoLazyElement.masks.DIFFUSE_MASK.getValue();
//    }
//
//    if (! transparency.isIgnored() && transparency.getNum() > 0
//        && ! SoOverrideElement.getTransparencyOverride(state)) {
//        if (isOverride()) {
//            SoOverrideElement.setTransparencyOverride(state, this, true);
//            // Diffuse color and transparency share override state
//            if (! diffuseColor.isIgnored() && diffuseColor.getNum() > 0)
//                bitmask |= SoLazyElement.masks.DIFFUSE_MASK.getValue();
//        }
//        bitmask |= SoLazyElement.masks.TRANSPARENCY_MASK.getValue();
//    }
//    if (! specularColor.isIgnored() && specularColor.getNum() > 0
//        && ! SoOverrideElement.getSpecularColorOverride(state)) {
//        if (isOverride()) {
//            SoOverrideElement.setSpecularColorOverride(state, this, true);
//        }
//        bitmask |= SoLazyElement.masks.SPECULAR_MASK.getValue();
//    }
//
//    if (! emissiveColor.isIgnored() && emissiveColor.getNum() > 0
//        && ! SoOverrideElement.getEmissiveColorOverride(state)) {
//        if (isOverride()) {
//            SoOverrideElement.setEmissiveColorOverride(state, this, true);
//        }
//        bitmask |= SoLazyElement.masks.EMISSIVE_MASK.getValue();
//    }
//
//    if (! shininess.isIgnored() && shininess.getNum() > 0
//        && ! SoOverrideElement.getShininessOverride(state)) {
//        if (isOverride()) {
//            SoOverrideElement.setShininessOverride(state, this, true);
//        }
//        bitmask |= SoLazyElement.masks.SHININESS_MASK.getValue();
//    }
//    SoLazyElement.setMaterials(state, this, bitmask, colorPacker,   
//        diffuseColor, transparency, ambientColor, 
//        emissiveColor, specularColor, shininess);
//
//    if (state.isElementEnabled(SoGLVBOElement.getClassStackIndex(SoGLVBOElement.class))) { // COIN 3D
//        SoBase.staticDataLock();
//        boolean setvbo = false;
//        if (SoGLVBOElement.shouldCreateVBO(state, numdiffuse)) {
//          setvbo = true;
//          if (pimpl.vbo == null) {
//            pimpl.vbo = new SoVBO(GL2.GL_ARRAY_BUFFER, GL2.GL_STATIC_DRAW);
//          }
//        }
//        else if (pimpl.vbo) {
//          pimpl.vbo.setBufferData(NULL, 0, 0);
//        }
//        // don't fill in any data in the VBO. Data will be filled in
//        // using the ColorPacker right before the VBO is used
//        SoBase.staticDataUnlock();
//        if (setvbo) {
//          SoGLVBOElement.setColorVBO(state, pimpl.vbo);
//        }
//      }
//}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Method for callback action
//
// Use: extender

public void
callback(SoCallbackAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoMaterial_doAction(action);
}

////////////////////////////////////////////////////////////////////////
//
// Description:
//    Method for GL rendering
//
// Use: extender

public void
GLRender(SoGLRenderAction action)
//
////////////////////////////////////////////////////////////////////////
{
    SoMaterial_doAction(action);

//    // If there's only one color, we might as well send it now.  This
//    // prevents cache dependencies in some cases that were
//    // specifically optimized for Inventor 2.0.
//    if (diffuseColor.getNum() == 1 && !diffuseColor.isIgnored())
//        SoGLLazyElement.sendAllMaterial(action.getState());
//
//    SoState state = action.getState();
//    if (state.isElementEnabled(SoGLVBOElement.getClassStackIndex(SoGLVBOElement.class))) {
//      // update the VBO, no data is passed since that is done by SoColorPacker later on
//      SoGLVBOElement.updateVBO(state, SoGLVBOElement.VBOType.COLOR_VBO, _vbo);
//    }
}

public void
notify(SoNotList list)
{
  SoField f = list.getLastField();
  if (f != null) pimpl.materialtype = TYPE_UNKNOWN;
  if (f == this.transparency) {
    pimpl.transparencyflag = -1; // unknown
  }
  super.notify(list);
}

//
// to test for special vrml1 case. It's not used right now,
// but it might be enabled again later. pederb, 2002-09-11
//
public int
getMaterialType()
{
  if (this.getNodeType() != SoNode.NodeType.VRML1) return TYPE_NORMAL;
  else {
    if (pimpl.materialtype == TYPE_UNKNOWN) {
      if (!this.diffuseColor.isIgnored() && this.diffuseColor.getNum() == 0 &&
          !this.ambientColor.isIgnored() && this.ambientColor.getNum() == 0 &&
          !this.specularColor.isIgnored() && this.specularColor.getNum() == 0 &&
          !this.emissiveColor.isIgnored() && this.emissiveColor.getNum() != 0) {
        pimpl.materialtype = TYPE_VRML1_ONLYEMISSIVE;
      }
      else if (this.emissiveColor.getNum() > this.diffuseColor.getNum()) {
        pimpl.materialtype = TYPE_VRML1_ONLYEMISSIVE;
      }
      else {
        pimpl.materialtype = TYPE_NORMAL;
      }
    }
    return pimpl.materialtype;
  }
}



}
