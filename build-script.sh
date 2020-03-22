./gradlew -b build-travis.gradle build --warning-mode all --scan
./gradlew -b build-travis.gradle jar --warning-mode all --scan
./gradlew -b build-travis.gradle jre --warning-mode all --scan
./gradlew -b build-travis.gradle runtimeZip --warning-mode all --scan