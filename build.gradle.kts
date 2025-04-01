plugins {
    base
}

allprojects {
    group = "net.pistonmaster"
    version = property("maven_version")!!
    description = "Best MOTD plugin with multi-platform support!"

    repositories {
        mavenCentral()
        maven("https://oss.sonatype.org/content/repositories/snapshots") {
            name = "Sonatype"
        }
        maven("https://repo.papermc.io/repository/maven-public/") {
            name = "PaperMC"
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

tasks.register("outputVersion") {
    doLast {
        println(project.version)
    }
}
