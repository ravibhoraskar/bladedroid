#!/bin/bash

[ $# -eq 0 ] && { echo "Usage: $0 <blade name> <package name of the app script for>. Eg: $0 LogBlade edu.uw.simpleapp"; exit 1; }

echo "compiling $1.jar"
cd bin/classes/
jar cf $1.jar edu/uw/bladedroid/blade/$1.class
echo "convert to dex"
dx --dex --output $1Dex.jar $1.jar
echo "pushing to sdcard/blades/$2/"
adb push $1Dex.jar /sdcard/blades/$2/$1Dex.jar
echo "clean jar files"
rm $1.jar
rm $1Dex.jar
echo "done"
