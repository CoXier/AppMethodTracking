#!/bin/bash
echo "Start testing"

cd method-tracking
echo "Into method-tracking folder"
ls

./gradlew  --stacktrace test
