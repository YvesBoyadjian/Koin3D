#version 120
void main(void)
{
    gl_Position = ftransform();
    gl_Position.z = 0.999f * gl_Position.w;
    //gl_Position.w = 1;
    gl_FrontColor = gl_Color;
}
