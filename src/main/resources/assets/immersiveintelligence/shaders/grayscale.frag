#version 130

//Author: Pabilo8 (pabilo@iiteam.net)
//Grayscale shader with a darkness parameter
uniform float darkness;
uniform sampler2D bgl_RenderedTexture;

void main()
{
    vec4 color = texture2D(bgl_RenderedTexture, vec2(gl_TexCoord[0])) * gl_Color;
    vec4 grayscaleColor = vec4(vec3(dot(color.rgb, vec3(0.299, 0.587, 0.114))) * (1.0 - darkness), 1.0);
    gl_FragColor = grayscaleColor;
}