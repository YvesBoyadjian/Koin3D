/**
 * 
 */
package jexample.parts;

import jscenegraph.database.inventor.engines.SoCalculator;
import jscenegraph.database.inventor.engines.SoElapsedTime;
import jscenegraph.database.inventor.nodes.SoMaterial;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.database.inventor.nodes.SoText3;
import jscenegraph.database.inventor.nodes.SoTransform;
import jscenegraph.nodekits.inventor.nodekits.SoShapeKit;

/**
 * @author Yves Boyadjian
 *
 */
public class FrolickingWords {

	public static SoSeparator createRoot() {
		
		SoSeparator root = new SoSeparator();
		root.ref();
		
		SoShapeKit happyKit = new SoShapeKit();
		root.addChild(happyKit);
		happyKit.setPart("shape", new SoText3());
		happyKit.set("shape { parts ALL string \"HAPPY\"}");
		happyKit.set("font { size 2}");
		
		SoShapeKit niceKit = new SoShapeKit();
		root.addChild(niceKit);
		niceKit.setPart("shape", new SoText3());
		niceKit.set("shape { parts ALL string \"NICE\"}");
		niceKit.set("font { size 2}");
		
		SoElapsedTime myTimer = new SoElapsedTime();
		myTimer.ref();
		
		SoCalculator happyCalc = new SoCalculator();
		happyCalc.ref();
		happyCalc.a.connectFrom(myTimer.timeOut);
		happyCalc.expression.setValue("ta=cos(2*a); tb=sin(2*a); oA = vec3f(3*pow(ta,3),3*pow(tb,3),1); oB = vec3f(fabs(ta)+.1,fabs(.5*fabs(tb))+.1,1); oC = vec3f(fabs(ta),fabs(tb),.5)");
		
		SoTransform happyXf = (SoTransform) happyKit.getPart("transform", true);
		happyXf.translation.connectFrom(happyCalc.oA);
		happyXf.scaleFactor.connectFrom(happyCalc.oB);
		
		SoMaterial happyMtl = (SoMaterial) happyKit.getPart("material", true);
		happyMtl.diffuseColor.connectFrom(happyCalc.oC);
		
		
		return root;
	}
}
