/**
 * 
 */
package jscenegraph.coin3d.fxviz.nodes;

import java.util.Objects;

import com.jogamp.opengl.GL2;

import jscenegraph.coin3d.fxviz.elements.SoShadowStyleElement;
import jscenegraph.coin3d.glue.cc_glglue;
import jscenegraph.coin3d.inventor.caches.SoShaderProgramCache;
import jscenegraph.coin3d.inventor.elements.SoEnvironmentElement;
import jscenegraph.coin3d.inventor.elements.SoGLMultiTextureEnabledElement;
import jscenegraph.coin3d.inventor.elements.SoLightElement;
import jscenegraph.coin3d.inventor.elements.SoMultiTextureEnabledElement;
import jscenegraph.coin3d.inventor.elements.SoMultiTextureMatrixElement;
import jscenegraph.coin3d.inventor.elements.SoTextureUnitElement;
import jscenegraph.coin3d.inventor.lists.SbList;
import jscenegraph.coin3d.inventor.misc.SoGLDriverDatabase;
import jscenegraph.coin3d.inventor.misc.SoShaderGenerator;
import jscenegraph.coin3d.inventor.nodes.SoFragmentShader;
import jscenegraph.coin3d.inventor.nodes.SoShaderObject;
import jscenegraph.coin3d.inventor.nodes.SoTextureUnit;
import jscenegraph.coin3d.inventor.nodes.SoVertexShader;
import jscenegraph.coin3d.misc.SoGL;
import jscenegraph.coin3d.shaders.inventor.nodes.SoShaderParameter1f;
import jscenegraph.coin3d.shaders.inventor.nodes.SoShaderParameter1i;
import jscenegraph.coin3d.shaders.inventor.nodes.SoShaderParameter4f;
import jscenegraph.coin3d.shaders.inventor.nodes.SoShaderParameterMatrix;
import jscenegraph.coin3d.shaders.inventor.nodes.SoShaderProgram;
import jscenegraph.database.inventor.SbBox3f;
import jscenegraph.database.inventor.SbMatrix;
import jscenegraph.database.inventor.SbName;
import jscenegraph.database.inventor.SbPlane;
import jscenegraph.database.inventor.SbRotation;
import jscenegraph.database.inventor.SbVec2s;
import jscenegraph.database.inventor.SbVec3f;
import jscenegraph.database.inventor.SbVec3fSingle;
import jscenegraph.database.inventor.SbViewVolume;
import jscenegraph.database.inventor.SbViewportRegion;
import jscenegraph.database.inventor.SbXfBox3f;
import jscenegraph.database.inventor.SoFullPath;
import jscenegraph.database.inventor.SoNodeList;
import jscenegraph.database.inventor.SoPath;
import jscenegraph.database.inventor.SoPathList;
import jscenegraph.database.inventor.actions.SoGLRenderAction;
import jscenegraph.database.inventor.actions.SoGetBoundingBoxAction;
import jscenegraph.database.inventor.actions.SoGetMatrixAction;
import jscenegraph.database.inventor.actions.SoSearchAction;
import jscenegraph.database.inventor.elements.SoCacheElement;
import jscenegraph.database.inventor.elements.SoClipPlaneElement;
import jscenegraph.database.inventor.elements.SoGLCacheContextElement;
import jscenegraph.database.inventor.elements.SoShapeStyleElement;
import jscenegraph.database.inventor.elements.SoViewVolumeElement;
import jscenegraph.database.inventor.elements.SoViewingMatrixElement;
import jscenegraph.database.inventor.errors.SoDebugError;
import jscenegraph.database.inventor.misc.SoState;
import jscenegraph.database.inventor.misc.SoTempPath;
import jscenegraph.database.inventor.nodes.SoCamera;
import jscenegraph.database.inventor.nodes.SoClipPlane;
import jscenegraph.database.inventor.nodes.SoDirectionalLight;
import jscenegraph.database.inventor.nodes.SoInfo;
import jscenegraph.database.inventor.nodes.SoLight;
import jscenegraph.database.inventor.nodes.SoNode;
import jscenegraph.database.inventor.nodes.SoOrthographicCamera;
import jscenegraph.database.inventor.nodes.SoPerspectiveCamera;
import jscenegraph.database.inventor.nodes.SoPointLight;
import jscenegraph.database.inventor.nodes.SoSpotLight;
import jscenegraph.port.Ctx;
import jscenegraph.port.Destroyable;

/**
 * @author BOYADJIAN
 *
 */
public class SoShadowGroupP implements Destroyable {
	

	// use to increase the VSM precision by using all four components
	public static final float DISTRIBUTE_FACTOR = 64.0f;

	
	  SoShadowGroup master; //ptr
	  final SoSearchAction searchaction = new SoSearchAction();
	  final SbList <SoTempPath> lightpaths = new SbList<>();
	  final SoGetBoundingBoxAction bboxaction;
	  final SoGetMatrixAction matrixaction;

	  boolean shadowlightsvalid;
	  boolean needscenesearch;
	  final SbList <SoShadowLightCache> shadowlights = new SbList<>(); // ptr

	  SoShaderProgram shaderprogram; //ptr
	  SoVertexShader vertexshader; //ptr
	  SoFragmentShader fragmentshader; // ptr

	  final SoShaderGenerator vertexgenerator = new SoShaderGenerator();
	  final SoShaderGenerator fragmentgenerator = new SoShaderGenerator();
	  SoShaderParameterMatrix cameratransform; // ptr

	  SoShaderProgramCache vertexshadercache; //ptr
	  SoShaderProgramCache fragmentshadercache; //ptr
	  SoShaderParameter1i texunit0; //ptr
	  SoShaderParameter1i texunit1; //ptr
	  SoShaderParameter1i lightmodel; //ptr
	  SoShaderParameter1i twosided; //ptr

	  int numtexunitsinscene;
	  boolean hasclipplanes;
	  boolean subgraphsearchenabled;
	

	public SoShadowGroupP(SoShadowGroup master) {
		this.master = master;
	    bboxaction = new SoGetBoundingBoxAction(new SbViewportRegion(new SbVec2s((short)100,(short)100)));
	    matrixaction= new SoGetMatrixAction(new SbViewportRegion(new SbVec2s((short)100,(short)100)));
	    needscenesearch = true;
	    numtexunitsinscene = 1;
	    subgraphsearchenabled = true;
	    
	    shaderprogram = new SoShaderProgram();
	    shaderprogram.ref();
	    shaderprogram.setEnableCallback(SoShadowGroupP::shader_enable_cb, this);
	    vertexshader = new SoVertexShader();
	    vertexshader.ref();
	    fragmentshader = new SoFragmentShader();
	    fragmentshader.ref();

	    cameratransform = new SoShaderParameterMatrix();
	    cameratransform.name.setValue("cameraTransform");
	    cameratransform.ref();

	    shaderprogram.shaderObject.set1Value(0, vertexshader);
	    shaderprogram.shaderObject.set1Value(1, fragmentshader);
	    
	}

	  public void destructor() {
		    clearLightPaths();
		    if (lightmodel != null) lightmodel.unref();
		    if (twosided != null) twosided.unref();
		    if (texunit0 != null) texunit0.unref();
		    if (texunit1 != null) texunit1.unref();
		    if (vertexshadercache != null) vertexshadercache.unref();
		    if (fragmentshadercache != null) fragmentshadercache.unref();
		    if (cameratransform != null) cameratransform.unref();
		    if (vertexshader != null) vertexshader.unref();
		    if (fragmentshader != null) fragmentshader.unref();
		    if (shaderprogram != null) shaderprogram.unref();
		    deleteShadowLights();
		    searchaction.destructor();
		    lightpaths.destructor();
		    bboxaction.destructor();
		    matrixaction.destructor();
		    shadowlights.destructor();
		    vertexgenerator.destructor();
		    fragmentgenerator.destructor();
		  }

	  public void clearLightPaths() {
		    for (int i = 0; i < this.lightpaths.getLength(); i++) {
		      this.lightpaths.operator_square_bracket(i).unref();
		    }
		    this.lightpaths.truncate(0);
		  }
	  
	  void copyLightPaths(SoPathList pl) {
		    for (int i = 0; i < pl.getLength(); i++) {
		      SoFullPath p = SoFullPath.cast(pl.operator_square_bracket(i));
		      SoNode tail = p.getTail();
		      if (tail.isOfType(SoSpotLight.getClassTypeId()) ||
		          tail.isOfType(SoShadowDirectionalLight.getClassTypeId())) {
		        SoTempPath tp = new SoTempPath(p.getLength());
		        tp.ref();
		        tp.setHead(p.getHead());

		        for (int j = 1; j < p.getLength(); j++) {
		          tp.append(p.getNode(j));
		        }
		        this.lightpaths.append(tp);
		      }
		    }
		  }
	  
	  
	  public void deleteShadowLights() {
		    for (int i = 0; i < this.shadowlights.getLength(); i++) {
		      Destroyable.delete(this.shadowlights.operator_square_bracket(i));
		    }
		    this.shadowlights.truncate(0);
		  }

	  
	  public static void
	  shader_enable_cb(Object closure,
	                                   SoState state,
	                                   final boolean enable)
	  {
	    SoShadowGroupP thisp = (SoShadowGroupP) closure;

	    int ctx = SoGLCacheContextElement.get(state);
	    
	    final cc_glglue glue = SoGL.cc_glglue_instance(ctx);
	    
	    GL2 gl2 = Ctx.get(ctx);

	    int error_pre = 0;
	    int error_post = 0;

	    for (int i = 0; i < thisp.shadowlights.getLength(); i++) {
	      SoShadowLightCache cache = thisp.shadowlights.operator_square_bracket(i);
	      int unit = cache.texunit;
	      if (unit == 0) {
	        if (enable) gl2.glEnable(GL2.GL_TEXTURE_2D);
	        else gl2.glDisable(GL2.GL_TEXTURE_2D);
	      }
	      else {
	        SoGL.cc_glglue_glActiveTexture(glue, /*(GLenum)*/ ((int)(GL2.GL_TEXTURE0) + unit));
	        if (enable) {
	  		  	error_pre = gl2.glGetError();

	        	gl2.glEnable(GL2.GL_TEXTURE_2D);

	        	error_post = gl2.glGetError();
	        }
	        else {
	  		  	error_pre = gl2.glGetError();

	  		  	gl2.glDisable(GL2.GL_TEXTURE_2D);

	        	error_post = gl2.glGetError();
	        }
	        
	        SoGL.cc_glglue_glActiveTexture(glue, GL2.GL_TEXTURE0);
	      }
	    }
	  }

      static boolean first = true;
      
	  public void
	  GLRender(SoGLRenderAction action, final boolean inpath)
	  {
	    SoState state = action.getState();
	    //final cc_glglue glue = SoGL.cc_glglue_instance(SoGLCacheContextElement.get(state));

	    // FIXME: should store results in a "context -> supported" map.  -mortene.
	    String reason;
	    final boolean supported = true;//SoShadowGroupP.supported(glue, reason); FIXME
	    if (!supported && master.isActive.getValue()) {
	      if (first) {
	        first = false;
	        SoDebugError.postWarning("SoShadowGroupP.GLRender", reason/*.getString()*/);
	      }
	    }

	    if (!supported || !master.isActive.getValue()) {
	      if (inpath) master.super_GLRenderInPath(action);
	      else master.super_GLRenderBelowPath(action);
	      return;
	    }

	    state.push();

	    if (this.vertexshadercache==null || !this.vertexshadercache.isValid(state)) {
	      // a bit hackish, but saves creating yet another cache
	      this.shadowlightsvalid = false;
	    }

	    SbMatrix camtransform = SoViewingMatrixElement.get(state).inverse();
	    if (camtransform.operator_not_equal(this.cameratransform.value.getValue())) {
	      this.cameratransform.value.setValue(camtransform);
	    }

	    SoShadowStyleElement.set(state, /*master,*/ SoShadowStyleElement.StyleFlags.CASTS_SHADOW_AND_SHADOWED.getValue());
	    SoShapeStyleElement.setShadowMapRendering(state, true);
	    this.updateShadowLights(action);
	    SoShapeStyleElement.setShadowMapRendering(state, false);

	    if (this.vertexshadercache == null || !this.vertexshadercache.isValid(state)) {
	      this.setVertexShader(state);
	    }

	    if (this.fragmentshadercache == null || !this.fragmentshadercache.isValid(state)) {
	      this.setFragmentShader(state);
	    }
	    this.shaderprogram.GLRender(action);

	    SoShapeStyleElement.setShadowsRendering(state, true);
	    if (inpath) master.super_GLRenderInPath(action);
	    else master.super_GLRenderBelowPath(action);
	    SoShapeStyleElement.setShadowsRendering(state, false);
	    state.pop();
	  }

	// *************************************************************************

	  public void
	  updateShadowLights(SoGLRenderAction action)
	  {
	    int i;
	    SoState state = action.getState();

	    if (!this.shadowlightsvalid) {
	      int lightidoffset = SoLightElement.getLights(state).getLength();
	      float smoothing = master.smoothBorder.getValue();
	      smoothing = 0.0f; // FIXME: temporary until we have time to fix this feature

	      int gaussmatrixsize = 0;
	      float gaussstandarddeviation = 0.6f;

	      // just hardcode some values for now
	      if (smoothing > 0.9) gaussmatrixsize = 7;
	      else if (smoothing > 0.5) gaussmatrixsize = 5;
	      else if (smoothing > 0.01) gaussmatrixsize = 3;

	      final cc_glglue glue = SoGL.cc_glglue_instance(SoGLCacheContextElement.get(state));

	      if (this.needscenesearch) {
	        this.hasclipplanes = SoClipPlaneElement.getInstance(state).getNum() > 0;
	        if (!this.hasclipplanes) {
	          this.searchaction.setType(SoClipPlane.getClassTypeId());
	          this.searchaction.setInterest(SoSearchAction.Interest.FIRST);
	          this.searchaction.setSearchingAll(false);
	          this.searchaction.apply(master);
	          if (this.searchaction.getPath()!=null) {
	            this.hasclipplanes = true;
	          }
	          this.searchaction.reset();
	        }
	        // first, search for texture unit nodes
	        this.searchaction.setType(SoTextureUnit.getClassTypeId());
	        this.searchaction.setInterest(SoSearchAction.Interest.ALL);
	        this.searchaction.setSearchingAll(false);
	        this.searchaction.apply(master);

	        final int[] lastenabled = new int[1];
	        SoMultiTextureEnabledElement.getEnabledUnits(state, lastenabled);
	        this.numtexunitsinscene = lastenabled[0] + 1;

	        for (i = 0; i < this.searchaction.getPaths().getLength(); i++) {
	          SoFullPath p = SoFullPath.cast(this.searchaction.getPaths().operator_square_bracket(i));
	          SoTextureUnit unit = (SoTextureUnit) p.getTail();
	          if (unit.unit.getValue() >= this.numtexunitsinscene) {
	            this.numtexunitsinscene = unit.unit.getValue() + 1;
	          }
	        }
	        if (this.numtexunitsinscene == 0) this.numtexunitsinscene = 1;

	        this.searchaction.reset();
	        this.searchaction.setType(SoLight.getClassTypeId());
	        this.searchaction.setInterest(SoSearchAction.Interest.ALL);
	        this.searchaction.setSearchingAll(false);
	        this.searchaction.apply(master);
	        this.clearLightPaths();
	        this.copyLightPaths(this.searchaction.getPaths());
	        this.searchaction.reset();
	        this.needscenesearch = false;
	      }
	      int maxunits = SoGL.cc_glglue_max_texture_units(glue);

	      int maxlights = maxunits - this.numtexunitsinscene;
	      final SbList <SoTempPath> pl = this.lightpaths;

	      int numlights = 0;
	      for (i = 0; i < pl.getLength(); i++) {
	        SoLight light = (SoLight)((SoFullPath.cast(pl.operator_square_bracket(i)))).getTail();
	        if (light.on.getValue() && (numlights < maxlights)) numlights++;
	      }
	      if (numlights != this.shadowlights.getLength()) {
	        // just delete and recreate all if the number of spot lights have changed
	        this.deleteShadowLights();
	        int id = lightidoffset;
	        for (i = 0; i < pl.getLength(); i++) {
	          SoLight light = (SoLight)(SoFullPath.cast(pl.operator_square_bracket(i))).getTail();
	          if (light.on.getValue() && (this.shadowlights.getLength() < maxlights)) {
	            SoNode scene = master;
	            SoNode bboxscene = master;
	            if (light.isOfType(SoShadowSpotLight.getClassTypeId())) {
	              SoShadowSpotLight ssl = (SoShadowSpotLight) light;
	              if (ssl.shadowMapScene.getValue() != null) {
	                scene = ssl.shadowMapScene.getValue();
	              }
	            }
	            else if (light.isOfType(SoShadowDirectionalLight.getClassTypeId())) {
	              SoShadowDirectionalLight sl = (SoShadowDirectionalLight) light;
	              if (sl.shadowMapScene.getValue() != null) {
	                scene = sl.shadowMapScene.getValue();
	              }
	            }
	            SoShadowLightCache cache = new SoShadowLightCache(state, pl.operator_square_bracket(i),
	                                                                master,
	                                                                scene,
	                                                                bboxscene,
	                                                                gaussmatrixsize,
	                                                                gaussstandarddeviation);
	            cache.lightid = id++;
	            this.shadowlights.append(cache);
	          }
	        }
	      }
	      // validate if spot light paths are still valid
	      int i2 = 0;
	      int id = lightidoffset;
	      for (i = 0; i < pl.getLength(); i++) {
	        SoPath path = pl.operator_square_bracket(i);
	        SoLight light = (SoLight) (SoFullPath.cast(path)).getTail();
	        if (light.on.getValue() && (i2 < maxlights)) {
	          SoShadowLightCache cache = this.shadowlights.operator_square_bracket(i2);
	          int unit = (maxunits - 1) - i2;
	          int lightid = id++;
	          if (unit != cache.texunit || lightid != cache.lightid) {
	            if (this.vertexshadercache != null) this.vertexshadercache.invalidate();
	            if (this.fragmentshadercache != null) this.fragmentshadercache.invalidate();
	            cache.texunit = unit;
	            cache.lightid = lightid;
	          }
	          if ((cache.path).operator_not_equals( path)) {
	            cache.path.unref();
	            cache.path = path.copy();
	          }
	          if (cache.light.isOfType(SoSpotLight.getClassTypeId())) {
	            this.matrixaction.apply(path);
	            this.updateSpotCamera(state, cache, this.matrixaction.getMatrix());
	          }
	          i2++;
	        }
	      }
	      this.shadowlightsvalid = true;
	    }
	    for (i = 0; i < this.shadowlights.getLength(); i++) {
	      SoShadowLightCache cache = this.shadowlights.operator_square_bracket(i);
	      if (cache.light.isOfType(SoDirectionalLight.getClassTypeId())) {
	        this.matrixaction.apply(cache.path);
	        this.updateDirectionalCamera(state, cache, this.matrixaction.getMatrix());
	      }
	      assert(cache.texunit >= 0);
	      assert(cache.lightid >= 0);
	      SoTextureUnitElement.set(state, /*master,*/ cache.texunit);

	      SbMatrix mat = cache.matrix;

	      assert(cache.texunit >= 0);

	      SoMultiTextureMatrixElement.set(state, master, cache.texunit, cache.matrix);
	      this.renderDepthMap(cache, action);
	      SoGLMultiTextureEnabledElement.set(state, master, cache.texunit,
	                                          SoMultiTextureEnabledElement.Mode.DISABLED.getValue() != 0);
	    }
	    SoTextureUnitElement.set(state, /*master,*/ 0);
	  }


public void
setVertexShader(SoState state)
{
  int i;
  SoShaderGenerator gen = this.vertexgenerator;
  gen.reset(false);

  boolean storedinvalid = SoCacheElement.setInvalid(false);

  state.push();

  final boolean[] perpixelspot = new boolean[1];//false;
  final boolean[] perpixelother = new boolean[1];//false;

  this.getQuality(state, perpixelspot, perpixelother);

  if (this.vertexshadercache != null) {
    this.vertexshadercache.unref();
  }
  this.vertexshadercache = new SoShaderProgramCache(state);
  this.vertexshadercache.ref();

  cc_glglue glue = SoGL.cc_glglue_instance(SoGLCacheContextElement.get(state));

  // set active cache to record cache dependencies
  SoCacheElement.set(state, this.vertexshadercache);
  SoNodeList lights = SoLightElement.getLights(state);

  int numshadowlights = this.shadowlights.getLength();

  for (i = 0; i < numshadowlights; i++) {
    String str;
    str = "varying vec4 shadowCoord"+i+";";
    gen.addDeclaration(str, false);

    if (!perpixelspot[0]) {
      str = "varying vec3 spotVertexColor"+i+";";
      gen.addDeclaration(str, false);
    }
  }

  if (numshadowlights != 0) {
    gen.addDeclaration("uniform mat4 cameraTransform;", false);
  }
  gen.addDeclaration("varying vec3 ecPosition3;", false);
  gen.addDeclaration("varying vec3 fragmentNormal;", false);
  gen.addDeclaration("varying vec3 perVertexColor;", false);

  boolean dirlight = false;
  boolean pointlight = false;
  boolean spotlight = false;
  String str = "";

  gen.addMainStatement("vec4 ecPosition = gl_ModelViewMatrix * gl_Vertex;\n"+
                       "ecPosition3 = ecPosition.xyz / ecPosition.w;");

  gen.addMainStatement("vec3 normal = normalize(gl_NormalMatrix * gl_Normal);\n"+
                       "vec3 eye = -normalize(ecPosition3);\n"+
                       "vec4 ambient;\n"+
                       "vec4 diffuse;\n"+
                       "vec4 specular;\n"+
                       "vec4 accambient = vec4(0.0);\n"+
                       "vec4 accdiffuse = vec4(0.0);\n"+
                       "vec4 accspecular = vec4(0.0);\n"+
                       "vec4 color;\n");

  gen.addMainStatement("fragmentNormal = normal;");

  if (!perpixelother[0]) {
    for (i = 0; i < lights.getLength(); i++) {
      SoLight l = (SoLight) lights.operator_square_bracket(i);
      if (l.isOfType(SoDirectionalLight.getClassTypeId())) {
        addDirectionalLight(gen, i);
        dirlight = true;
      }
      else if (l.isOfType(SoSpotLight.getClassTypeId())) {
        addSpotLight(gen, i);
        spotlight = true;
      }
      else if (l.isOfType(SoPointLight.getClassTypeId())) {
        addPointLight(gen, i);
        gen.addMainStatement(str);
        pointlight = true;
      }
      else {
        SoDebugError.postWarning("SoShadowGroupP.setVertexShader",
                                  "Unknown light type: "+
                                  l.getTypeId().getName().getString());
      }
      gen.addMainStatement("accambient += ambient; accdiffuse += diffuse; accspecular += specular;\n");
    }

    if (dirlight) gen.addNamedFunction(new SbName("lights/DirectionalLight"), false);
    if (pointlight) gen.addNamedFunction(new SbName("lights/PointLight"), false);

    gen.addMainStatement("color = gl_FrontLightModelProduct.sceneColor + "+
                         "  accambient * gl_FrontMaterial.ambient + "+
                         "  accdiffuse * gl_Color +"+
                         "  accspecular * gl_FrontMaterial.specular;\n"
                         );
  }
  else {
    gen.addMainStatement("color = gl_FrontLightModelProduct.sceneColor;\n");
  }

  if (numshadowlights != 0) {
    gen.addMainStatement("vec4 pos = cameraTransform * ecPosition;\n"); // in world space
  }
  for (i = 0; i < numshadowlights; i++) {
    SoShadowLightCache cache = this.shadowlights.operator_square_bracket(i);
    //String str;
    str = "shadowCoord"+i+" = gl_TextureMatrix["+cache.texunit+"] * pos;\n"; // in light space
    gen.addMainStatement(str);

    if (!perpixelspot[0]) {
      spotlight = true;
      addSpotLight(gen, cache.lightid);
      str = "spotVertexColor"+i+" = \n"+
                  "  ambient.rgb * gl_FrontMaterial.ambient.rgb + "+
                  "  diffuse.rgb * gl_Color.rgb + "+
                  "  specular.rgb * gl_FrontMaterial.specular.rgb;\n";
      gen.addMainStatement(str);
    }
  }

  if (spotlight) gen.addNamedFunction(new SbName("lights/SpotLight"), false);
  SoEnvironmentElement.FogType fogType = SoEnvironmentElement.FogType.fromValue(this.getFog(state));

  switch (fogType) {
  default:
    assert(false);// && "unknown fog type");
  case NONE:
    // do nothing
    break;
  case HAZE:
  case FOG:
  case SMOKE:
    gen.addMainStatement("gl_FogFragCoord = abs(ecPosition3.z);\n");
    break;
  }
  gen.addMainStatement("perVertexColor = vec3(clamp(color.r, 0.0, 1.0), clamp(color.g, 0.0, 1.0), clamp(color.b, 0.0, 1.0));"+
                       "gl_TexCoord[0] = gl_TextureMatrix[0] * gl_MultiTexCoord0;\n"+
                       "gl_TexCoord[1] = gl_TextureMatrix[1] * gl_MultiTexCoord1;\n"+
                       "gl_Position = ftransform();\n"+
                       "gl_FrontColor = gl_Color;\n");

  if (this.hasclipplanes) {
    if (SoGLDriverDatabase.isSupported(glue, SoGLDriverDatabase.SO_GL_GLSL_CLIP_VERTEX_HW)) {
      gen.addMainStatement("gl_ClipVertex = gl_ModelViewMatrix * gl_Vertex;\n");
    }
  }

  // never update unless the program has actually changed. Creating a
  // new GLSL program is very slow on current drivers.
  if (!Objects.equals(this.vertexshader.sourceProgram.getValue(), gen.getShaderProgram())) {
    this.vertexshader.sourceProgram.setValue(gen.getShaderProgram());
    this.vertexshader.sourceType.setValue(SoShaderObject.SourceType.GLSL_PROGRAM);
    this.vertexshadercache.set(gen.getShaderProgram());

    if (numshadowlights != 0) {
      this.vertexshader.parameter.set1Value(0, this.cameratransform);
    }
    else {
      this.vertexshader.parameter.setNum(0);
    }
//#if 0 // for debugging
//    fprintf(stderr,"new vertex program: %s\n",
//            gen.getShaderProgram().getString());
//#endif
  }


  this.vertexshadercache.set(gen.getShaderProgram());

  state.pop();
  SoCacheElement.setInvalid(storedinvalid);

}

public void
setFragmentShader(SoState state)
{
  int i;

  SoShaderGenerator gen = this.fragmentgenerator;
  gen.reset(false);

  final boolean[] perpixelspot = new boolean[1];
  final boolean[] perpixelother = new boolean[1];
  this.getQuality(state, perpixelspot, perpixelother);

  final cc_glglue glue = SoGL.cc_glglue_instance(SoGLCacheContextElement.get(state));
  boolean storedinvalid = SoCacheElement.setInvalid(false);
  state.push();

  if (this.fragmentshadercache != null) {
    this.fragmentshadercache.unref();
  }
  this.fragmentshadercache = new SoShaderProgramCache(state);
  this.fragmentshadercache.ref();

  // set active cache to record cache dependencies
  SoCacheElement.set(state, this.fragmentshadercache);

  int numshadowlights = this.shadowlights.getLength();
  boolean dirspot = false;

  // ATi doesn't seem to support gl_FrontFace in hardware. We've only
  // verified that nVidia supports it so far.
  boolean twosidetest = /*glue.vendor_is_nvidia &&*/ ((perpixelspot[0] && numshadowlights != 0) || perpixelother[0]);


  if (numshadowlights != 0) {
    String eps;
    eps = "const float EPSILON = "+master.epsilon.getValue()+";"
                ;
    gen.addDeclaration(eps, false);
    eps = "const float THRESHOLD = "+master.threshold.getValue()+";"
                ;
    gen.addDeclaration(eps, false);
  }
  for (i = 0; i < numshadowlights; i++) {
    String str;
    str = "uniform sampler2D shadowMap"+i+";";
    gen.addDeclaration(str, false);

    str = "uniform float farval"+i+";";
    gen.addDeclaration(str, false);

    str = "uniform float nearval"+i+";";
    gen.addDeclaration(str, false);

    str = "varying vec4 shadowCoord"+i+";";
    gen.addDeclaration(str, false);

    if (!perpixelspot[0]) {
      str = "varying vec3 spotVertexColor"+i+";";
      gen.addDeclaration(str, false);
    }
    if (this.shadowlights.operator_square_bracket(i).light.isOfType(SoDirectionalLight.getClassTypeId())) {
      str = "uniform vec4 lightplane"+i+";";
      gen.addDeclaration(str, false);
    }
  }

  String str;
  if (numshadowlights != 0) {
//#ifdef DISTRIBUTE_FACTOR
    str = "const float DISTRIBUTE_FACTOR = "+DISTRIBUTE_FACTOR+";\n";
    gen.addDeclaration(str, false);
//#endif
  }
  gen.addDeclaration("varying vec3 ecPosition3;", false);
  gen.addDeclaration("varying vec3 fragmentNormal;", false);
  gen.addDeclaration("varying vec3 perVertexColor;", false);

  final SoNodeList lights = SoLightElement.getLights(state);

  if (numshadowlights != 0) {
    gen.addNamedFunction("vsm/VsmLookup", false);
  }
  gen.addMainStatement("vec3 normal = normalize(fragmentNormal);\n");
  if (twosidetest) {
    gen.addMainStatement("if (coin_two_sided_lighting != 0 && !gl_FrontFacing) normal = -normal;\n");
  }
  gen.addMainStatement("vec3 eye = -normalize(ecPosition3);\n");
  gen.addMainStatement("vec4 ambient = vec4(0.0);\n"+
                       "vec4 diffuse = vec4(0.0);\n"+
                       "vec4 specular = vec4(0.0);\n"+ // YB
                       "vec4 mydiffuse = gl_Color;\n"+
                       "vec4 texcolor = (coin_texunit0_model != 0) ? texture2D(textureMap0, gl_TexCoord[0].xy) : vec4(1.0);\n");

  if (this.numtexunitsinscene > 1) {
    gen.addMainStatement("if (coin_texunit1_model != 0) texcolor *= texture2D(textureMap1, gl_TexCoord[1].xy);\n");
  }
  gen.addMainStatement("vec3 color = perVertexColor;\n"+
                       "vec3 scolor = vec3(0.0);\n"+
                       "float dist;\n"+
                       "float shadeFactor;\n"+
                       "vec3 coord;\n"+
                       "vec4 map;\n"+
                       "mydiffuse.a *= texcolor.a;\n");

  if (perpixelspot[0]) {
    boolean spotlight = false;
    boolean dirlight = false;
    for (i = 0; i < numshadowlights; i++) {
      SoShadowLightCache cache = this.shadowlights.operator_square_bracket(i);
      boolean dirshadow = false;
      //String str; java port
      boolean normalspot = false;
      String insidetest = "&& coord.x >= 0.0 && coord.x <= 1.0 && coord.y >= 0.0 && coord.y <= 1.0)";

      SoLight light = this.shadowlights.operator_square_bracket(i).light;
      if (light.isOfType(SoSpotLight.getClassTypeId())) {
        SoSpotLight sl = (SoSpotLight) (light);
        if (sl.dropOffRate.getValue() >= 0.0f) {
          insidetest = ")";
          spotlight = true;
          normalspot = true;
        }
        else {
          insidetest = ")";
          dirspot = true;
        }
      }
      else {
        dirshadow = true;
        dirlight = true;
      }
      if (dirshadow) {
        str = "dist = dot(ecPosition3.xyz, lightplane"+i+".xyz) - lightplane"+i+".w;\n";
        gen.addMainStatement(str);
        addDirectionalLight(gen, cache.lightid);
      }
      else {
        if (normalspot) {
          addSpotLight(gen, cache.lightid, true);
        }
        else {
          addDirSpotLight(gen, cache.lightid, true);
        }
      }
      str = "coord = 0.5 * (shadowCoord"+i+".xyz / shadowCoord"+i+".w + vec3(1.0));\n";
      gen.addMainStatement(str);
      str = "map = texture2D(shadowMap"+i+", coord.xy);\n";
      gen.addMainStatement(str);
//#ifdef USE_NEGATIVE
      gen.addMainStatement("map = (map + vec4(1.0)) * 0.5;\n");
//#endif // USE_NEGATIVE
//#ifdef DISTRIBUTE_FACTOR
      gen.addMainStatement("map.xy += map.zw / DISTRIBUTE_FACTOR;\n");
//#endif
      str = "shadeFactor = ((map.x < 0.9999) && (shadowCoord"+i+".z > -1.0 "+insidetest+") "+
                  "? VsmLookup(map, (dist - nearval"+i+") / (farval"+i+" - nearval"+i+"), EPSILON, THRESHOLD) : 1.0;\n";
      gen.addMainStatement(str);

      if (dirshadow) {
        SoShadowDirectionalLight sl = (SoShadowDirectionalLight) (light);
        if (sl.maxShadowDistance.getValue() > 0.0f) {
          gen.addMainStatement("shadeFactor = 1.0 - shadeFactor;\n");

          // linear falloff
          // str.sprintf("shadeFactor *= max(0.0, min(1.0, 1.0 + ecPosition3.z/maxshadowdistance%d));\n", i);

          // See SoGLEnvironemntElement.cpp (updategl()) to see how the magic exp() constants here are calculated

          // exp(f) falloff
          // str.sprintf("shadeFactor *= min(1.0, exp(5.545*ecPosition3.z/maxshadowdistance%d));\n", i);
          // just use exp(f^2) as a falloff formula for now, consider making this configurable
          str = "shadeFactor *= min(1.0, exp(2.35*ecPosition3.z*abs(ecPosition3.z)/(maxshadowdistance"+i+"*maxshadowdistance"+i+")));\n";
          gen.addMainStatement(str);
          gen.addMainStatement("shadeFactor = 1.0 - shadeFactor;\n");
        }
      }
      gen.addMainStatement("color += shadeFactor * diffuse.rgb * mydiffuse.rgb;");
      gen.addMainStatement("scolor += shadeFactor * gl_FrontMaterial.specular.rgb * specular.rgb;\n");
      gen.addMainStatement("color += ambient.rgb * gl_FrontMaterial.ambient.rgb;\n");
    }

    if (perpixelother[0]) {
      boolean pointlight = false;
      for (i = 0; i < lights.getLength(); i++) {
        SoLight l = (SoLight) lights.operator_square_bracket(i);
        if (l.isOfType(SoDirectionalLight.getClassTypeId())) {
          addDirectionalLight(gen, i);
          dirlight = true;
        }
        else if (l.isOfType(SoSpotLight.getClassTypeId())) {
          addSpotLight(gen, i);
          spotlight = true;
        }
        else if (l.isOfType(SoPointLight.getClassTypeId())) {
          addPointLight(gen, i);
          pointlight = true;
        }
        else {
          SoDebugError.postWarning("SoShadowGroupP.setFragmentShader",
                                    "Unknown light type: "+
                                    l.getTypeId().getName().getString());
        }
        gen.addMainStatement("color += ambient.rgb * gl_FrontMaterial.ambient.rgb + "+
                             "diffuse.rgb * mydiffuse.rgb;\n");
        gen.addMainStatement("scolor += specular.rgb * gl_FrontMaterial.specular.rgb;\n");
      }

      if (dirlight) gen.addNamedFunction(new SbName("lights/DirectionalLight"), false);
      if (pointlight) gen.addNamedFunction(new SbName("lights/PointLight"), false);
    }
    if (spotlight) gen.addNamedFunction(new SbName("lights/SpotLight"), false);
  }

  else {
    for (i = 0; i < numshadowlights; i++) {
      String insidetest = "&& coord.x >= 0.0 && coord.x <= 1.0 && coord.y >= 0.0 && coord.y <= 1.0)";

      SoLight light = this.shadowlights.operator_square_bracket(i).light;
      if (light.isOfType(SoSpotLight.getClassTypeId())) {
        SoSpotLight sl = (SoSpotLight) (light);
        if (sl.dropOffRate.getValue() >= 0.0f) {
          insidetest = ")";
        }
      }
      //String str; java port
      str = "dist = length(vec3(gl_LightSource["+lights.getLength()+i+"].position) - ecPosition3);\n"+
                  "coord = 0.5 * (shadowCoord"+i+".xyz / shadowCoord"+i+".w + vec3(1.0));\n"+
                  "map = texture2D(shadowMap"+i+", coord.xy);\n"+
//#ifdef USE_NEGATIVE
                  "map = (map + vec4(1.0)) * 0.5;\n"+
//#endif // USE_NEGATIVE
//#ifdef DISTRIBUTE_FACTOR
                  "map.xy += map.zw / DISTRIBUTE_FACTOR;\n"+
//#endif
                  "shadeFactor = (shadowCoord"+i+".z > -1.0"+insidetest/*.getString()*/+" ? VsmLookup(map, (dist - nearval"+i+")/(farval"+i+"-nearval"+i+"), EPSILON, THRESHOLD) : 1.0;\n"+
                  "color += shadeFactor * spotVertexColor"+i+";\n";
      gen.addMainStatement(str);
    }
  }

  gen.addMainStatement("color = vec3(clamp(color.r, 0.0, 1.0), clamp(color.g, 0.0, 1.0), clamp(color.b, 0.0, 1.0));");
  gen.addMainStatement("if (coin_light_model != 0) { color *= texcolor.rgb; color += scolor; }\n"+
                       "else color = mydiffuse.rgb * texcolor.rgb;\n");

  SoEnvironmentElement.FogType fogType = SoEnvironmentElement.FogType.fromValue(this.getFog(state));

  switch (fogType) {
  default:
    assert(false);// && "unknown fog type");
  case NONE:
    // do nothing
    break;
  case HAZE:
    gen.addMainStatement("float fog = (gl_Fog.end - gl_FogFragCoord) * gl_Fog.scale;\n");
    break;
  case FOG:
    gen.addMainStatement("float fog = exp(-gl_Fog.density * gl_FogFragCoord);\n");
    gen.setVersion("#version 110");
    break;
  case SMOKE:
    gen.addMainStatement("float fogfrag =  gl_FogFragCoord;");
    gen.addMainStatement("float fogdens =  gl_Fog.density;");
    gen.addMainStatement("float fog = exp(-fogdens * fogdens * fogfrag * fogfrag);\n");
    gen.setVersion("#version 110");
    break;
  }
  if (fogType != SoEnvironmentElement.FogType.NONE) {
    gen.addMainStatement("color = mix(gl_Fog.color.rgb, color, clamp(fog, 0.0, 1.0));\n");
  }

  gen.addMainStatement("gl_FragColor = vec4(color, mydiffuse.a);");
  gen.addDeclaration("uniform sampler2D textureMap0;\n", false);
  gen.addDeclaration("uniform int coin_texunit0_model;\n", false);
  if (this.numtexunitsinscene > 1) {
    gen.addDeclaration("uniform int coin_texunit1_model;\n", false);
    gen.addDeclaration("uniform sampler2D textureMap1;\n", false);
  }
  gen.addDeclaration("uniform int coin_light_model;\n", false);
  if (twosidetest) {
    gen.addDeclaration("uniform int coin_two_sided_lighting;\n", false);
  }

  if (dirspot) {
    gen.addNamedFunction("lights/DirSpotLight", false);
  }

  this.fragmentshader.parameter.setNum(0);

  for (i = 0; i < numshadowlights; i++) {
    SoShadowLightCache cache = this.shadowlights.operator_square_bracket(i);

    SoShaderParameter1i shadowmap = this.shadowlights.operator_square_bracket(i).shadowmapid;
    //String str; java port
    str = "shadowMap"+ i;
    if (!shadowmap.name.getValue().equals(str)) {
      shadowmap.name.setValue(str);
    }
    shadowmap.value.setValue( cache.texunit);
    this.fragmentshader.parameter.set1Value(this.fragmentshader.parameter.getNum(), shadowmap);
  }

  for (i = 0; i < numshadowlights; i++) {
    //String str;
    SoShaderParameter1f farval = this.shadowlights.operator_square_bracket(i).fragment_farval;
    str = "farval"+ i;
    if (!farval.name.getValue().equals(str)) {
      farval.name.setValue( str);
    }
    this.fragmentshader.parameter.set1Value(this.fragmentshader.parameter.getNum(), farval);
  }

  for (i = 0; i < numshadowlights; i++) {
    //String str;
    SoShaderParameter1f nearval = this.shadowlights.operator_square_bracket(i).fragment_nearval;
    str = "nearval"+ i;
    if (!nearval.name.getValue().equals(str)) {
      nearval.name.setValue(str);
    }
    this.fragmentshader.parameter.set1Value(this.fragmentshader.parameter.getNum(), nearval);
  }
  SoShaderParameter1i texmap =
    new SoShaderParameter1i();
  str = "textureMap0";
  texmap.name.setValue(str);
  texmap.value.setValue(0);

  SoShaderParameter1i texmap1 = null;

  if (this.texunit0 == null) {
    this.texunit0 = new SoShaderParameter1i();
    this.texunit0.ref();
    this.texunit0.name.setValue( "coin_texunit0_model");
    this.texunit0.value.setValue( 0);
  }

  if (this.numtexunitsinscene > 1) {
    if (this.texunit1 == null) {
      this.texunit1 = new SoShaderParameter1i();
      this.texunit1.ref();
      this.texunit1.name.setValue("coin_texunit1_model");
      this.texunit1.value.setValue( 0);
    }
    texmap1 = new SoShaderParameter1i();
    str = "textureMap1";
    texmap1.name.setValue(str);
    texmap1.value.setValue(1);
  }

  if (this.lightmodel == null) {
    this.lightmodel = new SoShaderParameter1i();
    this.lightmodel.ref();
    this.lightmodel.name.setValue( "coin_light_model");
    this.lightmodel.value.setValue(1);
  }
  this.fragmentshader.parameter.set1Value(this.fragmentshader.parameter.getNum(), texmap);
  if (texmap1 != null) this.fragmentshader.parameter.set1Value(this.fragmentshader.parameter.getNum(), texmap1);
  this.fragmentshader.parameter.set1Value(this.fragmentshader.parameter.getNum(), this.texunit0);
  if (this.numtexunitsinscene > 1) this.fragmentshader.parameter.set1Value(this.fragmentshader.parameter.getNum(), this.texunit1);
  this.fragmentshader.parameter.set1Value(this.fragmentshader.parameter.getNum(), this.lightmodel);

  if (twosidetest) {
    if (this.twosided == null) {
      this.twosided = new SoShaderParameter1i();
      this.twosided.ref();
      this.twosided.name.setValue("coin_two_sided_lighting");
      this.twosided.value.setValue(0);
    }
    this.fragmentshader.parameter.set1Value(this.fragmentshader.parameter.getNum(), this.twosided);
  }

  for (i = 0; i < numshadowlights; i++) {
    SoShadowLightCache cache = this.shadowlights.operator_square_bracket(i);

    if (cache.light.isOfType(SoShadowDirectionalLight.getClassTypeId())) {
      //String str;
      SoShadowDirectionalLight sl = (SoShadowDirectionalLight) (cache.light);
      if (sl.maxShadowDistance.getValue() > 0.0f) {
        SoShaderParameter1f maxdist = cache.maxshadowdistance;
        maxdist.value.connectFrom(sl.maxShadowDistance);
        str = "maxshadowdistance"+ i;
        if (!maxdist.name.getValue().equals(str)) {
          maxdist.name.setValue(str);
        }
        String uniform;
        uniform = "uniform float "+str+";\n";
        gen.addDeclaration(uniform, false);
        this.fragmentshader.parameter.set1Value(this.fragmentshader.parameter.getNum(), maxdist);
      }

      SoShaderParameter4f lightplane = cache.fragment_lightplane;
      str = "lightplane"+ i;
      if (!lightplane.name.getValue().equals(str)) {
        lightplane.name.setValue(str);
      }
      this.fragmentshader.parameter.set1Value(this.fragmentshader.parameter.getNum(), lightplane);
    }
  }

  this.shadowlightsvalid = true;
  // never update unless the program has actually changed. Creating a
  // new GLSL program is very slow on current drivers.
  if (!Objects.equals(this.fragmentshader.sourceProgram.getValue(), gen.getShaderProgram())) {
    // invalidate spotlights, and make sure the cameratransform variable is updated
    this.cameratransform.value.touch();
    this.fragmentshader.sourceProgram.setValue(gen.getShaderProgram());
    this.fragmentshader.sourceType.setValue(SoShaderObject.SourceType.GLSL_PROGRAM);

//#if 0 // for debugging
//    fprintf(stderr,"new fragment program: %s\n",
//            gen.getShaderProgram().getString());
//#endif // debugging

  }

  this.fragmentshadercache.set(gen.getShaderProgram());
  state.pop();
  SoCacheElement.setInvalid(storedinvalid);
}


public void
updateSpotCamera(SoState state, SoShadowLightCache cache, final SbMatrix transform)
{
  SoCamera cam = cache.camera;
  SoSpotLight light = (SoSpotLight) (cache.light);

  assert(cam.isOfType(SoPerspectiveCamera.getClassTypeId()));
  SbVec3f pos = new SbVec3f(light.location.getValue());
  transform.multVecMatrix(pos, pos);

  SbVec3f dir = new SbVec3f(light.direction.getValue());
  transform.multDirMatrix(dir, dir);
  dir.normalize();
  float cutoff = light.cutOffAngle.getValue();
  cam.position.setValue(pos);
  // the maximum heightAngle we can render with a camera is < PI/2,.
  // The max cutoff is therefore PI/4. Some slack is needed, and 0.78
  // is about the maximum angle we can do.
  if (cutoff > 0.78f) cutoff = 0.78f;

  cam.orientation.setValue(new SbRotation(new SbVec3f(0.0f, 0.0f, -1.0f), dir));
  ((SoPerspectiveCamera) cam).heightAngle.setValue(cutoff * 2.0f);
  SoShadowGroup.VisibilityFlag visflag = SoShadowGroup.VisibilityFlag.fromValue(this.master.visibilityFlag.getValue());

  float visnear = this.master.visibilityNearRadius.getValue();
  float visfar = this.master.visibilityRadius.getValue();

  boolean needbbox =
    (visflag == SoShadowGroup.VisibilityFlag.LONGEST_BBOX_EDGE_FACTOR) ||
    (visflag == SoShadowGroup.VisibilityFlag.PROJECTED_BBOX_DEPTH_FACTOR) ||
    ((visnear < 0.0f) || (visfar < 0.0f));

  if (light.isOfType(SoShadowSpotLight.getClassTypeId())) {
    SoShadowSpotLight sslight = (SoShadowSpotLight) (light);
    float ssnear = sslight.nearDistance.getValue();
    float ssfar = sslight.farDistance.getValue();

    if (ssnear > 0.0f && ssfar > ssnear) {
      visnear = ssnear;
      visfar = ssfar;
      needbbox = false;
    }
  }
  if (needbbox) {
    SbXfBox3f worldbox = this.calcBBox(cache);
    SbBox3f box = cache.toCameraSpace(worldbox);

    // Bounding box was calculated in camera space, so we need to "flip"
    // the box (because camera is pointing in the (0,0,-1) direction
    // from origo.
    cache.nearval = -box.getMax().getValueRead()[2];
    cache.farval = -box.getMin().getValueRead()[2];

    int depthbits = 16;
    float r = (float) Math.pow(2.0, (double) depthbits);
    float nearlimit = cache.farval / r;

    if (cache.nearval < nearlimit) {
      cache.nearval = nearlimit;
    }
    float SLACK = 0.001f;

    cache.nearval = cache.nearval * (1.0f - SLACK);
    cache.farval = cache.farval * (1.0f + SLACK);

    if (visflag == SoShadowGroup.VisibilityFlag.LONGEST_BBOX_EDGE_FACTOR) {
      final float[] sx = new float[1],sy = new float[1],sz = new float[1];
      worldbox.getSize(sx, sy, sz);
      float smax =  Math.max(Math.max(sx[0], sy[0]), sz[0]);
        if (visnear > 0.0f) visnear = smax * visnear;
        if (visfar > 0.0f) visfar = smax  * visfar;
    }
    else if (visflag == SoShadowGroup.VisibilityFlag.PROJECTED_BBOX_DEPTH_FACTOR) {
      if (visnear > 0.0f) visnear = cache.farval * visnear; // should be calculated from farval, not nearval
      if (visfar > 0.0f) visfar = cache.farval * visfar;
    }
  }

  if (visnear > 0.0f) cache.nearval = visnear;
  if (visfar > 0.0f) cache.farval = visfar;

  if (cache.nearval != cam.nearDistance.getValue()) {
    cam.nearDistance.setValue(cache.nearval);
  }
  if (cache.farval != cam.farDistance.getValue()) {
    cam.farDistance.setValue(cache.farval);
  }

  float realfarval = cutoff >= 0.0f ? cache.farval / (float)(Math.cos(cutoff * 2.0f)) : cache.farval;
  cache.fragment_farval.value.setValue(realfarval);
  cache.vsm_farval.value.setValue(realfarval);

  cache.fragment_nearval.value.setValue(cache.nearval);
  cache.vsm_nearval.value.setValue(cache.nearval);

  SbViewVolume vv = new SbViewVolume(cam.getViewVolume(1.0f));
  final SbMatrix affine = new SbMatrix(), proj = new SbMatrix();

  vv.getMatrices(affine, proj);
  cache.matrix.copyFrom(affine.operator_mul(proj));
}

public void
updateDirectionalCamera(SoState state, SoShadowLightCache cache, final SbMatrix transform)
{
  SoOrthographicCamera cam = (SoOrthographicCamera)(cache.camera);
  assert(cache.light.isOfType(SoShadowDirectionalLight.getClassTypeId()));
  SoShadowDirectionalLight light = (SoShadowDirectionalLight) (cache.light);

  float maxdist = light.maxShadowDistance.getValue();

  final SbVec3f dir = new SbVec3f(light.direction.getValue());
  dir.normalize();
  transform.multDirMatrix(dir, dir);
  dir.normalize();
  cam.orientation.setValue(new SbRotation(new SbVec3f(0.0f, 0.0f, -1.0f), dir));

  final SbViewVolume vv = new SbViewVolume(SoViewVolumeElement.get(state));
  final SbXfBox3f worldbox = this.calcBBox(cache);
  boolean visible = true;
  if (maxdist > 0.0f) {
    float nearv = vv.getNearDist();
    if (maxdist < nearv) visible = false;
    else {
      maxdist -= nearv;
      float depth = vv.getDepth();
      if (maxdist > depth) maxdist = depth;
      vv.copyFrom(vv.zNarrow(1.0f, 1.0f - maxdist/depth));
    }
  }
  final SbBox3f isect = new SbBox3f();
  if (visible) {
    isect.copyFrom(vv.intersectionBox(worldbox));
    if (isect.isEmpty()) visible = false;
  }
  if (!visible) {
    if (cache.depthmap.scene.getValue() == cache.depthmapscene) {
      cache.depthmap.scene.setValue( new SoInfo());
    }
    return;
  }
  if (cache.depthmap.scene.getValue() != cache.depthmapscene) {
    cache.depthmap.scene.setValue(cache.depthmapscene);
  }
  cam.viewBoundingBox(isect, 1.0f, 1.0f);

  SbBox3f box = cache.toCameraSpace(worldbox);

  // Bounding box was calculated in camera space, so we need to "flip"
  // the box (because camera is pointing in the (0,0,-1) direction
  // from origo. Add a little slack (multiply by 1.01)
  cam.nearDistance.setValue( -box.getMax().getValueRead()[2]*1.01f);
  cam.farDistance.setValue( -box.getMin().getValueRead()[2]*1.01f);

  final SbPlane plane = new SbPlane(dir, cam.position.getValue());
  // move to eye space
  plane.transform(SoViewingMatrixElement.get(state));
  SbVec3f N = new SbVec3f(plane.getNormal());
  float D = plane.getDistanceFromOrigin();

//#if 0
//  fprintf(stderr,"isect: %g %g %g, %g %g %g\n",
//          isect.getMin()[0],
//          isect.getMin()[1],
//          isect.getMin()[2],
//          isect.getMax()[0],
//          isect.getMax()[1],
//          isect.getMax()[2]);
//  fprintf(stderr,"plane: %g %g %g, %g\n", N[0], N[1], N[2], D);
//  fprintf(stderr,"nearfar: %g %g\n", cam.nearDistance.getValue(), cam.farDistance.getValue());
//  fprintf(stderr,"aspect: %g\n", SoViewportRegionElement::get(state).getViewportAspectRatio());
//#endif

  cache.fragment_lightplane.value.setValue(N.getValueRead()[0], N.getValueRead()[1], N.getValueRead()[2], D);

  //SoShadowGroup::VisibilityFlag visflag = (SoShadowGroup::VisibilityFlag) this.master.visibilityFlag.getValue();

  float visnear = cam.nearDistance.getValue();
  float visfar = cam.farDistance.getValue();

  cache.nearval = visnear;
  cache.farval = visfar;

  if (cache.nearval != cam.nearDistance.getValue()) {
    cam.nearDistance.setValue(cache.nearval);
  }
  if (cache.farval != cam.farDistance.getValue()) {
    cam.farDistance.setValue(cache.farval);
  }

  float realfarval = cache.farval * 1.1f;
  cache.fragment_farval.value.setValue(realfarval);
  cache.vsm_farval.value.setValue(realfarval);

  cache.fragment_nearval.value.setValue(cache.nearval);
  cache.vsm_nearval.value.setValue(cache.nearval);

  vv.copyFrom(new SbViewVolume(cam.getViewVolume(1.0f)));
  final SbMatrix affine = new SbMatrix(), proj = new SbMatrix();
  vv.getMatrices(affine, proj);
  cache.matrix.copyFrom( affine.operator_mul(proj));
}

public void
renderDepthMap(SoShadowLightCache cache,
                               SoGLRenderAction action)
{
  cache.depthmap.GLRender(action);
  if (cache.gaussmap != null) cache.gaussmap.GLRender(action);
}


public SbXfBox3f 
calcBBox(SoShadowLightCache cache)
{
  if (cache.light.isOfType(SoShadowDirectionalLight.getClassTypeId())) {
    SoShadowDirectionalLight sl = (SoShadowDirectionalLight) (cache.light);
    SbVec3fSingle size = new SbVec3fSingle(sl.bboxSize.getValue());
    if (size.getValue()[0] >= 0.0f && size.getValue()[1] >= 0.0f && size.getValue()[2] >= 0.0f) {
      SbVec3f center = new SbVec3f(sl.bboxCenter.getValue());
      size.operator_mul_equal(0.5f);
      this.bboxaction.getXfBoundingBox().copyFrom(new SbXfBox3f(center.operator_minus(size), center.operator_add(size)));
    }
    else {
      this.bboxaction.apply(cache.bboxnode);
    }
  }
  else {
    this.bboxaction.apply(cache.bboxnode);
  }
  return this.bboxaction.getXfBoundingBox();
}

private void getQuality(SoState state, boolean[] perpixelspot, boolean[] perpixelother) {
    float quality = this.master.quality.getValue();
    perpixelspot[0] = false;
    perpixelother[0] = false;

    if (quality > 0.3) {
      perpixelspot[0] = true;
    }
    if (quality > 0.7) {
      perpixelother[0] = true;
    }
  }

void initLightMaterial(SoShaderGenerator gen, int i) {
    String str;
    str = "ambient = gl_LightSource["+i+"].ambient;\n"+
                "diffuse = gl_LightSource["+i+"].diffuse;\n"+
                "specular = gl_LightSource["+i+"].specular;\n";
    gen.addMainStatement(str);
  }

//void addDirectionalLight(SoShaderGenerator gen, int i) {
//    initLightMaterial(gen, i);
//    String str;
//    str = "DirectionalLight(normalize(vec3(gl_LightSource["+i+"].position)),"+
//                "vec3(gl_LightSource["+i+"].halfVector), normal, diffuse, specular);";
//    gen.addMainStatement(str);
//  }

void addDirectionalLight(SoShaderGenerator gen, int i) {
    initLightMaterial(gen, i);
    String str;
    str = "DirectionalLight(normalize(vec3(gl_LightSource["+i+"].position)),"+
    		"normalize(eye),"+
                "normalize(normalize(vec3(gl_LightSource["+i+"].position))+normalize(eye)), normal, diffuse, specular);";
    gen.addMainStatement(str);
  }

	void addSpotLight(SoShaderGenerator gen, int i) {
		addSpotLight( gen, i, false);
	}
  void addSpotLight(SoShaderGenerator gen, int i, boolean needdist /*= FALSE*/) {
    initLightMaterial(gen, i);
    String dist = needdist ? "dist = " : "";
    String str;
    str = dist+" SpotLight("+
                "vec3(gl_LightSource["+i+"].position),"+
                "vec3(gl_LightSource["+i+"].constantAttenuation,"+
                "     gl_LightSource["+i+"].linearAttenuation,"+
                "     gl_LightSource["+i+"].quadraticAttenuation),"+
                "normalize(gl_LightSource["+i+"].spotDirection),"+
                "gl_LightSource["+i+"].spotExponent,"+
                "gl_LightSource["+i+"].spotCosCutoff,"+
                "eye, ecPosition3, normal, ambient, diffuse, specular);";
    gen.addMainStatement(str);
  }
  
  void addDirSpotLight(SoShaderGenerator gen, int i, boolean needdist /*= FALSE*/) {
    initLightMaterial(gen, i);
    String dist = needdist ? "dist = " : "";
    String str;
    str = dist+" DirSpotLight("+
                " -normalize(vec3(gl_LightSource["+i+"].spotDirection)),"+
                " vec3(gl_LightSource["+i+"].position),"+
                " eye, ecPosition3, normal, diffuse, specular);";
    gen.addMainStatement(str);
  }

  void addPointLight(SoShaderGenerator gen, int i) {
    initLightMaterial(gen, i);
    String str;
    str = "PointLight("+
                "vec3(gl_LightSource["+i+"].position),"+
                "vec3(gl_LightSource["+i+"].constantAttenuation,"+
                "     gl_LightSource["+i+"].linearAttenuation,"+
                "     gl_LightSource["+i+"].quadraticAttenuation),"+
                " eye, ecPosition3, normal, ambient, diffuse, specular);";

    gen.addMainStatement(str);

  }

  int getFog(SoState state) {
	    return SoEnvironmentElement.getFogType(state);
	  }

}
