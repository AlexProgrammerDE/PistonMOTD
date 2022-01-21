plugins {
    `java-library`
    `maven-publish`
}

dependencies {
    implementation("commons-io:commons-io:2.11.0")
    implementation("org.apiguardian:apiguardian-api:1.1.2")
    implementation("net.pistonmaster:PistonUtils:1.3.1")

    compileOnly("net.luckperms:api:5.3")

    compileOnly("org.projectlombok:lombok:1.18.22")
    annotationProcessor("org.projectlombok:lombok:1.18.22")
}

tasks {
    processResources {
        expand("version" to version, "description" to description, "url" to "https://pistonmaster.net/PistonMOTD")
    }
}

java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}
