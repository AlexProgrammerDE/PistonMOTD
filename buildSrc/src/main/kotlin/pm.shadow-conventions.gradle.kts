import com.github.jengelman.gradle.plugins.shadow.tasks.ConfigureShadowRelocation
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("pm.java-conventions")
    id("com.github.johnrengelman.shadow")
}

tasks {
    jar {
        archiveClassifier.set("unshaded")
        from(project.rootProject.file("LICENSE"))
    }

    shadowJar {
        exclude("META-INF/SPONGEPO.SF", "META-INF/SPONGEPO.DSA", "META-INF/SPONGEPO.RSA")
        minimize()
        configureRelocations()
    }

    build {
        dependsOn(shadowJar)
    }
}


fun ShadowJar.configureRelocations() {
    relocate("com.google.gson", "net.pistonmaster.pistonmotd.shadow.gson")
    relocate("io.papermc.lib", "net.pistonmaster.pistonmotd.shadow.paperlib")
    relocate("io.leangen.geantyref", "net.pistonmaster.pistonmotd.shadow.geantyref")
    relocate("org.apache", "net.pistonmaster.pistonmotd.shadow.apache")
    relocate("org.apiguardian", "net.pistonmaster.pistonmotd.shadow.apiguardian")
    relocate("org.bstats", "net.pistonmaster.pistonmotd.shadow.bstats")
    relocate("org.slf4j", "net.pistonmaster.pistonmotd.shadow.slf4j")
    relocate("org.intellij.lang.annotations", "net.pistonmaster.pistonmotd.shadow.ijannotations")
    relocate("org.jetbrains.annotations", "net.pistonmaster.pistonmotd.shadow.jbannotations")
    relocate("org.spongepowered.configurate", "net.pistonmaster.pistonmotd.shadow.configurate")
    relocate("org.yaml.snakeyaml", "net.pistonmaster.pistonmotd.shadow.snakeyaml")
    relocate("net.pistonmaster.pistonutils", "net.pistonmaster.pistonmotd.shadow.pistonutils")
    relocate("net.kyori", "net.pistonmaster.pistonmotd.shadow.kyori")
}