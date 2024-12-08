plugins {
    id("pm.shadow-conventions")
    id("xyz.jpenilla.run-velocity") version "2.3.1"
}

dependencies {
    implementation(project(":pistonmotd-api", "shadow"))
    implementation(projects.pistonmotdShared)
    compileOnly(projects.pistonmotdBuildData)

    implementation("org.bstats:bstats-velocity:3.1.0")

    compileOnly("com.velocitypowered:velocity-api:3.1.1")
    annotationProcessor("com.velocitypowered:velocity-api:3.1.1")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks {
    runVelocity {
        version("3.1.1")
    }
}
