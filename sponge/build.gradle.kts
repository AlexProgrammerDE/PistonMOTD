plugins {
    id("pm.kyori-conventions")
}

dependencies {
    implementation(projects.pistonmotdApi)
    implementation(projects.pistonmotdShared)
    implementation(projects.kyori.relocated)
    compileOnly(projects.pistonmotdBuildData)

    implementation("org.bstats:bstats-sponge:2.2.1")
    implementation("net.kyori:adventure-platform-spongeapi:4.0.1")

    compileOnly("org.spongepowered:spongeapi:8.0.0")
    annotationProcessor("org.spongepowered:spongeapi:8.0.0")
}
