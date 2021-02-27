/**
 * 
 */
package jexample.parts;

import jscenegraph.coin3d.fxviz.nodes.SoShadowDirectionalLight;
import jscenegraph.coin3d.fxviz.nodes.SoShadowGroup;
import jscenegraph.database.inventor.nodes.SoCube;
import jscenegraph.database.inventor.nodes.SoDirectionalLight;
import jscenegraph.database.inventor.nodes.SoEnvironment;
import jscenegraph.database.inventor.nodes.SoMaterial;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.database.inventor.nodes.SoTranslation;

/**
 * @author Yves Boyadjian
 *
 */
public class Fog {

	
	public static SoNode getScene() {
		
		SoSeparator sep1 = new SoSeparator();
		
		SoShadowGroup sep = new SoShadowGroup();
		sep.quality.setValue(1.0f);
		sep.precision.setValue(0.2f);
		sep.intensity.setValue(1.0f);
		sep.threshold.setValue(0.9f);
		sep.epsilon.setValue(3.0e-6f);
		sep.smoothBorder.setValue(1.0f);
		
		SoEnvironment en = new SoEnvironment();
		
		en.fogType.setValue(SoEnvironment.FogType.FOG);
		en.fogVisibility.setValue(10.0f);
		en.fogColor.setValue(0, 1, 0);
		en.attenuation.setValue(0, 0, 0.0001f);
		
		sep1.addChild(en);
		
		SoShadowDirectionalLight light = new SoShadowDirectionalLight();		
		
		sep.addChild(light);
		
	    SoSeparator waterSeparator = new SoSeparator();
	    
		SoTranslation tr = new SoTranslation();
		
		tr.translation.setValue(0, 0, - 10);
		
		waterSeparator.addChild(tr);
		
		SoMaterial mat = new SoMaterial();
		
		mat.diffuseColor.setValue(1, 0, 0);
		mat.transparency.setValue(0.5f);
		mat.ambientColor.setValue(0, 0, 0);
    	mat.specularColor.setValue(1.0f, 1.0f, 1.0f);
    	mat.shininess.setValue(0.5f);
		
    	waterSeparator.addChild(mat);
		
		SoCube cube = new SoCube();
		cube.height.setValue(100);
		
		waterSeparator.addChild(cube);
		
		sep.addChild(waterSeparator);
		
		sep1.addChild(sep);
		
		return sep1;
	}
}
