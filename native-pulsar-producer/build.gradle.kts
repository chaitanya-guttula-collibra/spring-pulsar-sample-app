plugins {
    java
    id("org.springframework.boot") version "3.2.2"
    id("io.spring.dependency-management") version "1.1.4"
    id("io.freefair.lombok") version "8.4"
}

group = "com.example"
version = "0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.avro:avro:1.11.3")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.apache.pulsar:pulsar-client-original:3.1.2") {//    api(libs.pulsar.client) {}
        exclude(
                group = "org.apache.avro",
                module = "avro"
        ) // exclude avro from pulsar client, as version 1.10.2 has vulnerability
    }
}

tasks.bootRun {
    jvmArgs("--add-opens",
            "java.base/sun.net=ALL-UNNAMED"
    )
}