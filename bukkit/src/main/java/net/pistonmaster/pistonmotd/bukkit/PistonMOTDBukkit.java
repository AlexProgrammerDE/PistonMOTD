package net.pistonmaster.pistonmotd.bukkit;

import io.papermc.lib.PaperLib;
import lombok.Getter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.pistonmaster.pistonmotd.api.PlaceholderUtil;
import net.pistonmaster.pistonmotd.shared.PistonMOTDPlatform;
import net.pistonmaster.pistonmotd.shared.PistonMOTDPlugin;
import net.pistonmaster.pistonmotd.shared.PlayerWrapper;
import net.pistonmaster.pistonmotd.shared.StatusFavicon;
import org.bstats.bukkit.Metrics;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class PistonMOTDBukkit extends JavaPlugin implements PistonMOTDPlatform {
    @Getter
    private final PistonMOTDPlugin plugin = new PistonMOTDPlugin(this);

    @Override
    public void onEnable() {
        BukkitAudiences.create(this);

        plugin.logName();

        plugin.startupLoadConfig();

        plugin.registerCommonPlaceholder();
        if (PaperLib.isPaper()) {
            PlaceholderUtil.registerParser(new TPSPlaceholder());
        }

        plugin.loadHooks();

        startup("Registering listeners");
        if (PaperLib.isPaper()) {
            getServer().getPluginManager().registerEvents(new PingEventPaper(plugin), this);
        } else {
            PaperLib.suggestPaper(this);

            getServer().getPluginManager().registerEvents(new PingEventSpigot(plugin), this);
        }

        startup("Registering command");
        Objects.requireNonNull(getServer().getPluginCommand("pistonmotd")).setTabCompleter(new BukkitCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("pistonmotd")).setExecutor(new BukkitCommand(this));

        plugin.checkUpdate();

        startup("Loading metrics");
        new Metrics(this, 9100);

        startup("Done! :D");
    }

    @Override
    public void onDisable() {
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
    public InputStream getDefaultConfig() {
        return getResource("config.yml");
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

    @Override
    public void warn(String message) {
        getLogger().warning(message);
    }

    @Override
    public void error(String message) {
        getLogger().severe(message);
    }

    @Override
    public String getSuperVanishName() {
        return "SuperVanish";
    }

    @Override
    public String getPremiumVanishName() {
        return "PremiumVanish";
    }

    @Override
    public String getLuckPermsName() {
        return "LuckPerms";
    }
}
