pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.msrandom.net/repository/cloche")
        maven(url = "https://maven.teamresourceful.com/repository/maven-public/")
        maven("https://maven.architectury.dev/")
    }
}

rootProject.name = "autoquest-modern"
include("src:common")
findProject(":src:common")?.name = "common"
include("src:fabric")
findProject(":src:fabric")?.name = "fabric"
