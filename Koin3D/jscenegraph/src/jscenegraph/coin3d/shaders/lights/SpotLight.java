/**
 * 
 */
package jscenegraph.coin3d.shaders.lights;

/**
 * @author Yves Boyadjian
 *
 */
public class SpotLight {

	/**
	 * 
	 */
	public SpotLight() {
	}

	public static final String SPOTLIGHT_shadersource =
			  "\n"+
			  "float SpotLight(in vec3 light_position,\n"+
			  "                in vec3 light_attenuation,\n"+
			  "                in vec3 light_spotDirection,\n"+
			  "                in float light_spotExponent,\n"+
			  "                in float light_spotCosCutOff,\n"+
			  "                in vec3 eye,\n"+
			  "                in vec3 ecPosition3,\n"+
			  "                in vec3 normal,\n"+
			  "                inout vec4 ambient,\n"+
			  "                inout vec4 diffuse,\n"+
			  "                inout vec4 specular)\n"+
			  "{\n"+
			  "  float nDotVP;\n"+
			  "  float nDotHV;\n"+
			  "  float pf;\n"+
			  "  float att;\n"+
			  "  float spotDot;\n"+
			  "  float spotAtt;\n"+
			  "  float d;\n"+
			  "  vec3 VP;\n"+
			  "  vec3 halfvec;\n"+
			  "\n"+
			  "  VP = light_position - ecPosition3;\n"+
			  "  d = length(VP);\n"+
			  "  VP = normalize(VP);\n"+
			  "\n"+
			  "  att = 1.0 / (light_attenuation.x +\n"+
			  "               light_attenuation.y * d +\n"+
			  "               light_attenuation.z * d * d);\n"+
			  "\n"+
			  "  spotDot = dot(-VP, light_spotDirection);\n"+
			  "\n"+
			  "  // need to read this variable outside the if statment to work around ATi driver issues\n"+
			  "  float spotexp = light_spotExponent;\n"+
			  "\n"+
			  "  if (spotDot < light_spotCosCutOff)\n"+
			  "    spotAtt = 0.0;\n"+
			  "  else\n"+
			  "    spotAtt = pow(spotDot, spotexp);\n"+
			  "\n"+
			  "  att *= spotAtt;\n"+
			  "\n"+
			  "  halfvec = normalize(VP + eye);\n"+
			  "  nDotVP = max(0.0, dot(normal, VP));\n"+
			  "  nDotHV = max(0.0, dot(normal, halfvec));\n"+
			  "\n"+
			  "  // need to read this variable outside the if statment to work around ATi driver issues\n"+
			  "  float shininess =  gl_FrontMaterial.shininess;\n"+
			  "\n"+
			  "  if (nDotVP == 0.0)\n"+
			  "    pf = 0.0;\n"+
			  "  else\n"+
			  "    pf = pow(nDotHV, shininess);\n"+
			  "\n"+
			  "  ambient *= att;\n"+
			  "  diffuse *= nDotVP * att;\n"+
			  "  specular *= pf * att;\n"+
			  "\n"+
			  "  return d;\n"+
			  "}\n"+
			  "\n";

	
}
