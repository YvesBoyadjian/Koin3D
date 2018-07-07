/**
 * 
 */
package jexample.parts;

import jscenegraph.database.inventor.nodes.SoFont;
import jscenegraph.database.inventor.nodes.SoMaterial;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.database.inventor.nodes.SoText2;

/**
 * @author Yves Boyadjian
 *
 */
public class Text2 {

	public static SoSeparator createScene() {
		SoSeparator root = new SoSeparator();
		root.ref();
		
		SoMaterial material = new SoMaterial();
		material.diffuseColor.setValue(1,1,0);
		root.addChild(material);
		
		SoFont font = new SoFont();
		font.name.setValue("times");
		font.size.setValue(48.0f);
		root.addChild(font);
		
		SoText2 text = new SoText2();
		String[] values = {"The quick brown fox", "jumped over the lazy dog"};
		text.string.setValues(0, values);
		text.justification.setValue(SoText2.Justification.CENTER);
		root.addChild(text);
		
		return root;
	}
}
