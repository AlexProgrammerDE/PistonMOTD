package me.alexprogrammerde.PistonMOTD.bukkit;

import io.papermc.lib.PaperLib;
import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class PistonMOTDBukkit extends JavaPlugin {
    Logger log;

    @Override
    public void onEnable() {
        log = getLogger();

        log.info(ChatColor.AQUA + "Loading config");
        saveDefaultConfig();

        log.info(ChatColor.AQUA + "Registering listeners");
        if (PaperLib.isPaper()) {
            getServer().getPluginManager().registerEvents(new PingEventPaper(this), this);
        } else {
            PaperLib.suggestPaper(this);

            getServer().getPluginManager().registerEvents(new PingEventSpigot(this), this);
        }

        log.info(ChatColor.AQUA + "Registering commands");
        getServer().getPluginCommand("pistonmotdbukkit").setTabCompleter(new BukkitCommand(this));
        getServer().getPluginCommand("pistonmotdbukkit").setExecutor(new BukkitCommand(this));
    }

    @Override
    public void onDisable() {
        log.info(ChatColor.AQUA + "Unregistering listeners");
        HandlerList.unregisterAll(this);
    }
}
