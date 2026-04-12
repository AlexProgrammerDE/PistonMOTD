plugins {
    id("pm.platform-conventions")
    id("pm.shadow-conventions")
    id("xyz.jpenilla.run-velocity") version "3.0.2"
}

dependencies {
    implementation(project(path = ":pistonmotd-api", configuration = "shadowRuntimeElements"))
    implementation(projects.pistonmotdShared)
    compileOnly(projects.pistonmotdBuildData)

    implementation("org.bstats:bstats-velocity:3.2.1")

    compileOnly("com.velocitypowered:velocity-api:3.4.0")
}

tasks {
    runVelocity {
        version("3.4.0-SNAPSHOT")
    }
}
