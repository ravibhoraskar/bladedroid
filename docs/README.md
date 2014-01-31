# BladeDroid

To convert eclipse-exported jar file to dalvik, which can be loaded.

    <sdk-dir>/build-tools/<version>/dx --dex --output=output.jar input.jar

Compile the instrumentation code using ant. Run it as follows:

    java -jar instrument.jar -apk <some-apk-file> -lib bladedroid.jar -aj <sdk-dir>/platforms/android-<num>/android.jar

Here bladedroid.jar contains classes for BladeDroid.java, BladeLoader.java etc
