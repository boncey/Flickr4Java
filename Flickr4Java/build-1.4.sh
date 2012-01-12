#!/bin/sh
# script for building FlickrJ with Java 1.4 on Mac OSX
# requirements:
#  JDK 1.4.2 installed
#  Ant with 1.4 support symlinked in path as ant-java-1.4

export JAVA_HOME=/System/Library/Frameworks/JavaVM.framework/Versions/1.4.2/Home
echo "Java Home: $JAVA_HOME"
ant-java-1.4 dist