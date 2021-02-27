/**
 * 
 */
package jexample.parts;

import jscenegraph.database.inventor.SoDB;
import jscenegraph.database.inventor.SoPrimitiveVertex;
import jscenegraph.database.inventor.actions.SoCallbackAction;
import jscenegraph.database.inventor.nodes.SoDirectionalLight;
import jscenegraph.database.inventor.nodes.SoMaterial;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoPerspectiveCamera;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.database.inventor.nodes.SoSphere;

/**
 * @author Yves Boyadjian
 *
 */
public class Callback {

	public static void main(String[] args) {
		
		SoDB.init();

		SoSeparator root = new SoSeparator();
		SoPerspectiveCamera myCamera = new SoPerspectiveCamera();
		SoMaterial myMaterial = new SoMaterial();
		root.ref();
		root.addChild(myCamera);
		root.addChild(new SoDirectionalLight());
		myMaterial.diffuseColor.setValue(1.0f,0,0); // Red
		root.addChild(myMaterial);
		root.addChild(new SoSphere());
		
		printSpheres(root);
		
		root.unref();
	}
	
	private static void printSpheres(SoNode root) {
		final SoCallbackAction myAction = new SoCallbackAction();
		
		myAction.addPreCallback(SoSphere.getClassTypeId(),(obj,action,node) ->printHeaderCallback(obj,action,node), null);
		myAction.addTriangleCallback(SoSphere.getClassTypeId(), (obj,action,v1,v2,v3) -> printTriangleCallback(obj,action,v1,v2,v3), null);		
		
		myAction.apply(root);
		
		myAction.destructor();
	}
	
	public static SoCallbackAction.Response printHeaderCallback(Object obj, SoCallbackAction action, SoNode node) {
		System.out.print("Sphere named "+node.getName()+" at address "+node.getAddress());
		
		return SoCallbackAction.Response.CONTINUE;
	}
	
	public static void printTriangleCallback(Object obj, SoCallbackAction action, SoPrimitiveVertex vertex1,SoPrimitiveVertex vertex2,SoPrimitiveVertex vertex3) {
		
	}
}
