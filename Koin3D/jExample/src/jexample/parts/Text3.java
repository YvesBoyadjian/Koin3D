/**
 * 
 */
package jexample.parts;

import jscenegraph.database.inventor.nodes.SoFont;
import jscenegraph.database.inventor.nodes.SoMaterial;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.database.inventor.nodes.SoText3;

/**
 * @author Yves Boyadjian
 *
 */
public class Text3 {

	public static SoSeparator createScene() {
		SoSeparator root = new SoSeparator();
		root.ref();
		
		SoMaterial mat = new SoMaterial();
		mat.diffuseColor.setValue(1,1,0);
		root.addChild(mat);
		
		SoFont font = new SoFont();
		font.name.setValue("times");
		font.size.setValue(1.0f);
		root.addChild(font);
		
		SoText3 text = new SoText3();
		String[] str = { "BOA", "BAO"};
		text.string.setValues(0,str);
		root.addChild(text);
		
		return root;
	}
}
