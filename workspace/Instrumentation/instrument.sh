#!/bin/sh

SDK=/Users/dola/Development/adt-bundle-mac-x86_64-20131030/sdk
V=19
KEY=~/.android/debug.keystore
ALIAS=androiddebugkey
PW=android
NAME="${1%.*}"

# Instrumentation
java -Xss50m -Xmx1500m -jar instrument.jar -apk $1 -lib bladedroid.jar -aj $SDK/platforms/android-$V/android.jar

# Add Permission
apktool d -s output/$1 output/$NAME
sed -i.bak $'s/^.*<application .*/    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" \/>\\\n& /g' output/$NAME/AndroidManifest.xml
rm output/$NAME/AndroidManifest.xml.bak
apktool b output/$NAME output/$1

# Sign APK
jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 -keystore $KEY -storepass $PW output/$1 $ALIAS

# Align APK
zipalign -v -f 4 output/$1 $NAME-aligned.apk

echo "Cleaning Up"
rm -rf output
echo "APK sucessfully instrumented"

