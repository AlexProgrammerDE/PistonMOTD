plugins {
    java
    id("net.kyori.blossom")
}

java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8

sourceSets {
    main {
        blossom {
            javaSources {
                property("version", project.version.toString())
                property("description", rootProject.description)
                property("url", "https://pistonmaster.net/PistonMOTD")
            }
        }
    }
}
