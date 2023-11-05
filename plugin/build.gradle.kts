plugins {
    id("project.common-conventions")
    alias(libs.plugins.shadow)
}

repositories {
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    maven("https://repo.alessiodp.com/releases/")
    maven("https://nexus.neetgames.com/repository/maven-releases/")
    maven("https://jitpack.io/")
    mavenCentral()
}

dependencies {
    api(project(":api"))

    compileOnly(libs.spigot)
    compileOnly(libs.placeholder)

    compileOnly("net.kyori:adventure-text-minimessage:4.14.0")
    compileOnly("net.kyori:adventure-text-serializer-gson:4.14.0")
    compileOnly("net.kyori:adventure-text-serializer-legacy:4.14.0")
    implementation("com.github.BlueSlimeDev:NMSHandlerAPI:0.4")
    implementation("net.byteflux:libby-bukkit:1.1.5")
    implementation("com.github.xism4:SternalBoard:2.2.7")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly(libs.spigot)
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}

tasks {
    shadowJar {
        relocate("net.byteflux.libby", "com.xism4.sternalboard.libs")
        relocate("net.kyori", "com.xism4.sternalboard.libs.kyori")
        relocate("me.blueslime", "com.xism4.sternalboard.libs.blueslime")
        archiveBaseName.set("SternalBoard")
        destinationDirectory.set(file("$rootDir/bin/"))
        minimize()
    }

    clean {
        delete("${rootDir}/bin/")
    }
}

tasks.processResources {
    filesMatching("plugin.yml") {
        expand("version" to (parent?.version ?: project.version))
    }
}