package jscenegraph.coin3d.fxviz.nodes;

import jscenegraph.coin3d.inventor.elements.SoEnvironmentElement;
import jscenegraph.coin3d.inventor.misc.SoShaderGenerator;

public class SoVolumetricShadowGroupP extends SoShadowGroupP {

    public SoVolumetricShadowGroupP(SoShadowGroup master) {
        super(master);
    }

    protected void startFragmentShader(SoShaderGenerator gen) {
        int numshadowlights = this.shadowlights.getLength();

        if (numshadowlights != 0) {
            gen.addMainStatement("vec3 g_CameraPosition = 0.0f.xxx;");
            gen.addMainStatement("vec3 endRayPosition = ecPosition3;");
            gen.addMainStatement("mat4 g_ShadowViewProjectionMatrix;");
            gen.addMainStatement("vec3 sunDirection;");
            gen.addMainStatement("vec3 g_SunColor;");

            gen.addMainStatement("vec3 startPosition = g_CameraPosition;");
            gen.addMainStatement("vec3 rayVector = endRayPosition.xyz- startPosition;");
            gen.addMainStatement("float rayLength = length(rayVector);");
            gen.addMainStatement("vec3 rayDirection = rayVector / rayLength;");
            gen.addMainStatement("float stepLength = rayLength / NB_STEPS;");
            gen.addMainStatement("vec3 step = rayDirection * stepLength;");
            gen.addMainStatement("vec3 currentPosition;");
            gen.addMainStatement("vec3 accumFog;");
            gen.addMainStatement("vec3 colorFog = 0.0f.xxx;");
        }
    }

    protected void endShadowLight(SoShaderGenerator gen,int index) {
        gen.addMainStatement("sunDirection = normalize(vec3(gl_LightSource["+index+"].position));");
        gen.addMainStatement("g_SunColor = gl_LightSource["+index+"].diffuse.rgb;");
        //gen.addMainStatement("g_ShadowViewProjectionMatrix = ;");

        gen.addMainStatement("currentPosition = startPosition;");
        gen.addMainStatement("accumFog = 0.0f.xxx;");
        gen.addMainStatement("for (int i = 0; i < NB_STEPS; i++)");
        gen.addMainStatement("{");
        gen.addMainStatement("  vec4 worldInShadowCameraSpace = mul(vec4(currentPosition, 1.0f), g_ShadowViewProjectionMatrix);");
        gen.addMainStatement("  {");
        gen.addMainStatement("    accumFog += ComputeScattering(dot(rayDirection, sunDirection)).xxx * g_SunColor;");
        gen.addMainStatement("  }");
        gen.addMainStatement("  currentPosition += step;");
        gen.addMainStatement("}");
        gen.addMainStatement("accumFog /= NB_STEPS;");
        gen.addMainStatement("colorFog += accumFog * stepLength/5.0;\n");
    }

    protected void endFragmentShader(SoShaderGenerator gen, SoEnvironmentElement.FogType fogType) {
        switch (fogType) {
            case NONE:
                break;
            case HAZE:
                break;
            case FOG:
                gen.addMainStatement("float fog = exp(-gl_Fog.density * abs(ecPosition3.z));");
                gen.addMainStatement("color = mix(gl_Fog.color.rgb, color, clamp(fog, 0.0, 1.0));");
                break;
            case SMOKE:
                break;
        }
    }
}
