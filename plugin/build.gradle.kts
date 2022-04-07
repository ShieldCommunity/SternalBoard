plugins {
    id("project.common-conventions")
    alias(libs.plugins.shadow)
}

repositories {
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    api(project(":api"))
    compileOnly(libs.spigot)
    compileOnly(libs.placeholder)
}