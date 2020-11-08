package me.alexprogrammerde.pistonmotd.bukkit;

import io.papermc.lib.PaperLib;
import me.alexprogrammerde.pistonmotd.api.PlaceholderUtil;
import me.alexprogrammerde.pistonmotd.utils.UpdateChecker;
import me.alexprogrammerde.pistonmotd.utils.UpdateParser;
import me.alexprogrammerde.pistonmotd.utils.UpdateType;
import org.bstats.bukkit.MetricsLite;
import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PistonMOTDBukkit extends JavaPlugin {
    private Logger log;
    protected File icons;

    @Override
    public void onEnable() {
        log = getLogger();

        log.info("  _____  _       _                 __  __   ____  _______  _____  ");
        log.info(" |  __ \\(_)     | |               |  \\/  | / __ \\|__   __||  __ \\ ");
        log.info(" | |__) |_  ___ | |_  ___   _ __  | \\  / || |  | |  | |   | |  | |");
        log.info(" |  ___/| |/ __|| __|/ _ \\ | '_ \\ | |\\/| || |  | |  | |   | |  | |");
        log.info(" | |    | |\\__ \\| |_| (_) || | | || |  | || |__| |  | |   | |__| |");
        log.info(" |_|    |_||___/ \\__|\\___/ |_| |_||_|  |_| \\____/   |_|   |_____/ ");
        log.info("                                                                  ");

        log.info(ChatColor.AQUA + "Loading config");
        saveDefaultConfig();
        File iconFolder = new File(getDataFolder(), "icons");

        if (!iconFolder.exists()) {
            if (!iconFolder.mkdir())  {
                getLogger().log(Level.SEVERE, "Couldn't create icon folder!");
            }
        }
        icons = iconFolder;

        log.info(ChatColor.AQUA + "Registering placeholders");
        PlaceholderUtil.registerParser(new CommonPlaceholder());
        if (PaperLib.isPaper()) {
            PlaceholderUtil.registerParser(new TPSPlaceholder());
        }

        log.info(ChatColor.AQUA + "Registering listeners");
        if (PaperLib.isPaper()) {
            getServer().getPluginManager().registerEvents(new PingEventPaper(this), this);
        } else {
            PaperLib.suggestPaper(this);

            getServer().getPluginManager().registerEvents(new PingEventSpigot(this), this);
        }

        log.info(ChatColor.AQUA + "Registering commands");
        Objects.requireNonNull(getServer().getPluginCommand("pistonmotd")).setTabCompleter(new BukkitCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("pistonmotd")).setExecutor(new BukkitCommand(this));

        log.info(ChatColor.AQUA + "Checking for a newer version");
        new UpdateChecker(getLogger()).getVersion(version -> new UpdateParser(getDescription().getVersion(), version).parseUpdate(updateType -> {
            if (updateType == UpdateType.NONE || updateType == UpdateType.AHEAD) {
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
                log.info(ChatColor.RED + "Download it at: https://www.spigotmc.org/resources/80567/updates");
            }
        }));

        log.info(ChatColor.AQUA + "Loading metrics");
        new MetricsLite(this, 9100);
    }

    @Override
    public void onDisable() {
        log.info(ChatColor.AQUA + "Unregistering listeners");
        HandlerList.unregisterAll(this);
    }
}
