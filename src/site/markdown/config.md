PistonMOTD Config
---------------

Best MOTD plugin multi platform support!

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

### Formatting
How to make your server look shiny in the server list!

#### Styling
|Color(Minecraft Name)|Color Code|
|---|---|
|Dark Red(dark_red)|&4|
|Red(red)|&c|
|Gold(gold)|&6|
|Yellow(yellow)|&e|
|Dark Green(dark_green)|&2|
|Green(green)|&a|
|Aqua(aqua)|&b|
|Dark Aqua(dark_aqua)|&3|
|Dark Blue(dark_blue)|&1|
|Blue(blue)|&9|
|Light Purple(light_purple)|&d|
|Dark Purple(dark_purple)|&5|
|White(white)|&f|
|Gray(gray)|&7|
|Dark Gray(dark_gray)|&8|
|Black(black)|&0|

|Format|Color Code|
|---|---|
|"reset" Reset color to default|&r|
|Bold|&l|
|Italic|&o|
|Underline|&n|
|Strike|&m|
|Random characters|&k|

#### RGB
The RGB format is `&#FFFFFF`. **MOTD only!**

#### MiniMessage
The MiniMessage formatting is supported! It also allows you to create RGB and not RGB gradients and rainbow colors!
Here is a link to the documentation of it: [Click here!](https://docs.adventure.kyori.net/minimessage.html#format)

#### Placeholders
| Placeholder | Description |
|---|---|
|%online%|The actual playercount.|
|%max%|The actual player maximum.|

### Specific
Some entries are platform specific:

#### Proxy
This includes proxy software such as bungeecord, waterfall and velocity.

##### Bukkit playercounter
Will show the playercounter like on bukkit:
```
Pistonmaster
Player2
Player3
```

##### Server placeholder
The playercount of a server can be get by `%online_servername%`!

#### Server
This includes server software such as spigot, paper and sponge.

##### TPS placeholder (paper only)
On paper, you have the `%tps%` placeholder.
