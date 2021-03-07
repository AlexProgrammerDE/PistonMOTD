PistonMOTD API
---------------
PistonMOTD has a great API that allows you to integrate your own placeholders.

<!-- MACRO{toc|fromDepth=1|toDepth=3} -->

### How to set up

PistonMOTD uses CodeMC as the host for the API dependency
hosting: [Click here!](https://ci.codemc.io/job/AlexProgrammerDE/job/PistonMOTD/)

#### Maven

Repository:

```xml

<repositories>
    <repository>
        <id>CodeMC</id>
        <url>https://repo.codemc.org/repository/maven-public</url>
    </repository>
</repositories>
```

Dependency:

```xml

<dependency>
    <groupId>net.pistonmaster</groupId>
    <artifactId>pistonmotd-api</artifactId>
    <version>4.3.2</version>
</dependency>
```

#### Gradle

Repository:

```groovy
allprojects {
    repositories {
        maven { url 'https://repo.codemc.org/repository/maven-public' }
    }
}
```

Dependency:

```groovy
dependencies {
    implementation 'com.github.AlexProgrammerDE:PistonMOTD:4.1.0'
}
```

### Usage

To register you need a class implementing
the [PlaceholderParser](https://github.com/AlexProgrammerDE/PistonMOTD/blob/master/pistonmotd-api/src/main/java/me/alexprogrammerde/pistonmotd/api/PlaceholderParser.java)
and to register it at
the [PlaceholderUtil](https://github.com/AlexProgrammerDE/PistonMOTD/blob/master/pistonmotd-api/src/main/java/me/alexprogrammerde/pistonmotd/api/PlaceholderUtil.java)
with [#registerParser()](https://github.com/AlexProgrammerDE/PistonMOTD/blob/master/pistonmotd-api/src/main/java/me/alexprogrammerde/pistonmotd/api/PlaceholderUtil.java#L44)
.

Here is an example of that:
[ServerPlaceholder](https://github.com/AlexProgrammerDE/PistonMOTD/blob/master/pistonmotd-bungee/src/main/java/me/alexprogrammerde/pistonmotd/bungee/ServerPlaceholder.java)
[Registering](https://github.com/AlexProgrammerDE/PistonMOTD/blob/master/pistonmotd-bungee/src/main/java/me/alexprogrammerde/pistonmotd/bungee/PistonMOTDBungee.java#L57)