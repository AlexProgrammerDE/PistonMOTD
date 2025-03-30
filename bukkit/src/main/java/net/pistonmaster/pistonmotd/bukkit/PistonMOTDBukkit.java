package net.pistonmaster.pistonmotd.bukkit;

import io.papermc.lib.PaperLib;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.pistonmaster.pistonmotd.api.PlaceholderUtil;
import net.pistonmaster.pistonmotd.shared.*;
import org.bstats.bukkit.Metrics;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@SuppressWarnings({"deprecation"})
public class PistonMOTDBukkit extends JavaPlugin implements PistonMOTDPlatform {
    private BukkitAudiences adventure;

    @Override
    public void onEnable() {
        this.adventure = BukkitAudiences.create(this);
        PistonMOTDPlugin plugin = new PistonMOTDPlugin(this);

        plugin.logName();

        plugin.startupLoadConfig();

        plugin.registerCommonPlaceholder();
        if (PaperLib.isPaper()) {
            PlaceholderUtil.registerParser(new TPSPlaceholder());
        }

        plugin.loadHooks();

        startup("Registering listeners");
        if (PaperLib.isPaper()) {
            getServer().getPluginManager().registerEvents(new PingEventPaper(new StatusPingHandler(plugin)), this);
        } else {
            PaperLib.suggestPaper(this);

            getServer().getPluginManager().registerEvents(new PingEventSpigot(new StatusPingHandler(plugin)), this);
        }

        startup("Registering command");
        PluginCommand command = Objects.requireNonNull(getServer().getPluginCommand("pistonmotd"));
        BukkitCommand bukkitCommand = new BukkitCommand(plugin);
        command.setTabCompleter(bukkitCommand);
        command.setExecutor(bukkitCommand);

        plugin.checkUpdate();

        startup("Loading metrics");
        new Metrics(this, 9100);

        startup("Done! :D");
    }

    @Override
    public void onDisable() {
        if (this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }

        startup("Unregistering listeners");
        HandlerList.unregisterAll(this);
    }

    @Override
    public boolean isPluginEnabled(String pluginName) {
        return getServer().getPluginManager().getPlugin(pluginName) != null;
    }

    @Override
    public StatusFavicon createFavicon(Path path) throws Exception {
        return new StatusFavicon(getServer().loadServerIcon(path.toFile()));
    }

    @Override
    public Path getPluginConfigFile() {
        return getDataFolder().toPath().resolve("config.yml");
    }

    @Override
    public Path getFaviconFolder() {
        return getDataFolder().toPath().resolve("favicons");
    }

    @Override
    public InputStream getBundledResource(String name) {
        return getResource(name);
    }

    @Override
    public List<PlayerWrapper> getPlayers() {
        return getServer().getOnlinePlayers().stream().map(this::wrap).collect(Collectors.toList());
    }

    @Override
    public int getMaxPlayers() {
        return getServer().getMaxPlayers();
    }

    @Override
    public int getPlayerCount() {
        return getPlayers().size();
    }

    private PlayerWrapper wrap(Player player) {
        return new PlayerWrapper() {
            @Override
            public String getDisplayName() {
                return player.getDisplayName();
            }

            @Override
            public String getName() {
                return player.getName();
            }

            @Override
            public UUID getUniqueId() {
                return player.getUniqueId();
            }

            @Override
            public Object getHandle() {
                return player;
            }
        };
    }

    @Override
    public String getVersion() {
        return getDescription().getVersion();
    }

    @Override
    public void info(String message) {
        getLogger().info(message);
    }

    @SuppressWarnings("CallToPrintStackTrace")
    @Override
    public void warn(String message, Throwable t) {
        getLogger().warning(message);
        if (t != null) {
            t.printStackTrace();
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    @Override
    public void error(String message, Throwable t) {
        getLogger().severe(message);
        if (t != null) {
            t.printStackTrace();
        }
    }

    @Override
    public boolean isSuperVanishBukkitAvailable() {
        return isPluginEnabled("SuperVanish");
    }

    @Override
    public boolean isPremiumVanishBukkitAvailable() {
        return isPluginEnabled("PremiumVanish");
    }

    @Override
    public boolean isPremiumVanishBungeeAvailable() {
        return false;
    }

    @Override
    public boolean isPremiumVanishVelocityAvailable() {
        return false;
    }

    @Override
    public boolean isLuckPermsAvailable() {
        return isPluginEnabled("LuckPerms");
    }

    @Override
    public Class<?> getPlayerClass() {
        return Player.class;
    }
}
