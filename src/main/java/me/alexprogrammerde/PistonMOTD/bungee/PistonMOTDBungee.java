package me.alexprogrammerde.PistonMOTD.bungee;

import me.alexprogrammerde.PistonMOTD.api.PlaceholderUtil;
import me.alexprogrammerde.PistonMOTD.utils.UpdateChecker;
import me.alexprogrammerde.PistonMOTD.utils.UpdateParser;
import me.alexprogrammerde.PistonMOTD.utils.UpdateType;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PistonMOTDBungee extends Plugin {
    Configuration config;
    File icons;
    ConfigManager manager;
    List<String> headlist = new ArrayList<>();
    Logger log;

    @Override
    public void onEnable() {
        log = getLogger();

        headlist.add("# You can find color codes here: https://minecraft.tools/en/color-code.php");
        headlist.add("# Formatting comes after the color! &d&l will work, but not &l&d.");
        headlist.add("# Placeholders: %online% (Players online)");
        headlist.add("# %max% (Server max slots)");
        headlist.add("# %aftericon% adds a bunch of spaces so the text is after the icon. (Only for protocol)");
        headlist.add("# %newline% adds a newline to your motd.");

        log.info(ChatColor.AQUA + "Loading config");
        manager = new ConfigManager(this, headlist);
        config = manager.getConfig("bungeeconfig.yml", "config.yml");
        icons = manager.getIcons();

        log.info(ChatColor.AQUA + "Registering placeholders");
        for (String server : getProxy().getServers().keySet()) {
            PlaceholderUtil.registerParser(new ServerPlaceholder(server));
        }

        PlaceholderUtil.registerParser(new CommonPlaceholder());

        log.info(ChatColor.AQUA + "Registering listeners");
        getProxy().getPluginManager().registerListener(this, new PingEvent(this, icons));

        log.info(ChatColor.AQUA + "Registering commands");
        getProxy().getPluginManager().registerCommand(this, new BungeeCommand(this, "pistonmotd"));

        log.info(ChatColor.AQUA + "Checking for a newer version");
        new UpdateChecker(getLogger(), 80567).getVersion(version -> {
            new UpdateParser(getDescription().getVersion(), version).parseUpdate(updatetype -> {
                if (updatetype == UpdateType.NONE) {
                    log.info(ChatColor.AQUA + "Your up to date!");
                } else {
                    if (updatetype == UpdateType.MAJOR) {
                        log.info(ChatColor.RED + "There is a MAJOR update available!");
                    } else if (updatetype == UpdateType.MINOR) {
                        log.info(ChatColor.RED + "There is a MINOR update available!");
                    } else if (updatetype == UpdateType.PATCH) {
                        log.info(ChatColor.RED + "There is a PATCH update available!");
                    }

                    log.info(ChatColor.RED + "Current version: " + this.getDescription().getVersion() + " New version: " + version);
                    log.info(ChatColor.RED + "Download it at: https://www.spigotmc.org/resources/80567");
                }
            });
        });

        log.info(ChatColor.AQUA + "Loading metrics");
        new Metrics(this, 8968);
    }

    @Override
    public void onDisable() {
        log.info(ChatColor.AQUA + "Unloading the listeners");
        getProxy().getPluginManager().unregisterListeners(this);

        log.info(ChatColor.AQUA + "Unloading the commands");
        getProxy().getPluginManager().unregisterCommands(this);

        log.info(ChatColor.AQUA + "Finished unloading!");
    }

    public void reloadConfiguration() {
        config = manager.getConfig("bungeeconfig.yml", "config.yml");
    }
}
