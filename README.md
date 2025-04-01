# PistonMOTD

[![Discord embed](https://discordapp.com/api/guilds/739784741124833301/embed.png)](https://discord.gg/FZtaMpuvj8)
[![Build Status](https://ci.codemc.io/buildStatus/icon?job=AlexProgrammerDE%2FPistonMOTD)](https://ci.codemc.io/job/AlexProgrammerDE/job/PistonMOTD/)
[![GitHub release (latest by date)](https://img.shields.io/github/v/release/AlexProgrammerDE/PistonMOTD)](https://github.com/AlexProgrammerDE/PistonBot/releases)

**Best MOTD plugin with multi-platform support!**

## Features

* Customize player-counter tooltip
* Customize the online player and max player counter
* Random MOTD
* A custom client out of date message. You can force that to always happen too.
* Random favicon
* Placeholders (extendable via api)
* Hide your player-count
* Very easy to understand config
* Modular (You can enable/disable every feature)

Compatible with external MOTD plugins. (Can't promise it will work with every MOTD plugin.)

## 🌈 Community

Feel free to join our Discord community server:

[![Discord Banner](https://discord.com/api/guilds/739784741124833301/widget.png?style=banner2)](https://discord.gg/FZtaMpuvj8)

This project is in active development, so if you have any feature requests or issues please submit them here on GitHub. PRs are welcome, too. :octocat:

## API

We got an easy to integrate API for adding additional placeholders into the plugin.

Repository:

```xml
  <repositories>
      <repository>
          <id>codemc-repo</id>
          <url>https://repo.codemc.org/repository/maven-public/</url>
      </repository>
  </repositories>
```

Dependency:

Latest Version (Without the v):
![Latest Version](https://img.shields.io/github/v/tag/AlexProgrammerDE/PistonMOTD?label=version)

```xml
  <dependencies>
    <dependency>
      <groupId>net.pistonmaster</groupId>
      <artifactId>pistonmotd-api</artifactId>
      <version>VERSION HERE</version>
    </dependency>
  </dependencies>
```

## Links

* Modrinth: https://modrinth.com/plugin/pistonmotd
* SpigotMC: https://www.spigotmc.org/resources/80567/
* Ore: https://ore.spongepowered.org/AlexProgrammerDE/PistonMOTD/
