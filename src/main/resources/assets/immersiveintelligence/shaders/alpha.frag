#version 130

//Author: Pabilo8
//Alpha fragment shader
uniform float alpha;// Passed in by callback

//Texture (GL_TEXTURE0)
uniform sampler2D texture;
//Lightmap Texture (GL_TEXTURE1)
//uniform sampler2D lightmap;

/*vec3 vanillaLight() {
    return clamp(texture2D(lightmap, gl_TexCoord[1].st).xyz, 0.0f, 1.0f);
}*/

void main()
{
    //    gl_FragColor = texture2D(texture, gl_TexCoord[0].st) * gl_Color * vec4(vanillaLight(), alpha);
    gl_FragColor = texture2D(texture, gl_TexCoord[0].st) * gl_Color * vec4(1, 1, 1, alpha);
}