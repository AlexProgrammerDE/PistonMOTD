import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("pm.java-conventions")
    id("com.gradleup.shadow")
}

tasks {
    jar {
        archiveClassifier.set("unshaded")
        from(project.rootProject.file("LICENSE"))
    }

    shadowJar {
        exclude("META-INF/SPONGEPO.SF", "META-INF/SPONGEPO.DSA", "META-INF/SPONGEPO.RSA")
        configureRelocations()
    }

    build {
        dependsOn(shadowJar)
    }
}

fun ShadowJar.configureRelocations() {
    relocate("io.papermc.lib", "net.pistonmaster.pistonmotd.shadow.paperlib")
    relocate("org.bstats", "net.pistonmaster.pistonmotd.shadow.bstats")
    relocate("net.pistonmaster.pistonutils", "net.pistonmaster.pistonmotd.shadow.pistonutils")
    relocate("net.skinsrestorer.axiom", "net.pistonmaster.pistonmotd.shadow.axiom")
    relocate("org.yaml.snakeyaml", "net.pistonmaster.pistonmotd.shadow.snakeyaml")
    relocate("org.intellij.lang.annotations", "net.pistonmaster.pistonmotd.shadow.annotations.intellij")
    relocate("org.jetbrains.annotations", "net.pistonmaster.pistonmotd.shadow.annotations.jetbrains")
    relocate("com.google.gson", "net.pistonmaster.pistonmotd.shadow.gson")
    relocate("com.google.errorprone", "net.pistonmaster.pistonmotd.shadow.errorprone")
    relocate("net.lenni0451.mcstructs", "net.pistonmaster.pistonmotd.shadow.mcstructs")
    relocate("com.tcoded.folialib", "net.pistonmaster.pistonmotd.shadow.folialib")
}
