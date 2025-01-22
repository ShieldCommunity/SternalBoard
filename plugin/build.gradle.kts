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
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://plugins.gradle.org/m2/")
    mavenCentral()
}

dependencies {
    api(project(":api"))

    compileOnly(libs.spigot)
    compileOnly(libs.placeholder)

    compileOnly("net.kyori:adventure-text-minimessage:4.14.0")
    compileOnly("net.kyori:adventure-text-serializer-gson:4.17.0")
    compileOnly("net.kyori:adventure-text-serializer-legacy:4.17.0")
    implementation("org.tinylog:tinylog-api-kotlin:2.7.0")

    implementation("org.tinylog:tinylog-impl:2.7.0")
    implementation("com.github.BlueSlimeDev:NMSHandlerAPI:0.5.0")
    implementation("com.alessiodp.libby:libby-bukkit:2.0.0-SNAPSHOT")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testRuntimeOnly(libs.spigot)
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
}

tasks {
    shadowJar {
        relocate("com.alessiodp.libby", "com.xism4.sternalboard.libs")
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
