pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

rootProject.name = "SternalBoard"

include("api", "plugin", "adapt")

// V1_7, V1_8, V1_13, V1_17
arrayOf("v1_7_R3", "v1_8_R3", "v1_13_R1", "v1_17_R1", "v1_19_R3").forEach {
    include("adapt:$it")
}