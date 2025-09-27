plugins {
    id("pm.platform-conventions")
    id("pm.kyori-conventions")
}

dependencies {
    implementation(projects.pistonmotdApi)
    implementation(projects.pistonmotdShared)
    implementation(projects.pistonmotdKyori)

    implementation("org.bstats:bstats-bungeecord:3.1.0")
    implementation("net.kyori:adventure-platform-bungeecord:4.4.1")

    compileOnly("net.md-5:bungeecord-api:1.21-R0.4")
}
