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
            gen.addMainStatement("vec3 g_CameraPosition = vec3(0.0f,0.0f,0.0f);");
            gen.addMainStatement("vec3 endRayPosition = ecPosition3;");
            gen.addMainStatement("mat4 g_ShadowViewProjectionMatrix;");
            gen.addMainStatement("vec3 sunDirection;");
            gen.addMainStatement("vec3 g_SunColor;");

            gen.addMainStatement("vec3 startPosition = g_CameraPosition;");
            gen.addMainStatement("vec3 rayVector = endRayPosition.xyz - startPosition;");
            gen.addMainStatement("float rayLength = length(rayVector);");
            gen.addMainStatement("vec3 rayDirection = rayVector / rayLength;");
            gen.addMainStatement("float stepLength = rayLength / NB_STEPS;");
            gen.addMainStatement("vec3 step = rayDirection * stepLength;");
            gen.addMainStatement("vec3 currentPosition;");
            gen.addMainStatement("vec3 accumFog;");
            gen.addMainStatement("vec3 colorFog = vec3(0.0f,0.0f,0.0f);");
        }
    }

    protected void endShadowLight(SoShaderGenerator gen,int index) {
        gen.addMainStatement("sunDirection = normalize(vec3(gl_LightSource["+index+"].position));");
        gen.addMainStatement("g_SunColor = gl_LightSource["+index+"].diffuse.rgb;");
        //gen.addMainStatement("g_ShadowViewProjectionMatrix = ;");

        gen.addMainStatement("currentPosition = startPosition;");
        gen.addMainStatement("accumFog = vec3(0.0f,0.0f,0.0f);");
        gen.addMainStatement("for (int i = 0; i < NB_STEPS; i++)");
        gen.addMainStatement("{");
        gen.addMainStatement("  vec4 worldInShadowCameraSpace = vec4(currentPosition, 1.0f) * g_ShadowViewProjectionMatrix;");
        gen.addMainStatement("  {");
        gen.addMainStatement("    float scatter = ComputeScattering(dot(rayDirection, sunDirection));");
        gen.addMainStatement("    accumFog += vec3(scatter,scatter,scatter) * g_SunColor;");
        gen.addMainStatement("  }");
        gen.addMainStatement("  currentPosition += step;");
        gen.addMainStatement("}");
        gen.addMainStatement("accumFog /= NB_STEPS;");
        gen.addMainStatement("colorFog += accumFog * rayLength * gl_Fog.density *5;\n");
    }

    protected void endFragmentShader(SoShaderGenerator gen, SoEnvironmentElement.FogType fogType) {
        switch (fogType) {
            case NONE:
                break;
            case HAZE:
                break;
            case FOG:
                gen.addMainStatement("float fog = exp(-gl_Fog.density * abs(ecPosition3.z));");
                //gen.addMainStatement("color = mix(gl_Fog.color.rgb, color, clamp(fog, 0.0, 1.0));");
                gen.addMainStatement("color = color * clamp(fog, 0.0, 1.0);");
                gen.addMainStatement("color += colorFog;");
                break;
            case SMOKE:
                break;
        }
    }
}
