plugins {
    base
}

allprojects {
    group = "net.pistonmaster"
    version = "5.2.0"
    description = "Best MOTD plugin with multi-platform support!"
}

tasks.create("outputVersion") {
    doLast {
        println(project.version)
    }
}
