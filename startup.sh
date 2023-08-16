#!/bin/sh

exec java -Xss512k \
-XX:MaxRAMPercentage=60.0 \
-XX:+UnlockDiagnosticVMOptions \
-XX:+ExitOnOutOfMemoryError \
-XX:+PrintFlagsFinal \
-jar app.jar \
--spring.profiles.active=docker
