/**
 * 
 */
package jscenegraph.coin3d.shaders.lights;

/**
 * @author Yves Boyadjian
 *
 */
public class DirectionalLight {

	/**
	 * 
	 */
	public DirectionalLight() {
	}

	public static final String DIRECTIONALLIGHT_shadersourceCoin3D =
			  "\n"+
			  "void DirectionalLight(in vec3 light_vector,\n"+
			  "                      in vec3 light_halfVector,\n"+
			  "                      in vec3 normal,\n"+
			  "                      inout vec4 diffuse,\n"+
			  "                      inout vec4 specular)\n"+
			  "{\n"+
			  "  float nDotVP; // normal . light direction\n"+
			  "  float nDotHV; // normal . light half vector\n"+
			  "  float pf;     // power factor\n"+
			  "\n"+
			  "  nDotVP = max(0.0, dot(normal, light_vector));\n"+
			  "  nDotHV = max(0.0, dot(normal, light_halfVector));\n"+
			  "\n"+
			  "  float shininess = gl_FrontMaterial.shininess;\n"+
			  "  if (nDotVP == 0.0)\n"+
			  "    pf = 0.0;\n"+
			  "  else\n"+
			  "    pf = pow(nDotHV, shininess);\n"+
			  "\n"+
			  "  diffuse *= nDotVP;  \n"+
			  "  specular *= pf;\n"+
			  "}\n"+
			  "\n";
	
	public static final String DIRECTIONALLIGHT_shadersource =
			  "\n"+
			  "void DirectionalLight(in vec3 light_vector,\n"+
			  "                      in vec3 eye,\n"+
			  "                      in vec3 light_halfVector,\n"+
			  "                      in vec3 normal,\n"+
			  "                      inout vec4 diffuse,\n"+
			  "                      inout vec4 specular)\n"+
			  "{\n"+
			  "  float nDotVP; // normal . light direction\n"+
			  "  float nDotHV; // normal . light half vector\n"+
			  "  float pf;     // power factor\n"+
			  "  float fZero = 0.2;"+ //
			  "\n"+
			  "  nDotVP = max(0.0, dot(normal, light_vector));\n"+
			  "  nDotHV = max(0.0, dot(normal, light_halfVector));\n"+
			  "\n"+
			  "  float shininess = gl_FrontMaterial.shininess;\n"+
			  "  if (nDotVP == 0.0)\n"+
			  "    pf = 0.0;\n"+
			  "  else\n"+
			  "    pf = pow(nDotHV, shininess);\n"+
			  "\n"+
			  "float base = 1-dot(eye, light_halfVector);"+

			  "float exp = pow(base, 5);"+

			  "float fresnel = fZero + (1 - fZero)*exp;"+			  
			  "  diffuse *= nDotVP;  \n"+			  
			  "  specular *= pf;\n"+
			  "  specular *= fresnel;\n"+
			  "}\n"+
			  "\n";
	
}
