plugins {
    id("project.common-conventions")
    alias(libs.plugins.shadow)
    alias(libs.plugins.zapper)
}

repositories {
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://jitpack.io/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.triumphteam.dev/snapshots/")
    mavenCentral()
}

dependencies {
    api(project(":api"))

    compileOnly(libs.placeholder)
    compileOnly(libs.paper)
    compileOnly(libs.tinylog)

    zap(libs.minimessage)
    zap(libs.gsonAdventure)
    zap(libs.legacySerializer)
    zap(libs.inject)
    zap(libs.tinylogImpl)

    implementation(libs.cmd) //only provides snapshot build

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
}

zapper {
    libsFolder = "libraries"

    relocationPrefix = "com.xism4.sternalboard.libs"

    repositories {
        maven("https://jitpack.io/")
        maven("https://oss.sonatype.org/content/groups/public/")
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
        maven("https://repo.alessiodp.com/releases/")
        maven("https://plugins.gradle.org/m2/")
        includeProjectRepositories()
    }

    relocate("net.elytrium.serializer", "serializer")
    relocate("team.unnamed", "injector")
    relocate("net.kyori", "adventure")
    relocate("org.tinylog", "tinylog")
}

tasks {
    shadowJar {
        archiveBaseName.set("SternalBoard")
        relocate("dev.triumphteam", "com.xism4.sternalboard.libs.commands") //same as up
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
