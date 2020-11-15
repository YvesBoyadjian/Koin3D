package jscenegraph.coin3d.shaders.scattering;

public class ComputeScattering {

    /**
     *
     */
    public ComputeScattering() {
    }

    public static final String COMPUTESCATTERING_shadersource =
    "// Mie scaterring approximated with Henyey-Greenstein phase function.\n"+
    "float ComputeScattering(float lightDotView)\n"+
    "{\n"+
            "    float G_SCATTERING = 0.5;\n"+
            "    float PI = 3.14159265359;\n"+
    "    float result = 1.0f - G_SCATTERING * G_SCATTERING;\n"+
    "    result /= (4.0f * PI * pow(1.0f + G_SCATTERING * G_SCATTERING - (2.0f * G_SCATTERING) *      lightDotView, 1.5f));\n"+
    "    return result;\n"+
    "}\n";

}
