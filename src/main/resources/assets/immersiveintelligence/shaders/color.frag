#version 130

//Author: Pabilo8 (pabilo@iiteam.net)
//RGB color shader
uniform vec3 color;// Passed in by callback

uniform sampler2D texture;
uniform sampler2D lightmap;

void main()
{
    vec4 tex = texture2D(texture, gl_TexCoord[0].st);
    vec4 light = texture2D(lightmap, gl_TexCoord[1].st);

    gl_FragColor = tex * gl_Color * light * vec4(color.xyz, 1.0);
}