#version 130

//Author: Pabilo8 (pabilo@iiteam.net)
uniform float time;

uniform sampler2D texture;
uniform sampler2D lightmap;

float noise(in vec2 coordinate, in float seed)
{
    vec2 coordActual = floor(vec2(textureSize(texture, 0)) * coordinate);
    return fract(sin(dot(coordActual * seed, vec2(12.9898, 78.233))) * 43758.5453);
}

void main()
{
    vec4 tex = texture2D(texture, gl_TexCoord[0].st);
    float n = (noise(vec2(gl_TexCoord[0]), time) - 0.5) * 0.25;
    gl_FragColor = tex * gl_Color * vec4(1 - n, 1 - n, 1 - n, 1);
}