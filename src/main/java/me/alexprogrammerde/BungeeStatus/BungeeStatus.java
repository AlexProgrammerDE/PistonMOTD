package me.alexprogrammerde.BungeeStatus;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

import java.io.File;
import java.util.logging.Logger;

public class BungeeStatus extends Plugin {
    public static BungeeStatus plugin;
    Configuration config;
    File icons;
    ConfigManager manager;

    public void onEnable() {
        plugin = this;
        Logger logger = this.getLogger();

        logger.info("§bLoading config.");
        manager = new ConfigManager(this);
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
