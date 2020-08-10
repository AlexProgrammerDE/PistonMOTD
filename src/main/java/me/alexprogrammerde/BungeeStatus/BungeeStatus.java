package me.alexprogrammerde.BungeeStatus;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class BungeeStatus extends Plugin {
    public static BungeeStatus plugin;
    Configuration config;
    File icons;
    ConfigManager manager;
    List<String> headlist = new ArrayList<>();

    public void onEnable() {
        plugin = this;
        Logger logger = this.getLogger();
        headlist.add("# You can find color codes here: https://minecraft.tools/en/color-code.php");
        headlist.add("# Formatting comes after the color! &d&l will work, but not &l&d.");
        headlist.add("# Placeholders: %real_players% (The real count of players), %displayed_players% (The displayed amount of players. Might get overwritten by overrideonline)");
        headlist.add("# %real_max% (The real maximum of players), %displayed_max% (The displayed maximum of players. Might get overwritten by overridemax)");
        headlist.add("# %aftericon% adds a bunch of spaces so the text is after the icon. (Only for protocol)");

        logger.info("§bLoading config.");
        manager = new ConfigManager(this, headlist);
        config = manager.getConfig("config.yml");
        icons = manager.getIcons();

        logger.info("§bRegistering listeners.");
        getProxy().getPluginManager().registerListener(this, new PingEvent(this, icons));

        logger.info("§bRegistering commands");
        getProxy().getPluginManager().registerCommand(this, new StatusCommand("status", "bungeestatus.reload"));

        logger.info("§bChecking for a newer version.");
        new UpdateChecker(this, 80567).getVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                logger.info("§bYour up to date!");
            } else {
                logger.info("§cThere is a update available.");
                logger.info("§cCurrent version: " + this.getDescription().getVersion() + " New version: " + version);
                logger.info("§cDownload it at: https://www.spigotmc.org/resources/80567");
            }
        });

        logger.info("§bLoading metrics");
        int pluginId = 7939;
        Metrics metrics = new Metrics(this, pluginId);
    }

    public void reloadConfiguration() {
        config = manager.getConfig("config.yml");
    }
}
