plugins {
    id("pm.kyori-conventions")
}

dependencies {
    implementation(projects.pistonmotdApi)
    implementation(projects.pistonmotdShared)
    implementation(projects.pistonmotdKyori)

    implementation("org.bstats:bstats-bungeecord:3.0.2")
    implementation("net.kyori:adventure-platform-bungeecord:4.3.4")

    compileOnly("net.md-5:bungeecord-api:1.20-R0.2")
}
