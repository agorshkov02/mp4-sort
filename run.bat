@echo off
SET SOURCE_DIR=<Path to source dir>
SET TARGET_DIR=<Path to target dir>
SET JAR_PATH=<Path to jar>
java -jar "%JAR_PATH%" "%SOURCE_DIR%" "%TARGET_DIR%"
