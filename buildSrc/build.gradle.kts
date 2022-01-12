plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation("gradle.plugin.com.github.johnrengelman:shadow:7.1.2")
    implementation("net.kyori:indra-common:2.0.6")
}

java.targetCompatibility = JavaVersion.VERSION_1_8