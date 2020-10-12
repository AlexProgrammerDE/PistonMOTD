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
    List<String> headList = new ArrayList<>();
    Logger log;

    @Override
    public void onEnable() {
        log = getLogger();

        headList.add("# You can find color codes here: https://minecraft.tools/en/color-code.php");
        headList.add("# Formatting comes after the color! &d&l will work, but not &l&d.");
        headList.add("# Placeholders: %online% (Players online)");
        headList.add("# %max% (Server max slots)");
        headList.add("# %aftericon% adds a bunch of spaces so the text is after the icon. (Only for protocol)");
        headList.add("# %newline% adds a newline to your motd.");
        headList.add("# %online_SERVERNAME% shows the current playercount of one of the servers.");

        log.info("  _____  _       _                 __  __   ____  _______  _____  ");
        log.info(" |  __ \\(_)     | |               |  \\/  | / __ \\|__   __||  __ \\ ");
        log.info(" | |__) |_  ___ | |_  ___   _ __  | \\  / || |  | |  | |   | |  | |");
        log.info(" |  ___/| |/ __|| __|/ _ \\ | '_ \\ | |\\/| || |  | |  | |   | |  | |");
        log.info(" | |    | |\\__ \\| |_| (_) || | | || |  | || |__| |  | |   | |__| |");
        log.info(" |_|    |_||___/ \\__|\\___/ |_| |_||_|  |_| \\____/   |_|   |_____/ ");
        log.info("                                                                  ");

        log.info(ChatColor.AQUA + "Loading config");
        manager = new ConfigManager(this, headList);
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
        new UpdateChecker(getLogger(), 80567).getVersion(version -> new UpdateParser(getDescription().getVersion(), version).parseUpdate(updateType -> {
            if (updateType == UpdateType.NONE) {
                log.info(ChatColor.AQUA + "Your up to date!");
            } else {
                if (updateType == UpdateType.MAJOR) {
                    log.info(ChatColor.RED + "There is a MAJOR update available!");
                } else if (updateType == UpdateType.MINOR) {
                    log.info(ChatColor.RED + "There is a MINOR update available!");
                } else if (updateType == UpdateType.PATCH) {
                    log.info(ChatColor.RED + "There is a PATCH update available!");
                }

                log.info(ChatColor.RED + "Current version: " + this.getDescription().getVersion() + " New version: " + version);
                log.info(ChatColor.RED + "Download it at: https://www.spigotmc.org/resources/80567");
            }
        }));

        log.info(ChatColor.AQUA + "Loading metrics");
        new BungeeMetrics(this, 8968);
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
