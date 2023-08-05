#version 130

//Author: Pabilo8
//RGB color shader
uniform vec3 color;// Passed in by callback
uniform sampler2D bgl_RenderedTexture;

void main()
{
    gl_FragColor = texture2D(bgl_RenderedTexture, vec2(gl_TexCoord[0])) * gl_Color * vec4(color.xyz,1.0);
}