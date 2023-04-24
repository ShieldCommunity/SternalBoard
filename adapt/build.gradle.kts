plugins {
    id("project.common-conventions")
}

subprojects {
    apply(plugin = "project.common-conventions")

    repositories {
        maven("https://repo.codemc.org/repository/nms/")
    }

    dependencies {
        implementation(project(":api"))

        testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
    }
}