enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
    plugins {
        id("com.gradleup.shadow") version "9.3.1"
        id("net.kyori.indra") version "4.0.0"
        id("net.kyori.indra.git") version "4.0.0"
        id("net.kyori.indra.publishing") version "4.0.0"
        id("net.kyori.blossom") version "2.2.0"
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "PistonMOTD"

setOf(
    "build-data",
    "kyori",
    "api",
    "shared",
    "bukkit",
    "bungee",
    "sponge",
    "velocity",
    "universal"
).forEach { setupPMSubproject(it) }

fun setupPMSubproject(name: String) {
    setupSubproject("pistonmotd-$name") {
        projectDir = file(name)
    }
}

inline fun setupSubproject(name: String, block: ProjectDescriptor.() -> Unit) {
    include(name)
    project(":$name").apply(block)
}
