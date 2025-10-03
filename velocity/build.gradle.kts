plugins {
    id("pm.platform-conventions")
    id("pm.shadow-conventions")
    id("xyz.jpenilla.run-velocity") version "3.0.1"
}

dependencies {
    implementation(project(":pistonmotd-api", "shadow"))
    implementation(projects.pistonmotdShared)
    compileOnly(projects.pistonmotdBuildData)

    implementation("org.bstats:bstats-velocity:3.1.0")

    compileOnly("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
}

tasks {
    runVelocity {
        version("3.4.0-SNAPSHOT")
    }
}
