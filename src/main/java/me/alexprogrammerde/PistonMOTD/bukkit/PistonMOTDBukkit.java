package me.alexprogrammerde.PistonMOTD.bukkit;

import io.papermc.lib.PaperLib;
import me.alexprogrammerde.PistonMOTD.api.PlaceholderUtil;
import me.alexprogrammerde.PistonMOTD.utils.UpdateChecker;
import me.alexprogrammerde.PistonMOTD.utils.UpdateParser;
import me.alexprogrammerde.PistonMOTD.utils.UpdateType;
import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Logger;

public class PistonMOTDBukkit extends JavaPlugin {
    Logger log;

    @Override
    public void onEnable() {
        log = getLogger();

        log.info(ChatColor.AQUA + "Loading config");
        saveDefaultConfig();

        log.info(ChatColor.AQUA + "Registering placeholders");
        PlaceholderUtil.registerParser(new CommonPlaceholder());

        log.info(ChatColor.AQUA + "Registering listeners");
        if (PaperLib.isPaper()) {
            getServer().getPluginManager().registerEvents(new PingEventPaper(this), this);
        } else {
            PaperLib.suggestPaper(this);

            getServer().getPluginManager().registerEvents(new PingEventSpigot(this), this);
        }

        log.info(ChatColor.AQUA + "Registering commands");
        Objects.requireNonNull(getServer().getPluginCommand("pistonmotdbukkit")).setTabCompleter(new BukkitCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("pistonmotdbukkit")).setExecutor(new BukkitCommand(this));

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
    }

    @Override
    public void onDisable() {
        log.info(ChatColor.AQUA + "Unregistering listeners");
        HandlerList.unregisterAll(this);
    }
}
