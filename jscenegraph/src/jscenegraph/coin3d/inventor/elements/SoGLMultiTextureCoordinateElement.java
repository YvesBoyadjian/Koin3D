/**
 * 
 */
package jscenegraph.coin3d.inventor.elements;

import jscenegraph.opengl.GL2;

import jscenegraph.coin3d.glue.cc_glglue;
import jscenegraph.coin3d.misc.SoGL;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.elements.SoElement;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.port.Mutable;

/**
 * @author Yves Boyadjian
 *
 */
public class SoGLMultiTextureCoordinateElement extends SoMultiTextureCoordinateElement {
	
	public interface SoTexCoordTexgenCB {
		void run(Object data);
	}
	
	public static class GLUnitData implements Mutable {
		public SoTexCoordTexgenCB texgenCB; //ptr
		public Object texgenData; // ptr
		@Override
		public void copyFrom(Object other) {
			texgenCB = ((SoGLMultiTextureCoordinateElement.GLUnitData)other).texgenCB;
			texgenData = ((SoGLMultiTextureCoordinateElement.GLUnitData)other).texgenData;
		}
	}
	
	SoGLMultiTextureCoordinateElementP pimpl;

	private boolean[] multienabled;
	private int multimax;
	
	  // Coin-3 support
	  public void send( int index) {
	    for (int i = 0; i <= this.multimax; i++) {
	      if (this.multienabled[i]) {
	        this.send(i, index);
	      }
	    }
	  }
	  public void send(int index, final SbVec3f c, final SbVec3f n) {
	    for (int i = 0; i <= this.multimax; i++) {
	      if (this.multienabled[i]) {
	        this.send(i, index, c, n);
	      }
	    }
	  }
	  public static  void setTexGen(SoState state, SoNode node,
	                         SoTexCoordTexgenCB texgenFunc,
	                         Object texgenData /*= NULL*/,
	                         SoTextureCoordinateFunctionCB func /*= NULL*/,
	                         Object funcData /*= NULL*/) {
	    setTexGen(state, node, 0, texgenFunc, texgenData, func, funcData);
	  }

/*!
  The constructor.
*/
public SoGLMultiTextureCoordinateElement()
{
  pimpl = new SoGLMultiTextureCoordinateElementP();

//  this.setTypeId(SoGLMultiTextureCoordinateElement::classTypeId);
//  this.setStackIndex(SoGLMultiTextureCoordinateElement::classStackIndex);
}

/*!
  The destructor.
*/

public void destructor()
{
  pimpl.destructor();
  super.destructor();
}

//!  FIXME: write doc.

public void
init(SoState state)
{
  SoAction action = state.getAction();
  assert(action.isOfType(SoGLRenderAction.getClassTypeId()));
  // fetch cache context id from action since SoGLCacheContextElement
  // might not be initialized yet.
  SoGLRenderAction glaction = (SoGLRenderAction) action;
  this.pimpl.contextid = glaction.getCacheContext();

  super.init(state);
  this.pimpl.unitdata.truncate(0);
  this.pimpl.sendlookup.truncate(0);
}

//!  FIXME: write doc.

public void
push(SoState state)
{
  super.push(state);
  SoGLMultiTextureCoordinateElement prev = (SoGLMultiTextureCoordinateElement)this.getNextInStack();
  
  this.pimpl.contextid = prev.pimpl.contextid;
  this.pimpl.unitdata.copyFrom( prev.pimpl.unitdata);
  
  prev.capture(state);
}

//!  FIXME: write doc.

public void
pop(SoState state,
                                       SoElement prevTopElement)
{
  super.pop(state, prevTopElement);
  SoGLMultiTextureCoordinateElement prev = (SoGLMultiTextureCoordinateElement) prevTopElement;

  cc_glglue glue = SoGL.cc_glglue_instance(this.pimpl.contextid);
  int maxunits = Math.max(this.pimpl.unitdata.getLength(), 
                             prev.pimpl.unitdata.getLength());
  
  for (int i = 0; i < maxunits; i++) {
    GLUnitData thisud = 
      i < this.pimpl.unitdata.getLength() ? 
      this.pimpl.unitdata.operator_square_bracket(i) : this.pimpl.defaultdata;
    GLUnitData prevud = 
      i < prev.pimpl.unitdata.getLength() ?
      prev.pimpl.unitdata.operator_square_bracket(i) : prev.pimpl.defaultdata;
    
    boolean enablegen = false;
    boolean disablegen = false;
    boolean docallback = false;

    if (thisud.texgenCB != null && prevud.texgenCB == null) {enablegen = true; docallback = true;}
    else if (thisud.texgenCB == null && prevud.texgenCB != null) disablegen = true;
    else if (thisud.texgenCB != null/* != prevud.texgenCB*/) docallback = true;

/*
  See the comments in the setElt function below for the explanation for commenting
  out the second half of the above else if statement. RHW
*/

	if (enablegen || disablegen || docallback) {
      // must change texture unit while updating OpenGL
      SoGL.cc_glglue_glActiveTexture(glue, /*(GLenum)*/ ((int)(GL2.GL_TEXTURE0) + i));
    }
	
	GL2 gl2 = glue.getGL2();
	
    if (enablegen) {
      gl2.glEnable(GL2.GL_TEXTURE_GEN_S);
      gl2.glEnable(GL2.GL_TEXTURE_GEN_T);
      gl2.glEnable(GL2.GL_TEXTURE_GEN_R);
      gl2.glEnable(GL2.GL_TEXTURE_GEN_Q);
    }
    if (disablegen) {
    	gl2.glDisable(GL2.GL_TEXTURE_GEN_S);
    	gl2.glDisable(GL2.GL_TEXTURE_GEN_T);
    	gl2.glDisable(GL2.GL_TEXTURE_GEN_R);
    	gl2.glDisable(GL2.GL_TEXTURE_GEN_Q);
    }
    if (docallback) {
      this.doCallback(i);
    }
    // restore default unit
    if (enablegen || disablegen || docallback) {
      SoGL.cc_glglue_glActiveTexture(glue, /*(GLenum)*/ GL2.GL_TEXTURE0);
    }
  }
}

//!  FIXME: write doc.

public static void
setTexGen(SoState state,
                                             SoNode node,
                                             int unit,
                                             SoTexCoordTexgenCB texgenFunc) {
	setTexGen(state,node,unit,texgenFunc,null,null,null);
}
public static void
setTexGen(SoState state,
                                             SoNode node,
                                             int unit,
                                             SoTexCoordTexgenCB texgenFunc,
                                             Object texgenData,
                                             SoTextureCoordinateFunctionCB func,
                                             Object funcData)
{
  SoMultiTextureCoordinateElement.setFunction(state, node, unit, func, funcData);

  SoGLMultiTextureCoordinateElement element = (SoGLMultiTextureCoordinateElement )
    SoElement.getElement(state, classStackIndexMap.get(SoGLMultiTextureCoordinateElement.class));
  if (element != null) {
    element.setElt(unit, texgenFunc, texgenData);
  }
}

//!  FIXME: write doc.

public SoMultiTextureCoordinateElement.CoordType
getType( int unit) 
{
  if (unit < this.pimpl.unitdata.getLength()) {
    if (this.pimpl.unitdata.operator_square_bracket(unit).texgenCB != null) return SoMultiTextureCoordinateElement.CoordType.NONE_TEXGEN;
  }
  return super.getType(unit);
}

//!  FIXME: write doc.

public static SoGLMultiTextureCoordinateElement 
getInstance(SoState state)
{
  return (SoGLMultiTextureCoordinateElement)
    SoElement.getConstElement(state, classStackIndexMap.get(SoGLMultiTextureCoordinateElement.class));
}

//!  FIXME: write doc.

public void
send( int unit, int index)
{
  UnitData ud = this.getUnitData(unit);
  int glunit = /*(GLenum)*/ ((int)(GL2.GL_TEXTURE0) + unit);
  cc_glglue glue = this.pimpl.glue;

  assert(unit < this.pimpl.sendlookup.getLength());
  switch (this.pimpl.sendlookup.operator_square_bracket(unit)) {
  case UNINITIALIZED:
    assert(false);// && "should not happen");
    break;
  case NONE:
    break;
  case FUNCTION:
    assert(false);// && "should not happen");
    break;
  case TEXCOORD2:
    assert(index < ud.numCoords);
    SoGL.cc_glglue_glMultiTexCoord2fv(glue, glunit, ud.coords2.getFast(index).getValueRead());
    break;
  case TEXCOORD3:
    SoGL.cc_glglue_glMultiTexCoord3fv(glue, glunit, ud.coords3.getFast(index).getValueRead());
    break;
  case TEXCOORD4:
    SoGL.cc_glglue_glMultiTexCoord4fv(glue, glunit, ud.coords4.get(index).getValueRead());
    break;
  default:
    assert(false);// && "should not happen");
    break;
  }
}

//!  FIXME: write doc.

public void
send( int unit,
                                         int index,
                                         SbVec3f c,
                                         SbVec3f n) 
{
  UnitData ud = this.getUnitData(unit);
  int glunit =  ((int)(GL2.GL_TEXTURE0) + unit);
  cc_glglue glue = this.pimpl.glue;
  
  assert(unit < this.pimpl.sendlookup.getLength());
  switch (this.pimpl.sendlookup.operator_square_bracket(unit)) {
  case NONE:
    break;
  case FUNCTION:
    assert(ud.funcCB != null);
    SoGL.cc_glglue_glMultiTexCoord4fv(glue, glunit,
                                 ud.funcCB.apply(ud.funcCBData, c, n).toFloatGL());

    break;
  case TEXCOORD2:
    SoGL.cc_glglue_glMultiTexCoord2fv(glue, glunit, ud.coords2.getFast(index).getValueRead());
    break;
  case TEXCOORD3:
    SoGL.cc_glglue_glMultiTexCoord3fv(glue, glunit, ud.coords3.getFast(index).getValueRead());
    break;
  case TEXCOORD4:
    SoGL.cc_glglue_glMultiTexCoord4fv(glue, glunit, ud.coords4.get(index).getValueRead());
    break;
  default:
    assert(false);// && "should not happen");
    break;
  }
}

//!  FIXME: write doc.

public void
setElt( int unit,
                                          SoTexCoordTexgenCB func,
                                          Object data)
{
  this.pimpl.ensureCapacity(unit);
  GLUnitData ud = this.pimpl.unitdata.operator_square_bracket(unit);
  
  boolean enablegen = false;
  boolean disablegen = false;
  boolean docallback = false;

  if (func != null && ud.texgenCB == null) {enablegen = true; docallback = true;}
  else if (func == null && ud.texgenCB != null) disablegen = true;
  else if (func != null /* && func != ud.texgenCB */) docallback = true;

  /*
  The last part of the above if else statement was modified because example 7.3 from The Inventor
  Mentor was not being correctly reproduced. However, the above solution causes a reduction in
  execution efficiency.  So...

  FIXME: Consider whether the caching mechanism can be used to overcome this problem.

  RHW 20141007
  */

  if (func != null) {
    // update SoMultiTextureCoordinateElement type
    this.getUnitData(unit).whatKind = SoMultiTextureCoordinateElement.CoordType.FUNCTION;
  }
  ud.texgenCB = func;
  ud.texgenData = data;

  cc_glglue glue = SoGL.cc_glglue_instance(this.pimpl.contextid);

  if (enablegen || disablegen || docallback) {
    SoGL.cc_glglue_glActiveTexture(glue, (int) ((int)(GL2.GL_TEXTURE0) + unit));
  }
  
  GL2 gl2 = glue.getGL2();

  if (enablegen) {
    gl2.glEnable(GL2.GL_TEXTURE_GEN_S);
    gl2.glEnable(GL2.GL_TEXTURE_GEN_T);
    gl2.glEnable(GL2.GL_TEXTURE_GEN_R);
    gl2.glEnable(GL2.GL_TEXTURE_GEN_Q);
  }
  if (disablegen) {
	  gl2.glDisable(GL2.GL_TEXTURE_GEN_S);
	  gl2.glDisable(GL2.GL_TEXTURE_GEN_T);
	  gl2.glDisable(GL2.GL_TEXTURE_GEN_R);
	  gl2.glDisable(GL2.GL_TEXTURE_GEN_Q);
  }
  if (docallback) this.doCallback(unit);

  if (enablegen || disablegen || docallback) {
    SoGL.cc_glglue_glActiveTexture(glue, (int) GL2.GL_TEXTURE0);
  }
}

public void
doCallback( int unit)
{
  if (this.pimpl.unitdata.operator_square_bracket(unit).texgenCB != null) {
    this.pimpl.unitdata.operator_square_bracket(unit).texgenCB.run(this.pimpl.unitdata.operator_square_bracket(unit).texgenData);
  }
}

/*!
  Internal method that is called from SoGLTextureCoordinateBundle to
  set up optimized rendering.
*/
public void initRender( boolean[] enabled, int maxenabled)
{
  this.pimpl.glue = SoGL.cc_glglue_instance(this.pimpl.contextid);
  this.pimpl.sendlookup.truncate(0);
  for (int i = 0; i <= maxenabled; i++) {
    this.pimpl.sendlookup.append(SoGLMultiTextureCoordinateElementP.SendLookup.NONE);
    // init the sendloopup variable
    if (enabled[i]) {
      UnitData ud = this.getUnitData(i);
      switch (ud.whatKind) {
      case DEFAULT:
        assert(false);// && "should not happen");
        break;
      case FUNCTION:
        if (ud.funcCB != null) {
          this.pimpl.sendlookup.operator_square_bracket(i, SoGLMultiTextureCoordinateElementP.SendLookup.FUNCTION);
        }
        break;
      case NONE_TEXGEN:
        break;
      case EXPLICIT:
        {
          switch (ud.coordsDimension) {
          case 2:
            this.pimpl.sendlookup.operator_square_bracket(i, SoGLMultiTextureCoordinateElementP.SendLookup.TEXCOORD2);
            break;
          case 3:
            this.pimpl.sendlookup.operator_square_bracket(i, SoGLMultiTextureCoordinateElementP.SendLookup.TEXCOORD3);
            break;
          case 4:
            this.pimpl.sendlookup.operator_square_bracket(i, SoGLMultiTextureCoordinateElementP.SendLookup.TEXCOORD4);
            break;
          default:
            assert(false);// && "should not happen");
            break;
          }
        }
        break;
      default:
        assert(false);// && "should not happen");
        break;
      }
    }
  }
}

/*!
  Called from SoTextureCoordinateBundle to initialize multi texturing.

  \internal
*/
public void initMulti(SoState state)
{
	final int[] dummy = new int[1];
  this.multienabled = SoMultiTextureEnabledElement.getEnabledUnits(state,
                                                                     /*this.multimax*/dummy);
  this.multimax = dummy[0];
  this.initRender(this.multienabled, this.multimax);
}


//#undef PRIVATE
	
}
