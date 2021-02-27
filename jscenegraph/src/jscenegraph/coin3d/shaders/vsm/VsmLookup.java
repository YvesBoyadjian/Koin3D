/**
 * 
 */
package jscenegraph.coin3d.shaders.vsm;

/**
 * @author Yves Boyadjian
 *
 */
public class VsmLookup {

	/**
	 * 
	 */
	public VsmLookup() {
	}

	public static final String VSMLOOKUP_shadersource =
			  "float VsmLookup(in vec4 map, in float dist, in float epsilon, float bleedthreshold)\n"+
			  "{\n"+
			  "  float mapdist = map.x;\n"+
			  "\n"+
			  "  // replace 0.0 with some factor > 0.0 to make the light affect even parts in shadow\n"+
			  "  float lit_factor = dist <= mapdist ? 1.0 : 0.0;\n"+
			  "  float E_x2 = map.y;\n"+
			  "  float Ex_2 = mapdist * mapdist;\n"+
			  "  float variance = min(max(E_x2 - Ex_2, 0.0) + epsilon, 1.0);\n"+
			  "\n"+
			  "  float m_d = mapdist - dist;\n"+
			  "  float p_max = variance / (variance + m_d * m_d);\n"+
			  "\n"+
			  "  p_max *= smoothstep(bleedthreshold, 1.0, p_max);\n"+
			  "\n"+
			  "  return max(lit_factor, p_max);\n"+
			  "}\n";
	
}
