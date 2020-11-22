PistonMOTD API
---------------

<!-- MACRO{toc|fromDepth=1|toDepth=3} -->

### How to set up

PistonMOTD uses jitpack as the host for the api dependency: https://jitpack.io/#AlexProgrammerDE/PistonMOTD/ 

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