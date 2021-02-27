/**
 * 
 */
package jexample.parts;

import jscenegraph.coin3d.inventor.nodes.SoTexture2;
import jscenegraph.coin3d.inventor.nodes.SoTexture2Transform;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.nodes.SoMaterial;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.database.inventor.nodes.SoSphere;
import jscenegraph.database.inventor.nodes.SoTextureCoordinatePlane;

/**
 * @author Yves Boyadjian
 *
 */
public class TextureCoordinatePlane {

	public static SoSeparator computeRoot() {
		SoSeparator root = new SoSeparator();
		root.ref();
		
		SoTexture2 faceTexture = new SoTexture2();
		root.addChild(faceTexture);
		faceTexture.filename.setValue("C:\\eclipseWorkspaces\\5-1-Tanky1\\maps\\Brkrun.jpg");
		
		SoMaterial myMaterial = new SoMaterial();
		myMaterial.diffuseColor.setValue(1, 1, 1);
		root.addChild(myMaterial);
		
		SoTexture2Transform myTexXf = new SoTexture2Transform();
		myTexXf.translation.setValue(.5f, .5f);
		root.addChild(myTexXf);
		
		SoTextureCoordinatePlane texPlane1 = new SoTextureCoordinatePlane();
		texPlane1.directionS.setValue(new SbVec3f(2,0,0));
		texPlane1.directionT.setValue(new SbVec3f(0,2,0));
		root.addChild(texPlane1);
		root.addChild(new SoSphere());
		
		return root;
	}
}
