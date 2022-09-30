/**
 * 
 */
package jscenegraph.coin3d.inventor.elements;

import jscenegraph.opengl.GL2;

import jscenegraph.coin3d.glue.cc_glglue;
import jscenegraph.coin3d.misc.SoGL;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.elements.SoElement;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.port.Ctx;

/**
 * @author Yves Boyadjian
 *
 */
public class SoGLMultiTextureEnabledElement extends SoMultiTextureEnabledElement {

	private int cachecontext;


	// doc from parent
	public void
	init(SoState state)
	{
	  super.init(state);

	  SoAction action = state.getAction();
	  assert(action.isOfType(SoGLRenderAction.getClassTypeId()));
	  this.cachecontext = ((SoGLRenderAction)action).getCacheContext();
	}

	// Documented in superclass. Overridden to track GL state.
	public void push(SoState state)
	{
	  SoGLMultiTextureEnabledElement prev = (SoGLMultiTextureEnabledElement) this.getNextInStack();

	  this.cachecontext = prev.cachecontext;

	  // copy state from previous element
	  super.push(state);
	  // capture previous element since we might or might not change the
	  // GL state in set/pop
	  prev.capture(state);
	}

	// Documented in superclass. Overridden to track GL state.
	public void
	pop(SoState state,
	                                    SoElement prevTopElement)
	{
	  SoGLMultiTextureEnabledElement prev = (SoGLMultiTextureEnabledElement) prevTopElement;
	  int maxunits = Math.max(this.getMaxUnits(), prev.getMaxUnits());
	  
	  for (int i = 0; i < maxunits; i++) {
	    Mode oldmode = prev.getMode(i);
	    Mode newmode =  this.getMode(i);
	    if (oldmode != newmode) {
	      this.updategl(i, newmode, oldmode);
	    }
	  }
	}

	public void
	setElt( int unit, int value)
	{
	  Mode oldmode = this.getMode(unit);
	  Mode newmode = (Mode) Mode.fromValue(value);

	  if (oldmode != newmode) {
	    super.setElt(unit, value);
	    this.updategl(unit, newmode, oldmode);
	  }
	}

	//
	// updates GL state
	//
	public void
	updategl( int unit)
	{
		GL2 gl2 = Ctx.get(this.cachecontext);
		
	  cc_glglue glue = SoGL.cc_glglue_instance(this.cachecontext);
	  SoGL.cc_glglue_glActiveTexture(glue, /*(GLenum)*/ ((int)(GL2.GL_TEXTURE0) + unit));
	  if (this.isEnabled(unit)) gl2.glEnable(GL2.GL_TEXTURE_2D);
	  else gl2.glDisable(GL2.GL_TEXTURE_2D);
	  SoGL.cc_glglue_glActiveTexture(glue, /*(GLenum)*/ GL2.GL_TEXTURE0);
	}

	public void
	updategl( int unit, Mode newvalue, Mode oldvalue)
	{
	  cc_glglue glue = SoGL.cc_glglue_instance(this.cachecontext);
	  SoGL.cc_glglue_glActiveTexture(glue, /*(GLenum)*/ ((int)(GL2.GL_TEXTURE0) + unit));
	  
	  GL2 gl2 = Ctx.get(this.cachecontext);

	  switch (oldvalue) {
	  case DISABLED:
	    break;
	  case TEXTURE2D:
	    gl2.glDisable(GL2.GL_TEXTURE_2D);
	    break;
	  case RECTANGLE:
	    gl2.glDisable(GL2.GL_TEXTURE_RECTANGLE/*_EXT*/);
	    break;
	  case CUBEMAP:
	    gl2.glDisable(GL2.GL_TEXTURE_CUBE_MAP);
	    break;
	  case TEXTURE3D:
	    gl2.glDisable(GL2.GL_TEXTURE_3D);
	    break;
	  default:
	    assert(false);// && "should not happen");
	    break;
	  }
	  switch (newvalue) {
	  case DISABLED:
	    break;
	  case TEXTURE2D:
	    gl2.glEnable(GL2.GL_TEXTURE_2D);
	    break;
	  case RECTANGLE:
	    gl2.glEnable(GL2.GL_TEXTURE_RECTANGLE/*_EXT*/);
	    break;
	  case CUBEMAP:
	    gl2.glEnable(GL2.GL_TEXTURE_CUBE_MAP);
	    break;
	  case TEXTURE3D:
	    gl2.glEnable(GL2.GL_TEXTURE_3D);
	    break;
	  default:
	    assert(false);// && "should not happen");
	    break;
	  }
	  gl2.glGetError(); // YB
	  gl2.glGetError();
	  
	  SoGL.cc_glglue_glActiveTexture(glue, /*(GLenum)*/ GL2.GL_TEXTURE0);

	}

}
