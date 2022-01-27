plugins {
    id("pm.shadow-conventions")
}

dependencies {
    implementation(project(":pistonmotd-api", "shadow"))
    implementation(projects.pistonmotdShared)
    implementation(projects.pistonmotdKyoriNormal)
    compileOnly(projects.pistonmotdBuildData)

    implementation("org.bstats:bstats-velocity:2.2.1")

    compileOnly("com.velocitypowered:velocity-api:3.1.1")
    annotationProcessor("com.velocitypowered:velocity-api:3.1.1")
}
