package jexample.parts;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

import jscenegraph.database.inventor.SoDB;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.nodes.SoCallback;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.port.Ctx;

public class Shadows {

	public static SoSeparator main()
	{

	  // The root of a scene graph
	  SoSeparator root = new SoSeparator();
	  root.ref();
	  
		SoInput input = new SoInput();

		String str = "C:/Coin3D/shadow.iv";
		//String str = "C:/Coin3D/spotlight.iv";
		//String str = "C:/Coin3D/texture.iv";
		input.openFile(str);
		
		SoCallback callback = new SoCallback();
		
		callback.setCallback(action -> {
			if(action instanceof SoGLRenderAction) {
				SoGLRenderAction renderAction = (SoGLRenderAction) action;
				GL2 gl2 = Ctx.get(renderAction.getCacheContext());
				gl2.glEnable(GL.GL_FRAMEBUFFER_SRGB);
			}
		});
		
		root.addChild(callback);

		root.addChild(SoDB.readAll(input));

		input.destructor();	  
	  
	  return root;
	}
}
