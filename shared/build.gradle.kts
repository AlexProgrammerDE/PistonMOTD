plugins {
    id("pm.shadow-conventions")
}

dependencies {
    api("net.skinsrestorer:axiom:1.0.0-SNAPSHOT")
    implementation(projects.pistonmotdApi)
}