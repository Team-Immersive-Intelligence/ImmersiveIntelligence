#version 130

//Author: Pabilo8 (pabilo@iiteam.net)
//Light-blue energy shader for electrical charge elements
uniform float alpha;
uniform float time;

uniform sampler2D texture;
uniform sampler2D lightmap;

float noise(in vec2 coordinate, in float seed)
{
    vec2 texel = floor(textureSize(texture, 0) * coordinate);
    return fract(sin(dot(texel + seed, vec2(12.9898, 78.233))) * 43758.5453);
}

void main()
{
    vec4 tex = texture2D(texture, gl_TexCoord[0].st);
    float flicker = 0.85 + noise(gl_TexCoord[0].st, time) * 0.15;
    vec3 energyColor = vec3(0.45, 0.85, 1.0) * flicker;

    gl_FragColor = vec4(tex.rgb * gl_Color.rgb * energyColor, tex.a * gl_Color.a * alpha);
}
