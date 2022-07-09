plugins {
    base
}

allprojects {
    group = "net.pistonmaster"
    version = "5.1.0-SNAPSHOT"
    description = "Best MOTD plugin with multi-platform support!"
}

tasks.create("outputVersion") {
    doLast {
        println(project.version)
    }
}

val platforms = setOf(
    projects.pistonmotdBukkit,
    projects.pistonmotdBungee,
    projects.pistonmotdSponge,
    projects.pistonmotdVelocity
).map { it.dependencyProject }

val special = setOf(
    projects.pistonmotdUniversal,
    projects.pistonmotdApi,
    projects.pistonmotdShared
).map { it.dependencyProject }

subprojects {
    when (this) {
        in platforms -> plugins.apply("pm.platform-conventions")
        in special -> plugins.apply("pm.java-conventions")
    }
}
