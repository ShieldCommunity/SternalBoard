import org.gradle.kotlin.dsl.maven

plugins {
    `java-library`
}

repositories {
    mavenLocal()
    maven("https://papermc.io/repo/repository/maven-public/")
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}