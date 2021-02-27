/**
 * 
 */
package jexample.parts;

import jscenegraph.coin3d.inventor.SoOffscreenRenderer;
import jscenegraph.coin3d.inventor.engines.SoInterpolateVec3f;
import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SbRotation;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbViewportRegion;
import jscenegraph.database.inventor.SoDB;
import jscenegraph.database.inventor.nodes.SoCube;
import jscenegraph.database.inventor.nodes.SoDirectionalLight;
import jscenegraph.database.inventor.nodes.SoPerspectiveCamera;
import jscenegraph.database.inventor.nodes.SoSeparator;

/**
 * @author Yves Boyadjian
 *
 */
public class Offscreen {

	public static void main(String[] args) {
		// Init Coin
		SoDB.init();

		// The root node
		SoSeparator root = new SoSeparator();
		root.ref();

		// It is mandatory to have at least one light for the offscreen renderer
		SoDirectionalLight light = new SoDirectionalLight();
		root.addChild(light);

		// It is mandatory to have at least one camera for the offscreen renderer
		SoPerspectiveCamera camera = new SoPerspectiveCamera();
		final SbRotation cameraRotation = SbRotation.identity();
		cameraRotation.operator_mul_equal( new SbRotation(new SbVec3f(1, 0, 0), -0.4f));
		cameraRotation.operator_mul_equal( new SbRotation(new SbVec3f(0, 1, 0), 0.4f));
		camera.orientation.setValue( cameraRotation);
		root.addChild(camera);

		// Something to show... A box
		SoCube cube = new SoCube();
		root.addChild(cube);

		// Set up the two camera positions we want to move the camera between
		SoInterpolateVec3f interpolate = new SoInterpolateVec3f();
		interpolate.input0.setValue( new SbVec3f(2, 2, 9));
		interpolate.input1.setValue( new SbVec3f(2, 2, 5));
		camera.position.connectFrom(interpolate.output);

		// Set up the offscreen renderer
		final SbViewportRegion vpRegion = new SbViewportRegion((short)400, (short)300);
		final SoOffscreenRenderer offscreenRenderer = new SoOffscreenRenderer(vpRegion);

		// How many frames to render for the video
		int frames = 5;
		System.out.println( "Writing " + frames + " frames...");

		for (int i = 0; i < frames; i++) {
			// Update the camera position
			interpolate.alpha.setValue( (float)(i) / (frames - 1));

			// Render the scene
			boolean ok = offscreenRenderer.render(root);

			// Save the image to disk
			String filename = new String("coinvideo-") + (i + 1) + ".jpg";
			if (ok) {
				offscreenRenderer.writeToFile(filename, new SbName("jpg"));
			}
			else {
				System.out.println( "Error saving image: " + filename );
				break;
			}
		}

		System.out.println( "Done!" );

		root.unref();
	}
}
