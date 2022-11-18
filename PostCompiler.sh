#!/usr/bin/env bash

mkdir -p -m a=rwx ~/Humans/openGl/openGLSL
mkdir -p -m a=rwx ~/Humans/res/game

cp -r $( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )/res/game ~/Humans/res/
cp -r $( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )/src/eu/openGLSL ~/Humans/openGl/

cp -r $( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )/out/artifacts/Humans_jar/Humans.jar ~/Humans/
