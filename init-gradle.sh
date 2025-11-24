#!/bin/sh

set -e

WRAPPER_JAR="gradle/wrapper/gradle-wrapper.jar"
WRAPPER_URL="https://raw.githubusercontent.com/gradle/gradle/v8.13.0/gradle/wrapper/gradle-wrapper.jar"

if [ ! -f "$WRAPPER_JAR" ]; then
    echo "Downloading Gradle Wrapper JAR..."
    mkdir -p gradle/wrapper

    if command -v curl > /dev/null; then
        curl -L -o "$WRAPPER_JAR" "$WRAPPER_URL"
    elif command -v wget > /dev/null; then
        wget -O "$WRAPPER_JAR" "$WRAPPER_URL"
    else
        echo "Error: curl or wget is required to download the Gradle Wrapper"
        exit 1
    fi

    echo "Gradle Wrapper downloaded successfully!"
fi

chmod +x gradlew
echo "Running: ./gradlew $@"
./gradlew "$@"

