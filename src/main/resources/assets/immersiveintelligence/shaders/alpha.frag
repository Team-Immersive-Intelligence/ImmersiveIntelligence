#version 130

//Author: Pabilo8
//Simple alpha fragment shader
uniform float alpha;// Passed in by callback
uniform sampler2D bgl_RenderedTexture;

void main()
{
    gl_FragColor = texture2D(bgl_RenderedTexture, vec2(gl_TexCoord[0])) * gl_Color * vec4(1f, 1f, 1f, alpha);
}