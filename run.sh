#!/bin/bash
vertexshader=default
pixelshader=default
material=core

java -jar dist/assettool.jar             \
    --viewmodel assets/$1                \
    --mtlfile assets/matlib/$2.mtl       \
    --vs assets/shaders/$vertexshader.vs \
    --ps assets/shaders/$pixelshader.ps
