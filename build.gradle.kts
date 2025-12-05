plugins {
    java
    id("io.freefair.lombok") version "8.4"
}

group = "com.staf"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    // STAF Framework Dependencies
    implementation("com.staf:module-taf-api:1.0-SNAPSHOT")
    implementation("com.staf:module-taf-apache-http:1.0-SNAPSHOT")
    implementation("com.staf:module-taf-retrofit:1.0-SNAPSHOT")
    implementation("com.staf:module-taf-restassured:1.0-SNAPSHOT")

    // Lombok
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    testCompileOnly("org.projectlombok:lombok:1.18.30")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.30")

    // TestNG
    testImplementation("org.testng:testng:7.8.0")

    // JSON Processing
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.3")

    // Logging
    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("ch.qos.logback:logback-classic:1.4.11")

    // AssertJ for fluent assertions
    testImplementation("org.assertj:assertj-core:3.24.2")

    // Apache Commons
    implementation("org.apache.commons:commons-lang3:3.13.0")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.test {
    useTestNG {
        suites("src/test/resources/testng.xml")
    }
    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true
    }
}
