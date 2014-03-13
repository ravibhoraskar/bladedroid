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
OUT="$APK"".d"
#echo "$ANDROID_JAR"

KEY=~/.android/debug.keystore
ALIAS=androiddebugkey
PW=android
NAME="${APK%.*}"

JAVA_CLASSPATH="\
libs/soot-trunk.jar:\
libs/baksmali-2.0.2.jar"

# Instrumentation
java -Xss50m -Xmx1500m -jar instrument.jar -apk $APK -lib bladedroid.jar -aj $ANDROID_JAR -d "$OUT"

#Jimple to dex
java -classpath ${JAVA_CLASSPATH} soot.Main -allow-phantom-refs -src-prec J -ire -f dex -process-dir "$OUT" -d "$OUT"/

#Dex to smali
baksmali -o "$OUT"/smali "$OUT"/classes.dex

# Decompile APK
apktool d "$APK" "$OUT"/$NAME

# Add permission
sed -i.bak $'s/^.*<application .*/    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" \/>\\\n& /g' "$OUT"/$NAME/AndroidManifest.xml
rm "$OUT"/$NAME/AndroidManifest.xml.bak

# Copy modified smali
cp -r "$OUT"/smali/* "$OUT"/"$NAME"/smali/

#Build back APK
apktool b "$OUT"/$NAME "$OUT"/"$NAME".apk

# Sign APK
jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 -keystore $KEY -storepass $PW "$OUT"/"$NAME".apk $ALIAS

# Align APK
zipalign -v -f 4 "$OUT"/$NAME.apk $NAME-aligned.apk

echo "Cleaning Up"
#rm -rf "$OUT"
echo "APK sucessfully instrumented"

