#! /bin/sh

#export CLASSPATH='.:../gdata/java/lib/*'
export CLASSPATH='./build/classes:../gdata/java/lib/*'
exec java $*
