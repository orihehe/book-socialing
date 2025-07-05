plugins {
    kotlin("jvm") version "1.9.25" apply false
    kotlin("plugin.spring") version "1.9.25" apply false

    id("org.springframework.boot") version "3.4.4" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
    id("org.jlleitschuh.gradle.ktlint") version "12.1.0"
}

allprojects {
    repositories {
        mavenCentral()
    }
}

tasks.register<Copy>("installGitHooks") {
    from("hooks")
    into(".git/hooks")
    fileMode = 0b111101101
}
