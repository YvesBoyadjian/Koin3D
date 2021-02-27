#version 330 core

// Per-vertex inputs
layout (location = 0) in vec3 vertexPositionInWorld;
layout (location = 1) in vec3 vertexNormalInWorld;
layout (location = 2) in vec3 vertexColor;

out smooth vec3 normalInViewSpaceNonNormalized;
out smooth vec3 positionInViewSpace;
out smooth vec3 color;

uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;

void main() {
    normalInViewSpaceNonNormalized = mat3(modelViewMatrix) * vertexNormalInWorld;
    positionInViewSpace = vec3(modelViewMatrix * vec4(vertexPositionInWorld,1.0));
    color = vertexColor;
    gl_Position = projectionMatrix * vec4(positionInViewSpace,1.0);
}
