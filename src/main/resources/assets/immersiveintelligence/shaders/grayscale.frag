#version 130

//Author: Pabilo8 (pabilo@iiteam.net)
//Grayscale shader with a darkness parameter
uniform float darkness;

uniform sampler2D texture;
uniform sampler2D lightmap;

void main()
{
    vec4 tex = texture2D(texture, gl_TexCoord[0].st);
    vec4 light = texture2D(lightmap, gl_TexCoord[1].st);
    vec4 color = tex * gl_Color * light * gl_Color;

    gl_FragColor = vec4(vec3(dot(color.rgb, vec3(0.299, 0.587, 0.114))) * (1.0 - darkness), 1.0);
}