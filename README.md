<img align="right" src="https://github.com/AlexProgrammerDE/PistonMOTD/blob/main/images/logo.png?raw=true" height="150" width="150">

[![modrinth](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/modrinth_vector.svg)](https://modrinth.com/plugin/pistonmotd) [![hangar](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/hangar_vector.svg)](https://hangar.papermc.io/Pistonmaster/PistonMOTD)

[![discord](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/social/discord-singular_vector.svg)](https://discord.gg/8Qk2mtUDFf) [![kofi](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/donate/kofi-singular_vector.svg)](https://ko-fi.com/alexprogrammerde)

# PistonMOTD

**✨ Control your Minecraft server's MOTD. Supports RGB colors and multiple platforms.**

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
* Hangar: https://hangar.papermc.io/Pistonmaster/PistonMOTD
* SpigotMC: https://www.spigotmc.org/resources/80567/
* Ore: https://ore.spongepowered.org/AlexProgrammerDE/PistonMOTD/
