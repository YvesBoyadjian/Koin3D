/**
 * 
 */
package jscenegraph.coin3d.glue;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.EXTGeometryShader4;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL3;

/**
 * @author Yves Boyadjian
 *
 */
public class cc_glglue {
	
	public GL2 contextid;
	GL2 gl2;

	public cc_glglue(GL2 gl2) {
		this.gl2 = gl2;
		this.contextid = gl2;
	}

	public GL2 getGL2() {
		return gl2;
	}

	public void glUniform1fARB(int location, float value) {
		ARBShaderObjects.glUniform1fARB(location, value);
	}

	public void glUniform2fARB(int location, float f, float g) {
		ARBShaderObjects.glUniform2fARB(location, f, g);
	}

	public void glUniform3fARB(int location, float f, float g, float h) {
		ARBShaderObjects.glUniform3fARB(location, f, g, h);
	}

	public void glUniform4fARB(int location, float f, float g, float h, float i) {
		ARBShaderObjects.glUniform4fARB(location, f, g, h, i);
	}

	public void glUniform1fvARB(int location, int i, float[] value) {
		ARBShaderObjects.glUniform1fvARB(location, value);
	}

	public void glUniform2fvARB(int location, int i, float[] value) {
		ARBShaderObjects.glUniform2fvARB(location,value);
	}

	public void glUniform3fvARB(int location, int i, float[] value) {
		ARBShaderObjects.glUniform3fvARB(location, value);
	}

	public void glUniform4fvARB(int location, int i, float[] value) {
		ARBShaderObjects.glUniform4fvARB(location,value);
	}

	public void glUniformMatrix4fvARB(int location, int i, boolean b, float[] value) {
		ARBShaderObjects.glUniformMatrix4fvARB(location,b,value);
	}

	public void glUniform1iARB(int location, int value) {
		ARBShaderObjects.glUniform1iARB(location,value);
	}

	public void glUniform2iARB(int location, int i, int j) {
		ARBShaderObjects.glUniform2iARB(location,i,j);
	}

	public void glUniform3iARB(int location, int i, int j, int k) {
		ARBShaderObjects.glUniform3iARB(location,i,j,k);
	}

	public void glUniform4iARB(int location, int i, int j, int k, int l) {
		ARBShaderObjects.glUniform4iARB(location,i,j,k,l);
	}

	public void glUniform1ivARB(int location, int num, int[] value) {
		ARBShaderObjects.glUniform1ivARB(location,value);
	}

	public void glUniform2ivARB(int location, int num, int[] value) {
		ARBShaderObjects.glUniform2ivARB(location,value);
	}

	public void glUniform3ivARB(int location, int num, int[] v) {
		ARBShaderObjects.glUniform3ivARB(location,v);
	}

	public void glUniform4ivARB(int location, int num, int[] v) {
		ARBShaderObjects.glUniform4ivARB(location,v);
	}

	public int glGetUniformLocationARB(int pHandle, String name) {
		return ARBShaderObjects.glGetUniformLocationARB(pHandle, new StringBuffer(name));
	}

	public void glGetObjectParameterivARB(int pHandle, int glObjectActiveUniformsArb, int[] activeUniforms) {
		ARBShaderObjects.glGetObjectParameterivARB(pHandle, glObjectActiveUniformsArb, activeUniforms);
	}

	public void glGetActiveUniformARB(int pHandle, int index, int[] length, int[] tmpSize, int[] tmpType,
			byte[] myName) {
		ARBShaderObjects.glGetActiveUniformARB(pHandle,index,length,tmpSize,tmpType,Buffers.newDirectByteBuffer(myName));
	}

	  static final String INVALID_VALUE = "GL_INVALID_VALUE";
	  static final String INVALID_ENUM = "GL_INVALID_ENUM";
	  static final String INVALID_OPERATION = "GL_INVALID_OPERATION";
	  static final String STACK_OVERFLOW = "GL_STACK_OVERFLOW";
	  static final String STACK_UNDERFLOW = "GL_STACK_UNDERFLOW";
	  static final String OUT_OF_MEMORY = "GL_OUT_OF_MEMORY";
	  static final String unknown = "Unknown OpenGL error";

/* Convert an OpenGL enum error code to a textual representation. */
public String
coin_glerror_string(/*GLenum*/int errorcode)
{
  switch (errorcode) {
  case GL2.GL_INVALID_VALUE:
    return INVALID_VALUE;
  case GL2.GL_INVALID_ENUM:
    return INVALID_ENUM;
  case GL2.GL_INVALID_OPERATION:
    return INVALID_OPERATION;
  case GL3.GL_STACK_OVERFLOW:
    return STACK_OVERFLOW;
  case GL3.GL_STACK_UNDERFLOW:
    return STACK_UNDERFLOW;
  case GL2.GL_OUT_OF_MEMORY:
    return OUT_OF_MEMORY;
  default:
    return unknown;
  }
}

public int glCreateShaderObjectARB(int sType) {
	return ARBShaderObjects.glCreateShaderObjectARB(sType);
}

public void glShaderSourceARB(int shaderHandle, int count, String srcStr, int[] length) {
	CharSequence string = new StringBuffer(srcStr);
	ARBShaderObjects.glShaderSourceARB(shaderHandle, string);
}

public void glCompileShaderARB(int shaderHandle) {
	ARBShaderObjects.glCompileShaderARB(shaderHandle);
}

public void glDeleteObjectARB(int shaderHandle) {
	ARBShaderObjects.glDeleteObjectARB(shaderHandle);
}

//public void glGetObjectParameterivARB(int shaderHandle, int glObjectCompileStatusArb, int[] flag) {
//	ARBShaderObjects.glGetObjectParameterivARB(shaderHandle, glObjectCompileStatusArb, flag);
//}

public void glAttachObjectARB(long programHandle, long shaderHandle) {
	gl2.glAttachObjectARB(programHandle, shaderHandle);
}

public void glDetachObjectARB(int programHandle, int shaderHandle) {
	ARBShaderObjects.glDetachObjectARB(programHandle, shaderHandle);
}

public void glGetInfoLogARB(int handle, int length, int[] charsWritten, byte[] infoLog) {
	ARBShaderObjects.glGetInfoLogARB(handle, charsWritten, Buffers.newDirectByteBuffer(infoLog));
}

public void glUseProgramObjectARB(int programhandle) {
	ARBShaderObjects.glUseProgramObjectARB(programhandle);
}

public void glLinkProgramARB(int programHandle) {
	ARBShaderObjects.glLinkProgramARB(programHandle);
}

public void glProgramParameteriEXT(long programHandle, int operator_square_bracket, int operator_square_bracket2) {
	EXTGeometryShader4.glProgramParameteriEXT((int)programHandle, operator_square_bracket, operator_square_bracket2);
}

public int glCreateProgramObjectARB() {
	return ARBShaderObjects.glCreateProgramObjectARB();
}

}
