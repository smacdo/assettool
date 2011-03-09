#version 110
attribute vec4 gl_Color;
varying vec4 gl_FrontColor;
varying vec4 gl_BackColor;

varying vec4 gl_Color;

void main()
{
    gl_FrontColor = gl_Color;
    gl_Position   = ftransform();
}
