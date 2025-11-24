#!/bin/sh

echo "Initializing Gradle Wrapper..."

if command -v gradle >/dev/null 2>&1; then
    echo "Using local gradle installation to generate wrapper..."
    gradle wrapper --gradle-version 8.13
    chmod +x gradlew
    echo "Gradle wrapper initialized successfully!"
    echo "You can now use: ./gradlew clean build"
else
    echo "Gradle not found. Downloading wrapper jar manually..."
    mkdir -p gradle/wrapper

    WRAPPER_JAR="gradle/wrapper/gradle-wrapper.jar"
    WRAPPER_URL="https://raw.githubusercontent.com/gradle/gradle/v8.13.0/gradle/wrapper/gradle-wrapper.jar"

    if command -v curl >/dev/null 2>&1; then
        curl -L -o "$WRAPPER_JAR" "$WRAPPER_URL"
    elif command -v wget >/dev/null 2>&1; then
        wget -O "$WRAPPER_JAR" "$WRAPPER_URL"
    else
        echo "Error: Neither gradle, curl, nor wget found."
        echo "Please install gradle or download the wrapper manually."
        exit 1
    fi

    chmod +x gradlew
    echo "Gradle wrapper jar downloaded!"
    echo "You can now use: ./gradlew clean build"
fi

