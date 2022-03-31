import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

val platforms = setOf(
    rootProject.projects.pistonmotdBukkit,
    rootProject.projects.pistonmotdBungee,
    rootProject.projects.pistonmotdSponge,
    rootProject.projects.pistonmotdVelocity
).map { it.dependencyProject }

tasks {
    jar {
        archiveClassifier.set("")
        archiveFileName.set("PistonMOTD-${rootProject.version}.jar")
        destinationDirectory.set(rootProject.projectDir.resolve("build/libs"))
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        platforms.forEach { platform ->
            val shadowJarTask = platform.tasks.named<ShadowJar>("shadowJar").forUseAtConfigurationTime().get()
            dependsOn(shadowJarTask)
            dependsOn(platform.tasks.withType<Jar>())
            from(zipTree(shadowJarTask.archiveFile))
        }
    }
}
