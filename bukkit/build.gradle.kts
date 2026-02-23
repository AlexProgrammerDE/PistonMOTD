plugins {
    id("pm.platform-conventions")
    id("pm.kyori-conventions")
    id("xyz.jpenilla.run-paper") version "3.0.2"
}

dependencies {
    implementation(projects.pistonmotdApi)
    implementation(projects.pistonmotdShared)
    implementation(projects.pistonmotdKyori)

    implementation("io.papermc:paperlib:1.0.8")
    implementation("org.bstats:bstats-bukkit:3.2.0")
    implementation("net.kyori:adventure-platform-bukkit:4.4.1")
    implementation("com.tcoded:FoliaLib:0.5.1")

    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    compileOnly("jakarta.annotation:jakarta.annotation-api:3.0.0")
}

tasks {
  runServer {
    minecraftVersion("1.21.10")

    jvmArgs = listOf("-Dcom.mojang.eula.agree=true")
  }
}

runPaper {
  folia {
    registerTask {
      minecraftVersion("1.21.10")

      jvmArgs = listOf("-Dcom.mojang.eula.agree=true")
      args = listOf("--nogui")
    }
  }
}
