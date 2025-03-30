plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation("com.gradleup.shadow:shadow-gradle-plugin:9.0.0-beta11")
    implementation("xyz.wagyourtail.jvmdowngrader:xyz.wagyourtail.jvmdowngrader.gradle.plugin:1.2.2")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

kotlin {
    jvmToolchain(17)
}
