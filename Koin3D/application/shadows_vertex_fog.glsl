varying vec4 shadowCoord0;
varying vec4 shadowCoord1;
varying vec4 shadowCoord2;
varying vec4 shadowCoord3;
uniform mat4 cameraTransform;
varying vec3 ecPosition3;
varying vec3 fragmentNormal;
varying vec3 perVertexColor;
void main(void) {
vec4 ecPosition = gl_ModelViewMatrix * gl_Vertex;
ecPosition3 = ecPosition.xyz / ecPosition.w;
vec3 normal = normalize(gl_NormalMatrix * gl_Normal);
vec3 eye = -normalize(ecPosition3);
vec4 ambient;
vec4 diffuse;
vec4 specular;
vec4 accambient = vec4(0.0);
vec4 accdiffuse = vec4(0.0);
vec4 accspecular = vec4(0.0);
vec4 color;

fragmentNormal = normal;
color = gl_FrontLightModelProduct.sceneColor;

vec4 pos = cameraTransform * ecPosition;

shadowCoord0 = gl_TextureMatrix[15] * pos;

shadowCoord1 = gl_TextureMatrix[14] * pos;

shadowCoord2 = gl_TextureMatrix[13] * pos;

shadowCoord3 = gl_TextureMatrix[12] * pos;

gl_FogFragCoord = abs(ecPosition3.z);

perVertexColor = vec3(clamp(color.r, 0.0, 1.0), clamp(color.g, 0.0, 1.0), clamp(color.b, 0.0, 1.0));gl_TexCoord[0] = gl_TextureMatrix[0] * gl_MultiTexCoord0;
gl_TexCoord[1] = gl_TextureMatrix[1] * gl_MultiTexCoord1;
gl_Position = ftransform();
gl_FrontColor = gl_Color;

}
