/**
 * 
 */
package jexample.parts;

import jscenegraph.coin3d.fxviz.nodes.SoShadowDirectionalLight;
import jscenegraph.coin3d.fxviz.nodes.SoShadowGroup;
import jscenegraph.coin3d.inventor.nodes.SoTexture2;
import jscenegraph.coin3d.inventor.nodes.SoVertexProperty;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.nodes.SoDirectionalLight;
import jscenegraph.database.inventor.nodes.SoIndexedFaceSet;
import jscenegraph.database.inventor.nodes.SoLight;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.port.memorybuffer.MemoryBuffer;

/**
 * @author Yves Boyadjian
 *
 */
public class ShadowTest {

	public static SoSeparator create() {
		
		SoSeparator.setNumRenderCaches(0);
		
//		SoShadowGroup shadowGroup = new SoShadowGroup();
//		shadowGroup.quality.setValue(1);
//		shadowGroup.precision.setValue(1);
		SoSeparator shadowGroup = new SoSeparator();

//		SoShadowDirectionalLight sun = new SoShadowDirectionalLight();
		SoDirectionalLight sun = new SoDirectionalLight();
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

		float[] xy = {
			0,0,
			0,1,
			1,1,
			1,0,
			0,0,
			0,1,
			1,1,
			1,0
		};
		
	    SoVertexProperty vertexProperty = new SoVertexProperty();
	    for(int i=0;i<10;i++) {
	    	vertexProperty.vertex.setValues(i*8,xyz);
	    	vertexProperty.normal.setValues(i*8,xyz);
	    	vertexProperty.texCoord.setValues(i*8, xy);
	    }
	    int[] indices = {0,1,2,3, -1, 4,5,6,7,-1,0,1,2,3, -1,4,5,6,7,-1};
	    
	    //float[] xy = new float[indices.length*2];
	    //vertexProperty.texCoord.setValues(0, xy);
	    vertexProperty.normalBinding.setValue(SoVertexProperty.Binding.PER_VERTEX_INDEXED);
	    	    
	    
	    
	    SoIndexedFaceSet faceSet = new SoIndexedFaceSet();
	    faceSet.vertexProperty.setValue(vertexProperty);
	    for(int i=0;i<10;i++)
	    	faceSet.coordIndex.setValues(i*20, indices);
	    
	    SoTexture2 tex = new SoTexture2();
	    int size = 3;
	    byte [] array = new byte[size*size*3];
		for (int i = 0; i < size*size * 3; i++) {
			array[i] = -1;
		}
	    array[1] = array[3] = array[5] = 0;
		tex.image.setValue(new SbVec2s((short)size,(short)size),3,MemoryBuffer.allocateFromByteArray(array));
	    
	    sep2.addChild(tex);
	    
	    /*shadowGroup*/sep2.addChild(faceSet);
	    
	    shadowGroup.addChild(sep2);

		SoSeparator sep = new SoSeparator(); 
		
		sep.addChild(shadowGroup);

		return sep;
	}
}
