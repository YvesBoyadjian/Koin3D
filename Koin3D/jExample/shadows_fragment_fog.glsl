varying vec3 ecPosition3;
varying vec3 fragmentNormal;
varying vec3 perVertexColor;
uniform sampler2D textureMap0;

uniform int coin_texunit0_model;

uniform int coin_light_model;

void main(void) {
vec3 normal = normalize(fragmentNormal);

vec3 eye = -normalize(ecPosition3);

vec4 ambient = vec4(0.0);
vec4 diffuse = vec4(0.0);
vec4 specular = vec4(0.0);
vec4 mydiffuse = gl_Color;
vec4 texcolor = (coin_texunit0_model != 0) ? texture2D(textureMap0, gl_TexCoord[0].xy) : vec4(1.0);

vec3 color = perVertexColor;
vec3 scolor = vec3(0.0);
float dist;
float shadeFactor;
vec3 coord;
vec4 map;
mydiffuse.a *= texcolor.a;

color = vec3(clamp(color.r, 0.0, 1.0), clamp(color.g, 0.0, 1.0), clamp(color.b, 0.0, 1.0));
if (coin_light_model != 0) { color *= texcolor.rgb; color += scolor; }
else color = mydiffuse.rgb * texcolor.rgb;

gl_FragColor = vec4(color, mydiffuse.a);
}
