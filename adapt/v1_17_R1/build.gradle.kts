plugins {
    id("io.papermc.paperweight.userdev") version ("1.5.4")
}

dependencies {
    paperweight.paperDevBundle("1.17.1-R0.1-SNAPSHOT")
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }
}