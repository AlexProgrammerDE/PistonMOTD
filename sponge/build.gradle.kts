import org.spongepowered.gradle.plugin.config.PluginLoaders
import org.spongepowered.plugin.metadata.model.PluginDependency

plugins {
    id("pm.shadow-conventions")
    id("org.spongepowered.gradle.plugin") version "2.0.1"
}

dependencies {
    implementation(projects.pistonmotdApi)
    implementation(projects.pistonmotdShared)
    implementation(projects.kyori.normal)
    compileOnly(projects.pistonmotdBuildData)

    implementation("org.bstats:bstats-sponge:2.2.1")
}

sponge {
    injectRepositories(false)
    apiVersion("8.0.0-SNAPSHOT")
    license("../LICENSE")
    loader {
        name(PluginLoaders.JAVA_PLAIN)
        version("1.0")
    }
    plugin("pistonmotd") {
        displayName("PistonMOTD")
        entrypoint("net.pistonmaster.pistonmotd.sponge.PistonMOTDSponge")
        description(rootProject.description)
        links {
            homepage("https://spongepowered.org")
            source("https://spongepowered.org/source")
            issues("https://spongepowered.org/issues")
        }
        contributor("AlexProgrammerDE") {
            description("Lead Developer")
        }
        dependency("spongeapi") {
            loadOrder.set(PluginDependency.LoadOrder.AFTER)
            optional(false)
        }
    }
}