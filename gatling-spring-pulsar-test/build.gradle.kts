plugins {
    java
    id("io.gatling.gradle") version "3.10.3.1"
    id("io.freefair.lombok") version "8.4"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

tasks.withType<Test> {
    useJUnitPlatform()
}


gatling {
    logHttp = io.gatling.gradle.LogHttp.FAILURES
    systemProperties.set("repeatCount", project.properties.getOrDefault("repeatCount", "100"))
    systemProperties.set("userCount", project.properties.getOrDefault("userCount", "1000"))
    systemProperties.set("rampUpDuration", project.properties.getOrDefault("rampUpDuration", "30"))
    systemProperties.set("baseUrl", project.properties.getOrDefault("baseUrl", "http://localhost:7070"))
}