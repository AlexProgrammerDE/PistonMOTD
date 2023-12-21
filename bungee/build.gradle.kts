plugins {
    id("pm.kyori-conventions")
}

dependencies {
    implementation(projects.pistonmotdApi)
    implementation(projects.pistonmotdShared)
    implementation(projects.pistonmotdKyoriRelocated)

    implementation("org.bstats:bstats-bungeecord:3.0.2")
    implementation("net.kyori:adventure-platform-bungeecord:4.3.2")

    compileOnly("net.md-5:bungeecord-api:1.20-R0.1")
}
