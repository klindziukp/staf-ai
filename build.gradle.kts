plugins {
    java
    id("io.freefair.lombok") version "8.4"
}

group = "com.klindziuk.staf"
version = "1.0.0"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    // STAF Framework
    implementation("com.github.klindziukp:staf-core:1.0.0")
    
    // Lombok
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    testCompileOnly("org.projectlombok:lombok:1.18.30")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.30")
    
    // Apache HTTP Client
    implementation("org.apache.httpcomponents.client5:httpclient5:5.3")
    implementation("org.apache.httpcomponents.core5:httpcore5:5.2.4")
    
    // JSON Processing
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.0")
    
    // TestNG
    testImplementation("org.testng:testng:7.8.0")
    
    // AssertJ
    testImplementation("org.assertj:assertj-core:3.24.2")
    
    // Logging
    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("ch.qos.logback:logback-classic:1.4.14")
    
    // Allure Reporting
    testImplementation("io.qameta.allure:allure-testng:2.25.0")
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

tasks.register<Test>("apiTest") {
    useTestNG {
        suites("src/test/resources/testng.xml")
    }
    
    systemProperty("env", System.getProperty("env", "dev"))
    
    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true
    }
}
