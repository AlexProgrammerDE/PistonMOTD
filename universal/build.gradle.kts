import io.papermc.hangarpublishplugin.model.Platforms

plugins {
    java
    id("xyz.wagyourtail.jvmdowngrader")
    id("org.spongepowered.gradle.ore") version "2.3.0"
    id("io.papermc.hangar-publish-plugin") version "0.1.3"
}

dependencies {
    implementation(project(":pistonmotd-bukkit", "downgraded"))
    implementation(project(":pistonmotd-bungee", "downgraded"))
    implementation(project(":pistonmotd-sponge", "downgraded"))
    implementation(project(":pistonmotd-velocity", "downgraded"))
}

tasks {
    jar {
        archiveClassifier = "only-merged"

        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        dependsOn(configurations.runtimeClasspath)
        from({ configurations.runtimeClasspath.get().map { zipTree(it) } })
    }
    shadeDowngradedApi {
        dependsOn(jar)

        inputFile = jar.get().archiveFile
        downgradeTo = JavaVersion.VERSION_1_8

        archiveBaseName = "PistonMOTD"
        archiveClassifier = null
        destinationDirectory = rootProject.projectDir.resolve("build/libs")

        shadePath = { _ -> "net/pistonmaster/pistonmotd/shadow/jvmdowngrader" }
    }
    build {
        dependsOn(shadeDowngradedApi)
    }
}

oreDeployment {
    oreEndpoint("https://ore.spongepowered.org/")
    apiKey().set(providers.environmentVariable("ORE_TOKEN"))

    defaultPublication {
        projectId.set("pistonmotd")
        createForumPost.set(true)
        versionBody.set(providers.environmentVariable("ORE_CHANGELOG"))
        channel.set("Release")
        publishArtifacts.from(tasks.shadeDowngradedApi.map { it.outputs })
    }
}

hangarPublish {
    publications.register("plugin") {
        version.set(project.version.toString())
        channel.set("Release")
        id.set("PistonMOTD")
        apiKey.set(providers.environmentVariable("HANGAR_TOKEN"))
        changelog.set(providers.environmentVariable("HANGAR_CHANGELOG"))
        platforms {
            register(Platforms.PAPER) {
                jar.set(tasks.shadeDowngradedApi.flatMap { it.archiveFile })
                val versions: List<String> = providers.gradleProperty("paperVersion").get()
                    .split(",")
                    .map { it.trim() }
                platformVersions.set(versions)
            }
            register(Platforms.VELOCITY) {
                jar.set(tasks.shadeDowngradedApi.flatMap { it.archiveFile })
                val versions: List<String> = providers.gradleProperty("velocityVersion").get()
                    .split(",")
                    .map { it.trim() }
                platformVersions.set(versions)
            }
            register(Platforms.WATERFALL) {
                jar.set(tasks.shadeDowngradedApi.flatMap { it.archiveFile })
                val versions: List<String> = providers.gradleProperty("waterfallVersion").get()
                    .split(",")
                    .map { it.trim() }
                platformVersions.set(versions)
            }
        }
    }
}
