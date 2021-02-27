/**
 * 
 */
package jscenegraph.coin3d.shaders.lights;

/**
 * @author Yves Boyadjian
 *
 */
public class DirSpotLight {

	/**
	 * 
	 */
	public DirSpotLight() {
	}

	public static final String DIRSPOTLIGHT_shadersource =
			  "\n"+
			  "float DirSpotLight(in vec3 dir,\n"+
			  "                   in vec3 light_position,\n"+
			  "                   in vec3 eye,\n"+
			  "                   in vec3 ecPosition3,\n"+
			  "                   in vec3 normal,\n"+
			  "                   inout vec4 diffuse,\n"+
			  "                   inout vec4 specular)\n"+
			  "{\n"+
			  "  float nDotVP;\n"+
			  "  float nDotHV;\n"+
			  "  float pf;\n"+
			  "  vec3 hv = normalize(eye + dir);\n"+
			  "  nDotVP = max(0.0, dot(normal, dir));\n"+
			  "  nDotHV = max(0.0, dot(normal, hv));\n"+
			  "  float shininess = gl_FrontMaterial.shininess;\n"+
			  "  if (nDotVP == 0.0)\n"+
			  "    pf = 0.0;\n"+
			  "  else\n"+
			  "    pf = pow(nDotHV, shininess);\n"+
			  "\n"+
			  "  diffuse *= nDotVP;\n"+
			  "  specular *= pf;\n"+
			  "  return length(light_position - ecPosition3);\n"+
			  "}\n";

	
}
