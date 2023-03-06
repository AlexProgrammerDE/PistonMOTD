import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `java-library`
    id("pm.shadow-conventions")
}

(tasks.getByName("shadowJar") as ShadowJar).archiveFileName.set(
    "PistonMOTD-${
        project.name.substringAfter("pistonmotd-").replaceFirstChar(Char::titlecase)
    }-${project.version}.jar"
)

(tasks.getByName("shadowJar") as ShadowJar).destinationDirectory.set(rootProject.layout.buildDirectory.dir("libs"))
