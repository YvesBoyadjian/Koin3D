/**
 * 
 */
package jexample.parts;

import jscenegraph.database.inventor.SbColor;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.nodes.SoCamera;
import jscenegraph.database.inventor.nodes.SoCone;
import jscenegraph.database.inventor.nodes.SoDirectionalLight;
import jscenegraph.database.inventor.nodes.SoLight;
import jscenegraph.database.inventor.nodes.SoMaterial;
import jscenegraph.database.inventor.nodes.SoPerspectiveCamera;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.database.inventor.nodes.SoSphere;
import jscenegraph.interaction.inventor.manips.SoHandleBoxManip;
import jscenegraph.interaction.inventor.manips.SoTrackballManip;

/**
 * @author Yves Boyadjian
 *
 */
public class Manips {

	public static SoSeparator createScene() {
		SoSeparator root = new SoSeparator();
		root.ref();
		
		SoCamera camera = new SoPerspectiveCamera();
		camera.position.setValue(0,0,10);
		camera.focalDistance.setValue(10);
		camera.farDistance.setValue(100);
		SoLight light = new SoDirectionalLight();
		root.addChild(camera);
		root.addChild(light);
		
		SoSeparator sep = new SoSeparator();
		SoMaterial material = new SoMaterial();
		
		SoHandleBoxManip hbox = new SoHandleBoxManip();
		hbox.translation.setValue(0,-2,0);
		
		SbColor ambient_diffuse = new SbColor(1,0,0.4f);
		material.diffuseColor.setValue(ambient_diffuse);
		material.ambientColor.setValue(ambient_diffuse);
		material.specularColor.setValue(new SbColor(0.6f,0.6f,0.6f));
		material.shininess.setValue(0.03f);
		
		SoSphere sphere = new SoSphere();
		sphere.radius.setValue(1.1f);
		
		sep.addChild(material);
		sep.addChild(hbox);
		sep.addChild(sphere);
		root.addChild(sep);
		
		sep = new SoSeparator();
		material = new SoMaterial();
		ambient_diffuse = new SbColor(0.2f,0.6f,0.6f);
		material.diffuseColor.setValue(ambient_diffuse);
		material.ambientColor.setValue(ambient_diffuse);
		material.specularColor.setValue(new SbColor(0.6f,0.6f,0.6f));
		material.shininess.setValue(0.1f);
		
		SoTrackballManip tball = new SoTrackballManip();
		tball.translation.setValue(0,2,0);
		tball.rotation.setValue(new SbVec3f(-0.848f,0,0.53f),0.8f);
		tball.scaleFactor.setValue(1,1.7f,1);
		
		sep.addChild(material);
		sep.addChild(tball);
		sep.addChild(new SoCone());
		root.addChild(sep);
		
		return root;
	}
}
