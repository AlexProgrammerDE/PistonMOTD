plugins {
    `java-library`
    `maven-publish`
    id("net.kyori.indra")
    id("net.kyori.indra.publishing")
}

dependencies {
    implementation("commons-io:commons-io:2.11.0")
    compileOnlyApi("org.apiguardian:apiguardian-api:1.1.2")
    implementation("net.pistonmaster:PistonUtils:1.3.2")

    compileOnly("net.luckperms:api:5.4")

    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")
}

tasks {
    processResources {
        expand("version" to version, "description" to description, "url" to "https://pistonmaster.net/PistonMOTD")
    }
}

java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-Xlint:all,-serial,-processing")
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
