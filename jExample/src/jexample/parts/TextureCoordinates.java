/**
 * 
 */
package jexample.parts;

import jscenegraph.coin3d.inventor.nodes.SoCoordinate3;
import jscenegraph.coin3d.inventor.nodes.SoTexture2;
import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.nodes.SoFaceSet;
import jscenegraph.database.inventor.nodes.SoNormal;
import jscenegraph.database.inventor.nodes.SoNormalBinding;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.database.inventor.nodes.SoTextureCoordinate2;
import jscenegraph.database.inventor.nodes.SoTextureCoordinateBinding;

/**
 * @author Yves Boyadjian
 *
 */
public class TextureCoordinates {

	public static SoSeparator createScene() {
		SoSeparator root = new SoSeparator();
		root.ref();
		
		SoTexture2 brick = new SoTexture2();
		root.addChild(brick);
		brick.filename.setValue("C:\\eclipseWorkspaces\\5-1-Tanky1\\maps\\Brkrun.jpg");
		
		SoCoordinate3 coord = new SoCoordinate3();
		root.addChild(coord);
		coord.point.set1Value(0, new SbVec3f(-3,-3,0));
		coord.point.set1Value(1, new SbVec3f(3,-3,0));
		coord.point.set1Value(2, new SbVec3f(3,3,0));
		coord.point.set1Value(3, new SbVec3f(-3,3,0));
		
		SoNormal normal = new SoNormal();
		root.addChild(normal);
		normal.vector.set1Value(0,new SbVec3f(0,0,1));
		
		SoTextureCoordinate2 texCoord = new SoTextureCoordinate2();
		root.addChild(texCoord);
		texCoord.point.set1Value(0, new SbVec2f(0,0));
		texCoord.point.set1Value(1, new SbVec2f(1,0));
		texCoord.point.set1Value(2, new SbVec2f(1,1));
		texCoord.point.set1Value(3, new SbVec2f(0,1));
		
		SoNormalBinding nBind = new SoNormalBinding();
		SoTextureCoordinateBinding tBind = new SoTextureCoordinateBinding();
		root.addChild(nBind);
		root.addChild(tBind);
		nBind.value.setValue(SoNormalBinding.Binding.OVERALL);
		tBind.value.setValue(SoTextureCoordinateBinding.Binding.PER_VERTEX);
		
		SoFaceSet myFaceSet = new SoFaceSet();
		root.addChild(myFaceSet);
		myFaceSet.numVertices.set1Value(0, 4);
		
		return root;
	}
}
