package net.pistonmaster.pistonmotd.bukkit;

import io.papermc.lib.PaperLib;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.pistonmaster.pistonmotd.api.PlaceholderUtil;
import net.pistonmaster.pistonmotd.utils.LuckPermsWrapper;
import net.pistonmaster.pistonutils.logging.PistonLogger;
import net.pistonmaster.pistonutils.update.UpdateChecker;
import net.pistonmaster.pistonutils.update.UpdateParser;
import net.pistonmaster.pistonutils.update.UpdateType;
import org.bstats.bukkit.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PistonMOTDBukkit extends JavaPlugin {
    protected File icons;
    protected LuckPermsWrapper luckpermsWrapper = null;
    private Logger log;

    @Override
    public void onEnable() {
        log = getLogger();
        BukkitAudiences.create(this);

        log.info("  _____  _       _                 __  __   ____  _______  _____  ");
        log.info(" |  __ \\(_)     | |               |  \\/  | / __ \\|__   __||  __ \\ ");
        log.info(" | |__) |_  ___ | |_  ___   _ __  | \\  / || |  | |  | |   | |  | |");
        log.info(" |  ___/| |/ __|| __|/ _ \\ | '_ \\ | |\\/| || |  | |  | |   | |  | |");
        log.info(" | |    | |\\__ \\| |_| (_) || | | || |  | || |__| |  | |   | |__| |");
        log.info(" |_|    |_||___/ \\__|\\___/ |_| |_||_|  |_| \\____/   |_|   |_____/ ");
        log.info("                                                                  ");

        log.info(ChatColor.AQUA + "Loading config");
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();
        File iconFolder = new File(getDataFolder(), "icons");

        if (!iconFolder.exists() && !iconFolder.mkdir()) {
            getLogger().log(Level.SEVERE, "Couldn't create icon folder!");
        }
        icons = iconFolder;

        log.info(ChatColor.AQUA + "Registering placeholders");
        PlaceholderUtil.registerParser(new CommonPlaceholder());
        if (PaperLib.isPaper()) {
            PlaceholderUtil.registerParser(new TPSPlaceholder());
        }

        log.info(ChatColor.AQUA + "Looking for hooks");
        if (getServer().getPluginManager().getPlugin("LuckPerms") != null) {
            try {
                log.info(ChatColor.AQUA + "Hooking into LuckPerms");
                luckpermsWrapper = new LuckPermsWrapper();
            } catch (Exception ignored) {
            }
        }

        log.info(ChatColor.AQUA + "Registering listeners");
        if (PaperLib.isPaper()) {
            getServer().getPluginManager().registerEvents(new PingEventPaper(this), this);
        } else {
            PaperLib.suggestPaper(this);

            getServer().getPluginManager().registerEvents(new PingEventSpigot(this), this);
        }

        log.info(ChatColor.AQUA + "Registering command");
        Objects.requireNonNull(getServer().getPluginCommand("pistonmotd")).setTabCompleter(new BukkitCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("pistonmotd")).setExecutor(new BukkitCommand(this));

        log.info(ChatColor.AQUA + "Checking for a newer version");
        new UpdateChecker(new PistonLogger(getLogger())).getVersion("https://www.pistonmaster.net/PistonMOTD/VERSION.txt", version -> new UpdateParser(getDescription().getVersion(), version).parseUpdate(updateType -> {
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
                log.info(ChatColor.RED + "Download it at: https://github.com/AlexProgrammerDE/PistonMOTD/releases");
            }
        }));

        log.info(ChatColor.AQUA + "Loading metrics");
        new Metrics(this, 9100);

        log.info(ChatColor.AQUA + "Done! :D");
    }

    @Override
    public void onDisable() {
        log.info(ChatColor.AQUA + "Unregistering listeners");
        HandlerList.unregisterAll(this);
    }
}
