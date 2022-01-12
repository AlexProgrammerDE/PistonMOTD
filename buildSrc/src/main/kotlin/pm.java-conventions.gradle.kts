plugins {
    `java-library`
    `maven-publish`
}

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots") {
        name = "Sonatype"
    }
    maven("https://papermc.io/repo/repository/maven-public/") {
        name = "PaperMC"
    }
    maven("https://repo.spongepowered.org/maven") {
        name = "SpongePowered"
    }
    maven("https://nexus.velocitypowered.com/repository/maven-public/") {
        name = "VelocityPowered"
    }
    maven("https://repo.codemc.org/repository/maven-public") {
        name = "CodeMC"
    }
    maven("https://jitpack.io") { name = "jitpack.io" }
}

dependencies {
    implementation("commons-io:commons-io:2.11.0")
    implementation("org.apiguardian:apiguardian-api:1.1.2")
    implementation("net.kyori:adventure-api:4.9.3")
    implementation("net.kyori:adventure-text-serializer-legacy:4.9.3")
    implementation("net.kyori:adventure-text-serializer-legacy-text3:4.0.1")
    implementation("net.pistonmaster:PistonUtils:1.3.0")
    compileOnly("net.luckperms:api:5.3")
}

tasks {
    processResources {
        expand("version" to version, "description" to description, "url" to "https://pistonmaster.net/PistonMOTD")
    }
}

java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}
