plugins {
    id("pm.shadow-conventions")
}

dependencies {
    api("net.skinsrestorer:axiom:1.1.2-SNAPSHOT")
    api("net.lenni0451.mcstructs:text:2.5.3")
    implementation(project(":pistonmotd-api", "shadow"))

    compileOnly("com.github.LeonMangler:PremiumVanishAPI:2.8.8")
}
