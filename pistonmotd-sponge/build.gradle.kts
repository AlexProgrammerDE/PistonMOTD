plugins {
    id("pm.java-conventions")
}

dependencies {
    implementation(projects.pistonmotdApi)
    implementation(projects.pistonmotdUtils)

    implementation("net.kyori:adventure-platform-spongeapi:4.0.1")
    implementation("org.bstats:bstats-sponge:2.2.1")

    compileOnly("org.spongepowered:spongeapi:7.4.0")
    annotationProcessor("org.spongepowered:spongeapi:7.4.0")
}
