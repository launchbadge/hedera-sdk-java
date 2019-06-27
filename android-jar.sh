#!/usr/bin/env sh

BUILD_CP=$(mvn dependency:build-classpath -DincludeScope=runtime -Dmdep.pathSeparator=' --lib ' | grep -v '^\[')
echo classpath: $BUILD_CP
java -jar ~/r8/build/libs/d8.jar --output target/sdk-$1-android.jar --lib ~/Android/Sdk/platforms/android-29/android.jar target/sdk-$1.jar --lib $BUILD_CP
