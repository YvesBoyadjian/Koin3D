/**
 * 
 */
package jexample.parts;

import jscenegraph.coin3d.inventor.SoOffscreenRenderer;
import jscenegraph.database.inventor.SbColor;
import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SbViewportRegion;
import jscenegraph.database.inventor.SoDB;
import jscenegraph.database.inventor.SoInput;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoSearchAction;
import jscenegraph.database.inventor.nodes.SoCamera;
import jscenegraph.database.inventor.nodes.SoLight;
import jscenegraph.database.inventor.nodes.SoOrthographicCamera;
import jscenegraph.database.inventor.nodes.SoSeparator;

/**
 * @author BOYADJIAN
 *
 */
public class OffscreenTest {

	public static void main(String[] argv)
	{
		// ./coin-config --build it ./offscreentest.cpp 
		argv = new String[3];
		argv[1] = "examples_iv/test/eyetPr.wrl";
		argv[2] = "examples_iv/test/output.png";
		  
		  SoDB.init();

		  final SoInput input = new SoInput();
		  boolean ret = input.openFile(argv[1]);
		  assert(ret);

		  SoSeparator root = SoDB.readAll(input);
		  //  FILE *fp = fopen(argv[2], "wb");
		  if (root != null){ // && fp) {
		    //SbViewportRegion vp(640,450); // set size of image here
		    final SbViewportRegion vp = new SbViewportRegion((short)/*4608*/4096,(short)3258); // set size of image here
		    //SbViewportRegion vp(2828,2000); // set size of image here
		    //    SbViewportRegion vp(1414,1000); // set size of image here
		    // SbViewportRegion vp(1000,1414); // set size of image here
		    //         SbViewportRegion vp(3500,3500); // set size of image here
		    // biggest on apollo08
		    //     SbViewportRegion vp(3850,3850); // set size of image here
		    //  SbViewportRegion vp(6515,4608);
		    // SbViewportRegion vp(8504,8504) ; // 18.0 cm width at 1200
		    //    SbViewportRegion vp(4104,4104) ; // 8.7cm width at 1200
		    root.ref();
		    final SoSearchAction sa = new SoSearchAction();
		    sa.setInterest(SoSearchAction.Interest.FIRST);
		       sa.setType(SoLight.getClassTypeId());
		    sa.apply(root);
		    // if no light source, insert one
		    if (sa.getPath() == null) {
		      //              root.insertChild(new SoDirectionalLight, 0);
				     System.out.println("imposed light\n");
		    }
		    // if no camera, insert one
		    sa.reset();
		    sa.setInterest(SoSearchAction.Interest.FIRST);
		    sa.setType(SoCamera.getClassTypeId());
		    sa.apply(root);
		    if (sa.getPath() == null) {
		      System.out.println("imposed camera\n");
		      SoOrthographicCamera cam = new SoOrthographicCamera();
		      root.insertChild(cam, 0);
		      cam.viewAll(root, vp);
		      //     cam.scaleHeight(0.46);
		      cam.focalDistance.setValue(7);     // was 7
		      // good values
		      //      cam.orientation.setValue(1,0,0,-1.27933953231703);
		      //      cam.position.setValue( 0,1.9156525704423,0.574695771132691 );


		      cam.orientation.setValue(0,0,1,0);
		      cam.position.setValue( 0,0,4 );




		      //      cam..setValue( 15.5,-610.07333703717,434.923685363132 );
		      //cam.position.setValue(SbVec3f( 0,0,0 ));
		      cam.nearDistance.setValue(0.1f);
		      cam.farDistance.setValue(100.0f);
		    }

		    final SoOffscreenRenderer orrend = new SoOffscreenRenderer(vp);
		    SoGLRenderAction gl = orrend.getGLRenderAction();
		    //    gl.setTransparencyType(SoGLRenderAction::SORTED_OBJECT_SORTED_TRIANGLE_BLEND);
		    //gl.setTransparencyType(SoGLRenderAction::SORTED_OBJECT_BLEND);
		    //gl.setTransparencyType(SoGLRenderAction::SCREEN_DOOR);
		    gl.setSmoothing(true);
		    gl.setNumPasses(1); // 9 pass antialiasing
		    {int passes;
		      passes=gl.getNumPasses();
		     
		      System.out.println("smoothing passes:"+passes+"\n");
		    }

		    orrend.setBackgroundColor(new SbColor(1.0f, 1.0f, 1.0f));


		    orrend.render(root);
		    orrend.writeToFile(argv[2],new SbName("png"));
		    //        orrend.writeToRGB(fp);
		                    System.out.println ("here\n");
				   
				    //        orrend.writeToPostScript(fp);
		    root.unref();
		    //    fclose(fp);
		  }
		
	}
}
