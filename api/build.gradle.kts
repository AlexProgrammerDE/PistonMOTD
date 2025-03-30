plugins {
    id("pm.java-conventions")
    id("pm.kyori-conventions")
}

dependencies {
    implementation(projects.pistonmotdKyori)
    implementation("org.apiguardian:apiguardian-api:1.1.2")
}
