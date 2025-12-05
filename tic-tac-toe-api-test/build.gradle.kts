plugins {
    id("java")
    id("io.qameta.allure") version "2.12.0"
}

group = "com.tictactoe.test"
version = "1.0.0"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("com.staf:module-taf-api:1.0-SNAPSHOT")
    implementation("com.staf:module-taf-apache-http:1.0-SNAPSHOT")
    implementation("com.staf:module-taf-retrofit:1.0-SNAPSHOT")
    implementation("com.staf:module-taf-restassured:1.0-SNAPSHOT")

    implementation("org.slf4j:slf4j-api:2.0.16")
    implementation("org.apache.logging.log4j:log4j-api:3.0.0-beta2")
    testImplementation("org.apache.logging.log4j:log4j-slf4j2-impl:3.0.0-beta2")

    testImplementation("org.testng:testng:7.11.0")
    testImplementation("io.qameta.allure:allure-testng:2.29.1")
    testImplementation("org.assertj:assertj-core:3.27.3")
    testImplementation("net.javacrumbs.json-unit:json-unit:4.1.0")
    testImplementation("net.javacrumbs.json-unit:json-unit-assertj:4.1.0")

    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")
    testCompileOnly("org.projectlombok:lombok:1.18.36")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.36")
}

tasks.test {
    enabled = false
}

tasks.withType<Test> {
    useTestNG()
}

tasks.register<Test>("executeTicTacToeTest") {
    doFirst {
        System.setProperty("test.suite", "tic-tac-toe-api-test/src/test/resources/suite/testng-tictactoe.xml")
    }
    finalizedBy("allureReport")
}
