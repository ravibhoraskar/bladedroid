#!/bin/sh

if [ -z "$1" ];then
	echo "Expected argument: APK"
	exit
fi
APK="$1"

if [ -z "$ANDROID_HOME" ];then
	echo "Please set the ANDROID_HOME environment variable to point to the SDK install location"
	exit
fi

SDK="$ANDROID_HOME"
V=`ls "$SDK"/platforms/ | tail -n1 | cut -d'-' -f2`
ANDROID_JAR="$SDK"/platforms/android-"$V"/android.jar
if [ ! -f "$ANDROID_JAR" ]; then
	echo "$ANDROID_JAR"" does not exist. Please check the ANDROID_HOME environment variable"
	exit
fi
#echo "$ANDROID_JAR"

KEY=~/.android/debug.keystore
ALIAS=androiddebugkey
PW=android
NAME="${APK%.*}"

JAVA_CLASSPATH="\
libs/soot-trunk.jar:\
libs/baksmali-2.0.2.jar"

# Instrumentation
java -Xss50m -Xmx1500m -jar instrument.jar -apk $APK -lib bladedroid.jar -aj $ANDROID_JAR

#Jimple to dex
java -classpath ${JAVA_CLASSPATH} soot.Main -allow-phantom-refs -src-prec J -ire -f dex -process-dir output/ -d output/

#Dex to smali
baksmali -o output/smali output/classes.dex

# Decompile APK
apktool d "$APK" output/$NAME

# Add permission
sed -i.bak $'s/^.*<application .*/    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" \/>\\\n& /g' output/$NAME/AndroidManifest.xml
rm output/$NAME/AndroidManifest.xml.bak

# Copy modified smali
cp -r output/smali/* output/"$NAME"/smali/

#Build back APK
apktool b output/$NAME output/"$NAME".apk

# Sign APK
jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 -keystore $KEY -storepass $PW output/"$NAME".apk $ALIAS

# Align APK
zipalign -v -f 4 output/$NAME.apk $NAME-aligned.apk

echo "Cleaning Up"
rm -rf output
echo "APK sucessfully instrumented"

