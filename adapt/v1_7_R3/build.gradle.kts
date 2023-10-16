dependencies {
    compileOnly("org.bukkit:craftbukkit:1.7.10-R0.1-SNAPSHOT")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}