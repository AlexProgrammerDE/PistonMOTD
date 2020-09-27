package me.alexprogrammerde.PistonMOTD.bungee;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class ConfigManager {
    Logger log;
    Plugin plugin;
    List<String> headlist;
    File icons;

    public ConfigManager(Plugin plugin, List<String> headlist) {
        this.plugin = plugin;
        log = plugin.getLogger();
        this.headlist = headlist;
    }

    public Configuration getConfig(String resourcename, String filename) {
        Configuration config = null;
        Configuration templateconfig;
        List<String> configkeys = new ArrayList<>();
        List<String> templatekeys = new ArrayList<>();

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        File file = new File(plugin.getDataFolder(), filename);

        if (!file.exists()) {
            try (InputStream in = plugin.getResourceAsStream(resourcename)) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            StringBuilder header = new StringBuilder();

            for (String head : headlist) {
                header.append(head).append('\n');
            }

            StringBuilder content = new StringBuilder();

            while (in.ready()) {
                content.append(in.readLine()).append('\n');
            }

            in.close();

            String output = header + "\n" + content;

            output = output.replaceAll("§", "&");

            FileWriter fstream = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(fstream);

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

        for (String key : config.getKeys()) {
            configkeys.add(key);

            if (config.get(key) instanceof Configuration) {
                iterateKey(key, configkeys, config);
            }
        }

        // Load template
        templateconfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(plugin.getResourceAsStream(resourcename));

        for (String key : templateconfig.getKeys()) {
            templatekeys.add(key);

            if (templateconfig.get(key) instanceof Configuration) {
                iterateKey(key, templatekeys, templateconfig);
            }
        }

        // Check if keys from template are in the config
        for (String key : templatekeys) {
            if (!configkeys.contains(key) || !config.get(key).getClass().equals(templateconfig.get(key).getClass())) {
                config.set(key, templateconfig.get(key));
            }
        }

        // Reload configkeys
        configkeys.clear();

        for (String key : config.getKeys()) {
            configkeys.add(key);

            if (config.get(key) instanceof Configuration) {
                iterateKey(key, configkeys, config);
            }
        }

        // Check if keys from config are in the template
        for (String key : configkeys) {
            if (!templatekeys.contains(key)) {
                config.set(key, null);
            } else if (!templateconfig.get(key).getClass().equals(config.get(key).getClass())) {
                config.set(key, templateconfig.get(key));
            }

            templatekeys.clear();

            // Get template config file
            templateconfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(plugin.getResourceAsStream(resourcename));

            for (String tkey : templateconfig.getKeys()) {
                templatekeys.add(tkey);

                if (templateconfig.get(tkey) instanceof Configuration) {
                    iterateKey(tkey, templatekeys, templateconfig);
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

            for (String head : headlist) {
                header.append(head).append('\n');
            }

            StringBuilder content = new StringBuilder();

            while (in.ready()) {
                content.append(in.readLine()).append('\n');
            }

            in.close();

            String output = header + "\n" + content;

            output = output.replaceAll("§", "&");

            FileWriter fstream = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(fstream);

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

    void iterateKey(String gkey, List<String> keylist, Configuration config) {
        for (String key : config.getSection(gkey).getKeys()) {
            keylist.add(gkey + "." + key);

            if (config.get(gkey + "." + key) instanceof Configuration) {
                iterateKey(gkey + "." + key, keylist, config);
            }
        }
    }
}
