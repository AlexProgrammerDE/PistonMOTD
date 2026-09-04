plugins {
    id("pm.platform-conventions")
    id("pm.shadow-conventions")
    id("xyz.jpenilla.run-velocity") version "3.1.0"
}

dependencies {
    implementation(project(path = ":pistonmotd-api", configuration = "shadowRuntimeElements"))
    implementation(projects.pistonmotdShared)
    compileOnly(projects.pistonmotdBuildData)

    implementation("org.bstats:bstats-velocity:3.2.1")

    compileOnly("com.velocitypowered:velocity-api:4.1.1")
}

tasks {
    runVelocity {
        version("3.4.0-SNAPSHOT")
    }
}
