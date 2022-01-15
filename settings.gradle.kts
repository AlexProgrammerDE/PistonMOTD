enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("VERSION_CATALOGS")

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
    plugins {
        id("com.github.johnrengelman.shadow") version "7.1.1"
        id("org.cadixdev.licenser") version "0.6.1"
        id("net.kyori.indra") version "2.0.6"
        id("net.kyori.indra.git") version "2.0.6"
        id("net.kyori.indra.publishing") version "2.0.6"
        id("net.kyori.blossom") version "1.3.0"
    }
}

rootProject.name = "PistonMOTD"

setOf("build-data", "shared", "api", "bukkit", "bungee", "sponge", "velocity", "universal").forEach { setupPMSubproject(it) }

fun setupPMSubproject(name: String) {
    setupSubproject("pistonmotd-$name") {
        projectDir = file(name)
    }
}

inline fun setupSubproject(name: String, block: ProjectDescriptor.() -> Unit) {
    include(name)
    project(":$name").apply(block)
}