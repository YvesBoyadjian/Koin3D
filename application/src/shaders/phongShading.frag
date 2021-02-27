#version 330 core

in vec3 normalInViewSpaceNonNormalized;
in vec3 positionInViewSpace;
in vec3 color;

uniform vec4 sunPositionInViewSpace; //OK
uniform vec3 sunIntensityAndColor; //OK
uniform vec3 skyIntensityAndColor; //OK
uniform vec3 specularAlbedo; //OK
uniform float specularPower; //OK
uniform float gamma; //OK

layout ( location = 0 ) out vec4 fragColor;

vec3 ads() {
    vec3 N = normalize(normalInViewSpaceNonNormalized);
    vec3 L = normalize(vec3(sunPositionInViewSpace));
    vec3 V = normalize(vec3(-positionInViewSpace));

    // Calculate R locally
    vec3 R = reflect(-L,N);

    vec3 ambient = color * skyIntensityAndColor;
    vec3 diffuse = color * max(dot(N,L),0.0) * sunIntensityAndColor;
    vec3 specular = specularAlbedo * pow(max(dot(R,V),0.0), specularPower) * sunIntensityAndColor;

    return ambient + diffuse + specular;
}

void main() {
    // Write final color to the frameBuffer
    fragColor = vec4(pow(ads(),vec3(1.0/gamma)), 1.0);
}

