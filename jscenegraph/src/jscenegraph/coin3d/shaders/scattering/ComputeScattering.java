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
                "    float F1_SCATTERING = 0.7;\n"+
                "    float G1_SCATTERING = 0.5;\n"+
                "    float G2_SCATTERING = -0.2;\n"+
                "    float PI = 3.14159265359;\n"+
        "    float result1 = 1.0f - G1_SCATTERING * G1_SCATTERING;\n"+
        "    result1 /= (4.0f * PI * pow(1.0f + G1_SCATTERING * G1_SCATTERING - (2.0f * G1_SCATTERING) *      lightDotView, 1.5f));\n"+
        "    float result2 = 1.0f - G2_SCATTERING * G2_SCATTERING;\n"+
        "    result2 /= (4.0f * PI * pow(1.0f + G2_SCATTERING * G2_SCATTERING - (2.0f * G2_SCATTERING) *      lightDotView, 1.5f));\n"+
        "    return F1_SCATTERING * result1 + (1 - F1_SCATTERING) * result2;\n"+
        "}\n";

}
