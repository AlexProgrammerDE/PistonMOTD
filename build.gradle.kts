plugins {
    base
}

allprojects {
    group = "net.pistonmaster"
    version = property("maven_version")!!
    description = "Best MOTD plugin with multi-platform support!"
}

tasks.register("outputVersion") {
    doLast {
        println(project.version)
    }
}
