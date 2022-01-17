plugins {
    id("net.kyori.indra")
    id("net.kyori.indra.publishing")
    id("pm.kyori-conventions")
}

dependencies {
    implementation(projects.pistonmotdShared)
    implementation(projects.kyori.relocated)

    implementation("net.kyori:adventure-text-minimessage:4.0.0-SNAPSHOT")
}

indra {
    github("AlexProgrammerDE", "PistonMOTD") {
        ci(true)
    }

    gpl3OnlyLicense()
    publishReleasesTo("codemc-releases", "https://repo.codemc.org/repository/maven-releases/")
    publishSnapshotsTo("codemc-snapshots", "https://repo.codemc.org/repository/maven-snapshots/")

    configurePublications {
        pom {
            name.set("PistonMOTD")
            url.set("https://pistonmaster.net/PistonMOTD")
            organization {
                name.set("AlexProgrammerDE")
                url.set("https://pistonmaster.net")
            }
            developers {
                developer {
                    id.set("AlexProgrammerDE")
                    timezone.set("Europe/Berlin")
                    url.set("https://pistonmaster.net")
                }
            }
        }

        versionMapping {
            usage(Usage.JAVA_API) { fromResolutionOf(JavaPlugin.RUNTIME_CLASSPATH_CONFIGURATION_NAME) }
            usage(Usage.JAVA_RUNTIME) { fromResolutionResult() }
        }
    }
}

val repoName = if (version.toString().endsWith("SNAPSHOT")) "maven-snapshots" else "maven-releases"
publishing {
    repositories {
        maven("https://repo.codemc.org/repository/${repoName}/") {
            credentials.username = System.getenv("CODEMC_USERNAME")
            credentials.password = System.getenv("CODEMC_PASSWORD")
            name = "codemc"
        }
    }
}