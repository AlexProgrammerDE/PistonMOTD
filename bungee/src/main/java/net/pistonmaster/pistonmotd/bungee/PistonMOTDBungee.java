package net.pistonmaster.pistonmotd.bungee;

import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.pistonmaster.pistonmotd.api.PlaceholderUtil;
import net.pistonmaster.pistonmotd.shared.*;
import org.bstats.bungeecord.Metrics;

import javax.imageio.ImageIO;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PistonMOTDBungee extends Plugin implements PistonMOTDPlatform {
    private BungeeAudiences adventure;

    @Override
    public void onEnable() {
        this.adventure = BungeeAudiences.create(this);
        PistonMOTDPlugin plugin = new PistonMOTDPlugin(this);

        plugin.logName();

        plugin.startupLoadConfig();

        plugin.startupRegisterTasks();

        plugin.registerCommonPlaceholder();
        PlaceholderUtil.registerParser(new ServerPlaceholder(getProxy()));

        plugin.loadHooks();

        startup("Registering listeners");
        getProxy().getPluginManager().registerListener(this, new PingEvent(new StatusPingHandler(plugin)));

        startup("Registering command");
        getProxy().getPluginManager().registerCommand(this, new BungeeCommand(plugin));

        plugin.checkUpdate();

        startup("Loading metrics");
        new Metrics(this, 8968);

        startup("Done! :D");
    }

    @Override
    public void onDisable() {
        if (this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }

        startup("Unloading the listeners");
        getProxy().getPluginManager().unregisterListeners(this);

        startup("Unloading the commands");
        getProxy().getPluginManager().unregisterCommands(this);

        startup("Finished unloading!");
    }

    @Override
    public boolean isPluginEnabled(String pluginName) {
        return getProxy().getPluginManager().getPlugin(pluginName) != null;
    }

    @Override
    public StatusFavicon createFavicon(Path path) throws Exception {
        return new StatusFavicon(Favicon.create(ImageIO.read(Files.newInputStream(path))));
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
        return getResourceAsStream(name);
    }

    @Override
    public List<PlayerWrapper> getPlayers() {
        return getProxy().getPlayers().stream().map(this::wrap).collect(Collectors.toList());
    }

    @Override
    public int getMaxPlayers() {
        return getProxy().getConfig().getPlayerLimit();
    }

    @Override
    public int getPlayerCount() {
        return getProxy().getOnlineCount();
    }

    private PlayerWrapper wrap(ProxiedPlayer player) {
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
        return false;
    }

    @Override
    public boolean isPremiumVanishBukkitAvailable() {
        return false;
    }

    @Override
    public boolean isPremiumVanishBungeeAvailable() {
        return isPluginEnabled("PremiumVanish");
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
        return ProxiedPlayer.class;
    }

    @Override
    public void runAsync(Runnable runnable, long delay, long period, TimeUnit unit) {
        getProxy().getScheduler().schedule(this, runnable, delay, period, unit);
    }
}
