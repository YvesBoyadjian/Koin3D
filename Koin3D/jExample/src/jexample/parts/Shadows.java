package jexample.parts;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL43;
import org.lwjgl.opengl.GLDebugMessageCallback;
import org.lwjgl.opengl.GLDebugMessageCallbackI;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

import jscenegraph.database.inventor.SoDB;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.nodes.SoCallback;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.port.Ctx;

public class Shadows {
	
	static boolean first = true;

	public static SoSeparator main()
	{

	  // The root of a scene graph
	  SoSeparator root = new SoSeparator();
	  root.ref();
	  
		SoInput input = new SoInput();

		//String str = "C:/Coin3D/test.iv";
		//String str = "C:/Coin3D/shadow.iv";
		String str = "C:/Coin3D/shadowSimple.iv";
		//String str = "C:/Coin3D/shadowDirectionalLight.iv";
		//String str = "C:/Coin3D/spotlight.iv";
		//String str = "C:/Coin3D/texture.iv";
		input.openFile(str);
		
		SoCallback callback = new SoCallback();
		
		
		callback.setCallback(action -> {
			if(action instanceof SoGLRenderAction) {
				SoGLRenderAction renderAction = (SoGLRenderAction) action;
				GL2 gl2 = Ctx.get(renderAction.getCacheContext());
				gl2.glEnable(GL.GL_FRAMEBUFFER_SRGB);

				//long userParam = 0;
				
//				GLDebugMessageCallback callbac = GLDebugMessageCallback.cnew GLDebugMessageCallbackI() {
//
//					@Override
//					public void invoke(int arg0, int arg1, int arg2, int arg3, int arg4, long arg5, long arg6) {
//						// TODO Auto-generated method stub
//						int i=0;
//					}
//					
//				};
				
				if(first) {
					int flags = GL11.glGetInteger(GL30.GL_CONTEXT_FLAGS);
					if ((flags & GL43.GL_CONTEXT_FLAG_DEBUG_BIT) != 0) {
					    int i=0;
					}
					GLDebugMessageCallback cb = GLDebugMessageCallback.create((source, type, id, severity, length, message, userParam) -> {
					    // print message
						//System.out.println("gudule");
					});
					org.lwjgl.opengl.GL43.glDebugMessageCallback(cb, 0);
				org.lwjgl.opengl.GL11.glEnable(GL2.GL_DEBUG_OUTPUT_SYNCHRONOUS);
//				org.lwjgl.opengl.GL43.glDebugMessageCallback(callbac, userParam);
				int[] unusedIds = new int [1];
//				org.lwjgl.opengl.GL43.glDebugMessageControl(GL2.GL_DONT_CARE,
//			            GL2.GL_DONT_CARE,
//			            GL2.GL_DONT_CARE,
//			            
//			            unusedIds,
//			            true);
				first = false;
				}
			}
		});
		
		//root.addChild(callback);

		root = SoDB.readAll(input);
		root.ref();

		input.destructor();	  
	  
	  return root;
	}
}
