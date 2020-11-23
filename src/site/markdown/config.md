PistonMOTD Config
---------------
Not everything in the config is self explaining. So we documented it here.

<!-- MACRO{toc|fromDepth=1|toDepth=4} -->

### Global
Config entries you will find across all platforms:

#### MOTD
List of motd shown to the client, which are separated by `%newline%`.

**Note:** RGB colors **ARE** supported!

#### Playercounter
This is a small list of entries shown when you hover over the playercount. (0/100 as an example)

#### Protocol
This is a message shown when the clients version isn't the one from the server.

It allows colors codes (When no color codes are there its red)

#### Override online
Overrides the displayed count of players who are online!

#### Override max
Overrides the displayed count of players who can join your server!

#### Icons
All `64x64` **png** formatted images in the icons folder of PistonMOTD (often where the config.yml is)
will be randomly displayed if this is enabled. 

**Note:** The name doesn't matter, and you can also force a default image by just putting one image into the folder!

### Hooks
Hooks allow you to hook into other plugins api and use them for a special feature:

#### Luckperms playercounter
Displays:
````
[Epic dev] Pistonmaster
````

instead of
````
Pistonmaster
````
In the playerlist aka playercounter.
The highest role of the player (by weight) will be displayed!

### Specific
Some features are platform specific:

#### Proxy
This includes proxy software such as bungeecord, waterfall and velocity.

##### Bukkit playercounter
Will show the playercounter like on bukkit:
```
Pistonmaster
Player2
Player3
```