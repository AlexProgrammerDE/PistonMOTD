plugins {
    id("pm.kyori-conventions")
}

dependencies {
    implementation(projects.pistonmotdApi)
    implementation(projects.pistonmotdShared)
    implementation(projects.pistonmotdKyori)

    implementation("io.papermc:paperlib:1.0.8")
    implementation("org.bstats:bstats-bukkit:3.1.0")
    implementation("net.kyori:adventure-platform-bukkit:4.3.4")

    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
}
