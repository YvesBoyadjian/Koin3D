/**
 * 
 */
package jexample.parts;

import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.nodes.SoCallback;
import jscenegraph.database.inventor.nodes.SoCoordinate3;
import jscenegraph.database.inventor.nodes.SoLineSet;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.database.inventor.nodes.SoSphere;
import jscenegraph.database.inventor.nodes.SoTexture2;
import jscenegraph.database.inventor.nodes.SoTranslation;
import jscenegraph.database.inventor.sensors.SoSensor;
import jscenegraph.database.inventor.sensors.SoTimerSensor;

/**
 * @author Yves Boyadjian
 *
 */
public class Orbits {



	private static SoTranslation planetTranslation=null;
	private static double tPhi=0.0;
	private static int i=0;

	public static void callback(Object object, SoSensor sensor) {
		double R=10;
		//tPhi = System.nanoTime()/2.0e9 % (Math.PI*2);
		planetTranslation.translation.setValue((float)(R*Math.cos(tPhi)), (float)(R*Math.sin(tPhi)), 0);
		tPhi+=0.01;
		i++;
		i = i%60;
		if(i==0)
			System.out.println((float)tPhi+" ");
		System.runFinalization();
	}

	public static SoSeparator makeOrbit(double a) {

		SoSeparator sep=new SoSeparator();

		SoCoordinate3 coords=new SoCoordinate3();
		int NPOINTS=100;
		for ( int i=0;i<NPOINTS;i++) {
			double phi=2*Math.PI*i/(double)(NPOINTS-1);
			coords.point.set1Value(i,(float)(a*Math.cos(phi)),(float)(a*Math.sin(phi)),0.0f);
		}
  sep.addChild(coords);
  SoLineSet lineSet=new SoLineSet();
  lineSet.numVertices.setValue(NPOINTS);
  sep.addChild(lineSet);

  return sep;
}




public static SoSeparator main()
{

  // The root of a scene graph
  SoSeparator root = new SoSeparator();
  root.ref();
  
//  SoCallback cb = new SoCallback();
//  cb.setCallback(action -> {
//	  if(action instanceof SoGLRenderAction) {
//		  SoGLRenderAction ra = (SoGLRenderAction)action;
//		  ra.getCacheContext().setSwapInterval(0);
//		  //root.removeChild(cb);
//	  }
//  });
//  root.addChild(cb);
  
  
  final double a=10.0;
  root.addChild(makeOrbit(a));

  {
    SoTexture2 txt=new SoTexture2();
    txt.filename.setValue("D:\\ACP\\EXAMPLES\\CH10\\ANIMATIONS\\ORBITS\\src\\photo.jpg");
    root.addChild(txt);
  }

  
  SoSphere sphere=new SoSphere();
  root.addChild(sphere);

  {
    SoTexture2 txt=new SoTexture2();
    txt.filename.setValue("D:\\ACP\\EXAMPLES\\CH10\\ANIMATIONS\\ORBITS\\src\\photo2.jpg");
    root.addChild(txt);
  }
  
  // Schedule the update right here: 
  SoTimerSensor timerSensor = new SoTimerSensor();
  timerSensor.setFunction(Orbits::callback);
  timerSensor.setInterval(0.01f);
  timerSensor.schedule();
  

  SoSeparator planetSep=new SoSeparator();
  SoSphere    planet = new SoSphere();
  planetTranslation = new SoTranslation();
  planetTranslation.translation.setValue((float)a, 0, 0);
  planetSep.addChild(planetTranslation);
  planetSep.addChild(planet);
  root.addChild(planetSep);
 
  return root;
}

}
