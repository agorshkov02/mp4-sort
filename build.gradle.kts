dependencies {
    // https://mvnrepository.com/artifact/com.drewnoakes/metadata-extractor
    implementation("com.drewnoakes:metadata-extractor:2.19.0")
}

group = "com.github.agorshkov02.mp4sort"
version = "1.0"

kotlin {
    jvmToolchain(17)
}

plugins {
    kotlin("jvm") version "2.0.0"
}

repositories {
    mavenCentral()
}

tasks.test {
    useJUnitPlatform()
}
