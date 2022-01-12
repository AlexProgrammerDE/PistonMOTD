plugins {
    id("pm.shadow-conventions")
}

dependencies {
    implementation(projects.pistonmotdApi)
    implementation(projects.pistonmotdUtils)
    implementation(projects.pistonmotdBuildData)

    compileOnly("com.google.code.gson:gson:2.8.9")

    compileOnly("com.velocitypowered:velocity-api:3.1.1")
    annotationProcessor("com.velocitypowered:velocity-api:3.1.1")
}
