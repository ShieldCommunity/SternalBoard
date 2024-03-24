plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}