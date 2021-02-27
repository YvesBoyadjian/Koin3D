varying vec3 ecPosition3;
varying vec3 fragmentNormal;
varying vec2 coordTex;

void main(void)
{
    vec4 ecPosition = gl_ModelViewMatrix * gl_Vertex;
    ecPosition3 = ecPosition.xyz / ecPosition.w;
    fragmentNormal = normalize(gl_NormalMatrix * gl_Normal);
    coordTex = gl_MultiTexCoord0.st;

    gl_Position = ftransform();
    gl_FrontColor = gl_Color;
}
