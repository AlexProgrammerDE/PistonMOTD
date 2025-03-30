plugins {
    id("pm.java-conventions")
    id("pm.shadow-conventions")
}

dependencies {
    api("net.skinsrestorer:axiom:1.1.2-SNAPSHOT")
    api("net.lenni0451.mcstructs:text:3.0.0")
    implementation(project(":pistonmotd-api", "shadow"))

    compileOnly("com.github.LeonMangler:PremiumVanishAPI:2.9.18-2")

    implementation("net.pistonmaster:PistonUtils:1.4.0")

    compileOnly("net.luckperms:api:5.4")
}
