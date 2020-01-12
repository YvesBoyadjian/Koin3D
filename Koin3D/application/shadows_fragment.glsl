const float EPSILON = 3.0E-6;
const float THRESHOLD = 0.9;
uniform sampler2D shadowMap0;
uniform float farval0;
uniform float nearval0;
varying vec4 shadowCoord0;
uniform vec4 lightplane0;
uniform sampler2D shadowMap1;
uniform float farval1;
uniform float nearval1;
varying vec4 shadowCoord1;
uniform vec4 lightplane1;
uniform sampler2D shadowMap2;
uniform float farval2;
uniform float nearval2;
varying vec4 shadowCoord2;
uniform vec4 lightplane2;
uniform sampler2D shadowMap3;
uniform float farval3;
uniform float nearval3;
varying vec4 shadowCoord3;
uniform vec4 lightplane3;
const float DISTRIBUTE_FACTOR = 64.0;

varying vec3 ecPosition3;
varying vec3 fragmentNormal;
varying vec3 perVertexColor;
uniform sampler2D textureMap0;

uniform int coin_texunit0_model;

uniform int coin_light_model;

uniform int coin_two_sided_lighting;

uniform float maxshadowdistance0;

uniform float maxshadowdistance1;

uniform float maxshadowdistance2;

uniform float maxshadowdistance3;

float VsmLookup(in vec4 map, in float dist, in float epsilon, float bleedthreshold)
{
  float mapdist = map.x;

  // replace 0.0 with some factor > 0.0 to make the light affect even parts in shadow
  float lit_factor = dist <= mapdist ? 1.0 : 0.0;
  float E_x2 = map.y;
  float Ex_2 = mapdist * mapdist;
  float variance = min(max(E_x2 - Ex_2, 0.0) + epsilon, 1.0);

  float m_d = mapdist - dist;
  float p_max = variance / (variance + m_d * m_d);

  p_max *= smoothstep(bleedthreshold, 1.0, p_max);

  return max(lit_factor, p_max);
}


void DirectionalLight(in vec3 light_vector,
                      in vec3 eye,
                      in vec3 light_halfVector,
                      in vec3 normal,
                      inout vec4 diffuse,
                      inout vec4 specular)
{
  float nDotVP; // normal . light direction
  float nDotHV; // normal . light half vector
  float pf;     // power factor
  float fZero = 0.2;
  nDotVP = max(0.0, dot(normal, light_vector));
  nDotHV = max(0.0, dot(normal, light_halfVector));

  float shininess = gl_FrontMaterial.shininess;
  if (nDotVP == 0.0)
    pf = 0.0;
  else
    pf = pow(nDotHV, shininess);

float base = 1-dot(eye, light_halfVector);float exp = pow(base, 5);float fresnel = fZero + (1 - fZero)*exp;  diffuse *= nDotVP;  
  specular *= pf;
  specular *= fresnel;
}


void main(void) {
vec3 normal = normalize(fragmentNormal);

if (coin_two_sided_lighting != 0 && !gl_FrontFacing) normal = -normal;

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

dist = dot(ecPosition3.xyz, lightplane0.xyz) - lightplane0.w;

ambient = gl_LightSource[4].ambient;
diffuse = gl_LightSource[4].diffuse;
specular = gl_LightSource[4].specular;

DirectionalLight(normalize(vec3(gl_LightSource[4].position)),normalize(eye),normalize(normalize(vec3(gl_LightSource[4].position))+normalize(eye)), normal, diffuse, specular);
coord = 0.5 * (shadowCoord0.xyz / shadowCoord0.w + vec3(1.0));

map = texture2D(shadowMap0, coord.xy);

map = (map + vec4(1.0)) * 0.5;

map.xy += map.zw / DISTRIBUTE_FACTOR;

shadeFactor = ((map.x < 0.9999) && (shadowCoord0.z > -1.0 && coord.x >= 0.0 && coord.x <= 1.0 && coord.y >= 0.0 && coord.y <= 1.0)) ? VsmLookup(map, (dist - nearval0) / (farval0 - nearval0), EPSILON, THRESHOLD) : 1.0;

shadeFactor = 1.0 - shadeFactor;

shadeFactor *= min(1.0, exp(2.35*ecPosition3.z*abs(ecPosition3.z)/(maxshadowdistance0*maxshadowdistance0)));

shadeFactor = 1.0 - shadeFactor;

color += shadeFactor * diffuse.rgb * mydiffuse.rgb;
scolor += shadeFactor * gl_FrontMaterial.specular.rgb * specular.rgb;

color += ambient.rgb * gl_FrontMaterial.ambient.rgb;

dist = dot(ecPosition3.xyz, lightplane1.xyz) - lightplane1.w;

ambient = gl_LightSource[5].ambient;
diffuse = gl_LightSource[5].diffuse;
specular = gl_LightSource[5].specular;

DirectionalLight(normalize(vec3(gl_LightSource[5].position)),normalize(eye),normalize(normalize(vec3(gl_LightSource[5].position))+normalize(eye)), normal, diffuse, specular);
coord = 0.5 * (shadowCoord1.xyz / shadowCoord1.w + vec3(1.0));

map = texture2D(shadowMap1, coord.xy);

map = (map + vec4(1.0)) * 0.5;

map.xy += map.zw / DISTRIBUTE_FACTOR;

shadeFactor = ((map.x < 0.9999) && (shadowCoord1.z > -1.0 && coord.x >= 0.0 && coord.x <= 1.0 && coord.y >= 0.0 && coord.y <= 1.0)) ? VsmLookup(map, (dist - nearval1) / (farval1 - nearval1), EPSILON, THRESHOLD) : 1.0;

shadeFactor = 1.0 - shadeFactor;

shadeFactor *= min(1.0, exp(2.35*ecPosition3.z*abs(ecPosition3.z)/(maxshadowdistance1*maxshadowdistance1)));

shadeFactor = 1.0 - shadeFactor;

color += shadeFactor * diffuse.rgb * mydiffuse.rgb;
scolor += shadeFactor * gl_FrontMaterial.specular.rgb * specular.rgb;

color += ambient.rgb * gl_FrontMaterial.ambient.rgb;

dist = dot(ecPosition3.xyz, lightplane2.xyz) - lightplane2.w;

ambient = gl_LightSource[6].ambient;
diffuse = gl_LightSource[6].diffuse;
specular = gl_LightSource[6].specular;

DirectionalLight(normalize(vec3(gl_LightSource[6].position)),normalize(eye),normalize(normalize(vec3(gl_LightSource[6].position))+normalize(eye)), normal, diffuse, specular);
coord = 0.5 * (shadowCoord2.xyz / shadowCoord2.w + vec3(1.0));

map = texture2D(shadowMap2, coord.xy);

map = (map + vec4(1.0)) * 0.5;

map.xy += map.zw / DISTRIBUTE_FACTOR;

shadeFactor = ((map.x < 0.9999) && (shadowCoord2.z > -1.0 && coord.x >= 0.0 && coord.x <= 1.0 && coord.y >= 0.0 && coord.y <= 1.0)) ? VsmLookup(map, (dist - nearval2) / (farval2 - nearval2), EPSILON, THRESHOLD) : 1.0;

shadeFactor = 1.0 - shadeFactor;

shadeFactor *= min(1.0, exp(2.35*ecPosition3.z*abs(ecPosition3.z)/(maxshadowdistance2*maxshadowdistance2)));

shadeFactor = 1.0 - shadeFactor;

color += shadeFactor * diffuse.rgb * mydiffuse.rgb;
scolor += shadeFactor * gl_FrontMaterial.specular.rgb * specular.rgb;

color += ambient.rgb * gl_FrontMaterial.ambient.rgb;

dist = dot(ecPosition3.xyz, lightplane3.xyz) - lightplane3.w;

ambient = gl_LightSource[7].ambient;
diffuse = gl_LightSource[7].diffuse;
specular = gl_LightSource[7].specular;

DirectionalLight(normalize(vec3(gl_LightSource[7].position)),normalize(eye),normalize(normalize(vec3(gl_LightSource[7].position))+normalize(eye)), normal, diffuse, specular);
coord = 0.5 * (shadowCoord3.xyz / shadowCoord3.w + vec3(1.0));

map = texture2D(shadowMap3, coord.xy);

map = (map + vec4(1.0)) * 0.5;

map.xy += map.zw / DISTRIBUTE_FACTOR;

shadeFactor = ((map.x < 0.9999) && (shadowCoord3.z > -1.0 && coord.x >= 0.0 && coord.x <= 1.0 && coord.y >= 0.0 && coord.y <= 1.0)) ? VsmLookup(map, (dist - nearval3) / (farval3 - nearval3), EPSILON, THRESHOLD) : 1.0;

shadeFactor = 1.0 - shadeFactor;

shadeFactor *= min(1.0, exp(2.35*ecPosition3.z*abs(ecPosition3.z)/(maxshadowdistance3*maxshadowdistance3)));

shadeFactor = 1.0 - shadeFactor;

color += shadeFactor * diffuse.rgb * mydiffuse.rgb;
scolor += shadeFactor * gl_FrontMaterial.specular.rgb * specular.rgb;

color += ambient.rgb * gl_FrontMaterial.ambient.rgb;

ambient = gl_LightSource[0].ambient;
diffuse = gl_LightSource[0].diffuse;
specular = gl_LightSource[0].specular;

DirectionalLight(normalize(vec3(gl_LightSource[0].position)),normalize(eye),normalize(normalize(vec3(gl_LightSource[0].position))+normalize(eye)), normal, diffuse, specular);
color += ambient.rgb * gl_FrontMaterial.ambient.rgb + diffuse.rgb * mydiffuse.rgb;

scolor += specular.rgb * gl_FrontMaterial.specular.rgb;

ambient = gl_LightSource[1].ambient;
diffuse = gl_LightSource[1].diffuse;
specular = gl_LightSource[1].specular;

DirectionalLight(normalize(vec3(gl_LightSource[1].position)),normalize(eye),normalize(normalize(vec3(gl_LightSource[1].position))+normalize(eye)), normal, diffuse, specular);
color += ambient.rgb * gl_FrontMaterial.ambient.rgb + diffuse.rgb * mydiffuse.rgb;

scolor += specular.rgb * gl_FrontMaterial.specular.rgb;

ambient = gl_LightSource[2].ambient;
diffuse = gl_LightSource[2].diffuse;
specular = gl_LightSource[2].specular;

DirectionalLight(normalize(vec3(gl_LightSource[2].position)),normalize(eye),normalize(normalize(vec3(gl_LightSource[2].position))+normalize(eye)), normal, diffuse, specular);
color += ambient.rgb * gl_FrontMaterial.ambient.rgb + diffuse.rgb * mydiffuse.rgb;

scolor += specular.rgb * gl_FrontMaterial.specular.rgb;

ambient = gl_LightSource[3].ambient;
diffuse = gl_LightSource[3].diffuse;
specular = gl_LightSource[3].specular;

DirectionalLight(normalize(vec3(gl_LightSource[3].position)),normalize(eye),normalize(normalize(vec3(gl_LightSource[3].position))+normalize(eye)), normal, diffuse, specular);
color += ambient.rgb * gl_FrontMaterial.ambient.rgb + diffuse.rgb * mydiffuse.rgb;

scolor += specular.rgb * gl_FrontMaterial.specular.rgb;

color = vec3(clamp(color.r, 0.0, 1.0), clamp(color.g, 0.0, 1.0), clamp(color.b, 0.0, 1.0));
if (coin_light_model != 0) { color *= texcolor.rgb; color += scolor; }
else color = mydiffuse.rgb * texcolor.rgb;

gl_FragColor = vec4(color, mydiffuse.a);
}
