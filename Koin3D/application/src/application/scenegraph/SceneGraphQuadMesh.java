/**
 * 
 */
package application.scenegraph;

import java.awt.image.Raster;

import jscenegraph.coin3d.inventor.nodes.SoVertexProperty;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.nodes.SoCone;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoQuadMesh;
import jscenegraph.database.inventor.nodes.SoSeparator;

/**
 * @author Yves Boyadjian
 *
 */
public class SceneGraphQuadMesh implements SceneGraph {
	
	private static final int I_START = 2500;
	
	private static final int MAX_I = 6000;
	
	private static final int MAX_J = 900;
	
	private SoSeparator sep = new SoSeparator();

	private SoQuadMesh quadMesh = new SoQuadMesh();
	
	public SceneGraphQuadMesh(Raster r) {
		super();
		
		int h = r.getHeight();
		int w = r.getWidth();
		
		h = Math.min(h, MAX_J);
		w = Math.min(w-I_START, MAX_I);
		
		quadMesh.verticesPerRow.setValue(h);
		quadMesh.verticesPerColumn.setValue(w);
		
		int nbVertices = w*h;
		
		float[][] vertices = new float[nbVertices][3];
		float[] fArray = new float[1];
		
		for(int i=0;i<w;i++) {
		for(int j=0; j<h;j++) {
				int index = i*h+j;
				vertices[index][0] = i;
				vertices[index][1] = j;
				float z = r.getPixel(i+I_START, j, fArray)[0];
				if( Math.abs(z)> 1e30) {
					z= 0;
				}
				vertices[index][2] = z;
			}
		}
		
		// Define coordinates
//	    SoCoordinate3 coords = new SoCoordinate3();
//	    coords.point.setValues(0, vertices);
//	    sep.addChild(coords);
	    
	    SoVertexProperty vertexProperty = new SoVertexProperty();
	    
	    vertexProperty.vertex.setValues(0, vertices);
	    
	    quadMesh.vertexProperty.setValue(vertexProperty);
		
		sep.addChild(quadMesh);
	}

	public SoNode getSceneGraph() {
		return sep;
	}

	@Override
	public float getCenterY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getCenterX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setPosition(float x, float y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSunPosition(SbVec3f sunPosition) {
		// TODO Auto-generated method stub
		
	}
	
	public float getZ(float x, float y, float z) {
		return 0;
	}

	@Override
	public void preDestroy() {
		// TODO Auto-generated method stub
		
	}
}
