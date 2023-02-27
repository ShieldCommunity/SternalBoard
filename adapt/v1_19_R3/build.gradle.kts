plugins {
    alias(libs.plugins.shadow)
}

dependencies {
    implementation(project(":api"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly(libs.spigot)
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
}