/**
 * 
 */
package jscenegraph.coin3d.inventor.nodes;

import jscenegraph.coin3d.glue.cc_glglue;
import jscenegraph.coin3d.inventor.elements.SoTextureCombineElement;
import jscenegraph.coin3d.inventor.elements.SoTextureUnitElement;
import jscenegraph.coin3d.inventor.misc.SoGLDriverDatabase;
import jscenegraph.coin3d.misc.SoGL;
import jscenegraph.database.inventor.SbColor4f;
import jscenegraph.database.inventor.SbVec4f;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoPickAction;
import jscenegraph.database.inventor.elements.SoGLCacheContextElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFEnum;
import jscenegraph.database.inventor.fields.SoSFEnum;
import jscenegraph.database.inventor.fields.SoSFFloat;
import jscenegraph.database.inventor.fields.SoSFVec4f;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoTextureCombine extends SoNode {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoTextureCombine.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoTextureCombine.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoTextureCombine.class); }    	  	
	
	  public enum Source {
		    PRIMARY_COLOR ( SoTextureCombineElement.Source.PRIMARY_COLOR.getValue()),
		    TEXTURE ( SoTextureCombineElement.Source.TEXTURE.getValue()),
		    CONSTANT ( SoTextureCombineElement.Source.CONSTANT.getValue()),
		    PREVIOUS ( SoTextureCombineElement.Source.PREVIOUS.getValue());

		    Source(int value) {
		    	this.value = value;
		    }
		    private int value;

			public int getValue() {
				return value;
			}
		  };
		  public enum Operand {
		    SRC_COLOR ( SoTextureCombineElement.Operand.SRC_COLOR.getValue()),
		    ONE_MINUS_SRC_COLOR ( SoTextureCombineElement.Operand.ONE_MINUS_SRC_COLOR.getValue()),
		    SRC_ALPHA ( SoTextureCombineElement.Operand.SRC_ALPHA.getValue()),
		    ONE_MINUS_SRC_ALPHA ( SoTextureCombineElement.Operand.ONE_MINUS_SRC_ALPHA.getValue());
		    
		    Operand(int value) {
		    	this.value = value;
		    }
		    
		    private int value;

			public int getValue() {
				return value;
			}
		  };
		  public enum Operation {
		    REPLACE ( SoTextureCombineElement.Operation.REPLACE.getValue()),
		    MODULATE ( SoTextureCombineElement.Operation.MODULATE.getValue()),
		    ADD ( SoTextureCombineElement.Operation.ADD.getValue()),
		    ADD_SIGNED ( SoTextureCombineElement.Operation.ADD_SIGNED.getValue()),
		    SUBTRACT ( SoTextureCombineElement.Operation.SUBTRACT.getValue()),
		    INTERPOLATE ( SoTextureCombineElement.Operation.INTERPOLATE.getValue()),
		    DOT3_RGB ( SoTextureCombineElement.Operation.DOT3_RGB.getValue()),
		    DOT3_RGBA ( SoTextureCombineElement.Operation.DOT3_RGBA.getValue());
		    
		    Operation(int value) {
		    	this.value = value;
		    }
		    
		    private int value;

			public int getValue() {
				return value;
			}

			public static Operation fromValue(Integer value2) {
				for(Operation o: values()) {
					if(o.getValue() == (int)value2) {
						return o;
					}
				}
				return null;
			}
		  };
		  
		  public final SoMFEnum rgbSource = new SoMFEnum();
		  public final SoMFEnum alphaSource = new SoMFEnum();

		  public final SoMFEnum rgbOperand = new SoMFEnum();
		  public final SoMFEnum alphaOperand = new SoMFEnum();

		  public final SoSFEnum rgbOperation = new SoSFEnum();
		  public final SoSFEnum alphaOperation = new SoSFEnum();
		  
		  public final SoSFFloat rgbScale = new SoSFFloat();
		  public final SoSFFloat alphaScale = new SoSFFloat();

		  public final SoSFVec4f constantColor = new SoSFVec4f();


/*!
  Constructor.
*/
public SoTextureCombine()
{
  nodeHeader.SO_NODE_INTERNAL_CONSTRUCTOR(SoTextureCombine.class);

  nodeHeader.SO_NODE_ADD_MFIELD(rgbSource,"rgbSource", (Source.TEXTURE));
  nodeHeader.SO_NODE_ADD_MFIELD(alphaSource,"alphaSource", (Source.TEXTURE));
  nodeHeader.SO_NODE_ADD_MFIELD(rgbOperand,"rgbOperand", (Operand.SRC_COLOR));
  nodeHeader.SO_NODE_ADD_MFIELD(alphaOperand,"alphaOperand", (Operand.SRC_ALPHA));

  this.rgbSource.setNum(0);
  this.rgbSource.setDefault(true);
  this.alphaSource.setNum(0);
  this.alphaSource.setDefault(true);
  this.rgbOperand.setNum(0);
  this.rgbOperand.setDefault(true);
  this.alphaOperand.setNum(0);
  this.alphaOperand.setDefault(true);

  nodeHeader.SO_NODE_ADD_FIELD(rgbOperation,"rgbOperation", (Operation.MODULATE.getValue()));
  nodeHeader.SO_NODE_ADD_FIELD(alphaOperation,"alphaOperation", (Operation.MODULATE.getValue()));

  nodeHeader.SO_NODE_ADD_FIELD(rgbScale,"rgbScale", (1.0f));
  nodeHeader.SO_NODE_ADD_FIELD(alphaScale,"alphaScale", (1.0f));

  nodeHeader.SO_NODE_ADD_FIELD(constantColor,"constantColor", new SbVec4f(1.0f, 1.0f, 1.0f, 1.0f));

  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Source.PRIMARY_COLOR);
  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Source.TEXTURE);
  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Source.CONSTANT);
  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Source.PREVIOUS);

  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Operand.SRC_COLOR);
  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Operand.ONE_MINUS_SRC_COLOR);
  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Operand.SRC_ALPHA);
  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Operand.ONE_MINUS_SRC_ALPHA);

  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Operation.REPLACE);
  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Operation.MODULATE);
  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Operation.ADD);
  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Operation.ADD_SIGNED);
  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Operation.SUBTRACT);
  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Operation.INTERPOLATE);
  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Operation.DOT3_RGB);
  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(Operation.DOT3_RGBA);

  nodeHeader.SO_NODE_SET_MF_ENUM_TYPE(rgbSource, "rgbSource","Source");
  nodeHeader.SO_NODE_SET_MF_ENUM_TYPE(alphaSource,"alphaSource", "Source");
  nodeHeader.SO_NODE_SET_MF_ENUM_TYPE(rgbOperand,"rgbOperand", "Operand");
  nodeHeader.SO_NODE_SET_MF_ENUM_TYPE(alphaOperand,"alphaOperand", "Operand");

  nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(rgbOperation,"rgbOperation", "Operation");
  nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(alphaOperation,"alphaOperation", "Operation");
}


/*!
  Destructor.
*/
public void destructor()
{
	super.destructor();
}

// Doc from superclass.
public static void
initClass()
{
  //SO_NODE_INTERNAL_INIT_CLASS(SoTextureCombine, SO_FROM_COIN_2_2);
	SoSubNode.SO__NODE_INIT_CLASS(SoTextureCombine.class, "TextureCombine", SoNode.class);

  SO_ENABLE(SoGLRenderAction.class, SoTextureCombineElement.class);
}

static int didwarn = 0;

// Doc from superclass.
public void
GLRender(SoGLRenderAction action)
{
  cc_glglue glue = SoGL.cc_glglue_instance(action.getCacheContext());

  SoTextureCombine.Operation rgbaop =
    SoTextureCombine.Operation.fromValue( this.rgbOperation.getValue());

  SoTextureCombine.Operation alphaop =
    SoTextureCombine.Operation.fromValue( this.alphaOperation.getValue());

  boolean supported = SoGL.cc_glglue_glversion_matches_at_least(glue, 1, 3, 0);

  if (!supported) {
    supported = SoGLDriverDatabase.isSupported(glue, "GL_ARB_texture_env_combine");
    if (supported && (alphaop == Operation.DOT3_RGB || alphaop == Operation.DOT3_RGBA ||
                      rgbaop == Operation.DOT3_RGB || rgbaop == Operation.DOT3_RGBA)) {
      supported =
        SoGLDriverDatabase.isSupported(glue, "GL_ARB_texture_env_dot3");
    }
  }
  
  if (supported) {
    SoTextureCombine_doAction((SoAction)action);
  }
  else {
    if (didwarn==0) {
      SoDebugError.postWarning("SoTextureCombine.GLRender",
                                "Your OpenGL driver does not support the "+
                                "required extensions to do texture combine.");
      didwarn = 1;
    }

  }
}

// Doc from superclass.
public void
SoTextureCombine_doAction(SoAction action)
{
  SoState state = action.getState();
  int unit = SoTextureUnitElement.get(state);

  SoTextureCombineElement.Source rgbsource[] = {
    SoTextureCombineElement.Source.TEXTURE,
    SoTextureCombineElement.Source.PREVIOUS,
    SoTextureCombineElement.Source.CONSTANT
  };
  SoTextureCombineElement.Operand rgboperand[] = {
    SoTextureCombineElement.Operand.SRC_COLOR,
    SoTextureCombineElement.Operand.SRC_COLOR,
    SoTextureCombineElement.Operand.SRC_COLOR
  };

  SoTextureCombineElement.Source alphasource[] = {
    SoTextureCombineElement.Source.TEXTURE,
    SoTextureCombineElement.Source.PREVIOUS,
    SoTextureCombineElement.Source.CONSTANT
  };
  SoTextureCombineElement.Operand alphaoperand[] = {
    SoTextureCombineElement.Operand.SRC_ALPHA,
    SoTextureCombineElement.Operand.SRC_ALPHA,
    SoTextureCombineElement.Operand.SRC_ALPHA
  };
  int i;
  for (i = 0; i < this.rgbSource.getNum() && i < 3; i++) {
    rgbsource[i] = SoTextureCombineElement.Source.fromValue( this.rgbSource.getValues(0).get(i));
  }
  for (i = 0; i < this.alphaSource.getNum() && i < 3; i++) {
    alphasource[i] = SoTextureCombineElement.Source.fromValue(this.alphaSource.getValues(0).get(i));
  }
  for (i = 0; i < this.rgbOperand.getNum() && i < 3; i++) {
    rgboperand[i] = SoTextureCombineElement.Operand.fromValue( this.rgbOperand.getValues(0).get(i));
  }
  for (i = 0; i < this.alphaOperand.getNum() && i < 3; i++) {
    alphaoperand[i] = SoTextureCombineElement.Operand.fromValue( this.alphaOperand.getValues(0).get(i));
  }

  final SbColor4f col = new SbColor4f();
  SbVec4f tmp = new SbVec4f(this.constantColor.getValue());
  col.getRef()[0].accept(tmp.getValueRead()[0]);
  col.getRef()[1].accept(tmp.getValueRead()[1]);
  col.getRef()[2].accept(tmp.getValueRead()[2]);
  col.getRef()[3].accept(tmp.getValueRead()[3]);


  cc_glglue glue = 
    SoGL.cc_glglue_instance(SoGLCacheContextElement.get(state));
  int maxunits = SoGL.cc_glglue_max_texture_units(glue);

  if (unit < maxunits) {
    SoTextureCombineElement.set(state, this, unit,
                                 SoTextureCombineElement.Operation.fromValue( this.rgbOperation.getValue()),
                                 SoTextureCombineElement.Operation.fromValue( this.alphaOperation.getValue()),
                                 rgbsource, alphasource,
                                 rgboperand, alphaoperand,
                                 col,
                                 this.rgbScale.getValue(),
                                 this.alphaScale.getValue());
  }
}


// Doc from superclass.
public void
callback(SoCallbackAction action)
{
  // So far only SoGLRenderAction supports SoTextureCombineElement.  We
  // may never support multiple texture units for SoCallbackAction,
  // but we reimplement the method just in case
  super.callback(action);
}

// Doc from superclass.
public void
pick(SoPickAction action)
{
  // So far only SoGLRenderAction supports SoTextureCombineElement.  We
  // may never support multiple texture units for SoPickAction, but we
  // reimplement the method just in case
  super.pick(action);
}
		  
}
