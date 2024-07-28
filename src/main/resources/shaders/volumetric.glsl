#version 450 core

layout(std430, binding = 0) buffer pos
{
    vec4 positions[];
};

void main()
{
    vec3 pos = positions[gl_GlobalInvocationID.x];

    pos = pos + 1;

    positions[gl_GlobalInvocationID.x] = pos;
}