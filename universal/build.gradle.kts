plugins {
    java
    id("xyz.wagyourtail.jvmdowngrader")
}

dependencies {
    implementation(project(":pistonmotd-bukkit", "downgraded"))
    implementation(project(":pistonmotd-bungee", "downgraded"))
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
