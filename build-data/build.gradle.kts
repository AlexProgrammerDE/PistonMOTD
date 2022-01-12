plugins {
    java
    id("net.kyori.blossom")
}

java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8

blossom {
    replaceToken("{version}", version)
    replaceToken("{description}", rootProject.description)
    replaceToken("{url}", "https://pistonmaster.net/PistonMOTD")
}