/**
 * 
 */
package jscenegraph.coin3d.inventor.misc;

import jscenegraph.coin3d.inventor.base.SbString;
import jscenegraph.coin3d.shaders.SoShader;
import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.errors.SoDebugError;

/**
 * @author Yves Boyadjian
 *
 */
public class SoShaderGenerator {

		  String version;
		  String defines;
		  String declarations;
		  String functions;
		  StringBuilder main;
		  
		  boolean dirty;
		  String combined;
		  
		  /*!
		  Constructor.
		*/
		public SoShaderGenerator()
		{
		 reset(false);
		}

		/*!
		  Destructor.
		*/
		public void destructor()
		{
		}

		public void 
		reset( boolean freeoldstrings)
		{
		  this.version = "";//.makeEmpty(freeoldstrings);
		  this.defines = "";//.makeEmpty(freeoldstrings);
		  this.declarations = "";//.makeEmpty(freeoldstrings);
		  this.functions = "";//.makeEmpty(freeoldstrings);
		  this.main = new StringBuilder();//.makeEmpty(freeoldstrings);
		  this.combined = "";//.makeEmpty(freeoldstrings);
		  this.dirty = false;
		}


		public void 
		setVersion( String str)
		{
		  this.version = str;
		  this.version += "\n";
		}

		/*!
		  Adds a define to the shader program.
		*/
		public void 
		addDefine( String str,  boolean checkexists)
		{
		  if (!checkexists || (SbString.find(this.defines,str) < 0)) {
		    this.dirty = true;
		    this.defines += str;
		    this.defines += "\n";
		  }
		}

		/*!
		  Adds a declaration (varying or uniform) to the script.
		*/
		public void 
		addDeclaration( String str,  boolean checkexists)
		{
		  if (!checkexists || (SbString.find(this.declarations,str) < 0)) {
		    this.dirty = true;
		    this.declarations += str;
		    this.declarations += "\n";
		  }
		}

		/*!
		  Adds a function to the script.
		*/
		public void 
		addFunction( String str,  boolean checkexists)
		{
		  if (!checkexists || (SbString.find(this.functions,str) < 0)) {
		    this.dirty = true;
		    this.functions += str;
		    this.functions += "\n";
		  }
		}

		/*!
		  Adds a named function to the script.
		*/
		public void 
		addNamedFunction( String name,  boolean checkexists) {
			addNamedFunction(new SbName(name), checkexists);
		}
		public void 
		addNamedFunction( SbName name,  boolean checkexists)
		{
		   String func = SoShader.getNamedScript(name, SoShader.Type.GLSL_SHADER);
		  
		  if (func != null) {
		    this.addFunction(func, checkexists);
		  }
		  else {
		    SoDebugError.postWarning("SoShaderGenerator::addNamedFunction",
		                              "Unknown named script: "+
		                              name.getString());
		  }
		}

		/*!
		  Add a statment to the main function.
		*/
		public void 
		addMainStatement( String str)
		{
		  this.dirty = true;
		  this.main.append( str);
		  this.main.append( "\n");
		}

		/*!
		  Returns the complete shader program.
		*/
		public String 
		getShaderProgram()
		{
		  if (this.dirty) {
		    this.combined = "";//.makeEmpty(false);
		    this.combined += this.version;
		    this.combined += this.defines;
		  
		    this.combined += this.declarations;
		    this.combined += this.functions;
		    this.combined += "void main(void) {\n";
		    this.combined += this.main.toString();
		    this.combined += "}\n";
		  }
		  return this.combined;
		}
		  
}
