/**
 * 
 */
package jexample.parts;

import jscenegraph.database.inventor.nodes.SoComplexity;
import jscenegraph.database.inventor.nodes.SoDrawStyle;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.database.inventor.nodes.SoSphere;
import jscenegraph.database.inventor.nodes.SoTexture2;
import jscenegraph.database.inventor.nodes.SoTranslation;

/**
 * @author Yves Boyadjian
 *
 */
public class DualWorld {

	
	  
	public static SoSeparator main()
	{
		boolean wireframe = true;
	  
	  // init the main node of the "scene graph"
	  SoSeparator root = new SoSeparator();
	  root.ref();

	  // Two nodes for the two versions of the Earth
	  SoSeparator earth1 = new SoSeparator();
	  SoSeparator earth2 = new SoSeparator();
	  


	  /* Add a SoComplexity node only to group node 2
	   * Default values:
	   *   Complexity {
	          type OBJECT_SPACE
	          value 0.5
	          textureQuality 0.5
	   *   }
	   */
	  SoComplexity complexity = new SoComplexity();
	  complexity.value.setValue( 1.0f);
	  complexity.textureQuality.setValue( 1.0f);
	complexity.type.setValue(SoComplexity.Type.SCREEN_SPACE);
	  earth2.addChild(complexity);


	  if (wireframe) {
		  /*
		   * Add a SoDrawStyle node to use 'wireframe' rendering
		   * Default values:
		   *   DrawStyle {
	       *      style FILLED
	       *      pointSize 0
	       *      lineWidth 0
	       *      linePattern 0xffff
	       *   }
		   *
		   */
	  SoDrawStyle style1 = new SoDrawStyle();
	  SoDrawStyle style2 = new SoDrawStyle();
	style1.style.setValue( SoDrawStyle.Style.LINES);
	style2.style.setValue( SoDrawStyle.Style.LINES);

	  earth1.addChild(style1);
	  earth2.addChild(style2);
	  }
	  else{

	  // A node for the texture
	  SoTexture2 texture_earth1 = new SoTexture2();
	  SoTexture2 texture_earth2 = new SoTexture2();

	  // Name of texture file
	  texture_earth1.filename.setValue( "D:\\ACP\\EXAMPLES\\CH10\\DUALWORLD\\src\\world32k.jpg");
	  texture_earth2.filename.setValue( "D:\\ACP\\EXAMPLES\\CH10\\DUALWORLD\\src\\world32k.jpg");

	  // Add texture to both groups
	  earth1.addChild(texture_earth1);
	  earth2.addChild(texture_earth2);
	  }

	  SoTranslation earth2Pos = new SoTranslation();
	  earth2Pos.translation.setValue(2.0f, 0, 0);
	  earth2.addChild(earth2Pos);

	  // Create a sphere...
	  SoSphere sphere1 = new SoSphere();
	  SoSphere sphere2 = new SoSphere();
	  sphere1.radius.setValue( 1.0f);
	  sphere2.radius.setValue( 1.0f);

	  // ...and add it to both group nodes
	  earth1.addChild(sphere1);
	  earth2.addChild(sphere2);
	  
	  // Add the Earth node to the root node
	  root.addChild(earth1);
	  root.addChild(earth2);
	  
	  

	  // then, exit.
	  return root;
	}
	
}
