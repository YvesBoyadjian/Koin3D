/**
 * 
 */
package jexample.parts;

import jscenegraph.database.inventor.SbColor;
import jscenegraph.database.inventor.SbVec2f;
import jscenegraph.database.inventor.nodes.SoFont;
import jscenegraph.database.inventor.nodes.SoLinearProfile;
import jscenegraph.database.inventor.nodes.SoMaterial;
import jscenegraph.database.inventor.nodes.SoMaterialBinding;
import jscenegraph.database.inventor.nodes.SoProfileCoordinate2;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.database.inventor.nodes.SoText3;

/**
 * @author Yves Boyadjian
 *
 */
public class FancyText3 {

	public static SoSeparator createScene() {
		SoSeparator root = new SoSeparator();
		root.ref();
		
		SoMaterial material = new SoMaterial();
		material.diffuseColor.set1Value(0, new SbColor(0.8f,0.6f,0));
		material.diffuseColor.set1Value(1, new SbColor(0.1f,0.1f,0.7f));
		material.diffuseColor.set1Value(2, new SbColor(0.1f,0.6f,0.7f));
		material.specularColor.setValue(1,1,1);
		material.shininess.setValue(0.5f);
		root.addChild(material);
		
		SoFont font = new SoFont();
		font.name.setValue("Times");
		font.size.setValue(1.0f);
		root.addChild(font);
		
		SoProfileCoordinate2 pc = new SoProfileCoordinate2();
		SbVec2f[] coords = new SbVec2f[4];
		coords[0] = new SbVec2f(0,0);
		coords[1] = new SbVec2f(0.05f,0.05f);
		coords[2] = new SbVec2f(0.25f,0.05f);
		coords[3] = new SbVec2f(0.3f,0);
		pc.point.setValues(0, coords);
		root.addChild(pc);
		
		SoLinearProfile lp = new SoLinearProfile();
		int[] index = new int[4];
		index[0] = 0;
		index[1] = 1;
		index[2] = 2;
		index[3] = 3;
		lp.index.setValues(0, index);
		root.addChild(lp);
		
		SoMaterialBinding mb = new SoMaterialBinding();
		mb.value.setValue(SoMaterialBinding.Binding.PER_PART);
		root.addChild(mb);
		
		SoText3 text = new SoText3();
		String[] strings = new String[2];
		strings[0] = "Foo";
		strings[1] = "Bar";
		text.string.setValues(0, strings);
		text.justification.setValue(SoText3.Justification.CENTER);
		text.parts.setValue(SoText3.Part.ALL);
		root.addChild(text);
		
		return root;
	}
}
