/**
 * 
 */
package jexample.parts;

import jscenegraph.coin3d.fxviz.nodes.SoShadowDirectionalLight;
import jscenegraph.coin3d.fxviz.nodes.SoShadowGroup;
import jscenegraph.coin3d.inventor.nodes.SoVertexProperty;
import jscenegraph.database.inventor.nodes.SoIndexedFaceSet;
import jscenegraph.database.inventor.nodes.SoSeparator;

/**
 * @author Yves Boyadjian
 *
 */
public class ShadowTest {

	public static SoSeparator create() {
		
		SoShadowGroup shadowGroup = new SoShadowGroup();
		shadowGroup.quality.setValue(1);
		shadowGroup.precision.setValue(1);

		SoShadowDirectionalLight sun = new SoShadowDirectionalLight();
		sun.direction.setValue(1, 1, -1);
		sun.intensity.setValue(0.5f);
	    
		shadowGroup.addChild(sun);
		
		SoSeparator sep2 = new SoSeparator();
		
		float[] xyz = { 
				-15e4f, -15e4f, -2e4f, 
				15e4f, -15e4f, -2e4f, 
				15e4f, 15e4f, -2e4f, 
				-15e4f, 15e4f, -2e4f,
				
				-1e4f,-1e4f, 2e4f,
				1e4f,-1e4f, 2e4f,
				1e4f,1e4f, 2e4f,
				-1e4f, 1e4f, 2e4f,
				};

	    SoVertexProperty vertexProperty = new SoVertexProperty();
	    vertexProperty.vertex.setValuesPointer(xyz);
	    	    
	    int[] indices = {0,1,2,3, -1, 4,5,6,7,-1,0,1,2,3, -1,4,5,6,7,-1};
	    
	    SoIndexedFaceSet faceSet = new SoIndexedFaceSet();
	    faceSet.vertexProperty.setValue(vertexProperty);
	    for(int i=0;i<1;i++)
	    faceSet.coordIndex.setValues(i*20, indices);
	    
	    
	    /*shadowGroup*/sep2.addChild(faceSet);
	    
	    shadowGroup.addChild(sep2);

		SoSeparator sep = new SoSeparator(); 
		
		sep.addChild(shadowGroup);

		return sep;
	}
}
