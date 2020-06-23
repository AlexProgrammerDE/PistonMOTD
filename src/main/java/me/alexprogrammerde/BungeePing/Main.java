package me.alexprogrammerde.BungeePing;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.logging.Logger;

public class Main extends Plugin {
    Configuration configuration;

    public void onEnable() {
        getLogger().info("§bLoading config.");
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

        getLogger().info("§bRegistering listeners.");
        getProxy().getPluginManager().registerListener(this, new PingEvent(text));

        Logger logger = this.getLogger();
        logger.info("§bChecking for a newer version.");
        new UpdateChecker(this, 80567).getVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                logger.info("§bYour up to date!");
            } else {
                logger.info("§bThere is a new update available. Download it at: https://www.spigotmc.org/resources/bungeeping.80567/history");
            }
        });

        getLogger().info("§bEnabled the plugin. :)");
    }


}
