#version 130

//Author: Flaxbeard (https://github.com/Flaxbeard/ImmersivePetroleum/blob/1.12.2/src/main/resources/assets/immersivepetroleum/shaders/alpha.frag)
uniform float alpha;// Passed in by callback
uniform float time;

uniform sampler2D texture;
uniform sampler2D lightmap;

float noise(in vec2 coordinate, in float seed)
{
    vec2 coordActual = floor(textureSize(texture, 0) * coordinate);
    return fract(sin(dot(coordActual*seed, vec2(12.9898, 78.233)))*43758.5453);
}

void main()
{
    float n = (noise(vec2(gl_TexCoord[0]), time) - 0.5) * 0.25;
    vec4 tex = texture2D(texture, gl_TexCoord[0].st);

    gl_FragColor = tex * gl_Color * vec4(0.0706 + n, 0.4 + n, 0.7804 + n, alpha);
}