enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
    plugins {
        id("com.gradleup.shadow") version "9.1.0"
        id("net.kyori.indra") version "3.2.0"
        id("net.kyori.indra.git") version "3.2.0"
        id("net.kyori.indra.publishing") version "3.2.0"
        id("net.kyori.blossom") version "2.1.0"
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
