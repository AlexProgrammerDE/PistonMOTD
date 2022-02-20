plugins {
    id("pm.shadow-conventions")
}

dependencies {
    api("net.skinsrestorer:axiom:1.0.0-SNAPSHOT")
    implementation(project(":pistonmotd-api", "shadow"))

    compileOnly("com.github.LeonMangler:PremiumVanishAPI:2.7.11")
}