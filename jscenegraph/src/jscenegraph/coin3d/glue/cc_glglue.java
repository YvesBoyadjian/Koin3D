/**
 * 
 */
package jscenegraph.coin3d.glue;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.EXTGeometryShader4;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL3;

import jscenegraph.coin3d.misc.SoGL;
import jscenegraph.port.Ctx;
import jscenegraph.port.FloatBufferAble;
import jscenegraph.port.IntArrayPtr;
import jscenegraph.port.SbColorArray;
import jscenegraph.port.SbVec3fArray;
import jscenegraph.port.ShortBufferAble;
import jscenegraph.port.VoidPtr;

/**
 * @author Yves Boyadjian
 *
 */
public class cc_glglue {
	
	public int contextid;
	GL2 gl2;
	public boolean vendor_is_intel;
	
	public static class Version {
		public int major = 4;
		public int minor = 0;
		public int release = 0;
	}
	
	public Version version = new Version();
	public String versionstr = "";
	public String vendorstr = "";
	public String rendererstr = "";
	public String extensionsstr = "";
	public int maxtextureunits;
	public boolean vendor_is_nvidia = false;
	public boolean has_fbo;
	  public float max_anisotropy;
	public boolean vendor_is_ati = true;

	public boolean can_do_anisotropic_filtering = true;

	public boolean has_arb_fragment_program = true;
	
	public boolean vbo_in_displaylist_ok = true;
	  
	public boolean non_power_of_two_textures = true;
	  public int max_lights;
	public int max_texture_size;
	
	public long glGenerateMipmap;
	
	public cc_glglue(int ctx) {
		this.gl2 = Ctx.get(ctx);
		this.contextid = ctx;
		
	    /*
	       Make sure all GL errors are cleared before we do our assert
	       test below. The OpenGL context might be set up by the user, and
	       it's better to print a warning than asserting here if the user
	       did something wrong while creating it.
	    */
	    int glerr = GL11.glGetError();
	    while (glerr != GL2.GL_NO_ERROR) {
	      Gl_wgl.cc_debugerror_postwarning("cc_glglue_instance",
	                                "Error when setting up the GL context. This can happen if "+
	                                "there is no current context, or if the context has been set "+
	                                "up incorrectly.");
	      glerr = GL11.glGetError();

	      /* We might get this error if there is no current context.
	         Break out and assert later in that case */
	      if (glerr == GL2.GL_INVALID_OPERATION) break;
	    }

	    /* NB: if you are getting a crash here, it's because an attempt at
	     * setting up a cc_glglue instance was made when there is no
	     * current OpenGL context. */
	    versionstr = (String)GL11.glGetString(GL2.GL_VERSION);	    
	    
    SoGL.glglue_set_glVersion(this);
    
	vendorstr = (String)GL11.glGetString(GL2.GL_VENDOR);
	
    rendererstr = (String)GL11.glGetString(GL2.GL_RENDERER);
    extensionsstr = (String)GL11.glGetString(GL2.GL_EXTENSIONS);

    /* Randall O'Reilly reports that the above call is deprecated from OpenGL 3.0
       onwards and may, particularly on some Linux systems, return NULL.

       The recommended method is to use glGetStringi to get each string in turn.
       The following code, supplied by Randall, implements this to end up with the
       same result as the old method.
    */
    if (extensionsstr == null || extensionsstr.trim().isEmpty()) {
      // COIN_PFNGLGETSTRINGIPROC glGetStringi = NULL;
      // glGetStringi = (COIN_PFNGLGETSTRINGIPROC)cc_glglue_getprocaddress(gi, "glGetStringi");
    	GLCapabilities c = GL.getCapabilities();
      if ( c.glGetStringi != NULL) {
        final int[] num_strings = new int[1];
        gl2.glGetIntegerv(GL2.GL_NUM_EXTENSIONS, num_strings);
        if (num_strings[0] > 0) {
          //int buffer_size = 1024;
          StringBuilder ext_strings_buffer = new StringBuilder();
          //int buffer_pos = 0;
          for (int i_string = 0 ; i_string < num_strings[0] ; i_string++) {
            String extension_string = (String)GL30.glGetStringi (GL2.GL_EXTENSIONS, i_string);
            //int extension_string_length = (int)strlen(extension_string);
//            if (buffer_pos + extension_string_length + 1 > buffer_size) {
//              buffer_size += 1024;
//              // ext_strings_buffer = (String)realloc(ext_strings_buffer, buffer_size * sizeof (char)); java port
//            }
            //strcpy(ext_strings_buffer + buffer_pos, extension_string);
            ext_strings_buffer.append( extension_string );
            //buffer_pos += extension_string_length;
            ext_strings_buffer.append(' '); // Space separated, overwrites NULL.
          }
          //ext_strings_buffer[++buffer_pos] = '\0';  // NULL terminate.
          extensionsstr = ext_strings_buffer.toString();   // Handing over ownership, don't free here.
        } else {
        	Gl_wgl.cc_debugerror_postwarning ("cc_glglue_instance",
                                     "glGetIntegerv(GL_NUM_EXTENSIONS) did not return a value, "+
                                     "so unable to get extensions for this GL driver, "+
                                     "version: "+versionstr+", vendor: "+vendorstr+"");
        }
      } else {
        Gl_wgl.cc_debugerror_postwarning ("cc_glglue_instance",
                                   "glGetString(GL_EXTENSIONS) returned null, but glGetStringi "+
                                   "procedure not found, so unable to get extensions for this GL driver, "+
                                   "version: "+versionstr+", vendor: "+vendorstr+"");
      }
    }

		

		final int[] gltmp = new int[1];
	    gl2.glGetIntegerv(GL2.GL_MAX_TEXTURE_SIZE, gltmp,0);
	    max_texture_size = gltmp[0];

	    gl2.glGetIntegerv(GL2.GL_MAX_LIGHTS, gltmp);
	    max_lights = (int) gltmp[0];
	    
	    maxtextureunits = 1; /* when multitexturing is not available */
	    //if (w->glActiveTexture) {
	      final int[] tmp = new int[1];
	      gl2.glGetIntegerv(GL2.GL_MAX_TEXTURE_COORDS_ARB, tmp,0);
	      maxtextureunits = (int) tmp[0];
	    //}
	      
	      /* anisotropic test */
	      can_do_anisotropic_filtering = false;
	      max_anisotropy = 0.0f;
	      if (SoGL.cc_glglue_glext_supported(this, "GL_EXT_texture_filter_anisotropic")) {
	        can_do_anisotropic_filtering = true;
		      final float[] tmp_float = new float[1];
	        gl2.glGetFloatv(GL2.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT, tmp_float/* &gi->max_anisotropy*/);
	        max_anisotropy = tmp_float[0];
	        if (Gl.coin_glglue_debug() != 0) {
	          Gl_wgl.cc_debugerror_postinfo("cc_glglue_instance",
	                                 "Anisotropic filtering: "+(can_do_anisotropic_filtering ? "TRUE" : "FALSE")+" ("+max_anisotropy+")");
	        }
	      }
	      
	      non_power_of_two_textures =
	    	      (SoGL.cc_glglue_glversion_matches_at_least(this, 2, 1, 0) ||
	    	       SoGL.cc_glglue_glext_supported(this, "GL_ARB_texture_non_power_of_two"));

	      /* Resolve our function pointers. */
	      Gl.glglue_resolve_symbols(this);
	      
	      GLCapabilities c = GL.getCapabilities();
	      glGenerateMipmap = c.glGenerateMipmap;	      
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
		ByteBuffer bb = Buffers.newDirectByteBuffer(myName);
		ARBShaderObjects.glGetActiveUniformARB(pHandle,index,length,tmpSize,tmpType,bb);
		bb.get(myName);
	}

	public void glGetActiveUniformARB(int pHandle, int index, int[] length, int[] tmpSize, int[] tmpType,
			ByteBuffer name) {
		//ByteBuffer bb = Buffers.newDirectByteBuffer(myName);
		ARBShaderObjects.glGetActiveUniformARB(pHandle,index,length,tmpSize,tmpType,name);
		//bb.get(myName);
	}

	  static final String INVALID_VALUE = "GL_INVALID_VALUE";
	  static final String INVALID_ENUM = "GL_INVALID_ENUM";
	  static final String INVALID_OPERATION = "GL_INVALID_OPERATION";
	  static final String STACK_OVERFLOW = "GL_STACK_OVERFLOW";
	  static final String STACK_UNDERFLOW = "GL_STACK_UNDERFLOW";
	  static final String OUT_OF_MEMORY = "GL_OUT_OF_MEMORY";
	  static final String INVALID_FRAMEBUFFER_OPERATION = "GL_INVALID_FRAMEBUFFER_OPERATION";
	  static final String unknown = "Unknown OpenGL error";

/* Convert an OpenGL enum error code to a textual representation. */
public static String
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
  case GL2.GL_INVALID_FRAMEBUFFER_OPERATION:
	  return INVALID_FRAMEBUFFER_OPERATION;
  default:
    return unknown;
  }
}

public int glCreateShaderObjectARB(int sType) {
	return ARBShaderObjects.glCreateShaderObjectARB(sType);
}

public void glShaderSourceARB(int shaderHandle, int count, String srcStr, int[] length) {
	CharSequence string = new StringBuilder(srcStr);
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
	ByteBuffer bb =  Buffers.newDirectByteBuffer(infoLog);
	ARBShaderObjects.glGetInfoLogARB(handle, charsWritten, bb);
	bb.get(infoLog);
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

public void glDeleteFramebuffers(int n, int[] framebuffers) {
	gl2.glDeleteFramebuffers(n,framebuffers);
}

public void glDeleteFramebuffers(int n, int frameBuffer) {
	gl2.glDeleteFramebuffers(n,frameBuffer);	
}

public void glDeleteRenderbuffers(int n, int renderbuffers) {
	gl2.glDeleteRenderbuffers( n, renderbuffers);
}

public void glMultiTexCoord2fv(int target, float[] v) {
	gl2.glMultiTexCoord2fv(target, v);
}

public void glMultiTexCoord3fv(int target, float[] v) {
	gl2.glMultiTexCoord3fv(target, v);
}

public void glMultiTexCoord4fv(int target, float[] v) {
	gl2.glMultiTexCoord4fv(target, v);
}

public int glCheckFramebufferStatus(int target) {
	return gl2.glCheckFramebufferStatus( target);
}

public void glBindFramebuffer(int target, int framebuffer) {
	gl2.glBindFrameBuffer(target, framebuffer);
}

public void glBindTexture(int target, int texture) {
	gl2.glBindTexture(target, texture);
}

public void glGenerateMipmap(int target) {
	gl2.glGenerateMipmap(target);
}

public void glFramebufferRenderbuffer(int target, int attachment, int renderbuffertarget, int renderbuffer) {
	gl2.glFramebufferRenderbuffer( target, attachment, renderbuffertarget, renderbuffer);
}

public void glRenderbufferStorage(int target, int internalformat, short width, short height) {
	gl2.glRenderbufferStorage(target, internalformat,width,height);
}

public void glBindRenderbuffer(int target, int renderbuffer) {
	gl2.glBindRenderbuffer( target, renderbuffer);
}

public void glFramebufferTexture2D(int target, int attachment, int textarget, int texture, int level) {
	gl2.glFramebufferTexture2D( target, attachment, textarget, texture, level);
}

public void glGenFramebuffers(int n, int[] framebuffers) {
	gl2.glGenFramebuffers( n, framebuffers);
}

public void glGenRenderbuffers(int n, int[] renderbuffers) {
	gl2.glGenRenderbuffers( n, renderbuffers);
}

public void glDrawArrays(int mode, int first, int count) {
	gl2.glDrawArrays(mode, first, count);
}

public void glGenBuffers(int n, int[] buffers) {
	gl2.glGenBuffers(n,buffers);
}

public void glBindBuffer(int target, int buffer) {
	gl2.glBindBuffer(target,buffer);
}

public void glBufferData(int target, long size, VoidPtr data, int usage) {
	gl2.glBufferData(target,size,data.toBuffer(),usage);
}

public void glColorPointer(int size, int type, int stride, FloatBufferAble pointer) {
	if(pointer == null) {
		gl2.glColorPointer(size,type,stride, 0);		
	}
	else {
		gl2.glColorPointer(size,type,stride,pointer.toFloatBuffer());
	}
}

public void glEnableClientState(int array) {
	gl2.glEnableClientState(array);
}

public void glClientActiveTexture(int texture) {
	gl2.glClientActiveTexture(texture);
}

public void glTexCoordPointer(int size, int type, int stride, FloatBufferAble pointer) {
	if(pointer == null) {
		gl2.glTexCoordPointer(size,type,stride, 0);		
	}
	else {
		gl2.glTexCoordPointer(size,type,stride,pointer.toFloatBuffer());
	}
}

public void glNormalPointer(int type, int stride, FloatBufferAble pointer) {
	if(pointer == null) {
		gl2.glNormalPointer(type,stride,0);		
	}
	else {
		gl2.glNormalPointer(type,stride,pointer.toFloatBuffer());
	}
}

public void glVertexPointer(int size, int type, int stride, FloatBufferAble pointer) {
	if(pointer == null) {
		gl2.glVertexPointer(size,type,stride,0);
	}
	else {
//		if( pointer instanceof SbVec3fArray) {
//			SbVec3fArray array = (SbVec3fArray)pointer;
//			try ( MemoryStack stack = stackPush() ) {
//				int sizeArray = array.getSizeFloat();
//				FloatBuffer buf = stack.malloc(sizeArray*Float.BYTES).asFloatBuffer();
//				array.copyIn(buf);
//				gl2.glVertexPointer(size,type,stride,buf);
//			}
//		}
//		else {
			gl2.glVertexPointer(size,type,stride,pointer.toFloatBuffer());
//		}
	}
}

public void glVertexAttrib1fARB(int index, float x) {
	gl2.glVertexAttrib1fARB(index,x);
}

public void glVertexAttrib2fvARB(int index, float[] v) {
	gl2.glVertexAttrib2fvARB(index,v);
}

public void glVertexAttrib3fvARB(int index, float[] v) {
	gl2.glVertexAttrib3fvARB(index,v);
}

public void glVertexAttrib4fvARB(int index, float[] v) {
	gl2.glVertexAttrib4fvARB(index,v);
}

public void glVertexAttrib1sARB(int index, short x) {
	gl2.glVertexAttrib1sARB(index,x);
}

public void glDisableVertexAttribArrayARB(int index) {
	gl2.glDisableVertexAttribArrayARB(index);
}

public void glEnableVertexAttribArrayARB(int index) {
	gl2.glEnableVertexAttribArrayARB(index);
}

public int glGetAttribLocationARB(int programobj, String name) {
	return gl2.glGetAttribLocationARB(programobj,name);
}

public void glVertexAttribPointerARB(int index, int size, int type, boolean normalized, int stride, Object pointer) {
	if(pointer == null) {
		gl2.glVertexAttribPointer/*ARB*/(index,size,type,normalized,stride,/*pointer*/0);
	}
	else if (pointer instanceof FloatBufferAble) {
		gl2.glVertexAttribPointer/*ARB*/(index,size,type,normalized,stride,((FloatBufferAble)pointer).toFloatBuffer());
	}
	else if (pointer instanceof ShortBufferAble) {
		gl2.glVertexAttribPointer/*ARB*/(index,size,type,normalized,stride,((ShortBufferAble)pointer).toShortBuffer());
	}
}

public void glDisableClientState(int array) {
	gl2.glDisableClientState(array);
}

public void glDrawElements(int mode, int count, int type, /*VoidPtr*/IntArrayPtr indices) {
	if(indices == null) {
		gl2.glDrawElements(mode, count, type, 0);		
	}
	else {
		gl2.glDrawElements(mode, count, type, indices);
	}
}

public void glMultiDrawElements(int mode, IntArrayPtr count, int type, VoidPtr[] indices, int primcount) {
	gl2.glMultiDrawElements(mode,count,type,indices,primcount);
}

public void glBlendFuncSeparate(int rgbsrc, int rgbdst, int alphasrc, int alphadst) {
	gl2.glBlendFuncSeparate( rgbsrc, rgbdst, alphasrc, alphadst);
}

public void glMultiTexCoord2f(int target, float s, float t) {
	gl2.glMultiTexCoord2f(target, s, t);
}

public void glDeleteBuffers(int n, int[] buffers) {
	gl2.glDeleteBuffers(n,buffers);
}

public void glGenProgramsARB( int n, int[] ids) {
	gl2.glGenProgramsARB(n,ids);
}

public void glBindProgramARB( int code, int id) {
	gl2.glBindProgramARB(code, id);
}

public void glProgramStringARB( int a, int b, int c, String str) {
	gl2.glProgramStringARB( a, b, c, str);
}
}
