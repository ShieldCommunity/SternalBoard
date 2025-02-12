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
    maven("https://repo.triumphteam.dev/snapshots/")
    mavenCentral()
}

dependencies {
    api(project(":api"))
    compileOnly(libs.placeholder)
    compileOnly(libs.paper)

    compileOnly(libs.minimessage)
    compileOnly(libs.gsonAdventure)
    compileOnly(libs.legacySerializer)
    compileOnly(libs.tinylog)
    implementation(libs.tinylogImpl)

    implementation("com.alessiodp.libby:libby-bukkit:2.0.0-SNAPSHOT")
    implementation(libs.cmd)
    implementation(libs.inject)

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
}

tasks {
    shadowJar {
        relocate("com.alessiodp.libby", "com.xism4.sternalboard.libs")
        relocate("net.kyori", "com.xism4.sternalboard.libs.kyori")
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
