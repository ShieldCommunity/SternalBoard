plugins {
    id("project.common-conventions")
    alias(libs.plugins.shadow)
}

repositories {
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.unnamed.team/repository/unnamed-public/")
    maven("https://repo.triumphteam.dev/snapshots/")
    mavenCentral()
}

dependencies {
    compileOnly(project(":api"))
    arrayOf("v1_7_R3", "v1_8_R3", "v1_13_R1", "v1_17_R1", "v1_19_R3").forEach {
        implementation(project(":adapt:$it"))
    }

    implementation(libs.inject)
    implementation(libs.command)

    compileOnly(libs.spigot)
    compileOnly(libs.placeholder)

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly(libs.spigot)
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
}

tasks {
    shadowJar {
        archiveBaseName.set("SternalBoard")
        destinationDirectory.set(file("$rootDir/bin/"))
        minimize()

        arrayOf(
            "team.unnamed.inject",
            "dev.triumphteam.cmd",
            "javax.inject"
        ).forEach {
            relocate(it, "${rootProject.group}.sternalboard.libs.$it")
        }
    }

    clean {
        delete("${rootDir}/bin/")
    }
}