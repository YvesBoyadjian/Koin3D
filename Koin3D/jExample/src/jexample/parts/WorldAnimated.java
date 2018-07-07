/**
 * 
 */
package jexample.parts;

import jscenegraph.database.inventor.engines.SoCalculator;
import jscenegraph.database.inventor.engines.SoTimeCounter;
import jscenegraph.database.inventor.nodes.SoRotationXYZ;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.database.inventor.nodes.SoSphere;
import jscenegraph.database.inventor.nodes.SoTexture2;

/**
 * @author Yves Boyadjian
 *
 */
public class WorldAnimated {


private static final SoSeparator drawEarth()
{
  // A node for the whole Earth object
  SoSeparator earth = new SoSeparator();
  
  // A node for the texture
  SoTexture2 texture_earth = new SoTexture2();
  
  // Name of texture file
  texture_earth.filename.setValue( "D:\\ACP\\EXAMPLES\\CH10\\ANIMATIONS\\ROTATING_EARTH\\src\\world32k.jpg");
  
  // Add texture to group node
  earth.addChild(texture_earth);
  
  // Add sphere to group node
  // and draws the texture image
  // on the surface of the sphere shape
  earth.addChild(new SoSphere());
  
  return earth;
}



public static final SoSeparator main()
{
  
  // init the main node of the "scene graph"
  SoSeparator root = new SoSeparator();
  root.ref();
  
  // A separator for the Earth
  SoSeparator earth = new SoSeparator();
  
  // Create a rotation node
  // and add it to the local 'earth' separator
  // *before* adding the Earth shape object
  SoRotationXYZ earthRotation = new SoRotationXYZ();
  earthRotation.axis.setValue("Y"); // set the rotation axis
  earth.addChild(earthRotation);
  
  // Create the Earth object
  // and add it to the local 'earth' separator
  earth.addChild( drawEarth() );
  
  // Create a 'TimeCounter' engine node
  // It is an integer counter, with one output
  SoTimeCounter counterEngine = new SoTimeCounter();
  counterEngine.max.setValue((short)360);  // set max steps
  counterEngine.step.setValue((short)1);   // set step value
  counterEngine.frequency.setValue(0.03f); // set the frequency (# of complete cycles min->max per second)
  
  // Create a converter using a 'Calculator' engine node
  // SoCalculator has 8 scalar inputs [a,...,h]
  // and 4 scalar outputs [oa, ..., od]
  // We convert Degrees -> Radians on-the-fly
  // using the input 'a' and the output 'oa'
  SoCalculator converterDegRad = new SoCalculator();
  converterDegRad.a.connectFrom( counterEngine.output ); // connect 'output' of the counter engine to 'a'
  converterDegRad.expression.set1Value(0,"oa=a/(2*M_PI)"); // set the (Deg->Rad) conversion formula
  
  // Connect the converter output 'oa'
  // to the 'angle' attribute Earth rotation node
  earthRotation.angle.connectFrom( converterDegRad.oa );
  
  // Add the Earth node to the root node
  root.addChild( earth );
 
  return root;
}  
}
