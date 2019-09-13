/**
 * 
 */
package application.scenegraph;

import jscenegraph.coin3d.fxviz.nodes.SoShadowDirectionalLight;
import jscenegraph.coin3d.fxviz.nodes.SoShadowGroup;
import jscenegraph.coin3d.fxviz.nodes.SoShadowStyle;
import jscenegraph.coin3d.inventor.nodes.SoCoordinate3;
import jscenegraph.coin3d.inventor.nodes.SoVertexProperty;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.nodes.SoCube;
import jscenegraph.database.inventor.nodes.SoFaceSet;
import jscenegraph.database.inventor.nodes.SoIndexedFaceSet;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoSeparator;

/**
 * @author Yves Boyadjian
 *
 */
public class ShadowTestSceneGraph implements SceneGraph {

	private SoSeparator sep = new SoSeparator();
	
	private float centerX;
	
	private float centerY;
	
	SoShadowDirectionalLight sun;
	
	public ShadowTestSceneGraph() {
		//test1();
		//test2();
		test3();
	}
	public void test1() {
		
		SoShadowGroup shadowGroup = new SoShadowGroup();
		shadowGroup.quality.setValue(1);

		SoShadowDirectionalLight sun = new SoShadowDirectionalLight();
	    
		shadowGroup.addChild(sun);
		
		SoShadowStyle shadowStyle = new SoShadowStyle();
		
		shadowStyle.style.setValue(SoShadowStyle.Style.CASTS_SHADOW_AND_SHADOWED);
		
		shadowGroup.addChild(shadowStyle);
	    
		SoCube cube = new SoCube();

		shadowGroup.addChild(cube);

		sep.addChild(shadowGroup);

	}
	public void test2() {
		
		SoShadowGroup shadowGroup = new SoShadowGroup();
		shadowGroup.quality.setValue(1);
		shadowGroup.precision.setValue(1);

		SoShadowDirectionalLight sun = new SoShadowDirectionalLight();
		sun.direction.setValue(1, 1, -1);
		sun.intensity.setValue(0.5f);
	    
		shadowGroup.addChild(sun);
		
		SoShadowStyle shadowStyle = new SoShadowStyle();
		
		shadowStyle.style.setValue(SoShadowStyle.Style.CASTS_SHADOW_AND_SHADOWED);
		
		shadowGroup.addChild(shadowStyle);
		
		SoCoordinate3 coord3 = new SoCoordinate3();
		float[] xyz = { -5, -5, -2, 5, -5, -2, 5, 5, -2, -5, 5, -2 };
	    coord3.point.setValues(0,xyz);
	    
	    shadowGroup.addChild(coord3);
	    
	    SoFaceSet faceSet = new SoFaceSet();
	    faceSet.numVertices.setValue(4);
	    
	    shadowGroup.addChild(faceSet);

		shadowStyle = new SoShadowStyle();
		
		shadowStyle.style.setValue(SoShadowStyle.Style.SHADOWED);
		
		shadowGroup.addChild(shadowStyle);
		
		coord3 = new SoCoordinate3();
		float[] xyz2 = { -10, -10, -3, 10, -10, -3, 10, 10, -3, -10, 10, -3 };
	    coord3.point.setValues(0,xyz2);
	    
	    shadowGroup.addChild(coord3);
	    
	    faceSet = new SoFaceSet();
	    faceSet.numVertices.setValue(4);
	    
	    shadowGroup.addChild(faceSet);

		sep.addChild(shadowGroup);

	}
	
	public void test3() {
		
		SoShadowGroup shadowGroup = new SoShadowGroup();
		shadowGroup.quality.setValue(1);
		shadowGroup.precision.setValue(1);

		sun = new SoShadowDirectionalLight();
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
	    	    
	    int[] indices = {0,1,2,3, -1, 4,5,6,7,-1/*,1,1,1,1, -1,4,4,4,4,-1*/};
	    
	    SoIndexedFaceSet faceSet = new SoIndexedFaceSet();
	    faceSet.vertexProperty.setValue(vertexProperty);
	    for(int i=0;i<1;i++)
	    faceSet.coordIndex.setValues(i*20, indices);
	    
	    
	    /*shadowGroup*/sep2.addChild(faceSet);
	    
	    shadowGroup.addChild(sep2);

		sep.addChild(shadowGroup);

	}
	
	/* (non-Javadoc)
	 * @see application.scenegraph.SceneGraph#getSceneGraph()
	 */
	@Override
	public SoNode getSceneGraph() {
		return sep;
	}

	/* (non-Javadoc)
	 * @see application.scenegraph.SceneGraph#getCenterY()
	 */
	@Override
	public float getCenterY() {
		return centerY;
	}

	/* (non-Javadoc)
	 * @see application.scenegraph.SceneGraph#getCenterX()
	 */
	@Override
	public float getCenterX() {
		return centerX;
	}

	/* (non-Javadoc)
	 * @see application.scenegraph.SceneGraph#setPosition(float, float, float)
	 */
	@Override
	public void setPosition(float x, float y) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see application.scenegraph.SceneGraph#setSunPosition(jscenegraph.database.inventor.SbVec3f)
	 */
	@Override
	public void setSunPosition(SbVec3f sunPosition) {
		SbVec3f dir = sunPosition.operator_minus();
		dir.normalize();
		//sun.direction.setValue(dir);
		//System.out.println( dir.getX()+" "+dir.getY()+" "+dir.getZ());
	}

	
	public float getZ(float x, float y, float z) {
		return 0;
	}
	@Override
	public void preDestroy() {
		// TODO Auto-generated method stub
		
	}
	
}
