enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("VERSION_CATALOGS")

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
    plugins {
        id("com.github.johnrengelman.shadow") version "7.1.2"
        id("org.cadixdev.licenser") version "0.6.1"
        id("net.kyori.indra") version "2.1.1"
        id("net.kyori.indra.git") version "2.1.1"
        id("net.kyori.indra.publishing") version "2.1.1"
        id("net.kyori.blossom") version "1.3.0"
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
        maven("https://oss.sonatype.org/content/repositories/snapshots") {
            name = "Sonatype"
        }
        maven("https://papermc.io/repo/repository/maven-public/") {
            name = "PaperMC"
        }
        maven("https://repo.spongepowered.org/maven") {
            name = "SpongePowered"
        }
        maven("https://nexus.velocitypowered.com/repository/maven-public/") {
            name = "VelocityPowered"
        }
        maven("https://repo.codemc.org/repository/maven-public") {
            name = "CodeMC"
        }
        maven("https://jitpack.io") {
            name = "jitpack.io"
        }
    }
}

rootProject.name = "PistonMOTD"

setOf(
    "normal",
    "relocated"
).forEach {
    setupSubproject("pistonmotd-kyori-$it") {
        projectDir = file("kyori").resolve(it)
    }
}

setOf(
    "build-data",
    "shared",
    "api",
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
