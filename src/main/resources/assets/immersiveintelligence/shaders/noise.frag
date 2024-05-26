#version 130

uniform float time;
uniform sampler2D bgl_RenderedTexture;

float noise(in vec2 coordinate, in float seed)
{
    vec2 coordActual = floor(textureSize(bgl_RenderedTexture, 0) * coordinate);
    return fract(sin(dot(coordActual*seed, vec2(12.9898, 78.233)))*43758.5453);
}

void main()
{
    float n = (noise(vec2(gl_TexCoord[0]), time) - 0.5) * 0.25;
    gl_FragColor = texture2D(bgl_RenderedTexture, vec2(gl_TexCoord[0])) * gl_Color * vec4(1-n, 1-n, 1-n, 1);
}