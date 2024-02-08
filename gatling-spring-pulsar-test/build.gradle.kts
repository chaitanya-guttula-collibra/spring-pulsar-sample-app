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

//dependencies {
//    gatling("org.springframework.boot:spring-boot-starter-pulsar")
//    gatling("org.springframework.boot:spring-boot-starter-test")
//}

tasks.withType<Test> {
    useJUnitPlatform()
}


gatling {
    logHttp = io.gatling.gradle.LogHttp.FAILURES
}