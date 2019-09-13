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

package jscenegraph.coin3d.inventor.nodes;

import java.util.Collection;
import java.util.HashMap;

import com.jogamp.opengl.GL2;

import jscenegraph.coin3d.glue.cc_glglue;
import jscenegraph.coin3d.inventor.elements.SoMultiTextureImageElement;
import jscenegraph.coin3d.inventor.elements.gl.SoGLMultiTextureImageElement;
import jscenegraph.coin3d.inventor.misc.SoContextHandler;
import jscenegraph.coin3d.inventor.misc.SoGLDriverDatabase;
import jscenegraph.coin3d.misc.SoGL;
import jscenegraph.coin3d.shaders.SoGLARBShaderObject;
import jscenegraph.coin3d.shaders.SoGLCgShaderObject;
import jscenegraph.coin3d.shaders.SoGLSLShaderObject;
import jscenegraph.coin3d.shaders.SoGLShaderObject;
import jscenegraph.coin3d.shaders.SoGLShaderProgram;
import jscenegraph.coin3d.shaders.inventor.elements.SoGLShaderProgramElement;
import jscenegraph.coin3d.shaders.inventor.nodes.SoMFUniformShaderParameter;
import jscenegraph.coin3d.shaders.inventor.nodes.SoShaderStateMatrixParameter;
import jscenegraph.coin3d.shaders.inventor.nodes.SoUniformShaderParameter;
import jscenegraph.database.inventor.SbColor;
import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SbStringList;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.SoType;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoSearchAction;
import jscenegraph.database.inventor.elements.SoGLCacheContextElement;
import jscenegraph.database.inventor.elements.SoLazyElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.fields.SoField;
import jscenegraph.database.inventor.fields.SoFieldData;
import jscenegraph.database.inventor.fields.SoMFNode;
import jscenegraph.database.inventor.fields.SoSFBool;
import jscenegraph.database.inventor.fields.SoSFEnum;
import jscenegraph.database.inventor.fields.SoSFString;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSubNode;
import jscenegraph.database.inventor.sensors.SoNodeSensor;
import jscenegraph.database.inventor.sensors.SoSensor;
import jscenegraph.port.FILE;
import jscenegraph.port.Util;

/*!
  \class SoShaderObject SoShaderObject.h Inventor/nodes/SoShaderObject.h

  The SoShaderObject class is the superclass for all shader classes in Coin.

  See \link coin_shaders Shaders in Coin \endlink for more information
  on how to set up a scene graph with shaders.

  \ingroup shaders

  \sa SoShaderProgram
*/

/*!
  \var SoSFBool SoShaderObject.isActive

  Enabled/disables the shader. Default value is TRUE.
*/

/*!
  \var SoSFString SoShaderObject.sourceProgram

  The shader program, or a file name if the shader should be loaded from a file.
  If the shader is loaded from a file, the shader type is identified by the
  file extension. .glsl for GLSL shaders, .cg for Cg shaders, and .vp and .fp
  for ARB shaders.
*/


/*!
  \var SoSFEnum SoShaderObject.sourceType

  The type of shader.
*/


/*!
  \enum SoShaderObject.SourceType

  Used for enumerating the shader types in sourceProgram.
*/

/*!
  \var SoShaderObject.SourceType SoShaderObject.ARB_PROGRAM

  Specifies an ARB shader.
*/

/*!
  \var SoShaderObject.SourceType SoShaderObject.CG_PROGRAM

  Specifies a Cg shader program.
*/

/*!
  \var SoShaderObject.SourceType SoShaderObject.GLSL_PROGRAM

  Specifies a GLSL program.
*/

/*!
  \var SoShaderObject.SourceType SoShaderObject.FILENAME

  Shader should be loaded from the file in sourceProgram.
*/


/*!
  \var SoMFNode SoShaderObject.parameter

  The shader program parameters.
*/

/**
 * @author Yves Boyadjian
 *
 */
public class SoShaderObject extends SoNode {

	private final SoSubNode nodeHeader = SoSubNode.SO_NODE_ABSTRACT_HEADER(SoShaderObject.class,this);
   	
	public                                                                     
    static SoType       getClassTypeId()        /* Returns class type id */   
                                    { return SoSubNode.getClassTypeId(SoShaderObject.class); }                   
    public SoType      getTypeId()       /* Returns type id      */
    {
		return nodeHeader.getClassTypeId();		    	
    }
  public                                                                  
    SoFieldData   getFieldData() {
	  return nodeHeader.getFieldData(); 
  }
  public  static SoFieldData[] getFieldDataPtr()                              
        { return SoSubNode.getFieldDataPtr(SoShaderObject.class); }              
	
  public enum SourceType {
    ARB_PROGRAM,
    CG_PROGRAM,
    GLSL_PROGRAM,
    FILENAME;
	  
	  public int getValue() {
		  return ordinal();
	  }

	public static SourceType fromValue(int value) {
		switch(value) {
		case 0: return ARB_PROGRAM;
		case 1: return CG_PROGRAM;
		case 2: return GLSL_PROGRAM;
		case 3: return FILENAME;
		default: return null;
		}
	}
  };

  public final SoSFBool isActive = new SoSFBool();
  public final SoSFEnum sourceType = new SoSFEnum();
  public final SoSFString sourceProgram = new SoSFString();
  // FIXED: this field is an SoMFUniformShaderParameter in TGS
  // Inventor. We should also implement that field. 20050125 mortene.
  public final SoMFUniformShaderParameter parameter = new SoMFUniformShaderParameter();
  
  private SoShaderObject owner;
  private SoShaderObject.SourceType cachedSourceType;
  private String cachedSourceProgram;
  private boolean didSetSearchDirectories;
  private boolean shouldload;
  private SoNodeSensor sensor;

  private final SbStringList searchdirectories = new SbStringList();
  final HashMap <Integer, SoGLShaderObject> glshaderobjects = new HashMap<>();  //java port

  public static void initClass()
  //
  ////////////////////////////////////////////////////////////////////////
  {
      SO__NODE_INIT_ABSTRACT_CLASS(SoShaderObject.class, "ShaderObject", SoNode.class);

  }  

/*!
  Constructor.
*/
protected SoShaderObject()
{
	nodeHeader.SO_NODE_CONSTRUCTOR(/*SoShaderObject.class*/);

	nodeHeader.SO_NODE_ADD_FIELD(isActive,"isActive", (true));

	nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(SourceType.ARB_PROGRAM);
	nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(SourceType.CG_PROGRAM);
	nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(SourceType.GLSL_PROGRAM);
	nodeHeader.SO_NODE_DEFINE_ENUM_VALUE(SourceType.FILENAME);

	nodeHeader.SO_NODE_ADD_FIELD(sourceType,"sourceType", (SourceType.FILENAME.getValue()));
	nodeHeader.SO_NODE_SET_SF_ENUM_TYPE(sourceType,"sourceType", "SourceType");

	nodeHeader.SO_NODE_ADD_FIELD(sourceProgram,"sourceProgram", (""));
	nodeHeader.SO_NODE_ADD_MFIELD(parameter,"parameter", (null));
	this.parameter.setNum(0);
	this.parameter.setDefault(true);

  //PRIVATE(this) = new SoShaderObjectP(this);
	  this.owner = /*ownerptr*/this;
	  this.sensor = new SoNodeSensor(SoShaderObject::sensorCB, this);
	  this.sensor.setPriority(0);
	  this.sensor.attach(/*ownerptr*/this);

	  this.cachedSourceType = SoShaderObject.SourceType.FILENAME;
	  this.didSetSearchDirectories = false;
	  this.shouldload = true;

	  SoContextHandler.addContextDestructionCallback(SoShaderObject::context_destruction_cb, this);
}

public void destructor() {
	  SoContextHandler.removeContextDestructionCallback(SoShaderObject::context_destruction_cb, this);

	  this.deleteGLShaderObjects();

	  final SbStringList empty = new SbStringList();
	  this.setSearchDirectories(empty);
	  this.sensor.destructor();
	  
	  parameter.destructor();
	  
	super.destructor();
}

// doc from parent
public void
GLRender(SoGLRenderAction action)
{
	  boolean isactive = this.owner.isActive.getValue();
	  if (!isactive) return;

	  SoState state = action.getState();

	  SoGLShaderProgram shaderProgram = SoGLShaderProgramElement.get(state);
	  if (shaderProgram == null) {
	    SoDebugError.postWarning("SoShaderObject.GLRender",
	                              "SoShaderObject seems to not be under a SoShaderProgram node");
	    return;
	  }

	  final int cachecontext = SoGLCacheContextElement.get(state);
	  final cc_glglue glue = SoGL.cc_glglue_instance(cachecontext);

	  SoGLShaderObject shaderobject = this.getGLShaderObject(cachecontext);

	  if (this.owner.sourceProgram.isDefault() ||
	      this.owner.sourceProgram.getValue().length() == 0) { return; }

	  if (shaderobject == null) {
	    if (this.shouldload) {
	      this.checkType(); // set this.cachedSourceType
	      this.readSource(); // set this.cachedSourceProgram
	      this.shouldload = false;
	    }
	    // if file could not be read
	    if (this.cachedSourceType == SoShaderObject.SourceType.FILENAME) return;

	    if (!this.isSupported(this.cachedSourceType, glue)) {
	      String s = "unknown shader";
	      switch (this.cachedSourceType) {
	      case ARB_PROGRAM: s = "ARB_PROGRAM"; break;
	      case CG_PROGRAM: s = "CG_PROGRAM"; break;
	      case GLSL_PROGRAM: s = "GLSL_PROGRAM"; break;
	      default: assert(false);// && "unknown shader");
	      }
	      SoDebugError.postWarning("SoShaderObjectP.GLRender",
	                                s+" is not supported");
	      return;
	    }

	    switch (this.cachedSourceType) {
	    case ARB_PROGRAM:
	      shaderobject = (SoGLShaderObject )new SoGLARBShaderObject(cachecontext);
	      break;
	    case CG_PROGRAM:
	      shaderobject = (SoGLShaderObject) new SoGLCgShaderObject(cachecontext);
	      break;
	    case GLSL_PROGRAM:
	      shaderobject = (SoGLShaderObject) new SoGLSLShaderObject(cachecontext);
	      break;
	    default:
	      assert(false);// && "This shouldn't happen!");
	    }

	    if (this.owner.isOfType(SoVertexShader.getClassTypeId())) {
	      shaderobject.setShaderType(SoGLShaderObject.ShaderType.VERTEX);
	    }
	    else if (this.owner.isOfType(SoFragmentShader.getClassTypeId())) {
	      shaderobject.setShaderType(SoGLShaderObject.ShaderType.FRAGMENT);
	    }
	    else {
	      assert(this.owner.isOfType(SoGeometryShader.getClassTypeId()));
	      shaderobject.setShaderType(SoGLShaderObject.ShaderType.GEOMETRY);

	      SoGeometryShader geomshader = (SoGeometryShader) this.owner;

	    }

	//#if defined(SOURCE_HINT)
	    shaderobject.sourceHint = getSourceHint();
	//#endif
	    shaderobject.load(this.cachedSourceProgram/*.getString()*/);
	    this.setGLShaderObject(shaderobject, cachecontext);
	  }
	  if (shaderobject != null) {
	    shaderProgram.addShaderObject(shaderobject);
	    shaderobject.setIsActive(isactive);
	  }
}

// doc from parent
public void
search(SoSearchAction action)
{
  // Include this node in the search.
  super.search(action);
  if (action.isFound()) return;

  // we really can't do this since this node hasn't got an SoChildList
  // instance
//#if 0 // disabled, not possible to search under this node
//  int numindices;
//  const int * indices;
//  if (action.getPathCode(numindices, indices) == SoAction.IN_PATH) {
//    // FIXME: not implemented -- 20050129 martin
//  }
//  else { // traverse all shader parameter
//    int num = this.parameter.getNum();
//    for (int i=0; i<num; i++) {
//      SoNode * node = this.parameter[i];
//      action.pushCurPath(i, node);
//      SoNodeProfiling profiling;
//      profiling.preTraversal(action);
//      node.search(action);
//      profiling.postTraversal(action);
//      action.popCurPath();
//      if (action.isFound()) return;
//    }
//  }
//#endif // disabled
}

// doc from parent
public boolean
readInstance(SoInput in, short flags)
{
  /*PRIVATE(this).*/sensor.detach();
  /*PRIVATE(this).*/deleteGLShaderObjects();

  boolean ret = /*inherited.*/super.readInstance(in, flags);
  if (ret) {
    /*PRIVATE(this).*/setSearchDirectories(SoInput.getDirectories());
  }
  /*PRIVATE(this).*/sensor.attach(this);

  return ret;
}

/*!
  Returns the shader type detected in sourceProgram.
*/
public SoShaderObject.SourceType
getSourceType() 
{
  return /*PRIVATE(this).*/cachedSourceType;
}

/*!
  Returns the actual shader program.
*/
public String getSourceProgram()
{
  return /*PRIVATE(this).*/cachedSourceProgram;
}

/*!
  Used internally to update shader paramters.
*/
public void
updateParameters(SoState state)
{
  int cachecontext = SoGLCacheContextElement.get(state);
  /*PRIVATE(this).*/updateAllParameters(cachecontext);
  /*PRIVATE(this).*/updateStateMatrixParameters(cachecontext, state);
  /*PRIVATE(this).*/updateCoinParameters(cachecontext, state);
}

// sets this.cachedSourceType to [ARB|CG|GLSL]_PROGRAM
// this.cachedSourceType will be set to FILENAME, if sourceType is unknown
private void
checkType()
{
  this.cachedSourceType =
    SoShaderObject.SourceType.fromValue(this.owner.sourceType.getValue());

  if (this.cachedSourceType != SoShaderObject.SourceType.FILENAME) return;

  // determine sourceType from file extension
  String fileName = this.owner.sourceProgram.getValue();
  int len = fileName.length();
  if (len > 5) {
    String subStr = fileName.substring(len-5);
    if (subStr.equals(".glsl") || subStr.equals(".vert") || subStr.equals(".frag")) {
      this.cachedSourceType = SoShaderObject.SourceType.GLSL_PROGRAM;
      return;
    }
  }
  if (len > 3) {
    String subStr = fileName.substring(len-3);
    if (subStr.equals(".cg")) {
      this.cachedSourceType = SoShaderObject.SourceType.CG_PROGRAM;
      return;
    }
    if (subStr.equals(".fp")) {
      this.cachedSourceType = this.owner.isOfType(SoVertexShader.getClassTypeId())
        ? SoShaderObject.SourceType.FILENAME : SoShaderObject.SourceType.ARB_PROGRAM;
      return;
    }
    if (subStr.equals(".vp")) {
      this.cachedSourceType = this.owner.isOfType(SoVertexShader.getClassTypeId())
        ? SoShaderObject.SourceType.ARB_PROGRAM : SoShaderObject.SourceType.FILENAME;
      return;
    }
  }
  SoDebugError.postWarning("SoShaderObjectP.checkType",
                            "Could not determine shader type of file '"+fileName+"'!\n"+
                            "Following file extensions are supported:\n"+
                            "*.fp -> ARB_PROGRAM (fragment)\n"+
                            "*.vp -> ARB_PROGRAM (vertex)\n"+
                            "*.cg -> CG_PROGRAM (fragment|vertex)\n"+
                            "*.glsl *.vert *.frag -> GLSL_PROGRAM (fragment|vertex)\n"
                            );
  // error: could not determine SourceType
  this.cachedSourceType = SoShaderObject.SourceType.FILENAME;
}

// read the file if neccessary and assign content to this.cachedSourceProgram
private void
readSource()
{
  SoShaderObject.SourceType srcType =
    SoShaderObject.SourceType.fromValue(this.owner.sourceType.getValue());

  this.cachedSourceProgram = "";//.makeEmpty();

  if (this.owner.sourceProgram.isDefault())
    return;
  else if (srcType != SoShaderObject.SourceType.FILENAME)
    this.cachedSourceProgram = this.owner.sourceProgram.getValue();
  else {
    if (this.cachedSourceType != SoShaderObject.SourceType.FILENAME) {

      final SbStringList subdirs = new SbStringList();
      subdirs.append(new String("shader"));
      subdirs.append(new String("shaders"));
      String fileName = SoInput.searchForFile(this.owner.sourceProgram.getValue(),
                                                 this.searchdirectories,
                                                 subdirs);
      // delete allocated subdirs before continuing
      //delete subdirs[0]; java port
      //delete subdirs[1]; java port

      if (fileName.length() <= 0) {
        SoDebugError.postWarning("SoShaderObjectP.readSource",
                                  "Shader file not found: '"+this.owner.sourceProgram.getValue()+"'");
        this.cachedSourceType = SoShaderObject.SourceType.FILENAME;
        return;
      }

      FILE f = FILE.fopen(fileName, "rb");
      boolean readok = false;
      if (f != null) {
        if (FILE.fseek(f, 0L, FILE.SEEK_END) == 0) {
          final int length = FILE.ftell(f);
          if ((length > 0) && (FILE.fseek(f, 0L, FILE.SEEK_SET) == 0)) {
            char[] srcstr = new char[length+1];
            long readlen = FILE.fread(srcstr, 1, length, f);
            if (readlen == (long) length) {
              srcstr[length] = '\0';
              this.cachedSourceProgram = Util.toString(srcstr);
              readok = true;
            }
            //delete[] srcstr; java port
          }
        }
        FILE.fclose(f);
      }
      if (!readok) {
        this.cachedSourceType = SoShaderObject.SourceType.FILENAME;
        SoDebugError.postWarning("SoShaderObjectP.readSource",
                                  "Could not read shader file '"+fileName+"'");
      }
    }
  }
}

private boolean
isSupported(SoShaderObject.SourceType sourceType, cc_glglue glue)
{
  if (this.owner.isOfType(SoVertexShader.getClassTypeId())) {
    // don't call this function. It's not context safe. pederb, 20051103
    // return SoVertexShader.isSupported(sourceType);

    if (sourceType == SoShaderObject.SourceType.ARB_PROGRAM) {
      return SoGLDriverDatabase.isSupported(glue, SoGLDriverDatabase.SO_GL_ARB_VERTEX_PROGRAM);
    }
    else if (sourceType == SoShaderObject.SourceType.GLSL_PROGRAM) {
      return SoGLDriverDatabase.isSupported(glue, SoGLDriverDatabase.SO_GL_ARB_SHADER_OBJECT);
    }
    // FIXME: Add support for detecting missing Cg support
    // (20050427 handegar)
    else if (sourceType == SoShaderObject.SourceType.CG_PROGRAM) return true;
    return false;
  }
  else if (this.owner.isOfType(SoFragmentShader.getClassTypeId())) {
    // don't call this function. It's not context safe. pederb, 20051103
    // return SoFragmentShader.isSupported(sourceType);

    if (sourceType == SoShaderObject.SourceType.ARB_PROGRAM) {
      return SoGLDriverDatabase.isSupported(glue, SoGLDriverDatabase.SO_GL_ARB_FRAGMENT_PROGRAM);
    }
    else if (sourceType == SoShaderObject.SourceType.GLSL_PROGRAM) {
      return SoGLDriverDatabase.isSupported(glue, SoGLDriverDatabase.SO_GL_ARB_SHADER_OBJECT);
    }
    // FIXME: Add support for detecting missing Cg support (20050427
    // handegar)
    else if (sourceType == SoShaderObject.SourceType.CG_PROGRAM) return true;
    return false;
  }
  else {
    assert(this.owner.isOfType(SoGeometryShader.getClassTypeId()));
    if (sourceType == SoShaderObject.SourceType.GLSL_PROGRAM) {
      return 
        SoGLDriverDatabase.isSupported(glue, "GL_EXT_geometry_shader4") && 
        SoGLDriverDatabase.isSupported(glue, SoGLDriverDatabase.SO_GL_ARB_SHADER_OBJECT);
    }
    return false;
  }
}

private void
updateParameters(final int cachecontext, int start, int num)
{

  if (!this.owner.isActive.getValue()) return;
  if (start < 0 || num < 0) return;

  SoGLShaderObject shaderobject = this.getGLShaderObject(cachecontext);
  if ((shaderobject == null) || !shaderobject.getParametersDirty()) return;

  int cnt = this.owner.parameter.getNum();
  int end = start+num;

  end = (end > cnt) ? cnt : end;
  for (int i=start; i<end; i++) {
    SoUniformShaderParameter param =
      (SoUniformShaderParameter)this.owner.parameter.operator_square_bracket(i)[0];
    param.updateParameter(shaderobject);
  }
}

//#include <Inventor/elements/SoGLTextureImageElement.h>
//#include <Inventor/elements/SoGLMultiTextureImageElement.h>
//#include <Inventor/elements/SoLazyElement.h>

//private void
//updateCoinParameters(final GL2 cachecontext, SoState state)
//{
//  int i, cnt = this.owner.parameter.getNum();
//
//  SoGLShaderObject shaderobject = this.getGLShaderObject(cachecontext);
//
//  for (i = 0; i < cnt; i++) {
//    SoUniformShaderParameter param =
//      (SoUniformShaderParameter)this.owner.parameter.operator_square_bracket(i)[0];
//    SbName name = new SbName(param.name.getValue());
//
//    if (Util.strncmp(name.getString(), "coin_", 5) == 0) {
//      if (name.operator_equal_equal("coin_texunit0_model")) {
//        final SoTextureImageElement.Model[] model = new SoTextureImageElement.Model[1];
//        final SbColor[] dummy = new SbColor[1];
//        boolean tex = SoGLTextureImageElement.get(state, model, dummy) != null;
//        shaderobject.updateCoinParameter(state, name, null, tex ? model[0].getValue() : 0);
//      }
//      else if (name.operator_equal_equal("coin_texunit1_model")) {
//    	  final SoTextureImageElement.Model[] model = new SoTextureImageElement.Model[1];
//    	  final SbColor[] dummy = new SbColor[1];
//        boolean tex = SoGLMultiTextureImageElement.get(state, 1, model, dummy) != null;
//        shaderobject.updateCoinParameter(state, name, null, tex ? model[0].getValue() : 0);
//      }
//      else if (name.operator_equal_equal("coin_texunit2_model")) {
//    	  final SoTextureImageElement.Model[] model = new SoTextureImageElement.Model[1];
//    	  final SbColor[] dummy = new SbColor[1];
//        boolean tex = SoGLMultiTextureImageElement.get(state, 2, model, dummy) != null;
//        shaderobject.updateCoinParameter(state, name, null, tex ? model[0].getValue() : 0);
//      }
//      else if (name.operator_equal_equal("coin_texunit3_model")) {
//    	  final SoTextureImageElement.Model[] model = new SoTextureImageElement.Model[1];
//    	  final SbColor[] dummy = new SbColor[1];
//        boolean tex = SoGLMultiTextureImageElement.get(state, 3, model, dummy) != null;
//        shaderobject.updateCoinParameter(state, name, null, tex ? model[0].getValue() : 0);
//      }
//      else if (name.operator_equal_equal("coin_light_model")) {
//        shaderobject.updateCoinParameter(state, name, null, SoLazyElement.getLightModel(state));
//      }
//    }
//  }
//}

private void
updateCoinParameters(final int cachecontext, SoState state)
{
  int i, cnt = this.owner.parameter.getNum();

  SoGLShaderObject shaderobject = this.getGLShaderObject(cachecontext);

  for (i = 0; i < cnt; i++) {
    SoUniformShaderParameter param =
      (SoUniformShaderParameter)this.owner.parameter.operator_square_bracket(i)[0];
    SbName name = new SbName(param.name.getValue());
    
    if (Util.strncmp(name.getString(), "coin_", 5) == 0) {
      if (name.operator_equal_equal("coin_texunit0_model")) {
        final SoMultiTextureImageElement.Model[] model = new SoMultiTextureImageElement.Model[1];
        final SbColor dummy = new SbColor();
        boolean tex = SoGLMultiTextureImageElement.get(state, model, dummy) != null;
        shaderobject.updateCoinParameter(state, name, null, tex ? model[0].getValue() : 0);
      }
      else if (name.operator_equal_equal("coin_texunit1_model")) {
        final SoMultiTextureImageElement.Model[] model = new SoMultiTextureImageElement.Model[1];
        final SbColor dummy = new SbColor();
        boolean tex = SoGLMultiTextureImageElement.get(state, 1, model, dummy) != null;
        shaderobject.updateCoinParameter(state, name, null, tex ? model[0].getValue() : 0);
      }
      else if (name.operator_equal_equal("coin_texunit2_model")) {
        final SoMultiTextureImageElement.Model[] model = new SoMultiTextureImageElement.Model[1];
        final SbColor dummy = new SbColor();
        boolean tex = SoGLMultiTextureImageElement.get(state, 2, model, dummy) != null;
        shaderobject.updateCoinParameter(state, name, null, tex ? model[0].getValue() : 0);
      }
      else if (name.operator_equal_equal("coin_texunit3_model")) {
        final SoMultiTextureImageElement.Model[] model = new SoMultiTextureImageElement.Model[1];
        final SbColor dummy = new SbColor();
        boolean tex = SoGLMultiTextureImageElement.get(state, 3, model, dummy) != null;
        shaderobject.updateCoinParameter(state, name, null, tex ? model[0].getValue() : 0);
      }
      else if (name.operator_equal_equal("coin_light_model")) {
        shaderobject.updateCoinParameter(state, name, null, SoLazyElement.getLightModel(state));
      }
      else if (name.operator_equal_equal("coin_two_sided_lighting")) {
        shaderobject.updateCoinParameter(state, name, null, SoLazyElement.getTwoSidedLighting(state)?1:0); //java port
      }
    }
  }
}



private void
updateAllParameters(final int cachecontext)
{
  if (!this.owner.isActive.getValue()) return;

  SoGLShaderObject shaderobject = this.getGLShaderObject(cachecontext);
  if ((shaderobject == null) || !shaderobject.getParametersDirty()) return;

  int i, cnt = this.owner.parameter.getNum();

  for (i=0; i<cnt; i++) {
    SoUniformShaderParameter param =
      (SoUniformShaderParameter)this.owner.parameter.operator_square_bracket(i)[0];
    param.updateParameter(shaderobject);
  }
  shaderobject.setParametersDirty(false);
}

// Update state matrix paramaters
private void
updateStateMatrixParameters(final int cachecontext, SoState state)
{
//#define STATE_PARAM SoShaderStateMatrixParameter
  if (!this.owner.isActive.getValue()) return;

  SoGLShaderObject shaderobject = this.getGLShaderObject(cachecontext);
  if (shaderobject == null) return;

  int i, cnt = this.owner.parameter.getNum();
  for (i= 0; i <cnt; i++) {
	  SoUniformShaderParameter param = (SoUniformShaderParameter)this.owner.parameter.operator_square_bracket(i)[0];
    if (param.isOfType(SoShaderStateMatrixParameter.getClassTypeId())) {
    	((SoShaderStateMatrixParameter)param).updateValue(state);
      ((SoShaderStateMatrixParameter)param).updateParameter(shaderobject);
    }
  }
//#undef STATE_PARAM
}

private boolean
containStateMatrixParameters()
{
//#define STATE_PARAM SoShaderStateMatrixParameter
  int i, cnt = this.owner.parameter.getNum();
  for (i = 0; i < cnt; i++) {
    if (this.owner.parameter.operator_square_bracket(i)[0].isOfType(SoShaderStateMatrixParameter.getClassTypeId()))
      return true;
  }
//#undef STATE_PARAM
  return false;
}

//#if defined(SOURCE_HINT)
private String
getSourceHint()
{
  SoShaderObject.SourceType srcType =
    SoShaderObject.SourceType.fromValue(this.owner.sourceType.getValue());

  if (srcType == SoShaderObject.SourceType.FILENAME)
    return this.owner.sourceProgram.getValue();
  else
    return ""; // FIXME: should return first line of shader source code
}
//#endif

private static void
sensorCB(Object data, SoSensor sensor)
{
  SoShaderObject thisp = (SoShaderObject) data;
  SoField field = ((SoNodeSensor )sensor).getTriggerField();
  
  if (field == thisp.owner.sourceProgram ||
      field == thisp.owner.sourceType) {
    thisp.deleteGLShaderObjects();
    thisp.shouldload = true;
  }
  else if (field == thisp.owner.parameter) {
    thisp.invalidateParameters();
  }
  if (!thisp.didSetSearchDirectories) {
    thisp.setSearchDirectories(SoInput.getDirectories());
  }
}

private void
setSearchDirectories(final SbStringList list)
{
  int i;
  for (i = 0; i< this.searchdirectories.getLength(); i++) {
    // this.searchdirectories.operator_square_bracket(i).destructor(); java port
  }

  for (i = 0; i < list.getLength(); i++) {
    this.searchdirectories.append(new String(((String)list.operator_square_bracket(i))));
  }
  this.didSetSearchDirectories = true;
}

//#undef PRIVATE

private   SoGLShaderObject getGLShaderObject(final int cachecontext) {
    SoGLShaderObject obj = null;
    if ((obj = this.glshaderobjects.get(cachecontext))!=null) return obj;
    return null;
  }

private void setGLShaderObject(SoGLShaderObject obj, final int cachecontext) {
    SoGLShaderObject oldshader;
    if ((oldshader = this.glshaderobjects.get(cachecontext))!=null) {
      SoGLCacheContextElement.scheduleDeleteCallback(oldshader.getCacheContext(),
    		  SoShaderObject::really_delete_object, oldshader);
    }
    this.glshaderobjects.put(cachecontext, obj);
  }

private void deleteGLShaderObjects() {
    final Collection <Integer> keylist = this.glshaderobjects.keySet();
    for (int key : keylist) {
      SoGLShaderObject glshader = this.glshaderobjects.get(key);
      SoGLCacheContextElement.scheduleDeleteCallback(glshader.getCacheContext(),
                                                      SoShaderObject::really_delete_object, glshader);
    }
    this.glshaderobjects.clear();
  }
  //
  // Callback from SoGLCacheContextElement
  //
private static void really_delete_object(Object closure, int contextid) {
    SoGLShaderObject obj = (SoGLShaderObject) closure;
    obj.destructor();
  }
  //
  // callback from SoContextHandler
  //
private static void context_destruction_cb(int cachecontext, Object userdata) {
    SoShaderObject thisp = (SoShaderObject) userdata;

    SoGLShaderObject oldshader;
    if ((oldshader = thisp.glshaderobjects.get(cachecontext))!=null) {
      // just delete immediately. The context is current
      oldshader.destructor();
      thisp.glshaderobjects.remove(cachecontext);
    }
  }

  private void invalidateParameters() {
    final Collection <Integer> keylist = this.glshaderobjects.keySet();
    for (int key : keylist) {
      SoGLShaderObject glshader = null;
      glshader = this.glshaderobjects.get(key);
      glshader.setParametersDirty(true);
    }
  }


}
