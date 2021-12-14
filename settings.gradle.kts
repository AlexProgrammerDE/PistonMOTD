enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("VERSION_CATALOGS")

rootProject.name = "PistonMOTD"

setOf("utils", "api", "bukkit", "bungee", "sponge", "velocity", "plugin").forEach { add(it) }

fun add(name: String) {
    include(":pistonmotd-$name")
}