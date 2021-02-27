/**
 * 
 */
package application.scenegraph;

import java.awt.Color;
import java.awt.image.Raster;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.jogamp.opengl.GL2;

import jscenegraph.coin3d.fxviz.nodes.SoShadowDirectionalLight;
import jscenegraph.coin3d.fxviz.nodes.SoShadowGroup;
import jscenegraph.coin3d.fxviz.nodes.SoShadowStyle;
import jscenegraph.coin3d.inventor.nodes.SoVertexProperty;
import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbColor;
import jscenegraph.database.inventor.SbRotation;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.actions.SoAction;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.nodes.SoCallback;
import jscenegraph.database.inventor.nodes.SoCamera;
import jscenegraph.database.inventor.nodes.SoCube;
import jscenegraph.database.inventor.nodes.SoDirectionalLight;
import jscenegraph.database.inventor.nodes.SoGroup;
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
	
	private static final float SUN_REAL_DISTANCE = 150e9f;
	
	private static final float SUN_RADIUS = 7e8f;
	
	public static final float SUN_FAKE_DISTANCE = 1e5f;
	
	public static final float WATER_HORIZON = 5e4f;
	
	public static final float WATER_BRIGHTNESS = 0.4f;
	
	private static final SbColor SUN_COLOR = new SbColor(1f, 0.85f, 0.8f);
	
	private static final SbColor SKY_COLOR = new SbColor(0.3f, 0.3f, 0.5f);
	
	private static final float SKY_INTENSITY = 0.2f; 
	
	private static final Color STONE = new Color(139,141,122); //http://www.colourlovers.com/color/8B8D7A/stone_gray
	
	private static final float GRASS_LUMINOSITY = 0.6f;
	
	private SoSeparator sep = new SoSeparator();
	
	private SoTranslation transl = new SoTranslation();
	
	private float zTranslation;

	private ChunkArray chunks;
	
	private float centerX;
	
	private float centerY;
	
	private SoShadowDirectionalLight[] sun = new SoShadowDirectionalLight[4];
	//private SoDirectionalLight sun;
	
	private SoDirectionalLight[] sky;

	private SoSeparator sunSep = new SoSeparator();
	
	private SoTranslation sunTransl = new SoTranslation();
	
	//private SoTranslation inverseSunTransl = new SoTranslation();
	
	private SoSphere sunView;
	
	private SbRotation r1 = new SbRotation();
	private SbRotation r2 = new SbRotation();
	private SbRotation r3 = new SbRotation();
	private SbRotation r4 = new SbRotation();
	
	float delta_y;
	float delta_x;
	int h;
	int w;
	
	int jstart;
	
	SoTouchLODMaster master;
	
	public SceneGraphIndexedFaceSet(Raster rw, Raster re, int overlap, float zTranslation) {
		super();
		this.zTranslation = zTranslation;
		
		int hImageW = rw.getHeight();
		int wImageW = rw.getWidth();
		
		int hImageE = re.getHeight();
		int wImageE = re.getWidth();
		
		h = Math.min(hImageW, MAX_J);// 8112
		w = Math.min(wImageW+wImageE-I_START-overlap, MAX_I);// 13711
		
		chunks = new ChunkArray(w,h,wImageW+wImageE-I_START-overlap);
		
		float West_Bounding_Coordinate = -122.00018518518522f;
	      float East_Bounding_Coordinate= -121.74981481481484f;
	      float North_Bounding_Coordinate= 47.000185185185195f;
	      float South_Bounding_Coordinate= 46.749814814814826f;
	      
	      float delta_y_deg = (North_Bounding_Coordinate - South_Bounding_Coordinate)/wImageW;
	      float delta_x_deg = (East_Bounding_Coordinate - West_Bounding_Coordinate)/hImageW;
	      
	      float earth_radius = 6371e3f;
	      float earth_circumference = earth_radius * 2 * (float)Math.PI;
	      
	      delta_y = earth_circumference * delta_y_deg / 360.0f; // 3.432
	      delta_x = earth_circumference * delta_x_deg / 360.0f * (float)Math.cos(South_Bounding_Coordinate*Math.PI/180);// 2.35
	      
	      float width = delta_x * w;// 32241
	      float height = delta_y * h;// 27840
	      
	      centerX = width/2;// 16120
	      centerY = height/2;// 13920
		
		//int nbVertices = w*h; // 111 million vertices
		
		chunks.initArrays();
		
		//float[] vertices = new float[nbVertices*3];
		//float[] normals = new float[nbVertices*3];
		//int[] colors = new int[nbVertices];
		float[] fArray = new float[1];
		
		//int nbCoordIndices = (w-1)*(h-1)*5;
		//int[] coordIndices = new int[nbCoordIndices];
		
		Color snow = new Color(1.0f,1.0f,1.0f);

		
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
		
		chunks.initXY(delta_x,delta_y);
		
		jstart = (int)(Math.ceil((float)(h-1)/(Chunk.CHUNK_WIDTH-1)))*(Chunk.CHUNK_WIDTH-1) - h + 1;
		
		int red_,green_,blue_;
		
		float[] xyz = new float[3];
		
		for(int i=0;i<w;i++) {
		for(int j=0; j<h;j++) {
				int index = i*h+j;
				//chunks.verticesPut(index*3+0, i * delta_x);
				//chunks.verticesPut(index*3+1, (h - j -1) * delta_y);
				float z = ((i+I_START) >= wImageW ? re.getPixel(i+I_START-wImageW+overlap, j, fArray)[0] - delta : rw.getPixel(i+I_START, j, fArray)[0]);
				if( Math.abs(z)> 1e30) {
					z= ZMIN;
				}
				else {
					zmin = Math.min(zmin, z);
					zmax = Math.max(zmax, z);
				}
				chunks.verticesPut(index*3+2, z);
				
				ptV.setValue(chunks.verticesGet(index,xyz));
				sceneBox.extendBy(ptV);
				
				//Color color = snow;
				
				red_ = snow.getRed();
				green_ = snow.getGreen();
				blue_ = snow.getBlue();
				
				if(z < ALPINE_HEIGHT + 400 * (random.nextDouble()-0.3)) {
					//color = new Color((float)random.nextDouble()*GRASS_LUMINOSITY, 1.0f*GRASS_LUMINOSITY, (float)random.nextDouble()*0.75f*GRASS_LUMINOSITY);
					
					red_ = (int)(255.99f * (float)random.nextDouble()*GRASS_LUMINOSITY);
					green_ = (int)(255.99f *  1.0f*GRASS_LUMINOSITY);
					blue_ = (int)(255.99f * (float)random.nextDouble()*0.75f*GRASS_LUMINOSITY);
				}
				int red = /*color.getRed()*color.getRed()*/red_*red_/255;
				int green = /*color.getGreen()*color.getGreen()*/green_*green_/255;
				int blue = /*color.getBlue()*color.getBlue()*/blue_*blue_/255;
				int alpha = 255;//color.getAlpha();
				
				chunks.colorsPut(index, red, green, blue, alpha);
			}
		}
		
		rw = null;
		re = null;
		
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
			p0.setValue(chunks.verticesGet(index0,xyz));
			p1.setValue(chunks.verticesGet(index1,xyz));
			p2.setValue(chunks.verticesGet(index2,xyz));
			p3.setValue(chunks.verticesGet(index3,xyz));
			v0.setValue(p0.operator_minus_equal(p2));
			v1.setValue(p1.operator_minus_equal(p3));
			n.setValue(v0.operator_cross_equal(v1));
			n.normalize();
			chunks.normalsPut(index, n.getX(), n.getY(), n.getZ());

			if((n.getZ()<0.45 && chunks.verticesGet(index,xyz)[2] < ALPINE_HEIGHT) || n.getZ()<0.35) {
				chunks.colorsPut(index, redStone, greenStone, blueStone, 255);
			}
		}
		}
		
		//chunks.initIndices();
//		int indice=0;
//		for(int i=1;i<w;i++) {
//		for(int j=1; j<h;j++) {
//			coordIndices[indice++] = (i-1)*h+(j-1); //1
//			coordIndices[indice++] = (i)*h+(j-1); //2
//			coordIndices[indice++] = (i)*h+(j); //3
//			coordIndices[indice++] = (i-1)*h+(j); //4
//			coordIndices[indice++] = -1; 
//		}
//		}
		// Define coordinates
	    //SoCoordinate3 coords = new SoCoordinate3();
	    //coords.point.setValues(0, vertices);
	    //sep.addChild(coords);

		chunks.initIndexedFaceSets();
		
//	    SoVertexProperty vertexProperty = new SoVertexProperty();
//	    
//	    vertexProperty.vertex.setValuesPointer(/*0,*/ vertices);
//	    vertexProperty.normalBinding.setValue(SoVertexProperty.Binding.PER_VERTEX_INDEXED);
//	    vertexProperty.normal.setValuesPointer(/*0,*/ normals);
//	    vertexProperty.materialBinding.setValue(SoVertexProperty.Binding.PER_VERTEX_INDEXED);
//	    vertexProperty.orderedRGBA.setValues(0, colors);
//	    
//		SoIndexedFaceSet indexedFaceSet;
//		
//	    indexedFaceSet = new SoIndexedFaceSet() {
//	    	public void computeBBox(SoAction action, SbBox3f box, SbVec3f center) {
//	    		box.copyFrom(sceneBox);
//	    		center.copyFrom(sceneCenter);
//	    	}
//	    };
//	    
//	    indexedFaceSet.vertexProperty.setValue(vertexProperty);
//	    indexedFaceSet.coordIndex.setValues(0, coordIndices);
	    
	    
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
	    sunView.radius.setValue(SUN_RADIUS/SUN_REAL_DISTANCE*SUN_FAKE_DISTANCE);
	    
	    SoMaterial sunMat = new SoMaterial();
	    sunMat.emissiveColor.setValue(SUN_COLOR);
	    sunMat.ambientColor.setValue(0, 0, 0);
	    
	    sunSep.addChild(sunMat);
	    sunSep.addChild(sunTransl);
	    sunSep.addChild(sunView);
	    sep.addChild(sunSep);
	    
	    sky = new SoDirectionalLight[4];
	    sky[0] = new SoDirectionalLight();
	    sky[0].color.setValue(SKY_COLOR);
	    sky[0].intensity.setValue(SKY_INTENSITY);
	    sky[0].direction.setValue(0, 1, -1);
	    sky[1] = new SoDirectionalLight();
	    sky[1].color.setValue(SKY_COLOR);
	    sky[1].intensity.setValue(SKY_INTENSITY);
	    sky[1].direction.setValue(0, -1, -1);
	    sky[2] = new SoDirectionalLight();
	    sky[2].color.setValue(SKY_COLOR);
	    sky[2].intensity.setValue(SKY_INTENSITY);
	    sky[0].direction.setValue(1, 0, -1);
	    sky[3] = new SoDirectionalLight();
	    sky[3].color.setValue(SKY_COLOR);
	    sky[3].intensity.setValue(SKY_INTENSITY);
	    sky[3].direction.setValue(-1, 0, -1);
	    
	    sep.addChild(sky[0]);
	    sep.addChild(sky[1]);
	    sep.addChild(sky[2]);
	    sep.addChild(sky[3]);
	    
	    SoShadowGroup shadowGroup = new SoShadowGroup();
	    //SoGroup shadowGroup = new SoGroup();
	    shadowGroup.quality.setValue(1.0f);
	    shadowGroup.precision.setValue(0.2f);
	    //shadowGroup.shadowCachingEnabled.setValue(false);
	    shadowGroup.intensity.setValue(1.0f);
	    //shadowGroup.visibilityRadius.setValue(10000f);
	    //shadowGroup.smoothBorder.setValue(0.0f);
	    
for(int is=0;is<4;is++) {	    
	    sun[is] = new SoShadowDirectionalLight();
	    //sun = new SoDirectionalLight();
	    sun[is].color.setValue(SUN_COLOR);
	    
	    //sun.maxShadowDistance.setValue(SUN_FAKE_DISTANCE*1.5f);
	    //sun.bboxSize.setValue(SUN_FAKE_DISTANCE, SUN_FAKE_DISTANCE, SUN_FAKE_DISTANCE);
	    sun[is].bboxSize.setValue(1e5f, 1e5f, 1e4f);
	    
	    sun[is].intensity.setValue(1.0F/4.0f);
	    
	    shadowGroup.addChild(sun[is]);
}
//	    SoShadowStyle shadowStyle = new SoShadowStyle();	    
//	    shadowStyle.style.setValue(SoShadowStyle.Style.CASTS_SHADOW_AND_SHADOWED);	    
//	    shadowGroup.addChild(shadowStyle);
	    
	    SoSeparator landSep = new SoSeparator();
	    
	    SoMaterial mat = new SoMaterial();
	    mat.ambientColor.setValue(0, 0, 0); // no ambient
	    
	    landSep.addChild(mat);
	    
	    landSep.addChild(transl);
	    
	    OverallTexture ot = chunks.getOverallTexture();
	    
	    landSep.addChild(ot.getTexture());
	    
	    RecursiveChunk rc = chunks.getRecursiveChunk();
	    
	    master = new SoTouchLODMaster("truc");

	    master.setLodFactor(250);
	    
	    landSep.addChild(master);
	    
	    landSep.addChild(rc.getGroup(master,true));
	    
		//landSep.addChild(chunks.getGroup());
		
		//shadowGroup.addChild(landSep);

//	    SoShadowStyle shadowStyleW = new SoShadowStyle();	    
//	    shadowStyleW.style.setValue(SoShadowStyle.Style.SHADOWED);	    
//	    shadowGroup.addChild(shadowStyleW);
		
		shadowGroup.addChild(landSep);

		addWater(shadowGroup,185 + zTranslation, 0.0f);
		addWater(shadowGroup,175 + zTranslation, 0.2f);
		addWater(shadowGroup,165 + zTranslation, 0.4f);
		addWater(shadowGroup,160 + zTranslation, 0.5f);
		addWater(shadowGroup,155 + zTranslation, 0.6f);
		addWater(shadowGroup,150 + zTranslation, 0.7f);		
		
		sep.addChild(shadowGroup);
		
		SoSeparator castingShadowScene = new SoSeparator();
		
		//castingShadowScene.addChild(inverseSunTransl);
		
		addWater(castingShadowScene,185 + zTranslation,0.0f);
		
//	    SoVertexProperty vertexPropertyGeom = new SoVertexProperty();
//	    vertexPropertyGeom.vertex.setValuesPointer(/*0,*/ vertices);
//	    
//	    SoIndexedFaceSet indexedFaceSetGeom = new SoIndexedFaceSet() {
//	    	public void computeBBox(SoAction action, SbBox3f box, SbVec3f center) {
//	    		box.copyFrom(sceneBox);
//	    		center.copyFrom(sceneCenter);
//	    	}
//	    };
//	    
//	    indexedFaceSetGeom.vertexProperty.setValue(vertexPropertyGeom);
//	    indexedFaceSetGeom.coordIndex.setValues(0, coordIndices);
//	    
	    //castingShadowScene.addChild(transl);
//	    
//	    SoMaterial shadowMat = new SoMaterial();
//	    shadowMat.diffuseColor.setValue(0, 0, 0);
//	    
//	    castingShadowScene.addChild(shadowMat);
//	    
//	    castingShadowScene.addChild(indexedFaceSetGeom);
		
	    SoSeparator shadowLandSep = new SoSeparator();
	    
	    SoMaterial shadowMat = new SoMaterial();
	    shadowMat.ambientColor.setValue(0, 0, 0); // no ambient
	    
	    shadowLandSep.addChild(shadowMat);
	    
	    shadowLandSep.addChild(transl);
	    
	    shadowLandSep.addChild(chunks.getShadowGroup());
		
		castingShadowScene.addChild(shadowLandSep);
		
		sun[0].shadowMapScene.setValue(castingShadowScene);
		sun[1].shadowMapScene.setValue(castingShadowScene);
		sun[2].shadowMapScene.setValue(castingShadowScene);
		sun[3].shadowMapScene.setValue(castingShadowScene);
		
		//sep.ref();
	}
	
	public void addWater(SoGroup group, float z, float transparency) {
		
	    SoSeparator waterSeparator = new SoSeparator();
	    
		SoCube water = new SoCube();
		
		water.depth.setValue(2000);
		water.height.setValue(WATER_HORIZON*2);
		water.width.setValue(WATER_HORIZON*2);
		
	    SoMaterial waterMat = new SoMaterial();
	    waterMat.diffuseColor.setValue(0.1f*WATER_BRIGHTNESS,0.5f*WATER_BRIGHTNESS,0.6f*WATER_BRIGHTNESS);
	    waterMat.ambientColor.setValue(0, 0, 0);
	    waterMat.transparency.setValue(transparency);
	    
	    waterSeparator.addChild(waterMat);
	    
	    SoTranslation waterTranslation = new SoTranslation();
	    
	    waterTranslation.translation.setValue( /*14000*/- 4000 + WATER_HORIZON/2, /*-8000*/0, - /*transl.translation.getValue().getZ()*/z);	    
	    
	    waterSeparator.addChild(waterTranslation);
	    
	    waterSeparator.addChild(water);
	    
	    group.addChild(waterSeparator);
	    
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
	public void setPosition(float x, float y) {
		transl.translation.setValue(-x,-y,-zTranslation);
	}

	@Override
	public void setSunPosition(SbVec3f sunPosition) {
		SbVec3f dir = sunPosition.operator_minus();
		
		SbVec3f p1 = null;
		float a = dir.getX();
		float b = dir.getY();
		float c = dir.getZ();
		
		float aa = Math.abs(a);
		float ba = Math.abs(b);
		float ca = Math.abs(c);
		
		int max = 0;
		if( ba > aa) {
			max = 1;
			if(ca > ba) {
				max = 2;
			}
		}
		else {
			if( ca > aa) {
				max = 2;
			}
		}
		if(max == 2) {
			p1 = new SbVec3f(1,1,-(a+b)/c);
		}
		else if(max == 1) {
			p1 = new SbVec3f(1,-(a+c)/b,1);
		}
		else if( max == 0) {
			p1 = new SbVec3f(-(c+b)/a,1,1);
		}
		p1.normalize();
		SbVec3f p2 = dir.cross(p1);
		
		float angle = SUN_RADIUS / SUN_REAL_DISTANCE * 0.8f;
		
	    r1.setValue(p1, angle);
	    r2.setValue(p2, angle);
	    r3.setValue(p1, -angle);
	    r4.setValue(p2, -angle);
	    
		
		sun[1].direction.setValue(r1.multVec(dir));
		sun[2].direction.setValue(r2.multVec(dir));
		sun[0].direction.setValue(r3.multVec(dir));
		sun[3].direction.setValue(r4.multVec(dir));
		sunTransl.translation.setValue(sunPosition.operator_mul(SUN_FAKE_DISTANCE));
		//inverseSunTransl.translation.setValue(sunPosition.operator_mul(SUN_FAKE_DISTANCE).operator_minus());
	}
	
	
	public float getZ(float x, float y, float z) {
		
		float ifloat = (x - transl.translation.getValue().getX())/delta_x;
		float jfloat = (delta_y*(h-1) -(y - transl.translation.getValue().getY() - jstart * delta_y))/delta_y;
		
		int i = Math.round(ifloat);
		//int j = Math.round((y - transl.translation.getValue().getY() - 3298)/delta_y);
		int j = Math.round(jfloat);
		
		i = Math.max(0,i);
		j = Math.max(0,j);
		i = Math.min(w-1,i);
		j = Math.min(h-1,j);
		
		int imin = (int)Math.floor(ifloat);
		int imax = (int)Math.ceil(ifloat);
		int jmin = (int)Math.floor(jfloat);
		int jmax = (int)Math.ceil(jfloat);
		
		imin = Math.max(0,imin);
		jmin = Math.max(0,jmin);
		imin = Math.min(w-1,imin);
		jmin = Math.min(h-1,jmin);
		
		imax = Math.max(0,imax);
		jmax = Math.max(0,jmax);
		imax = Math.min(w-1,imax);
		jmax = Math.min(h-1,jmax);
		
		int index0 = imin*h + jmin;
		int index1 = imax*h + jmin;
		int index2 = imax*h + jmax;
		int index3 = imin*h + jmax;
		
		float[] xyz = new float[3];
 		
		float z0 = chunks.verticesGet(index0,xyz)[2] - zTranslation;
		float z1 = chunks.verticesGet(index1,xyz)[2] - zTranslation;
		float z2 = chunks.verticesGet(index2,xyz)[2] - zTranslation;
		float z3 = chunks.verticesGet(index3,xyz)[2] - zTranslation;
		
		float alpha = ifloat - imin;
		float beta = jfloat - jmin;
		
		//int index = i*h+ h - j -1;
		int index = i*h+ j;
		
		//z = chunks.verticesGet(index*3+2) - zTranslation;
		
		if(alpha + beta < 1) {
			z = z0 + (z1 - z0)*alpha + (z3 - z0)*beta;
		}
		else {
			z = z2 + (z3 - z2)*(1-alpha) + (z1 - z2)*(1-beta);			
		}
		
//		float xx = chunks.verticesGet(index*3+0);
//		float yy = chunks.verticesGet(index*3+1);
//		
//		System.out.println("i = "+i+" j = "+j);
//		
//		System.out.println("y ="+(y - transl.translation.getValue().getY())+ " yy = "+yy);
//		
//		float delta = yy - (y - transl.translation.getValue().getY());
//		
//		System.out.println("delta = " +delta);
		return z;
	}

	@Override
	public void preDestroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCamera(SoCamera camera) {
		// TODO Auto-generated method stub
	    master.setCamera(camera);	
	}

	@Override
	public void idle() {
		// TODO Auto-generated method stub
		
	}

}
