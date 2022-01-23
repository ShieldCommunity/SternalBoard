plugins {
    java
    `maven-publish`
}

repositories {
    mavenLocal()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://jitpack.io")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.maven.apache.org/maven2/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.18-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.1")
}

description = "SternalBoard"
java.sourceCompatibility = JavaVersion.VERSION_1_8

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.withType<Jar> {
    archiveBaseName.set("SternalBoard")
}
