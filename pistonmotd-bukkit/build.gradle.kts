plugins {
    id("pm.java-conventions")
}

dependencies {
    implementation(projects.pistonmotdApi)
    implementation(projects.pistonmotdUtils)

    implementation("io.papermc:paperlib:1.0.7")
    implementation("org.bstats:bstats-bukkit:2.2.1")
    implementation("net.kyori:adventure-platform-bukkit:4.0.1")

    compileOnly("com.destroystokyo.paper:paper-api:1.16.4-R0.1-SNAPSHOT")
}
