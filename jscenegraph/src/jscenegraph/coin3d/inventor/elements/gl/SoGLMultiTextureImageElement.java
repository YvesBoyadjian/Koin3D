
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

package jscenegraph.coin3d.inventor.elements.gl;

import com.jogamp.opengl.GL2;

import jscenegraph.coin3d.glue.cc_glglue;
import jscenegraph.coin3d.inventor.elements.SoMultiTextureImageElement;
import jscenegraph.coin3d.inventor.elements.SoTextureCombineElement;
import jscenegraph.coin3d.inventor.lists.SbList;
import jscenegraph.coin3d.inventor.lists.SbListOfMutableRefs;
import jscenegraph.coin3d.inventor.misc.SoGLBigImage;
import jscenegraph.coin3d.inventor.misc.SoGLImage;
import jscenegraph.coin3d.misc.SoGL;
import jscenegraph.coin3d.shaders.SoGLShaderProgram;
import jscenegraph.coin3d.shaders.inventor.elements.SoGLShaderProgramElement;
import jscenegraph.database.inventor.SbColor;
import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SbVec3s;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.elements.SoElement;
import jscenegraph.database.inventor.elements.SoGLDisplayList;
import jscenegraph.database.inventor.elements.SoShapeStyleElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.port.Mutable;

/**
 * @author Yves Boyadjian
 *
 */
/*!
  \class SoGLMultiTextureImageElement Inventor/elements/SoGLMultiTextureImageElement.h
  \brief The SoGLMultiTextureImageElement is used to control the current GL texture for texture units.
  \ingroup elements
*/

public class SoGLMultiTextureImageElement extends SoMultiTextureImageElement {

	//private static final int MAX_UNITS =16;
	
	  public static class GLUnitData implements Mutable {
		  public SoGLImage glimage; // ptr

		@Override
		public void copyFrom(Object other) {
			glimage = ((SoGLMultiTextureImageElement.GLUnitData)other).glimage;
		}
		  };
		  
		  private SoGLMultiTextureImageElementP pimpl;
		  
		  private static class SoGLMultiTextureImageElementP {
			  
			  public final SoGLMultiTextureImageElement.GLUnitData defaultdata = new SoGLMultiTextureImageElement.GLUnitData();
			  public final SbListOfMutableRefs<SoGLMultiTextureImageElement.GLUnitData> unitdata = new SbListOfMutableRefs<>(()->new SoGLMultiTextureImageElement.GLUnitData());
			  public SoState state;
			  public int cachecontext;

			  public
				  void ensureCapacity(int unit) {
				    while (unit >= this.unitdata.getLength()) {
				      this.unitdata.append(new SoGLMultiTextureImageElement.GLUnitData());
				    }
				  }

		  }
		  
	//private int lastunitset;
	//private		  final SoGLMultiTextureImageElement.GLUnitData[] unitdata = new SoGLMultiTextureImageElement.GLUnitData[MAX_UNITS];
	//private		  SoState state;
	//private		  int cachecontext;
		  
		  
		  public SoGLMultiTextureImageElement()
		  {
		    this.pimpl = new SoGLMultiTextureImageElementP();

		    //this->setTypeId(SoGLMultiTextureImageElement::classTypeId);
		    //this->setStackIndex(SoGLMultiTextureImageElement::classStackIndex);
		  }

		  
		  
// doc from parent
public void
init(SoState state)
{
  super.init(state);

  SoAction action = state.getAction();
  assert(action.isOfType(SoGLRenderAction.getClassTypeId()));

  // fetch cache context from action since SoGLCacheContextElement
  // might not be initialized yet.
  SoGLRenderAction glaction = (SoGLRenderAction) action;
  this.pimpl.cachecontext = glaction.getCacheContext();
  this.pimpl.state = state;
}


// Documented in superclass. Overridden to pass GL state to the next
// element.
public void
push(SoState state)
{
  super.push(state);
  SoGLMultiTextureImageElement prev = (SoGLMultiTextureImageElement)
    this.getNextInStack();
  this.pimpl.state = state;
  this.pimpl.cachecontext = prev.pimpl.cachecontext;
  this.pimpl.unitdata.copyFrom(prev.pimpl.unitdata);
  
  // capture previous element since we might or might not change the
  // GL state in set/pop
  prev.capture(state);
}


// Documented in superclass. Overridden to pass GL state to the
// previous element.
public void
pop(SoState state,
                                  SoElement prevTopElement)
{
  super.pop(state, prevTopElement);
  SoGLMultiTextureImageElement prev = (SoGLMultiTextureImageElement)
    prevTopElement;

  SoGLShaderProgram prog = SoGLShaderProgramElement.get(state);
  String str;
  
  int maxunits = Math.max(prev.pimpl.unitdata.getLength(),
                             this.pimpl.unitdata.getLength());

  for (int i = 0; i < maxunits; i++) {
    GLUnitData prevud = 
      (i < prev.pimpl.unitdata.getLength()) ?
      prev.pimpl.unitdata.operator_square_bracket(i) :
      prev.pimpl.defaultdata;
    
    GLUnitData thisud = 
      (i < this.pimpl.unitdata.getLength()) ?
      this.pimpl.unitdata.operator_square_bracket(i) :
      this.pimpl.defaultdata;

    if (thisud.glimage != prevud.glimage) this.updateGL(i);
    str = "coin_texunit"+i+"_model";
    if (prog != null) prog.updateCoinParameter(state, new SbName(str/*.getString()*/),
                                        thisud.glimage != null ? this.getUnitData(i).model.getValue() : 0);
  }
}

public static SoMultiTextureImageElement.Wrap
multi_translateWrap( SoGLImage.Wrap wrap)
{
  if (wrap == SoGLImage.Wrap.REPEAT) return SoMultiTextureImageElement.Wrap.REPEAT;
  return SoMultiTextureImageElement.Wrap.CLAMP;
}

/*!
  Sets the current texture. Id \a didapply is TRUE, it is assumed
  that the texture image already is the current GL texture. Do not
  use this feature unless you know what you're doing.
*/
public static void
set(SoState state, SoNode node,
                                  int unit,
                                  SoGLImage image,
                                  Model model,
                                  final SbColor blendColor)
{
  SoGLMultiTextureImageElement elem = (SoGLMultiTextureImageElement)
    state.getElement(classStackIndexMap.get(SoGLMultiTextureImageElement.class));

  elem.pimpl.ensureCapacity(unit);
  GLUnitData ud = elem.pimpl.unitdata.operator_square_bracket(unit);
  
  // FIXME: buggy. Find some solution to handle this. pederb, 2003-11-12
  // if (ud.glimage && ud.glimage->getImage()) ud.glimage->getImage()->readUnlock();

  if (image != null) {
    // keep SoMultiTextureImageElement "up-to-date"
	  SoMultiTextureImageElement.set(state, node,
                   unit,
                   new SbVec3s((short)0,(short)0,(short)0),
                   0,
                   null,
                   multi_translateWrap(image.getWrapS()),
                   multi_translateWrap(image.getWrapT()),
                   multi_translateWrap(image.getWrapR()),
                   model,
                   blendColor);
    ud.glimage = image;
    // make sure image isn't changed while this is the active texture
    // FIXME: buggy. Find some solution to handle this. pederb, 2003-11-12
    // if (image->getImage()) image->getImage()->readLock();
  }
  else {
    ud.glimage = null;
    SoMultiTextureImageElement.setDefault(state, node, unit);
  }
  elem.updateGL(unit);

  // FIXME: check if it's possible to support for other units as well
  if ((unit == 0) && image != null && image.isOfType(SoGLBigImage.getClassTypeId())) {
    SoShapeStyleElement.setBigImageEnabled(state, true);
  }
  SoShapeStyleElement.setTransparentTexture(state,
                                             SoGLMultiTextureImageElement.hasTransparency(state));
  
  SoGLShaderProgram prog = SoGLShaderProgramElement.get(state);
  if (prog != null) {
    String str;
    str = "coin_texunit"+unit+"_model";
    prog.updateCoinParameter(state, new SbName(str/*.getString()*/), ud.glimage != null ? model.getValue() : 0);
  }
}

public static void
restore(SoState state, int unit)
{
  SoGLMultiTextureImageElement elem = (SoGLMultiTextureImageElement)
    state.getConstElement(classStackIndexMap.get(SoGLMultiTextureImageElement.class));
  
  elem.updateGL(unit);
}

public static SoGLImage
get(SoState state,
                                  int unit,
                                  final Model[] model,
                                  final SbColor blendcolor)
{
  SoGLMultiTextureImageElement elem = (SoGLMultiTextureImageElement)
    getConstElement(state, classStackIndexMap.get(SoGLMultiTextureImageElement.class));

  if (unit < elem.getNumUnits()) {
    UnitData ud = elem.getUnitData(unit);
    model[0] = ud.model;
    blendcolor.copyFrom( ud.blendColor);
    return elem.pimpl.unitdata.operator_square_bracket(unit).glimage;
  }
  return null;
}


/*!
  Returns TRUE if any of the images have at least one transparent pixel.
  
  \since Coin 3.1
*/
public static boolean 
hasTransparency(SoState state)
{
  SoGLMultiTextureImageElement elem = ( SoGLMultiTextureImageElement)
    getConstElement(state, classStackIndexMap.get(SoGLMultiTextureImageElement.class));
  
  for (int i = 0; i <= elem.pimpl.unitdata.getLength(); i++) {
    if (elem.hasTransparency(i)) return true;
  }
  return false;
}

// doc from parent
public boolean
hasTransparency( int unit) 
{
  if (unit < this.pimpl.unitdata.getLength()) {
    GLUnitData ud = this.pimpl.unitdata.operator_square_bracket(unit);
    if (ud.glimage != null) {
      return ud.glimage.hasTransparency();
    }
  }
  return false;
}

public void
updateGL( int unit)
{
  GLUnitData glud = 
    (unit < this.pimpl.unitdata.getLength()) ? 
    this.pimpl.unitdata.operator_square_bracket(unit) :
    this.pimpl.defaultdata;
  
  if (glud.glimage != null) {
    cc_glglue glue = SoGL.cc_glglue_instance(this.pimpl.cachecontext);
    SoGL.cc_glglue_glActiveTexture(glue, (int) ((int)(GL2.GL_TEXTURE0) + unit));

    UnitData ud = this.getUnitData(unit);
    SoState state = this.pimpl.state;
    SoGLDisplayList dl = glud.glimage.getGLDisplayList(state);

    // tag image (for GLImage LRU cache).
    SoGLImage.tagImage(state, glud.glimage);
    
    GL2 gl2 = glue.getGL2();

    if (SoTextureCombineElement.isDefault(state, unit)) {
      switch (ud.model) {
      case DECAL:
        gl2.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_DECAL);
        break;
      case MODULATE:
    	  gl2.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
        break;
      case BLEND:
    	  gl2.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_BLEND);
    	  gl2.glTexEnvfv(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_COLOR, ud.blendColor.getValueRead(),0);
        break;
      case REPLACE:
        // GL_REPLACE mode was introduced with OpenGL 1.1. It is
        // considered the client code's responsibility to check
        // that it can use this mode.
        //
        // FIXME: ..but we should do a sanity check anyway.
        // 20030901 mortene.
        gl2.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE);
        break;
      default:
        assert(false);// && "unknown model");
        break;
      }
      gl2.glGetError(); // YB
      gl2.glGetError();
    }
    else {
      SoTextureCombineElement.apply(state, unit);
    }
    if (dl != null) {
      dl.call(state);
    }
    SoGL.cc_glglue_glActiveTexture(glue, (int) GL2.GL_TEXTURE0);
  }
}

/*!
  The size returned by this function will just be a very coarse
  estimate as it only uses the more or less obsoleted technique of
  calling glGetIntegerv(GL_MAX_TEXTURE_SIZE).
  
  For a better estimate, use
  SoGLTextureImageElement::isTextureSizeLegal().
  
  Note that this function needs an OpenGL context to be made current
  for it to work. Without that, you will most likely get a faulty
  return value or even a crash.
*/
public int
getMaxGLTextureSize(GL2 gl2)
{
  SoDebugError.postWarning("SoGLMultiTextureImageElement::getMaxGLTextureSize",
                            "This function is obsoleted. It should not "+
                            "be used because its interface is fubar: "+
                            "the maximum texture size handled by "+
                            "the OpenGL driver depends on the context, and "+
                            "this function does not know which context this "+
                            "information is requested for.");

  int[] val = new int[1];
  gl2.glGetIntegerv(GL2.GL_MAX_TEXTURE_SIZE, val,0);
  return (int)val[0];
}

//#undef PRIVATE
public static void set(SoState state, SoNode node,
        SoGLImage image, Model model,
        SbColor blendColor) {
set(state, node, 0, image, model, blendColor);
}

public static SoGLImage get(SoState state, final Model[] model,
               SbColor blendcolor) {
return get(state, 0, model, blendcolor);
}

}
