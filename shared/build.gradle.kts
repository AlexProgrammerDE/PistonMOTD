plugins {
    id("pm.shadow-conventions")
}

dependencies {
    api("net.skinsrestorer:axiom:1.1.2-SNAPSHOT")
    implementation(project(":pistonmotd-api", "shadow"))

    compileOnly("com.github.LeonMangler:PremiumVanishAPI:2.7.11")
}
