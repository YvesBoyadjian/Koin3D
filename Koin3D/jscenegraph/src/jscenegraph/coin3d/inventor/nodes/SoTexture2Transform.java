/**
 * 
 */
package jscenegraph.coin3d.inventor.nodes;

import jscenegraph.coin3d.glue.cc_glglue;
import jscenegraph.coin3d.inventor.elements.SoGLMultiTextureMatrixElement;
import jscenegraph.coin3d.inventor.elements.SoMultiTextureMatrixElement;
import jscenegraph.coin3d.inventor.elements.SoTextureUnitElement;
import jscenegraph.coin3d.misc.SoGL;
import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoGetMatrixAction;
import jscenegraph.database.inventor.actions.SoPickAction;
import jscenegraph.database.inventor.elements.SoGLCacheContextElement;
import jscenegraph.database.inventor.elements.SoShapeStyleElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFFloat;
import jscenegraph.database.inventor.fields.SoSFVec2f;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoTexture2Transform extends SoNode {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoTexture2Transform.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoTexture2Transform.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoTexture2Transform.class); }    	  	
	
 //! Translation in S and T.
 public final SoSFVec2f           translation = new SoSFVec2f();    

 //! Counter-clockwise rotation of the coordinate space, in radians. This
 //! results in a clockwise rotation of the texture on the object.
 public final SoSFFloat           rotation = new SoSFFloat();       

 //! Scaling factors in S and T.
 public final SoSFVec2f           scaleFactor = new SoSFVec2f();    

 //! Center point used for scaling and rotation.
 public final SoSFVec2f           center = new SoSFVec2f();         

////////////////////////////////////////////////////////////////////////
//
//Description:
//Constructor
//
//Use: public

public SoTexture2Transform()
//
////////////////////////////////////////////////////////////////////////
{
nodeHeader.SO_NODE_CONSTRUCTOR(/*SoTexture2Transform*/);
nodeHeader.SO_NODE_ADD_SFIELD(translation,"translation", (new SbVec2f(0.0f, 0.0f)));
nodeHeader.SO_NODE_ADD_SFIELD(rotation,"rotation",    (0.0f));
nodeHeader.SO_NODE_ADD_SFIELD(scaleFactor,"scaleFactor", (new SbVec2f(1.0f, 1.0f)));
nodeHeader.SO_NODE_ADD_SFIELD(center,"center",      (new SbVec2f(0.0f, 0.0f)));
isBuiltIn = true;
}

////////////////////////////////////////////////////////////////////////
//
//Description:
//Destructor.
//
//Use: private

public void destructor()
//
////////////////////////////////////////////////////////////////////////
{
super.destructor();
}


//Documented in superclass.
public void
GLRender(SoGLRenderAction action)
{
SoState state = action.getState();

// don't modify the texture matrix while rendering the shadow map
if ((SoShapeStyleElement.get(state).getFlags() & SoShapeStyleElement.Flags.SHADOWMAP.getValue())!=0) return;

int unit = SoTextureUnitElement.get(state);

cc_glglue glue =
 SoGL.cc_glglue_instance(SoGLCacheContextElement.get(state));
int maxunits = SoGL.cc_glglue_max_texture_units(glue);

if (unit < maxunits) {
 final SbMatrix mat = new SbMatrix();
 this.makeMatrix(mat);
 SoMultiTextureMatrixElement.mult(state, this, unit, mat);
}
else {
 // we already warned in SoTextureUnit. I think it's best to just
 // ignore the texture here so that all textures for non-supported
 // units will be ignored. pederb, 2003-11-10
}
}

//Documented in superclass.
public void
SoTexture2Transform_doAction(SoAction action)
{
final SbMatrix mat = new SbMatrix();
this.makeMatrix(mat);
SoState state = action.getState();
int unit = SoTextureUnitElement.get(state);
SoMultiTextureMatrixElement.mult(state, this, unit, mat);
}

//Documented in superclass.
public void callback(SoCallbackAction action)
{
SoTexture2Transform_doAction(action);
}

//Documented in superclass.
public void
getMatrix(SoGetMatrixAction action)
{
int unit = SoTextureUnitElement.get(action.getState()); 
if (unit == 0) {
 final SbMatrix mat = new SbMatrix();
 this.makeMatrix(mat);
 action.getTextureMatrix().multLeft(mat);
 action.getTextureInverse().multRight(mat.inverse());
}
}

//Documented in superclass.
public void pick(SoPickAction action)
{
SoTexture2Transform_doAction(action);
}

//
//generate a matrix based on the fields
//
public void
makeMatrix(final SbMatrix mat)
{
final SbMatrix tmp = new SbMatrix();
SbVec2f c = this.center.isIgnored() ?
 new SbVec2f(0.0f, 0.0f) :
 new SbVec2f(center.getValue());

mat.makeIdentity();
mat.getValue()[3][0] = -c.getValueRead()[0];
mat.getValue()[3][1] = -c.getValueRead()[1];

SbVec2f scale = this.scaleFactor.getValue();
if (!this.scaleFactor.isIgnored() &&
   scale.operator_not_equal(new SbVec2f(1.0f, 1.0f))) {
 tmp.makeIdentity();
 tmp.getValue()[0][0] = scale.getValueRead()[0];
 tmp.getValue()[1][1] = scale.getValueRead()[1];
 mat.multRight(tmp);
}
if (!this.rotation.isIgnored() && (this.rotation.getValue() != 0.0f)) {
 float cosa = (float)Math.cos(this.rotation.getValue());
 float sina = (float)Math.sin(this.rotation.getValue());
 tmp.makeIdentity();
 tmp.getValue()[0][0] = cosa;
 tmp.getValue()[1][0] = -sina;
 tmp.getValue()[0][1] = sina;
 tmp.getValue()[1][1] = cosa;
 mat.multRight(tmp);
}
if (!translation.isIgnored()) c.operator_add_equal(this.translation.getValue());
if (c.operator_not_equal(new SbVec2f(0.0f, 0.0f))) {
 tmp.makeIdentity();
 tmp.getValue()[3][0] = c.getValueRead()[0];
 tmp.getValue()[3][1] = c.getValueRead()[1];
 mat.multRight(tmp);
}
}

 
////////////////////////////////////////////////////////////////////////
//
//Description:
//This initializes the SoTexture2Transform class.
//
//Use: internal

public static void initClass()
//
////////////////////////////////////////////////////////////////////////
{
SO__NODE_INIT_CLASS(SoTexture2Transform.class, "Texture2Transform", SoNode.class);

SO_ENABLE(SoCallbackAction.class,         SoMultiTextureMatrixElement.class);
SO_ENABLE(SoPickAction.class,             SoMultiTextureMatrixElement.class);
SO_ENABLE(SoGLRenderAction.class,         SoGLMultiTextureMatrixElement.class);
}


}
