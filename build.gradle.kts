plugins {
  base
  id("org.openrewrite.rewrite") version "latest.release"
}

allprojects {
  group = "net.pistonmaster"
  version = property("maven_version")!!
  description = "Control your Minecraft server's MOTD. Supports RGB colors and multiple platforms."

  repositories {
    mavenCentral()
    maven("https://central.sonatype.com/repository/maven-snapshots/") {
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
    maven("https://repo.tcoded.com/releases") {
      name = "tcoded-releases"
    }
  }
}

tasks.register("outputVersion") {
  doLast {
    println(project.version)
  }
}

dependencies {
  rewrite(platform("org.openrewrite.recipe:rewrite-recipe-bom:latest.release"))
  rewrite("org.openrewrite.recipe:rewrite-java")
}

rewrite {
  activeRecipe("org.openrewrite.java.ShortenFullyQualifiedTypeReferences")
}
