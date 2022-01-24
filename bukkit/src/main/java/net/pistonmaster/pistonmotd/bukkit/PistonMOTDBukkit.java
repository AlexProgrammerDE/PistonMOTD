package net.pistonmaster.pistonmotd.bukkit;

import io.papermc.lib.PaperLib;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.pistonmaster.pistonmotd.api.PlaceholderUtil;
import net.pistonmaster.pistonmotd.shared.PistonMOTDPlugin;
import net.pistonmaster.pistonmotd.shared.utils.LuckPermsWrapper;
import org.apache.commons.io.FilenameUtils;
import org.bstats.bukkit.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.CachedServerIcon;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

public class PistonMOTDBukkit extends JavaPlugin implements PistonMOTDPlugin {
    protected File icons;
    protected LuckPermsWrapper luckpermsWrapper = null;
    protected List<CachedServerIcon> favicons;
    protected ThreadLocalRandom random;
    private Logger log;

    @Override
    public void onEnable() {
        log = getLogger();
        BukkitAudiences.create(this);

        logName();

        log.info(ChatColor.AQUA + "Loading config");
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();
        File iconFolder = new File(getDataFolder(), "icons");

        if (!iconFolder.exists() && !iconFolder.mkdir()) {
            getLogger().severe("Couldn't create icon folder!");
        }
        icons = iconFolder;
        if (getConfig().getBoolean("icons")) { //Dont load favicons into memory unless the feature is enabled
            favicons = loadFavicons();
            random = ThreadLocalRandom.current();
        }

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

        checkUpdate();

        log.info(ChatColor.AQUA + "Loading metrics");
        new Metrics(this, 9100);

        log.info(ChatColor.AQUA + "Done! :D");
    }

    @Override
    public void onDisable() {
        log.info(ChatColor.AQUA + "Unregistering listeners");
        HandlerList.unregisterAll(this);
    }

    private List<CachedServerIcon> loadFavicons() {
        File[] icons = this.icons.listFiles();

        List<File> validFiles = new ArrayList<>();

        if (icons != null && icons.length != 0) {
            for (File image : icons) {
                if (FilenameUtils.getExtension(image.getPath()).equals("png")) {
                    validFiles.add(image);
                }
            }
        }
        return Arrays.asList(validFiles.stream().map(this::createFavicon).filter(Objects::nonNull).toArray(CachedServerIcon[]::new));
    }

    private CachedServerIcon createFavicon(File file) {
        try {
            return getServer().loadServerIcon(file);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Path getPluginConfigFile() {
        return getDataFolder().toPath().resolve("config.yml");
    }

    @Override
    public InputStream getDefaultConfig() {
        return getResource("config.yml");
    }

    @Override
    public String getVersion() {
        return getDescription().getVersion();
    }

    @Override
    public void info(String message) {
        getLogger().info(message);
    }

    @Override
    public void warn(String message) {
        getLogger().warning(message);
    }

    @Override
    public void error(String message) {
        getLogger().severe(message);
    }
}
