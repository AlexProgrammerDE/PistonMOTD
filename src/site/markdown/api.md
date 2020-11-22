PistonMOTD API
---------------

<!-- MACRO{toc|fromDepth=1|toDepth=3} -->

### How to set up

PistonMOTD uses jitpack as the host for the API dependency hosting: [Click here!](https://jitpack.io/#AlexProgrammerDE/PistonMOTD/)

#### Maven
Repository:
```xml
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```

Dependency:
```xml
	<dependency>
	    <groupId>com.github.AlexProgrammerDE</groupId>
	    <artifactId>PistonMOTD</artifactId>
	    <version>${project.version}</version>
	</dependency>
```

#### Gradle
Repository:
```hocon
	allprojects {
		repositories {
			maven { url 'https://jitpack.io' }
		}
	}
```

Dependency:
```hocon
	dependencies {
    	        implementation 'com.github.AlexProgrammerDE:PistonMOTD:4.1.0'
    	}
```

### Usage

To register you need a class implementing the [PlaceholderParser](https://github.com/AlexProgrammerDE/PistonMOTD/blob/master/pistonmotd-api/src/main/java/me/alexprogrammerde/pistonmotd/api/PlaceholderParser.java) and to register it at the [PlaceholderUtil](https://github.com/AlexProgrammerDE/PistonMOTD/blob/master/pistonmotd-api/src/main/java/me/alexprogrammerde/pistonmotd/api/PlaceholderUtil.java) with [#registerParser()](https://github.com/AlexProgrammerDE/PistonMOTD/blob/master/pistonmotd-api/src/main/java/me/alexprogrammerde/pistonmotd/api/PlaceholderUtil.java#L44).

Here is a example of that:
[ServerPlaceholder](https://github.com/AlexProgrammerDE/PistonMOTD/blob/master/pistonmotd-bungee/src/main/java/me/alexprogrammerde/pistonmotd/bungee/ServerPlaceholder.java)
[Registering](https://github.com/AlexProgrammerDE/PistonMOTD/blob/master/pistonmotd-bungee/src/main/java/me/alexprogrammerde/pistonmotd/bungee/PistonMOTDBungee.java#L57)