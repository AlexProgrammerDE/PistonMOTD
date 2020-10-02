package me.alexprogrammerde.PistonMOTD.bungee;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ConfigManager {
    Logger log;
    Plugin plugin;
    List<String> headList;
    File icons;

    public ConfigManager(Plugin plugin, List<String> headList) {
        this.plugin = plugin;
        log = plugin.getLogger();
        this.headList = headList;
    }

    public Configuration getConfig(String resourceName, String fileName) {
        Configuration config = null;
        Configuration templateConfig;
        List<String> configKeys = new ArrayList<>();
        List<String> templateKeys = new ArrayList<>();

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        File file = new File(plugin.getDataFolder(), fileName);

        if (!file.exists()) {
            try (InputStream in = plugin.getResourceAsStream(resourceName)) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            StringBuilder header = new StringBuilder();

            for (String head : headList) {
                header.append(head).append('\n');
            }

            StringBuilder content = new StringBuilder();

            while (in.ready()) {
                content.append(in.readLine()).append('\n');
            }

            in.close();

            String output = header + "\n" + content;

            output = output.replaceAll("§", "&");

            FileWriter fStream = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(fStream);

            out.write(output);
            out.close();
        } catch (IOException e){
            System.err.println("Error: " + e.getMessage());
        }

        // Load config
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert config != null;
        for (String key : config.getKeys()) {
            configKeys.add(key);

            if (config.get(key) instanceof Configuration) {
                iterateKey(key, configKeys, config);
            }
        }

        // Load template
        templateConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(plugin.getResourceAsStream(resourceName));

        for (String key : templateConfig.getKeys()) {
            templateKeys.add(key);

            if (templateConfig.get(key) instanceof Configuration) {
                iterateKey(key, templateKeys, templateConfig);
            }
        }

        // Check if keys from template are in the config
        for (String key : templateKeys) {
            if (!configKeys.contains(key) || !config.get(key).getClass().equals(templateConfig.get(key).getClass())) {
                config.set(key, templateConfig.get(key));
            }
        }

        // Reload configkeys
        configKeys.clear();

        for (String key : config.getKeys()) {
            configKeys.add(key);

            if (config.get(key) instanceof Configuration) {
                iterateKey(key, configKeys, config);
            }
        }

        // Check if keys from config are in the template
        for (String key : configKeys) {
            if (!templateKeys.contains(key)) {
                config.set(key, null);
            } else if (!templateConfig.get(key).getClass().equals(config.get(key).getClass())) {
                config.set(key, templateConfig.get(key));
            }

            templateKeys.clear();

            // Get template config file
            templateConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(plugin.getResourceAsStream(resourceName));

            for (String tKey : templateConfig.getKeys()) {
                templateKeys.add(tKey);

                if (templateConfig.get(tKey) instanceof Configuration) {
                    iterateKey(tKey, templateKeys, templateConfig);
                }
            }
        }

        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            StringBuilder header = new StringBuilder();

            for (String head : headList) {
                header.append(head).append('\n');
            }

            StringBuilder content = new StringBuilder();

            while (in.ready()) {
                content.append(in.readLine()).append('\n');
            }

            in.close();

            String output = header + "\n" + content;

            output = output.replaceAll("§", "&");

            FileWriter fStream = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(fStream);

            out.write(output);
            out.close();

        } catch (IOException e){
            System.err.println("Error: " + e.getMessage());
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

    void iterateKey(String gKey, List<String> keyList, Configuration config) {
        for (String key : config.getSection(gKey).getKeys()) {
            keyList.add(gKey + "." + key);

            if (config.get(gKey + "." + key) instanceof Configuration) {
                iterateKey(gKey + "." + key, keyList, config);
            }
        }
    }
}
