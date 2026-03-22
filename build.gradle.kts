plugins {
    java
    jacoco
    checkstyle
}

group = "ru.yarsu"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.27.3")
}

tasks.test {
    useJUnitPlatform()
}
checkstyle {
    toolVersion = "10.12.5"
    configFile = rootDir.resolve("config/checkstyle/checks.xml")
    isIgnoreFailures = false
    maxWarnings = 0
}

// Чтобы checkstyle проверял и тесты
tasks.named("checkstyleTest") {
    dependsOn("compileTestJava")
}
