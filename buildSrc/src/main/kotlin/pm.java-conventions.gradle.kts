plugins {
    `java-library`
    `maven-publish`
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://oss.sonatype.org/content/repositories/snapshots")
    }

    maven {
        url = uri("https://papermc.io/repo/repository/maven-public/")
    }

    maven {
        url = uri("https://repo.spongepowered.org/maven")
    }

    maven {
        url = uri("https://nexus.velocitypowered.com/repository/maven-public/")
    }

    maven {
        url = uri("https://repo.codemc.org/repository/maven-public")
    }

    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    implementation("commons-io:commons-io:2.11.0")
    implementation("org.apiguardian:apiguardian-api:1.1.2")
    implementation("net.kyori:adventure-api:4.9.3")
    implementation("net.kyori:adventure-text-serializer-legacy:4.9.3")
    implementation("net.kyori:adventure-text-serializer-legacy-text3:4.0.1")
    implementation("net.pistonmaster:PistonUtils:1.2.1")
    compileOnly("net.luckperms:api:5.3")
}

group = "net.pistonmaster"
version = "4.4.0"
java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}
