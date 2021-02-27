/**
 * 
 */
package jscenegraph.coin3d.shaders.inventor.nodes;

import jscenegraph.coin3d.inventor.elements.SoGLMultiTextureMatrixElement;
import jscenegraph.coin3d.inventor.elements.SoTextureUnitElement;
import jscenegraph.coin3d.shaders.SoGLShaderObject;
import jscenegraph.coin3d.shaders.SoShader;
import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.elements.SoModelMatrixElement;
import jscenegraph.database.inventor.elements.SoProjectionMatrixElement;
import jscenegraph.database.inventor.elements.SoViewingMatrixElement;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoSFEnum;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoSubNode;

/**
 * @author Yves Boyadjian
 *
 */
public class SoShaderStateMatrixParameter extends SoUniformShaderParameter {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_HEADER(SoShaderStateMatrixParameter.class,this);
	   
	   public                                                                     
	    static SoType       getClassTypeId()        /* Returns class type id */   
	                                    { return SoSubNode.getClassTypeId(SoShaderStateMatrixParameter.class);  }                   
	  public  SoType      getTypeId()      /* Returns type id      */
	  {
		  return nodeHeader.getClassTypeId();
	  }
	  public                                                                  
	    SoFieldData   getFieldData()  {
		  return nodeHeader.getFieldData();
	  }
	  public  static SoFieldData[] getFieldDataPtr()                              
	        { return SoSubNode.getFieldDataPtr(SoShaderStateMatrixParameter.class); }    	  	
	
	  public enum MatrixType {
		    MODELVIEW(0),
		    PROJECTION(1),
		    TEXTURE(2),
		    MODELVIEW_PROJECTION(3);
		  
		  private int value;
		  
		  MatrixType(int value) {
			  this.value = value;
		  }

			public int getValue() {
				return value;
			}

			public static MatrixType fromValue(Integer value2) {
				return values()[value2];
			}
		  };

		  public enum MatrixTransform{
		    IDENTITY(0),
		    TRANSPOSE(1),
		    INVERSE(2),
		    INVERSE_TRANSPOSE(3);

			  private int value;
			  
			  MatrixTransform(int value) {
				  this.value = value;
			  }
			public int getValue() {
				return value;
			}
			public static MatrixTransform fromValue(Integer value2) {
				return values()[value2];
			}
		  };

		  public final SoSFEnum matrixType = new SoSFEnum();
		  public final SoSFEnum matrixTransform = new SoSFEnum();


		protected
		  // Unlike in other parameter classes, here value is not a field because 
		  // it is updated dynamically from the state.
		  final SbMatrix value = new SbMatrix();
	  
	
public static void initClass()
{
//  SO_NODE_INTERNAL_INIT_CLASS(SoShaderStateMatrixParameter.class,
//                              SO_FROM_COIN_2_5|SO_FROM_INVENTOR_5_0);
    SoSubNode.SO__NODE_INIT_CLASS(SoShaderStateMatrixParameter.class, "ShaderStateMatrixParameter", SoUniformShaderParameter.class);

  SO_ENABLE(SoGLRenderAction.class, SoModelMatrixElement.class);
  SO_ENABLE(SoGLRenderAction.class, SoViewingMatrixElement.class);
  SO_ENABLE(SoGLRenderAction.class, SoProjectionMatrixElement.class);
  SO_ENABLE(SoGLRenderAction.class, SoGLMultiTextureMatrixElement.class);
  SO_ENABLE(SoGLRenderAction.class, SoTextureUnitElement.class);
}

public SoShaderStateMatrixParameter()
{
  nodeHeader.SO_NODE_INTERNAL_CONSTRUCTOR(SoShaderStateMatrixParameter.class);

  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(MatrixType.MODELVIEW);
  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(MatrixType.PROJECTION);
  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(MatrixType.TEXTURE);
  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(MatrixType.MODELVIEW_PROJECTION);

  nodeHeader.SO_NODE_ADD_FIELD(matrixType,"matrixType", (MatrixType.MODELVIEW.getValue()));
  nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(matrixType,"matrixType", "MatrixType");


  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(MatrixTransform.IDENTITY);
  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(MatrixTransform.TRANSPOSE);
  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(MatrixTransform.INVERSE);
  nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(MatrixTransform.INVERSE_TRANSPOSE);

  nodeHeader.SO_NODE_ADD_FIELD(matrixTransform,"matrixTransform", (MatrixTransform.IDENTITY.getValue()));
  nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(matrixTransform,"matrixTransform", "MatrixTransform");

  value.copyFrom(SbMatrix.identity());
}

public void destructor()
{
	super.destructor();
}

// State matrices work differently with CG!
// COMMENT: Because they are only defined in CG (and not in ARB or GLSL)
//          a state matrix uniform delivers the current GL_MODELVIEW,
//          GL_PROJECTION,... matrices, which can be also accessed via
//          glstate.matrix.modelview, glstate.matrix.projection,...
//          since CG 1.2 (or earlier)
//                                           -- 20050126 martin.
//          In ARB or GLSL the matrices are retrieved from the state
//          in the updateValue() method and then set here as uniform
//          variables of type matrix.
//                                           -- 20141129 thomas.
public void updateParameter(SoGLShaderObject shader)
{
  if (this.name.isDefault()) return;

  this.ensureParameter(shader);

  if (shader.shaderType() == SoShader.Type.CG_SHADER) {
//    CGGLenum type;
//    switch (this.matrixType.getValue()) {
//    case MODELVIEW: type = CG_GL_MODELVIEW_MATRIX; break;
//    case PROJECTION: type = CG_GL_PROJECTION_MATRIX; break;
//    case TEXTURE: type = CG_GL_TEXTURE_MATRIX; break;
//    case MODELVIEW_PROJECTION: type = CG_GL_MODELVIEW_PROJECTION_MATRIX; break;
//    default: assert(0 && "illegal shader type"); break;
//    }
//
//    CGGLenum tform;
//    switch (this.matrixTransform.getValue()) {
//    case IDENTITY: tform = CG_GL_MATRIX_IDENTITY; break;
//    case TRANSPOSE: tform = CG_GL_MATRIX_TRANSPOSE; break;
//    case INVERSE: tform = CG_GL_MATRIX_INVERSE; break;
//    case INVERSE_TRANSPOSE: tform = CG_GL_MATRIX_INVERSE_TRANSPOSE; break;
//    default: assert(0 && "illegal matrix transform type"); break;
//    }
//
//    SoGLCgShaderParameter * param = (SoGLCgShaderParameter *)
//      this.getGLShaderParameter(shader.getCacheContext());
//    param.setState(shader, type, tform, this.name.getValue().getString());
  }
  else {
    // if not CG then set the value retrieved from state before
	this.getGLShaderParameter(shader.getCacheContext()).setMatrix(
        shader,
        value.getValueLinear(),
        this.name.getValue()/*.getString()*/,
        this.identifier.getValue());
  }
}

/*!
 * Updates matrix value from state
 */
public void
updateValue(SoState state)
{
  SbMatrix matrix = SbMatrix.identity();
  switch (MatrixType.fromValue(this.matrixType.getValue())) {
    case MODELVIEW: {
      matrix = SoModelMatrixElement.get(state);
      matrix.multRight(SoViewingMatrixElement.get(state));
    } break;
    case PROJECTION: {
      matrix = SoProjectionMatrixElement.get(state); 
    } break;
    case TEXTURE: {
      int unit = SoTextureUnitElement.get(state);
      matrix = SoGLMultiTextureMatrixElement.get(state, unit);
    } break;
    case MODELVIEW_PROJECTION: {
      matrix = SoModelMatrixElement.get(state);
      matrix.multRight(SoViewingMatrixElement.get(state));
      matrix.multRight(SoProjectionMatrixElement.get(state)); 
    } break;
    default: assert(false);// && "illegal matrix type"); break;
  }

  switch (MatrixTransform.fromValue(this.matrixTransform.getValue())) {
  case IDENTITY: value.copyFrom(matrix); break;
  case TRANSPOSE: value.copyFrom(matrix.transpose()); break;
  case INVERSE: value.copyFrom(matrix.inverse()); break;
  case INVERSE_TRANSPOSE: value.copyFrom(matrix.inverse().transpose()); break;
  default: assert(false);// && "illegal matrix transform type"); break;
  }
}

		
		
}
