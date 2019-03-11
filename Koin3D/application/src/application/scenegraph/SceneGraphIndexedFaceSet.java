/**
 * 
 */
package application.scenegraph;

import java.awt.Color;
import java.awt.image.Raster;
import java.util.Random;

import com.jogamp.opengl.GL2;

import jscenegraph.coin3d.inventor.nodes.SoVertexProperty;
import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbColor;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.nodes.SoCallback;
import jscenegraph.database.inventor.nodes.SoDirectionalLight;
import jscenegraph.database.inventor.nodes.SoIndexedFaceSet;
import jscenegraph.database.inventor.nodes.SoLight;
import jscenegraph.database.inventor.nodes.SoMaterial;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoQuadMesh;
import jscenegraph.database.inventor.nodes.SoSeparator;
import jscenegraph.database.inventor.nodes.SoSphere;
import jscenegraph.database.inventor.nodes.SoTranslation;
import jscenegraph.port.Ctx;

/**
 * @author Yves Boyadjian
 *
 */
public class SceneGraphIndexedFaceSet implements SceneGraph {		
	
	private static final float ZMAX = 4392.473f;
	
	private static final float ZMIN = 523.63275f;
	
	private static final int I_START = 2500;
	
	private static final int MAX_I = 20000;//9000;
	
	private static final int MAX_J = 9000;
	
	private static final float ALPINE_HEIGHT = 2000f;
	
	private static final float SUN_DISTANCE = 150e9f;
	
	private static final float SUN_RADIUS = 7e8f;
	
	private static final SbColor SUN_COLOR = new SbColor(1f, 0.87f, 0.7f);
	
	private static final SbColor SKY_COLOR = new SbColor(0.3f, 0.3f, 0.5f);
	
	private static final Color STONE = new Color(139,141,122); //http://www.colourlovers.com/color/8B8D7A/stone_gray
	
	private static final float GRASS_LUMINOSITY = 0.6f;
	
	private SoSeparator sep = new SoSeparator();
	
	private SoTranslation transl = new SoTranslation();

	private SoIndexedFaceSet indexedFaceSet;
	
	private float centerX;
	
	private float centerY;
	
	private SoDirectionalLight sun;
	
	private SoDirectionalLight[] sky;

	private SoSeparator sunSep = new SoSeparator();
	
	private SoTranslation sunTransl = new SoTranslation();
	
	private SoSphere sunView;
	
	
	public SceneGraphIndexedFaceSet(Raster rw, Raster re, int overlap) {
		super();
		
		int hImageW = rw.getHeight();
		int wImageW = rw.getWidth();
		
		int hImageE = re.getHeight();
		int wImageE = re.getWidth();
		
		int h = Math.min(hImageW, MAX_J);
		int w = Math.min(wImageW+wImageE-I_START-overlap, MAX_I);
		
		float West_Bounding_Coordinate = -122.00018518518522f;
	      float East_Bounding_Coordinate= -121.74981481481484f;
	      float North_Bounding_Coordinate= 47.000185185185195f;
	      float South_Bounding_Coordinate= 46.749814814814826f;
	      
	      float delta_y_deg = (North_Bounding_Coordinate - South_Bounding_Coordinate)/wImageW;
	      float delta_x_deg = (East_Bounding_Coordinate - West_Bounding_Coordinate)/hImageW;
	      
	      float earth_radius = 6371e3f;
	      float earth_circumference = earth_radius * 2 * (float)Math.PI;
	      
	      float delta_y = earth_circumference * delta_y_deg / 360.0f;
	      float delta_x = earth_circumference * delta_x_deg / 360.0f * (float)Math.cos(South_Bounding_Coordinate*Math.PI/180);
	      
	      float width = delta_x * w;
	      float height = delta_y * h;
	      
	      centerX = width/2;
	      centerY = height/2;
		
		int nbVertices = w*h;
		
		float[] vertices = new float[nbVertices*3];
		float[] normals = new float[nbVertices*3];
		int[] colors = new int[nbVertices];
		float[] fArray = new float[1];
		
		int nbCoordIndices = (w-1)*(h-1)*5;
		int[] coordIndices = new int[nbCoordIndices];
		
		Color snow = new Color(0.97f,1.0f,1.0f);

		
		Color colorStone = STONE;
		int redStone = colorStone.getRed()*colorStone.getRed()/255;
		int greenStone = colorStone.getGreen()*colorStone.getGreen()/255;
		int blueStone = colorStone.getBlue()*colorStone.getBlue()/255;
		
		final int rgbaStone = (colorStone.getAlpha() << 0) | (redStone << 24)| (greenStone << 16)|(blueStone<<8); 
		
		
		float delta = 0;
		int nb = 0;
		for(int j=hImageW/4;j<hImageW*3/4;j++) {
			float zw = rw.getPixel(wImageW-1, j, fArray)[0];
			float ze = re.getPixel(overlap-1, j, fArray)[0];
		
			delta += (ze - zw);
			nb++;
		}
		delta /= nb;
		
		SbBox3f sceneBox = new SbBox3f();
		SbVec3f sceneCenter = new SbVec3f();
		SbVec3f ptV = new SbVec3f();
		
		float zmin = Float.MAX_VALUE;
		float zmax = -Float.MAX_VALUE;
		Random random = new Random();
		for(int i=0;i<w;i++) {
		for(int j=0; j<h;j++) {
				int index = i*h+j;
				vertices[index*3+0] = i * delta_x;
				vertices[index*3+1] = (h - j -1) * delta_y;
				float z = ((i+I_START) >= wImageW ? re.getPixel(i+I_START-wImageW+overlap, j, fArray)[0] - delta : rw.getPixel(i+I_START, j, fArray)[0]);
				if( Math.abs(z)> 1e30) {
					z= ZMIN;
				}
				else {
					zmin = Math.min(zmin, z);
					zmax = Math.max(zmax, z);
				}
				vertices[index*3+2] = z;
				
				ptV.setValue(vertices[index*3+0],vertices[index*3+1], vertices[index*3+2]);
				sceneBox.extendBy(ptV);
				
				Color color = snow;
				
				if(z < ALPINE_HEIGHT + 400 * (random.nextDouble()-0.3)) {
					color = new Color((float)random.nextDouble()*GRASS_LUMINOSITY, 1.0f*GRASS_LUMINOSITY, (float)random.nextDouble()*0.75f*GRASS_LUMINOSITY);
				}
				int red = color.getRed()*color.getRed()/255;
				int green = color.getGreen()*color.getGreen()/255;
				int blue = color.getBlue()*color.getBlue()/255;
				
				int rgba = (color.getAlpha() << 0) | (red << 24)| (green << 16)|(blue<<8); 
				colors[index] = rgba;
			}
		}
		
		sceneCenter.setValue(sceneBox.getCenter());
		
		SbVec3f p0 = new SbVec3f();
		SbVec3f p1 = new SbVec3f();
		SbVec3f p2 = new SbVec3f();
		SbVec3f p3 = new SbVec3f();
		SbVec3f v0 = new SbVec3f();
		SbVec3f v1 = new SbVec3f();
		SbVec3f n = new SbVec3f();
		for(int i=1;i<w-1;i++) {
		for(int j=1; j<h-1;j++) {
			int index = i*h+j;
			int index0 = (i-1)*h+j;
			int index1 = i*h+j+1;
			int index2 = (i+1)*h+j;
			int index3 = i*h+j-1;
			p0.setValue(vertices[index0*3],vertices[index0*3+1],vertices[index0*3+2]);
			p1.setValue(vertices[index1*3],vertices[index1*3+1],vertices[index1*3+2]);
			p2.setValue(vertices[index2*3],vertices[index2*3+1],vertices[index2*3+2]);
			p3.setValue(vertices[index3*3],vertices[index3*3+1],vertices[index3*3+2]);
			v0.setValue(p0.operator_minus_equal(p2));
			v1.setValue(p1.operator_minus_equal(p3));
			n.setValue(v0.operator_cross_equal(v1));
			n.normalize();
			normals[index*3+0] = n.getX();
			normals[index*3+1] = n.getY();
			normals[index*3+2] = n.getZ();

			if((n.getZ()<0.45 && vertices[index*3+2] < ALPINE_HEIGHT) || n.getZ()<0.35) {
				colors[index] = rgbaStone;
			}
		}
		}
		
		int indice=0;
		for(int i=1;i<w;i++) {
		for(int j=1; j<h;j++) {
			coordIndices[indice++] = (i-1)*h+(j-1); 
			coordIndices[indice++] = (i)*h+(j-1); 
			coordIndices[indice++] = (i)*h+(j); 
			coordIndices[indice++] = (i-1)*h+(j); 
			coordIndices[indice++] = -1; 
		}
		}
		// Define coordinates
	    //SoCoordinate3 coords = new SoCoordinate3();
	    //coords.point.setValues(0, vertices);
	    //sep.addChild(coords);
	    
	    SoVertexProperty vertexProperty = new SoVertexProperty();
	    
	    vertexProperty.vertex.setValuesPointer(/*0,*/ vertices);
	    vertexProperty.normalBinding.setValue(SoVertexProperty.Binding.PER_VERTEX_INDEXED);
	    vertexProperty.normal.setValuesPointer(/*0,*/ normals);
	    vertexProperty.materialBinding.setValue(SoVertexProperty.Binding.PER_VERTEX_INDEXED);
	    vertexProperty.orderedRGBA.setValues(0, colors);
	    
	    indexedFaceSet = new SoIndexedFaceSet() {
	    	public void computeBBox(SoAction action, SbBox3f box, SbVec3f center) {
	    		box.copyFrom(sceneBox);
	    		center.copyFrom(sceneCenter);
	    	}
	    };
	    
	    indexedFaceSet.vertexProperty.setValue(vertexProperty);
	    indexedFaceSet.coordIndex.setValues(0, coordIndices);
	    
	    
	    SoCallback callback = new SoCallback();
	    
	    callback.setCallback(action -> {
	    	if(action instanceof SoGLRenderAction) {
	    		SoGLRenderAction glRenderAction = (SoGLRenderAction)action;
	    		GL2 gl2 = Ctx.get(glRenderAction.getCacheContext());
	    		gl2.glEnable(GL2.GL_FRAMEBUFFER_SRGB);
	    	}
	    });
	    
	    sep.addChild(callback);
	    
	    sunView = new SoSphere();
	    sunView.radius.setValue(SUN_RADIUS/SUN_DISTANCE*1e6f);
	    
	    SoMaterial sunMat = new SoMaterial();
	    sunMat.emissiveColor.setValue(SUN_COLOR);
	    
	    sunSep.addChild(sunMat);
	    sunSep.addChild(sunTransl);
	    sunSep.addChild(sunView);
	    sep.addChild(sunSep);
	    
	    sun = new SoDirectionalLight();
	    sun.color.setValue(SUN_COLOR);
	    sep.addChild(sun);
	    
	    sky = new SoDirectionalLight[4];
	    sky[0] = new SoDirectionalLight();
	    sky[0].color.setValue(SKY_COLOR);
	    sky[0].intensity.setValue(0.1f);
	    sky[0].direction.setValue(0, 1, -1);
	    sky[1] = new SoDirectionalLight();
	    sky[1].color.setValue(SKY_COLOR);
	    sky[1].intensity.setValue(0.1f);
	    sky[1].direction.setValue(0, -1, -1);
	    sky[2] = new SoDirectionalLight();
	    sky[2].color.setValue(SKY_COLOR);
	    sky[2].intensity.setValue(0.1f);
	    sky[0].direction.setValue(1, 0, -1);
	    sky[3] = new SoDirectionalLight();
	    sky[3].color.setValue(SKY_COLOR);
	    sky[3].intensity.setValue(0.1f);
	    sky[3].direction.setValue(-1, 0, -1);
	    
	    sep.addChild(sky[0]);
	    sep.addChild(sky[1]);
	    sep.addChild(sky[2]);
	    sep.addChild(sky[3]);
	    
	    SoMaterial mat = new SoMaterial();
	    mat.ambientColor.setValue(0, 0, 0); // no ambient
	    
	    sep.addChild(mat);
	    
	    sep.addChild(transl);
	    
		sep.addChild(indexedFaceSet);
		
	}
	
	public SoNode getSceneGraph() {
		return sep;
	}

	@Override
	public float getCenterY() {
		return centerY;
	}

	@Override
	public float getCenterX() {
		return centerX;
	}

	@Override
	public void setPosition(float x, float y, float z) {
		transl.translation.setValue(-x,-y,-z);
	}

	@Override
	public void setSunPosition(SbVec3f sunPosition) {
		sun.direction.setValue(sunPosition.operator_minus());
		sunTransl.translation.setValue(sunPosition.operator_mul(1e6f));
	}
}
