package me.alexprogrammerde.BungeeStatus;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class ConfigManager {
    Configuration config;
    Configuration templateconfig;
    List<String> configkeys = new ArrayList<>();
    List<String> templatekeys = new ArrayList<>();
    File icons;
    Logger log;
    Plugin plugin;

    public ConfigManager(Plugin plugin) {
        this.plugin = plugin;
        log = plugin.getLogger();
    }

    public Configuration getConfig(String filename) {
        if (!plugin.getDataFolder().exists())
            plugin.getDataFolder().mkdir();
        File file = new File(plugin.getDataFolder(), filename);

        if (!file.exists()) {
            try (InputStream in = plugin.getResourceAsStream(filename)) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(plugin.getDataFolder(), filename));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Get all keys from the real file
        for (String key : config.getKeys()) {
            configkeys.add(key);

            if (config.get(key) instanceof Configuration) {
                iterateKey(key, configkeys, config);
            }
        }

        // Get template config file
        templateconfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(plugin.getResourceAsStream(filename));

        // Get all keys from the template
        for (String key : templateconfig.getKeys()) {
            templatekeys.add(key);

            if (templateconfig.get(key) instanceof Configuration) {
                iterateKey(key, templatekeys, templateconfig);
            }
        }

        Collections.reverse(configkeys);
        Collections.reverse(templatekeys);

        for (String key : templatekeys) {
            if (!config.contains(key)) {
                config.set(key, templateconfig.get(key));
            }
        }

        for (String key : configkeys) {
            if (!templateconfig.contains(key)) {
                config.set(key, null);
            } else if (!templateconfig.get(key).getClass().equals(config.get(key).getClass())) {
                config.set(key, templateconfig.get(key));
            }

            templatekeys = new ArrayList<>();

            // Get template config file
            templateconfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(plugin.getResourceAsStream(filename));

            for (String tkey : templateconfig.getKeys()) {
                templatekeys.add(tkey);

                if (templateconfig.get(tkey) instanceof Configuration) {
                    iterateKey(tkey, templatekeys, templateconfig);
                }
            }

            Collections.reverse(templatekeys);
        }

        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, new File(plugin.getDataFolder(), filename));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return config;
    }

    public File getIcons() {
        File iconFolder = new File(plugin.getDataFolder(), "icons");

        if (!iconFolder.exists())
            iconFolder.mkdir();
        icons = iconFolder;

        return icons;
    }

    void iterateKey(String gkey, List<String> keylist, Configuration config) {
        for (String key : config.getSection(gkey).getKeys()) {
            keylist.add(gkey + "." + key);

            if (config.get(gkey + "." + key) instanceof Configuration) {
                iterateKey(gkey + "." + key, keylist, config);
            }
        }
    }
}
