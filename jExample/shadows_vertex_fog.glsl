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
color = gl_FrontLightModelProduct.sceneColor +   accambient * gl_FrontMaterial.ambient +   accdiffuse * gl_Color +  accspecular * gl_FrontMaterial.specular;

perVertexColor = vec3(clamp(color.r, 0.0, 1.0), clamp(color.g, 0.0, 1.0), clamp(color.b, 0.0, 1.0));gl_TexCoord[0] = gl_TextureMatrix[0] * gl_MultiTexCoord0;
gl_TexCoord[1] = gl_TextureMatrix[1] * gl_MultiTexCoord1;
gl_Position = ftransform();
gl_FrontColor = gl_Color;

}
