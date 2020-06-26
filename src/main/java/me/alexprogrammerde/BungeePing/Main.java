package me.alexprogrammerde.BungeePing;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.bstats.bungeecord.Metrics;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.logging.Logger;

public class Main extends Plugin {
    Configuration configuration;

    public void onEnable() {
        Logger logger = this.getLogger();

        logger.info("§bLoading config.");
        if (!getDataFolder().exists())
            getDataFolder().mkdir();
        File file = new File(getDataFolder(), "config.yml");

        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String text = configuration.getString("text");

        logger.info("§bRegistering listeners.");
        getProxy().getPluginManager().registerListener(this, new PingEvent(configuration, this));

        logger.info("§bRegistering commands");
        getProxy().getPluginManager().registerCommand(this, new StatusCommand("status", this));

        logger.info("§bChecking for a newer version.");
        new UpdateChecker(this, 80567).getVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                logger.info("§bYour up to date!");
            } else {
                logger.info("§bThere is a new update available. Download it at: https://www.spigotmc.org/resources/bungeeping.80567/history (You may need to remove the old config to get a never one.)");
            }
        });

        logger.info("§bLoading metrics");
        int pluginId = 7939;
        Metrics metrics = new Metrics(this, pluginId);
    }

    public void reloadConfiguration() {
        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
